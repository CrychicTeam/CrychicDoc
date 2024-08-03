package org.violetmoon.quark.content.building.entity;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.content.building.module.GlassItemFrameModule;

public class GlassItemFrame extends ItemFrame implements IEntityAdditionalSpawnData {

    public static final EntityDataAccessor<Boolean> IS_SHINY = SynchedEntityData.defineId(GlassItemFrame.class, EntityDataSerializers.BOOLEAN);

    private static final String TAG_SHINY = "isShiny";

    private static final GameProfile DUMMY_PROFILE = new GameProfile(UUID.randomUUID(), "ItemFrame");

    private boolean didHackery = false;

    private int onSignRotation = 0;

    private GlassItemFrame.SignAttachment attachment = GlassItemFrame.SignAttachment.NOT_ATTACHED;

    public GlassItemFrame(EntityType<? extends GlassItemFrame> type, Level worldIn) {
        super(type, worldIn);
    }

    public GlassItemFrame(Level worldIn, BlockPos blockPos, Direction face) {
        super(GlassItemFrameModule.glassFrameEntity, worldIn);
        this.f_31698_ = blockPos;
        this.m_6022_(face);
    }

    @NotNull
    @Override
    public InteractionResult interact(Player player, @NotNull InteractionHand hand) {
        ItemStack item = this.m_31822_();
        if (!player.m_6144_() && !item.isEmpty() && !(item.getItem() instanceof BannerItem)) {
            BlockPos behind = this.getBehindPos();
            BlockEntity tile = this.m_9236_().getBlockEntity(behind);
            if (tile != null && tile.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
                BlockState behindState = this.m_9236_().getBlockState(behind);
                InteractionResult result = behindState.m_60664_(this.m_9236_(), player, hand, new BlockHitResult(new Vec3(this.m_20185_(), this.m_20186_(), this.m_20189_()), this.f_31699_, behind, true));
                if (result.consumesAction()) {
                    return result;
                }
            }
        }
        InteractionResult res = super.interact(player, hand);
        this.updateIsOnSign();
        return res;
    }

    @Override
    public void tick() {
        super.m_8119_();
        boolean shouldUpdateMaps = GlassItemFrameModule.glassItemFramesUpdateMapsEveryTick;
        if (this.m_9236_().getGameTime() % 100L == 0L) {
            this.updateIsOnSign();
            shouldUpdateMaps = true;
        }
        if (!this.m_9236_().isClientSide && GlassItemFrameModule.glassItemFramesUpdateMaps && shouldUpdateMaps) {
            ItemStack stack = this.m_31822_();
            if (stack.getItem() instanceof MapItem map && this.m_9236_() instanceof ServerLevel sworld) {
                ItemStack clone = stack.copy();
                MapItemSavedData data = MapItem.getSavedData(clone, this.m_9236_());
                if (data != null && !data.locked) {
                    FakePlayer fakePlayer = FakePlayerFactory.get(sworld, DUMMY_PROFILE);
                    clone.setEntityRepresentation(null);
                    fakePlayer.m_6034_(this.m_20185_(), this.m_20186_(), this.m_20189_());
                    fakePlayer.m_150109_().setItem(0, clone);
                    map.update(this.m_9236_(), fakePlayer, data);
                }
            }
        }
    }

