package journeymap.client.model;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import journeymap.client.JourneymapClient;
import journeymap.client.data.DataCache;
import journeymap.client.mod.IBlockColorProxy;
import journeymap.client.mod.IBlockSpritesProxy;
import journeymap.client.mod.ModBlockDelegate;
import journeymap.client.world.JmBlockAccess;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.Logger;

public class BlockMD implements Comparable<BlockMD> {

    public static final EnumSet<BlockFlag> FlagsPlantAndCrop = EnumSet.of(BlockFlag.Plant, BlockFlag.Crop);

    public static final EnumSet<BlockFlag> FlagsNormal = EnumSet.complementOf(EnumSet.of(BlockFlag.Error, BlockFlag.Ignore));

    public static final BlockMD AIRBLOCK = new BlockMD(Blocks.AIR.defaultBlockState(), "minecraft:air", "0", "Air", 0.0F, EnumSet.of(BlockFlag.Ignore), false);

    public static final BlockMD VOIDBLOCK = new BlockMD(Blocks.AIR.defaultBlockState(), "journeymap:void", "0", "Void", 0.0F, EnumSet.of(BlockFlag.Ignore), false);

    private static Logger LOGGER = Journeymap.getLogger();

    private final BlockState blockState;

    private final String blockId;

    private final String blockStateId;

    private final String name;

    private EnumSet<BlockFlag> flags;

    private Integer color;

    private float alpha;

    private IBlockSpritesProxy blockSpritesProxy;

    private IBlockColorProxy blockColorProxy;

    private boolean noShadow;

    private boolean isIgnore;

    private boolean isWater;

    private boolean isLava;

    private boolean isFluid;

    private boolean isFire;

    private boolean isIce;

    private boolean isFoliage;

    private boolean isGrass;

    private boolean isPlantOrCrop;

    private boolean isError;

    private BlockMD(@Nonnull BlockState blockState) {
        this(blockState, getBlockId(blockState), getBlockStateId(blockState), getBlockName(blockState));
    }

    private BlockMD(@Nonnull BlockState blockState, String blockId, String blockStateId, String name) {
        this(blockState, blockId, blockStateId, name, 1.0F, EnumSet.noneOf(BlockFlag.class), true);
    }

    private BlockMD(@Nonnull BlockState blockState, String blockId, String blockStateId, String name, Float alpha, EnumSet<BlockFlag> flags, boolean initDelegates) {
        this.blockState = blockState;
        this.blockId = blockId;
        this.blockStateId = blockStateId;
        this.name = name;
        this.alpha = alpha;
        this.flags = flags;
        if (initDelegates) {
            ModBlockDelegate.INSTANCE.initialize(this);
        }
        this.updateProperties();
    }

    public Set<BlockMD> getValidStateMDs() {
        return (Set<BlockMD>) this.getBlock().getStateDefinition().getPossibleStates().stream().map(BlockMD::get).collect(Collectors.toSet());
    }

    private void updateProperties() {
        this.isIgnore = this.blockState == null || this.hasFlag(BlockFlag.Ignore) && !this.hasFlag(BlockFlag.Force) || this.blockState.m_60734_() instanceof AirBlock && !this.hasFlag(BlockFlag.Force) || this.blockState.m_60799_() == RenderShape.INVISIBLE && !(this.blockState.m_60734_() instanceof LiquidBlock);
        if (this.isIgnore) {
            this.color = -1;
            this.setAlpha(0.0F);
            this.flags.add(BlockFlag.Ignore);
            this.flags.add(BlockFlag.OpenToSky);
            this.flags.add(BlockFlag.NoShadow);
        }
        if (this.blockState != null) {
            Block block = this.blockState.m_60734_();
            this.isLava = block == Blocks.LAVA || BuiltInRegistries.BLOCK.getKey(block).getPath().equalsIgnoreCase("flowing_lava");
            this.isIce = block == Blocks.ICE;
            this.isFire = block == Blocks.FIRE;
        }
        this.isFluid = this.hasFlag(BlockFlag.Fluid);
        this.isWater = this.hasFlag(BlockFlag.Water);
        this.noShadow = this.hasFlag(BlockFlag.NoShadow);
        this.isFoliage = this.hasFlag(BlockFlag.Foliage);
        this.isGrass = this.hasFlag(BlockFlag.Grass);
        this.isPlantOrCrop = this.hasAnyFlag(FlagsPlantAndCrop);
        this.isError = this.hasFlag(BlockFlag.Error);
    }

    public Block getBlock() {
        return this.blockState.m_60734_();
    }

    public static void reset() {
        DataCache.INSTANCE.resetBlockMetadata();
    }

    public static Set<BlockMD> getAll() {
        return (Set<BlockMD>) StreamSupport.stream(Block.BLOCK_STATE_REGISTRY.spliterator(), false).map(BlockMD::get).collect(Collectors.toSet());
    }

