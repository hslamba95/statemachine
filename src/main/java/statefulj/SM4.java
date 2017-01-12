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
import java.util.concurrent.*;

/**
 * Created by hslamba on 12/26/16.
 */
public class SM4 implements Runnable {
    List<String> eventsListnew;
    List<State<Foo>> statesListnew;
    List<Action<Foo>> actionsListnew;
    private Scanner readStateFile;
    private Scanner readEventFile;
    private Scanner readActionFile;

    ExecutorService executor = Executors.newSingleThreadExecutor();


    public void openFile() throws FileNotFoundException {
        readStateFile = new Scanner(new File("states.txt"));
        readEventFile = new Scanner(new File("events.txt"));
        readActionFile = new Scanner(new File("actions.txt"));
    }

    public void readFile() throws InterruptedException, TooBusyException, ExecutionException {
        // Events in StatefulJ are Strings.
        final String eventA = readEventFile.next();
        String eventB = readEventFile.next();
        String eventC = readEventFile.next();
        String eventD = readEventFile.next();
        String eventE = readEventFile.next();


        //  A State defines the state value for an Entity and holds the mapping of all Transitions for that State.


        String stateA1 = readStateFile.next();
        final State<Foo> stateA = new StateImpl<Foo>(stateA1);

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
        final Action<Foo> actionA = new HellAction(action1);

        String action2 = readActionFile.next();
        final Action<Foo> actionB = new HellAction(action2);

        String action3 = readActionFile.next();
        final Action<Foo> actionC = new HellAction(action3);

        String action4 = readActionFile.next();
        Action<Foo> actionD = new HellAction(action4);


        //List for adding events
        eventsListnew = new LinkedList<String>();
        eventsListnew.add(eventA);
        eventsListnew.add(eventB);
        eventsListnew.add(eventC);
        eventsListnew.add(eventD);
        eventsListnew.add(eventE);

        //List for adding states
        statesListnew = new LinkedList<State<Foo>>();
        statesListnew.add(stateA);
        statesListnew.add(stateB);
        statesListnew.add(stateC);
        statesListnew.add(stateD);
        statesListnew.add(stateE);
        statesListnew.add(stateF);

        //List for adding actions
        actionsListnew = new LinkedList<Action<Foo>>();
        actionsListnew.add(actionA);
        actionsListnew.add(actionB);
        actionsListnew.add(actionC);
        actionsListnew.add(actionD);


        stateA.addTransition(eventA,stateB,actionA);



        stateB.addTransition(eventB,stateC);
        stateC.addTransition(eventA,stateD,actionC);

    }

    public void closeFile() {
        readStateFile.close();
        readEventFile.close();
        readActionFile.close();
    }

    @Override
    public void run() {
        try {
            System.out.println("Starting the transition in some time.....");
            executor.awaitTermination(5,TimeUnit.SECONDS);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        MemoryPersisterImpl<Foo> persister1 = new MemoryPersisterImpl<Foo>(statesListnew,statesListnew.get(0));  // Set of States and Start State



        FSM<Foo> fsm = new FSM<Foo>("Foo FSM", persister1);

        Foo foo = new Foo();


        for (int i = 0; i < eventsListnew.size(); i++) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                fsm.onEvent(foo, eventsListnew.get(i));  // stateA(EventA) -> stateB/actionA

            } catch (TooBusyException e) {
                e.printStackTrace();
            }

            foo.setBar(true);


        }

        System.out.println("Now i am on State B");
    }


}


class HellAction<T> implements Action<T> {
    String what;

    public HellAction(String what) {
        this.what = what;
    }


    //Main Method

    public void execute(T stateful, String event, Object... args) throws RetryException {

        System.out.println("Bye " + what);
        System.out.println("State Changed Successfully of State Machine 4");


    }


}
