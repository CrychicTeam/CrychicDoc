package moe.wolfgirl.probejs.lang.decompiler.remapper;

import dev.latvian.mods.rhino.mod.util.RemappingHelper;
import java.lang.reflect.Array;
import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;

public class RemappedType {

    public final RemappedClass parent;

    public final int array;

    public Optional<Class<?>> realClass;

    public String descriptorString;

    public RemappedType(RemappedClass parent, int array) {
        this.parent = parent;
        this.array = array;
        this.realClass = null;
    }

    public String toString() {
        return this.array == 0 ? this.parent.toString() : this.parent.toString() + "[]".repeat(this.array);
    }

    public boolean isRemapped() {
        return this.array == 0 && this.parent.remapped;
    }

    @Nullable
    private Class<?> getRealClass(boolean debug) {
        if (this.realClass == null) {
            Optional<Class<?>> r = RemappingHelper.getClass(this.parent.realName);
            if (r.isPresent()) {
                if (this.array > 0) {
                    this.realClass = Optional.of(Array.newInstance((Class) r.get(), this.array).getClass());
                } else {
                    this.realClass = r;
                }
            } else {
                this.realClass = Optional.empty();
                if (debug) {
                    RemappingHelper.LOGGER.error("Class " + this.parent.realName + " / " + this.parent.remappedName + " not found!");
                }
            }
        }
        return (Class<?>) this.realClass.orElse(null);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else {
            if (obj instanceof RemappedType type && type.parent == this.parent && type.array == this.array) {
                return true;
            }
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.parent, this.array });
    }

    public String descriptorString() {
        if (this.descriptorString == null) {
            if (this.array > 0) {
                this.descriptorString = "[".repeat(this.array) + this.parent.descriptorString();
            } else {
                this.descriptorString = this.parent.descriptorString();
            }
        }
        return this.descriptorString;
    }
}