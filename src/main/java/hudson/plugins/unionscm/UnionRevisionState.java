package hudson.plugins.unionscm;

import hudson.scm.SCMRevisionState;

import java.util.List;
import java.util.Map;

public class UnionRevisionState extends SCMRevisionState {
    private final Map<String,SCMRevisionState> children;

    UnionRevisionState(Map<String,SCMRevisionState> children) {
        this.children = children;
    }

    SCMRevisionState get(String name) {
        return children.get(name);
    }
}
