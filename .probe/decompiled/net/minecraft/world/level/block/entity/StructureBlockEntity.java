package net.minecraft.world.level.block.entity;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.ResourceLocationException;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class StructureBlockEntity extends BlockEntity {

    private static final int SCAN_CORNER_BLOCKS_RANGE = 5;

    public static final int MAX_OFFSET_PER_AXIS = 48;

    public static final int MAX_SIZE_PER_AXIS = 48;

    public static final String AUTHOR_TAG = "author";

    private ResourceLocation structureName;

    private String author = "";

    private String metaData = "";

    private BlockPos structurePos = new BlockPos(0, 1, 0);

    private Vec3i structureSize = Vec3i.ZERO;

    private Mirror mirror = Mirror.NONE;

    private Rotation rotation = Rotation.NONE;

    private StructureMode mode;

    private boolean ignoreEntities = true;

    private boolean powered;

    private boolean showAir;

    private boolean showBoundingBox = true;

    private float integrity = 1.0F;

    private long seed;

    public StructureBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.STRUCTURE_BLOCK, blockPos0, blockState1);
        this.mode = (StructureMode) blockState1.m_61143_(StructureBlock.MODE);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.saveAdditional(compoundTag0);
        compoundTag0.putString("name", this.getStructureName());
        compoundTag0.putString("author", this.author);
        compoundTag0.putString("metadata", this.metaData);
        compoundTag0.putInt("posX", this.structurePos.m_123341_());
        compoundTag0.putInt("posY", this.structurePos.m_123342_());
        compoundTag0.putInt("posZ", this.structurePos.m_123343_());
        compoundTag0.putInt("sizeX", this.structureSize.getX());
        compoundTag0.putInt("sizeY", this.structureSize.getY());
        compoundTag0.putInt("sizeZ", this.structureSize.getZ());
        compoundTag0.putString("rotation", this.rotation.toString());
        compoundTag0.putString("mirror", this.mirror.toString());
        compoundTag0.putString("mode", this.mode.toString());
        compoundTag0.putBoolean("ignoreEntities", this.ignoreEntities);
        compoundTag0.putBoolean("powered", this.powered);
        compoundTag0.putBoolean("showair", this.showAir);
        compoundTag0.putBoolean("showboundingbox", this.showBoundingBox);
        compoundTag0.putFloat("integrity", this.integrity);
        compoundTag0.putLong("seed", this.seed);
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.load(compoundTag0);
        this.setStructureName(compoundTag0.getString("name"));
        this.author = compoundTag0.getString("author");
        this.metaData = compoundTag0.getString("metadata");
        int $$1 = Mth.clamp(compoundTag0.getInt("posX"), -48, 48);
        int $$2 = Mth.clamp(compoundTag0.getInt("posY"), -48, 48);
        int $$3 = Mth.clamp(compoundTag0.getInt("posZ"), -48, 48);
        this.structurePos = new BlockPos($$1, $$2, $$3);
        int $$4 = Mth.clamp(compoundTag0.getInt("sizeX"), 0, 48);
        int $$5 = Mth.clamp(compoundTag0.getInt("sizeY"), 0, 48);
        int $$6 = Mth.clamp(compoundTag0.getInt("sizeZ"), 0, 48);
        this.structureSize = new Vec3i($$4, $$5, $$6);
        try {
            this.rotation = Rotation.valueOf(compoundTag0.getString("rotation"));
        } catch (IllegalArgumentException var11) {
            this.rotation = Rotation.NONE;
        }
        try {
            this.mirror = Mirror.valueOf(compoundTag0.getString("mirror"));
        } catch (IllegalArgumentException var10) {
            this.mirror = Mirror.NONE;
        }
        try {
            this.mode = StructureMode.valueOf(compoundTag0.getString("mode"));
        } catch (IllegalArgumentException var9) {
            this.mode = StructureMode.DATA;
        }
        this.ignoreEntities = compoundTag0.getBoolean("ignoreEntities");
        this.powered = compoundTag0.getBoolean("powered");
        this.showAir = compoundTag0.getBoolean("showair");
        this.showBoundingBox = compoundTag0.getBoolean("showboundingbox");
        if (compoundTag0.contains("integrity")) {
            this.integrity = compoundTag0.getFloat("integrity");
        } else {
            this.integrity = 1.0F;
        }
        this.seed = compoundTag0.getLong("seed");
        this.updateBlockState();
    }

    private void updateBlockState() {
        if (this.f_58857_ != null) {
            BlockPos $$0 = this.m_58899_();
            BlockState $$1 = this.f_58857_.getBlockState($$0);
            if ($$1.m_60713_(Blocks.STRUCTURE_BLOCK)) {
                this.f_58857_.setBlock($$0, (BlockState) $$1.m_61124_(StructureBlock.MODE, this.mode), 2);
            }
        }
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public boolean usedBy(Player player0) {
        if (!player0.canUseGameMasterBlocks()) {
            return false;
        } else {
            if (player0.m_20193_().isClientSide) {
                player0.openStructureBlock(this);
            }
            return true;
        }
    }

    public String getStructureName() {
        return this.structureName == null ? "" : this.structureName.toString();
    }

    public String getStructurePath() {
        return this.structureName == null ? "" : this.structureName.getPath();
    }

    public boolean hasStructureName() {
        return this.structureName != null;
    }

    public void setStructureName(@Nullable String string0) {
        this.setStructureName(StringUtil.isNullOrEmpty(string0) ? null : ResourceLocation.tryParse(string0));
    }

    public void setStructureName(@Nullable ResourceLocation resourceLocation0) {
        this.structureName = resourceLocation0;
    }

    public void createdBy(LivingEntity livingEntity0) {
        this.author = livingEntity0.m_7755_().getString();
    }

    public BlockPos getStructurePos() {
        return this.structurePos;
    }

    public void setStructurePos(BlockPos blockPos0) {
        this.structurePos = blockPos0;
    }

    public Vec3i getStructureSize() {
        return this.structureSize;
    }

    public void setStructureSize(Vec3i vecI0) {
        this.structureSize = vecI0;
    }

    public Mirror getMirror() {
        return this.mirror;
    }

    public void setMirror(Mirror mirror0) {
        this.mirror = mirror0;
    }

    public Rotation getRotation() {
        return this.rotation;
    }

    public void setRotation(Rotation rotation0) {
        this.rotation = rotation0;
    }

    public String getMetaData() {
        return this.metaData;
    }

    public void setMetaData(String string0) {
        this.metaData = string0;
    }

    public StructureMode getMode() {
        return this.mode;
    }

    public void setMode(StructureMode structureMode0) {
        this.mode = structureMode0;
        BlockState $$1 = this.f_58857_.getBlockState(this.m_58899_());
        if ($$1.m_60713_(Blocks.STRUCTURE_BLOCK)) {
            this.f_58857_.setBlock(this.m_58899_(), (BlockState) $$1.m_61124_(StructureBlock.MODE, structureMode0), 2);
        }
    }

    public boolean isIgnoreEntities() {
        return this.ignoreEntities;
    }

    public void setIgnoreEntities(boolean boolean0) {
        this.ignoreEntities = boolean0;
    }

    public float getIntegrity() {
        return this.integrity;
    }

    public void setIntegrity(float float0) {
        this.integrity = float0;
    }

    public long getSeed() {
        return this.seed;
    }

    public void setSeed(long long0) {
        this.seed = long0;
    }

    public boolean detectSize() {
        if (this.mode != StructureMode.SAVE) {
            return false;
        } else {
            BlockPos $$0 = this.m_58899_();
            int $$1 = 80;
            BlockPos $$2 = new BlockPos($$0.m_123341_() - 80, this.f_58857_.m_141937_(), $$0.m_123343_() - 80);
            BlockPos $$3 = new BlockPos($$0.m_123341_() + 80, this.f_58857_.m_151558_() - 1, $$0.m_123343_() + 80);
            Stream<BlockPos> $$4 = this.getRelatedCorners($$2, $$3);
            return calculateEnclosingBoundingBox($$0, $$4).filter(p_155790_ -> {
                int $$2x = p_155790_.maxX() - p_155790_.minX();
                int $$3x = p_155790_.maxY() - p_155790_.minY();
                int $$4x = p_155790_.maxZ() - p_155790_.minZ();
                if ($$2x > 1 && $$3x > 1 && $$4x > 1) {
                    this.structurePos = new BlockPos(p_155790_.minX() - $$0.m_123341_() + 1, p_155790_.minY() - $$0.m_123342_() + 1, p_155790_.minZ() - $$0.m_123343_() + 1);
                    this.structureSize = new Vec3i($$2x - 1, $$3x - 1, $$4x - 1);
                    this.m_6596_();
                    BlockState $$5 = this.f_58857_.getBlockState($$0);
                    this.f_58857_.sendBlockUpdated($$0, $$5, $$5, 3);
                    return true;
                } else {
                    return false;
                }
            }).isPresent();
        }
    }

    private Stream<BlockPos> getRelatedCorners(BlockPos blockPos0, BlockPos blockPos1) {
        return BlockPos.betweenClosedStream(blockPos0, blockPos1).filter(p_272561_ -> this.f_58857_.getBlockState(p_272561_).m_60713_(Blocks.STRUCTURE_BLOCK)).map(this.f_58857_::m_7702_).filter(p_155802_ -> p_155802_ instanceof StructureBlockEntity).map(p_155785_ -> (StructureBlockEntity) p_155785_).filter(p_155787_ -> p_155787_.mode == StructureMode.CORNER && Objects.equals(this.structureName, p_155787_.structureName)).map(BlockEntity::m_58899_);
    }

    private static Optional<BoundingBox> calculateEnclosingBoundingBox(BlockPos blockPos0, Stream<BlockPos> streamBlockPos1) {
        Iterator<BlockPos> $$2 = streamBlockPos1.iterator();
        if (!$$2.hasNext()) {
            return Optional.empty();
        } else {
            BlockPos $$3 = (BlockPos) $$2.next();
            BoundingBox $$4 = new BoundingBox($$3);
            if ($$2.hasNext()) {
                $$2.forEachRemaining($$4::m_162371_);
            } else {
                $$4.encapsulate(blockPos0);
            }
            return Optional.of($$4);
        }
    }

    public boolean saveStructure() {
        return this.saveStructure(true);
    }

    public boolean saveStructure(boolean boolean0) {
        if (this.mode == StructureMode.SAVE && !this.f_58857_.isClientSide && this.structureName != null) {
            BlockPos $$1 = this.m_58899_().offset(this.structurePos);
            ServerLevel $$2 = (ServerLevel) this.f_58857_;
            StructureTemplateManager $$3 = $$2.getStructureManager();
            StructureTemplate $$4;
            try {
                $$4 = $$3.getOrCreate(this.structureName);
            } catch (ResourceLocationException var8) {
                return false;
            }
            $$4.fillFromWorld(this.f_58857_, $$1, this.structureSize, !this.ignoreEntities, Blocks.STRUCTURE_VOID);
            $$4.setAuthor(this.author);
            if (boolean0) {
                try {
                    return $$3.save(this.structureName);
                } catch (ResourceLocationException var7) {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean loadStructure(ServerLevel serverLevel0) {
        return this.loadStructure(serverLevel0, true);
    }

    public static RandomSource createRandom(long long0) {
        return long0 == 0L ? RandomSource.create(Util.getMillis()) : RandomSource.create(long0);
    }

    public boolean loadStructure(ServerLevel serverLevel0, boolean boolean1) {
        if (this.mode == StructureMode.LOAD && this.structureName != null) {
            StructureTemplateManager $$2 = serverLevel0.getStructureManager();
            Optional<StructureTemplate> $$3;
            try {
                $$3 = $$2.get(this.structureName);
            } catch (ResourceLocationException var6) {
                return false;
            }
            return !$$3.isPresent() ? false : this.loadStructure(serverLevel0, boolean1, (StructureTemplate) $$3.get());
        } else {
            return false;
        }
    }

    public boolean loadStructure(ServerLevel serverLevel0, boolean boolean1, StructureTemplate structureTemplate2) {
        BlockPos $$3 = this.m_58899_();
        if (!StringUtil.isNullOrEmpty(structureTemplate2.getAuthor())) {
            this.author = structureTemplate2.getAuthor();
        }
        Vec3i $$4 = structureTemplate2.getSize();
        boolean $$5 = this.structureSize.equals($$4);
        if (!$$5) {
            this.structureSize = $$4;
            this.m_6596_();
            BlockState $$6 = serverLevel0.m_8055_($$3);
            serverLevel0.sendBlockUpdated($$3, $$6, $$6, 3);
        }
        if (boolean1 && !$$5) {
            return false;
        } else {
            StructurePlaceSettings $$7 = new StructurePlaceSettings().setMirror(this.mirror).setRotation(this.rotation).setIgnoreEntities(this.ignoreEntities);
            if (this.integrity < 1.0F) {
                $$7.clearProcessors().addProcessor(new BlockRotProcessor(Mth.clamp(this.integrity, 0.0F, 1.0F))).setRandom(createRandom(this.seed));
            }
            BlockPos $$8 = $$3.offset(this.structurePos);
            structureTemplate2.placeInWorld(serverLevel0, $$8, $$8, $$7, createRandom(this.seed), 2);
            return true;
        }
    }

    public void unloadStructure() {
        if (this.structureName != null) {
            ServerLevel $$0 = (ServerLevel) this.f_58857_;
            StructureTemplateManager $$1 = $$0.getStructureManager();
            $$1.remove(this.structureName);
        }
    }

    public boolean isStructureLoadable() {
        if (this.mode == StructureMode.LOAD && !this.f_58857_.isClientSide && this.structureName != null) {
            ServerLevel $$0 = (ServerLevel) this.f_58857_;
            StructureTemplateManager $$1 = $$0.getStructureManager();
            try {
                return $$1.get(this.structureName).isPresent();
            } catch (ResourceLocationException var4) {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isPowered() {
        return this.powered;
    }

    public void setPowered(boolean boolean0) {
        this.powered = boolean0;
    }

    public boolean getShowAir() {
        return this.showAir;
    }

    public void setShowAir(boolean boolean0) {
        this.showAir = boolean0;
    }

    public boolean getShowBoundingBox() {
        return this.showBoundingBox;
    }

    public void setShowBoundingBox(boolean boolean0) {
        this.showBoundingBox = boolean0;
    }

    public static enum UpdateType {

        UPDATE_DATA, SAVE_AREA, LOAD_AREA, SCAN_AREA
    }
}