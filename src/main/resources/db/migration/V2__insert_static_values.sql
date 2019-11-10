
INSERT INTO skill("skill_id", "skill_name") VALUES (1, 'skill1');
INSERT INTO skill("skill_id", "skill_name") VALUES (2, 'skill2');
INSERT INTO skill("skill_id", "skill_name") VALUES (3, 'skill3');

INSERT INTO agent("agent_id","agent_name", "skill_ids", "email", "created_date") VALUES (1, 'agent1', '{1,2,3}', 'agent1@gmail.com', CURRENT_DATE);
INSERT INTO agent("agent_id","agent_name", "skill_ids", "email", "created_date") VALUES (2, 'agent2', '{1,2}', 'agent2@gmail.com', CURRENT_DATE);
INSERT INTO agent("agent_id","agent_name", "skill_ids", "email", "created_date") VALUES (3, 'agent3', '{1}', 'agent3@gmail.com', CURRENT_DATE);
INSERT INTO agent("agent_id","agent_name", "skill_ids", "email", "created_date") VALUES (4, 'agent4', '{1,2,3}', 'agent4@gmail.com', CURRENT_DATE);
INSERT INTO agent("agent_id","agent_name", "skill_ids", "email", "created_date") VALUES (5, 'agent5', '{1,2}', 'agent5@gmail.com', CURRENT_DATE);
INSERT INTO agent("agent_id","agent_name", "skill_ids", "email", "created_date") VALUES (6, 'agent6', '{1}', 'agent6@gmail.com', CURRENT_DATE);
INSERT INTO agent("agent_id","agent_name", "skill_ids", "email", "created_date") VALUES (7, 'agent7', '{1,2,3}', 'agent7@gmail.com', CURRENT_DATE);
INSERT INTO agent("agent_id","agent_name", "skill_ids", "email", "created_date") VALUES (8, 'agent8', '{1,2}', 'agent8@gmail.com', CURRENT_DATE);
INSERT INTO agent("agent_id","agent_name", "skill_ids", "email", "created_date") VALUES (9, 'agent9', '{1}', 'agent9@gmail.com', CURRENT_DATE);
INSERT INTO agent("agent_id","agent_name", "skill_ids", "email", "created_date") VALUES (10, 'agent10', '{1,2,3}', 'agent10@gmail.com', CURRENT_DATE);
