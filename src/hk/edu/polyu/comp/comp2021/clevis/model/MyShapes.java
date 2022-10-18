package hk.edu.polyu.comp.comp2021.clevis.model;

import java.text.DecimalFormat;

/**
 * abstract class for defining MyShapes
 */
public abstract class MyShapes implements SimpleVision, LinesRepresentation {

    protected static final DecimalFormat df = new DecimalFormat("0.##");
    // shape name
    protected String name;

    /**
     * constructor of MyShapes
     * @param name the shape name
     */
    public MyShapes(String name){
        this.name = name;
    }

    // abstract method to be implemented in the subclass

    /**
     * get the shape properties
     * @return the double array storing the properties
     */
    abstract double[] getProp();

    /**
     * convert this shape to a simple string that tells the shape category and name
     * @return the formatted string
     */
    abstract String toSimpleString();

    /**
     * move this shape with given dx and dy
     * @param dx the moving distance of x-coordinate
     * @param dy the moving distance of y-coordinate
     */
    abstract void move(double dx, double dy);
}
