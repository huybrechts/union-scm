package hudson.plugins.unionscm;

import hudson.model.AbstractBuild;
import hudson.scm.ChangeLogParser;
import hudson.scm.ChangeLogSet;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tom Huybrechts
 */
public class UnionChangeLogParser extends ChangeLogParser {

    private Map<String, ChangeLogParser> children = new HashMap<String, ChangeLogParser>();

    @Override
    public ChangeLogSet<? extends ChangeLogSet.Entry> parse(AbstractBuild build, File changelogFile) throws IOException, SAXException {
        List<ChangeLogSet.Entry> entries = new ArrayList<ChangeLogSet.Entry>();
        for (Map.Entry<String,ChangeLogParser> entry: children.entrySet()) {
            File clf = new File(build.getRootDir(), "changelog-" + entry.getKey() + ".xml");
        }
        return new UnionChangeLogSet();

    }
}
