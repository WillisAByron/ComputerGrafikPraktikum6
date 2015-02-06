/**
 * 
 */
package computergraphics.scenegraph;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.media.opengl.GL2;

import computergraphics.hlsvis.hls.HlsReturnSimulator;
import computergraphics.hlsvis.hls.TransportEvent;
import computergraphics.hlsvis.hls.TransportEvent.EventType;
import computergraphics.math.Matrix3;
import computergraphics.math.Vector3;
import computergraphics.terrain.GenerateTerrain;

/**
 * @author Glenn
 *
 */
public class MovableObject extends Node {
	
	private static final double MAX_FLIGHT_HEIGHT = 0.5;
	private double alpha = 0.00;
	private double stepLength;
	private Vector3 start;
	private Vector3 end;
	private BufferedImage bImage;
	
	private ScaleNode sN;
	private ColorNode cN;
	private RotationNode rN;
	private TranslationsNode tN;
	private List<Vector3> waypoints;
	private TransportEvent transportEvent;
	private HlsReturnSimulator hlsReturnSim = new HlsReturnSimulator();
	private boolean plane;
	private double planeHeight = 0;
	private RotationNode globalRotation;
	
	public MovableObject(ScaleNode sN, ColorNode cN, RotationNode rN, TranslationsNode tN, TriangleMeshNode tMeshNode,
			List<Vector3> waypoints, double stepLength, String heightField, TransportEvent transportEvent, boolean plane, RotationNode globalRotation) {
		this.sN = sN;
		this.cN = cN;
		this.rN = rN;
		this.tN = tN;
		this.waypoints = waypoints;
		this.stepLength = stepLength;
		this.addChild(tN);
		this.tN.addChild(rN);
		this.rN.addChild(cN);
		this.cN.addChild(sN);
		this.sN.addChild(tMeshNode);
		
		try {
			bImage = ImageIO.read(new File(heightField));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.start = waypoints.get(0);
		this.end = waypoints.get(1);
		setMatrix();
		this.plane = plane;
		this.transportEvent = transportEvent;
		hlsReturnSim.sendEvent(transportEvent);
		this.globalRotation = globalRotation;
	}

	@Override
	public void drawGl(GL2 gl) {
		// Remember current state of the render system
		gl.glPushMatrix();
		for (int childIndex = 0; childIndex < getNumberOfChildren(); childIndex++) {
			getChildNode(childIndex).drawGl(gl);
		}
		// Restore original state
		gl.glPopMatrix();
	}
	
	private void setMatrix() {
		Vector3 aVector = end.subtract(start);
		aVector.normalize();
		Vector3 bVector = new Vector3(0, 1, 0);
		Vector3 cVector = aVector.cross(bVector);
		Matrix3 matrix = new Matrix3(aVector, bVector, cVector);
		rN.setMatrix(matrix);
	}

	private void setHeigth(Vector3 aVector, double alpha) {
		final double pictureX = (bImage.getWidth(null) * (((aVector.get(0) + 2) / 4) / GenerateTerrain.MAX_X));
		final double pictureZ = (bImage.getHeight(null) * (((aVector.get(2) + 2 ) / 4) / GenerateTerrain.MAX_Z));
		final double height = (new Color(bImage.getRGB((int) pictureX, (int) pictureZ)).getRed() / 255.0) * GenerateTerrain.MAX_Y;
		if(plane){
			setPlaneHeight(aVector, height, alpha);
		}else{
			aVector.set(1, height + 0.05 + this.planeHeight);
		}
	}
	
	private void setPlaneHeight(Vector3 aVector, double height, double alpha){
		if(alpha <= 0.5){
			this.planeHeight += 0.05;
		}else{
			this.planeHeight -= 0.05;
		}
		if(height + this.planeHeight + 0.05 <= MAX_FLIGHT_HEIGHT){
			aVector.set(1, height + 0.05 + this.planeHeight);
		}else{
			aVector.set(1, MAX_FLIGHT_HEIGHT);
		}
	}

	public void tick(LocalDateTime realTime, List<MovableObject> lMO) {
		Date date = Date.from(realTime.atZone(ZoneOffset.systemDefault()).toInstant());
		alpha += stepLength;
		Vector3 aVector = (start.multiply(1-alpha)).add(end.multiply(alpha));
		setHeigth(aVector, alpha);
		tN.setTranslationsVector(aVector);
		if (alpha > 1) {
//			alpha = 0;
			start = end;
//			end = waypoints.get(waypoints.indexOf(end) + 1);
			setMatrix();
		}
		sendEvent(EventType.UNTERWEGS, date, new double[]{aVector.get(0)*100, aVector.get(2)*100});
		Vector3 endWaypoint = waypoints.get(waypoints.size() - 1);
		if(alpha >= 1){
			sendEvent(EventType.ANGEKOMMEN, date, new double[]{aVector.get(0)*100, aVector.get(2)*100});
			lMO.remove(this);
			globalRotation.removeNode(this);
			System.out.println("Test!");
		}
	}
	
	private void sendEvent(EventType type, Date date, double[] value){
		this.transportEvent.setType(type);
		this.transportEvent.setTimestamp(date);
		this.transportEvent.setGpsCoords(value);
		this.hlsReturnSim.sendEvent(transportEvent);
	}
}
