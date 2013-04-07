
package gr.hua.hellu.searchData.googleResults;

import gr.hua.hellu.Objects.Author;
import gr.hua.hellu.Objects.Publication;
import java.util.ArrayList;

/**
 *
 * @author Moulou 
 * @version 1.0
 * contact me: moujan21@gmail.com
 *       site: www.dit.hua.gr/~it20818/
 */

public class DataFromCitations extends ParseGoogleData{
    
    
    public DataFromCitations(ArrayList<String> data){
        super(data);
    }
    
    @Override
    public ArrayList<Publication> parseData() {
        
        ArrayList<Publication> citations = new ArrayList<Publication>();
        Publication citation;
        String each;

        
        for (int i = 0; i < this.Data.size(); i++) {

            String contentJavascript = null;
            
            each = this.Data.get(i).toString();
            String title = getTitle(each);
            String href = getHrefPage(each);
            String type = getType(each);

            String CODE = getPublicationCode(each);
            int year = getYear(each);
            ArrayList<Author> authors = getAuthorsFromGoogle(each);

            if (authors == null) {
                if ( href.equals("DOES_NOT_EXIST") || href.contains("google") ||
                        (authors = getAuthorsFromExternalSite(href, type, title)) == null ) {
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

            citation = new Publication(title, authors, href, type, year);
            citations.add(citation);
        }
        return citations;
    }
}
