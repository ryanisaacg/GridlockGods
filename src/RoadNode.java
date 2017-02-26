import java.util.ArrayList;
import java.util.List;

public class RoadNode
{
	public class Connection
    {
    RoadNode endpoint;
	int length;
	float speed;
	List<Car> traveling;

	public Connection(RoadNode endpoint, int length, float speed)
	{
	    this.endpoint = endpoint;
	    this.length = length;
	    this.speed = speed;
	    traveling = new ArrayList<>();
	}
    }

    TrafficLight light;

    Connection[] connections;

    int x, y;

    public RoadNode(int x, int y)
    {
	connections = new Connection[4];
	this.x = x;
	this.y = y;
    }

    public void addConnection(int direction, RoadNode endpoint, int length, float speed)
    {
	connections[direction] = new Connection(endpoint, length, speed);
	endpoint.connections[(direction + 2) % 4] = new Connection(this, length, speed);
	if (light == null)
	{
	    int numConnect = 0;
	    for (int i = 0; i < 4; i++)
		if (connections[i] != null)
		    numConnect++;
	    if (numConnect > 2) 
	    	light = new TrafficLight(this);
	}
    }

    public void die()
    {
	for (int i = 0; i < 4; i++)
	{
	    if (connections[i] == null)
		continue;
	    int numConnect = 0;
	    for (int j = 0; j < 4; j++)
		if (connections[i].endpoint.connections[j] != null)
		    numConnect++;
	    if(numConnect <= 2)
		connections[i].endpoint.light = null;
	    connections[i].endpoint.connections[(i + 2) % 4] = null;
	}
    }

    public String toString()
    {
	return "Node: " + x + "," + y;
    }
}
