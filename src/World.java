import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class World
{
    List<RoadNode> intersections;
    List<Spawner> spawners;
    Random rand;

    public World()
    {
	intersections = new ArrayList<>();
	spawners = new ArrayList<>();
	rand = new Random();
    }

    public void populateRoads(int width, int height)
    {
	System.out.println(width);
	RoadNode node = new RoadNode(0, 0);
	populate(node, width, height);
	for (int i = 0; i < 5 && intersections.size() > 1; i++)
	{
	    int index = rand.nextInt(intersections.size());
	    RoadNode removed = intersections.remove(index);
	    removed.die();
	}
    }

    private RoadNode containsPosition(int x, int y)
    {
	for (RoadNode node : intersections)
	{
	    if (x == node.x && y == node.y)
		return node;
	}
	return null;
    }

    private void populate(RoadNode current, int width, int height)
    {
	intersections.add(current);
	int x = current.x;
	int y = current.y;
	if (x >= width || y >= height)
	    return;
	RoadNode right = containsPosition(x + 10, y);
	if (right == null)
	{
	    RoadNode next = new RoadNode(x + 10, y);
	    current.addConnection(0, next, 10, (float) Math.min(rand.nextFloat() + 0.5, 1));
	    populate(next, width, height);
	}
	else 
	{
	    current.addConnection(0, right, 10, (float) Math.min(rand.nextFloat() + 0.5, 1));
	}
	RoadNode down = containsPosition(x, y + 10);
	if (down == null)
	{
	    RoadNode next = new RoadNode(x, y + 10);
	    current.addConnection(3, next, 10, (float) Math.min(rand.nextFloat() + 0.5, 1));
	    populate(next, width, height);
	}
	else 
	{
	    current.addConnection(3, down, 10, (float) Math.min(rand.nextFloat() + 0.5, 1));
	}
    }
}
