package gr.hua.hellu.Objects;

import java.util.ArrayList;

/**
 *
 * @author Moulou 
 * @version 1.0
 * contact me: moujan21@gmail.com
 *       site: www.dit.hua.gr/~it20818/
 */

public class CitedPublication extends Publication{
    
    private String hrefCitations;
    private ArrayList<Publication> self_citations;
    private ArrayList<Publication> hetero_citations;

    public CitedPublication(String title, ArrayList<Author> authors,
            String hrefPage, String type, String hrefCitations, int year) {

        super(title, authors, hrefPage, type, year);
        this.hrefCitations = hrefCitations;
        this.self_citations = new ArrayList<Publication>();
        this.hetero_citations = new ArrayList<Publication>();
    }

    public void addSelfCitation(Publication cite) {
        this.self_citations.add(cite);
    }

    public void addHeteroCitation(Publication cite) {
        this.hetero_citations.add(cite);
    }

    public int getCitations() {
        return this.self_citations.size()+this.hetero_citations.size();
    }

    public String getHrefCitations() {
        return hrefCitations;
    }

    public ArrayList<Publication> getSelfCitations() {
        return self_citations;
    }

    public ArrayList<Publication> getHeteroCitations() {
        return hetero_citations;
    }
    
}
