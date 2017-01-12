package statefulj;

import com.splunk.*;
import org.statefulj.fsm.FSM;
import org.statefulj.fsm.RetryException;
import org.statefulj.fsm.TooBusyException;
import org.statefulj.fsm.model.Action;
import org.statefulj.fsm.model.State;
import org.statefulj.fsm.model.impl.StateImpl;
import org.statefulj.persistence.memory.MemoryPersisterImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hslamba on 1/5/17.
 */
public class SM6 {


    Service splunkService;
    String eventA;
    String eventB;

    State<Foo> stateA = new StateImpl<Foo>("State A");
    State<Foo> stateB = new StateImpl<Foo>("State B");
    State<Foo> stateC = new StateImpl<Foo>("State C", true); // End State

    Action<Foo> actionA = new SsaAction1("Surinder");
    Action<Foo> actionB = new SsaAction1("Folks");

    String mySearch = "search * | head 50000 | search VendorID=5036";

    JobArgs jobArgs = new JobArgs();

    Job job;

    List<State<Foo>> states;



    public void splunkConnection() {

        ServiceArgs serviceArgs = new ServiceArgs();
        serviceArgs.setUsername("admin");
        serviceArgs.setPassword("hsWalker4343");
        serviceArgs.setHost("localhost");
        serviceArgs.setPort(8089);

        HttpService.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2);

        splunkService = Service.connect(serviceArgs);
        System.out.println(splunkService.getToken());

    }

    public void stateMachine6() throws InterruptedException, IOException, TooBusyException {


        jobArgs.setExecutionMode(JobArgs.ExecutionMode.NORMAL);
        job = splunkService.search(mySearch, jobArgs);;
        job.enablePreview();
        job.update();

        // Wait for the job to be ready
        while (!job.isReady()) {
            Thread.sleep(500);
        }

        int countPreview=0;  // count the number of previews displayed
        int countBatch=0;    // count the number of times previews are retrieved

        while (!job.isDone()){

            JobResultsPreviewArgs previewargs = new JobResultsPreviewArgs();
            previewargs.setCount(500);  // Get 500 previews at a time
            previewargs.setOutputMode(JobResultsPreviewArgs.OutputMode.XML);

            InputStream results =  job.getResultsPreview(previewargs);
            ResultsReaderXml resultsReader = new ResultsReaderXml(results);
            HashMap<String, String> event;
            while ((event = resultsReader.getNextEvent()) != null) {
                System.out.println("BATCH " + countBatch + "\nPREVIEW " + countPreview++ + " ********");
                for (String key: event.keySet())
                    System.out.println("   " + key + ":  " + event.get(key));
            }



            states = new LinkedList<State<Foo>>();
            states.add(stateA);
            states.add(stateB);
            states.add(stateC);
            stateA.addTransition(eventA,stateB,actionA);
            countBatch++;
            resultsReader.close();


        }
        System.out.println("Job is done with " + job.getResultCount() + " results");

        MemoryPersisterImpl<Foo> persister = new MemoryPersisterImpl<Foo>(states, states.get(0));  // Start State

        FSM<Foo> fsm = new FSM<Foo>("Foo FSM", persister);


        Foo foo = new Foo();

        fsm.onEvent(foo, eventA);  // stateA(EventA) -> stateB/actionA

        foo.setBar(true);

        fsm.onEvent(foo, eventA);  // stateB(EventA) -> stateB/NOOP

        foo.setBar(false);

        fsm.onEvent(foo, eventA);  // stateB(EventA) -> stateC/NOOP






    }

    public static void main(String[] args) throws IOException, InterruptedException, TooBusyException {

            SM6 sm6 = new SM6();
            sm6.splunkConnection();
            sm6.stateMachine6();

    }


}

class SsaAction1<T> implements Action<T> {

    String what;

    public SsaAction1(String what) {
        this.what = what;
    }

    public void execute(T stateful, String event, Object... args) throws RetryException {
        System.out.println("SSA " + what);
        System.out.println("State Changed Successfully of State Machine 6");
    }
}



