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
	GraphicsContext gc;
	Canvas canvas;
	Thread thread;
	Timeline timeline;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		int amountX = 50;
		int amountY = 50;
		int size = 20;
		
		mg = new MazeGenerator(amountX, amountY, size);
//		mg.maze = mg.readMaze("/home/sam/Desktop/maze_1.maze");
		
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
                        	mg.generateByStep();
                        	update(mg.currentCell.x, mg.currentCell.y);
                        	if(mg.toVisit == 0){
                        		update(-size, -size);
//                        		mg.saveMazeAsImage("/home/sam/Desktop/maze", canvas);
                        		stopTimeline();
                        	}
                        }
                ),
                new KeyFrame(Duration.millis(10))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        
        update(mg.currentCell.x, mg.currentCell.y);
	}
	
	public void stopTimeline(){
		timeline.stop();
	}
	
	public void update(int x, int y){
		
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		gc.setFill(Color.BURLYWOOD);
		
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
		
		for (int i = 0; i < mg.maze.grid.length; i++) {
			for (int j = 0; j < mg.maze.grid[i].length; j++) {
				
				Cell c = mg.maze.grid[i][j];
				
				if(c.wasVisited){
					gc.fillRect(c.x, c.y, mg.size, mg.size);
				}
				if(c.wallLeft){
					gc.strokeLine(c.x, c.y, c.x+mg.size, c.y);
				}
				if(c.wallUp){
					gc.strokeLine(c.x, c.y, c.x, c.y+mg.size);
				}
				if(c.wallRight){
					gc.strokeLine(c.x, c.y+mg.size, c.x+mg.size, c.y+mg.size);
				}
				if(c.wallDown){
					gc.strokeLine(c.x+mg.size, c.y, c.x+mg.size, c.y+mg.size);
				}
				
			}
		}
		
		gc.setFill(Color.BLUE);
		gc.fillRect(x, y, mg.size, mg.size);
	}

	
	public static void main(String[] args) {
		launch(args);
	}
	
}
