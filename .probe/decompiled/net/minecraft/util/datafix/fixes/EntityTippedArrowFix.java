package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.schemas.Schema;
import java.util.Objects;

public class EntityTippedArrowFix extends SimplestEntityRenameFix {

    public EntityTippedArrowFix(Schema schema0, boolean boolean1) {
        super("EntityTippedArrowFix", schema0, boolean1);
    }

    @Override
    protected String rename(String string0) {
        return Objects.equals(string0, "TippedArrow") ? "Arrow" : string0;
    }
}