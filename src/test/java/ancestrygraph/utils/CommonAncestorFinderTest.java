package ancestrygraph.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import ancestrygraph.AncestryGraph;
import ancestrygraph.Node;

public class CommonAncestorFinderTest {
        @ParameterizedTest
        @MethodSource("provideArgsForGroupByParentCount")
        void commonAncestorsAreComputedCorrectly(int[][] parentChildPairs, int[] nodePair,
                        boolean expected) {
                assertThat(CommonAncestorFinder.fromAncestryGraph(new AncestryGraph(parentChildPairs))
                                .shareCommonAncestor(
                                                new Node(nodePair[0]),
                                                new Node(nodePair[1])),
                                equalTo(expected));
        }

        private static Stream<Arguments> provideArgsForGroupByParentCount() {
                // Sample from README
                int[][] fylturaSample = new int[][] {
                                { 10, 3 }, { 2, 3 }, { 3, 6 }, { 5, 6 }, { 5, 17 },
                                { 4, 5 }, { 4, 8 }, { 8, 9 }
                };
                return Stream.of(
                                Arguments.of(
                                                fylturaSample,
                                                new int[] { 3, 8 },
                                                false),
                                Arguments.of(
                                                fylturaSample,
                                                new int[] { 5, 8 },
                                                true),
                                Arguments.of(
                                                fylturaSample,
                                                new int[] { 6, 8 },
                                                true),
                                Arguments.of(
                                                fylturaSample,
                                                new int[] { 6, 5 },
                                                true));
        }
}
