import java.awt.Polygon;
import java.awt.Rectangle;

@SuppressWarnings("serial")
public class PhotonTorpedo extends Polygon{
	
	//used to make torpedo disappear when goes off the screen
	int gBWidth = GameBoard.boardWidth;
	int gBHeight = GameBoard.boardHeight;
	
	private double centerX = 0;
	private double centerY = 0;
	
	public static int[] polyXArray = {-3, 3, 3, -3, -3};
	public static int[] polyYArray = {-3, -3, 3, 3, -3};
	
	private int torpedoWidth = 6;
	private int torpedoHeight = 6;
	
	public boolean onScreen = false;
	
	private double movingAngle = 0;
	
	private double xVelocity = 5;
	private double yVelocity = 5;
	
	public PhotonTorpedo(double centerX, double centerY, double movingAngle){
		
		super(polyXArray, polyYArray, 5);
		
		this.centerX = centerX;
		this.centerY = centerY;
		this.movingAngle = movingAngle;
		
		this.onScreen = true;
		
		this.setXVelocity(this.torpedoXMoveAngle(movingAngle) * 10);
		this.setYVelocity(this.torpedoYMoveAngle(movingAngle) * 10);
		
	}
	
	public double getXCenter() {return centerX;}
	public double getYCenter() {return centerY;}
	public void setXCenter(double xCent) {this.centerX = xCent;}
	public void setYCenter(double yCent) {this.centerY = yCent;}
	
	//change x and y position
	
	public void changeXPos(double incAmt) {this.centerX += incAmt;}
	public void changeYPos(double incAmt) {this.centerY += incAmt;}
	
	public double getXVelocity() {return xVelocity;}
	public double getYVelocity() {return yVelocity;}
	public void setXVelocity(double xVel) {this.xVelocity = xVel;}
	public void setYVelocity(double yVel) {this.yVelocity = yVel;}
	
	//getwidth and height of torpedo
	public int getWidth() {return torpedoWidth;}
	public int getHeight() {return torpedoHeight;}
	
	//change torpedo angle
	public double getMovingAngle() {return movingAngle;}
	public void setMovingAngle(double moveAngle){this.movingAngle = moveAngle;}
	
	//for collision detection
	public Rectangle getBounds(){
		
		return new Rectangle((int) getXCenter() - 6, (int) getYCenter() - 6, getWidth(), getHeight());
		
	}
	
	public double torpedoXMoveAngle(double xMoveAngle){
		
		return (double)(Math.cos(xMoveAngle * Math.PI / 180));
		
	}
	
	public double torpedoYMoveAngle(double yMoveAngle){
		
		return (double)(Math.sin(yMoveAngle * Math.PI / 180));
		
	}
	
	public void move() {
		
		if(this.onScreen){
			
			this.changeXPos(this.getXVelocity());
			
			if(this.getXCenter() < 0) {
				this.onScreen = false;
			}
			else if(this.getXCenter() > gBWidth){
				this.onScreen = false;
			}
			
			this.changeYPos(this.getYVelocity());
			
			if(this.getYCenter() < 0) {
				this.onScreen = false;
			}
			else if(this.getYCenter() > gBHeight){
				this.onScreen = false;
			}
		}
		
	}
}