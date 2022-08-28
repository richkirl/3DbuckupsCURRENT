package water;

import java.io.IOException;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import entity.Camera;
import entity.Light;
import models.RawModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import toolbox.Maths;

public class WaterRenderer {
	private static final String DUDV_MAP = "src/res/resWater/waterDUDV.png";
	private static final String DUDV1_MAP = "src/res/resWater/waterDUDV1.png";
	private static final String NORMAL_MAP = "src/res/resWater/normalMap.png";
	private static final float WAVE_SPEED = 0.03f;
	private RawModel quad;
	private WaterShader shader;
	private WaterFrameBuffers fbos;
	private float moveFactor =0;
	private int dudvTexture;
	private int normalMap;
	public WaterRenderer(Loader loader,WaterShader shader, Matrix4f projectionMatrix,WaterFrameBuffers fbos) throws IOException {
		this.shader = shader;
		this.fbos = fbos;
		dudvTexture = loader.loadTexture(DUDV_MAP);
		normalMap = loader.loadTexture(NORMAL_MAP);
		shader.start();
		shader.connectTexture();
		shader.loadProjectionMatrix(projectionMatrix);
		
		shader.stop();
		setUpVAO(loader);
	}
	public void render(List<WaterTile> water, Camera camera,Light sun) {
		prepareRender(camera,sun);
		for(WaterTile tile: water) {
			Matrix4f modelMatrix = Maths.createTransformationMatrix(
					new Vector3f(tile.getX(),tile.getHeight(),tile.getZ()), 
					0, 
					0, 
					0, 
					WaterTile.TILE_SIZE);
					shader.loadModelMatrix(modelMatrix);
					GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
		unbind();
	}
	private void prepareRender(Camera camera, Light sun) {
		shader.start();
		shader.loadViewMatrix(camera);
		moveFactor += WAVE_SPEED * (DisplayManager.getFrameTimeSeconds()+Math.sin(0.002f));
		moveFactor %= 1;
		shader.loadMoveFactor(moveFactor);
		shader.loadLight(sun);
		GL30.glBindVertexArray(quad.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalMap);
	}
	private void unbind() {
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	private void setUpVAO(Loader loader) {
		float[] vertices = {-1,-1,-1,1,1,-1,1,-1,-1,1,1,1};
		quad = loader.loadToVAO(vertices,2);
	}
}
