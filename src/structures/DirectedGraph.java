package structures;

import graphs.Edge;
import graphs.IGraph;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * A class that creates a Directed Graph that uses a bijection
 * to store vertices and edges.
 *
 * @author Keny Dutton-Gillespie
 * @version 1.0
 */
public class DirectedGraph<V> implements IGraph<V>
{
    private final Bijection<V, Integer> bijection;
    private final Stack<Integer> stack = new Stack<>();
    private int[][] adjacencyMatrix;
    private int edgeCount;

    /**
     * Constructs a directed graph object
     */
    public DirectedGraph()
    {
        bijection = new Bijection<>();
        stack.push(0);
        int defaultSize = 10;
        adjacencyMatrix = new int[defaultSize][defaultSize];
    }

    /**
     * @param initialSize the given initial size of the adjacency matrix
     */
    public DirectedGraph(int initialSize)
    {
        bijection = new Bijection<>();
        stack.push(initialSize);
        adjacencyMatrix = new int[initialSize][initialSize];
    }


    /**
     * boolean addVertex(V vertex)	Adds a new vertex to the graph.
     * If the vertex already exists, then no change is made to the graph.
     *
     * @param vertex the new vertex
     * @return true if the vertex was added, otherwise false.
     */
    @Override
    public boolean addVertex(V vertex)
    {
        if(containsVertex(vertex))
        {
            return false;
        }

        if(!containsVertex(vertex))
        {
            // check if adjacencyMatrix is full
            if (adjacencyMatrix.length <= vertexSize())
            {
                // increase the size of the matrix by one, so it's never full
                int[][] tempAdjacencyMatrix = new int[vertexSize() + 1][vertexSize() + 1];

                // place adjacency matrix into the temp matrix
                for (int i = 0; i < adjacencyMatrix.length; i++)
                {
                    adjacencyMatrix[i][i] = tempAdjacencyMatrix[i][i];
                }
                // reassign the temp matrix back into the adjacency matrix
                adjacencyMatrix = tempAdjacencyMatrix;
            }

            // get the value off the top of the stack
            Integer value = stack.pop();
            // add a new vertex into the bijection
            bijection.add(vertex, value);
            // push the next value onto the top of the stack
            stack.push(value + 1);
        }
        return true;
    }

    /**
     * Adds a new edge to the graph. If the edge already exists, then no change is made to the graph.
     * Edges are considered to be directed.
     *
     * @param source      the source vertex of the edge
     * @param destination the destination vertex of the edge
     * @param weight      the edge weight, throws an IllegalArgumentException
     *                    if the weight is negative
     * @return  true if the edge was added, otherwise false
     */
    @Override
    public boolean addEdge(V source, V destination, int weight)
    {
        // check if weight is invalid
        if (weight < 0 )
        {
            throw new IllegalArgumentException("Negative edge weights are not supported");
        }
        // ensure that both the source and destination are in the bijection
        if(containsVertex(source) && containsVertex(destination))
        {
            // if the weight already exists
            // return false
            if (containsEdge(source, destination))
            {
                return false;
            }
            // otherwise, store the weight value in the matrix
            else
            {
                int sourceIndex = bijection.getValue(source);
                int destinationIndex = bijection.getValue(destination);
                adjacencyMatrix[sourceIndex][destinationIndex] = weight;

                // increase the edgeCount
                edgeCount++;
            }
        }
        else
        {
            return false;
        }
        return true;
    }


    /**
     * Returns the number of vertices in the graph.
     *
     * @return the vertex count.
     */
    @Override
    public int vertexSize()
    {
        return bijection.keySet().size();
    }

    /**
     * Returns the number of edges in the graph.
     *
     * @return the edge count
     */
    @Override
    public int edgeSize()
    {
        return edgeCount;
    }

    /**
     * Reports whether a vertex is in the graph or not.
     *
     * @param vertex a vertex to search for
     * @return true if the vertex is in the graph, or false otherwise
     */
    @Override
    public boolean containsVertex(V vertex)
    {
        return bijection.containsKey(vertex);

    }

