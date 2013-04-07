package gr.hua.hellu.Connection;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author Moulou 
 * @version 1.0
 * contact me: moujan21@gmail.com
 *       personal website: www.dit.hua.gr/~it20818/
 */
public class HttpExceptionHandler {
    
    HttpConnection currentConnect;
    
    public HttpExceptionHandler(HttpConnection currentConnect){
        this.currentConnect = currentConnect;
        
    }
    
    public HttpConnection checkStatement(){
        int STATUS = this.currentConnect.getConnectionStatus();
        if (STATUS == 200)//CONNECTED
            return this.currentConnect;//"Get same user.";
        
        if ( STATUS == 503 )//SERVICE UNAVAILABLE
            return null;//"Service Unavailable - Access denied.";
        
        if ( STATUS == 401 )//UNAUTHORIZED
            return SSLConnection();//"Create Authorized User.";
        
        //return "Unknown Error - Null Page.";
        return null;
    }
    
    public HttpConnection SSLConnection(){
        
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException { }

                @Override
                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException { }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = this.currentConnect.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));
            DefaultHttpClient d = new DefaultHttpClient(ccm, this.currentConnect.getParams());
            
        return new HttpConnection(this.currentConnect.getURL(), this.currentConnect.getURL(), 
                ccm, this.currentConnect.getParams());
        }catch (NoSuchAlgorithmException ex){
            return null;
        }catch ( KeyManagementException ex ){
            return null;
        }
    }
}
