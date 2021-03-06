package grakn.core.graql.internal.reasoner;


import grakn.core.GraknSession;
import grakn.core.GraknTx;
import grakn.core.GraknTxType;
import grakn.core.concept.Entity;
import grakn.core.factory.EmbeddedGraknSession;
import grakn.core.graql.Query;
import grakn.core.graql.admin.Conjunction;
import grakn.core.graql.admin.Unifier;
import grakn.core.graql.admin.VarPatternAdmin;
import grakn.core.graql.answer.ConceptMap;
import grakn.core.graql.internal.pattern.Patterns;
import grakn.core.graql.internal.query.answer.ConceptMapImpl;
import grakn.core.graql.internal.reasoner.cache.LazyQueryCache;
import grakn.core.graql.internal.reasoner.iterator.LazyAnswerIterator;
import grakn.core.graql.internal.reasoner.query.ReasonerAtomicQuery;
import grakn.core.graql.internal.reasoner.query.ReasonerQueries;
import grakn.core.kb.internal.EmbeddedGraknTx;
import grakn.core.test.rule.ConcurrentGraknServer;
import com.google.common.collect.ImmutableMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static grakn.core.graql.Graql.var;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("CheckReturnValue")
public class LazyQueryCacheIT {

    @ClassRule
    public static final ConcurrentGraknServer server = new ConcurrentGraknServer();

    private static EmbeddedGraknSession ruleApplicabilitySession;

    private static void loadFromFile(String fileName, GraknSession session) {
        try {
            InputStream inputStream = LazyQueryCacheIT.class.getClassLoader().getResourceAsStream("test-integration/test/graql/reasoner/resources/" + fileName);
            String s = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
            GraknTx tx = session.transaction(GraknTxType.WRITE);
            tx.graql().parser().parseList(s).forEach(Query::execute);
            tx.commit();
        } catch (Exception e) {
            System.err.println(e);
            throw new RuntimeException(e);
        }
    }

    @BeforeClass
    public static void loadContext() {
        ruleApplicabilitySession = server.sessionWithNewKeyspace();
        loadFromFile("ruleApplicabilityTest.gql", ruleApplicabilitySession);

    }

    @AfterClass
    public static void closeSession() {
        ruleApplicabilitySession.close();
    }

    private static ReasonerAtomicQuery recordQuery;
    private static ReasonerAtomicQuery retrieveQuery;
    private static ConceptMap singleAnswer;
    private static Unifier retrieveToRecordUnifier;
    private static Unifier recordToRetrieveUnifier;
    private EmbeddedGraknTx tx;

    @Before
    public void onStartup() {
        tx = ruleApplicabilitySession.transaction(GraknTxType.WRITE);
        String recordPatternString = "{(someRole: $x, subRole: $y) isa reifiable-relation;}";
        String retrievePatternString = "{(someRole: $p1, subRole: $p2) isa reifiable-relation;}";
        Conjunction<VarPatternAdmin> recordPattern = conjunction(recordPatternString, tx);
        Conjunction<VarPatternAdmin> retrievePattern = conjunction(retrievePatternString, tx);
        recordQuery = ReasonerQueries.atomic(recordPattern, tx);
        retrieveQuery = ReasonerQueries.atomic(retrievePattern, tx);
        retrieveToRecordUnifier = retrieveQuery.getMultiUnifier(recordQuery).getUnifier();
        recordToRetrieveUnifier = retrieveToRecordUnifier.inverse();

        Entity entity = tx.getEntityType("anotherNoRoleEntity").instances().findFirst().orElse(null);
        singleAnswer = new ConceptMapImpl(
                ImmutableMap.of(
                        var("x"), entity,
                        var("y"), entity
                ));
    }

    @After
    public void closeTx() {
        tx.close();
    }

    @Test
    public void lazilyRecordRetrieveAnswers() {
        LazyQueryCache<ReasonerAtomicQuery> cache = new LazyQueryCache<>();
        cache.record(recordQuery, recordQuery.getQuery().stream());

        Set<ConceptMap> retrieve = cache.getAnswerStream(retrieveQuery).map(ans -> ans.unify(retrieveToRecordUnifier)).collect(toSet());
        Set<ConceptMap> record = cache.getAnswerStream(recordQuery).collect(toSet());

        assertTrue(!retrieve.isEmpty());
        assertEquals(record, retrieve);
    }

    @Test
    public void lazilyRecordUpdateRetrieveAnswers() {
        LazyQueryCache<ReasonerAtomicQuery> cache = new LazyQueryCache<>();
        cache.record(recordQuery, recordQuery.getQuery().stream());
        cache.record(recordQuery, Stream.of(singleAnswer));

        Set<ConceptMap> retrieve = cache.getAnswerStream(retrieveQuery).map(ans -> ans.unify(retrieveToRecordUnifier)).collect(toSet());
        Set<ConceptMap> record = cache.getAnswerStream(recordQuery).collect(toSet());

        assertTrue(!retrieve.isEmpty());
        assertTrue(retrieve.contains(singleAnswer));
        assertEquals(record, retrieve);
    }

    @Test
    public void lazilyGetRetrieveAnswers() {
        LazyQueryCache<ReasonerAtomicQuery> cache = new LazyQueryCache<>();
        cache.record(recordQuery, recordQuery.getQuery().stream());
        LazyAnswerIterator retrieveIterator = cache.getAnswers(retrieveQuery);
        LazyAnswerIterator recordIterator = cache.getAnswers(recordQuery);

        Set<ConceptMap> record = recordIterator.stream().collect(toSet());
        Set<ConceptMap> retrieve = retrieveIterator.stream().map(ans -> ans.unify(retrieveToRecordUnifier)).collect(toSet());

        assertTrue(!retrieve.isEmpty());
        assertEquals(record, retrieve);
    }

    @Test
    public void lazilyGetUpdateRetrieveAnswers() {
        LazyQueryCache<ReasonerAtomicQuery> cache = new LazyQueryCache<>();
        ConceptMap retrieveSingleAnswer = singleAnswer.unify(recordToRetrieveUnifier);
        cache.record(recordQuery, recordQuery.getQuery().stream());
        LazyAnswerIterator retrieveIterator = cache.getAnswers(retrieveQuery);
        LazyAnswerIterator recordIterator = cache.getAnswers(recordQuery);

        cache.record(recordQuery, Stream.of(singleAnswer));

        Set<ConceptMap> record = recordIterator.stream().collect(toSet());
        Set<ConceptMap> retrieve = retrieveIterator.stream().map(ans -> ans.unify(retrieveToRecordUnifier)).collect(toSet());

        assertTrue(!retrieve.isEmpty());
        assertTrue(!retrieve.contains(singleAnswer));
        assertEquals(record, retrieve);

        assertTrue(cache.getAnswers(recordQuery).stream().anyMatch(ans -> ans.equals(singleAnswer)));
        assertTrue(cache.getAnswers(retrieveQuery).stream().anyMatch(ans -> ans.equals(retrieveSingleAnswer)));
    }

    private Conjunction<VarPatternAdmin> conjunction(String patternString, GraknTx tx) {
        Set<VarPatternAdmin> vars = tx.graql().parser().parsePattern(patternString).admin()
                .getDisjunctiveNormalForm().getPatterns()
                .stream().flatMap(p -> p.getPatterns().stream()).collect(toSet());
        return Patterns.conjunction(vars);
    }
}