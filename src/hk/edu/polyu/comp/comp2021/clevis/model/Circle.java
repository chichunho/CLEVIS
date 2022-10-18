package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.ArrayList;

public class Circle extends MyShapes{

    private final Coordinate center;
    private final double radius;

    public Circle(ArrayList<String> args){
        super(args.get(0));
        center = new Coordinate(Double.parseDouble(args.get(1)), Double.parseDouble(args.get(2)));
        radius = Float.parseFloat(args.get(3));
    }

    @Override
    public String toString(){
        return "Circle"+" "+name+": "+
                "Center coordinate="+center+
                ", radius="+df.format(this.radius);
    }

    @Override
    public double[] getProp() {
        double[] prop = new double[3];
        prop[0] = this.center.getX();
        prop[1] = this.center.getY();
        prop[2] = this.radius;
        return prop;
    }

    @Override
    public String toSimpleString() {
        return "Circle"+" "+this.name;
    }

    @Override
    public double getMinX() {
        return this.center.getX()-this.radius;
    }

    @Override
    public double getMinY() {
        return this.center.getY()-this.radius;
    }

    @Override
    public double getMaxX() {
        return this.center.getX()+this.radius;
    }

    @Override
    public double getMaxY() {
        return this.center.getY()+this.radius;
    }

    /**
     * determine if the point touches the circle
     * @param cor_x the x-coordinate of the point
     * @param cor_y the y-coordinate of the point
     * @return true for touched, false for not
     */
    private boolean isTouch(double cor_x, double cor_y){
        return Math.pow(cor_x-this.center.getX(), 2)+Math.pow(cor_y-this.center.getY(), 2)-Math.pow(this.radius, 2) == 0;
    }

    /**
     * determine if the point lies inside the circle
     * @param cor_x the x-coordinate of the point
     * @param cor_y the y-coordinate of the point
     * @param hitboxDelta the allowed error between the point and the border of the shape
     * @return true for lying inside, false for not
     */
    private boolean isInside(double cor_x, double cor_y, double hitboxDelta){
        return Math.pow(cor_x-this.center.getX(), 2)+Math.pow(cor_y-this.center.getY(), 2)-Math.pow(this.radius+hitboxDelta, 2) < 0;
    }

    @Override
    public boolean containsPoint(double cor_x, double cor_y, double hitboxDelta) {
        return isTouch(cor_x, cor_y) || isInside(cor_x, cor_y, hitboxDelta);
    }

    @Override
    public boolean isLinesRepresentable(){
        return false;
    }

    @Override
    public void move(double dx, double dy){
        this.center.setX(this.center.getX()+dx);
        this.center.setY(this.center.getY()+dy);
    }

    @Override
    public ArrayList<Line> intoLines(){
        return null;
    }

    @Override
    public boolean isIntersectWith(ArrayList<Line> otherLines) {
        double m, x1, y1, x2, y2, x, y;
        double[] lineProp;
        Line temp;
        for (Line currentLine : otherLines) {
            m = currentLine.getSlope();
            lineProp = currentLine.getProp();
            x1 = lineProp[0];
            y1 = lineProp[1];
            x2 = lineProp[2];
            y2 = lineProp[3];
            // if either one end point touches the circle, then it must intersect the circle
            if (this.isTouch(x1, y1) || this.isTouch(x2, y2)) {
                return true;
            }
            // if 2 points inside the circle, then it must not intersect the circle
            if (this.isInside(x1, y1, 0) && this.isInside(x2, y2, 0)) {
                return false;
            }
            // if 1 point inside the circle and another point is not, then it must intersect the circle
            if ((this.isInside(x1, y1, 0) && !this.isInside(x2, y2, 0)) ||
                    this.isInside(x2, y2, 0) && !this.isInside(x1, y1, 0)) {
                return true;
            }
            // test for tangent to or overlap the circle
            // construct the perpendicular line which intersect the center and the testing line
            // if the distance of intersection point and center is <= radius and
            // the intersection point lies on the testing line,
            // then it must intersect the circle
            // the testing line equation is -> y=m(x-x1)+y1
            // the perpendicular line equation can be obtained from x=Cx+m*Cy-m*y
            // from above, x=(Cx+m*Cy-m*y1+(m^2)*x1)/(1+(m^2))
            // substitute back to testing line equation
            // handle for any vertical line (Slope = infinity)
            if (Double.isInfinite(m)){
                x = currentLine.getProp()[0];
                y = this.center.getY();
            }
            else{
                x = (this.center.getX() + m * this.center.getY() - m * y1 + Math.pow(m, 2) * x1) /
                        (1 + Math.pow(m, 2));
                y = m * (x - x1) + y1;
            }
            temp = new Line("temp", this.center.getX(), this.center.getY(), x, y);
            if (temp.getDistance() <= this.radius && currentLine.containsPoint(x, y, 0)) {
                return true;
            }
        }
        return false;
    }

}
