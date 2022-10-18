package hk.edu.polyu.comp.comp2021.clevis.model;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Exceptions used in Clevis, helps to print out informational warnings
 */
class CommandNameNotFoundException extends Exception{
    public CommandNameNotFoundException(String name){
        super("Command name "+name+" is not available\n");
    }
}

class ArgumentNumberException extends Exception{
    public ArgumentNumberException(int expected, int received){
        super(expected+" command arguments expected, but "+received+" received\n");
    }
}

class ArgumentTypeException extends Exception{
    public ArgumentTypeException(String expected, String received){
        super(expected+" expected, but "+received+" received\n");
    }
}

class ShapeNameDuplicatedException extends Exception{
    public ShapeNameDuplicatedException(String name){
        super("Shape name "+name+" has already exist in the image\n");
    }
}

class ArgumentNonPositiveException extends Exception{
    public ArgumentNonPositiveException(double val){
        super("Positive numeric values expected, but "+val+" received\n");
    }
}

class ShapeNotFoundException extends Exception{
    public ShapeNotFoundException(String name){
        super("Shape name "+name+" is not exist in the image\n");
    }
}

class EmptyImageException extends Exception{
    public EmptyImageException(String commandName){
        super("Command "+commandName+" cannot be accomplished because the image has no shape\n");
    }
}

class EmptyredoStackException extends Exception{
    public EmptyredoStackException(){
        super("Command redo cannot be accomplished because the redo stack is empty\n");
    }
}

/**
 * Clevis implementation
 * in this implementation, shapes are also called layers for better understanding
 */
public class Clevis {

    // define an image which has multiple layers
    private final ArrayList<Layer> image;

    // output format of numeric values
    private static final DecimalFormat df = new DecimalFormat("0.##");

    // total available command names
    private final String[] CMDNAMES = {
            "rectangle", "line", "circle", "square", "group",
            "ungroup", "delete", "boundingbox", "move", "pick-and-move",
            "intersect", "list", "listall", "quit", "redo",
            "undo"};

    // a list that store all successful executed layers operation commands
    private final ArrayList<Command> executeList;

    // stack for storing the Command pop from executeList
    private final ArrayList<Command> redoStack;

    // the file path passing from main at the beginning
    private String htmlPath;
    private String txtPath;

    // check if this is the first time writing to file
    // if yes then it should overwrite the existing file
    // if no then it should append the existing file
    private boolean isAppend;

    /**
     * constructor of Clevis
     * @param htmlPath the path of the html file
     * @param txtPath the path of the txt file
     */
    public Clevis(String htmlPath, String txtPath){
        File temp;
        try{
            temp = new File(htmlPath);
            this.htmlPath = temp.getAbsolutePath();
            if (temp.createNewFile()){
                System.out.println("New html file created at "+this.htmlPath);
            }
            else{
                System.out.println("html file found at "+this.htmlPath);
            }
        }
        catch (IOException e){
            System.out.println("Cannot create html file at "+this.htmlPath);
        }
        try{
            temp = new File(txtPath);
            this.txtPath = temp.getAbsolutePath();
            if (temp.createNewFile()){
                System.out.println("New txt file created at "+this.htmlPath);
            }
            else{
                System.out.println("txt file found at "+this.htmlPath);
            }
        }
        catch (IOException e){
            System.out.println("Cannot create txt file at "+this.txtPath);
        }
        image = new ArrayList<>();
        executeList = new ArrayList<>();
        redoStack = new ArrayList<>();
        isAppend = false;
    }

    /**
     * writing log to txt file and html file
     * @param command the Command object that had successfully executed
     */
    private void logWrite(Command command) {

        // write to txt file
        try{
            FileWriter oTxtFileWriter = new FileWriter(txtPath, isAppend);
            oTxtFileWriter.write(command.toString()+"\n");
            oTxtFileWriter.close();
        }
        catch (IOException e){
            System.out.println("Error occurred when accessing the txt file at "+txtPath);
        }

        // write to html file
        try{
            ArrayList<String> content = new ArrayList<>();
            File oHtmlFile = new File(htmlPath);
            Scanner sc = new Scanner(oHtmlFile);
            sc.useDelimiter("\n");
            if (isAppend){
                while (sc.hasNextLine()){
                    content.add(sc.nextLine());
                }
            }
            sc.close();

            FileWriter oHtmlFileWriter = new FileWriter(htmlPath);
            oHtmlFileWriter.write("<html><body><table>\n");
            oHtmlFileWriter.write("<tr><th>Index</th><th>Command</th></tr>\n");
            if (isAppend){
                for (int i = 2; i < content.size()-1; i++){
                    oHtmlFileWriter.write(content.get(i));
                    oHtmlFileWriter.write("\n");
                }
            }
            oHtmlFileWriter.write("<tr><td>"+(Math.max(1, content.size()-2))+"</td><td>"+command.toString()+"</td></tr>\n");
            oHtmlFileWriter.write("</table></body></html>");
            oHtmlFileWriter.close();
        }
        catch (IOException e){
            System.out.println("Error occurred when accessing the HTML file at "+htmlPath);
        }
        isAppend = true;
    }

