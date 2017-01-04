package statefulj;

import org.statefulj.fsm.TooBusyException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by hslamba on 12/24/16.
 */
public class main {

    public static void main(String[] args) throws IOException, TooBusyException, InterruptedException, ExecutionException {
//        SM1 sm1 = new SM1();
//        sm1.openFile();
//        sm1.readFile();
//        sm1.closeFile();
//
//        Thread t1 = new Thread(sm1);
//        t1.start();

        SM2 sm2 = new SM2();
        for (int i=0 ; i<1000 ; i++){
            sm2.openFile();
            sm2.readFile();
            sm2.closeFile();

            Thread thread = new Thread(sm2);
            thread.start();
        }




//        Thread.sleep(1000);
//        Thread t2 = new Thread(sm2);
//        t2.start();


//        SM3 sm3 = new SM3();
////        Thread.sleep(11000);
//        sm3.read();
//        sm3.checkPattern();
//
//        Thread t3 = new Thread(String.valueOf(sm3));
//        t3.start();
//
//        SM4 sm4 = new SM4();
//        Thread.sleep(20000);
//        sm4.openFile();
//        sm4.readFile();
//        sm4.closeFile();
//        sm4.run();
//
//
//        Thread t4 = new Thread(sm4);
//        t4.start();

    }

}



