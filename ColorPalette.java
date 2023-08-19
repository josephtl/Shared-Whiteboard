import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ColorPalette extends JDialog implements ActionListener {

	private JPanel contentPane;
	private Color currentColor;
	private Color preview;
	private int opacity = 255;

	/**
	 * Create the frame.
	 */

    // textfield to enter RGBA value
    JTextField R, G, B, A;
 
    // panel
    JPanel p;
 
    // constructor
    public ColorPalette(ArrayList<Color> colorList) {
	    
        super();
        
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        currentColor = colorList.get(colorList.size() - 1);
        // create textfield
        R = new JTextField(3);
        G = new JTextField(3);
        B = new JTextField(3);
        A = new JTextField(3);
 
        // create labels
        JLabel l = new JLabel("Red= ");
        JLabel l1 = new JLabel("Green= ");
        JLabel l2 = new JLabel("Blue= ");
        JLabel l3 = new JLabel("Alpha= ");
 
        // create a panel
        p = new JPanel();
 
        // create button
        JButton b = new JButton("ok");
        JButton b1 = new JButton("brighter");
        JButton b2 = new JButton("Darker");
 
        // add ActionListener
        b.addActionListener(this);
        b2.addActionListener(this);
        b1.addActionListener(this);
 
        // add components to panel
        p.add(l);
        p.add(R);
        p.add(l1);
        p.add(G);
        p.add(l2);
        p.add(B);
        p.add(l3);
        p.add(A);
        p.add(b);
        p.add(b1);
        p.add(b2);
 
        setSize(500, 100);
        getContentPane().add(p);
        
        JButton btnApply = new JButton("Apply & Quit");
        btnApply.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		colorList.add(currentColor);
        		dispose();
        		
        	}
        });
        p.add(btnApply);
        
    }
	 
    // if button is pressed
    public void actionPerformed(ActionEvent evt)
    {
        String s = evt.getActionCommand();
        if (s.equals("ok")) {
            int r, g, b, a;
 
            // get rgba value
            r = Integer.parseInt(R.getText());
            if (r > 255) {
            	R.setText("255");
            	r = 255;
            }
            g = Integer.parseInt(G.getText());
            if (g > 255) {
            	G.setText("255");
            	g = 255;
            }
            b = Integer.parseInt(B.getText());
            if (b > 255) {
            	B.setText("255");
            	b = 255;
            }
            a = Integer.parseInt(A.getText());
            if (a > 255) {
            	A.setText("255");
            	a = 255;
            }
 
            // create a new Color
            currentColor = new Color(r, g, b, a);
            opacity = a;
            preview = new Color(r, g, b);
 
            // set the color as background of panel
            p.setBackground(preview);
        }
        else if (s.equals("brighter")) {
 
            // getBackgroundColor
            Color background = p.getBackground();
 
            // make the color brighter
            background = background.brighter();
 
            // set the color as background of panel
            p.setBackground(background);

            currentColor = new Color (background.getRed(), background.getGreen(), background.getBlue(), opacity);
            
        }
        else {
 
            // getBackgroundColor
            Color background = p.getBackground();
 
            // make the color darker
            background = background.darker();
 
            // set the color as background of panel
            p.setBackground(background);

            currentColor = new Color (background.getRed(), background.getGreen(), background.getBlue(), opacity);
            
        }
    }
    public Color getColor() {
    	return currentColor;
    }
    

	
}
