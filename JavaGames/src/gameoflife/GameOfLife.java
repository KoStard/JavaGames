package gameoflife;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.GameTemplate;
import main.Starter;

@SuppressWarnings("serial")
public class GameOfLife extends JFrame implements GameTemplate{
	
	public Random random;

	public static Color blockColor = new Color(69,105,144);
	public static Color activeBlockColor = new Color(78,165,217);
	public static Color backgroundColor = new Color(50,50,44);
	
	int blocksWidth, blocksHeight;
	
	public static Dimension blockDim = new Dimension(12,12), 
			blockPadding = new Dimension(1,1),
			btnDimension = new Dimension(70, 30),
			realBlockDim = new Dimension(blockDim.width+blockPadding.width*2, blockDim.height+blockPadding.height*2);
	public int width, height;
	public Canvas canvas;
	
	boolean[][] matrix;
	public boolean running = true,
			working = false;
	
	int frameDelay = 150;
	Deque<boolean[][]> lastGens = new LinkedList<boolean[][]>();
	final int maxLastGensSize = 40;

	boolean mousePressed = false, reviving = false, dragged = false;
	

	JButton StartPauseContinueButton = new JButton("Start");
	
	public GameOfLife(int blocksWidth, int blocksHeight){
		random = new Random();
		
		this.blocksWidth = blocksWidth;
		this.blocksHeight = blocksHeight;
		matrix = new boolean[blocksHeight][blocksWidth];
		width = blocksWidth*(blockDim.width+blockPadding.width*2);
		height = blocksHeight*(blockDim.height+blockPadding.height*2);
		
		JFrame jframe = Starter.getJFrame("Game Of Life", width, height);
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setBackground(backgroundColor);
		
		jframe.setFocusable(true);
		jframe.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					matrix = getLastGen();
					pauseCycles();
					break;
				case KeyEvent.VK_RIGHT:
					matrix = getNextGen();
					pauseCycles();
					break;
				case KeyEvent.VK_SPACE:
					if (working) pauseCycles();
					else startCycles();
//					switch(StartPauseContinueButton.getText()){
//					case "Start":
//						StartPauseContinueButton.setText("Pause");
//						startCycles();
//						break;
//					case "Pause":
//						StartPauseContinueButton.setText("Resume");
//						pauseCycles();
//						break;
//					case "Resume":
//						StartPauseContinueButton.setText("Pause");
//						startCycles();
//						break;
//					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
			
		});
		
		
		canvas.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				mousePressed = true;
				int x = e.getX()/(realBlockDim.width);
				int y = e.getY()/(realBlockDim.width);
				
				reviving = !matrix[y][x];
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (!dragged) {
					int x = e.getX()/(realBlockDim.width);
					int y = e.getY()/(realBlockDim.width);
					matrix[y][x] = !matrix[y][x];
				}
				canvas.repaint();
				dragged = false;
				mousePressed = false;
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		canvas.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				if (mousePressed) {
					dragged = true;
					int x = e.getX()/(realBlockDim.width);
					int y = e.getY()/(realBlockDim.width);
					if (y >= matrix.length || x >= matrix[0].length || x < 0 || y < 0) return;
					if (reviving!=matrix[y][x]) {matrix[y][x] = !matrix[y][x]; canvas.repaint();}
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
			}
			
		});
		
		StartPauseContinueButton.setFocusable(false);
		StartPauseContinueButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (working) pauseCycles();
				else startCycles();
