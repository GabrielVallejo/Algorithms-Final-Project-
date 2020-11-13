import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;

public class KPMP {
	
	private static Edge[] edges;
	private static LinkedList<Integer>[] vertices;
	private static PriorityQueue<Individuo> poblacion;
	private static Individuo[] poblacionO, 
							   poblacion1;
	private static int[] pi, 
						 s;
	private static int genCount, 
					   size, 
					   p_number;
	private static Random r;
	//private static float avgCrossing;
	
	
	private static void select() {
		//avgCrossing = 0;

		
		if(size<35) {
			for (int i = 0; i < size; i++) {
				poblacionO[i] = poblacion.poll();
			}
			
		}else {
			int temp = (int)Math.round(size*0.9);
			for (int i = 0; i < temp; i++) {
				poblacionO[i] = poblacion.poll();
				//avgCrossing += poblacionO[i].getCruces();
			}
			for (int i = temp; i < size; i++) {
				
				int rep = Math.min(r.nextInt(8), r.nextInt((int)Math.floor(size*0.1))) ;
				for (int j = 0; j < rep; j++) {
					poblacion.poll();
				}
				poblacionO[i] = poblacion.poll();
				//avgCrossing += poblacionO[i].getCruces();
			}
		}
		
		//avgCrossing /= size;
	}
	
	
	private static Object[] crossoverEdges(int[] pi1, int[] pi2) {
		int start = r.nextInt(pi1.length)%pi1.length;
		int len = r.nextInt(pi1.length)%pi1.length;
        int[] omega= pi1.clone();
        int[] omega2= pi2.clone();
        int j1 = 0;
        for(int i = start;i <(start+len)%pi1.length;i++)
        {
        	j1++;
        	omega[i] = pi2[i];
        }

        for(int i = start;i <(start+len)%pi1.length;i++)
        {
        	omega2[i] = pi1[i];
        }
        /*
        for(int i= 0; i <omega.length; i++) {
        	System.out.print(omega[i] +","); 
        }
        
        System.out.println();
        for(int i= 0; i <omega2.length; i++) {
            System.out.print(omega2[i] +","); 
        }*/
        return new Object[]{omega, omega2};
 	}
	
	
	//crossover, funciona para nodos 
	private static Object[] crossoverNodes(int[] pi1, int[] pi2) {
		int start = r.nextInt(pi1.length)%pi1.length;
		int len = r.nextInt(pi1.length)%pi1.length;
        int[] omega= new int[pi1.length];
        int[] omega2= new int[pi1.length];
        int j1 = 0;
        for(int i = start;i <(start+len)%pi1.length;i++)
        {
        	j1++;
        	omega[i] = pi1[i];
        }
        
     
        
        for(int i = start;i <(start+len)%pi1.length;i++)
        {
        	omega2[i] = pi2[i];
        }
        
       
        
        ArrayList<Integer> last = new ArrayList<Integer>();
        for(int j = (start+len)%pi1.length;j < pi1.length;j++)
        {
        	
        	if(last.size()<omega.length) {
        		
        		last.add(pi1[j]);
        	}
        }
        
        
        for(int j = 0;j < start;j++)
        {
        	if(last.size()<omega.length) {
        		last.add(pi1[j]);
        	}
        	
        }
        
        
        ArrayList<Integer> last1 = new ArrayList<Integer>();
        for(int j = (start+len)%pi1.length;j < pi1.length;j++)
        {
        	if(last1.size()<omega.length) {

        		last1.add(pi2[j]);
        	}
        }
        
        for(int j = 0;j < start;j++)
        {
        	if(last1.size()<omega.length) {

        		last1.add(pi2[j]);
        	}
        	
        }
        
        
        Collections.shuffle(last);
        Collections.shuffle(last1);
	    if(last1.size()>0) {
	    	for(int j = 0;j < start;j++)
	        {
	    	   omega[j] = last.get(j);        	
	        }
	       
	        for(int j = start+j1,i=start;j < pi1.length;j++,i++)
	        {
	           omega[j] = last.get(i);
	        }
	    }
        
	    if(last.size()>0) {
	
	    	for(int j = 0;j < start;j++)
	        {
	        	omega2[j] = last1.get(j);        	
	        }
	        
	        for(int j = start+j1, i = start;j < pi1.length;j++, i++)
	        {
	           omega2[j] = last1.get(i);
	        	
	        }
	     }
//        for(int i= 0; i <omega.length; i++) {
//         System.out.print(omega[i] +","); 
//        }
//        
//        System.out.println();
//        for(int i= 0; i <omega2.length; i++) {
//            System.out.print(omega2[i] +","); 
//           }
//        
//        System.out.println();
  
        return new Object[]{omega, omega2};
 	}
	
