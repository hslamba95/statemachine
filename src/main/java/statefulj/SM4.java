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
public class SM4 {

    // Events
//
    String eventA;
    String input;

    State<Foo> stateA = new StateImpl<Foo>("State A");
    State<Foo> stateB = new StateImpl<Foo>("State B");
    State<Foo> stateC = new StateImpl<Foo>("State C", true); // End State

    Action<Foo> actionA = new SsaAction("World");
    Action<Foo> actionB = new SsaAction("Folks");

    public void read() throws TooBusyException {

    }

    public void checkPattern() throws IOException, TooBusyException {
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
                System.out.println("Match Done");
                stateA.addTransition(eventA, stateB, actionA);
                List<State<Foo>> states = new LinkedList<State<Foo>>();
                states.add(stateA);
                states.add(stateB);
                states.add(stateC);



                MemoryPersisterImpl<Foo> persister = new MemoryPersisterImpl<Foo>(states,stateA);  // Start State

                FSM<Foo> fsm = new FSM<Foo>("Foo FSM", persister);


                Foo foo = new Foo();

// Drive the FSM with a series of events: eventA, eventA, eventA
//
                fsm.onEvent(foo, eventA);  // stateA(EventA) -> stateB/actionA

                foo.setBar(true);

                fsm.onEvent(foo, eventA);  // stateB(EventA) -> stateB/NOOP

                foo.setBar(false);

                fsm.onEvent(foo, eventA);  // stateB(EventA) -> stateC/NOOP

            }

        }
    }
    public static void main(String[] args) throws TooBusyException, IOException {
        SM4 sm4 = new SM4();
        sm4.read();
        sm4.checkPattern();
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
        System.out.println("Hello " + what);
    }
}
