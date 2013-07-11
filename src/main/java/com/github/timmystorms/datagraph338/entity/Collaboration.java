package com.github.timmystorms.datagraph338.entity;

import java.util.Set;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.support.index.IndexType;

@NodeEntity
public class Collaboration extends BaseEntity {

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "global", fieldName = "coll_name")
    @GraphProperty
    private String name;
    
    @Indexed(indexType = IndexType.FULLTEXT, indexName = "global", fieldName = "coll_desc")
    @GraphProperty
    private String description;
    
    @GraphProperty
    private Visibility visibility;
    
    @RelatedTo(type = "HAS")
    @Fetch
    private Set<Tag> tags;
    
    public Collaboration() {}
    
    public Collaboration(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(final Set<Tag> tags) {
        this.tags = tags;
    }
    
    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(final Visibility visibility) {
        this.visibility = visibility;
    }

    public static enum Visibility {
        PUBLIC, PRIVATE;
    }
    
}
