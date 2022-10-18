package hk.edu.polyu.comp.comp2021.clevis.model;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class LineTest {
    public static float DELTA = 1E-6f;
    ArrayList<String> myLine1Prop;
    Line myLine1, myLine2, myLine3, myLine4;

    @Before
    public void init(){
        // Line properties list
        myLine1Prop = new ArrayList<>(List.of("myLine1", "0", "0", "10", "10"));
        // line set
        myLine1 = new Line(myLine1Prop);
        // line that overlap myLine1
        myLine2 = new Line("myLine2", 0, 0, 10, 10);
        // line that intersect myLine1
        myLine3 = new Line("myLine3", 2, 0, 2, 10);
        // line that not intersect myLine1
        myLine4 = new Line("myLine4", -1, -1, -10, -9);
    }

    @Test
    public void line_toStringTest(){
        assertEquals(myLine1.toString(), "Line myLine1: Point 1=(0, 0), Point 2=(10, 10)");
    }

    @Test
    public void line_getPropTest(){
        double[] myLine1Prop = myLine1.getProp();
        assertTrue(myLine1Prop[0] - 0 < DELTA);
        assertTrue(myLine1Prop[1] - 0 < DELTA);
        assertTrue(myLine1Prop[2] - 10 < DELTA);
        assertTrue(myLine1Prop[3] - 10 < DELTA);
    }

    @Test
    public void line_toSimpleStringTest(){
        assertEquals(myLine1.toSimpleString(), "Line myLine1");
    }

    @Test
    public void line_MinMaxTest(){
        assertTrue(myLine1.getMinX() - 0 < DELTA);
        assertTrue(myLine1.getMaxX() - 10 < DELTA);
        assertTrue(myLine1.getMinY() - 0 < DELTA);
        assertTrue(myLine1.getMaxY() - 10 < DELTA);
    }

    @Test
    public void line_containsPointTest(){
        assertTrue(myLine1.containsPoint(1, 1, 0));
        assertFalse(myLine1.containsPoint(11, 11, 0));
    }

    @Test
    public void line_moveTest(){
        myLine1.move(2, 2);
        double[] myLine1Prop = myLine1.getProp();
        assertTrue(myLine1Prop[0] - 2 < DELTA);
        assertTrue(myLine1Prop[1] - 2 < DELTA);
        assertTrue(myLine1Prop[2] - 12 < DELTA);
        assertTrue(myLine1Prop[3] - 12 < DELTA);
    }

    @Test
    public void line_getDistanceTest(){
        assertTrue(myLine1.getDistance() - Math.sqrt(200) < DELTA);
    }

    @Test
    public void line_getSlopeTest(){
        assertTrue(myLine1.getSlope() - 1 < DELTA);
    }

    @Test
    public void line_isIntersectWithTest(){
        assertTrue(myLine1.isIntersectWith(myLine2.intoLines()));
        assertTrue(myLine1.isIntersectWith(myLine3.intoLines()));
        assertFalse(myLine1.isIntersectWith(myLine4.intoLines()));
    }
}
