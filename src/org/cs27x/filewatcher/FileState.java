package org.cs27x.filewatcher;


public class FileState {

	private long size_;

	public FileState(long size) {
		super();
		size_ = size;
	}

	public long getSize() {
		return size_;
	}

	public void setSize(long size) {
		size_ = size;
	}

}
