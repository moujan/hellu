package gr.hua.hellu.ExternalSites;

import gr.hua.hellu.Connection.HttpConnection;
import gr.hua.hellu.Connection.HttpExceptionHandler;
import gr.hua.hellu.Objects.Author;
import gr.hua.hellu.searchData.googleResults.Extract;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Moulou 
 * @version 1.0
 * contact me: moujan21@gmail.com
 *       site: www.dit.hua.gr/~it20818/
 */

public class PDFs {

    private static String PAGE = "Academic Microsoft";
    
    
    static public ArrayList<Author> getAuthors(String title) {

        String[] authors = requestToMicrosoft(title);
        if ( authors == null ) return null;
        return Extract.extractAuthors(authors);
    }

    static private boolean TitleExists(String Content, String Title){
        Content = Content.toLowerCase();
        Title = Title.toLowerCase();
        
        Title = Title.replaceAll("[^\\p{L}\\p{N}]", "-");
        
        String SplittingContent [] = Content.split("<h3>");
        String SplittingContent2 [] = SplittingContent[1].split("</h3>");
        String last = SplittingContent2[0];
        
                
        if( Title.contains("--") ){
            Title = Title.replaceAll("--", "-");
        }
        
        if ( last.contains(Title)) return true;
        
        return false;
    }
    
    static private String[] requestToMicrosoft(String title) {

        String title2 = exchangeTitle(title);
        
        String[] authors = null;
        String Page = null;
        String URL =
                "http://academic.research.microsoft.com/Search?query=" + title2;

        HttpConnection http = new HttpConnection(URL, PAGE);
        http.connect();
        http.getContent();
//        HttpExceptionHandler handler = new HttpExceptionHandler(http);
//        http = handler.checkStatement();
        Page = http.getContent();

        if ( Page.equals("")) return null;
        
        try{
        
            //Page = clearFormatting.cleanPage(Page);
            //spliting page
            if (Page.contains("<li class=\"paper-item\">")) {
                String academicPublishes[] = Page.split("<li class=\"paper-item\">");

                String annoying = academicPublishes[1];

                //if the begining title is not included in the results
                //we can't find the authors - returning null...
                if ( !TitleExists(annoying, title) ) return null;

                String auth[] = annoying.split("<div class=\"content\">");
                String autho[] = auth[1].split("</div>");

                String author[] = autho[0].split("<a class=\"");//ekei briskontai oloi oi authors

                authors = extractAuthors(author);

                http.closeConnection();

                return authors;
            }
        }catch(Exception ex){
            return null;
        }
        return null;

    }
   
    
    static private String[] extractAuthors(String[] authors) {
        String justNames[] = new String[authors.length - 1];

        for (int i = 1; i < authors.length; i++) {

            String A[] = authors[i].split("</a>");

            String B[] = A[0].split("\">");

            justNames[i - 1] = B[1];
        }

        return justNames;
    }

    static private String exchangeTitle(String title) {
        try {
            //we need to see and change more for this situation
            return URLEncoder.encode(title, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(PDFs.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
