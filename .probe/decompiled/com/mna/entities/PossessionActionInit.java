package com.mna.entities;

import com.mna.api.entities.possession.PossessionActions;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.collections.Components;
import com.mna.api.spells.collections.Shapes;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.items.ItemInit;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class PossessionActionInit {

    @SubscribeEvent
    public static void loadCompleteEventHandler(FMLLoadCompleteEvent event) {
        PossessionActions.RegisterLeftClickAction(EntityType.CREEPER, c -> c.ignite());
        PossessionActions.RegisterLeftClickAction(EntityType.ENDERMAN, e -> {
            SpellRecipe recipe = new SpellRecipe();
            recipe.setShape(Shapes.SELF);
            recipe.addComponent(Components.BLINK);
            recipe.getComponent(0).setValue(Attribute.RANGE, 16.0F);
            ItemStack spell = new ItemStack(ItemInit.SPELL.get());
            recipe.writeToNBT(spell.getOrCreateTag());
            SpellCaster.Affect(spell, recipe, e.m_9236_(), new SpellSource(e, InteractionHand.MAIN_HAND));
        });
        PossessionActions.RegisterLeftClickAction(EntityType.GHAST, e -> {
            Vec3 vector3d = e.m_20252_(1.0F);
            e.m_9236_().m_5898_((Player) null, 1016, e.m_20183_(), 0);
            LargeFireball fireballentity = new LargeFireball(e.m_9236_(), e, vector3d.x, vector3d.y, vector3d.z, e.getExplosionPower());
            fireballentity.m_6034_(e.m_20185_() + vector3d.x * 4.0, e.m_20227_(0.5) + 0.5, fireballentity.m_20189_() + vector3d.z * 4.0);
            e.m_9236_().m_7967_(fireballentity);
        });
        PossessionActions.RegisterLeftClickAction(EntityType.WITCH, e -> {
            Vec3 vector3d = e.m_20252_(1.0F);
            ThrownPotion potionentity = new ThrownPotion(e.m_9236_(), e);
            potionentity.m_37446_(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.POISON));
            potionentity.m_146926_(potionentity.m_146909_() - 20.0F);
            potionentity.m_6686_(vector3d.x, vector3d.y, vector3d.z, 0.75F, 8.0F);
            e.m_9236_().playSound((Player) null, e.m_20185_(), e.m_20186_(), e.m_20189_(), SoundEvents.WITCH_THROW, e.m_5720_(), 1.0F, 0.8F + e.m_9236_().getRandom().nextFloat() * 0.4F);
            e.m_9236_().m_7967_(potionentity);
        });
        PossessionActions.RegisterLeftClickAction(EntityType.SKELETON, e -> {
            ItemStack itemstack = e.m_6298_(e.m_21120_(ProjectileUtil.getWeaponHoldingHand(e, item -> item == Items.BOW)));
            AbstractArrow abstractarrowentity = ProjectileUtil.getMobArrow(e, itemstack, BowItem.getPowerForTime(40));
            if (e.m_21205_().getItem() instanceof BowItem) {
                abstractarrowentity = ((BowItem) e.m_21205_().getItem()).customArrow(abstractarrowentity);
            }
            Vec3 fwd = Vec3.directionFromRotation(e.m_20155_()).normalize();
            abstractarrowentity.shoot(fwd.x, fwd.y, fwd.z, 1.6F, (float) (14 - e.m_9236_().m_46791_().getId() * 4));
            e.m_5496_(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (e.m_217043_().nextFloat() * 0.4F + 0.8F));
            e.m_9236_().m_7967_(abstractarrowentity);
        });
        PossessionActions.RegisterLeftClickAction(EntityType.BEE, e -> {
            ClipContext ctx = new ClipContext(e.m_20182_(), e.m_20182_().add(e.m_20156_().scale(4.0)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, e);
            BlockHitResult bhr = e.m_9236_().m_45547_(ctx);
            if (bhr.getType() == HitResult.Type.MISS) {
                PossessionActions.InvokeDefault(e);
            } else {
                BlockState clickState = e.m_9236_().getBlockState(bhr.getBlockPos());
                if (clickState.m_204336_(BlockTags.FLOWERS)) {
                    e.setHasNectar(true);
                }
            }
        });
        PossessionActions.RegisterLeftClickAction(EntityType.ILLUSIONER, e -> {
            SpellRecipe recipe = new SpellRecipe();
            recipe.setShape(Shapes.BOLT);
            recipe.addComponent(Components.BLIND);
            recipe.addComponent(Components.MAGIC_DAMAGE);
            recipe.getShape().setValue(Attribute.RANGE, 16.0F);
            ItemStack spell = new ItemStack(ItemInit.SPELL.get());
            recipe.writeToNBT(spell.getOrCreateTag());
            SpellCaster.Affect(spell, recipe, e.m_9236_(), new SpellSource(e, InteractionHand.MAIN_HAND));
        });
        PossessionActions.RegisterLeftClickAction(EntityType.EVOKER, e -> {
            BlockPos blockpos = e.m_20183_();
            boolean flag = false;
            double d0 = 0.0;
            Vec3 forward = e.m_20156_().normalize();
            Vec3 summonPos = e.m_20182_().add(forward.x, 0.0, forward.z);
            do {
                BlockPos blockpos1 = blockpos.below();
                BlockState blockstate = e.m_9236_().getBlockState(blockpos1);
                if (blockstate.m_60783_(e.m_9236_(), blockpos1, Direction.UP)) {
                    if (!e.m_9236_().m_46859_(blockpos)) {
                        BlockState blockstate1 = e.m_9236_().getBlockState(blockpos);
                        VoxelShape voxelshape = blockstate1.m_60812_(e.m_9236_(), blockpos);
                        if (!voxelshape.isEmpty()) {
                            d0 = voxelshape.max(Direction.Axis.Y);
                        }
                    }
                    flag = true;
                    break;
                }
                blockpos = blockpos.below();
            } while (blockpos.m_123342_() >= Mth.floor(summonPos.z) - 1);
            if (flag) {
                for (int i = 0; i < 8; i++) {
                    Vec3 scaledFwd = forward.scale((double) ((float) i * 1.25F));
                    Vec3 pos = summonPos.add(scaledFwd.x, 0.0, scaledFwd.z);
                    e.m_9236_().m_7967_(new EvokerFangs(e.m_9236_(), pos.x, (double) blockpos.m_123342_() + d0, pos.z, e.m_146908_(), i * 2, e));
                }
            }
        });
    }
}