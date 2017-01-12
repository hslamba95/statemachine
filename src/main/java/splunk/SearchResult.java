package splunk;

import com.splunk.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by hslamba on 1/4/17.
 */


public class SearchResult{
    public static void main(String[] args) throws InterruptedException, IOException {

        ServiceArgs serviceArgs = new ServiceArgs();
        serviceArgs.setUsername("admin");
        serviceArgs.setPassword("hsWalker4343");
        serviceArgs.setHost("localhost");
        serviceArgs.setPort(8089);

        HttpService.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2);
        Service splunkService = Service.connect(serviceArgs);

        System.out.println(splunkService.getToken());



        // Create a simple search job
        String mySearch = "search * | head 5 | search VendorID=5036";
        Job job = splunkService.getJobs().create(mySearch);

        // Wait for the job to finish
        while (!job.isDone()) {
            Thread.sleep(500);
        }

        // Display results
        InputStream results = job.getResults();
        String line = null;
        System.out.println("Results from the search job as XML:\n");
        BufferedReader br = new BufferedReader(new InputStreamReader(results, "UTF-8"));
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        br.close();

     // Set up the job properties
        String mySearch1 = "search * | head 50000 | search VendorID=5036";

        // Create an argument map for the job arguments:
        JobArgs jobArgs = new JobArgs();
        jobArgs.setExecutionMode(JobArgs.ExecutionMode.NORMAL);

        // Create the job
        Job job1 = splunkService.search(mySearch1, jobArgs);
        job1.enablePreview();
        job1.update();

        // Wait for the job to be ready
        while (!job1.isReady()) {
            Thread.sleep(500);
        }

        // Display previews using the built-in XML parser
        int countPreview=0;  // count the number of previews displayed
        int countBatch=0;    // count the number of times previews are retrieved
        while (!job1.isDone()) {
            JobResultsPreviewArgs previewargs = new JobResultsPreviewArgs();
            previewargs.setCount(500);  // Get 500 previews at a time
            previewargs.setOutputMode(JobResultsPreviewArgs.OutputMode.XML);

            InputStream results1 =  job1.getResultsPreview(previewargs);
            ResultsReaderXml resultsReader = new ResultsReaderXml(results1);
            HashMap<String, String> event;
            while ((event = resultsReader.getNextEvent()) != null) {
                System.out.println("BATCH " + countBatch + "\nPREVIEW " + countPreview++ + " ********");
                for (String key: event.keySet())
                    System.out.println("   " + key + ":  " + event.get(key));
            }
            countBatch++;
            resultsReader.close();
        }
        System.out.println("Job is done with " + job1.getResultCount() + " results");


//  // Set up the job properties
//     String mySearch2 = "search index=_internal | head 10";
//
//     // Set up a real-time export with a 30-second window
//     JobExportArgs jobArgs2 = new JobExportArgs();
//     jobArgs2.setSearchMode(JobExportArgs.SearchMode.REALTIME);
//     jobArgs2.setEarliestTime("rt-30s");
//     jobArgs2.setLatestTime("rt");
//     jobArgs2.setOutputMode(JobExportArgs.OutputMode.XML);
//
//     // Create the job
//     InputStream exportStream = splunkService.export(mySearch2, jobArgs2);
//
//     //Display previews
//     MultiResultsReaderXml multiResultsReader =
//             new MultiResultsReaderXml(exportStream);
//
//     int counterSet = 0;  // count the number of results sets
//     for (SearchResults searchResults : multiResultsReader)
//     {
//         System.out.println("Result set " + counterSet++ + " ********");
//         int counterEvent = 0;  // count the number of events in each set
//         for (Event event : searchResults) {
//             System.out.println("Event " + counterEvent++ + " --------");
//             for (String key: event.keySet())
//                 System.out.println("   " + key + ":  " + event.get(key));
//         }
//     }
//
//     multiResultsReader.close();
//


//  // Set up the job properties
//     String mySearch = "search * | stats count by host";
//
//     JobExportArgs jobArgs = new JobExportArgs();
//     jobArgs.setOutputMode(JobExportArgs.OutputMode.XML);
//
//     // Create the job
//     InputStream stream = splunkService.export(mySearch, jobArgs);
//
//     //Display previews
//     MultiResultsReaderXml multiResultsReader =
//             new MultiResultsReaderXml(stream);
//
//     int counterSet = 0;
//     for (SearchResults searchResults : multiResultsReader)
//     {
//         // Display whether the results is a preview (search in progress) or
//         // final (search is finished)
//         String resultSetType = searchResults.isPreview() ? "Preview":"Final";
//         System.out.println(resultSetType + " result set " + counterSet++ + " ********");
//         int counterEvent = 0;
//         for (Event event : searchResults) {
//             System.out.println("Event " + counterEvent++ + " --------");
//             for (String key: event.keySet())
//                 System.out.println("   " + key + ":  " + event.get(key));
//         }
//     }
//
//     multiResultsReader.close();


    }
}
