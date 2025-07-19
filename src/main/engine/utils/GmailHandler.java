package engine.utils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import engine.reporters.Loggers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class GmailHandler {
    private   String applicationName ;
    private   JsonFactory jsonFactory ;
    private  List<String> scopes;
    private  String credentialsFilePath="/credentials.json";
    private  String tokensPath = "tokens";
    Gmail service = null;
    public GmailHandler(String appName){
        this.scopes = Collections.singletonList(GmailScopes.GMAIL_READONLY);
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
        try {
            clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Set up token store directory
        FileDataStoreFactory tokenStore = null;
        try {
            tokenStore = new FileDataStoreFactory(new File(tokensPath));
        } catch (IOException e) {
            Loggers.log.warn("Couldn't store token");
        }

        // Build flow with stored tokens
        GoogleAuthorizationCodeFlow flow = null;
        try {
            flow = new GoogleAuthorizationCodeFlow.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    jsonFactory,
                    clientSecrets,
                    scopes)
                    .setDataStoreFactory(tokenStore)
                    .setAccessType("offline")
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (GeneralSecurityException e) {
            Loggers.log.warn("gmail security issue");
        }

        try {
            return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        } catch (IOException e) {
            Loggers.log.warn("Failed to authorize");
            throw new RuntimeException();

        }
    }

    private   Gmail getGmailService() throws Exception {
        Credential credential = authorize();
        return new Gmail.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                jsonFactory,
                credential)
                .setApplicationName(applicationName)
                .build();
    }
//    From a specific sender	"from:example@gmail.com"
//    Subject contains certain words	"subject:invoice"
//    Emails that have attachments	"has:attachment"
//    Emails within a date range	"after:2023/01/01 before:2023/12/31"
//    Unread emails	"is:unread"
//    Emails with a certain label	"label:IMPORTANT"
//    Combining filters	"from:abc@gmail.com is:unread"
    private List<Message> returnMessages(int numberOfMessages, String inbox,String query){
        ListMessagesResponse response = null;
        try {
            response = service.users().messages().list("me")
                    .setMaxResults((long) numberOfMessages)
                    .setQ("in:"+inbox)
                    .setQ(query)
                    .execute();
        } catch (IOException e) {
            Loggers.log.warn("Failed to fetch messages through API call");
        }
        List<Message> messages = response.getMessages();
        if (messages == null || messages.isEmpty()) {
            Loggers.log.warn("No messages found.");
        }
        return messages;
    }



    public List<Message> readMultipleEmails(int numberOfMessages, String inbox, String query){
        List<Message> messages = returnMessages(numberOfMessages, inbox, query);
        List<Message> fullMessages = new ArrayList<>();
        for (Message message : messages) {
            try {
                Message fullMessage = service.users().messages().get("me", message.getId()).execute();
                fullMessages.add(fullMessage);
            } catch (IOException e) {
                Loggers.log.warn("Failed to fetch full message");
            }
        }
        return fullMessages;
    }

    public Message readMailByPosition(int numberOfMessages, String inbox, String query,int id){
        int count =1;
        List<Message> messages = returnMessages(numberOfMessages, inbox, query);
        Message fullMessage = null;
        for (Message message : messages) {
            if(count==id) {
                    try {
                        fullMessage = service.users().messages().get("me", message.getId()).execute();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }
                count++;
        }
        return fullMessage;
    }
    public String readFullBody(Message message){
        Message fullMessage = null;
        try {
            fullMessage = service.users().messages().get("me", message.getId()).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MessagePart payload = fullMessage.getPayload();
        String body = extractMessageBody(payload);
        return formatHtmlData(body);
    }

    public String readFullSnippet(Message message){
        String snippet ="";
        try {
            snippet=service.users().messages().get("me", message.getId()).execute().getSnippet();
        } catch (IOException e) {
            Loggers.log.warn("Couldn't get email info");
        }
        return snippet;
    }

    public List<String> readLinksFromEmail(Message message){
        List<String> links = new ArrayList<>();
        try {
            Message fullMessage = service.users().messages().get("me", message.getId()).execute();
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
                String html = new String(Base64.getUrlDecoder().decode(data), StandardCharsets.UTF_8);
                Document doc = Jsoup.parse(html);
                Elements aTags = doc.select("a[href]");
                for (Element a : aTags) {
                    links.add(a.attr("href"));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch message", e);
        }
        return links;
    }


    private String formatHtmlData(String data){
      return   data .replaceAll("(?s)<style[^>]*>.*?</style>", "")
              .replaceAll("<[^>]*>", "").
                replaceAll("&nbsp;", " ")
              .replaceAll("&nbsp;", " ")
              .replaceAll("&amp;", "&")
              .replaceAll("&lt;", "<")
              .replaceAll("&gt;", ">")
              .replaceAll("&quot;", "\"")
              .replaceAll("(?m)^[ \t]*\r?\n", "").trim();
    }

    // Recursive method to extract body content from message parts
    private String extractMessageBody(MessagePart part) {
        if (part == null) return "";
        if (part.getBody() != null && part.getBody().getData() != null) {
            byte[] bytes = Base64.getUrlDecoder().decode(part.getBody().getData());
            return new String(bytes, StandardCharsets.UTF_8);
        }
        if (part.getParts() != null) {
            for (MessagePart subPart : part.getParts()) {
                String result = extractMessageBody(subPart);
                if (!result.isEmpty()) return result;
            }
        }
        return "";
    }



}
