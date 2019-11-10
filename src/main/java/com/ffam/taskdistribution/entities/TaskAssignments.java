package com.ffam.taskdistribution.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TaskAssignments implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name="task_sequence",sequenceName="task_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="task_sequence")
    @Generated(GenerationTime.INSERT)
    private Integer taskId;

    private Integer agentId;

    @Type( type = "com.ffam.taskdistribution.entities.IntArrayUserType" )
    @Column(
        name = "skill_ids",
        columnDefinition = "integer[]"
    )
    private Integer[] skillIds;
    
    @Transient
    private String[] skillNames;
    
    private String priority;
    
    private String taskStatus;
    
    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime startDate;
    
    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime completeDate;

}
