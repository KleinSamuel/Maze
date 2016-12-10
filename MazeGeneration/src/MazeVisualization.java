import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Visualize a given maze with javaFX.
 * 
 * @author Samuel Klein
 */
public class MazeVisualization extends Application{

	MazeGenerator mg;
	MazeSolver ms;
	GraphicsContext gc;
	Canvas canvas;
	Thread thread;
	Timeline timeline;
	Random rand;
	Maze maze;
	
	int size;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		this.rand = new Random();
		int amountX = 50;
		int amountY = 50;
		size = 20;
		
		mg = new MazeGenerator(amountX, amountY, size);
//		mg.maze = mg.readMaze("/home/sam/Desktop/maze_1.maze");
		mg.generate();
		this.maze = mg.maze;
		mg.setUnvisited(this.maze);
		this.ms = new MazeSolver(this.maze, mg.maze.grid[0][0], mg.maze.grid[mg.maze.grid.length-1][mg.maze.grid[0].length-1]);
		
		primaryStage.setTitle("Maze Generation Algorithm Visualization");
        Group root = new Group();
        canvas = new Canvas(mg.maze.grid[0].length*mg.size, mg.maze.grid.length*mg.size);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
        
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), 
                        event -> {
//                        	mg.generateByStep();
//                        	update(mg.currentCell.x, mg.currentCell.y);
                        	ms.solveByStep();
                        	update(ms.currentCell.x, ms.currentCell.y, true);
                        	
//                        	if(mg.toVisit == 0){
//                        		update(-size, -size);
//                        		mg.saveMazeAsImage("/home/sam/Desktop/maze2", canvas);
//                        		stopTimeline();
//                        	}
                        	
                        	if(ms.solved){
                        		update(-size, -size, false);
                        		stopTimeline();
                        		double all = maze.grid.length*maze.grid[0].length;
                        		int path = ms.stack.size();
                        		System.out.println("STATISTICS:");
                        		System.out.println("ALL: "+all+"\t- "+(all/all)*100+"%");
                        		System.out.println("PATH: "+path+"\t- "+(path/all)*100+"%");
                        		int unvis = 0, vis = 0;
                        		for (int i = 0; i < maze.grid.length; i++) {
                        			for (int j = 0; j < maze.grid[i].length; j++) {
                        				if(!maze.grid[i][j].wasVisited){
                        					unvis++;
                        				}else{
                        					vis++;
                        				}
                        			}
                        		}
                        		System.out.println("UNVISITED: "+unvis+"\t- "+(unvis/all)*100+"%");
                        		System.out.println("VISITED: "+vis+"\t- "+(vis/all)*100+"%");
                        	}
                        }
                ),
                new KeyFrame(Duration.millis(20))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        
        update(mg.currentCell.x, mg.currentCell.y, true);
	}
	
	public void stopTimeline(){
		timeline.stop();
	}
	
	public void update(int x, int y, boolean flag){
		
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		gc.setFill(Color.CADETBLUE);
		
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
		
		for (int i = 0; i < maze.grid.length; i++) {
			for (int j = 0; j < maze.grid[i].length; j++) {
				
				Cell c = maze.grid[i][j];
				
				gc.setFill(Color.CADETBLUE);
				
				if(c.wasVisited && flag){
					gc.fillRect(c.x, c.y, size, size);
				}
				if(c.wallLeft){
					gc.strokeLine(c.x, c.y, c.x+size, c.y);
				}
				if(c.wallUp){
					gc.strokeLine(c.x, c.y, c.x, c.y+size);
				}
				if(c.wallRight){
					gc.strokeLine(c.x, c.y+size, c.x+size, c.y+size);
				}
				if(c.wallDown){
					gc.strokeLine(c.x+size, c.y, c.x+size, c.y+size);
				}
				
				if(c.equals(ms.endCell)){
					gc.setFill(Color.BLACK);
					gc.fillRect(c.x, c.y, size, size);
				}else if(ms.stack.contains(c)){
					gc.setFill(Color.CRIMSON);
					gc.fillRect(c.x, c.y, size, size);
				}
				
			}
		}
		
		gc.setFill(Color.BLUE);
		gc.fillRect(x, y, size, size);
	}

	
	public static void main(String[] args) {
		launch(args);
	}
	
}
