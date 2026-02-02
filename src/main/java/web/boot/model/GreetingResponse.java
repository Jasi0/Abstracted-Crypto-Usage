package web.boot.model;

/**
 * Plain POJO to avoid Lombok dependency during initial compilation.
 * Lombok annotations can be re-added later if desired.
 */
public class GreetingResponse {
    private String message;

    public GreetingResponse() {
    }

    public GreetingResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
