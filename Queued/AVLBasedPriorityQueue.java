
public class AVLBasedPriorityQueue<T extends Comparable<T>> {

	private static class Node<T extends Comparable<T>>
	{
		Node lesser, greater;
		T data;
		int balance;
		
		public Node(T myData)
		{
			data = myData;
		}
		
		public boolean isLeaf()
		{
			return lesser == null && greater == null;
		}
	}
	
	Node rootNode;
	
	public boolean offer(T data)
	{
		if(rootNode==null)
		{
			rootNode = new Node(data);
		}
		else
		{
			addValue(rootNode, null, true, data);
		}
		obtainBalanceFactors(rootNode);
		checkForBalancing(null, rootNode);
		return true;
	}
	
	private void addValue(Node node, Node parent, boolean greater, T value)
	{
		if(node == null)
		{
			Node newNode = new Node(value);
			if(greater)
			{
				parent.greater = newNode;
			}
			else
			{
				parent.lesser = newNode;
			}
		}
		else if(value.compareTo((T) node.data)<0)
		{
			addValue(node.lesser, node, false, value);
		}
		else if(value.compareTo((T) node.data)>0)
		{
			addValue(node.greater, node, true, value);
		}
		else
		{
			return;
		}
	}
	
	public void obtainBalanceFactors(Node node)
	{
		if(node.isLeaf())
		{
			node.balance = 0;
			return;
		}
		node.balance = gimmeHeight(node.lesser) - gimmeHeight(node.greater);
		if(node.lesser != null)
		{
			obtainBalanceFactors(node.lesser);
		}
		if(node.greater != null)
		{
			obtainBalanceFactors(node.greater);
		}
	}
	
	private int gimmeHeight(Node node)
	{
		if(node == null)
		{
			return 0;
		}
		if(node.isLeaf())
		{
			return 1;
		}
		int lesserHeight = (node.lesser != null)? gimmeHeight(node.lesser)+1:0;
		int greaterHeight = (node.greater != null)?gimmeHeight(node.greater)+1:0;
		int largestHeight = (lesserHeight > greaterHeight)?lesserHeight:greaterHeight;
		return largestHeight;	
	}
	
	private void checkForBalancing(Node parent, Node node)
	{	
		if(node == null)
		{
			return;
		}
		checkForBalancing(node, node.lesser);
		checkForBalancing(node, node.greater);
		if(node.balance >= 2)
		{
			balance(parent, node);
		}
		if(node.balance <= -2)
		{
			balance(parent, node);
		}
		
	}
	
	private void balance(Node parent, Node node)
	{
		if(node.balance > 0)
		{
			if(node.lesser.balance < 0)
			{
				//kink LR
				rotateLeft(node.lesser, node);
				rotateRight(node, parent);
			}
			else
			{
				//stick R
				rotateRight(node, parent);
			}
		}
		else
		{
			if(node.greater.balance > 0)
			{
				//kink RL
				rotateRight(node.greater, node);
				rotateLeft(node, parent);
			}
			else
			{
				//stick L
				rotateLeft(node, parent);
			}
		}
		obtainBalanceFactors(rootNode);
	}
	
	private void rotateRight(Node node, Node parent)
	{
		Node pivot = node.lesser;
		if(parent != null && parent.greater == node)
		{
			parent.greater = pivot;
		}
		else if(parent != null)
		{
			parent.lesser = pivot;
		}
		node.lesser = pivot.greater;
		pivot.greater = node;
		if(parent == null)
		{
			rootNode = pivot;
		}
	}
	
	private void rotateLeft(Node node, Node parent)
	{
		Node pivot = node.greater;
		if(parent != null && parent.greater == node)
		{
			parent.greater = pivot;	
		}
		else if(parent != null)
		{
			parent.lesser = pivot;
		}
		node.greater = pivot.lesser;
		pivot.lesser = node;
		if(parent == null)
		{
			rootNode = pivot;
		}
	}
	
	public void testPrint(Node n)
	{
		if(n == null)
		{
			System.out.println();
			return;
		}
		testPrint(n.lesser);
		System.out.print(n.data + ", ");
		testPrint(n.greater);
	}
	
	public T poll()
	{
		T data = null;
		if(rootNode != null)
		{
			Node n = rootNode.lesser;
			Node parent = rootNode;
			while(n.lesser != null)
			{
				parent = n;
				n = n.lesser;
			}
			parent.lesser = n.greater;
			obtainBalanceFactors(rootNode);
			checkForBalancing(null, rootNode);
			data = (T) n.data;
		}
		return data;
	}
	
	private void calculateNewRoot()
	{
		Node newRoot, parent = null;
		if(rootNode.balance > 0)
		{
			newRoot = rootNode.lesser;
			while(newRoot.greater != null)
			{
				parent = newRoot;
				newRoot = newRoot.greater;
			}
			if(parent != null)
			{
				parent.greater = newRoot.lesser;
				newRoot.lesser = parent;
			}
			newRoot.greater = rootNode.greater;
		}
		else
		{
			newRoot = rootNode.greater;
			while(newRoot.lesser != null)
			{
				parent = newRoot;
				newRoot = newRoot.lesser;
			}
			if(parent != null)
			{
				parent.lesser = newRoot.greater;
				newRoot.greater = parent;
			}
			newRoot.lesser = rootNode.lesser;
		}
		rootNode = newRoot;
	}
	
	public T peek()
	{
		Node result = rootNode;
		while(result.lesser != null)
		{
			result = result.lesser;
		}
		T data = (T) ((result != null)?result.data:null);
		
		return (T) data;
	}
}
