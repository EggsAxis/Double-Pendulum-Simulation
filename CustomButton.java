package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import javax.swing.JButton;

public class CustomButton extends JButton implements MouseListener {
	
	private static final long serialVersionUID = 1L;

	public static final int PAUSE_BUTTON = 1;
	public static final int SKIP_BUTTON = 2;
	public static final int RESET_BUTTON = 3;
	
	private int type = 1;
	private boolean hoovering = false;
	private boolean pressed = false;
	private int state = 0;
	
	private Shape shape;
	
	public CustomButton(int type, int x) {
		super();
		this.type = type;
		
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.addMouseListener(this);
		this.setFocusable(false);
		this.setBorderPainted(false);
		this.setContentAreaFilled(false);
		this.setFocusPainted(false);
		this.setBounds(x, 10, 34, 34);
		
		createShape();
	}
	
	
	private void createShape() {
		shape = null;
		
		if (type == PAUSE_BUTTON) {
			
			if (state == 0) {
				GeneralPath path = new GeneralPath();
				int width = 8, height = 28;
				path.moveTo(getWidth()/2 - 1.5*width, getHeight()/2 - height/2);
				path.lineTo(getWidth()/2 - .5*width, getHeight()/2 - height/2);
				path.lineTo(getWidth()/2 - .5*width, getHeight()/2 + height/2);
				path.lineTo(getWidth()/2 - 1.5*width, getHeight()/2 + height/2);
				path.closePath();
				
				path.moveTo(getWidth()/2 + 1.5*width, getHeight()/2 - height/2);
				path.lineTo(getWidth()/2 + .5*width, getHeight()/2 - height/2);
				path.lineTo(getWidth()/2 + .5*width, getHeight()/2 + height/2);
				path.lineTo(getWidth()/2 + 1.5*width, getHeight()/2 + height/2);
				path.closePath();
				
				shape = path;
				
				
			} else {
				GeneralPath path = new GeneralPath();
				path.moveTo(4, 1);
				path.lineTo(getWidth()-4, getHeight()/2);
				path.lineTo(4, -1 + getHeight());
				path.closePath();
				
				shape = path;
			}
		}
		
		else if (type == SKIP_BUTTON) {
			int width = 24, height = 28;
			GeneralPath path = new GeneralPath();
			path.moveTo(getWidth()/2 - width/2, getHeight()/2 - height/2);
			path.lineTo(getWidth()/2 + width/7, getHeight()/2);
			path.lineTo(getWidth()/2 - width/2, getHeight()/2 + height/2);
			path.closePath();
			
			path.moveTo(getWidth()/2 + width/4, getHeight()/2 - height/2);
			path.lineTo(getWidth()/2 + width/2 , getHeight()/2 - height/2);
			path.lineTo(getWidth()/2 + width/2 , getHeight()/2 + height/2);
			path.lineTo(getWidth()/2 + width/4, getHeight()/2 + height/2);
			path.closePath();
			
			shape = path;
			
		}
		
		else if (type == RESET_BUTTON) {
			int r = 9, thickness = 4;
			Arc2D outer = new Arc2D.Double((double) getWidth()/2 - r - thickness, (double) getHeight()/2 - r - thickness, (double) 2* (r+thickness), (double) 2* (r+thickness), 90, -290, Arc2D.PIE);
			Arc2D inner = new Arc2D.Double((double) getWidth()/2 - r, (double) getHeight()/2 - r, (double) 2*r, (double) 2*r, 90, -290, Arc2D.PIE);
			
			int ah = 4;
			GeneralPath path = new GeneralPath();
			path.moveTo(getWidth()/2, getHeight()/2 - r - thickness - ah);
			path.lineTo(getWidth()/2, getHeight()/2 - r + ah);
			path.lineTo(getWidth()/2 - 8, getHeight()/2 - r - thickness/2);
			path.closePath();
			
			Area area = new Area(outer);
			area.subtract(new Area(inner));
			area.add(new Area(path));
			
			shape = area;
		}
		
	}


	@Override
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		
		if (shape == null) return;
		
		if (hoovering) 	g.setColor(new Color(132, 132, 132));
		else 			g.setColor(new Color(115, 115, 115));
		
		g.fill(shape);
		
		if (pressed) {
			g.setColor(new Color(29, 29, 29));
			g.setStroke(new BasicStroke(2));
			g.draw(shape);
		}
		
	}


	@Override
	public void mouseClicked(MouseEvent e) {
	}


	@Override
	public void mousePressed(MouseEvent e) {
		pressed = true;
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		pressed = false;
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		hoovering = true;
	}


	@Override
	public void mouseExited(MouseEvent e) {
		hoovering = false;
	}
	
	public void setState(int state) {
		this.state = state;
		createShape();
	}


	public int getState() {
		return state;
	}
	
}
