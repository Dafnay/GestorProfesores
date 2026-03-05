package com.albavg.gestiondocentes.servicio;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.url}")
    private String appUrl;

    public void enviarSetupPassword(String email, String nombre, String token) {
        String enlace = appUrl + "/setup-password?token=" + token;

        String html = """
                <!DOCTYPE html>
                <html lang="es">
                <body style="margin:0;padding:0;background:#f5f5f5;font-family:Arial,sans-serif;">
                  <table width="100%%" cellpadding="0" cellspacing="0" style="background:#f5f5f5;padding:40px 0;">
                    <tr>
                      <td align="center">
                        <table width="560" cellpadding="0" cellspacing="0" style="background:#ffffff;border-radius:8px;box-shadow:0 2px 8px rgba(0,0,0,0.08);overflow:hidden;">
                          <tr>
                            <td style="background:#2c3e50;padding:28px 40px;">
                              <h1 style="margin:0;color:#ffffff;font-size:1.3rem;font-weight:bold;">Gestor de Profesores</h1>
                            </td>
                          </tr>
                          <tr>
                            <td style="padding:36px 40px;">
                              <p style="margin:0 0 12px;font-size:1rem;color:#333;">Hola <strong>%s</strong>,</p>
                              <p style="margin:0 0 24px;font-size:0.95rem;color:#555;line-height:1.6;">
                                El administrador ha creado tu cuenta en el <strong>Gestor de Profesores</strong>.<br>
                                Haz clic en el botón para configurar tu contraseña y acceder al sistema.
                              </p>
                              <table cellpadding="0" cellspacing="0" style="margin:0 0 28px;">
                                <tr>
                                  <td style="border-radius:5px;background:#2c3e50;">
                                    <a href="%s" target="_blank"
                                       style="display:inline-block;padding:13px 32px;color:#ffffff;font-size:0.95rem;font-weight:bold;text-decoration:none;border-radius:5px;">
                                      Configurar contraseña
                                    </a>
                                  </td>
                                </tr>
                              </table>
                              <p style="margin:0 0 6px;font-size:0.85rem;color:#888;">O copia este enlace en tu navegador:</p>
                              <p style="margin:0;font-size:0.8rem;color:#aaa;word-break:break-all;">%s</p>
                            </td>
                          </tr>
                          <tr>
                            <td style="padding:16px 40px;border-top:1px solid #eeeeee;background:#fafafa;">
                              <p style="margin:0;font-size:0.8rem;color:#aaa;">Este enlace expira en 24 horas. Si no esperabas este correo, ignóralo.</p>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </body>
                </html>
                """.formatted(nombre, enlace, enlace);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setFrom("noreply@gestordocentes.es");
            helper.setTo(email);
            helper.setSubject("Configura tu contraseña - Gestor de Profesores");
            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el email", e);
        }
    }
}
