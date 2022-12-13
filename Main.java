package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel implements Runnable {
	
	private static final long serialVersionUID = 1L;
	
	private JFrame frame;
	private JPanel container;
	public Dimension dim;
	private Simulation simulation;
	private InputScreen inputScreen;
	private DragManager dragMng;
	
	public Color backgroundColor;
	public Color containerColor;
	
	private Thread thread;
	private ScheduledFuture<?> future;
	private ScheduledExecutorService executor;
	public boolean paused = false;
	
	public Main() {
		super();
		frame = new JFrame();
		dim = new Dimension(1000,900);
		
		frame.setSize(dim);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		backgroundColor = new Color(49, 49, 49);
		containerColor = new Color(30, 30, 30);
		
		container = new JPanel();
		container.setPreferredSize(new Dimension(dim.width + 14, dim.height + 15));
		container.setBackground(containerColor);
		
		this.setPreferredSize(dim);
		this.setFocusable(true);
		this.setBackground(containerColor);
		this.setLayout(null);
	
		simulation = new Simulation(this);
		inputScreen = new InputScreen(simulation, this);
		
		dragMng = new DragManager(this, simulation);
		this.addMouseListener(dragMng);
		this.addMouseMotionListener(dragMng);
		
		this.add(inputScreen);
		container.add(this);
		
		frame.add(container);
		frame.pack();
		frame.setVisible(true);
		frame.setTitle("Double Pendulum Simulation");
	
		start();
	}
	

	private void start() {
		thread = new Thread(this);
		executor = Executors.newScheduledThreadPool(1);
		future = executor.scheduleAtFixedRate(thread, 0, 1000/60, TimeUnit.MILLISECONDS);
	}
	public void pause() {
		if (paused) return;
		paused = true;
		future.cancel(false);
		
		
	}
	public void resume() {
		if (!paused) return;
		paused = false;
		future = executor.scheduleAtFixedRate(thread, 0, 1000/60, TimeUnit.MILLISECONDS);
	}

	
	@Override 
	public void paintComponent(Graphics graphics ) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		
		g.setColor(backgroundColor);
		g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
		
		
		// Draw pendulum
		if (simulation != null) simulation.draw(g, !paused);
		
		// Credits
		g.setColor(new Color(130, 129, 129));
		g.setFont(new Font("", Font.BOLD, 14));
		g.drawString("By M. Veul & S. Lustig",  getWidth() - 160, getHeight() -5);
		
	}
	
	
	
	public static void main(String[] args) {
		new Main();
	}
	
	public static double round(double value, int places) {
		BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

	@Override
	public void run() {
		this.repaint();
		
	}


}
