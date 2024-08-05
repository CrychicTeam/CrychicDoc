package noppes.npcs.schematics;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class SchematicWrapper {

    public static final int buildSize = 10000;

    private BlockPos offset = BlockPos.ZERO;

    private BlockPos start = BlockPos.ZERO;

    public ISchematic schema;

    public int buildPos;

    public int size;

    public int rotation = 0;

    private Level level;

    public boolean isBuilding = false;

    public boolean firstLayer = true;

    private Map<ChunkPos, CompoundTag>[] tileEntities;

    public SchematicWrapper(ISchematic schematic) {
        this.schema = schematic;
        this.size = schematic.getWidth() * schematic.getHeight() * schematic.getLength();
        this.tileEntities = new Map[schematic.getHeight()];
        for (int i = 0; i < schematic.getBlockEntityDimensions(); i++) {
            CompoundTag teTag = schematic.getBlockEntity(i);
            int x = teTag.getInt("x");
            int y = teTag.getInt("y");
            int z = teTag.getInt("z");
            Map<ChunkPos, CompoundTag> map = this.tileEntities[y];
            if (map == null) {
                this.tileEntities[y] = map = new HashMap();
            }
            map.put(new ChunkPos(x, z), teTag);
        }
    }

    public void load(Schematic s) {
    }

    public void init(BlockPos pos, Level level, int rotation) {
        this.start = pos;
        this.level = level;
        this.rotation = rotation;
    }

    public void offset(int x, int y, int z) {
        this.offset = new BlockPos(x, y, z);
    }

    public void build() {
        if (this.level != null && this.isBuilding) {
            long endPos = (long) (this.buildPos + 10000);
            if (endPos > (long) this.size) {
                endPos = (long) this.size;
            }
            for (; (long) this.buildPos < endPos; this.buildPos++) {
                int x = this.buildPos % this.schema.getWidth();
                int z = (this.buildPos - x) / this.schema.getWidth() % this.schema.getLength();
                int y = ((this.buildPos - x) / this.schema.getWidth() - z) / this.schema.getLength();
                if (this.firstLayer) {
                    this.place(x, y, z, 1);
                } else {
                    this.place(x, y, z, 2);
                }
            }
            if (this.buildPos >= this.size) {
                if (this.firstLayer) {
                    this.firstLayer = false;
                    this.buildPos = 0;
                } else {
                    this.isBuilding = false;
                }
            }
        }
    }

    public void place(int x, int y, int z, int flag) {
        BlockState state = this.schema.getBlockState(x, y, z);
        if (state != null && (flag != 1 || state.m_60838_(EmptyBlockGetter.INSTANCE, BlockPos.ZERO) || state.m_60734_() == Blocks.AIR) && (flag != 2 || !state.m_60838_(EmptyBlockGetter.INSTANCE, BlockPos.ZERO) && state.m_60734_() != Blocks.AIR)) {
            int rotation = this.rotation / 90;
            BlockPos pos = this.start.offset(this.rotatePos(x, y, z, rotation));
            state = this.rotationState(state, rotation);
            this.level.setBlock(pos, state, 2);
            if (state.m_60734_() instanceof EntityBlock) {
                BlockEntity tile = this.level.getBlockEntity(pos);
                if (tile != null) {
                    CompoundTag comp = this.getBlockEntity(x, y, z, pos);
                    if (comp != null) {
                        tile.load(comp);
                    }
                }
            }
        }
    }

    public BlockState rotationState(BlockState state, int rotation) {
        if (rotation == 0) {
            return state;
        } else {
            for (Property prop : state.m_61147_()) {
                if (prop instanceof DirectionProperty) {
                    Direction direction = (Direction) state.m_61143_(prop);
                    if (direction != Direction.UP && direction != Direction.DOWN) {
                        for (int i = 0; i < rotation; i++) {
                            direction = direction.getClockWise();
                        }
                        return (BlockState) state.m_61124_(prop, direction);
                    }
                }
            }
            return state;
        }
    }

    public CompoundTag getBlockEntity(int x, int y, int z, BlockPos pos) {
        if (y < this.tileEntities.length && this.tileEntities[y] != null) {
            CompoundTag compound = (CompoundTag) this.tileEntities[y].get(new ChunkPos(x, z));
            if (compound == null) {
                return null;
            } else {
                compound = compound.copy();
                compound.putInt("x", pos.m_123341_());
                compound.putInt("y", pos.m_123342_());
                compound.putInt("z", pos.m_123343_());
                return compound;
            }
        } else {
            return null;
        }
    }

    public CompoundTag getNBTSmall() {
        CompoundTag compound = new CompoundTag();
        compound.putShort("Width", this.schema.getWidth());
        compound.putShort("Height", this.schema.getHeight());
        compound.putShort("Length", this.schema.getLength());
        compound.putString("SchematicName", this.schema.getName());
        ListTag list = new ListTag();
        for (int i = 0; i < this.size && i < 25000; i++) {
            BlockState state = this.schema.getBlockState(i);
            if (state.m_60734_() != Blocks.AIR && state.m_60734_() != Blocks.STRUCTURE_VOID) {
                list.add(NbtUtils.writeBlockState(this.schema.getBlockState(i)));
            } else {
                list.add(new CompoundTag());
            }
        }
        compound.put("Data", list);
        return compound;
    }

    public BlockPos rotatePos(int x, int y, int z, int rotation) {
        if (rotation == 1) {
            return new BlockPos(this.schema.getLength() - z - 1, y, x);
        } else if (rotation == 2) {
            return new BlockPos(this.schema.getWidth() - x - 1, y, this.schema.getLength() - z - 1);
        } else {
            return rotation == 3 ? new BlockPos(z, y, this.schema.getWidth() - x - 1) : new BlockPos(x, y, z);
        }
    }

    public int getPercentage() {
        double l = (double) (this.buildPos + (this.firstLayer ? 0 : this.size));
        return (int) (l / (double) this.size * 50.0);
    }
}