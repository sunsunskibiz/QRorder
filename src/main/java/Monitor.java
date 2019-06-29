//import java.util.*;
//import java.io.*;
//import javax.mail.*;
//import javax.mail.event.*;
//import javax.activation.*;
//
//import com.sun.mail.imap.*;
//
///* Monitors given mailbox for new mail */
//
//public class Monitor {
//    private String host;
//    private String port;
//    private String user;
//    private String password;
//    private int freq;
//
//    public Monitor(String host, String port, String user, String password, int freq) {
//        this.host = host;
//        this.port = port;
//        this.user = user;
//        this.password = password;
//        this.freq = freq;
//
//        System.out.println("\nTesting monitor\n");
//        try {
//
//            Properties prop = new Properties();
//            prop.put("mail.smtp.auth", true);
//            prop.put("mail.smtp.starttls.enable", "true");
//            prop.put("mail.smtp.host", host);
//            prop.put("mail.smtp.port", port);
//            prop.put("mail.smtp.ssl.trust", host);
//            Session session = Session.getDefaultInstance(prop);
//
//            Store store = session.getStore("imaps");
//            store.connect(host, user, password);
//
////            // Create folder
////            Folder folder = store.getFolder("INBOX");
////            folder.open(Folder.READ_WRITE);
//            // Open a Folder
//            Folder folder = store.getFolder("INBOX");
//            if (folder == null || !folder.exists()) {
//                System.out.println("Invalid folder");
//                System.exit(1);
//            } else {
//                System.out.println("Folder exist");
//            }
//
//            folder.open(Folder.READ_WRITE);
//
//            // Add messageCountListener to listen for new messages
//            folder.addMessageCountListener(new MessageCountAdapter() {
//                public void messagesAdded(MessageCountEvent ev) {
//                    Message[] msgs = ev.getMessages();
//                    System.out.println("Got " + msgs.length + " new messages");
//
//                    // Just dump out the new messages
//                    for (int i = 0; i < msgs.length; i++) {
//                        try {
//                            System.out.println("-----");
//                            System.out.println("Message " +
//                                    msgs[i].getMessageNumber() + ":");
//                            msgs[i].writeTo(System.out);
//                        } catch (IOException ioex) {
//                            ioex.printStackTrace();
//                        } catch (MessagingException mex) {
//                            mex.printStackTrace();
//                        }
//                    }
//                }
//            });
//
//            // Check mail once in "freq" MILLIseconds
//            boolean supportsIdle = false;
//            try {
//                if (folder instanceof IMAPFolder) {
//                    IMAPFolder f = (IMAPFolder)folder;
//                    f.idle();
//                    supportsIdle = true;
//                }
//            } catch (FolderClosedException fex) {
//                throw fex;
//            } catch (MessagingException mex) {
//                supportsIdle = false;
//            }
//            for (;;) {
//                if (supportsIdle && folder instanceof IMAPFolder) {
//                    IMAPFolder f = (IMAPFolder)folder;
//                    f.idle();
//                    System.out.println("IDLE done");
//                } else {
//                    Thread.sleep(freq); // sleep for freq milliseconds
//
//                    // This is to force the IMAP server to send us
//                    // EXISTS notifications.
//                    folder.getMessageCount();
//                }
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
////    public static void main(String argv[]) {
//////        if (argv.length != 5) {
//////            System.out.println(
//////                    "Usage: monitor <host> <user> <password> <mbox> <freq>");
//////            System.exit(1);
//////        }
////        System.out.println("\nTesting monitor\n");
////        String host = "smtp.gmail.com";
////        int port = 993;
////        String username = "cafeone.kitchen@gmail.com";
////        String password = "Cafeone2019";
////
////        try {
////
////            Properties prop = new Properties();
////            prop.put("mail.smtp.auth", true);
////            prop.put("mail.smtp.starttls.enable", "true");
////            prop.put("mail.smtp.host", host);
////            prop.put("mail.smtp.port", port);
////            prop.put("mail.smtp.ssl.trust", host);
////            Session session = Session.getDefaultInstance(prop);
////
////            Store store = session.getStore("imaps");
////            store.connect(host, username, password);
////
//////            // Create folder
//////            Folder folder = store.getFolder("INBOX");
//////            folder.open(Folder.READ_WRITE);
////            // Open a Folder
////            Folder folder = store.getFolder("INBOX");
////            if (folder == null || !folder.exists()) {
////                System.out.println("Invalid folder");
////                System.exit(1);
////            } else {
////                System.out.println("Folder exist");
////            }
////
////            folder.open(Folder.READ_WRITE);
////
////            // Add messageCountListener to listen for new messages
////            folder.addMessageCountListener(new MessageCountAdapter() {
////                public void messagesAdded(MessageCountEvent ev) {
////                    Message[] msgs = ev.getMessages();
////                    System.out.println("Got " + msgs.length + " new messages");
////
////                    // Just dump out the new messages
////                    for (int i = 0; i < msgs.length; i++) {
////                        try {
////                            System.out.println("-----");
////                            System.out.println("Message " +
////                                    msgs[i].getMessageNumber() + ":");
////                            msgs[i].writeTo(System.out);
////                        } catch (IOException ioex) {
////                            ioex.printStackTrace();
////                        } catch (MessagingException mex) {
////                            mex.printStackTrace();
////                        }
////                    }
////                }
////            });
////
////            // Check mail once in "freq" MILLIseconds
////            int freq = 500;
//////            int freq = Integer.parseInt(argv[4]);
////            boolean supportsIdle = false;
////            try {
////                if (folder instanceof IMAPFolder) {
////                    IMAPFolder f = (IMAPFolder)folder;
////                    f.idle();
////                    supportsIdle = true;
////                }
////            } catch (FolderClosedException fex) {
////                throw fex;
////            } catch (MessagingException mex) {
////                supportsIdle = false;
////            }
////            for (;;) {
////                if (supportsIdle && folder instanceof IMAPFolder) {
////                    IMAPFolder f = (IMAPFolder)folder;
////                    f.idle();
////                    System.out.println("IDLE done");
////                } else {
////                    Thread.sleep(freq); // sleep for freq milliseconds
////
////                    // This is to force the IMAP server to send us
////                    // EXISTS notifications.
////                    folder.getMessageCount();
////                }
////            }
////
////        } catch (Exception ex) {
////            ex.printStackTrace();
////        }
////    }
//
//
//}