package net.mehvahdjukaar.amendments.common.tile;

import com.mojang.datafixers.util.Pair;
import java.util.HashMap;
import java.util.Map;
import net.mehvahdjukaar.amendments.common.block.CarpetSlabBlock;
import net.mehvahdjukaar.amendments.reg.ModBlockProperties;
import net.mehvahdjukaar.amendments.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.block.MimicBlockTile;
import net.mehvahdjukaar.moonlight.api.client.model.ExtraModelData;
import net.mehvahdjukaar.moonlight.api.client.model.ModelDataKey;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CarpetedBlockTile extends MimicBlockTile {

    private static final Map<Pair<SoundType, SoundType>, SoundType> MIXED_SOUND_MAP = new HashMap();

    public static final ModelDataKey<BlockState> CARPET_KEY = new ModelDataKey<>(BlockState.class);

    private BlockState carpet = Blocks.WHITE_CARPET.defaultBlockState();

    private SoundType soundType = null;

    public CarpetedBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.CARPET_STAIRS_TILE.get(), pos, state);
    }

    @Override
    public void addExtraModelData(ExtraModelData.Builder builder) {
        super.addExtraModelData(builder);
        builder.with(CARPET_KEY, this.carpet);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        HolderGetter<Block> holderGetter = (HolderGetter<Block>) (this.f_58857_ != null ? this.f_58857_.m_246945_(Registries.BLOCK) : BuiltInRegistries.BLOCK.m_255303_());
        this.setCarpet(NbtUtils.readBlockState(holderGetter, compound.getCompound("Carpet")));
    }

    public void setCarpet(BlockState carpet) {
        this.setHeldBlock(carpet, 1);
    }

    public BlockState getCarpet() {
        return this.getHeldBlock(1);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Carpet", NbtUtils.writeBlockState(this.carpet));
    }

    @Override
    public BlockState getHeldBlock(int index) {
        return index == 1 ? this.carpet : super.getHeldBlock(index);
    }

    @Override
    public boolean setHeldBlock(BlockState state, int index) {
        if (this.f_58857_ instanceof ServerLevel) {
            this.m_6596_();
            int newLight = Math.max(ForgeHelper.getLightEmission(this.getCarpet(), this.f_58857_, this.f_58858_), ForgeHelper.getLightEmission(this.getHeldBlock(), this.f_58857_, this.f_58858_));
            this.f_58857_.setBlock(this.f_58858_, (BlockState) ((BlockState) this.m_58900_().m_61124_(ModBlockProperties.LIGHT_LEVEL, newLight)).m_61124_(CarpetSlabBlock.SOLID, this.getHeldBlock().m_60815_()), 3);
            this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 2);
        } else {
            this.requestModelReload();
        }
        if (index == 0) {
            this.mimic = state;
            return true;
        } else if (index == 1) {
            this.carpet = state;
            return true;
        } else {
            this.soundType = null;
            return false;
        }
    }

    public void initialize(BlockState stairs, BlockState carpet) {
        this.setHeldBlock(carpet, 1);
        this.setHeldBlock(stairs, 0);
    }

    @Nullable
    public SoundType getSoundType() {
        if (this.soundType == null) {
            BlockState stairs = this.getHeldBlock();
            BlockState carpet = this.getHeldBlock(1);
            if (stairs.m_60795_() || carpet.m_60795_()) {
                return null;
            }
            SoundType stairsSound = stairs.m_60827_();
            SoundType carpetSound = carpet.m_60827_();
            this.soundType = (SoundType) MIXED_SOUND_MAP.computeIfAbsent(Pair.of(stairsSound, carpetSound), p -> new SoundType(1.0F, 1.0F, stairsSound.getBreakSound(), carpetSound.getStepSound(), stairsSound.getPlaceSound(), stairsSound.getHitSound(), carpetSound.getFallSound()));
        }
        return this.soundType;
    }
}