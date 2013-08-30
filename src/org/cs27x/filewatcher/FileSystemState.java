package org.cs27x.filewatcher;

import org.cs27x.dropbox.DropboxCmd;

/**
 * A FileSystemState is responsible for holding what the Dropbox application
 * believes should be the correct state of the shared file system. The FileSystemState
 * holds what it believes *will* be the correct state when all DropboxCmds have
 * finished processing. The FileSystemState is updated by DropboxCmds before the
 * cmds are actually executed and affect the actual file system. 
 * 
 * The actual file system state on the local host may differ from the FileSystemState's
 * view of the shared file system. A key responsibility of the FileSystemState is to
 * identify which local FileEvents represent changes that are bringing the local file
 * system in-line with the share file system state (e.g., a remote change that was
 * enacted on the local file system) and which events represent true changes external
 * to the application that are altering the local file system (e.g., a user saves a
 * file to a directory and it needs to be synch'd into the shared file system state).
 * For example, the FileSystemState will receive a file add command before the
 * command is actually executed and the new file added to the file system. A
 * FileEventSource may generate a new FileEvent as a result of the added file.
 * It is the FileSystemState's responsibility to determine that the event represents
 * a change that is due to a file system modification in response to a DropboxCmd's
 * execution.
 * 
 * @author jules
 *
 */
public interface FileSystemState {

	/**
	 * This method determines if the DropboxCmd brings the local file state in line
	 * with the known shared file system state (e.g., the expected state of the shared folder
	 * across all the hosts). If the DropboxCmd brings the local file system in line
	 * with the known shared file system state, the method returns true, indicating
	 * that the cmd should not be executed and applied to the local file system. The method
	 * also updates the known shared file system state. If the
	 * cmd represents a change that has already occurred locally, then the method merely
	 * updates the expected shared file system state with the information in the event and 
	 * returns false to indicate that the cmd should not be executed. 
	 * 
	 * 
	 * @param evt
	 * @return
	 */
	public boolean updateState(DropboxCmd cmd);
	
	/**
	 * This method determines if the FileEvent brings the local file state in line
	 * with the known shared file system state (e.g., the expected state of the shared folder
	 * across all the hosts). If the FileEvent brings the local file system in line
	 * with the known shared file system state, the method returns false, indicating
	 * that the event should not be propagated. If the
	 * FileEvent represents a local change that needs to be propagated to the 
	 * shared file system, then the method updates the expected shared file system
	 * state with the information in the event and returns true to indicate that the
	 * event should be propagated. 
	 * 
	 * 
	 * @param evt
	 * @return
	 */
	public boolean updateState(FileEvent evt);
	
}
