public class MinHeap extends Heap{
    /**
     * Constructs an empty min binary heap with max size of k
     * @param k
     */
        public MinHeap(int k)
        {
            size = 0;
            heap =new Point[k+1];
        }

        /**
         * Construct the min binary heap given an array of items.
         * @param array
         */
        public MinHeap(Point[] array)
        {
            size = array.length;
            heap = new Point[size+get10LogOfN(array.length)];
            for (int i = 1; i <= size; i++) {
                heap[i]=array[i-1];
            }
            buildHeap();
        }

        protected void percolatingDown(int k)
        {
            Point tmp = heap[k];
            int child;

            for(; 2*k <= size; k = child)
            {
                child = 2*k;

                if(child != size && (heap[child].getY()>heap[child + 1].getY() ||
                        (heap[child].getY()==heap[child + 1].getY() && heap[child].getX()>heap[child + 1].getX())))
                    child++;
                if(tmp.getY()>heap[child].getY() || child!=size &&
                        (heap[child].getY()==heap[child + 1].getY() && heap[child].getX()>heap[child + 1].getX()))
                    heap[k] = heap[child];
                else

                    break;
            }
            heap[k] = tmp;
        }

        /**
         * Inserts a new item
         */
        public void insert(Point x)
        {
            if(size == heap.length - 1) doubleSize();

            int pos = ++size;

            //Percolate up
            for(; pos > 1 && (x.getY()<heap[pos/2].getY() || (x.getY()==heap[pos/2].getY() && x.getX()<heap[pos/2].getX())) ; pos = pos/2 )
                heap[pos] = heap[pos/2];

            heap[pos] = x;
        }


    @Override
    public Point[] getFirstK(int k) {
        Point[] points=new Point[k];
        IndexMinHeap temp=new IndexMinHeap(k);
        temp.insert(new IndexHeapNode(this.heap[1],1));
        IndexHeapNode nh;
        int rootIndex;
        for (int i = 0; i < k; i++) {
            nh=temp.deleteTop();
            rootIndex=nh.getIndex();
            points[i] = this.heap[rootIndex];
            if(i!=k-1) {
                if (2 * rootIndex <= size)
                    temp.insert(new IndexHeapNode(this.heap[2 * rootIndex], 2 * rootIndex));
                if (2 * rootIndex + 1 <= size)
                    temp.insert(new IndexHeapNode(this.heap[2 * rootIndex + 1], 2 * rootIndex + 1));
            }
        }
        return points;
    }
}
