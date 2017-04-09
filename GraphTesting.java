package graph;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the Graph class.
 *
 * @author Shrey Malhotra
 */
public class GraphTesting {

    @Test
    public void emptyGraphTest() {
        DirectedGraph g = new DirectedGraph();
        assertEquals("Initial graph has vertices", 0, g.vertexSize());
        assertEquals("Initial graph has edges", 0, g.edgeSize());
    }

    @Test
    public void maxvertexpossibleTest() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.remove(3);
        assertEquals(2, g.maxVertex());
    }

    @Test
    public void directeddegreeTest() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(1, 3);
        g.add(1, 4);
        g.add(3, 1);
        g.add(2, 3);
        g.add(2, 1);
        assertEquals(2, g.inDegree(3));
        assertEquals(0, g.inDegree(5));
        assertEquals(2, g.outDegree(2));
        assertEquals(1, g.outDegree(3));
        assertEquals(0, g.outDegree(5));
    }

    @Test
    public void undircteddegreeTest() {
        UndirectedGraph g = new UndirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(1, 3);
        g.add(1, 4);
        g.add(2, 3);
        g.add(2, 1);
        assertEquals(2, g.inDegree(3));
        assertEquals(0, g.inDegree(5));
        assertEquals(2, g.outDegree(2));
        assertEquals(0, g.outDegree(5));
    }

    @Test
    public void addtestUndirectedgraph() {
        UndirectedGraph g = new UndirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(1, 3);
        g.add(1, 4);
        g.add(1, 5);
        g.add(2, 6);
        g.add(5, 9);
        g.add(1, 8);
        g.add(5, 7);
    }

}
