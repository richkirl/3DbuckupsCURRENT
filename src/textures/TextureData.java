package textures;

import java.nio.ByteBuffer;

public class TextureData {
	private int pWidth;
	private int pHeight;
	private ByteBuffer pBuffer;
	public TextureData(int pWidth, int pHeight, ByteBuffer pBuffer) {
		
		this.pWidth = pWidth;
		this.pHeight = pHeight;
		this.pBuffer = pBuffer;
	}
	public int getpWidth() {
		return pWidth;
	}
	public void setpWidth(int pWidth) {
		this.pWidth = pWidth;
	}
	public int getpHeight() {
		return pHeight;
	}
	public void setpHeight(int pHeight) {
		this.pHeight = pHeight;
	}
	public ByteBuffer getpBuffer() {
		return pBuffer;
	}
	public void setpBuffer(ByteBuffer pBuffer) {
		this.pBuffer = pBuffer;
	}
	
}
