package com.ffam.taskdistribution.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Agent implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer agentId;

    private String agentName;

    @Type( type = "com.ffam.taskdistribution.entities.IntArrayUserType" )
    @Column(
        name = "skill_ids",
        columnDefinition = "integer[]"
    )
    private Integer [] skillIds;
    
    private String email;

    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime createdDate;

}
