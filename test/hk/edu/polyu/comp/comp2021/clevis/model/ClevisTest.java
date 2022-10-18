package hk.edu.polyu.comp.comp2021.clevis.model;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;
import java.util.ArrayList;


public class ClevisTest {
    String[] path = {"-html","log.html","-txt","log.txt"};
    protected Clevis myclevis;

    @Before
    public void init() {
        myclevis = new Clevis(path[1],path[3]);

        myclevis.run("rectangle rec1 2 3 3 4");
        myclevis.run("rectangle rec2 -3 -5 4 5");
        myclevis.run("square squ1 6 7 2");
        myclevis.run("square squ2 -4 -6 3");
        myclevis.run("circle cir1 -4 5 7");
        myclevis.run("circle cir2 6 -2 4");
        myclevis.run("line line1 -5 -1 2 3");
        myclevis.run("line line2 8 -5 -6 6");

    }

    @Test
    public void test_isValid () {
        Response response;
        response = myclevis.run("rectangl rec1 2 3 3 4");
        assertFalse(response.isAccepted());
        response = myclevis.run("rectangle rec1 2 3 3 -4");
        assertFalse(response.isAccepted());
        response = myclevis.run("rectangle rec3 1 2 3 4 5");
        assertFalse(response.isAccepted());
        response = myclevis.run("rectangle rec4 2 3 3 a");
        assertFalse(response.isAccepted());
        response = myclevis.run("rectangle rec3 3 4 5 6");
        assertTrue(response.isAccepted());
        response = myclevis.run("line line3 2 3 4");
        assertFalse(response.isAccepted());
        response = myclevis.run("line line4 2 3 4 5 6");
        assertFalse(response.isAccepted());
        response = myclevis.run("line line5 a 2 4 5");
        assertFalse(response.isAccepted());
        response = myclevis.run("circle cir1 2 3 4");
        assertFalse(response.isAccepted());
        response = myclevis.run("circle cir1 2 3 4");
        assertFalse(response.isAccepted());
        response = myclevis.run("circle cir6 2 > 5");
        assertFalse(response.isAccepted());
        response = myclevis.run("circle cir4 2 3 -1");
        assertFalse(response.isAccepted());
        response = myclevis.run("move unknownshape 4 5");
        assertFalse(response.isAccepted());
        response = myclevis.run("move squ1 % 2");
        assertFalse(response.isAccepted());
        response = myclevis.run("move rec1 2 3 4");
        assertFalse(response.isAccepted());
        response = myclevis.run("group gp1 rec0 rec0 rec0");
        assertFalse(response.isAccepted());
        response = myclevis.run("group gp2 rec1 rec1 rec2");
        assertFalse(response.isAccepted());
        response = myclevis.run("ungroup testgp4");
        assertFalse(response.isAccepted());
        response = myclevis.run("intersect rec3 rec1");
        assertTrue(response.isAccepted());
        response = myclevis.run("intersect cir1 cir2 line1");
        assertFalse(response.isAccepted());
        response = myclevis.run("boundingbox rec1 rec2"); //
        assertFalse(response.isAccepted());
        response = myclevis.run("list rec10");
        assertFalse(response.isAccepted());
        response = myclevis.run("list rec1 rec2");
        assertFalse(response.isAccepted());
        response = myclevis.run("listAll x");
        assertFalse(response.isAccepted());
        response = myclevis.run("undo last");
        assertFalse(response.isAccepted());
    }

