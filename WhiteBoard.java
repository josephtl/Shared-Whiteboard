import java.awt.BasicStroke;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Point;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

//import org.json.JSONObject;
//import org.json.JSONArray;

@SuppressWarnings("serial")
public class WhiteBoard extends JFrame implements Serializable {
	private JPanel whiteboard;
	private int startX, startY, currentX, currentY;
	private ShapeType currentShapeType = ShapeType.PENCIL;
	private IRemoteWhiteBoard remoteWB;
	private IRemoteWhiteBoardClient remoteWBC;
    private int prevX, prevY;
    private ArrayList<Point> pencilPoints = new ArrayList<>();
	private JPanel contentPane;
	private int currentStroke = 2;
	private Color currentColor = Color.BLACK;
	private ArrayList<Color> colorList = new ArrayList<>();
	private DefaultListModel<String> listModel;
	private String username = "";
	private List<String> usernameList;
	private List<IRemoteWhiteBoardClient> clientList;
	private JScrollPane scrollPaneUserList;
	private JScrollPane scrollPane_2;
	private JTextArea chatbox;
	private Boolean isManager;
	private JTextField textField;
	private Graphics2D g2d;
	private String typeText;
	private List<JTextField> textFields = new ArrayList<>();
	private Boolean hasSaved = false;
	/**
	 * Create the frame.
	 * @throws RemoteException 
	 */

