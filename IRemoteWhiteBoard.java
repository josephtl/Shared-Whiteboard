import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface IRemoteWhiteBoard extends Remote {
    void createWhiteBoard(IRemoteWhiteBoardClient newClient) throws RemoteException;
    void joinWhiteBoard(String serverIPAddress, int serverPort, IRemoteWhiteBoardClient newClient) throws RemoteException;
    void kickUser(int userIndex) throws RemoteException;
    List<IRemoteWhiteBoardClient> getClientList() throws RemoteException;
    List<String> getUserList() throws RemoteException;
	void registerClient(IRemoteWhiteBoardClient client) throws RemoteException;
	void broadcastShape(WhiteboardShape shape) throws RemoteException;
	public ArrayList<WhiteboardShape> getDrawnShapes()throws RemoteException;
	void broadcastMessage(String message) throws RemoteException;
	public List<String> getTextList() throws RemoteException;
	void clearShapes() throws RemoteException;
	boolean requestJoin(IRemoteWhiteBoardClient client) throws RemoteException;
	void managerClose()throws RemoteException;
	void saveShapesToCSV(File file) throws RemoteException;
	File getFile()throws RemoteException;
	void loadShapes(File file)throws RemoteException;


}
