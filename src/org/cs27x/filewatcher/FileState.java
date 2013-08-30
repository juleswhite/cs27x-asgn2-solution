package org.cs27x.filewatcher;

import com.google.common.hash.HashCode;

public class FileState {

	private long size_;
	private HashCode fileHash_;

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

	public HashCode getFileHash() {
		return fileHash_;
	}

	public void setFileHash(HashCode fileHash) {
		fileHash_ = fileHash;
	}

}
