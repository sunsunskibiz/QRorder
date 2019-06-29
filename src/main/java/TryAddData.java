//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class TryAddData {
//    static ArrayList<String> order;
//    static String pathname = "D:\\newProject\\out\\now\\";
//    static String tableNO = "26";
//    static String[] arrOrdered = {"DARK_CHOCOLATE_PRAPPE", "FIGGY_PUDDING", "HONEY_TOAST"};
//    static String predicate = "http://cafeone.com#";
//
//
//
//    public static void main(String[] args) throws IOException {
//        order = new ArrayList<>();
//        String newpath = pathname + "T26_20200623024117";
//        String destpath = null;
//        File folder = new File(pathname);
//        listFilesForFolder(folder);
//        String fullURL = "http://cafeone.com/" + tableNO + "/" + "20200623024117";
//
//
//        // Search  file
//        for (int j = 0; j<order.size(); j++) {
//            String tbNO = order.get(j).substring(1, 3);
//            if (tbNO.equals(tableNO)) {
//                destpath = pathname + order.get(j);
//                break;
//            }
//        }
//
//        if (destpath != null) {
//            Rdf rdf = new Rdf();
//            rdf.prepareAdd(destpath, fullURL);
//            for (int j=0; j<arrOrdered.length; j++) {
//                rdf.addStatement(fullURL, predicate + arrOrdered[j], "Ordered");
//            }
//            if (rdf.deleteRdfFile(destpath)) {
//                rdf.writeRDF(newpath);
//                System.out.println("------------- Write RDF file ----------------");
//            } else {
//                System.out.println("changeStatusToserved is failed");
//            }
//        }
//    }
//
//    private static void listFilesForFolder(final File folder) {
//        order.clear();
//        File[] listOfFiles = folder.listFiles();
//        for (File fileEntry : listOfFiles) {
//            if (fileEntry.isDirectory()) {
//                listFilesForFolder(fileEntry);
//            } else {
//                order.add(fileEntry.getName());
//            }
//        }
//    }
//
//
//}
