package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.ArrayList;

/**
 * storing shape or a folder of Layers
 */
public class Layer {

     // the group name or the shape name
     private final String name;
     // either one of the below variable will be used
     private final MyShapes content;
     private final ArrayList<Layer> folder;

     /**
      * Constructor of Layer
      * @param command the Command class object
      */
     public Layer(Command command){
          this.name = command.getArgAt(0);
          switch (command.getName()){
               case "rectangle": this.content = new Rectangle(command.getArgs());
               break;
               case "square": this.content = new Square(command.getArgs());
               break;
               case "line": this.content = new Line(command.getArgs());
               break;
               case "circle": this.content = new Circle(command.getArgs());
               break;
               default: this.content = null;
          }
          this.folder = new ArrayList<>();
     }

     /**
      * Overloaded constructor of Layer
      * @param name the group name
      */
     public Layer(String name){
          this.name = name;
          this.content = null;
          this.folder = new ArrayList<>();
     }

     /**
      * get this Layer name
      * @return the name of this Layer object
      */
     public String getName(){
          return this.name;
     }

     /**
      * get this Layer content
      * @return the MyShapes object storing in the content
      */
     public MyShapes getContent(){
          return content;
     }

     /**
      * get the whole Layer folder in this Layer
      * @return ArrayList storing Layer objects
      */
     public ArrayList<Layer> getFolder(){
          return folder;
     }

     /**
      * add a new Layer object to this folder
      * @param otherLayer the new Layer object
      */
     public void addToFolder(Layer otherLayer){
          this.folder.add(otherLayer);
     }

     /**
      * tell if this Layer is a folder
      * @return true for this Layer is a folder, false for content Layer
      */
     public boolean isFolder(){
          return content == null;
     }

     /**
      * a shortcut to get Layer object storing in this Layer folder at specific index
      * @param index the specific index
      * @return the required Layer object
      */
     public Layer getFolderLayerAt(int index){
          return this.folder.get(index);
     }

     /**
      * (recursive method)
      * access the Layers and collect all content Layers references
      * @param currentLayer current Layer object
      * @return ArrayList of content Layer(s) object references
      */
     private ArrayList<Layer> getContentLayer(Layer currentLayer){
          ArrayList<Layer> result = new ArrayList<>();
          if (currentLayer.isFolder()){
               for (int i = currentLayer.getFolder().size()-1; i >= 0; i--){
                    result.addAll(getContentLayer(currentLayer.getFolderLayerAt(i)));
               }
          }
          else{
               result.add(currentLayer);
          }
          return result;
     }

     /**
      * get content Layer(s) object under this Layer object
      * @return ArrayList of content Layer(s) object references
      */
     public ArrayList<Layer> getContentLayerList(){
          return getContentLayer(this);
     }

     /**
      * (recursive method)
      * access the Layers and collect all Layer names
      * @param currentLayer current Layer object
      * @return ArrayList of Layer names
      */
     public ArrayList<String> getLayerNameList(Layer currentLayer){
          ArrayList<String> result = new ArrayList<>();
          if (currentLayer.isFolder()){
               for (int i = currentLayer.getFolder().size()-1; i >= 0; i--){
                    result.addAll(getLayerNameList(currentLayer.getFolderLayerAt(i)));
               }
          }
          result.add(currentLayer.getName());
          return result;
     }

     /**
      * determine if name exist in the current Layer names
      * @param name the name
      * @return true for exist in current Layer, false for not
      */
     public boolean existName(String name){
          ArrayList<String> layerNameList = getLayerNameList(this);
          return layerNameList.contains(name);
     }

     /**
      * (static recursive method)
      * print the current Layer with suitable intention
      * @param currentLayer current Layer object
      * @param intent the number of intention needed
      * @return the formatted string
      */
     private static String printLayer(Layer currentLayer, int intent){
          StringBuilder result = new StringBuilder();
          if (currentLayer.isFolder()){
               result.append("|-".repeat(intent));
               result.append("Group ").append(currentLayer.name).append("\n");
               for (int i = currentLayer.getFolder().size()-1; i >= 0; i--){
                    result.append(printLayer(currentLayer.getFolderLayerAt(i), intent+1));
               }
          }
          else if (intent > 0){
               result.append("|-".repeat(intent));
               return result+currentLayer.getContent().toSimpleString()+"\n";
          }
          else{
               result.append(currentLayer.getContent()).append("\n");
          }
          return result.toString();
     }

     /**
      * print this Layer object as the formatted declared in the project description
      * @return required string
      */
     public String toString(){
          return printLayer(this, 0);
     }
}
