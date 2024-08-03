package net.mehvahdjukaar.moonlight.api.block;

import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

public interface ILightable {

    TagKey<Item> FLINT_AND_STEELS = TagKey.create(Registries.ITEM, new ResourceLocation("forge", "tools/flint_and_steel"));

    default boolean isLitUp(BlockState state, BlockGetter level, BlockPos pos) {
        return this.isLitUp(state);
    }

    default void setLitUp(BlockState state, LevelAccessor world, BlockPos pos, boolean lit) {
        world.m_7731_(pos, this.toggleLitState(state, lit), 3);
    }

    @Deprecated(forRemoval = true)
    default boolean isLitUp(BlockState state) {
        return false;
    }

    @Deprecated(forRemoval = true)
    default BlockState toggleLitState(BlockState state, boolean lit) {
        return state;
    }

    default boolean lightUp(@Nullable Entity player, BlockState state, BlockPos pos, LevelAccessor world, ILightable.FireSourceType fireSourceType) {
        if (!this.isLitUp(state, world, pos)) {
            if (!world.m_5776_()) {
                this.setLitUp(state, world, pos, true);
                this.playLightUpSound(world, pos, fireSourceType);
            }
            world.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            return true;
        } else {
            return false;
        }
    }

    default boolean extinguish(@Nullable Entity player, BlockState state, BlockPos pos, LevelAccessor world) {
        if (this.isLitUp(state, world, pos)) {
            if (!world.m_5776_()) {
                this.playExtinguishSound(world, pos);
                this.setLitUp(state, world, pos, false);
            } else {
                this.spawnSmokeParticles(state, pos, world);
            }
            world.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            return true;
        } else {
            return false;
        }
    }

    default boolean interactWithProjectile(Level level, BlockState state, Projectile projectile, BlockPos pos) {
        if (projectile.m_6060_()) {
            Entity entity = projectile.getOwner();
            if ((entity == null || entity instanceof Player || PlatHelper.isMobGriefingOn(level, entity)) && this.lightUp(projectile, state, pos, level, ILightable.FireSourceType.FLAMING_ARROW)) {
                return true;
            }
        } else if (projectile instanceof ThrownPotion potion && PotionUtils.getPotion(potion.m_7846_()) == Potions.WATER) {
            Entity entity = projectile.getOwner();
            boolean flag = entity == null || entity instanceof Player || PlatHelper.isMobGriefingOn(level, entity);
            if (flag && this.extinguish(projectile, state, pos, level)) {
                return true;
            }
        }
        return false;
    }

    default InteractionResult interactWithPlayer(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn) {
        ItemStack stack = player.m_21120_(handIn);
        if (Utils.mayPerformBlockAction(player, pos, stack)) {
            if (!this.isLitUp(state, level, pos)) {
                Item item = stack.getItem();
                if (!(item instanceof FlintAndSteelItem) && !stack.is(FLINT_AND_STEELS)) {
                    if (item instanceof FireChargeItem && this.lightUp(player, state, pos, level, ILightable.FireSourceType.FIRE_CHANGE)) {
                        stack.hurtAndBreak(1, player, playerIn -> playerIn.m_21190_(handIn));
                        if (!player.isCreative()) {
                            stack.shrink(1);
                        }
                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
                } else if (this.lightUp(player, state, pos, level, ILightable.FireSourceType.FLINT_AND_STEEL)) {
                    stack.hurtAndBreak(1, player, playerIn -> playerIn.m_21190_(handIn));
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            } else if (this.canBeExtinguishedBy(stack) && this.extinguish(player, state, pos, level) && !(stack.getItem() instanceof BrushItem)) {
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    default boolean canBeExtinguishedBy(ItemStack item) {
        return item.getItem() instanceof ShovelItem || item.getItem() instanceof BrushItem;
    }

    default void playLightUpSound(LevelAccessor world, BlockPos pos, ILightable.FireSourceType type) {
        type.play(world, pos);
    }

    default void playExtinguishSound(LevelAccessor world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 0.5F, 1.5F);
    }

    default void spawnSmokeParticles(BlockState state, BlockPos pos, LevelAccessor world) {
        RandomSource random = world.getRandom();
        for (int i = 0; i < 10; i++) {
            world.addParticle(ParticleTypes.SMOKE, (double) ((float) pos.m_123341_() + 0.25F + random.nextFloat() * 0.5F), (double) ((float) pos.m_123342_() + 0.35F + random.nextFloat() * 0.5F), (double) ((float) pos.m_123343_() + 0.25F + random.nextFloat() * 0.5F), 0.0, 0.005, 0.0);
        }
    }

    public static enum FireSourceType {

        FLINT_AND_STEEL, FIRE_CHANGE, FLAMING_ARROW;

        public void play(LevelAccessor world, BlockPos pos) {
            switch(this) {
                case FIRE_CHANGE:
                    world.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.2F + 1.0F);
                    break;
                case FLAMING_ARROW:
                    world.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 0.5F, 1.4F);
                    break;
                case FLINT_AND_STEEL:
                    world.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
            }
        }
    }
}