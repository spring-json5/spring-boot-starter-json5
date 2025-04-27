package io.github.spring.json5;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {

    @PostMapping("/resource")
    public Resource createResource(@RequestBody Resource resource) {
        return resource;
    }

    @PostMapping("/features")
    public Object testFeatures(@RequestBody Object json5Object) {
        return json5Object;
    }
}