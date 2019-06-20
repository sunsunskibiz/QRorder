 import org.apache.jena.rdf.model.*;
 import org.apache.jena.util.FileManager;
 import org.apache.log4j.varia.NullAppender;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class Rdf {
    private Model model;
//
//    public static void main(String[] args) throws IOException {
//       Rdf r = new Rdf();
//       String pathDIR = "D:\\Project\\marvenEX\\prj01\\src\\main\\java\\tu\\myorg\\";
//       String pathFile = pathDIR + "test.ttl";
//        pathFile = "D:\\newProject\\out\\now\\T15_20190611103433.ttl";
////        ArrayList<String[]> arrListOrdered = r.listMenuStatus(pathFile);
//        if (r.changeStatusToserved(pathFile)) {
//            System.out.println("changeStatusToserved success.");
//        } else {
//            System.out.println("changeStatusToserved failed");
//        }
//        System.out.println();
//        System.out.println("After change to paid");
//        r.listMenuStatus(pathFile);
//    }

     public boolean changeStatusTopaid(String file) throws IOException {
         model = readModel(file);
         Model newModel = ModelFactory.createDefaultModel();

         // list the statements in the graph
         StmtIterator iter = model.listStatements();

         // print out the predicate, subject and object of each statement
         while (iter.hasNext()) {
             Statement stmt      = iter.nextStatement(); // get next statement
             Resource  subject   = stmt.getSubject();   // get the subject
             Property  predicate = stmt.getPredicate(); // get the predicate

             Statement newStmt = newModel.createStatement(subject, predicate, "Paid");
             newModel.add(newStmt);
         }

         if (deleteRdfFile(file)) {
             // now write the model in XML form to a file
             FileWriter write = new FileWriter(file, true);
             PrintWriter printLine = new PrintWriter(write);
             newModel.write(printLine, "TTL");
             printLine.close();

             System.out.println("Print to change to paid");
             return true;
         } else {
             System.out.println("changeStatusTopaid is failed");
             return false;
         }

     }

    public boolean changeStatusToserved(String file, String menu) throws IOException {
        model = readModel(file);
        Model newModel = ModelFactory.createDefaultModel();

        // list the statements in the graph
        StmtIterator iter = model.listStatements();

        // print out the predicate, subject and object of each statement
        while (iter.hasNext()) {
            Statement stmt      = iter.nextStatement(); // get next statement
            Resource  subject   = stmt.getSubject();   // get the subject
            Property  predicate = stmt.getPredicate(); // get the predicate
            RDFNode   object    = stmt.getObject();

            String individualMenu = predicate.toString().substring(19);
            Statement newStmt;
            if (individualMenu.equals(menu)) {
                newStmt = newModel.createStatement(subject, predicate, "Served");
            } else {
                newStmt = newModel.createStatement(subject, predicate, object);
            }
            newModel.add(newStmt);
        }

        if (deleteRdfFile(file)) {
            // now write the model in XML form to a file
            FileWriter write = new FileWriter(file, true);
            PrintWriter printLine = new PrintWriter(write);
            newModel.write(printLine, "TTL");
            printLine.close();

            return true;
        } else {
            System.out.println("changeStatusToserved is failed");
            return false;
        }

    }

     public ArrayList<String[]> listMenuStatus(String file) {
         model = readModel(file);
         ArrayList<String[]> menuStatus = new ArrayList<String[]>();
         // list the statements in the graph
         StmtIterator iter = model.listStatements();

         // print out the predicate, subject and object of each statement
         while (iter.hasNext()) {
             Statement stmt      = iter.nextStatement();         // get next statement
             Resource  subject   = stmt.getSubject();   // get the subject
             Property  predicate = stmt.getPredicate(); // get the predicate
             RDFNode   object    = stmt.getObject();    // get the object

             String s = subject.toString();
             String p = predicate.toString();
             String o = object.toString();

             System.out.print(s);
             System.out.print(" " + p + " ");

             String[] menuStatusEachOne = new String[2];
             // Menu name
             menuStatusEachOne[0] = p.substring(19);
             // Status
             if (object instanceof Resource) {
                 System.out.print(o);
                 menuStatusEachOne[1] = o;
             } else {
                 // object is a literal
                 System.out.print(" \"" + o + "\"");
                 menuStatusEachOne[1] = "\"" + o + "\"";
             }
             System.out.println(" .");
             menuStatus.add(menuStatusEachOne);
         }
         return menuStatus;
     }

     public void writeRDF(String pathFile) throws IOException {
         String fileName = pathFile + ".ttl";
         FileWriter write = new FileWriter(fileName, true);
         PrintWriter printLine = new PrintWriter(write);
         model.write(printLine, "TTL");
         printLine.close();

         System.out.println("Print to " + fileName);
     }

     public void addStatement(String s, String p, String o) {
         Resource subject = model.createResource(s);
         Property predicate = model.createProperty(p);
         // Not use o to be URI
         //RDFNode object = model.createResource(o);

         Statement stmt = model.createStatement(subject, predicate, o);
         model.add(stmt);
     }

     public void createModel () {
         // setup the default config for Log4j
         org.apache.log4j.BasicConfigurator.configure(new NullAppender());

         model = ModelFactory.createDefaultModel();
     }

     public Model readModel (String file) {
         // setup the default config for Log4j
         org.apache.log4j.BasicConfigurator.configure(new NullAppender());

         model = FileManager.get().loadModel(file, "ttl");
         return model;
     }

     private boolean deleteRdfFile (String f) {
         File file = new File(f);
         return (file.delete());
     }

     public String helloRDF() {
         return "Hi from RDF";
     }

}

