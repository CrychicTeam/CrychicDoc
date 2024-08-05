package com.mna.rituals.effects;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.factions.Factions;
import com.mna.items.ItemInit;
import com.mna.items.sorcery.ItemEntityCrystal;
import com.mna.tools.EntityUtil;
import com.mna.tools.SummonUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public class RitualEffectConsumeCrystalSoul extends RitualEffect {

    public RitualEffectConsumeCrystalSoul(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    public Component canRitualStart(IRitualContext context) {
        if (context.getCaster() == null) {
            return Component.literal("No player reference found for ritual, aborting.");
        } else {
            IPlayerProgression p = (IPlayerProgression) context.getCaster().getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
            return p != null && p.getAlliedFaction() == Factions.UNDEAD ? null : Component.translatable("mna:rituals/consume_crystal_soul.not_undead");
        }
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        ItemStack crystalStack = ItemStack.EMPTY;
        for (ItemStack stack : context.getCollectedReagents()) {
            if (stack.getItem() == ItemInit.ENTITY_ENTRAPMENT_CRYSTAL.get()) {
                crystalStack = stack;
                break;
            }
        }
        if (!crystalStack.isEmpty() && ItemEntityCrystal.getEntityType(crystalStack) != null) {
            Entity e = ItemEntityCrystal.restoreEntity(context.getLevel(), crystalStack);
            if (SummonUtils.isSummon(e)) {
                context.getCaster().m_213846_(Component.translatable("mna:rituals/consume_crystal_soul.no_summons"));
                return false;
            } else {
                float souls = EntityUtil.getSoulsRestored(context.getCaster(), e) * 20.0F;
                context.getCaster().getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.getCastingResource().restore(souls));
                Vec3 center = Vec3.atCenterOf(context.getCenter());
                ItemEntity itemEntity = new ItemEntity(context.getLevel(), center.x, center.y, center.z, new ItemStack(Items.DIAMOND));
                context.getLevel().m_7967_(itemEntity);
                return true;
            }
        } else {
            context.getCaster().m_213846_(Component.translatable("mna:rituals/consume_crystal_soul.crystal_empty"));
            return false;
        }
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 20;
    }
}