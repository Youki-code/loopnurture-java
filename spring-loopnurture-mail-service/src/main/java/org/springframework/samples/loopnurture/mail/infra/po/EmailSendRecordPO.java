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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "org_code")
    private String orgCode;

    @Column(name = "template_id")
    private String templateId;

    @Column(name = "extends_info")
    private String extendsInfo;

    @Column(name = "variables")
    private String variables;

    @Column(name = "status")
    private Integer status;

    @Column(name = "sent_at")
    private Date sentAt;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;
} 