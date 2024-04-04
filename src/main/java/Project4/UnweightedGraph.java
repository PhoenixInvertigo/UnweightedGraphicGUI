package Project4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Author: Ryo Kilgannon
 * Date: 2/28/24
 *
 * The class which holds the structure of vertices
 * and edges, as well as calculating information about
 * the overall graph, such as its connection and cycle
 * status and the depth- and breadth-first searches.
 *
 */
public class UnweightedGraph {

    //Used for keeping track of the next vertex name
    String vertexNames = "A";

    //Maps the vertex names to their vertex
    HashMap<String, Vertex> vertexMap = new HashMap<>();

    //Maps the vertices to their neighboring vertices
    HashMap<Vertex, HashSet<Vertex>> neighborMap = new HashMap<>();

    //The list of vertices found in the current search
    ArrayList<Vertex> currentSearch = new ArrayList<>();

    //The nodes already visited in the current search
    HashSet<Vertex> alreadyVisited = new HashSet<>();

    //Tracks whether or not there are cycles found during DFS
    boolean hasCycles;

    /**
     * Method for incrementing the vertex names.
     * It should work well for double digits, but it
     * will break after reaching AZZ, I think. Still,
     * that's 2*26^26 vertices before breaking, which
     * should exceed the needs of this program.
     */
    private void incrementVertexName(){
        char[] oldChars = vertexNames.toCharArray();
        if(oldChars[oldChars.length - 1] == 'Z' && oldChars.length == 1){
            char[] newChars = new char[oldChars.length + 1];
            for(int i = 0; i < oldChars.length; i++){
                newChars[i] = oldChars[i];
            }

                newChars[oldChars.length - 1] = 'A';
                newChars[oldChars.length] = 'A';

            vertexNames = new String(newChars);
        } else if(oldChars[oldChars.length - 1] == 'Z'){
            boolean allZs = true;
            for(int i = 0; i < oldChars.length; i++){
                if(oldChars[i] != 'Z'){
                    allZs = false;
                }
            }

            if(allZs){
                char[] newChars = new char[oldChars.length + 1];
                for(int i = 0; i < newChars.length; i++){
                    newChars[i] = 'A';
                }

                vertexNames = new String(newChars);
            } else {
                oldChars[oldChars.length - 1] = 'A';
                oldChars[oldChars.length - 2]++;

                vertexNames = new String(oldChars);
            }
        } else {
            oldChars[oldChars.length - 1]++;
            vertexNames = new String(oldChars);
        }
    }

    /**
     * Creates a new vertex, adds it to the vertexMap by
     * its name, then adds the vertex to the neighborMap
     * with an empty set of neighbors.
     *
     * @param x X-coordinate of the new Vertex
     * @param y Y-coordinate of the new Vertex
     * @return The name of the new Vertex
     */
    public String addVertex(double x, double y){
        Vertex vertex = new Vertex(x, y, vertexNames);
        incrementVertexName();

        vertexMap.put(vertex.getName(), vertex);
        neighborMap.put(vertex, new HashSet<>());

        return vertex.getName();
    }

    /**
     * Adds a new edge between two vertices, throwing an
     * error if either vertex doesn't exist or if the same
     * vertex was passed in for both values.
     *
     * Does this by adding each vertex to the other's neighbor
     * list.
     *
     * @param v1Name The first Vertex in the Edge
     * @param v2Name The second Vertex in the Edge
     * @return A double[4] with x and y coordinates for each
     * vertex to be used in drawing the Edge.
     */
    public double[] addEdge(String v1Name, String v2Name){

        if(!vertexMap.containsKey(v1Name)){
            throw new IllegalArgumentException("Vertex 1 does not exist");
        }

        if(!vertexMap.containsKey(v2Name)){
            throw new IllegalArgumentException("Vertex 2 does not exist");
        }

        if(v1Name.equals(v2Name)){
            throw new IllegalArgumentException("Vertex cannot have edge to itself");
        }

        neighborMap.get(vertexMap.get(v1Name)).add(vertexMap.get(v2Name));
        neighborMap.get(vertexMap.get(v2Name)).add(vertexMap.get(v1Name));

        double[] result = new double[4];
        result[0] = vertexMap.get(v1Name).getX();
        result[1] = vertexMap.get(v1Name).getY();
        result[2] = vertexMap.get(v2Name).getX();
        result[3] = vertexMap.get(v2Name).getY();

        return result;

    }

