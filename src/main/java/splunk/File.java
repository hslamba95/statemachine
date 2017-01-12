package splunk;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by hslamba on 1/11/17.
 */
public class File {


    public static void main(String[] args) throws IOException {


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Name","India");
        jsonObject.put("Population",new Integer(100000));

        JSONArray listOfStates = new JSONArray();
        listOfStates.add("Madhya Pradesh");
        listOfStates.add("Maharastra");
        listOfStates.add("Rajasthan");

        jsonObject.put("States",listOfStates);


        try {

            // Writing to a file
            java.io.File file=new java.io.File("sifti.json");
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            System.out.println("Writing JSON object to file");
            System.out.println("-----------------------");
            System.out.print(jsonObject);

            fileWriter.write(jsonObject.toJSONString());
            fileWriter.flush();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }







    }}