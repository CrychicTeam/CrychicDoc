package com.mna.rituals.effects;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.entities.EntityInit;
import com.mna.entities.rituals.FlatLands;
import com.mna.items.ItemInit;
import com.mna.tools.math.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class RitualEffectFlatLands extends RitualEffect {

    public static final int MAX_DISTANCE = 80;

    public RitualEffectFlatLands(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        ItemStack location_rune_a = ItemStack.EMPTY;
        ItemStack location_rune_b = ItemStack.EMPTY;
        for (ItemStack stack : context.getCollectedReagents(i -> i.getItem() == ItemInit.RUNE_MARKING.get())) {
            if (!location_rune_a.isEmpty()) {
                location_rune_b = stack;
                break;
            }
            location_rune_a = stack;
        }
        if (!location_rune_a.isEmpty() && !location_rune_b.isEmpty()) {
            BlockPos location_a = ItemInit.RUNE_MARKING.get().getLocation(location_rune_a);
            BlockPos location_b = ItemInit.RUNE_MARKING.get().getLocation(location_rune_b);
            if (location_a != null && location_b != null) {
                if (!location_a.m_123314_(context.getCenter(), 80.0) && !location_b.m_123314_(context.getCenter(), 80.0)) {
                    context.getCaster().m_213846_(Component.translatable("mna:rituals/flat_lands.tooFar"));
                    this.dropCollectedReagents(context);
                    return false;
                } else {
                    AABB bb = MathUtils.createInclusiveBB(location_a, location_b);
                    if (!(bb.getXsize() > 80.0) && !(bb.getYsize() > 80.0) && !(bb.getZsize() > 80.0)) {
                        FlatLands flat_lands = new FlatLands(EntityInit.FLAT_LANDS.get(), context.getLevel());
                        flat_lands.m_146884_(Vec3.atCenterOf(context.getCenter().above(2)));
                        flat_lands.setCaster(context.getCaster());
                        flat_lands.setBounds(bb);
                        context.getLevel().m_7967_(flat_lands);
                        return true;
                    } else {
                        context.getCaster().m_213846_(Component.translatable("mna:rituals/flat_lands.tooBig"));
                        this.dropCollectedReagents(context);
                        return false;
                    }
                }
            } else {
                this.dropCollectedReagents(context);
                return false;
            }
        } else {
            this.dropCollectedReagents(context);
            return false;
        }
    }

    private void dropCollectedReagents(IRitualContext context) {
        Vec3 drop = Vec3.atCenterOf(context.getCenter().above());
        context.getCollectedReagents().forEach(is -> {
            ItemEntity ie = new ItemEntity(context.getLevel(), drop.x, drop.y, drop.z, is);
            context.getLevel().m_7967_(ie);
        });
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 0;
    }
}