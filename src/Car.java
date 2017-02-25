
public class Car
{
    public RoadNode start, end;
    public float progress;
    private RoadNode endGoal;
    
    public Car(RoadNode start, RoadNode endGoal) 
    {
	this.start = start;
	this.progress = 0;
	this.endGoal = endGoal;
    }
}
