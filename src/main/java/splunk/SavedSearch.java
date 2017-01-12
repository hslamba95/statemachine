package splunk;

import com.splunk.*;
import com.splunk.User;

/**
 * Created by hslamba on 1/4/17.
 */
public class SavedSearch {
    public static void main(String[] args) {
        ServiceArgs serviceArgs = new ServiceArgs();
        serviceArgs.setUsername("admin");
        serviceArgs.setPassword("hsWalker4343");
        serviceArgs.setHost("localhost");
        serviceArgs.setPort(8089);

        HttpService.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2);
        Service splunkService = Service.connect(serviceArgs);

        System.out.println(splunkService.getToken());

        // List all saved searches for the current namespace
        SavedSearchCollection savedSearches = splunkService.getSavedSearches();
        System.out.println(savedSearches.size() + " saved searches are available to the current user:\n");
        for (com.splunk.SavedSearch entity : savedSearches.values()) {
            System.out.println("     " + entity.getName());
        }

        // Get the collection of users and save the name of the last user
        UserCollection users = splunkService.getUsers();
        String lastUser = null;
        for (User user : users.values()) {
            lastUser = user.getName();
        }

        // Specify a namespace using the name of the last user
        ServiceArgs namespace = new ServiceArgs();
        namespace.setApp("search");
        namespace.setOwner(lastUser);
        SavedSearchCollection savedSearches2 = splunkService.getSavedSearches(namespace);

        System.out.println(savedSearches2.size() + " saved searches are available to '" + lastUser + "':\n");
        for (com.splunk.SavedSearch search : savedSearches2.values()) {
            System.out.println("     " + search.getName());
        }


        // Retrieve the collection of saved searches
        SavedSearchCollection savedSearches1 = splunkService.getSavedSearches();

        // Iterate through the collection of saved searches and display the history for each one
        for (com.splunk.SavedSearch entity : savedSearches1.values()) {
            Job[] sHistory = entity.history();
            System.out.println("\n" + sHistory.length + " jobs for the '" + entity.getName() + "' saved search");
            for (int i = 0; i < sHistory.length; ++i) {
                System.out.println("     " + sHistory[i].getEventCount() + " events for Search ID " + sHistory[i].getSid() + "\n");
            }
        }

        // Retrieve the new saved search
        com.splunk.SavedSearch savedSearch2 = splunkService.getSavedSearches().get("Purchases by Product");

        // Run a saved search and poll for completion
        System.out.println("Run the '" + savedSearch2.getName() + "' search ("
                + savedSearch2.getSearch() + ")\n");
        Job jobSavedSearch = null;

        // Run the saved search
        try {
            jobSavedSearch = savedSearch2.dispatch();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        System.out.println("Waiting for the job to finish...\n");

        // Wait for the job to finish
        while (!jobSavedSearch.isDone()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

//     // Create a saved search by specifying a name and search query
//     // Note: Do not include the 'search' keyword for a saved search
//     String myQuery = "* | head 10";
//     String mySearchName = "Test Search";
//     com.splunk.SavedSearch savedSearch = splunkService.getSavedSearches().create(mySearchName, myQuery);
//     System.out.println("The search '" + savedSearch.getName() +
//             "' (" + savedSearch.getSearch() + ") was saved");

     // Retrieve a saved search
        com.splunk.SavedSearch savedSearch3 = splunkService.getSavedSearches().get("Test Search");

        // Delete the saved search
        savedSearch3.remove();
    }


}
