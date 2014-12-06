package computergraphics.terrain;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import computergraphics.datastructures.Triangle;
import computergraphics.datastructures.TriangleMesh;
import computergraphics.datastructures.Vertex;
import computergraphics.math.Vector3;

public class GenerateTerrain {

	public String colorFile;

	public static final double MAX_X = 1;

	public static final double MAX_Y = 0.1;

	public static final double PRECISION = 0.02;

	public static final double MAX_Z = 1;

	public static final double STEP = 0.005;

	public static final int SMOOTH_FACTOR = 100;

	public static final int ITERATIONS = 10000;

	/**
	 * @return
	 * @throws IOException
	 */
	public TriangleMesh generateGround(double maxX, double maxY, double maxZ, double step, String heightField) throws IOException {
		TriangleMesh trMesh = new TriangleMesh();
		BufferedImage bImage = ImageIO.read(new File(heightField));
		final double maxStepsX = maxX / step;
		final double maxStepsZ = maxZ / step;
		for (double x = 0; x <= maxX - step; x += step) {
			for (double z = 0; z <= maxZ - step; z += step) {
				int a = trMesh.addVertex(new Vertex(new Vector3(x, getHeight(bImage, x, z, maxStepsX, maxStepsZ), z)));
				int b = trMesh.addVertex(new Vertex(new Vector3(x + step, getHeight(bImage, x + step, z, maxStepsX,
						maxStepsZ), z)));
				int c = trMesh.addVertex(new Vertex(new Vector3(x,
						getHeight(bImage, x, z + step, maxStepsX, maxStepsZ), z + step)));
				trMesh.addTriangle(new Triangle(a, b, c));
				int aOppeside = trMesh.addVertex(new Vertex(new Vector3(x + step, getHeight(bImage, x + step, z + step,
						maxStepsX, maxStepsZ), z + step)));
				int bOppeside = trMesh.addVertex(new Vertex(new Vector3(x + step, getHeight(bImage, x + step, z,
						maxStepsX, maxStepsZ), z)));
				int cOppeside = trMesh.addVertex(new Vertex(new Vector3(x, getHeight(bImage, x, z + step, maxStepsX,
						maxStepsZ), z + step)));
				trMesh.addTriangle(new Triangle(aOppeside, bOppeside, cOppeside));
			}
		}
		trMesh.updateNormals();
		if (colorFile != null) {
			trMesh.updateColor(ImageIO.read(new File(colorFile)), maxStepsX, maxStepsZ, STEP);
		}
		return trMesh;
	}
	
	public TriangleMesh generateGround(double maxX, double maxY, double maxZ, double step, String heightField, String colorFile) throws IOException{
		this.colorFile = colorFile;
		return generateGround(maxX, maxY, maxZ, step, heightField);
	}
	
	public TriangleMesh generateGroundWithTexture(double maxX, double maxY, double maxZ, double step, String heightField, String textureFilrName) throws IOException {
		//TODO
		TriangleMesh trMesh = new TriangleMesh();
		BufferedImage bImage = ImageIO.read(new File(heightField));
		final double maxStepsX = maxX / step;
		final double maxStepsZ = maxZ / step;
		for (double x = 0; x <= maxX - step; x += step) {
			for (double z = 0; z <= maxZ - step; z += step) {
				int a = trMesh.addVertex(new Vertex(new Vector3(x, getHeight(bImage, x, z, maxStepsX, maxStepsZ), z)));
				int b = trMesh.addVertex(new Vertex(new Vector3(x + step, getHeight(bImage, x + step, z, maxStepsX,
						maxStepsZ), z)));
				int c = trMesh.addVertex(new Vertex(new Vector3(x,
						getHeight(bImage, x, z + step, maxStepsX, maxStepsZ), z + step)));
				trMesh.addTriangle(new Triangle(a, b, c));
				int aOppeside = trMesh.addVertex(new Vertex(new Vector3(x + step, getHeight(bImage, x + step, z + step,
						maxStepsX, maxStepsZ), z + step)));
				int bOppeside = trMesh.addVertex(new Vertex(new Vector3(x + step, getHeight(bImage, x + step, z,
						maxStepsX, maxStepsZ), z)));
				int cOppeside = trMesh.addVertex(new Vertex(new Vector3(x, getHeight(bImage, x, z + step, maxStepsX,
						maxStepsZ), z + step)));
				trMesh.addTriangle(new Triangle(aOppeside, bOppeside, cOppeside));
			}
		}
		trMesh.updateNormals();
		if (colorFile != null) {
			trMesh.updateColor(ImageIO.read(new File(colorFile)), maxStepsX, maxStepsZ, STEP);
		}
		return trMesh;
	}

