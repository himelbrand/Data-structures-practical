/** Class AVLNode
 * @author omri himelbrand
 * */
class AVLNode
{
    AVLNode left, right,parent;
    Point data;
    int height;
    int sumOfY;
    int size;
    int key;

    /** Constructor empty node */
    public AVLNode()
    {
        parent=null;
        left = null;
        right = null;
        data = null;
        height = 0;
        sumOfY = 0;
        key=-1;
        size=0;
    }

    /**
     * Constructor with data
     * @param p
     * @param sumY
     * @param s
     */
    public AVLNode(Point p,int sumY,int s)
    {
        parent=null;
        left = null;
        right = null;
        data = p;
        height = 0;
        sumOfY = sumY;
        key=p.getX();
        size=s;
    }
}
 