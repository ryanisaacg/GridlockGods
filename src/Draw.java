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
    private BufferedImage[][] carPic = new BufferedImage[2][2];

    public Draw()
    {
	world = new World();
	world.populateRoads(WIDTH, HEIGHT);
	this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

	new Thread(() -> {
	    while (true)
	    {
		world.update();
		repaint();
		try
		{
		    Thread.sleep(16);
		} catch (InterruptedException e)
		{
		    e.printStackTrace();
		}
	    }
	}).start();

	BufferedImageLoader loader = new BufferedImageLoader();

	carFile = loader.loadImage("/car.png");
	streetTilesFile = loader.loadImage("/StreetTiles.png");
	trafficLightFile = loader.loadImage("/TrafficLight.png");
	buildingFile = loader.loadImage("/Building.png");

	SpriteSheet ss = new SpriteSheet(streetTilesFile);
	SpriteSheet ssBuilding = new SpriteSheet(buildingFile);
	SpriteSheet ssCar = new SpriteSheet(carFile);

	for (int i = 0; i < 4; i++)
	{
	    for (int j = 0; j < 4; j++)
	    {
		street1[i][j] = ss.grabImage(i + 1, j + 1, 32, 32);
	    }
	}
	building = ssBuilding.grabImage(1, 1, 32, 32);
	for (int i = 0; i < 2; i++)
	{
	    for (int j = 0; j < 2; j++)
	    {
		carPic[i][j] = ssCar.grabImage(i + 1, j + 1, 32, 32);
	    }
	}
    }

    @Override
    public void paint(Graphics g)
    {
	super.paint(g);
	Graphics2D g2d = (Graphics2D) g;

	for (int x = 0; x < WIDTH * 3; x += 32)
	{
	    for (int y = 0; y < HEIGHT * 2; y += 32)
	    {
		g2d.drawImage(building, x, y, null);
	    }
	}
	for (RoadNode node : world.intersections)
	{
	    if (node.connections[0] != null) // Right
	    {
		g2d.setColor(Color.GRAY);
		// g2d.fillRect(node.x * 16 + 8, node.y * 16, 320, 32);

		for (int i = 4; i <= 16; i += 4)
		{
		    g2d.drawImage(street1[0][3], node.x * 16 + 8 * i, node.y * 16, null);
		}
	    }
	    if (node.connections[3] != null) // Down
	    {
		g2d.setColor(Color.GRAY);
		// g2d.fillRect(node.x * 16, node.y * 16 + 8, 32, 320);
		for (int i = 4; i <= 16; i += 4)
		{
		    g2d.drawImage(street1[3][0], node.x * 16, node.y * 16 + 8 * i, null);
		}
	    }
	}
	for (RoadNode node : world.intersections)
	{
	    g2d.setColor(Color.RED);
	    g2d.drawImage(street1[3][3], node.x * 16, node.y * 16, null);
	    g2d.fillOval(node.x * 16 + 10, node.y * 16 + 10, 8, 8);
	    if (node.x * 8 > 640)
		System.out.println(node.x * 8);
	}
	for (Car car : world.cars)
	{
	    g2d.setColor(Color.BLUE);
	    if (car.path.size() <= 1)
		continue;
	    RoadNode start = car.path.get(0);
	    RoadNode next = car.path.get(1);

	    if (next.x - start.x > 0)
		g2d.drawImage(carPic[0][0], car.getTileX() * 16, car.getTileY() * 16, null);
	    if (next.x - start.x < 0)
		g2d.drawImage(carPic[1][1], car.getTileX() * 16, car.getTileY() * 16, null);
	    if (next.y - start.y > 0)
		g2d.drawImage(carPic[0][1], car.getTileX() * 16, car.getTileY() * 16, null);
	    if (next.y - start.y < 0)
		g2d.drawImage(carPic[1][0], car.getTileX() * 16, car.getTileY() * 16, null);
	}
    }
}
