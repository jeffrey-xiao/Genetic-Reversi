import java.util.*;
import java.io.*;
public class Human implements Player {
	private BufferedReader br;
	private StringTokenizer st;
	private int color;
	Human (int color) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		this.color = color;
	}
	public int getColor () {
		return color;
	}
	public void setColor (int color) {
		this.color = color;
	}
	@Override
	public Move getMove (Board b) throws IOException {
		int r = -1;
		int c = -1;
		String piece = color == 2 ? "X" : "O";
		while (!b.isValidMove(r, c, color)) {
			System.out.printf("(%s) Enter a value square to move (r, c): \n", piece);
			r = readInt();
			c = readInt();
		}
		return new Move(r, c);
		
	}
	private int readInt () throws IOException {
		return Integer.parseInt(next());
	}
	private String next () throws IOException {
		while (st == null || !st.hasMoreTokens())
			st = new StringTokenizer(br.readLine().trim());
		return st.nextToken();
	}
}
