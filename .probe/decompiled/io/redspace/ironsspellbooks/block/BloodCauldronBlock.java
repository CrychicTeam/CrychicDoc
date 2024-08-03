package io.redspace.ironsspellbooks.block;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class BloodCauldronBlock extends LayeredCauldronBlock {

    public static final Predicate<Biome.Precipitation> NO_WEATHER = p_153526_ -> false;

    public BloodCauldronBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.CAULDRON), NO_WEATHER, getInteractionMap());
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos pos, Entity entity) {
        if (entity.tickCount % 20 == 0) {
            attemptCookEntity(blockState, level, pos, entity, () -> {
                level.setBlockAndUpdate(pos, (BlockState) blockState.m_61122_(LayeredCauldronBlock.LEVEL));
                level.m_142346_(null, GameEvent.FLUID_PLACE, pos);
            });
        }
        super.entityInside(blockState, level, pos, entity);
    }

    public static void attemptCookEntity(BlockState blockState, Level level, BlockPos pos, Entity entity, BloodCauldronBlock.CookExecution execution) {
        if (!level.isClientSide && CampfireBlock.isLitCampfire(level.getBlockState(pos.below())) && level.getBlockState(pos).m_60734_() instanceof AbstractCauldronBlock cauldron && entity instanceof LivingEntity livingEntity && livingEntity.m_20191_().intersects(cauldron.getInteractionShape(blockState, level, pos).bounds().move(pos)) && livingEntity.hurt(DamageSources.get(level, ISSDamageTypes.CAULDRON), 2.0F)) {
            MagicManager.spawnParticles(level, ParticleHelper.BLOOD, entity.getX(), entity.getY() + (double) (entity.getBbHeight() / 2.0F), entity.getZ(), 20, 0.05, 0.05, 0.05, 0.1, false);
            if (Utils.random.nextDouble() <= 0.5 && !isCauldronFull(blockState)) {
                execution.execute();
            }
        }
    }

    private static boolean isCauldronFull(BlockState blockState) {
        return !blockState.m_61138_(f_153514_) ? false : (Integer) blockState.m_61143_(LayeredCauldronBlock.LEVEL) == 3;
    }

    public static Map<Item, CauldronInteraction> getInteractionMap() {
        Map<Item, CauldronInteraction> BLOOD_CAULDRON_INTERACTIONS = CauldronInteraction.newInteractionMap();
        BLOOD_CAULDRON_INTERACTIONS.put(Items.GLASS_BOTTLE, (CauldronInteraction) (blockState, level, blockPos, player, hand, itemStack) -> {
            if (!level.isClientSide) {
                Item item = itemStack.getItem();
                player.m_21008_(hand, ItemUtils.createFilledResult(itemStack, player, new ItemStack(ItemRegistry.BLOOD_VIAL.get())));
                player.awardStat(Stats.USE_CAULDRON);
                player.awardStat(Stats.ITEM_USED.get(item));
                LayeredCauldronBlock.lowerFillLevel(blockState, level, blockPos);
                level.playSound(null, blockPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.m_142346_(null, GameEvent.FLUID_PICKUP, blockPos);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        });
        return BLOOD_CAULDRON_INTERACTIONS;
    }

    public interface CookExecution {

        void execute();
    }
}