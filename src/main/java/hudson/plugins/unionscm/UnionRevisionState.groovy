package hudson.plugins.unionscm

import hudson.scm.SCMRevisionState

/**
 * @author Tom Huybrechts
 */
class UnionRevisionState extends SCMRevisionState {

  private final List<UnionRevisionState> children;

  def UnionRevisionState(children) {
    this.children = children;
  }

}
