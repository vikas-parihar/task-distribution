CREATE TABLE agent(
   agent_id serial PRIMARY KEY,
   agent_name VARCHAR (50) UNIQUE NOT NULL,
   skill_ids integer[] NOT NULL,
   email VARCHAR (355) UNIQUE NOT NULL,
   created_date TIMESTAMP NOT NULL
);

CREATE TABLE skill(
   skill_id serial PRIMARY KEY,
   skill_name VARCHAR (50) UNIQUE NOT NULL
);

CREATE TABLE task_assignments(
   task_id serial PRIMARY KEY,
   agent_id integer REFERENCES agent (agent_id),
   skill_ids integer[] NOT NULL,
   priority VARCHAR (10) NOT NULL,
   task_status VARCHAR (15),
   start_date TIMESTAMP NOT NULL,
   complete_date TIMESTAMP
);

CREATE SEQUENCE task_id_seq INCREMENT 1 START 1;
