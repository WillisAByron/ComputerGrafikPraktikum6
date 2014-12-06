package computergraphics.scenegraph;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

public class SingleTriangleNode extends Node {

	private static final int NUMBER = 1;
	
	private boolean listExist = false;

	/** 
	 * Überschriebene Methode aus der Klasse Node, hier kann man bestimmen welches
	 * Objekt 'gezeichnet' wird.
	 */
	@Override
	public void drawGl(GL2 gl) {
		if(!listExist)
			gl = getList(gl, NUMBER);

		gl.glCallList(NUMBER);
		gl.glPushMatrix();
		gl.glFlush();
	}

	private GL2 getTriangleStandart(GL2 gl) {
		gl.glColor3f(0.75f, 0.25f, 0.25f);
		gl.glBegin(GL.GL_TRIANGLES);
		gl.glNormal3f(0, 0, 1);
		gl.glColor3d(1.0, 0.0, 0.0);
		gl.glVertex3f(-0.5f, -0.5f, 0);
		gl.glNormal3f(0, 0, 1);
		gl.glColor3d(0.0, 1.0, 0.0);
		gl.glVertex3f(0.5f, -0.5f, 0);
		gl.glNormal3f(0, 0, 1);
		gl.glColor3d(0.0, 0.0, 1.0);
		gl.glVertex3f(0, 0.5f, 0);
		gl.glEnd();
		return gl;
	}

	private GL2 getTriangleGreen(GL2 gl) {
		// Set color of Triangle to green!!
		gl.glColor3f(0.0f, 1.0f, 0.0f);

		gl.glBegin(GL.GL_TRIANGLES);
		gl.glNormal3f(0, 0, 1);
		gl.glVertex3f(-0.5f, -0.5f, 0);
		gl.glNormal3f(0, 0, 1);
		gl.glVertex3f(0.5f, -0.5f, 0);
		gl.glNormal3f(0, 0, 1);
		gl.glVertex3f(0, 0.5f, 0);
		gl.glEnd();
		return gl;
	}

	private GL2 getQuadsStandart(GL2 gl) {
		// Set color of Triangle to green!!
		gl.glColor3f(0.0f, 1.0f, 0.0f);

		gl.glBegin(GL2.GL_QUAD_STRIP);
		gl.glVertex3f(-.5f, .5f, 0);
		gl.glVertex3f(.5f, .5f, 0);
		gl.glVertex3f(-.5f, -.5f, 0);
		gl.glVertex3f(.5f, -.5f, 0);
		gl.glEnd();
		return gl;
	}

	private GL2 getList(GL2 gl, int number) {
		gl.glNewList(number, GL2.GL_COMPILE);
//		drawQuad(gl);
		drawTorus(gl, 4, 16);
		gl.glEndList();
		// Set color of Triangle to green!!
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		this.listExist = true;
		return gl;
	}

	private void drawTorus(GL2 gl, int numc, int numt) {
		double s, t, x, y, z, twopi = 2 * Math.PI;
		for (int i = 0; i < numc; i++) {
			gl.glBegin(GL2.GL_QUAD_STRIP);
			for (int j = 0; j <= numt; j++) {
				for (int k = 1; k >= 0; k--) {
					s = (i + k) % numc + 0.5;
					t = j % numt;
					x = (1 + 0.1 * Math.cos(s * twopi / numc)) * Math.cos(t * twopi / numt);
					y = (1 + 0.1 * Math.cos(s * twopi / numc)) * Math.sin(t * twopi / numt);
					z = 0.1 * Math.sin(s * twopi / numc);
					gl.glVertex3d(x, y, z);
				}// k
			}// j
			gl.glEnd();
		}// i
	}

	private void drawQuad(GL2 gl) {
		gl.glBegin(GL2.GL_QUAD_STRIP);
		gl.glVertex3f(-.5f, .5f, 0);
		gl.glVertex3f(.5f, .5f, 0);
		gl.glVertex3f(-.5f, -.5f, 0);
		gl.glVertex3f(.5f, -.5f, 0);
		gl.glEnd();
	}
}