    /**
     * break down the command string into name and arguments,
     * also trim and lowercase the arguments.
     * @param commandStr command string input by the user
     * @return result Command object
     */
    private Command separate(String commandStr){
        // replace any spaces to 1 space, then split the String
        String[] content = commandStr.replaceAll("\\s{2,}", " ").split(" ");
        // trim and lowercase the command name
        Command result = new Command(content[0].trim().toLowerCase());
        // add the arguments
        for (int i = 1; i < content.length; i++){
            result.addArg(content[i].trim());
        }
        return result;
    }

    /**
     * group the specific layers by creating new layer folder and
     * add the target layers to the folder, then
     * delete the layer(s) being grouped in the image.
     * @param args the new group name at index 0 and layer(s) being grouped at index > 0
     */
    private void group(ArrayList<String> args){
        // create a new Layer
        image.add(new Layer(args.get(0)));
        int count = 0;
        // List for storing the references of the Layer that will be removed at the end of the method
        ArrayList<Layer> removeLayerList = new ArrayList<>();
        // List of names that needed to find
        ArrayList<String> targetNameList = new ArrayList<>(args);

        // remove the first name in the targetNameList
        // since it is the new Layer name
        targetNameList.remove(0);

        // find the target Layer and move them to the new Layer folder
        for (int i = 0; count < targetNameList.size(); i++){
            if (targetNameList.contains(image.get(i).getName())){
                image.get(image.size()-1).addToFolder(image.get(i));
                removeLayerList.add(image.get(i));
                count++;
            }
        }

        // remove the affected Layers
        for (Layer currentLayer : removeLayerList){
            image.remove(currentLayer);
        }
    }

    /**
     * ungroup a specific folder by
     * access the target layer folder,
     * extract the layers inside and add them back to image, and
     * delete the empty folder layer.
     * @param name layer name in String
     * @return List of Layers' name in the target Layer folder
     */
    private ArrayList<String> ungroup(String name){
        ArrayList<String> targetNameList = new ArrayList<>();
        for (int i = 0; i < image.size(); i++){
            if (name.equals(image.get(i).getName())){
                image.addAll(image.get(i).getFolder());
                for (Layer currentLayer : image.get(i).getFolder()){
                    targetNameList.add(currentLayer.getName());
                }
                image.remove(i);
                break;
            }
        }
        return targetNameList;
    }

    /**
     * delete a specific layer with name.
     * @param name layer name in String
     */
    private void delete(String name){
        for (int i = 0; i < image.size(); i++){
            if (image.get(i).getName().equals(name)){
                image.remove(i);
                break;
            }
        }
    }

    /**
     * find the minimum bounding box of a specific layer by
     * getting all required content layers and
     * comparing their min max x, y.
     * @param targetName the target layer name in String
     * @return a LIST of Coordinate objects with size = 4 represent the corners of the bounding box
     */
    private ArrayList<Coordinate> boundingbox(String targetName){
        ArrayList<Coordinate> result = new ArrayList<>();
        double[] minmaxTemp = new double[4];
        ArrayList<Layer> targetLayerList = new ArrayList<>();

        for (Layer currentLayer : image) {
            if (currentLayer.getName().equals(targetName)) {
                targetLayerList = new ArrayList<>(currentLayer.getContentLayerList());
                break;
            }
        }

        for (Layer currentLayer : targetLayerList){
            // if it is the first loop
            // initialize the variable
            if (minmaxTemp[0] == minmaxTemp[1]){
                minmaxTemp[0] = currentLayer.getContent().getMinX();
                minmaxTemp[1] = currentLayer.getContent().getMaxX();
                minmaxTemp[2] = currentLayer.getContent().getMinY();
                minmaxTemp[3] = currentLayer.getContent().getMaxY();
            }
            else{
                minmaxTemp[0] = Math.min(minmaxTemp[0], currentLayer.getContent().getMinX());
                minmaxTemp[1] = Math.max(minmaxTemp[1], currentLayer.getContent().getMaxX());
                minmaxTemp[2] = Math.min(minmaxTemp[2], currentLayer.getContent().getMinY());
                minmaxTemp[3] = Math.max(minmaxTemp[3], currentLayer.getContent().getMaxY());
            }
        }

        // convert the result to Coordinates
        // top left corner
        result.add(new Coordinate(minmaxTemp[0], minmaxTemp[3]));
        // top right corner
        result.add(new Coordinate(minmaxTemp[1], minmaxTemp[3]));
        // bottom left corner
        result.add(new Coordinate(minmaxTemp[0], minmaxTemp[2]));
        // bottom right corner
        result.add(new Coordinate(minmaxTemp[1], minmaxTemp[2]));

        return result;
    }

