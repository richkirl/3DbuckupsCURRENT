package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import entity.Camera;
import entity.Entity;
import entity.Light;
import models.TextureModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import skybox.SkyboxRenderer;
import terrain.Terrain;

public class MasterRenderer {
	private static final float FOV =70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	private Matrix4f projectionMatrix;
	private static final float RED = 0.5444f;
	private static final float GREEN = 0.62f;
	private static final float BLUE = 0.69f;
	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;
	
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	
	
	private Map<TextureModel,List<Entity>> entitys = new HashMap<TextureModel,List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	private SkyboxRenderer skyboxrenderer;
	public MasterRenderer(Loader loader) {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		createProjectionMatrix();
		renderer = new EntityRenderer(shader,projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader,projectionMatrix);
		skyboxrenderer = new SkyboxRenderer(loader, projectionMatrix);
	}
	
	public void render(Light sun, Camera camera,Vector4f clipPlane) {
		prepare();
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadSkyCol(RED, GREEN, BLUE);
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		renderer.render(entitys);
		shader.stop();
		terrainShader.start();
		terrainShader.loadClipPlane(clipPlane);
		terrainShader.loadSkyCol(RED, GREEN, BLUE);
		terrainShader.loadLight(sun);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		skyboxrenderer.render(camera);
		terrains.clear();
		entitys.clear();
	}
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	public void processEntity(Entity e) {
		TextureModel entityModel = e.getModel();
		List<Entity> batch = entitys.get(entityModel);
		if(batch !=null) {
			batch.add(e);
		}else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(e);
			entitys.put(entityModel, newBatch);
		}
	}
	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
	}
	public void prepare() {
		GL15.glEnable(GL15.GL_DEPTH_TEST);
		GL15.glClearColor(RED, GREEN, BLUE, 1.0f);
		GL15.glClear(GL15.GL_COLOR_BUFFER_BIT | GL15.GL_DEPTH_BUFFER_BIT); // clear the framebuffer
	}
	private void createProjectionMatrix() {
		try {
			float aspectRatio = (float)DisplayManager.getWIDTH()/DisplayManager.getHEIGHT();
			float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV/2f)))*aspectRatio);
			float x_scale = y_scale / aspectRatio;
			float frustum_leght=FAR_PLANE - NEAR_PLANE;
			projectionMatrix = new Matrix4f();
			projectionMatrix.m00(x_scale);
			projectionMatrix.m11(y_scale);
			projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE)/frustum_leght));
			projectionMatrix.m23(-1);
			projectionMatrix.m32(-((2*NEAR_PLANE*FAR_PLANE )/frustum_leght));
			projectionMatrix.m33(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public void setProjectionMatrix(Matrix4f projectionMatrix) {
		this.projectionMatrix = projectionMatrix;
	}
	
}
