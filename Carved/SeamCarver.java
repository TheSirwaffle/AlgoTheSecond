import edu.neumont.ui.Picture;


public class SeamCarver {
		private Picture pic;
	
		private static class Node
		{
			public double myEnergy;
			public double totalEnergy = Double.MAX_VALUE;
			public double parentEnergy;
			public int parent = -1;
		}
	
		public SeamCarver(Picture pic)
		{
			this.pic = pic;
		}
		
		public Picture getPicture()
		{
			return pic;
		}
		
		public int width()
		{
			return pic.width();
		}
		
		public int height()
		{
			return pic.height();
		}
		
		public double energy(int x, int y)
		{
			try{
				int prevX = (x-1 != -1)?x-1:pic.width()-1;
				int postX = (x+1 != pic.width())?x+1:0;
				int r1 = pic.get(prevX, y).getRed() - pic.get(postX, y).getRed();
				int g1 = pic.get(prevX, y).getGreen() - pic.get(postX, y).getGreen();
				int b1 = pic.get(prevX, y).getBlue() - pic.get(postX, y).getBlue();
				
				int prevY = (y-1 != -1)?y-1:pic.height()-1;
				int postY = (y+1 != pic.height())?y+1:0;
				int r2 = pic.get(x, prevY).getRed() - pic.get(x, postY).getRed();
				int g2 = pic.get(x, prevY).getGreen() - pic.get(x, postY).getGreen();
				int b2 = pic.get(x, prevY).getBlue() - pic.get(x, postY).getBlue();
				
				return (r1*r1+g1*g1+b1*b1)+(r2*r2+g2*g2+b2*b2);
			}catch(IndexOutOfBoundsException e)
			{
				System.out.println("Color index was out of bounds. (Energy)");
				return 0;
			}
		}
		
		public int[] findHorizontalSeam()
		{
			Node[] nodes = new Node[pic.width()*pic.height()];
			for(int i=0; i<nodes.length; i++)
			{
				nodes[i] = new Node();
			}
			int[] indices = new int[pic.width()];
			double leastTotalEnergy = Double.MAX_VALUE;
			int index = 0;
			for(int i=0; i<pic.width(); i++)
			{
				for(int j=0; j<pic.height(); j++)
				{
					int nodeIndex = j*pic.width()+i;
					int y = nodeIndex/pic.width();
					int x = nodeIndex-(pic.width()*y);
					nodes[nodeIndex].myEnergy = energy(x,y);
					if(i==0)
					{
						nodes[nodeIndex].totalEnergy = 0;
					}
					if((i+1)<pic.width())
					{
						if(j!=0)
						{
							checkEnergy(nodes, nodeIndex-pic.width()+1, nodeIndex);
						
						}
						checkEnergy(nodes, nodeIndex+1, nodeIndex);
						if(nodeIndex<(pic.width()*pic.height()-pic.width()))
						{
							checkEnergy(nodes, nodeIndex+pic.width()+1, nodeIndex);
						}
					}
					else
					{
						if(nodes[nodeIndex].totalEnergy+nodes[nodeIndex].myEnergy < leastTotalEnergy)
						{
							leastTotalEnergy = nodes[nodeIndex].totalEnergy+nodes[nodeIndex].myEnergy;
							index = nodeIndex;
						}
					}
				}
			}
			int currIndex = pic.width()-1;
			while(nodes[index].parent != -1)
			{
					indices[currIndex--] = index;
					index = nodes[index].parent;
			}
			indices[currIndex--] = index;
			return indices;
		}
		
		private void checkEnergy(Node[] nodes, int i1, int i2)
		{
			if(nodes[i1].totalEnergy > nodes[i2].totalEnergy+nodes[i2].myEnergy)
			{
				nodes[i1].totalEnergy = nodes[i2].totalEnergy+nodes[i2].myEnergy;
				nodes[i1].parent = i2;
			}
		}
		
