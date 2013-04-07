package gr.hua.hellu.searchData.googleResults;

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

public class Extract {

    public static ArrayList<Author> extractAuthors(String display[]) {
        ArrayList<Author> authors = new ArrayList<Author>();

        if (display != null) {
            for (int i = 0; i < display.length; i++) {

                if ( display[i].contains(">") ){
                    display[i] = display[i].split(">")[1];
                    display[i] = display[i].split("<")[0];
                }
                
                if (display[i].contains("  ")) {
                    display[i] = display[i].replaceAll("  ", " ");
                }
                if (display[i].startsWith(" ")) {
                    display[i] = display[i].replaceFirst(" ", "");
                }
                String[] name = display[i].split(" ");
                for ( int j = 0; j < name.length; j++ ) {
                    if (name[j].contains(".")) {
                        name[j] = name[j].replaceAll("[.]", "");
                    }
                }
                
                String []name2 = splitAuthorMiddleName(name);
                
                authors.add(new Author(name2));
            }
        }
        return authors;
    }

    public static void cleanData(CitedPublication MainPub, ArrayList<Publication> citations) {
        
        for ( int i = 0; i < citations.size(); i++ ){
            if ( MainPub.getYear() > citations.get(i).getYear() )
                citations.get(i).setYear(0);
        }
    }

    private static String [] splitAuthorMiddleName(String[] name) {
        int willBeSplitted = -1;
        int isUpper = 0;
        for ( int i = 0; i < name.length; i++ ){
            if ( isUpperCase(name[i]) ){
                isUpper++;
                if ( name[i].length() == 2)
                    willBeSplitted = i;
            }
        }
        if ( isUpper == 1 && willBeSplitted != -1 ){
            if ( isUpperCase(name[willBeSplitted]) ){
                String []name2 = new String [name.length+1];

                String [] letters = splitMiddleName(name[willBeSplitted]);

                for ( int i = 0; i < letters.length; i++ ){
                    name2[i] = letters[i];
                }

                for ( int i = 0, j = letters.length; i < name.length; i++){
                    if ( i == willBeSplitted ) continue;
                    else {
                        name2[j] = name[i]; j++;
                    }
                } 
                return name2;
            }
        }
        return name;
        
    }

    private static String[] splitMiddleName(String string) {
        char [] letters = string.toCharArray();
        String []name = new String[letters.length];
       
        for ( int i = 0; i < name.length; i++ ){
            name[i] = ""+letters[i];
        }
        return name;
    }

    private static boolean isUpperCase(String string) {
        for ( int i = 0; i < string.length(); i++ ){
            if ( Character.isLowerCase(string.charAt(i)) )
                return false;
        }
        return true;
    }
}


