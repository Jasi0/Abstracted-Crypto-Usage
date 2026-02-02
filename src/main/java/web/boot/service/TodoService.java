package web.boot.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import web.boot.model.TodoItem;

public class TodoService {
    private static final Logger log = LoggerFactory.getLogger(TodoService.class);

    private final ConcurrentMap<Long, TodoItem> store = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(0);

    public List<TodoItem> list() {
        List<TodoItem> items = new ArrayList<>(store.values());
        items.sort((a, b) -> Long.compare(a.getId(), b.getId()));
        return Collections.unmodifiableList(items);
    }

    public TodoItem add(String text) {
        String t = text == null ? "" : text.trim();
        if (t.isEmpty()) {
            throw new IllegalArgumentException("text must not be blank");
        }
        long id = idGen.incrementAndGet();
        TodoItem item = new TodoItem(id, t, false);
        store.put(id, item);
        log.info("Added todo id={} text='{}'", id, t);
        return item;
    }

    public TodoItem toggle(long id) {
        TodoItem item = store.get(id);
        if (item == null) {
            return null;
        }
        item.setDone(!item.isDone());
        log.info("Toggled todo id={} -> done={}", id, item.isDone());
        return item;
    }

    public boolean delete(long id) {
        TodoItem removed = store.remove(id);
        if (removed != null) {
            log.info("Deleted todo id={}", id);
            return true;
        }
        return false;
    }
}