import java.io.IOException;


public interface Player {
	Move getMove (Board b) throws IOException;
	int getColor ();
	void setColor (int color);
}
