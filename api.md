Ancestry Graph Application {#header}
====================================

Create New Graph {#_create_new_graph}
-------------------------------------

### HTTP request {#_create_new_graph_http_request}

```http
POST /api/ancestry-graphs HTTP/1.1
Content-Type: application/json;charset=UTF-8
Accept: application/hal+json
Content-Length: 102
Host: localhost:8080

{
  "edges" : [ [ 10, 3 ], [ 2, 3 ], [ 3, 6 ], [ 5, 6 ], [ 5, 17 ], [ 4, 5 ], [ 4, 8 ], [ 8, 9 ] ]
}
```

### HTTP response {#_create_new_graph_http_response}

```http
HTTP/1.1 200 OK
Content-Type: application/hal+json
Content-Length: 796

{
  "id" : "af4b9c6b-3fd3-4bb6-aea5-9920eae36cb2",
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/api/ancestry-graphs/af4b9c6b-3fd3-4bb6-aea5-9920eae36cb2"
    },
    "nodesByParentCount" : {
      "href" : "http://localhost:8080/api/ancestry-graphs/af4b9c6b-3fd3-4bb6-aea5-9920eae36cb2/nodes?groupByParentCount=true"
    },
    "shareCommonAncestor" : {
      "href" : "http://localhost:8080/api/ancestry-graphs/af4b9c6b-3fd3-4bb6-aea5-9920eae36cb2/nodes/share-common-ancestor?nodeA={nodeA}&nodeB={nodeB}",
      "templated" : true
    }
  },
  "nodes" : [ {
    "id" : 17
  }, {
    "id" : 2
  }, {
    "id" : 3
  }, {
    "id" : 4
  }, {
    "id" : 5
  }, {
    "id" : 6
  }, {
    "id" : 8
  }, {
    "id" : 9
  }, {
    "id" : 10
  } ]
}
```

### Links {#_create_new_graph_links}

| Relation              | Description                               |
|:----------------------|:------------------------------------------|
| `self`                | Link to this graph                        |
| `nodesByParentCount`  | Get nodes grouped by parent count         |
| `shareCommonAncestor` | Query whether two nodes share an ancestor |

Find Nodes Grouped by Parent Count {#_find_nodes_grouped_by_parent_count}
-------------------------------------------------------------------------

### HTTP request {#_find_nodes_grouped_by_parent_count_http_request}

```http
GET /api/ancestry-graphs/af4b9c6b-3fd3-4bb6-aea5-9920eae36cb2/nodes?groupByParentCount=true HTTP/1.1
Host: localhost:8080
```

### HTTP response {#_find_nodes_grouped_by_parent_count_http_response}

```http
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 317

{
  "nodesByParentCount" : {
    "0" : [ {
      "id" : 2
    }, {
      "id" : 4
    }, {
      "id" : 10
    } ],
    "1" : [ {
      "id" : 17
    }, {
      "id" : 5
    }, {
      "id" : 8
    }, {
      "id" : 9
    } ],
    "2" : [ {
      "id" : 3
    }, {
      "id" : 6
    } ]
  }
}
```

Query whether Two Nodes Share a Common Ancestor {#_query_whether_two_nodes_share_a_common_ancestor}
---------------------------------------------------------------------------------------------------

### HTTP request {#_query_whether_two_nodes_share_a_common_ancestor_http_request}

```http
GET /api/ancestry-graphs/af4b9c6b-3fd3-4bb6-aea5-9920eae36cb2/nodes/share-common-ancestor?nodeA=6&nodeB=8 HTTP/1.1
Host: localhost:8080
```

### HTTP response {#_query_whether_two_nodes_share_a_common_ancestor_http_response}

```http
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 23

{
  "result" : true
}
```

Last updated 2026-09-21 05:59:49 +0200
