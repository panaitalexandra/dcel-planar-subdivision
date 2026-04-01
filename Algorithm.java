import java.util.*;

public class Algorithm {
    private Point[] a;
    private Edge[] dcel;
    private List sweepLine; //the sweep line
    private List[] sweepLineStates;
    private Object[] slab;
    private double x, y;
    private Edge binarySearchResultEdge;

    public Algorithm(Point[] a, Edge[] dcel) {
        this.a = a;
        this.dcel = dcel;
        Arrays.sort(a);
        swapEndpoints();
        buildAdjacencyLists();
        sweep();
    }

     private void sort() {
         for (int i = 0; i < a.length - 1; ++i)
             for (int j = i + 1; j < a.length; ++j)
                 if (a[i].getY() > a[j].getY()) {
                     Point aux;
                     aux = a[i];
                     a[i] = a[j];
                     a[j] = aux;
                 }
     }

    private void swapEndpoints() {
        for (int i = 0; i < dcel.length; ++i) {
            if (((dcel[i].getV1()).compareTo(dcel[i].getV2())) > 0)
                dcel[i].reverse();
        }
    }

    public void buildAdjacencyLists() {
        for (int i = 0; i < a.length; ++i) {
            TreeSet c = new TreeSet();
            TreeSet b = new TreeSet();
            for (int j = 0; j < dcel.length; ++j) {
                if (((dcel[j].getV1()).compareTo(a[i])) == 0)
                    c.add(dcel[j]);
                if (((dcel[j].getV2()).compareTo(a[i])) == 0)
                    b.add(dcel[j]);
            }
            Iterator it = c.iterator();
            System.out.print("+" + (i + 1) + "\t:");
            while (it.hasNext())
                System.out.print(" " + ((Edge) (it.next())).getId());
            System.out.println();

            a[i].setC(c);
            it = b.iterator();
            System.out.print("-" + (i + 1) + "\t:");
            while (it.hasNext())
                System.out.print(" " + ((Edge) (it.next())).getId());
            System.out.println();
            a[i].setB(b);
        }
    }

    public void sweep() {
        sweepLineStates = new ArrayList[a.length - 1];
        sweepLine = new ArrayList(a[0].getC());
        sweepLineStates[0] = new ArrayList(sweepLine);
        for (int i = 1; i < a.length - 1; ++i) {
            Iterator itb = (a[i].getB()).iterator();
            while (itb.hasNext()) {
                Edge mb = (Edge) (itb.next());
                if (sweepLine.contains(mb)) {
                    int pos = sweepLine.indexOf(mb);
                    sweepLine.remove(pos);
                    Iterator itc = (a[i].getC()).iterator();
                    while (itc.hasNext()) {
                        Edge mc = (Edge) (itc.next());
                        if (!sweepLine.contains(mc)) {
                            sweepLine.add(pos++, mc);
                        }
                    }
                }
            }
            sweepLineStates[i] = new ArrayList(sweepLine);
        }

        System.out.println("---***---...displaying sweep line states...---***---");
        for (int i = 0; i < sweepLineStates.length; ++i) {
            Object[] vAux = sweepLineStates[i].toArray();
            System.out.print(i + "\t- " + vAux.length + "- :\t");
            for (int j = 0; j < vAux.length; ++j)
                System.out.print(((Edge) (vAux[j])).getId() + " ");
            System.out.println();
        }
    }

    public int search(double x, double y) {
        this.x = x;
        this.y = y;
        System.out.println(searchSlab(x, y));
        int result = searchSlab(x, y) - 1;
        if (result > a.length - 2)
            result = a.length - 2;
        if (result < 0)
            return 0;
        slab = sweepLineStates[result].toArray();
        binarySearchEdges(0, slab.length - 1);
        System.out.println(binarySearchResultEdge.getId());

        int F = 0;

        if (calcDeterminant(binarySearchResultEdge.getV1().getX(), binarySearchResultEdge.getV1().getY(), x, y, binarySearchResultEdge.getV2().getX(), binarySearchResultEdge.getV2().getY()) > 0)
            F = binarySearchResultEdge.getF2();
        else
            F = binarySearchResultEdge.getF1();

        return F;
    }

    public void binarySearchEdges(int s, int d) {
        int middle = (s + d) / 2;
        if (s < d) {
            if (calcDeterminant(((Edge) (slab[middle])).getV1().getX(), ((Edge) (slab[middle])).getV1().getY(), x, y, ((Edge) (slab[middle])).getV2().getX(), ((Edge) (slab[middle])).getV2().getY()) > 0)
                binarySearchEdges(middle + 1, d);
            else
                binarySearchEdges(s, middle - 1);
        } else
            binarySearchResultEdge = ((Edge) (slab[middle]));
    }

    public int searchSlab(double x, double y) {
        return ~Arrays.binarySearch(a, new Point(x, y));
    }

    public double calcDeterminant(double x1, double y1, double x2, double y2, double x3,
                                  double y3) {
        return x1 * y2 + x2 * y3 + x3 * y1 - y2 * x3 - y3 * x1 - y1 * x2;
    }

    public Edge[] getDCEL() {
        return dcel;
    }
}