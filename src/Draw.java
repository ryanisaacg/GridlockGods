import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Draw extends JPanel {
	private static final long serialVersionUID = 1L;

	World world;
	Camera cam;

	private final int WIDTH = 640, HEIGHT = 480;
	private Camera camera;
	private BufferedImage carFile, streetTilesFile, trafficLightFile, buildingFile;

	private BufferedImage building;
	private BufferedImage[][] street1 = new BufferedImage[4][4];
	private BufferedImage[][] carPic = new BufferedImage[2][2];
	private BufferedImage[] trafficLight = new BufferedImage[2];

	public Draw() {
		world = new World();
		camera = new Camera(0, 0);
		//cam = new Camera(1440, 960);
		
		world.populateRoads(WIDTH/ 10, HEIGHT /10);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

		new Thread(() -> {
			while (true) {
				world.update();
				repaint();
				try {
					Thread.sleep(16);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		BufferedImageLoader loader = new BufferedImageLoader();

		carFile = loader.loadImage("/Car1.png");
		streetTilesFile = loader.loadImage("/StreetTiles.png");
		trafficLightFile = loader.loadImage("/TrafficLight.png");
		buildingFile = loader.loadImage("/Building.png");

		SpriteSheet ss = new SpriteSheet(streetTilesFile);
		SpriteSheet ssBuilding = new SpriteSheet(buildingFile);
		SpriteSheet ssCar = new SpriteSheet(carFile);
		SpriteSheet ssLight = new SpriteSheet(trafficLightFile);

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				street1[i][j] = ss.grabImage(i + 1, j + 1, 32, 32);
			}
		}
		building = ssBuilding.grabImage(1, 1, 32, 32);
		
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				carPic[i][j] = ssCar.grabImage(i + 1, j + 1, 32, 32);
			}
		}
		
		for (int i = 0; i < 2; i++) 
		{
			trafficLight[i] = ssLight.grabImage(i + 1, 1, 32, 32);
		}
		
		this.setFocusable(true);
		this.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e)
			{
				switch(e.getKeyCode()){
				case KeyEvent.VK_D:
					camera.x += 4;
					break;
				case KeyEvent.VK_W:
					camera.y -= 4;
					break;
				case KeyEvent.VK_A:
					camera.x -= 4;
					break;
				case KeyEvent.VK_S:
					camera.y += 4;
					break;
				}
			}
		});
		this.requestFocus();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(-camera.x, -camera.y);

		for (int x = 0; x < WIDTH * 3; x += 32) {
			for (int y = 0; y < HEIGHT * 2; y += 32) {
				g2d.drawImage(building, x, y, null);
			}
		}
		for (RoadNode node : world.intersections) {
			if (node.connections[0] != null) // Right
			{
				g2d.setColor(Color.GRAY);
				// g2d.fillRect(node.x * 16 + 8, node.y * 16, 320, 32);

				for (int i = 4; i <= 16; i += 4) {
					g2d.drawImage(street1[0][3], node.x * 16 + 8 * i, node.y * 16, null);
				}
			}
			if (node.connections[3] != null) // Down
			{
				g2d.setColor(Color.GRAY);
				// g2d.fillRect(node.x * 16, node.y * 16 + 8, 32, 320);
				for (int i = 4; i <= 16; i += 4) {
					g2d.drawImage(street1[3][0], node.x * 16, node.y * 16 + 8 * i, null);
				}
			}
		}
		for (RoadNode node : world.intersections) {
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
			
			//g2d.drawImage(street1[3][3], node.x * 16, node.y * 16, null);
			//g2d.drawImage(trafficLight[0], node.x * 16 , node.y * 16 , null);
			//g2d.fillOval(node.x * 16 + 10, node.y * 16 + 10, 8, 8);
			//if (node.x * 8 > 640)
				//System.out.println(node.x * 8);
		}
		for (Car car : world.cars) {
			g2d.setColor(Color.BLUE);
			if (car.path.size() <= 1)
				continue;
			RoadNode start = car.path.get(0);
			RoadNode next = car.path.get(1);

			if (next.x - start.x > 0)
				g2d.drawImage(carPic[0][0], car.getTileX() * 16, car.getTileY() * 16, null);
			else if (next.x - start.x < 0)
				g2d.drawImage(carPic[1][1], car.getTileX() * 16, car.getTileY() * 16, null);
			else if (next.y - start.y > 0)
				g2d.drawImage(carPic[0][1], car.getTileX() * 16, car.getTileY() * 16, null);
			else if (next.y - start.y < 0)
				g2d.drawImage(carPic[1][0], car.getTileX() * 16, car.getTileY() * 16, null);
			
			System.out.println(car.getTileX() * 16 + "," + car.getTileY() * 16);
		}
		for (RoadNode node : world.intersections) 
		{
			if (node.light != null) 
			{
				if(node.light.vertical)
					g2d.drawImage(trafficLight[1], node.x * 16, node.y * 16, null);
				if (!node.light.vertical)
					g2d.drawImage(trafficLight[0], node.x * 16, node.y * 16, null);
			}
			//g2d.drawImage(trafficLight[0], node.light.x * 16, node.light.y * 16, null);
		}
	}
}