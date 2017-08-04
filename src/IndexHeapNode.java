/**
 * IndexHeapNode class
 * used to hold point and its original heap index
 */
public class IndexHeapNode {
    private Point point;
    private int index;

    /**
     * Constructor
     * @param p
     * @param i
     */
    public IndexHeapNode(Point p,int i){
        point=p;
        index=i;
    }
    public int getIndex(){
        return index;
    }
    public int getX(){
        return point.getX();
    }
    public int getY(){
        return point.getY();
    }
    public String toString(){
        return "("+point.getX()+","+point.getY()+")";
    }
    public Point getPoint(){
        return point;
    }
}
