package computergraphics.scenegraph;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLException;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import computergraphics.datastructures.Triangle;
import computergraphics.datastructures.TriangleMesh;
import computergraphics.math.Vector3;

public class TriangleMeshNode extends Node {

	private int number;

	private boolean listExist = false;

	private TriangleMesh triangleMesh;

	private boolean texture = false;

	public TriangleMeshNode(TriangleMesh triangleMesh, boolean texture) {
		this.triangleMesh = triangleMesh;
		this.texture = texture;
		this.number = 1;
	}

	public TriangleMeshNode(TriangleMesh triangleMesh, boolean texture, int number){
		this(triangleMesh, texture);
		this.number = number;
	}
	
	public TriangleMeshNode(TriangleMesh triangleMesh) {
		this(triangleMesh, false);
	}

	@Override
	public void drawGl(GL2 gl) {
		if (!listExist)
			gl = getList(gl, number);

		gl.glCallList(number);
		gl.glFlush();
	}

	private GL2 getList(GL2 gl, int number) {
		gl.glNewList(number, GL2.GL_COMPILE);
		drawObject(gl);
		gl.glEndList();
		// Set color of Triangle to green!!
		// gl.glColor3f(0.0f, 0.7f, 0.2f);
		this.listExist = true;
		return gl;
	}

	private void drawObject(GL2 gl) {
		if (texture) {
			InputStream is;
			Texture tex = null;
			try {
				is = new FileInputStream(triangleMesh.getTextureFilename());
				tex = TextureIO.newTexture(is, false, "png");
			} catch (GLException | IOException e) {
			}
			tex.enable(gl);
			tex.bind(gl);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
			gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
			Vector3 vector;
			for (Triangle triangle : triangleMesh.getTriangleList()) {
				gl.glBegin(GL.GL_TRIANGLES);
				gl.glNormal3dv(triangle.getNormal().data(), 0);
				vector = triangleMesh.getTextureCoordinate(triangle.getTextureCoordinate(0));
				gl.glTexCoord2d(vector.get(0), vector.get(1));
				gl.glVertex3dv(triangleMesh.getVertex(triangle.getA()).getPosition().data(), 0);
				vector = triangleMesh.getTextureCoordinate(triangle.getTextureCoordinate(1));
				gl.glTexCoord2d(vector.get(0), vector.get(1));
				gl.glVertex3dv(triangleMesh.getVertex(triangle.getB()).getPosition().data(), 0);
				vector = triangleMesh.getTextureCoordinate(triangle.getTextureCoordinate(2));
				gl.glTexCoord2d(vector.get(0), vector.get(1));
				gl.glVertex3dv(triangleMesh.getVertex(triangle.getC()).getPosition().data(), 0);
				gl.glEnd();
			}
		} else {
			for (Triangle triangle : triangleMesh.getTriangleList()) {
				gl.glBegin(GL.GL_TRIANGLES);
				gl.glNormal3dv(triangle.getNormal().data(), 0);
				gl.glColor3dv(triangleMesh.getVertex(triangle.getA()).getColor().data(), 0);
				gl.glVertex3dv(triangleMesh.getVertex(triangle.getA()).getPosition().data(), 0);
				gl.glColor3dv(triangleMesh.getVertex(triangle.getB()).getColor().data(), 0);
				gl.glVertex3dv(triangleMesh.getVertex(triangle.getB()).getPosition().data(), 0);
				gl.glColor3dv(triangleMesh.getVertex(triangle.getC()).getColor().data(), 0);
				gl.glVertex3dv(triangleMesh.getVertex(triangle.getC()).getPosition().data(), 0);
				gl.glEnd();
			}
		}
	}

}
