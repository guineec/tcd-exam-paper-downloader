//This one doesn't really need to be a class of its own either, what can you do?

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.*;
import java.util.Scanner;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFSaver {
	private String url;
	private String filename;
	private String pathToDirectory;

	public PDFSaver(String link, String chosenDir) {
		url = link;
		pathToDirectory = chosenDir;
		Scanner linkScan = new Scanner(link);
		linkScan.useDelimiter("/");
		for (int i = 0; i < 8; i++) {
			filename = linkScan.next();
		}
		linkScan.close();
	}

	public boolean getPDF(String year, String stnd) {
		try {
			if (!filename.equals("pink_fab.jpg")) {
				String stand;
				switch (Integer.parseInt(stnd)) {
					case 2:
						stand = "Senior Freshman (2nd Year)";
						break;
					case 3:
						stand = "Junior Sophister (3rd Year)";
						break;
					case 4:
						stand = "Senior Sophister (4th Year)";
						break;
					default:
						stand = "Junior Freshman (1st Year)";
				}

				URL website = new URL(url);
				ReadableByteChannel rbc = Channels.newChannel(website.openStream());
				File folder = pathToDirectory.equals("") ? new File(stand + "/" + year)
						: new File(pathToDirectory + "/" + stand + "/" + year);
				if (!folder.mkdirs())
					System.out.println("Error creating directories. Files might not be where you want!");
				FileOutputStream fos = new FileOutputStream(folder + "/" + filename);
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				fos.close();
			}
			return true;
		} catch (MalformedURLException e) {
			System.out.println("Error with pdf url.");
			return false;
		} catch (IOException e) {
			System.out.println("File not found on TCD website, probably a dead link");
			return false;
		}
	}
}
