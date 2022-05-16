package de.coworking_m1.future_lab.hybrid_meeting.tv_control;

import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Message.RecipientType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.sun.mail.iap.ProtocolException;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.protocol.IMAPProtocol;
import javax.mail.Message;

@Slf4j
@Service
public class ReadInboundEmailService {
    private Store store = null;

    private String password = "";

    @PostConstruct
    public void connect(){
        Session session = this.getImapSession();

        try {
            store = session.getStore("imap");
            store.connect("web2.ipp-webspace.net", 993, "hybrid-meeting@synthro.coop", password);
        } catch (NoSuchProviderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }

    public void readInboundEmails() {
        // create session object
        
        try {
            if(store != null && !store.isConnected()){
                connect();
            }
            // connect to message store
            // open the inbox folder
            IMAPFolder inbox = (IMAPFolder) store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);
            // fetch messages
            Message[] messages = inbox.getMessages();

            log.info("Found {} messages", messages.length);
            // read messages
            for (int i = 0; i < messages.length; i++) {
                Message msg = messages[i];
                Address[] fromAddress = msg.getFrom();
                String from = fromAddress[0].toString();
                String subject = msg.getSubject();
                Address[] toList = msg.getRecipients(RecipientType.TO);
                Address[] ccList = msg.getRecipients(RecipientType.CC);
                String contentType = msg.getContentType();
            }
        } catch (AuthenticationFailedException e) {
            log.error("Exception in reading EMails : " + e.getMessage());
        } catch (MessagingException e) {
            log.error("Exception in reading EMails : " + e.getMessage());
        } catch (Exception e) {
            log.error("Exception in reading EMails : " + e.getMessage());
        }
    }

    private Session getImapSession() {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.debug", "true");
        props.setProperty("mail.imap.host", "web2.ipp-webspace.net");
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imap.ssl.enable", "true");
        props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(true);
        return session;
    }
}