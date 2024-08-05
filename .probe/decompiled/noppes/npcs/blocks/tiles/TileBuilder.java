package noppes.npcs.blocks.tiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.CustomBlocks;
import noppes.npcs.NBTTags;
import noppes.npcs.controllers.SchematicController;
import noppes.npcs.controllers.data.Availability;
import noppes.npcs.controllers.data.BlockData;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.JobBuilder;
import noppes.npcs.schematics.SchematicWrapper;

public class TileBuilder extends BlockEntity {

    private SchematicWrapper schematic = null;

    public int rotation = 0;

    public int yOffest = 0;

    public boolean enabled = false;

    public boolean started = false;

    public boolean finished = false;

    public Availability availability = new Availability();

    private Stack<Integer> positions = new Stack();

    private Stack<Integer> positionsSecond = new Stack();

    public static BlockPos DrawPos = null;

    public static boolean Compiled = false;

    private int ticks = 20;

    public TileBuilder(BlockPos pos, BlockState state) {
        super(CustomBlocks.tile_builder, pos, state);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (compound.contains("SchematicName")) {
            this.schematic = SchematicController.Instance.load(compound.getString("SchematicName"));
        }
        Stack<Integer> positions = new Stack();
        positions.addAll(NBTTags.getIntegerList(compound.getList("Positions", 10)));
        this.positions = positions;
        positions = new Stack();
        positions.addAll(NBTTags.getIntegerList(compound.getList("PositionsSecond", 10)));
        this.positionsSecond = positions;
        this.readPartNBT(compound);
    }

    public void readPartNBT(CompoundTag compound) {
        this.rotation = compound.getInt("Rotation");
        this.yOffest = compound.getInt("YOffset");
        this.enabled = compound.getBoolean("Enabled");
        this.started = compound.getBoolean("Started");
        this.finished = compound.getBoolean("Finished");
        this.availability.load(compound.getCompound("Availability"));
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        if (this.schematic != null) {
            compound.putString("SchematicName", this.schematic.schema.getName());
        }
        compound.put("Positions", NBTTags.nbtIntegerCollection(new ArrayList(this.positions)));
        compound.put("PositionsSecond", NBTTags.nbtIntegerCollection(new ArrayList(this.positionsSecond)));
        this.writePartNBT(compound);
    }

    public CompoundTag writePartNBT(CompoundTag compound) {
        compound.putInt("Rotation", this.rotation);
        compound.putInt("YOffset", this.yOffest);
        compound.putBoolean("Enabled", this.enabled);
        compound.putBoolean("Started", this.started);
        compound.putBoolean("Finished", this.finished);
        compound.put("Availability", this.availability.save(new CompoundTag()));
        return compound;
    }

    @OnlyIn(Dist.CLIENT)
    public void setDrawSchematic(SchematicWrapper schematics) {
        this.schematic = schematics;
    }

    public void setSchematic(SchematicWrapper schematics) {
        this.schematic = schematics;
        if (schematics == null) {
            this.positions.clear();
            this.positionsSecond.clear();
        } else {
            Stack<Integer> positions = new Stack();
            for (int y = 0; y < schematics.schema.getHeight(); y++) {
                for (int z = 0; z < schematics.schema.getLength() / 2; z++) {
                    for (int x = 0; x < schematics.schema.getWidth() / 2; x++) {
                        positions.add(0, this.xyzToIndex(x, y, z));
                    }
                }
                for (int z = 0; z < schematics.schema.getLength() / 2; z++) {
                    for (int x = schematics.schema.getWidth() / 2; x < schematics.schema.getWidth(); x++) {
                        positions.add(0, this.xyzToIndex(x, y, z));
                    }
                }
                for (int z = schematics.schema.getLength() / 2; z < schematics.schema.getLength(); z++) {
                    for (int x = 0; x < schematics.schema.getWidth() / 2; x++) {
                        positions.add(0, this.xyzToIndex(x, y, z));
                    }
                }
                for (int z = schematics.schema.getLength() / 2; z < schematics.schema.getLength(); z++) {
                    for (int x = schematics.schema.getWidth() / 2; x < schematics.schema.getWidth(); x++) {
                        positions.add(0, this.xyzToIndex(x, y, z));
                    }
                }
            }
            this.positions = positions;
            this.positionsSecond.clear();
        }
    }

