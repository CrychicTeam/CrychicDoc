package noppes.npcs.schematics;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class Blueprint implements ISchematic {

    private List<String> requiredMods;

    private short sizeX;

    private short sizeY;

    private short sizeZ;

    private short palleteSize;

    private BlockState[] pallete;

    private String name;

    private String[] architects;

    private short[][][] structure;

    private CompoundTag[] tileEntities;

    public Blueprint(short sizeX, short sizeY, short sizeZ, short palleteSize, BlockState[] pallete, short[][][] structure, CompoundTag[] tileEntities, List<String> requiredMods) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.palleteSize = palleteSize;
        this.pallete = pallete;
        this.structure = structure;
        this.tileEntities = tileEntities;
        this.requiredMods = requiredMods;
    }

    public void build(Level level, BlockPos pos) {
        BlockState[] pallete = this.getPallete();
        short[][][] structure = this.getStructure();
        for (short y = 0; y < this.getSizeY(); y++) {
            for (short z = 0; z < this.getSizeZ(); z++) {
                for (short x = 0; x < this.getSizeX(); x++) {
                    BlockState state = pallete[structure[y][z][x] & '\uffff'];
                    if (state.m_60734_() != Blocks.STRUCTURE_VOID && state.m_60838_(EmptyBlockGetter.INSTANCE, BlockPos.ZERO)) {
                        level.setBlock(pos.offset(x, y, z), state, 2);
                    }
                }
            }
        }
        for (short y = 0; y < this.getSizeY(); y++) {
            for (short z = 0; z < this.getSizeZ(); z++) {
                for (short xx = 0; xx < this.getSizeX(); xx++) {
                    BlockState state = pallete[structure[y][z][xx]];
                    if (state.m_60734_() != Blocks.STRUCTURE_VOID && !state.m_60838_(EmptyBlockGetter.INSTANCE, BlockPos.ZERO)) {
                        level.setBlock(pos.offset(xx, y, z), state, 2);
                    }
                }
            }
        }
        if (this.getTileEntities() != null) {
            for (CompoundTag tag : this.getTileEntities()) {
                BlockEntity te = level.getBlockEntity(pos.offset(tag.getShort("x"), tag.getShort("y"), tag.getShort("z")));
                tag.putInt("x", pos.m_123341_() + tag.getShort("x"));
                tag.putInt("y", pos.m_123342_() + tag.getShort("y"));
                tag.putInt("z", pos.m_123343_() + tag.getShort("z"));
                te.deserializeNBT(tag);
            }
        }
    }

    public short getSizeX() {
        return this.sizeX;
    }

    public short getSizeY() {
        return this.sizeY;
    }

    public short getSizeZ() {
        return this.sizeZ;
    }

    public short getPalleteSize() {
        return this.palleteSize;
    }

    public BlockState[] getPallete() {
        return this.pallete;
    }

    public short[][][] getStructure() {
        return this.structure;
    }

    public CompoundTag[] getTileEntities() {
        return this.tileEntities;
    }

    public List<String> getRequiredMods() {
        return this.requiredMods;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getArchitects() {
        return this.architects;
    }

    public void setArchitects(String[] architects) {
        this.architects = architects;
    }

    @Override
    public short getWidth() {
        return this.getSizeX();
    }

    @Override
    public short getHeight() {
        return this.getSizeZ();
    }

    @Override
    public short getLength() {
        return this.getSizeY();
    }

    @Override
    public int getBlockEntityDimensions() {
        return this.tileEntities.length;
    }

    @Override
    public CompoundTag getBlockEntity(int i) {
        return this.tileEntities[i];
    }

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        return this.pallete[this.structure[y][z][x]];
    }

    @Override
    public BlockState getBlockState(int i) {
        int x = i % this.getWidth();
        int z = (i - x) / this.getWidth() % this.getLength();
        int y = ((i - x) / this.getWidth() - z) / this.getLength();
        return this.getBlockState(x, y, z);
    }

    @Override
    public CompoundTag getNBT() {
        return BlueprintUtil.writeBlueprintToNBT(this);
    }
}