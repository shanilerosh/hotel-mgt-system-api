package com.esoft.hotelmanagementsystem.dto;

import com.esoft.hotelmanagementsystem.entity.SuperEntity;
import com.esoft.hotelmanagementsystem.enums.RecordStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.text.ParseException;
import java.time.ZonedDateTime;

/**
 * @author ShanilErosh
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public abstract class SuperDto<E extends SuperEntity> implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String modifiedBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime createdDate;
    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "IST")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime modifiedDate;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private RecordStatus recordStatus;


    public abstract E toEntity() throws ParseException;
}
