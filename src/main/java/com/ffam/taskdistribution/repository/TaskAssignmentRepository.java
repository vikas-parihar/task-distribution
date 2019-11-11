package com.ffam.taskdistribution.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ffam.taskdistribution.entities.TaskAssignments;

@Repository 
@Qualifier("taskAssignmentRepository")
public interface TaskAssignmentRepository
        extends CrudRepository<TaskAssignments, String> {

    @Query("select ta from TaskAssignments ta where ta.skillIds = ?1 and ta.agentId in ?2 and ta.taskStatus != 'Completed'"
            + " ORDER BY start_date DESC NULLS LAST")
    LinkedList<TaskAssignments> getTasksBySkillIdAndAgent(Integer[] skillIds, Collection<Integer> agentIds);
    
    @Modifying
    @Transactional
    @Query("update TaskAssignments set taskStatus = ?2, completeDate = ?3  where taskId = ?1")
    Integer updateTaskStatus(Integer taskId, String taskStatus, LocalDateTime completionDate);

}
