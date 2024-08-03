package de.keksuccino.fancymenu.customization.loadingrequirement.internal;

import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.fancymenu.customization.action.ValuePlaceholderHolder;
import de.keksuccino.fancymenu.util.ListUtils;
import de.keksuccino.fancymenu.util.input.CharacterFilter;
import de.keksuccino.fancymenu.util.properties.PropertyContainer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LoadingRequirementGroup implements ValuePlaceholderHolder {

    public LoadingRequirementContainer parent;

    public String identifier;

    public LoadingRequirementGroup.GroupMode mode;

    protected final Map<String, Supplier<String>> valuePlaceholders = new HashMap();

    protected final List<LoadingRequirementInstance> instances = new ArrayList();

    public LoadingRequirementGroup(@NotNull String identifier, @NotNull LoadingRequirementGroup.GroupMode mode, @NotNull LoadingRequirementContainer parent) {
        this.parent = parent;
        this.identifier = identifier;
        this.mode = mode;
    }

    public boolean requirementsMet() {
        for (LoadingRequirementInstance i : this.instances) {
            boolean met = i.requirementMet();
            if (met && this.mode == LoadingRequirementGroup.GroupMode.OR) {
                return true;
            }
            if (!met && this.mode == LoadingRequirementGroup.GroupMode.AND) {
                return false;
            }
        }
        return this.mode != LoadingRequirementGroup.GroupMode.OR;
    }

    public void addInstance(LoadingRequirementInstance instance) {
        if (!this.instances.contains(instance)) {
            this.instances.add(instance);
            this.valuePlaceholders.forEach(instance::addValuePlaceholder);
        }
        instance.group = this;
    }

    @Nullable
    public LoadingRequirementInstance removeInstance(LoadingRequirementInstance instance) {
        instance.group = null;
        return this.instances.remove(instance) ? instance : null;
    }

    public List<LoadingRequirementInstance> getInstances() {
        return new ArrayList(this.instances);
    }

    @Override
    public void addValuePlaceholder(@NotNull String placeholder, @NotNull Supplier<String> replaceWithSupplier) {
        if (!CharacterFilter.buildResourceNameFilter().isAllowedText(placeholder)) {
            throw new RuntimeException("Illegal characters used in placeholder name! Use only [a-z], [0-9], [_], [-]!");
        } else {
            this.valuePlaceholders.put(placeholder, replaceWithSupplier);
            for (LoadingRequirementInstance i : this.instances) {
                i.addValuePlaceholder(placeholder, replaceWithSupplier);
            }
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
        } else if (o instanceof LoadingRequirementGroup other) {
            if (!Objects.equals(this.identifier, other.identifier)) {
                return false;
            } else {
                return this.mode != other.mode ? false : ListUtils.contentEqualIgnoreOrder(this.instances, other.instances);
            }
        } else {
            return false;
        }
    }

    public LoadingRequirementGroup copy(boolean unique) {
        LoadingRequirementGroup g = new LoadingRequirementGroup(unique ? ScreenCustomization.generateUniqueIdentifier() : this.identifier, this.mode, null);
        this.instances.forEach(instance -> {
            LoadingRequirementInstance i = instance.copy(unique);
            i.group = g;
            g.instances.add(i);
        });
        g.valuePlaceholders.putAll(this.valuePlaceholders);
        return g;
    }

    @NotNull
    public static PropertyContainer serializeRequirementGroup(@NotNull LoadingRequirementGroup group) {
        PropertyContainer sec = new PropertyContainer("requirement_group");
        String key = "[loading_requirement_group:" + group.identifier + "]";
        String value = "[group_mode:" + group.mode.name + "]";
        sec.putProperty(key, value);
        for (LoadingRequirementInstance i : group.instances) {
            i.group = group;
            List<String> l = LoadingRequirementInstance.serializeRequirementInstance(i);
            sec.putProperty((String) l.get(0), (String) l.get(1));
        }
        return sec;
    }

    @Nullable
    public static LoadingRequirementGroup deserializeRequirementGroup(@NotNull String key, @NotNull String value, @NotNull LoadingRequirementContainer parent) {
        if (key.startsWith("[loading_requirement_group:")) {
            String groupId = key.split("\\[loading_requirement_group:", 2)[1].split("\\]", 2)[0];
            if (value.startsWith("[group_mode:")) {
                String modeString = value.split("\\[group_mode:", 2)[1].split("\\]", 2)[0];
                LoadingRequirementGroup.GroupMode mode = LoadingRequirementGroup.GroupMode.getByName(modeString);
                if (mode != null) {
                    return new LoadingRequirementGroup(groupId, mode, parent);
                }
            }
        }
        return null;
    }

    public static enum GroupMode {

        AND("and"), OR("or");

        public final String name;

        private GroupMode(String name) {
            this.name = name;
        }

        @Nullable
        public static LoadingRequirementGroup.GroupMode getByName(String name) {
            for (LoadingRequirementGroup.GroupMode m : values()) {
                if (m.name.equals(name)) {
                    return m;
                }
            }
            return null;
        }
    }
}