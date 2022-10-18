package hk.edu.polyu.comp.comp2021.clevis.model;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class RectangleTest {
    public static float DELTA = 1E-6f;
    ArrayList<String> myRec1Prop;
    Rectangle myRec1;
    Line line1, line2;


    @Before
    public void init(){
        // rectangle properties list
        myRec1Prop = new ArrayList<>(List.of("myRec1", "0", "0", "4", "3"));
        // rectangle set
        myRec1 = new Rectangle(myRec1Prop);
        // intersect line
        line1 = new Line("line1", 2, -1, 5, 5);
        // non-intersect line
        line2 = new Line("line2", 9, 9, 12, 12);

    }

    @Test
    public void rectangle_toStringTest(){
        assertEquals(myRec1.toString(), "Rectangle myRec1: Top-left coordinate=(0, 0), width=4, height=3");
    }

    @Test
    public void rectangle_getPropTest(){
        double[] prop = myRec1.getProp();
        assertTrue(prop[0] - 0 < DELTA);
        assertTrue(prop[1] - 0 < DELTA);
        assertTrue(prop[2] - 4 < DELTA);
        assertTrue(prop[3] - 3 < DELTA);
    }

    @Test
    public void rectangle_toSimpleStringTest(){
        assertEquals(myRec1.toSimpleString(), "Rectangle myRec1");
    }

    @Test
    public void rectangle_MinMaxTest(){
        assertTrue(myRec1.getMinX() - 0 < DELTA);
        assertTrue(myRec1.getMaxX() - 4 < DELTA);
        assertTrue(myRec1.getMinY() - (-3) < DELTA);
        assertTrue(myRec1.getMaxY() - 0 < DELTA);
    }

    @Test
    public void rectangle_containsPointTest(){
        assertTrue(myRec1.containsPoint(0, 0, 0));
        assertFalse(myRec1.containsPoint(5, 5, 0));
    }

    @Test
    public void rectangle_moveTest(){
        myRec1.move(2, 2);
        double [] prop = myRec1.getProp();
        assertTrue(prop[0] - 2 < DELTA);
        assertTrue(prop[1] - 2 < DELTA);
    }

    @Test
    public void rectangle_isIntersectWithTest(){
        assertTrue(myRec1.isIntersectWith(line1.intoLines()));
        assertFalse(myRec1.isIntersectWith(line2.intoLines()));
    }

    // intoLines will not be tested here,
    // it will be tested in ClevisTest as a part of other methods
}
