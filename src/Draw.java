import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class Draw extends JPanel
{
    private static final long serialVersionUID = 1L;

    World world;
    
    private final int WIDTH = 640, HEIGHT = 480;
    private BufferedImage carFile, streetTilesFile, trafficLightFile, buildingFile;
    
    private BufferedImage building;
    private BufferedImage[][] street1 = new BufferedImage[4][4];
    
    public Draw()
    {
    	world = new World();
    	world.populateRoads(WIDTH, HEIGHT);
    	this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    	
    	BufferedImageLoader loader = new BufferedImageLoader();
    	
    	carFile = loader.loadImage("/car.png");
    	streetTilesFile = loader.loadImage("/StreetTiles.png");
    	trafficLightFile = loader.loadImage("/TrafficLight.png");
    	buildingFile = loader.loadImage("/Building.png");
    	
    	SpriteSheet ss = new SpriteSheet(streetTilesFile);
    	SpriteSheet ssBuilding = new SpriteSheet(buildingFile);
    	
    	for (int i = 0; i < 4; i++) 
    	{
    		for (int j = 0; j < 4; j++) 
    		{
    			street1[i][j] = ss.grabImage(i+1, j+1, 32, 32);
    		}
    	}
    	building = ssBuilding.grabImage(1, 1, 32, 32);
    }
    
    @Override
    public void paint(Graphics g) 
    {
		Graphics2D g2d = (Graphics2D)g;
		
		for (int x = 0; x < WIDTH * 3; x += 32) 
		{
			for (int y = 0; y < HEIGHT * 2; y += 32) 
			{
				g2d.drawImage(building, x, y, null);
			}
		}
		for(RoadNode node : world.intersections) 
		{
		    if(node.connections[0] != null) // Right
		    {
				g2d.setColor(Color.GRAY);
				//g2d.fillRect(node.x * 16 + 8, node.y * 16, 320, 32);
				
				for (int i = 4; i <= 16; i += 4) 
				{
					g2d.drawImage(street1[0][3], node.x * 16 + 8*i, node.y * 16, null);	
				}
		    }
		    if(node.connections[3] != null) // Down
		    {
				g2d.setColor(Color.GRAY);
				//g2d.fillRect(node.x * 16, node.y * 16 + 8, 32, 320);
				for (int i = 4; i <= 16; i += 4) 
				{
					g2d.drawImage(street1[3][0], node.x * 16, node.y * 16 + 8*i, null);	
				}
		    }
		}
		for(RoadNode node : world.intersections) 
		{
		    g2d.setColor(Color.RED);
		    g2d.fillRect(node.x * 16, node.y * 16, 32, 32);
		    g2d.drawImage(street1[3][3], node.x * 16, node.y * 16, null);
		    if(node.x * 8 > 640)
		    	System.out.println(node.x * 8);
		}
    }
}
