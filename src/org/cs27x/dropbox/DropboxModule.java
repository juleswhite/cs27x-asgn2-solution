package org.cs27x.dropbox;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.cs27x.filewatcher.FileEventSource;
import org.cs27x.filewatcher.FileSystemState;
import org.cs27x.filewatcher.FileSystemStateImpl;
import org.cs27x.filewatcher.FileWatcherSource;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import static com.google.common.base.Preconditions.*;

/**
 * The DropboxModule is used by Guice to bind the various
 * interface in the application to concrete classes.
 * 
 * 
 * @author jules
 *
 */
public class DropboxModule extends AbstractModule {

	private final Path sharedDir_;

	public DropboxModule(Path sharedDir) {
		checkArgument(Files.exists(sharedDir),
				"The shared directory for the Dropbox application must exist.");

		sharedDir_ = sharedDir;
	}

	@Override
	protected void configure() {
		bind(FileEventSource.class).to(FileWatcherSource.class);
		bind(ExecutorService.class).toInstance(
				Executors.newSingleThreadExecutor());
		bind(FileSystemState.class).to(FileSystemStateImpl.class);
		bind(DropboxTransport.class).to(HazelcastTransport.class);
		bind(FileManager.class).to(FileManagerImpl.class);

		bindConstant().annotatedWith(Names.named(FileManager.ROOT_DIR)).to(
				sharedDir_.toString());
	}

}
