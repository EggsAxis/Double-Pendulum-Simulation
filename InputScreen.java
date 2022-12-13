package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class InputScreen extends JPanel implements ChangeListener, ActionListener {

	private static final long serialVersionUID = 1L;
	
	private Simulation sim;
	private Main main;
	private Color backgroundColor;
	
	private JSlider[] sliders;
	private int beginY = 65, padding = 50, x = 80;
	
	private JButton pauseBtn, skipBtn, resetBtn;
	
	
	public InputScreen(Simulation sim, Main main) {
		this.sim = sim;
		this.main = main;
		backgroundColor = new Color(61, 61, 61, 0);
		
		this.setBounds(main.dim.width - 310, 10, 300, 340);
		this.setFocusable(true);
		this.setPreferredSize(this.getSize());
		this.setBackground(main.backgroundColor);
		this.setLayout(null);
		
		initSliders();
		
		pauseBtn = new CustomButton(CustomButton.PAUSE_BUTTON, 133);
		pauseBtn.addActionListener(this);
		
		skipBtn = new CustomButton(CustomButton.SKIP_BUTTON, 180);
		skipBtn.addActionListener(this);
		
		resetBtn = new CustomButton(CustomButton.RESET_BUTTON, 86);
		resetBtn.addActionListener(this);
		
		this.add(resetBtn);
		this.add(skipBtn);
		this.add(pauseBtn);
		
	}
	
	
	
	private void initSliders() {
		
		sliders = new JSlider[5];
		
		for (int i = 0; i < sliders.length; i++) {
			if (i == 0) 				sliders[i] = new JSlider(JSlider.HORIZONTAL, 1, 200, 30);		// Gravity
			else if (i == 1 || i == 2) 	sliders[i] = new JSlider(JSlider.HORIZONTAL, 30, 200, 150);		// Radii
			else if (i == 3) 			sliders[i] = new JSlider(JSlider.HORIZONTAL, 10, 200, 100);		// Mass 1
			else if (i == 4) 			sliders[i] = new JSlider(JSlider.HORIZONTAL, 10, 200, 30);		// Mass 2
			
			sliders[i].setBounds(x, beginY + i*padding, 180, 40);
			sliders[i].setUI(new CustomSliderUI());
			sliders[i].addChangeListener(this);
			sliders[i].setBackground(backgroundColor);
			sliders[i].setFocusable(false);
			sliders[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
			
			this.add(sliders[i]);
		}
		this.repaint();
	}

	
	
	
	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		
		Graphics2D g = (Graphics2D) graphics;
		
		// Background
		g.setColor(new Color(61, 61, 61));
		g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 20, 20);
		
		// Labels 
		g.setColor(new Color(130, 129, 129));
		g.setFont(new Font("", Font.BOLD, 15));
		g.drawString("Gravity", 10, beginY + 0*padding + 22);
		g.drawString("Radius 1", 10, beginY + 1*padding + 22);
		g.drawString("Radius 2", 10, beginY + 2*padding + 22);
		g.drawString("Mass 1", 10, beginY + 3*padding + 22);
		g.drawString("Mass 2", 10, beginY + 4*padding + 22);
		
		
		g.drawString("" + sim.g, this.getWidth() - g.getFontMetrics().stringWidth("" + sim.g) - 8, beginY + 0*padding + 22);
		g.drawString("" + sim.r1, this.getWidth() - g.getFontMetrics().stringWidth("" + sim.r1) - 8, beginY + 1*padding + 22);
		g.drawString("" + sim.r2, this.getWidth() - g.getFontMetrics().stringWidth("" + sim.r2) - 8, beginY + 2*padding + 22);
		g.drawString("" + sim.m1, this.getWidth() - g.getFontMetrics().stringWidth("" + sim.m1) - 8, beginY + 3*padding + 22);
		g.drawString("" + sim.m2, this.getWidth() - g.getFontMetrics().stringWidth("" + sim.m2) - 8, beginY + 4*padding + 22);
		
		// Line
		g.setColor(new Color(80, 80, 80));
		g.setStroke(new BasicStroke(2));
		g.drawLine(10, beginY -5, getWidth() - 10, beginY - 5);
	}



	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider slider = (JSlider) e.getSource();
		
		if (slider.equals(sliders[0]))
			sim.g = (double) slider.getValue() / 100;
		else if (slider.equals(sliders[1]))
			sim.r1 = slider.getValue();
		else if (slider.equals(sliders[2]))
			sim.r2 = slider.getValue();
		else if (slider.equals(sliders[3]))
			sim.m1 = (double) slider.getValue() / 10;
		else if (slider.equals(sliders[4]))
			sim.m2 = (double) slider.getValue() / 10;
		
		this.repaint();
		if (main.paused) main.repaint();
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		CustomButton btn = (CustomButton) e.getSource();
		
		if (btn.equals(pauseBtn)) {
			btn.setState((btn.getState() + 1)%2);
			if (main.paused)
				main.resume();
			else
				main.pause();
		}
		
		else if (btn.equals(skipBtn)) 
			sim.skipSimulation(5);
		
		else if (btn.equals(resetBtn)) {
			sim.reset();
			
			sliders[0].setValue(30);
			sliders[1].setValue(150);
			sliders[2].setValue(150);
			sliders[3].setValue(100);
			sliders[4].setValue(30);
			
			this.repaint();
		}
		
	}
	
}
