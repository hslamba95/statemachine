package statefulj;

import org.statefulj.fsm.FSM;
import org.statefulj.fsm.RetryException;
import org.statefulj.fsm.TooBusyException;
import org.statefulj.fsm.model.Action;
import org.statefulj.fsm.model.State;
import org.statefulj.fsm.model.impl.StateImpl;
import org.statefulj.persistence.memory.MemoryPersisterImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by hslamba on 12/24/16.
 */
public class SM2 implements Runnable{

    List<String> eventsList1;
    List<State<Foo>> statesList1;
    List<Action<Foo>> actionsList1;
    private Scanner readStateFile;
    private Scanner readEventFile;
    private Scanner readActionFile;

    MemoryPersisterImpl<Foo> persister;

    public void openFile() throws FileNotFoundException {
        readStateFile = new Scanner(new File("states.txt"));
        readEventFile = new Scanner(new File("events.txt"));
        readActionFile = new Scanner(new File("actions.txt"));
    }

    public void readFile() throws InterruptedException {

        // Events in StatefulJ are Strings.
        String eventA = readEventFile.next();
        String eventB = readEventFile.next();
        String eventC = readEventFile.next();
        String eventD = readEventFile.next();
        String eventE = readEventFile.next();


        //  A State defines the state value for an Entity and holds the mapping of all Transitions for that State.


        String stateA1 = readStateFile.next();
        State<Foo> stateA = new StateImpl<Foo>(stateA1);

        String stateB1 = readStateFile.next();
        final State<Foo> stateB = new StateImpl<Foo>(stateB1);

        String stateC1 = readStateFile.next();
        final State<Foo> stateC = new StateImpl<Foo>(stateC1);

        String stateD1 = readStateFile.next();
        final State<Foo> stateD = new StateImpl<Foo>(stateD1);

        String stateE1 = readStateFile.next();
        final State<Foo> stateE = new StateImpl<Foo>(stateE1);

        String stateF1 = readStateFile.next();
        final State<Foo> stateF = new StateImpl<Foo>(stateF1, true);


        //Actions

        String action1 = readActionFile.next();
        Action<Foo> actionA = new MainAction(action1);

        String action2 = readActionFile.next();
        Action<Foo> actionB = new MainAction(action2);

        String action3 = readActionFile.next();
        Action<Foo> actionC = new MainAction(action3);

        String action4 = readActionFile.next();
        Action<Foo> actionD = new MainAction(action4);


        //List for adding events
        eventsList1 = new LinkedList<String>();
        eventsList1.add(eventA);
        eventsList1.add(eventB);
        eventsList1.add(eventC);
        eventsList1.add(eventD);
        eventsList1.add(eventE);

        //List for adding states
        statesList1 = new LinkedList<State<Foo>>();
        statesList1.add(stateA);
        statesList1.add(stateB);
        statesList1.add(stateC);
        statesList1.add(stateD);
        statesList1.add(stateE);
        statesList1.add(stateF);

        //List for adding actions
        actionsList1 = new LinkedList<Action<Foo>>();
        actionsList1.add(actionA);
        actionsList1.add(actionB);
        actionsList1.add(actionC);
        actionsList1.add(actionD);

//        for (String tempEvents : eventsList1) {
//
//            Thread.sleep(500);
//            System.out.println(tempEvents);
//        }

         /*Deterministic Transition
            A Deterministic Transition means that for a given State and Event, there is only a single Transition.
         */
        for (State<Foo> tempStates : statesList1) {
            Thread.sleep(500);
            tempStates.addTransition(eventB, stateC, actionB);
            tempStates.addTransition(eventA, stateB, actionA);
            tempStates.addTransition(eventD, stateE, actionC);
            tempStates.addTransition(eventC, stateD, actionD);

//            System.out.println(tempStates);

        }

//        for (Action<Foo> tempActions : actionsList1) {
//
//            Thread.sleep(500);
//            System.out.println(tempActions);
//        }

    }

    public void closeFile() {
        readStateFile.close();
        readEventFile.close();
        readActionFile.close();
    }


    public void run() {
        persister = new MemoryPersisterImpl<Foo>(statesList1, statesList1.iterator().next());  // Set of States and Start State
        FSM<Foo> fsm = new FSM<Foo>("Foo FSM", persister);

        Foo foo = new Foo();

        for (int i = 0; i < eventsList1.size(); i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                fsm.onEvent(foo, eventsList1.get(i));  // stateA(EventA) -> stateB/actionA
            } catch (TooBusyException e) {
                e.printStackTrace();
            }

            foo.setBar(true);

        }
    }
}

class MainAction<T> implements Action<T> {
    String what;

    public MainAction(String what) {
        this.what = what;
    }


    //Main Method

    public void execute(T stateful, String event, Object... args) throws RetryException {

        System.out.println("Hi " + what);


    }


}
