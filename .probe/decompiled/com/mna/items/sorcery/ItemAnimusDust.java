package com.mna.items.sorcery;

import com.mna.api.items.TieredItem;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.collections.Components;
import com.mna.api.spells.collections.Shapes;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import java.util.HashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ItemAnimusDust extends TieredItem {

    public ItemAnimusDust() {
        super(new Item.Properties());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        context.getPlayer().m_6674_(context.getHand());
        if (world.isClientSide) {
            if (world.getBlockEntity(context.getClickedPos()) == null) {
                spawnParticles(world, context.getClickedPos());
            }
            return InteractionResult.PASS;
        } else {
            BlockPos pos = context.getClickedPos();
            Direction face = context.getClickedFace();
            SpellRecipe animus = new SpellRecipe();
            animus.setShape(Shapes.TOUCH);
            animus.addComponent(Components.ANIMUS);
            HashMap<SpellEffect, ComponentApplicationResult> result = SpellCaster.ApplyComponents(animus, new SpellSource(context.getPlayer(), context.getHand()), new SpellTarget(pos, face), new SpellContext(world, animus));
            if (((ComponentApplicationResult) result.get(Components.ANIMUS)).is_success) {
                if (!context.getPlayer().isCreative()) {
                    context.getItemInHand().shrink(1);
                }
                return InteractionResult.CONSUME;
            } else {
                return super.m_6225_(context);
            }
        }
    }

    public static void spawnParticles(Level world, BlockPos pos) {
        Vec3 center = Vec3.atCenterOf(pos);
        int[] blue = new int[] { 79, 106, 158 };
        int[] purple = new int[] { 144, 102, 147 };
        int[] red = new int[] { 241, 54, 64 };
        for (int i = 0; i < 15; i++) {
            world.addParticle(new MAParticleType(ParticleInit.ARCANE.get()).setColor(blue[0], blue[1], blue[2]).setScale(0.075F), center.x() - 0.5 + Math.random(), center.y() - 0.5 + Math.random(), center.z() - 0.5 + Math.random(), (-0.5 + Math.random()) * 0.1F, 0.1, (-0.5 + Math.random()) * 0.1F);
        }
        for (int i = 0; i < 5; i++) {
            world.addParticle(new MAParticleType(ParticleInit.ARCANE.get()).setColor(purple[0], purple[1], purple[2]), center.x() - 1.0 + Math.random() * 2.0, center.y() - 1.0 + Math.random() * 2.0, center.z() - 1.0 + Math.random() * 2.0, (-0.5 + Math.random()) * 0.1F, 0.1, (-0.5 + Math.random()) * 0.1F);
        }
        for (int i = 0; i < 5; i++) {
            world.addParticle(new MAParticleType(ParticleInit.ARCANE.get()).setColor(red[0], red[1], red[2]), center.x() - 1.0 + Math.random() * 2.0, center.y() - 1.0 + Math.random() * 2.0, center.z() - 1.0 + Math.random() * 2.0, (-0.5 + Math.random()) * 0.1F, 0.1, (-0.5 + Math.random()) * 0.1F);
        }
    }
}