	public WhiteBoard(String name, List<IRemoteWhiteBoardClient> clientList, Boolean isManager, List<String> textList) throws RemoteException {
		this.username = name;
		this.clientList = clientList;
		this.isManager = isManager;
		
		colorList.add(currentColor);
		setTitle("Whiteboard - " + username);
		if (isManager) {
			setSize(800, 620);
		} else {
			setSize(800, 600);
		}
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        
        
        this.addWindowListener( new WindowAdapter() {
            public void windowClosed(WindowEvent we) {
                System.exit(0);
            }
        } );
        
        
        JPanel panel = new JPanel();
        panel.setBounds(4, 0, 88, 572);
        getContentPane().add(panel);

        
        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();
        
        // Create the "File" menu
        JMenu fileMenu = new JMenu("File");
        
        // Create the "New" menu item
        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Code to handle the "New" action
            	
            	clearWhiteboard();
            }
        });
        fileMenu.add(newMenuItem);
        
        
        
        // Create the "Save" menu item
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Code to handle the "Save" action
                if (!hasSaved) {
	            	JFileChooser fileChooser = new JFileChooser();
	                int option = fileChooser.showSaveDialog(WhiteBoard.this);
	                if (option == JFileChooser.APPROVE_OPTION) {
	                    File file = fileChooser.getSelectedFile();
	                    hasSaved = true;
		            	try {
							remoteWB.saveShapesToCSV(file);
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	                }
                } else {
                	try {
						remoteWB.saveShapesToCSV(remoteWB.getFile());
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
            }

        });
        fileMenu.add(saveMenuItem);
        
        // Create the "Open" menu item
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Code to handle the "Open" action
            	JFileChooser fileChooser = new JFileChooser();
                int option = fileChooser.showOpenDialog(WhiteBoard.this);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    // Call a method to load the data from the file
                    try {
                    	remoteWB.loadShapes(file);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
            }
        });
        fileMenu.add(openMenuItem);
        
        // Create the "Save As" menu item
        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	JFileChooser fileChooser = new JFileChooser();
                int option = fileChooser.showSaveDialog(WhiteBoard.this);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
            	
	            	try {
						remoteWB.saveShapesToCSV(file);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
            }
        });
        fileMenu.add(saveAsMenuItem);
        
        // Create the "Close" menu item
        JMenuItem closeMenuItem = new JMenuItem("Close");
        closeMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Code to handle the "Close" action
            	try {
					remoteWB.managerClose();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        fileMenu.add(closeMenuItem);
        
        if (isManager) {
	        // Add the "File" menu to the menu bar
	        menuBar.add(fileMenu);
	        
	        // Set the menu bar for the frame
	        setJMenuBar(menuBar);

        }
      
        // Drawing tools buttons
        JButton btnPencil = new JButton("Pencil");
        btnPencil.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		currentShapeType = ShapeType.PENCIL;
        	}
        });
        panel.add(btnPencil);
        
        JButton btnText = new JButton("Text");
        btnText.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		currentShapeType = ShapeType.TEXT;
        	}
        });
        panel.add(btnText);
        
        
        JButton btnCircle = new JButton("Circle");
        btnCircle.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		currentShapeType = ShapeType.CIRCLE;
        	}
        });
        panel.add(btnCircle);
        
               
        
        JButton btnOval = new JButton("Oval");
        btnOval.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		currentShapeType = ShapeType.OVAL;
        	}
        });
        panel.add(btnOval);
        
        JButton btnRect = new JButton("Rectangle");
        btnRect.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		currentShapeType = ShapeType.RECTANGLE;
        	}
        });
        panel.add(btnRect);
        
        JButton btnLine = new JButton("Line");
        btnLine.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		currentShapeType = ShapeType.LINE;
        	}
        });
        panel.add(btnLine);
        
        JButton btnColor = new JButton("Color");
        panel.add(btnColor);
        btnColor.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		ColorPalette cp = new ColorPalette(colorList);
        		
        		cp.setVisible(true);
        		
        		currentColor = cp.getColor();
        	}
    		
        });
        
        JButton btnThinner = new JButton("Thinner");
        panel.add(btnThinner);
        btnThinner.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if (currentStroke > 0) {
        			currentStroke -= 1;
        		}        	
        	}
        });
        
        JButton btnThicker = new JButton("Thicker");
        panel.add(btnThicker);
        btnThicker.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		currentStroke += 1;
        	}
        });
        
        
        whiteboard = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                try {
                    for (WhiteboardShape shape : remoteWB.getDrawnShapes()) {
                    	
                    	shape.draw(g2d);
                        if (shape.getType() == ShapeType.TEXT) {
                            
                            Point p1 = shape.getPoints().get(0);
                            Point p2 = shape.getPoints().get(1);
                            boolean isNewText = true;

                            // Check if a text field already exists for this shape
                            for (JTextField tf : textFields) {
                                if (tf.getX() == p1.x && tf.getY() == p1.y) {
                                    isNewText = false;
                                    break;
                                }
                            }

                            // If it's a new text shape, create a text field for it
                            if (isNewText) {
                                textField = new JTextField();
                                textField.setBackground(new Color(0, 0, 0, 0));
                                textField.setText(shape.getString());
                                textField.setForeground(shape.getColor());
                                Font font = new Font("Arial", Font.PLAIN, shape.getStroke() + 10);
                                textField.setFont(font);
                                textField.setBounds(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
                                whiteboard.add(textField);
                                textField.setBorder(BorderFactory.createEmptyBorder());
                                textField.setEditable(false);
                                textFields.add(textField);
                            }
                        }
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }          	

            }

        };
        whiteboard.setBounds(104, 0, 428, 572);
        whiteboard.setBackground(Color.WHITE);
        getContentPane().add(whiteboard);
        whiteboard.setLayout(null);
        whiteboard.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                startX = e.getX();
                startY = e.getY();
                currentX = startX;
                currentY = startY;
                
                pencilPoints = new ArrayList<Point>();
                pencilPoints.add(new Point(startX, startY));

            }

            public void mouseReleased(MouseEvent e) {
                currentX = e.getX();
                currentY = e.getY();
                pencilPoints.add(new Point(currentX, currentY));
                WhiteboardShape shape = new WhiteboardShape(currentShapeType, currentColor, currentStroke, pencilPoints);                
                
                if (currentShapeType == ShapeType.TEXT) {
                	textField = new JTextField();
                	textField.setBackground(new Color(0, 0, 0, 0)); // Set the background color to null
                	
                	textField.setForeground(currentColor);
                	Font font = new Font("Arial", Font.PLAIN, currentStroke + 10);
                	textField.setFont(font);
                	textField.setBounds(startX, startY, currentX - startX, currentY - startY); // Set the bounds of the text field as per your requirement
                	whiteboard.add(textField);
                    textField.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                                textField.setBorder(BorderFactory.createEmptyBorder());
                                textField.setEditable(false);
                                typeText = textField.getText();
                                shape.setText(typeText);
            	                try {
            	                	
            						remoteWB.broadcastShape(shape);
            					} catch (RemoteException e1) {
            						// TODO Auto-generated catch block
            						e1.printStackTrace();
            					}
            	                textField.removeKeyListener(this);
                            }                         
                        }
                    });                
                }          

                if (currentShapeType != ShapeType.TEXT) {
	                try {
	                	
						remoteWB.broadcastShape(shape);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }

            }
        });

        whiteboard.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                currentX = e.getX();
                currentY = e.getY();
                if (currentShapeType == ShapeType.PENCIL) {
                    pencilPoints.add(new Point(currentX, currentY));
                    whiteboard.repaint();
                }
                else if (currentShapeType != ShapeType.TEXT) {
                	whiteboard.repaint();
                }
                
            }
        });

        // Create the default list model
        listModel = new DefaultListModel<>();
        
        // Populate the default list model with the elements from the ArrayList
        for (int i = 1; i < clientList.size(); i++) {
            IRemoteWhiteBoardClient client = clientList.get(i);
            listModel.addElement(client.getUsername());
            
        }        
        
        JList<String> userList = new JList<String>(listModel);
        
        scrollPaneUserList = new JScrollPane(userList);
        
        scrollPaneUserList.setBounds(542, 0, 252, 136);
        getContentPane().add(scrollPaneUserList);        
        
        userList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isManager && e.getClickCount() == 2) { // Double-click event
                    int index = userList.locationToIndex(e.getPoint());
                    String selectedElement = listModel.getElementAt(index);
                    // Perform your action on the selected element
                    
                    KickUser kickOrNot = new KickUser(selectedElement, index, remoteWB, listModel);
                    kickOrNot.setVisible(true);
                    
                }
            }
        });
        
        
        JPanel chatPane = new JPanel();
        chatPane.setBounds(542, 151, 252, 415);
        getContentPane().add(chatPane);
        chatPane.setLayout(null);
        
        chatbox = new JTextArea();
        scrollPane_2 = new JScrollPane(chatbox);
        scrollPane_2.setBounds(0, 0, 252, 386);
        chatPane.add(scrollPane_2);
        chatbox.setEditable(false);
        for (String text : textList) {
        	chatbox.append(text);
        }        
        
        JTextArea typeArea = new JTextArea();
        JScrollPane scrollPane_3 = new JScrollPane(typeArea);
        scrollPane_3.setBounds(0, 392, 160, 23);
        chatPane.add(scrollPane_3);
    
        
        JButton btnSend = new JButton("send");
        btnSend.setBounds(165, 389, 87, 29);
        chatPane.add(btnSend);
        
        // Add ActionListener to the "send" button
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newText = typeArea.getText(); // Get the text from typeArea
                if (newText.length() > 0) {
	                textList.add(username + ": " + newText + "\n");
	                typeArea.setText(""); // Clear the typeArea
	                
	             // Broadcast the new message to other clients
                    try {
                        remoteWB.broadcastMessage(username + ": " + newText + "\n");
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
                
            }
        });
    }
        	
    public void setUser(String name) {
    	this.username = name;
    }

    public void setRemoteWB(IRemoteWhiteBoard remoteWB) {
    	this.remoteWB = remoteWB;
    }
    
    public void setRemoteWBC(IRemoteWhiteBoardClient remoteWBC) {
    	this.remoteWBC = remoteWBC;
    }
    public JPanel getWhiteboard() {
    	return whiteboard;
    }
    public void setWhiteboard(JPanel whiteboard) {
    	this.whiteboard = whiteboard;
    }

    public void userJoined(IRemoteWhiteBoardClient newClient) throws RemoteException {
    	clientList.add(newClient);
        listModel.addElement(newClient.getUsername());
        scrollPaneUserList.revalidate();
        scrollPaneUserList.repaint();
    }
    
    public void userKicked(int userIndex) throws RemoteException {
    	clientList = remoteWB.getClientList();
    	listModel.removeElementAt(userIndex);
    	scrollPaneUserList.revalidate();
        scrollPaneUserList.repaint();
    }

    public void textListRefresh(String newText) {
    	chatbox.append(newText);
    	chatbox.revalidate();
    	chatbox.repaint();
    	scrollPane_2.revalidate();
    	scrollPane_2.repaint(); 	
    }

	public void refreshCanvas() {
		// TODO Auto-generated method stub
		whiteboard.revalidate();
		whiteboard.repaint();		
	}
	private void clearWhiteboard(){
	    // Code to clear the whiteboard
	    // For example, you can remove all the shapes drawn on the whiteboard
	    try {
			remoteWB.clearShapes();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Assuming you have a method to clear the shapes in the remote whiteboard
	    
	    whiteboard.removeAll();

	    whiteboard.repaint();
	}
	
	public void clearTextField() {
	
		textFields.clear();

		whiteboard.removeAll();
	}

}
