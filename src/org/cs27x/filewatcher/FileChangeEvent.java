package org.cs27x.filewatcher;

public interface FileChangeEvent {

	public String getPath();
	public byte[] getData();

}