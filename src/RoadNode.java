public class RoadNode
{
    public class Connection
    {
	RoadNode endpoint;
	int length, speed;

	public Connection(RoadNode endpoint, int length, int speed)
	{
	    this.endpoint = endpoint;
	    this.length = length;
	    this.speed = speed;
	}
    }

    Connection[] connections;

    public RoadNode()
    {
	connections = new Connection[4];
    }

    public void addConnection(int direction, RoadNode endpoint, int length, int speed)
    {
	connections[direction] = new Connection(endpoint, length, speed);
    }
}
