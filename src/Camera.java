
public class Camera 
{
	
	private int offsetMaxX = 1440*2 - 640;
	private int offsetMaxY = 960*2 - 480;
	private int offsetMinX = 0;
	private int offsetMinY = 0;
	
	private int x, y;
	
	
	public Camera(int x, int y) 
	{
		this.x = x;
		this.y = y;
	}
	public int getX() 
	{
		return x;
	}
	public int getY() 
	{
		return y;
	}
	public void setX(int x) 
	{
		this.x = x;
	}
	public void setY(int y) 
	{
		this.y = y;
	}
}
