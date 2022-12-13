package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicSliderUI;

public class CustomSliderUI extends BasicSliderUI  {

	private static final long serialVersionUID = 1L;
	
	private final Dimension THUMB_SIZE = new Dimension(15,15);
	private final RoundRectangle2D.Float trackShape = new RoundRectangle2D.Float();
	
	
	@Override
    protected void calculateTrackRect() {
        super.calculateTrackRect();
        
        trackRect.y = trackRect.y + (trackRect.height - 8) / 2;
        trackRect.height = 6;
        
        trackShape.setRoundRect(trackRect.x, trackRect.y, trackRect.width, trackRect.height, 8, 8);
    }
	
	@Override
	protected void calculateThumbLocation() {
		super.calculateThumbLocation();
		thumbRect.y = trackRect.y + (trackRect.height - thumbRect.height) / 2;
		
	}
	
	
	
	@Override
    public void paint(final Graphics g, final JComponent c) {
         ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         super.paint(g, c);
         
         
    }
	
	@Override
    public void paintTrack(final Graphics g) {
         Graphics2D g2 = (Graphics2D) g;
         Shape clip = g2.getClip();

         boolean inverted = slider.getInverted();

         // Paint shadow.
         g2.setColor(new Color(82, 82, 82));
         g2.fill(trackShape);

         // Paint track background.
         g2.setColor(new Color(130, 130, 130));
         g2.setClip(trackShape);
         trackShape.y += 1;
         g2.fill(trackShape);
         trackShape.y = trackRect.y;

         g2.setClip(clip);

         // Paint selected track.
         boolean ltr = slider.getComponentOrientation().isLeftToRight();
         if (ltr) inverted = !inverted;
         int thumbPos = thumbRect.x + thumbRect.width / 2;
         if (inverted) {
             g2.clipRect(0, 0, thumbPos, slider.getHeight());
         } else {
             g2.clipRect(thumbPos, 0, slider.getWidth() - thumbPos, slider.getHeight());
         }

         
         g2.setColor(new Color(88, 88, 88));
         g2.fill(trackShape);
         g2.setClip(clip);
    }

     @Override
     public void paintThumb(final Graphics g) {
         g.setColor(new Color(71, 71, 71));
         g.fillOval(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
         g.setColor(new Color(117, 117, 117));
         g.drawOval(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
     }
     
     @Override
     protected Dimension getThumbSize() {
         return THUMB_SIZE;
     }
	
	@Override
    public void paintFocus(final Graphics g) {}
	
	@Override 
	public void calculateFocusRect() {
		try {
			super.calculateFocusRect();
		} catch (Exception e) {
			
		}
	}
}
