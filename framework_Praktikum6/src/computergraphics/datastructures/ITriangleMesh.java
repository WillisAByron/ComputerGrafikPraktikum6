/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package computergraphics.datastructures;

import computergraphics.math.Vector3;

/**
 * This interface describes the valid operations for a triangle mesh structure.
 * 
 * @author Philipp Jenke
 * 
 */
public interface ITriangleMesh {

    /**
     * Add a new triangle to the mesh with the vertex indices a, b, c. The index
     * of the first vertex is 0.
     * 
     * @param t
     *            Container object to represent a triangle.
     */
    public void addTriangle(Triangle t);

    /**
     * Add a new vertex to the vertex list. The new vertex is appended to the
     * end of the list.
     * 
     * @param v
     *            Vertex to be added.
     * 
     * @return Index of the vertex in the vertex list.
     */
    public int addVertex(Vertex v);

    /**
     * Getter.
     * 
     * @return Number of triangles in the mesh.
     */
    public int getNumberOfTriangles();

    /**
     * Getter.
     * 
     * @return Number of vertices in the triangle mesh.
     */
    public int getNumberOfVertices();

    /**
     * Getter.
     * 
     * @param index
     *            Index of the triangle to be accessed.
     * @return Triangle at the given index.
     */
    public Triangle getTriangle(int index);

    /**
     * Getter
     * 
     * @param index
     *            Index of the vertex to be accessed.
     * @return Vertex at the given index.
     */
    public Vertex getVertex(int index);

    /**
     * Clear mesh - remove all triangles and vertices.
     */
    public void clear();
    
    /**
	 * Setzen des Textur-Dateinamens. Ist der String null, dann ist keine Textur
	 * gesetzt.
	 */
	public void setTextureFilename(String filename );

	/**
	 * Zugriff auf den Namen der Texture-Datei. Liefert null, wenn keine Textur
	 * gesetzt ist.
	 */
	public String getTextureFilename();

	/**
	 * Hinzuf�gen einer Texture-Koordinate (u, v, -).
	 */
	public void addTextureCoordinate(Vector3 texCoord);

	/**
	 * Hinzuf�gen einer Texture-Koordinate (u, v, -).
	 * Gibt den Index aus der Texturkoordinatenliste zur�ck.
	 */
	public int addTextureCoordinateMR(Vector3 texCoord);
	
	/**
	 * Zugriff auf eine Texturkoordinate (u, v, -).
	 * @param index
	 * @return
	 */
	public Vector3 getTextureCoordinate(int index);
}
