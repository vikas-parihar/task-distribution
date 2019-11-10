package com.ffam.taskdistribution.repository;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ffam.taskdistribution.entities.Skill;

@Repository 
@Qualifier("skillRepository")
public interface SkillRepository
        extends CrudRepository<Skill, String> {

    @Query("select skill from Skill skill where skill.skillName = ?1")
    Skill getSkillsBySkillIds(String stringName);
    
    @Query("select skill.skillId from Skill skill where skill.skillName in ?1")
    Integer[] getSkillIdsBySkillNames( Collection<String> names);

}
