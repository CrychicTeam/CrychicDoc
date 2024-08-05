package com.almostreliable.summoningrituals.altar;

import com.almostreliable.summoningrituals.Registration;
import com.almostreliable.summoningrituals.platform.Platform;
import com.almostreliable.summoningrituals.platform.PlatformBlockEntity;
import com.almostreliable.summoningrituals.recipe.AltarRecipe;
import com.almostreliable.summoningrituals.recipe.component.BlockReference;
import com.almostreliable.summoningrituals.recipe.component.RecipeSacrifices;
import com.almostreliable.summoningrituals.util.GameUtils;
import com.almostreliable.summoningrituals.util.TextUtils;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class AltarBlockEntity extends PlatformBlockEntity {

    public static final AltarObservable SUMMONING_START = new AltarObservable();

    public static final AltarObservable SUMMONING_COMPLETE = new AltarObservable();

    @Nullable
    private AltarRecipe currentRecipe;

    @Nullable
    private List<AltarBlockEntity.EntitySacrifice> sacrifices;

    @Nullable
    private ServerPlayer invokingPlayer;

    private int processTime;

    public AltarBlockEntity(BlockPos pos, BlockState state) {
        super(Registration.ALTAR_ENTITY.get(), pos, state);
    }

    @Override
    public void load(CompoundTag tag) {
        super.m_142466_(tag);
        if (tag.contains("inventory")) {
            this.inventory.deserialize(tag.getCompound("inventory"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.m_183515_(tag);
        tag.put("inventory", this.inventory.serialize());
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.m_5995_();
        this.saveAdditional(tag);
        return tag;
    }

    @Override
    public ItemStack handleInteraction(@Nullable ServerPlayer player, ItemStack stack) {
        if (this.progress > 0) {
            GameUtils.sendPlayerMessage(player, "progress", ChatFormatting.RED);
            return stack;
        } else if (stack.isEmpty()) {
            if (player != null && player.m_6144_()) {
                this.inventory.popLastInserted();
            }
            return ItemStack.EMPTY;
        } else {
            if (AltarRecipe.CATALYST_CACHE.stream().anyMatch(ingredient -> ingredient.test(stack))) {
                this.inventory.setCatalyst(stack.copyWithCount(1));
                AltarRecipe recipe = this.findRecipe();
                if (recipe != null) {
                    this.handleSummoning(recipe, player);
                    ItemStack remainder = stack.copyWithCount(stack.getCount() - 1);
                    return remainder.isEmpty() ? ItemStack.EMPTY : remainder;
                }
                this.inventory.setCatalyst(ItemStack.EMPTY);
            }
            ItemStack remaining = this.inventory.handleInsertion(stack);
            if (player != null && (remaining.isEmpty() || stack.getCount() != remaining.getCount())) {
                GameUtils.playSound(this.f_58857_, this.f_58858_, SoundEvents.ITEM_PICKUP);
            }
            return remaining;
        }
    }

    void playerDestroy(boolean creative) {
        assert this.f_58857_ != null && !this.f_58857_.isClientSide;
        this.inventory.dropContents();
        if (!creative) {
            GameUtils.dropItem(this.f_58857_, this.f_58858_, new ItemStack(Registration.ALTAR_ITEM.get()), true);
        }
    }

    void tick() {
        if (this.f_58857_ != null) {
            if (!this.inventory.getCatalyst().isEmpty() && this.currentRecipe == null) {
                AltarRecipe recipe = this.findRecipe();
                if (recipe == null) {
                    this.resetSummoning(true);
                    return;
                }
                this.handleSummoning(recipe, null);
            }
            if (this.currentRecipe != null) {
                if (this.progress >= this.currentRecipe.getRecipeTime()) {
                    if (this.inventory.handleRecipe(this.currentRecipe)) {
                        this.currentRecipe.getOutputs().handleRecipe((ServerLevel) this.f_58857_, this.f_58858_);
                        SUMMONING_COMPLETE.invoke((ServerLevel) this.f_58857_, this.f_58858_, this.currentRecipe, this.invokingPlayer);
                        GameUtils.playSound(this.f_58857_, this.f_58858_, SoundEvents.EXPERIENCE_ORB_PICKUP);
                        this.resetSummoning(false);
                    } else {
                        GameUtils.sendPlayerMessage(this.invokingPlayer, "invalid", ChatFormatting.RED);
                        this.resetSummoning(true);
                    }
                } else {
                    if (this.progress == 0) {
                        this.changeActivityState(true);
                        if (this.sacrifices != null && !this.sacrifices.isEmpty()) {
                            this.sacrifices.stream().map(AltarBlockEntity.EntitySacrifice::kill).filter(positions -> !positions.isEmpty()).forEach(p -> Platform.sendParticleEmit(this.f_58857_, p));
                        }
                    }
                    this.progress++;
                    Platform.sendProgressUpdate(this.f_58857_, this.f_58858_, this.progress);
                }
            }
        }
    }

    private void resetSummoning(boolean popLastInserted) {
        assert this.f_58857_ != null;
        this.currentRecipe = null;
        this.sacrifices = null;
        this.invokingPlayer = null;
        this.progress = 0;
        Platform.sendProgressUpdate(this.f_58857_, this.f_58858_, this.progress);
        this.processTime = 0;
        Platform.sendProcessTimeUpdate(this.f_58857_, this.f_58858_, this.processTime);
        this.changeActivityState(false);
        if (popLastInserted) {
            this.inventory.popLastInserted();
        }
    }

    private void handleSummoning(AltarRecipe recipe, @Nullable ServerPlayer player) {
        assert this.f_58857_ != null && !this.f_58857_.isClientSide;
        this.sacrifices = this.checkSacrifices(recipe.getSacrifices(), player);
        if (this.sacrifices == null || !this.checkBlockBelow(recipe.getBlockBelow(), player) || !recipe.getDayTime().check(this.f_58857_, player) || !recipe.getWeather().check(this.f_58857_, player)) {
            this.inventory.popLastInserted();
            GameUtils.playSound(this.f_58857_, this.f_58858_, SoundEvents.CHAIN_BREAK);
        } else if (!SUMMONING_START.invoke((ServerLevel) this.f_58857_, this.f_58858_, recipe, player)) {
            this.resetSummoning(true);
        } else {
            this.currentRecipe = recipe;
            this.invokingPlayer = player;
            this.processTime = recipe.getRecipeTime();
            GameUtils.playSound(this.f_58857_, this.f_58858_, SoundEvents.BEACON_ACTIVATE);
            Platform.sendProcessTimeUpdate(this.f_58857_, this.f_58858_, this.processTime);
        }
    }

    @Nullable
    private AltarRecipe findRecipe() {
        assert this.f_58857_ != null && !this.f_58857_.isClientSide;
        RecipeManager recipeManager = this.f_58857_.getRecipeManager();
        return (AltarRecipe) recipeManager.getRecipeFor(Registration.ALTAR_RECIPE.type().get(), this.inventory.getVanillaInv(), this.f_58857_).orElse(null);
    }

    @Nullable
    private List<AltarBlockEntity.EntitySacrifice> checkSacrifices(RecipeSacrifices sacrifices, @Nullable ServerPlayer player) {
        assert this.f_58857_ != null && !this.f_58857_.isClientSide;
        if (sacrifices.isEmpty()) {
            return List.of();
        } else {
            AABB region = sacrifices.getRegion(this.f_58858_);
            List<Entity> entities = this.f_58857_.m_45933_(player, region);
            List<AltarBlockEntity.EntitySacrifice> toKill = new ArrayList();
            boolean success = sacrifices.test(sacrifice -> {
                List<Entity> found = entities.stream().filter(sacrifice).toList();
                if (found.size() < sacrifice.count()) {
                    GameUtils.sendPlayerMessage(player, "sacrifices", ChatFormatting.YELLOW);
                    return false;
                } else {
                    toKill.add(new AltarBlockEntity.EntitySacrifice(found, sacrifice.count()));
                    return true;
                }
            });
            return success ? toKill : null;
        }
    }

    private boolean checkBlockBelow(@Nullable BlockReference blockBelow, @Nullable ServerPlayer player) {
        assert this.f_58857_ != null && !this.f_58857_.isClientSide;
        if (blockBelow != null && !blockBelow.test(this.f_58857_.getBlockState(this.f_58858_.below()))) {
            GameUtils.sendPlayerMessage(player, "block_below", ChatFormatting.YELLOW);
            return false;
        } else {
            return true;
        }
    }

    private void changeActivityState(boolean state) {
        if (this.f_58857_ != null && !this.f_58857_.isClientSide) {
            BlockState oldState = this.f_58857_.getBlockState(this.f_58858_);
            if (!((Boolean) oldState.m_61143_(AltarBlock.ACTIVE)).equals(state)) {
                this.f_58857_.setBlockAndUpdate(this.f_58858_, (BlockState) oldState.m_61124_(AltarBlock.ACTIVE, state));
            }
        }
    }

    public int getProcessTime() {
        return this.processTime;
    }

    public void setProcessTime(int processTime) {
        this.processTime = processTime;
    }

    private static record EntitySacrifice(List<Entity> entities, int count) {

        private List<BlockPos> kill() {
            List<BlockPos> positions = new ArrayList();
            for (int i = 0; i < this.count; i++) {
                Entity entity = (Entity) this.entities.get(i);
                entity.addTag(TextUtils.f("{}_sacrificed", "summoningrituals"));
                entity.kill();
                positions.add(entity.blockPosition());
            }
            return positions;
        }
    }
}