    /**
     * Reports whether an edge is in the graph or not.
     *
     * @param source      the source vertex of the edge
     * @param destination the destination vertex of the edge
     * @return true if edge is in the graph, or false otherwise
     */
    @Override
    public boolean containsEdge(V source, V destination)
    {
        if(!bijection.containsKey(source) || !bijection.containsKey(destination))
        {
            return false;
        }
        else {
            // assign the indices of each vertex to a variable
            int sourceIndex = bijection.getValue(source);
            int destinationIndex = bijection.getValue(destination);

            // check if the source and destination are in the bijection
            if (!containsVertex(source) && !containsVertex(destination))
            {
                return false;
            }
            else
            {
                // if they are in the bijection,
                // return if there is a non-zero value in the matrix for
                // the intersection of those values
                return adjacencyMatrix[sourceIndex][destinationIndex] != 0;
            }
        }
    }

    /**
     * Returns the edge weight of an edge in the graph.
     *
     * @param source      the source vertex of the edge
     * @param destination the destination vertex of the edge
     * @return the edge weight, or -1 if the edge weight is not found
     */
    @Override
    public int edgeWeight(V source, V destination)
    {
        // assign the indices of each vertex to a variable
        int sourceIndex = bijection.getValue(source);
        int destinationIndex = bijection.getValue(destination);

        // return the value stored in the matrix @ the given
        // source and destination
        return adjacencyMatrix[sourceIndex][destinationIndex];
    }

    /**
     * Returns a set with all vertices in the graph.
     * The set returned shares not references with any internal structures used by the DirectedGraph class.
     * @return  a vertex set
     */
    @Override
    public Set<V> vertices()
    {
        Set<V> holderSet = bijection.keySet();
        Set<V> returnSet = new HashSet<>();

        // assign each item in the bijection set to a new set
        returnSet.addAll(holderSet);

        // return that set
        return returnSet;
    }

    /**
     * Returns a set with all edges in the graph.
     * The set returned shares not references with any internal
     * structures used by the DirectedGraph class.
     *
     * @return an edge set
     */
    @Override
    public Set<Edge<V>> edges()
    {
        Set<V> holderKeySet = bijection.keySet();
        Set<Edge<V>> returnSet = new HashSet<>();

        // search through all the source vertices
        for (V source :holderKeySet)
        {
            // look at their relationships with all vertexes
           for (V destination : holderKeySet)
            {
                // assign the indices of each vertex to a variable
               int sourceIndex = bijection.getValue(source);
               int destinationIndex = bijection.getValue(destination);
               int weight = adjacencyMatrix[sourceIndex][destinationIndex];

               // if there is an edge for the given index, add it to the set
               if(adjacencyMatrix[sourceIndex][destinationIndex] != 0)
               {
                   Edge<V> edge = new Edge<>(source, destination, weight);
                   returnSet.add(edge);
               }
          }
        }

        return returnSet;
    }

    /**
     * Removes a vertex from the graph.
     *
     * @param vertex the vertex to search for and remove
     * @return true if the vertex was found and removed, otherwise false
     */
    @Override
    public boolean removeVertex(V vertex)
    {
        if(!bijection.containsKey(vertex))
        {
            return false;
        }
        else
        {
            // save the value before the key is removed
           Integer saveValue = bijection.getValue(vertex);

            // remove the key
           bijection.removeKey(vertex);

            // push the saved value back onto the stack
           stack.push(saveValue);
        }

        return true;
    }


    /**
     * Removes an edge from the graph.
     *
     * @param source      the source vertex of the edge to search for and remove
     * @param destination the destination vertex of the edge to search for and remove
     * @return true if the edge was found and removed, otherwise false
     */
    @Override
    public boolean removeEdge(V source, V destination)
    {
        // if both the source and destination are found
        if(containsVertex(source) && containsVertex(destination)) {

            // create variables of the indices for each
            int sourceIndex = bijection.getValue(source);
            int destinationIndex = bijection.getValue(destination);

            // if the weight is zero, there is no edge
            if (adjacencyMatrix[sourceIndex][destinationIndex] == 0) {
                return false;
            }
            // if the weight is a non-zero, save it to zero to remove the edge value
            else
            {
                adjacencyMatrix[sourceIndex][destinationIndex] = 0;
            }
            // decrease the edge count
            edgeCount--;
        }
        else
        {
            return false;
        }

        return true;
    }

    /**
     * Removes all vertices and edges from the graph.
     */
    @Override
    public void clear()
    {
        bijection.clear();
        edgeCount = 0;
    }

    @Override
    public String toString()
    {
        return "DirectedGraph{" +
                "bijection=" + bijection +
                ", stack=" + stack +
                ", adjacencyMatrix=" + Arrays.toString(adjacencyMatrix) +
                ", edgeCount=" + edgeCount +
                '}';
    }
}
