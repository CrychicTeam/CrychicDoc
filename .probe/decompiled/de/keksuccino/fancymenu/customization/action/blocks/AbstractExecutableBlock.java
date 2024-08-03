package de.keksuccino.fancymenu.customization.action.blocks;

import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.fancymenu.customization.action.Executable;
import de.keksuccino.fancymenu.customization.action.ValuePlaceholderHolder;
import de.keksuccino.fancymenu.util.input.CharacterFilter;
import de.keksuccino.fancymenu.util.properties.PropertyContainer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractExecutableBlock implements Executable, ValuePlaceholderHolder {

    protected final List<Executable> executables = new ArrayList();

    @NotNull
    protected final Map<String, Supplier<String>> valuePlaceholders = new HashMap();

    @NotNull
    public String identifier = ScreenCustomization.generateUniqueIdentifier();

    @NotNull
    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    public abstract String getBlockType();

    @Override
    public void execute() {
        for (Executable e : this.executables) {
            e.execute();
        }
    }

    @Override
    public void addValuePlaceholder(@NotNull String placeholder, @NotNull Supplier<String> replaceWithSupplier) {
        if (!CharacterFilter.buildResourceNameFilter().isAllowedText(placeholder)) {
            throw new RuntimeException("Illegal characters used in placeholder name! Use only [a-z], [0-9], [_], [-]!");
        } else {
            this.valuePlaceholders.put(placeholder, replaceWithSupplier);
            for (Executable e : this.executables) {
                if (e instanceof ValuePlaceholderHolder h) {
                    h.addValuePlaceholder(placeholder, replaceWithSupplier);
                }
            }
            if (this.getAppendedBlock() != null) {
                this.getAppendedBlock().addValuePlaceholder(placeholder, replaceWithSupplier);
            }
        }
    }

    @NotNull
    @Override
    public Map<String, Supplier<String>> getValuePlaceholders() {
        return this.valuePlaceholders;
    }

    public List<Executable> getExecutables() {
        return this.executables;
    }

    public AbstractExecutableBlock addExecutable(Executable executable) {
        this.executables.add(executable);
        if (executable instanceof ValuePlaceholderHolder h) {
            this.valuePlaceholders.forEach(h::addValuePlaceholder);
        }
        return this;
    }

    public AbstractExecutableBlock removeExecutable(Executable executable) {
        this.executables.remove(executable);
        return this;
    }

    public AbstractExecutableBlock clearExecutables() {
        this.executables.clear();
        return this;
    }

    @Nullable
    public AbstractExecutableBlock getAppendedBlock() {
        return null;
    }

    public void setAppendedBlock(@Nullable AbstractExecutableBlock appended) {
        if (appended != null) {
            this.valuePlaceholders.forEach(appended::addValuePlaceholder);
        }
    }

    @NotNull
    @Override
    public PropertyContainer serialize() {
        PropertyContainer container = new PropertyContainer("executable_block");
        String key = "[executable_block:" + this.identifier + "][type:" + this.getBlockType() + "]";
        String value = "[executables:";
        for (Executable e : this.executables) {
            value = value + e.getIdentifier() + ";";
            e.serializeToExistingPropertyContainer(container);
        }
        value = value + "]";
        if (this.getAppendedBlock() != null) {
            value = value + "[appended:" + this.getAppendedBlock().getIdentifier() + "]";
        }
        container.putProperty(key, value);
        if (this.getAppendedBlock() != null) {
            this.getAppendedBlock().serializeToExistingPropertyContainer(container);
        }
        return container;
    }
}