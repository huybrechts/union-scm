package hudson.plugins.unionscm;

import hudson.model.AbstractBuild;
import hudson.scm.ChangeLogSet;
import hudson.scm.SCM;

import java.util.*;

/**
 * @author Tom Huybrechts
 */
public class UnionChangeLogSet extends ChangeLogSet<ChangeLogSet.Entry> {

    private AbstractBuild build;
    private Map<UnionSCM.ChildSCM, ChangeLogSet> entries;

    public UnionChangeLogSet(AbstractBuild build, Map<UnionSCM.ChildSCM, ChangeLogSet> entries) {
        super(build);
        this.entries = entries;
    }

    @Override
    public boolean isEmptySet() {
        return !iterator().hasNext();
    }

    public Iterator<Entry> iterator() {
        List<Entry> list =new ArrayList<Entry>();
        for (Map.Entry<UnionSCM.ChildSCM, ChangeLogSet> entry: entries.entrySet()) {
            Iterator it = entry.getValue().iterator();
            while (it.hasNext()) {
                list.add((Entry) it.next());
            }
        }
        return list.iterator();
    }

    public Set<UnionSCM.ChildSCM> getSCMs() {
        return entries.keySet();
    }

    public ChangeLogSet getChangeLogSet(UnionSCM.ChildSCM scm) {
        return entries.get(scm);
    }
}