//				switch(StartPauseContinueButton.getText()){
//				case "Start":
//					StartPauseContinueButton.setText("Pause");
//					startCycles();
//					break;
//				case "Pause":
//					StartPauseContinueButton.setText("Resume");
//					pauseCycles();
//					break;
//				case "Resume":
//					StartPauseContinueButton.setText("Pause");
//					startCycles();
//					break;
//				}
			}
		});
		
		JButton ResetButton = new JButton("Reset");
		ResetButton.setFocusable(false);
		ResetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				matrix = getClearGen();
			}
		});
		
		JButton RandomizeButton = new JButton("Randomize");
		RandomizeButton.setFocusable(false);
		RandomizeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				matrix = getRandomGen();
			}
		});
		
		JButton LastGenButton = new JButton("<< Last");
		LastGenButton.setFocusable(false);
		LastGenButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				matrix = getLastGen();
				pauseCycles();
			}
		});
		
		JButton NextGenButton = new JButton("Next >>");
		NextGenButton.setFocusable(false);
		NextGenButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				matrix = getNextGen();
				pauseCycles();
			}
		});
		
		JPanel btnpanel = new JPanel();

		btnpanel.add(LastGenButton);
		btnpanel.add(RandomizeButton);
		btnpanel.add(StartPauseContinueButton);
		btnpanel.add(ResetButton);
		btnpanel.add(NextGenButton);
		
		jframe.getContentPane().add(BorderLayout.CENTER, canvas);
		jframe.getContentPane().add(BorderLayout.SOUTH, btnpanel);
		jframe.setVisible(true);
		jframe.pack();
		
		while (running) {
			canvas.repaint();
			if (working) {
				tick();
			}
			try {
				Thread.sleep(frameDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
		System.out.println("Finishing program");
	}
	
	public int getNeighboursNumber(int x, int y) {
		int res = 0;
		for (int yi = y-1; yi <= y+1; yi++) {
			if (yi < 0 || yi >= matrix.length) continue;
			for (int xi = x-1; xi <= x+1; xi++) {
				if (xi < 0 || xi >= matrix[0].length) continue;
				if (xi == x && yi == y) continue;
				if (matrix[yi][xi]) res++;
			}
		}
		return res;
	}
	
	public boolean[][] getNextGen(){
		addToLastGens(matrix);
		boolean[][] newGen = new boolean[blocksHeight][blocksWidth];
		for (int y = 0; y < matrix.length; y++) {
			for (int x = 0; x < matrix[0].length; x++) {
				int neighbours = getNeighboursNumber(x, y);
				if (matrix[y][x]) {
					if (neighbours < 2 || neighbours > 3) newGen[y][x] = false;
					else newGen[y][x] = true;
				} else {
					if (neighbours == 3) newGen[y][x] = true;
					else newGen[y][x] = false;
				}
			}
		}
		return newGen;
	}
	
	public boolean[][] getLastGen() {
		boolean[][] last = lastGens.pollLast();
		return (last!=null?last:matrix);
	}
	
	public void addToLastGens(boolean[][] matrix) {
		lastGens.addLast(matrix);
		if (lastGens.size() > maxLastGensSize) {
			lastGens.pollFirst();
		}
	}
	
	public boolean[][] getClearGen(){
		addToLastGens(matrix);
		boolean[][] newGen = new boolean[blocksHeight][blocksWidth];
		return newGen;
	}
	
	public boolean[][] getRandomGen() {
		addToLastGens(matrix);
		boolean[][] newGen = new boolean[blocksHeight][blocksWidth];
		for (int y = 0; y < matrix.length; y++) {
			for (int x = 0; x < matrix[0].length; x++) {
				newGen[y][x] = random.nextBoolean();
			}
		}
		return newGen;
	}
	
	public void tick() {
		matrix = getNextGen();
	}
	
	class Canvas extends JPanel{
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2 = (Graphics2D) g;
			for (int y = 0; y < matrix.length; y++) {
				for (int x = 0; x < matrix[0].length; x++) {
					if (matrix[y][x]) {
						g2.setColor(activeBlockColor);
					} else {
						g2.setColor(blockColor);
					}
					Rectangle2D rect = new Rectangle2D.Double(x*(blockDim.width+blockPadding.width*2)+blockPadding.width, 
							y*(blockDim.height+blockPadding.height*2)+blockPadding.height, 
							blockDim.width, blockDim.height);
					g2.fill(rect);
				}
			}
		}
	}

	@Override
	public void start() {
		running = true;
	}

	@Override
	public void end() {
		running = false;
	}
	
	public void startCycles() {
		if (!working) {
			working = true;
			StartPauseContinueButton.setText("Pause");
		}
	}
	
	public void pauseCycles() {
		if (working) {
			working = false;
			StartPauseContinueButton.setText("Resume");
		}
	}

	@Override
	public void reset() {
	}
}
