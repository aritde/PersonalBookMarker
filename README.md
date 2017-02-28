# PersonalBookMarker
This program sends out an email containing all bookmarks (Page Title and URL) from different browsers in a single machine.

Problem Definition:
This program creates a personal bookmark mechanism which makes it easier for a person using different browsers in a single system to get all his bookmarks available under one roof – his personal email.
Email has been chosen as the medium to aggregate the bookmarks because it will allow the user to access all his bookmarks from any system across the world which has access to internet. 
Currently, the program has been implemented with the below flow. I am working on automating this entire process so that all the bookmarks are immediately available for a person to access from any system.

1.	The bookmarks from the respective browsers are exported in the HTML format.
    a. For Firefox, the export is manual.
    b. For Chrome, the program copies the original(local) copy of the bookmark file that is maintained by Chrome and works on the copy    and does not perform any operations on the original file. The copy is made to reduce the chances of corrupting the file. 

2.	The program parses the respective bookmark file of the corresponding browsers in order to extract the URL and the associated Page Title.

3.	Checks whether the bookmarks are redirected to the same URL or not.

4.	Sends out an email to the sender with the consolidated bookmarks from all web browsers in his/her machine.
	
5.	A proper housekeeping policy is in place which maintains the desired backup files in order to ensure that duplicate URLs are not existing in the final consolidated Bookmark list which is being sent out to the user.

6. When the program is executed for the first time, it is likely for an user having large number of bookmarks across all his/her browsers to overwhelm the SMTP server. Necessary steps have been taken (currently hashed out in the program, though) to handle this situation. This would otherwise cause the ISP to erroneously infer that SPAM emails are being sent out by this user.

Some ongoing modifications are marked with a //TODO: in parts of the program.

In case you are interested to execute this program in order to set up your personal consolidated bookmark, the following needs to be modified:
1.	For Firefox: -> -> Export the file and name it bookmark.html .
2.	Change the email address to your personal email address.
3.	Change the necessary filenames and their path.
4.	Add the below JAR files to the project : 
	a.commons-io-2.5-jar
	b.json-simple-1.1.1.jar
	c.Jsoup-1.9.2.jar
	d.mail.jar
In case you need any other information, drop an email to aritde@indiana.edu .
