package engineTester;


import java.util.ArrayList;
import java.util.List;



//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;


import entity.Camera;
import entity.Entity;
import entity.Light;
import entity.Player;
import models.RawModel;
import models.TextureModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJloader;
//import renderEngine.Renderer;
//import shaders.StaticShader;
import terrain.Terrain;
import terrain.TerrainTexture;
import terrain.TerrainTexturePack;
import textures.ModelTexture;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

public class MainLoop implements Runnable {
	private DisplayManager displayManager;
	private TerrainTexture backgroundTexture;
	private TerrainTexture rTexture, gTexture, bTexture;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	private Loader loader;
	private Loader loader1;
	private RawModel rawmodel;
	private ModelTexture texture;
	private TextureModel textureModel;
	private Light light;
	private Terrain terrain;
	private Player player;
	private Camera camera;
	private MasterRenderer masterRenderer;
	private Thread gameThread;
	public MainLoop() throws Exception {
		displayManager = new DisplayManager(1920, 1080);
		displayManager.createDisplay();
		loader = new Loader();
		
		backgroundTexture = new TerrainTexture(loader.loadTexture("src/res/img1.png"));
		rTexture = new TerrainTexture(loader.loadTexture("src/res/resTerrain/dirt.png"));
		gTexture = new TerrainTexture(loader.loadTexture("src/res/resTerrain/pinkFlowers.png"));
		bTexture = new TerrainTexture(loader.loadTexture("src/res/resTerrain/path.png"));
		texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		blendMap = new TerrainTexture(loader.loadTexture("src/res/resTerrain/blendMap.png"));
		
		rawmodel = OBJloader.loadObjModel("5", loader);
		texture = new ModelTexture(loader.loadTexture("src/res/img1.png"));
		textureModel = new TextureModel(rawmodel, texture);
		
		light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));
		
		
		
		
		terrain = new Terrain(0, 0, loader, texturePack, blendMap,new String("src/res/resTerrain/heightmap.png"));

		player = new Player(textureModel, new Vector3f(389f, 0f, -220f), 0, 0, 0, 1);
		camera = new Camera(player);
		masterRenderer = new MasterRenderer(loader);
		//init();
		gameThread = new Thread(this);
		//gameThread.start();
		run();
	}
	public void init() throws Exception {
		
		//loop();
		//startGameLoop();
	}

//	private void startGameLoop() {
//		gameThread = new Thread(this);
//		gameThread.start();
//	}
	public void loop() throws Exception {
		
		WaterFrameBuffers fbos = new WaterFrameBuffers();
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader,waterShader,masterRenderer.getProjectionMatrix(),fbos);
		List<WaterTile> waters = new ArrayList<WaterTile>();
		waters.add(new WaterTile(-75,-75,-2));
		
		RawModel crabby = OBJloader.loadObjModel("EnemyAnimation5", loader);
		ModelTexture texture1 = new ModelTexture(loader.loadTexture("src/res/img1.png"));
		TextureModel textureModel1 = new TextureModel(crabby, texture1);
		Entity crab = new Entity(textureModel1, new Vector3f(389f,0f,-250f), 0, 0, 0, 20);
		
		while (!GLFW.glfwWindowShouldClose(displayManager.getWindow())) {
			
			
			
			player.move(displayManager,terrain);
			camera.move(displayManager);
			
			GL30.glEnable(GL30.GL_CLIP_DISTANCE0);
			fbos.bindReflectionFrameBuffers();
			float distance = 2*(camera.getPosition().y -15);
			camera.getPosition().y-=distance;
			camera.setPitch(camera.getPitch()*(-1.0f));
			masterRenderer.processEntity(player);
			masterRenderer.processTerrain(terrain);
			//masterRenderer.processEntity(crab);
			masterRenderer.render(light, camera,new Vector4f(0,1,0,-15f));
			camera.getPosition().y += distance;
			camera.setPitch(camera.getPitch()*(-1.0f));
			fbos.bindRefractionFrameBuffer();
			
			masterRenderer.processEntity(player);
			masterRenderer.processTerrain(terrain);
			//masterRenderer.processEntity(crab);
			masterRenderer.render(light, camera,new Vector4f(0,-1,0,15f));
			GL30.glDisable(GL30.GL_CLIP_DISTANCE0);
			
			fbos.unbindCurrentFrameBuffer();
			masterRenderer.processEntity(player);
			masterRenderer.processTerrain(terrain);
			masterRenderer.processEntity(crab);
			masterRenderer.render(light, camera,new Vector4f(0,-1,0,100000));
			waterRenderer.render(waters, camera,light);
			
			
			//System.out.println(player.getPosition().x+" "+player.getPosition().y+" "+player.getPosition().z);
			//System.out.println(terrain.getHeightOfTerrain(player.getPosition().x, player.getPosition().z));
			//System.out.println(player.getTerrainHeight());
			displayManager.swappoll();
		}
		fbos.cleanUp();
		//guiShader.cleanUp();
		waterShader.cleanUp();
		masterRenderer.cleanUp();
		loader.cleanUp();
		displayManager.closeDisplay();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			loop();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
