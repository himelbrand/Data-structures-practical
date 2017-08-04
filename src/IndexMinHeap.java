public class IndexMinHeap extends IndexHeap {
    /**
     * Constructs an empty binary heap with max size of k
     * @param k
     */
    public IndexMinHeap(int k)
    {
        size = 0;
        heap =new IndexHeapNode[k+1];
    }
    protected void percolatingDown(int k)
    {
        IndexHeapNode tmp = heap[k];
        int child;

        for(; 2*k <= size; k = child)
        {
            child = 2*k;

            if(child != size && (heap[child].getY()>heap[child + 1].getY() ||
                    (heap[child].getY()==heap[child + 1].getY() && heap[child].getX()>heap[child + 1].getX())))
                child++;
            if(tmp.getY()>heap[child].getY() ||
                    (heap[child].getY()==heap[child + 1].getY() && heap[child].getX()>heap[child + 1].getX()))
                heap[k] = heap[child];
            else

                break;
        }
        heap[k] = tmp;
    }
    public void insert(IndexHeapNode x)
    {

        if(size == heap.length - 1) doubleSize();
        //Insert a new item to the end of the array
        int pos = ++size;

        //Percolate up
        for(; pos > 1 && (x.getY()<heap[pos/2].getY() || (x.getY()==heap[pos/2].getY() && x.getX()<heap[pos/2].getX())) ; pos = pos/2 ) {
            heap[pos] = heap[pos / 2];

        }

        heap[pos] = x;

    }

    public String toString()
    {
        String out = "";
        for(int k = 1; k <= size; k++) out += heap[k]+" ";
        return out;
    }
}
