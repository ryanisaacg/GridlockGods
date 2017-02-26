import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World
{
    List<TrafficLight> trafficLights;
    List<RoadNode> intersections;
    List<Spawner> spawners;
    List<Car> cars;
    Random rand;

    public World()
    {
	trafficLights = new ArrayList<>();
	intersections = new ArrayList<>();
	spawners = new ArrayList<>();
	cars = new ArrayList<>();
	rand = new Random();
    }
    
    public boolean carFree(int x, int y)
    {
	return carFree(cars, x, y);
    }

    public boolean carFree(List<Car> cars, int x, int y)
    {
	for (Car car : cars)
	    if (car.getTileX() == x && car.getTileY() == y)
		return false;
	return true;
    }

    public void update()
    {
	for(Spawner spawn : spawners)
	    if(rand.nextFloat() <= spawn.spawnWeight && carFree(spawn.entrypoint.x, spawn.entrypoint.y))
		cars.add(new Car(spawn.entrypoint, intersections.get(rand.nextInt(intersections.size()))));
	for(RoadNode node : intersections)
	    if(node.light != null)
	    	node.light.update();
	for(int i = 0; i < cars.size(); i++)
	{
	    if(cars.get(i).update(this))
	    {
	    	cars.remove(i);
	    	i--;
	    }
	}
    }

    public void populateRoads(int width, int height)
    {
	RoadNode node = new RoadNode(0, 0);
	populate(node, width, height);
	for (int i = 0; i < 5 && intersections.size() > 1; i++)
	{
	    int index = rand.nextInt(intersections.size() - 1) + 1;
	    RoadNode removed = intersections.remove(index);
	    removed.die();
	}
	for(int i = 0; i < 15; i++) {
	    Spawner spawn = new Spawner();
	    spawn.entrypoint = intersections.get(rand.nextInt(intersections.size()));
	    spawn.spawnWeight = rand.nextFloat() / 25;
	    spawners.add(spawn);
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
	    current.addConnection(0, next, 10, 0.05f);
	    populate(next, width, height);
	} else
	{
	    current.addConnection(0, right, 10, 0.05f);
	}
	RoadNode down = containsPosition(x, y + 10);
	if (down == null)
	{
	    RoadNode next = new RoadNode(x, y + 10);
	    current.addConnection(3, next, 10, 0.05f);
	    populate(next, width, height);
	} else
	{
	    current.addConnection(3, down, 10, 0.05f);
	}
    }
}
