package gr.hua.hellu.Objects;

/**
 *
 * @author Moulou 
 * @version 1.0
 * contact me: moujan21@gmail.com
 *       site: www.dit.hua.gr/~it20818/
 */

public class Author {
    String [] name;

    public Author( String [] name ){
        
        this.name = name;
    }

    public String[] getName() {
        return name;
    }
    
    @Override
    public String toString(){
        String Hname = "";
        for ( int i = 0; i < name.length; i++ ){
            Hname += name[i];
            if ( i != name.length-1 )
                Hname += " ";
        }
        return Hname;
    }
    
    public String toStringBIB(){
        String Hname = "";
        for ( int i = 0; i < name.length; i++ ){
            Hname += name[i];
            if ( i != name.length-1 )
                Hname += ", ";
        }
        return Hname;
    }
}
