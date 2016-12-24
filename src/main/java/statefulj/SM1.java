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
 * Created by hslamba on 12/23/16.
 */
public class SM1 implements Runnable{

    List<String> eventsList;
    List<State<Foo>> statesList;
    List<Action<Foo>> actionsList;
    private Scanner readStateFile;
    private Scanner readEventFile;
    private Scanner readActionFile;


    public void openFile() throws FileNotFoundException {
        readStateFile = new Scanner(new File("states.txt"));
        readEventFile = new Scanner(new File("events.txt"));
        readActionFile = new Scanner(new File("actions.txt"));
    }

    public void readFile() throws InterruptedException, TooBusyException {

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
        Action<Foo> actionA = new HelloAction(action1);

        String action2 = readActionFile.next();
        Action<Foo> actionB = new HelloAction(action2);

        String action3 = readActionFile.next();
        Action<Foo> actionC = new HelloAction(action3);

        String action4 = readActionFile.next();
        Action<Foo> actionD = new HelloAction(action4);


        //List for adding events
        eventsList = new LinkedList<String>();
        eventsList.add(eventA);
        eventsList.add(eventB);
        eventsList.add(eventC);
        eventsList.add(eventD);
        eventsList.add(eventE);

        //List for adding states
        statesList = new LinkedList<State<Foo>>();
        statesList.add(stateA);
        statesList.add(stateB);
        statesList.add(stateC);
        statesList.add(stateD);
        statesList.add(stateE);
        statesList.add(stateF);

        //List for adding actions
        actionsList = new LinkedList<Action<Foo>>();
        actionsList.add(actionA);
        actionsList.add(actionB);
        actionsList.add(actionC);
        actionsList.add(actionD);

        for (String tempEvents : eventsList) {

            Thread.sleep(500);
            System.out.println(tempEvents);
        }


         /*Deterministic Transition
            A Deterministic Transition means that for a given State and Event, there is only a single Transition.
         */
        for (State<Foo> tempStates : statesList) {
            Thread.sleep(500);

            tempStates.addTransition(eventA, stateB, actionA);
            tempStates.addTransition(eventB, stateC, actionB);
            tempStates.addTransition(eventC, stateD, actionC);
            tempStates.addTransition(eventD, stateE, actionD);

            System.out.println(tempStates);

        }

        for (Action<Foo> tempActions : actionsList) {

            Thread.sleep(500);
            System.out.println(tempActions);
        }

         /*
             A Persister is a Class Responsible for persisting the State value for a Stateful Entity.
             A Persister implements the Persister interface and must ensure that updates are atomic, isolated and thread-safe.
             The Stateful FSM library comes with an in-memory Persister which maintains the State only on the in-memory Stateful Entity.
             If you need to persist to a database, you will need to use one of the Database Persisters or integrate the StatefulJ Framework.
        */


    }



    public void closeFile() {
        readStateFile.close();
        readEventFile.close();
        readActionFile.close();
    }


    @Override
    public void run() {

        MemoryPersisterImpl<Foo> persister = new MemoryPersisterImpl<Foo>(statesList, statesList.iterator().next());  // Set of States and Start State

        FSM<Foo> fsm = new FSM<Foo>("Foo FSM", persister);

        Foo foo = new Foo();

        for (int i = 0; i < eventsList.size(); i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                fsm.onEvent(foo, eventsList.get(i));  // stateA(EventA) -> stateB/actionA

            } catch (TooBusyException e) {
                e.printStackTrace();
            }

            foo.setBar(true);

        }
    }
}


    class HelloAction<T> implements Action<T> {
        String what;

        public HelloAction(String what) {
            this.what = what;
        }


        //Main Method

        public void execute(T stateful, String event, Object... args) throws RetryException {

            System.out.println("Hello " + what);
            System.out.println("State Changed Successfully of State Machine 1");


        }


    }



