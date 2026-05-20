package br.edu.utfpr.pb.pw44s.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
            String status,
            String email
    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(email);
        message.setTo(to);

        message.setSubject("Atualização do status do pedido");

        message.setText(
                "Olá " + userName + " Como vai?,\n\n" +
                        "Passando aqui para avisar que o status do seu pedido foi atualizado para: "
                        + status + "."
        );

        mailSender.send(message);
    }
}