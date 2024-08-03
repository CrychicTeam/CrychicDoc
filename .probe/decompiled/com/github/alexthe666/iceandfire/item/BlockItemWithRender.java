package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.citadel.client.CitadelItemRenderProperties;
import com.github.alexthe666.iceandfire.client.render.tile.IceAndFireTEISR;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.NonNullLazy;

public class BlockItemWithRender extends BlockItem {

    public BlockItemWithRender(Block block0, Item.Properties itemProperties1) {
        super(block0, itemProperties1);
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new CitadelItemRenderProperties() {

            static final NonNullLazy<BlockEntityWithoutLevelRenderer> renderer = NonNullLazy.of(() -> new IceAndFireTEISR(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer.get();
            }
        });
    }
}