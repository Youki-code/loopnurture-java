package org.springframework.samples.loopnurture.mail.service.impl;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailSendLogDO;
import org.springframework.samples.loopnurture.mail.domain.repository.MarketingEmailSendLogRepository;
import org.springframework.samples.loopnurture.mail.service.EmailSender;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Service
public class SendGridEmailSender implements EmailSender {

    private final SendGrid sendGrid;
    private final MarketingEmailSendLogRepository sendLogRepository;

    public SendGridEmailSender(
            @Value("${sendgrid.api-key}") String sendGridApiKey,
            MarketingEmailSendLogRepository sendLogRepository) {
        this.sendGrid = new SendGrid(sendGridApiKey);
        this.sendLogRepository = sendLogRepository;
    }

    @Override
    public MarketingEmailSendLogDO sendEmail(MarketingEmailSendLogDO emailLog) {
        try {
            // 更新状态为发送中
            emailLog.setStatus(1);
            emailLog = sendLogRepository.save(emailLog);

            // 构建SendGrid邮件对象
            Email from = new Email(emailLog.getFromEmail(), emailLog.getFromName());
            Email to = new Email(emailLog.getToEmail(), emailLog.getToName());
            Content content = new Content("text/html", emailLog.getContent());
            Mail mail = new Mail(from, emailLog.getSubject(), to, content);

            // 发送邮件
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            // 根据响应状态码更新发送状态
            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                // 发送成功
                emailLog.setStatus(2);
                log.info("Successfully sent email to {} with subject: {}", emailLog.getToEmail(), emailLog.getSubject());
            } else {
                // 发送失败
                emailLog.setStatus(3);
                emailLog.setFailReason("SendGrid API returned status code: " + response.getStatusCode() + 
                    ", body: " + response.getBody());
                emailLog.setRetryCount(emailLog.getRetryCount() + 1);
                emailLog.setNextRetryTime(calculateNextRetryTime(emailLog.getRetryCount()));
                log.error("Failed to send email. Status code: {}, Body: {}", 
                    response.getStatusCode(), response.getBody());
            }

        } catch (IOException e) {
            // 处理发送异常
            log.error("Error sending email via SendGrid", e);
            emailLog.setStatus(3);
            emailLog.setFailReason(e.getMessage());
            emailLog.setRetryCount(emailLog.getRetryCount() + 1);
            emailLog.setNextRetryTime(calculateNextRetryTime(emailLog.getRetryCount()));
        }

        // 更新发送记录
        emailLog.setUpdatedAt(LocalDateTime.now());
        return sendLogRepository.save(emailLog);
    }

    private LocalDateTime calculateNextRetryTime(int retryCount) {
        // 实现指数退避重试策略
        int minutes = (int) Math.pow(2, retryCount);
        return LocalDateTime.now().plusMinutes(minutes);
    }
} 