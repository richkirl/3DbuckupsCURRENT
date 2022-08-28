package terrain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.joml.Vector2f;
import org.joml.Vector3f;

import models.RawModel;
import renderEngine.Loader;
import textures.ModelTexture;
import toolbox.Maths;

public class Terrain {
	private static final float SIZE = 800;
	//private static final int VERTEX_COUNT = 128;
	private static final float MAX_HEIGHT = 40;
	private static final float MAX_PIXEL_COLOUR = 256*256*256;
	
	private float[][] heights; 
	
	
	private float x;
	private float z;
	private RawModel model;
	//private ModelTexture texture;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	
	public Terrain(float x, float z, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap,String heightMap) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = x*SIZE;
		this.z = z*SIZE;
		
		this.model = generateTerrain(loader,heightMap);
	}
	private RawModel generateTerrain(Loader loader,String heightmap) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(heightmap));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int VERTEX_COUNT = image.getHeight();
		heights=new float[VERTEX_COUNT][VERTEX_COUNT];
		
		int count = VERTEX_COUNT*VERTEX_COUNT;
		float[] vertices = new float[count *3];
		float[] normals = new float[count *3];
		float[] textureCoords = new float[count *2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT*1)];
		int vertexPointer =0;
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				vertices[vertexPointer*3]=-(float)j/((float)VERTEX_COUNT-1)*SIZE;
				float height = getHeight(j, i, image);
				heights[j][i] = height;
				vertices[vertexPointer*3+1]=height-1;
				vertices[vertexPointer*3+2]=-(float)i/((float)VERTEX_COUNT-1)*SIZE;
				Vector3f normal = calculateNormal(j, i, image);
				normals[vertexPointer*3]=normal.x;
				normals[vertexPointer*3+1]=normal.y;
				normals[vertexPointer*3+2]=normal.z;
				textureCoords[vertexPointer*2]=-(float)j/((float)VERTEX_COUNT-1);
				textureCoords[vertexPointer*2+1]=(float)i/((float)VERTEX_COUNT-1);
			
				vertexPointer++;
			}
		}
		int pointer =0;
		for (int i = 0; i <VERTEX_COUNT-1; i++) {
			for (int j = 0; j < VERTEX_COUNT-1; j++) {
				int topLeft = (i*VERTEX_COUNT)+j;
				int topRight = topLeft+1;
				int bottomLeft = ((i+1)*VERTEX_COUNT)+j;
				int bottomRight = bottomLeft +1;
				indices[pointer++]=topLeft;
				indices[pointer++]=bottomLeft;
				indices[pointer++]=topRight;
				indices[pointer++]=topRight;
				indices[pointer++]=bottomLeft;
				indices[pointer++]=bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	public float getHeightOfTerrain(float worldX,float worldZ) {
		float terrainX = Math.abs(worldX-this.x);
		float terrainZ = Math.abs(worldZ-this.z);
		//System.out.println(terrainX+" "+terrainZ);
		float gridSquareSize = SIZE/((float)heights.length -1);
		int gridX=(int)Math.floor(terrainX/gridSquareSize);
		int gridZ=(int)Math.floor(terrainZ/gridSquareSize);
		if(gridX>=heights.length-1||gridZ>=heights.length-1||gridX<0||gridZ<0) {
			return 0;
		}
		float xCoord = (terrainX % gridSquareSize)/gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize)/gridSquareSize;
		float answer;
		if(xCoord <= (1-zCoord)) {
			answer = Maths.barryCentric(
						new Vector3f(0,heights[gridX][gridZ],0)
					, new Vector3f(1,heights[gridX+1][gridZ],0)
					, new Vector3f(0,heights[gridX][gridZ+1],1)
					, new Vector2f(xCoord,zCoord));
		}else {
			answer = Maths.barryCentric(
						new Vector3f(1,heights[gridX+1][gridZ],0)
					, new Vector3f(1,heights[gridX+1][gridZ+1],1)
					, new Vector3f(0,heights[gridX][gridZ+1],1)
					, new Vector2f(xCoord,zCoord));
		}
		return answer;
	}
	private float getHeight(int x, int z, BufferedImage image) {
		if(x<0||x>=image.getHeight()||z<0||z>=image.getHeight()) {
			return 0;
		}
		float height = image.getRGB(x, z);
		height += MAX_PIXEL_COLOUR/2f;
		height /= MAX_PIXEL_COLOUR/2f;
		height *= MAX_HEIGHT;
		return height;
	}
	private Vector3f calculateNormal(int x,int z,BufferedImage image) {
		float heightL = getHeight(x-1, z, image);
		float heightR = getHeight(x+1, z, image);
		float heightD = getHeight(x, z-1, image);
		float heightU = getHeight(x, z+1, image);
		Vector3f normal = new Vector3f(heightL-heightR,2f,heightD-heightU);
		normal.normalize();
		return normal;
		
	}
	private RawModel generateTerrain(Loader loader) {
		int VERTEX_COUNT =128;
		int count = VERTEX_COUNT*VERTEX_COUNT;
		float[] vertices = new float[count *3];
		float[] normals = new float[count *3];
		float[] textureCoords = new float[count *2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer =0;
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				vertices[vertexPointer*3]=-(float)j/((float)VERTEX_COUNT-1)*SIZE;
				vertices[vertexPointer*3+1]=0;
				vertices[vertexPointer*3+2]=-(float)i/((float)VERTEX_COUNT-1)*SIZE;
				normals[vertexPointer*3]=0;
				normals[vertexPointer*3+1]=1;
				normals[vertexPointer*3+2]=0;
				textureCoords[vertexPointer*2]=-(float)j/((float)VERTEX_COUNT-1);
				textureCoords[vertexPointer*2+1]=(float)i/((float)VERTEX_COUNT-1);
			
				vertexPointer++;
			}
		}
		int pointer =0;
		for (int i = 0; i <VERTEX_COUNT-1; i++) {
			for (int j = 0; j < VERTEX_COUNT-1; j++) {
				int topLeft = (i*VERTEX_COUNT)+j;
				int topRight = topLeft+1;
				int bottomLeft = ((i+1)*VERTEX_COUNT)+j;
				int bottomRight = bottomLeft +1;
				indices[pointer++]=topLeft;
				indices[pointer++]=bottomLeft;
				indices[pointer++]=topRight;
				indices[pointer++]=topRight;
				indices[pointer++]=bottomLeft;
				indices[pointer++]=bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getZ() {
		return z;
	}
	public void setZ(float z) {
		this.z = z;
	}
	public RawModel getModel() {
		return model;
	}
	public void setModel(RawModel model) {
		this.model = model;
	}
//	public ModelTexture getTexture() {
//		return texture;
//	}
//	public void setTexture(ModelTexture texture) {
//		this.texture = texture;
//	}
	public static float getSize() {
		return SIZE;
	}

	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}
	public void setTexturePack(TerrainTexturePack texturePack) {
		this.texturePack = texturePack;
	}
	public TerrainTexture getBlendMap() {
		return blendMap;
	}
	public void setBlendMap(TerrainTexture blendMap) {
		this.blendMap = blendMap;
	}
	
}
