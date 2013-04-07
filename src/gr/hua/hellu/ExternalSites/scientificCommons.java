
package gr.hua.hellu.ExternalSites;

import gr.hua.hellu.Connection.HttpConnection;
import gr.hua.hellu.Connection.HttpExceptionHandler;
import gr.hua.hellu.Objects.Author;
import gr.hua.hellu.searchData.googleResults.Extract;
import java.util.ArrayList;

/**
 *
 * @author Moulou 
 * @version 1.0
 * contact me: moujan21@gmail.com
 *       site: www.dit.hua.gr/~it20818/
 */

public class scientificCommons {
    
    private static String PAGE = "Scientific Commons";
    
    public static ArrayList<Author> getAuthors(String URL) {
        
        String [] authors = null;
        
        HttpConnection http = new HttpConnection(URL, PAGE);
        http.connect();
        http.getContent();
//        HttpExceptionHandler handler = new HttpExceptionHandler(http);
//        http = handler.checkStatement();
        String Page = http.getContent();
        
        try{
            String [] first = Page.split("class=\"dc_creator\">");

            String [] second = first[1].split("</ul>");


            String [] subListAuthor = second[0].split("/li>");


            authors = exportAuthors(subListAuthor);
        }catch(Exception ex){
            return null;
        }
        
        http.closeConnection();
        
        return Extract.extractAuthors(authors);
        
    }

    private static String[] exportAuthors(String[] subListAuthor) {
        
        String [] authors = null;
        
        for ( int i = 0; i < subListAuthor.length; i++ ){
            
            String [] A = subListAuthor[i].split("</a>");
            String [] B = A[0].split("\"<");
            
            authors[i] = B[1];
            
        }
        return authors;
        
    }
    
    
}
