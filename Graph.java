package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class Graph {
	
	private Simulation sim;
	
	private Point p;
	private int width, height; 
	private double min;
	
	private double varHeight;
	private String title;
	
	private ArrayList<double[]> values;
	
	public Graph(Simulation sim, String title, Point origin, int width, int height, double min) {
		this.sim = sim;
		this.p = origin;
		this.width = width;
		this.height = height;
		this.min = min;
		this.title = title;
		
		varHeight = min;
		
		values = new ArrayList<double[]>();
	}
	
	
	
	public void draw(Graphics2D g, double[] vars, boolean addToGraph) {
		
		// Draw axisses
		g.setColor(new Color(161, 161, 161));
		g.drawLine(p.x, p.y - height - 5, p.x, p.y + height + 5);
		g.drawLine(p.x - 5, p.y, p.x + width, p.y);
		
		
		if (sim.dragging == 0 && addToGraph) {
			// Add vars
			values.add(0, vars);
			if (values.size() > width + 1) values.subList(width, values.size()).clear();
			
			// Determine varHeight
			double max = 0;
			for (int i = 0; i < values.size(); i ++) {
				if (Math.abs(values.get(i)[0]) > max) 
					max = Math.abs(values.get(i)[0]);
				else if (Math.abs(values.get(i)[1]) > max)
					max = Math.abs(values.get(i)[1]);
			}
			varHeight = (max < min? min : max);
		}
		
		// Generate graph
		ArrayList<int[]> graph = new ArrayList<int[]>();
		for (int i = 0; i < values.size(); i++) {
			int y1 = (int) (values.get(i)[0] * height / varHeight);
			int y2 = (int) (values.get(i)[1] * height / varHeight);
			int[] arr = {p.y - y1, p.y - y2};
			graph.add(arr);
		}
		
		
		// Draw Graph
		double fade = 50;
		for (int i = 0; i < graph.size()-1; i++) {
			
			int alpha = 255;
			if ((graph.size() - i) <= fade) 
				alpha = (int) ((double) (graph.size() - i) / fade * 255.0);
			
			
			Color color = new Color(sim.p1Color.getRed(), sim.p1Color.getGreen(), sim.p1Color.getBlue(), alpha);
			g.setColor(color);
			g.drawLine(p.x + i, graph.get(i)[0], p.x + i + 1, graph.get(i+1)[0]);
			
			color = new Color(sim.p2Color.getRed(), sim.p2Color.getGreen(), sim.p2Color.getBlue(), alpha);
			g.setColor(color);
			g.drawLine(p.x + i, graph.get(i)[1], p.x + i + 1, graph.get(i+1)[1]);
		}
		
		// Draw points
		sim.drawPoint(g, p.x, graph.get(0)[0], 3, sim.p1Color);
		sim.drawPoint(g, p.x, graph.get(0)[1], 3, sim.p2Color);
		
		// Draw axis label 
		g.setFont(new Font("", Font.PLAIN, 14));
		g.setColor(new Color(161, 161, 161));
		g.drawString("" + Main.round(varHeight, 3), p.x + 4, p.y - height + 4);
		g.drawString("-" + Main.round(varHeight, 3), p.x + 4, p.y + height + 5);
		
		// Draw title
		g.setFont(new Font("", Font.BOLD, 15));
		FontMetrics fm = g.getFontMetrics();
		g.drawString(title, p.x + width/2 - fm.stringWidth(title)/2 , p.y - height);
	}
	
	public void clear() {
		values.clear();
	}
}
