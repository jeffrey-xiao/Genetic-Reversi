import java.io.IOException;
import java.util.*;


public class GeneticPlayer implements Player, Comparable<GeneticPlayer>{
	private int color;
	double[] values;
	double fitness;
	static final int[][] dist = {{5,4,3,2,2,3,4,5},
									{4,4,4,4,4,4,4,4},
									{3,4,1,1,1,1,4,3},
									{2,4,1,0,0,1,4,2},
									{2,4,1,0,0,1,4,2},
									{3,4,1,1,1,1,4,3},
									{4,4,4,4,4,4,4,4},
									{5,4,3,2,2,3,4,5}};
	static final int FITNESS_COUNT = 10;
	GeneticPlayer (int color, double[] values) {
		this.color = color;
		this.values = Arrays.copyOf(values, values.length);
	}
	public int getColor () {
		return color;
	}
	public void setColor (int color) {
		this.color = color;
	}
	private ArrayList<Move> getScores (Board b) {
		ArrayList<Move> res = b.getValidMoves(color);
		int prevSameMoves = res.size();
		int prevDiffMoves = b.getValidMoves(color % 2 + 1).size();
		for (int i = 0; i < res.size(); i++) {
			Board nb = b.getCopy();
			nb.movePiece(res.get(i).r, res.get(i).c, color);
			int currSameMoves = nb.getValidMoves(color).size();
			int currDiffMoves = nb.getValidMoves(color % 2 + 1).size();
			double score = 0;
			for (int x = 0; x < b.height; x++)
				for (int y = 0; y < b.width; y++) {
					if (color == b.grid[x][y])
						score += values[dist[x][y]];
					else if (color % 2 + 1 == b.grid[x][y])
						score -= values[dist[x][y]];
				}
			res.get(i).score = score + values[6] * (prevDiffMoves - currDiffMoves) + values[7] * (currSameMoves - prevSameMoves) + values[8] * (nb.countPieces() - b.countPieces());
		}
		return res;
	}
	
	@Override
	public Move getMove (Board b) throws IOException {
		ArrayList<Move> res = getScores(b);
		double max = Integer.MIN_VALUE;
		for (Move m : res)
			max = Math.max(max, m.score);
		ArrayList<Move> choose = new ArrayList<Move>();
		for (Move m : res)
			if (Math.abs(max - m.score) < 0.0001)
				choose.add(m);
		return choose.get((int)(Math.random()*choose.size()));
	}
	public void calcFitness (Player p) throws IOException {
		Board b = new Board();
		p.setColor(color % 2 + 1);
		for (int i = 0; i < FITNESS_COUNT; i++) {
			b.setInit();
			if (color == 1) {
				int gameRes = b.hostGame(this, p);
				if (gameRes > 0)
					fitness += 1.0;
				else if (gameRes == 0)
					fitness += 0.5;
			} else {
				int gameRes = b.hostGame(p, this);
				if (gameRes < 0)
					fitness += 1.0;
				else if (gameRes == 0)
					fitness += 0.5;
			}
//			System.out.println(i);
		}
		fitness /= FITNESS_COUNT;
	}
	@Override
	public int compareTo (GeneticPlayer o) {
		return new Double(o.fitness).compareTo(fitness);
	}
}
