package computergraphics.scenegraph;

import javax.media.opengl.GL2;

import computergraphics.math.Matrix3;
import computergraphics.math.Vector3;

/**
 * @author Johann Bronsch, Glenn Bickel
 *
 *         Node zur rotation von Knoten.
 */
public class RotationNode extends Node {

	private Vector3 rotationsAchse;

	private double winkel;
	
	private double[] matrix4;

	/**
	 * Konstruktor
	 * 
	 * @param winkel
	 *            = Bogenmaﬂ
	 * @param rotationsAchse
	 *            = Zielvektor
	 */
	public RotationNode(double winkel, Vector3 rotationsAchse) {
		this.rotationsAchse = rotationsAchse;
		this.winkel = winkel;
	}

	@Override
	public void drawGl(GL2 gl) {
		// Remember current state of the render system
		gl.glPushMatrix();
		
		// Rotation
		if (matrix4 == null) {
			gl.glRotated(winkel, rotationsAchse.get(0), rotationsAchse.get(1), rotationsAchse.get(2));	
		}	else {
			gl.glMultMatrixd(matrix4, 0);
		}

		for (int childIndex = 0; childIndex < getNumberOfChildren(); childIndex++) {
			getChildNode(childIndex).drawGl(gl);
		}

		// Restore original state
		gl.glPopMatrix();
	}
	public Vector3 getRotationsAchse() {
		return rotationsAchse;
	}

	public void setRotationsAchse(Vector3 rotationsAchse) {
		this.rotationsAchse = rotationsAchse;
	}

	public double getWinkel() {
		return winkel;
	}

	public void setWinkel(double winkel) {
		this.winkel = winkel;
	}
	
	public void setMatrix(Matrix3 matrix){
		matrix4 = new double [] {	matrix.getRow(0).get(0), matrix.getRow(0).get(1), matrix.getRow(0).get(2), 0,
				matrix.getRow(1).get(0), matrix.getRow(1).get(1), matrix.getRow(1).get(2), 0,
				matrix.getRow(2).get(0), matrix.getRow(2).get(1), matrix.getRow(2).get(2), 0,
				0,							0,						0,						1};
	}
	
	public double[] getMatrix(){
		return this.matrix4;
	}
}