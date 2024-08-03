package net.mehvahdjukaar.supplementaries.common.events.overrides;

import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.common.block.blocks.JarBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.JarBlockTile;
import net.mehvahdjukaar.supplementaries.common.items.JarItem;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModDamageSources;
import net.mehvahdjukaar.supplementaries.reg.ModParticles;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;

class XPBottlingBehavior implements ItemUseOnBlockOverride {

    @Override
    public boolean isEnabled() {
        return (Boolean) CommonConfigs.Tweaks.BOTTLE_XP.get();
    }

    @Override
    public boolean appliesToItem(Item item) {
        return item == Items.GLASS_BOTTLE || item instanceof JarItem || item == Items.EXPERIENCE_BOTTLE;
    }

    @Override
    public InteractionResult tryPerformingAction(Level world, Player player, InteractionHand hand, ItemStack stack, BlockHitResult hit) {
        JarBlockTile dummyTile = new JarBlockTile(BlockPos.ZERO, ((Block) ModRegistry.JAR.get()).defaultBlockState());
        BlockPos pos = hit.getBlockPos();
        Item i = stack.getItem();
        if (CommonConfigs.xpBottlingOverride.test(world.getBlockState(pos).m_60734_())) {
            ItemStack returnStack = null;
            if (i == Items.EXPERIENCE_BOTTLE) {
                return InteractionResult.FAIL;
            }
            if (player.experienceLevel > 0 || player.isCreative()) {
                if (i == Items.GLASS_BOTTLE) {
                    returnStack = new ItemStack(Items.EXPERIENCE_BOTTLE);
                } else if (i instanceof JarItem) {
                    dummyTile.resetHolders();
                    CompoundTag tag = stack.getTagElement("BlockEntityTag");
                    if (tag != null) {
                        dummyTile.load(tag);
                    }
                    if (dummyTile.canInteractWithSoftFluidTank()) {
                        ItemStack tempStack = new ItemStack(Items.EXPERIENCE_BOTTLE);
                        ItemStack temp = dummyTile.fluidHolder.interactWithItem(tempStack, null, null, false);
                        if (temp != null && temp.getItem() == Items.GLASS_BOTTLE) {
                            returnStack = ((JarBlock) ((BlockItem) i).getBlock()).getJarItem(dummyTile);
                        }
                    }
                }
                if (returnStack != null) {
                    player.hurt(ModDamageSources.bottling(), (float) ((Integer) CommonConfigs.Tweaks.BOTTLING_COST.get()).intValue());
                    Utils.swapItem(player, hand, returnStack);
                    if (!player.isCreative()) {
                        player.giveExperiencePoints(-Utils.getXPinaBottle(1, world.random) - 3);
                    }
                    if (world.isClientSide) {
                        Minecraft.getInstance().particleEngine.createTrackingEmitter(player, (ParticleOptions) ModParticles.BOTTLING_XP_PARTICLE.get(), 1);
                    }
                    world.playSound(null, player.m_20183_(), SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return InteractionResult.sidedSuccess(world.isClientSide);
                }
            }
        }
        return InteractionResult.PASS;
    }
}