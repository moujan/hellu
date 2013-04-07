
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

public class ioPress {
    
    private static String PAGE = "IO Press";
    
    public static ArrayList<Author> getAuthors(String URL) {
        
        String [] authors = null;
        
        HttpConnection http = new HttpConnection(URL, PAGE);
        http.connect();
        http.getContent();
//        HttpExceptionHandler handler = new HttpExceptionHandler(http);
//        http = handler.checkStatement();
        String Page = http.getContent();
        
        //do stuff and find authors!!!!
        
        http.closeConnection();
        
        return Extract.extractAuthors(authors);
        
    }
    
}
