package org.violetmoon.quark.content.tweaks.client.emote;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public final class EmoteHandler {

    public static final String CUSTOM_EMOTE_NAMESPACE = "quark_custom";

    public static final String CUSTOM_PREFIX = "custom:";

    public static final Map<String, EmoteDescriptor> emoteMap = new LinkedHashMap();

    private static final Map<String, EmoteBase> playerEmotes = new HashMap();

    private static int count;

    public static void clearEmotes() {
        emoteMap.clear();
    }

    public static void addEmote(String name, Class<? extends EmoteBase> clazz) {
        EmoteDescriptor desc = new EmoteDescriptor(clazz, name, name, count++);
        emoteMap.put(name, desc);
    }

    public static void addEmote(String name) {
        addEmote(name, TemplateSourcedEmote.class);
    }

    public static void addCustomEmote(String name) {
        String reg = "custom:" + name;
        EmoteDescriptor desc = new CustomEmoteDescriptor(name, reg, count++);
        emoteMap.put(reg, desc);
    }

    public static void putEmote(Entity player, String emoteName, int tier) {
        if (player instanceof AbstractClientPlayer clientPlayer && emoteMap.containsKey(emoteName)) {
            putEmote(clientPlayer, (EmoteDescriptor) emoteMap.get(emoteName), tier);
        }
    }

    private static void putEmote(AbstractClientPlayer player, EmoteDescriptor desc, int tier) {
        String name = player.m_36316_().getName();
        if (desc != null) {
            if (desc.getTier() <= tier) {
                HumanoidModel<?> model = getPlayerModel(player);
                HumanoidModel<?> armorModel = getPlayerArmorModel(player);
                HumanoidModel<?> armorLegModel = getPlayerArmorLegModel(player);
                if (model != null && armorModel != null && armorLegModel != null) {
                    resetPlayer(player);
                    EmoteBase emote = desc.instantiate(player, model, armorModel, armorLegModel);
                    emote.startAllTimelines();
                    playerEmotes.put(name, emote);
                }
            }
        }
    }

    public static void updateEmotes(Entity e) {
        if (e instanceof AbstractClientPlayer player) {
            String name = player.m_36316_().getName();
            if (player.m_20089_() == Pose.STANDING && playerEmotes.containsKey(name)) {
                resetPlayer(player);
                EmoteBase emote = (EmoteBase) playerEmotes.get(name);
                boolean done = emote.isDone();
                if (!done) {
                    emote.update();
                }
            }
        }
    }

    public static void preRender(PoseStack stack, Player player) {
        EmoteBase emote = getPlayerEmote(player);
        if (emote != null) {
            stack.pushPose();
            emote.rotateAndOffset(stack);
        }
    }

    public static void postRender(PoseStack stack, Player player) {
        EmoteBase emote = getPlayerEmote(player);
        if (emote != null) {
            stack.popPose();
        }
    }

    public static void onRenderTick(Minecraft mc) {
        Level world = mc.level;
        if (world != null) {
            for (Player player : world.m_6907_()) {
                updatePlayerStatus(player);
            }
        }
    }

    private static void updatePlayerStatus(Player e) {
        if (e instanceof AbstractClientPlayer player) {
            String name = player.m_36316_().getName();
            if (playerEmotes.containsKey(name)) {
                EmoteBase emote = (EmoteBase) playerEmotes.get(name);
                boolean done = emote.isDone();
                if (done) {
                    playerEmotes.remove(name);
                    resetPlayer(player);
                } else {
                    emote.update();
                }
            }
        }
    }

    public static EmoteBase getPlayerEmote(Player player) {
        return (EmoteBase) playerEmotes.get(player.getGameProfile().getName());
    }

    private static PlayerRenderer getRenderPlayer(AbstractClientPlayer player) {
        Minecraft mc = Minecraft.getInstance();
        EntityRenderDispatcher manager = mc.getEntityRenderDispatcher();
        EntityRenderer<? extends Player> render = (EntityRenderer<? extends Player>) manager.getSkinMap().get(player.getModelName());
        return render instanceof PlayerRenderer ? (PlayerRenderer) render : null;
    }

    private static HumanoidModel<?> getPlayerModel(AbstractClientPlayer player) {
        PlayerRenderer render = getRenderPlayer(player);
        return render != null ? (HumanoidModel) render.m_7200_() : null;
    }

    private static HumanoidModel<?> getPlayerArmorModel(AbstractClientPlayer player) {
        return getPlayerArmorModelForSlot(player, EquipmentSlot.CHEST);
    }

    private static HumanoidModel<?> getPlayerArmorLegModel(AbstractClientPlayer player) {
        return getPlayerArmorModelForSlot(player, EquipmentSlot.LEGS);
    }

    private static HumanoidModel<?> getPlayerArmorModelForSlot(AbstractClientPlayer player, EquipmentSlot slot) {
        PlayerRenderer render = getRenderPlayer(player);
        if (render == null) {
            return null;
        } else {
            for (RenderLayer<?, ?> r : render.f_115291_) {
                if (r instanceof HumanoidArmorLayer) {
                    return ((HumanoidArmorLayer) r).getArmorModel(slot);
                }
            }
            return null;
        }
    }

    private static void resetPlayer(AbstractClientPlayer player) {
        resetModel(getPlayerModel(player));
        resetModel(getPlayerArmorModel(player));
        resetModel(getPlayerArmorLegModel(player));
    }

    private static void resetModel(HumanoidModel<?> model) {
        if (model != null) {
            resetPart(model.head);
            resetPart(model.hat);
            resetPart(model.body);
            resetPart(model.leftArm);
            resetPart(model.rightArm);
            resetPart(model.leftLeg);
            resetPart(model.rightLeg);
            if (model instanceof PlayerModel<?> pmodel) {
                resetPart(pmodel.jacket);
                resetPart(pmodel.leftSleeve);
                resetPart(pmodel.rightSleeve);
                resetPart(pmodel.leftPants);
                resetPart(pmodel.rightPants);
            }
            ModelAccessor.INSTANCE.resetModel(model);
        }
    }

    private static void resetPart(ModelPart part) {
        if (part != null) {
            part.zRot = 0.0F;
        }
    }
}