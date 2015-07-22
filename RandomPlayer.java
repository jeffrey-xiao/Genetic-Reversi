import java.io.IOException;
import java.util.ArrayList;


public class RandomPlayer implements Player{
	private int color;
	RandomPlayer (int color) {
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
		ArrayList<Move> res = b.getValidMoves(color);
		return res.get((int)(Math.random()*res.size()));
	}

}
