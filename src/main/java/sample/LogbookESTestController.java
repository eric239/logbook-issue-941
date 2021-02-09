package sample;

import java.io.IOException;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LogbookESTestController {

    private final RestHighLevelClient esClient;

    public LogbookESTestController(final RestHighLevelClient esClient) {
        this.esClient = esClient;
    }

    @GetMapping("/cluster-health")
    public ClusterHealthResponse getCatalog() throws IOException {
        return esClient.cluster().health(new ClusterHealthRequest(), RequestOptions.DEFAULT);
    }
}
