package com.heimonen;

public class Entry {
	private final int value;
	private boolean marked;

	public Entry(int value) {
		this.value = value;
		this.marked = false;
	}

	public int getValue() {
		return value;
	}

	public boolean isMarked() {
		return marked;
	}

	public void mark() {
		this.marked = true;
	}
}
