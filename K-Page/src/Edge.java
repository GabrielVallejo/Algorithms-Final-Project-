
public class Edge {
	
	private int [] vertices;
	
	public Edge(int v1, int v2) {
		this.vertices = new int[2];
		this.vertices[0] = v1;
		this.vertices[1] = v2;
	}
	
	public int [] getVertices() {
		return this.vertices;
	}
}
