
package gr.hua.hellu.searchData.googleResults;

import gr.hua.hellu.ExternalSites.PDFs;
import gr.hua.hellu.ExternalSites.htmls;
import gr.hua.hellu.ExternalSites.inderScience;
import gr.hua.hellu.ExternalSites.ioPress;
import gr.hua.hellu.ExternalSites.scienceDirect;
import gr.hua.hellu.ExternalSites.scientificCommons;
import gr.hua.hellu.Objects.Author;
import java.util.ArrayList;

/**
 *
 * @author Moulou 
 * @version 1.0
 * contact me: moujan21@gmail.com
 *       site: www.dit.hua.gr/~it20818/
 */

public abstract class ParseGoogleData {
    
    protected ArrayList<String> Data;
    
    public ParseGoogleData(ArrayList<String> data){
        
        this.Data = data;
    }
    
    abstract public ArrayList<?> parseData();
        
    protected String getCitationsPage(String main) {

        String[] class_fl = main.split("class=\"gs_fl\"");

        if (class_fl[1].contains("Cited by ")) {
            String[] href = class_fl[1].split("href=");
            String[] hrefCite = href[1].split("\"");
            String[] A = hrefCite[1].split("\\?");
            String[] B = A[1].split("&");
            return B[0];
        }

        return "DOES_NOT_EXIST";
    }

    protected String getHrefPage(String main) {

        String[] h3 = main.split("<h3");
        String[] bhref = h3[1].split("/h3>");
        if (bhref[0].contains("href")) {
            String[] chref = bhref[0].split("<a href=");
            String[] href = chref[1].split("\"");
            return href[1];
        }
        return "No page found";
    }

    protected String getType(String main) {

        String[] h3 = main.split("h3");
        if (h3[1].contains("[PDF]")) {
            return "PDF";
        }
        if (h3[1].contains("[BOOK]")) {
            return "BOOK";
        }
        if (h3[1].contains("[HTML]")) {
            return "HTML";
        }
        if (h3[1].contains("[CITATION]")) {
            return "CITATION";
        }
        return "UNKNOW_TYPE";
    }

    protected String getTitle(String main) {

        String[] h3 = main.split("<h3");
        String h3b[] = h3[1].split("</h3>");
        if (h3b[0].contains("<a")) {
            String[] a = h3b[0].split("<a");
            String[] class_yC = a[1].split("\">");
            String[] title = class_yC[1].split("</a>");
            return title[0];
        }

        String span[] = h3b[0].split("</span></span>");
        return span[1];
    }

    protected ArrayList<Author> getAuthorsFromExternalSite(String href, String type, String title) {
        ArrayList<Author> authors = null;

        if (type.equals("PDF")) {

            authors = PDFs.getAuthors(title);
        } else if (href.contains("www.springerlink.com")) {
            authors = htmls.getAuthors(href);//springerLink.getAuthors(href);
        } else if (href.contains("ieeexplore.ieee.org")) {
            authors = htmls.getAuthors(href);//ieeexplore.getAuthors(href);
        } else if (href.contains("sciencedirect")) {
            authors = scienceDirect.getAuthors(href);
        } else if (href.contains("inderscience")) {
            authors = inderScience.getAuthors(href);

        } else if (href.contains("iopress")) {
            authors = ioPress.getAuthors(href);
        } else if (href.contains("scientificcommons")) {
            authors = scientificCommons.getAuthors(href);
        } else if (type.equals("HTML")) {
            authors = htmls.getAuthors(href);
        } else if (type.equals("BOOK") || href.contains("books.google.com")) {
            authors = PDFs.getAuthors(title);
        } else if (type.equals("CITATION")) {
            authors = null;
        } else {
            authors = htmls.getAuthors(href);
        }

        return authors;
    }

    protected ArrayList<Author> getAuthorsFromGoogle(String each) {
        String GreenText = each.split("div class=\"gs_a\">")[1].split("</div")[0];
        String GreenAuthors = GreenText.split(" - ")[0];

        if (GreenAuthors.contains("\u2026") || GreenAuthors.contains("&hellip;")) {
            return null;
        }

        String finalString = GreenAuthors.replaceAll("<b>", "");
        finalString = finalString.replaceAll("</b>", "");

        String[] Authors = finalString.split(",");

        return Extract.extractAuthors(Authors);
    }

    protected String getPublicationCode(String each) {
        String CODE = "";
        String A = each.split("gs_ocit")[1];
        CODE = A.split("'")[1];
        return CODE;
    }

    protected int getYear(String content) {

        String GreenText = content.split("div class=\"gs_a\"")[1].split("/div")[0];
        for (int i = 1900; i < 2020; i++) {
            String iS = i + "";
            if (GreenText.contains(" "+iS+" ")) {
                return i;
            }
        }
        return 0;
    }

    protected ArrayList<Author> getAuthorsFromCITE(String content) {

        if (!content.contains("\"gs_cit2\">")) {
            return null;
        } else {
            String Chicago[] = content.split("\"gs_cit2\">")[1].split("div");
            String StrAuth = "";
            if (Chicago[0].contains("\"")) {
                StrAuth = Chicago[0].split("\"")[0];
            } else {
                StrAuth = Chicago[0].split("<i>")[0];
            }

            StrAuth = StrAuth.replaceFirst(",", "");
            StrAuth = StrAuth.replaceAll("\\.", "");
            StrAuth = StrAuth.replaceAll(" and ", "");
            StrAuth = StrAuth.replaceAll("et al", "");
            String[] Authors = StrAuth.split(",");
            return Extract.extractAuthors(Authors);
        }
    }

    protected String getTitleFromCite(String content) {
        if (!content.contains("\"gs_cit2\">")) {
            return null;
        } else {
            String Chicago = content.split("\"gs_cit2\">")[1].split("div")[0];
            String Title;
            if (Chicago.contains("\"")) {
                Title = Chicago.split("\"")[1];
            } else {
                Title = Chicago.split("<i>")[1];
                Title = Title.split("</i>")[0];
            }     
           
            return Title;
        }
    }
}
