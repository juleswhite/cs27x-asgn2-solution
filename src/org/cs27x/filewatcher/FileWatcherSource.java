package org.cs27x.filewatcher;

import static com.google.common.base.Preconditions.checkArgument;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.cs27x.dropbox.FileManager;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class FileWatcherSource implements FileEventSource {


	private class Dispatcher implements Runnable {
		private final FileEventListener handler_;
		private final FileEvent event_;

		public Dispatcher(FileEventListener handler, FileEvent event) {
			super();
			handler_ = handler;
			event_ = event;
		}

		public void run() {
			try {
				handler_.handleEvent(event_);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	private final Path toWatch_;
	private ExecutorService executor_;
	private List<FileEventListener> fileHandlers_ = new ArrayList<>();
	private boolean running_;

	@Inject
	public FileWatcherSource(@Named(FileManager.ROOT_DIR) String toWatch, ExecutorService executor) {
		super();
		toWatch_ = Paths.get(toWatch).toAbsolutePath();
		executor_ = executor;
		
		checkArgument(Files.exists(toWatch_),
				"The shared directory for the Dropbox application must exist.");
	}

	public FileWatcherSource(String toWatch) {
		this(toWatch, Executors.newSingleThreadExecutor());
	}

	public void start() throws IOException {
		running_ = true;
		watchLoop();
	}

	private void watchLoop() throws IOException {

		WatchService watcher = FileSystems.getDefault().newWatchService();
		toWatch_.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

		while (running_) {

			// wait for key to be signaled
			WatchKey key;
			try {

				key = watcher.take();
				
			} catch (InterruptedException x) {
				return;
			}

			if (key != null) {
				for (WatchEvent<?> event : key.pollEvents()) {

					if (!running_) {
						return;
					}
					System.out.println("Processing event...");
					processEvent(event);

				}

				// Reset the key -- this step is critical if you want to
				// receive further watch events. If the key is no longer valid,
				// the directory is inaccessible so exit the loop.
				boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void processEvent(WatchEvent<?> event) {
		WatchEvent.Kind<?> kind = event.kind();
		// This key is registered only
		// for ENTRY_CREATE events,
		// but an OVERFLOW event can
		// occur regardless if events
		// are lost or discarded.
		if (kind != OVERFLOW) {

			// The filename is the
			// context of the event.
			WatchEvent<Path> ev = (WatchEvent<Path>) event;
			Path filename = ev.context();

			// Resolve the filename against the directory.
			// If the filename is "test" and the directory is "foo",
			// the resolved name is "test/foo".
			Path child = toWatch_.resolve(filename);

			try {
				FileEvent evt = new FileEvent(kind, child);
				dispatch(evt);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		running_ = false;
		Thread.interrupted();
	}

	private void dispatch(final FileEvent evt) {
		for (FileEventListener h : fileHandlers_) {
			executor_.submit(new Dispatcher(h, evt));
		}
	}

	public void addListener(FileEventListener h) {
		fileHandlers_.add(h);
	}

	public void removeListener(FileEventListener h) {
		fileHandlers_.remove(h);
	}
}