    /**
     * Runs the DFS from every Vertex in order to determine
     * whether or not there are cycles in any part of the
     * graph, even if disconnected from Vertex A.
     *
     * @return Whether or not the graph has cycles.
     */
    public boolean getHasCycles(){

        hasCycles = false;

        for(Vertex v : neighborMap.keySet()){

            currentSearch = new ArrayList<>();
            alreadyVisited = new HashSet<>();

            recursiveDepthFirstSearch(v, null);
            if(hasCycles){
                return true;
            }
        }

        return false;
    }

    /**
     * Runs the DFS to get the size of the connected nodes,
     * then compares it to the vertexMap's size to determine
     * if the graph is fully connected.
     *
     * @return Whether or not the graph is connected.
     */
    public boolean isConnected(){
        return vertexMap.size() == depthFirstSearch().size();
    }

    /**
     * The kicking-off point of the depth-first
     * search. If there is no Vertex A, it returns
     * an empty list. Otherwise, it sets the currentSearch
     * and alreadyVisited variables to new collections,
     * then starts the recursive DFS with Vertex A.
     *
     * @return The resulting depth-first search nodes
     * in an ArrayList.
     */
    public ArrayList<Vertex> depthFirstSearch(){

        if(!vertexMap.containsKey("A")){
            return new ArrayList<>();
        }

        currentSearch = new ArrayList<>();
        alreadyVisited = new HashSet<>();

        recursiveDepthFirstSearch(vertexMap.get("A"), null);

        return currentSearch;
    }

    /**
     * The recursive part of DFS. It checks whether a node has
     * already been visited, then sets hasCycles to true if it
     * has.
     *
     * Afterward, it adds itself to the currentSearch and alreadyVisited
     * collections, then loops through its neighboring nodes, excluding
     * the one that called it, and performs this method on them.
     *
     * @param vertex The Vertex to search.
     * @param exclude The previous Vertex which needs to be excluded from
     *                Vertex's neighbor list.
     */
    private void recursiveDepthFirstSearch(Vertex vertex, Vertex exclude){
        if(alreadyVisited.contains(vertex)){
            hasCycles = true;
            return;
        }

        currentSearch.add(vertex);
        alreadyVisited.add(vertex);

        HashSet<Vertex> neighbors = new HashSet<>(neighborMap.get(vertex));
        neighbors.remove(exclude);

        for(Vertex v : neighbors){
            recursiveDepthFirstSearch(v, vertex);
        }
    }

    /**
     * Performs the breadth-first search from Vertex A.
     *
     * It sets up a current tier and a next tier, starting
     * from the current tier, then taking all of their neighbors
     * and removing nodes already visited to create the next
     * tier. While doing this, it adds the next tier to the
     * currentSearch and alreadyVisited lists. When the nextTier
     * is empty, the loop (and search) end.
     *
     * @return The list of vertices found in the breadth-first
     * search.
     */
    public ArrayList<Vertex> breadthFirstSearch(){

        if(!vertexMap.containsKey("A")){
            return new ArrayList<>();
        }

        currentSearch = new ArrayList<>();
        alreadyVisited = new HashSet<>();

        HashSet<Vertex> currentTier = new HashSet<>();
        currentTier.add(vertexMap.get("A"));

        currentSearch.add(vertexMap.get("A"));
        alreadyVisited.add(vertexMap.get("A"));

        while(true){
            HashSet<Vertex> nextTier = new HashSet<>();

            for(Vertex v : currentTier){
                HashSet<Vertex> neighbors = new HashSet<>(neighborMap.get(v));
                neighbors.removeAll(alreadyVisited);

                currentSearch.addAll(neighbors);
                alreadyVisited.addAll(neighbors);

                nextTier.addAll(neighbors);
            }

            if(nextTier.isEmpty()){
                break;
            } else {
                currentTier = nextTier;
            }
        }


        return currentSearch;
    }
}