    @Test
    public void test_createShapes () {
        ArrayList<String> myRec = new ArrayList<>();
        myRec.add("rec1"); myRec.add("2");myRec.add("3");myRec.add("3");myRec.add("4");
        ArrayList<String> mySqu = new ArrayList<>();
        mySqu.add("squ1"); mySqu.add("4"); mySqu.add("5"); mySqu.add("6");
        ArrayList<String> myCir = new ArrayList<>();
        myCir.add("cir1"); myCir.add("7");myCir.add("8");myCir.add("4");
        ArrayList<String> myLine = new ArrayList<>();
        myLine.add("line1"); myLine.add("4");myLine.add("5");myLine.add("6");myLine.add("7");

        Rectangle rec1 = new Rectangle(myRec);
        Square squ1 = new Square(mySqu);
        Circle cir1 = new Circle(myCir);
        Line line1 = new Line(myLine);

        assertEquals(2,(int)rec1.getProp()[0]);
        assertEquals(3,(int)rec1.getProp()[1]);
        assertEquals(3,(int)rec1.getProp()[2]);
        assertEquals(4,(int)rec1.getProp()[3]);

        assertEquals(4,(int)squ1.getProp()[0]);
        assertEquals(5,(int)squ1.getProp()[1]);
        assertEquals(6,(int)squ1.getProp()[2]);

        assertEquals(7,(int)cir1.getProp()[0]);
        assertEquals(8,(int)cir1.getProp()[1]);
        assertEquals(4,(int)cir1.getProp()[2]);

        assertEquals(4,(int)line1.getProp()[0]);
        assertEquals(5,(int)line1.getProp()[1]);
        assertEquals(6,(int)line1.getProp()[2]);
        assertEquals(7,(int)line1.getProp()[3]);


    }

    @Test
    public void test_group () {
        Response response;

        response = myclevis.run("group testgp1 rec1 rec2");
        int fin_size = 8 - 2 + 1;  //8 = originally 8 shapes created, 2 = the testgp1 contains 2 shapes, 1 = testgp1
        assertEquals(fin_size,response.getCurrentImageSize());
        response = myclevis.run("group testgp2 cir1 cir2 squ1 squ2");
        int fin_size2 = fin_size - 4 + 1;
        assertEquals(fin_size2,response.getCurrentImageSize());
    }

    @Test
    public void test_ungroup () {
        Response response;

        response = myclevis.run("group testgp1 rec1 rec2 cir1 cir2");
        int expectResult = response.getCurrentImageSize()+4-1;
        response = myclevis.run("ungroup testgp1");
        assertEquals(expectResult,response.getCurrentImageSize());
    }

    @Test
    public void test_delete () {
        Response response;

        response = myclevis.run("delete rec1");  //deleting a single shape
        assertEquals(7,response.getCurrentImageSize());
        response = myclevis.run("group testgp1 cir1 cir2");  //deleting a group
        int expectResult = response.getCurrentImageSize()-1;
        response = myclevis.run("delete testgp1");
        assertEquals(expectResult,response.getCurrentImageSize());
    }

    @Test
    public void test_boundingbox () {
        Response response;
        String responseComponent;

        response = myclevis.run("boundingbox cir2");
        responseComponent = String.join(" ", response.getMessageComponent());
        assertEquals("2.0 2.0 8.0 8.0",responseComponent);



        myclevis.run("group testgp1 rec1 rec2 squ1 cir1");
        response = myclevis.run("boundingbox testgp1");
        responseComponent = String.join(" ", response.getMessageComponent());
        assertEquals("-11.0 12.0 19.0 22.0",responseComponent);
    }

    @Test
    public void test_move () {
        Response response;
        String responseComponent;

        response = myclevis.run("move rec1 3 3");
        responseComponent = String.join(" ", response.getMessageComponent());
        assertEquals("rec1 (5, 6)",responseComponent);

        myclevis.run("group testgp1 rec2 cir1 cir2");
        response = myclevis.run("move testgp1 2 3");
        responseComponent = String.join(" ", response.getMessageComponent());
        assertEquals("cir2 (8, 1) cir1 (-2, 8) rec2 (-1, -2)",responseComponent);
    }

    @Test
    public void test_picknmove () {
        Response response;
        String responseComponent;

        response = myclevis.run("pick-and-move 4 0 3 4");
        responseComponent = String.join(" ", response.getMessageComponent());
        assertEquals("cir2 cir2 (9, 2)",responseComponent);

        response = myclevis.run("pick-and-move 7 6 -2 2");
        responseComponent = String.join(" ", response.getMessageComponent());
        assertEquals("squ1 squ1 (4, 9)",responseComponent);

        response = myclevis.run("pick-and_move -20 -20 1 1"); //no shapes or groups should be picked
        responseComponent = String.join(" ",response.getMessageComponent());
        assertEquals("",responseComponent);
    }

