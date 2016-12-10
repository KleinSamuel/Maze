import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;

/**
 * Generate a maze which is solvable and every cell is reachable.
 * 
 * @author Samuel Klein
 */
public class MazeGenerator {

	public Maze maze;
	public Stack<Cell> stack;
	public Cell currentCell;
	public Random rand;
	public int toVisit;
	public MazeVisualization mv;
	
	public int size;
	
	public MazeGenerator(int width, int height, int size){
		this.maze = new Maze(width, height);
		this.maze.grid = new Cell[width][height];
		this.stack = new Stack<Cell>();
		this.size = size;
		this.toVisit = 0;
		this.rand = new Random();
		
		for (int i = 0; i < maze.grid.length; i++) {
			for (int j = 0; j < maze.grid[i].length; j++) {
				maze.grid[i][j] = new Cell(i*size, j*size);
				toVisit++;
			}
		}
		for (int i = 0; i < maze.grid.length; i++) {
			for (int j = 0; j < maze.grid[i].length; j++) {
				if(i > 0){
					maze.grid[i][j].up = maze.grid[i-1][j];
				}
				if(j > 0){
					maze.grid[i][j].left = maze.grid[i][j-1];
				}
				if(i < maze.grid.length-1){
					maze.grid[i][j].down = maze.grid[i+1][j];
				}
				if(j < maze.grid[0].length-1){
					maze.grid[i][j].right = maze.grid[i][j+1];
				}
			}
		}
		
		currentCell = maze.grid[0][0];
	}
	
	public enum Direction{
		LEFT,UP,RIGHT,DOWN;
	}
	
	/**
	 * Generate the whole maze at once.
	 */
	public void generate(){
		while(toVisit != 0){
			generateByStep();
		}
	}
	
	/**
	 * Generate the next step in the maze creation process.
	 * Used for visualization.
	 */
	public void generateByStep(){
		
		if(toVisit == 0){
			return;
		}
			
		if(!currentCell.wasVisited){
			currentCell.wasVisited = true;
			toVisit--;				
		}
		
		/* find neighbors of current cell which are not visited */
		ArrayList<Direction> neightbors = new ArrayList<>();
		
		if(currentCell.left != null && !currentCell.left.wasVisited){
			neightbors.add(Direction.LEFT);
		}
		if(currentCell.up != null && !currentCell.up.wasVisited){
			neightbors.add(Direction.UP);
		}
		if(currentCell.right != null && !currentCell.right.wasVisited){
			neightbors.add(Direction.RIGHT);
		}
		if(currentCell.down != null && !currentCell.down.wasVisited){
			neightbors.add(Direction.DOWN);
		}
		
		
		/* if there are neighbors which are unvisited */
		if(neightbors.size() != 0){
			Direction dir = neightbors.get(rand.nextInt(neightbors.size()));
			stack.push(currentCell);
			
			Cell newCell = null;
			switch (dir) {
			case LEFT:
				newCell = currentCell.left;
				newCell.wallRight = false;
				currentCell.wallLeft = false;
				break;
			case UP:
				newCell = currentCell.up;
				newCell.wallDown = false;
				currentCell.wallUp = false;
				break;
			case RIGHT:
				newCell = currentCell.right;
				newCell.wallLeft = false;
				currentCell.wallRight = false;
				break;
			case DOWN:
				newCell = currentCell.down;
				newCell .wallUp = false;
				currentCell.wallDown = false;
				break;
			default:
				break;
			}
			
			currentCell = newCell;
			
		}
		/* if there a no unvisited neighbors */
		else{
			currentCell = stack.pop();
		}
	}
	
	public void setUnvisited(Maze maze){
		for (int i = 0; i < maze.grid.length; i++) {
			for (int j = 0; j < maze.grid[i].length; j++) {
				maze.grid[i][j].wasVisited = false;
			}
		}
	}
	
	/**
	 * Store a maze as a file.
	 * 
	 * @param filepath
	 * @param maze
	 */
	public void storeMaze(String filepath, Maze maze){
		
		try {
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(filepath));
			
			bw.write(maze.grid.length+","+maze.grid[0].length+","+size+"\n");
			
			for (int i = 0; i < maze.grid.length; i++) {
				for (int j = 0; j < maze.grid[i].length; j++) {
					Cell curr = maze.grid[i][j];
					String l = (curr.wallLeft) ? "1" : "0";
					String u = (curr.wallUp) ? "1" : "0";
					String r = (curr.wallRight) ? "1" : "0";
					String d = (curr.wallDown) ? "1" : "0";
					bw.write(l+","+u+","+r+","+d+"\n");
				}
			}
			
			bw.flush();
			bw.close();
			
			System.out.println("Stored maze at "+filepath+".");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Read in a maze file.
	 * 
	 * @param filepath
	 * @return
	 */
	public Maze readMaze(String filepath){
		Maze out = null;
		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(filepath));
			
			String line = br.readLine();
			
			int width = Integer.parseInt(line.split(",")[0]);
			int height = Integer.parseInt(line.split(",")[1]);
			int size = Integer.parseInt(line.split(",")[2]);
			this.size = size;
			out = new Maze(width, height);
			
			int x = 0;
			int y = 0;
			
			while((line = br.readLine()) != null && !line.equals("")){
				
				String[] walls = line.split(",");
				out.grid[x][y] = new Cell(x*size, y*size);
				out.grid[x][y].wallLeft = (Integer.parseInt(walls[0]) == 1);
				out.grid[x][y].wallUp = (Integer.parseInt(walls[1]) == 1);
				out.grid[x][y].wallRight = (Integer.parseInt(walls[2]) == 1);
				out.grid[x][y].wallDown = (Integer.parseInt(walls[3]) == 1);
				
				if(y == height - 1){
					y = 0;
					x++;
				}else{
					y++;
				}
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}
	
	/**
	 * Save a picture of the current canvas as png on disk.
	 * 
	 * @param filepath
	 * @param canvas
	 */
	public void saveMazeAsImage(String filepath, Canvas canvas) {

		WritableImage writableImage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
        canvas.snapshot(null, writableImage);
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
        
        try {
			ImageIO.write(renderedImage, "png", new File(filepath));
			
			System.out.println("Saved image to "+filepath+".");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
