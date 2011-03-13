package hudson.plugins.unionscm;

import hudson.Extension;
import hudson.FilePath;
import hudson.Functions;
import hudson.Launcher;
import hudson.model.*;
import hudson.plugins.git.*;
import hudson.plugins.git.opt.PreBuildMergeOptions;
import hudson.plugins.git.util.DefaultBuildChooser;
import hudson.scm.*;
import hudson.scm.browsers.Sventon;
import hudson.util.DescriptorList;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.spearce.jgit.transport.RemoteConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class UnionSCM extends SCM {

    private List<ChildSCM> scms;

    public static class ChildSCM {
        private final String name;
        private final SCM scm;

        private ChildSCM(String name, SCM scm) {
            this.name = name;
            this.scm = scm;
        }

        public String getName() {
            return name;
        }

        public SCM getScm() {
            return scm;
        }
    }

    public UnionSCM(List<ChildSCM> children) {
        this.scms = children;
    }

    public UnionSCM() {
        try {
            SubversionSCM scm1 = new SubversionSCM(
                    Arrays.asList(
                            new SubversionSCM.ModuleLocation("file:///D:/temp/svn-repo1", "repo1")),
                    true,false,new Sventon(new URL("http://www.sun.com/"),"test"),"exclude","user","revprop","excludeMessage", null);

            GitSCM scm2 = new GitSCM(
                    Arrays.asList(
                            new GitSCMModule(
                                    Arrays.asList(new Repository("file:///temp/git-repo",null,null)),
                                    Arrays.asList(new BranchSpec("master")), null, null, ".")
                    ), false, false, new DefaultBuildChooser(), null, "git", true, ".", null, false, null, null, false);

            scms = Arrays.<ChildSCM>asList(new ChildSCM("repo1",scm1), new ChildSCM("repo2", scm2));
        } catch (IOException e) {
            throw new RuntimeException("",e);
        }
    }

    public List<ChildSCM> getScms() {
        return scms;
    }

    public ChildSCM getSCM(Descriptor d) {
        for (ChildSCM scm: scms) {
            if (scm.scm.getDescriptor() == d) return scm;
        }
        return null;
    }

    private static SCMRevisionState workAroundFor1_346Incompatibility = new SCMRevisionState() {};
    
    @Override
    public SCMRevisionState calcRevisionsFromBuild(AbstractBuild<?, ?> build, Launcher launcher, TaskListener taskListener) throws IOException, InterruptedException {
        Map<String,SCMRevisionState> children = new HashMap<String,SCMRevisionState>();
        for(ChildSCM scm: scms) {
            try {
                SCMRevisionState state = _calcRevisionsFromBuild(scm.scm, build, launcher, taskListener);
                children.put(scm.name, state);
            } catch (AbstractMethodError e) {
                children.put(scm.name, workAroundFor1_346Incompatibility);
            }
        }
        return new UnionRevisionState(children);
    }

    /**
     * A pointless function to work around what appears to be a HotSpot problem. See HUDSON-5756 and bug 6933067
     * on BugParade for more details.
     */
    private SCMRevisionState _calcRevisionsFromBuild(SCM scm, AbstractBuild<?, ?> build, Launcher launcher, TaskListener listener) throws IOException, InterruptedException {
        return scm.calcRevisionsFromBuild(build, launcher, listener);
    }

    @Override
    protected PollingResult compareRemoteRevisionWith(AbstractProject<?, ?> project, Launcher launcher, FilePath workspace, TaskListener taskListener, SCMRevisionState baseline) throws IOException, InterruptedException {
        UnionRevisionState state = (UnionRevisionState) baseline;
        Map<String,SCMRevisionState> remote = new HashMap<String,SCMRevisionState>();
        PollingResult.Change change = PollingResult.Change.NONE;
        for (ChildSCM scm: scms) {
            SCMRevisionState childBaseline = state.get(scm.name);
            FilePath childWorkspace = workspace.child(scm.name);
            PollingResult r = scm.scm.poll(project, launcher, childWorkspace, taskListener, childBaseline);
            if (r.change.compareTo(change) > 0) {
                change = r.change;
            }
            remote.put(scm.name, r.remote);
        }

        return new PollingResult(baseline, new UnionRevisionState(remote), change);
    }

    @Override
    public boolean checkout(AbstractBuild<?, ?> build, Launcher launcher, FilePath workspace, BuildListener listener, File changelogFile) throws IOException, InterruptedException {
        for (ChildSCM scm: scms) {
            File clf = new File(build.getRootDir(), "changelog-" + scm.name + ".xml");
            boolean result = scm.scm.checkout(build, launcher, workspace.child(scm.name), listener, clf);
            if (!result) return false;
        }
        new FileOutputStream(changelogFile).close();
        return true;
    }

    @Override
    public ChangeLogParser createChangeLogParser() {
        return new UnionChangeLogParser();
    }

    @Override
    public RepositoryBrowser<?> getBrowser() {
        return new UnionSCMRepositoryBrowser(this);
    }

    @Extension
    public static class DescriptorImpl extends SCMDescriptor<UnionSCM> {
        public DescriptorImpl() {
            super(null);
        }

        @Override
        public String getDisplayName() {
            return "Union SCM";
        }

        public List<SCMDescriptor<?>> getSCMDescriptors(AbstractProject<?,?> project) {
            List<SCMDescriptor<?>> result = Functions.getSCMDescriptors(project);
            result = new ArrayList<SCMDescriptor<?>>(result);
            result.remove(this);
            result.remove(SCM.all().find(NullSCM.class));
            return result;
        }

        @Override
        public SCM newInstance(StaplerRequest req, JSONObject formData) throws FormException {
            System.out.println(formData);
            StaplerRequest wrappedReq = new StaplerRequestWrapper(req);
            List<ChildSCM> children = new ArrayList<ChildSCM>();
            for (SCMDescriptor<?> d: getSCMDescriptors(null)) {
                String klass = d.getJsonSafeClassName();
                if (formData.has(klass)) {
                    JSONObject childFormData = formData.getJSONObject(klass);
                    String name = childFormData.getString("name");
                    childFormData.remove("name");
                    SCM scm = d.newInstance(wrappedReq, childFormData);
                    children.add(new ChildSCM(name, scm));
                }
            }
            return new UnionSCM(children);
        }
    }
}
