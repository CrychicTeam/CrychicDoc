package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.ClientProxy;
import com.github.alexthe666.iceandfire.client.IafKeybindRegistry;
import com.github.alexthe666.iceandfire.client.gui.IceAndFireMainMenu;
import com.github.alexthe666.iceandfire.client.particle.CockatriceBeamRender;
import com.github.alexthe666.iceandfire.client.render.entity.RenderChain;
import com.github.alexthe666.iceandfire.client.render.tile.RenderFrozenState;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.entity.util.ICustomMoveController;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.message.MessageDragonControl;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.WorldEventContext;
import java.util.Random;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = "iceandfire", value = { Dist.CLIENT })
public class ClientEvents {

    private static final ResourceLocation SIREN_SHADER = new ResourceLocation("iceandfire:shaders/post/siren.json");

    private final Random rand = new Random();

    public final boolean AUTO_ADAPT_3RD_PERSON = true;

    private static boolean shouldCancelRender(LivingEntity living) {
        return living.m_20202_() != null && living.m_20202_() instanceof EntityDragonBase ? ClientProxy.currentDragonRiders.contains(living.m_20148_()) || living == Minecraft.getInstance().player && Minecraft.getInstance().options.getCameraType().isFirstPerson() : false;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void renderWorldLastEvent(@NotNull RenderLevelStageEvent event) {
        WorldEventContext.INSTANCE.renderWorldLastEvent(event);
    }

    @SubscribeEvent
    public void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        Player player = Minecraft.getInstance().player;
        if (player.m_20202_() != null && player.m_20202_() instanceof EntityDragonBase) {
            int currentView = IceAndFire.PROXY.getDragon3rdPersonView();
            float scale = ((EntityDragonBase) player.m_20202_()).getRenderSize() / 3.0F;
            if (Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_BACK || Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_FRONT) {
                if (currentView == 1) {
                    event.getCamera().move(-event.getCamera().getMaxZoom((double) (scale * 1.2F)), 0.0, 0.0);
                } else if (currentView == 2) {
                    event.getCamera().move(-event.getCamera().getMaxZoom((double) (scale * 3.0F)), 0.0, 0.0);
                } else if (currentView == 3) {
                    event.getCamera().move(-event.getCamera().getMaxZoom((double) (scale * 5.0F)), 0.0, 0.0);
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (event.getEntity() instanceof ICustomMoveController) {
            Entity entity = event.getEntity();
            ICustomMoveController moveController = (ICustomMoveController) event.getEntity();
            if (entity.getVehicle() != null && entity.getVehicle() == mc.player) {
                byte previousState = moveController.getControlState();
                moveController.dismount(mc.options.keyShift.isDown());
                byte controlState = moveController.getControlState();
                if (controlState != previousState) {
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonControl(entity.getId(), controlState, entity.getX(), entity.getY(), entity.getZ()));
                }
            }
        }
        if (event.getEntity() instanceof Player player) {
            if (player.m_9236_().isClientSide && player.m_20202_() instanceof ICustomMoveController) {
                Entity entity = player.m_20202_();
                ICustomMoveController moveController = (ICustomMoveController) player.m_20202_();
                byte previousState = moveController.getControlState();
                moveController.up(mc.options.keyJump.isDown());
                moveController.down(IafKeybindRegistry.dragon_down.isDown());
                moveController.attack(IafKeybindRegistry.dragon_strike.isDown());
                moveController.dismount(mc.options.keyShift.isDown());
                moveController.strike(IafKeybindRegistry.dragon_fireAttack.isDown());
                byte controlState = moveController.getControlState();
                if (controlState != previousState) {
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonControl(entity.getId(), controlState, entity.getX(), entity.getY(), entity.getZ()));
                }
            }
            if (player.m_9236_().isClientSide && IafKeybindRegistry.dragon_change_view.isDown()) {
                int currentView = IceAndFire.PROXY.getDragon3rdPersonView();
                if (currentView + 1 > 3) {
                    currentView = 0;
                } else {
                    currentView++;
                }
                IceAndFire.PROXY.setDragon3rdPersonView(currentView);
            }
            if (player.m_9236_().isClientSide) {
                GameRenderer renderer = Minecraft.getInstance().gameRenderer;
                EntityDataProvider.getCapability(player).ifPresent(data -> {
                    if (IafConfig.sirenShader && data.sirenData.charmedBy == null && renderer.currentEffect() != null && SIREN_SHADER.toString().equals(renderer.currentEffect().getName())) {
                        renderer.shutdownEffect();
                    }
                    if (data.sirenData.charmedBy != null) {
                        if (IafConfig.sirenShader && !data.sirenData.isCharmed && renderer.currentEffect() != null && SIREN_SHADER.toString().equals(renderer.currentEffect().getName())) {
                            renderer.shutdownEffect();
                        }
                        if (data.sirenData.isCharmed) {
                            if (player.m_9236_().isClientSide && this.rand.nextInt(40) == 0) {
                                IceAndFire.PROXY.spawnParticle(EnumParticles.Siren_Appearance, player.m_20185_(), player.m_20186_(), player.m_20189_(), (double) data.sirenData.charmedBy.getHairColor(), 0.0, 0.0);
                            }
                            if (IafConfig.sirenShader && renderer.currentEffect() == null) {
                                renderer.loadEffect(SIREN_SHADER);
                            }
                        }
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public void onPreRenderLiving(RenderLivingEvent.Pre event) {
        if (shouldCancelRender(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPostRenderLiving(RenderLivingEvent.Post event) {
        if (shouldCancelRender(event.getEntity())) {
            event.setCanceled(true);
        }
        LivingEntity entity = event.getEntity();
        EntityDataProvider.getCapability(entity).ifPresent(data -> {
            for (LivingEntity target : data.miscData.getTargetedByScepter()) {
                CockatriceBeamRender.render(entity, target, event.getPoseStack(), event.getMultiBufferSource(), event.getPartialTick());
            }
            if (data.frozenData.isFrozen) {
                RenderFrozenState.render(event.getEntity(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), data.frozenData.frozenTicks);
            }
            RenderChain.render(entity, event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), data.chainData.getChainedTo());
        });
    }

    @SubscribeEvent
    public void onGuiOpened(ScreenEvent.Opening event) {
        if (IafConfig.customMainMenu && event.getScreen() instanceof TitleScreen && !(event.getScreen() instanceof IceAndFireMainMenu)) {
            event.setNewScreen(new IceAndFireMainMenu());
        }
    }

    @SubscribeEvent
    public void onEntityMount(EntityMountEvent event) {
        if (event.getEntityBeingMounted() instanceof EntityDragonBase dragon && event.getLevel().isClientSide && event.getEntityMounting() == Minecraft.getInstance().player && dragon.m_21824_() && dragon.m_21830_(Minecraft.getInstance().player)) {
            IceAndFire.PROXY.setDragon3rdPersonView(2);
            if (IafConfig.dragonAuto3rdPerson) {
                if (event.isDismounting()) {
                    Minecraft.getInstance().options.setCameraType(CameraType.values()[IceAndFire.PROXY.getPreviousViewType()]);
                } else {
                    IceAndFire.PROXY.setPreviousViewType(Minecraft.getInstance().options.getCameraType().ordinal());
                    Minecraft.getInstance().options.setCameraType(CameraType.values()[1]);
                }
            }
        }
    }
}