import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class JoinWhiteBoard {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java JoinWhiteBoard <serverIPAddress> <serverPort> <username>");
            System.exit(1);
        }

        String serverIPAddress = args[0];
        int serverPort = Integer.parseInt(args[1]);
        String username = args[2];

        try {
            Registry registry = LocateRegistry.getRegistry(serverIPAddress, serverPort);
            IRemoteWhiteBoard remoteWB = (IRemoteWhiteBoard) registry.lookup("WhiteBoard");           
            
            // If the username been used, cannont join
            if (remoteWB.getUserList().contains(username)) {
            	System.out.println("The username has been used. Please change another username.");
                System.exit(1);
            }
            // Create an instance of the Whiteboard GUI
            WhiteBoard whiteboard = new WhiteBoard(username, remoteWB.getClientList(), false, remoteWB.getTextList());
            IRemoteWhiteBoardClient remoteWBClient = new RemoteWhiteBoardClientServant(username, whiteboard);
            
            
            boolean approved = remoteWB.requestJoin(remoteWBClient);
            
            if (approved) {
            	// Call the joinWhiteBoard method to connect the client to the server
	            remoteWB.joinWhiteBoard(serverIPAddress, serverPort, remoteWBClient);
	            whiteboard.setRemoteWB(remoteWB);
	            
	
	            remoteWBClient.setWhiteboard(whiteboard);


	            // Show the GUI window
	            whiteboard.setVisible(true);
            } else {
                System.out.println("Your request to join the whiteboard was not approved by the manager.");
            }

            
        } catch (Exception e) {
            System.err.println("JoinWhiteBoard exception:");
            e.printStackTrace();
        }
    }
}
