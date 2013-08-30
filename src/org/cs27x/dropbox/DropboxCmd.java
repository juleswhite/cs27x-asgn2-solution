package org.cs27x.dropbox;

import java.io.Serializable;

import org.cs27x.filewatcher.FileChangeEvent;

public class DropboxCmd implements Serializable, FileChangeEvent {

	private static final long serialVersionUID = 1L;

	public enum OpCode {
		ADD, REMOVE, UPDATE, SYNC, GET
	}

	private final String path_;
	private final byte[] data_;
	private final OpCode opCode_;

	public DropboxCmd(OpCode opCode, String path, byte[] data) {
		super();
		path_ = path;
		data_ = data;
		opCode_ = opCode;
	}
	
	public DropboxCmd(OpCode opCode, String path) {
		this(opCode,path,null);
	}
	
	public String getPath() {
		return path_;
	}

	/* (non-Javadoc)
	 * @see org.cs27x.dropbox.FileData#getData()
	 */
	@Override
	public byte[] getData() {
		return data_;
	}

	public OpCode getOpCode() {
		return opCode_;
	}

}
