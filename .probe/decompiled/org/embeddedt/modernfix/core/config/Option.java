package org.embeddedt.modernfix.core.config;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class Option {

    private final String name;

    private Set<String> modDefined = null;

    private boolean enabled;

    private boolean userDefined;

    private Option parent = null;

    public Option(String name, boolean enabled, boolean userDefined) {
        this.name = name;
        this.enabled = enabled;
        this.userDefined = userDefined;
    }

    public void setEnabled(boolean enabled, boolean userDefined) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            this.userDefined = userDefined;
        }
    }

    public void addModOverride(boolean enabled, String modId) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            if (this.modDefined == null) {
                this.modDefined = new LinkedHashSet();
            }
            this.modDefined.add(modId);
        }
    }

    public void setParent(Option option) {
        this.parent = option;
    }

    public Option getParent() {
        return this.parent;
    }

    public int getDepth() {
        return this.parent == null ? 0 : this.parent.getDepth() + 1;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isEffectivelyDisabledByParent() {
        return this.parent != null && (!this.parent.enabled || this.parent.isEffectivelyDisabledByParent());
    }

    public boolean isOverridden() {
        return this.isUserDefined() || this.isModDefined();
    }

    public boolean isUserDefined() {
        return this.userDefined;
    }

    public boolean isModDefined() {
        return this.modDefined != null;
    }

    public String getName() {
        return this.name;
    }

    public String getSelfName() {
        return this.parent == null ? this.name : this.name.substring(this.parent.getName().length() + 1);
    }

    public void clearModsDefiningValue() {
        this.modDefined = null;
    }

    public void clearUserDefined() {
        this.userDefined = false;
    }

    public Collection<String> getDefiningMods() {
        return (Collection<String>) (this.modDefined != null ? Collections.unmodifiableCollection(this.modDefined) : Collections.emptyList());
    }
}