/** Class AVLTree
 * @author omri himelbrand
 * */
class AVLTree
{
    private AVLNode root;
    private int size;
    private int rIndex;
    public int max;
    public int min;


    /**
     * Constructor empty tree
     */
    public AVLTree()
    {
        root=null;
        size=0;

    }//O(1)


    /**
     * Constructor from sorted array
     * @param arr
     */
    public AVLTree(Point[] arr){
        size=arr.length;
        root=sortedArrayToAVL(arr,0,arr.length-1);
    }//O(n)
    private AVLNode sortedArrayToAVL(Point[] arr, int start, int end) {
        int sumOfY[]=sumOfYArray(arr);
        min=arr[0].getX();
        max=arr[arr.length-1].getX();
        if (start > end) {
            return null;
        }
        /* Get the middle element and make it root */
        int mid = (start + end) / 2;
        int leftSize=(mid-1)%2==0?(mid-1)/2:(mid)/2;
        int rightSize=(mid-1)%2==0?leftSize+1:leftSize;
        AVLNode node = new AVLNode(arr[mid],sumOfY[mid],size);
        /* Recursively construct the left subtree and make it
         left child of root */
        node.left = sortedArrayToAVL(arr, start, mid - 1);
        if(node.left!=null)
            node.left.parent=node;
        /* Recursively construct the right subtree and make it
         right child of root */
        node.right = sortedArrayToAVL(arr, mid + 1, end);
        if(node.right!=null)
            node.right.parent=node;
        if(node.left!=null)
            node.sumOfY+=node.left.data.getY()+node.left.sumOfY;
        sizeUpdate(node);
        sumUpdate(node);
        heightUpdate(node);
        return node;
    }//O(n)
    /*
     * creates an array of sums of y up to each index in original array
     */
    private int[] sumOfYArray(Point[] arr){
        int ans[]=new int[arr.length];
        int sum=0;
        for (int i = 0; i < arr.length; i++) {
            sum+=arr[i].getY();
            ans[i]=sum;

        }
        return ans;
    }//O(n)

    /**
     * Getter
     * @return size of AVL tree
     */
    public int getSize(){
        return size;
    }//O(1)



    /**
     *  Function to check if tree is empty
     */
    public boolean isEmpty()
    {
        return root==null;
    }//O(1)

    /**
     * Make the tree logically empty
     */
    public void makeEmpty()
    {
        root=null;
    }//O(1)

    /** Function to insert data */
    public void insert(Point p)
    {
        size++;
        root = insert(p,root,0);
    }//O(logn)
    private void sumUpdate(AVLNode t){
        if(t!=null) {
            int left = t.left == null ? 0 : t.left.sumOfY;
            int right = t.right == null ? 0 : t.right.sumOfY;
            t.sumOfY = left + right +t.data.getY();
        }
    }
    private void sizeUpdate(AVLNode t){
        if(t!=null) {
            int left = t.left == null ? 0 : t.left.size;
            int right = t.right == null ? 0 : t.right.size;
            t.size = left + right + 1;
        }
    }//O(1)
    private void heightUpdate(AVLNode t){
        t.height=max(height(t.left),height(t.right))+1;
    }//O(1)
    /* Function to get height of node */
    private int height(AVLNode t )
    {
        return t==null?-1:t.height;
    }//O(1)
    /* Function to max of left/right node */
    private int max(int lhs, int rhs)
    {
        return lhs>rhs?lhs:rhs;
    }//O(1)
    /* Function to insert data recursively */
    private AVLNode insert(Point p, AVLNode t,int sumOfY)
    {
            if(p.getX()<min)
                min=p.getX();
            if(p.getX()>max)
                max=p.getX();
            if (t == null)
                t = new AVLNode(p,sumOfY,size);
            else if (p.getX() < t.key) {
                if(t.left==null)
                    t.left = insert(p, t.left , sumOfY+p.getY());
                else
                    t.left = insert(p, t.left , sumOfY);
                if (height(t.left) - height(t.right) == 2)
                    if (p.getX()< t.left.key)
                        t = rotateWithLeftChild(t);
                    else
                        t = doubleWithLeftChild(t);
            } else if (p.getX() > t.key) {
                if(t.right==null)
                    t.right = insert(p,t.right,t.sumOfY+p.getY());
                else
                    t.right = insert(p, t.right,sumOfY);
                if (height(t.right) - height(t.left) == 2)
                    if (p.getX() > t.right.key)
                        t = rotateWithRightChild(t);
                    else
                        t = doubleWithRightChild(t);
            }

            if(t.left!=null)
                t.left.parent=t;
            if(t.right!=null)
                t.right.parent=t;
            sizeUpdate(t);
            sumUpdate(t);
            heightUpdate(t);
            return t;
    }//O(logn)

