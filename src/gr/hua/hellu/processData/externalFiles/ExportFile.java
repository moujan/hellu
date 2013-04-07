package gr.hua.hellu.processData.externalFiles;

import gr.hua.hellu.Objects.CitedPublication;
import gr.hua.hellu.Objects.Publication;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Moulou 
 * @version 1.0
 * contact me: moujan21@gmail.com
 *       site: www.dit.hua.gr/~it20818/
 */

public class ExportFile {
    public static void createXMLFILE(ArrayList<CitedPublication> P, String author, String chosenFile) {

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            //root elements is the author
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("author");
            rootElement.setAttribute("name", author);
            doc.appendChild(rootElement);

            //publication elements
            for (int i = 0; i < P.size(); i++) {
                //each publication element
                Element publicationElement = doc.createElement("publication");
                publicationElement.setAttribute("id", "" + (i + 1));

                // title element
                Element titleElement = doc.createElement("title");
                titleElement.appendChild(doc.createTextNode(P.get(i).getTitle()));
                publicationElement.appendChild(titleElement);

                //authors element
                Element authorsElement = doc.createElement("authors");
                authorsElement.appendChild(doc.createTextNode(P.get(i).printAuthors()));
                publicationElement.appendChild(authorsElement);

                //site element
                Element siteElement = doc.createElement("site");
                siteElement.appendChild(doc.createTextNode(P.get(i).getHrefPage()));
                publicationElement.appendChild(siteElement);

                //year element
                Element yearElement = doc.createElement("year");
                yearElement.appendChild(doc.createTextNode(""+P.get(i).getYear()));
                publicationElement.appendChild(yearElement);
                
                //citations element
                Element citationsElement = doc.createElement("citations");

                Element heteroElement = doc.createElement("hetero");
                Element selfElement = doc.createElement("self");

                ArrayList<Publication> hetero = P.get(i).getHeteroCitations();
                ArrayList<Publication> self = P.get(i).getSelfCitations();

                for (int hj = 0; hj < hetero.size(); hj++) {
                    //each citation element
                    Element citationElement = doc.createElement("citation");

                    // title element
                    Element titleCElement = doc.createElement("title");
                    titleCElement.appendChild(doc.createTextNode(hetero.get(hj).getTitle()));
                    citationElement.appendChild(titleCElement);

                    //authors element
                    Element authorsCElement = doc.createElement("authors");
                    authorsCElement.appendChild(doc.createTextNode(hetero.get(hj).printAuthors()));
                    citationElement.appendChild(authorsCElement);

                    //site element
                    Element siteCElement = doc.createElement("site");
                    siteCElement.appendChild(doc.createTextNode(hetero.get(hj).getHrefPage()));
                    citationElement.appendChild(siteCElement);
                    //year element
                    Element yearCElement = doc.createElement("year");
                    yearCElement.appendChild(doc.createTextNode(""+hetero.get(hj).getYear()));
                    publicationElement.appendChild(yearElement);

                    heteroElement.appendChild(citationElement);
                }

                for (int sj = 0; sj < self.size(); sj++) {
                    //each citation element
                    Element citationElement = doc.createElement("citation");

                    // title element
                    Element titleCElement = doc.createElement("title");
                    titleCElement.appendChild(doc.createTextNode(self.get(sj).getTitle()));
                    citationElement.appendChild(titleCElement);

                    //authors element
                    Element authorsCElement = doc.createElement("authors");
                    authorsCElement.appendChild(doc.createTextNode(self.get(sj).printAuthors()));
                    citationElement.appendChild(authorsCElement);

                    //site element
                    Element siteCElement = doc.createElement("site");
                    siteCElement.appendChild(doc.createTextNode(self.get(sj).getHrefPage()));
                    citationElement.appendChild(siteCElement);
                    //year element
                    Element yearCElement = doc.createElement("year");
                    yearCElement.appendChild(doc.createTextNode(""+self.get(sj).getYear()));
                    publicationElement.appendChild(yearElement);

                    selfElement.appendChild(citationElement);
                }

                citationsElement.appendChild(heteroElement);
                citationsElement.appendChild(selfElement);
                publicationElement.appendChild(citationsElement);


                rootElement.appendChild(publicationElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(doc);
            StreamResult result;
            if ( chosenFile.endsWith(".xml") ){
                result = new StreamResult(new File(chosenFile));
            }
            else{
                result = new StreamResult(new File(chosenFile + ".xml"));
            }


            transformer.transform(source, result);


        } catch (TransformerException ex) {
            Logger.getLogger(ExportFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ExportFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void createRTFFILE(ArrayList<CitedPublication> P, String author) {

        try {
            String content = setContent(P, author);
            File file = new File("rtf files/" + author + ".rtf");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(ExportFile.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println(ex.getLocalizedMessage() + " LUKAS");
                }
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(content);

            bw.close();

        } catch (IOException ex) {
            Logger.getLogger(ExportFile.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getLocalizedMessage() + " HELEN");
        }

    }

    private static String setContent(ArrayList<CitedPublication> P, String author) {
        String content = "";

        content += "Author's name\n" + author + "\n\nNumber of publicationes: " + P.size();

        int citationsNUM = 0;
        int heteroNUM = 0;
        int selfNUM = 0;

        for (int i = 0; i < P.size(); i++) {
            citationsNUM += P.get(i).getCitations();
            heteroNUM += P.get(i).getHeteroCitations().size();
            selfNUM += P.get(i).getSelfCitations().size();
        }
        content += "\nNumber of citations: " + citationsNUM + "\nNumber of self citations: " + selfNUM
                + "\nNumber of hetero citations: " + heteroNUM;

        for (int i = 0; i < P.size(); i++) {

            CitedPublication Pi = P.get(i);
            ArrayList<Publication> heteroC = Pi.getHeteroCitations();
            ArrayList<Publication> selfC = Pi.getSelfCitations();

            content += "\n\nPublish (" + (i + 1) + ")\n\tTitle: " + Pi.getTitle()
                    + "\n\tAuthors: " + Pi.printAuthors() + "\n\tSite: " + Pi.getHrefPage() + "\n\tNumber of citations: "
                    + (heteroC.size() + selfC.size()) + "\n\t";

            for (int hj = 0; hj < heteroC.size(); hj++) {
                content += "\n\n\tCitation (" + (1 + hj) + ") (hetero)";

                content += "\n\t\tTitle: " + heteroC.get(hj).getTitle()
                        + "\n\t\tAuthors: " + heteroC.get(hj).printAuthors()
                        + "\n\t\tSite: " + heteroC.get(hj).getHrefPage();

            }

            for (int sj = 0; sj < selfC.size(); sj++) {
                content += "\n\n\tCitation (" + (sj + heteroC.size() + 1) + ") (self)";

                content += "\n\t\tTitle: " + selfC.get(sj).getTitle()
                        + "\n\t\tAuthors: " + selfC.get(sj).printAuthors()
                        + "\n\t\tSite: " + selfC.get(sj).getHrefPage();
            }
        }
        return content;
    }
    
    
    
    public static void createBibTexFile(ArrayList<CitedPublication> P, String chosenFile){
        String content = "";
        
        for ( int i = 0; i < P.size(); i++ ){
            content += createBibTexElement(P.get(i));
        }
        try {
            File file;
            if ( chosenFile.endsWith(".bib")){
            file = new File( chosenFile );
            }else {
                file = new File( chosenFile + ".bib" );
            }
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(ExportFile.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println(ex.getLocalizedMessage() + " LUKAS");
                }
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(content);

            bw.close();

        } catch (IOException ex) {
            Logger.getLogger(ExportFile.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getLocalizedMessage() + " HELEN");
        }  
        
    }
    
    private static String createBibTexElement(CitedPublication Pi){
        String element = "";
        
        String year = "";
        if ( Pi.getYear() != 0 )
            year = ""+ Pi.getYear();
        String title = Pi.getTitle();
        String authors = Pi.printAuthorsBIB();
        String site = Pi.getHrefPage();
        String author[] = Pi.getAuthors().get(0).getName();
        if (author.length == 1)
            element = "@article{"+author[0]+year+", \n\t"
                + "title={"+title+"}, \n\t"
                + "author={"+authors+"}, \n\t"
                + "year={"+year+"}, \n\t"
                + "site={"+site+"}\n}\n\n";        
        else
            element = "@article{"+author[0]+author[1]+year+", \n\t"
                + "title={"+title+"}, \n\t"
                + "author={"+authors+"}, \n\t"
                + "year={"+year+"}, \n\t"
                + "site={"+site+"}\n}\n\n"; 
        
        return element;
    }
}
