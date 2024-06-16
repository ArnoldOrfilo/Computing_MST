import java.util.*;

public class MST {

    private static class ComponentTracker{
        private int[]reps;

        public ComponentTracker(int n){
            reps=new int[n];
            for (int i=0;i<n;i++){
                reps[i]=i;
            }
        }

        public int representativeOf(int x){
            if (x>=reps.length){
                System.out.println("Invalid value");// index out of boundary exception
                return -1;
            }

            return reps[x];
        }

        public boolean inSameComponent(int x, int y){
            if (x>=reps.length||y>=reps.length){
                System.out.println("Invalid value");// index out of boundary exception
                return false;
            }

            if (reps[x]==reps[y]){
                return true;
            }

            return false;
        }

        public void mergeComponents(int x, int y){

            int comp_y=representativeOf(y);

            for (int i=0;i<reps.length;i++){
                if (reps[i]==comp_y){
                    reps[i]=reps[x];
                }
            }

        }

        public boolean isDone(){
            for (int i=0;i<reps.length;i++){
                for (int j=i+1;j<reps.length;j++){
                    if (reps[i]!=reps[j]){
                        return false;
                    }
                }
            }
            return true;
        }

    }

    public static double totalEdgeWeight(Graph g){
        double weight=0;

        for (int i=0;i<g.numVertices();i++){
            int [] temp=g.inNeighbours(i);
            for (int j:temp){
                weight+=g.weight(i,j);

            }
        }

        weight/=2;
        return weight;
    }

    public static Graph getRandomGraph(int n, double p){

        Random r=new Random();

        Graph g=new MatrixGraph(n,Graph.UNDIRECTED_GRAPH);
        for (int i=0; i<n-1;i++){
            for (int j=i+1;j<n;j++){
                if (r.nextDouble()<=p){
                    g.addEdge(i,j,r.nextDouble());
                }
            }
        }
        return g;
    }


    public static Graph spanningTree(Graph g){

        int n=g.numVertices;
        Graph m=new MatrixGraph(n,Graph.UNDIRECTED_GRAPH);
        ComponentTracker c=new ComponentTracker(n);
        ArrayList<int[]>edges=new ArrayList<>();

        for (int i=0;i<n;i++){
            for(int j=0;j<n-1;j++){
                if (g.isValidEdge(i,j)){
                    int []temp={i,j};
                    edges.add(temp);
                }
            }
        }
        edges.sort(Comparator.comparingDouble(a -> g.weight(a[0], a[1])));


        for (int[]i:edges){
            if (!m.isEdge(i[0],i[1])){
                if (!c.inSameComponent(i[0],i[1])) {
                    m.addEdge(i[0], i[1], g.weight(i[0], i[1]));
                    c.mergeComponents(i[0], i[1]);
                }
            }

            if (c.isDone()){
                break;
            }

        }

        return m;

    }


    public static void main(String[] args) {

//      5A: Total weight of MST of Graph of Essex
        Graph test1=GraphOfEssex.getGraph();
        Graph m=spanningTree(test1);

        System.out.println("\n5A: Compute and print the total edge weight of the Graph of Essex. Compute its MST and print its weight.");
        System.out.println("The total edge weight: "+totalEdgeWeight(test1));
        System.out.println("The MST total edge weight: "+totalEdgeWeight(m));
        System.out.println("\n");


//      5B: Test random graph generation
        Graph test2= getRandomGraph(100,0.4);
        System.out.println("5B. Generate a random graph with 100 vertices and edge probability 0.4. Compute and print its total edge weight.");
        System.out.println("Total edge weight: "+totalEdgeWeight(test2));
        System.out.println("\n");

      //5C: Compute the total edge weight of the MST of a random graph(the graph generated in 5B)
        System.out.println("5C. Generate 100 random graphs, each with 100 vertices and edge probability 0.4. Compoute and print the average total edge weight of their minimum spanning trees.");
        double sum=0;
        for(int i=0;i<100;i++){
            Graph temp=getRandomGraph(100,0.4);
            Graph m2=spanningTree(temp);
            sum+=totalEdgeWeight(m2);

        }
        System.out.println("The average MST total edge weight:"+sum/100);



    }

}
