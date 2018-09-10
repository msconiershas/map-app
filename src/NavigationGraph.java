
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * NavigationGraph implements GraphADT.  Some operations are adding and getting
 * an edge or vertex, getting outgoing edges, edge properties, and shortest
 * route. This class also has a toString method that helps print a graph.
 *
 * @authors: McKinley Sconiers-Hasan
 */
public class NavigationGraph implements GraphADT<Location, Path> {
	private String[] edgePropertyNames;
	private List<GraphNode<Location,Path>> graph;

	/** NavigationGraph constructor
	 * 
	 * @param String[] edgePropertyNames
	 * 				array with the edge property names
	 *  */
	public NavigationGraph(String[] edgePropertyNames) {
		this.edgePropertyNames = edgePropertyNames;
		graph = new ArrayList<GraphNode<Location,Path>>();
	}

	/**
	 * Returns a Location object given its name
	 * 
	 * @param name
	 *            name of the location
	 * @return Location object
	 */
	public Location getLocationByName(String name) {
		if(name == null) {
			throw new IllegalArgumentException();
		}
		//get location regardless of letter case
		for(int i = 0; i < graph.size(); i++) {
			if(graph.get(i).getVertexData().getName().
					equalsIgnoreCase(name)) {
				return graph.get(i).getVertexData();
			}
		}
		return null; 
	}

	/**
	 * Getter method for edge property names
	 * 
	 * @return String[] 
	 * 		array of String that denotes the edge property names
	 */
	public String[] getEdgePropertyNames() {
		return this.edgePropertyNames;
	}

	/**
	 * Return a string representation of the graph
	 * 
	 * @return String 
	 * 				String representation of the graph
	 */
	public String toString() {
		
		
		String out = "";//String to add each outgoing edge to
		int count = 0;//count to see how many edges have been put on each line
		for(int i = 0; i < graph.size(); i++) {//get each vertex
			Location vertex = graph.get(i).getVertexData();
			
			//get all the out edges for the vertex
			List<Path> list = getOutEdges(vertex);
			
			//for each out edge, add it to the out String with a comma
			for(int j = 0; j < list.size(); j++) {
				if(count == 3) {
					out += "\n" + list.get(j) + ", ";
				}
				else {
					out += list.get(j) + ", ";
				}

				if(count == 3) {
					count = 0;
				}
				count++;	
			}
		}
		int last = out.lastIndexOf(',');//get index of the last comma
		String w = out.substring(0, last);//make a new String without the last comma
		return w;
	}

	/**
	 * Adds a vertex to the Graph
	 * 
	 * @param vertex
	 *            vertex to be added
	 */
	public void addVertex(Location vertex) {
		if(vertex == null) {
			throw new IllegalArgumentException();
		}
		GraphNode<Location,Path> temp =
				new GraphNode<Location,Path>(vertex,graph.size()); //create a vertex at "Location vertex"
		graph.add(temp);//add the new vertex to the list of GraphNodes
	}

	/**
	 * Creates a directed edge from src to dest
	 * 
	 * @param src
	 *            source vertex from where the edge is outgoing
	 * @param dest
	 *            destination vertex where the edge is incoming
	 * @param edge
	 *            edge between src and dest
	 */
	public void addEdge(Location src, Location dest, Path edge) {
		//see if src already exists, if so, get it
		if(src == null || dest == null || edge == null || src == dest 
				|| edge.getProperties().size() != edgePropertyNames.length 
				|| doesNotExist(src) || doesNotExist(dest)) {
			throw new IllegalArgumentException("Src: " + src + ", Dest: " + 
					dest + ", Edge: " + edge);
		}
		//edge initially linked to null until proper vertex found
		GraphNode<Location, Path> source = null; 
		for(int i = 0; i < graph.size(); i++) {
			if(src.equals(graph.get(i).getVertexData())) {
				source = graph.get(i);
			}
		}
		source.addOutEdge(edge); //link edge to vertex
	}

	/**
	 * Returns edge if there is one from src to dest vertex else null
	 * 
	 * @param src
	 *            Source vertex
	 * @param dest
	 *            Destination vertex
	 * @return Edge of type E from src to dest
	 * 	
	 */
	public Path getEdgeIfExists(Location src, Location dest) {
		if(src == dest || src == null || dest == null) {
			throw new IllegalArgumentException();
		}
		Path exists = null;
		GraphNode<Location, Path> source = null;
		//vertex initially null until found
		for(int i = 0; i < graph.size(); i++) {
			if(src.equals(graph.get(i).getVertexData())) {
				source = graph.get(i);
			}
		}
		//path between dest, src null until found
		List<Path> list = source.getOutEdges();
		for(int i = 0; i < list.size(); i++) {
			if(dest.equals(list.get(i).getDestination())) {
				exists = list.get(i);
			}
		}
		return exists; //either returns null or path between dest, src
	}

