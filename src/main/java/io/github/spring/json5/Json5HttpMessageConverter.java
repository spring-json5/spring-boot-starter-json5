package io.github.spring.json5;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class Json5HttpMessageConverter extends AbstractJackson2HttpMessageConverter {

    public Json5HttpMessageConverter() {
        super(new Json5Mapper(), Json5.MEDIA_TYPE);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        var requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            // Probably code called from RestTemplate
            return mediaType.equalsTypeAndSubtype(Json5.MEDIA_TYPE);
        } else {
            // Code called on RestController
            var servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            var request = servletRequestAttributes.getRequest();
            var accepts = MediaType.parseMediaTypes(request.getHeader(HttpHeaders.ACCEPT));
            boolean json5Requested = accepts.stream().anyMatch(mt -> mt.equalsTypeAndSubtype(Json5.MEDIA_TYPE));
            return json5Requested && super.canWrite(clazz, mediaType);
        }
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        if (mediaType == null) {
            return false;
        }
        boolean json5Content = mediaType.equalsTypeAndSubtype(Json5.MEDIA_TYPE);
        return json5Content && super.canRead(clazz, mediaType);
    }

}
