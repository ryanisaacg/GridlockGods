import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Debug extends JPanel
{
    private static final long serialVersionUID = 1L;

    World world;

    private final int WIDTH = 640, HEIGHT = 480;

    public Debug()
    {
	world = new World();
	world.populateRoads(WIDTH / 8, HEIGHT / 8);
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
    }

    @Override
    public void paint(Graphics g)
    {
	super.paint(g);
	Graphics2D g2d = (Graphics2D) g;
	for (RoadNode node : world.intersections)
	{
	    if (node.connections[0] != null) // Right
	    {
		g2d.setColor(Color.GRAY);
		g2d.fillRect(node.x * 8 + 8, node.y * 8, 80, 8);
	    }
	    if (node.connections[3] != null) // Down
	    {
		g2d.setColor(Color.GRAY);
		g2d.fillRect(node.x * 8, node.y * 8 + 8, 8, 80);
	    }
	}
	for (RoadNode node : world.intersections)
	{
	    g2d.setColor(Color.RED);
	    g2d.fillRect(node.x * 8, node.y * 8, 8, 8);
	}
	for (Car car : world.cars)
	{
	    g2d.setColor(Color.BLUE);
	    if(car.path.size() <= 1)
		continue;
	    RoadNode.Connection target = car.target();
	    RoadNode start = car.path.get(0);
	    RoadNode next = car.path.get(1);
	    int x = ((int) ((next.x - start.x) * (car.progress / target.length) + start.x) * 8);
	    int y = ((int) ((next.y - start.y) * (car.progress / target.length) + start.x) * 8);
	    g2d.fillRect(x, y, 8, 8);
	}
    }
}
