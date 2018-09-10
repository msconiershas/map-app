
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Driver class that reads/parses the input file and creates NavigationGraph
 * object.
 * 
 *
 */
public class MapApp {

	private NavigationGraph graphObject;

	/**
	 * Constructs a MapApp object
	 * 
	 * @param graph
	 *            NaviagtionGraph object
	 */
	public MapApp(NavigationGraph graph) {
		this.graphObject = graph;
	}

	/**Main method that accepts user input and creates a NavigationGraph
	 * based on the file name. Also creates an instance of MapApp for graph
	 * operation.
	 * 
	 * @param: String [] args: will store command line arguments
	 */
	public static void main(String[] args) {
		//exit if CLAs are incorrect
		if (args.length != 1) {
			System.out.println("Usage: java MapApp <pathToGraphFile>");
			System.exit(1);
		}

		// read the filename from command line argument
		String locationFileName = args[0];
		try
		{
			//create a new Navigation graph for file name
			NavigationGraph graph = createNavigationGraphFromMapFile
					(locationFileName);
			//create new MapApp and go to startService for graph operations
			MapApp appInstance = new MapApp(graph);
			appInstance.startService();

			//exit if either exception occurs during input of file name
		} catch (FileNotFoundException e) {
			System.out.println("GRAPH FILE: " + 
					locationFileName + " was not found.");
			System.exit(1);
		} catch (InvalidFileException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

	}

	/**
	 * Displays options to user about the various operations on the 
	 * loaded graph
	 */
	public void startService() {
		System.out.println("Navigation App");
		Scanner sc = new Scanner(System.in);
		int choice = 0;

		do {
			System.out.println();
			System.out.println("1. List all locations");
			System.out.println("2. Display Graph");
			System.out.println("3. Display Outgoing Edges");
			System.out.println("4. Display Shortest Route");
			System.out.println("5. Quit");
			System.out.print("Enter your choice: ");

			//keep prompting user to input an integer
			while (!sc.hasNextInt()) {
				sc.next();
				System.out.println("Please select a valid option: ");
			}
			choice = sc.nextInt();

			switch (choice) {
			//input 1: prints all locations
			case 1:
				System.out.println(graphObject.getVertices());
				break;
				//input 2: prints graph
			case 2:
				System.out.println(graphObject);
				break;
				//input 3: prints outgoing edges for specific location
			case 3: {
				System.out.println("Enter source location name: ");
				String srcName = sc.next();
				Location src = graphObject.getLocationByName(srcName);

				//location is not a valid place
				if (src == null) {
					System.out.println(srcName + " is not a valid Location");
					break;
				}
				//get outgoing edges for location stored in src
				List<Path> outEdges = graphObject.getOutEdges(src);
				System.out.println("Outgoing edges for " + src + ": ");
				//print all outgoing edges for src
				for (Path path : outEdges) {
					System.out.println(path);
				}
			}
			break;
			//input 4: prints shortest route between two vertices based on 
			//time or cost.
			case 4:
				System.out.println("Enter source location name: ");
				String srcName = sc.next();
				Location src = graphObject.getLocationByName(srcName);

				System.out.println("Enter destination location name: ");
				String destName = sc.next();
				Location dest = graphObject.getLocationByName(destName);

				//location and/or destination aren't valid places
				if (src == null || dest == null) {
					System.out.println(srcName + " and/or " + destName 
							+ " are not valid Locations in the graph");
					break;
				}

				//user inputs location and destination as same place
				if (src == dest) {
					System.out.println(srcName + " and " + destName 
							+ " correspond to the same Location");
					break;
				}

				//get edge property names and print them to show user
				System.out.println("Edge properties: ");
				String[] propertyNames = graphObject.getEdgePropertyNames();
				for (int i = 0; i < propertyNames.length; i++) {
					System.out.println("\t" + (i + 1) + ": "
							+ propertyNames[i]);
				}
				System.out.println("Select property to compute shortest "
						+ "route on: ");
				try{
					int selectedPropertyIndex = sc.nextInt() - 1; 
					//if the property # inputed by user is not one listed
					if (selectedPropertyIndex >= propertyNames.length) {
						System.out.println("Invalid option chosen: " + 
								(selectedPropertyIndex + 1));
						break;
					}

					String selectedPropertyName = propertyNames
							[selectedPropertyIndex];
					//get shortest route based on location, destination,
					//and specified property
					List<Path> shortestRoute = graphObject.getShortestRoute
							(src, dest, selectedPropertyName);
					//print shortest route
					for(Path path : shortestRoute) {
						System.out.print(path.displayPathWithProperty
								(selectedPropertyIndex) + ",");
					}
					if(shortestRoute.size()==0) {
						System.out.print("No route exists");
					}
					System.out.println();

					break;
					//for case that user inputs something other than an integer
				} catch (InputMismatchException e) {
					System.out.println("Must choose and integer "
							+ "between 1 and " + propertyNames.length + ".");
				}
				//input 5: exits program
			case 5:
				break;
			default:
				System.out.println("Please select a valid option: ");
				break;
			}
		} 
		//quit program
		while (choice != 5);
		sc.close();
	}

	/**
	 * Reads and parses the input file passed as argument create a
	 * NavigationGraph object. The edge property names required for
	 * the constructor can be got from the first line of the file
	 * by ignoring the first 2 columns - source, destination. 
	 * Use the graph object to add vertices and edges as
	 * you read the input file.
	 * 
	 * @param graphFilepath
	 *            path to the input file
	 * @return NavigationGraph object
	 * @throws FileNotFoundException
	 *             if graphFilepath is not found
	 * @throws InvalidFileException
	 *             if header line in the file has < 3 columns or 
	 *             if any line that describes an edge has different 
	 *             number of properties than as described in the header or 
	 *             if any property value is not numeric 
	 */

	public static NavigationGraph createNavigationGraphFromMapFile
	(String graphFilepath) throws FileNotFoundException, 
	InvalidFileException {
		File file = new File(graphFilepath);

		if(!file.exists()) {
			System.out.println(file.exists());
			throw new FileNotFoundException();
		}

		String[] properties = null;//to store the property names
		int numProperties = 0;
		Scanner sc = new Scanner(file);

		//for the first line
		if(sc.hasNextLine()) {
			String a = sc.nextLine();
			String[] split = a.split(" ");
			properties = new String[split.length - 2];
			numProperties = properties.length;

			if(split.length < 3) {
				throw new InvalidFileException("Invalid file format.");
			}

			//get each property and put it into properties array
			for(int i = 2; i < split.length; i++) {
				properties[i - 2] = split[i];
			}
		}

		//create a graph based on the properties
		NavigationGraph graph = new NavigationGraph(properties);

		//for the rest of the lines
		while(sc.hasNextLine()) {
			String curr = sc.nextLine();
			String[] line = curr.split(" ");//all words in current line

			//check if there is the correct number of words
			if(line.length != properties.length + 2) {
				throw new InvalidFileException(
						"Incorrect number of properties.");
			}
			//make a new array to store all the property values
			List<Double> props = new ArrayList<Double>();
			//find properties in "line" and add them to propery list
			for(int i = 0; i < line.length - 2; i++) {
				try {
					props.add(Double.parseDouble(line[i + 2]));
				} catch(NumberFormatException e) {
					throw new InvalidFileException(
							"All properties must be Doubles.");
				}
			}

			for(int i = 0; i < 2; i++) {
				//get location with name
				Location src = graph.getLocationByName(line[i].toLowerCase());
				if(src == null) {//if no vertex exists yet
					//create new vertex
					Location vertex = new Location(line[i].toLowerCase());
					graph.addVertex(vertex);//add that vertex
				}
			}
			//create new path with give names and properties
			Path path = new Path(graph.getLocationByName(line[0].
					toLowerCase()), graph.getLocationByName(line[1].
							toLowerCase()), props);

			//add edge to graph
			graph.addEdge(graph.getLocationByName(line[0].toLowerCase()), 
					graph.getLocationByName(line[1].toLowerCase()), path);
		}
		return graph;
	}
}
