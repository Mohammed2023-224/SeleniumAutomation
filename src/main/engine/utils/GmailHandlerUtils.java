package engine.utils;

import org.jsoup.Jsoup;
import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class GmailHandlerUtils {
    protected static MailcapCommandMap getMailcapCommandMap() {
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        return mc;
    }

    protected static String formatHtmlData(String data){
        return   data .replaceAll("(?s)<style[^>]*>.*?</style>", "")
                .replaceAll("<[^>]*>", "").
                replaceAll("&nbsp;", " ")
                .replaceAll("&amp;", "&")
                .replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&quot;", "\"")
                .replaceAll("(?m)^[ \t]*\r?\n", "").trim();
    }
    protected static String decodeBase64(String data) {
        return new String(Base64.getUrlDecoder().decode(data), StandardCharsets.UTF_8);
    }
}
