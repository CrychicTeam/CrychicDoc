package com.mna.blocks;

import com.mna.ManaAndArtifice;
import com.mna.api.blocks.interfaces.IDontCreateBlockItem;
import com.mna.api.items.TieredBlockItem;
import com.mna.items.OffsetPlacerItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class BlockItemRegistration {

    @SubscribeEvent
    public static void onRegisterItems(RegisterEvent event) {
        event.register(ForgeRegistries.ITEMS.getRegistryKey(), helper -> {
            BlockInit.BLOCKS.getEntries().stream().filter(b -> !(b.get() instanceof IDontCreateBlockItem) && !(b.get() instanceof FlowerPotBlock)).map(RegistryObject::get).forEach(block -> {
                Item.Properties properties = new Item.Properties();
                if (block instanceof IOffsetPlace) {
                    OffsetPlacerItem blockItem = new OffsetPlacerItem(block, properties, ((IOffsetPlace) block)::adjustPlacement);
                    helper.register(ForgeRegistries.BLOCKS.getKey(block), blockItem);
                } else {
                    TieredBlockItem blockItem = new TieredBlockItem(block, properties);
                    helper.register(ForgeRegistries.BLOCKS.getKey(block), blockItem);
                }
            });
            helper.register(new ResourceLocation("mna", "aum"), new BlockItem(BlockInit.AUM.get(), new Item.Properties()));
            helper.register(new ResourceLocation("mna", "cerublossom"), new BlockItem(BlockInit.CERUBLOSSOM.get(), new Item.Properties()));
            helper.register(new ResourceLocation("mna", "tarma_root"), new BlockItem(BlockInit.TARMA_ROOT.get(), new Item.Properties()));
            helper.register(new ResourceLocation("mna", "desert_nova"), new BlockItem(BlockInit.DESERT_NOVA.get(), new Item.Properties()));
        });
        ManaAndArtifice.LOGGER.info("M&A -> Block Items Registered");
    }

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(BlockInit.AUM.getId(), BlockInit.POTTED_AUM);
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(BlockInit.CERUBLOSSOM.getId(), BlockInit.POTTED_CERUBLOSSOM);
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(BlockInit.WAKEBLOOM.getId(), BlockInit.POTTED_WAKEBLOOM);
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(BlockInit.TARMA_ROOT.getId(), BlockInit.POTTED_TARMA_ROOT);
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(BlockInit.DESERT_NOVA.getId(), BlockInit.POTTED_DESERT_NOVA);
        ManaAndArtifice.LOGGER.info("M&A -> Flower Pots Registered");
    }
}