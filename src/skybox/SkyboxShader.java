package skybox;

import org.joml.Matrix4f;

import entity.Camera;
import shaders.ShaderProgram;
import toolbox.Maths;

public class SkyboxShader extends ShaderProgram{
	private static final String VERTEX ="src/skybox/skyboxVertex.glsl";
	private static final String FRAGMENT ="src/skybox/skyboxFragment.glsl";
	private int location_projectionMatrix;
	private int location_viewMatrix;
	public SkyboxShader() {
		super(VERTEX, FRAGMENT);
		// TODO Auto-generated constructor stub
	}
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	public void loadViewMatrix(Camera camera) {
		Matrix4f matrix = Maths.getViewMatrix(camera);
		matrix.m30(0);
		matrix.m31(0);
		matrix.m32(0);		
		super.loadMatrix(location_viewMatrix, matrix);
	}
	@Override
	protected void getAllUniformLocations() {
		// TODO Auto-generated method stub
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	}

	@Override
	protected void bindAtrributes() {
		// TODO Auto-generated method stub
		super.bindAtrribute(0, "position");
	}

}
