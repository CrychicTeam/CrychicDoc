package dev.kosmx.playerAnim.api.firstPerson;

public class FirstPersonConfiguration {

    boolean showRightArm = false;

    boolean showLeftArm = false;

    boolean showRightItem = true;

    boolean showLeftItem = true;

    public boolean isShowRightArm() {
        return this.showRightArm;
    }

    public boolean isShowLeftArm() {
        return this.showLeftArm;
    }

    public boolean isShowRightItem() {
        return this.showRightItem;
    }

    public boolean isShowLeftItem() {
        return this.showLeftItem;
    }

    public FirstPersonConfiguration setShowRightArm(boolean showRightArm) {
        this.showRightArm = showRightArm;
        return this;
    }

    public FirstPersonConfiguration setShowLeftArm(boolean showLeftArm) {
        this.showLeftArm = showLeftArm;
        return this;
    }

    public FirstPersonConfiguration setShowRightItem(boolean showRightItem) {
        this.showRightItem = showRightItem;
        return this;
    }

    public FirstPersonConfiguration setShowLeftItem(boolean showLeftItem) {
        this.showLeftItem = showLeftItem;
        return this;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof FirstPersonConfiguration)) {
            return false;
        } else {
            FirstPersonConfiguration other = (FirstPersonConfiguration) o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.isShowRightArm() != other.isShowRightArm()) {
                return false;
            } else if (this.isShowLeftArm() != other.isShowLeftArm()) {
                return false;
            } else {
                return this.isShowRightItem() != other.isShowRightItem() ? false : this.isShowLeftItem() == other.isShowLeftItem();
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof FirstPersonConfiguration;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isShowRightArm() ? 79 : 97);
        result = result * 59 + (this.isShowLeftArm() ? 79 : 97);
        result = result * 59 + (this.isShowRightItem() ? 79 : 97);
        return result * 59 + (this.isShowLeftItem() ? 79 : 97);
    }

    public String toString() {
        return "FirstPersonConfiguration(showRightArm=" + this.isShowRightArm() + ", showLeftArm=" + this.isShowLeftArm() + ", showRightItem=" + this.isShowRightItem() + ", showLeftItem=" + this.isShowLeftItem() + ")";
    }

    public FirstPersonConfiguration() {
    }

    public FirstPersonConfiguration(boolean showRightArm, boolean showLeftArm, boolean showRightItem, boolean showLeftItem) {
        this.showRightArm = showRightArm;
        this.showLeftArm = showLeftArm;
        this.showRightItem = showRightItem;
        this.showLeftItem = showLeftItem;
    }
}