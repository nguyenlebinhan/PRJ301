package service;

import jakarta.mail.Authenticator;
import jakarta.mail.Session;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.ConfigManager;


public class EmailService {
    private static final Logger LOGGER = Logger.getLogger(EmailService.class.getName());
    private final ConfigManager config = ConfigManager.getInstance();

    // Lấy thông tin từ ENV hoặc File thông qua ConfigManager
    private final String HOST = config.getProperty("EMAIL_HOST", "smtp.gmail.com");
    private final String PORT = config.getProperty("EMAIL_PORT", "587");
    private final String FROM_EMAIL = config.getProperty("EMAIL_FROM");
    private final String APP_PASSWORD = config.getProperty("EMAIL_APP_PASSWORD");
    private static final ExecutorService EMAIL_EXECUTOR = Executors.newFixedThreadPool(10);
    

    public boolean sendEmail(String to, String subject, String body) {
        // 1. SMTP Server Configuration
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);
        props.put("mail.mime.charset", "UTF-8");

        // 2. Create Session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });

        try {
            
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject, "UTF-8");
            message.setContent(body, "text/html; charset=utf-8");            

            // 4. Send Email
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "SMTP connection error or invalid credentials", e);
            return false;
        }
        
    }    


    public void sendWelcomeEmailAsync(String toEmail, String username, String fullName) {
        EMAIL_EXECUTOR.submit(() -> {
            String subject = "Welcome to the Thesis Management System";
            String name = (fullName != null && !fullName.isEmpty()) ? fullName : "User";
            
            String body = "<html><head><meta charset='UTF-8'></head><body>" +
                          "<p>Hello <strong>" + name + "</strong>,</p>" +
                          "<p>Congratulations! You have successfully registered an account: <strong>" + username + "</strong></p>" +
                          "<p>You can log in to the system now.</p>" +
                          "<p>Best regards,<br/>System Admin</p>" +
                          "</body></html>";
            
            sendEmail(toEmail, subject, body);
        });
    }


    public void sendResetPasswordEmailAsync(String toEmail, String resetUrl) {
        EMAIL_EXECUTOR.submit(() -> {
            String subject = "Password Reset Request - Thesis Management System";
            
            String body = "<html><head><meta charset='UTF-8'></head><body>" +
                          "<p>You have requested to reset your password.</p>" +
                          "<p>Please click the link below to proceed with the reset:</p>" +
                          "<p><a href='" + resetUrl + "'>Click here to reset your password</a></p>" +
                          "<p><i>Note: This link will expire in 24 hours.</i></p>" +
                          "<p>If you did not request this, please ignore this email.</p>" +
                          "</body></html>";
            
            sendEmail(toEmail, subject, body);
        });
    }
    
    public void sendEmailToStudent(String toEmail,String message){
        EMAIL_EXECUTOR.submit(() ->{
            String subject = "Message From Lecturer";
            String body = "<html><head><meta charset='UTF-8'></head><body>" 
                           +message+
                          "</body></html>";
            sendEmail(toEmail, subject, body);
        });
    }
}