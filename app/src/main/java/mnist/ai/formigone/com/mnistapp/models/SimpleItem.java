package mnist.ai.formigone.com.mnistapp.models;

/**
 * Created by rsilveira on 9/18/17.
 */

public class SimpleItem {
    private String label;
    private boolean active;

    public SimpleItem(String label, boolean active) {
        this.label = label;
        this.active = active;
    }

    public SimpleItem(String label) {
        this.label = label;
        this.active = true;
    }

    public String toString() {
        return "{\"label\": " + label + ", \"active\": " + active + "}";
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
