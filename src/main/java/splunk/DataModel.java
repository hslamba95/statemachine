//package splunk;
//
//import com.splunk.*;
//
//import java.io.IOException;
//
///**
// * Created by hslamba on 1/4/17.
// */
//public class DataModel {
//    public static void main(String[] args) throws InterruptedException, IOException {
//        ServiceArgs serviceArgs = new ServiceArgs();
//        serviceArgs.setUsername("admin");
//        serviceArgs.setPassword("hsWalker4343");
//        serviceArgs.setHost("localhost");
//        serviceArgs.setPort(8089);
//
//        HttpService.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2);
//        Service splunkService = Service.connect(serviceArgs);
//
//        System.out.println(splunkService.getToken());
//
//
//        // Get the collection of data models.
//        DataModelCollection dataModelCollection = splunkService.getDataModels();
//        for (com.splunk.DataModel name : dataModelCollection.values()){
//            System.out.println(name);
//        }
//        // Get the specified data model.
//        com.splunk.DataModel dataModel = dataModelCollection.get("internal_audit_logs").;
//
//        System.out.println("Data model named " + dataModel.getDisplayName() + " (internal name: " + dataModel.getName() + ")");
//        System.out.println("Description:");
//        System.out.println(dataModel.getDescription());
//
//        for (DataModelObject object : dataModel.getObjects()) {
//            System.out.println("Object: " + object.getDisplayName() + " (internal name: " + object.getName() + ")");
//        }
//
////        assert dataModel.containsObject("Purchase_Request");
////        DataModelObject searches = dataModel.getObject("Purchase_Request");
////        System.out.println("Object: " + searches.getDisplayName() + " (internal name: " + searches.getName() + ")");
////
////        int offset = 0;
////        for (String entry : searches.getLineage()) {
////            for (int i = 0; i < offset; i++) System.out.print(" ");
////            System.out.println(entry);
////            offset += 2;
////        }
////
////
////
////
////
//////     // Iterate over all fields.
//////        for (DataModelField field : searches.getFields()) {
//////             System.out.println("Field " + field.getDisplayName() + " (internal name: " + field.getName() + ")");
//////             System.out.print("  defined on ");
//////             boolean first = true;
//////             for (String entry : field.getOwnerLineage()) {
//////                  if (!first)
//////                       System.out.print(" -> ");
//////                  System.out.print(entry);
//////                  first = false;
//////             }
//////             System.out.println("  of type " + field.getType().toString());
//////             // Will this field potentially contain multiple values?
//////             System.out.println("  multivalued: " + field.isMultivalued());
//////             // Must this field appear on an event in this object?
//////             System.out.println("  isRequired: " + field.isRequired());
//////             // Should the field be displayed to the user if you are writing an
//////             // interface to a data model?
//////             System.out.println("  isHidden: " + field.isHidden());
//////             // Can you edit this field? Typically system fields or fields
//////             // inherited from other objects cannot be edited.
//////             System.out.println("  isEditable: " + field.isEditable());
//////        }
//////
////        // Run a query, appending "| head5" to get just the first five events.
////        Job firstFiveEntries = searches.runQuery("| head 5");
////        while (!firstFiveEntries.isDone()) {
////            Thread.sleep(100);
////        }
////
////        // Print results using an XML reader.
////        ResultsReaderXml results = new ResultsReaderXml(firstFiveEntries.getResults());
////        for (Event event : results) {
////            System.out.println(event.toString());
////        }
////
////        // Create a pivot on the data model object.
////        PivotSpecification pivotSpecification = searches.createPivotSpecification();
////
////        // Split the events in the object into groups with:
////        //   distinct user
////        //   ranges of execution time (but no more than four bins)
////        // Produce a list of the distinct search queries for each cell.
////        pivotSpecification.addRowSplit("productId", "Executing user").
////                addColumnSplit("status", null, null, null, 4).
////                addCellValue("price", "Search Query", StatsFunction.DISTINCT_VALUES);
////
////        // Retrieve the pivot's corresponding queries.
////        Pivot pivot = pivotSpecification.pivot();
////
////        // Print the pivot's SPL query.
////        System.out.println("Query for binning search queries by execution time and executing user:");
////        System.out.println("  " + pivot.getPrettyQuery());
////
////        // Run the pivot.
////        Job pivotJob = pivot.run();
////        while (!pivotJob.isDone()) {
////            Thread.sleep(100);
////        }
////
////        // Print results using an XML reader.
////        results = new ResultsReaderXml(pivotJob.getResults());
////        for (Event event : results) {
////            System.out.println(event.toString());
////        }
////
////        InputCollection myInputs = splunkService.getInputs();
////
////        // Iterate and list the collection of inputs
////        System.out.println("There are " + myInputs.size() + " data inputs:\n");
////        for (Input entity: myInputs.values()) {
////            System.out.println("  " + entity.getName() + " (" + entity.getKind() + ")");
////        }
//
//    }
//}
