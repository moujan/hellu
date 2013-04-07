package gr.hua.hellu.processData.Metrics;

import gr.hua.hellu.Objects.CitedPublication;
import java.util.ArrayList;

/**
 *
 * @author Moulou 
 * @version 1.0
 * contact me: moujan21@gmail.com
 *       site: www.dit.hua.gr/~it20818/
 */

public class Metrics {
    
    public static final int CITATION_CHOICE = 1;
    public static final int HETERO_CHOICE = 2;
    public static final int SELF_CHOICE = 3;
    private ArrayList<CitedPublication> publications;

    public Metrics(ArrayList<CitedPublication> publications) {
        this.publications = publications;
    }

    public CitedPublication[] getDescArray(int choice) {
        CitedPublication[] array = publications.toArray(new CitedPublication[]{});
      
        if (choice == CITATION_CHOICE) {
            for ( int i = 0; i< array.length; i++ ){
                for ( int j = i+1; j < array.length; j++ ){
                    if ( array[j].getCitations() > array[i].getCitations() ){
                        CitedPublication temp = array[i];
                        array[i] = array[j];
                        array[j] = temp;
                    }
                }
            }
        } else if (choice == HETERO_CHOICE) {
            for ( int i = 0; i< array.length; i++ ){
                for ( int j = i+1; j < array.length; j++ ){
                    if ( array[j].getHeteroCitations().size() > array[i].getHeteroCitations().size() ){
                        CitedPublication temp = array[i];
                        array[i] = array[j];
                        array[j] = temp;
                    }
                }
            }
        } else if (choice == SELF_CHOICE) {
            for ( int i = 0; i< array.length; i++ ){
                for ( int j = i+1; j < array.length; j++ ){
                    if ( array[j].getSelfCitations().size() > array[i].getSelfCitations().size() ){
                        CitedPublication temp = array[i];
                        array[i] = array[j];
                        array[j] = temp;
                    }
                }
            }
        }

        return array;
    }
    //

    public int gethIndex(int choice) {
        int h_index = 0;
        CitedPublication[] array = this.getDescArray(choice);
        for (int i = 0; i < array.length; i++) {
            if (choice == CITATION_CHOICE) {
                if (array[i].getCitations() >= (i + 1) ) {
                    h_index = i + 1;
                } else {
                    break;
                }
            } else if (choice == HETERO_CHOICE) {
                if (array[i].getHeteroCitations().size() >= (i + 1) ) {
                    h_index = i + 1;
                } else {
                    break;
                }

            } else if (choice == SELF_CHOICE) {
                if (array[i].getSelfCitations().size() >= (i + 1) ) {
                    h_index = i + 1;
                } else {
                    break;
                }

            }
        }
        return h_index;
    }

    public int getgIndex(int choice) {
        int g_index = 0;
        int g = 0;
        CitedPublication[] array = this.getDescArray(choice);

        for (int i = 0; i < publications.size(); i++) {

            if (choice == CITATION_CHOICE) {
                g += array[i].getCitations();
                if (g >= i * i) {
                    g_index = i;
                } else {
                    break;
                }
            } else if (choice == HETERO_CHOICE) {
                g += array[i].getHeteroCitations().size();
                if (g >= i * i) {
                    g_index = i;
                } else {
                    break;
                }

            } else if (choice == SELF_CHOICE) {
                g += array[i].getSelfCitations().size();
                if (g >= i * i) {
                    g_index = i;
                } else {
                    break;
                }
            }

        }
        return g_index;

    }

    public double getARindex(int choice) {

        int h_index = this.gethIndex(choice);
        CitedPublication[] array = this.getDescArray(choice);

        double Sum = 0.0;
        for (int i = 0; i < h_index; i++) {
            
            int div;
            if ( array[i].getYear() == 0 )
                div = 1;
            else div = (2014 - array[i].getYear());
            
            if (choice == CITATION_CHOICE) {
                Sum += array[i].getCitations() / div;
            } else if (choice == HETERO_CHOICE) {
                Sum += array[i].getHeteroCitations().size() / div;
            } else if (choice == SELF_CHOICE) {
                Sum += array[i].getSelfCitations().size() / div;
            }
        }
        return Math.sqrt(Sum);

    }
}
