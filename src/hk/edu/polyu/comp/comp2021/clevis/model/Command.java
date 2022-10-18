package hk.edu.polyu.comp.comp2021.clevis.model;

import java.util.ArrayList;

/**
 * Split the command string into command name and arguments for further use
 */
public class Command {
    // command name
    private final String name;
    // command arguments
    private final ArrayList<String> args;

    /**
     * Constructor of Command:
     * @param name command name
     */
    public Command(String name){
        this.name = name;
        this.args = new ArrayList<>();
    }

    /**
     * Print this Command as the format declared on the description
     * @return formatted command string
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        for (String arg : args){
            sb.append(" ").append(arg);
        }
        return sb.toString();
    }

    /**
     * Get the command name
     * @return command name
     */
    public String getName(){
        return this.name;
    }

    /**
     * Add a SINGLE argument to this Command
     * @param arg the argument
     */
    public void addArg(String arg){
        this.args.add(arg);
    }

    /**
     * get the argument in this Command at specific index
     * @param index the argument index
     * @return the argument
     */
    public String getArgAt(int index){
        return this.args.get(index);
    }

    /**
     * Get the list of arguments in this Command
     * @return the list of arguments
     */
    public ArrayList<String> getArgs(){
        return this.args;
    }
}
