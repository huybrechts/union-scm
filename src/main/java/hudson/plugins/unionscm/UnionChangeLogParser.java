package hudson.plugins.unionscm;

import hudson.model.AbstractBuild;
import hudson.scm.ChangeLogParser;
import hudson.scm.ChangeLogSet;
import hudson.scm.SCM;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Tom Huybrechts
 */
public class UnionChangeLogParser extends ChangeLogParser {

    public UnionChangeLogParser() {
    }

    @Override
    public ChangeLogSet<? extends ChangeLogSet.Entry> parse(AbstractBuild build, File changelogFile) throws IOException, SAXException {
        List<UnionSCM.ChildSCM> scms = ((UnionSCM) build.getProject().getScm()).getScms();
        Map<UnionSCM.ChildSCM,ChangeLogSet> entries = new HashMap<UnionSCM.ChildSCM, ChangeLogSet>();
        for (UnionSCM.ChildSCM scm: scms) {
            ChangeLogParser parser = scm.getScm().createChangeLogParser();
            File clf = new File(build.getRootDir(), "changelog-" + scm.getName() + ".xml");
            entries.put(scm, parser.parse(build, clf));
        }

        return new UnionChangeLogSet(build, entries);
    }

}
