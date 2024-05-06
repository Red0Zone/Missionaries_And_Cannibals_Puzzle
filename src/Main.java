import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
	private VBox vb;
	private TextArea tx;
	private BreadthFirstSearch bfs;
	private DepthFirstSearch dfs;
	private ArrayList<Shape> shapes;
	private Pane pane;
	private Button bfsBT, dfsBT, nextBT;
	private List<State> path;
	private int index;
	private boolean look = true;

	public static void main(String[] args) {

		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		pane = new Pane();
		pane.setPrefSize(600, 600);
		vb = new VBox();
		tx = new TextArea();
		tx.setMinSize(300, 200);
		bfsBT = new Button("BFS");
		dfsBT = new Button("DFS");
		nextBT = new Button("Next state");
		nextBT.setVisible(false);

		vb.setPadding(new Insets(20, 20, 20, 20));
		vb.setAlignment(Pos.CENTER);
		HBox hb1 = new HBox(10, bfsBT, dfsBT, nextBT);
		hb1.setAlignment(Pos.CENTER);
		hb1.setPadding(new Insets(20, 20, 20, 20));
		HBox hb2 = new HBox(10, tx, hb1);
		hb2.setAlignment(Pos.CENTER);
		hb2.setPadding(new Insets(20, 20, 20, 20));
		vb.getChildren().addAll(pane, hb2);
		pane.setStyle("-fx-background-image:url('file:cover.png');-fx-background-size:cover");
		createPlayers();

		Scene mainScene = new Scene(vb, 1300, 800);
		stage.setScene(mainScene);
		stage.show();

		bfsBT.setOnAction(e -> {
			executeBFS(new State(3, 3, true, 0, 0));
			nextBT.setVisible(true);
			bfsBT.setVisible(false);
			dfsBT.setVisible(false);
			tx.setText("cannibalLeft: " + path.get(index - 1).getCannibalLeft() + ", missionaryLeft: "
					+ path.get(index - 1).getMissionaryLeft() + ", cannibalRight: "
					+ path.get(index - 1).getCannibalRight() + ", missionaryRight: "
					+ path.get(index - 1).getMissionaryRight());
			bfsBT.setStyle("-fx-background-color:green");
		});
		dfsBT.setOnAction(e -> {
			executeDFS(new State(3, 3, true, 0, 0));
			nextBT.setVisible(true);
			bfsBT.setVisible(false);
			dfsBT.setVisible(false);
			tx.setText("cannibalLeft: " + path.get(index - 1).getCannibalLeft() + ", missionaryLeft: "
					+ path.get(index - 1).getMissionaryLeft() + ", cannibalRight: "
					+ path.get(index - 1).getCannibalRight() + ", missionaryRight: "
					+ path.get(index - 1).getMissionaryRight());
			dfsBT.setStyle("-fx-background-color:red");
		});
		nextBT.setOnAction(e -> {
			if (index > 1) {
				if (look != false)
					Animation(path.get(index - 2), path.get(index - 1));
			} else {
				nextBT.setVisible(false);
				bfsBT.setVisible(true);
				dfsBT.setVisible(true);
				tx.setText(tx.getText().concat("\nSteps = " + (path.size() - 1)));
			}
		});
	}

	private void executeBFS(State initialState) {
		BreadthFirstSearch search = new BreadthFirstSearch();
		State solution = search.exec(initialState);
		getPath(solution);
		createPlayers();
	}

	private void executeDFS(State initialState) {
		DepthFirstSearch search = new DepthFirstSearch();
		State solution = search.exec(initialState);
		getPath(solution);
		createPlayers();
	}

	private void getPath(State solution) {

		path = new ArrayList<State>();
		State state = solution;
		while (null != state) {
			path.add(state);
			state = state.getParentState();
		}
		index = path.size();

	}

	private void createPlayers() {
		pane.getChildren().clear();
		shapes = new ArrayList<>();
		Circle circle1 = null;
		for (int i = 0, x = 100, y = 400; i < 3; i++, x += 45, y -= 20) {
			circle1 = new Circle(x, y, 20, Color.GREEN);
			shapes.add(circle1);
		}
		for (int i = 0, x = 150, y = 450; i < 3; i++, x += 45, y -= 20) {
			circle1 = new Circle(x, y, 20, Color.RED);
			shapes.add(circle1);
		}
		Text tx1 = new Text("Cannibals");
		tx1.setStyle("-fx-fill:red;-fx-font-size:20px");
		tx1.setX(40);
		tx1.setY(50);
		pane.getChildren().add(tx1);
		Text tx2 = new Text("Missioners");
		tx2.setStyle("-fx-fill:green;-fx-font-size:20px");
		tx2.setX(40);
		tx2.setY(70);
		pane.getChildren().add(tx2);
		pane.getChildren().addAll(shapes);

	}

	private void Animation(State state, State stateBefore) {

		int missionariesLeft = state.getMissionaryLeft();
		int cannibalsLeft = state.getCannibalLeft();
		int missionariesRight = state.getMissionaryRight();
		int cannibalsRight = state.getCannibalRight();
		boolean boat = state.isOnLeft();

		String s = tx.getText();
		tx.setText(s + "\n" + "cannibalLeft: " + cannibalsLeft + ", missionaryLeft: " + missionariesLeft
				+ ", cannibalRight: " + cannibalsRight + ", missionaryRight: " + missionariesRight);
		index--;

		if (missionariesLeft == 3 && cannibalsLeft == 3)
			return;
		if (missionariesLeft == 2 && stateBefore.getMissionaryLeft() == 3)
			move(shapes.get(2), true);
		if (missionariesLeft == 3 && stateBefore.getMissionaryLeft() == 2)
			move(shapes.get(2), false);
		if (missionariesLeft == 1 && stateBefore.getMissionaryLeft() == 2)
			move(shapes.get(1), true);
		if (missionariesLeft == 2 && stateBefore.getMissionaryLeft() == 1)
			move(shapes.get(1), false);
		if (missionariesLeft == 0 && stateBefore.getMissionaryLeft() == 1)
			move(shapes.get(0), true);
		if (missionariesLeft == 1 && stateBefore.getMissionaryLeft() == 0)
			move(shapes.get(0), false);
		if (missionariesLeft == 3 && stateBefore.getMissionaryLeft() == 1) {
			move(shapes.get(2), false);
			move(shapes.get(1), false);
		}
		if (missionariesLeft == 1 && stateBefore.getMissionaryLeft() == 3) {
			move(shapes.get(2), true);
			move(shapes.get(1), true);
		}
		if (missionariesLeft == 0 && stateBefore.getMissionaryLeft() == 2) {
			move(shapes.get(1), true);
			move(shapes.get(0), true);
		}
		if (missionariesLeft == 2 && stateBefore.getMissionaryLeft() == 0) {
			move(shapes.get(2), false);
			move(shapes.get(1), false);
		}

		if (cannibalsLeft == 2 && stateBefore.getCannibalLeft() == 3)
			move(shapes.get(5), true);
		if (cannibalsLeft == 3 && stateBefore.getCannibalLeft() == 2)
			move(shapes.get(5), false);
		if (cannibalsLeft == 1 && stateBefore.getCannibalLeft() == 2)
			move(shapes.get(4), true);
		if (cannibalsLeft == 2 && stateBefore.getCannibalLeft() == 1)
			move(shapes.get(4), false);
		if (cannibalsLeft == 0 && stateBefore.getCannibalLeft() == 1)
			move(shapes.get(3), true);
		if (cannibalsLeft == 1 && stateBefore.getCannibalLeft() == 0)
			move(shapes.get(3), false);
		if (cannibalsLeft == 3 && stateBefore.getCannibalLeft() == 1) {
			move(shapes.get(5), false);
			move(shapes.get(4), false);
		}
		if (cannibalsLeft == 1 && stateBefore.getCannibalLeft() == 3) {
			move(shapes.get(5), true);
			move(shapes.get(4), true);
		}
		if (cannibalsLeft == 0 && stateBefore.getCannibalLeft() == 2) {
			move(shapes.get(4), true);
			move(shapes.get(3), true);
		}
		if (cannibalsLeft == 2 && stateBefore.getCannibalLeft() == 0) {
			move(shapes.get(4), false);
			move(shapes.get(3), false);
		}

	}

	private void move(Shape circle1, boolean direction) {

		Circle circle = (Circle) circle1;
		new Thread(() -> {
			look = false;
			for (int i = 0; i < 40; i++) {
				Platform.runLater(() -> {
					if (direction)
						circle.setCenterX(circle.getCenterX() + 20);
					else
						circle.setCenterX(circle.getCenterX() - 20);
				});
				try {
					Thread.sleep(15);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			look = true;
		}).start();
	}

}