    /**
     * move a specific layer.
     * @param args name at index 0, dx and dy at index 1 and 2 respectively
     * @return List of Layer name moved and their final coordinate
     */
    private ArrayList<String> move(ArrayList<String> args){
        ArrayList<Layer> targetLayerList = new ArrayList<>();
        ArrayList<String> resultList = new ArrayList<>();

        String targetName = args.get(0);
        double dx = Double.parseDouble(args.get(1));
        double dy = Double.parseDouble(args.get(2));

        // find the target Layer and obtained its inner layers
        for (Layer currentLayer : image){
            if (currentLayer.getName().equals(targetName)){
                targetLayerList = new ArrayList<>(currentLayer.getContentLayerList());
            }
        }

        for (Layer currentLayer : targetLayerList){
            currentLayer.getContent().move(dx, dy);
            resultList.add(currentLayer.getName());
            resultList.add("("+ df.format(currentLayer.getContent().getProp()[0])+", "+
                    df.format(currentLayer.getContent().getProp()[1])+ ")");
        }

        return resultList;
    }

    /**
     * search for the layer that the specific point lies on, and
     * select all content layers inside, then
     * move them one by one
     * @param args specific point coordinate at index 0 and 1, dx and dy at index 2 and 3
     * @return List of Layer name of the selected Layer
     */
    private ArrayList<String> pick_move(ArrayList<String> args){
        ArrayList<String> resultList = new ArrayList<>();
        double cor_x = Double.parseDouble(args.get(0));
        double cor_y = Double.parseDouble(args.get(1));
        double dx = Double.parseDouble(args.get(2));
        double dy = Double.parseDouble(args.get(3));
        ArrayList<Layer> targetLayerList = new ArrayList<>();
        boolean found = false;

        // from the List tail to the head
        // get each outer Layer's all shapes
        // compare with the given point
        // if the point is inside the shapes, break the loop
        for (int i = image.size()-1; i >= 0 && !found; i--){
            targetLayerList.clear();
            targetLayerList.addAll(image.get(i).getContentLayerList());
            for (Layer currentLayer : targetLayerList){
                if (currentLayer.getContent().containsPoint(cor_x, cor_y, 0.05)){
                    found = true;
                    resultList.add(image.get(i).getName());
                    break;
                }
            }
        }

        // if found
        if (found){
            for (Layer currentLayer : targetLayerList){
                currentLayer.getContent().move(dx, dy);
                resultList.add(currentLayer.getName());
                resultList.add("("+ df.format(currentLayer.getContent().getProp()[0])+", "+
                        df.format(currentLayer.getContent().getProp()[1])+ ")");
            }
        }

        return resultList;
    }

    /**
     * print a specific layer information
     * @return the required result string
     */
    private String list(String targetName){
        String result = "";
        for (int i = image.size()-1; i >= 0 && result.length() == 0; i--){
            if (image.get(i).getName().equals(targetName)){
                result = image.get(i).toString();
            }
        }
        return result;
    }

    /**
     * print all layer information in image
     * @return the required result string
     */
    private String listAll(){
        StringBuilder sb = new StringBuilder();
        for (int i = image.size()-1; i >= 0; i--){
            sb.append(image.get(i));
        }
        return sb.toString();
    }

