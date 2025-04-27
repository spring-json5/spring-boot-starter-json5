package io.github.spring.json5;

import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

import static io.github.spring.json5.Json5RestTemplate.Feature.USE_ONLY_JSON5_ACCEPT;
import static io.github.spring.json5.Json5RestTemplate.Feature.USE_ONLY_JSON5_CONTENT_TYPE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringBootStarterJson5ApplicationTests {

    @LocalServerPort
    private int port;

    private static final Random RANDOM = new Random();

    @Test
    void testWithNormalJson() {
        String url = "http://localhost:" + port + "/resource";
        var restTemplate = new RestTemplate();

        var resource = new Resource(RandomString.make(10), RANDOM.nextInt(), RANDOM.nextDouble(), List.of("tag1", "tag2"));

        var response = restTemplate.postForEntity(url, resource, Resource.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getHeaders().asSingleValueMap()).containsEntry(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.getBody()).isEqualTo(resource);
    }

    /**
     * Provide a normal JSON request body. Means 'Content-Type: application/json'
     * And JSON5 response body. Means 'Accept: application/json5'
     */
    @Test
    void testWithNormalJsonRequestAndJson5Response() {
        String url = "http://localhost:" + port + "/resource";
        var restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(List.of(new MappingJackson2HttpMessageConverter(), new Json5HttpMessageConverter()));

        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(List.of(Json5.MEDIA_TYPE));

        var resource = new Resource(RandomString.make(10), RANDOM.nextInt(), RANDOM.nextDouble(), List.of("tag1", "tag2"));

        var response = restTemplate.postForEntity(url, new HttpEntity<>(resource, httpHeaders), Resource.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getHeaders().asSingleValueMap()).containsEntry(HttpHeaders.CONTENT_TYPE, Json5.MEDIA_TYPE_NAME);
        assertThat(response.getBody()).isEqualTo(resource);
    }

    /**
     * Provide a JSON5 request body. Means 'Content-Type: application/json5'
     * And JSON5 response body. Means 'Accept: application/json5'
     */
    @Test
    void testWithJson5RequestAndJson5Response() {
        String url = "http://localhost:" + port + "/resource";
        var json5RestTemplate = new Json5RestTemplate(USE_ONLY_JSON5_CONTENT_TYPE, USE_ONLY_JSON5_ACCEPT);

        var resource = new Resource(RandomString.make(10), RANDOM.nextInt(), RANDOM.nextDouble(), List.of("tag1", "tag2"));

        var response = json5RestTemplate.postForEntity(url, new HttpEntity<>(resource), Resource.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getHeaders().asSingleValueMap()).containsEntry(HttpHeaders.CONTENT_TYPE, Json5.MEDIA_TYPE_NAME);
        assertThat(response.getBody()).isEqualTo(resource);
    }

    /**
     * Provide a JSON5 request body. Means 'Content-Type: application/json5'
     * And normal JSON response body. Means 'Accept: application/json'
     */
    @Test
    void testWithJson5RequestAndNormalJsonResponse() {
        String url = "http://localhost:" + port + "/resource";
        var restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(List.of(new MappingJackson2HttpMessageConverter(), new Json5HttpMessageConverter()));

        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(Json5.MEDIA_TYPE);
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

        var resource = new Resource(RandomString.make(10), RANDOM.nextInt(), RANDOM.nextDouble(), List.of("tag1", "tag2"));

        var response = restTemplate.postForEntity(url, new HttpEntity<>(resource, httpHeaders), Resource.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getHeaders().asSingleValueMap()).containsEntry(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.getBody()).isEqualTo(resource);
    }

    /**
     * Checking features for serialization and deserialization.
     * Most features for deserialization (read from JSON5 string) are working.
     * But for serialization only removing quotes is the main feature.
     */
    @Test
    void testJson5SerializationFeatures() {
        String url = "http://localhost:" + port + "/features";
        var json5RestTemplate = new Json5RestTemplate(USE_ONLY_JSON5_CONTENT_TYPE, USE_ONLY_JSON5_ACCEPT);

        final var json5StringForTestFeaturesRequest = """
                {
                  // comments
                  unquoted: 'and you can quote me on that',
                  singleQuotes: 'I can use "double quotes" here', // <-- You can provide double quotes inside string for request body
                  lineBreaks: "Look, Mom! \
                No \\n's!",
                  // hexadecimal: 0xdecaf, <-- NOT SUPPORTED
                  leadingDecimalPoint: .8675309, andTrailing: 8675309.,
                  positiveSign: +1,
                  trailingComma: 'in objects', andIn: ['arrays',],
                  "backwardsCompatible": "with JSON",
                }
                """;

        final var json5StringForTestFeaturesResponse = """
                {
                unquoted:"and you can quote me on that",
                singleQuotes:"I can use \\"double quotes\\" here",
                lineBreaks:"Look, Mom! \
                No \\n's!",
                leadingDecimalPoint:0.8675309,andTrailing:8675309.0,
                positiveSign:1,
                trailingComma:"in objects",andIn:["arrays"],
                backwardsCompatible:"with JSON"
                }
                """.replaceAll("\n", "");
        // For response all string values will have double quotes
        // For response double quotes will have backslashes
        // For response leading and trailing decimal point will be with zeros
        // For response positive sign will not have a plus mark

        var response = json5RestTemplate.postForEntity(url, new HttpEntity<>(json5StringForTestFeaturesRequest), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getHeaders().asSingleValueMap()).containsEntry(HttpHeaders.CONTENT_TYPE, Json5.MEDIA_TYPE_NAME);
        assertThat(response.getBody()).isEqualTo(json5StringForTestFeaturesResponse);
    }

}