package com.esoft.hotelmanagementsystem.entity;

import com.esoft.hotelmanagementsystem.dto.SuperDto;
import com.esoft.hotelmanagementsystem.enums.RecordStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.time.ZonedDateTime;

/**
 * @author ShanilErosh
 */
@MappedSuperclass
@Getter
@Setter
public abstract class SuperEntity<D extends SuperDto> {


    private static final int HOURS = 5;
    private static final int MINUTES = 30;
    @Column(nullable = false, updatable = false)
    @CreatedBy
    private String createdBy = "";
    @LastModifiedBy
    private String modifiedBy;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RecordStatus recordStatus = RecordStatus.ACTIVE;
    @Column(nullable = false, updatable = false)
    @CreatedDate
    private ZonedDateTime createdDate;
    @LastModifiedDate
    private ZonedDateTime modifiedDate;

    public abstract D toDto();

    protected void setAuditDetail(D d) {

        d.setCreatedBy(createdBy);
        d.setCreatedDate(createdDate);
        d.setModifiedBy(modifiedBy);
        d.setModifiedDate(modifiedDate);
        d.setRecordStatus(recordStatus);
    }
}