		public int[] findVerticalSeam()
		{
			Node[] nodes = new Node[pic.width()*pic.height()];
			for(int i=0; i<nodes.length; i++)
			{
				nodes[i] = new Node();
			}
			int[] indices = new int[pic.height()];
			double leastEnergyPath = Double.MAX_VALUE;
			int index = 0;
			for(int i=0; i<pic.height()*pic.width(); i++)
			{
				int y = i/pic.width();
				int x = i-(pic.width()*y);
					nodes[i].myEnergy = energy(x,y);
					if(i<pic.width())
					{
						nodes[i].totalEnergy = 0;
					}
					if(i < pic.height()*pic.width()-pic.width())
					{
						if(i%pic.width()!=0)
						{
							checkEnergy(nodes, i+pic.width()-1, i);
						
						}
						checkEnergy(nodes, i+pic.width(), i);
						if((i+1)%pic.width() != 0)
						{
							checkEnergy(nodes, i+pic.width()+1, i);
						}
					}
					else
					{
						if(nodes[i].totalEnergy+nodes[i].myEnergy < leastEnergyPath)
						{
							leastEnergyPath = nodes[i].totalEnergy+nodes[i].myEnergy;
							index = i;
						}
					}
			}
			int currIndex = pic.height()-1;
			while(nodes[index].parent != -1)
			{
					indices[currIndex--] = index;
					index = nodes[index].parent;
			}
			indices[currIndex--] = index;
			return indices;
		}
		
		public void removeHorizontalSeam(int[] indices)
		{
			if(indices.length != pic.width() || pic.width() == 1)
			{
				throw new IndexOutOfBoundsException();
			}
			
			Picture newPic = new Picture(pic.width(), pic.height()-1);
			int lastX = -1;
			int lastY = -1;
			for(int i=0; i<indices.length; i++)
			{
				int y = indices[i]/pic.width();
				int x = indices[i]%pic.width();
				if(lastX == -1 || lastY == -1 || ((x-lastX)==1 && Math.abs(y-lastY)<2))
				{
					lastY = y;
					lastX = x;
				
					for(int j=0; j<indices[i]/pic.width();j++)
					{
						if(x < 0 || y < 0 || x > pic.width() || y > pic.height())
						{
							throw new IndexOutOfBoundsException();
						}
						pic.set(x, indices[i]/pic.width()-j, pic.get(i, indices[i]/pic.width()-j-1));
					}
				}
				else
				{
					throw new IndexOutOfBoundsException();
				}
			}
			
			for(int i=0; i<pic.width(); i++)
			{
				for(int j=1; j<pic.height(); j++)
				{
					newPic.set(i, j-1, pic.get(i, j));
				}
			}
			pic = newPic;
		}
		
		public void saveFile(String title)
		{
			pic.save(title);
		}
		
		public void removeVerticalSeam(int[] indices)
		{
			if(indices.length != pic.height() || pic.height() == 1)
			{
				throw new IndexOutOfBoundsException();
			}
			
			Picture newPic = new Picture(pic.width()-1, pic.height());
			int lastX = -1;
			int lastY = -1;
			for(int i=0; i<indices.length; i++)
			{
				int y = indices[i]/pic.width();
				int x = indices[i]%pic.width();
				if(lastX == -1 || lastY == -1 || ((y-lastY)==1 && Math.abs(x-lastX)<2))
				{
					lastY = y;
					lastX = x;
					for(int j=0; j<(indices[i]-pic.width()*i);j++)
					{
						if(x <= 0 || y < 0|| x > pic.width() || y > pic.height())
						{
							throw new IndexOutOfBoundsException();
						}
						pic.set(indices[i]-pic.width()*i-j, i, pic.get(indices[i]-pic.width()*i-j-1, i));
					}
				}
			}
			for(int i=1; i<pic.width(); i++)
			{
				for(int j=0; j<pic.height(); j++)
				{
					newPic.set(i-1, j, pic.get(i, j));
				}
			}
			pic = newPic;
		}
}
