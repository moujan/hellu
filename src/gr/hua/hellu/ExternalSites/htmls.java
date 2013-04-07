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

public class htmls {

    private static String PAGE = "Common HTML page";

    public static ArrayList<Author> getAuthors(String URL) {

        String authors[] = null;
        String Page = "";

        HttpConnection http = new HttpConnection(URL, PAGE);
        http.connect();
        http.getContent();
//        HttpExceptionHandler handler = new HttpExceptionHandler(http);
//        http = handler.checkStatement();
        Page = http.getContent();

        if (Page.equals("")) {
            return null;
        }
        try{            
            authors = checkForAuthors(Page);
        }catch(Exception ex){
            return null;
        }
        http.closeConnection();

        if ( authors == null )return null;
        
        return Extract.extractAuthors(authors);
    }

    private static String[] checkForAuthors(String check) {
        //check*********** http://onlinelibrary.wiley.com/doi/10.1002/hep.21533/full
        if (check.contains("name=\"citation_author\"")) {
            return citationAuthor(check);
        }
        //check*********** http://dl.acm.org/citation.cfm?id=1358115
        else if (check.contains("name=\"citation_authors\"")) {
            return citationAuthorS(check);
        }
        //check*********** http://www.nature.com/nature/journal/v421/n6920/abs/nature01353.html?lang=en
        else if (check.contains("name=\"dc.creator\"") 
              || check.contains("name=\"DC.creator\"")
              || check.contains("name=\"dc.Creator\"") )  {
            return dcCreator(check);
        } else if (check.contains("name=\"wkhealth_authors\"")) {
            return wkhealthAuthors(check);
        }/*
         * else if ( check.contains("") ){
         *
         * }
         */

        return null;
    }
    //the page contain metadata of "citation_author"

    private static String[] citationAuthor(String sector) {

        String authors[] = sector.split("name=\"citation_author\"");

        String finalAuthors[] = new String[authors.length - 1];

        for (int i = 0; i < authors.length - 1; i++) {
            String content[] = authors[i + 1].split("ent=");

            String A = content[1];

            String B[] = A.split(">");
            finalAuthors[i] = B[0];//"author's name"
        }

        for (int i = 0; i < finalAuthors.length; i++) {
            if (finalAuthors[i].contains("&#x2010;")) {
                finalAuthors[i].replace("&#x2010;", "-");
            }
            finalAuthors[i] = finalAuthors[i].replaceAll("[^\\p{L}\\p{N}\\.\\' '\\-]", "");
        }

        return finalAuthors;
    }

    //the page contain metadata of "citation_author"
    private static String[] citationAuthorS(String sector) {

        String finalAuthors[] = null;

        String array[] = sector.split("name=\"citation_authors\"");

        String meta[] = array[0].split("<meta");
        
        String after[] = array[1].split(">");
        
        String A[] = null;
        if (meta[meta.length - 1].contains("content")) {
            A = meta[meta.length - 1].split("ent=");
        } else {
            A = after[0].split("ent=");
        }

        A[1] = A[1].replaceAll("[\"\\/]", "");
//        if (A[1].contains("&#x2010;")) {
//            A[1].replace("&#x2010;", "-");
//        }
        if (A[1].contains(";")) {
            A[1] = A[1].replaceAll("[' ']", "");
            A[1] = A[1].replaceAll(",", " ");

            finalAuthors = A[1].split(";");

        } else {
            finalAuthors = A[1].split(",");
        }

        return finalAuthors;
    }

    public static String[] dcCreator(String check) {

        String array[] = check.split("name=\"dc.creator\"");
        if ( array.length == 1)
            array = check.split("name=\"DC.creator\"");
        if ( array.length == 1)
            array = check.split("name=\"dc.Creator\"");
            
        String[] finalString = new String[array.length - 1];

        for (int i = 0; i < finalString.length; i++) {

            String A[] = array[i + 1].split(">");
            String B[] = A[0].split("ent=");
            if (B[1].contains("&#x2010;")) {
                B[1].replace("&#x2010;", "-");
            }
            finalString[i] = B[1].replaceAll("[^\\p{L}\\p{N}\\.\\-]", " ");
        }
        return finalString;

    }

    public static String[] wkhealthAuthors(String check) {
        String finalAuthors[] = null;

        String array[] = check.split("name=\"wkhealth_authors\"");

        String authors[] = array[1].split(">");

        String A[] = authors[0].split("ent=");
        A[1] = A[1].replaceAll("[^\\p{L}\\p{N}\\.\\;\\,\\-\\' ']", "");
//        if (A[1].contains("&#x2010;")) {
//            A[1].replace("&#x2010;", "-");
//        }
        finalAuthors = A[1].split(";");

        for (int i = 0; i < finalAuthors.length; i++) {
            if (finalAuthors[i].contains("&#x2010;")) {
                finalAuthors[i].replace("&#x2010;", "-");
            }
            finalAuthors[i] = finalAuthors[i].replaceAll("[^\\p{L}\\p{N}\\.\\-]", " ");
        }
        return finalAuthors;
    }
}
