package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.schemas.Schema;
import java.util.Map;
import java.util.Objects;

public class EntityZombifiedPiglinRenameFix extends SimplestEntityRenameFix {

    public static final Map<String, String> RENAMED_IDS = ImmutableMap.builder().put("minecraft:zombie_pigman_spawn_egg", "minecraft:zombified_piglin_spawn_egg").build();

    public EntityZombifiedPiglinRenameFix(Schema schema0) {
        super("EntityZombifiedPiglinRenameFix", schema0, true);
    }

    @Override
    protected String rename(String string0) {
        return Objects.equals("minecraft:zombie_pigman", string0) ? "minecraft:zombified_piglin" : string0;
    }
}