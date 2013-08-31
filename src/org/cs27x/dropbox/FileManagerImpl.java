package org.cs27x.dropbox;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class FileManagerImpl implements FileManager {

	private final Path rootDir_;

	@Inject
	public FileManagerImpl(@Named(ROOT_DIR) String rootDir) {
		super();
		rootDir_ = Paths.get(rootDir);

		checkArgument(Files.exists(rootDir_),
				"The shared directory for the Dropbox application must exist.");
	}

	@Override
	public Path ensureRelative(Path path) {
		return rootDir_.relativize(path);
	}

	@Override
	public Path resolve(String relativePathName) {
		return rootDir_.resolve(relativePathName);
	}

	@Override
	public boolean exists(Path p) {
		return Files.exists(p);
	}

	@Override
	public void write(Path p, byte[] data, boolean overwrite)
			throws IOException {
		if (!Files.exists(p) || overwrite) {
			try (OutputStream out = Files.newOutputStream(p)) {
				out.write(data);
			}
		}
	}

	@Override
	public void delete(Path p) throws IOException {
		if (exists(p)) {
			Files.delete(p);
		}
	}

}
