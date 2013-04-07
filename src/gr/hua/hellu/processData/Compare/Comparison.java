package gr.hua.hellu.processData.Compare;

import gr.hua.hellu.Objects.Author;

/**
 *
 * @author Moulou 
 * @version 1.0
 * contact me: moujan21@gmail.com
 *       site: www.dit.hua.gr/~it20818/
 */

public class Comparison {
    
    private final int ZERO = 0;
    private final int LETTER_TO_LETTER = 1;
    private final int LETTER_TO_WORD = 2;
    private final int WORD_TO_WORD = 4;
    
    private Author A;
    private Author B;
    
    public Comparison(Author A, Author B){
        
        this.A = A;
        this.B = B;
    
    }
    
    public boolean isTheSameAuthor(){
    
        int Asize = A.getName().length;
        int Bsize = B.getName().length;

        int[][] index = new int[Asize][Bsize];
        
        for ( int i = 0; i < Asize; i++ ){
            for ( int j = 0; j < Bsize; j++ ){
                index[i][j] = ZERO;
            }
        }

        for (int i = 0; i < Asize; i++) {
            for (int j = 0; j < Bsize; j++) {
                if (B.getName()[j].length() <= 2 && A.getName()[i].startsWith(B.getName()[j])) {
                    if (A.getName()[i].length() <= 2) {
                        index[i][j] = LETTER_TO_LETTER;
                    } else {
                        index[i][j] = LETTER_TO_WORD;
                    }

                } else if (A.getName()[i].length() <= 2 && B.getName()[j].startsWith(A.getName()[i])) {
                    if (B.getName()[j].length() <= 2) {
                        index[i][j] = LETTER_TO_LETTER;
                    } else {
                        index[i][j] = LETTER_TO_WORD;
                    }
                }else if (A.getName()[i].equalsIgnoreCase(B.getName()[j]) && A.getName()[i].length() >= 2 
                        && B.getName()[j].length() >= 2) 
                    index[i][j] = WORD_TO_WORD;
            }
        }
        int isSame = 0;
        int largerNum = 0;
        
        for ( int i = 0; i < Asize; i++ ){
            largerNum = 0;
            for ( int j = 0; j < Bsize; j++ ){
                if ( largerNum < index[i][j] )
                    largerNum = index[i][j];
                else index[i][j] = ZERO;
            }
        }

        for ( int j = 0; j < Bsize; j++ ){
            largerNum = 0;
            for ( int i = 0; i < Asize; i++ ){
                if ( largerNum < index[i][j] )
                    largerNum = index[i][j];
                else index[i][j] = ZERO;
            }
        }        
        
        for ( int i = 0; i < Asize; i++ ){
            for ( int j = 0; j < Bsize; j++ ){
                isSame += index[i][j];
            }
        }
        
        if ( isSame >= 5 ) return true;
        
        return false;
    }
}
