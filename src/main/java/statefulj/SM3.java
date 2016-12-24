package statefulj;

import org.statefulj.fsm.FSM;
import org.statefulj.fsm.RetryException;
import org.statefulj.fsm.TooBusyException;
import org.statefulj.fsm.model.Action;
import org.statefulj.fsm.model.State;
import org.statefulj.fsm.model.impl.StateImpl;
import org.statefulj.persistence.memory.MemoryPersisterImpl;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hslamba on 12/24/16.
 */
public class SM3 implements Runnable {

    String input;

    List<String> eventsList2;
    List<State<Foo>> statesList2;
    List<Action<Foo>> actionsList2;
    private Scanner readStateFile;
    private Scanner readEventFile;
    private Scanner readActionFile;
    String eventA;


    MemoryPersisterImpl<Foo> persister;



    public void openFile() throws FileNotFoundException {
        readStateFile = new Scanner(new File("states.txt"));
//        readEventFile = new Scanner(new File("events.txt"));
        readActionFile = new Scanner(new File("actions.txt"));


    }

    public void readFile() throws InterruptedException, IOException {

        // Events in StatefulJ are Strings.
//        String eventA = readEventFile.next();
//        String eventB = readEventFile.next();
//        String eventC = readEventFile.next();
//        String eventD = readEventFile.next();
//        String eventE = readEventFile.next();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the pattern you want to match...");
        input = scanner.nextLine();

        System.out.println("Input = " +input);

        Pattern patt = Pattern.compile(input);
        BufferedReader r = new BufferedReader(new FileReader("matching.txt"));
        String line;
        while ((line = r.readLine()) != null){
            Matcher m = patt.matcher(line);
            while (m.find()){
                eventA  = line;

            }
        }





        //  A State defines the state value for an Entity and holds the mapping of all Transitions for that State.


        String stateA1 = readStateFile.next();
        State<Foo> stateA = new StateImpl<Foo>(stateA1);

        String stateB1 = readStateFile.next();
        final State<Foo> stateB = new StateImpl<Foo>(stateB1);

        String stateC1 = readStateFile.next();
        final State<Foo> stateC = new StateImpl<Foo>(stateC1,true);
//
//        String stateD1 = readStateFile.next();
//        final State<Foo> stateD = new StateImpl<Foo>(stateD1);
//
//        String stateE1 = readStateFile.next();
//        final State<Foo> stateE = new StateImpl<Foo>(stateE1);
//
//        String stateF1 = readStateFile.next();
//        final State<Foo> stateF = new StateImpl<Foo>(stateF1, true);


        //Actions

        String action1 = readActionFile.next();
        Action<Foo> actionA = new MatchingAction<Foo>(action1);

        String action2 = readActionFile.next();
        Action<Foo> actionB = new MatchingAction<Foo>(action2);
//
//        String action3 = readActionFile.next();
//        Action<Foo> actionC = new MatchingAction<Foo>(action3);
//
//        String action4 = readActionFile.next();
//        Action<Foo> actionD = new MatchingAction<Foo>(action4);



        //List for adding events
        eventsList2 = new LinkedList<String>();
        eventsList2.add(eventA);
//        eventsList2.add(eventB);
//        eventsList2.add(eventC);
//        eventsList2.add(eventD);
//        eventsList2.add(eventE);

        System.out.println(eventA);

        //List for adding states
        statesList2 = new LinkedList<State<Foo>>();
        statesList2.add(stateA);
        statesList2.add(stateB);
        statesList2.add(stateC);
//        statesList2.add(stateD);
//        statesList2.add(stateE);
//        statesList2.add(stateF);

        stateA.addTransition(eventA,stateB,actionA);

        //List for adding actions
        actionsList2 = new LinkedList<Action<Foo>>();
        actionsList2.add(actionA);
        actionsList2.add(actionB);
//        actionsList2.add(actionC);
//        actionsList2.add(actionD);


        for (State<Foo> tempStates : statesList2) {
            Thread.sleep(500);
//            tempStates.addTransition(eventB, stateC, actionB);
            tempStates.addTransition(eventA, stateB, actionA);
//            tempStates.addTransition(eventD, stateE, actionC);
//            tempStates.addTransition(eventC, stateD, actionD);


        }

    }

    public void closeFile() {
        readStateFile.close();
        readEventFile.close();
        readActionFile.close();
    }


    @Override
    public void run() {
        persister = new MemoryPersisterImpl<Foo>(statesList2, statesList2.iterator().next());  // Set of States and Start State
        FSM<Foo> fsm = new FSM<Foo>("Foo FSM", persister);

        Foo foo = new Foo();

        for (int i = 0; i < eventsList2.size(); i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                fsm.onEvent(foo, eventsList2.get(i));  // stateA(EventA) -> stateB/actionA
            } catch (TooBusyException e) {
                e.printStackTrace();
            }

            foo.setBar(true);

        }
    }
}

class MatchingAction<T> implements Action<T> {
    String what;

    public MatchingAction(String what) {
        this.what = what;
    }




    //Main Method

    public void execute(T stateful, String event, Object... args) throws RetryException {

        System.out.println("SSA " + what);


    }


}
