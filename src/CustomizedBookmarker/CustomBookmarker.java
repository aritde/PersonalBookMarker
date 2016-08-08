package CustomizedBookmarker;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class CustomBookmarker {
	public static void main(String[] args)
	{
		CustomBookmarker myBookmarker=new CustomBookmarker();
		/* Hard Coded For Testing */
		  String outputPath = "<outputpath>";
		  String destAddr="<dest-email-address>";
		  String smtpServer="<smtp-host>";
		  //int startBookMarkNumber=0;
		  //int endBookMarkNumber=5;
		  boolean sendMsgs=true;
		  /* *********** */
		String bookMarkHistoryFile = outputPath+"BookMarkHistory.txt";
		myBookmarker.createHistoryFile(bookMarkHistoryFile);
		
		HashSet<String> listForComparison = myBookmarker.createListForComparison(bookMarkHistoryFile);
	    ArrayList<BookMark> currentBookMarkList=new ArrayList<BookMark>();
	    //Accumulates Firefox Bookmarks
	    	currentBookMarkList = myBookmarker.getFireFoxBookmarks(
	    			outputPath,myBookmarker);
	    //Accumulates Google Chrome Bookmarks
	    	currentBookMarkList.addAll(myBookmarker.getGoogleChromeBookmarks(outputPath,myBookmarker));
	   // For testing
	    /*for (BookMark b:currentBookMarkList )
	    {
	    	System.out.println(b.name);
	      	System.out.println(b.url);
	      	System.out.println("\n");
	    }*/
	   
	    myBookmarker.processBookMarks(destAddr,currentBookMarkList,smtpServer,/*startBookMarkNumber,endBookMarkNumber*/0,currentBookMarkList.size(),sendMsgs,listForComparison);
	    myBookmarker.updateBookMarkHistoryFile(bookMarkHistoryFile,listForComparison);
	}
	private void updateBookMarkHistoryFile(String bookMarkHistoryFile, HashSet<String> listForComparison) {
		try
		{
			PrintWriter writer = new PrintWriter(bookMarkHistoryFile, "UTF-8");
		    for (String value : listForComparison ) 
		    	writer.println(value);
		    writer.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	private HashSet<String> createListForComparison(String bookMarkHistoryFile) {
		HashSet<String> uniqueBookmarkList = new HashSet<String>();
		try 
		{
			BufferedReader input = new BufferedReader(new FileReader(bookMarkHistoryFile));
			String inputLine;
			while((inputLine = input.readLine()) != null)
			{
				uniqueBookmarkList.add(inputLine);
		    }
			input.close();
		}
		catch(Exception e)
		{
		      e.printStackTrace();
		}
		return uniqueBookmarkList;
	}
	private ArrayList<BookMark> getFireFoxBookmarks(String outputPath, CustomBookmarker myBookmarker) {
		ArrayList<BookMark> currentBookMarkList=new ArrayList<BookMark>();
		ParseHTML linkInfo= new ParseHTML();
		ParseHTML linkInfoResults=linkInfo.bookmarks();
		for(int i=0;i< linkInfoResults.linkDetails.size();i++)
		{
			currentBookMarkList.add(new BookMark(linkInfoResults.title.get(i),linkInfoResults.linkDetails.get(i)));
		}
		
		return currentBookMarkList;
	}
	private ArrayList<BookMark> getGoogleChromeBookmarks(String outputPath, CustomBookmarker myBookmarker) {
		ArrayList<BookMark> currentBookMarkList=new ArrayList<BookMark>();
		ParseJson linkInfo= new ParseJson();
		ParseJson linkInfoResults=linkInfo.bookmarks();
		for(int i=0;i< linkInfoResults.linkDetails.size();i++)
		{
			currentBookMarkList.add(new BookMark(linkInfoResults.title.get(i),linkInfoResults.linkDetails.get(i)));
		}
		return currentBookMarkList;
	}
	private void createHistoryFile(String bookMarkHistoryFile)
	{
		try
		{
			new File(bookMarkHistoryFile).createNewFile();
		}
		catch(Exception e)
		{
			//TODO : Write in Log File
			e.printStackTrace();
		}
	}
	private void processBookMarks(String destAddr,ArrayList<BookMark> currentBookMarkList,String smtpServer,int startBookMarkNumber,int endBookMarkNumber,boolean sendMsgs,HashSet<String> listForComparison)
	{
		int newEntry=0;
		StringBuilder emailMessage = new StringBuilder();
		for ( int i=startBookMarkNumber;i<startBookMarkNumber+endBookMarkNumber;i++)
		{
			if(!listForComparison.contains(currentBookMarkList.get(i).url+currentBookMarkList.get(i).name))
			{
				newEntry++;
					emailMessage.append((i+1)+". "+"<b>"+" Name :"+"</b>"+currentBookMarkList.get(i).name +"<b>"+"  | URL :"+"</b>"+currentBookMarkList.get(i).url +"<br />");
					listForComparison.add(currentBookMarkList.get(i).url+currentBookMarkList.get(i).name);
				}
			}
		if(sendMsgs)
		{
			try
			{
				sendBookMark(destAddr,smtpServer,emailMessage.toString(),newEntry);
				//TODO : Try sending an email saying that the last Bookmark update sending was failed to you
				//TODO : Write in logfile
				//TODO : For Windows systems, try creating a pop-up which says that last email sending failed. 
				
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			    System.exit(0);
			}
			System.out.println("Bookmarks sent out = " + newEntry);
		}
	}
	void sendBookMark(String destAdr,String smtpServer,String emailMessage,int totalNewBookmarks)
	{
		String to = destAdr;
        String from ="bookmarkAlert";
        String host = "localhost";
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties);
        try{
	 	      MimeMessage message = new MimeMessage(session);
		      message.setFrom(new InternetAddress(from));
		      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		      message.setSubject("Bookmarks");
		      message.setContent(emailMessage, "text/html" );
		      //TODO: Send has the option of sending to multiple address. Try and extend this for sending out to different emails
		      if(totalNewBookmarks>0)
		      {
		    	  Transport.send(message);
		    	  System.out.println("Sent message successfully....");
		      }
	      else
	    	  System.out.println("No new bookmarks have been added since the last email was sent out. Hence supressing email in this run.");
	   }catch (MessagingException mex) {
	      mex.printStackTrace();
	   }
	}

}
