package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.ArrayList;

public interface LinesRepresentation {

    /**
     * determine if the shape can represent by a list of line segments
     * @return true for representable, false for not
     */
    boolean isLinesRepresentable();

    /**
     * convert the shape into a list of Line
     * @return List of Line
     */
    ArrayList<Line> intoLines();
}
