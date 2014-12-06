package computergraphics.datastructures;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import jogamp.opengl.glu.mipmap.Image;

import com.jogamp.opengl.util.texture.Texture;
import computergraphics.math.Vector3;

public class TriangleMesh implements ITriangleMesh {

	private List<Triangle> triangleList = new ArrayList<>();

	private List<Vertex> vertexList = new ArrayList<>();
	
	private List<Vector3> textureCoordinates = new ArrayList<>();

	private Image colorMap;
	
	private Image heighField;
	
	private String textureFileName;
	
	private Texture texture;
	
	@Override
	public void addTriangle(Triangle t) {
		triangleList.add(t);
	}

	
//	//Fügt nur Vertices hinzu, die noch nicht in der Liste sind
//	@Override
//	public int addVertex(Vertex v) {
//		int returnValue = -1;
//		int counter = 0;
//		for (Vertex vertex : vertexList) {
//			if (compareVertex(vertex, v))
//				returnValue = counter;
//			counter++;
//		}
//		if (returnValue == -1)
//			vertexList.add(v);
//		return returnValue > -1? returnValue : vertexList.size() - 1;
//	}
//
//	private boolean compareVertex(Vertex v, Vertex u) {
//		return (v.getPosition().data()[0] == u.getPosition().data()[0]
//				&& v.getPosition().data()[1] == u.getPosition().data()[1] && v.getPosition().data()[2] == u
//				.getPosition().data()[2]);
//	}
	
	@Override
	public int addVertex(Vertex v) {
		vertexList.add(v);
		return vertexList.size() - 1;
	}
	
	@Override
	public int getNumberOfTriangles() {
		return triangleList.size();
	}

	@Override
	public int getNumberOfVertices() {
		return vertexList.size();
	}

	@Override
	public Triangle getTriangle(int index) {
		return triangleList.get(index);
	}

	@Override
	public Vertex getVertex(int index) {
		return vertexList.get(index);
	}

	@Override
	public void clear() {
		vertexList.clear();
		triangleList.clear();
	}

	public List<Triangle> getTriangleList() {
		return this.triangleList;
	}

	public void updateNormals() {
		for (Triangle triangle : triangleList) {
			calculateNormal(triangle);
			//showNormalVector(triangle);
		}
	}
	
	private void calculateNormal(Triangle triangle) {
		final Vector3 vectorU = subtrationOfVertecies(vertexList.get(triangle.getB()), vertexList.get(triangle.getA()));
		final Vector3 vectorV = subtrationOfVertecies(vertexList.get(triangle.getC()), vertexList.get(triangle.getA()));
		final Vector3 normal = crossProduct(vectorU, vectorV);
		final Vector3 normalized = normal.getNormalized();
		triangle.setNormal(normalized);
	}

	private Vector3 crossProduct(Vector3 vectorU, Vector3 vectorV) {
		return vectorU.cross(vectorV);
	}

	private Vector3 subtrationOfVertecies(Vertex vertexA, Vertex vertexB) {
		Vector3 subtract = vertexA.getPosition().subtract(vertexB.getPosition());
		return subtract;
	}

	public List<Vertex> getVertexList() {
		return vertexList;
	}

	public void setVertexList(List<Vertex> vertexList) {
		this.vertexList = vertexList;
	}

	public Image getColorMap() {
		return colorMap;
	}

	public void setColorMap(Image colorMap) {
		this.colorMap = colorMap;
	}

	public Image getHeighField() {
		return heighField;
	}

	public void setHeighField(Image heighField) {
		this.heighField = heighField;
	}

	public void setTriangleList(List<Triangle> triangleList) {
		this.triangleList = triangleList;
	}
	
	public void updateColor(BufferedImage bImage, double maxStepsX, double maxStepsZ, double step){
		for (Vertex vertex : vertexList) {
			final double[] position = vertex.getPosition().data();
			final double pictureX = (bImage.getWidth(null) / maxStepsX) * (position[0] / step);
			final double pictureZ = (bImage.getHeight(null) / maxStepsZ) * (position[2] / step);
			final Color color = new Color(bImage.getRGB((int) pictureX, (int) pictureZ));
			final Vector3 colorVector = new Vector3(color.getRed() / 255.0, color.getGreen() / 255.0, color.getBlue() / 255.0);
			vertex.setColor(colorVector);
		}
		
	}

	@Override
	public void setTextureFilename(String filename) {
		textureFileName = filename;
	}

	@Override
	public String getTextureFilename() {
		return textureFileName;
	}

	@Override
	public void addTextureCoordinate(Vector3 texCoord) {
		textureCoordinates.add(texCoord);
	}

	@Override
	public Vector3 getTextureCoordinate(int index) {
		return textureCoordinates.get(index);
	}


	public Texture getTexture() {
		return texture;
	}
}