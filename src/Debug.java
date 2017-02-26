import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class Debug extends JPanel
{
    private static final long serialVersionUID = 1L;

    World world;
    
    private final int WIDTH = 640, HEIGHT = 480;
    private BufferedImage car, streetTiles, trafficLight;
    
    private BufferedImage street, streetLR, streetUD;
    private BufferedImage[][] street1 = new BufferedImage[4][4];
    
    public Debug()
    {
    	world = new World();
    	world.populateRoads(WIDTH, HEIGHT);
    	this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    	
    	BufferedImageLoader loader = new BufferedImageLoader();
    	
    	car = loader.loadImage("/car.png");
    	streetTiles = loader.loadImage("/StreetTiles.png");
    	trafficLight = loader.loadImage("/TrafficLight.png");
    	
    	SpriteSheet ss = new SpriteSheet(streetTiles);
    	
    	for (int i = 0; i < 4; i++) 
    	{
    		for (int j = 0; j < 4; j++) 
    		{
    			//street1[i][j] = ss.grabImage(i, j, 32, 32);
    		}
    	}
    	
    	
    	
    	street = ss.grabImage(4, 4, 32, 32);
    	streetLR = ss.grabImage(1, 4, 32, 32);
    	streetUD = ss.grabImage(4, 1, 32, 32);
    	
    }
    
    @Override
    public void paint(Graphics g) 
    {
		Graphics2D g2d = (Graphics2D)g;
		for(RoadNode node : world.intersections) 
		{
		    if(node.connections[0] != null) // Right
		    {
				g2d.setColor(Color.GRAY);
				//g2d.fillRect(node.x * 16 + 8, node.y * 16, 320, 32);
				
				for (int i = 4; i <= 16; i += 4) 
				{
					g2d.drawImage(streetLR, node.x * 16 + 8*i, node.y * 16, null);	
				}
		    }
		    if(node.connections[3] != null) // Down
		    {
				g2d.setColor(Color.GRAY);
				//g2d.fillRect(node.x * 16, node.y * 16 + 8, 32, 320);
				for (int i = 4; i <= 16; i += 4) 
				{
					g2d.drawImage(streetUD, node.x * 16, node.y * 16 + 8*i, null);	
				}
		    }
		}
		for(RoadNode node : world.intersections) 
		{
		    g2d.setColor(Color.RED);
		    g2d.fillRect(node.x * 16, node.y * 16, 32, 32);
		    g2d.drawImage(street, node.x * 16, node.y * 16, null);
		    if(node.x * 8 > 640)
		    	System.out.println(node.x * 8);
		}
    }
}
