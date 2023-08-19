
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IRemoteWhiteBoardClient extends Remote {
	void userJoined(IRemoteWhiteBoardClient newClient) throws RemoteException;
	public void userKicked(int userIndex) throws RemoteException;
    void receiveMessage(String username, String message) throws RemoteException;
    void receiveUsername(String username) throws RemoteException;
    public void receiveShape(WhiteboardShape shape) throws RemoteException;
    public void setWhiteboard(WhiteBoard whiteboard)throws RemoteException;
    public String getUsername() throws RemoteException;
    public WhiteBoard getWhiteboard() throws RemoteException;
    void receiveMessage(String message) throws RemoteException;
	void disconnect()throws RemoteException;
	void refreshCanvas()throws RemoteException;
	void clearAll() throws RemoteException;
	boolean approveJoinRequest(String peerUsername) throws RemoteException;
	void systemExit()throws RemoteException;
	
}