    public static Set<BlockMD> getAllValid() {
        return (Set<BlockMD>) getAll().stream().filter(blockMD -> !blockMD.isIgnore() && !blockMD.hasFlag(BlockFlag.Error)).collect(Collectors.toSet());
    }

    public static Set<BlockMD> getAllMinecraft() {
        return (Set<BlockMD>) StreamSupport.stream(Block.BLOCK_STATE_REGISTRY.spliterator(), false).filter(blockState1 -> BuiltInRegistries.BLOCK.getKey(blockState1.m_60734_()).getNamespace().equals("minecraft")).map(BlockMD::get).collect(Collectors.toSet());
    }

    public static BlockMD getBlockMDFromChunkLocal(ChunkMD chunkMd, int localX, int y, int localZ) {
        return getBlockMD(chunkMd, chunkMd.getBlockPos(localX, y, localZ));
    }

    public static BlockMD getBlockMD(ChunkMD chunkMd, BlockPos blockPos) {
        try {
            if (blockPos.m_123342_() < chunkMd.getMinY()) {
                return VOIDBLOCK;
            } else {
                BlockState blockState;
                if (chunkMd != null && chunkMd.hasChunk()) {
                    blockState = chunkMd.getChunkBlockState(blockPos);
                } else {
                    blockState = JmBlockAccess.INSTANCE.getBlockState(blockPos);
                }
                return get(blockState);
            }
        } catch (Exception var3) {
            LOGGER.error(String.format("Can't get blockId/meta for chunk %s,%s at %s : %s", chunkMd.getChunk().m_7697_().x, chunkMd.getChunk().m_7697_().z, blockPos, LogFormatter.toString(var3)));
            return AIRBLOCK;
        }
    }

    public static BlockMD get(BlockState blockState) {
        return DataCache.INSTANCE.getBlockMD(blockState);
    }

    public static String getBlockId(BlockMD blockMD) {
        return getBlockId(blockMD.getBlockState());
    }

    public static String getBlockId(BlockState blockState) {
        return BuiltInRegistries.BLOCK.getKey(blockState.m_60734_()).toString();
    }

    public static String getBlockStateId(BlockMD blockMD) {
        return getBlockStateId(blockMD.getBlockState());
    }

    public static String getBlockStateId(BlockState blockState) {
        Collection properties = blockState.m_61147_();
        return properties.isEmpty() ? Integer.toString(Block.getId(blockState)) : Joiner.on(",").join(properties);
    }

    private static String getBlockName(BlockState blockState) {
        String displayName = null;
        try {
            Block block = blockState.m_60734_();
            Item item = Item.byBlock(block);
            if (item != null) {
                ItemStack idPicked = new ItemStack(item, 1);
                displayName = item.getName(idPicked).getString();
            }
            if (Strings.isNullOrEmpty(displayName)) {
                block.getName().getString();
            }
        } catch (Exception var5) {
            LOGGER.debug(String.format("Couldn't get display name for %s: %s ", blockState, var5));
        }
        if (Strings.isNullOrEmpty(displayName) || displayName.contains("tile")) {
            displayName = blockState.m_60734_().getClass().getSimpleName().replaceAll("Block", "");
        }
        return displayName;
    }

    public static String getBlockName(Block block) {
        String displayName = null;
        try {
            Item item = (Item) Item.BY_BLOCK.get(block);
            if (item != null) {
                ItemStack idPicked = new ItemStack(item, 1);
                displayName = item.getName(idPicked).getString();
            }
            if (Strings.isNullOrEmpty(displayName)) {
                displayName = block.getName().getString();
            }
        } catch (Exception var4) {
            LOGGER.debug(String.format("Couldn't get display name for %s: %s ", block, var4));
        }
        if (Strings.isNullOrEmpty(displayName) || displayName.contains("tile")) {
            displayName = block.getClass().getSimpleName().replaceAll("Block", "");
        }
        return displayName;
    }

    public static void setAllFlags(Block block, BlockFlag... flags) {
        BlockMD defaultBlockMD = get(block.defaultBlockState());
        for (BlockMD blockMD : defaultBlockMD.getValidStateMDs()) {
            blockMD.addFlags(flags);
        }
        LOGGER.debug(block.getName() + " flags set: " + flags);
    }

    public boolean hasFlag(BlockFlag checkFlag) {
        return this.flags.contains(checkFlag);
    }

    public boolean hasAnyFlag(EnumSet<BlockFlag> checkFlags) {
        for (BlockFlag flag : checkFlags) {
            if (this.flags.contains(flag)) {
                return true;
            }
        }
        return false;
    }

    public void addFlags(BlockFlag... addFlags) {
        Collections.addAll(this.flags, addFlags);
        this.updateProperties();
    }

