import java.util.Random;

public class Individuo implements Comparable<Individuo>{
	
	private int[] pi, 
	 			  s;
	private Double prob;
	private int num, 
				cruces;
	private int [][] adj;
	private Edge[] edges;
	
	
	public Individuo(int[] pi, int[] s, int num, int[][] adj) {
		this.pi = pi;
		this.s = s;
		this.num = num;
		this.adj = adj;
		this.cruces = cruces(this.pi.length, this.s.length);
		this.prob = 1/(Math.pow(cruces, 2)+1);
	}
	
	public Individuo(int[] pi, int[] s, int num, Edge[] edges) {
		this.pi = pi;
		this.s = s;
		this.num = num;
		this.edges = edges;
	}

	private int cruces(int n, int m) {
		int num = 0;
		for (int i = 0; i <= n-4; i++) {
			for (int j = i+2; j <= n-2; j++) {
				if(this.adj[i][j]!=0) {
					for (int k = i+1; k < j; k++) {
						for (int l = j+1; l < n; l++) {
							if(this.adj[i][j] == this.adj[k][l]) {
								num++;
							}
						}
					}
				}
			}
		}
		return num;
	}
	
	public void calcularAdj() {
		Random r = new Random();
		this.adj = new int[this.pi.length][this.pi.length];
		for (int j = 0; j < this.s.length/2; j++) {
			//s[j] = r.nextInt(2)+1;
			int u = pi[edges[j].getVertices()[0]-1];
			int w = pi[edges[j].getVertices()[1]-1];
			adj[u][w] = adj[w][u] = s[j];
			//System.out.print(s[j]+",");
		}
		this.cruces = cruces(this.pi.length, this.s.length);
		this.prob = 1/(Math.pow(cruces, 2)+1);
	}
	
	public void setPi(int[] pi) {
		this.pi = pi;
	}
	
	public void setS(int[] s) {
		this.s = s;
	}
	
	public int[][] getAdj(){
		return this.adj;
	}

	public int[] getPi() {
		return this.pi;
	}

	public int[] getS() {
		return this.s;
	}

	public double getProb() {
		return this.prob;
	}

	public int getNum() {
		return this.num;
	}
	
	public int getCruces() {
		return this.cruces;
	}

	@Override
	public int compareTo(Individuo o) {
		// TODO Auto-generated method stub
		return this.prob.compareTo(o.prob);
	}
	
	public String toString() {
		return "Individuo: "+this.num+", cruces: "+this.cruces+", probabilidad: "+this.prob;
		
	}
}
