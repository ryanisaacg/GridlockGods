import Jama.Matrix;

public class NeuralNetwork
{
	Matrix[] weights, activity, activation;
	
	/**
	 * Creates a new Neural Network with initially random weights
	 * @param layerSizes The size of each layer. layerSizes[0] = the number of inputs,
	 *  layerSizes[length-1] = number of outputs. layerSizes.length controls how many layers there are.
	 */
	public NeuralNetwork(int[] layerSizes)
	{
		weights = new Matrix[layerSizes.length-1];
		activity = new Matrix[layerSizes.length-1];
		activation = new Matrix[layerSizes.length-1];
		for(int i = 0; i < weights.length; i++)
		{
			double[][] weight = new double[layerSizes[i]][layerSizes[i+1]];
			weights[i] = new Matrix(normallyRandomArray(weight));
		}
	}
	
	public Matrix forward(Matrix input)
	{
		Matrix current = input.copy();
				
		for(int l = 0; l < weights.length; l++)
		{
			current = current.times(weights[l]);
			activity[l] = current.copy();
			current = sigmoid(current);
			activation[l] = current.copy();
		}
		
		return current;
	}
	
	public Matrix[] costPrime(Matrix input, Matrix output)
	{
		Matrix prediction = this.forward(input);
		
		Matrix errorFinal = output.minus(prediction).times(-1).times(activity[1]);
		Matrix djdW2 = activation[1].transpose().times(errorFinal);
		
		Matrix errorBefore = errorFinal.times(weights[1].transpose()).times(sigmoidPrime(activity[0]));
		Matrix djdW1 = input.transpose().times(errorBefore);
		
		return new Matrix[]{djdW2, djdW1};
	}
	
	private Matrix sigmoid(Matrix m)
	{
		Matrix r = m.copy();
		for(int i = 0; i < m.getRowDimension(); i++)
			for(int j = 0; j < m.getColumnDimension(); j++)
				r.set(i, j, 1 / (1 + Math.exp(-m.get(i, j))));
		return r;
	}
	
	private Matrix sigmoidPrime(Matrix m)
	{
		Matrix r = m.copy();
		for(int i = 0; i < m.getRowDimension(); i++)
			for(int j = 0; j < m.getColumnDimension(); j++)
				r.set(i, j, Math.exp(-m.get(i, j)) / Math.pow(1 + Math.exp(-m.get(i, j)), 2));
		return r;
	}
	
	private double[][] normallyRandomArray(double[][] array)
	{
		for(int i = 0; i < array.length; i++)
			for(int j = 0; j < array[i].length; j+=2)
			{
				double[] nums = normallyRandomNumbers();
				array[i][j] = nums[0];
				if(j+1 < array[i].length)
					array[i][j+1] = nums[1];
			}
		
		return array;
	}
	
	private double[] normallyRandomNumbers()
	{
		double x = Math.random(), y = Math.random();
		return new double[]{Math.sqrt(-2*Math.log(x))*Math.cos(2*Math.PI*y),
				Math.sqrt(-2*Math.log(x))*Math.sin(2*Math.PI*y)};
	}
}