    public void removeFlags(BlockFlag... removeFlags) {
        for (BlockFlag flag : removeFlags) {
            this.flags.remove(flag);
        }
        this.updateProperties();
    }

    public void removeFlags(Collection<BlockFlag> removeFlags) {
        this.flags.removeAll(removeFlags);
        this.updateProperties();
    }

    public void addFlags(Collection<BlockFlag> addFlags) {
        this.flags.addAll(addFlags);
        this.updateProperties();
    }

    public int getBlockColor(ChunkMD chunkMD, BlockPos blockPos) {
        return this.blockColorProxy.getBlockColor(chunkMD, this, blockPos);
    }

    public int getTextureColor(@Nullable ChunkMD chunkMD, @Nullable BlockPos blockPos) {
        if (this.color == null && !this.isError && this.blockColorProxy != null) {
            this.color = this.blockColorProxy.deriveBlockColor(this, chunkMD, blockPos);
        }
        if (this.color == null) {
            this.color = 0;
        }
        return this.color;
    }

    public int getTextureColor() {
        return this.getTextureColor(null, null);
    }

    public void clearColor() {
        this.color = null;
    }

    public int setColor(int baseColor) {
        this.color = baseColor;
        return baseColor;
    }

    public boolean hasColor() {
        return this.color != null;
    }

    public void setBlockSpritesProxy(IBlockSpritesProxy blockSpritesProxy) {
        this.blockSpritesProxy = blockSpritesProxy;
    }

    public IBlockSpritesProxy getBlockSpritesProxy() {
        return this.blockSpritesProxy;
    }

    public void setBlockColorProxy(IBlockColorProxy blockColorProxy) {
        this.blockColorProxy = blockColorProxy;
    }

    public IBlockColorProxy getBlockColorProxy() {
        return this.blockColorProxy;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        if (alpha < 1.0F) {
            this.flags.add(BlockFlag.Transparency);
        } else {
            this.flags.remove(BlockFlag.Transparency);
        }
    }

    public boolean hasNoShadow() {
        return this.noShadow ? true : this.isPlantOrCrop && !JourneymapClient.getInstance().getCoreProperties().mapPlantShadows.get();
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    public boolean hasTransparency() {
        return this.alpha < 1.0F;
    }

    public boolean isIgnore() {
        return this.isIgnore;
    }

    public boolean isIce() {
        return this.isIce;
    }

    public boolean isWater() {
        return this.isWater;
    }

    public boolean isFluid() {
        return this.isFluid;
    }

    public boolean isLava() {
        return this.isLava;
    }

    public boolean isFire() {
        return this.isFire;
    }

    public boolean isFoliage() {
        return this.isFoliage;
    }

    public boolean isGrass() {
        return this.isGrass;
    }

    public String getName() {
        return this.name;
    }

    public String getBlockId() {
        return this.blockId;
    }

    public String getBlockStateId() {
        return this.blockStateId;
    }

    public String getBlockDomain() {
        return BuiltInRegistries.BLOCK.getKey(this.getBlock()).getNamespace();
    }

    public EnumSet<BlockFlag> getFlags() {
        return this.flags;
    }

    public boolean isVanillaBlock() {
        return this.getBlockDomain().equals("minecraft");
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return !(o instanceof BlockMD blockMD) ? false : Objects.equal(this.getBlockId(), blockMD.getBlockId()) && Objects.equal(this.getBlockStateId(), blockMD.getBlockStateId());
        }
    }

    public int hashCode() {
        return Objects.hashCode(new Object[] { this.getBlockId(), this.getBlockStateId() });
    }

    public String toString() {
        return String.format("BlockMD [%s] (%s)", this.blockState, Joiner.on(",").join(this.flags));
    }

    public int compareTo(BlockMD that) {
        Ordering ordering = Ordering.natural().nullsLast();
        return ComparisonChain.start().compare(this.blockId, that.blockId, ordering).compare(this.blockStateId, that.blockStateId, ordering).result();
    }

    public static class CacheLoader extends com.google.common.cache.CacheLoader<BlockState, BlockMD> {

        public BlockMD load(@Nonnull BlockState blockState) throws Exception {
            try {
                if (blockState != null && (blockState.m_60799_() != RenderShape.INVISIBLE || blockState.m_60734_() instanceof LiquidBlock)) {
                    if (BuiltInRegistries.BLOCK.getKey(blockState.m_60734_()) == null) {
                        BlockMD.LOGGER.warn("Unregistered block will be treated like air: " + blockState);
                        return BlockMD.AIRBLOCK;
                    } else {
                        return new BlockMD(blockState);
                    }
                } else {
                    return BlockMD.AIRBLOCK;
                }
            } catch (Exception var3) {
                BlockMD.LOGGER.error(String.format("Can't get BlockMD for %s : %s", blockState, LogFormatter.toPartialString(var3)));
                return BlockMD.AIRBLOCK;
            }
        }
    }
}