package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.ArrayList;

public class Line extends MyShapes{

    private final Coordinate pt1, pt2;

    public Line(ArrayList<String> args){
        super(args.get(0));
        pt1 = new Coordinate(Double.parseDouble(args.get(1)), Double.parseDouble(args.get(2)));
        pt2 = new Coordinate(Double.parseDouble(args.get(3)), Double.parseDouble(args.get(4)));
    }

    public Line(String name, double cor_x1, double cor_y1, double cor_x2, double cor_y2){
        super(name);
        pt1 = new Coordinate(cor_x1, cor_y1);
        pt2 = new Coordinate(cor_x2, cor_y2);
    }

    @Override
    public String toString(){
        return "Line "+this.name+": "+
                "Point 1="+pt1+
                ", Point 2="+pt2;
    }

    @Override
    public double[] getProp() {
        double[] prop = new double[4];
        prop[0] = pt1.getX();
        prop[1] = pt1.getY();
        prop[2] = pt2.getX();
        prop[3] = pt2.getY();
        return prop;
    }

    @Override
    public String toSimpleString() {
        return "Line"+" "+this.name;
    }

    @Override
    public double getMinX() {
        return Math.min(this.pt1.getX(), this.pt2.getX());
    }

    @Override
    public double getMinY() {
        return Math.min(this.pt1.getY(), this.pt2.getY());
    }

    @Override
    public double getMaxX() {
        return Math.max(this.pt1.getX(), this.pt2.getX());
    }

    @Override
    public double getMaxY() {
        return Math.max(this.pt1.getY(), this.pt2.getY());
    }

    @Override
    public boolean containsPoint(double cor_x, double cor_y, double hitboxDelta) {
        // if the testing cor_x and cor_y is at either one of the end points
        // then it must lie on the line segment
        if ((cor_x == pt1.getX() && cor_y == pt1.getY())||
                (cor_x == pt2.getX() && cor_y == pt2.getY())){
            return true;
        }
        double m1 = (this.pt2.getY()-this.pt1.getY())/(this.pt2.getX()-this.pt1.getX());
        double m2 = (cor_y-this.pt1.getY())/(cor_x-this.pt1.getX());
        // if the slope is not the same
        // it must not lie on the line segment
        if (m1 != m2){
            return false;
        }
        // this line of code is simplified,
        // original code:
        // if (cor_x < this.getMinX()) ||
        //      cor_x > this.getMaxX() ||
        //      cor_y < getMinY() ||
        //      cor_y > getMaxY()){
        //      return false;
        // else{
        //      return true;
        // }
        return !(cor_x < this.getMinX()) && !(cor_x > this.getMaxX()) && !(cor_y < this.getMinY()) && !(cor_y > this.getMaxY());
    }

    @Override
    public void move(double dx, double dy){
        this.pt1.setX(this.pt1.getX()+dx);
        this.pt1.setY(this.pt1.getY()+dy);
        this.pt2.setX(this.pt2.getX()+dx);
        this.pt2.setY(this.pt2.getY()+dy);
    }

    public double getDistance(){
        // sqrt((x2-x1)^2+(y2-y1)^2)
        return Math.sqrt(Math.pow(pt2.getX()-pt1.getX(), 2)+Math.pow(pt2.getY()-pt1.getY(), 2));
    }

    @Override
    public boolean isLinesRepresentable(){
        return true;
    }

    /**
     * get the slope of this Line
     * @return the required slope
     */
    public double getSlope(){
        // (y2-y1)/(x2-x1)
        return (this.pt2.getY()-this.pt1.getY())/(this.pt2.getX()-this.pt1.getX());
    }

    /**
     * assume the line segment is extended, then
     * calculate the possible intersection point
     * @param other the other Line object
     * @return the possible common point index
     */
    private Coordinate getPossibleCommonPoint(Line other){
        double[] otherProp = other.getProp();
        Coordinate otherPt1 = new Coordinate(otherProp[0], otherProp[1]);
        double x, y;
        // extend the line segment to -inf and +inf
        // line equation of this line is -> y=m1(x-x11)+y11
        // line equation of other line is -> y=m2(x-x21)+y21
        // combine these two line equation so that -> m1(x-x11)+y11=m2(x-x21)+y21
        // from above, x=(m1*x11-y11-m2*x21+y21)/(m1-m2)
        // substitute back to line equation
        // also handle any vertical line that slope is undefined (produce slope=infinity)
        if (Double.isInfinite(this.getSlope())){
            x = this.pt1.getX();
            y = other.getSlope()*(x-otherProp[0])+otherProp[1];
        }
        else if (Double.isInfinite(other.getSlope())){
            x = otherProp[0];
            y = this.getSlope()*(x-this.pt1.getX())+this.pt1.getY();
        }
        else{
            x = (this.getSlope()*this.pt1.getX()-this.pt1.getY()-other.getSlope()*otherPt1.getX()+otherPt1.getY())/
                    (this.getSlope()-other.getSlope());
            y = this.getSlope()*(x-this.pt1.getX())+this.pt1.getY();
        }
        return new Coordinate(x, y);
    }

    @Override
    public ArrayList<Line> intoLines(){
        ArrayList<Line> result = new ArrayList<>();
        // since itself is already a Line
        result.add(this);
        return result;
    }

    @Override
    public boolean isIntersectWith(ArrayList<Line> otherLines){
        Coordinate possibleCommonPoint;
        double thisSlope = this.getSlope();
        double otherSlope;
        for (Line currentLine : otherLines){
            otherSlope = currentLine.getSlope();
            // if the slope of these two lines are not the same, then there must be a possible intersection point
            if (thisSlope != otherSlope){
                possibleCommonPoint = this.getPossibleCommonPoint(currentLine);
                // if the possible intersection point is lies on both lines segment, then they are intersected
                if (this.containsPoint(possibleCommonPoint.getX(), possibleCommonPoint.getY(), 0) &&
                        currentLine.containsPoint(possibleCommonPoint.getX(), possibleCommonPoint.getY(), 0)){
                    return true;
                }
            }
            // else they may have inf. intersection point or 0
            // if they are overlapped, then inf., else 0
            else{
                return currentLine.containsPoint(this.pt1.getX(), this.pt1.getY(), 0) ||
                        currentLine.containsPoint(this.pt2.getX(), this.pt2.getY(), 0);
            }
        }
        return false;
    }
}
