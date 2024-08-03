package noppes.npcs.api.wrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IContainer;
import noppes.npcs.api.INbt;
import noppes.npcs.api.IPos;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.entity.data.IData;
import noppes.npcs.blocks.BlockScripted;
import noppes.npcs.blocks.BlockScriptedDoor;
import noppes.npcs.blocks.tiles.TileNpcEntity;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.mixin.EntityIMixin;
import noppes.npcs.shared.common.util.LRUHashMap;

public class BlockWrapper implements IBlock {

    private static final Map<String, BlockWrapper> blockCache = new LRUHashMap<String, BlockWrapper>(400);

    protected final IWorld level;

    protected final Block block;

    protected final BlockPos pos;

    protected final BlockPosWrapper bPos;

    protected BlockEntity tile;

    protected TileNpcEntity storage;

    private final IData tempdata = new IData() {

        @Override
        public void remove(String key) {
            if (BlockWrapper.this.storage != null) {
                BlockWrapper.this.storage.tempData.remove(key);
            }
        }

        @Override
        public void put(String key, Object value) {
            if (BlockWrapper.this.storage != null) {
                BlockWrapper.this.storage.tempData.put(key, value);
            }
        }

        @Override
        public boolean has(String key) {
            return BlockWrapper.this.storage == null ? false : BlockWrapper.this.storage.tempData.containsKey(key);
        }

        @Override
        public Object get(String key) {
            return BlockWrapper.this.storage == null ? null : BlockWrapper.this.storage.tempData.get(key);
        }

        @Override
        public void clear() {
            if (BlockWrapper.this.storage != null) {
                BlockWrapper.this.storage.tempData.clear();
            }
        }

        @Override
        public String[] getKeys() {
            return (String[]) BlockWrapper.this.storage.tempData.keySet().toArray(new String[BlockWrapper.this.storage.tempData.size()]);
        }
    };

    private final IData storeddata = new IData() {

        @Override
        public void put(String key, Object value) {
            CompoundTag compound = this.getNBT();
            if (compound != null) {
                if (value instanceof Number) {
                    compound.putDouble(key, ((Number) value).doubleValue());
                } else if (value instanceof String) {
                    compound.putString(key, (String) value);
                }
            }
        }

        @Override
        public Object get(String key) {
            CompoundTag compound = this.getNBT();
            if (compound == null) {
                return null;
            } else if (!compound.contains(key)) {
                return null;
            } else {
                Tag base = compound.get(key);
                return base instanceof NumericTag ? ((NumericTag) base).getAsDouble() : base.getAsString();
            }
        }

        @Override
        public void remove(String key) {
            CompoundTag compound = this.getNBT();
            if (compound != null) {
                compound.remove(key);
            }
        }

        @Override
        public boolean has(String key) {
            CompoundTag compound = this.getNBT();
            return compound == null ? false : compound.contains(key);
        }

        @Override
        public void clear() {
            if (BlockWrapper.this.tile != null) {
                BlockWrapper.this.tile.getPersistentData().put("CustomNPCsData", new CompoundTag());
            }
        }

        private CompoundTag getNBT() {
            if (BlockWrapper.this.tile == null) {
                return null;
            } else {
                CompoundTag compound = BlockWrapper.this.tile.getPersistentData().getCompound("CustomNPCsData");
                if (compound.isEmpty() && !BlockWrapper.this.tile.getPersistentData().contains("CustomNPCsData")) {
                    BlockWrapper.this.tile.getPersistentData().put("CustomNPCsData", compound);
                }
                return compound;
            }
        }

        @Override
        public String[] getKeys() {
            CompoundTag compound = this.getNBT();
            return compound == null ? new String[0] : (String[]) compound.getAllKeys().toArray(new String[compound.getAllKeys().size()]);
        }
    };

    protected BlockWrapper(Level level, Block block, BlockPos pos) {
        this.level = NpcAPI.Instance().getIWorld((ServerLevel) level);
        this.block = block;
        this.pos = pos;
        this.bPos = new BlockPosWrapper(pos);
        this.setTile(level.getBlockEntity(pos));
    }

    @Override
    public int getX() {
        return this.pos.m_123341_();
    }

    @Override
    public int getY() {
        return this.pos.m_123342_();
    }

    @Override
    public int getZ() {
        return this.pos.m_123343_();
    }

    @Override
    public IPos getPos() {
        return this.bPos;
    }

    @Override
    public Object getProperty(String name) {
        BlockState state = this.getMCBlockState();
        for (Property p : state.m_61147_()) {
            if (p.getName().equalsIgnoreCase(name)) {
                return state.m_61143_(p);
            }
        }
        throw new CustomNPCsException("Unknown property: " + name);
    }

    @Override
    public void setProperty(String name, Object val) {
        if (!(val instanceof Comparable)) {
            throw new CustomNPCsException("Not a valid property value: " + val);
        } else {
            BlockState state = this.getMCBlockState();
            for (Property<? extends Comparable<?>> p : state.m_61147_()) {
                if (p.getName().equalsIgnoreCase(name)) {
                    this.setPropertyValue(state, p, (Comparable<?>) val);
                    return;
                }
            }
            throw new CustomNPCsException("Unknown property: " + name);
        }
    }

