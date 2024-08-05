package dev.xkmc.modulargolems.content.client.overlay;

import dev.xkmc.l2itemselector.select.item.ItemSelectionOverlay;
import dev.xkmc.l2library.base.overlay.OverlayUtil;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.raytrace.IGlowingTarget;
import dev.xkmc.l2library.util.raytrace.RayTraceUtil;
import dev.xkmc.modulargolems.compat.materials.botania.BotUtils;
import dev.xkmc.modulargolems.content.capability.GolemConfigEntry;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.GolemFlags;
import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import dev.xkmc.modulargolems.content.item.wand.GolemInteractItem;
import dev.xkmc.modulargolems.init.data.MGLangData;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class GolemStatusOverlay implements IGuiOverlay {

    @Override
    public void render(ForgeGui gui, GuiGraphics g, float partialTick, int screenWidth, int screenHeight) {
        if (Minecraft.getInstance().screen == null) {
            boolean offset = ItemSelectionOverlay.INSTANCE.isRendering();
            LocalPlayer player = Proxy.getClientPlayer();
            if (player != null) {
                if (player.m_21205_().getItem() instanceof GolemInteractItem wand) {
                    Entity target;
                    if (wand instanceof IGlowingTarget) {
                        target = RayTraceUtil.serverGetTarget(player);
                    } else {
                        if (!(Minecraft.getInstance().hitResult instanceof EntityHitResult entityHit)) {
                            return;
                        }
                        target = entityHit.getEntity();
                    }
                    if (target instanceof AbstractGolemEntity<?, ?> golem) {
                        gui.setupOverlayRenderState(true, false);
                        List<Component> text = new ArrayList();
                        text.add(golem.m_7755_());
                        if (golem.hasFlag(GolemFlags.BOTANIA)) {
                            text.add(BotUtils.getDesc(golem));
                        }
                        text.add(golem.getMode().getDesc(golem));
                        GolemConfigEntry config = golem.getConfigEntry(MGLangData.LOADING.get());
                        if (config != null) {
                            config.clientTick(player.m_9236_(), false);
                            text.add(config.getDisplayName());
                            if (config.locked) {
                                text.add(MGLangData.CONFIG_LOCK.get().withStyle(ChatFormatting.RED));
                            }
                        }
                        golem.getModifiers().forEach((k, v) -> text.add(k.getTooltip(v)));
                        int textPos = offset ? Math.round((float) (screenWidth * 3) / 4.0F) : Math.round((float) screenWidth / 8.0F);
                        new OverlayUtil(g, textPos, -1, -1).renderLongText(gui.m_93082_(), text);
                        if (golem instanceof HumanoidGolemEntity humanoid) {
                            OverlayUtil util = new OverlayUtil(g, (int) ((double) screenWidth * 0.6), -1, -1);
                            util.bg = -3750202;
                            List list = List.of(new GolemStatusOverlay.GolemEquipmentTooltip(humanoid));
                            util.renderTooltipInternal(gui.m_93082_(), list);
                        }
                    }
                }
            }
        }
    }

    private static record GolemEquipmentTooltip(HumanoidGolemEntity golem) implements ClientTooltipComponent {

        public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/gui/container/bundle.png");

        @Override
        public int getHeight() {
            return 72;
        }

        @Override
        public int getWidth(Font pFont) {
            return 36;
        }

        @Override
        public void renderImage(Font font, int mx, int my, GuiGraphics g) {
            this.renderSlot(font, mx, my + 18, g, this.golem.m_6844_(EquipmentSlot.MAINHAND), null);
            this.renderSlot(font, mx, my + 36, g, this.golem.m_6844_(EquipmentSlot.OFFHAND), InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
            this.renderSlot(font, mx + 18, my, g, this.golem.m_6844_(EquipmentSlot.HEAD), InventoryMenu.EMPTY_ARMOR_SLOT_HELMET);
            this.renderSlot(font, mx + 18, my + 18, g, this.golem.m_6844_(EquipmentSlot.CHEST), InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE);
            this.renderSlot(font, mx + 18, my + 36, g, this.golem.m_6844_(EquipmentSlot.LEGS), InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS);
            this.renderSlot(font, mx + 18, my + 54, g, this.golem.m_6844_(EquipmentSlot.FEET), InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS);
        }

        private void renderSlot(Font font, int x, int y, GuiGraphics g, ItemStack stack, @Nullable ResourceLocation atlasID) {
            this.blit(g, x, y);
            if (stack.isEmpty()) {
                if (atlasID != null) {
                    TextureAtlasSprite atlas = (TextureAtlasSprite) Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(atlasID);
                    g.blit(x + 1, y + 1, 100, 16, 16, atlas);
                }
            } else {
                g.renderItem(stack, x + 1, y + 1, 0);
                g.renderItemDecorations(font, stack, x + 1, y + 1);
            }
        }

        private void blit(GuiGraphics g, int x, int y) {
            g.blit(TEXTURE_LOCATION, x, y, 0, 0.0F, 0.0F, 18, 18, 128, 128);
        }
    }
}