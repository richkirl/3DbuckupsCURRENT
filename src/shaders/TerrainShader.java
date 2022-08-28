package shaders;

import org.joml.Matrix4f;
//import org.joml.Vector3f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import entity.Camera;
import entity.Light;
import toolbox.Maths;

public class TerrainShader extends ShaderProgram{

	private static final String VERTEX ="src/shaders/terrainVertex.glsl";
	private static final String FRAGMENT ="src/shaders/terrainFragment.glsl";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPos;
	private int location_lightCol;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyCol;
	private int location_backgroundTexture;
	private int location_rTexture;
	private int location_gTexture;
	private int location_bTexture;
	private int location_blendMap;
	private int location_plane;
	public TerrainShader() {
		super(VERTEX, FRAGMENT);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	protected void bindAtrributes() {
		// TODO Auto-generated method stub
		super.bindAtrribute(0, "position");
		super.bindAtrribute(1, "textureCoords");
		super.bindAtrribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		// TODO Auto-generated method stub
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_lightPos = super.getUniformLocation("lightPos");
		location_lightCol = super.getUniformLocation("lightCol");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_skyCol = super.getUniformLocation("skyCol");
		location_backgroundTexture = super.getUniformLocation("backgroundTexture");
		location_rTexture = super.getUniformLocation("rTexture");
		location_gTexture = super.getUniformLocation("gTexture");
		location_bTexture = super.getUniformLocation("bTexture");
		location_blendMap = super.getUniformLocation("blendMap");
		location_plane = super.getUniformLocation("plane");
	}
	public void connectTexturesUnits() {
		super.loadInt(location_backgroundTexture, 0);
		super.loadInt(location_rTexture, 1);
		super.loadInt(location_gTexture, 2);
		super.loadInt(location_bTexture, 3);
		super.loadInt(location_blendMap, 4);
	}
	public void loadClipPlane(Vector4f plane) {
		super.loadVector(location_plane, plane);
	}
	public void loadSkyCol(float r,float g,float b) {
		super.loadVector(location_skyCol, new Vector3f(r,g,b));
	}
	public void loadShineVariables(float damper,float reflectivity) {
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	public void loadLight(Light light) {
		super.loadVector(location_lightPos, light.getPosition());
		super.loadVector(location_lightCol, light.getColour());
	}
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.getViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}

}
