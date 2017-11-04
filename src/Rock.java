import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;

class Rock extends Polygon{
	
	int uLeftXPos;
    int uLeftYPos;
	int xDirection;
	int yDirection;
	
	//width and height of the rectangle that wil surround the rock for collision detection
	int rockWidth = 26;
	int rockHeight = 31;
	
	int width = GameBoard.boardWidth;
	int height = GameBoard.boardHeight;
	
	int[] polyXArray;
	int[] polyYArray;
	
	public static int[] sPolyXArray = {10,17,26,34,27,36,26,14,8,1,5,1,10};
	public static int[] sPolyYArray = {0,5,1,8,13,20,31,28,31,22,16,7,0};
	
	public boolean onScreen = true;
	
	String explodeFile = "file:./src/explode.wav";
	
	static ArrayList<Rock> rocks = new ArrayList<Rock>();
	
	
	public Rock(int[] polyXArray, int[] polyYArray, int pointsInPoly, int randomStartXPos, int randomStartYPos){
		super(polyXArray, polyYArray, pointsInPoly);
		
		this.xDirection = (int) (Math.random() * 4 + 1);
		this.yDirection = (int) (Math.random() * 4 + 1);
		
		this.uLeftXPos = randomStartXPos;
		this.uLeftYPos = randomStartYPos;
	}
	
	//returns a rectangle that will be used for collison detection around the rock objects
	public Rectangle getBounds(){
		
		return new Rectangle(super.xpoints[0], super.ypoints[0], rockWidth, rockHeight);
	}
	
	public void move(SpaceShip theShip, ArrayList<PhotonTorpedo> torpedoes){
		
		Rectangle rockToCheck = this.getBounds();
		
		for (Rock rock : rocks){
			
			if (rock.onScreen){
			
				Rectangle otherRock = rock.getBounds();
			
				if(rock != this && otherRock.intersects(rockToCheck)){
				
					int tempXDirection = this.xDirection;
					int tempYDirection = this.yDirection;
					
					this.xDirection = rock.xDirection;
					this.yDirection = rock.yDirection;
				
					rock.xDirection = tempXDirection;
					rock.yDirection = tempYDirection;
				
				}
				
			Rectangle shipBox = theShip.getBounds();
			
			if (otherRock.intersects(shipBox)){
				
				GameBoard.playSoundEffect(explodeFile);
				
				theShip.setXCenter(theShip.gBWidth / 2);
				theShip.setYCenter(theShip.gBHeight / 2);
				
				theShip.setXVelocity(0);
				theShip.setYVelocity(0);
			}
			
			for (PhotonTorpedo torpedo: torpedoes){
				
				if(torpedo.onScreen){
					
					if(otherRock.contains(torpedo.getXCenter(), torpedo.getYCenter())){
						
						rock.onScreen = false;
						torpedo.onScreen = false;
						GameBoard.playSoundEffect(explodeFile);
						
					}
					
				}
				
			}
		}
	}
		
		int uLeftXPos = super.xpoints[0];
		int uLeftYPos = super.ypoints[0];
		
		//cehck to see if one of the rocks is gonna hit the wall
		if (uLeftXPos < 0 || (uLeftXPos + 25) > width){
			xDirection = -xDirection;
		}
		
		if (uLeftYPos < 0 || (uLeftYPos + 50) > height){
			yDirection = -yDirection;
		}
		
		//change all the points for polygons
		for(int i = 0; i < super.xpoints.length;  i++){
			super.xpoints[i] += xDirection;
			super.ypoints[i] += yDirection;
		}
	}
	
	public static int[] getPolyXArray(int randomStartXPos){
		int[] tempPolyXArray = (int[])sPolyXArray.clone();
		
		for(int i = 0; i < tempPolyXArray.length; i++){
			tempPolyXArray[i] += randomStartXPos;
		}
		
		return tempPolyXArray;
	}
	
	public static int[] getPolyYArray(int randomStartYPos){
		int[] tempPolyYArray = (int[]) sPolyYArray.clone();
		
		for(int i = 0; i < tempPolyYArray.length; i++){
			tempPolyYArray[i] += randomStartYPos;
		}
		
		return tempPolyYArray;
	}
	
}