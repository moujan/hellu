
package gr.hua.hellu.searchData.googleResults;

import gr.hua.hellu.Objects.Author;
import gr.hua.hellu.Objects.CitedPublication;
import java.util.ArrayList;

/**
 *
 * @author Moulou 
 * @version 1.0
 * contact me: moujan21@gmail.com
 *       site: www.dit.hua.gr/~it20818/
 */

public class DataFromPublications extends ParseGoogleData{

    
    public DataFromPublications(ArrayList<String> data){
        super(data);
    }
    
    @Override
    public ArrayList<CitedPublication> parseData() {
        
        ArrayList<CitedPublication> publications = new ArrayList<CitedPublication>();
        String each;
        CitedPublication publication;
        
        for (int i = 0; i < this.Data.size(); i++) {
            each = this.Data.get(i).toString();


            String contentJavascript = null;
            String title = getTitle(each);
            String type = getType(each);
            //if it is there...! I have to do an examination on it.
            String hrefPage = getHrefPage(each);
            String citationsPage = "DOES_NOT_EXIST";

            if (each.contains("class=\"gs_fl\"")) {
                citationsPage = getCitationsPage(each);
            }

            String CODE = getPublicationCode(each);

            int year = getYear(each);
            ArrayList<Author> authors = getAuthorsFromGoogle(each);

            if (authors == null) {
                if ( hrefPage.equals("DOES_NOT_EXIST") || 
                        (authors = getAuthorsFromExternalSite(hrefPage, type, title)) == null ) {
                    GoogleRequestJS js = new GoogleRequestJS(CODE);
                    contentJavascript = js.getContent();
                    authors = getAuthorsFromCITE(contentJavascript);
                    if ( authors == null ) continue;
                }
            }
            
            if ( title.contains("\u2026")){
                if ( contentJavascript == null){
                    GoogleRequestJS js = new GoogleRequestJS(CODE);
                    contentJavascript = js.getContent();
                }
                title = getTitleFromCite(contentJavascript);
            }
            
            
            
            
            publication = new CitedPublication(title, authors, hrefPage, type, citationsPage, year);
            publications.add(publication);
        }
        return publications;
    }
    
}
