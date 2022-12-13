package main;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class DragManager implements MouseListener, MouseMotionListener {
	
	private Main main;
	private Simulation sim;
	
	private Point begin;
	
	public DragManager(Main main, Simulation sim) {
		this.main = main;
		this.sim = sim;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Point p = e.getPoint();
		
		double distToCenter = Math.sqrt( (p.x-sim.getFixedPoint().x)*(p.x-sim.getFixedPoint().x) + (p.y-sim.getFixedPoint().y)*(p.y-sim.getFixedPoint().y) );
		if (distToCenter > (sim.r1 + sim.r2 + 20)) return;
		
		if (e.getButton() == MouseEvent.BUTTON1) {
			sim.dragging = 1;
			begin = sim.getFixedPoint();
		}
		else if (e.getButton() == MouseEvent.BUTTON3) {
			sim.dragging = 2;
			begin = sim.getP1Pos();
		}
		sim.a1_v = sim.a2_v = 0;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		sim.dragging = 0;
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (sim.dragging == 0) return;
		
		Point p = e.getPoint();
		
		// Calculate new angle 
		int dx = p.x - begin.x;
		int dy = p.y - begin.y;
		
		double theta = -Math.atan2(dy, dx) + Math.PI/2;
		
		if (sim.dragging == 1) 
			sim.a1 = theta;
		else
			sim.a2 = theta;
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
