package renderEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import models.RawModel;
import textures.TextureData;

public class Loader {
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();

	public RawModel loadToVAO(float[] positions, float[] textureCoords,float[] normals, int[] indices) {
		int vaoId = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoId, indices.length);
	}
	public RawModel loadToVAO(float[] positions, int dimensions) {
		int vaoID = createVAO();
		this.storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length / dimensions);
	}
	
	public int loadCubeMap(String[] textureFiles) {
		int texID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
		
		for (int i = 0; i < textureFiles.length; i++) {
			TextureData data = decodeTextureFile("src/res/resSkyBox/"+textureFiles[i]+".png");
			//System.out.println(textureFiles[i]+"\n"+data.getpWidth()+" "+data.getpHeight()+"\n"+data.getpBuffer().capacity());
			GL11.glTexImage2D(
					GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X+i, 
					0, 
					GL11.GL_RGBA, 
					data.getpWidth(), 
					data.getpHeight(), 
					0, 
					GL11.GL_RGBA, 
					GL11.GL_UNSIGNED_BYTE, 
					data.getpBuffer().flip());
			data.getpBuffer().clear();
		}
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP,GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP,GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		textures.add(texID);
		return texID;
	}
	private TextureData decodeTextureFile(String filename) {
		
		
		try (MemoryStack stack = MemoryStack.stackPush()){
			ByteBuffer buffer = null;
			IntBuffer pX = stack.mallocInt(1); // int*
			IntBuffer pY = stack.mallocInt(1); // int*
			IntBuffer pChannels = stack.mallocInt(1); // int*
			// Get the window size passed to glfwCreateWindow
			//buffer = ByteBuffer.allocateDirect(4*pX*pY);
			buffer = STBImage.stbi_load(filename, pX, pY, pChannels, 4);
			//buffer.flip();
			return new TextureData(pX.get(),pY.get(),buffer);
		}
		
	}
	
	public int loadTexture(String file) throws IOException {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			ByteBuffer texture = null;
			IntBuffer pX = stack.mallocInt(1); // int*
			IntBuffer pY = stack.mallocInt(1); // int*
			IntBuffer pChannels = stack.mallocInt(1); // int*
			// Get the window size passed to glfwCreateWindow
			texture = STBImage.stbi_load(file, pX, pY, pChannels, 0);
			// int texture;
			int textureID = GL11.glGenTextures();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
			
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);	
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, pX.get(), pY.get(), 0, GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE, texture);
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			//System.out.println(texture.getInt());
			textures.add(textureID);
			STBImage.stbi_image_free(texture);
			return textureID;
		}
	}

	public void cleanUp() {
		for (int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		for (int texture : textures) {
			GL15.glDeleteTextures(texture);
		}
	}

	private void unbindVAO() {
		// TODO Auto-generated method stub
		GL30.glBindVertexArray(0);
	}

	public void bindIndicesBuffer(int[] indices) {
		int vboId = GL15.glGenBuffers();
		vbos.add(vboId);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

	}

	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	private int createVAO() {
		int vaoId = GL30.glGenVertexArrays();
		vaos.add(vaoId);
		GL30.glBindVertexArray(vaoId);
		return vaoId;
	}

	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vboId = GL15.glGenBuffers();
		vbos.add(vboId);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}
