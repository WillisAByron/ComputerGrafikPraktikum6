/**
 * Prof. Philipp Jenke
 * Hochschule fÃ¼r Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package computergraphics.applications;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;

import computergraphics.datastructures.ObjIO;
import computergraphics.datastructures.TriangleMesh;
import computergraphics.framework.AbstractCGFrame;
import computergraphics.hlsvis.hls.City.Location;
import computergraphics.hlsvis.hls.HlsSimulator;
import computergraphics.hlsvis.hls.TransportEvent;
import computergraphics.hlsvis.hls.TransportEvent.EventType;
import computergraphics.hlsvis.hls.TransportNetwork;
import computergraphics.hlsvis.hls.TransportOrder;
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

	private static final String DHL_PACKAGE = "meshes/cube.obj";
	
	private static final String PLANE = "meshes/Plane2.obj";
	
	public final String heightFile = "ground/hoehenkarte_deutschland.png";

	private final String textureFileName = "meshes/textures/karte_deutschland_fix.png";

	private List<MovableObject> lMO = new ArrayList<>();

	private LocalDateTime realTime = LocalDateTime.of(2014, 12, 8, 0, 0);

	private HlsSimulator hlsSim;
	
	private RotationNode globalRotation =  new RotationNode(90, new Vector3(1, 0, 0));

	/**
	 * Constructor.
	 */
	public CGFrame(int timerInverval) {
		super(timerInverval);

		// HLS Simulator starten
		hlsSim = new HlsSimulator();

		globalRotation.addChild(createLandscape(new Vector3(4, 1, 4)));
		getRoot().addChild(globalRotation);
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
		TranslationsNode tn = new TranslationsNode(new Vector3((vector.get(0) / 2) * (-1), 0, (vector.get(2) / 2)
				* (-1)));
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
		this.setRealTime(this.getRealTime().plusMinutes(5));
		hlsSim.tick(Date.from(this.getRealTime().atZone(ZoneOffset.systemDefault()).toInstant()));
		createMovebleObject(hlsSim.getTransportOrderQueue().getList());
		for (MovableObject mO : new ArrayList<>(lMO)) {
			mO.tick(this.getRealTime(), lMO);
		}
	}

	private void createMovebleObject(List<TransportOrder> list) {
		Date dateTimeNow = Date.from(this.getRealTime().atZone(ZoneOffset.systemDefault()).toInstant());
		for (TransportOrder transportOrder : new ArrayList<>(list)) {
			if (transportOrder.getStartTime().before(dateTimeNow)) {
				createMONode(transportOrder);
				boolean removed = false;
				do{
					try{
						removed = list.remove(transportOrder);
					}catch(ConcurrentModificationException e){
						System.err.println("Nicht gelöscht!");
					}
				}while(!removed);
			}
		}
		
	}

	private void createMONode(TransportOrder transportOrder) {
		List<Vector3> wP = new ArrayList<>();
		double[] start = getCoordinates(transportOrder.getStartLocation());
		double[] target = getCoordinates(transportOrder.getTargetLocation());
		wP.add(new Vector3((start[0] * 4) - 2, 0, (start[1] * 4) - 2));
		wP.add(new Vector3((target[0] * 4) - 2, 0, (target[1] * 4) - 2));
		double stepLength = 1 / (TransportNetwork.getEntfernung(transportOrder.getStartLocation(), transportOrder.getTargetLocation()) / 5);
		double random = Math.random() * 10;
		String mesh = DHL_PACKAGE;
		ScaleNode sN = new ScaleNode(new Vector3(0.05, 0.05, 0.05));
		boolean texture = true;
		boolean plane = false;
		int number = 2;
		if(random < 3){
			number = 3;
			mesh = PLANE;
			texture = false;
			plane = true;
			sN = new ScaleNode(new Vector3(0.0001, 0.0001, 0.0001));
		}
		ColorNode cN = new ColorNode(new Vector3(0, 0, 1), texture);
		RotationNode rN = new RotationNode(0, new Vector3(0, 0, 0));
		TranslationsNode tN = new TranslationsNode(new Vector3(0, 0.02, 0));
		TriangleMeshNode tMN = new TriangleMeshNode(createTriangleMeshFromObject(mesh), texture, number);
		TransportEvent transportEvent = new TransportEvent(transportOrder.getDeliveryNumber(), transportOrder.getOrderNumber(),
				transportOrder.getStartTime(), EventType.ABGEFAHREN, new double[]{start[0] * 100, start[1] *100});
		MovableObject mO = new MovableObject(sN, cN, rN, tN, tMN, wP, stepLength, this.heightFile, transportEvent, plane, globalRotation);
		globalRotation.addChild(mO);

		lMO.add(mO);
	}

	private double[] getCoordinates(Location loc) {
		return TransportNetwork.getCity(loc).getCoords();
	}

	/**
	 * Program entry point.
	 */
	public static void main(String[] args) {
		new CGFrame(500);
	}

	public LocalDateTime getRealTime() {
		return realTime;
	}

	public void setRealTime(LocalDateTime realTime) {
		this.realTime = realTime;
	}
}
