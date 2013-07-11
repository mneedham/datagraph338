package com.github.timmystorms.datagraph338;

import static org.neo4j.cypherdsl.CypherQuery.distinct;
import static org.neo4j.cypherdsl.CypherQuery.id;
import static org.neo4j.cypherdsl.CypherQuery.identifier;
import static org.neo4j.cypherdsl.CypherQuery.node;
import static org.neo4j.cypherdsl.CypherQuery.order;
import static org.neo4j.cypherdsl.CypherQuery.query;
import static org.neo4j.cypherdsl.CypherQuery.start;
import static org.neo4j.cypherdsl.Order.DESCENDING;

import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.conversion.Handler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.github.timmystorms.datagraph338.entity.Collaboration;
import com.github.timmystorms.datagraph338.entity.Collaboration.Visibility;
import com.github.timmystorms.datagraph338.entity.Tag;
import com.github.timmystorms.datagraph338.repository.CollaborationRepository;
import com.github.timmystorms.datagraph338.repository.TagRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringConfiguration.class })
public class QueryTest {

    @Autowired
    private CollaborationRepository collaborationRepo;

    @Autowired
    private TagRepository tagRepo;

    @Autowired
    private ExecutionEngine executionEngine;

    @Before
    @Transactional
    @SuppressWarnings("serial")
    public void init() {
        final Tag tag = tagRepo.save(new Tag("neo4j"));
        final Collaboration collab = new Collaboration("collab", "a collab description");
        collab.setTags(new HashSet<Tag>() {
            {
                add(tag);
            }
        });
        collab.setVisibility(Visibility.PUBLIC);
        collaborationRepo.save(collab);
        final Collaboration collab2 = new Collaboration("collab2", "a collab description");
        collab2.setTags(new HashSet<Tag>() {
            {
                add(tag);
            }
        });
        collab2.setVisibility(Visibility.PRIVATE);
        collaborationRepo.save(collab2);
    }

    @After
    @Transactional
    public void clear() {
        tagRepo.deleteAll();
        collaborationRepo.deleteAll();
    }

    @Test
    public void queryWithExecutionEngine() {
        executionEngine.execute("CYPHER 1.9 "
                + "START entity=node:global(\"(coll_name:*collab*) OR (collab_desc:*collab*)\") "
                + "MATCH (entity)-[:HAS|ANOTHER_REL_TYPE]->(x) WHERE entity.visibility? = \"PUBLIC\" "
                + "RETURN DISTINCT entity " + "ORDER BY id(entity) DESCENDING");
    }

    // FIXME Failing test!
    @Test
    public void queryWithCypherDsl() {
        final String nodeIdentifier = "entity";
        final EndResult<Collaboration> result = collaborationRepo.query(
                start(query(nodeIdentifier, "global", "(coll_name:*collab*) OR (collab_desc:*collab*)"))
                        .match(node(nodeIdentifier).out("HAS", "ANOTHER_REL_TYPE").node("x"))
                        .where(identifier(nodeIdentifier).property("visibility").trueIfMissing()
                                .eq(Visibility.PUBLIC.name())).returns(distinct(identifier(nodeIdentifier)))
                        .orderBy(order(id(identifier(nodeIdentifier)), DESCENDING)), null);
        result.handle(new Handler<Collaboration>() {
            public void handle(final Collaboration value) {
                // do something
            }
        });
    }

}
