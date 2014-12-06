package computergraphics.scenegraph;

import javax.media.opengl.GL2;

import computergraphics.math.Vector3;

/**
 * @author Johann Bronsch, Glenn Bickel
 *
 *         Node zur translation von Knoten.
 */
public class TranslationsNode extends Node {

	private Vector3 translationsVector;

	/**
	 * Konstruktor
	 * 
	 * @param translationsVector
	 *            = Tranlationsvektor
	 */
	public TranslationsNode(Vector3 translationsVector) {
		this.translationsVector = translationsVector;
	}

	@Override
	public void drawGl(GL2 gl) {
		// Remember current state of the render system
		gl.glPushMatrix();

		gl.glTranslatef((float) translationsVector.get(0), (float) translationsVector.get(1),
				(float) translationsVector.get(2));

		for (int childIndex = 0; childIndex < getNumberOfChildren(); childIndex++) {
			getChildNode(childIndex).drawGl(gl);
		}

		// Restore original state
		gl.glPopMatrix();
	}
	public Vector3 getTranslationsVector() {
		return translationsVector;
	}

	public void setTranslationsVector(Vector3 translationsVector) {
		this.translationsVector = translationsVector;
	}
}