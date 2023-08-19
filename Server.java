import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            IRemoteWhiteBoard remoteWB = new RemoteWhiteBoardServant();

            Registry registry = LocateRegistry.createRegistry(4444);
            registry.rebind("WhiteBoard", remoteWB);
            System.out.println("Whiteboard server ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}