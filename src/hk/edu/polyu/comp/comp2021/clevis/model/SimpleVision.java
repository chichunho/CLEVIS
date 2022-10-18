package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.ArrayList;

/**
 * define the calculations related to the shapes
 */
public interface SimpleVision {
    /**
     * get the leftmost x-coordinate of the shape
     * @return minimum x-coordinate
     */
    double getMinX();
    /**
     * get the bottom y-coordinate of the shape
     * @return minimum y-coordinate
     */
    double getMinY();
    /**
     * get the rightmost x-coordinate of the shape
     * @return maximum x-coordinate
     */
    double getMaxX();
    /**
     * get the top y-coordinate of the shape
     * @return maximum x-coordinate
     */
    double getMaxY();

    /**
     * determine if the point is lying on the shape
     * @param cor_x the point x-coordinate
     * @param cor_y the point y-coordinate
     * @param hitboxDelta the allowed error of the distance from the point and the outline of the shape
     * @return true for lying inside, false for not
     */
    boolean containsPoint(double cor_x, double cor_y, double hitboxDelta);

    /**
     * determine if the shape intersect with certain line segment in otherLines
     * @param otherLines the list of line segments
     * @return true for intersect, false for not
     */
    boolean isIntersectWith(ArrayList<Line> otherLines);
}
