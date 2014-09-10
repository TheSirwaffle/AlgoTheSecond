
public class EditDistanceCalculator {
	public int editDistance(String a, String b) {
		return editDistanceHelper(a, b, 1, 1, new int[a.length()+1][b.length()+1]);
	}
	
	private int editDistanceHelper(String a, String b, int[][] eds) {
		for ( int i = 1; i <= a.length(); i++ ) {
			eds[i][0] = b.length();
		}
		for ( int j = 1; j <= b.length(); j++ ) {
			eds[0][j] = a.length();
		}
		
		for ( int i = 1; i <= a.length(); i++ ) {
			for ( int j = 1;  j <= b.length(); j++ ) {
				int delete = eds[i-1][j] + 1;
				int insert = eds[i][j-1] + 1;
				int remove = eds[i-1][j-1] + (a.charAt(i-1) == b.charAt(j-1) ? 0 : 1);
				eds[i][j] = Math.min(delete, Math.min(insert, remove));
			}
		}
		
		return eds[a.length()][b.length()];
	}
	
	/**
	 * #2.  Add a recursive version here, which uses a memory function.  Change your internal implementation
	 * to use this version. (1 point)
	 */
	
	private int editDistanceHelper(String a, String b, int i, int j, int[][] eds)
	{
		getPreliminaryValues(a,b,i,j,eds);
		return editDistanceRecurse(a,b,i,j,eds);
	}
	
	private int editDistanceRecurse(String a, String b, int i, int j, int[][] eds)
	{
		if(i > a.length())
		{
			return eds[a.length()][b.length()];
		}
		if(j<=b.length())
		{
			int delete = eds[i-1][j] + 1;
			int insert = eds[i][j-1] + 1;
			int remove = eds[i-1][j-1] + (a.charAt(i-1) == b.charAt(j-1) ? 0 : 1);
			eds[i][j] = Math.min(delete, Math.min(insert, remove));
			return editDistanceHelper(a,b,i,j+1,eds);
		}
		else
		{
			return editDistanceHelper(a,b,i+1,1,eds);
		}
	}
	
	private void getPreliminaryValues(String a, String b, int i, int j, int[][] eds)
	{
		if(i<=a.length())
		{
			eds[i][0] = a.length();
		}
		if(j<=b.length())
		{
			eds[0][j] = b.length();
		}
		if(i<=a.length() || j<=b.length())
		{
			getPreliminaryValues(a,b,i+1,j+1,eds);
		}
	}
}
