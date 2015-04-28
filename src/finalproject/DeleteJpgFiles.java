package finalproject;


import java.io.File;

public class DeleteJpgFiles {

	private static String parentDirectory = ".";
	private static String deleteExtension = ".jpg";

	public static void delJpg() {
		FileFilter fileFilter = new FileFilter(deleteExtension);
		File parentDir = new File(parentDirectory);

		// Put the names of all files ending with .txt in a String array
		String[] listOfTextFiles = parentDir.list(fileFilter);

		if (listOfTextFiles.length == 0) {
			System.out.println("There are no text files in this direcotry!");
			return;
		}

		File fileToDelete;

		for (String file : listOfTextFiles) {

			//construct the absolute file paths...
			String absoluteFilePath = new StringBuffer(parentDirectory).append(File.separator).append(file).toString();

			//open the files using the absolute file path, and then delete them...
			fileToDelete = new File(absoluteFilePath);
			boolean isdeleted = fileToDelete.delete();
			System.out.println("File : " + absoluteFilePath + " was deleted : " + isdeleted);
		}
	}
}
