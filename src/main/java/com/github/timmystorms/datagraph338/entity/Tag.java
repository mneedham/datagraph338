package com.github.timmystorms.datagraph338.entity;

import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.support.index.IndexType;

@NodeEntity
public class Tag extends BaseEntity {

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "global", fieldName = "tag_name")
    private String name;
    
    @RelatedTo(type = "HAS", direction = Direction.INCOMING)
    @Fetch
    private Set<Collaboration> collaborations;
    
    public Tag() {}
    
    public Tag(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Set<Collaboration> getCollaborations() {
        return collaborations;
    }

    public void setCollaborations(final Set<Collaboration> collaborations) {
        this.collaborations = collaborations;
    }
    
}
