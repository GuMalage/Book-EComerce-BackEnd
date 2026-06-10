package br.edu.utfpr.pb.pw44s.server.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendOrderStatusEmail(
            String to,
            String userName,
            String newStatus,
            String oldStatus,
            String email
    ) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(email);
            helper.setTo(to);
            helper.setSubject("🔄 Atualização de Status do seu Pedido!");


            String htmlContent = "<div style='font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px; color: #333;'>"
                    + "<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.05);'>"
                    + "  "
                    + "  <div style='background-color: #4F46E5; padding: 30px; text-align: center; color: #ffffff;'>"
                    + "    <h1 style='margin: 0; font-size: 24px; font-weight: bold;'>Novidades sobre seu pedido!</h1>"
                    + "  </div>"
                    + "  "
                    + "  <div style='padding: 30px; line-height: 1.6;'>"
                    + "    <p style='font-size: 16px; margin-top: 0;'>Olá, <strong>" + userName + "</strong>! Tudo bem?</p>"
                    + "    <p style='font-size: 15px; color: #555;'>Passando aqui para te avisar que o status do seu pedido foi atualizado com sucesso.</p>"
                    + "    "
                    + "    "
                    + "    <div style='background-color: #f3f4f6; border-left: 4px solid #4F46E5; padding: 15px; margin: 25px 0; border-radius: 0 4px 4px 0;'>"
                    + "      <p style='margin: 0 0 8px 0; font-size: 14px; color: #666;'>Status anterior: <span style='text-decoration: line-through; color: #9ca3af;'>" + oldStatus + "</span></p>"
                    + "      <p style='margin: 0; font-size: 16px; font-weight: bold; color: #111827;'>Novo status: <span style='color: #059669;'>" + newStatus + "</span></p>"
                    + "    </div>"
                    + "    "
                    + "  </div>"
                    + "  "
                    + "  <div style='background-color: #f3f4f6; padding: 20px; text-align: center; font-size: 12px; color: #9ca3af; border-top: 1px solid #e5e7eb;'>"
                    + "    <p style='margin: 0;'>UTF-8 E-commerce - Pato Branco/PR</p>"
                    + "    <p style='margin: 5px 0 0 0;'>Por favor, não responda a esta mensagem automática.</p>"
                    + "  </div>"
                    + "</div>"
                    + "</div>";

            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException e) {
            System.err.println("Erro ao enviar e-mail de status: " + e.getMessage());
            e.printStackTrace();
        }
    }
}