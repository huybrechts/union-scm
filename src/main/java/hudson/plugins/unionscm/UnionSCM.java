package hudson.plugins.unionscm;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.TaskListener;
import hudson.scm.*;
import hudson.scm.browsers.Sventon;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnionSCM extends SCM {

    private List<ChildSCM> scms;

    private static class ChildSCM {
        private final String name;
        private final SCM scm;

        private ChildSCM(String name, SCM scm) {
            this.name = name;
            this.scm = scm;
        }
    }

    public UnionSCM() throws IOException {
        SubversionSCM scm = new SubversionSCM(
                Arrays.asList(
                		new SubversionSCM.ModuleLocation("https://svn.dev.java.net/svn/hudson/trunk/hudson/test-projects/testSubversionExclusion", "c"),
                		new SubversionSCM.ModuleLocation("https://svn.dev.java.net/svn/hudson/trunk/hudson/test-projects/testSubversionExclusion", "d")),
                true,false,new Sventon(new URL("http://www.sun.com/"),"test"),"exclude","user","revprop","excludeMessage", null);
        scms = Arrays.<ChildSCM>asList(new ChildSCM("subversion",scm));
    }

    @Override
    public SCMRevisionState calcRevisionsFromBuild(AbstractBuild<?, ?> build, Launcher launcher, TaskListener taskListener) throws IOException, InterruptedException {
        List<SCMRevisionState> children = new ArrayList<SCMRevisionState>();
        for(ChildSCM scm: scms) {
            children.add(scm.scm.calcRevisionsFromBuild(build, launcher, taskListener));
        }
        return new UnionRevisionState(children);
    }

    @Override
    protected PollingResult compareRemoteRevisionWith(AbstractProject<?, ?> project, Launcher launcher, FilePath workspace, TaskListener taskListener, SCMRevisionState baseline) throws IOException, InterruptedException {
        for (ChildSCM scm: scms) {
            try {
                Method method = scm.getClass().getMethod("compareRemoteRevisionWith", AbstractProject.class, Launcher.class, FilePath.class, TaskListener.class, SCMRevisionState.class);
                method.setAccessible(true);
                PollingResult r = (PollingResult) method.invoke(scm.scm, project, launcher, workspace, taskListener, baseline);

                return r; //TODO combine
            } catch (IllegalAccessException e) {
                throw new Error(e);
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof IOException) throw (IOException) e.getCause();
                if (e.getCause() instanceof InterruptedException) throw (InterruptedException) e.getCause();
                throw new Error(e);
            } catch (NoSuchMethodException e) {
                throw new Error(e);
            }
        }

        return null;
    }

    @Override
    public boolean checkout(AbstractBuild<?, ?> build, Launcher launcher, FilePath workspace, BuildListener listener, File changelogFile) throws IOException, InterruptedException {
        for (ChildSCM scm: scms) {
            boolean result = scm.scm.checkout(build, launcher, workspace.child(scm.name), listener, changelogFile);
            if (!result) return false;
        }
        return true;
    }

    @Override
    public ChangeLogParser createChangeLogParser() {
        return new UnionChangeLogParser();
    }
}
