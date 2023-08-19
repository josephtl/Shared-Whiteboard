import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CreateWhiteBoard {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java CreateWhiteBoard <serverIPAddress> <serverPort> <username>");
            System.exit(1);
        }

        String serverIPAddress = args[0];
        int serverPort = Integer.parseInt(args[1]);
        String username = args[2];

        try {
            Registry registry = LocateRegistry.getRegistry(serverIPAddress, serverPort);
            IRemoteWhiteBoard remoteWB = (IRemoteWhiteBoard) registry.lookup("WhiteBoard");

            
            WhiteBoard whiteboard = new WhiteBoard(username, remoteWB.getClientList(), true, remoteWB.getTextList());
            IRemoteWhiteBoardClient remoteWBClient = new RemoteWhiteBoardClientServant(username, whiteboard);
            
            remoteWB.createWhiteBoard(remoteWBClient);
            whiteboard.setRemoteWB(remoteWB);

            remoteWBClient.setWhiteboard(whiteboard);
            // Set up any necessary configurations or data for the whiteboard

            // Show the GUI window
            whiteboard.setVisible(true);
        } catch (Exception e) {
            System.err.println("CreateWhiteBoard exception:");
            e.printStackTrace();
        }
    }
}
