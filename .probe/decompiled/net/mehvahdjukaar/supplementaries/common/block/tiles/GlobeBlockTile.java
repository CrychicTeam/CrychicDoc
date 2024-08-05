package net.mehvahdjukaar.supplementaries.common.block.tiles;

import com.mojang.datafixers.util.Pair;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.mehvahdjukaar.supplementaries.client.GlobeManager;
import net.mehvahdjukaar.supplementaries.common.block.blocks.GlobeBlock;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModSounds;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Nameable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GlobeBlockTile extends BlockEntity implements Nameable {

    private final boolean sepia;

    private boolean sheared = false;

    private Component customName;

    private float yaw = 0.0F;

    private float prevYaw = 0.0F;

    private Pair<GlobeManager.Model, ResourceLocation> renderData = Pair.of(GlobeManager.Model.GLOBE, null);

    public GlobeBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.GLOBE_TILE.get(), pos, state);
        this.sepia = state.m_60713_((Block) ModRegistry.GLOBE_SEPIA.get());
    }

    public int getFaceRot() {
        return (3 - (Integer) this.m_58900_().m_61143_(GlobeBlock.ROTATION)) * 90;
    }

    public float getRotation(float partialTicks) {
        int face = this.getFaceRot();
        return Mth.lerp(partialTicks, this.prevYaw + (float) face, this.yaw + (float) face);
    }

    public Pair<GlobeManager.Model, ResourceLocation> getRenderData() {
        return this.renderData;
    }

    public boolean isSepia() {
        return this.sepia;
    }

    public void setCustomName(Component name) {
        this.customName = name;
        this.updateRenderData();
    }

    public void toggleShearing() {
        this.sheared = !this.sheared;
        this.updateRenderData();
    }

    private void updateRenderData() {
        if (this.sheared) {
            this.renderData = Pair.of(GlobeManager.Model.SHEARED, this.sepia ? ModTextures.GLOBE_SHEARED_SEPIA_TEXTURE : ModTextures.GLOBE_SHEARED_TEXTURE);
        } else if (this.m_8077_()) {
            this.renderData = GlobeManager.Type.getModelAndTexture(this.getCustomName().getString());
        } else {
            this.renderData = Pair.of(GlobeManager.Model.GLOBE, null);
        }
    }

    @Override
    public Component getName() {
        return this.customName != null ? this.customName : this.getDefaultName();
    }

    @Override
    public Component getCustomName() {
        return this.customName;
    }

    public Component getDefaultName() {
        return Component.translatable("block.supplementaries.globe");
    }

    @Override
    public void load(CompoundTag compound) {
        if (compound.contains("CustomName", 8)) {
            this.setCustomName(Component.Serializer.fromJson(compound.getString("CustomName")));
        }
        this.yaw = compound.getFloat("Yaw");
        this.sheared = compound.getBoolean("Sheared");
        super.load(compound);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (this.customName != null) {
            tag.putString("CustomName", Component.Serializer.toJson(this.customName));
        }
        tag.putFloat("Yaw", this.yaw);
        tag.putBoolean("Sheared", this.sheared);
    }

    public void spin() {
        int spin = 360;
        int inc = 90;
        int face = (this.getFaceRot() - inc + 360) % 360;
        this.yaw = this.yaw + (float) spin + (float) inc;
        this.prevYaw = this.prevYaw + (float) spin + (float) inc;
        this.f_58857_.setBlockAndUpdate(this.f_58858_, (BlockState) this.m_58900_().m_61124_(GlobeBlock.ROTATION, 3 - face / 90));
        this.m_6596_();
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == 1) {
            this.spin();
            this.f_58857_.playSound(null, this.f_58858_, (SoundEvent) ModSounds.GLOBE_SPIN.get(), SoundSource.BLOCKS, 0.65F, MthUtils.nextWeighted(this.f_58857_.random, 0.2F) + 0.9F);
            return true;
        } else {
            return super.triggerEvent(id, type);
        }
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, GlobeBlockTile tile) {
        tile.prevYaw = tile.yaw;
        if (tile.yaw != 0.0F) {
            if (tile.yaw < 0.0F) {
                tile.yaw = 0.0F;
                pLevel.updateNeighbourForOutputSignal(pPos, pState.m_60734_());
            } else {
                tile.yaw = tile.yaw * 0.94F - 0.7F;
            }
        }
    }

    public Direction getDirection() {
        return (Direction) this.m_58900_().m_61143_(GlobeBlock.FACING);
    }

    public boolean isSpinningVeryFast() {
        return this.yaw > 1500.0F;
    }

    public int getSignalPower() {
        return this.yaw != 0.0F ? 15 : this.getFaceRot() / 90 + 1;
    }
}