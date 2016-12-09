import java.io.Serializable;

/**
 * Used to store a maze to disk.
 * 
 * @author Samuel Klein
 */
public class Maze implements Serializable{

	public Cell[][] grid;
	
	public Maze(int x, int y){
		this.grid = new Cell[x][y];
	}
	
}
