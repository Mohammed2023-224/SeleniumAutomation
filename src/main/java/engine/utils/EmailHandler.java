package engine.utils;

import com.mailosaur.MailosaurClient;
import com.mailosaur.MailosaurException;
import com.mailosaur.models.*;

import java.io.IOException;

public class EmailHandler {
    //Mail Saour Handler
     String serverID = "";
     MailosaurClient mailosaur;
     SearchCriteria criteria;
    MessageSearchParams msp;
    MessageListParams mlp;

    public EmailHandler(String apiKey,String inbox){
        this.mailosaur= new MailosaurClient(apiKey);
        this.serverID=getServerIDByInboxName(inbox);
    }

    private  String getServerIDByInboxName(String inbox) {

        //get id of a certain server
        try {
            for (Server s : mailosaur.servers().list().items()) {
                if (s.name().equals(inbox)) {
                    serverID = s.id();
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (MailosaurException e) {
            throw new RuntimeException(e);
        }
        return serverID;
    }

    private  SearchCriteria  addSearchCriteria(String sentTo,String sentFrom,String subject,String body){
        criteria=new SearchCriteria();
        if(!sentTo.isEmpty()) {
            criteria.withSentTo(sentTo);
        }
        if(!sentFrom.isEmpty()) {
            criteria.withSentFrom(sentFrom);
        }
        if(!subject.isEmpty()) {
            criteria.withSubject(subject);
        }
        if(!body.isEmpty()) {
            criteria.withBody(body);
        }
        return criteria;
    }

    private MessageSearchParams addMessageSearchParams(){
        msp  =new MessageSearchParams();
        msp.withTimeout(100000);
        msp.withServer(serverID);
        return msp;
    }
    private MessageListParams addMessageListSearchParams(){
        mlp  =new MessageListParams();
        mlp.withServer(serverID);
        return mlp;
    }

    public MessageListResult getAllEmailsInInbox(){
        try {
            return  mailosaur.messages().list(addMessageListSearchParams());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (MailosaurException e) {
            throw new RuntimeException(e);
        }
    }

    public MessageListResult getAllEmailsInInboxWithCertainCriteria(String sentTo,String sentFrom,String subject,String body){
        try {
            return  mailosaur.messages().search(addMessageSearchParams(),addSearchCriteria(sentTo,sentFrom,subject,body));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (MailosaurException e) {
            throw new RuntimeException(e);
        }
    }

    public Message getLatestEmailWithCriteria(String sentTo,String sentFrom,String subject,String body){
        try {
            return mailosaur.messages().get(addMessageSearchParams(),addSearchCriteria(sentTo,sentFrom,subject,body));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (MailosaurException e) {
            throw new RuntimeException(e);
        }
    }

}
