package net.blay09.mods.waystones.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class WaystoneButton extends Button {

    private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/enchanting_table.png");

    private final int xpLevelCost;

    private final IWaystone waystone;

    public WaystoneButton(int x, int y, IWaystone waystone, int xpLevelCost, Button.OnPress pressable) {
        super(x, y, 200, 20, getWaystoneNameComponent(waystone), pressable, Button.DEFAULT_NARRATION);
        Player player = Minecraft.getInstance().player;
        this.xpLevelCost = xpLevelCost;
        this.waystone = waystone;
        if (player == null || !PlayerWaystoneManager.mayTeleportToWaystone(player, waystone)) {
            this.f_93623_ = false;
        } else if (player.experienceLevel < xpLevelCost && !player.getAbilities().instabuild) {
            this.f_93623_ = false;
        }
    }

    private static Component getWaystoneNameComponent(IWaystone waystone) {
        String effectiveName = waystone.getName();
        if (effectiveName.isEmpty()) {
            effectiveName = I18n.get("gui.waystones.waystone_selection.unnamed_waystone");
        }
        MutableComponent textComponent = Component.literal(effectiveName);
        if (waystone.isGlobal()) {
            textComponent.withStyle(ChatFormatting.YELLOW);
        }
        return textComponent;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.m_87963_(guiGraphics, mouseX, mouseY, partialTicks);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft mc = Minecraft.getInstance();
        if (this.waystone.getDimension() == mc.player.m_9236_().dimension() && this.m_142518_()) {
            int distance = (int) mc.player.m_20182_().distanceTo(this.waystone.getPos().getCenter());
            String distanceStr;
            if (distance >= 10000 || mc.font.width(this.m_6035_()) >= 120 && distance >= 1000) {
                distanceStr = String.format("%.1f", (float) distance / 1000.0F).replace(",0", "").replace(".0", "") + "km";
            } else {
                distanceStr = distance + "m";
            }
            int xOffset = this.m_5711_() - mc.font.width(distanceStr);
            guiGraphics.drawString(mc.font, distanceStr, this.m_252754_() + xOffset - 4, this.m_252907_() + 6, 16777215);
        }
        if (this.xpLevelCost > 0) {
            boolean canAfford = ((LocalPlayer) Objects.requireNonNull(mc.player)).f_36078_ >= this.xpLevelCost || mc.player.m_150110_().instabuild;
            guiGraphics.blit(ENCHANTMENT_TABLE_GUI_TEXTURE, this.m_252754_() + 2, this.m_252907_() + 2, (Math.min(this.xpLevelCost, 3) - 1) * 16, 223 + (!canAfford ? 16 : 0), 16, 16);
            if (this.xpLevelCost > 3) {
                guiGraphics.drawString(mc.font, "+", this.m_252754_() + 17, this.m_252907_() + 6, 13172623);
            }
            if (this.f_93622_ && mouseX <= this.m_252754_() + 16) {
                List<Component> tooltip = new ArrayList();
                MutableComponent levelRequirementText = Component.translatable("gui.waystones.waystone_selection.level_requirement", this.xpLevelCost);
                levelRequirementText.withStyle(canAfford ? ChatFormatting.GREEN : ChatFormatting.RED);
                tooltip.add(levelRequirementText);
                guiGraphics.renderTooltip(mc.font, tooltip, Optional.empty(), mouseX, mouseY + 9);
            }
        }
    }
}