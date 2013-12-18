package org.example.PowerCursor;

public class ObjectWrapper {
	// members
	private int left;
	private int right;
	private int top;
	private int bottom;

	private int width;
	private int height;

	// methods
	public ObjectWrapper() {
		left = 0;
		right = 100;
		top = 0;
		bottom = 100;
		width = right - left;
		height = bottom - top;
	}

	public ObjectWrapper(int left, int top, int right, int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		width = right - left;
		height = bottom - top;
	}
	
	public void setWrapper(int left, int top, int right, int bottom){
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		width = right - left;
		height = bottom - top;
	}

	public int getLeft() {
		return left;
	}

	public int getRight() {
		return right;
	}

	public int getTop() {
		return top;
	}

	public int getBottom() {
		return bottom;
	}

	public int getMidX() {
		return (left + right) / 2;
	}

	public int getMidY() {
		return (top + bottom) / 2;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
