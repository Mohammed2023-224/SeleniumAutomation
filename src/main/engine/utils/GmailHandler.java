package engine.utils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import engine.constants.Constants;
import engine.reporters.Loggers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import static org.awaitility.Awaitility.await;

public class GmailHandler {
    private   String applicationName ;
    private   JsonFactory jsonFactory ;
    private  List<String> scopes;
    private  String credentialsFilePath= Constants.gmailCredential;
    private  String tokensPath = Constants.gmailToken;
    Gmail service = null;
    private String currentUser="me";

    public GmailHandler(String appName){
        this.scopes = Arrays.asList(GmailScopes.GMAIL_SEND,GmailScopes.GMAIL_READONLY,GmailScopes.GMAIL_COMPOSE,GmailScopes.GMAIL_MODIFY);
        this.jsonFactory=JacksonFactory.getDefaultInstance();
        this.applicationName=appName;
        try {
            this.service = getGmailService();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private  Credential authorize()  {
        // Load credentials.json
        InputStream in = GmailHandler.class.getResourceAsStream(credentialsFilePath);
        if (in == null) {
            throw new RuntimeException(credentialsFilePath+" not found in resources folder.");
        }
        GoogleClientSecrets clientSecrets = null;
        FileDataStoreFactory tokenStore = null;
        GoogleAuthorizationCodeFlow flow = null;

        try {
            clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));
            tokenStore = new FileDataStoreFactory(new File(tokensPath));
            flow = new GoogleAuthorizationCodeFlow.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    jsonFactory,
                    clientSecrets,
                    scopes)
                    .setDataStoreFactory(tokenStore)
                    .setAccessType("offline")
                    .build();
        } catch (IOException e) {
         Loggers.log.error("Error with token file ", e );
        } catch (GeneralSecurityException e) {
         Loggers.log.warn("gmail security issue ", e );
        }

