package renderEngine;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.*;

import org.lwjgl.glfw.Callbacks;

public class DisplayManager {
	private static int WIDTH;
	private static int HEIGHT;
	public long window;
	private static long lastFrameTime;
	private static float delta;
	public DisplayManager(int wIDTH, int hEIGHT) {
		super();
		WIDTH = wIDTH;
		HEIGHT = hEIGHT;
	}
	private static long getCurrentTime() {
		return System.currentTimeMillis();
	}
	
	
	
	public void createDisplay() {
		init();
		//loop();

		// Free the window callbacks and destroy the window

		// Terminate GLFW and free the error callback
		
		
				// Enable v-sync
				
				// Make the window visible
				//GLFW.glfwShowWindow(window);
		
		
		//updateDisplaySync(true);
		lastFrameTime = getCurrentTime();
	}
	private void init() {
		// TODO Auto-generated method stub
		GLFWErrorCallback.createPrint(System.err).set();
		if (!GLFW.glfwInit())
			throw new IllegalStateException("Unnable to init GLFW!");

		// Configure GLFW
		GLFW.glfwDefaultWindowHints(); // optional, the current window hints are already the default
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE); // the window will stay hidden after creation
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE); // the window will be resizable

		// Create the window
		window = GLFW.glfwCreateWindow(getWIDTH(), getHEIGHT(), "Hello World!", 0, 0);
		if (window == 0)
			throw new RuntimeException("Failed to create the GLFW window");

		GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
				GLFW.glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});
		// Get the thread stack and push a new frame
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			GLFW.glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

			// Center the window
			GLFW.glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();
		updateDisplaySync(true);
		GLFW.glfwShowWindow(window);
		
	}
	public void updateDisplaySync(boolean e) {
		if(e)
			GLFW.glfwSwapInterval(1);
		else
			GLFW.glfwSwapInterval(0);
	}
	public void swappoll() {
		long currentFrame = getCurrentTime();
		delta = (currentFrame - lastFrameTime)/1000000000.0f;
		lastFrameTime = currentFrame;
		GLFW.glfwSwapBuffers(getWindow()); // swap the color buffers

		// Poll for window events. The key callback above will only be
		// invoked during this call.
		GLFW.glfwPollEvents();
	}
	public static float getFrameTimeSeconds() {
		return delta;
	}
	public void postinit() {
		
		// Set the clear color
		//GL15.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	}
	public void updateclear() {
		//GL15.glClear(GL15.GL_COLOR_BUFFER_BIT | GL15.GL_DEPTH_BUFFER_BIT); // clear the framebuffer
	}
	public void closeDisplay() {
		Callbacks.glfwFreeCallbacks(window);
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}
	public boolean isKeyPressed(int keycode){
        return GLFW.glfwGetKey(window,keycode)==GLFW.GLFW_PRESS;
    }
	public long getWindow() {
		return this.window;
	}
	public void setWindow(long window) {
		this.window = window;
	}
	public static int getWIDTH() {
		return WIDTH;
	}
	public void setWIDTH(int wIDTH) {
		this.WIDTH = wIDTH;
	}
	public static int getHEIGHT() {
		return HEIGHT;
	}
	public void setHEIGHT(int hEIGHT) {
		this.HEIGHT = hEIGHT;
	}
	
}
