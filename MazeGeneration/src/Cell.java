import java.io.Serializable;

/**
 * Part of a maze.
 * Contains 4 possible walls.
 * Contains upt o 4 possible neighbor cells.
 * 
 * @author Samuel Klein
 */
public class Cell implements Serializable{

	public int x;
	public int y;
	
	public boolean wallLeft = true;
	public boolean wallUp = true;
	public boolean wallRight = true;
	public boolean wallDown = true;
	
	public Cell left;
	public Cell up;
	public Cell right;
	public Cell down;
	
	public boolean wasVisited = false;
	
	public Cell(int x, int y){
		this.x = x;
		this.y = y;
	}
	
}
