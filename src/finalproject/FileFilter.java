package finalproject;

import java.io.File;
import java.io.FilenameFilter;

/**
 * This class is used to find the files that need to be deleted
 * 
 * @author Nacer Abreu
 * @author Michael Norris
 * @author Emmanuel Bonilla
 *
 */
public class FileFilter implements FilenameFilter {

	private String fileExtension;

	/**
	 * Constructor for setting the file extension
	 * @param fileExtension - string of the extension to use for files
	 */
	public FileFilter(String fileExtension) {
		this.fileExtension = fileExtension;
	} //end FileFilter()

	/**
	 * This method returns the name of a file for the given file extension
	 * @param directory - directory to search for files
	 * @param fileName - name of file in given directory
	 * @return	fileName - returns the filename with the .jpg extension
	 */
	@Override
	public boolean accept(File directory, String fileName) {
		return (fileName.endsWith(this.fileExtension));
	} //end accept()
} //end FileFilter class