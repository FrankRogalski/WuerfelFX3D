package tuncer.privat;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Cube extends Application {
	
	private Würfel würfel;
	private Box platte;
	
	private CameraFX3D cam;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() throws Exception {
		final double fps = 1000/60;
		final Timeline tl_draw = new Timeline(new KeyFrame(Duration.millis(fps), e -> {
			this.draw();
		}));
		tl_draw.setCycleCount(Timeline.INDEFINITE);
		tl_draw.play();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
			throw new RuntimeException("Dein System ist veraltet!");
		}
		
		Pane root = new Pane();
		Scene scene = new Scene(root, 1000, 1000, true, SceneAntialiasing.BALANCED);
		
		this.würfel = new Würfel(100, 100, 100);
		
		this.platte = new Box(2000, 10, 2000);
		this.platte.setMaterial(new PhongMaterial(Color.BROWN.darker().darker()));
		this.platte.setTranslateY(500);
		
		Group group = new Group(this.würfel, platte, new AmbientLight());
		
		SubScene subScene = new SubScene(group, scene.getWidth(), scene.getHeight(), true, SceneAntialiasing.BALANCED);
		subScene.setFill(Color.BLACK);
		
		root.getChildren().addAll(subScene);

		this.cam = new CameraFX3D(subScene, true);
		cam.setZoomSpeed(50);
		cam.setNearClip(10);
		cam.setFarClip(100000);
		cam.setZ(-5000);
		
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				switch (e.getCode()) {
					case SPACE:
						if (!würfel.getCanFall()) {
							würfel.setTranslateY(0);
							würfel.roll();
						}
						break;
					default:	
				}
			}
		});
		
		subScene.setCamera(this.cam);
		primaryStage.setTitle("WürfelFX3D");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void draw() {
		if (this.würfel.getCanFall()) {
			this.würfel.fall(this.platte);
		}
	}
}