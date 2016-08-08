package CustomizedBookmarker;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;


public class ParseJson 
{
	ArrayList<String> linkDetails;
	ArrayList<String> title;
	ParseJson()
	{
		linkDetails = new ArrayList<String>();
		title = new ArrayList<String>();
	}
	public ParseJson bookmarks()
	{
		ParseJson bookMarks= new ParseJson();
		JSONParser parser = new JSONParser();
		try
		{
			Object obj = parser.parse(new FileReader("/nfs/nfs4/home/aritde/customBookMarker/ChromeBookMarks.html"));
			JSONObject jsonObject = (JSONObject) obj;
			JSONObject obj1 = (JSONObject) jsonObject.get("roots");
			JSONObject obj2 = (JSONObject)obj1.get("bookmark_bar");
			JSONArray arr = (JSONArray)obj2.get("children");
			Iterator<JSONObject> iterator = arr.iterator();
            while (iterator.hasNext())
            {
            	JSONObject value = iterator.next();
            	bookMarks.title.add(value.get("name").toString());
            	bookMarks.linkDetails.add(value.get("url").toString());
			}
        } catch (Exception e) 
		{
            e.printStackTrace();
		}
		return bookMarks;
	}
}
