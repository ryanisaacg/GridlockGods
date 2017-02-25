
public class Car
{
    public static int CAR_LENGTH = 1;
    public RoadNode start, end;
    public int progress, waiting_time;
    private RoadNode endGoal;

    public Car(RoadNode start, RoadNode endGoal)
    {
	this.start = start;
	this.progress = 0;
	this.endGoal = endGoal;
    }
}
