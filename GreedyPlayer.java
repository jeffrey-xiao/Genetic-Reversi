import java.io.IOException;
import java.util.ArrayList;


public class GreedyPlayer implements Player {
	private int color, look;
	GreedyPlayer (int color, int look) {
		this.color = color;
		this.look = look;
	}
	public int getColor () {
		return color;
	}
	public void setColor (int color) {
		this.color = color;
	}
	private ArrayList<Move> getScores (Board b, int look) {
		if (look == 0) {
			ArrayList<Move> res = new ArrayList<Move>();
			res.add(new Move(-1, -1, color == 1 ? b.countPieces() : -b.countPieces()));
			return res;
		}
		ArrayList<Move> res = b.getValidMoves(color);
		if (res.size() == 0) {
			res.add(new Move(-1, -1, color == 1 ? b.countPieces() : -b.countPieces()));
			return res;
		}
		for (int i = 0; i < res.size(); i++) {
			Board nb = b.getCopy();
			nb.movePiece(res.get(i).r, res.get(i).c, color);
			ArrayList<Move> next = getScores(nb, look-1);
			double max = Integer.MIN_VALUE;
			for (int j = 0; j < next.size(); j++) {
				max = Math.max(max, next.get(j).score);
			}
			res.get(i).score = max;
		}
		return res;
	}
	@Override
	public Move getMove (Board b) throws IOException {
		ArrayList<Move> res = getScores(b, look);
		double max = Integer.MIN_VALUE;
		for (Move m : res)
			max = Math.max(max, m.score);
		ArrayList<Move> choose = new ArrayList<Move>();
		for (Move m : res)
			if (Math.abs(m.score - max) < 0.0001)
				choose.add(m);
		return choose.get((int)(Math.random()*choose.size()));
	}

}
