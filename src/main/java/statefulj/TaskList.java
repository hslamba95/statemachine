package statefulj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by hslamba on 12/30/16.
 */
public class TaskList {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        String line;
        Process p = Runtime.getRuntime().exec("ps -few");
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((line = input.readLine()) != null) {
            System.out.println(line);
        }
        input.close();
    }
}
