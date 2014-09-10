

import java.util.Iterator;
import java.util.LinkedList;


public class Hashtable<K, V> {
	private Entry<K,V>[] values;
	private int size;
	
	public Hashtable(int initialCapacity) {
		values = (Entry<K,V>[])new Entry[initialCapacity];
	}
	
	/**
	 * #3b. Implement this (1 point)
	 * 
	 * @param key
	 * @param value
	 */
	public void put(K key, V value) {
		int hash = key.hashCode();
		int index = hash%values.length;
		Entry<K,V> nextVal = values[index];
		boolean added = false;
		if(values[index] == null)
		{
			values[index] = new Entry(key, value);
			added = true;
		}
		while(!added && nextVal.next != null)
		{
			if(nextVal.key.equals(key))
			{
				nextVal.data = value;
				added = true;
			}
			nextVal = nextVal.next;
		}
		if(!added)
		{
			nextVal.next = new Entry(key, value);
		}
		
	}
	
	/**
	 * #3b. Implement this (1 point)
	 * @param key
	 * @return
	 */
	public V get(K key) {
		V result = null;
		int hash = key.hashCode();
		int index = hash%values.length;
		Entry<K,V> nextVal = values[index];
		while(nextVal != null && result == null)
		{
			result = (nextVal.key.equals(key))?nextVal.data:result;
			nextVal = nextVal.next;
		}
		return result;
	}

	/**
	 * #3c.  Implement this. (1 point)
	 * 
	 * @param key
	 * @return
	 */
	public V remove(K key) {
		int hash = key.hashCode()%values.length;
		Entry<K,V> previous = null;
		Entry<K,V> current = values[hash];
		V result = null;
		if(current != null)
		{
			while(current.next != null)
			{
				previous = current;
				current = current.next;
			}
			result = current.data;
			if(previous != null)
			{
				previous.next = null;
			}
			else
			{
				values[hash] = null;
			}
		}
		return result;
	}
	
	public int size() {
		return size;
	}
	
	public boolean containsKey(K key) {
		return this.get(key) != null; 
	}

	public Iterator<V> values() {
		return new Iterator<V>() {
			private int count = 0;
			private Entry<K, V> currentEntry;
			
			{
				while ( ( currentEntry = values[count] ) == null && count < values.length ) {
					count++;
				}
			}

			@Override
			public boolean hasNext() {
				return count < values.length;
			}

			@Override
			public V next() {
				V toReturn = currentEntry.data;
				currentEntry = currentEntry.next;
				while ( currentEntry == null && ++count < values.length && (currentEntry = values[count]) == null );
				return toReturn;
			}

			@Override
			public void remove() {
			}
			
		};
	}
	
	private static class Entry<K, V> {
		public K key;
		public V data;
		public Entry<K,V> next;
		
		public Entry(K key, V data) {
			this.key = key;
			this.data = data;
		}
		
		public String toString() {
			return "{" + key + "=" + data + "}";
		}
	}
}