    /**
     * Function to remove certain node
     * @param med
     */
    public void delete(Point med) {
        root = delete(root, med,med.getX());
        size--;
    }//O(logn)

    private AVLNode delete(AVLNode n, Point med,int key) {
        int x=med.getX();



        // Base case: key doesn't exist.
        if (n == null) return null;
        // If it's in the left sub-tree, go left.
        if (x < n.key) {
            n.left=delete(n.left, med,key);
            sizeUpdate(n);
            sumUpdate(n);
            heightUpdate(n);
            return n; // Deleting may have unbalanced tree.
        }
        // If it's in the right sub-tree, go right.
        else if (x>n.key) {
            n.right=delete(n.right, med,key);
            sizeUpdate(n);
            sumUpdate(n);
            heightUpdate(n);
            return n; // Deleting may have unbalanced tree.
        }
        // Else, we found it! Remove n.
        else {
            // 0 children
            if (n.left == null && n.right == null)
                return null;
            // 1 child - guaranteed to be balanced.
            if (n.left == null) {
                n.right.parent=n.parent;
                return n.right;
            }
            if (n.right == null){
                n.left.parent=n.parent;
                return n.left;
            }
            // 2 children - deleting may have unbalanced tree.
            Point smallest = smallest(n.right);
            int smallestKey=smallest.getX();
            n.data=smallest;
            n.key=smallestKey;
            n.right=delete(n.right, smallest ,smallestKey);
            sizeUpdate(n);
            sumUpdate(n);
            heightUpdate(n);
            n.key=n.data.getX();
            return n;
        }
    }//O(logn)
    private Point smallest(AVLNode n) {
        if (n.left == null)
            return n.data;
        return smallest(n.left);
    }//O(logn)
    /* Rotate binary tree node with left child */
    private AVLNode rotateWithLeftChild(AVLNode k2)
    {
        AVLNode x = k2.left;
        k2.left = x.right;
        x.right = k2;
        x.parent=k2.parent;
        k2.parent=x;
        if(x.left!=null)
            x.left.parent=x;
        sizeUpdate(k2);
        sizeUpdate(x);
        sumUpdate(x);
        sumUpdate(k2);
        heightUpdate(k2);
        heightUpdate(x);

        return x;
    }//O(1)

    /* Rotate binary tree node with right child */
    private AVLNode rotateWithRightChild(AVLNode k1)
    {
        AVLNode x = k1.right;
        k1.right = x.left;
        x.left = k1;
        x.parent=k1.parent;
        if(x.right!=null)
            x.right.parent=x;
        k1.parent=x;
        sizeUpdate(k1);
        sizeUpdate(x);
        sumUpdate(k1);
        sumUpdate(x);
        heightUpdate(k1);
        heightUpdate(x);

        return x;
    }//O(1)
    /*
     * Double rotate binary tree node: first left child
     * with its right child; then node k3 with new left child */
    private AVLNode doubleWithLeftChild(AVLNode k3)
    {
        k3.left=rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }//O(1)
    /*
     * Double rotate binary tree node: first right child
     * with its left child; then node k1 with new right child */
    private AVLNode doubleWithRightChild(AVLNode k1)
    {
        k1.right=rotateWithLeftChild(k1.right);
        return rotateWithRightChild(k1);
    }//O(1)
    /** Functions to search for an element by key
     * @param x
     * @return true if found else false
     * */
    public boolean search(int x)
    {
        return search(root,x);
    }//O(logn)
    private boolean search(AVLNode r, int x)
    {
        if(r==null)
            return false;
        else if(x>r.data.getX())
            return search(r.right,x);
        else if(x<r.data.getX())
            return search(r.left,x);
        else
            return true;
    }//O(logn)

