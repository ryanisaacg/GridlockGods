import Jama.Matrix;

public class TrafficLight
{
    RoadNode node;
    NeuralNetwork network;
    boolean vertical;
    Matrix previous, current;
    float previous_anger, current_anger;
    
    public TrafficLight(RoadNode node)
    {
	this.node = node;
	this.vertical = true;
	network = new NeuralNetwork(new int[] { 5, 30, 2 });
	previous = new Matrix(1, 5);
	current = new Matrix(1, 5);
	previous_anger = totalAnger();
	fillMatrix(previous);
	if(network.shouldToggle(previous))
	    vertical = !vertical;
    }
    
    private float totalAnger() 
    {
	float anger = 0;
	for(int i = 0; i < 4; i++)
	{
	    if(node.connections[i] != null)
	    {
		for(Car car : node.connections[i].traveling)
		    anger += car.waitingTime;
	    }
	}
	return anger;
    }
    
    private void fillMatrix(Matrix m)
    {
	for(int i = 0; i < 4; i++)
	{
	    if(node.connections[i] == null)
	    {
		m.set(0, i, 0);
	    } else
	    {
		float anger = 0;
		for(Car car : node.connections[i].traveling)
		    anger += car.waitingTime;
		m.set(0, i, anger);
	    }
	}
	m.set(0, 4, vertical ? 1 : 0);
    }
    
    public void update()
    {
	fillMatrix(current);
	current_anger = totalAnger();
	network.newExperience(previous, current_anger, current);
	network.learn();
	if(network.shouldToggle(current))
	    vertical = !vertical;
    }
}
