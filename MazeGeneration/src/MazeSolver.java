import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MazeSolver {

	Maze maze;
	Stack<Cell> stack;
	Random rand;
	boolean solved;
	Cell currentCell, startCell, endCell;
	
	public MazeSolver(Maze maze, Cell startCell, Cell endCell){
		this.maze = maze;
		this.stack = new Stack<>();
		this.rand = new Random();
		this.solved = false;
		this.startCell = startCell;
		this.endCell = endCell;
		this.currentCell = this.startCell;
		
	}
	
	public void solve(){
		while(!solved){
			solveByStep();
		}
	}
	
	public void solveByStep(){
		
		if(currentCell.equals(endCell)){
			solved = true;
			return;
		}
		
		if(!currentCell.wasVisited){
			currentCell.wasVisited = true;
		}
		
		ArrayList<Cell> neighbors = new ArrayList<>();
		
		if(currentCell.left != null && !currentCell.left.wasVisited && !currentCell.wallLeft){
			neighbors.add(currentCell.left);
		}
		if(currentCell.up != null && !currentCell.up.wasVisited && !currentCell.wallUp){
			neighbors.add(currentCell.up);
		}
		if(currentCell.right != null && !currentCell.right.wasVisited && !currentCell.wallRight){
			neighbors.add(currentCell.right);
		}
		if(currentCell.down != null && !currentCell.down.wasVisited && !currentCell.wallDown){
			neighbors.add(currentCell.down);
		}
		
		if(neighbors.size() != 0){
			
			stack.push(currentCell);
			Cell newCell = neighbors.get(rand.nextInt(neighbors.size()));
			currentCell = newCell;
			
		}else{
			currentCell = stack.pop();
		}
		
	}
	
}
