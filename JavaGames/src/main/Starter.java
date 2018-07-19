package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.beans.EventHandler;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

enum Games {GAMEOFLIFE("Game Of Life");
	public String name = "";
	Games(String name) {
		this.name = name;
	}};
	
public class Starter {
	public static void main(String[] args) {
		Starter starter = new Starter(Games.GAMEOFLIFE);
	}
	
	
	// Variables
	public int width = 500, height = 500;
	
	class MyComponent extends JComponent {
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			Ellipse2D el = new Ellipse2D.Double(0, 0, 100, 100);
			g2.draw(el);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Ellipse2D el2 = new Ellipse2D.Double(200, 0, 100, 100);
			g2.draw(el2);
			
		}
	}
	
	public Starter(Games game) {
		JFrame jframe = getJFrame(game.name);
		jframe.add(new MyComponent());
	}
	
	
	
	public JFrame getJFrame(String title) {
		JFrame jframe = new JFrame() {};
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		jframe.setBounds(screenSize.width/2-width/2, screenSize.height/2-height/2, width, height);
		jframe.setTitle(title);
		return jframe;
	}
}
