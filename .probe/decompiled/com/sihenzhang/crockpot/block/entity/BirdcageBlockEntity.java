package com.sihenzhang.crockpot.block.entity;

import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.entity.Birdcage;
import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.recipe.ParrotFeedingRecipe;
import java.util.ArrayDeque;
import java.util.Queue;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

public class BirdcageBlockEntity extends BlockEntity {

    private static final int FED_COOLDOWN = 10;

    public static final int OUTPUT_COOLDOWN = 40;

    private int fedCooldown;

    private final Queue<Pair<ItemStack, Long>> outputBuffer = new ArrayDeque(4);

    public BirdcageBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(CrockPotBlockEntities.BIRDCAGE_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, BirdcageBlockEntity pBlockEntity) {
        if (pBlockEntity.isOnCooldown()) {
            pBlockEntity.fedCooldown--;
        }
        while (!pBlockEntity.outputBuffer.isEmpty() && ((Pair) pBlockEntity.outputBuffer.peek()).getSecond() < pLevel.getGameTime()) {
            ItemStack output = (ItemStack) ((Pair) pBlockEntity.outputBuffer.poll()).getFirst();
            Containers.dropContents(pLevel, pPos, new SimpleContainer(output));
        }
    }

    public boolean isOnCooldown() {
        return this.fedCooldown > 0;
    }

    public Queue<Pair<ItemStack, Long>> getOutputBuffer() {
        return this.outputBuffer;
    }

    public boolean captureParrot(Level pLevel, BlockPos pPos, Player pPlayer, Parrot pParrot, Birdcage pBirdcage, boolean isLeftShoulder) {
        pParrot.m_21816_(pPlayer.m_20148_());
        pParrot.m_6034_(pPlayer.m_20185_(), pPlayer.m_20186_() + 0.7, pPlayer.m_20189_());
        pLevel.m_7967_(pParrot);
        pBirdcage.m_6034_((double) pPos.m_123341_() + 0.5, (double) pPos.m_123342_() + 0.475, (double) pPos.m_123343_() + 0.5);
        pLevel.m_7967_(pBirdcage);
        if (!pParrot.m_7998_(pBirdcage, true)) {
            pParrot.m_146870_();
            pBirdcage.m_146870_();
            return false;
        } else {
            if (isLeftShoulder) {
                pPlayer.setShoulderEntityLeft(new CompoundTag());
            } else {
                pPlayer.setShoulderEntityRight(new CompoundTag());
            }
            return true;
        }
    }

    public boolean fedByMeat(ItemStack meat, FoodValues foodValues, Parrot parrot) {
        if (this.isOnCooldown()) {
            return false;
        } else if (meat.isEmpty()) {
            return false;
        } else {
            boolean isMonsterFood = foodValues.has(FoodCategory.MONSTER);
            if (!isMonsterFood || this.f_58857_.random.nextBoolean()) {
                ItemStack parrotEgg = ((Item) ((RegistryObject) CrockPotItems.PARROT_EGGS.get(parrot.getVariant())).get()).getDefaultInstance();
                this.outputBuffer.offer(Pair.of(parrotEgg, this.f_58857_.getGameTime() + 40L));
            }
            meat.shrink(1);
            this.fedCooldown = 10;
            if (!parrot.m_20067_()) {
                this.f_58857_.playSound(null, parrot.m_20185_(), parrot.m_20186_(), parrot.m_20189_(), SoundEvents.GENERIC_EAT, parrot.getSoundSource(), 1.0F, isMonsterFood ? 0.75F : 1.25F);
            }
            this.f_58857_.broadcastEntityEvent(parrot, (byte) 6);
            return true;
        }
    }

    public boolean fedByRecipe(ItemStack input, ParrotFeedingRecipe recipe, RegistryAccess registryAccess, Parrot parrot) {
        if (this.isOnCooldown()) {
            return false;
        } else if (input.isEmpty()) {
            return false;
        } else {
            ItemStack result = recipe.assemble(new SimpleContainer(input), registryAccess);
            if (!result.isEmpty()) {
                this.outputBuffer.offer(Pair.of(result, this.f_58857_.getGameTime() + 40L));
            }
            input.shrink(1);
            this.fedCooldown = 10;
            if (!parrot.m_20067_()) {
                this.f_58857_.playSound(null, parrot.m_20185_(), parrot.m_20186_(), parrot.m_20189_(), SoundEvents.PARROT_EAT, parrot.getSoundSource(), 1.0F, 1.0F);
            }
            this.f_58857_.broadcastEntityEvent(parrot, (byte) 7);
            return true;
        }
    }
}