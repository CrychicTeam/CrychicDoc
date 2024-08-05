package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.schemas.Schema;
import java.util.Map;
import java.util.Objects;

public class EntityRavagerRenameFix extends SimplestEntityRenameFix {

    public static final Map<String, String> RENAMED_IDS = ImmutableMap.builder().put("minecraft:illager_beast_spawn_egg", "minecraft:ravager_spawn_egg").build();

    public EntityRavagerRenameFix(Schema schema0, boolean boolean1) {
        super("EntityRavagerRenameFix", schema0, boolean1);
    }

    @Override
    protected String rename(String string0) {
        return Objects.equals("minecraft:illager_beast", string0) ? "minecraft:ravager" : string0;
    }
}