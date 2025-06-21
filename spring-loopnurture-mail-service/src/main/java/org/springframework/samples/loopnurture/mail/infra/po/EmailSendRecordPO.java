package org.springframework.samples.loopnurture.mail.infra.po;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

/**
 * 邮件发送记录持久化对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "email_send_record")
@org.hibernate.annotations.Where(clause="deleted = false")
@Getter
@Setter
public class EmailSendRecordPO {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "org_code")
    private String orgCode;

    @Column(name = "rule_id")
    private String ruleId;

    @Column(name = "template_id")
    private String templateId;

    @Column(name = "subject")
    private String subject;

    @Column(name = "content")
    private String content;

    @Column(name = "recipients")
    private String recipients;

    @Column(name = "cc")
    private String cc;

    @Column(name = "bcc")
    private String bcc;

    @Column(name = "status", columnDefinition = "SMALLINT")
    private Short status;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "send_time")
    private Date sendTime;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;
} 