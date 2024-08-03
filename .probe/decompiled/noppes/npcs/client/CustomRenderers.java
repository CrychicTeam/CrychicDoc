package noppes.npcs.client;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import noppes.npcs.CustomBlocks;
import noppes.npcs.CustomEntities;
import noppes.npcs.client.model.ModelClassicPlayer;
import noppes.npcs.client.model.ModelNPCGolem;
import noppes.npcs.client.model.ModelNpcCrystal;
import noppes.npcs.client.model.ModelNpcDragon;
import noppes.npcs.client.model.ModelNpcSlime;
import noppes.npcs.client.model.ModelPlayer64x32;
import noppes.npcs.client.model.ModelPony;
import noppes.npcs.client.renderer.RenderCustomNpc;
import noppes.npcs.client.renderer.RenderNPCInterface;
import noppes.npcs.client.renderer.RenderNPCPony;
import noppes.npcs.client.renderer.RenderNpcCrystal;
import noppes.npcs.client.renderer.RenderNpcDragon;
import noppes.npcs.client.renderer.RenderNpcSlime;
import noppes.npcs.client.renderer.RenderProjectile;
import noppes.npcs.client.renderer.blocks.BlockBuilderRenderer;
import noppes.npcs.client.renderer.blocks.BlockCarpentryBenchRenderer;
import noppes.npcs.client.renderer.blocks.BlockCopyRenderer;
import noppes.npcs.client.renderer.blocks.BlockDoorRenderer;
import noppes.npcs.client.renderer.blocks.BlockMailboxRenderer;
import noppes.npcs.client.renderer.blocks.BlockScriptedRenderer;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(bus = Bus.MOD, modid = "customnpcs", value = { Dist.CLIENT })
public class CustomRenderers {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(CustomEntities.entityNpcPony, manager -> new RenderNPCPony(manager, new ModelPony()));
        event.registerEntityRenderer(CustomEntities.entityNpcCrystal, manager -> new RenderNpcCrystal(manager, new ModelNpcCrystal()));
        event.registerEntityRenderer(CustomEntities.entityNpcDragon, manager -> new RenderNpcDragon<>(manager, new ModelNpcDragon(), 0.5F));
        event.registerEntityRenderer(CustomEntities.entityNpcSlime, manager -> new RenderNpcSlime(manager, new ModelNpcSlime(16), new ModelNpcSlime(0), 0.25F));
        event.registerEntityRenderer(CustomEntities.entityProjectile, manager -> new RenderProjectile(manager));
        event.registerEntityRenderer(CustomEntities.entityCustomNpc, manager -> new RenderCustomNpc<>(manager, new PlayerModel(manager.getModelSet().bakeLayer(ModelLayers.PLAYER), false)));
        event.registerEntityRenderer(CustomEntities.entityNPC64x32, manager -> new RenderCustomNpc<>(manager, new ModelPlayer64x32(manager.getModelSet().bakeLayer(ModelLayers.PLAYER))));
        event.registerEntityRenderer(CustomEntities.entityNPCGolem, manager -> new RenderNPCInterface<>(manager, new ModelNPCGolem(0.0F), 0.0F));
        event.registerEntityRenderer(CustomEntities.entityNpcAlex, manager -> new RenderCustomNpc<>(manager, new PlayerModel(manager.getModelSet().bakeLayer(ModelLayers.PLAYER_SLIM), true)));
        event.registerEntityRenderer(CustomEntities.entityNpcClassicPlayer, manager -> new RenderCustomNpc<>(manager, new ModelClassicPlayer(manager.getModelSet().bakeLayer(ModelLayers.PLAYER), 0.0F)));
        event.registerBlockEntityRenderer(CustomBlocks.tile_anvil, BlockCarpentryBenchRenderer::new);
        event.registerBlockEntityRenderer(CustomBlocks.tile_mailbox, BlockMailboxRenderer::new);
        event.registerBlockEntityRenderer(CustomBlocks.tile_scripted, BlockScriptedRenderer::new);
        event.registerBlockEntityRenderer(CustomBlocks.tile_scripteddoor, BlockDoorRenderer::new);
        event.registerBlockEntityRenderer(CustomBlocks.tile_copy, BlockCopyRenderer::new);
        event.registerBlockEntityRenderer(CustomBlocks.tile_builder, BlockBuilderRenderer::new);
    }
}