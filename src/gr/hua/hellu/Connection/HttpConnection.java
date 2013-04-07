package gr.hua.hellu.Connection;

import java.io.IOException;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;

/**
 *
 * @author Moulou 
 * @version 1.0
 * contact me: moujan21@gmail.com
 *       site: www.dit.hua.gr/~it20818/
 */
@SuppressWarnings( "deprecation" )
public class HttpConnection extends DefaultHttpClient{
    
    //-------------> ERRORS ON LOADING PAGE
    protected final String SURVICE_UNAVAILABLE = "service unavailable";
    protected final String UNAUTHORIZED = "peer not authenticated : unauthenticated";
    
    protected final int CONNECTED = 200;
    protected final int SERVICE_UNAVAILABLE_ER = 503;
    protected final int UNAUTHORIZED_ER = 401;
    
    protected final int UNKNOWN_ERROR = 1000;
    //-------------/
    private int ConnectionStatus;
    
    private HttpGet httpget;
    private ResponseHandler<String> responseHandler;
    private String Content;
    
    private String URL;
    private String TYPE_OF_SITE;
    
    
    
    //constructors
    public HttpConnection(String URL, String PAGE){
        //get DefaultHttpClient methods
        super();
        
        //like common user and not like system.
        this.getParams().setParameter("http.useragent","Browser at Client level");
        
        this.URL = URL;
        this.TYPE_OF_SITE = PAGE;
    }
    //secure connection
    public HttpConnection(String URL, String PAGE, ClientConnectionManager ccm, HttpParams params ){
        //get DefaultHttpClient methods
        super(ccm, params);
        
        //like common user and not like system.
        this.getParams().setParameter("http.useragent","Browser at Client level");
        
        this.URL = URL;
        this.TYPE_OF_SITE = PAGE;
    }
   
    public void setURL(String URL){ 
        this.URL = URL;
    }
    public String getURL(){ 
        return this.URL;
    }
    public String getSiteType(){
        return this.TYPE_OF_SITE;
    }
    //Status of connection
    public void connect(){
        
        try{
            
            System.out.println("Try to connect to site: " + this.TYPE_OF_SITE + "\nLoading page:\n" + this.URL
                    + "\n\n");
            
            //create status for the connection
            this.httpget = new HttpGet(URL);
            this.responseHandler = new BasicResponseHandler();
            this.Content = this.execute(httpget, responseHandler);
            
            System.out.println("Successful connect to site: " + this.TYPE_OF_SITE + "\nLoading page:\n" + this.URL
                    + " with method: " + httpget.getMethod() + "\n\n");
            
        }catch(IOException ex){
            
            System.out.println("Problem on loading " + this.TYPE_OF_SITE + " - Reason: "
                    + ex.getLocalizedMessage() + "\nURL: " + URL + "\n");
            
            if ( ex.getLocalizedMessage().equalsIgnoreCase(this.SURVICE_UNAVAILABLE))
                this.ConnectionStatus = this.SERVICE_UNAVAILABLE_ER;
            
            if ( this.UNAUTHORIZED.contains(ex.getLocalizedMessage()) )
                this.ConnectionStatus = this.UNAUTHORIZED_ER;
            
            this.Content = "";
        }
        
        this.ConnectionStatus = this.CONNECTED;
    }
    
    public int getConnectionStatus(){
        return this.ConnectionStatus;
    }
    
    public String getContent(){
        //Get the content (html) page
        return StringEscapeUtils.unescapeHtml4(this.Content);
    }
    
    //close connection with PAGE
    public void closeConnection(){
        super.getConnectionManager().shutdown();
    }
    
    
}
