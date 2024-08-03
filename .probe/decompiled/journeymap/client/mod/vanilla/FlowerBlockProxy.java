package journeymap.client.mod.vanilla;

import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import java.util.Collection;
import journeymap.client.JourneymapClient;
import journeymap.client.cartography.color.ColorManager;
import journeymap.client.cartography.color.ColoredSprite;
import journeymap.client.cartography.color.RGB;
import journeymap.client.mod.IBlockColorProxy;
import journeymap.client.mod.ModBlockDelegate;
import journeymap.client.model.BlockMD;
import journeymap.client.model.ChunkMD;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;

public enum FlowerBlockProxy implements IBlockColorProxy {

    INSTANCE;

    boolean enabled = true;

    @Override
    public int deriveBlockColor(BlockMD blockMD, @Nullable ChunkMD chunkMD, @Nullable BlockPos blockPos) {
        if (blockMD.getBlock() instanceof FlowerBlock) {
            Integer color = this.getFlowerColor(blockMD, blockPos);
            if (color != null) {
                return color;
            }
        }
        return ModBlockDelegate.INSTANCE.getDefaultBlockColorProxy().deriveBlockColor(blockMD, chunkMD, blockPos);
    }

    @Override
    public int getBlockColor(ChunkMD chunkMD, BlockMD blockMD, BlockPos blockPos) {
        if (blockMD.getBlock() instanceof FlowerBlock) {
            return blockMD.getTextureColor();
        } else {
            if (blockMD.getBlock() instanceof FlowerPotBlock && JourneymapClient.getInstance().getCoreProperties().mapPlants.get()) {
                try {
                    BlockState blockState = blockMD.getBlockState();
                    ItemStack iStack = ((FlowerPotBlock) blockState.m_60734_()).getCloneItemStack(chunkMD.getWorld(), blockPos, blockState);
                    if (iStack != null) {
                        BlockState contentBlockState = Block.byItem(iStack.getItem()).defaultBlockState();
                        return BlockMD.get(contentBlockState).getTextureColor();
                    }
                } catch (Exception var7) {
                    Journeymap.getLogger().error("Error checking FlowerPot: " + var7, LogFormatter.toPartialString(var7));
                    this.enabled = false;
                }
            }
            return ModBlockDelegate.INSTANCE.getDefaultBlockColorProxy().getBlockColor(chunkMD, blockMD, blockPos);
        }
    }

    private Integer getFlowerColor(BlockMD blockMd, BlockPos blockPos) {
        BlockState blockState = blockMd.getBlockState();
        if (blockState.m_60734_() instanceof FlowerBlock) {
            Block block = blockState.m_60734_();
            String var5 = BuiltInRegistries.BLOCK.getKey(block).getPath();
            return switch(var5) {
                case "poppy", "red_tulip" ->
                    9962502;
                case "blue_orchid" ->
                    1998518;
                case "allium" ->
                    8735158;
                case "azure_bluet" ->
                    10330535;
                case "orange_tulip" ->
                    10704922;
                case "white_tulip" ->
                    11579568;
                case "pink_tulip" ->
                    11573936;
                case "oxeye_daisy" ->
                    11776947;
                case "dandelion" ->
                    11514881;
                case "cornflower" ->
                    3361970;
                case "lily_of_the_valley" ->
                    16777215;
                case "wither_rose" ->
                    1644825;
                default ->
                    this.getUnknownFlowerColor(blockMd, blockPos);
            };
        } else {
            return null;
        }
    }

    private int getUnknownFlowerColor(BlockMD blockMD, BlockPos blockPos) {
        try {
            Collection<ColoredSprite> sprites = blockMD.getBlockSpritesProxy().getSprites(blockMD, null, null);
            float[] rgba = ColorManager.INSTANCE.getAverageColor(sprites);
            if (rgba != null) {
                return RGB.toInteger(rgba);
            } else {
                BlockState blockState = blockMD.getBlockState();
                ClientLevel level = Minecraft.getInstance().level;
                return blockState.m_284242_(level, blockPos).col;
            }
        } catch (Exception var7) {
            return 0;
        }
    }
}