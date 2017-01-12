package splunk;

import com.splunk.*;

/**
 * Created by hslamba on 1/4/17.
 */
public class User {


    public static void main(String[] args) {
        ServiceArgs serviceArgs = new ServiceArgs();
        serviceArgs.setUsername("admin");
        serviceArgs.setPassword("hsWalker4343");
        serviceArgs.setHost("localhost");
        serviceArgs.setPort(8089);

        HttpService.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2);
        Service splunkService = Service.connect(serviceArgs);

        System.out.println(splunkService.getToken());


        // Get the collection of users
        UserCollection usercollection = splunkService.getUsers();


        // List all users, sorted by real name
        Args userArgs = new Args();
        userArgs.put("sort_key", "realname");
        userArgs.put("sort_dir", "asc");
        UserCollection usercoll = splunkService.getUsers(userArgs);

        // Display info for each user (real name, username, roles)
        System.out.println("There are " + usercoll.size() + " users:\n");
        for (com.splunk.User user: usercoll.values()) {
            System.out.println(user.getRealName() + " (" + user.getName() + ")");
            for (String role: user.getRoles()) {
                System.out.println(" - " + role);
            }
        }

        // Display the current user
        System.out.println("The current user is " + splunkService.getUsername());


        // Get the collection of roles
        EntityCollection<Role> rolecoll = splunkService.getRoles();

        // Display the name and capabilities (included imported ones) of each role
        System.out.println("There are " + rolecoll.size() + " defined roles:\n");
        for (Role role: rolecoll.values()) {
            System.out.println("Role: " + role.getName());
            System.out.println("  Capabilities:");
            for (String cape: role.getCapabilities()) {
                System.out.println("   - " + cape);
            }
            if(role.getImportedCapabilities().length >0) {
                System.out.println("  Imported capabilities: ");
                for (String cape: role.getImportedCapabilities()) {
                    System.out.println("   - " + cape);
                }
            }
            System.out.println("\n");
        }


//        System.out.println(splunkService.getApplications().create("Demo"));


//        EntityCollection<Application> applicationEntityCollection = splunkService.getApplications();
//
//        for (Application application : applicationEntityCollection.values()){
//            System.out.println(application.getLabel());
//        }


//        System.out.println(splunkService.getApplications().remove("ASD"));
    }
}