	private static Object[] mutation(int[] pi, int[] s) {
		int aux;
		//Swap aleatorio de 2 elementos de pi
		int pi1 = (int) Math.floor(Math.random()*(pi.length-1));
		int pi2 = (int) Math.floor(Math.random()*(pi.length-1));

		aux = pi[pi1];
		pi[pi1] = pi[pi2];
		pi[pi2]= aux;

		//Cambio de pagina en s
		int s1 = (int) Math.floor(Math.random()*(s.length-1));
		int temp = r.nextInt(p_number)+1;
		while(s[s1] == temp) {
			temp = r.nextInt(p_number)+1;
		}
		
		s[s1] = temp;
		return new Object[]{pi, s};
	}
	
	public static void main(String[] args) {

		try {
			Scanner scan = new Scanner(System.in);
			String[] linea;
			System.out.println("Ingrese el nombre (con ruta de ser necesario) del documento de prueba: ");
			String doc = scan.nextLine();
			FileReader file = new FileReader(doc);
			BufferedReader br = new BufferedReader(file);
			br.readLine();
			linea = br.readLine().split(" ");
			// m es el número de aristas
			int m = Integer.parseInt(linea[2]);
			// n es el número de vértices
			int n = Integer.parseInt(linea[0]);			
			edges = new Edge[m];
			vertices =  new LinkedList[n];
			pi = new int[n];
			s = new int[m];
			int pos = 0;
			String pointer;
			//Llena el arreglo de aristas y la lista de adyacencia de los vertices
			while((pointer = br.readLine()) != null) {
				linea = pointer.split(" ");
				//System.out.println(Integer.parseInt(linea[0])+","+Integer.parseInt(linea[1]));
				if(vertices[Integer.parseInt(linea[0])] == null) {
					LinkedList<Integer> adyacentes = new LinkedList<Integer>(); 
					adyacentes.add(Integer.parseInt(linea[1]));
					vertices[Integer.parseInt(linea[0])] = adyacentes;
				}else {
					vertices[Integer.parseInt(linea[0])].add(Integer.parseInt(linea[1]));
				}
				Edge arista = new Edge(Integer.parseInt(linea[0]),Integer.parseInt(linea[1]));
				edges[pos++] = arista;	

			}
			br.close();
			System.out.print("Ingrese el tamaño de la población que desea calcular: ");
			size = scan.nextInt();
			System.out.print("Ingrese el número de páginas existentes para estas poblaciones: ");
			p_number = scan.nextInt();
			poblacionO = new Individuo[size];
			poblacion1 = new Individuo[size];
			genCount = 0;
			r = new Random();
			int limite = Math.min(3*n+3*m+100, 1000);
			//Genera una población random inicial 
			int [][] adj;
			int [] historial;
			long start = System.currentTimeMillis();
			for (int i = 0; i < size; i++) {
				adj= new int[n][n];
				historial = new int[n];
				//Para cada individuo se genera un arreglo con orden aleatorio de los vertices
				for(int j = 0; j < n; j++) {
					int temp = r.nextInt(n);
					while(historial[temp] != 0) {
						temp = r.nextInt(n);
					}
					historial[temp] = 1;
					pi[j] = temp;
					//System.out.print(temp+",");
				}
				//System.out.println();
				//Para cada individuo se genera un arreglo con la pagina correspondiente a cada arista 
				//y se asigna a la matriz de adyacencia.
				for (int j = 0; j < m; j++) {
					s[j] = r.nextInt(p_number)+1;
					
					int u = pi[edges[j].getVertices()[0]-1];
					int w = pi[edges[j].getVertices()[1]-1];
					adj[u][w] = adj[w][u] = s[j];
					//System.out.print(s[j]+",");
				}
				//System.out.println();
				
				Individuo in = new Individuo(pi, s, i+1, adj);

				poblacionO[i] = in;
			}

			//Criterio para terminar el número de intentos de generacion de poblaciones
			//Genera el resto de las poblaciones a partir de la poblacion inicial.		
			Object[] nS;
			
			int comodin;
			while(genCount<limite) {
				poblacion = new PriorityQueue<Individuo>(Collections.reverseOrder());
				comodin = 0;
				if(size%2 != 0) {
					comodin++;
					nS = crossoverNodes(poblacionO[0].getPi(), poblacionO[1].getPi());
					poblacion1[0] = new Individuo((int[])nS[0], poblacionO[0].getS(), 1, edges);
					nS = crossoverEdges(poblacionO[0].getS(), poblacionO[1].getS());
					poblacion1[0].setS((int[])nS[0]);
					nS = mutation(poblacion1[0].getPi(), poblacion1[0].getS());
					poblacion1[0].setPi((int[]) nS[0]);
					poblacion1[0].setS((int[]) nS[1]);
					poblacion1[0].calcularAdj();
					poblacion.add(poblacionO[0]);
				}
				
				for (int i = 0 + comodin; i < poblacionO.length; i+=2) {
					
					//Aquí se genera las nuevas Pi con el crossover
					nS = crossoverNodes(poblacionO[i].getPi(), poblacionO[i+1].getPi());
					poblacion1[i] = new Individuo((int[])nS[0], poblacionO[i].getS(), i+1, edges);
					poblacion1[i+1] = new Individuo((int[])nS[1], poblacionO[i+1].getS(), i+2, edges);
					
					//aqui se genera las nuevas S con el crossover
					nS = crossoverEdges(poblacionO[i].getS(), poblacionO[i+1].getS());
					poblacion1[i].setS((int[])nS[0]);
					poblacion1[i+1].setS((int[])nS[1]);
					
	                //Aquí se genera la mutacion en cada pi y S de cada individuo
					nS = mutation(poblacion1[i].getPi(), poblacion1[i].getS());
					poblacion1[i].setPi((int[]) nS[0]);
					poblacion1[i].setS((int[]) nS[1]);
					nS = mutation(poblacion1[i+1].getPi(), poblacion1[i+1].getS());
					poblacion1[i+1].setPi((int[]) nS[0]);
					poblacion1[i+1].setS((int[]) nS[1]);
					
					//Aquí se calculan los cruces de los individuos de P'' con su respectiva probabilidad
					poblacion1[i].calcularAdj();
					poblacion1[i+1].calcularAdj();
					
					//Aquí se combinan P y P''
					poblacion.add(poblacionO[i]);
					poblacion.add(poblacionO[i+1]);
					poblacion.add(poblacion1[i]);
					poblacion.add(poblacion1[i+1]);
	
				}
				//Escogemos el 90% de la población más apto y el 10% de la población de manera random.
				select();
				genCount++;
				
			}
			long end = System.currentTimeMillis();
			long time = (end-start);
			FileWriter pen = new FileWriter(new File("Resultados.txt"));
			pen.write("Menor individuo: "+poblacionO[0].getNum()+", # Cruces: "+poblacionO[0].getCruces()+", Tiempo de ejecución: "+Long.toString(time));
			pen.close();
			//Individuo in = poblacion.poll();
			System.out.println(poblacionO[0].toString());
			//System.out.println("Promedio de Población: "+avgCrossing);
			System.out.print("Posición de los vertices: ");
			for (int i = 0; i < poblacionO[0].getPi().length; i++) {
				System.out.print((poblacionO[0].getPi()[i]+1)+",");
			}
			System.out.println();
			System.out.print("Página de las aristas: ");
			for (int i = 0; i < poblacionO[0].getS().length; i++) {
				System.out.print(poblacionO[0].getS()[i]+",");
			}
		} catch (FileNotFoundException e) {
			System.out.println("Archivo no encontrado en la dirección especificada.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error en input o output encontrado.");
		} catch (NumberFormatException e) {
			System.out.println("Se esperaba un número entero.");
		}

	}
}
