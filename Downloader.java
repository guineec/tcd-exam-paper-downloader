import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Downloader {
    private String fullURL;
    private String tcdURL = "https://www.tcd.ie";
    private String year;
    private String chosenPath = "";
    private String stand;


    public Downloader(String yr, String stnd, int course, String path) {
        fullURL = "https://www.tcd.ie/Local/Exam_Papers/annual_search.cgi?Course=" + course + "&Standing=" + stnd
                + "&acyear=" + yr + "&annual_search.cgi=Search";
        year = yr;
        stand = stnd;
        chosenPath = path;
    }

    public boolean getPapers() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(fullURL).openStream()));
            String currentLine;

            while ((currentLine = br.readLine()) != null) {
                Scanner urlFinder = new Scanner(currentLine);
                urlFinder.useDelimiter("\"");
                urlFinder.next();

                if (urlFinder.hasNext()) {
                    String localURL = urlFinder.next();

                    if (!localURL.equals("http://www.tcd.ie/Local/Exam_Papers/summer_nonTSM.html") && !localURL.equals("http://www.tcd.ie/Local/Exam_Papers/pink_fab.jpg")) {
                        String fullURL = tcdURL + localURL;

                        boolean getPDFSuccess = new PDFSaver(fullURL, chosenPath).getPDF(year, stand);


                        if (!getPDFSuccess) {
                            System.out.println("File at URL: " + fullURL + " could not be downloaded.");
                            urlFinder.close();
                        } else {
                            System.out.println("Success in download of pdf at: " + fullURL);
                        }

                    } else {
                        System.out.println("\n\nAll URLs Read.");
                    }

                }
                urlFinder.close();
            }

            return true;
        } catch (IOException e) {
            System.out.println("Error with URL, email cian.guinee@gmail.com with this message.");
            return false;
        }
    }
}
