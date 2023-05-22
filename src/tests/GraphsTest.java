package tests;

import structures.DirectedGraph;
import graphs.Edge;
import graphs.IGraph;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Runs a series of tests to verify a directed graph
 * structure using the IGraph<V> interface.
 * DO NOT ALTER THIS FILE!
 *
 * @author Josh Archer
 * @version 1.0
 */
public class GraphsTest
{
    private static final int DEFAULT_WEIGHT = 1;
    private static final int TEST_WEIGHT = 15;
    private static final String[] testVerts = {"A", "B", "C", "D", "E", "F", "G", "H",
                                  "I", "J", "K", "L"};
    private IGraph<String> graph;

    /**
     * Creates a new graph for each test.
     */
    @Before
    public void setup()
    {
        graph = new DirectedGraph<>();
    }

    private void addFewVertices()
    {
        for (String letter : testVerts)
        {
            graph.addVertex(letter);
        }
    }

    private void addFewEdges(int weight)
    {
        //add link between letters that are adjacent (A-B, B-C, ... , J-K, K-L)
        for (int i = 0; i < testVerts.length - 1; i++)
        {
            String source = testVerts[i];
            String destination = testVerts[i + 1];
            graph.addEdge(source, destination, weight);
        }
    }

    /**
     * Verifies that vertices are added to the graph.
     */
    @Test
    public void addVertexTest()
    {
        addFewVertices();

        //verify size
        assertEquals("Size should be " + testVerts.length + " after adding elements",
                graph.vertexSize(), testVerts.length);

        //verify contains
        for (String letter : testVerts)
        {
            assertTrue(letter + " was not found in graph after being added to graph",
                    graph.containsVertex(letter));
        }
    }

    /**
     * Checks the return type of Graph.addVertex(), making sure
     * that duplicate vertices are not added to the graph.
     */
    @Test
    public void testDuplicateVertex()
    {
        addFewVertices();

        assertTrue("Adding a new vertex returns false", graph.addVertex("M"));
        assertFalse("Adding a duplicate vertex returns true", graph.addVertex("M"));
    }

    /**
     * Verifies that edges can be added to the graph.
     */
    @Test
    public void addEdgeTest()
    {
        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);

        int numEdges = testVerts.length - 1;

        //verify size
        assertEquals("Vertex size should be " + testVerts.length + " after adding vertices",
                graph.vertexSize(), testVerts.length);
        assertEquals("Edge size should be " + numEdges + " after adding edges",
                graph.edgeSize(), numEdges);

