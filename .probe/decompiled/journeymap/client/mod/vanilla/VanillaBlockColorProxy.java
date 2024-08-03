package journeymap.client.mod.vanilla;

import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import journeymap.client.JourneymapClient;
import journeymap.client.cartography.color.ColorManager;
import journeymap.client.cartography.color.ColoredSprite;
import journeymap.client.cartography.color.RGB;
import journeymap.client.mod.IBlockColorProxy;
import journeymap.client.model.BlockFlag;
import journeymap.client.model.BlockMD;
import journeymap.client.model.ChunkMD;
import journeymap.client.properties.CoreProperties;
import journeymap.client.world.JmBlockAccess;
import journeymap.common.Journeymap;
import journeymap.common.accessors.FluidAccess;
import journeymap.common.log.LogFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import org.apache.logging.log4j.Logger;

public class VanillaBlockColorProxy implements IBlockColorProxy, FluidAccess {

    static Logger logger = Journeymap.getLogger();

    private static final int DEFAULT_WATER_COLOR = 4159204;

    private final BlockColors blockColors = Minecraft.getInstance().getBlockColors();

    private final CoreProperties coreProperties = JourneymapClient.getInstance().getCoreProperties();

    @Override
    public int deriveBlockColor(BlockMD blockMD, @Nullable ChunkMD chunkMD, @Nullable BlockPos blockPos) {
        BlockState blockState = blockMD.getBlockState();
        try {
            if (blockState.m_60734_() instanceof LiquidBlock) {
                return getSpriteColor(blockMD, 12369084, chunkMD, blockPos);
            } else {
                Integer color = getSpriteColor(blockMD, null, chunkMD, blockPos);
                if (color == null) {
                    color = setBlockColorToMaterial(blockMD);
                }
                return color;
            }
        } catch (Throwable var6) {
            logger.error("Error deriving color for " + blockMD + ": " + LogFormatter.toPartialString(var6));
            blockMD.addFlags(BlockFlag.Error);
            return setBlockColorToMaterial(blockMD);
        }
    }

    @Override
    public int getBlockColor(ChunkMD chunkMD, BlockMD blockMD, BlockPos blockPos) {
        boolean showBiomeWaterColor = this.coreProperties.mapWaterBiomeColors.get();
        boolean mapBiome = this.coreProperties.mapBiome.get();
        int result = blockMD.getTextureColor(chunkMD, blockPos);
        if (blockMD.isFoliage()) {
            result = RGB.adjustBrightness(result, 0.8F);
        } else if (blockMD.isFluid() && (!blockMD.isWater() || !showBiomeWaterColor)) {
            FlowingFluid fluidBlock = this.getFluid(blockMD.getBlock());
            if (!Fluids.FLOWING_WATER.equals(fluidBlock) && !Fluids.WATER.equals(fluidBlock)) {
                return RGB.multiply(result, IClientFluidTypeExtensions.of(fluidBlock.getFluidType()).getTintColor());
            }
            return RGB.multiply(result, 4159204);
        }
        return RGB.multiply(result, this.getColorMultiplier(chunkMD, blockMD, blockPos, blockMD.getBlock().m_7514_(blockMD.getBlockState()).ordinal()));
    }

    public int getColorMultiplier(ChunkMD chunkMD, BlockMD blockMD, BlockPos blockPos, int tintIndex) {
        boolean blendFoliage = this.coreProperties.mapBlendFoliage.get();
        boolean blendGrass = this.coreProperties.mapBlendGrass.get();
        boolean blendWater = this.coreProperties.mapBlendWater.get();
        if (!blendGrass && blockMD.isGrass()) {
            return chunkMD.getBiome(blockPos).getGrassColor((double) blockPos.m_123341_(), (double) blockPos.m_123343_());
        } else if (!blendFoliage && blockMD.isFoliage()) {
            return chunkMD.getBiome(blockPos).getFoliageColor();
        } else {
            return !blendWater && blockMD.isWater() ? chunkMD.getBiome(blockPos).getWaterColor() : this.blockColors.getColor(blockMD.getBlockState(), JmBlockAccess.INSTANCE, blockPos, tintIndex);
        }
    }

    public static Integer getSpriteColor(@Nonnull BlockMD blockMD, @Nullable Integer defaultColor, @Nullable ChunkMD chunkMD, @Nullable BlockPos blockPos) {
        Collection<ColoredSprite> sprites = blockMD.getBlockSpritesProxy().getSprites(blockMD, chunkMD, blockPos);
        float[] rgba = ColorManager.INSTANCE.getAverageColor(sprites);
        return rgba != null ? RGB.toInteger(rgba) : defaultColor;
    }

    public static int setBlockColorToError(BlockMD blockMD) {
        blockMD.setAlpha(0.0F);
        blockMD.addFlags(BlockFlag.Ignore, BlockFlag.Error);
        blockMD.setColor(-1);
        return -1;
    }

    public static int setBlockColorToMaterial(BlockMD blockMD) {
        try {
            blockMD.setAlpha(1.0F);
            blockMD.addFlags(BlockFlag.Ignore);
            return blockMD.setColor(blockMD.getBlock().m_284356_().col);
        } catch (Exception var2) {
            logger.warn(String.format("Failed to use MaterialMapColor, marking as error: %s", blockMD));
            return setBlockColorToError(blockMD);
        }
    }
}