package de.keksuccino.fancymenu.customization.loadingrequirement.internal;

import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.fancymenu.customization.action.ValuePlaceholderHolder;
import de.keksuccino.fancymenu.customization.loadingrequirement.LoadingRequirement;
import de.keksuccino.fancymenu.customization.loadingrequirement.LoadingRequirementRegistry;
import de.keksuccino.fancymenu.customization.placeholder.PlaceholderParser;
import de.keksuccino.fancymenu.util.input.CharacterFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.Supplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LoadingRequirementInstance implements ValuePlaceholderHolder {

    private static final Logger LOGGER = LogManager.getLogger();

    public LoadingRequirementContainer parent;

    public LoadingRequirement requirement;

    @Nullable
    public String value;

    protected final Map<String, Supplier<String>> valuePlaceholders = new HashMap();

    @Nullable
    public LoadingRequirementGroup group;

    public LoadingRequirementInstance.RequirementMode mode;

    public String instanceIdentifier = ScreenCustomization.generateUniqueIdentifier();

    public LoadingRequirementInstance(LoadingRequirement requirement, @Nullable String value, LoadingRequirementInstance.RequirementMode mode, LoadingRequirementContainer parent) {
        this.parent = parent;
        this.requirement = requirement;
        this.value = value;
        this.mode = mode;
    }

    public boolean requirementMet() {
        String v = this.value;
        if (v != null) {
            for (Entry<String, Supplier<String>> m : this.valuePlaceholders.entrySet()) {
                String replaceWith = (String) ((Supplier) m.getValue()).get();
                if (replaceWith == null) {
                    replaceWith = "";
                }
                v = v.replace("$$" + (String) m.getKey(), replaceWith);
            }
            v = PlaceholderParser.replacePlaceholders(v);
        }
        boolean met = this.requirement.isRequirementMet(v);
        return this.mode == LoadingRequirementInstance.RequirementMode.IF_NOT ? !met : met;
    }

    @Override
    public void addValuePlaceholder(@NotNull String placeholder, @NotNull Supplier<String> replaceWithSupplier) {
        if (!CharacterFilter.buildResourceNameFilter().isAllowedText(placeholder)) {
            throw new RuntimeException("Illegal characters used in placeholder name! Use only [a-z], [0-9], [_], [-]!");
        } else {
            this.valuePlaceholders.put(placeholder, replaceWithSupplier);
        }
    }

    @NotNull
    @Override
    public Map<String, Supplier<String>> getValuePlaceholders() {
        return this.valuePlaceholders;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (this == o) {
            return true;
        } else if (o instanceof LoadingRequirementInstance other) {
            if (!Objects.equals(this.instanceIdentifier, other.instanceIdentifier)) {
                return false;
            } else if (this.requirement != other.requirement) {
                return false;
            } else {
                return !Objects.equals(this.value, other.value) ? false : this.mode == other.mode;
            }
        } else {
            return false;
        }
    }

    public LoadingRequirementInstance copy(boolean unique) {
        LoadingRequirementInstance i = new LoadingRequirementInstance(this.requirement, this.value, this.mode, null);
        if (!unique) {
            i.instanceIdentifier = this.instanceIdentifier;
        }
        i.valuePlaceholders.putAll(this.valuePlaceholders);
        return i;
    }

    @NotNull
    public static List<String> serializeRequirementInstance(@NotNull LoadingRequirementInstance instance) {
        List<String> l = new ArrayList();
        String key = "[loading_requirement:" + instance.requirement.getIdentifier() + "][requirement_mode:" + instance.mode.name + "]";
        if (instance.group != null) {
            key = key + "[group:" + instance.group.identifier + "]";
        }
        key = key + "[req_id:" + instance.instanceIdentifier + "]";
        l.add(key);
        if (instance.requirement.hasValue() && instance.value != null) {
            l.add(instance.value);
        } else {
            l.add("");
        }
        return l;
    }

    @Nullable
    public static LoadingRequirementInstance deserializeRequirementInstance(@NotNull String key, @Nullable String value, @NotNull LoadingRequirementContainer parent) {
        if (key.startsWith("[loading_requirement:")) {
            String reqId = key.split(":", 2)[1].split("\\]", 2)[0];
            LoadingRequirement req = LoadingRequirementRegistry.getRequirement(reqId);
            if (req != null && key.contains("[requirement_mode:")) {
                String modeName = key.split("\\[requirement_mode:", 2)[1].split("\\]", 2)[0];
                LoadingRequirementInstance.RequirementMode mode = LoadingRequirementInstance.RequirementMode.getByName(modeName);
                if (mode != null) {
                    LoadingRequirementGroup group = null;
                    if (key.contains("[group:")) {
                        String groupId = key.split("\\[group:", 2)[1].split("\\]", 2)[0];
                        if (!parent.groupExists(groupId)) {
                            parent.createAndAddGroup(groupId, LoadingRequirementGroup.GroupMode.AND);
                        }
                        group = parent.getGroup(groupId);
                    }
                    if (key.contains("[req_id:")) {
                        String id = key.split("\\[req_id:", 2)[1].split("\\]", 2)[0];
                        LoadingRequirementInstance instance = new LoadingRequirementInstance(req, value, mode, parent);
                        if (!req.hasValue()) {
                            instance.value = null;
                        }
                        instance.instanceIdentifier = id;
                        instance.group = group;
                        return instance;
                    }
                }
            }
        }
        return null;
    }

    public static enum RequirementMode {

        IF("if"), IF_NOT("if_not");

        public final String name;

        private RequirementMode(String name) {
            this.name = name;
        }

        @Nullable
        public static LoadingRequirementInstance.RequirementMode getByName(String name) {
            for (LoadingRequirementInstance.RequirementMode m : values()) {
                if (m.name.equals(name)) {
                    return m;
                }
            }
            return null;
        }
    }
}