        //make sure all expected edges are present
        for (int i = 0; i < testVerts.length - 1; i++)
        {
            String source = testVerts[i];
            String destination = testVerts[i + 1];

            assertTrue("Edge does not exist when it should " + source + " - " + destination,
                    graph.containsEdge(source, destination));
        }
    }

    /**
     * Checks whether adding an edge with a vertex not in the map is recognized
     * by the return type of the addEdge() method. (i.e. the method should return
     * true when the edge is added and false otherwise)
     */
    @Test
    public void addEdgeWithoutVertexTest()
    {
        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);

        //add an edge with both vertices
        assertTrue("Graph does not recognize adding a valid edge",
                graph.addEdge(testVerts[0], testVerts[testVerts.length - 1], DEFAULT_WEIGHT));

        //add edges with missing vertices
        assertFalse("Graph does recognizes adding an invalid edge",
                graph.addEdge(testVerts[0], "M", DEFAULT_WEIGHT));
        assertFalse("Graph does recognizes adding an invalid edge",
                graph.addEdge("M", testVerts[0], DEFAULT_WEIGHT));
        assertFalse("Graph does recognizes adding an invalid edge",
                graph.addEdge("P", "M", DEFAULT_WEIGHT));
    }

    /**
     * Checks whether the graph reports the unsuccessful addition
     * of duplicate edges.
     */
    @Test
    public void addDuplicateEdgeTest()
    {
        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);

        int sizeBefore = graph.vertexSize();

        //add the duplicate and verify that the graph hasn't changed
        assertFalse("addEdge() returns true when adding a duplicate edge",
                graph.addEdge(testVerts[0], testVerts[1], DEFAULT_WEIGHT));

        int sizeAfter = graph.vertexSize();
        assertEquals("Size should be unchanged after adding a duplicate",
                sizeBefore, sizeAfter);
    }

    /**
     * Checks whether the graph recognizes missing vertices.
     */
    @Test
    public void missingVertexTest()
    {
        addFewVertices();

        //verify no false positives
        assertFalse("Missing vertex was found in graph", graph.containsVertex("M"));
    }

    /**
     * Checks whether the graph recognizes missing edges.
     */
    @Test
    public void missingEdgeTest()
    {
        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);

        //verify no false positives (both of these edges should not be in graph)
        assertFalse("Missing edge (A, A) was found in graph",
                graph.containsEdge("A", "A"));
        assertFalse("Missing edge (D, F) was found in graph",
                graph.containsEdge("A", "A"));

        //force a resize and then verify no false positives
        for (int i = 1; i <= 100; i++)
        {
            graph.addVertex(i + ""); //add "1", "2", ... , "100"
        }

        //verify that we can't find an edge with both missing vertices
        assertFalse("Missing edge (101, 101) was found in graph",
                graph.containsEdge("101", "101"));

        //verify that we can't find an edge with one missing vertex
        assertFalse("Missing edge (A, 50) was found in graph",
                graph.containsEdge("A", "50"));
        assertFalse("Missing edge (50, A) was found in graph",
                graph.containsEdge("50", "A"));
    }

    /**
     * Verifies that edges are added to the graph in a directed way.
     */
    @Test
    public void directednessTest()
    {
        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);

        //verify that pairs only work in one direction
        for (int i = 0; i < testVerts.length - 1; i++)
        {
            String source = testVerts[i];
            String destination = testVerts[i + 1];

            assertTrue("Edge does not exist when it should " + source + " - " + destination,
                    graph.containsEdge(source, destination));
            assertFalse("Edge does exist when it should not " + source + " - " + destination,
                    graph.containsEdge(destination, source));
        }
    }

    /**
     * Verifies that Graph.vertices() returns all vertices added to the graph.
     */
    @Test
    public void vertexSetTest()
    {
        //empty vertex set
        assertEquals("Vertex array should be empty before adding any vertices",
                0, graph.vertices().size());

        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);

        String[] vertices = graph.vertices().toArray(new String[0]);
        Arrays.sort(vertices);

        assertArrayEquals("All vertices are not available through this method",
                testVerts, vertices);
    }

    /**
     * Verifies that Graph.edges() returns all edges added to the graph.
     */
    @Test
    public void edgeSetTest()
    {
        //empty edge set
        assertEquals("Edge array should be empty before adding any edges",
                0, graph.edges().size());

        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);

        //make sure the elements in the edge set match
        Set<Edge<String>> edges = graph.edges();
        Set<Edge<String>> others = new HashSet<>();
        for (int i = 0; i < testVerts.length - 1; i++)
        {
            String source = testVerts[i];
            String destination = testVerts[i + 1];

            others.add(new Edge<>(source, destination, DEFAULT_WEIGHT));
        }

        //does size match?
        assertEquals("Number of edges returned does is incorrect", others.size(), edges.size());

        //does each element match?
        for (Edge<String> edge : others)
        {
            assertTrue("Edge set contains an edge that was NOT previously added to the graph",
                    edges.contains(edge));
        }
    }

    /**
     * Tests whether edges weights passed to Graph.addEdge() are stored with
     * the correct weights.
     */
    @Test
    public void weightednessTest()
    {
        addFewVertices();
        addFewEdges(TEST_WEIGHT);

        //verify weighted edges
        for (int i = 0; i < testVerts.length - 1; i++)
        {
            String source = testVerts[i];
            String destination = testVerts[i + 1];

            assertEquals("Edge weight is incorrect for edge (" +
                            source + ", " + destination + ")",
                            TEST_WEIGHT, graph.edgeWeight(source, destination));
        }

        //make sure we can't add an invalid weight
        try
        {
            graph.addEdge("A", "B", -DEFAULT_WEIGHT);
        }
        catch (IllegalArgumentException ex)
        {
            assert true; //do nothing
        }
    }

    /**
     * Verifies that vertices can reliably be removed from the graph.
     */
    @Test
    public void removeVertexTest()
    {
        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);

        //make sure you cannot remove missing vertex elements
        assertFalse("Vertex reported as removed, when it is not in the graph",
                graph.removeVertex("M"));

        //make sure you can remove vertices in graph
        assertTrue("Vertex reported as not removed, when it is in the graph",
                graph.removeVertex("L"));

        //remove all vertices in the graph
        Set<String> vertices = graph.vertices();
        assertEquals("Number of vertices in vertex set after removing an element is incorrect",
                testVerts.length - 1, vertices.size());
        assertEquals("Number of vertices in graph after removing an element is incorrect",
                testVerts.length - 1, graph.vertexSize());

        for (String vertex : vertices)
        {
            assertTrue("Element reported as not removed, when it is in the graph",
                    graph.removeVertex(vertex));
        }

        assertEquals("Number of vertices in graph after removing all elements is incorrect",
                0, graph.vertexSize());
    }

    /**
     * Verifies that edges can reliably be removed from the graph.
     */
    @Test
    public void removeEdgeTest()
    {
        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);

        //make sure you cannot remove missing edge elements (one missing vertex, or both)
        assertFalse("Edge reported as removed, when it is not in the graph",
                graph.removeEdge("A", "M"));
        assertFalse("Edge reported as removed, when it is not in the graph",
                graph.removeEdge("M", "A"));
        assertFalse("Edge reported as removed, when it is not in the graph",
                graph.removeEdge("N", "M"));

        //make sure you can remove edges in graph
        Set<Edge<String>> edges = graph.edges();
        Edge<String> first = edges.iterator().next();

        assertTrue("Edge reported as not removed, when it is in the graph",
                graph.removeEdge(first.getSource(), first.getDestination()));
        assertEquals("Number of edges in graph after removing an element is incorrect",
                testVerts.length - 2, graph.edgeSize());

        //add the edge back
        graph.addEdge(first.getSource(), first.getDestination(), DEFAULT_WEIGHT);

        //remove all edges in the graph
        for (Edge<String> edge : edges)
        {
            assertTrue("Edge reported as not removed, when it is in the graph",
                    graph.removeEdge(edge.getSource(), edge.getDestination()));
        }

        assertEquals("Number of edges in graph after removing all elements is incorrect",
                0, graph.edgeSize());
    }

    /**
     * Tests whether the Graph.clear() method removes all vertices
     * and edges from the graph.
     */
    @Test
    public void clearTest()
    {
        addFewVertices();
        addFewEdges(DEFAULT_WEIGHT);

        graph.clear();

        //verify size
        assertEquals("Number of vertices reported in the graph should be zero",
                0, graph.vertexSize());
        assertEquals("Number of edges reported in the graph should be zero",
                0, graph.edgeSize());

        //verify sets are empty
        assertEquals("Number of vertices returned by the graph should be zero",
                0, graph.vertices().size());
        assertEquals("Number of edges returned by the graph should be zero",
                0, graph.edges().size());
    }
}
