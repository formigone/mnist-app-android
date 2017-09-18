package mnist.ai.formigone.com.mnistapp.models;

/**
 * Created by rsilveira on 9/18/17.
 */

public class SimpleItem {
    private String label;
    private String category;
    private boolean active;

    public SimpleItem(String label, String category, boolean active) {
        this.label = label;
        this.category = category;
        this.active = active;
    }

    public SimpleItem(String label, String category) {
        this.label = label;
        this.category = category;
        this.active = true;
    }

    public String toString() {
        return "{\"label\": " + label + ", \"category\": " + category + ", \"active\": " + active + "}";
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
