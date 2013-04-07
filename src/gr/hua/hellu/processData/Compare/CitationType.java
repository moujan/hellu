package gr.hua.hellu.processData.Compare;

import gr.hua.hellu.Objects.Author;
import gr.hua.hellu.Objects.CitedPublication;
import gr.hua.hellu.Objects.Publication;
import java.util.ArrayList;

/**
 *
 * @author Moulou 
 * @version 1.0
 * contact me: moujan21@gmail.com
 *       site: www.dit.hua.gr/~it20818/
 */

public class CitationType {
    
    private CitedPublication publication;
    private ArrayList<Publication> citations;
    
    
    public CitationType(CitedPublication publication, ArrayList<Publication> citations){
        
        this.publication = publication;
        this.citations = citations;
    }
    
    public void setCitationType(){
        ArrayList<Author> publicationSAuthors = this.publication.getAuthors();
        if (publicationSAuthors != null) {
            for (int i = 0; i < citations.size(); i++) {

                ArrayList<Author> citationSAuthors = citations.get(i).getAuthors();
                boolean isShelf = false;
                if (citationSAuthors != null) {
                    for (int mainPubAuthor = 0; mainPubAuthor < publicationSAuthors.size(); mainPubAuthor++) {
                        for (int citationAuthor = 0; citationAuthor < citationSAuthors.size(); citationAuthor++) {
                            Comparison c = new Comparison(publicationSAuthors.get(mainPubAuthor), citationSAuthors.get(citationAuthor));
                            if (c.isTheSameAuthor()) {
                                isShelf = true;
                            }
                        }
                    }
                    if (isShelf) {
                        this.publication.addSelfCitation(citations.get(i));
                    } else {
                        this.publication.addHeteroCitation(citations.get(i));
                    }
                }
            }
        }
    }
    
    public CitedPublication getCitedPulication(){
        return this.publication;
    }
}
