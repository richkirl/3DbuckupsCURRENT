package entity;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import models.TextureModel;
import renderEngine.DisplayManager;
import terrain.Terrain;

public class Player extends Entity{
	private static final float RUN_SPEED = 20f;
	private static final float TURN_SPEED = 160f;
	private static final float GRAVITY = -50f;
	private static final float JUMP_POWER =-10f;
	private static float TERRAIN_HEIGHT = 0;
	private float upwardsSpeed = 0;
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	//private Vector3f position = new Vector3f(0,5,20);
	public long window;
	private TextureModel model;
    //private TextTexture textr;
	private Vector3f position;
	private float rotX,rotY,rotZ;
	private float scale;
	private boolean isInAir = false;
	
	public Player(TextureModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		// TODO Auto-generated constructor stub
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	public void move(DisplayManager w,Terrain terrain) {
		//checkInputs(w);
		this.window = w.getWindow();
		
		super.increaseRotation(0,currentSpeed* DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance*Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance*Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		upwardsSpeed+=GRAVITY*DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, upwardsSpeed*DisplayManager.getFrameTimeSeconds(), 0);
		//super.increasePosition(0.2f, 0, 0.2f);
		TERRAIN_HEIGHT = terrain.getHeightOfTerrain(super.getPosition().x,super.getPosition().z);
		if(position.y<TERRAIN_HEIGHT) {
			
			upwardsSpeed = 0;
			isInAir=true;
			super.getPosition().y=TERRAIN_HEIGHT;
			//System.out.println(super.getPosition().y);
		}
		if(position.y>TERRAIN_HEIGHT) {
			
			upwardsSpeed = 0;
			isInAir=false;
			super.getPosition().y=TERRAIN_HEIGHT;
			
		}
		//window.getWindow().isKeyPressed()
		if(w.isKeyPressed(GLFW.GLFW_KEY_W)) {
			//this.currentSpeed = -RUN_SPEED;
			position.z-=0.2f;
		}
		if(w.isKeyPressed(GLFW.GLFW_KEY_D)) {
			//this.currentTurnSpeed = +TURN_SPEED;
			position.x+=0.2f;
		}
		if(w.isKeyPressed(GLFW.GLFW_KEY_A)) {
			//this.currentTurnSpeed = -TURN_SPEED;
			position.x-=0.2f;
		}
		if(w.isKeyPressed(GLFW.GLFW_KEY_S)) {
			//this.currentSpeed = +RUN_SPEED;
			position.z+=0.2f;
		}
//		if(w.isKeyPressed(GLFW.GLFW_KEY_Z)) {
//			position.y+=0.2f;
//		}
//		if(w.isKeyPressed(GLFW.GLFW_KEY_X)) {
//			position.y-=0.2f;
//		}
		if(w.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
			if(!isInAir) {
			
			upwardsSpeed = JUMP_POWER;
			isInAir=true;
//			position.x+=0.2f;
//			position.z+=0.2f;
			super.getPosition().y+=29f;
			
			}
			
			
		}
	}
	private void checkInputs(DisplayManager w) {
		this.window = w.getWindow();
		if(w.isKeyPressed(GLFW.GLFW_KEY_W)) {
			this.currentSpeed = -RUN_SPEED;
		}else if(w.isKeyPressed(GLFW.GLFW_KEY_S)){
			this.currentSpeed = RUN_SPEED;
		}else {
			this.currentSpeed = 0;
		}
		
		if(w.isKeyPressed(GLFW.GLFW_KEY_D)) {
			this.currentTurnSpeed = +TURN_SPEED;
			
		}
		else if(w.isKeyPressed(GLFW.GLFW_KEY_A)) {
			this.currentTurnSpeed = -TURN_SPEED;
			
		}
		else {
			this.currentSpeed = 0;
		}
	}
	public float getUpwardsSpeed() {
		return upwardsSpeed;
	}
	public void setUpwardsSpeed(float upwardsSpeed) {
		this.upwardsSpeed = upwardsSpeed;
	}
	public float getCurrentSpeed() {
		return currentSpeed;
	}
	public void setCurrentSpeed(float currentSpeed) {
		this.currentSpeed = currentSpeed;
	}
	public float getCurrentTurnSpeed() {
		return currentTurnSpeed;
	}
	public void setCurrentTurnSpeed(float currentTurnSpeed) {
		this.currentTurnSpeed = currentTurnSpeed;
	}
	public long getWindow() {
		return window;
	}
	public void setWindow(long window) {
		this.window = window;
	}
	public TextureModel getModel() {
		return model;
	}
	public void setModel(TextureModel model) {
		this.model = model;
	}
	public Vector3f getPosition() {
		return position;
	}
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	public float getRotX() {
		return rotX;
	}
	public void setRotX(float rotX) {
		this.rotX = rotX;
	}
	public float getRotY() {
		return rotY;
	}
	public void setRotY(float rotY) {
		this.rotY = rotY;
	}
	public float getRotZ() {
		return rotZ;
	}
	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}
	public float getScale() {
		return scale;
	}
	public void setScale(float scale) {
		this.scale = scale;
	}
	public boolean isInAir() {
		return isInAir;
	}
	public void setInAir(boolean isInAir) {
		this.isInAir = isInAir;
	}
	public static float getRunSpeed() {
		return RUN_SPEED;
	}
	public static float getTurnSpeed() {
		return TURN_SPEED;
	}
	public static float getGravity() {
		return GRAVITY;
	}
	public static float getJumpPower() {
		return JUMP_POWER;
	}
	public static float getTerrainHeight() {
		return TERRAIN_HEIGHT;
	}
	
}
