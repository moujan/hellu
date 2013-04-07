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

public class scienceDirect {

    private static String PAGE = "Science Direct";
    static public ArrayList<Author> getAuthors(String URL) {

        String[] authors = null;
        String Page = null;

        HttpConnection http = new HttpConnection(URL, PAGE);
        http.connect();
        http.getContent();
//        HttpExceptionHandler handler = new HttpExceptionHandler(http);
//        http = handler.checkStatement();
        Page = http.getContent();

        if ( Page.equals("")) return null;
        try{
            String A[] = Page.split("<ul class=\"authorGr");

            String B[] = A[1].split("<li>");

            authors = extractAuthors(B);
        }catch(Exception ex){
            return null;
        }
        http.closeConnection();
        
        if ( authors == null ) return null;
        
        return Extract.extractAuthors(authors);
    }

    static private String[] extractAuthors(String[] authors) {

        int length = 0;
        for (int i = 1; i < authors.length; i++) {
            if (authors[i].contains("class=\"authorName\"")) {
                length++;
            }
        }

        String[] finalAuthors = new String[length];

        for (int i = 1; i < authors.length; i++) {
            if (authors[i].contains("class=\"authorName\"")) {
                String A[] = null;
                if ( authors[i].contains("</a><a"))
                     A = authors[i].split("</a><a");
                else if (authors[i].contains("</a> <a"))
                     A = authors[i].split("</a> <a");
                else 
                    A = authors[i].split("</a>");
                String B[] = A[0].split(">");
                //
                finalAuthors[i - 1] = B[1];
            }
        }

        return finalAuthors;

    }
}