	private double getHeight(BufferedImage bImage, double x, double z, double maxStepsX, double maxStepsZ) {
		final double pictureX = (bImage.getWidth(null) / maxStepsX) * (x / STEP);
		final double pictureZ = (bImage.getHeight(null) / maxStepsZ) * (z / STEP);
		final double height = (new Color(bImage.getRGB((int) pictureX, (int) pictureZ)).getRed() / 255.0) * MAX_Y;
		return height;
	}

	public TriangleMesh generateRandomGround(double maxX, double maxY, double maxZ, double step, double precision)
			throws IOException {
		TriangleMesh trMesh = new TriangleMesh();
		double[][] heightMap = generateMap(maxX, maxZ, step, maxY, precision, ITERATIONS);
		for (double x = 0; x <= maxX - step; x += step) {
			for (double z = 0; z <= maxZ - step; z += step) {
				int a = trMesh.addVertex(new Vertex(new Vector3(x, heightMap[(int) (x / step)][(int) (z / step)], z)));
				int b = trMesh.addVertex(new Vertex(new Vector3(x + step,
						heightMap[(int) ((x + step) / step)][(int) (z / step)], z)));
				int c = trMesh.addVertex(new Vertex(new Vector3(x,
						heightMap[(int) (x / step)][(int) ((z + step) / step)], z + step)));
				trMesh.addTriangle(new Triangle(a, b, c));
				int aOppeside = trMesh.addVertex(new Vertex(new Vector3(x + step,
						heightMap[(int) ((x + step) / step)][(int) ((z + step) / step)], z + step)));
				int bOppeside = trMesh.addVertex(new Vertex(new Vector3(x + step,
						heightMap[(int) ((x + step) / step)][(int) (z / step)], z)));
				int cOppeside = trMesh.addVertex(new Vertex(new Vector3(x,
						heightMap[(int) (x / step)][(int) ((z + step) / step)], z + step)));
				trMesh.addTriangle(new Triangle(aOppeside, bOppeside, cOppeside));
			}
		}
		trMesh.updateNormals();
		return trMesh;
	}

	private double[][] generateMap(double maxX, double maxZ, double step, double maxY, double precision, int iterations) {
		int xArray = (int) (maxX / step);
		int zArray = (int) (maxZ / step);
		double[][] heightMap = initMap(xArray, zArray, maxY);
		for (int i = 0; i < iterations; i++) {
			int[] abc = calculateLineEquation((int) (Math.random() * xArray), (int) (Math.random() * zArray),
					(int) (Math.random() * xArray), (int) (Math.random() * zArray));
			for (int x = 0; x < xArray; x++) {
				for (int z = 0; z < zArray; z++) {
					int temp = abc[0] * x + abc[1] * z + abc[2];
					if (temp >= 0) {
						heightMap[x][z] += precision;
					} else {
						heightMap[x][z] -= precision;
					}
				}
			}
			precision /= 1.02;
		}
		System.out.println("Prezision: " + precision);
		heightMap = smoothMap(heightMap, SMOOTH_FACTOR, xArray, zArray);
		return heightMap;
	}

	private double[][] smoothMap(double[][] heightMap, int smoothFactor, int xArray, int zArray) {
		for (int i = 0; i < smoothFactor; i++) {
			for (int x = 0; x < xArray; x++) {
				for (int z = 0; z < zArray; z++) {
					heightMap[x][z] = smoothHeight(heightMap, x, z, xArray - 1, zArray - 1);
				}
			}
		}
		return heightMap;
	}

	private double smoothHeight(double[][] heightMap, int x, int z, int xArray, int zArray) {
		double temp = 0;
		int counter = 0;
		if (z != 0){
			temp += heightMap[x][z - 1];
			counter++;
		}
		if (x != xArray && z != 0){
			temp += heightMap[x + 1][z - 1];
			counter++;
		}
		if (x != xArray){
			temp += heightMap[x + 1][z];
			counter++;
		}
		if (z != zArray){
			temp += heightMap[x][z + 1];
			counter++;
		}
		if (x != 0 && z != zArray){
			temp += heightMap[x - 1][z + 1];
			counter++;
		}
		if (x != 0){
			temp += heightMap[x - 1][z];
			counter++;
		}
		temp /= counter;
		temp += heightMap[x][z];
		temp /= 2;
		return temp;
	}

	private double[][] initMap(int xArray, int zArray, double maxY) {
		double[][] mapInit = new double[xArray][zArray];
		double middleY = maxY / 2;
		for (int x = 0; x < xArray; x++) {
			for (int z = 0; z < zArray; z++) {
				mapInit[x][z] = middleY;
			}
		}
		return mapInit;
	}

	private int[] calculateLineEquation(int x1, int z1, int x2, int z2) {
		int[] abc = { 0, 0, 0 };
		abc[0] = (z2 - z1);
		abc[1] = -(x2 - x1);
		abc[2] = -x1 * (z2 - z1) + z1 * (x2 - x1);
		return abc;
	}

}
