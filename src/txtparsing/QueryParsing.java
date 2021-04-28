package txtparsing;

import utils.IO;

import javax.management.Query;
import java.util.ArrayList;
import java.util.List;

public class QueryParsing {
    public static List<String[]> parse(String file) throws Exception {
        try{
            //Parse txt file
            String txt_file = IO.ReadEntireFileIntoAString(file);
            //Split queries based on id
            String[] queries = txt_file.split("(\n[.]I)");
            System.out.println("Read: "+queries.length + " queries");

            //Parse each document from the txt file
            List<String[]> parsed_queries= new ArrayList<String[]>();
            for (String query:queries){
                String[] adoc = query.split("(\n[.]W)");
                adoc[1] = adoc[1].replaceAll("[^\\w\\d\\s]" , "");

                parsed_queries.add(adoc);
            }
            System.out.println("Queries" + parsed_queries.get(55)[1] + "\n" + parsed_queries.get(10)[1]);
            return parsed_queries;
        } catch (Throwable err) {
            err.printStackTrace();
            return null;
        }

    }

}
