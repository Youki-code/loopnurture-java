package org.springframework.samples.loopnurture.mail.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRecordDO;
import org.springframework.samples.loopnurture.mail.domain.repository.EmailSendRecordRepository;
import org.springframework.samples.loopnurture.mail.exception.ResourceNotFoundException;
import org.springframework.samples.loopnurture.mail.exception.MailSendException;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * 邮件发送服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSendService {
    
    private final SendGrid sendGrid;
    private final EmailSendRecordRepository sendRecordRepository;

    /**
     * 发送邮件
     */
    @Transactional
    public EmailSendRecordDO sendEmail(EmailSendRecordDO emailRecord) {
        try {
            // 标记为发送中
            emailRecord.markAsSending();

            // 构建SendGrid邮件对象
            Email from = new Email(emailRecord.getFromEmail(), emailRecord.getFromName());
            Email to = new Email(emailRecord.getToEmail(), emailRecord.getToName());
            Content content = new Content(
                emailRecord.isHtmlContent() ? "text/html" : "text/plain", 
                emailRecord.getContent()
            );
            Mail mail = new Mail(from, emailRecord.getSubject(), to, content);

            // 添加抄送
            List<String> ccList = emailRecord.getCcList();
            if (ccList != null && !ccList.isEmpty()) {
                for (String cc : ccList) {
                    mail.personalization.get(0).addCc(new Email(cc));
                }
            }

            // 添加密送
            List<String> bccList = emailRecord.getBccList();
            if (bccList != null && !bccList.isEmpty()) {
                for (String bcc : bccList) {
                    mail.personalization.get(0).addBcc(new Email(bcc));
                }
            }

            // 发送邮件
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);

            // 检查发送结果
            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                emailRecord.markAsSent();
            } else {
                emailRecord.markAsFailed("SendGrid API error: " + response.getStatusCode() + " - " + response.getBody());
            }
        } catch (IOException e) {
            log.error("Failed to send email", e);
            emailRecord.markAsFailed("SendGrid API error: " + e.getMessage());
            throw new MailSendException("Failed to send email", e);
        }

        return createSendRecord(emailRecord);
    }


    // 发送日志相关方法
    @Transactional
    public EmailSendRecordDO createSendRecord(EmailSendRecordDO record) {
        return sendRecordRepository.save(record);
    }


    public EmailSendRecordDO getSendRecord(String recordId) {
        EmailSendRecordDO record = sendRecordRepository.findByRecordId(recordId);
        if(record==null){
            throw new ResourceNotFoundException("Send record not found");
        }
        return record;
    }




} 