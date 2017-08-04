public abstract class Heap {
    protected int size;            // Number of elements in heap
    protected Point[] heap;

    /**
     * @param n
     * @return The log of n in base 2 times 10
     */
    protected int get10LogOfN(int n){
        return (int)(10*(Math.log(n)/Math.log(2)));
    }

    /**
     * Removes the top element in the heap
     * @return The deleted Element
     * @throws RuntimeException
     */
    public Point deleteTop() throws RuntimeException
    {
        if (size == 0) throw new RuntimeException();
        Point top = heap[1];
        heap[1] = heap[size--];
        percolatingDown(1);
        return top;
    }
    protected void buildHeap()
    {
        for (int k = size/2; k > 0; k--)
        {
            percolatingDown(k);
        }
    }
    /**
     * Doubles the max size of the heap
     */
    protected void doubleSize()
    {
        Point [] old = heap;
        heap = new Point[heap.length * 2];
        for (int i = 1; i < size; i++) {
            heap[i]=old[i];
        }
    }

    /**
     * Percolates down kth element of heap
     * @param k
     */
    protected void percolatingDown(int k){}
    /**
     * Inserts a new item
     */
    public void insert(Point x){}

    /**
     * Returns an array of the k first elements of the sorted heap
     * @param k
     * @return
     */
    public Point[] getFirstK(int k){
        Point[] points = new Point[k];
        return points;
    }

    /**
     * Returns a string of the heap by levels of the representing tree
     * @return
     */
    public String asLevels(){
        String out ="";
        int i=1;
        while(i<=size){
            for(int j=0;j<i && j+i<=size;j++)
                out+=heap[i+j]+" ";
            out+="\n";
            i=i*2;
        }
        return out;
    }
    public String toString()
    {
        String out = "";
        for(int k = 1; k <= size; k++) out += heap[k]+" ";
        return out;
    }


}
