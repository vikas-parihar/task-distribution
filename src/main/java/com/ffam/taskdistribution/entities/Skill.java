package com.ffam.taskdistribution.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Skill implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer skillId;

    private String skillName;

}
