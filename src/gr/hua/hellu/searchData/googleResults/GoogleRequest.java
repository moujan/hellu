
package gr.hua.hellu.searchData.googleResults;

import gr.hua.hellu.Connection.HttpConnection;
import gr.hua.hellu.Connection.HttpExceptionHandler;
import java.util.ArrayList;

/**
 *
 * @author Moulou 
 * @version 1.0
 * contact me: moujan21@gmail.com
 *       site: www.dit.hua.gr/~it20818/
 */

public class GoogleRequest {
    
    private String PAGE = "Google Scholar";
    public int AUTHOR = 100;
    public int CITATION = 200;    
    
    private ArrayList<String> ListOfElelements;
    private String query;
    //private HttpConnection httpClient;
    
    public GoogleRequest(String mainQuery){
        
        this.query = mainQuery;
        this.ListOfElelements = new ArrayList<String>();
        //this.httpClient = new HttpConnection(null, this.PAGE);
    }
       
    //set query for Google 
    private String ConvertSimpleQueryToGSQuery(String mainQuery) {
        String queryURL = "";
        mainQuery = mainQuery.replaceAll("\\s+", " ");
        String[] splitMainQuery = mainQuery.split(" ");
        for (int i = 0; i < splitMainQuery.length; i++) {
            queryURL += "author:" + splitMainQuery[i].toString();
            if (i != splitMainQuery.length - 1) {
                queryURL += "+";
            }
        }
        return queryURL;
    }
    
    public ArrayList<String> getPublications(boolean mainSearch){
        String URL = "";
        int pages = 0;
        String publications[];
        String content = "";
        
        //In case where there isn't citation page...
        if (this.query.equals("DOES_NOT_EXIST")) {
            return null;
        }
        
        while(true){
        
            if ( mainSearch ){//it is the case of main publications
                //publications
                URL = "http://scholar.google.com/scholar?start=" + pages + "&q=" + ConvertSimpleQueryToGSQuery(this.query) + "&hl=en&num=20&sciui=1&as_sdt=0,5/";
            } else{//citations for a publication
                //citations
                URL = "http://scholar.google.com/scholar?start=" + pages + "&hl=en&as_sdt=0&num=20&sciodt=0&" + this.query + "&scipsc=";
            }
            HttpConnection httpClient = new HttpConnection(URL, this.PAGE);
            httpClient.connect();
            HttpExceptionHandler httpHandler = new HttpExceptionHandler(httpClient);
            httpClient = httpHandler.checkStatement();
                        
            if ( httpClient == null )break;
            else if ((content = httpClient.getContent() ).equals("")) {
                break;
            }
            
            publications = content.split("<div class=\"gs_r\"");


            String lastPublication = publications[publications.length - 1];
            String[] last = lastPublication.split(">Create alert<");

            publications[publications.length - 1] = last[0];

            
            if (publications.length <= (20+1) ){
                this.ListOfElelements.addAll(TransportPublicationsToAList(publications, 1));
            } else {//contains a profil for the author
                this.ListOfElelements.addAll(TransportPublicationsToAList(publications, 2));
            }
            //delay after every site
            delay();

            if (last[1].contains("visibility:hidden\">Next") || publications.length == 0
                    || (!last[1].contains("Previus") && !last[1].contains("Next"))) {
                break;
            }

            httpClient.closeConnection();
            pages += 20;       
        
        }
        
        return this.ListOfElelements;
    }
    
    //fill in the ArrayList with publications
    private static ArrayList TransportPublicationsToAList(String[] array, int start) {
        ArrayList<String> List = new ArrayList<String>();
        for (int i = start; i < array.length; i++) {
            List.add(array[i]);
        }
        return List;
    }
    
    
    //delay for each google request
    private void delay(){
        double count = 0.0;
        //-0.50
        while (count < 5000000000.0) {         
                count++;
        }
    }
    
    
}
