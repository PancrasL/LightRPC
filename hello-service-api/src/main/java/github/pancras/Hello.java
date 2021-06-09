package github.pancras;

/**
 * @author pancras
 * @create 2021/6/3 18:50
 */
public class Hello {
    private String message;
    private String description;

    public Hello() {
    }

    public Hello(String message, String description) {
        this.message = message;
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Hello{" +
                "message='" + message + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
