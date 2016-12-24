package statefulj;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hslamba on 12/24/16.
 */
public class Simple {

    String input;
    String eventE = null;
    public void scanFile(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the pattern you want to match....");
        input = scanner.nextLine();

        System.out.println("Input = " +input);
    }

    public void readFile() throws IOException {
        Pattern patt = Pattern.compile(input);
        BufferedReader r = new BufferedReader(new FileReader("matching.txt"));
        String line;
        while ((line = r.readLine()) != null){
            Matcher m = patt.matcher(line);
            while (m.find()){
                 // Simplest method:
                 System.out.println(m.group(0));
                 eventE = line;

            }
        }
    }

    public static void main(String[] args) throws IOException {

        Simple simple = new Simple();
        simple.scanFile();
        simple.readFile();



    }
}
