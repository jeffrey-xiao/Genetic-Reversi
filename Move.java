class Move {
	int r, c;
	double score;
	Move (int r, int c) {
		this.r = r;
		this.c = c;
	}
	Move (int r, int c, double score) {
		this.r = r;
		this.c = c;
		this.score = score;
	}
	@Override
	public String toString () {
		return String.format("(%d, %d)", r, c);
	}
}