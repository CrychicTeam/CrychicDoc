package org.violetmoon.quark.content.building.client.render.be;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.content.building.module.VariantChestsModule;

public class VariantChestRenderer extends ChestRenderer<ChestBlockEntity> {

    private final Map<Pair<Block, ChestType>, Material> materialMap = new HashMap();

    protected final boolean isTrap;

    public VariantChestRenderer(BlockEntityRendererProvider.Context context, boolean isTrap) {
        super(context);
        this.isTrap = isTrap;
    }

    @Nullable
    public Material getMaterial(ChestBlockEntity tile, ChestType type) {
        Block block = tile.m_58900_().m_60734_();
        Pair<Block, ChestType> pair = Pair.of(block, type);
        return (Material) this.materialMap.computeIfAbsent(pair, b -> {
            if (block instanceof VariantChestsModule.IVariantChest v) {
                StringBuilder tex = new StringBuilder(v.getTextureFolder()).append('/').append(v.getTexturePath()).append('/');
                if (this.isTrap) {
                    tex.append(this.choose(type, "trap", "trap_left", "trap_right"));
                } else {
                    tex.append(this.choose(type, "normal", "left", "right"));
                }
                return new Material(Sheets.CHEST_SHEET, new ResourceLocation("quark", tex.toString()));
            } else {
                return null;
            }
        });
    }

    protected <X> X choose(ChestType type, X single, X left, X right) {
        return (X) (switch(type) {
            case SINGLE ->
                single;
            case LEFT ->
                left;
            case RIGHT ->
                right;
        });
    }
}