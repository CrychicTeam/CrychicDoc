package journeymap.client.mod.impl;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import journeymap.client.cartography.color.ColoredSprite;
import journeymap.client.mod.IBlockSpritesProxy;
import journeymap.client.mod.IModBlockHandler;
import journeymap.client.mod.ModBlockDelegate;
import journeymap.client.mod.ModPropertyEnum;
import journeymap.client.model.BlockMD;
import journeymap.client.model.ChunkMD;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class Bibliocraft implements IModBlockHandler, IBlockSpritesProxy {

    List<ModPropertyEnum<String>> colorProperties = new ArrayList(2);

    public Bibliocraft() {
        this.colorProperties.add(new ModPropertyEnum("jds.bibliocraft.blocks.BiblioColorBlock", "COLOR", "getWoolTextureString", String.class));
        this.colorProperties.add(new ModPropertyEnum("jds.bibliocraft.blocks.BiblioWoodBlock", "WOOD_TYPE", "getTextureString", String.class));
    }

    @Override
    public void initialize(BlockMD blockMD) {
        blockMD.setBlockSpritesProxy(this);
    }

    @Nullable
    @Override
    public Collection<ColoredSprite> getSprites(BlockMD blockMD, @Nullable ChunkMD chunkMD, @Nullable BlockPos blockPos) {
        BlockState blockState = blockMD.getBlockState();
        String textureString = ModPropertyEnum.getFirstValue(this.colorProperties, blockState);
        if (!Strings.isNullOrEmpty(textureString)) {
            try {
                ResourceLocation loc = new ResourceLocation(textureString);
                TextureAtlasSprite tas = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS).getSprite(loc);
                return Collections.singletonList(new ColoredSprite(tas, null));
            } catch (Exception var8) {
                Journeymap.getLogger().error(String.format("Error getting sprite from %s: %s", textureString, LogFormatter.toPartialString(var8)));
            }
        }
        return ModBlockDelegate.INSTANCE.getDefaultBlockSpritesProxy().getSprites(blockMD, chunkMD, blockPos);
    }
}