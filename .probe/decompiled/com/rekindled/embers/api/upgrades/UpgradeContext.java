package com.rekindled.embers.api.upgrades;

public class UpgradeContext {

    protected IUpgradeProvider upgrade;

    protected int distance;

    protected int count;

    public UpgradeContext(IUpgradeProvider upgrade, int distance, int count) {
        this.upgrade = upgrade;
        this.distance = distance;
        this.count = count;
    }

    public UpgradeContext(IUpgradeProvider upgrade, int distance) {
        this.upgrade = upgrade;
        this.distance = distance;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public IUpgradeProvider upgrade() {
        return this.upgrade;
    }

    public int distance() {
        return this.distance;
    }

    public int count() {
        return this.count;
    }
}