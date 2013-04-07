
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

public class ieeexplore {
    private static String PAGE = "IEEExplore";
    
    static public ArrayList<Author> getAuthors ( String URL ){

        String authors[] = null;
        String Page = "";

        HttpConnection http = new HttpConnection(URL, PAGE);
        http.connect();
        http.getContent();
//        HttpExceptionHandler handler = new HttpExceptionHandler(http);
//        http = handler.checkStatement();
        Page = http.getContent();

        if ( Page.equals("")) return null;
        
        try{
            String A [] = Page.split("r\\(s\\):");

            String B [] = A[1].split("<strong>");

            String C [] = B[0].split("</script>");
            
            authors = extractAuthors( C );
        }catch(Exception ex){
            return null;
        }
                
        http.closeConnection();

        return Extract.extractAuthors(authors);
    }

    static private String[] extractAuthors ( String[] authors){

            String finalAuthors[] = new String[authors.length-1];

            for ( int i = 1; i < authors.length; i++ ){
                String Dfinal[] = authors[i].split("</a>");

                finalAuthors[i-1] = Dfinal[0];
            }

            for ( int i = 0; i < finalAuthors.length; i++ ){
                finalAuthors[i] = finalAuthors[i].replaceAll("[^\\p{L}\\p{N}\\.\\,]", "");
                finalAuthors[i] = finalAuthors[i].replaceAll(",", " ");
            }

            return finalAuthors;
    }
}
