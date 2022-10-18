package hk.edu.polyu.comp.comp2021.clevis.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CircleTest {
    public static float DELTA = 1E-6f;
    ArrayList<String> myCir1Prop;
    Circle myCir1;
    Line line1, line2, line3, line4, line5;


    @Before
    public void init(){
        // rectangle properties list
        myCir1Prop = new ArrayList<>(List.of("myCir1", "0", "0", "4"));
        // rectangle set
        myCir1 = new Circle(myCir1Prop);
        // line with one point touching the circle
        line1 = new Line("line1", 0, 4, 5, 5);
        // line with 2 point inside the circle
        line2 = new Line("line2", 0, 2, 0, -2);
        // line with one point outside one point inside
        line3 = new Line("line3", 0, 0, 5, 5);
        // line tangent to circle
        line4 = new Line("line4", -5, 4, 5, 4);
        // line overlap the circle
        line5 = new Line("line5", 0, -5, 0, 5);

    }

    @Test
    public void circle_toStringTest(){
        assertEquals(myCir1.toString(), "Circle myCir1: Center coordinate=(0, 0), radius=4");
    }

    @Test
    public void circle_getPropTest(){
        double[] prop = myCir1.getProp();
        assertTrue(prop[0] - 0 < DELTA);
        assertTrue(prop[1] - 0 < DELTA);
        assertTrue(prop[2] - 4 < DELTA);
    }

    @Test
    public void circle_toSimpleStringTest(){
        assertEquals(myCir1.toSimpleString(), "Circle myCir1");
    }

    @Test
    public void circle_MinMaxTest(){
        assertTrue(myCir1.getMinX() - 4 < DELTA);
        assertTrue(myCir1.getMaxX() - 4 < DELTA);
        assertTrue(myCir1.getMinY() - 4 < DELTA);
        assertTrue(myCir1.getMaxY() - 4 < DELTA);
    }

    @Test
    public void circle_containsPointTest(){
        assertTrue(myCir1.containsPoint(0, 0, 0));
        assertFalse(myCir1.containsPoint(5, 5, 0));
    }

    @Test
    public void circle_moveTest(){
        myCir1.move(2, 2);
        double [] prop = myCir1.getProp();
        assertTrue(prop[0] - 2 < DELTA);
        assertTrue(prop[1] - 2 < DELTA);
    }

    @Test
    public void circle_isIntersectWithTest(){
        assertTrue(myCir1.isIntersectWith(line1.intoLines()));
        assertFalse(myCir1.isIntersectWith(line2.intoLines()));
        assertTrue(myCir1.isIntersectWith(line3.intoLines()));
        assertTrue(myCir1.isIntersectWith(line4.intoLines()));
        assertTrue(myCir1.isIntersectWith(line5.intoLines()));
    }
}
