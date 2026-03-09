package com.albavg.gestiondocentes.servicio;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.url}")
    private String appUrl;

    public void enviarSetupPassword(String email, String nombreCompleto, String token) {
        String enlace = appUrl + "/setup-password?token=" + token;
        String html = buildHtml(
                "Configura tu contraseña",
                nombreCompleto,
                "El administrador ha creado tu cuenta en el <strong style=\"color:#2c3e50;\">Gestor de Profesores</strong>.<br>"
                + "Haz clic en el botón para configurar tu contraseña y acceder al sistema.",
                enlace,
                "Configurar contraseña",
                "Este enlace expira en 24 horas. Si no esperabas este correo, ignóralo."
        );
        enviar(email, "Configura tu contrasena - Gestor de Profesores", html);
    }

    public void enviarResetPassword(String email, String nombreCompleto, String token) {
        String enlace = appUrl + "/reset-password?token=" + token;
        String html = buildHtml(
                "Restablece tu contrasena",
                nombreCompleto,
                "Hemos recibido una solicitud para <strong style=\"color:#2c3e50;\">restablecer tu contrasena</strong>.<br>"
                + "Si no has sido tu, puedes ignorar este correo con total seguridad.",
                enlace,
                "Restablecer contrasena",
                "Este enlace expira en 24 horas."
        );
        enviar(email, "Restablece tu contrasena - Gestor de Profesores", html);
    }

    private String buildHtml(String titulo, String nombreCompleto, String descripcion,
                              String enlace, String textoBoton, String notaFooter) {
        return "<!DOCTYPE html>"
            + "<html lang=\"es\"><head><meta charset=\"UTF-8\"></head>"
            + "<body style=\"margin:0;padding:0;background:#f0f2f5;font-family:'Segoe UI',Arial,sans-serif;\">"
            + "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background:#f0f2f5;padding:48px 0;\">"
            + "<tr><td align=\"center\">"
            + "<table width=\"560\" cellpadding=\"0\" cellspacing=\"0\" style=\"background:#ffffff;border-radius:12px;box-shadow:0 4px 16px rgba(0,0,0,0.08);overflow:hidden;\">"

            // Header: logo centrado + nombre app
            + "<tr><td align=\"center\" style=\"padding:36px 40px 24px;\">"
            + "<img src=\"cid:logo\" alt=\"Gestor de Profesores\" width=\"64\" height=\"64\" style=\"display:block;margin:0 auto 12px;\" />"
            + "<div style=\"font-size:1.15rem;font-weight:700;color:#2c3e50;\">Gestor de Profesores</div>"
            + "</td></tr>"

            // Divisor
            + "<tr><td style=\"padding:0 40px;\"><hr style=\"border:none;border-top:1px solid #eef0f3;margin:0;\"/></td></tr>"

            // Contenido
            + "<tr><td style=\"padding:32px 40px 32px;\">"
            + "<h2 style=\"margin:0 0 16px;font-size:1.25rem;color:#2c3e50;\">" + titulo + "</h2>"
            + "<p style=\"margin:0 0 6px;font-size:1rem;color:#1a202c;\">Hola, <strong>" + nombreCompleto + "</strong></p>"
            + "<p style=\"margin:16px 0 28px;font-size:0.95rem;color:#4a5568;line-height:1.7;\">" + descripcion + "</p>"
            + "<table cellpadding=\"0\" cellspacing=\"0\" style=\"margin:0 0 28px;\">"
            + "<tr><td style=\"border-radius:8px;background:#2c3e50;\">"
            + "<a href=\"" + enlace + "\" target=\"_blank\" "
            + "style=\"display:inline-block;padding:14px 36px;color:#ffffff;font-size:0.95rem;font-weight:600;text-decoration:none;border-radius:8px;\">"
            + textoBoton
            + "</a></td></tr></table>"
            + "<p style=\"margin:0 0 6px;font-size:0.82rem;color:#a0aec0;\">No funciona el boton? Copia este enlace:</p>"
            + "<p style=\"margin:0 0 20px;font-size:0.78rem;color:#cbd5e0;word-break:break-all;\">" + enlace + "</p>"
            + "<p style=\"margin:0;font-size:0.82rem;color:#e53e3e;\">" + notaFooter + "</p>"
            + "</td></tr>"

            // Footer
            + "<tr><td style=\"padding:20px 40px;border-top:1px solid #eef0f3;background:#f8f9fa;\">"
            + "<p style=\"margin:0;font-size:0.78rem;color:#9aa3af;line-height:1.5;\">Este es un mensaje automatico, por favor no respondas a este correo.<br>Gestor de Profesores</p>"
            + "</td></tr>"

            + "</table></td></tr></table>"
            + "</body></html>";
    }

    private void enviar(String to, String subject, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("noreply@gestordocentes.es");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            helper.addInline("logo", new ClassPathResource("static/icon_dark.png"));
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el email", e);
        }
    }
}
