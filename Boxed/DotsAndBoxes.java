import java.util.ArrayList;
import java.util.List;


public class DotsAndBoxes 
{
	Graph graph;
	DfsGraphTraversal dfs;
	int[] playerScores;
	int rowSize, columnSize;

	public DotsAndBoxes(int rows, int columns)
	{
		graph = new Graph((rows+1)*(columns+1));
		playerScores = new int[10];
		setupGraph(rows+1,columns+1);
		rowSize = rows+1;
		columnSize = columns+1;
		dfs = new DfsGraphTraversal();
	}

	private void setupGraph(int rows, int columns)
	{
		for(int i=1; i<columns; i++)
		{
			for(int j=1; j<rows; j++)
			{
				if(i+1 < columns)
				{
					graph.addEdge(i*rows+j, i*rows+j-1, 1);
				}
				if(j+1 < rows)
				{
					graph.addEdge(i*rows+j, (i-1)*rows+j, 1);

				}
			}
		}
		System.out.println();
	}

	public int score(int player)
	{
		return playerScores[player];
	}

	public boolean areMovesLeft()
	{
		boolean moveLeft = false;
		for(int i=0; i<graph.vcount() && !moveLeft; i++)
		{
			moveLeft = graph.degree(i) > 0;
		}
		return moveLeft;	
	}

	private int countScoredBoxes()
	{
		int count = 0;
		for(int i=1; i<columnSize-1; i++)
		{
			for(int j=1; j<rowSize-1; j++)
			{
				count += (graph.degree(i*rowSize+j)==0 && doubleCrossChecks(i*rowSize+j))?1:0;
			}
		}
		return count;
	}

	public int drawLine(int player, int x1, int y1, int x2, int y2)
	{
		int originalCount = countScoredBoxes();
		if(Math.abs(x1-x2)>0)
		{
			int index1 = Math.max(x1, x2)+y1*rowSize;
			int index2 = index1+rowSize;
			graph.removeEdge(index1, index2);
		}
		else
		{
			int index1 = Math.max(y1,y2)*rowSize+x1;
			int index2 = index1+1;
			graph.removeEdge(index1, index2);
		}
		int difference = countScoredBoxes() - originalCount;
		playerScores[player] += difference;
		return difference;
	}

	public int countDoubleCrosses()
	{
		int count = 0;
		List<List<Integer>> forest = dfs.traverse(graph);
		for(List<Integer> tree: forest)
		{
			if(tree.size() == 2)
			{
				if(doubleCrossChecks(tree.get(0)) && doubleCrossChecks(tree.get(1)))
				{
					count++;
				}
			}
		}
		return count;
	}

	private boolean doubleCrossChecks(int index)
	{
		return (index > rowSize && index < columnSize*rowSize-rowSize && (index%rowSize != 0) && ((index+1)%rowSize != 0));
	}

	public int countCycles()
	{
		int count = 0;
		List<List<Integer>> forest = dfs.traverse(graph);
		for(List<Integer> tree: forest)
		{
			boolean acyclic = true;
			for(int i=0; i<tree.size() && acyclic; i++)
			{
				acyclic = (graph.degree(tree.get(i)) == 2);
			}
			if(acyclic && graph.isEdge(tree.get(0), tree.get(tree.size()-1)))
			{
				count++;
			}
		}
		return count;
	}

	public int countOpenChains()
	{
		int count = 0;
		BfsGraphTraversal bfs = new BfsGraphTraversal();
		List<List<Integer>> forest = bfs.traverse(graph);
		for(List<Integer> tree: forest)
		{
			if(tree.size() >2)
			{
				count += countOpenChains(tree);
			}
		}
		return count;
	}
	
	private static class ChainIndex
	{
		int index;
		boolean hasFailed;
		public ChainIndex()
		{
			index = -1;
			hasFailed = false;
		}
	}
	
	private int countOpenChains(List<Integer> tree)
	{
		List<Integer> dfsTree = new ArrayList<Integer>();
		boolean dfsCreated = false;
		int chainCount = 0;
		for(int i=0; i<tree.size();i++)
		{
			if(graph.degree(tree.get(i)) == 2 && graph.getMark(tree.get(i)) == 0)
			{
				int indexA = graph.first(tree.get(i));
				int indexB = graph.next(tree.get(i), indexA);
				if((graph.degree(indexA) > 2 || (graph.degree(indexA) == 1 && !doubleCrossChecks(indexA))))
				{
					dfsTree = dfs.makeSpecificTree(graph, tree.get(i));
					dfsCreated = dfsTree.size() > 2;
				}
				else if((graph.degree(indexB) > 2 || (graph.degree(indexB) == 1 && !doubleCrossChecks(indexB))))
				{
					dfsTree = dfs.makeSpecificTree(graph, tree.get(i));
					dfsCreated = dfsTree.size() > 2;
				}
				if(dfsCreated)
				{
					int chainLength = 0;
					boolean failed = false;
					for(int j=0; j<dfsTree.size() && !failed; j++)
					{
						if(graph.degree(dfsTree.get(j)) == 2)
						{
							chainLength++;
							graph.setMark(dfsTree.get(j), 1);
						}
						else
						{
							if(chainLength > 2 && (graph.degree(dfsTree.get(j)) == 1 && !doubleCrossChecks(dfsTree.get(j)) || graph.degree(dfsTree.get(j)) > 2))
							{
								chainCount++;
							}
							failed = true;
						}
					}
					dfsCreated = false;
				}
			}
		}
		return chainCount;
	}

}
