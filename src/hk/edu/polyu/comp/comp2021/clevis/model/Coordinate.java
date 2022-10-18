package hk.edu.polyu.comp.comp2021.clevis.model;

import java.text.DecimalFormat;

/**
 * Storing the coordinate
 */
public class Coordinate {

    private static final DecimalFormat df = new DecimalFormat("0.##");
    // the x -coordinate and y-coordinate
    private double cor_x, cor_y;

    /**
     * Constructor of coordinate
     * @param cor_x the x-coordinate
     * @param cor_y the y-coordinate
     */
    public Coordinate(double cor_x, double cor_y){
        this.cor_x = cor_x;
        this.cor_y = cor_y;
    }

    /**
     * print this Coordinate object as (x, y) format
     * @return the formatted string
     */
    @Override
    public String toString(){
        return "("+df.format(cor_x)+", "+df.format(cor_y)+")";
    }

    /**
     * get this x-coordinate
     * @return this x-coordinate
     */
    public double getX(){
        return cor_x;
    }

    /**
     * get this y-coordinate
     * @return this y-coordinate
     */
    public double getY(){
        return cor_y;
    }

    /**
     * set a new x-coordinate to replace current x-coordinate
     * @param cor_x the new x-coordinate
     */
    public void setX(double cor_x){
        this.cor_x = cor_x;
    }

    /**
     * set a new y-coordinate to replace current y-coordinate
     * @param cor_y the new y-coordinate
     */
    public void setY(double cor_y){
        this.cor_y = cor_y;
    }
}
