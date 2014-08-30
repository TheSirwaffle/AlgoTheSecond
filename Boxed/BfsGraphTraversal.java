import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class BfsGraphTraversal 
{
	
	private static class EdgeWeight implements Comparable<EdgeWeight>
	{
		int index;
		int weight;
		
		public EdgeWeight(int index, int weight)
		{
			this.index = index;
			this.weight = weight;
		}

		@Override
		public int compareTo(EdgeWeight ew) {
			return this.weight - ew.weight;
		}	
	}

	public List<List<Integer>> traverse(Graph g)
	{
		List<List<Integer>> forest = new ArrayList<List<Integer>>();
		int startIndex = -1;
		for(int i=0; i<g.vcount(); i++)
		{
			if(g.getMark(i) == 0)
			{
				List<Integer> list = obtainTreeHelper(g, i);
				forest.add(list);
			}
		}
		g.resetMarks();
		return forest;
	}
	
	private List<Integer> obtainTreeHelper(Graph g, int index)
	{	
		List<Integer> path = new ArrayList<Integer>();
		PriorityQueue<EdgeWeight> queue = new PriorityQueue<EdgeWeight>();
		queue.add(new EdgeWeight(index, 0));
		while(queue.size() > 0)
		{
			EdgeWeight value = queue.poll();
			if(path.size() == 0)
			{
				path.add(value.index);
				g.setMark(value.index, 1);
			}
			PriorityQueue<EdgeWeight> children = new PriorityQueue<EdgeWeight>();
			for(int next = g.first(value.index); next<g.vcount(); next = g.next(value.index, next))
			{
				children.add(new EdgeWeight(next, g.getEdgeWeight(value.index, next)));
			}
			while(children.size() > 0)
			{
				EdgeWeight child = children.poll();
				if(g.getMark(child.index) == 0)
				{
					queue.add(child);
					path.add(child.index);
					g.setMark(child.index, 1);
				}
			}
		}
		return path;
	}

}