    private <T extends Comparable<T>> void setPropertyValue(BlockState state, Property<T> p, Comparable<?> c) {
        state.m_61124_(p, (Comparable) p.getValueClass().cast(c));
    }

    @Override
    public String[] getProperties() {
        Collection<Property<?>> props = this.getMCBlockState().m_61147_();
        List<String> list = new ArrayList();
        for (Property prop : props) {
            list.add(prop.getName());
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    @Override
    public void remove() {
        this.level.getMCLevel().m_7471_(this.pos, false);
    }

    @Override
    public boolean isRemoved() {
        BlockState state = this.level.getMCLevel().m_8055_(this.pos);
        return state == null ? true : state.m_60734_() != this.block;
    }

    @Override
    public boolean isAir() {
        return this.level.getMCLevel().m_8055_(this.pos).m_60795_();
    }

    public BlockWrapper setBlock(String name) {
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name));
        if (block == null) {
            return this;
        } else {
            this.level.getMCLevel().m_7731_(this.pos, block.defaultBlockState(), 2);
            return new BlockWrapper(this.level.getMCLevel(), block, this.pos);
        }
    }

    public BlockWrapper setBlock(IBlock block) {
        this.level.getMCLevel().m_7731_(this.pos, block.getMCBlock().defaultBlockState(), 2);
        return new BlockWrapper(this.level.getMCLevel(), block.getMCBlock(), this.pos);
    }

    @Override
    public boolean isContainer() {
        return this.tile != null && this.tile instanceof Container ? ((Container) this.tile).getContainerSize() > 0 : false;
    }

    @Override
    public IContainer getContainer() {
        if (!this.isContainer()) {
            throw new CustomNPCsException("This block is not a container");
        } else {
            return NpcAPI.Instance().getIContainer((Container) this.tile);
        }
    }

    @Override
    public IData getTempdata() {
        return this.tempdata;
    }

    @Override
    public IData getStoreddata() {
        return this.storeddata;
    }

    @Override
    public String getName() {
        return ForgeRegistries.BLOCKS.getKey(this.block).toString();
    }

    @Override
    public String getDisplayName() {
        return this.tile != null && this.tile instanceof Nameable ? ((Nameable) this.tile).getDisplayName().getString() : this.getName();
    }

    @Override
    public IWorld getWorld() {
        return this.level;
    }

    @Override
    public Block getMCBlock() {
        return this.block;
    }

    @Deprecated
    public static IBlock createNew(Level level, BlockPos pos, BlockState state) {
        Block block = state.m_60734_();
        String key = state.toString() + pos.toString();
        BlockWrapper b = (BlockWrapper) blockCache.get(key);
        if (b != null) {
            b.setTile(level.getBlockEntity(pos));
            return b;
        } else {
            Object var6;
            if (block instanceof BlockScripted) {
                var6 = new BlockScriptedWrapper(level, block, pos);
            } else if (block instanceof BlockScriptedDoor) {
                var6 = new BlockScriptedDoorWrapper(level, block, pos);
            } else if (block instanceof IFluidBlock) {
                var6 = new BlockFluidContainerWrapper(level, block, pos);
            } else {
                var6 = new BlockWrapper(level, block, pos);
            }
            blockCache.put(key, var6);
            return (IBlock) var6;
        }
    }

    public static void clearCache() {
        blockCache.clear();
    }

    @Override
    public boolean hasTileEntity() {
        return this.tile != null;
    }

    protected void setTile(BlockEntity tile) {
        this.tile = tile;
        if (tile instanceof TileNpcEntity) {
            this.storage = (TileNpcEntity) tile;
        }
    }

    @Override
    public INbt getBlockEntityNBT() {
        CompoundTag compound = this.tile.saveWithoutMetadata();
        return NpcAPI.Instance().getINbt(compound);
    }

    @Override
    public void setTileEntityNBT(INbt nbt) {
        this.tile.load(nbt.getMCNBT());
        this.tile.setChanged();
        BlockState state = this.level.getMCLevel().m_8055_(this.pos);
        this.level.getMCLevel().sendBlockUpdated(this.pos, state, state, 3);
    }

    @Override
    public BlockEntity getMCTileEntity() {
        return this.tile;
    }

    @Override
    public BlockState getMCBlockState() {
        return this.level.getMCLevel().m_8055_(this.pos);
    }

    @Override
    public void blockEvent(int type, int data) {
        this.level.getMCLevel().blockEvent(this.pos, this.getMCBlock(), type, data);
    }

    @Override
    public void interact(int side) {
        Player player = EntityNPCInterface.GenericPlayer;
        Level w = this.level.getMCLevel();
        ((EntityIMixin) player).setLevel(w);
        player.m_6034_((double) this.pos.m_123341_(), (double) this.pos.m_123342_(), (double) this.pos.m_123343_());
        this.getMCBlockState().m_60664_(w, EntityNPCInterface.CommandPlayer, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.ZERO, Direction.from3DDataValue(side), this.pos, true));
    }
}