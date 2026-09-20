package io.ailurid.ancestrygraph.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.ailurid.ancestrygraph.model.AncestryGraph;
import io.ailurid.ancestrygraph.model.Node;
import io.ailurid.ancestrygraph.model.RawEdgeList;

public class ParentCounterTest {
        @ParameterizedTest
        @MethodSource("provideArgsForGroupByParentCount")
        void parentCountsAreComputedCorrectly(
                        int[][] parentChildPairs,
                        Integer[] expectedZeroParentNodes,
                        Integer[] expectedOneParentNodes) {
                var graph = new AncestryGraph(new RawEdgeList(parentChildPairs));
                var groupedByParentCount = ParentCounter.groupByParentCount(graph);

                BiConsumer<Integer, Integer[]> checkGroup = (count, expected) -> assertThat(
                                groupedByParentCount.get(count),
                                containsInAnyOrder(
                                                Arrays.stream(expected).map(nodeId -> new Node(nodeId))
                                                                .toArray())

                );

                checkGroup.accept(0, expectedZeroParentNodes);
                checkGroup.accept(1, expectedOneParentNodes);
        }

        private static Stream<Arguments> provideArgsForGroupByParentCount() {
                return Stream.of(
                                // Sample from README
                                Arguments.of(
                                                new int[][] {
                                                                { 10, 3 }, { 2, 3 }, { 3, 6 }, { 5, 6 }, { 5, 17 },
                                                                { 4, 5 }, { 4, 8 }, { 8, 9 }
                                                },
                                                new Integer[] { 10, 2, 4 },
                                                new Integer[] { 5, 17, 8, 9 }),
                                // No single-parent nodes
                                Arguments.of(
                                                new int[][] {
                                                                { 10, 3 }, { 2, 3 }
                                                },
                                                new Integer[] { 10, 2 },
                                                new Integer[] {}));
        }
}
