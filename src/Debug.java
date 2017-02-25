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
    
    public Debug()
    {
    	world = new World();
    	world.populateRoads(WIDTH / 8, HEIGHT / 8);
    	this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    	
    	BufferedImageLoader loader = new BufferedImageLoader();
    	
    	car = loader.loadImage("/car.png");
    	streetTiles = loader.loadImage("/StreetTiles.png");
    	trafficLight = loader.loadImage("/TrafficLight.png");
    }
    
    @Override
    public void paint(Graphics g) 
    {
		Graphics2D g2d = (Graphics2D)g;
		for(RoadNode node : world.intersections) 
	{
	    if(node.connections[0] != null) 
	    {
			g2d.setColor(Color.GRAY);
			g2d.fillRect(node.x * 8 + 8, node.y * 8, 80, 8);
			//g2d.drawImage(car, node.x * 8 + 8, node.y * 8 , null);
	    }
	    if(node.connections[3] != null) 
	    {
			g2d.setColor(Color.GRAY);
			g2d.fillRect(node.x * 8, node.y * 8 + 8, 8, 80);
	    }
	}
		for(RoadNode node : world.intersections) 
		{
		    g2d.setColor(Color.RED);
		    g2d.fillRect(node.x * 8, node.y * 8, 8, 8);
		    if(node.x * 8 > 640)
			System.out.println(node.x * 8);
		}
    }
}
