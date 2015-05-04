package finalproject;


import java.io.File;
import java.io.IOException;

public class DeleteImageTask {

	private static final String parentDirectory = ".";
	private static final String deleteExtension = ".jpg";

	public static void delJpg() throws IOException {
		FileFilter fileFilter = new FileFilter(deleteExtension);
		File parentDir = new File(parentDirectory);

		// Put the names of all files ending with .jpg in a String array
		String[] listOfTextFiles = parentDir.list(fileFilter);

		if (listOfTextFiles.length == 0) {
			System.out.println("There are no jpg files in: \'" +  parentDir.getCanonicalPath() + "\'");
			return;
		}

		File fileToDelete;

		for (String file : listOfTextFiles) {

			//construct the absolute file paths...
			String absoluteFilePath = new StringBuffer(parentDirectory).append(File.separator).append(file).toString();

			//open the files using the absolute file path, and then delete them...
			fileToDelete = new File(absoluteFilePath);
			boolean isdeleted = fileToDelete.delete();
			System.out.println("Deleted : " + absoluteFilePath );
		}
	}
}
