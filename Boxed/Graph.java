
public class Graph {

	private int[][] matrix;
	private int[] marks;
	
	public Graph(int vertCount)
	{
		matrix = new int[vertCount][vertCount];
		marks = new int[vertCount];
	}
	
	public int vcount()
	{
		return matrix.length;
	}
	
	public int ecount()
	{
		int count = 0;
		for(int i=0; i<vcount(); i++)
		{
			for(int j=i;j<vcount(); j++)
			{
				count += (matrix[i][j] != 0)?1:0;
			}
		}
		return count;
	}
	
	public int first(int index)
	{
		return next(index, -1);
	}
	
	public int next(int index, int lastVisited)
	{
		int result = matrix[index].length;
		for(int j = lastVisited+1; j<matrix[index].length && result == matrix[index].length; j++)
		{
			if(matrix[index][j] != 0)
			{
				result = j;
			}
		}
		return result;
	}
	
	public void addEdge(int indexA, int indexB, int weight)
	{
		matrix[indexA][indexB] = weight;
		matrix[indexB][indexA] = weight;
	}
	
	public void removeEdge(int indexA, int indexB)
	{
		matrix[indexA][indexB] = 0;
		matrix[indexB][indexA] = 0;
	}
	
	public boolean isEdge(int indexA, int indexB)
	{
		int lastVisited = -1;
		while(lastVisited != matrix[indexA].length && lastVisited != indexB)
		{
			lastVisited = next(indexA, lastVisited);
		}
		return (lastVisited == indexB);
	}
	
	public int degree(int index)
	{
		int count = 0;
		for(int i=0; i<vcount(); i++)
		{
			count += (matrix[index][i] != 0)?1:0;
		}
		return count;
	}
	
	public void resetMarks()
	{
		for(int i=0; i<marks.length; i++)
		{
			marks[i] = 0;
		}
	}
	
	public int getEdgeWeight(int indexA, int indexB)
	{
		return matrix[indexA][indexB];
	}
	
	public int getMark(int index)
	{
		return marks[index];
	}
	
	public void setMark(int index, int mark)
	{
		marks[index] = mark;
	}
	
}
