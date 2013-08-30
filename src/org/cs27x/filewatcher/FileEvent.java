package org.cs27x.filewatcher;

import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;


/**
 * FileEvents represent changes to the file system, such
 * as the addition, removal, or modification of a file.
 * 
 * @author jules
 *
 */
public class FileEvent {

	private final Kind<?> eventType_;
	private final Path file_;
	private final byte[] data_;

	public FileEvent(Kind<?> eventType, Path file, byte[] data) {
		super();
		eventType_ = eventType;
		file_ = file;
		data_ = data;
	}
	
	public FileEvent(Kind<?> eventType, Path file) {
		this(eventType,file,null);
	}

	public Path getFile() {
		return file_;
	}

	public Kind<?> getEventType() {
		return eventType_;
	}
}
