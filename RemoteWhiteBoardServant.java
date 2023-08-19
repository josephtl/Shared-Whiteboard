import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class RemoteWhiteBoardServant extends UnicastRemoteObject implements IRemoteWhiteBoard {
    private static final long serialVersionUID = 1L;
    private List<IRemoteWhiteBoardClient> clientList;
    private ArrayList<WhiteboardShape> shapeDrawn = new ArrayList<>();
    private ArrayList<String> textList = new ArrayList<>();
    private List<String> users = new ArrayList<>();
    private File currentFile;
    
    public RemoteWhiteBoardServant() throws RemoteException {
        super();
        clientList = new ArrayList<>();
        
    }
    
    @Override
    public void createWhiteBoard(IRemoteWhiteBoardClient newClient) throws RemoteException {

    	clientList.add(newClient);
    	users.add(newClient.getUsername());
        System.out.println("Whiteboard created by " + newClient.getUsername());
    }

    @Override
    public void joinWhiteBoard(String serverIPAddress, int serverPort, IRemoteWhiteBoardClient newClient) throws RemoteException {


        clientList.add(newClient);
        users.add(newClient.getUsername());
        
        System.out.println(newClient.getUsername() + " joined the whiteboard");
        
        for (IRemoteWhiteBoardClient client : clientList) {
        	System.out.println("Broadcast add to " + client.getUsername());
            client.userJoined(newClient);
        }
    }

    @Override
    public void kickUser(int userIndex) throws RemoteException {
    	System.out.println(clientList.get(userIndex+1).getUsername() + " has been kicked from the whiteboard");

    	clientList.get(userIndex + 1).disconnect();
    	users.remove(clientList.get(userIndex + 1).getUsername());
    	clientList.remove(userIndex + 1);
    	
    	for (IRemoteWhiteBoardClient client : clientList) {
    		
    		System.out.println("Broadcast kick to " + client.getUsername());
    		client.userKicked(userIndex);
    		
        }                
          
	}
    @Override
    public List<IRemoteWhiteBoardClient> getClientList() throws RemoteException {
//        List<String> clientList = new ArrayList<>(clients.keySet());
        return clientList;
    }


    @Override
    public List<String> getUserList() throws RemoteException {
//        List<String> clientList = new ArrayList<>(clients.keySet());
        return users;
    }
    @Override
    public List<String> getTextList() throws RemoteException {
//      List<String> clientList = new ArrayList<>(clients.keySet());
      return textList;
  }
    
    
    @Override
    public void registerClient(IRemoteWhiteBoardClient newClient) throws RemoteException {
        clientList.add(newClient);
//        List<String> updatedUserList = getUserList(); // Get the updated user list
        for (IRemoteWhiteBoardClient client : clientList) {
//            client.getWhiteBoard().userListRefresh(users);
        }
    }



    public void broadcastShape(WhiteboardShape shape) throws RemoteException {
    	shapeDrawn.add(shape);
    	
    	for (IRemoteWhiteBoardClient client : clientList) {
    		client.refreshCanvas();

    	}
    	
    }


    
    public ArrayList<WhiteboardShape> getDrawnShapes()throws RemoteException{
    	return shapeDrawn;
    }
    public void broadcastMessage(String message) throws RemoteException {
    	textList.add(message);
        for (IRemoteWhiteBoardClient client : clientList) {
            client.receiveMessage(message);
        }
    }

	@Override
	public void clearShapes() throws RemoteException {
		shapeDrawn.clear();
		
		for (IRemoteWhiteBoardClient client : clientList) {
            client.clearAll();
        }
		
	}
	
    @Override
    public boolean requestJoin(IRemoteWhiteBoardClient client) throws RemoteException {
        String peerUsername = client.getUsername();


        // Notify the manager about the join request
        IRemoteWhiteBoardClient manager = clientList.get(0); // Assuming the first client is the manager
        boolean approved = manager.approveJoinRequest(peerUsername);

        return approved;
    }

	@Override
	public void managerClose() throws RemoteException {
		// TODO Auto-generated method stub
		for (int i = 1; i < clientList.size(); i++) {
		    IRemoteWhiteBoardClient client = clientList.get(i);
		    client.disconnect();
		}
		clientList.get(0).systemExit();
	}

	@Override
	public void saveShapesToCSV(File file) {
	    currentFile = file;
		try (FileWriter writer = new FileWriter(file)) {
	        // Write the CSV header
	        writer.write("Shape Type,R,G,B,A,Stroke,Points,Text\n");

	        // Write each shape to the CSV file
	        for (WhiteboardShape shape : shapeDrawn) {
	            writer.write(shape.getType() + ",");
	            writer.write(shape.getColor().getRed() + ",");
	            writer.write(shape.getColor().getGreen() + ",");
	            writer.write(shape.getColor().getBlue() + ",");
	            writer.write(shape.getColor().getAlpha() + ",");
	            writer.write(shape.getStroke() + ",");
	            
	            StringBuilder sb = new StringBuilder();
	            for (Point p : shape.getPoints()) {
	                sb.append(p.x).append(",").append(p.y).append(",");
	            }
	            // Remove the trailing "," from the last point
	            if (sb.length() >= 1) {
	                sb.setLength(sb.length() - 1);
	            }

	            writer.write("\"" + sb.toString() + "\",");
	            writer.write(shape.getString() + "\n");
	        }

	        System.out.println("Shapes saved to CSV file successfully!");
	    } catch (IOException e) {
	        System.out.println("Error while saving shapes to CSV file: " + e.getMessage());
	    }
	}

	@Override
	public File getFile() throws RemoteException {
		// TODO Auto-generated method stub
		return currentFile;
	}

	@Override
	public void loadShapes(File file) throws RemoteException {
	    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	        // Skip the header line
	        reader.readLine();

	        // Clear the existing shapes
	        shapeDrawn.clear();

	        String line;
	        while ((line = reader.readLine()) != null) {
	            String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
	            
	           
	            // Parse the values from the CSV line
	            ShapeType shapeType = ShapeType.valueOf(values[0]);
	            
	            Color color = new Color(Integer.parseInt(values[1]), Integer.parseInt(values[2]), Integer.parseInt(values[3]), Integer.parseInt(values[4]));
	            
	            int stroke = Integer.parseInt(values[5]);
	            
	            String[] loadPoints = values[6].replace("\"", "").split(",");


	            ArrayList<Point> points = new ArrayList<>();
	            for (int i = 0; i < loadPoints.length; i += 2) {
	                String pointX = loadPoints[i];
	                String pointY = loadPoints[i + 1];

	                

	                int parsedPointX = Integer.parseInt(pointX);
	                int parsedPointY = Integer.parseInt(pointY);

	                points.add(new Point(parsedPointX, parsedPointY));
                
	            }

	            // Create the WhiteboardShape object
	            WhiteboardShape shape;
	            if (shapeType == ShapeType.TEXT) {
	                String textInput = values[7].replace("\"", "");
	                shape = new WhiteboardShape(shapeType, color, stroke, points, textInput);
	            } else {
	                shape = new WhiteboardShape(shapeType, color, stroke, points);
	            }

	            // Add the shape to the list
	            shapeDrawn.add(shape);
	        }

	        // Broadcast the updated shapes to clients
	        for (IRemoteWhiteBoardClient client : clientList) {
	            client.refreshCanvas();
	        }

	        System.out.println("Shapes loaded from CSV file successfully!");
	    } catch (IOException e) {
	        System.out.println("Error while loading shapes from CSV file: " + e.getMessage());
	    }
	}

	// Helper method to parse the points from the CSV value
	private ArrayList<Point> parsePoints(String pointsValue) {
	    ArrayList<Point> points = new ArrayList<>();
	    String[] pointValues = pointsValue.replaceAll("[\\[\\]]", "").split("\\),\\(");

	    for (String pointValue : pointValues) {
	        String[] coordinates = pointValue.split(",");
	        int x = Integer.parseInt(coordinates[0]);
	        int y = Integer.parseInt(coordinates[1]);
	        points.add(new Point(x, y));
	    }

	    return points;
	}


	

    
    
}
