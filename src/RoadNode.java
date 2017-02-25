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
	endpoint.connections[(direction + 2) % 4] = new Connection(this, length, speed); 
    }
    
    public void die() 
    {
	for(int i = 0; i < 4; i++) 
	{
	    if(connections[i] == null) continue;
	    connections[i].endpoint.connections[(i + 2) % 4] = null;
	}
    }
}
