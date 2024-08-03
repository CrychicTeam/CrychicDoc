package net.zanckor.questapi.mod.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.zanckor.questapi.ClientForgeQuestAPI;
import net.zanckor.questapi.api.file.npc.entity_type_tag.codec.EntityTypeTagDialog;
import net.zanckor.questapi.api.registry.ScreenRegistry;
import net.zanckor.questapi.api.screen.AbstractQuestLog;
import net.zanckor.questapi.mod.common.config.client.RendererConfig;
import net.zanckor.questapi.mod.common.config.client.ScreenConfig;
import net.zanckor.questapi.mod.common.network.handler.ClientHandler;
import net.zanckor.questapi.util.GsonManager;
import net.zanckor.questapi.util.Timer;
import org.joml.Quaternionf;

@EventBusSubscriber(modid = "questapi", bus = Bus.FORGE, value = { Dist.CLIENT })
public class ClientEvent {

    @SubscribeEvent
    public static void keyOpenScreen(InputEvent.Key e) throws IOException {
        if (ClientForgeQuestAPI.questMenu != null && ClientForgeQuestAPI.questMenu.isDown()) {
            AbstractQuestLog questLogScreen = ScreenRegistry.getQuestLogScreen(ScreenConfig.QUEST_LOG_SCREEN.get());
            Minecraft.getInstance().setScreen(questLogScreen.modifyScreen());
        }
    }

    @SubscribeEvent
    public static void loadHashMaps(ClientPlayerNetworkEvent.LoggingIn e) {
        ClientHandler.activeQuestList = new ArrayList();
    }

    @SubscribeEvent
    public static void renderNPCQuestMarker(RenderLivingEvent.Pre e) {
        Player player = Minecraft.getInstance().player;
        PoseStack poseStack = e.getPoseStack();
        Font font = Minecraft.getInstance().font;
        float distance = player.m_20270_(e.getEntity());
        double alphaMultiplier = Math.pow(8.0, (double) ((75.0F - distance) / 120.0F)) - 6.65;
        int flatColor = 16776960;
        int color = (int) (alphaMultiplier * 255.0) << 24 | flatColor;
        if (player.m_20270_(e.getEntity()) < 15.0F && (checkEntityTagIsValid(e.getEntity()) || checkEntityTypeIsValid(e.getEntity()))) {
            poseStack.pushPose();
            poseStack.translate(0.0, (double) e.getEntity().m_20206_() + 1.25, 0.0);
            poseStack.scale(0.15F, 0.125F, 0.15F);
            poseStack.mulPose(new Quaternionf().rotateXYZ((float) Math.toRadians(180.0), (float) Math.toRadians((double) (player.m_6080_() + 180.0F)), 0.0F));
            font.drawInBatch(Component.literal("!"), 0.0F, 0.0F, color, false, poseStack.last().pose(), Minecraft.getInstance().renderBuffers().bufferSource(), Font.DisplayMode.SEE_THROUGH, 0, 0);
            poseStack.popPose();
        }
    }

    public static boolean checkEntityTypeIsValid(LivingEntity entity) {
        EntityType entityType = entity.m_6095_();
        if (entity.getPersistentData().getBoolean("beingRenderedOnInventory")) {
            return false;
        } else if (entity.getPersistentData().contains("availableForDialog")) {
            return entity.getPersistentData().getBoolean("availableForDialog");
        } else if (ClientHandler.availableEntityTypeForQuest != null) {
            entity.getPersistentData().putBoolean("availableForDialog", ClientHandler.availableEntityTypeForQuest.contains(entityType));
            return ClientHandler.availableEntityTypeForQuest.contains(entityType);
        } else {
            return false;
        }
    }

    public static boolean checkEntityTagIsValid(LivingEntity entity) {
        for (Entry<String, String> entry : ClientHandler.availableEntityTagForQuest.entrySet()) {
            if (Timer.canUseWithCooldown(entity.m_20148_(), "UPDATE_MARKER", (float) RendererConfig.QUEST_MARK_UPDATE_COOLDOWN.get().intValue())) {
                Timer.updateCooldown(entity.m_20148_(), "UPDATE_MARKER", (float) RendererConfig.QUEST_MARK_UPDATE_COOLDOWN.get().intValue());
                CompoundTag entityNBT = NbtPredicate.getEntityTagToCompare(entity);
                String value = (String) entry.getValue();
                String targetEntityType = EntityType.getKey(entity.m_6095_()).toString();
                EntityTypeTagDialog entityTypeDialog = (EntityTypeTagDialog) GsonManager.gson.fromJson(value, EntityTypeTagDialog.class);
                for (EntityTypeTagDialog.EntityTypeTagDialogCondition conditions : entityTypeDialog.getConditions()) {
                    try {
                        EntityTypeTagDialog entityTypeConversation = (EntityTypeTagDialog) GsonManager.getJsonClass(value, EntityTypeTagDialog.class);
                        boolean isEntityType = entityTypeConversation.getEntity_type().contains(targetEntityType);
                        if (isEntityType) {
                            switch(conditions.getLogic_gate()) {
                                case OR:
                                    for (EntityTypeTagDialog.EntityTypeTagDialogCondition.EntityTypeTagDialogNBT nbtx : conditions.getNbt()) {
                                        if (entityNBT.get(nbtx.getTag()) != null) {
                                            boolean tagComparex = entityNBT.get(nbtx.getTag()).getAsString().contains(nbtx.getValue());
                                            entity.getPersistentData().putBoolean("availableForDialog", tagComparex);
                                            if (tagComparex) {
                                                return true;
                                            }
                                        }
                                    }
                                    break;
                                case AND:
                                    boolean shouldAddMarker = false;
                                    Iterator nbt = conditions.getNbt().iterator();
                                    while (true) {
                                        if (nbt.hasNext()) {
                                            EntityTypeTagDialog.EntityTypeTagDialogCondition.EntityTypeTagDialogNBT nbtx = (EntityTypeTagDialog.EntityTypeTagDialogCondition.EntityTypeTagDialogNBT) nbt.next();
                                            boolean tagCompare;
                                            if (entityNBT.get(nbtx.getTag()) != null) {
                                                tagCompare = entityNBT.get(nbtx.getTag()).getAsString().contains(nbtx.getValue());
                                            } else {
                                                tagCompare = false;
                                            }
                                            shouldAddMarker = tagCompare;
                                            if (tagCompare) {
                                                continue;
                                            }
                                        }
                                        entity.getPersistentData().putBoolean("availableForDialog", shouldAddMarker);
                                        if (shouldAddMarker) {
                                            return true;
                                        }
                                        break;
                                    }
                            }
                        }
                    } catch (IOException var15) {
                        throw new RuntimeException(var15);
                    }
                }
            }
            if (entity.getPersistentData().getBoolean("beingRenderedOnInventory")) {
                return false;
            }
            if (entity.getPersistentData().contains("availableForDialog")) {
                return entity.getPersistentData().getBoolean("availableForDialog");
            }
        }
        return false;
    }
}