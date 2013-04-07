
package gr.hua.hellu.ExternalSites;

import gr.hua.hellu.Connection.HttpConnection;
import gr.hua.hellu.Connection.HttpExceptionHandler;
import gr.hua.hellu.Objects.Author;
import gr.hua.hellu.searchData.googleResults.Extract;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author Moulou 
 * @version 1.0
 * contact me: moujan21@gmail.com
 *       site: www.dit.hua.gr/~it20818/
 */

public class inderScience {

    private static String PAGE = "Inder Science";
    
    public static ArrayList<Author> getAuthors(String URL) {
        
        String [] authors = null;
        
        HttpConnection http = new HttpConnection(URL, PAGE);
        http.connect();
        http.getContent();
//        HttpExceptionHandler handler = new HttpExceptionHandler(http);
//        http = handler.checkStatement();
        String Page = http.getContent();
        
        try{
            String [] splitting = Page.split("</h5><div>");

            String [] splitting2 = splitting[1].split("</div><p>");

            String authorS = splitting2[0];

            authors = exportAuthors(authorS);
        }catch(Exception ex){
            return null;
        }
        
        http.closeConnection();
        
        return Extract.extractAuthors(authors);
        
    }

    private static String [] exportAuthors(String line){
        String [] authors = null;
        
        authors = line.split(",");
        
        for ( int i = 0; i < authors.length; i++ ){
            
            if( authors[i].contains("/"))
                authors[i] = authors[i].replaceAll("/", "");
            if( authors[i].contains("<sup>"))
                authors[i] = authors[i].replaceAll("<sup>", "");
            
            authors[i] = authors[i].replaceAll("[^\\p{L}\\' '\\-]","");
        }
        
        
        return authors;
    }
}
