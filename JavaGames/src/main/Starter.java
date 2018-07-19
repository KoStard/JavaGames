package main;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import gameoflife.GameOfLife;

enum Games {GAMEOFLIFE("Game Of Life");
	public String name = "";
	Games(String name) {
		this.name = name;
	}};
	
public class Starter extends JFrame {
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new Starter(Games.GAMEOFLIFE);
	}
	
	
	// Variables
	
	public Starter(Games gamenum) {
		GameTemplate game;
		switch (gamenum) {
		case GAMEOFLIFE:
			game = new GameOfLife(48, 48);
		}
	}
	
	public static JFrame getJFrame(String title, int width, int height) {
		JFrame jframe = new JFrame() {
			private static final long serialVersionUID = 1L;};
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		jframe.setTitle(title);
		Dimension screenSize = toolkit.getScreenSize();
//		jframe.setVisible(true);
//		jframe.setBounds(screenSize.width/2-width/2, screenSize.height/2-height/2, width, height);
//		jframe.setPreferredSize(new Dimension(width, height));
//		jframe.pack();
		return jframe;
	}
}
