import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Draw extends JPanel
{
	private static final long serialVersionUID = 1L;

	World world;
	Camera cam;

	private final int WIDTH = 1440, HEIGHT = 960;
	private Camera camera, cameraVelocity, cameraTarget;
	private BufferedImage carFile, streetTilesFile, trafficLightFile, buildingFile, bCarFile, rCarFile;

	private BufferedImage building;
	private BufferedImage[][] street1 = new BufferedImage[4][4];
	private BufferedImage[][] carPic = new BufferedImage[2][2];
	private BufferedImage[][] bCarFilePic = new BufferedImage[2][2];
	private BufferedImage[][] rCarFilePic = new BufferedImage[2][2];
	private BufferedImage[] trafficLight = new BufferedImage[3];

	public Draw()
	{
		world = new World();
		camera = new Camera(0, 0);
		cameraVelocity = new Camera(0, 0);
		cameraTarget = new Camera(0, 0);
		// cam = new Camera(1440, 960);

		world.populateRoads(WIDTH / 10, HEIGHT / 10);
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

		carFile = loader.loadImage("/Car1.png");
		streetTilesFile = loader.loadImage("/StreetTiles.png");
		trafficLightFile = loader.loadImage("/TrafficLight.png");
		buildingFile = loader.loadImage("/Building.png");
		bCarFile = loader.loadImage("/BlueCar.png");
		rCarFile = loader.loadImage("/RedCar.png");

		SpriteSheet ss = new SpriteSheet(streetTilesFile);
		SpriteSheet ssBuilding = new SpriteSheet(buildingFile);
		SpriteSheet ssCar = new SpriteSheet(carFile);
		SpriteSheet ssLight = new SpriteSheet(trafficLightFile);
		SpriteSheet ssBlueCar = new SpriteSheet(bCarFile);
		SpriteSheet ssRedCar = new SpriteSheet(rCarFile);

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
		for (int i = 0; i < 2; i++)
		{
			for (int j = 0; j < 2; j++)
			{
				bCarFilePic[i][j] = ssBlueCar.grabImage(i + 1, j + 1, 32, 32);
			}
		}
		for (int i = 0; i < 2; i++)
		{
			for (int j = 0; j < 2; j++)
			{
				rCarFilePic[i][j] = ssRedCar.grabImage(i + 1, j + 1, 32, 32);
			}
		}

		for (int i = 0; i < trafficLight.length; i++)
		{
			trafficLight[i] = ssLight.grabImage(i + 1, 1, 32, 32);
		}

		this.setFocusable(true);
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e)
			{
				switch (e.getKeyCode())
				{
				case KeyEvent.VK_D:
					cameraVelocity.x = 8;
					break;
				case KeyEvent.VK_W:
					cameraVelocity.y = -8;
					break;
				case KeyEvent.VK_A:
					cameraVelocity.x = -8;
					break;
				case KeyEvent.VK_S:
					cameraVelocity.y = 8;
					break;
				}
			}

			public void keyReleased(KeyEvent e)
			{
				switch (e.getKeyCode())
				{
				case KeyEvent.VK_D:
					cameraVelocity.x = 0;
					break;
				case KeyEvent.VK_W:
					cameraVelocity.y = 0;
					break;
				case KeyEvent.VK_A:
					cameraVelocity.x = 0;
					break;
				case KeyEvent.VK_S:
					cameraVelocity.y = 0;
					break;
				case KeyEvent.VK_ESCAPE:
					System.exit(0);
					break;
				}
			}
		});
		this.requestFocus();
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		cameraTarget.x += cameraVelocity.x;
		cameraTarget.y += cameraVelocity.y;
		camera.x += (cameraTarget.x - camera.x) / 32;
		camera.y += (cameraTarget.y - camera.y) / 32;
		g2d.translate(-camera.x, -camera.y);

		for (int x = (int)((camera.x - 32)/32)*32; x < camera.x + WIDTH + 64; x += 32)
		{
			for (int y = (int)((camera.y - 32)/32)*32; y < camera.y + HEIGHT + 64; y += 32)
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
			// g2d.drawImage(trafficLight[0], node.x * 16 , node.y * 16 , null);
			// g2d.fillOval(node.x * 16 + 10, node.y * 16 + 10, 8, 8);
			// if (node.x * 8 > 640)
			// System.out.println(node.x * 8);
		}
		for (RoadNode node : world.intersections)
		{
			if (node.light != null)
			{
				if (node.light.switchCooldown > 0)
				{
					g2d.drawImage(trafficLight[2], node.x * 16, node.y * 16, null);
					//System.out.println("!!!!!!!!!!!!!!!!");
				}
				else if (node.light.vertical)

					g2d.drawImage(trafficLight[1], node.x * 16, node.y * 16, null);
				else
					g2d.drawImage(trafficLight[0], node.x * 16, node.y * 16, null);
			}
			// g2d.drawImage(trafficLight[0], node.light.x * 16, node.light.y *
			// 16, null);
		}
	for (RoadNode node : world.intersections)
	{
	    g2d.setColor(Color.RED);
	    
	    if (node.connections[0] == null && node.connections[1] == null && node.connections[2] == null && node.connections[3] == null)
			g2d.drawImage(street1[0][0], node.x * 16, node.y * 16, null);
		else if (node.connections[0] == null && node.connections[1] == null && node.connections[2] == null && node.connections[3] != null)
			g2d.drawImage(street1[1][0], node.x * 16, node.y * 16, null);
		else if (node.connections[0] == null && node.connections[1] != null && node.connections[2] == null && node.connections[3] == null)
			g2d.drawImage(street1[2][0], node.x * 16, node.y * 16, null);
		else if (node.connections[0] != null && node.connections[1] == null && node.connections[2] == null && node.connections[3] == null)
			g2d.drawImage(street1[0][1], node.x * 16, node.y * 16, null);
		else if (node.connections[0] != null && node.connections[1] == null && node.connections[2] == null && node.connections[3] != null)
			g2d.drawImage(street1[1][1], node.x * 16, node.y * 16, null);
		else if (node.connections[0] != null && node.connections[1] != null && node.connections[2] == null && node.connections[3] == null)
			g2d.drawImage(street1[2][1], node.x * 16, node.y * 16, null);
		else if (node.connections[0] != null && node.connections[1] != null && node.connections[2] == null && node.connections[3] != null)
			g2d.drawImage(street1[3][1], node.x * 16, node.y * 16, null);
		else if (node.connections[0] == null && node.connections[1] == null && node.connections[2] != null && node.connections[3] == null)
			g2d.drawImage(street1[0][2], node.x * 16, node.y * 16, null);
		else if (node.connections[0] == null && node.connections[1] == null && node.connections[2] != null && node.connections[3] != null)
			g2d.drawImage(street1[1][2], node.x * 16, node.y * 16, null);
		else if (node.connections[0] == null && node.connections[1] != null && node.connections[2] != null && node.connections[3] == null)
			g2d.drawImage(street1[2][2], node.x * 16, node.y * 16, null);
		else if (node.connections[0] == null && node.connections[1] != null && node.connections[2] != null && node.connections[3] != null)
			g2d.drawImage(street1[3][2], node.x * 16, node.y * 16, null);
		else if (node.connections[0] != null && node.connections[1] == null && node.connections[2] != null && node.connections[3] != null)
			g2d.drawImage(street1[1][3], node.x * 16, node.y * 16, null);
		else if (node.connections[0] != null && node.connections[1] != null && node.connections[2] != null && node.connections[3] == null)
			g2d.drawImage(street1[2][3], node.x * 16, node.y * 16, null);
		else //(node.connections[0] != null && node.connections[1] != null && node.connections[2] != null && node.connections[3] != null)
			g2d.drawImage(street1[3][3], node.x * 16, node.y * 16, null);
	   // g2d.drawImage(street1[3][3], node.x * 16, node.y * 16, null);
	    // g2d.drawImage(trafficLight[0], node.x * 16 , node.y * 16 , null);
	    // g2d.fillOval(node.x * 16 + 10, node.y * 16 + 10, 8, 8);
	    // if (node.x * 8 > 640)
	    // System.out.println(node.x * 8);
	}
	for (Car car : world.cars)
	{
		g2d.setColor(Color.BLUE);
		if (car.path.size() <= 1)
			continue;
		RoadNode start = car.path.get(0);
		RoadNode next = car.path.get(1);
		
		//int carColor = (int) (Math.random() * 3);
		System.out.println(car.carColor);
		if (car.carColor == 0)

			if (next.x - start.x > 0)
				g2d.drawImage(carPic[0][0], car.getTileX() * 16, car.getTileY() * 16, null);
			else if (next.x - start.x < 0)
				g2d.drawImage(carPic[1][1], car.getTileX() * 16, car.getTileY() * 16, null);
			else if (next.y - start.y > 0)
				g2d.drawImage(carPic[0][1], car.getTileX() * 16, car.getTileY() * 16, null);
			else if (next.y - start.y < 0)
				g2d.drawImage(carPic[1][0], car.getTileX() * 16, car.getTileY() * 16, null);
		if (car.carColor == 1)
			if (next.x - start.x > 0)
				g2d.drawImage(bCarFilePic[0][0], car.getTileX() * 16, car.getTileY() * 16, null);
			else if (next.x - start.x < 0)
				g2d.drawImage(bCarFilePic[1][1], car.getTileX() * 16, car.getTileY() * 16, null);
			else if (next.y - start.y > 0)
				g2d.drawImage(bCarFilePic[0][1], car.getTileX() * 16, car.getTileY() * 16, null);
			else if (next.y - start.y < 0)
				g2d.drawImage(bCarFilePic[1][0], car.getTileX() * 16, car.getTileY() * 16, null);
		if (car.carColor == 2)
			if (next.x - start.x > 0)
				g2d.drawImage(rCarFilePic[0][0], car.getTileX() * 16, car.getTileY() * 16, null);
			else if (next.x - start.x < 0)
				g2d.drawImage(rCarFilePic[1][1], car.getTileX() * 16, car.getTileY() * 16, null);
			else if (next.y - start.y > 0)
				g2d.drawImage(rCarFilePic[0][1], car.getTileX() * 16, car.getTileY() * 16, null);
			else if (next.y - start.y < 0)
				g2d.drawImage(rCarFilePic[1][0], car.getTileX() * 16, car.getTileY() * 16, null);
	}
	for (RoadNode node : world.intersections)
	{
	    if (node.light != null)
	    {
		if (node.light.switchCooldown > 0)
		    g2d.drawImage(trafficLight[2], node.x * 16, node.y * 16, null);
		else if (node.light.vertical)
		    g2d.drawImage(trafficLight[1], node.x * 16, node.y * 16, null);
		else
		    g2d.drawImage(trafficLight[0], node.x * 16, node.y * 16, null);
	    }
	    // g2d.drawImage(trafficLight[0], node.light.x * 16, node.light.y *
	    // 16, null);


			//System.out.println(car.getTileX() * 16 + "," + car.getTileY() * 16);
		}
		for (RoadNode node : world.intersections)
		{
			if (node.light != null)
			{
				if (node.light.switchCooldown > 0)
					g2d.drawImage(trafficLight[2], node.x * 16, node.y * 16, null);
				else if (node.light.vertical)
					g2d.drawImage(trafficLight[1], node.x * 16, node.y * 16, null);
				else
					g2d.drawImage(trafficLight[0], node.x * 16, node.y * 16, null);
			}
			// g2d.drawImage(trafficLight[0], node.light.x * 16, node.light.y *
			// 16, null);
		}

	}
}