	/**
	 * Returns the outgoing edges from a vertex
	 * 
	 * @param src
	 *          Source vertex for which the outgoing edges need to be obtained
	 * @return List of edges of type E
	 */
	public List<Path> getOutEdges(Location src) {
		if(src == null || doesNotExist(src)) {
			throw new IllegalArgumentException("Src: " + src);
		}
		List<Path> list = null; //list of outgoing edges of src 
		for(int i = 0; i < graph.size(); i++) {
			if(src.equals(graph.get(i).getVertexData())) {
				list = graph.get(i).getOutEdges();
			}
		}
		return list; //return outgoing edges or null if none
	}

	/**
	 * Returns neighbors of a vertex
	 * 
	 * @param vertex
	 *            vertex for which the neighbors are required
	 * @return List of vertices(neighbors) of type V
	 */
	public List<Location> getNeighbors(Location vertex) {
		if(vertex == null || doesNotExist(vertex)) {
			throw new IllegalArgumentException("Vertex: " + null);
		}

		GraphNode<Location, Path> src = null;
		//initialize src to vertex at "Location vertex"
		for(int i = 0; i < graph.size(); i++) {
			if(vertex.equals(graph.get(i).getVertexData())) {
				src = graph.get(i);
			}
		}

		List<Path> list = src.getOutEdges();//get all the out edges of vertex
		//list to store all the destinations
		List<Location> neighbors = new ArrayList<Location>();

		for(int i = 0; i < list.size(); i++) {//for each path
			neighbors.add(list.get(i).getDestination());//add the destination
		}
		return neighbors; //return all neighbors or null if none
	}

