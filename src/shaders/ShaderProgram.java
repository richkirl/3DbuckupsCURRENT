package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public abstract class ShaderProgram {
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	public ShaderProgram(String vertexString,String fragmentString) {
		vertexShaderID = loadShader(vertexString, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentString, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAtrributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
	}
	protected abstract void getAllUniformLocations();
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	public void start() {
		GL20.glUseProgram(programID);
		
	}
	public void stop() {
		GL20.glUseProgram(0);
	}
	public void cleanUp() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	protected abstract void bindAtrributes();
	protected void bindAtrribute(int attribute, String variable) {
		GL20.glBindAttribLocation(programID, attribute, variable);
	}
	protected void loadInt(int location, int value) {
		GL20.glUniform1i(location, value);
	}
	protected void loadFloat(int location,float value) {
		GL20.glUniform1f(location, value);
	}
	protected void loadVector(int location,Vector3f value) {
		GL20.glUniform3f(location, value.x,value.y,value.z);
	}
	protected void loadVector(int location,Vector4f value) {
		GL20.glUniform4f(location, value.x,value.y,value.z,value.w);
	}
	protected void loadBoolean(int location,boolean value) {
		int toLoad =0;
		if(value) {
			toLoad=1;
		}
		GL20.glUniform1f(location, toLoad);
	}
	protected void loadMatrix(int location,Matrix4f value) {
		//value.add(matrixBuffer.get());
		//value.get(matrixBuffer);
		//matrixBuffer.flip();
		GL20.glUniformMatrix4fv(location, false, value.get(matrixBuffer));
	}
	private static int loadShader(String file,int type) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine())!=null) {
				shaderSource.append(line).append("\n");
				
			}
			reader.close();
		} catch(IOException e) {
			System.out.println("Couldnt read file!");
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS)==GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID,500));
			System.out.println("Couldnt compile shader!");
			System.exit(-1);
		}
		return shaderID;
		
	}
}
