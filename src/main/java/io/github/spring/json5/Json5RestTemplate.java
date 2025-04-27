package io.github.spring.json5;

import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * This child of RestTemplate out-of-the-box able Serialize/Deserialize JSON5.
 * Important note: You still must provide headers Content-Type and/or Accept with value application/json5 if you want to send and/or receive JSON5 messages.
 * If you do not want every time to provide Content-Type and/or Accept, you can once call useOnlyJson5ContentType() and/or useOnlyJson5AcceptHeader() to this class automatically set these headers in value application/json5.
 */
public class Json5RestTemplate extends RestTemplate {

    public Json5RestTemplate() {
        getMessageConverters().add(new Json5HttpMessageConverter());
    }

    public Json5RestTemplate(Feature... features) {
        getMessageConverters().add(new Json5HttpMessageConverter());
        for (Feature feature : features) {
            switch (feature) {
                case USE_ONLY_JSON5_CONTENT_TYPE -> useOnlyJson5ContentType();
                case USE_ONLY_JSON5_ACCEPT -> useOnlyJson5AcceptHeader();
                default -> throw new IllegalArgumentException("Unknown feature: " + feature);
            }
        }
    }

    protected void useOnlyJson5ContentType() {
        getInterceptors().add((request, body, execution) -> {
            request.getHeaders().setContentType(Json5.MEDIA_TYPE);
            return execution.execute(request, body);
        });
    }

    protected void useOnlyJson5AcceptHeader() {
        getInterceptors().add((request, body, execution) -> {
            request.getHeaders().setAccept(List.of(Json5.MEDIA_TYPE));
            return execution.execute(request, body);
        });
    }

    public enum Feature {
        USE_ONLY_JSON5_CONTENT_TYPE,
        USE_ONLY_JSON5_ACCEPT,
    }
}