    private void updateIsOnSign() {
        this.attachment = GlassItemFrame.SignAttachment.NOT_ATTACHED;
        if (this.f_31699_.getAxis() != Direction.Axis.Y) {
            BlockState back = this.m_9236_().getBlockState(this.getBehindPos());
            boolean standing = back.m_204336_(BlockTags.STANDING_SIGNS);
            boolean hangingCeil = back.m_204336_(BlockTags.CEILING_HANGING_SIGNS);
            if (standing || hangingCeil) {
                this.onSignRotation = (Integer) back.m_61143_(BlockStateProperties.ROTATION_16);
                double[] angles = new double[] { 0.0, 0.0, 0.0, 180.0, -90.0, 90.0 };
                double ourRotation = angles[this.m_6350_().getOpposite().ordinal()];
                double signRotation = (double) ((Integer) back.m_61143_(BlockStateProperties.ROTATION_16)).intValue() / 16.0 * 360.0;
                if (signRotation > 180.0) {
                    signRotation -= 360.0;
                }
                double diff = ourRotation - signRotation;
                double absDiff = Math.abs(diff);
                if (diff < 0.0) {
                    absDiff -= 0.01;
                }
                if (absDiff < 45.0) {
                    this.attachment = standing ? GlassItemFrame.SignAttachment.STANDING_IN_FRONT : GlassItemFrame.SignAttachment.HANGING_IN_FRONT;
                } else if (absDiff >= 135.0 && absDiff < 225.0) {
                    this.attachment = standing ? GlassItemFrame.SignAttachment.STANDING_BEHIND : GlassItemFrame.SignAttachment.HANGING_BEHIND;
                }
                return;
            }
            if (back.m_204336_(BlockTags.WALL_SIGNS)) {
                Direction signDir = (Direction) back.m_61143_(WallSignBlock.FACING);
                if (signDir == this.m_6350_()) {
                    this.attachment = GlassItemFrame.SignAttachment.WALL_SIGN;
                }
                return;
            }
            if (back.m_204336_(BlockTags.WALL_HANGING_SIGNS)) {
                Direction signDir = (Direction) back.m_61143_(WallHangingSignBlock.FACING);
                if (signDir == this.m_6350_() || signDir == this.m_6350_().getOpposite()) {
                    this.attachment = GlassItemFrame.SignAttachment.HANGING_FROM_WALL;
                }
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(IS_SHINY, false);
    }

    @Override
    public boolean survives() {
        return this.isOnSign() || super.survives();
    }

    public BlockPos getBehindPos() {
        return this.f_31698_.relative(this.f_31699_.getOpposite());
    }

    public GlassItemFrame.SignAttachment getSignAttachment() {
        return this.attachment;
    }

    public boolean isOnSign() {
        return this.getSignAttachment() != GlassItemFrame.SignAttachment.NOT_ATTACHED;
    }

    public int getOnSignRotation() {
        return this.onSignRotation;
    }

    @Nullable
    @Override
    public ItemEntity spawnAtLocation(@NotNull ItemStack stack, float offset) {
        if (stack.getItem() == Items.ITEM_FRAME && !this.didHackery) {
            stack = new ItemStack(this.getDroppedItem());
            this.didHackery = true;
        }
        return super.m_5552_(stack, offset);
    }

    @NotNull
    public ItemStack getPickedResult(HitResult target) {
        ItemStack held = this.m_31822_();
        return held.isEmpty() ? new ItemStack(this.getDroppedItem()) : held.copy();
    }

    private Item getDroppedItem() {
        return this.f_19804_.get(IS_SHINY) ? GlassItemFrameModule.glowingGlassFrame : GlassItemFrameModule.glassFrame;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag cmp) {
        super.addAdditionalSaveData(cmp);
        cmp.putBoolean("isShiny", this.f_19804_.get(IS_SHINY));
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag cmp) {
        super.readAdditionalSaveData(cmp);
        this.f_19804_.set(IS_SHINY, cmp.getBoolean("isShiny"));
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.f_31698_);
        buffer.writeVarInt(this.f_31699_.get3DDataValue());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        this.f_31698_ = buffer.readBlockPos();
        this.m_6022_(Direction.from3DDataValue(buffer.readVarInt()));
    }

    public static enum SignAttachment {

        NOT_ATTACHED,
        STANDING_IN_FRONT,
        STANDING_BEHIND,
        WALL_SIGN,
        HANGING_FROM_WALL,
        HANGING_IN_FRONT,
        HANGING_BEHIND
    }
}