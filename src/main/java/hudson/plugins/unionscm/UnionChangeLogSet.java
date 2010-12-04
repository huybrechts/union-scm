package hudson.plugins.unionscm;

import hudson.model.AbstractBuild;
import hudson.scm.ChangeLogSet;

import java.util.Iterator;
import java.util.List;

/**
 * @author Tom Huybrechts
 */
public class UnionChangeLogSet extends ChangeLogSet<ChangeLogSet.Entry> {

    private final List<Entry> entries;
    
    protected UnionChangeLogSet(AbstractBuild<?, ?> build, List<Entry> entries) {
        super(build);
        this.entries = entries;
    }

    @Override
    public boolean isEmptySet() {
        return entries.isEmpty();
    }

    public Iterator<Entry> iterator() {
        return entries.iterator();
    }
}
