package org.cs27x.filewatcher;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.cs27x.dropbox.DropboxCmd;

public class FileSystemStateImpl implements FileSystemState {

	private static final int UNININITIALIZED = -2;
	
	private Map<String, FileState> states_ = new HashMap<>();

	private synchronized FileState getOrCreateState(Path p) {
		String key = p.toString();
		FileState state = states_.get(key);
		if (state == null) {
			state = new FileState(UNININITIALIZED);
			states_.put(key, state);
		}
		return state;
	}

	@Override
	public synchronized boolean updateState(FileEvent evt) {
		final Path p = evt.getFile();
		final long size = (evt.getData() != null)? evt.getData().length : UNININITIALIZED;
		
		final FileState state = getOrCreateState(p);
		final boolean update = size != state.getSize();
		
		state.setSize(size);

		return update;
	}

	@Override
	public boolean updateState(DropboxCmd cmd) {
		final Path p = Paths.get(cmd.getPath());
		final long size = (cmd.getData() != null) ? cmd.getData().length : 0;
		
		final FileState state = getOrCreateState(p);
		final boolean update = size != state.getSize();
		
		state.setSize(size);
		
		return update;
	}

	
}
