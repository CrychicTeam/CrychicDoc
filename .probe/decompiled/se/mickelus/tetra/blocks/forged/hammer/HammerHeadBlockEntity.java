package se.mickelus.tetra.blocks.forged.hammer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

@ParametersAreNonnullByDefault
public class HammerHeadBlockEntity extends BlockEntity {

    private static final String jamKey = "jam";

    public static RegistryObject<BlockEntityType<HammerHeadBlockEntity>> type;

    private long activationTime = -1L;

    private long unjamTime = -1L;

    private boolean jammed;

    public HammerHeadBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(type.get(), blockPos0, blockState1);
    }

    public void activate() {
        this.activationTime = System.currentTimeMillis();
    }

    public long getActivationTime() {
        return this.activationTime;
    }

    public long getUnjamTime() {
        return this.unjamTime;
    }

    public boolean isJammed() {
        return this.jammed;
    }

    public void setJammed(boolean jammed) {
        this.jammed = jammed;
        if (!jammed) {
            this.unjamTime = System.currentTimeMillis();
        }
        this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 3);
        this.m_6596_();
    }

    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.jammed = compound.contains("jam") && compound.getBoolean("jam");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        if (this.isJammed()) {
            compound.putBoolean("jam", true);
        }
    }
}