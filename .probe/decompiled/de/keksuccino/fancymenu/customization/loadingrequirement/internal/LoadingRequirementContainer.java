package de.keksuccino.fancymenu.customization.loadingrequirement.internal;

import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.fancymenu.customization.action.ValuePlaceholderHolder;
import de.keksuccino.fancymenu.util.ListUtils;
import de.keksuccino.fancymenu.util.input.CharacterFilter;
import de.keksuccino.fancymenu.util.properties.PropertyContainer;
import java.util.ArrayList;
import java.util.Arrays;
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

public class LoadingRequirementContainer implements ValuePlaceholderHolder {

    private static final Logger LOGGER = LogManager.getLogger();

    protected final List<LoadingRequirementGroup> groups = new ArrayList();

    protected final List<LoadingRequirementInstance> instances = new ArrayList();

    protected final Map<String, Supplier<String>> valuePlaceholders = new HashMap();

    @NotNull
    public String identifier = ScreenCustomization.generateUniqueIdentifier();

    protected boolean forceRequirementsMet = false;

    protected boolean forceRequirementsNotMet = false;

    public boolean requirementsMet() {
        if (this.forceRequirementsMet) {
            return true;
        } else if (this.forceRequirementsNotMet) {
            return false;
        } else {
            try {
                for (LoadingRequirementGroup g : this.groups) {
                    if (!g.requirementsMet()) {
                        return false;
                    }
                }
                for (LoadingRequirementInstance i : this.instances) {
                    if (!i.requirementMet()) {
                        return false;
                    }
                }
                return true;
            } catch (Exception var3) {
                LOGGER.error("[FANCYMENU] Error while checking LoadingRequirements of LoadingRequirementContainer!");
                var3.printStackTrace();
                return false;
            }
        }
    }

    @Nullable
    public LoadingRequirementGroup createAndAddGroup(@NotNull String identifier, @NotNull LoadingRequirementGroup.GroupMode mode) {
        if (!this.groupExists(identifier)) {
            LoadingRequirementGroup g = new LoadingRequirementGroup(identifier, mode, this);
            this.groups.add(g);
            this.valuePlaceholders.forEach(g::addValuePlaceholder);
            return g;
        } else {
            return null;
        }
    }

    public boolean addGroup(@NotNull LoadingRequirementGroup group) {
        if (!this.groupExists(group.identifier)) {
            this.groups.add(group);
            this.valuePlaceholders.forEach(group::addValuePlaceholder);
            return true;
        } else {
            return false;
        }
    }

    public List<LoadingRequirementGroup> getGroups() {
        return new ArrayList(this.groups);
    }

    @Nullable
    public LoadingRequirementGroup getGroup(@NotNull String identifier) {
        for (LoadingRequirementGroup g : this.groups) {
            if (g.identifier.equals(identifier)) {
                return g;
            }
        }
        return null;
    }

    public boolean groupExists(@NotNull String identifier) {
        return this.getGroup(identifier) != null;
    }

    public boolean removeGroup(@NotNull LoadingRequirementGroup group) {
        return this.groups.remove(Objects.requireNonNull(group));
    }

    public boolean removeGroupByIdentifier(@NotNull String identifier) {
        LoadingRequirementGroup g = this.getGroup(identifier);
        return g != null ? this.groups.remove(g) : false;
    }

