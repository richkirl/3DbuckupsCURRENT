package renderEngine;



import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import entity.Entity;
import models.RawModel;
import models.TextureModel;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;


public class EntityRenderer {
//	private static final float FOV =70;
//	private static final float NEAR_PLANE = 0.1f;
//	private static final float FAR_PLANE = 1000;
//	private static final float RED = 0.0f;
//	private static final float GREEN = 0.0f;
//	private static final float BLUE = 0.0f;
//	private Matrix4f projectionMatrix;
	
	private StaticShader shader;
	public EntityRenderer(StaticShader shader,Matrix4f projectionMatrix) {
		this.shader = shader;
//		GL11.glEnable(GL11.GL_CULL_FACE);
//		GL11.glCullFace(GL11.GL_BACK);
//		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		
	}
	public void render (Map<TextureModel,List<Entity>> entitys) {
		for (TextureModel model : entitys.keySet()) {
			prepareTextureModel(model);
			List<Entity> batch = entitys.get(model);
			for(Entity e:batch) {
				prepareInstance(e);
				GL11.glDrawElements(GL11.GL_TRIANGLES, 
						model.getRawModel().getVertexCount(),
						GL11.GL_UNSIGNED_INT,0);
			}
			unbindTextureModel();
		}
	}
	private void prepareTextureModel(TextureModel model) {
		RawModel rawmodel = model.getRawModel();
		GL30.glBindVertexArray(rawmodel.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = model.getTexture();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_2D, model.getTexture().getID());
	}
	private void unbindTextureModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		
		GL30.glBindVertexArray(0);
	}
	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(),entity.getRotY(),entity.getRotZ(),entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}
	
//	public void render(Entity entity, StaticShader shader) {
//		TextureModel textureModel = entity.getModel();
//		RawModel model = textureModel.getRawModel();
//		GL30.glBindVertexArray(model.getVaoId());
//		GL20.glEnableVertexAttribArray(0);
//		GL20.glEnableVertexAttribArray(1);
//		GL20.glEnableVertexAttribArray(2);
//		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(),entity.getRotY(),entity.getRotZ(),entity.getScale());
//		shader.loadTransformationMatrix(transformationMatrix);
//		ModelTexture texture = textureModel.getTexture();
//		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
//		GL13.glActiveTexture(GL13.GL_TEXTURE0);
//		GL11.glBindTexture(GL13.GL_TEXTURE_2D, textureModel.getTexture().getID());
//		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(),GL11.GL_UNSIGNED_INT,0);
//		
//		GL20.glDisableVertexAttribArray(0);
//		GL20.glDisableVertexAttribArray(1);
//		GL20.glDisableVertexAttribArray(2);
//		
//		GL30.glBindVertexArray(0);
//	}
//	private void createProjectionMatrix() {
//		try {
//			float aspectRatio = (float)DisplayManager.getWIDTH()/DisplayManager.getHEIGHT();
//			float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV/2f)))*aspectRatio);
//			float x_scale = y_scale / aspectRatio;
//			float frustum_leght=FAR_PLANE - NEAR_PLANE;
//			projectionMatrix = new Matrix4f();
//			projectionMatrix.m00(x_scale);
//			projectionMatrix.m11(y_scale);
//			projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE)/frustum_leght));
//			projectionMatrix.m23(-1);
//			projectionMatrix.m32(-((2*NEAR_PLANE*FAR_PLANE )/frustum_leght));
//			projectionMatrix.m33(0);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
