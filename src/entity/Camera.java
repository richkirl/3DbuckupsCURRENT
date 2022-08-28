package entity;

import org.joml.Vector3f;

import renderEngine.DisplayManager;

public class Camera {
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch=-4;
	private float yaw=0;
	private float roll=0;
	public long window;
	//public DisplayManager w;
	private float distanceFromPlayer = -50;
	private float angleAroundPlayer = 0;
	private Player player;
	public Camera(Player player) {	
		this.player=player;
	}
	//Keyboard
	public void move(DisplayManager w) {
		//calculatePitch();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPos(horizontalDistance, verticalDistance);
//		this.window = w.getWindow();
//		//window.getWindow().isKeyPressed()
//		if(w.isKeyPressed(GLFW.GLFW_KEY_W)) {
//			position.z-=0.9f;
//		}
//		if(w.isKeyPressed(GLFW.GLFW_KEY_D)) {
//			position.x+=0.9f;
//		}
//		if(w.isKeyPressed(GLFW.GLFW_KEY_A)) {
//			position.x-=0.9f;
//		}
//		if(w.isKeyPressed(GLFW.GLFW_KEY_S)) {
//			position.z+=0.9f;
//		}
//		if(w.isKeyPressed(GLFW.GLFW_KEY_Z)) {
//			position.y+=0.9f;
//		}
//		if(w.isKeyPressed(GLFW.GLFW_KEY_X)) {
//			position.y-=0.9f;
//		}
	}
//	private void calculatePitch(DisplayManager w) {
//		// TODO Auto-generated method stub
//		
//	}
	private void calculateCameraPos(float horiz,float vertical) {
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horiz*Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horiz*Math.cos(Math.toRadians(theta)));
		this.position.x = player.getPosition().x - offsetX;
		this.position.z = player.getPosition().z - offsetZ;
		this.position.y = player.getPosition().y + vertical;
	}
	private float calculateHorizontalDistance() {
		// TODO Auto-generated method stub
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	private float calculateVerticalDistance() {
		// TODO Auto-generated method stub
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	public Vector3f getPosition() {
		return position;
	}
	public float getPitch() {
		return pitch;
	}
	public float getYaw() {
		return yaw;
	}
	public float getRoll() {
		return roll;
	}
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	public void setRoll(float roll) {
		this.roll = roll;
	}
	public void setWindow(long window) {
		this.window = window;
	}
	
	
}
