package org.sc.smartcommunitybackend.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.sc.smartcommunitybackend.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * 邮件发送工具类
 * 使用Spring Boot内置的JavaMailSender发送邮件
 */
@Component
public class EmailUtil {

    private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${email.sender.name:智能社区系统}")
    private String senderName;

    /**
     * 发送简单文本邮件
     *
     * @param toEmail 收件人邮箱
     * @param title 邮件标题
     * @param content 邮件内容（纯文本）
     * @return 是否发送成功
     */
    public boolean sendEmail(String toEmail, String title, String content) {
        return sendEmail(toEmail, title, content, false);
    }

    /**
     * 发送邮件（支持HTML）
     *
     * @param toEmail 收件人邮箱
     * @param title 邮件标题
     * @param content 邮件内容
     * @param isHtml 是否为HTML格式
     * @return 是否发送成功
     */
    public boolean sendEmail(String toEmail, String title, String content, boolean isHtml) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            // 设置为true表示支持附件，第二个参数指定编码
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // 设置发件人（使用InternetAddress处理中文昵称编码）
            try {
                helper.setFrom(new jakarta.mail.internet.InternetAddress(
                    senderEmail, 
                    senderName, 
                    "UTF-8"
                ));
            } catch (Exception e) {
                // 如果中文昵称设置失败，使用默认的
                helper.setFrom(senderEmail);
                logger.warn("设置中文昵称失败，使用默认发件人: {}", e.getMessage());
            }
            
            // 设置收件人
            helper.setTo(toEmail);
            // 设置邮件标题
            helper.setSubject(title);
            // 设置邮件内容
            helper.setText(content, isHtml);

            logger.info("准备发送邮件到: {}, 标题: {}", toEmail, title);

            // 发送邮件
            mailSender.send(message);

            logger.info("邮件发送成功: {}", toEmail);
            return true;

        } catch (MessagingException e) {
            logger.error("邮件发送失败: {}", toEmail, e);
            throw new BusinessException("邮件发送失败：" + e.getMessage());
        } catch (Exception e) {
            logger.error("发送邮件异常: {}", toEmail, e);
            throw new BusinessException("邮件发送失败：" + e.getMessage());
        }
    }

    /**
     * 发送验证码邮件（HTML格式）
     *
     * @param toEmail 收件人邮箱
     * @param verifyCode 验证码
     * @return 是否发送成功
     */
    public boolean sendVerifyCode(String toEmail, String verifyCode) {
        String title = "【东软智慧社区】验证码";
        
        // HTML格式的邮件内容
        String content = String.format(
            "<div style='padding: 20px; background-color: #f5f5f5;'>" +
            "  <div style='max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);'>" +
            "    <h2 style='color: #333; border-bottom: 2px solid #4CAF50; padding-bottom: 10px;'>验证码通知</h2>" +
            "    <p style='font-size: 16px; color: #666; line-height: 1.6;'>您好！</p>" +
            "    <p style='font-size: 16px; color: #666; line-height: 1.6;'>您正在进行重要操作，您的验证码是：</p>" +
            "    <div style='text-align: center; margin: 30px 0;'>" +
            "      <span style='font-size: 32px; font-weight: bold; color: #4CAF50; letter-spacing: 5px; padding: 15px 30px; background-color: #f0f0f0; border-radius: 5px; display: inline-block;'>%s</span>" +
            "    </div>" +
            "    <p style='font-size: 14px; color: #999;'>验证码有效期为 <strong style='color: #f44336;'>5分钟</strong>，请勿泄露给他人。</p>" +
            "    <p style='font-size: 14px; color: #999;'>如非本人操作，请忽略此邮件。</p>" +
            "    <hr style='border: none; border-top: 1px solid #eee; margin: 20px 0;'>" +
            "    <p style='font-size: 12px; color: #999; text-align: center;'>东软智慧社区系统</p>" +
            "  </div>" +
            "</div>",
            verifyCode
        );
        
        return sendEmail(toEmail, title, content, true);
    }

    /**
     * 发送密码重置成功通知邮件（HTML格式）
     *
     * @param toEmail 收件人邮箱
     * @return 是否发送成功
     */
    public boolean sendPasswordResetNotification(String toEmail) {
        String title = "【东软智慧社区】密码重置成功";
        
        // HTML格式的邮件内容
        String content = 
            "<div style='padding: 20px; background-color: #f5f5f5;'>" +
            "  <div style='max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);'>" +
            "    <h2 style='color: #333; border-bottom: 2px solid #4CAF50; padding-bottom: 10px;'>密码重置成功</h2>" +
            "    <p style='font-size: 16px; color: #666; line-height: 1.6;'>您好！</p>" +
            "    <p style='font-size: 16px; color: #666; line-height: 1.6;'>您的账户密码已成功重置。</p>" +
            "    <div style='background-color: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 20px 0;'>" +
            "      <p style='margin: 0; color: #856404;'><strong>安全提醒：</strong>如非本人操作，请立即联系管理员！</p>" +
            "    </div>" +
            "    <p style='font-size: 14px; color: #999;'>为了您的账户安全，建议您：</p>" +
            "    <ul style='font-size: 14px; color: #999; line-height: 1.8;'>" +
            "      <li>定期更换密码</li>" +
            "      <li>使用强密码（包含字母、数字和特殊字符）</li>" +
            "      <li>不要在多个网站使用相同密码</li>" +
            "    </ul>" +
            "    <hr style='border: none; border-top: 1px solid #eee; margin: 20px 0;'>" +
            "    <p style='font-size: 12px; color: #999; text-align: center;'>东软智慧社区系统</p>" +
            "  </div>" +
            "</div>";
        
        return sendEmail(toEmail, title, content, true);
    }
}

