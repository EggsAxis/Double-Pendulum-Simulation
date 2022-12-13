package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class Simulation {
	
	private Main main;
	
	private long t = 0;
	
	public int r1, r2; 					// Radius of arms
	public double m1, m2;				// Masses of points
	public double a1, a2;				// Initial angles
	public double a1_v, a2_v;			// Iniial velocities
	public double a1_a, a2_a;
	
	private int pRadius = 8;
	public double g = 0.3;				// Gravitational acceleration
	
	private ArrayList<int[]> path;
	private ArrayList<Integer> graph1, graph2;
	private ArrayList<int[]> multiGraph;
	
	public Color p1Color, p2Color;
	
	private final int deletePath = 600;
	
	public int dragging = 0;
	
	private int[] drawnEnergy = new int[3]; // Ep, Ek, total
	private Point fixedPoint = new Point(350, 330);
	
	private Graph accGraph, velGraph;
	
	public Simulation(Main main) {
		this.main = main;
		
		a1 = 4;
		a2 = 3;
		r1 = 150;
		r2 = 150;
		m1 = 10;
		m2 = 3;
		
		path = new ArrayList<int[]>();
		graph1 = new ArrayList<Integer>();
		graph2 = new ArrayList<Integer>();
		multiGraph = new ArrayList<int[]>();
		
		p1Color = new Color(117, 114, 1);
		p2Color = new Color(150, 44, 44);
		
		accGraph = new Graph(this, "Angular Acceleration", new Point(20, 805), 300, 70, 0.01);
		velGraph = new Graph(this, "Angular Velocity", new Point(350, 805), 300, 70, 0.25);
	}
	
	public void reset() {
		a1 = 4;
		a2 = 3;
		r1 = 150;
		r2 = 150;
		m1 = 10;
		m2 = 3;
		a1_v = a2_v = 0;
		
		path.clear();
		graph1.clear();
		graph2.clear();
		multiGraph.clear();
		accGraph.clear();
	}
	
	
	private void drawEnergy(Graphics2D g, double y1, double y2) {
		// Distance unit: 			pixel
		// Angular Velocity unit: 	radians / frame
		// Velocity unit: 			pixels / frame
		
		// Potential Energy		
		double h1 = (r1 + r2) - y1;
		double h2 = (r1 + r2) - y2;
		double Ep = m1 * this.g * h1 + m2 * this.g *h2;
		
		// Kinetic Energy
		double Ek = 0.5 * m1 * r1*r1 * a1_v*a1_v  +  0.5 * m2 *  (r1*r1 * a1_v*a1_v + r2*r2 * a2_v*a2_v + 2 * r1 * r2 * a1_v * a2_v * Math.cos(a1-a2));
		
		double E_tot = Ep + Ek;
		
		if (t % 8 == 0) {
			drawnEnergy[0] = (int) Ep;
			drawnEnergy[1] = (int) Ek;
			drawnEnergy[2] = (int) E_tot;
		}
		
		// Draw Energy bar
		int yBegin = 30;
		g.setColor(new Color(63, 112, 76));
		g.fillRoundRect(15, yBegin, 657, 30, 10, 10);
		g.setColor(new Color(63, 112, 109));		// Potential
		g.fillRoundRect(15, yBegin, (int) (657 * Ep / E_tot), 30, 10, 10);
		
		// Draw Text
		g.setFont(new Font("", Font.ITALIC, 14));
		g.setColor(new Color(173, 173, 173));
		g.drawString("Potential", 22, yBegin - 7);
		g.drawString("Kinetic", 620, yBegin - 7);
		
		g.setFont(new Font("", Font.BOLD, 16));
		g.drawString("" + drawnEnergy[2], 324, yBegin + 19);
		g.drawString("" + drawnEnergy[0], 20, yBegin + 20);
		g.drawString("" + drawnEnergy[1], 635, yBegin + 20);
		g.drawString("Energy", 316, yBegin - 10);
		
	}
	
	private void findAngles() {
		
		double num1 = -g * (2 * m1 + m2) * Math.sin(a1);
		double num2 = -m2 * g * Math.sin(a1 - 2 * a2);
		double num3 = -2 * Math.sin(a1 - a2) * m2;
		double num4 = a2_v * a2_v * r2 + a1_v * a1_v * r1 * Math.cos(a1 - a2);
		double den = r1 * (2 * m1 + m2 - m2 * Math.cos(2 * a1 - 2 * a2));
		a1_a = (num1 + num2 + num3 * num4) / den;
		
		num1 = 2 * Math.sin(a1 - a2);
		num2 = (a1_v * a1_v * r1 * (m1 + m2));
		num3 = g * (m1 + m2) * Math.cos(a1);
		num4 = a2_v * a2_v * r2 * m2 * Math.cos(a1 - a2);
		den = r2 * (2 * m1 + m2 - m2 * Math.cos(2 * a1 - 2 * a2));
		a2_a = (num1 * (num2 + num3 + num4)) / den;
		
		
		a1_v += a1_a;
		a2_v += a2_a;
		
		a1 += a1_v;
		a2 += a2_v;

//		rk4.a1 = a1;
//		rk4.a2 = a2;
//		rk4.a1_v = a1_v;
//		rk4.a2_v = a2_v;
//		rk4.m1 = 10;
//		rk4.m2 = 3;
//		rk4.r1 = rk4.r2 = 150;
//		
//		double dt = 0.8;
//		State[] state = {
//				new State(a1, a1_v),
//				new State(a2, a2_v)
//		};
//		State[] output = rk4.integrate(state, t, dt);
//		
//		a1 = output[0].theta;
//		a2 = output[1].theta;
//		a1_v = output[0].v;
//		a2_v = output[1].v;
	}
	
	public void draw(Graphics2D g, boolean runCalculations) {
		t++;
		
		if (runCalculations) {
			// Calculate angles
			if (dragging == 0)
				findAngles();
		}
					
		// Calculate coords
		double x1 = r1 * Math.sin(a1);
		double y1 = r1 * Math.cos(a1);
		
		double x2 = x1 + r2 * Math.sin(a2);
		double y2 = y1 + r2 * Math.cos(a2);	
		
		// Calculate Energy
		drawEnergy(g, y1, y2);
		
		// Draw multi graph
		drawMultiGraph(g, runCalculations);
		
		// Draw time graph
		drawTimeGraph(g, runCalculations);
		
		// Draw frames
		drawFrames(g);
		
		// Draw graphs
		double[] acceleration = {a1_a, a2_a};
		double[] velocity = {a1_v, a2_v};
		accGraph.draw(g, acceleration, runCalculations);
		velGraph.draw(g, velocity, runCalculations);
		
		// Translate
		g.translate(fixedPoint.x, fixedPoint.y);
		g.setStroke(new BasicStroke(5));
		
		
		// Draw path
		drawPath(g, x2, y2);
		
		// Draw pendulum
		g.setStroke(new BasicStroke(4));
		
		g.setColor(new Color(195, 195, 195));
		g.drawLine(0, 0, (int) x1, (int) y1);
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
		
		drawPoint(g, (int) x1, (int) y1, pRadius, new Color(131, 131, 131));
		drawPoint(g, (int) x2, (int) y2, pRadius, new Color(131, 131, 131));
		
		drawPoint(g, (int) x1, (int) y1, 4, p1Color);
		drawPoint(g, (int) x2, (int) y2, 4, p2Color);
		
		// Translate back
		g.translate(-fixedPoint.x, -fixedPoint.y);
		g.setStroke(new BasicStroke(1));
	}
	
	private void drawPath(Graphics2D g, double x2, double y2) {
		if (dragging == 0) {
			int[] point = {(int) x2, (int) y2};
			path.add(0, point);
		}
		
		if (deletePath >= 0)
			if (path.size() > deletePath+1) 
				path.subList(deletePath, path.size()).clear();
		
		if (path.size() >= 2) {
			g.setStroke(new BasicStroke(1));
			
			int freq = 1000;
			for (int i = path.size()-1; i > 0; i--) {
				Color color = Color.getHSBColor(((float) (t - i) % freq)/freq, 1, 0.7f);
				
				if (deletePath >= 0) {
					color = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (255 * (path.size()-1 - i) / deletePath));
				}
				
				g.setColor(color);
				g.drawLine(path.get(i)[0], path.get(i)[1], path.get(i-1)[0], path.get(i-1)[1]);
			}
		}
	}

	private void drawTimeGraph(Graphics2D g, boolean addToGraph) {
		
		int xBegin = main.getWidth() - 310, yBegin = 805;
		int height = 70;
		if (dragging == 0 && addToGraph) {
			a1 = (a1 + Math.PI) % (2*Math.PI) - Math.PI;
			a2 = (a2 + Math.PI) % (2*Math.PI) - Math.PI;
			
			int g1 = (int) (a1 * 70.0 / (Math.PI));
			int g2 = (int) (a2 * 70.0 / (Math.PI));
			
			if (g1 < -70) g1 += 140;
			if (g2 < -70) g2 += 140;
			
			graph1.add(0, yBegin - g1);
			graph2.add(0, yBegin - g2);
			if (graph1.size() > 311) graph1.subList(310, graph1.size()).clear();
			if (graph2.size() > 311) graph2.subList(310, graph2.size()).clear();
		}
		
		// Draw axis
		g.setColor(new Color(161, 161, 161));
		g.drawLine(xBegin-10, yBegin, main.getWidth(), yBegin);
		g.drawLine(xBegin, yBegin - height - 5, xBegin, yBegin + height + 5);
		
		
		// Draw Labels
		g.setFont(new Font("", Font.PLAIN, 14));
		g.drawString("\u03C0", xBegin - 14, yBegin - height);
		g.drawString("-\u03C0", xBegin - 19, yBegin + height + 4);
		
		// Draw graphs
		g.setColor(p1Color);
		for (int i = 0; i < graph1.size()-1; i++) {
			if (Math.abs(graph1.get(i) - graph1.get(i+1)) > 70) 
				g.drawLine(xBegin + i, graph1.get(i), xBegin + i, graph1.get(i));
			else 
				g.drawLine(xBegin + i, graph1.get(i), xBegin + i + 1, graph1.get(i+1));
		}
		drawPoint(g, xBegin, graph1.get(0), 3, null);
		
		g.setColor(p2Color);
		for (int i = 0; i < graph2.size()-1; i++) {
			if (Math.abs(graph2.get(i) - graph2.get(i+1)) > 70) 
				g.drawLine(xBegin + i, graph2.get(i), xBegin + i, graph2.get(i));
			else 
				g.drawLine(xBegin + i, graph2.get(i), xBegin + i + 1, graph2.get(i+1));
		}
		drawPoint(g, xBegin, graph2.get(0), 3, null);
		
		// Draw title
		String title = "Angle";
		g.setColor(new Color(161, 161, 161));
		g.setFont(new Font("", Font.BOLD, 15));
		FontMetrics fm = g.getFontMetrics();
		g.drawString(title, xBegin + (main.getWidth() - xBegin)/2 - fm.stringWidth(title)/2 , yBegin - height);
	}
	
	private void drawMultiGraph(Graphics2D g, boolean addToGraph) {
		int originX = main.getWidth() - 165, originY = 540;
		int width = 130, margin = 5;;
		int deletePath = 200;
		
		// Draw axisses
		g.setColor(new Color(161, 161, 161));
		g.drawLine(originX - width - margin, originY, originX + width + margin, originY);
		g.drawLine(originX, originY - width - margin, originX, originY + width + margin);
		
		// Draw labels
		g.setFont(new Font("", Font.PLAIN, 14));
		g.setColor(p1Color);
		g.drawString("\u03B81", originX + 110, originY + 14);
		g.setColor(p2Color);
		g.drawString("\u03B82", originX - 18, originY - 110);
		
		// Draw Graph
		if (dragging == 0 && addToGraph) {
			a1 = (a1 + Math.PI) % (2*Math.PI) - Math.PI;
			a2 = (a2 + Math.PI) % (2*Math.PI) - Math.PI;
			
			int g1 = (int) (a1 * width / (Math.PI));
			int g2 = (int) (a2 * width / (Math.PI));
			
			if (g1 < -width) g1 += 2*width;
			if (g2 < -width) g2 += 2*width;
			
			int[] point = {g1, g2};
			multiGraph.add(0, point);
			if (multiGraph.size() > deletePath + 1) multiGraph.subList(deletePath, multiGraph.size()).clear();
		}
		
		g.setColor(new Color(74, 147, 68));
		for (int i = multiGraph.size()-1; i > 0; i--) {
			g.setColor(new Color(74, 147, 68, (int) (255.0 * (multiGraph.size()-1 - i) / deletePath)));
			
			if (Math.abs(multiGraph.get(i)[0] - multiGraph.get(i-1)[0]) > 100 || Math.abs(multiGraph.get(i)[1] - multiGraph.get(i-1)[1]) > 100)
				g.drawLine(originX + multiGraph.get(i)[0], originY - multiGraph.get(i)[1], originX + multiGraph.get(i)[0], originY - multiGraph.get(i)[1]);
			else 
				g.drawLine(originX + multiGraph.get(i)[0], originY - multiGraph.get(i)[1], originX + multiGraph.get(i-1)[0], originY - multiGraph.get(i-1)[1]);
			
		}
		drawPoint(g, originX + multiGraph.get(0)[0], originY -  multiGraph.get(0)[1], 4, null);
	}


	private void drawFrames(Graphics2D g) {
		g.setColor(new Color(130, 129, 129));
		g.setFont(new Font("", Font.BOLD, 16));
		g.drawString("FPS: 60", 17 , 85);
		g.drawString("Frame: " + t, 17 , 108);
	}
	
	public void drawPoint(Graphics2D g, int x, int y, int r, Color color) {
		if (color != null) g.setColor(color);
		g.fillOval(x - r, y - r, 2*r, 2*r);
	}
	
	public void skipSimulation(int times) {
		if (!main.paused) return;
		for (int i = 0; i < times; i++) {
			findAngles();
			
		}
		main.repaint();
	}
	
	public Point getP1Pos() {
		return new Point((int) (r1 * Math.sin(a1)) + fixedPoint.x, (int) (r1 * Math.cos(a1)) + fixedPoint.y);
	}
	public Point getP2Pos() {
		return new Point((int) (r1 * Math.sin(a1) + r2 * Math.sin(a2)) + fixedPoint.x, (int) (r1 * Math.cos(a1) + r2 * Math.cos(a2)) + fixedPoint.y);
	}

	public Point getFixedPoint() {
		return fixedPoint;
	}
}
