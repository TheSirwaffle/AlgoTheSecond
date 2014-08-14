import java.util.PriorityQueue;

import edu.neumont.io.Bits;


public class HuffmanTree{
	
	public Node rootNode;
	private PriorityQueue<Node> nodes;
	
	private static class Node implements Comparable<Node>
	{
		Node less, greater;
		byte data;
		float frequency;
		
		public Node(byte leData, float frequency)
		{
			data = leData;
			this.frequency = frequency;
		}

		@Override
		public int compareTo(Node n) {
			int test = (int)(100000000*this.frequency - 100000000*n.frequency);
			return test;
		}
	}
	
	public HuffmanTree(byte[] bytes)
	{ 
		byte[] frequency = new byte[Byte.MAX_VALUE - Byte.MIN_VALUE + 1];
		nodes = new PriorityQueue<Node>();
		for(byte b: bytes)
		{
			frequency[b-Byte.MIN_VALUE]++;
		}
		for(int i=0; i<frequency.length; i++)
		{
			if(frequency[i] != 0)
			{
				Node n = new Node((byte)(i+Byte.MIN_VALUE), (float)frequency[i]/bytes.length);
				nodes.add(n);
			}
		}
		ConstructTree();
		PrintNodes();
	}
	
	public void RecalculateTreeWithFreq(int[] frequencies)
	{
		int totalValues = 54679;
		nodes.removeAll(nodes);
		for(int i=0; i<frequencies.length; i++)
		{
			if(frequencies[i] != 0)
			{
				Node n = new Node((byte)(i+Byte.MIN_VALUE), (float)frequencies[i]/totalValues);
				nodes.add(n);
			}
		}
		ConstructTree();
		PrintNodes();
	}
	
	private void ConstructTree()
	{
		if(nodes.size() < 2)
		{
			rootNode = nodes.peek();
		}
		else
		{
			Node lesser = nodes.poll();
			Node greater = nodes.poll();
			Node n = new Node((byte)-1, lesser.frequency+greater.frequency);
			n.less = lesser;
			n.greater = greater;
			nodes.add(n);
			ConstructTree();
		}
	}
	
	private void PrintNodes()
	{
		System.out.println("Huffman Tree");
		System.out.println("Total Frequency: "+rootNode.frequency);
		PrintHelper(rootNode, true);
	}
	
	private void PrintHelper(Node n, boolean root)
	{
		Node less = n.less;
		Node greater = n.greater;
		
		if(less != null)
		{
			if(root)
			{
				System.out.println();
				System.out.println("Left Side Leaves: ");
				System.out.println();
			}
			PrintHelper(less, false);
		}
		if(greater != null)
		{
			if(root)
			{
				System.out.println();
				System.out.println("Right Side Leaves: ");
				System.out.println();
			}
			PrintHelper(greater, false);
		}
		if(!root && less==null && greater == null)
		{
			System.out.println("Frequency: " + n.frequency + ", Value: " + n.data);
		}
	}
	
	public byte toByte(Bits bits)
	{
		return toByteHelper(rootNode, bits);
	}
	
	private byte toByteHelper(Node n, Bits bits)
	{
		if(bits.size()==0 || (n.less == null && n.greater == null))
		{
			return n.data;
		}
		if(!bits.poll())
		{
			return toByteHelper(n.less, bits);
		}
		else
		{
			return toByteHelper(n.greater, bits);
		}
	}
	
	public void fromByte(byte b, Bits bits)
	{
		bits.addAll(fromByteHelper(rootNode, b, new Bits()));
	}
	
	private Bits fromByteHelper(Node n, byte b, Bits currentPath)
	{
		if(n.data == b && n.less == null && n.greater == null)
		{
			return currentPath;
		}
		else if(n.less == null && n.greater == null)
		{
			return null;
		}
		Bits newCurrentPath = new Bits();
		Bits newCurrentPath2 = new Bits();
		newCurrentPath.addAll(currentPath);
		newCurrentPath2.addAll(currentPath);
		newCurrentPath.add(new Boolean(false));
		newCurrentPath2.add(new Boolean(true));
		currentPath = fromByteHelper(n.greater, b, newCurrentPath2);
		if(currentPath == null)
		{
			currentPath = fromByteHelper(n.less, b, newCurrentPath);
		}
		return currentPath;
	}	
}
