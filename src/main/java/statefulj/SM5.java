package statefulj;

import org.statefulj.fsm.FSM;
import org.statefulj.fsm.TooBusyException;
import org.statefulj.fsm.model.Action;
import org.statefulj.fsm.model.State;
import org.statefulj.fsm.model.impl.StateImpl;
import org.statefulj.persistence.memory.MemoryPersisterImpl;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hslamba on 12/30/16.
 */
public class SM5 {
    String eventA;
    String eventB;
    String input;

    State<Foo> stateA = new StateImpl<Foo>("State A");
    State<Foo> stateB = new StateImpl<Foo>("State B");
    State<Foo> stateC = new StateImpl<Foo>("State C", true); // End State

    Action<Foo> actionA = new SsaAction("Surinder");
    Action<Foo> actionB = new SsaAction("Folks");

    public void add() throws InterruptedException, TooBusyException {

        int a=10;
        int b=30;

        int c = a + b;

        List<State<Foo>> states = new LinkedList<State<Foo>>();
        states.add(stateA);
        states.add(stateB);
        states.add(stateC);


        if (c>30){
            for (int i=1 ; i<1000000000 ; i++){
                Thread.sleep(0);
                System.out.println(c);
            }

            stateA.addTransition(eventA,stateB,actionA);
        }

        else {
            System.out.println(c);
            stateA.addTransition(eventB,stateC,actionB);
        }

        MemoryPersisterImpl<Foo> persister = new MemoryPersisterImpl<Foo>(states, states.get(0));  // Start State

        FSM<Foo> fsm = new FSM<Foo>("Foo FSM", persister);


        Foo foo = new Foo();

        fsm.onEvent(foo, eventA);  // stateA(EventA) -> stateB/actionA

        foo.setBar(true);

        fsm.onEvent(foo, eventA);  // stateB(EventA) -> stateB/NOOP

        foo.setBar(false);

        fsm.onEvent(foo, eventA);  // stateB(EventA) -> stateC/NOOP





    }

    public static void main(String[] args) throws InterruptedException, TooBusyException {
        SM5 sm5 = new SM5();
        sm5.add();
    }



}
