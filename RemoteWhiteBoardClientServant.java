import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class RemoteWhiteBoardClientServant extends UnicastRemoteObject implements IRemoteWhiteBoardClient {
    private String username; // New member variable to store the username
    private WhiteBoard whiteboard;

    public RemoteWhiteBoardClientServant(String username, WhiteBoard whiteboard) throws RemoteException {
    	this.username = username;
    	this.whiteboard = whiteboard;
    }
    
    
    @Override
    public void receiveMessage(String username, String message) throws RemoteException {
        System.out.println(username + ": " + message);
        // Perform any desired operations with the received message
    }

    @Override
    public void receiveUsername(String username) throws RemoteException {
        
        System.out.println("Received username: " + username);
        // Perform any desired operations with the received username
    }
    public void receiveShape(WhiteboardShape shapes) throws RemoteException {
        // Perform any desired operations with the received shapes
    	
    }
    @Override
    public String getUsername() throws RemoteException {
    	return username;
    }
    public void setWhiteboard(WhiteBoard whiteboard)throws RemoteException {
    	this.whiteboard = whiteboard;
    }
    
    public WhiteBoard getWhiteboard() throws RemoteException {
    	return whiteboard;
    }

    @Override
    public void userJoined(IRemoteWhiteBoardClient newClient) throws RemoteException {
        SwingUtilities.invokeLater(() -> {
            try {
				whiteboard.userJoined(newClient);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
    }
    
    public void receiveMessage(String message) throws RemoteException {
        SwingUtilities.invokeLater(() -> {
        	whiteboard.textListRefresh(message);
        });
    }
    
    public void userKicked(int userIndex) throws RemoteException {
        SwingUtilities.invokeLater(() -> {
            try {
				whiteboard.userKicked(userIndex);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
    }
    
    
    public void disconnect() throws RemoteException {
        
    	// Close the JFrame window
    	whiteboard.dispose();

    }


	@Override
	public void refreshCanvas() throws RemoteException {
		SwingUtilities.invokeLater(() -> {
        	whiteboard.refreshCanvas();
        });
		
	}


	@Override
	public void clearAll() throws RemoteException {
        SwingUtilities.invokeLater(() -> {
        	whiteboard.clearTextField();
        	whiteboard.repaint();
        });
		
	}
	
    @Override
    public boolean approveJoinRequest(String peerUsername) throws RemoteException {
        // Show a dialog box to the manager for approval
        int option = JOptionPane.showConfirmDialog(
                whiteboard,
                "Allow " + peerUsername + " to join the whiteboard?",
                "Join Request",
                JOptionPane.YES_NO_OPTION
        );

        // Return true if the manager approves, false otherwise
        return option == JOptionPane.YES_OPTION;
    }


	@Override
	public void systemExit() throws RemoteException {
		// TODO Auto-generated method stub
		System.exit(0);
	}




    
    
}