    @Test
    public void test_intersect () {
        Response response;
        String result;
        /*
        testing single shape
         */
        response = myclevis.run("intersect rec1 cir2");
        result = String.join("",response.getMessageComponent());
        assertEquals("true",result);

        response = myclevis.run("intersect squ1 cir1");
        result = String.join("",response.getMessageComponent());
        assertEquals("false",result);

        response = myclevis.run("intersect cir1 cir2");
        result = String.join("",response.getMessageComponent());
        assertEquals("false",result);

        response = myclevis.run("intersect cir1 rec2");
        result = String.join("",response.getMessageComponent());
        assertEquals("false",result);
        /*
        testing group components
         */
        myclevis.run("group testgp1 line1 line2");
        myclevis.run("group testgp2 squ1 squ2");
        myclevis.run("group testgp3 cir1 cir2");

        response = myclevis.run("intersect testgp1 testgp3");
        result = String.join("",response.getMessageComponent());
        assertEquals("true", result);

        response = myclevis.run("intersect testgp2 testgp3");
        result = String.join("",response.getMessageComponent());
        assertEquals("false", result);
    }

    @Test
    public void test_list() {
        Response response;

        response = myclevis.run("list cir1");
        assertEquals("Circle cir1: Center coordinate=(-4, 5), radius=7\n",response.getMessage());

        response = myclevis.run("list line1");
        assertEquals("Line line1: Point 1=(-5, -1), Point 2=(2, 3)\n",response.getMessage());

        myclevis.run("group testgp1 rec1 rec2");
        response = myclevis.run("list testgp1");
        assertEquals("Group testgp1\n|-Rectangle rec2\n|-Rectangle rec1\n",response.getMessage());
    }

    @Test
    public void test_listall () {
        Response response;

        response = myclevis.run("listAll");
        assertEquals("Line line2: Point 1=(8, -5), Point 2=(-6, 6)\nLine line1: Point 1=(-5, -1), Point 2=(2, 3)\n" +
                "Circle cir2: Center coordinate=(6, -2), radius=4\nCircle cir1: Center coordinate=(-4, 5), radius=7\n" +
                "Square squ2: Top-left coordinate=(-4, -6), Length=3\nSquare squ1: Top-left coordinate=(6, 7), Length=2\n" +
                "Rectangle rec2: Top-left coordinate=(-3, -5), width=4, height=5\nRectangle rec1: Top-left coordinate=(2, 3), width=3, height=4\n",response.getMessage());

        myclevis.run("delete line1");
        myclevis.run("delete line2");
        myclevis.run("delete cir1");
        myclevis.run("delete cir2");
        myclevis.run("delete rec1");

        response = myclevis.run("listAll");
        assertEquals("Square squ2: Top-left coordinate=(-4, -6), Length=3\n" +
                "Square squ1: Top-left coordinate=(6, 7), Length=2\n" +
                "Rectangle rec2: Top-left coordinate=(-3, -5), width=4, height=5\n", response.getMessage());
    }

    @Test
    public void test_undo_redo () {
        Response response;

        response = myclevis.run("delete cir1");
        assertEquals(7,response.getCurrentImageSize());
        response = myclevis.run("undo");
        assertEquals(8,response.getCurrentImageSize());
        response = myclevis.run("redo");
        assertEquals(7,response.getCurrentImageSize());

        String result;
        response = myclevis.run("move rec2 3 4");
        result = String.join(" ", response.getMessageComponent());
        assertEquals("rec2 (0, -1)",result);
        myclevis.run("undo");
        response = myclevis.run("list rec2");
        assertEquals("Rectangle rec2: Top-left coordinate=(-3, -5), width=4, height=5\n",response.getMessage());
        myclevis.run("redo");
        response = myclevis.run("list rec2");
        assertEquals("Rectangle rec2: Top-left coordinate=(0, -1), width=4, height=5\n",response.getMessage());
    }

    @Test
    public void test_invalid_undo(){
        for (int i = 0; i < 8; i++){
            myclevis.run("undo");
        }
        Response response = myclevis.run("undo");
        assertFalse(response.isAccepted());
    }

    @Test
    public void test_invalid_redo(){
        Response response = myclevis.run("redo");
        assertFalse(response.isAccepted());
    }
}