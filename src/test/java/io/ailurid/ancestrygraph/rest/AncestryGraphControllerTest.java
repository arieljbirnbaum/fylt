package io.ailurid.ancestrygraph.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restdocs.test.autoconfigure.AutoConfigureRestDocs;
import org.springframework.boot.restdocs.test.autoconfigure.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;
import org.springframework.test.web.servlet.MockMvc;

import io.ailurid.ancestrygraph.model.Node;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class AncestryGraphControllerTest {
        @Autowired
        MockMvc mockMvc;
        @Autowired
        ObjectMapper objectMapper;

        private static final String VALID_RAW_EDGE_LIST_JSON = "{\n" +
                        "  \"edges\": [\n" +
                        "       [10, 3],\n" +
                        "       [2, 3],\n" +
                        "       [3, 6],\n" +
                        "       [5, 6],\n" +
                        "       [5, 17],\n" +
                        "       [4, 5],\n" +
                        "       [4, 8],\n" +
                        "       [8, 9]\n" +
                        "  ]\n" +
                        "}\n" +
                        "";

        @TestConfiguration(proxyBeanMethods = false)
        static class ResponsePrettyPrinter implements RestDocsMockMvcConfigurationCustomizer {
                @Override
                public void customize(MockMvcRestDocumentationConfigurer configurer) {
                        configurer.operationPreprocessors()
                                        .withRequestDefaults(prettyPrint())
                                        .withResponseDefaults(prettyPrint());
                }
        }

        @Test
        public void shouldCreateNewGraphAndRespondToQueriesCorrectly() throws Exception {
                // Create graph and parse response
                var createRequest = post("/api/ancestry-graphs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(VALID_RAW_EDGE_LIST_JSON)
                                .accept(MediaTypes.HAL_JSON);
                var createResult = this.mockMvc.perform(createRequest)
                                .andExpectAll(status().isOk())
                                .andDo(document("create", links(
                                                linkWithRel("self").description("Link to this graph"),
                                                linkWithRel("nodesByParentCount")
                                                                .description("Get nodes grouped by parent count"),
                                                linkWithRel("shareCommonAncestor")
                                                                .description("Query whether two nodes share an ancestor"))))
                                .andReturn();
                var createNode = objectMapper.readTree(createResult.getResponse().getContentAsByteArray());

                // Get nodes by parent count
                var nodesByParentCountRequest = get(
                                createNode.at("/_links/nodesByParentCount/href").asString());
                var nodesByParentCountResult = this.mockMvc.perform(nodesByParentCountRequest)
                                .andExpect(status().isOk())
                                .andDo(document("nodesByParentCount"))
                                .andReturn();
                var nodesByParentCount = objectMapper.readValue(
                                objectMapper.readTree(
                                                nodesByParentCountResult.getResponse().getContentAsByteArray())
                                                .path("nodesByParentCount").toString(),
                                new TypeReference<Map<Integer, Set<Node>>>() {
                                });
                assertThat(
                                nodesByParentCount.get(0),
                                containsInAnyOrder(
                                                Arrays.stream(new Integer[] { 10, 2, 4 })
                                                                .map(nodeId -> new Node(nodeId)).toArray()));

                // Get share common ancestor
                var shareCommonAncestorRequest = get(
                                createNode.at("/_links/shareCommonAncestor/href").asString(),
                                6,
                                8);
                var shareCommonAncestorResult = this.mockMvc.perform(shareCommonAncestorRequest)
                                .andExpect(status().isOk())
                                .andDo(document("shareCommonAncestor"))
                                .andReturn();
                var shareCommonAncestorNode = objectMapper
                                .readTree(shareCommonAncestorResult.getResponse().getContentAsByteArray());
                assertThat(shareCommonAncestorNode
                                .get("result").asBoolean(), equalTo(true));

        }

}
