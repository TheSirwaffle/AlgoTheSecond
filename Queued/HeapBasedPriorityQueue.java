
public class HeapBasedPriorityQueue<T extends Comparable<T>> {
	T[] heap;
	int size;
	
	public HeapBasedPriorityQueue(int initialSize)
	{
		int startSize = 2;
		size = 1;
		while(startSize < initialSize)
		{
			startSize *= 2;
		}
		heap = (T[])(new Comparable[startSize]);
	}
	
	public boolean offer(T data)
	{
		boolean result = false;
		if(size < heap.length)
		{
			heap[size] = data;
			heapify(size);
			size++;
			result = true;
		}
		return result;
	}
	
	public void heapify(int index)
	{
		if(index != 1 && heap[index/2].compareTo(heap[index]) > 0)
		{
			T temp = heap[index/2];
			heap[index/2] = heap[index];
			heap[index] = temp;
			heapify(index/2);
		}
		if(index*2 < size)
		{
			if((heap[index].compareTo(heap[index*2]) > 0) || (index*2+1 < size && heap[index].compareTo(heap[index*2+1]) > 0 ))
			{
				int largest = (index*2+1 >= size)?index*2:-1;
				largest = (largest == -1 && heap[index*2].compareTo(heap[index*2+1])<0)?index*2:index*2+1;
				T temp = heap[largest];
				heap[largest] = heap[index];
				heap[index] = temp;
				heapify(largest);
			}
		}
	}
	
	public T poll()
	{
		T data = null;
		if(size > 0)
		{
			T temp = heap[1];
			heap[1] = heap[size-1];
			data = temp;
			size--;
			heapify(1);
		}
		return data;
	}
	
	public T peek()
	{
		T data = null;
		data = heap[size];
		return data;
	}
	
	public void printValues()
	{
		System.out.println();
		for(int i=1; i<size;i++)
		{
			System.out.println(heap[i]);
		}
	}
}
