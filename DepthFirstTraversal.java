package graph;

import java.util.LinkedList;

import static java.util.Collections.asLifoQueue;

/* See restrictions in Graph.java. */

/**
 * Implements a depth-first traversal of a graph.  Generally, the
 * client will extend this class, overriding the visit and
 * postVisit methods, as desired (by default, they do nothing).
 *
 * @author Shrey Malhotra
 */
public class DepthFirstTraversal extends Traversal {

    /**
     * A depth-first Traversal of G.
     */
    protected DepthFirstTraversal(Graph G) {
        super(G, asLifoQueue(new LinkedList<Integer>()));
    }

    @Override
    protected boolean shouldPostVisit(int v) {
        return true;
    }

    @Override
    protected boolean visit(int v) {
        return super.visit(v);
    }

    @Override
    protected boolean postVisit(int v) {
        return super.postVisit(v);
    }

    @Override
    protected boolean reverseSuccessors(int v) {
        return true;
    }
}
