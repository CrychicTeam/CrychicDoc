package snownee.jade.api;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public interface SimpleStringRepresentable extends StringRepresentable {

    @NotNull
    @Override
    default String getSerializedName() {
        return this.toString();
    }
}