    /**
     * determine if the shape is intersected with another shape
     * @param args the names of the layer
     * @return true for intersected, false for not
     */
    private boolean isIntersect(ArrayList<String> args){
        ArrayList<Layer> layerAContent = new ArrayList<>();
        ArrayList<Layer> layerBContent = new ArrayList<>();

        // get all content layers of A
        for (Layer currentLayer : image){
            if (currentLayer.getName().equals(args.get(0))){
                layerAContent.addAll(currentLayer.getContentLayerList());
                break;
            }
        }
        // get all content layers of B
        for (Layer currentLayer : image){
            if (currentLayer.getName().equals(args.get(1))){
                layerBContent.addAll(currentLayer.getContentLayerList());
                break;
            }
        }

        // for each shape in A, compare the shape in B
        // if any shape is found intersect with another, return true
        for (Layer currentALayer : layerAContent){
            for (Layer currentBLayer : layerBContent){
                // if shape A can break into Lines
                if (currentALayer.getContent().isLinesRepresentable()){
                    if(currentBLayer.getContent().isIntersectWith(currentALayer.getContent().intoLines())){
                        return true;
                    }
                }
                // if shape B can break into Lines
                else if (currentBLayer.getContent().isLinesRepresentable()){
                    if(currentALayer.getContent().isIntersectWith(currentBLayer.getContent().intoLines())){
                        return true;
                    }
                }
                // if 2 shapes are circles
                // calculate the distance between their center
                // if it is <= the sum fo their radius and >= their min. radius, then it is intersected
                else{
                    double[] c1 = currentALayer.getContent().getProp();
                    double[] c2 = currentBLayer.getContent().getProp();
                    Line ppLine = new Line("ppLine", c1[0], c1[1], c2[0], c2[1]);
                    double radiusDistance = ppLine.getDistance();
                    if(radiusDistance <= (c1[2]+c2[2]) && radiusDistance+Math.min(c1[2], c2[2]) <=Math.max(c1[2], c2[2])){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * according to the executeList and executeIndex,
     * clear the image, and
     * rebuild the image
     */
    private void rebuildImage(Response response){
        image.clear();
        for (Command currentCommand : executeList){
            execute(currentCommand, response, false);
        }
    }

    /**
     * pop the Command from executeList, and
     * push it to redoStack, then
     * rebuild the image
     */
    private void undo(Response response){
        redoStack.add(executeList.get(executeList.size()-1));
        executeList.remove(executeList.size()-1);
        rebuildImage(response);
    }

    /**
     * pop the Command from redoStack, and
     * move the Command to executeList, then
     * execute the popped Command
     */
    private void redo(Response response){
        executeList.add(redoStack.get(redoStack.size()-1));
        redoStack.remove(redoStack.size()-1);
        execute(executeList.get(executeList.size()-1), response, false);
    }

    /**
     * insert the new Command to the ExecuteList, and
     * clear the redoStack
     * @param command the last successfully executed Command
     */
    private void updateExecuteList(Command command){
        executeList.add(command);
        redoStack.clear();
    }

    /**
     * validate the Command for different command name
     * @param command the Command object
     */
    private void isValid(Command command)
            throws
            CommandNameNotFoundException,
            ArgumentNumberException,
            ShapeNameDuplicatedException,
            ArgumentTypeException,
            ArgumentNonPositiveException,
            ShapeNotFoundException,
            EmptyImageException,
            EmptyredoStackException {
        ArrayList<Double> numArgs = new ArrayList<>();
        boolean found = false;
        // if the command name is not exist in the set
        if(!Arrays.asList(CMDNAMES).contains(command.getName())){
            throw new CommandNameNotFoundException(command.getName());
        }

        switch (command.getName()) {
            case "rectangle":  // rectangle n, x, y, w, h
                // args number must be exactly 5
                if (command.getArgs().size() != 5) {
                    throw new ArgumentNumberException(5, command.getArgs().size());
                }
                // shape name must not duplicate current layer names
                for (Layer currentLayer : image){
                    if (currentLayer.existName(command.getArgAt(0))){
                        throw new ShapeNameDuplicatedException(command.getArgAt(0));
                    }
                }
                // x, y, w, h must be numbers
                for (int i = 1; i < command.getArgs().size(); i++) {
                    try {
                        numArgs.add(Double.parseDouble(command.getArgAt(i)));
                    } catch (NumberFormatException e) {
                        throw new ArgumentTypeException("Numeric values", command.getArgAt(i));
                    }
                }
                // width and height must not less than or equal zero
                if (numArgs.get(2) <= 0) {
                    throw new ArgumentNonPositiveException(numArgs.get(2));
                }
                if (numArgs.get(3) <= 0){
                    throw new ArgumentNonPositiveException(numArgs.get(3));
                }
                // end of the rectangle validation
                break;
            case "line": // line n, x1, y1, x2, y2
                // args number must be exactly 5
                if (command.getArgs().size() != 5) {
                    throw new ArgumentNumberException(5, command.getArgs().size());
                }
                // shape name must not duplicate current layer names
                for (Layer currentLayer : image){
                    if (currentLayer.existName(command.getArgs().get(0))){
                        throw new ShapeNameDuplicatedException(command.getArgAt(0));
                    }
                }
                // x1, y1, x2, y2 must be numbers
                for (int i = 1; i < command.getArgs().size(); i++) {
                    try {
                        Double.parseDouble(command.getArgAt(i));
                    } catch (NumberFormatException e) {
                        throw new ArgumentTypeException("Numeric values", command.getArgAt(i));
                    }
                }
                // end of the line validation
                break;
            case "circle": // circle n, x, y, r
            case "square": // square n, x, y, l
                // args number must be exactly 4
                if (command.getArgs().size() != 4) {
                    throw new ArgumentNumberException(4, command.getArgs().size());
                }
                // shape name must not duplicate current layer names
                for (Layer currentLayer : image){
                    if (currentLayer.existName(command.getArgAt(0))){
                        throw new ShapeNameDuplicatedException(command.getArgAt(0));
                    }
                }
                // x, y, r(l) must be numbers
                for (int i = 1; i < command.getArgs().size(); i++) {
                    try {
                        numArgs.add(Double.parseDouble(command.getArgAt(i)));
                    } catch (NumberFormatException e) {
                        throw new ArgumentTypeException("Numeric values", command.getArgAt(i));
                    }
                }
                // radius (length) must not less than or equal zero
                if (numArgs.get(2) <= 0) {
                    throw new ArgumentNonPositiveException(numArgs.get(2));
                }
                // end of the circle validation
                break;
            case "group": // group n, n1, n2...
                // number of [n1, n2...] must not larger than the current image.size
                if (command.getArgs().size() - 1 > image.size()) {
                    throw new EmptyImageException(command.getName());
                }
                // group name must not duplicate current layer names
                for (Layer currentLayer : image){
                    if (currentLayer.existName(command.getArgAt(0))){
                        throw new ShapeNameDuplicatedException(command.getArgAt(0));
                    }
                }
                // [n1, n2...] must exist
                for (int i = 1; i < command.getArgs().size(); i++) {
                    found = false;
                    for (Layer currentLayer : image){
                        if (currentLayer.existName(command.getArgAt(i))){
                            found = true;
                        }
                    }
                    if (!found){
                        throw new ShapeNotFoundException(command.getArgAt(0));
                    }
                }
                // [n, n1, n2...] must not duplicate each other
                for (int i = 0; i < command.getArgs().size(); i++) {
                    for (int j = i + 1; j < command.getArgs().size(); j++) {
                        if (command.getArgAt(i).equals(command.getArgAt(j))) {
                            throw new ShapeNameDuplicatedException(command.getArgAt(j));
                        }
                    }
                }
                // end of the group validation
                break;
            case "ungroup": // ungroup n
            case "list": // list n
            case "delete": // delete n
            case "boundingbox": // boundingbox n
                // image must have at least 1 layer
                if (image.size() == 0) {
                    throw new EmptyImageException(command.getName());
                }
                // args number must be exactly 1
                if (command.getArgs().size() != 1) {
                    throw new ArgumentNumberException(1, command.getArgs().size());
                }
                // n must exist in the outer-layer of image
                for (int i = 0; i < image.size() && !found; i++) {
                    found = image.get(i).getName().equals(command.getArgAt(0));
                }
                if (!found){
                    throw new ShapeNotFoundException(command.getArgAt(0));
                }

                // end of the ungroup, list, delete, boundingbox validation
                break;
            case "move": // move n, dx, dy
                // image must have at least 1 layer
                if (image.size() == 0) {
                    throw new EmptyImageException(command.getName());
                }
                // args number must be exactly 3
                if (command.getArgs().size() != 3) {
                    throw new ArgumentNumberException(3, command.getArgs().size());
                }
                // n must exist
                for (Layer currentLayer : image){
                    if (currentLayer.existName(command.getArgAt(0))){
                        found = true;
                        break;
                    }
                }
                if (!found){
                    throw new ShapeNotFoundException(command.getArgAt(0));
                }
                // dx, dy must be numbers
                for (int i = 1; i < command.getArgs().size(); i++) {
                    try {
                        Double.parseDouble(command.getArgAt(i));
                    } catch (NumberFormatException e) {
                        throw new ArgumentTypeException("Numeric values", command.getArgAt(i));
                    }
                }
                // end of the move validation
                break;
            case "intersect": // intersect n1, n2
                // image must have at least 1 layer
                if (image.size() == 0) {
                    throw new EmptyImageException(command.getName());
                }
                // args number must be exactly 2
                if (command.getArgs().size() != 2) {
                    throw new ArgumentNumberException(2, command.getArgs().size());
                }
                // n1, n2 must exist
                for (String currentName : command.getArgs()) {
                    found = false;
                    for (Layer currentLayer : image){
                        if (currentLayer.existName(currentName)){
                            found = true;
                            break;
                        }
                    }
                    if (!found){
                        throw new ShapeNotFoundException(currentName);
                    }
                }
                // end of intersect validation
                break;
            case "listall": //listAll
                // image must have at least 1 layer
                if (image.size() == 0) {
                    throw new EmptyImageException(command.getName());
                }
                // args number must be exactly 0
                if (command.getArgs().size() != 0) {
                    throw new ArgumentNumberException(0, command.getArgs().size());
                }
                // end of the listAll validation
                break;
            case "redo": // redo
                // args number must be exactly 0
                if (command.getArgs().size() != 0) {
                    throw new ArgumentNumberException(0, command.getArgs().size());
                }
                // redoStack must not empty
                if (redoStack.isEmpty()) {
                    throw new EmptyredoStackException();
                }
                // end of redo validation
                break;
            case "undo": // undo
                // args number must be exactly 0
                if (command.getArgs().size() != 0) {
                    throw new ArgumentNumberException(0, command.getArgs().size());
                }
                // executeList must not empty
                if (executeList.isEmpty()) {
                    throw new EmptyImageException(command.getName());
                }
                // end of redo validation
                break;
            // end of redo validation
            case "quit":
        }
    }

    /**
     * execute the command according to the command name
     * @param command the Command object
     * @param response the Response object
     * @param needUpdate true for updating the executeList in certain commands, and false for quiet execution
     */
    private void execute(Command command, Response response, boolean needUpdate){
        StringBuilder responseMaker = new StringBuilder();
        ArrayList<String> resultList;
        switch(command.getName()){
            case "rectangle":
            case "line":
            case "square":
            case "circle":
                image.add(new Layer(command));
                if (needUpdate){
                    updateExecuteList(command);
                    // the response message
                    responseMaker.append(command.getName().substring(0, 1).toUpperCase())
                            .append(command.getName().substring(1))
                            .append(" \"")
                            .append(command.getArgAt(0))
                            .append("\" has been created\n");
                    // record the name of the shape
                    response.addMessageComponent(command.getArgAt(0));
                }
                break;
            case "group":
                group(command.getArgs());
                if (needUpdate){
                    updateExecuteList(command);
                    // construct the response message
                    responseMaker.append("Shape(s) \"");
                    for (int i = 1; i < command.getArgs().size(); i++){
                        // the name of the shape that are going to be grouped
                        responseMaker.append(command.getArgAt(i));
                        // record the name of the shape that are going to be grouped
                        response.addMessageComponent(command.getArgAt(i));
                        if (i == command.getArgs().size()-1){
                            responseMaker.append("\" ");
                        }
                        else{
                            responseMaker.append("\", \"");
                        }
                    }
                    responseMaker.append("has been grouped into \"")
                            .append(command.getArgAt(0)).append("\"\n");
                    // record the group name
                    response.addMessageComponent(command.getArgAt(0));
                }
                break;
            case "ungroup":
                resultList = ungroup(command.getArgAt(0));
                if (needUpdate){
                    updateExecuteList(command);
                    // construct the response message
                    responseMaker.append("Shape \"")
                            .append(command.getArgAt(0))
                            .append("\" has been ungrouped into \"");
                    // record the group name
                    response.addMessageComponent(command.getArgAt(0));
                    for (String currentLayerName : resultList){
                        if (currentLayerName.equals(resultList.get(resultList.size()-1))){
                            responseMaker.append(currentLayerName).append("\" ");
                        }
                        else{
                            responseMaker.append(currentLayerName).append("\", \"");
                        }
                    }
                    responseMaker.append("\n");
                    response.addMessageComponent(command.getArgAt(0));
                    // record the ungrouped shape names
                    response.addMessageComponent(resultList);
                }
                break;
            case "delete":
                delete(command.getArgAt(0));
                if (needUpdate){
                    updateExecuteList(command);
                    // the response message
                    responseMaker.append("Shape \"")
                            .append(command.getArgAt(0))
                            .append("\" has been deleted\n");
                    // record the deleted shape name
                    response.addMessageComponent(command.getArgAt(0));
                }
                break;
            case "boundingbox":
                ArrayList<Coordinate> result = boundingbox(command.getArgAt(0));
                double width, height;
                width = Math.abs(result.get(1).getX() - result.get(0).getX());
                height = Math.abs(result.get(0).getY() - result.get(2).getY());
                // construct the response message
                responseMaker.append("Required bounding box:\n");
                responseMaker.append("Top-left coordinate: ").append(result.get(0)).append("\n");
                responseMaker.append("Width: ").append(df.format(width)).append("\n");
                responseMaker.append("Height: ").append(df.format(height)).append("\n");
                // record the coordinate, width and height
                response.addMessageComponent(String.valueOf(result.get(0).getX()));
                response.addMessageComponent(String.valueOf(result.get(0).getY()));
                response.addMessageComponent(String.valueOf(width));
                response.addMessageComponent(String.valueOf(height));
                break;
            case "move":
                resultList = move(command.getArgs());
                if (needUpdate){
                    updateExecuteList(command);
                    for (int i = 0; i < resultList.size(); i += 2){
                        responseMaker.append("Shape \"")
                                .append(resultList.get(i))
                                .append("\" has been moved to ")
                                .append(resultList.get(i + 1)).append("\n");
                    }
                    // record the shape name and final coordinate
                    response.addMessageComponent(resultList);
                }
                break;
            case "pick-and-move":
                resultList = pick_move(command.getArgs());
                if (needUpdate){
                    updateExecuteList(command);
                    if (resultList.isEmpty()){
                        responseMaker.append("No shape is selected");
                    }
                    else{
                        responseMaker.append("Shape \"")
                                .append(resultList.get(0))
                                .append("\" has been selected\n");
                        for (int i = 1; i < resultList.size(); i += 2){
                            responseMaker.append("Shape \"")
                                    .append(resultList.get(i))
                                    .append("\" has been moved to ")
                                    .append(resultList.get(i + 1)).append("\n");
                        }
                        // record the shape selected, shape moved and their final coordinate
                        response.addMessageComponent(resultList);
                    }
                }
                break;
            case "intersect":
                if (isIntersect(command.getArgs())){
                    responseMaker.append("\"")
                            .append(command.getArgAt(0))
                            .append("\" is intersect with \"")
                            .append(command.getArgAt(1)).append("\"\n");
                    response.addMessageComponent("true");
                }
                else{
                    responseMaker.append("\"")
                            .append(command.getArgAt(0))
                            .append("\" is not intersect with \"")
                            .append(command.getArgAt(1)).append("\"\n");
                    response.addMessageComponent("false");
                }
                break;
            case "list":
                responseMaker.append(list(command.getArgAt(0)));
                break;
            case "listall":
                responseMaker.append(listAll());
                break;
            case "redo":
                redo(response);
                responseMaker.append("Command redo has been successfully accomplished\n");
                break;
            case "undo":
                undo(response);
                responseMaker.append("Command undo has been successfully accomplished\n");
        }
        response.setMessage(responseMaker.toString());
    }

    /**
     * called from outside to required running a command string
     * @param commandStr the command string input by the user
     */
    public Response run(String commandStr){
        Command command = separate(commandStr);
        Response response = new Response(command, image.size());
        try{
            isValid(command);
        }
        catch(CommandNameNotFoundException|
                ArgumentNumberException|
                ShapeNameDuplicatedException|
                ArgumentTypeException|
                ArgumentNonPositiveException|
                ShapeNotFoundException|
                EmptyImageException|
                EmptyredoStackException ex){
            response.setMessage(ex.getMessage());
            return response;
        }
        response.setIsAccepted(true);
        execute(command, response, true);
        logWrite(command);
        response.setCurrentImageSize(image.size());
        return response;
    }

}
