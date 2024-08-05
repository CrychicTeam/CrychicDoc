package journeymap.client.mod.vanilla;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.annotation.Nullable;
import journeymap.client.cartography.color.ColoredSprite;
import journeymap.client.mod.IBlockSpritesProxy;
import journeymap.client.model.BlockMD;
import journeymap.client.model.ChunkMD;
import journeymap.client.ui.GuiHooks;
import journeymap.common.Journeymap;
import journeymap.common.accessors.FluidAccess;
import journeymap.common.log.LogFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import org.apache.logging.log4j.Logger;

public class VanillaBlockSpriteProxy implements IBlockSpritesProxy, FluidAccess {

    private static Logger logger = Journeymap.getLogger();

    BlockModelShaper bms = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper();

    @Nullable
    @Override
    public Collection<ColoredSprite> getSprites(BlockMD blockMD, @Nullable ChunkMD chunkMD, @Nullable BlockPos blockPos) {
        BlockState blockState = blockMD.getBlockState();
        Block block = blockState.m_60734_();
        if (block instanceof LiquidBlock) {
            ResourceLocation loc = IClientFluidTypeExtensions.of(this.getFluid(blockMD.getBlock()).getFluidType()).getStillTexture();
            TextureAtlasSprite tas = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(loc);
            return Collections.singletonList(new ColoredSprite(tas, null));
        } else {
            if (blockState.m_61147_().contains(DoublePlantBlock.HALF)) {
                blockState = (BlockState) blockState.m_61124_(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER);
            }
            HashMap<String, ColoredSprite> map = new HashMap();
            try {
                BakedModel model = this.bms.getBlockModel(blockState);
                label44: for (BlockState state : new BlockState[] { blockState, null }) {
                    for (Direction facing : new Direction[] { Direction.UP, null }) {
                        if (this.getSprites(blockMD, model, state, facing, map, chunkMD, blockPos)) {
                            break label44;
                        }
                    }
                }
                if (map.isEmpty()) {
                    TextureAtlasSprite defaultSprite = this.bms.getParticleIcon(blockState);
                    if (defaultSprite != null) {
                        map.put(defaultSprite.contents().name().getPath(), new ColoredSprite(defaultSprite, null));
                        if (!blockMD.isVanillaBlock() && logger.isDebugEnabled()) {
                            logger.debug(String.format("Resorted to using BlockModelStates.getTexture() to use %s as color for %s", defaultSprite.contents().name().getPath(), blockState));
                        }
                    } else {
                        logger.warn(String.format("Unable to get any texture to use as color for %s", blockState));
                    }
                }
            } catch (Exception var16) {
                logger.error("Unexpected error during getSprites(): " + BuiltInRegistries.BLOCK.getKey(blockMD.getBlock()) + " - " + LogFormatter.toPartialString(var16));
            }
            return map.values();
        }
    }

    private boolean getSprites(BlockMD blockMD, BakedModel model, @Nullable BlockState blockState, @Nullable Direction facing, HashMap<String, ColoredSprite> map, @Nullable ChunkMD chunkMD, @Nullable BlockPos blockPos) {
        if (blockPos != null && chunkMD != null && chunkMD.getWorld() != null) {
            try {
                blockState = chunkMD.getWorld().m_8055_(blockPos);
                model = this.bms.getBlockModel(blockState);
            } catch (Exception var12) {
            }
        }
        boolean success = false;
        try {
            for (RenderType type : RenderType.chunkBufferLayers()) {
                if (RenderShape.MODEL.equals(blockMD.getBlockState().m_60799_())) {
                    List<BakedQuad> quads = model.getQuads(blockState, facing, RandomSource.create(), GuiHooks.getModelData(blockPos), type);
                    if (this.addSprites(map, quads)) {
                        if (!blockMD.isVanillaBlock() && logger.isDebugEnabled()) {
                            logger.debug(String.format("Success during [%s] %s.getQuads(%s, %s, %s)", type, model.getClass(), blockState, facing, 0));
                        }
                        success = true;
                    }
                }
            }
        } catch (Exception var13) {
            if (logger.isDebugEnabled()) {
                logger.error(String.format("Error during [] %s.getQuads(%s, %s, %s): %s", model.getClass(), blockState, facing, 0, LogFormatter.toPartialString(var13)));
            }
        }
        return success;
    }

    public boolean addSprites(HashMap<String, ColoredSprite> sprites, List<BakedQuad> quads) {
        if (quads != null && !quads.isEmpty()) {
            if (quads.size() > 1) {
                HashSet<BakedQuad> culled = new HashSet(quads.size());
                culled.addAll(quads);
                quads = new ArrayList(culled);
            }
            boolean added = false;
            for (BakedQuad quad : quads) {
                TextureAtlasSprite sprite = quad.getSprite();
                if (sprite != null) {
                    String iconName = sprite.contents().name().getPath();
                    if (!sprites.containsKey(iconName)) {
                        ResourceLocation resourceLocation = new ResourceLocation(iconName);
                        if (!resourceLocation.equals(new ResourceLocation("missingno"))) {
                            sprites.put(iconName, new ColoredSprite(quad));
                            added = true;
                        }
                    }
                }
            }
            return added;
        } else {
            return false;
        }
    }
}