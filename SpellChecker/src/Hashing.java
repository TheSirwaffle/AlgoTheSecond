
public class Hashing {
	private static int hash(String str)
	{
		int value = 0;
		for(int i=0; i<str.length();i++)
		{
			value += (str.charAt(i)*i);
		}
		return value;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Soundex s = new Soundex("testString");
		System.out.println(s.hashCode());
	}

}
