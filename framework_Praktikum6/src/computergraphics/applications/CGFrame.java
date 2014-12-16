/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package computergraphics.applications;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

import computergraphics.datastructures.ObjIO;
import computergraphics.datastructures.TriangleMesh;
import computergraphics.framework.AbstractCGFrame;
import computergraphics.framework.Camera;
import computergraphics.framework.CameraController;
import computergraphics.math.Vector3;
import computergraphics.scenegraph.ColorNode;
import computergraphics.scenegraph.MovableObject;
import computergraphics.scenegraph.Node;
import computergraphics.scenegraph.RotationNode;
import computergraphics.scenegraph.ScaleNode;
import computergraphics.scenegraph.TranslationsNode;
import computergraphics.scenegraph.TriangleMeshNode;
import computergraphics.terrain.GenerateTerrain;

/**
 * Application for the first exercise.
 * 
 * @author Philipp Jenke
 * 
 */
public class CGFrame extends AbstractCGFrame {

	public final double MAX_X = 1;

	public final double MAX_Y = 0.1;

	public final double MAX_Z = 1;

	public final double STEP = 0.005;
	
	private static final long serialVersionUID = 4257130065274995543L;

	private static final String FILE_NAME_SPHERE = "meshes/sphere.obj";
	
	public final String heightFile = "ground/hoehenkarte_deutschland.png";
	
	private final String textureFileName = "meshes/textures/karte_deutschland_fix.png";
	
	private List<MovableObject> lMO = new ArrayList<>();
	
	/**
	 * Constructor.
	 */
	public CGFrame(int timerInverval) {
		super(timerInverval);
		
		RotationNode landscapeRotation = new RotationNode(90, new Vector3(1, 0, 0));
		landscapeRotation.addChild(createLandscape(new Vector3(4, 1, 4)));
		getRoot().addChild(landscapeRotation);
		
//		List<Vector3> wP = new ArrayList<>();
//		wP.add(new Vector3(0.45, 0, 0.45));
//		wP.add(new Vector3(-0.45, 0, -0.45));
//		wP.add(new Vector3(0.45, 0, -0.45));
//		wP.add(new Vector3(-0.45, 0, 0.45));
//		
//		ScaleNode sN = new ScaleNode(new Vector3(0.2, 0.2, 0.2));
//		ColorNode cN = new ColorNode(new Vector3(0, 0, 1), false);
//		RotationNode rN = new RotationNode(0, new Vector3(0, 0, 0));
//		TranslationsNode tN = new TranslationsNode(new Vector3(0, 0.02, 0));
//		TriangleMeshNode tMN = new TriangleMeshNode(createTriangleMeshFromObject(FILE_NAME_SPHERE), false, 2);
//		MovableObject mO = new MovableObject(sN, cN, rN, tN, tMN, wP, heightFile);
//		getRoot().addChild(mO);
//		
//		lMO.add(mO);
	}

	private TriangleMesh createTriangleMeshFromObject(String filePath) {
		TriangleMesh trMesh = new TriangleMesh();
		ObjIO objIO = new ObjIO();
		objIO.einlesen(filePath, trMesh);
		trMesh.updateNormals();
		return trMesh;
	}

	private Node createLandscape(Vector3 vector) {
		ColorNode cn = new ColorNode(new Vector3(0, 0, 0), true);
		ScaleNode sn = new ScaleNode(vector);
		TranslationsNode tn = new TranslationsNode(new Vector3((vector.get(0) / 2) * (-1), 0, (vector.get(2) / 2) * (-1)));
		GenerateTerrain gt = new GenerateTerrain();
		TriangleMesh newGround = null;
		try {
			newGround = gt.generateGroundWithTexture(MAX_X, MAX_Y, MAX_Z, STEP, heightFile, textureFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		TriangleMeshNode trMeshNode = new TriangleMeshNode(newGround, true, 1);
		cn.addChild(tn);
		tn.addChild(sn);
		sn.addChild(trMeshNode);
		return cn;
	}

	/*
	 * (nicht-Javadoc)
	 * 
	 * @see computergrafik.framework.ComputergrafikFrame#timerTick()
	 */
	@Override
	protected void timerTick() {
		 for (MovableObject mO : lMO) {
			mO.tick();
		}
	}

	/**
	 * Program entry point.
	 */
	public static void main(String[] args) {
		new CGFrame(500);
	}
}
