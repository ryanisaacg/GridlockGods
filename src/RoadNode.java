import java.util.List;

public class RoadNode
{
    public class Connection
    {
	RoadNode endpoint;
	int length;
	float speed;
	List<Car> incoming;

	public Connection(RoadNode endpoint, int length, float speed)
	{
	    this.endpoint = endpoint;
	    this.length = length;
	    this.speed = speed;
	}
    }

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
    }
}