    public boolean addInstance(@NotNull LoadingRequirementInstance instance) {
        if (!this.instances.contains(instance)) {
            this.instances.add(instance);
            this.valuePlaceholders.forEach(instance::addValuePlaceholder);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeInstance(@NotNull LoadingRequirementInstance instance) {
        return this.instances.remove(instance);
    }

    public List<LoadingRequirementInstance> getInstances() {
        return new ArrayList(this.instances);
    }

    public LoadingRequirementContainer forceRequirementsMet(boolean forceMet) {
        this.forceRequirementsMet = forceMet;
        this.forceRequirementsNotMet = false;
        return this;
    }

    public LoadingRequirementContainer forceRequirementsNotMet(boolean forceNotMet) {
        this.forceRequirementsNotMet = forceNotMet;
        this.forceRequirementsMet = false;
        return this;
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
            for (LoadingRequirementGroup g : this.groups) {
                g.addValuePlaceholder(placeholder, replaceWithSupplier);
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
        } else if (o instanceof LoadingRequirementContainer other) {
            if (!Objects.equals(this.identifier, other.identifier)) {
                return false;
            } else {
                return !ListUtils.contentEqualIgnoreOrder(this.groups, other.groups) ? false : ListUtils.contentEqualIgnoreOrder(this.instances, other.instances);
            }
        } else {
            return false;
        }
    }

    public LoadingRequirementContainer copy(boolean unique) {
        LoadingRequirementContainer c = new LoadingRequirementContainer();
        if (!unique) {
            c.identifier = this.identifier;
        }
        this.groups.forEach(group -> {
            LoadingRequirementGroup g = group.copy(unique);
            g.parent = c;
            for (LoadingRequirementInstance i : g.instances) {
                i.parent = c;
            }
            c.groups.add(g);
        });
        this.instances.forEach(instance -> {
            LoadingRequirementInstance i = instance.copy(unique);
            i.parent = c;
            c.instances.add(i);
        });
        c.valuePlaceholders.putAll(this.valuePlaceholders);
        return c;
    }

    public void serializeToExistingPropertyContainer(@NotNull PropertyContainer target) {
        PropertyContainer sec = this.serialize();
        for (Entry<String, String> m : sec.getProperties().entrySet()) {
            target.putProperty((String) m.getKey(), (String) m.getValue());
        }
    }

    @NotNull
    public PropertyContainer serialize() {
        PropertyContainer container = new PropertyContainer("loading_requirement_container");
        String containerMetaKey = "[loading_requirement_container_meta:" + this.identifier + "]";
        String containerMetaValue = "[groups:";
        for (LoadingRequirementGroup g : this.groups) {
            containerMetaValue = containerMetaValue + g.identifier + ";";
        }
        containerMetaValue = containerMetaValue + "][instances:";
        for (LoadingRequirementInstance i : this.instances) {
            containerMetaValue = containerMetaValue + i.instanceIdentifier + ";";
        }
        containerMetaValue = containerMetaValue + "]";
        container.putProperty(containerMetaKey, containerMetaValue);
        for (LoadingRequirementGroup g : this.groups) {
            PropertyContainer sg = LoadingRequirementGroup.serializeRequirementGroup(g);
            for (Entry<String, String> m : sg.getProperties().entrySet()) {
                container.putProperty((String) m.getKey(), (String) m.getValue());
            }
        }
        for (LoadingRequirementInstance i : this.instances) {
            List<String> l = LoadingRequirementInstance.serializeRequirementInstance(i);
            container.putProperty((String) l.get(0), (String) l.get(1));
        }
        return container;
    }

    @Nullable
    public static LoadingRequirementContainer deserializeWithIdentifier(@NotNull String identifier, @NotNull PropertyContainer serialized) {
        for (LoadingRequirementContainer c : deserializeAll(serialized)) {
            if (c.identifier.equals(Objects.requireNonNull(identifier))) {
                return c;
            }
        }
        return null;
    }

    @NotNull
    public static List<LoadingRequirementContainer> deserializeAll(@NotNull PropertyContainer serialized) {
        List<LoadingRequirementContainer> containers = new ArrayList();
        List<List<String>> containerMetas = new ArrayList();
        for (Entry<String, String> m : ((PropertyContainer) Objects.requireNonNull(serialized)).getProperties().entrySet()) {
            if (((String) m.getKey()).startsWith("[loading_requirement_container_meta:")) {
                containerMetas.add(ListUtils.of((String) m.getKey(), (String) m.getValue()));
            }
        }
        LoadingRequirementContainer combined = deserializeToSingleContainer(serialized);
        if (containerMetas.isEmpty()) {
            containers.add(combined);
        } else {
            for (List<String> meta : containerMetas) {
                String key = (String) meta.get(0);
                String value = (String) meta.get(1);
                if (key.contains("[loading_requirement_container_meta:")) {
                    String identifier = key.split("\\[loading_requirement_container_meta:", 2)[1];
                    if (identifier.contains("]")) {
                        identifier = identifier.split("]", 2)[0];
                        List<String> groupIdentifiers = new ArrayList();
                        List<String> instanceIdentifiers = new ArrayList();
                        if (value.contains("[groups:")) {
                            String groupsRaw = value.split("\\[groups:", 2)[1];
                            if (groupsRaw.contains("]")) {
                                groupsRaw = groupsRaw.split("]", 2)[0];
                                if (groupsRaw.contains(";")) {
                                    groupIdentifiers = Arrays.asList(groupsRaw.split(";"));
                                }
                            }
                        }
                        if (value.contains("[instances:")) {
                            String instancesRaw = value.split("\\[instances:", 2)[1];
                            if (instancesRaw.contains("]")) {
                                instancesRaw = instancesRaw.split("]", 2)[0];
                                if (instancesRaw.contains(";")) {
                                    instanceIdentifiers = Arrays.asList(instancesRaw.split(";"));
                                }
                            }
                        }
                        if (!identifier.replace(" ", "").isEmpty()) {
                            LoadingRequirementContainer container = new LoadingRequirementContainer();
                            container.identifier = identifier;
                            for (String groupId : groupIdentifiers) {
                                for (LoadingRequirementGroup g : combined.groups) {
                                    if (g.identifier.equals(groupId)) {
                                        container.groups.add(g);
                                        break;
                                    }
                                }
                            }
                            for (String instanceId : instanceIdentifiers) {
                                for (LoadingRequirementInstance i : combined.instances) {
                                    if (i.instanceIdentifier.equals(instanceId)) {
                                        container.instances.add(i);
                                        break;
                                    }
                                }
                            }
                            containers.add(container);
                        }
                    }
                }
            }
        }
        return containers;
    }

    @NotNull
    public static LoadingRequirementContainer deserializeToSingleContainer(@NotNull PropertyContainer serialized) {
        LoadingRequirementContainer c = new LoadingRequirementContainer();
        for (Entry<String, String> m : ((PropertyContainer) Objects.requireNonNull(serialized)).getProperties().entrySet()) {
            if (((String) m.getKey()).startsWith("[loading_requirement_group:")) {
                LoadingRequirementGroup g = LoadingRequirementGroup.deserializeRequirementGroup((String) m.getKey(), (String) m.getValue(), c);
                if (g != null) {
                    c.addGroup(g);
                }
            }
        }
        for (Entry<String, String> mx : serialized.getProperties().entrySet()) {
            if (((String) mx.getKey()).startsWith("[loading_requirement:")) {
                LoadingRequirementInstance i = LoadingRequirementInstance.deserializeRequirementInstance((String) mx.getKey(), (String) mx.getValue(), c);
                if (i != null) {
                    if (i.group != null) {
                        i.group.addInstance(i);
                    } else {
                        c.addInstance(i);
                    }
                }
            }
        }
        return c;
    }

    public static LoadingRequirementContainer stackContainers(@NotNull LoadingRequirementContainer... containers) {
        LoadingRequirementContainer stack = new LoadingRequirementContainer();
        for (LoadingRequirementContainer c : containers) {
            LoadingRequirementContainer copy = c.copy(true);
            stack.instances.addAll(copy.instances);
            stack.groups.addAll(copy.groups);
        }
        for (LoadingRequirementInstance i : stack.instances) {
            i.parent = stack;
        }
        for (LoadingRequirementGroup g : stack.groups) {
            g.parent = stack;
            for (LoadingRequirementInstance i : g.instances) {
                i.parent = stack;
            }
        }
        return stack;
    }
}