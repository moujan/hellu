package gr.hua.hellu.processData.Charts;

import gr.hua.hellu.Objects.CitedPublication;
import gr.hua.hellu.Objects.Publication;
import java.util.ArrayList;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Moulou 
 * @version 1.0
 * contact me: moujan21@gmail.com
 *       site: www.dit.hua.gr/~it20818/
 */
public class CreateDatasets {

    static private final int NO_CITE = 0;
    
    static public DefaultCategoryDataset createXes(int data[][], int[] year, int[] Values, String Var) {
        DefaultCategoryDataset result = new DefaultCategoryDataset();
        int[] he = data[0];
        for (int i = 0; i < year.length; i++) {
            if (year[i] == 0) {
                result.addValue(Values[i], Var, "Unknown year");
            } else if (year[i] != -1) {
                result.addValue(Values[i], Var, year[i] + "");
            }
        }
        return result;
    }

    static public int[][] createArrayDataYear(ArrayList<CitedPublication> publications, String TYPE ) {
        int[][] result = new int[2][100];
        //initialize array
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 100; j++) {
                result[i][j] = -1;
            }
        }
        
        ArrayList<Publication> choice = new ArrayList<Publication>();
        
        if ( TYPE.equals("PUBLICATION") ){
            for ( int i = 0; i < publications.size(); i++ ){
                choice.add((Publication)publications.get(i));
            }
            fillDataArray(choice, result);
        }
        else{
        
            for (int i = 0; i < publications.size(); i++) {
                if ( TYPE.equals("HETERO") )
                    choice = publications.get(i).getHeteroCitations();
                else if (TYPE.equals("SELF"))
                    choice = publications.get(i).getSelfCitations();

                fillDataArray(choice, result);

            }
        }
        result = addMissedYears(result, 1);

        return result;
    }

    private static void fillDataArray(ArrayList<Publication> choice, int[][] result ) {
        
        for (int cite = 0; cite < choice.size(); cite++) {
                int currentYear = choice.get(cite).getYear();
                int currentNum = 1;
                for (int y = 0; y < result[0].length; y++) {
                    if (result[0][y] == -1) {
                        result[0][y] = currentYear;
                        result[1][y] = currentNum;
                        break;
                    } else if (result[0][y] > currentYear) {
                        int tempYear = result[0][y];
                        result[0][y] = currentYear;
                        currentYear = tempYear;

                        int tempNum = result[1][y];
                        result[1][y] = currentNum;
                        currentNum = tempNum;
                    } else if (result[0][y] == currentYear) {
                        result[1][y]++;
                        break;
                    } else if (result[0][y] < currentYear) {
                        continue;
                    }
                }
            }
    }
    public static int[][] addMissedYears(int[][] result, int start) {
        int currentYear = 0;
        int currentNum = 0;
        int nextStart = 0;
        for ( int i = start; i < 100; i++ ){
            if ( result[0][i+1] == -1 ){
                return result;
            }
            else if ( result[0][i+1] - result[0][i] == 1 ){
                continue;
            }
            else if ( result[0][i+1] - result[0][i] > 1 ){
                currentYear = result[0][i+1];
                currentNum = result[1][i+1];
                
                result[0][i+1] = result[0][i]+1;
                result[1][i+1] = NO_CITE;
                nextStart = i+1;
                break;
            }
        }
        int tempYear = 0;
        int tempNum = 0;
        for ( int i = nextStart+1; i < 100; i++ ){
            tempYear = result[0][i];
            tempNum = result[1][i];
               
            result[0][i] = currentYear;
            result[1][i] = currentNum;
            
            if ( tempYear == -1 ) {break;}
            
            currentYear = tempYear;
            currentNum = tempNum;
        }
        return addMissedYears( result, nextStart );
        
    }
    
    public static int [][] mergeData(int [][]array1, int [][]array2){
        
        int result[][] = new int[3][100];
        for( int i = 0; i < 3; i++ ){
            for ( int j = 0; j < 100; j++ ){
                result[i][j] = -1;
            }
        }
        
        int earlyYear = 0;
        int laterYear = 0;
        
        if ( array1[0][1] <= array2[0][1] && array1[0][1] != -1)
            earlyYear = array1[0][1];
        else earlyYear = array2[0][1];
        
        int latest1 = -1;
        int latest2 = -1;
        for( int i = 0; i < 100; i++ ){
            if ( latest1 > -1 && latest2 > -1 ) break;
            
            if ( array1[0][1] == -1 ) latest1 = 0;
            else if( array1[0][i+1] == -1 && latest1 == -1 ) latest1 = array1[0][i];
            
            if ( array2[0][1] == -1 ) latest2 = 0;
            else if( array2[0][i+1] == -1 && latest2 == -1 ) latest2 = array2[0][i];
        }
        if ( latest1 >= latest2)
            laterYear = latest1;
        else laterYear = latest2;
        //create the years!!!
        result[0][0] = 0;
        result[0][1] = earlyYear;
        for ( int i = 2; i < 100; i++ ){
            if ( result[0][i-1] == laterYear ) break;
            
            result[0][i] = result[0][i-1]+1;
        }
        
        int botomLimit = 0;
        //create first and second bar!!
        //find earlier year for array1
        for ( int  i = 1; i < 100; i++ ){
            if ( array1[0][1] == result[0][i] ){
                botomLimit = i;
                break;
            }
        }
        result[1][0] = array1[1][0];
        for ( int i = 1, j=1; i < 100; i++ ){
            if ( i < botomLimit){
                result[1][i] = 0;
            }
            else{
                if ( result[0][i] == -1) break;
                if ( array1[0][j] == -1)
                    result[1][i] = 0;
                else
                    result[1][i] = array1[1][j];
                j++;
            }
        }
        
        
        for ( int  i = 1; i < 100; i++ ){
            if ( array2[0][1] == result[0][i] ){
                botomLimit = i;
                break;
            }
        }
        result[2][0] = array2[1][0];
        for ( int i = 1, j=1; i < 100; i++ ){
            if ( i < botomLimit){
                result[2][i] = 0;
            }
            else{
                if ( result[0][i] == -1) break;
                if ( array2[0][j] == -1)
                    result[2][i] = 0;
                else
                    result[2][i] = array2[1][j];
                j++;
            }
        }
        
        return result;
        
    }
}
