package gr.hua.hellu.Objects;

import java.util.ArrayList;

/**
 *
 * @author Moulou 
 * @version 1.0
 * contact me: moujan21@gmail.com
 *       site: www.dit.hua.gr/~it20818/
 */

public class Publication {
    protected String title;
    protected ArrayList<Author> authors;
    protected String hrefPage;
    protected String type;
    protected int year;
    

    public Publication(String title, ArrayList<Author> authors, String hrefPage, String type, int year) {

        this.title = title;
        this.authors = authors;
        this.hrefPage = hrefPage;
        this.type = type;
        this.year = year;
    }

    public ArrayList<Author> getAuthors() {
        return authors;
    }

    public String getHrefPage() {
        return hrefPage;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }
    
    public int getYear() {
        return year;
    }
    public void setYear( int year ){
        this.year = year;
    }
    public String printAuthors() {
        String authorsString = "";

        if (authors != null) {
            for (int i = 0; i < authors.size(); i++) {
                if (i < authors.size() - 1) {
                    authorsString += authors.get(i).toString() + ", ";
                } else {
                    authorsString += authors.get(i).toString();
                }
            }
            return authorsString;
        }
        return "NO AUTHORS FOUND";
    }
    
    public String printAuthorsBIB() {
        String authorsString = "";

        if (authors != null) {
            for (int i = 0; i < authors.size(); i++) {
                if (i < authors.size() - 1) {
                    authorsString += authors.get(i).toString() + " and ";
                } else {
                    authorsString += authors.get(i).toString();
                }
            }
            return authorsString;
        }
        return "";
    }
}
