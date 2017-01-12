package splunk;

import com.splunk.*;

/**
 * Created by hslamba on 1/4/17.
 */
public class DataInput {
    public static void main(String[] args) {
        ServiceArgs serviceArgs = new ServiceArgs();
        serviceArgs.setUsername("admin");
        serviceArgs.setPassword("hsWalker4343");
        serviceArgs.setHost("localhost");
        serviceArgs.setPort(8089);

        HttpService.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2);
        Service splunkService = Service.connect(serviceArgs);

        System.out.println(splunkService.getToken());

        // Retrieve the collection of data inputs
        InputCollection myInputs = splunkService.getInputs();


        // Iterate and list the collection of inputs
        System.out.println("There are " + myInputs.size() + " data inputs:\n");
        for (Input entity: myInputs.values()) {
            System.out.println("  " + entity.getName() + " (" + entity.getKind() + ")");
        }





//     // Create a new Monitor data input
//        String monitor_filepath = "/Applications/splunk/readme-splunk.txt";
//        MonitorInput monitorInput= myInputs.create(monitor_filepath, InputKind.Monitor);



        // Retrieve the new input
        String testinput = "/Applications/splunk/readme-splunk.txt";
        MonitorInput monitorInput = (MonitorInput) splunkService.getInputs().get(testinput);

        // Retrieve and display some properties for the new input
        System.out.println("Name:      " + monitorInput.getName());
        System.out.println("Kind:      " + monitorInput.getKind());
        System.out.println("Path:      " + monitorInput.getPath());
        System.out.println("Index:     " + monitorInput.getIndex());
        System.out.println("Whitelist: " + monitorInput.getWhitelist());



        // Retrieve the collection of indexes, sorted by number of events
        IndexCollectionArgs indexcollArgs = new IndexCollectionArgs();
        indexcollArgs.setSortKey("totalEventCount");
        indexcollArgs.setSortDirection(IndexCollectionArgs.SortDirection.DESC);
        IndexCollection myIndexes = splunkService.getIndexes(indexcollArgs);

        // List the indexes and their event counts
        System.out.println("There are " + myIndexes.size() + " indexes:\n");
        for (Index entity: myIndexes.values()) {
            System.out.println("  " + entity.getName() + " (events: "
                    + entity.getTotalEventCount() + ")");
        }
    }
}
