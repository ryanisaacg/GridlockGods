import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Car
{
    public static int CAR_LENGTH = 1;
    public List<RoadNode> path;
    public float progress, waitingTime;

    public Car(RoadNode start, RoadNode endGoal)
    {
	path = new ArrayList<>();
	aStar(start, endGoal);
	this.progress = 0;
    }

    public int getTileX()
    {
	if (path.size() == 0)
	    return 0;
	else if (path.size() == 1)
	    return path.get(0).x;
	else
	{
	    RoadNode start = path.get(0);
	    RoadNode next = path.get(1);
	    return (int) ((next.x - start.x) * ((progress - 1) / target().length) + start.x);
	}
    }

    public int getTileY()
    {
	if (path.size() == 0)
	    return 0;
	else if (path.size() == 1)
	    return path.get(0).y;
	else
	{
	    RoadNode start = path.get(0);
	    RoadNode next = path.get(1);
	    return (int) ((next.y - start.y) * ((progress - 1) / target().length) + start.y);
	}
    }

    public RoadNode.Connection target()
    {
	RoadNode.Connection connect = null;
	for (int i = 0; i < 4; i++)
	    if (path.get(0).connections[i] != null && path.get(0).connections[i].endpoint == path.get(1))
		connect = path.get(0).connections[i];
	return connect;
    }

    public boolean update()
    {
	if (path.size() <= 1)
	{
	    return true;
	}
	RoadNode.Connection connect = target();
	if (progress >= connect.length)
	{
	    boolean horizontal = path.get(0).x == path.get(1).x;
	    if (connect.endpoint.light == null || horizontal != connect.endpoint.light.vertical)
	    {
		connect.traveling.remove(this);
		path.remove(0);
		progress = 0;
		waitingTime = 0;
	    } else
	    {
		waitingTime++;
	    }
	} else
	{
	    progress += connect.speed;
	    if (!connect.traveling.contains(this))
		connect.traveling.contains(this);
	}
	return false;
    }

    private int cost(RoadNode check, RoadNode end, Integer g)
    {
	return g + (int) Math.sqrt(Math.pow(check.x - end.x, 2) + Math.pow(check.y - end.y, 2));
    }

    private void aStar(RoadNode start, RoadNode endGoal)
    {
	HashMap<RoadNode, Integer> open = new HashMap<>();
	HashMap<RoadNode, Integer> closed = new HashMap<>();
	open.put(start, 0);
	while (!open.containsKey(endGoal))
	{
	    if (open.keySet().size() == 0)
	    {
		path = new ArrayList<>();
		path.add(start);
		return;
	    }
	    RoadNode first = open.keySet().iterator().next();
	    Integer min = cost(first, endGoal, open.get(first));
	    for (RoadNode node : open.keySet())
	    {
		if (open.get(node) < min)
		    min = cost(first, endGoal, open.get(node));
	    }
	    RoadNode next = first;
	    for (RoadNode node : open.keySet())
	    {
		if (cost(node, endGoal, open.get(node)) <= min)
		{
		    next = node;
		    break;
		}
	    }
	    Integer nextCost = open.get(next);
	    open.remove(next);
	    closed.put(next, nextCost);
	    for (int i = 0; i < 4; i++)
		if (next.connections[i] != null && !closed.containsKey(next.connections[i].endpoint))
		    open.put(next.connections[i].endpoint, nextCost + next.connections[i].length);
	}
	this.path = new LinkedList<>();
	path.add(endGoal);
	while (path.get(0) != start)
	{
	    RoadNode lowest = null;
	    for (int i = 0; i < 4; i++)
	    {
		if (path.get(0).connections[i] != null && closed.containsKey(path.get(0).connections[i].endpoint))
		{
		    if (lowest == null || closed.get(lowest) > closed.get(path.get(0).connections[i].endpoint))
			lowest = path.get(0).connections[i].endpoint;
		}
	    }
	    path.add(0, lowest);
	    closed.remove(lowest);
	}
    }
}