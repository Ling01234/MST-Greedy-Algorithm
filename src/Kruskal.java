import java.util.*;

public class Kruskal{

    public static WGraph kruskal(WGraph g){
    	
    	ArrayList <Edge> list = g.listOfEdgesSorted();
    	int totalNodes = g.getNbNodes();
    	int counter = 0;
    	DisjointSets p = new DisjointSets(totalNodes);
    	WGraph wg = new WGraph();
    	
    	
    	
    	
    	//iterate for all the edges
    	for (int i = 0; i < list.size(); i++) {
    		
    		if (counter == totalNodes) {
    			break; //get out of loop if all vertices are grouped
    		}
    		
    		if (IsSafe(p, list.get(i))) {
    			p.union(list.get(i).nodes[0], list.get(i).nodes[1]);
    			wg.addEdge(list.get(i));
    			
    			counter++;
    		}
    		
    		
    	}
    	
    	
        /* Fill this method (The statement return null is here only to compile) */
        
        return wg;
    }

    public static Boolean IsSafe(DisjointSets p, Edge e){
    	
    	if (p.find(e.nodes[0]) == p.find(e.nodes[1])) { //2 vertices are in the same group
    		return false;
    	}
    	
    	
    	
        /* Fill this method (The statement return 0 is here only to compile) */
        return true;
    
    }
    /*
    public static void main(String[] args){

        String file = "g1.txt";
        WGraph g = new WGraph(file);
        WGraph t = kruskal(g);
        System.out.println(t);

   } 
   */
}
