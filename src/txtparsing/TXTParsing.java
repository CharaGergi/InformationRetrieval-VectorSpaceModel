package txtparsing;

import utils.IO;
import java.util.ArrayList;
import java.util.List;

public class TXTParsing {
    public static List<MyDoc> parse(String file) throws Exception {
        try{
            //Parse txt file
            String txt_file = IO.ReadEntireFileIntoAString(file);

            //Split document to individual docs
            String[] docs = txt_file.split("(\n[.]I)");
            System.out.println("Read: "+docs.length + " docs");

            //Parse each document from the txt file
            List<MyDoc> parsed_docs= new ArrayList<MyDoc>();
            for (String doc:docs){
                String[] adoc = doc.split("(\n[.]T)|(\n[.]W)|(\n[.]X)");

                String authors = "";
                String title = "";
                int iend = adoc[1].indexOf(".A" );
                if (iend != -1)
                {
                    title = adoc[1].substring(0, iend);
                    authors = adoc[1].substring(iend+1);
                    authors = authors.replaceAll("(\n[.]A)", "");
                }
                //= adoc[1].substring(adoc[1].indexOf(".A"));
                /*if (adoc.length == 6){
                    adoc[2] = adoc[2] + adoc[3];
                    //System.out.println(adoc[2]);
                    adoc[3] =  adoc[4];
                    adoc[4] = adoc[5];
                }*/
                //Get index of space and then find the numerical part of the id
                String id_index = adoc[0];
                int index = adoc[0].indexOf(" ");
                id_index = adoc[0].substring(index+1).replaceAll("\\s+","");

                //Citations are stored at index 4 of the adoc array
                //Split each row of citations and save at another array
                String[] citation  = adoc[3].split("\n");

                //The final citation arrays that will contain the triplets
                ArrayList<String[]> citations = new ArrayList<String[]>();

                //Each index of citation array is split to triplets and saved to the final array.
                for (String c : citation){
                    String[] citation_triplet = c.split("\t");
                    //System.out.println("Length of citation triplet " + citation_triplet.length);
                    citations.add(citation_triplet);
                }
                //Make the id int
                int id = Integer.parseInt(id_index);
                //Create the doc and add it to parsed_docs
                MyDoc mydoc = new MyDoc( id ,title ,authors, adoc[2], citations);
                parsed_docs.add(mydoc);

            }
            System.out.println("TITLE " + parsed_docs.get(276).getTitle());
            System.out.println("AUTHORS " + parsed_docs.get(276).getAuthor());
            return parsed_docs;
        } catch (Throwable err) {
            err.printStackTrace();
            return null;
        }
        
    }

}
