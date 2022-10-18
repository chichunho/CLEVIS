package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.ArrayList;

public class Square extends MyShapes{

    private final Coordinate topLeft;
    private final double length;

    public Square(ArrayList<String> args){
        super(args.get(0));
        topLeft = new Coordinate(Double.parseDouble(args.get(1)), Double.parseDouble(args.get(2)));
        length = Double.parseDouble(args.get(3));
    }

    @Override
    public String toString(){
        return "Square"+" "+this.name+ ": "+
                "Top-left coordinate="+topLeft+
                ", Length="+df.format(this.length);
    }

    @Override
    public double[] getProp() {
        double[] prop = new double[3];
        prop[0] = this.topLeft.getX();
        prop[1] = this.topLeft.getY();
        prop[2] = this.length;
        return prop;
    }

    @Override
    public String toSimpleString() {
        return "Square"+" "+this.name;
    }

    @Override
    public double getMinX() {
        return this.topLeft.getX();
    }

    @Override
    public double getMinY() {
        return this.topLeft.getY()-this.length;
    }

    @Override
    public double getMaxX() {
        return this.topLeft.getX()+this.length;
    }

    @Override
    public double getMaxY() {
        return this.topLeft.getY();
    }

    @Override
    public boolean containsPoint(double cor_x, double cor_y, double hitboxDelta) {
        // this line of code is simplified,
        // original code:
        // if (cor_x < this.getMinX())-hitboxDelta ||
        //      cor_x > this.getMaxX()+hitboxDelta ||
        //      cor_y < getMinY()-hitboxDelta ||
        //      cor_y > getMaxY()+hitboxDelta){
        //      return false;
        // else{
        //      return true;
        // }
        return !(cor_x < this.getMinX()-hitboxDelta) && !(cor_x > this.getMaxX()+hitboxDelta) && !(cor_y < this.getMinY()-hitboxDelta) && !(cor_y > this.getMaxY()+hitboxDelta);
    }

    @Override
    public boolean isLinesRepresentable(){
        return true;
    }

    @Override
    public void move(double dx, double dy){
        this.topLeft.setX(this.topLeft.getX()+dx);
        this.topLeft.setY(this.topLeft.getY()+dy);
    }

    @Override
    public ArrayList<Line> intoLines(){
        ArrayList<Line> result = new ArrayList<>();

        result.add(new Line("left", this.getMinX(), this.getMaxY(), this.getMinX(), this.getMinY()));
        result.add(new Line("top", this.getMinX(), this.getMaxY(), this.getMaxX(), this.getMaxY()));
        result.add(new Line("right", this.getMaxX(), this.getMaxY(), this.getMaxX(), this.getMinY()));
        result.add(new Line("bottom", this.getMinX(), this.getMinY(), this.getMinX(), this.getMaxY()));

        return result;
    }

    @Override
    public boolean isIntersectWith(ArrayList<Line> otherLines){
        ArrayList<Line> thisLines = this.intoLines();
        for (Line currentLine : thisLines){
            if (currentLine.isIntersectWith(otherLines)){
                return true;
            }
        }
        return false;
    }
}
