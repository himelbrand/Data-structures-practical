public abstract class IndexHeap {
    protected int size;            // Number of elements in heap
    protected IndexHeapNode[] heap;
    /**
     * Doubles the max size of the heap
     */
    protected void doubleSize()
    {
        IndexHeapNode [] old = heap;
        heap = new IndexHeapNode[heap.length * 2];
        for (int i = 1; i < size; i++) {
            heap[i]=old[i];
        }
    }
    /**
     * Removes the top element in the heap
     * @return The deleted Element
     * @throws RuntimeException
     */
    public IndexHeapNode deleteTop() throws RuntimeException
    {
        if (size == 0) throw new RuntimeException();
        IndexHeapNode top = heap[1];
        heap[1] = heap[size--];
        percolatingDown(1);
        return top;
    }
    /**
     * Percolates down kth element of heap
     * @param k
     */
    protected void percolatingDown(int k){}
    /**
     * Inserts a new item
     */
    public void insert(IndexHeapNode x){}
}
