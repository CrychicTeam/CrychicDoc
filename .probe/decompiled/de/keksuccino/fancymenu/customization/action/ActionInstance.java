package de.keksuccino.fancymenu.customization.action;

import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.fancymenu.customization.placeholder.PlaceholderParser;
import de.keksuccino.fancymenu.util.input.CharacterFilter;
import de.keksuccino.fancymenu.util.properties.PropertyContainer;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.math.MathUtils;
import java.util.ArrayList;
import java.util.Collections;
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

public class ActionInstance implements Executable, ValuePlaceholderHolder {

    private static final Logger LOGGER = LogManager.getLogger();

    @NotNull
    public volatile Action action;

    @Nullable
    public volatile String value;

    @NotNull
    protected final Map<String, Supplier<String>> valuePlaceholders = new HashMap();

    @NotNull
    public String identifier = ScreenCustomization.generateUniqueIdentifier();

    public ActionInstance(@NotNull Action action, @Nullable String value) {
        this.action = (Action) Objects.requireNonNull(action);
        this.value = value;
    }

    @NotNull
    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public void execute() {
        String v = this.value;
        try {
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
            if (this.action.hasValue()) {
                this.action.execute(v);
            } else {
                this.action.execute(null);
            }
        } catch (Exception var5) {
            LOGGER.error("################################");
            LOGGER.error("[FANCYMENU] An error occurred while trying to execute an action!");
            LOGGER.error("[FANCYMENU] Action: " + this.action.getIdentifier());
            LOGGER.error("[FANCYMENU] Value Raw: " + this.value);
            LOGGER.error("[FANCYMENU] Value: " + v);
            LOGGER.error("################################", var5);
        }
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

    @NotNull
    public ActionInstance copy(boolean unique) {
        ActionInstance i = new ActionInstance(this.action, this.value);
        if (!unique) {
            i.identifier = this.identifier;
        }
        i.valuePlaceholders.putAll(this.valuePlaceholders);
        return i;
    }

    @NotNull
    @Override
    public PropertyContainer serialize() {
        PropertyContainer c = new PropertyContainer("executable_action_instance");
        String key = "[executable_action_instance:" + this.identifier + "][action_type:" + this.action.getIdentifier() + "]";
        String val = this.value;
        c.putProperty(key, val != null ? val : "");
        return c;
    }

    @NotNull
    public static List<ActionInstance> deserializeAll(@NotNull PropertyContainer serialized) {
        List<ActionInstance> instances = new ArrayList();
        for (Entry<String, String> m : serialized.getProperties().entrySet()) {
            if (((String) m.getKey()).equals("buttonaction")) {
                instances = deserializeLegacyButtonActions(serialized);
                break;
            }
            if (((String) m.getKey()).startsWith("tickeraction_")) {
                instances = deserializeLegacyTickerActions(serialized);
                break;
            }
            if (((String) m.getKey()).startsWith("[executable_action_instance:") && ((String) m.getKey()).contains("]")) {
                String identifier = ((String) m.getKey()).split("\\[executable_action_instance:", 2)[1].split("]", 2)[0];
                if (((String) m.getKey()).contains("[action_type:")) {
                    String actionType = ((String) m.getKey()).split("\\[action_type:", 2)[1];
                    if (actionType.contains("]")) {
                        actionType = actionType.split("]", 2)[0];
                        Action action = ActionRegistry.getAction(actionType);
                        if (action != null) {
                            ActionInstance i = new ActionInstance(action, action.hasValue() ? (String) m.getValue() : null);
                            i.identifier = identifier;
                            instances.add(i);
                        }
                    }
                }
            }
        }
        return instances;
    }

    @NotNull
    protected static List<ActionInstance> deserializeLegacyButtonActions(@NotNull PropertyContainer serialized) {
        List<ActionInstance> instances = new ArrayList();
        String buttonAction = serialized.getValue("buttonaction");
        String actionValue = serialized.getValue("value");
        if (actionValue == null) {
            actionValue = "";
        }
        if (buttonAction != null) {
            if (buttonAction.contains("%btnaction_splitter_fm%")) {
                for (String s : StringUtils.splitLines(buttonAction, "%btnaction_splitter_fm%")) {
                    if (s.length() > 0) {
                        String actionIdentifier = s;
                        String value = null;
                        if (s.contains(";")) {
                            actionIdentifier = s.split(";", 2)[0];
                            value = s.split(";", 2)[1];
                        }
                        Action a = ActionRegistry.getAction(actionIdentifier);
                        if (a != null) {
                            instances.add(new ActionInstance(a, value));
                        }
                    }
                }
            } else {
                Action a = ActionRegistry.getAction(buttonAction);
                if (a != null) {
                    instances.add(new ActionInstance(a, actionValue));
                }
            }
        }
        return instances;
    }

    @NotNull
    protected static List<ActionInstance> deserializeLegacyTickerActions(@NotNull PropertyContainer serialized) {
        List<ActionInstance> instances = new ArrayList();
        Map<Integer, ActionInstance> tempActions = new HashMap();
        for (Entry<String, String> m : serialized.getProperties().entrySet()) {
            if (((String) m.getKey()).startsWith("tickeraction_")) {
                String index = ((String) m.getKey()).split("_", 3)[1];
                String tickerAction = ((String) m.getKey()).split("_", 3)[2];
                String actionValue = (String) m.getValue();
                if (MathUtils.isInteger(index)) {
                    Action a = ActionRegistry.getAction(tickerAction);
                    if (a != null) {
                        tempActions.put(Integer.parseInt(index), new ActionInstance(a, actionValue));
                    }
                }
            }
        }
        List<Integer> indexes = new ArrayList(tempActions.keySet());
        Collections.sort(indexes);
        for (int i : indexes) {
            instances.add((ActionInstance) tempActions.get(i));
        }
        return instances;
    }
}