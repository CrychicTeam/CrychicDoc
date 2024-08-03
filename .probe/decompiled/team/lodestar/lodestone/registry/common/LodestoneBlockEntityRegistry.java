package team.lodestar.lodestone.registry.common;

import java.util.ArrayList;
import java.util.Arrays;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.lodestone.systems.block.sign.LodestoneStandingSignBlock;
import team.lodestar.lodestone.systems.block.sign.LodestoneWallSignBlock;
import team.lodestar.lodestone.systems.blockentity.LodestoneSignBlockEntity;
import team.lodestar.lodestone.systems.multiblock.ILodestoneMultiblockComponent;
import team.lodestar.lodestone.systems.multiblock.MultiBlockComponentEntity;

public class LodestoneBlockEntityRegistry {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "lodestone");

    public static final RegistryObject<BlockEntityType<MultiBlockComponentEntity>> MULTIBLOCK_COMPONENT = BLOCK_ENTITY_TYPES.register("multiblock_component", () -> BlockEntityType.Builder.of(MultiBlockComponentEntity::new, getBlocks(ILodestoneMultiblockComponent.class)).build(null));

    public static final RegistryObject<BlockEntityType<LodestoneSignBlockEntity>> SIGN = BLOCK_ENTITY_TYPES.register("sign", () -> BlockEntityType.Builder.of(LodestoneSignBlockEntity::new, getBlocks(LodestoneStandingSignBlock.class, LodestoneWallSignBlock.class)).build(null));

    public static Block[] getBlocks(Class<?>... blockClasses) {
        IForgeRegistry<Block> blocks = ForgeRegistries.BLOCKS;
        ArrayList<Block> matchingBlocks = new ArrayList();
        for (Block block : blocks) {
            if (Arrays.stream(blockClasses).anyMatch(b -> b.isInstance(block))) {
                matchingBlocks.add(block);
            }
        }
        return (Block[]) matchingBlocks.toArray(new Block[0]);
    }

    public static Block[] getBlocksExact(Class<?> clazz) {
        IForgeRegistry<Block> blocks = ForgeRegistries.BLOCKS;
        ArrayList<Block> matchingBlocks = new ArrayList();
        for (Block block : blocks) {
            if (clazz.equals(block.getClass())) {
                matchingBlocks.add(block);
            }
        }
        return (Block[]) matchingBlocks.toArray(new Block[0]);
    }

    @EventBusSubscriber(modid = "lodestone", value = { Dist.CLIENT }, bus = Bus.MOD)
    public static class ClientOnly {

        @SubscribeEvent
        public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(LodestoneBlockEntityRegistry.SIGN.get(), SignRenderer::new);
        }
    }
}