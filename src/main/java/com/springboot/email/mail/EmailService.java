package com.springboot.email.mail;

import com.springboot.email.user.User;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * This Service Class is used for sending emails.
 *
 * @author Papa Attrams
 * @since 1.0
 */

@Service
public class EmailService {
    private final Configuration configuration; // this is coming from freemarker
    private final JavaMailSender javaMailSender;

    public EmailService(Configuration configuration, JavaMailSender javaMailSender) {
        this.configuration = configuration;
        this.javaMailSender = javaMailSender;
    }

    /**
     * This method creates and send the email
     *
     * @param title The title for the html template
     * @param user  The {@link User} entity
     * @throws MessagingException
     * @throws TemplateException
     * @throws IOException
     */
    @Async
    public void sendEmail(String title, User user) throws MessagingException, TemplateException, IOException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setSubject("Welcome To Sample-Email Application");
        mimeMessageHelper.setTo(user.getEmail());

        String emailContent = getEmailContent(title, user.getUsername(), user.getFirstname(), user.getLastname());

        mimeMessageHelper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
    }

    /**
     * This method gets the freemaker template from the templates' directory under the resources folder and injects the
     * necessary field into the template.
     *
     * @param title     The Title of the html
     * @param username  The User username
     * @param firstname The User firstname
     * @param lastname  The User lastname
     * @return The Free maker template of type string
     * @throws IOException
     * @throws TemplateException
     */
    private String getEmailContent(String title, String username, String firstname, String lastname) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();

        Map<String, String> model = new HashMap<>();
        model.put("title", title);
        model.put("username", username);
        model.put("firstname", firstname);
        model.put("lastname", lastname);

        configuration.getTemplate("sample.ftlh").process(model, stringWriter); // get the template and inject the fields
        return stringWriter.getBuffer().toString();
    }
}
