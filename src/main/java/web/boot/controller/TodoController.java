package web.boot.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import web.boot.model.ErrorResponse;
import web.boot.model.TodoItem;
import web.boot.service.TodoService;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private static final Logger log = LoggerFactory.getLogger(TodoController.class);

    // Keep simple: instantiate service directly (no Spring bean wiring needed for this demo)
    private final TodoService service = new TodoService();

    @GetMapping
    public List<TodoItem> listTodos() {
        log.info("Listing todos");
        return service.list();
    }

    @PostMapping
    public ResponseEntity<?> addTodo(@RequestParam(name = "text") String text) {
        try {
            TodoItem item = service.add(text);
            return ResponseEntity.ok(item);
        } catch (IllegalArgumentException ex) {
            log.warn("Add todo failed: {}", ex.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse("text must not be blank", ex.getMessage()));
        }
    }

    @PostMapping("/{id}/toggle")
    public ResponseEntity<?> toggle(@PathVariable("id") long id) {
        TodoItem item = service.toggle(id);
        if (item == null) {
            log.warn("Toggle todo failed: id={} not found", id);
            return ResponseEntity.status(404).body(new ErrorResponse("not found", "No todo with id=" + id));
        }
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        boolean removed = service.delete(id);
        if (!removed) {
            log.warn("Delete todo failed: id={} not found", id);
            return ResponseEntity.status(404).body(new ErrorResponse("not found", "No todo with id=" + id));
        }
        return ResponseEntity.noContent().build();
    }
}