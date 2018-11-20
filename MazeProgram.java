import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.awt.Rectangle;
import javax.sound.sampled.*;
import java.awt.GradientPaint;
import java.net.*;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class MazeProgram extends JPanel implements KeyListener,MouseListener
{//add ons: music, starting gif(press spacebar to continue), color change when hit spacebar
	JFrame frame;
	int x = 100;
	Thread thread;
	int movDig = -5;
	boolean gameOn = true;
	ArrayList <Wall> list = new ArrayList<Wall>();
	BufferedImage heroImg;
	int moves;
	int counter=3;
	int colorChange;
	int xx=0;
	int yy=0;
	boolean canMove;
	boolean endScreen=false;
	JFrame f;
	Clip clip;
	int rowCount;
	int[]xLRect = new int[4];
	int[]yLRect = new int[4];
	int[]xLTrap = new int[4];
	int[]yLTrap = new int[4];
	int[]xRTrap = new int[4];
	int[]yRTrap = new int[4];
	int currentRow;
	JPanel jPanel;
	int currentCol=1;
	String[][]maze = new String[22][40];
	ArrayList<Wall3D> walls=new ArrayList<Wall3D>();
	ArrayList<Wall3D> wallsR=new ArrayList<Wall3D>();
	ArrayList<Ceiling> traps=new ArrayList<Ceiling>();
	ArrayList<Ceiling> trapsR=new ArrayList<Ceiling>();
	boolean right=false,left=false,up=false;
	boolean check = true;
	URL url;
	int j=1;
	public MazeProgram () {
		createMaze();

		frame = new JFrame("Maze Runner");
		frame.add(this);
		frame.setSize(1350,1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try{
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("game.wav"));
			clip = AudioSystem.getClip();
			clip.open(audioStream);
			}
		catch(LineUnavailableException lue){}
		catch(UnsupportedAudioFileException uafe){}
		catch(IOException ioe){System.out.println("Hello?");}
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		frame.setVisible(true);
		frame.addKeyListener(this);
		this.addMouseListener(this);

		 f = new JFrame("Start screen");
		 f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jPanel = new JPanel();
		 jPanel.setBackground(Color.BLACK);
		 jPanel.setPreferredSize(new Dimension (300,300));
		 ImageIcon image = new ImageIcon("start.gif");
		 JLabel label = new JLabel(image);
		 jPanel.add(label);
		 f.getContentPane().add(jPanel);
		 f.pack();
		 f.setVisible(true);
		 f.addKeyListener(this);
			check = false;

	}

	public void createMaze()
	{
		int r = 0;
		for(int i=0;i<maze.length;i++) {
			for(int j=0;j<maze[0].length;j++) {
				maze[i][j]=" ";
			}
		}
		File name = new File("MazeDesign.txt");
		try
		{
			BufferedReader input = new BufferedReader(new FileReader(name));
			String text;


			while( (text=input.readLine())!=null)
			{
				for(int i=0;i<text.length();i++) {
					if((int)(text.charAt(i))==42) {
						list.add(new Wall(r, i, 30,30,100) );
						maze[rowCount][i]= "*";
					}
				}
				r++;
				rowCount++;
			}
		}
		catch (IOException io)
		{
			System.err.println("File does not exist");
		}
		createLeftRects();
		createRightRects();
		createLeftTrap();
		createRightTrap();

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		g2.setColor(Color.BLACK);
		g2.fillRect(0,0,1350,800);
		g2.setColor(Color.GRAY);
		g2.setFont(new Font("Arial",Font.PLAIN,50));
		g2.fillRect(100,0,1100,300);//ceiling
		g2.fillRect(100,500,1100,300);//ground
		g2.setColor(Color.RED);
		g2.fillRect(600,300,500,250);
		if(endScreen) {
				g2.setColor(Color.PINK);
				g2.drawString("YOU WIN",550,70);
		}
		g2.setColor(Color.RED);

		for(Wall3D w:walls) {
			if(colorChange%3==0)
				g2.setColor(Color.RED);
			if(colorChange%3==1)
				g2.setColor(Color.BLUE);
			if(colorChange%3==2)
				g2.setColor(Color.PINK);
			g2.fill(w.getPoly());
			g2.setColor(Color.BLACK);
			g2.draw(w.getPoly());
		}
		for(Ceiling w:traps) {
			if(colorChange%3==0)
				g2.setColor(Color.RED);
			if(colorChange%3==1)
				g2.setColor(Color.YELLOW);
			if(colorChange%3==2)
				g2.setColor(Color.BLUE);
			g2.fill(w.getTrap());
			g2.setColor(Color.BLACK);
			g2.draw(w.getTrap());
		}
		for(Ceiling w:trapsR) {
			if(colorChange%3==0)
				g2.setColor(Color.RED);
			if(colorChange%3==1)
				g2.setColor(Color.YELLOW);
			if(colorChange%3==2)
				g2.setColor(Color.BLUE);
			g2.fill(w.getTrap());
			g2.setColor(Color.BLACK);
			g2.draw(w.getTrap());
		}/*
		for(Ceiling w:trapsR) {
			g2.setPaint(w.getPaint());
			g2.fill(w.getTrap());
			g2.setColor(Color.BLACK);
			g2.draw(w.getTrap());
		}*/
		/*g2.setColor(Color.MAGENTA);
		g2.drawString(""+xx+","+yy,400,400);*/

	}
	public void createLeftRects() {

		for(int j=0;j<5;j++)
		{
			int[]xLRect = {100,100,750,750};
			int[]yLRect = {100,200,200,100};
			for(int i=0;i<4;i++){
				yLRect[i]+=100*j;
				if(i<2)
					xLRect[i]=xLRect[i]+(50*j);
				else
					xLRect[i]=xLRect[i]-(50*j);
			}
			walls.add(new Wall3D(xLRect,yLRect));
		}
	}
	public void createRightRects() {

		for(int j=0;j<5;j++)
		{
			int[]xRRect = {100,100,750,750};
			int[]yRRect = {1100,1200,1200,1100};
			for(int i=0;i<4;i++){
				yRRect[i]-=100*j;
				if(i<2)
					xRRect[i]+=(50*j);
				else xRRect[i]-=(50*j);
			}
			walls.add(new Wall3D(xRRect,yRRect));
		}

	}

		public void createLeftTrap() {
			traps.clear();

			if(counter==0) {//right
				for(int j=0;j<5;j++) {
					int[]xLTrap = {50,100,750,800};
					int[]yLTrap = {100,200,200,100};
					for(int i=0;i<4;i++) {
						yLTrap[i]+=100*j;
						if(i<2)
							xLTrap[i]+=(50*j);
						else xLTrap[i]-=(50*j);

					}
					if((currentRow-1) >= 0 && (currentCol + j)<maze[0].length && maze[currentRow-1][currentCol+j].equals("*"))
						traps.add(new Ceiling(xLTrap,yLTrap));
				}
			}
			if(counter==1) {//left
				for(int j=0;j<5;j++) {
					int[]xLTrap = {50,100,750,800};
					int[]yLTrap = {100,200,200,100};
					for(int i=0;i<4;i++) {
						yLTrap[i]+=100*j;
						if(i<2)
							xLTrap[i]+=(50*j);
						else xLTrap[i]-=(50*j);

					}
					if((currentRow+1) < maze.length && (currentCol - j)>=0 && maze[currentRow+1][currentCol-j].equals("*"))
						traps.add(new Ceiling(xLTrap,yLTrap));
				}
			}
			if(counter==2) {//up
				for(int j=0;j<5;j++) {
					int[]xLTrap = {50,100,750,800};
					int[]yLTrap = {100,200,200,100};
					for(int i=0;i<4;i++) {
						yLTrap[i]+=100*j;
						if(i<2)
							xLTrap[i]+=(50*j);
						else xLTrap[i]-=(50*j);

					}
					if((currentCol-1) >= 0 && (currentRow - j)>=0 && maze[currentRow-j][currentCol-1].equals("*"))
						traps.add(new Ceiling(xLTrap,yLTrap));
				}
			}
			if(counter==3) {//down
				for(int j=0;j<5;j++) {
					int[]xLTrap = {50,100,750,800};
					int[]yLTrap = {100,200,200,100};
					for(int i=0;i<4;i++) {
						yLTrap[i]+=100*j;
						if(i<2)
							xLTrap[i]+=(50*j);
						else xLTrap[i]-=(50*j);

					}
					if((currentCol+1) < maze[0].length && (currentRow + j)<maze.length && maze[currentRow+j][currentCol+1].equals("*"))
						traps.add(new Ceiling(xLTrap,yLTrap));
				}
			}


		}

		public void createRightTrap() {
				trapsR.clear();
				if(counter==0) {//right
					for(int j=0;j<5;j++) {
						int[]xRTrap = {100,50,800,750};
						int[]yRTrap = {1100,1200,1200,1100};
						for(int i=0;i<4;i++) {
							yRTrap[i]-=100*j;
							if(i<2)
								xRTrap[i]+=(50*j);
							else xRTrap[i]-=(50*j);

						}
						if(currentRow+1<maze.length && currentCol+j < maze[0].length && maze[currentRow+1][currentCol+j].equals("*"))
							trapsR.add(new Ceiling(xRTrap,yRTrap));
					}
				}
				if(counter==1) {//left
					for(int j=0;j<5;j++) {
						int[]xRTrap = {100,50,800,750};
						int[]yRTrap = {1100,1200,1200,1100};
						for(int i=0;i<4;i++) {
							yRTrap[i]-=100*j;
							if(i<2)
								xRTrap[i]+=(50*j);
							else xRTrap[i]-=(50*j);

						}
						if(currentCol-j >0 && maze[currentRow-1][currentCol-j].equals("*"))
							trapsR.add(new Ceiling(xRTrap,yRTrap));
					}
				}
				if(counter==2) {//up
					for(int j=0;j<5;j++) {
						int[]xRTrap = {100,50,800,750};
						int[]yRTrap = {1100,1200,1200,1100};
						for(int i=0;i<4;i++) {
							yRTrap[i]-=100*j;
							if(i<2)
								xRTrap[i]+=(50*j);
							else xRTrap[i]-=(50*j);

						}
						if(currentRow-j>=0 && maze[currentRow-j][currentCol+1].equals("*"))
							trapsR.add(new Ceiling(xRTrap,yRTrap));
					}
				}
				if(counter==3) {//down
					for(int j=0;j<5;j++) {
						int[]xRTrap = {100,50,800,750};
						int[]yRTrap = {1100,1200,1200,1100};
						for(int i=0;i<4;i++) {
							yRTrap[i]-=100*j;
							if(i<2)
								xRTrap[i]+=(50*j);
							else xRTrap[i]-=(50*j);

						}
						if(currentRow+j<maze.length && maze[currentRow+j][currentCol-1].equals("*"))
							trapsR.add(new Ceiling(xRTrap,yRTrap));
					}
				}
	}
	public void wallInFront()
	{
				if(counter==0) {//right
					for(int j=4;j>=0;j--) {
						int[]xRTrap = {50,50,800,800};
						int[]yRTrap = {100,1200,1200,100};
						for(int i=0;i<4;i++) {

							if(i<2)
								xRTrap[i]+=(50*j);
							else xRTrap[i]-=(50*j);


						}
							yRTrap[0]+=100*j;
							yRTrap[1]-=100*j;
							yRTrap[2]-=100*j;
							yRTrap[3]+=100*j;
						if(currentCol+j < maze[0].length && maze[currentRow][currentCol+j].equals("*"))
							trapsR.add(new Ceiling(xRTrap,yRTrap));
					}
				}
				if(counter==1) {//left
					for(int j=4;j>=0;j--) {
						int[]xRTrap = {50,50,800,800};
						int[]yRTrap = {100,1200,1200,100};
						for(int i=0;i<4;i++) {

							if(i<2)
								xRTrap[i]+=(50*j);
							else xRTrap[i]-=(50*j);


						}
							yRTrap[0]+=100*j;
							yRTrap[1]-=100*j;
							yRTrap[2]-=100*j;
							yRTrap[3]+=100*j;
						if(currentCol-j>=0 && maze[currentRow][currentCol-j].equals("*"))
							trapsR.add(new Ceiling(xRTrap,yRTrap));
					}
				}
				if(counter==2) {//up
					for(int j=4;j>=0;j--) {
						int[]xRTrap = {50,50,800,800};
						int[]yRTrap = {100,1200,1200,100};
						for(int i=0;i<4;i++) {

							if(i<2)
								xRTrap[i]+=(50*j);
							else xRTrap[i]-=(50*j);


						}
							yRTrap[0]+=100*j;
							yRTrap[1]-=100*j;
							yRTrap[2]-=100*j;
							yRTrap[3]+=100*j;
						if(currentRow-j >=0 && maze[currentRow-j][currentCol].equals("*"))
							trapsR.add(new Ceiling(xRTrap,yRTrap));
					}
				}
				if(counter==3) {//down
					for(int j=4;j>=0;j--) {
						int[]xRTrap = {50,50,800,800};
						int[]yRTrap = {100,1200,1200,100};
						for(int i=0;i<4;i++) {

							if(i<2)
								xRTrap[i]+=(50*j);
							else xRTrap[i]-=(50*j);


						}
							yRTrap[0]+=100*j;
							yRTrap[1]-=100*j;
							yRTrap[2]-=100*j;
							yRTrap[3]+=100*j;
						if(currentRow+j < maze.length && maze[currentRow+j][currentCol].equals("*"))
							trapsR.add(new Ceiling(xRTrap,yRTrap));
					}
				}


	}

	public void keyReleased(KeyEvent e) {
		canMove=false;
	}
	public void keyPressed(KeyEvent e) {
		if(counter==0) { //right
			if(e.getKeyCode()==39) //right
				counter=3;//down
			else if(e.getKeyCode()==37)//left
				counter=2;//up
			else if(e.getKeyCode()==38) {//move forward
					if( (currentCol+1)< maze[0].length && maze[currentRow][currentCol+1].equals(" ") )
						currentCol++;
				}
		}
		else if(counter==1) { //left
			if(e.getKeyCode()==39) //right
				counter=2;//up
			else if(e.getKeyCode()==37)//left
				counter=3;//down
			else if(e.getKeyCode()==38) {
				if( (currentCol-1) >= 0 && maze[currentRow][currentCol-1].equals(" ") )
					currentCol--;
			}
		}
		else if(counter==2) { //up
			if(e.getKeyCode()==39) //right
				counter=0;//right
			else if(e.getKeyCode()==37)//left
				counter=1;//left
			else if(e.getKeyCode()==38) {
				if( (currentRow-1) >= 0 && maze[currentRow-1][currentCol].equals(" ") )
					currentRow--;
			}
		}
		else if(counter==3) {//down

			if(e.getKeyCode()==39) //right
				counter=1;//left
			else if(e.getKeyCode()==37)//left
				counter=0;//right
			else if(e.getKeyCode()==38) {//up
				if( (currentRow+1)< maze.length && maze[currentRow+1][currentCol].equals(" ") )
					currentRow++;
			}
		}

		if(e.getKeyCode()==32) {
			if(!check){
				f.setVisible(false);
			}
			colorChange++;
		}
		System.out.println(currentRow + " " + currentCol);
		if(currentRow==21 && currentCol == 39)
			endScreen=true;
		createLeftRects();
		createRightRects();
		createLeftTrap();
		createRightTrap();
		wallInFront();
		repaint();


	}
	public void keyTyped(KeyEvent e) {

	}

	public void mouseClicked(MouseEvent e) {
		xx=e.getX();
		yy=e.getY();
		repaint();
	}

	public void mousePressed(MouseEvent e) {

	}
	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}
	public void mouseExited(MouseEvent e) {

	}




	public static void main(String args[])
	{
		MazeProgram app=new MazeProgram();
	}





}
