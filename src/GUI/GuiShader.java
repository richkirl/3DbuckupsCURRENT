package GUI;

import org.joml.Matrix4f;

import shaders.ShaderProgram;

public class GuiShader extends ShaderProgram {
	private static final String VERTEX_FILE="src/GUI/vertexGui.glsl";
	private static final String FRAGMENT_FILE="src/GUI/fragmentGui.glsl";
	private int location_transformationMatrix;
	private int location_color;
	public GuiShader() throws Exception {
		super(VERTEX_FILE, FRAGMENT_FILE);
		// TODO Auto-generated constructor stub
	}
	public void loadTransformation(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	@Override
	protected void getAllUniformLocations() {
		// TODO Auto-generated method stub
		location_transformationMatrix=super.getUniformLocation("transformationMatrix");
		location_color = super.getUniformLocation("vColor");
	}

	@Override
	protected void bindAtrributes() {
		// TODO Auto-generated method stub
		super.bindAtrribute(0, "position");
		//super.bindAttribute(1, "guiTexture");
	}

}
