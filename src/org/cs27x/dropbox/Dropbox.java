package org.cs27x.dropbox;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.cs27x.filewatcher.FileEventSource;

import com.beust.jcommander.JCommander;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * The root class of the application. This class is
 * primarily responsible for constructing the other
 * classes using a dependency injection framework and
 * then starting the DropboxTransport and FileEventSource.
 * 
 * @author jules
 *
 */
public class Dropbox {

	private final DropboxTransport transport_;
	@SuppressWarnings("unused")
	private final DropboxClient client_;
	private final FileEventSource fileEventSource_;
	
	@Inject
	public Dropbox(DropboxClient client, 
			FileEventSource fileEventSource,
			DropboxTransport transport) {
		super();
		client_ = client;
		fileEventSource_ = fileEventSource;
		transport_ = transport;
	}

	public void connect(String server) throws Exception {
		transport_.connect(server);
		fileEventSource_.start();
	}
	
	public boolean connected(){
		return transport_.isConnected();
	}
	
	public void disconnect(){
		fileEventSource_.stop();
		transport_.disconnect();
	}
	
	public static void main(String[] args) throws Exception {

		DropboxOptions options = new DropboxOptions();
		JCommander jcommander = new JCommander(options,args);
		jcommander.setProgramName("org.cs27x.dropbox.Dropbox");
		
		if(options.getRootDir() == null){
			jcommander.usage();
		}
		else if(!Files.exists(Paths.get(options.getRootDir()))){
			System.err.println("The specified directory to share ["+options.getRootDir()+"] does not exist.");
			jcommander.usage();
		}
		else {
			Injector injector = Guice.createInjector(new DropboxModule(Paths.get(options.getRootDir())));
			Dropbox db = injector.getInstance(Dropbox.class);		
			db.connect(options.getHost());
		}
	}
	
}
