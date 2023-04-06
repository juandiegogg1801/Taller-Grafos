package co.edu.uptc.ProgramacionIII.TallerGrafos.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraDistance;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
/**
 * Clase que representa el grafo
 * @author JUAN DIEGO
 *
 */
public class Map {
	private Graph<Integer, Double> graph;
	private List<String> vertices;
	private List<Integer>[] ady; //lista de adyacencia
	public Map() {
		graph = new SparseGraph<Integer, Double>();
		graph.addVertex(0);
		graph.addVertex(1);
		graph.addVertex(2);
		graph.addVertex(3);
		graph.addVertex(4);
		
		graph.addEdge(0.5, 0, 1);
		graph.addEdge(4.0, 0, 3);
		graph.addEdge(1.0, 0, 4);
		graph.addEdge(1.5, 1, 3);
		graph.addEdge(2.0, 1, 2);
		graph.addEdge(3.0, 2, 3);
		
		vertices = new ArrayList<String>();
		vertices.add("Terminal");
		vertices.add("Colegio");
		vertices.add("Cementerio");
		vertices.add("Hospital");
		vertices.add("Parque");
		
		createListaAdyacencia();
		// Se agregan aristas a la lista de adyacencia en ambas direcciones
	    addArista(0, 1);
	    addArista(0, 3);
	    addArista(0, 4);
	    addArista(1, 2);
	    addArista(1, 3);
	    addArista(2, 3);
	    
	    addArista(1, 0);
	    addArista(3, 0);
	    addArista(4, 0);
	    addArista(2, 1);
	    addArista(3, 1);
	    addArista(3, 2);
	}
	
	/**
	 * Retorna el tamaño del grafo (numero de vertices)
	 * @return tamaño del grago
	 */
	public int getSizeGrafo() {
		return graph.getVertices().size();
	}
	
	/**
	 * Crea una lista de adyacencia
	 */
	public void createListaAdyacencia() {
		int size = getSizeGrafo();
		ady = new ArrayList[size];
		for(int i = 0;i < size; i++) {
			ady[i] = new ArrayList<Integer>();
		}
	}
	
	/**
	 * Agrega a una arista de un vertice a otro a la lista de adyacencia
	 * @param v
	 * @param w
	 */
    public void addArista(int v, int w) {
        ady[v].add(w);
    }

	/**
	 * Envia un vertice de origen y uno de destino ambos de tipo entero al 
	 * metodo encontrar todos los caminos
	 * @param source
	 * @param destination
	 * @return todosLosCaminos
	 */
	public List<List<Integer>> findWholeRoutes(String source, String destination) {
		int origen = getIndexVertex(source);
		int destino = getIndexVertex(destination);
		List<List<Integer>> todosLosCaminos = encontrarTodosLosCaminos(origen, destino);
		return todosLosCaminos;
	}
	
	/**
	 * Relaciona una estacion con un numero entero
	 * @param vertex
	 * @return numVertex
	 */
    public Integer getIndexVertex(String vertex) {  	
    	Integer numVertex = 0;
    	boolean centinela = false;
    	for (int i = 0; i < vertices.size() && !centinela; i++) {
			if(vertices.get(i) == vertex) {
				numVertex = i;
				centinela = true;
			}
		}
    	return numVertex;
    }

	/***
	 * Encuentra todas las rutas posibles de un punto de origen a uno de 
	 * destino 
	 * @param origen
	 * @param destino
	 * @return caminos
	 */
	public List<List<Integer>> encontrarTodosLosCaminos(int origen, int destino) {
		int size = getSizeGrafo();
        List<List<Integer>> caminos = new ArrayList<List<Integer>>();
        boolean[] visitados = new boolean[size];
        List<Integer> camino = new ArrayList<Integer>();
        camino.add(origen);
        encontrarTodosLosCaminosAux(origen, destino, visitados, camino, caminos);
        return caminos;
    }
	/**
	 * Recorre al grafo para encontrar los vertices vecinos con ayuda de una 
	 * lista de adyacencia
	 * @param origen
	 * @param destino
	 * @param visitados
	 * @param camino
	 * @param caminos
	 */
    private void encontrarTodosLosCaminosAux(int origen, int destino, boolean[] visitados, List<Integer> camino, List<List<Integer>> caminos) {
    	// La primera estacionvisitada es la de origen
    	visitados[origen] = true;
        if (origen == destino) {
            caminos.add(new ArrayList<Integer>(camino));
        } else {
            for (Integer adyacente : ady[origen]) { // Recorrer los nodos adyacentes al nodo actual
            	
            	if (!visitados[adyacente]) { // Si el nodo adyacente no ha sido visitado, agregarlo al camino actual y continuar la búsqueda
                    camino.add(adyacente);
            		
                    encontrarTodosLosCaminosAux(adyacente, destino, visitados, camino, caminos);
                    camino.remove(camino.size() - 1); // Eliminar el nodo adyacente del camino actual antes de continuar con otros caminos
                }
            }
        }
        visitados[origen] = false;
    }    

    /**
     * Calcula las distancias totales de cada ruta
     * @param todosLosCaminos
     * @return distancesTotal
     */
    public ArrayList<Double> calculateDistances(List<List<Integer>> todosLosCaminos) {
        ArrayList<Double> distancesTotal = new ArrayList<Double>();
    	for (List<Integer> camino : todosLosCaminos) {
    		int sizeCamino = camino.size();
    		double[] distances = new double[sizeCamino];
    		int v1 = 0;
    		int v2 = 1;
    		int enlaces = camino.size()-1;
    		int contador = 0;
    		while(contador < enlaces) {
    			distances[contador] = graph.findEdge(camino.get(v1), camino.get(v2));
    			v1++;
    			v2++;
    			contador++;
    		}
        	double distance = 0;
    		for (int i = 0; i < distances.length; i++) {
    			distance = distance + distances[i];
    		}
    		distancesTotal.add(distance);
		}
    	return distancesTotal;
    }
    /**
     * Encuentra la ruta rapida con la distancia mas corta
     * @param distances
     * @param rutasEstaciones
     * @return cadena
     */
    public String distanceShortest(ArrayList<Double> distances, ArrayList<ArrayList<String>> rutasEstaciones) {
    	double distanceTotal = 0;
    	int size = distances.size();
    	String cadena = "";
    	if(size == 1) {
    		distanceTotal = distances.get(0);
    		cadena = "No existe una ruta corta";
    	}else {
        	double distance1 = distances.get(0);
        	double distance2 = distances.get(1);
    		int i = 1;
    		while(i<size) {
    			if(distance2 < distance1) {
    				distance1 = distance2;
    			}
    			i++;
    			if(i<size) {
    				distance2 = distances.get(i);
    			}
    		}
    		distanceTotal = distance1;
    		ArrayList<String> ruta = new ArrayList<String>();
    		for (int j = 0; j < distances.size(); j++) {
				if(distances.get(j) == distanceTotal) {
					ruta = rutasEstaciones.get(j);
				}
			}
    		cadena = "Ruta corta: ";
    		for (int j = 0; j < ruta.size(); j++) {
				cadena = cadena + ruta.get(j)+", ";
			}
    		cadena = cadena+" distancia: "+distanceTotal;
    	}
    	return cadena;
    }
}
