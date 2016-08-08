package CustomizedBookmarker;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseHTML {
	ArrayList<String> linkDetails;
	ArrayList<String> title;
	ParseHTML()
	{
		linkDetails = new ArrayList<String>();
		title = new ArrayList<String>();
	}
  public ParseHTML bookmarks() {
	  
	  ParseHTML bookMarks= new ParseHTML();
	  Document doc;
	try {

		File input = new File("/nfs/nfs4/home/aritde/customBookMarker/bookmarks.html");
		doc = Jsoup.parse(input, "UTF-8");
		int count=0;
		// get all links
		Elements links = doc.select("A[HREF]");
		for (Element link : links) {
			if(count<3)
			{
				count++;
				continue;
			}
			// get the value from href attribute
			else
			{
				bookMarks.linkDetails.add(link.attr("href"));
				bookMarks.title.add(link.text());
				
			}

		}

	} catch (IOException e) {
		e.printStackTrace();
	}
	return bookMarks;
  }

}