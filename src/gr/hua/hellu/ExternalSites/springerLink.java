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

public class springerLink {

    private static String PAGE = "Springer Link";

    static public ArrayList<Author> getAuthors(String URL) {

        String[] tableAuthors = null;
        String Page = "";
        

        HttpConnection http = new HttpConnection(URL, PAGE);
        http.connect();
        http.getContent();
//        HttpExceptionHandler handler = new HttpExceptionHandler(http);
//        http = handler.checkStatement();
        Page = http.getContent();

        if ( Page.equals("") ) return null;
        
        String[] A = Page.split("</h1><p class=\"authors\">");

        String[] B = A[1].split("/p>");

        String[] C = B[0].split("</a>");

        tableAuthors = extractAuthors(C);

        http.closeConnection();

        return Extract.extractAuthors(tableAuthors);

    }

    static private String[] extractAuthors(String[] authors) {

        String finalAuthors[] = new String[authors.length - 1];

        for (int i = 0; i < authors.length - 1; i++) {

            String Dfinal[] = authors[i].split("\">");

            finalAuthors[i] = Dfinal[1];
        }
        return finalAuthors;

    }
}
