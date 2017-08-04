import sun.awt.X11.XlibUtil;
import sun.awt.image.PixelConverter;

public class PointDataStructure implements PDT {
	AVLTree AVLByX;
	MaxHeap maxHeap;
	MinHeap minHeap;
	Point medianPoint;

	//////////////// DON'T DELETE THIS CONSTRUCTOR ////////////////
	public PointDataStructure(Point[] points, Point initialYMedianPoint)
	{
		int n=points.length;
		medianPoint=initialYMedianPoint;
		Point[] topPoints;
		Point[] bottomPoints=new Point[n/2];
		if(n%2==0)
			topPoints=new Point[n/2];
		else
			topPoints=new Point[n/2+1];
		int topIndex=0;
		int bottomIndex=0;
		for (int i = 0; i < n; i++) {
			if(comparePoints(points[i],initialYMedianPoint)>0){
				topPoints[topIndex]=new Point(points[i]);
				topIndex++;
			}
			else if(comparePoints(points[i],initialYMedianPoint)<0){
				bottomPoints[bottomIndex]=new Point(points[i]);
				bottomIndex++;
			}
			else{
				topPoints[topIndex]=new Point(points[i]);
				topIndex++;
			}
		}
		if(isSortedByX(points))
			AVLByX=new AVLTree(points);
		else
			AVLByX=new AVLTree(sortByX(points));
		minHeap=new MinHeap(topPoints);
		maxHeap=new MaxHeap(bottomPoints);

	}

	@Override
	public void addPoint(Point point) {
		if(comparePoints(point,medianPoint)!=0) {
			AVLByX.insert(point);
			Point temp;
			int size=AVLByX.getSize();
			if (comparePoints(point, medianPoint) < 0) {
				maxHeap.insert(point);
				if(size%2!=0) {
					temp = maxHeap.deleteTop();
					medianPoint = temp;
					minHeap.insert(temp);
				}
			} else {
				minHeap.insert(point);
				if(size%2==0) {
					temp = minHeap.deleteTop();
					medianPoint = minHeap.heap[1];
					maxHeap.insert(temp);
				}
			}
		}
		
	}

	@Override
	public Point[] getPointsInRange(int XLeft, int XRight) {
		int range=numOfPointsInRange(XLeft,XRight);
		return AVLByX.getPointsInRange(XLeft,XRight,range);
	}
	@Override
	public int numOfPointsInRange(int XLeft, int XRight) {
		if(XLeft>AVLByX.max || XRight<AVLByX.min)
			return 0;
		if(XLeft<AVLByX.min)
			XLeft=AVLByX.min;
		if(XRight>AVLByX.max)
			XRight=AVLByX.max;
		if(XRight==AVLByX.max && XLeft==AVLByX.min)
			return AVLByX.getSize();
		if(!AVLByX.search(XLeft) && !AVLByX.search(XRight))
			return 0;
		if(!AVLByX.search(XLeft))
			XLeft=AVLByX.getUpX(XLeft);
		if(!AVLByX.search(XRight))
			XRight=AVLByX.getCloseX(XRight);
		if(XRight-XLeft==1)
			return 2;
		int left=XLeft==AVLByX.min?0:AVLByX.getPos(XLeft);
		int right=XRight==AVLByX.max?AVLByX.getSize()-1:AVLByX.getPos(XRight);

		if(XLeft==XRight || right==left)
			return 1;


		return Math.abs(right-left)+1;
	}

	@Override
	public double averageHeightInRange(int XLeft, int XRight) {
		double pointsInRange=numOfPointsInRange(XLeft, XRight);
		if(pointsInRange==0)
			return 0;
		if(pointsInRange==1)
			return AVLByX.getYByX(XLeft);
		if(pointsInRange==2)
			return (AVLByX.getYByX(XLeft)+AVLByX.getYByX(XRight))/pointsInRange;
		if(XLeft<AVLByX.min)
			XLeft=AVLByX.min;
		if(XRight>AVLByX.max)
			XRight=AVLByX.max;
		if(!AVLByX.search(XLeft))
			XLeft=AVLByX.getUpX(XLeft);
		if(!AVLByX.search(XRight))
			XRight=AVLByX.getCloseX(XRight);
		double left=AVLByX.getSum(XLeft);
		double right=AVLByX.getSum(XRight);


			return (Math.abs(right-left)+AVLByX.getYByX(XLeft))/pointsInRange;

	}

	@Override
	public void removeMedianPoint() {
		AVLByX.delete(medianPoint);
		minHeap.deleteTop();
		int size=AVLByX.getSize();
		if(size%2!=0) {
			Point temp = maxHeap.deleteTop();
			minHeap.insert(temp);
		}

			medianPoint=minHeap.heap[1];


	}

	@Override
	public Point[] getMedianPoints(int k) {
		Point[] ans=new Point[k];
		double x=k;
		Point[] fromMin;
		Point[] fromMax;
		if(k==1)
			ans[0]=medianPoint;
		else if (k>1){
			if(k%2!=0) {
				fromMin = maxHeap.getFirstK((int) Math.floor((k - 1) / 2));
				fromMax = minHeap.getFirstK((int) Math.ceil((k - 1) / 2)+1);
			}
			else{
				fromMin = maxHeap.getFirstK(k/2);
				fromMax = minHeap.getFirstK(k/2);
			}
			for (int i = 0; i < fromMin.length; i++) {
				ans[i]=fromMin[i];
			}
			for (int i = 0; i < fromMax.length; i++) {
				ans[fromMin.length+i]=fromMax[i];
			}
		}
		return ans;
	}

	@Override
	public Point[] getAllPoints() {
		Point[] ans=new Point[AVLByX.getSize()];
		for (int i = 1; i <= maxHeap.size; i++) {
			ans[i-1]=maxHeap.heap[i];
		}
		for (int i = 1; i <= minHeap.size; i++) {
			ans[maxHeap.size+i-1]=minHeap.heap[i];
		}
		return ans;
	}

	public boolean isSortedByX(Point[] points){
		for (int i=1;i<points.length;i++)
			if(points[i].getX()<points[i-1].getX())
				return false;
		return true;
	}

	public Point[] sortByX(Point[] points){
		Point[] ans=new Point[points.length];
		for (int i = 0; i < points.length; i++) {
			ans[points[i].getX()]=points[i];
		}
		return ans;

	}
	private int comparePoints(Point point1,Point point2){
		if(point1.getY()<point2.getY())
			return -1;
		if(point1.getY()>point2.getY())
			return 1;
		if(point1.getX()<point2.getX())
			return -1;
		if(point1.getX()>point2.getX())
			return 1;
		else
			return 0;
	}


}