        try {
            return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        } catch (IOException e) {
            throw new RuntimeException("Failed to authorize {}", e);

        }
    }

    private   Gmail getGmailService()  {
        Credential credential = authorize();
        try {
            return new Gmail.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    jsonFactory,
                    credential)
                    .setApplicationName(applicationName)
                    .build();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Failed to fetch API "+e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch API "+e);
        }
    }
    /**
     * * How to write queries
    * From a specific sender	"from:example@gmail.com"
   * Subject contains certain words	"subject:invoice"
   * Emails that have attachments	"has:attachment"
   * Emails within a date range	"after:2023/01/01 before:2023/12/31"
    * Unread emails	"is:unread"
    * in inbox       "in:inbox"
    * Emails with a certain label	"label:IMPORTANT"
    * Combining filters	"from:abc@gmail.com is:unread"
     */
    private List<Message> returnMessages(int numberOfMessages,String query){
        ListMessagesResponse response = null;
        try {
            response = service.users().messages().list(currentUser)
                    .setMaxResults((long) numberOfMessages)
                    .setQ(query)
                    .execute();
        } catch (IOException e) {
         Loggers.log.warn("Failed to fetch messages through API call");
        }
        assert response != null;
        List<Message> messages = response.getMessages();
        if (messages == null || messages.isEmpty()) {
         Loggers.log.warn("No messages found.");
        }
        return messages;
    }



    public List<Message> readMultipleEmails(int numberOfMessages,  String query){
        List<Message> messages = returnMessages(numberOfMessages, query);
        List<Message> fullMessages = new ArrayList<>();
        if (messages == null || messages.isEmpty()) return Collections.emptyList();
        for (Message message : messages) {
                Message fullMessage = getMessage(message.getId());
                fullMessages.add(fullMessage);
        }
        return fullMessages;
    }

    public Message readMailByPosition(int numberOfMessages, String query,int id){
        int count =1;
        List<Message> messages = returnMessages(numberOfMessages, query);
        Message fullMessage = null;
        if (messages == null || messages.isEmpty()) return null;
        for (Message message : messages) {
            if(count==id) {
                fullMessage =getMessage(message.getId());
                    break;
                }
                count++;
        }
        return fullMessage;
    }

    public Message waitForEmail(int timeoutSeconds,  String query) {
        return await()
                .atMost(timeoutSeconds, TimeUnit.SECONDS)
                .pollInterval(5, TimeUnit.SECONDS)
                .ignoreExceptions()
                .until(() -> {
                    List<Message> messages = returnMessages(1, query);
                    if (messages != null && !messages.isEmpty()) {
                        return service.users().messages().get(currentUser, messages.get(0).getId()).execute();
                    }
                    return null;
                }, Objects::nonNull);
    }


    public String readFullBody(Message message){
        Message fullMessage = null;
        fullMessage =getMessage(message.getId());
        MessagePart payload = fullMessage.getPayload();
        String body = extractMessageBody(payload);
        return GmailHandlerUtils.formatHtmlData(body);
    }



    public String readFullSnippet(Message message){
        String snippet ="";
            snippet=getMessage(message.getId()).getSnippet();
        return snippet;
    }

    public List<String> readLinksFromEmail(Message message){
        List<String> links = new ArrayList<>();
        Message fullMessage = getMessage(message.getId());
        MessagePart payload = fullMessage.getPayload();
        // Get body data
        String data = payload.getBody().getData();
        if (data == null && payload.getParts() != null) {
            for (MessagePart part : payload.getParts()) {
                if ("text/html".equalsIgnoreCase(part.getMimeType())) {
                    data = part.getBody().getData();
                    break;
                }
            }
        }
        if (data != null) {
            String html =GmailHandlerUtils.decodeBase64(data);
            Document doc = Jsoup.parse(html);
            Elements aTags = doc.select("a[href]");
            for (Element a : aTags) {
                links.add(a.attr("href"));
            }
        }
        return links;
    }

    public void sendEmail(String to, String subject, String bodyText,String attachment) {
        try {
            String fromEmail = service.users().getProfile(currentUser).execute().getEmailAddress();
            MimeMessage mimeMessage = createEmail(to, fromEmail, subject, bodyText,attachment);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            mimeMessage.writeTo(buffer);
            byte[] rawMessageBytes = buffer.toByteArray();
            String encodedEmail = Base64.getUrlEncoder().encodeToString(rawMessageBytes);
            Message message = new Message();
            message.setRaw(encodedEmail);

            message = service.users().messages().send(currentUser, message).execute();
         Loggers.log.info("Email sent successfully: {}", message.getId());
        }catch (GoogleJsonResponseException e) {
            int statusCode = e.getStatusCode();
            String details = e.getDetails() != null ? e.getDetails().getMessage() : "No error details";
            switch (statusCode) {
                case 403:
                 Loggers.log.error("Permission denied. Check Gmail scopes and API access: {}", details);
                    break;
                case 400:
                 Loggers.log.error("Bad Request - possibly invalid email or message format: {}", details);
                    break;
                case 401:
                 Loggers.log.error("Unauthorized - token may have expired or is invalid: {}", details);
                    break;
                default:
                 Loggers.log.error("Google API error {}: {}", statusCode, details);
            }
        }catch (Exception e) {
         Loggers.log.error("Failed to send email", e);
        }
    }


    // Recursive method to extract body content from message parts
    private String extractMessageBody(MessagePart part) {
        if (part == null) return "";
        if (part.getBody() != null && part.getBody().getData() != null) {
            return GmailHandlerUtils.decodeBase64(part.getBody().getData());
        }
        if (part.getParts() != null) {
            for (MessagePart subPart : part.getParts()) {
                String result = extractMessageBody(subPart);
                if (!result.isEmpty()) return result;
            }
        }
        return "";
    }


    private MimeMessage createEmail(String to, String from, String subject, String bodyText,String attachment) {
        final MailcapCommandMap mc = GmailHandlerUtils.getMailcapCommandMap();
        CommandMap.setDefaultCommandMap(mc);
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        MimeBodyPart textPart = new MimeBodyPart();
        // Body part for attachment
        MimeBodyPart attachmentPart = new MimeBodyPart();
        // Combine parts
        Multipart multipart = new MimeMultipart();
        try {
            email.setFrom(new InternetAddress(from));
            email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
            email.setSubject(subject);
            textPart.setText(bodyText);
            attachmentPart.attachFile(attachment);
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachmentPart);
            email.setContent(multipart);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to create mail" + e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return email;
    }
    private Message getMessage(String messageID){
        try {
            return service.users().messages().get(currentUser, messageID).execute();
        } catch (IOException e) {
            throw new RuntimeException("Failed to get message "+ e);
        }
    }

}
