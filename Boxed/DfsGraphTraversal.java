import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;


public class DfsGraphTraversal {
	
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
				List<Integer> list = obtainTree(g, i);
				forest.add(list);
			}
		}
		g.resetMarks();
		return forest;
	}
	
	private List<Integer> obtainTree(Graph g, int startIndex)
	{
		List<Integer> list = new ArrayList<Integer>();
		obtainTreeHelper(g, startIndex, list);
		return list;
	}
	
	private void obtainTreeHelper(Graph g, int index, List<Integer> path)
	{
		if(path.size() == 0)
		{
			path.add(index);
			g.setMark(index, 1);
		}
		PriorityQueue<EdgeWeight> children = new PriorityQueue<EdgeWeight>();
		int nextIndex = g.next(index, -1);
		while(nextIndex != g.vcount())
		{
			children.add(new EdgeWeight(nextIndex, g.getEdgeWeight(index, nextIndex)));
			nextIndex = g.next(index, nextIndex);
		}
		while(children.size() > 0)
		{
			EdgeWeight ew = children.poll();
			if(g.getMark(ew.index) == 0)
			{
				path.add(ew.index);
				g.setMark(ew.index, 1);
				obtainTreeHelper(g, ew.index, path);
			}
		}
	}
	
	public List<Integer> makeSpecificTree(Graph g, int start)
	{
		int edgeOne = g.first(start);
		if(g.degree(edgeOne)!= 2)
		{
			g.removeEdge(start, edgeOne);
		}
		else
		{
			edgeOne = g.next(start, edgeOne);
			g.removeEdge(start, edgeOne);
		}
		List<Integer> list = new ArrayList<Integer>();
		obtainTreeHelper(g, start, list);
		g.addEdge(start, edgeOne, 1);
		for(int i=0; i<list.size(); i++)
		{
			g.setMark(list.get(i), 0);
		}
		return list;
	}
}
