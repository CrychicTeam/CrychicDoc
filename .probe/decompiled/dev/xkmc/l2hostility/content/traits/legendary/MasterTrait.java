package dev.xkmc.l2hostility.content.traits.legendary;

import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.init.L2Hostility;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

public class MasterTrait extends LegendaryTrait {

    @Nullable
    public static EntityConfig.MasterConfig getConfig(EntityType<?> type) {
        EntityConfig.Config config = L2Hostility.ENTITY.getMerged().get(type);
        return config == null ? null : config.asMaster;
    }

    public MasterTrait(ChatFormatting format) {
        super(format);
    }

    @Override
    public boolean allow(LivingEntity le, int difficulty, int maxModLv) {
        return getConfig(le.m_6095_()) != null && super.allow(le, difficulty, maxModLv);
    }
}