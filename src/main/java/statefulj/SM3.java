package statefulj;

import org.statefulj.fsm.FSM;
import org.statefulj.fsm.RetryException;
import org.statefulj.fsm.TooBusyException;
import org.statefulj.fsm.model.Action;
import org.statefulj.fsm.model.State;
import org.statefulj.fsm.model.impl.StateImpl;
import org.statefulj.persistence.memory.MemoryPersisterImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hslamba on 12/24/16.
 */
public class SM3 {

    // Events
//
    String eventA;
    String eventB;
    String input;

    State<Foo> stateA = new StateImpl<Foo>("State A");
    State<Foo> stateB = new StateImpl<Foo>("State B");
    State<Foo> stateC = new StateImpl<Foo>("State C", true); // End State

    Action<Foo> actionA = new SsaAction("Surinder");
    Action<Foo> actionB = new SsaAction("Folks");

    public void read() throws TooBusyException {

    }

    public void checkPattern() throws IOException, TooBusyException, InterruptedException {
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
                System.out.println("Match Done... Making Transistion to change states..");
                Thread.sleep(1000);
                stateA.addTransition(eventA, stateB, actionA);
                stateB.addTransition(eventB,stateC,actionB);
                List<State<Foo>> states = new LinkedList<State<Foo>>();
                states.add(stateA);
                states.add(stateB);
                states.add(stateC);



                MemoryPersisterImpl<Foo> persister = new MemoryPersisterImpl<Foo>(states,states.get(0));  // Start State

                FSM<Foo> fsm = new FSM<Foo>("Foo FSM", persister);


                Foo foo = new Foo();

                fsm.onEvent(foo, eventA);  // stateA(EventA) -> stateB/actionA

                foo.setBar(true);

                fsm.onEvent(foo, eventA);  // stateB(EventA) -> stateB/NOOP

                foo.setBar(false);

                fsm.onEvent(foo, eventA);  // stateB(EventA) -> stateC/NOOP

            }

        }
    }

}


class SsaAction<T> implements Action<T> {

    String what;

    public SsaAction(String what) {
        this.what = what;
    }

    public void execute(T stateful,
                        String event,
                        Object ... args) throws RetryException {
        System.out.println("SSA " + what);
        System.out.println("State Changed Successfully of State Machine 3");
    }
}
