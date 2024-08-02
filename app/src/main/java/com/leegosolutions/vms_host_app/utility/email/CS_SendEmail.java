package com.leegosolutions.vms_host_app.utility.email;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;

import androidx.core.content.ContextCompat;

import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.database.action.CS_Action_AccessDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_EmailDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_AccessDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_EmailDetails;
import com.leegosolutions.vms_host_app.utility.CS_ED;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

import java.util.Calendar;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class CS_SendEmail {

    private Context context;
    private String emailId = "", password = "", server = "", port = "", enableSSL = "";
    private String receptionEmail = "", cc = "", emailSubject = "", emailBody = "", attachmentPath = "", attachmentFileNameWithExtension = "";
    private byte[] emailLogo = null;
    private boolean sendEmailBuildingWise = true; // default true
    private ResultListener listener = null;

    public CS_SendEmail(Context context) {
        try {
            this.context = context;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public CS_SendEmail(Context context, ResultListener listener) {
        try {
            this.context = context;
            this.listener = listener;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    // Sends EMAIL to Active Building
    public void sendEmail(byte[] emailLogo, String receptionEmail, String cc, String emailSubject, String emailBody) {
        try {
            this.emailLogo = emailLogo;
            this.receptionEmail = receptionEmail;
            this.cc = cc;
            this.emailSubject = emailSubject;
            this.emailBody = emailBody;

            send();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public void sendEmailWithAttachment(byte[] emailLogo, String receptionEmail, String cc, String emailSubject, String emailBody, String attachmentPath, String attachmentFileNameWithExtension) {
        try {
            this.emailLogo = emailLogo;
            this.receptionEmail = receptionEmail;
            this.cc = cc;
            this.emailSubject = emailSubject;
            this.emailBody = emailBody;
            this.attachmentPath = attachmentPath;
            this.attachmentFileNameWithExtension = attachmentFileNameWithExtension;

            send();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void send() {
        try {
//            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                new FetchEmailDetailsFromSQLite().execute();

//            } else {
//                new CS_Utility(context).showToast("Storage permission required for email.", 0);
//            }
        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private class FetchEmailDetailsFromSQLite extends AsyncTask<Void, Void, Void> {

        private boolean settingsAvailable = false;

        @Override
        protected Void doInBackground(Void... strings) {
            try {
                //check building wise email setup
                if (sendEmailBuildingWise && getEmailSetUpBuildingWise()) {
                    settingsAvailable = true;

                } else if (getDefaultEmail()) {
                    settingsAvailable = true;
                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            try {
                if (settingsAvailable) {
                    // Send Email
                    new Send_Email().execute();

                } else {
                    if (listener != null) {
                        listener.status(context.getResources().getString(R.string.email_settings_not_available));

                    } else {
                        new CS_Utility(context).showToast(context.getResources().getString(R.string.email_settings_not_available), 0);
                    }
                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        }
    }

    private boolean getEmailSetUpBuildingWise() {
        boolean result = false;
        try {
            // Building Wise
            CS_Entity_EmailDetails model = new CS_Action_EmailDetails(context).getEmailDetails("Building_Wise");
            if (model != null) {

                emailId = CS_ED.Decrypt(model.getED_EmailId());
                password = CS_ED.Decrypt(model.getED_Password());
                server = CS_ED.Decrypt(model.getED_Server());
                port = CS_ED.Decrypt(model.getED_Port());
                enableSSL = model.getED_EnableSSL();

                if (!server.isEmpty() && !port.isEmpty() && !emailId.isEmpty() && !password.isEmpty()) {
                    result = true;
                }
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return result;
    }

    private boolean getDefaultEmail() {
        boolean result = false;
        try {
            // Default
            CS_Entity_EmailDetails model = new CS_Action_EmailDetails(context).getEmailDetails("Default");
            if (model != null) {

                emailId = CS_ED.Decrypt(model.getED_EmailId());
                password = CS_ED.Decrypt(model.getED_Password());
                server = CS_ED.Decrypt(model.getED_Server());
                port = CS_ED.Decrypt(model.getED_Port());
                enableSSL = model.getED_EnableSSL();

                if (!server.isEmpty() && !port.isEmpty() && !emailId.isEmpty() && !password.isEmpty()) {
                    result = true;
                }
            }
        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return result;
    }

    private class Send_Email extends AsyncTask<Void, Void, Void> {

        private String result = "";

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");

                if (enableSSL.equals("1")) {
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.ssl.protocols", "TLSv1.2");
                } else {
                    props.put("mail.smtp.starttls.enable", "false");
                }
                props.put("mail.smtp.host", server);
                props.put("mail.smtp.port", port);

                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        try {
                            return new PasswordAuthentication(emailId, password);
                        } catch (Exception e) {
                            result = "Error sending email: " + e.getMessage();
                            new CS_Utility(context).saveError(e);
                        }
                        return null;
                    }
                });

                String[] recipientList = receptionEmail.split(";");
                InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
                int counter = 0;
                for (String recipient : recipientList) {
                    recipientAddress[counter] = new InternetAddress(recipient.trim());
                    counter++;
                }

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(emailId));
                message.setRecipients(Message.RecipientType.TO, recipientAddress);
                // CC
                if (!cc.equals("")) {
                    String[] addressList = cc.split(";");
                    for (String address : addressList) {
                        message.addRecipient(Message.RecipientType.CC, new InternetAddress(address.trim()));
                    }
//                    message.addRecipient(Message.RecipientType.CC, new InternetAddress(addressList));
                }
                message.setSubject(emailSubject);

                Multipart multipart = new MimeMultipart();
                MimeBodyPart bodyPartImage = new MimeBodyPart();

                if (emailLogo != null || !attachmentPath.equals("")) {

                    // DO not move this check from this place
                    if (attachmentPath.equals("")) {
                        try {
                            // Check for ATT00001.htm
                            bodyPartImage.setContent(emailBody, "text/html");
                            multipart.addBodyPart(bodyPartImage);

                        } catch (Exception e) {
                            result = "Error sending email: " + e.getMessage();
                            new CS_Utility(context).saveError(e);
                        }
                    }

                    // Logo
                    if (emailLogo != null) {
                        try {
                            bodyPartImage = new MimeBodyPart();
                            DataSource ds = new ByteArrayDataSource(emailLogo, "image/jpeg");
                            bodyPartImage.setDataHandler(new DataHandler(ds));
                            bodyPartImage.setHeader("Content-Type", "image/jpeg; name=image.jpg");
                            bodyPartImage.setHeader("Content-ID", "<img_buildingLogo>");
                            bodyPartImage.setHeader("Content-Disposition", "inline");
                            multipart.addBodyPart(bodyPartImage);

                        } catch (Exception e) {
                            result = "Error sending email: " + e.getMessage();
                            new CS_Utility(context).saveError(e);
                        }
                    }

                    // Attachment - Export Excel
                    if (!attachmentPath.equals("")) {
                        try {
                            BodyPart pdfBodyPart = new MimeBodyPart();
                            DataSource source = new FileDataSource(attachmentPath);
                            pdfBodyPart.setDataHandler(new DataHandler(source));
                            pdfBodyPart.setFileName(attachmentFileNameWithExtension);
                            multipart.addBodyPart(pdfBodyPart);

                            BodyPart messageBodyPart = new MimeBodyPart();
                            messageBodyPart.setContent(emailBody, "text/html; charset=utf-8");
                            multipart.addBodyPart(messageBodyPart);

                        } catch (Exception e) {
                            result = "Error sending email: " + e.getMessage();
                            new CS_Utility(context).saveError(e);
                        }
                    }

                    if (!attachmentPath.equals("")) {
                        BodyPart messageBodyPart = new MimeBodyPart();
                        messageBodyPart.setContent(emailBody, "text/html; charset=utf-8");
                        multipart.addBodyPart(messageBodyPart);
                    }

                    message.setContent(multipart);

                } else {
                    message.setContent(emailBody, "text/html; charset=utf-8");
                }

//                message.setHeader("X-Priority", "1");
                Transport.send(message);
                result = "success";

            } catch (Exception e) {
                result = "Error sending email: " + e.getMessage();

                if (sendEmailBuildingWise && getDefaultEmail()) {
                    sendEmailBuildingWise = false;
                    send();
                } else {
                    new CS_Utility(context).saveError(e);

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            try {
                if (listener != null) {
                    listener.status(result);
                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        }
    }

    public interface ResultListener {
        void status(String status);
    }

}
