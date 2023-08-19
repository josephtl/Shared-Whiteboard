import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;




class WhiteboardShape implements Serializable{
    private ShapeType shapeType;
    private Color color;
    private int stroke;
    private ArrayList<Point> points;
    private String textInput;

    
    public WhiteboardShape(ShapeType currentShapeType, Color color, int stroke, ArrayList<Point> points) {
    	this.shapeType = currentShapeType;
        this.color = color;
        this.stroke = stroke;
        this.points = points;
    }
    
    
    public WhiteboardShape(ShapeType currentShapeType, Color color, int stroke, ArrayList<Point> points, String textInput) {
    	this.shapeType = currentShapeType;
        this.color = color;
        this.stroke = stroke;
        this.points = points;
        this.textInput = textInput;
    }


	public void draw(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(stroke));
        g2d.setColor(color);

        switch (shapeType) {
            case LINE:
                drawLine(g2d);
                break;
            case RECTANGLE:
                drawRectangle(g2d);
                break;
            case OVAL:
                drawOval(g2d);
                break;
            case CIRCLE:
                drawCircle(g2d);
                break;
            case PENCIL:
                drawPencil(g2d);
                break;
            case TEXT:
            	break;
        }
    }

    private void drawLine(Graphics2D g2d) {
        if (points.size() >= 2) {
            Point p1 = points.get(0);
            Point p2 = points.get(1);
            
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    private void drawRectangle(Graphics2D g2d) {
        
    	if (points.size() >= 2) {
            Point p1 = points.get(0);
            Point p2 = points.get(1);
            int x = Math.min(p1.x, p2.x);
            int y = Math.min(p1.y, p2.y);
            int width = Math.abs(p1.x - p2.x);
            int height = Math.abs(p1.y - p2.y);
            g2d.drawRect(x, y, width, height);

        }
    }

    private void drawOval(Graphics2D g2d) {
        if (points.size() >= 2) {
            Point p1 = points.get(0);
            Point p2 = points.get(1);
            int x = Math.min(p1.x, p2.x);
            int y = Math.min(p1.y, p2.y);
            int width = Math.abs(p1.x - p2.x);
            int height = Math.abs(p1.y - p2.y);
            g2d.drawOval(x, y, width, height);
        }
    }

    private void drawCircle(Graphics2D g2d) {
        if (points.size() >= 2) {
            Point p1 = points.get(0);
            Point p2 = points.get(1);       
            int diameter = Math.max(Math.abs(p2.x- p1.x), Math.abs(p2.y - p1.y));
            int radius = diameter / 2;
            int centerX = p1.x + radius;
            int centerY = p1.y + radius;
            int x = centerX - radius;
            int y = centerY - radius;
            g2d.drawOval(x, y, diameter, diameter);
        }
    }

    private void drawPencil(Graphics2D g2d) {
        if (points.size() >= 2) {
            Point prevPoint = points.get(0);
            for (int i = 1; i < points.size(); i++) {
                Point currentPoint = points.get(i);
                g2d.drawLine(prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
                prevPoint = currentPoint;

            }
        }
    }

    
    public ShapeType getType() {
    	return shapeType;
    }
    
    public ArrayList<Point> getPoints() {
    	return points;
    }
    
    public int getStroke() {
    	return stroke;
    }
    
    public Color getColor() {
    	return color;
    }
    
    public String getString() {
    	return textInput;
    }
    
    public void setText(String textInput) {
    	this.textInput = textInput;
    }
}




