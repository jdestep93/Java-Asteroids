import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//sound libraries
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.*;
import javax.swing.*;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class GameBoard extends JFrame{
	
	public static int boardWidth = 1000;
	public static int boardHeight = 1000;
	
	public static boolean keyHeld = false;
	public static int keyHeldCode;
	
	//array list that holds torpedos
	public static ArrayList<PhotonTorpedo> torpedoes = new ArrayList<PhotonTorpedo>();
	
	String thrustFile = "file:./src/thrust.au";
	String laserFile = "file:./src/laser.aiff";
	
	public static void main(String[] args){
		new GameBoard();
	}
	
	public GameBoard(){
		this.setSize(boardWidth, boardHeight);
		this.setTitle("Java Asteroids");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.addKeyListener(new KeyListener(){

			public void keyPressed(KeyEvent e) {
				
				if (e.getKeyCode() == 87){
					System.out.println("Forward");
					
					playSoundEffect(thrustFile);
					
					keyHeldCode = e.getKeyCode();
					keyHeld = true;
				}
				else if (e.getKeyCode() == 83){
					System.out.println("Backward");
					
					keyHeldCode = e.getKeyCode();
					keyHeld = true;
				}
				else if (e.getKeyCode() == 68){
					System.out.println("Rotate Right");
					
					keyHeldCode = e.getKeyCode();
					keyHeld = true;
				}
				else if (e.getKeyCode() == 65){
					System.out.println("Rotate Left");
					
					keyHeldCode = e.getKeyCode();
					keyHeld = true;
				}
				
				//Check for enter key pressed to fire torpedo
				else if (e.getKeyCode() == KeyEvent.VK_ENTER){
					
					playSoundEffect(laserFile);
					
					torpedoes.add(new PhotonTorpedo(GameDrawingPanel.theShip.getShipNoseX(),
							GameDrawingPanel.theShip.getShipNoseY(), 
							GameDrawingPanel.theShip.getRotationAngle()));
					
				}
				
			}

			
			public void keyReleased(KeyEvent e) {
			//keycodes: w is 87, a is 65, s is 83, d is 68
				keyHeld = false;			
			}

			
			public void keyTyped(KeyEvent e) {
				
				
			}
			
		});
		
		GameDrawingPanel gamePanel = new GameDrawingPanel();
		this.add(gamePanel, BorderLayout.CENTER);
		
		//execute redraws of the screen everytime something happens on the screen
		//use the thread pool executor to do this
		//5 is the pool size
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);
		
		executor.scheduleAtFixedRate(new RepaintTheBoard(this), 0L, 20L, TimeUnit.MILLISECONDS);
		
		this.setVisible(true);
		
	}
	
	class RepaintTheBoard implements Runnable{
		
		GameBoard theBoard;
		
		public RepaintTheBoard(GameBoard theBoard){
			this.theBoard = theBoard;
		}
		
		public void run(){
			theBoard.repaint();
		}
		
	}
	
	public static void playSoundEffect(String soundToPlay){
		
		URL soundLocation;
		
		try{
			soundLocation = new URL(soundToPlay);
			
			Clip clip = null;
		
			clip = AudioSystem.getClip();
			
			AudioInputStream inputStream;
			
			inputStream = AudioSystem.getAudioInputStream(soundLocation);
			
			clip.open(inputStream);
			clip.loop(0);
			clip.start();
		}
		catch (MalformedURLException e1){
			e1.printStackTrace();
		}
		catch (LineUnavailableException e2){
			e2.printStackTrace();
		}
		catch (UnsupportedAudioFileException e3){
			e3.printStackTrace();
		}
		catch (IOException e4){
			e4.printStackTrace();
		}
		
	}
	
}

@SuppressWarnings("serial")
class GameDrawingPanel extends JComponent {
	
	public static ArrayList<Rock> rocks = new ArrayList<Rock>();
	
	int[] polyXarray = Rock.sPolyXArray;
	int[] polyYArray = Rock.sPolyYArray;
	
	static SpaceShip theShip = new SpaceShip();
	
	int width = GameBoard.boardWidth;
	int height = GameBoard.boardHeight;
	
	public GameDrawingPanel(){
		for (int i = 0; i < 15; i++) {
			int randomStartXPos = (int) (Math.random() * (GameBoard.boardWidth - 40) + 1);
			int randomStartYPos = (int) (Math.random() * (GameBoard.boardHeight - 40) + 1);
			
			rocks.add(new Rock(Rock.getPolyXArray(randomStartXPos), Rock.getPolyYArray(randomStartYPos), 13, randomStartXPos, randomStartYPos));
			Rock.rocks = rocks;
		}
	}
	
	public void paint(Graphics g){
		
		Graphics2D graphicSetting = (Graphics2D)g;
		
		AffineTransform identity = new AffineTransform();
		
		graphicSetting.setColor(Color.BLACK);
		graphicSetting.fillRect(0, 0, getWidth(), getHeight());
		
		//set up rendering rules
		graphicSetting.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		graphicSetting.setPaint(Color.WHITE);
		
		//draw all the asteroids on the screen
		for (Rock rock : rocks){
			
			if (rock.onScreen){
				rock.move(theShip, GameBoard.torpedoes);
				
				graphicSetting.draw(rock);
			}
			
		}
		
		if(GameBoard.keyHeld == true && GameBoard.keyHeldCode == 68){
			theShip.increaseRotationAngle();
		}
		
		else if(GameBoard.keyHeld == true && GameBoard.keyHeldCode == 65){
			theShip.decreaseRotationAngle();
		}
		else if(GameBoard.keyHeld == true && GameBoard.keyHeldCode == 87){
			theShip.setMovingAngle(theShip.getRotationAngle());
			
			theShip.increaseXVelocity(theShip.shipXMoveAngle(theShip.getMovingAngle())*0.1);
			theShip.increaseYVelocity(theShip.shipYMoveAngle(theShip.getMovingAngle())*0.1);
		}
		else if(GameBoard.keyHeld == true && GameBoard.keyHeldCode == 83){
			theShip.setMovingAngle(theShip.getRotationAngle());
			
			theShip.decreaseXVelocity(theShip.shipXMoveAngle(theShip.getMovingAngle())*0.1);
			theShip.decreaseYVelocity(theShip.shipYMoveAngle(theShip.getMovingAngle())*0.1);
		}
		
		
		
		theShip.move();
		
		//move the ship to the center of th screen
		graphicSetting.translate(theShip.getXCenter(), theShip.getYCenter());
		
		//rotate the ship
		graphicSetting.rotate(Math.toRadians(theShip.getRotationAngle()));
		
		graphicSetting.draw(theShip);
		
		//draw the torpedoes
		for(PhotonTorpedo torpedo : GameBoard.torpedoes){
			
			torpedo.move();
			
			if(torpedo.onScreen){
				
				graphicSetting.setTransform(identity);
				
				graphicSetting.translate(torpedo.getXCenter(), torpedo.getYCenter());
				
				graphicSetting.draw(torpedo);
			}
			
		}
	}
}
