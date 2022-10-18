package hk.edu.polyu.comp.comp2021.clevis.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SquareTest {
    public static float DELTA = 1E-6f;
    ArrayList<String> mySqu1Prop;
    Square mySqu1;
    Line line1, line2;


    @Before
    public void init(){
        // rectangle properties list
        mySqu1Prop = new ArrayList<>(List.of("mySqu1", "0", "0", "4"));
        // rectangle set
        mySqu1 = new Square(mySqu1Prop);
        // intersect line
        line1 = new Line("line1", 2, -1, 5, 5);
        // non-intersect line
        line2 = new Line("line2", 9, 9, 12, 12);

    }

    @Test
    public void rectangle_toStringTest(){
        assertEquals(mySqu1.toString(), "Square mySqu1: Top-left coordinate=(0, 0), Length=4");
    }

    @Test
    public void rectangle_getPropTest(){
        double[] prop = mySqu1.getProp();
        assertTrue(prop[0] - 0 < DELTA);
        assertTrue(prop[1] - 0 < DELTA);
        assertTrue(prop[2] - 4 < DELTA);
    }

    @Test
    public void rectangle_toSimpleStringTest(){
        assertEquals(mySqu1.toSimpleString(), "Square mySqu1");
    }

    @Test
    public void rectangle_MinMaxTest(){
        assertTrue(mySqu1.getMinX() - 0 < DELTA);
        assertTrue(mySqu1.getMaxX() - 4 < DELTA);
        assertTrue(mySqu1.getMinY() - (-4) < DELTA);
        assertTrue(mySqu1.getMaxY() - 0 < DELTA);
    }

    @Test
    public void rectangle_containsPointTest(){
        assertTrue(mySqu1.containsPoint(0, 0, 0));
        assertFalse(mySqu1.containsPoint(5, 5, 0));
    }

    @Test
    public void rectangle_moveTest(){
        mySqu1.move(2, 2);
        double [] prop = mySqu1.getProp();
        assertTrue(prop[0] - 2 < DELTA);
        assertTrue(prop[1] - 2 < DELTA);
    }

    @Test
    public void rectangle_isIntersectWithTest(){
        assertTrue(mySqu1.isIntersectWith(line1.intoLines()));
        assertFalse(mySqu1.isIntersectWith(line2.intoLines()));
    }

    // intoLines will not be tested here,
    // it will be tested in ClevisTest as a part of other methods
}
