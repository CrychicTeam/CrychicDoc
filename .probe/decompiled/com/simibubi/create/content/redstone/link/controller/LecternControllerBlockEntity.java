package com.simibubi.create.content.redstone.link.controller;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.DistExecutor;

public class LecternControllerBlockEntity extends SmartBlockEntity {

    private ItemStack controller;

    private UUID user;

    private UUID prevUser;

    private boolean deactivatedThisTick;

    public LecternControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("Controller", this.controller.save(new CompoundTag()));
        if (this.user != null) {
            compound.putUUID("User", this.user);
        }
    }

    @Override
    public void writeSafe(CompoundTag compound) {
        super.writeSafe(compound);
        compound.put("Controller", this.controller.save(new CompoundTag()));
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.controller = ItemStack.of(compound.getCompound("Controller"));
        this.user = compound.hasUUID("User") ? compound.getUUID("User") : null;
    }

    public ItemStack getController() {
        return this.controller;
    }

    public boolean hasUser() {
        return this.user != null;
    }

    public boolean isUsedBy(Player player) {
        return this.hasUser() && this.user.equals(player.m_20148_());
    }

    public void tryStartUsing(Player player) {
        if (!this.deactivatedThisTick && !this.hasUser() && !playerIsUsingLectern(player) && playerInRange(player, this.f_58857_, this.f_58858_)) {
            this.startUsing(player);
        }
    }

    public void tryStopUsing(Player player) {
        if (this.isUsedBy(player)) {
            this.stopUsing(player);
        }
    }

    private void startUsing(Player player) {
        this.user = player.m_20148_();
        player.getPersistentData().putBoolean("IsUsingLecternController", true);
        this.sendData();
    }

    private void stopUsing(Player player) {
        this.user = null;
        if (player != null) {
            player.getPersistentData().remove("IsUsingLecternController");
        }
        this.deactivatedThisTick = true;
        this.sendData();
    }

    public static boolean playerIsUsingLectern(Player player) {
        return player.getPersistentData().contains("IsUsingLecternController");
    }

    @Override
    public void tick() {
        super.tick();
        if (this.f_58857_.isClientSide) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::tryToggleActive);
            this.prevUser = this.user;
        }
        if (!this.f_58857_.isClientSide) {
            this.deactivatedThisTick = false;
            if (!(this.f_58857_ instanceof ServerLevel)) {
                return;
            }
            if (this.user == null) {
                return;
            }
            if (!(((ServerLevel) this.f_58857_).getEntity(this.user) instanceof Player player)) {
                this.stopUsing(null);
                return;
            }
            if (!playerInRange(player, this.f_58857_, this.f_58858_) || !playerIsUsingLectern(player)) {
                this.stopUsing(player);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void tryToggleActive() {
        if (this.user == null && Minecraft.getInstance().player.m_20148_().equals(this.prevUser)) {
            LinkedControllerClientHandler.deactivateInLectern();
        } else if (this.prevUser == null && Minecraft.getInstance().player.m_20148_().equals(this.user)) {
            LinkedControllerClientHandler.activateInLectern(this.f_58858_);
        }
    }

    public void setController(ItemStack newController) {
        this.controller = newController;
        if (newController != null) {
            AllSoundEvents.CONTROLLER_PUT.playOnServer(this.f_58857_, this.f_58858_);
        }
    }

    public void swapControllers(ItemStack stack, Player player, InteractionHand hand, BlockState state) {
        ItemStack newController = stack.copy();
        stack.setCount(0);
        if (player.m_21120_(hand).isEmpty()) {
            player.m_21008_(hand, this.controller);
        } else {
            this.dropController(state);
        }
        this.setController(newController);
    }

    public void dropController(BlockState state) {
        Entity playerEntity = ((ServerLevel) this.f_58857_).getEntity(this.user);
        if (playerEntity instanceof Player) {
            this.stopUsing((Player) playerEntity);
        }
        Direction dir = (Direction) state.m_61143_(LecternControllerBlock.f_54465_);
        double x = (double) this.f_58858_.m_123341_() + 0.5 + 0.25 * (double) dir.getStepX();
        double y = (double) (this.f_58858_.m_123342_() + 1);
        double z = (double) this.f_58858_.m_123343_() + 0.5 + 0.25 * (double) dir.getStepZ();
        ItemEntity itementity = new ItemEntity(this.f_58857_, x, y, z, this.controller.copy());
        itementity.setDefaultPickUpDelay();
        this.f_58857_.m_7967_(itementity);
        this.controller = null;
    }

    public static boolean playerInRange(Player player, Level world, BlockPos pos) {
        double reach = 0.4 * player.m_21133_(ForgeMod.BLOCK_REACH.get());
        return player.m_20238_(Vec3.atCenterOf(pos)) < reach * reach;
    }
}