package toolbox;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
//import org.joml.Vector3fc;

import entity.Camera;


public class Maths {
	public static float barryCentric(Vector3f p1,Vector3f p2,Vector3f p3,Vector2f pos) {
		float det = (p2.z-p3.z)*(p1.x- p3.x)+(p3.x-p2.x)*(p1.z- p3.z);
		float l1 = ((p2.z-p3.z)*(pos.x-p3.x)+(p3.x-p2.x)*(pos.y-p3.z))/det;
		float l2 = ((p3.z-p1.z)*(pos.x-p3.x)+(p1.x-p3.x)*(pos.y-p3.z))/det;
		float l3 = 1.0f-l1-l2;
		return l1* p1.y + l2*p2.y + l3 * p3.y;
	}
	public static Matrix4f createTransformationMatrix(Vector2f translation,Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.identity().translate(new Vector3f(translation.x,translation.y,0f)).scale(new Vector3f(scale.x,scale.y,1f),matrix);
		
		
		return matrix;
		
	}
	public static Matrix4f createTransformationMatrix(Vector3f translation,float rx,float ry,float rz,float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.identity().
		translate(translation)
				.rotate((float)Math.toRadians(rx),new Vector3f(1,0,0))
				.rotate((float)Math.toRadians(ry),new Vector3f(0,1,0))
				.rotate((float)Math.toRadians(rz),new Vector3f(0,0,1));
		matrix.scale(new Vector3f(scale,scale,scale),matrix);
		return matrix;
		
	}
	public static Matrix4f getViewMatrix(Camera camera){
        Matrix4f matrix =new Matrix4f();
        matrix.identity();
        matrix.rotate((float)Math.toRadians(camera.getPitch()),new Vector3f(1,0,0))
                .rotate((float)Math.toRadians(camera.getYaw()),new Vector3f(0,1,0));
        Vector3f cameraPos = camera.getPosition();
        
        matrix.translate(-cameraPos.x,-cameraPos.y,-cameraPos.z);
        return matrix;
    }
}