	/**
	 * Calculate the shortest route from src to dest vertex using
	 * edgePropertyName and Dijkstra's algorithm.
	 * 
	 * @param src
	 *            Source vertex from which the shortest route is desired
	 * @param dest
	 *            Destination vertex to which the shortest route is desired
	 * @param edgePropertyName
	 *            edge property by which shortest route has to be calculated
	 * @return List of edges that denote the shortest route by edgePropertyName
	 */
	public List<Path> getShortestRoute(Location src, Location dest, 
			String edgePropertyName) {
		if(src == null || dest == null || edgePropertyName == null 
				|| src == dest) {
			throw new IllegalArgumentException("Locations and "
					+ "properties cannot be null");
		}

		//if the edges or property don't exist
		if(doesNotExist(src) || doesNotExist(dest) || 
				propertyDoesNotExist(edgePropertyName)) {
			throw new IllegalArgumentException("Src: " + src 
					+ ", Dest: " + dest + ", Edge: " + edgePropertyName);
		}

		String[] propNames = getEdgePropertyNames();
		int t = 0;//stores the index of the property name

		//find the correct property name that matches
		for(int i = 0; i < propNames.length; i++) {
			if(propNames[i].equals(edgePropertyName)) {
				t = i;
			}
		}

		//array to keep track of which vertices have been visited
		boolean[] visited = new boolean[graph.size()];
		//to keep track of the total weight of each vertex
		double[] totalWeight = new double[graph.size()];
		//list to store predecessors
		List<GraphNode<Location,Path>> pred = new ArrayList<GraphNode<
				Location,Path>>();

		for(int i = 0; i < graph.size(); i++) {
			visited[i]  = false;//initialize visited to false
			totalWeight[i] = Double.MAX_VALUE;//set total weight to infinity
			pred.add(null);//initialize all predecessors to null
		}

		int srcIndex = getIndex(src);//index of the incoming source node
		int destIndex = getIndex(dest);//index of the incoming destination node

		totalWeight[srcIndex] = 0;//set the total weight for the source to 0

		VertexPriorityQueue pq = new VertexPriorityQueue();

		try {
			pq.insert(totalWeight[srcIndex], src);
		} catch (PriorityQueueFullException e) {
			e.printStackTrace();
		}

		while(!pq.isEmpty()) {

			GraphNode<Location, Path> currNode = null;
			//make new array to store output from queue
			Object[] min = new Object[2];
			int w = 0;//index of current node
			List<Location> b = getVertices();

			try {
				min = pq.removeMin();//remove min from the queue; 
				w = getIndex((Location) min[1]);//get index of the Location
				visited[w] = true;//set min Location to visited
			} catch (PriorityQueueEmptyException e) {
				System.out.println("Queue is empty.");
			}

			//the node that has the location that just came out of the queue
			currNode = graph.get(w); 

			Location curr = (Location) min[1];
			//get the neighbors of vertex
			List<Location> neighbors = getNeighbors(curr);
			//to store all the names of the neighbors
			List<String> names = new ArrayList<String>();

			for(int i = 0; i < neighbors.size(); i++) {//for each neighbor
				//add that name to the list
				names.add(neighbors.get(i).getName());
			}

			Collections.sort(names);//sort the names in alphabetical order

			//to check every Location in neighbors
			for(int j = 0; j < neighbors.size(); j++) {
				int index = 0;

				//to check every Location in graph
				List<Location> locations = getVertices();
				for(int i = 0; i < locations.size(); i++) {
					//get the index for the neighbor
					if(neighbors.get(j).equals(locations.get(i))) {
						index = i;
					}
				}
				//this neighbor has not been visited
				if(visited[index] == false) {
					//find out if total weight can be reduced
					Path d = null;
					Location successor = locations.get(index);//successor
					//get the out edges for curr
					List<Path> edges = getOutEdges(curr);

					for(int i = 0; i < edges.size(); i++) {
						//if the destination equals the neighbor
						if(edges.get(i).getDestination().equals
								(locations.get(index))) {
							d = edges.get(i);//we have the correct Path
						}
					}
					
					//get the properties for the edge we want
					List<Double> prop = d.getProperties();
					//get the value of the property that matches
					Double y = prop.get(t);

					//if successor total weight > curr's total weight + edge 
					//weight from curr to successor
					if(totalWeight[index] > totalWeight[w] + y) {
						//update the total weight
						totalWeight[index] = totalWeight[w] + y;
						pred.remove(index);//remove the current predecessor
						//replace it with the current node
						pred.add(index, currNode);

						try {
							pq.insert(totalWeight[index], successor);//update 		
						} catch (PriorityQueueFullException e) {
							System.out.println("Queue is full.");
						}
					}
				}
			}
		}

		//find the path given the current list of predecessors 
		List<Path> c = new ArrayList<Path>();//list to store the final path
		Location s = dest;
		int q = destIndex;

		while(!s.equals(src)) {
			Location pre = pred.get(q).getVertexData();//get the predecessor
			//get edge and add it to the list of edges
			c.add(getEdgeIfExists(pre, s));
			s = pre;//make the new current node the predecessor
			q = getIndex(s);//get the index of the new current node

		}
		return c;//return the list of Paths
	}
	
	/**Returns the index of location x in the graph or -1 if not found
	 * 
	 * @param Location x
	 * @returns int
	 * 		
	 * */
	private int getIndex(Location x) {
		//look through all vertices to see if one has location "x"
		for(int i = 0; i < graph.size(); i++) {
			if(x.equals(graph.get(i).getVertexData())) {
				return i;
			}
		}
		return -1;
	}

	/**Returns a boolean depending if a vertex exists
	 * 
	 * @param Location x
	 * @return boolean
	 * 
	 * */
	private boolean doesNotExist(Location x) {
		//look through all vertices to see if one matches "x"
		for(int i = 0; i < graph.size(); i++) {
			if(graph.get(i).getVertexData().equals(x)) {
				return false;
			}
		}
		return true;
	}
	/**Returns a boolean depending if a propery exists
	 * 
	 * @param String name
	 * 			name we are checking if exists
	 * @return boolean 
	 * 
	 */
	private boolean propertyDoesNotExist(String name) {
		//look through all properties to see if one matches "name"
		for(int i = 0; i < edgePropertyNames.length; i++) {
			if(edgePropertyNames[i].equals(name)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Getter method for the vertices
	 * 
	 * @return List of vertices of type V
	 */
	public List<Location> getVertices() {
		List<Location> list = new ArrayList<Location>();
		//look through each index of graph and add vertices to list
		for(int i = 0; i < graph.size(); i++) {
			list.add(graph.get(i).getVertexData());
		}
		return list;
	}
}
