//import javax.mail.*;
//import java.util.Properties;
//import javax.mail.internet.MimeBodyPart;
//
//public class ReceiveEmail {
//    private String host = "smtp.gmail.com";
//    private int port = 993;
//    private String username = "cafeone.official@gmail.com";
//    private String password = "cafeOne2019";
//
//    public ReceiveEmail(String host, int port, String username, String password) {
//        try {
//            Properties prop = new Properties();
//            prop.put("mail.smtp.auth", true);
//            prop.put("mail.smtp.starttls.enable", "true");
//            prop.put("mail.smtp.host", host);
//            prop.put("mail.smtp.port", port);
//            prop.put("mail.smtp.ssl.trust", host);
//            Session session = Session.getDefaultInstance(prop);
//
//            Store store = session.getStore("imaps");
//            store.connect(host, username, password);
//
//            // Create folder
//            Folder folder = store.getFolder("INBOX");
//            folder.open(Folder.READ_ONLY);
//
//            // Fetch massage from folder
//            Message[] messages = folder.getMessages();
//
//            for (int i = 0, n = messages.length; i < n; i++) {
//                Message individualmsg = messages[i];
//                System.out.println("==========================Print individual messages=============================");
//                System.out.println("No# " + (i + 1));
//                System.out.println("Email Subejct: " + individualmsg.getSubject());
//                System.out.println("Sender: " + individualmsg.getFrom()[0]);
////                System.out.println("Content: " + individualmsg.getContent().toString());
//
//                String contentType = individualmsg.getContentType();
//                String messageContent = "";
//
//                if (contentType.contains("multipart")) {
//                    Multipart multiPart = (Multipart) individualmsg.getContent();
//                    int numberOfParts = multiPart.getCount();
//                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
//                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
//                        messageContent = part.getContent().toString();
//                    }
//                } else if (contentType.contains("text/plain") || contentType.contains("text/html")) {
//                    Object content = individualmsg.getContent();
//                    if (content != null) {
//                        messageContent = content.toString();
//                    }
//                } else if (contentType.contains("image/jpeg")) {
//                    System.out.println("--------> image/jpeg");
////                    Object o = p.getContent();
////
////                    InputStream x = (InputStream) o;
////                    // Construct the required byte array
////                    System.out.println("x.length = " + x.available());
////                    while ((i = (int) ((InputStream) x).available()) > 0) {
////                        int result = (int) (((InputStream) x).read(bArray));
////                        if (result == -1)
////                            int i = 0;
////                        byte[] bArray = new byte[x.available()];
////
////                        break;
////                    }
////                    FileOutputStream f2 = new FileOutputStream("/tmp/image.jpg");
////                    f2.write(bArray);
//                }
//                System.out.println("Content: " + messageContent);
//            }
//            // Close all the objects
//            folder.close(false);
//            store.close();
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
