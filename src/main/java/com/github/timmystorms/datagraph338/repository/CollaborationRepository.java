package com.github.timmystorms.datagraph338.repository;

import org.springframework.data.neo4j.repository.CypherDslRepository;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.github.timmystorms.datagraph338.entity.Collaboration;

public interface CollaborationRepository extends CypherDslRepository<Collaboration>, GraphRepository<Collaboration> {

}
