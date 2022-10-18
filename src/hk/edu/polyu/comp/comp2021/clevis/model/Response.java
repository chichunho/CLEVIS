package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.ArrayList;

public class Response {
    // the result message
    private String message;
    // the component that used to form the message
    private final ArrayList<String> messageComponent;
    // true for command string accepted, false for rejected
    private boolean isAccepted;
    // the command object converted
    private final Command command;
    // the size of the image after the executing the command
    private int currentImageSize;

    public Response(Command command, int size) {
        this.message = "";
        this.messageComponent = new ArrayList<>();
        this.isAccepted = false;
        this.command = command;
        this.currentImageSize = size;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void addMessageComponent(String component){
        this.messageComponent.add(component);
    }

    public void addMessageComponent(ArrayList<String> component){
        this.messageComponent.addAll(component);
    }

    public void setIsAccepted(boolean state){
        this.isAccepted = state;
    }

    public void setCurrentImageSize(int size){
        this.currentImageSize = size;
    }

    public String getMessage(){
        return message;
    }

    public ArrayList<String> getMessageComponent(){
        return messageComponent;
    }

    public boolean isAccepted(){
        return isAccepted;
    }

    public Command getCommand(){
        return command;
    }

    public int getCurrentImageSize(){
        return currentImageSize;
    }
}
