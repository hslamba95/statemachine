package statefulj;

import org.statefulj.fsm.TooBusyException;

import java.io.IOException;

/**
 * Created by hslamba on 12/24/16.
 */
public class main {

    public static void main(String[] args) throws IOException, TooBusyException, InterruptedException {
        SM1 sm1 = new SM1();
        sm1.openFile();
        sm1.readFile();
        sm1.closeFile();

        Thread t1 = new Thread(sm1);
        t1.start();

        SM2 sm2 = new SM2();

        sm2.openFile();
        sm2.readFile();
        sm2.closeFile();

        Thread.sleep(1000);
        Thread t2 = new Thread(sm2);
        t2.start();


        SM3 sm3 = new SM3();
        Thread.sleep(11000);
        sm3.read();
        sm3.checkPattern();

    }

}



