import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

/**
 * @author danushka (github.com/danushka96)
 */

public class OTP {

    //Email Configuration
    private static String username = "<YOUR EMAIL>"; // Enter your gmail address here
    private static String password = "<YOUR PASSWORD>"; // Enter your password here
    // Recommend to generate a app password for your account from this link
    // https://myaccount.google.com/apppasswords

    //Memorizing Generated Tokens
    private static ArrayList<String> otpList = new ArrayList<>();

    public static void main(String[] args){
        showMenu();
    }

    // Main Menu of the programme
    private static void showMenu(){
        System.out.println("");
        System.out.println("Welcome to OTP Generator...");
        System.out.println("");
        System.out.println("");
        System.out.println("1. Generate an OTP");
        System.out.println("2. Verify an OTP");
        System.out.println("");
        System.out.print("Enter the Option Number: ");

        Scanner input = new Scanner(System.in);
        int option = input.nextInt();
        if(option==1) { // For generate a new OTP
            genPassword(10);
        }else if(option==2){ // For verify currently generated token
            verifyOTP();
        }else{
            System.out.println("Wrong input. Try Again!");
            showMenu();
        }
    }

    private static void genPassword(int length) {

        System.out.print("your email: ");
        Scanner getEmail = new Scanner(System.in);
        String userEmail = getEmail.next();

        if(userEmail==null){
            System.out.println("Email cannot be empty..");
            genPassword(length);
            return;
        }

        System.out.println("Generating random password....");

        String capital_letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // Capital Letters
        String simple_letters = "abcdefghijklmnopqrstuvwxyz"; // Simple Letters
        String numbers = "0123456789"; // Numbers

        String values = capital_letters+simple_letters+numbers; // All available content for password

        Random rndm_method = new Random();

        String password = "";

        for (int i=0; i<length; i++){
            password = password+values.charAt(rndm_method.nextInt(values.length()));
        }

        otpList.add(password);
        sendMail(userEmail,password); // sending the mail
        showMenu();
    }

    private static void sendMail(String email,String OTP){
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // host
        props.put("mail.smtp.port", "587"); // port
        props.put("mail.smtp.auth", "true"); // authentication method
        props.put("mail.smtp.starttls.enable","true"); //tls enabled or not

        System.out.println("Sending Mail to "+email+"...");
        System.out.println("Please Wait.....");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator(){
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("from@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email)
            );
            message.setSubject("Password Reset Request");
            message.setText("Dear User, \n Your new Password is "+OTP);
            Transport.send(message);
            System.out.println("Message send Successfully");
            System.out.println("Check your inbox..");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static void verifyOTP(){ // Verification of generated password
        System.out.print("Enter your token: ");
        Scanner getOTP = new Scanner(System.in);
        String OTP = getOTP.next();
        for(String item : otpList){
            if(OTP.equals(item)){
                System.out.println("Token Verified....");
                return;
            }
        }
        System.out.println("Token Verification Failed....");
        System.out.println("");
        showMenu();
    }
}
