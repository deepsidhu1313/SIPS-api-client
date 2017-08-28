/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.s13.sips.api.client;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import org.json.JSONObject;

/**
 *
 * @author nika
 */
public class APIClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        File settingsFile = new File("settings.json");
        if (settingsFile.exists()) {
            JSONObject settings = new JSONObject(Util.readFile(settingsFile.getAbsolutePath()));

        }

        Scanner scanner = new Scanner(System.in);
        String input;
        Client client = null;
        String firstWord;
        while ((input = scanner.nextLine()) != null) {
            String inputWords[] = input.split("\\s+");
            ArrayList<String> inputs = new ArrayList<>(Arrays.asList(inputWords));
            if (inputs.contains("quit") || inputs.contains("exit")) {
                System.out.println("BYE !!");
                return;
            }
            firstWord = inputs.get(0);

            switch (firstWord) {
                case "connect":
                    client = new Client(inputs.get(1), Integer.parseInt(inputs.get(2).trim()), inputs.get(3));
                    break;
                default:
                    if(client==null){
                        System.err.println("Please Connect to a host first!!!\n"
                                + "Example:\n"
                                + "\t<connect host port apikey>");
                        break;
                    }
                    System.out.println(""+client.executeRequest(input));
                    break;

            }

            //System.out.println("" + inputs);
        }
    }

}
