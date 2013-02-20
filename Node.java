package miniJava;

import java.util.Random;
import java.util.Vector;

/**
 * Display AST in DOT format
 * @author Blaine Stancill
 * @email blaine.stancill@gmail.com
 * @version COMP 520 V2.3
 * 
 */
public class Node {
	protected Node parent = null;
	private long id;
	private static int idLength = 12;
	private String name = "";
	private String label = "";
	private String labelAdd = "";
	private String nodeStyle = "";
	public static Vector<Long> ids = new Vector<Long>();
	public static boolean color = false;
		
	public Node(Node parent, String name) {
		this.parent = parent;
		this.name = name;
		id = getId();
	}
	
	public Node(String name) {
		this.name = name;
	}
	
	/**
	 * Print out:
	 *  - Node's style if available
	 *    - Parent -> Child node relation
	 *    - Node's label (default it's name)
	 *  - Closing node style '}'
	 */
	public void print() {
		if (nodeStyle.length() > 0)
			printNodeStyle();        // start node styling
		
		System.out.println(this.toString());

		printLabel();
		
		if (nodeStyle.length() > 0)
			System.out.println("}"); // end node styling
	}
	
	/**
	 * Prints out the node's label with it's name as default.
	 * See {@link #setNodeLabel(String) setNodeLabel} to give it a different name.
	 */
	public void printLabel() {
		System.out.println(name + id + " [label=\"" + (label.length() > 0 ? label : name) + " " + labelAdd + "\"];");
	}
	
	public void printNodeStyle() {
		System.out.println(nodeStyle);
	}
	
	/**
	 * Set's the node's label. If never set, the default label
	 * is the node's name. To add more to the node's label use
	 * {@link #addToLabelName(String) addToLabelName}.
	 * @param label - The displayed label of the node
	 * @return This node
	 */
	public Node setNodeLabel(String label) {
		this.label = label;
		return this;
	}
	
	/**
	 * If you need more than a label's name, add it here.
	 * @param labelAdd - String to add onto the node's label
	 * @return This node
	 */
	public Node addToLabelName(String labelAdd) {
		this.labelAdd = labelAdd;
		return this;
	}

	/**
	 * 
	 * @param color  - Color of the node
	 * @param filled - Fill the node with color?
	 * @return This node
	 */
	public Node setNodeStyle(String color) {
		return setNodeStyle(color, "");
	}
	
	/**
	 * 
	 * @param color  - Color of the node
	 * @param filled - Fill the node with color?
	 * @param additional - Additional node styling
	 * @return This node
	 */
	public Node setNodeStyle(String color, String additional) {
		nodeStyle = "{\nnode [" +
				"fontsize=10, fontname=\"Times-Roman\"," +
				"shape=ellipse" +
				(Node.color && color.length() > 0 ? ", style=filled, color=\"" + color + "\"" : "") +
				(additional.length() > 0 ? ", " + additional : "") + "];";
		return this;
	}
	
	// Generate a fairly random number for the specified length
	private static long generateRandom(int length) {
	    Random random = new Random();
	    
	    char[] digits = new char[length];
	    digits[0] = (char) (random.nextInt(9) + '1');
	    
	    for (int i = 1; i < length; i++) {
	        digits[i] = (char) (random.nextInt(10) + '0');
	    }
	    
	    return Long.parseLong(new String(digits));
	}
	
	// Get a unique fairly random number
	public static long getId() {
		Long tempId = generateRandom(idLength);
		
		while (ids.contains(tempId)) {
			tempId = generateRandom(idLength);
		}
		
		ids.add(tempId);
		
		return tempId.longValue();
	}
	
	@Override
	public String toString() {
		if (parent == null) {
			return name + id + ";";
		} else {
			return parent.name + parent.id + " -> " + name + id + ";";
		}
	}
}