    /**
     * Functions to search for an element by key
     * @param x
     * @return Pointer to the node holding the element
     */
    public AVLNode getNodeByX(int x) {
        AVLNode node=root;
        boolean found=false;
        while (!found){
            if(node.key>x && node.left!=null)
                node=node.left;
            else if (node.key<x && node.right!=null)
                node=node.right;
            else
                found=true;
        }
        return node;
    }//O(logn)

    /**
     * Functions to compute the position of an element by key in the sorted array of the elements
     * @param x
     * @return The computed position
     */
    public int getPos(int x){
        AVLNode node=getNodeByX(x);
        int pos =node.left==null? 0 : node.left.size;
        while(node!=root) {

            if (node == node.parent.right)
                pos += node.parent.left == null ? 1 : node.parent.left.size + 1;

            node=node.parent;
        }
        return pos;

    }//O(logn)

    /**
     * Functions to compute the sum of y values up to the element with key x
     * @param x
     * @return The computed sum
     */
    public int getSum(int x){
        AVLNode node=getNodeByX(x);

        int sum =node.left==null? node.data.getY(): node.left.sumOfY+node.data.getY();
        while(node!=root) {
            if (node == node.parent.right)
                sum += node.parent.left == null ? node.parent.data.getY() : node.parent.left.sumOfY+node.parent.data.getY();

            node=node.parent;
        }
        return sum;
    }

    /**
     * Functions to find the key before the key x which is not in the tree
     * @param x
     * @return The closest key from below of key x in the tree
     */
    public int getCloseX(int x){
        AVLNode node=root;
        boolean found=false;
        while (!found){
            if(node.key>x && node.left!=null)
                node=node.left;
            else if (node.key<x && node.right!=null)
                node=node.right;
            else
                found=true;
        }
        return node.data.getX();
    }//O(logn)
    /**
     * Functions to find the key after the key x which is not in the tree
     * @param x
     * @return The closest key from above of key x in the tree
     */
    public int getUpX(int x){
        AVLNode node=root;
        boolean found=false;
        while (!found){
            if(node.key>x && node.left!=null)
                node=node.left;
            else if (node.key<x && node.right!=null)
                node=node.right;
            else
                found=true;
        }
        return node.parent.data.getX();
    }//O(logn)

    /**
     * Getter
     * @param x
     * @return The y value of the the element with key x
     */
    public int getYByX(int x){
        if(search(x))
        return getYbyX(root,x);
        else
            return getYbyX(root,getCloseX(x));
    }//O(logn)
    private int getYbyX(AVLNode n,int x){
        if(n==null)
            return -1;
        else if(x>n.data.getX()) {
            return getYbyX(n.right, x);
        }
        else if(x<n.data.getX()) {
            return getYbyX(n.left, x);
        }
        else
            return n.data.getY();
    }//O(logn)

    /**
     * Functions to get all the elements between the keys given
     * @param XLeft
     * @param XRight
     * @param range
     * @return Array of points between XLeft and XRight inclusive
     */
    public Point[] getPointsInRange(int XLeft, int XRight,int range){
        if(range<=0)
            return new Point[0];
        Point[] ans=new Point[range];
        rIndex=0;
        return getPointsInRange(root,XLeft,XRight,ans);
    }//O(logn+k)
    private Point[] getPointsInRange(AVLNode n,int XLeft,int XRight,Point[] ans){
        if(n==null || rIndex==ans.length)
            return ans;
        if(n.key>=XLeft && n.key<=XRight) {
            ans[rIndex] = n.data;
            rIndex++;
        }
        if(n.key<XRight && n.right!=null)
            getPointsInRange(n.right,XLeft,XRight,ans);
        if(n.key>XLeft && n.left!=null)
            getPointsInRange(n.left,XLeft,XRight,ans);

        return ans;

    }//O(logn+k)




}
