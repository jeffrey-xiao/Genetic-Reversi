import java.io.IOException;
import java.util.ArrayList;


public class Board {
	int width, height;
	int[][] grid;
	Board (int width, int height) {
		this.width = width;
		this.height = height;
		grid = new int[height][width];
	}
	Board () {
		width = 8;
		height = 8;
		grid = new int[8][8];
	}
	@Override
	public String toString () {
		String res = "   ";
		for (int i = 0; i < width; i++)
			res += i%10 + " ";
		res += "\n";
		res += "   ";
		for (int i = 0; i < width; i++)
			res += "__";
		res += "\n";
		for (int i = 0; i < height; i++) {
			res += i%10 + " |";
			for (int j = 0; j < width; j++) {
				if (grid[i][j] == 0)
					res += "  ";
				else if (grid[i][j] == 1)
					res += "O ";
				else
					res += "X ";
			}
			res += "\n";
		}
		return res;
	}
	public void setInit () {
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				grid[i][j] = 0;
		grid[height/2][width/2]=1;
		grid[height/2-1][width/2]=2;
		grid[height/2][width/2-1]=2;
		grid[height/2-1][width/2-1]=1;
	}
	public Board getCopy () {
		Board res = new Board(width, height);
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				res.grid[i][j] = grid[i][j];
		return res;
	}
	public boolean isValidMove (int r, int c, int color) {
		if (r < 0 || r >= height || c < 0 || c >= width || grid[r][c] != 0)
			return false;
		int opp = color % 2 + 1;
		boolean valid = false;
		for (int mr = -1; mr <= 1; mr++) {
			for (int mc = -1; mc <= 1; mc++) {
				int cr = r + mr;
				int cc = c + mc;
				boolean hasOpp = false;
				while (0 <= cc && cc < width && 0 <= cr && cr < height) {
					if (grid[cr][cc] == opp)
						hasOpp = true;
					else if (grid[cr][cc] == color) {
						valid |= hasOpp;
						break;
					} else if (grid[cr][cc] == 0) {
						break;
					}
					cr += mr;
					cc += mc;
				}
			}
		}
		return valid;
	}
	public void movePiece (int r, int c, int color) {
		int opp = color % 2 + 1;
		for (int mr = -1; mr <= 1; mr++) {
			for (int mc = -1; mc <= 1; mc++) {
				int cr = r + mr;
				int cc = c + mc;
				boolean hasOpp = false;
				boolean valid = false;
				while (0 <= cc && cc < width && 0 <= cr && cr < height) {
					if (grid[cr][cc] == opp)
						hasOpp = true;
					else if (grid[cr][cc] == color) {
						valid = hasOpp;
						break;
					} else if (grid[cr][cc] == 0) {
						break;
					}
					cr += mr;
					cc += mc;
				}
				if (valid) {
					cr = r + mr;
					cc = c + mc;
					while (0 <= cc && cc < width && 0 <= cr && cr < height) {
						if (grid[cr][cc] == color)
							break;
						grid[cr][cc] = color;
						cr += mr;
						cc += mc;
					}
				}
			}
		}
		grid[r][c] = color;
	}
	public boolean isGameOver (int color) {
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				if (isValidMove(i, j, color))
					return false;
		return true;
	}
	public int countPieces () {
		int res = 0;
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {
				if (grid[i][j] == 1)
					res++;
				else if (grid[i][j] == 2)
					res--;
			}
		return res;
	}
	public ArrayList<Move> getValidMoves (int color) {
		ArrayList<Move> res = new ArrayList<Move>();
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				if (isValidMove(i, j, color))
					res.add(new Move(i, j));
		return res;
	}
	public int hostGame (Player p1, Player p2) throws IOException {
		setInit();
		boolean humanPlaying = p1 instanceof Human || p2 instanceof Human;
//		boolean humanPlaying = true;
		int currMove = 1;
		Move move = null;
		while (true) {
			if (isGameOver(currMove)) {
				if (humanPlaying)
					System.out.println(toString());
				return countPieces();
			}
			if (humanPlaying)
				System.out.println(toString());
			move = p1.getMove(this);
			movePiece(move.r, move.c, currMove);
			currMove = currMove % 2 + 1;
			
			if (isGameOver(currMove)) {
				if (humanPlaying)
					System.out.println(toString());
				return countPieces();
			}
			if (humanPlaying)
				System.out.println(toString());
			move = p2.getMove(this);
			movePiece(move.r, move.c, currMove);
			currMove = currMove % 2 + 1;
		}
	}
}