    public int xyzToIndex(int x, int y, int z) {
        return (y * this.schematic.schema.getLength() + z) * this.schematic.schema.getWidth() + x;
    }

    public SchematicWrapper getSchematic() {
        return this.schematic;
    }

    public boolean hasSchematic() {
        return this.schematic != null;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TileBuilder tile) {
        if (!level.isClientSide && tile.hasSchematic() && !tile.finished) {
            tile.ticks--;
            if (tile.ticks <= 0) {
                tile.ticks = 200;
                if (tile.positions.isEmpty() && tile.positionsSecond.isEmpty()) {
                    tile.finished = true;
                } else {
                    if (!tile.started) {
                        for (Player player : tile.getPlayerList()) {
                            if (tile.availability.isAvailable(player)) {
                                tile.started = true;
                                break;
                            }
                        }
                        if (!tile.started) {
                            return;
                        }
                    }
                    for (EntityNPCInterface npc : level.m_45976_(EntityNPCInterface.class, new AABB(pos, pos).inflate(32.0, 32.0, 32.0))) {
                        if (npc.job.getType() == 10) {
                            JobBuilder job = (JobBuilder) npc.job;
                            if (job.build == null) {
                                job.build = tile;
                            }
                        }
                    }
                }
            }
        }
    }

    private List<Player> getPlayerList() {
        return this.f_58857_.m_45976_(Player.class, new AABB((double) this.f_58858_.m_123341_(), (double) this.f_58858_.m_123342_(), (double) this.f_58858_.m_123343_(), (double) (this.f_58858_.m_123341_() + 1), (double) (this.f_58858_.m_123342_() + 1), (double) (this.f_58858_.m_123343_() + 1)).inflate(10.0, 10.0, 10.0));
    }

    public Stack<BlockData> getBlock() {
        if (this.enabled && !this.finished && this.hasSchematic()) {
            boolean bo = this.positions.isEmpty();
            Stack<BlockData> list = new Stack();
            int size = this.schematic.schema.getWidth() * this.schematic.schema.getLength() / 4;
            if (size > 30) {
                size = 30;
            }
            for (int i = 0; i < size; i++) {
                if (this.positions.isEmpty() && !bo || this.positionsSecond.isEmpty() && bo) {
                    return list;
                }
                int pos = bo ? (Integer) this.positionsSecond.pop() : (Integer) this.positions.pop();
                if (pos < this.schematic.size) {
                    int x = pos % this.schematic.schema.getWidth();
                    int z = (pos - x) / this.schematic.schema.getWidth() % this.schematic.schema.getLength();
                    int y = ((pos - x) / this.schematic.schema.getWidth() - z) / this.schematic.schema.getLength();
                    BlockState state = this.schematic.schema.getBlockState(x, y, z);
                    if (!state.m_60838_(EmptyBlockGetter.INSTANCE, BlockPos.ZERO) && !bo && state.m_60734_() != Blocks.AIR) {
                        this.positionsSecond.add(0, pos);
                    } else {
                        BlockPos blockPos = this.m_58899_().offset(1, this.yOffest, 1).offset(this.schematic.rotatePos(x, y, z, this.rotation));
                        BlockState original = this.f_58857_.getBlockState(blockPos);
                        if (Block.getId(state) != Block.getId(original)) {
                            state = this.schematic.rotationState(state, this.rotation);
                            CompoundTag tile = null;
                            if (state.m_60734_() instanceof EntityBlock) {
                                tile = this.schematic.getBlockEntity(x, y, z, blockPos);
                            }
                            list.add(0, new BlockData(blockPos, state, tile));
                        }
                    }
                }
            }
            return list;
        } else {
            return null;
        }
    }

    public AABB getRenderBoundingBox() {
        return this.schematic == null ? super.getRenderBoundingBox() : new AABB((double) this.f_58858_.m_123341_(), (double) this.f_58858_.m_123342_(), (double) this.f_58858_.m_123343_(), (double) (this.f_58858_.m_123341_() + this.schematic.schema.getWidth() + 1), (double) (this.f_58858_.m_123342_() + this.schematic.schema.getHeight() + 1), (double) (this.f_58858_.m_123343_() + this.schematic.schema.getLength() + 1));
    }

    public static void SetDrawPos(BlockPos pos) {
        DrawPos = pos;
        Compiled = false;
    }
}