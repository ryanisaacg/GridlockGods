import java.util.ArrayList;
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
	RoadNode node = new RoadNode(rand.nextInt(5), rand.nextInt(5));
	populate(node, width, height);
    }

    private void populate(RoadNode current, int width, int height)
    {
	intersections.add(current);
	for (int i = 0; i < 4; i++)
	{
	    if (rand.nextBoolean())
	    {
		int x = current.x;
		int y = current.y;
		int roadLength = rand.nextInt(100) + 25;
		if (i % 2 == 0)
		    x += roadLength * (i - 1);
		else
		    y += roadLength * (i - 2);
		if(x >= width || y >= height)
		    continue; //Don't generate nodes outside of valid space
		current.addConnection(i, new RoadNode(x, y), roadLength, (float)Math.min(rand.nextFloat() + 0.5, 1));
	    }
	}
    }
}
