package hk.edu.polyu.comp.comp2021.clevis;

import hk.edu.polyu.comp.comp2021.clevis.model.Clevis;
import hk.edu.polyu.comp.comp2021.clevis.model.Response;

import java.util.Scanner;

public class Application {

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String inp;
        Response response;
        if (args.length == 4){
            System.out.println("Welcome to CLEVIS, a command-line vector graphic software");
            System.out.println("Supported shapes: rectangle, square, line, circle");
            System.out.println("For the full instruction, please do check the user manual of CLEVIS");
            System.out.println("Initializing log files........");
            Clevis clevis = new Clevis(args[1], args[3]);
            do{
                System.out.print("\nCommand: ");
                inp = sc.nextLine();
                response = clevis.run(inp);
                System.out.print(response.getMessage());
                if (!response.isAccepted()){
                    System.out.println("The command \""+response.getCommand()+"\" is rejected");
                }
                else{
                    System.out.println("The command \""+response.getCommand()+"\" runs successfully");
                }
            }while (!inp.equals("quit"));
        }
        else{
            System.out.println("Not enough arguments");
        }
        // Initialize and utilize the system
    }

}
