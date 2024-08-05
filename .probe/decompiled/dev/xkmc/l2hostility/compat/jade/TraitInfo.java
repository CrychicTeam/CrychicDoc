package dev.xkmc.l2hostility.compat.jade;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class TraitInfo implements IEntityComponentProvider {

    public static final ResourceLocation ID = new ResourceLocation("l2hostility", "mob");

    @Override
    public void appendTooltip(ITooltip list, EntityAccessor entity, IPluginConfig config) {
        if (entity.getEntity() instanceof LivingEntity le && MobTraitCap.HOLDER.isProper(le)) {
            list.addAll(((MobTraitCap) MobTraitCap.HOLDER.get(le)).getTitle(true, true));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }
}