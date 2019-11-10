package com.ffam.taskdistribution.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ffam.taskdistribution.entities.Agent;

@Repository 
@Qualifier("agentRepository")
public interface AgentRepository
        extends CrudRepository<Agent, String> {

    @Query("select agent from Agent agent where agent.skillIds = ?1")
    List<Agent> getAgentBySkillIds(Integer[] skillIds);
    

}
