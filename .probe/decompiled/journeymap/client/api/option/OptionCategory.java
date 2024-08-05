package journeymap.client.api.option;

import com.google.common.base.MoreObjects;

public class OptionCategory {

    private final String modId;

    private final String toolTip;

    private final String label;

    public OptionCategory(String modId, String label, String toolTip) {
        this.modId = modId;
        this.label = label;
        this.toolTip = toolTip;
    }

    public OptionCategory(String modId, String key) {
        this.modId = modId;
        this.label = key;
        this.toolTip = key + ".tooltip";
    }

    public String getModId() {
        return this.modId;
    }

    public String getToolTip() {
        return this.toolTip;
    }

    public String getLabel() {
        return this.label;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("modId", this.modId).add("toolTip", this.toolTip).add("label", this.label).toString();
    }
}