package web.boot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import web.boot.model.GreetingResponse;
import web.boot.model.ErrorResponse;
import web.boot.util.StringUtil;

@RestController
@RequestMapping("/api")
public class HelloController {
    private static final Logger log = LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/hello")
    public String hello(@RequestParam(name = "name", defaultValue = "World") String name) {
        String trimmed = name == null ? "World" : name.trim();
        log.info("Hello endpoint called with name='{}'", trimmed);
        return "Hello, " + (trimmed.isEmpty() ? "World" : trimmed) + "!";
    }

    @GetMapping("/hello/json")
    public GreetingResponse helloJson(@RequestParam(name = "name", defaultValue = "World") String name) {
        String trimmed = name == null ? "World" : name.trim();
        log.info("Hello JSON endpoint called with name='{}'", trimmed);
        return new GreetingResponse("Hello, " + (trimmed.isEmpty() ? "World" : trimmed) + "!");
    }

    @GetMapping("/util/reverse")
    public ResponseEntity<?> reverse(@RequestParam(name = "text") String text) {
        if (text == null || text.trim().isEmpty()) {
            log.warn("Reverse called with blank text");
            return ResponseEntity.badRequest().body(new ErrorResponse("text must not be blank", "Provide a non-empty 'text' query parameter"));
        }
        String reversed = StringUtil.reverse(text);
        log.debug("Reversed '{}' -> '{}'", text, reversed);
        return ResponseEntity.ok(reversed);
    }

    @GetMapping("/util/uppercase")
    public ResponseEntity<?> uppercase(@RequestParam(name = "text") String text) {
        if (text == null || text.trim().isEmpty()) {
            log.warn("Uppercase called with blank text");
            return ResponseEntity.badRequest().body(new ErrorResponse("text must not be blank", "Provide a non-empty 'text' query parameter"));
        }
        String upper = StringUtil.uppercase(text);
        log.debug("Uppercase '{}' -> '{}'", text, upper);
        return ResponseEntity.ok(upper);
    }
}