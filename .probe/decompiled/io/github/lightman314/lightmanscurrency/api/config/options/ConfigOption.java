package io.github.lightman314.lightmanscurrency.api.config.options;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.config.ConfigFile;
import io.github.lightman314.lightmanscurrency.api.config.options.parsing.ConfigParser;
import io.github.lightman314.lightmanscurrency.api.config.options.parsing.ConfigParsingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraftforge.common.util.NonNullSupplier;

public abstract class ConfigOption<T> implements Supplier<T>, NonNullSupplier<T> {

    private List<String> comments = new ArrayList();

    private ConfigFile parent = null;

    private String name = "null";

    private String optionID = "null.null";

    private final NonNullSupplier<T> defaultValue;

    private T currentValue = (T) null;

    private T syncedValue = (T) null;

    public final void setComments(@Nonnull List<String> comments) {
        if (this.comments instanceof ArrayList) {
            this.comments = ImmutableList.copyOf(comments);
        } else {
            LightmansCurrency.LogWarning("Attempted to define an options comments twice!");
        }
    }

    @Nonnull
    public String getName() {
        return this.name;
    }

    public final void init(@Nonnull ConfigFile parent, @Nonnull String name, @Nonnull String optionID) {
        if (this.parent == null) {
            this.parent = parent;
            this.name = name;
            this.optionID = optionID;
        } else {
            LightmansCurrency.LogWarning("Attempted to define an options parent twice!");
        }
    }

    @Nonnull
    public final List<String> getComments() {
        String c = this.bonusComment();
        if (c == null) {
            return this.comments;
        } else {
            List<String> cl = new ArrayList(this.comments);
            cl.add(c);
            return cl;
        }
    }

    protected ConfigOption(@Nonnull NonNullSupplier<T> defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Nullable
    protected String bonusComment() {
        return null;
    }

    @Nonnull
    protected abstract ConfigParser<T> getParser();

    @Nullable
    public final Pair<Boolean, ConfigParsingException> load(@Nonnull String line, @Nonnull ConfigOption.LoadSource source) {
        line = cleanWhitespace(line);
        try {
            T val = this.getParser().tryParse(line);
            if (source == ConfigOption.LoadSource.SYNC) {
                this.syncedValue = val;
            } else {
                this.currentValue = val;
            }
            if (source == ConfigOption.LoadSource.COMMAND && this.parent != null) {
                this.parent.onOptionChanged(this);
            }
            return Pair.of(true, null);
        } catch (ConfigParsingException var4) {
            LightmansCurrency.LogError("Error parsing " + this.optionID + "!", var4);
            this.currentValue = this.defaultValue.get();
            return Pair.of(false, var4);
        }
    }

    public final void clear() {
        this.currentValue = null;
    }

    public final boolean isLoaded() {
        return this.currentValue != null;
    }

    public final void setToDefault() {
        this.set(this.getDefaultValue());
    }

    public final void loadDefault() {
        this.currentValue = this.getDefaultValue();
    }

    public final void clearSyncedData() {
        this.syncedValue = null;
    }

    @Nonnull
    public final String write() {
        return this.getParser().write(this.getCurrentValue());
    }

    public final void write(@Nonnull String name, @Nonnull Consumer<String> writer) {
        ConfigFile.writeComments(this.getComments(), writer);
        writer.accept(name + "=" + this.write());
        writer.accept("");
    }

    @Nonnull
    public static String cleanWhitespace(@Nonnull String line) {
        StringBuilder result = new StringBuilder();
        boolean start = true;
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (Character.isWhitespace(c)) {
                if (!start) {
                    temp.append(c);
                }
            } else {
                if (start) {
                    start = false;
                }
                if (!temp.isEmpty()) {
                    result.append(temp);
                    temp = new StringBuilder();
                }
                result.append(c);
            }
        }
        return result.toString();
    }

    @Nonnull
    @Override
    public final T get() {
        return this.syncedValue != null ? this.syncedValue : this.getCurrentValue();
    }

    public void set(@Nonnull T newValue) {
        if (this.currentValue == null || !this.currentValue.equals(newValue)) {
            this.currentValue = (T) Objects.requireNonNull(newValue);
            if (this.parent != null) {
                this.parent.onOptionChanged(this);
            }
        }
    }

    @Nonnull
    protected final T getCurrentValue() {
        if (this.currentValue == null) {
            LightmansCurrency.LogDebug("Attempted to access the value of '" + this.optionID + "' before the config was loaded!");
            return this.getDefaultValue();
        } else {
            return this.currentValue;
        }
    }

    @Nonnull
    protected T getDefaultValue() {
        return this.defaultValue.get();
    }

    public static enum LoadSource {

        FILE, COMMAND, SYNC
    }
}