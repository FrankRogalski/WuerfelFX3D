package tuncer.privat;

import java.util.concurrent.ThreadLocalRandom;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class Würfel extends MeshView {
	
	private boolean canFall = false;
	
	private Timeline tl_animationX, tl_animationY, tl_animationZ;
	
	
	public Würfel(float width, float height, float depth) {
		Image img = new Image(Cube.class.getResourceAsStream("Bilder/Wuerfel.png"));
		
		PhongMaterial material = new PhongMaterial();
		material.setDiffuseMap(img);
		material.setDiffuseColor(Color.SILVER);
		
		this.setMesh(this.createTriangleMesh(width, height, depth));
		this.setMaterial(material);
	}
	
	public void setRotate(double angle, Point3D rotateAxis) {
		this.getTransforms().add(new Rotate(angle, rotateAxis));
	}
	
	public void roll() {
		final double fps = 1000/60;
		
		final double x = ThreadLocalRandom.current().nextDouble(-6, 6);
		final double y = ThreadLocalRandom.current().nextDouble(-6, 6);
		final double z = ThreadLocalRandom.current().nextDouble(-6, 6);
		
		
		tl_animationX = new Timeline(new KeyFrame(Duration.millis(fps), e -> {
			this.getTransforms().add(new Rotate(x, Rotate.X_AXIS));
		}));
		
		tl_animationY = new Timeline(new KeyFrame(Duration.millis(fps), e -> {
			this.getTransforms().add(new Rotate(y, Rotate.Y_AXIS));
		}));
		
		tl_animationZ = new Timeline(new KeyFrame(Duration.millis(fps), e -> {
			this.getTransforms().add(new Rotate(z, Rotate.Z_AXIS));
		}));
		
		tl_animationX.setCycleCount(Timeline.INDEFINITE);
		tl_animationY.setCycleCount(Timeline.INDEFINITE);
		tl_animationZ.setCycleCount(Timeline.INDEFINITE);
		
		tl_animationY.play();
		tl_animationX.play();
		tl_animationZ.play();
		
		this.canFall = true;
	}
	
	public void fall(Box platte) {
		this.setTranslateY(this.getTranslateY() + 2);
	
		if (this.getBoundsInParent().intersects(platte.getBoundsInParent())) {
			tl_animationY.stop();
			tl_animationX.stop();
			tl_animationZ.stop();
			this.canFall = false;
		}
	}
	
	public boolean getCanFall() {
		return this.canFall;
	}
	
	
	
	private TriangleMesh createTriangleMesh(float hw, float hh, float hd) {
		final float[] points = {		
				hw, -hh, hd,	// 0 Rechts Oben Hinten
				hw, -hh, -hd,	// 1 Rechts Oben Vorne
				hw, hh, -hd,	// 2 Rechts Unten Vorne
				hw, hh, hd,		// 3 Rechts Unten Hinten
				-hw, -hh, hd,	// 4 Links Oben Hinten
				-hw, hh, -hd,	// 5 Links Unten Vorne
				-hw, hh, hd,	// 6 Links Unten Hinten
				-hw, -hh, -hd	// 7 Links Oben Vorne
		};
		
		final float[] tex = {
				(200f/800f), (0f/600f), 	// 0
	    		(400f/800f), (0f/600f), 	// 1
	    		(0f/800f), (200f/600f), 	// 2
	    		(200f/800f), (200f/600f), 	// 3
	    		(400f/800f), (200f/600f), 	// 4
	    		(600f/800f), (200f/600f), 	// 5
	    		(800f/800f), (200f/600f),	// 6
	    		(0f/800f), (400f/600f), 	// 7
	    		(200f/800f), (400f/600f),	// 8
	    		(400f/800f), (400f/600f),	// 9
	    		(600f/800f), (400f/600f),	// 10
	    		(800f/800f), (400f/600f),	// 11
	    		(200f/800f), (600f/600f),	// 12
	    		(400f/800f), (600f/600f)	// 13
		};
		
		final int[] faces = {
	           7,3, 5,8, 2,9,	// 0 Vordere Seite
	           2,9, 1,4, 7,3,	// 1 Vordere Seite
	           1,4, 2,9, 3,10,	// 2 Rechte Seite
	           3,10, 0,5, 1,4,	// 3 Rechte Seite
	           4,0, 7,3, 1,4,	// 4 Obere Seite
	           4,0, 1,4, 0,1,	// 5 Obere Seite
	           4,6, 0,5, 3,10,	// 6 Hintere Seite
	           3,10, 6,11, 4,6,	// 7 Hintere Seite
	           4,2, 6,7, 5,8,	// 8 Linke Seite
	           5,8, 7,3, 4,2,	// 9 Linke Seite
	           6,12, 3,13, 2,9,	// 10 Untere Seite
	           2,9, 5,8, 6,12	// 11 Untere Seite
		};
		
		TriangleMesh mesh = new TriangleMesh();
		mesh.getPoints().addAll(points);
		mesh.getTexCoords().addAll(tex);
		mesh.getFaces().addAll(faces);
		
		return mesh;
	}
}