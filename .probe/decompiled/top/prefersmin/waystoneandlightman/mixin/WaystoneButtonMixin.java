package top.prefersmin.waystoneandlightman.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.client.gui.widget.WaystoneButton;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.prefersmin.waystoneandlightman.util.CostUtil;
import top.prefersmin.waystoneandlightman.vo.TeleportCostVo;

@Mixin({ WaystoneButton.class })
public class WaystoneButtonMixin extends Button {

    @Final
    @Shadow(remap = false)
    private int xpLevelCost;

    @Final
    @Shadow(remap = false)
    private IWaystone waystone;

    @Unique
    private int wayStoneAndLightMan$distance;

    @Unique
    private TeleportCostVo wayStoneAndLightMan$teleportCost;

    @Unique
    private boolean wayStoneAndLightMan$isRender;

    @Unique
    private static final ResourceLocation COIN_GOLD = new ResourceLocation("lightmanscurrency", "textures/item/coin_gold.png");

    protected WaystoneButtonMixin(int pX, int pY, int pWidth, int pHeight, Component pMessage, Button.OnPress pOnPress, Button.CreateNarration pCreateNarration) {
        super(pX, pY, pWidth, pHeight, pMessage, pOnPress, pCreateNarration);
    }

    @Inject(method = { "<init>" }, at = { @At("TAIL") }, remap = false)
    private void WaystoneButton(int x, int y, IWaystone waystone, int xpLevelCost, Button.OnPress pressable, CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            this.wayStoneAndLightMan$distance = (int) player.m_20182_().distanceTo(waystone.getPos().getCenter());
            this.wayStoneAndLightMan$teleportCost = CostUtil.TeleportCostCalculate(player, this.wayStoneAndLightMan$distance);
            if (!this.wayStoneAndLightMan$teleportCost.isCanAfford() && !player.getAbilities().instabuild) {
                this.f_93623_ = false;
            }
            if (this.wayStoneAndLightMan$distance > 5) {
                this.wayStoneAndLightMan$isRender = true;
            }
        }
    }

    @Inject(method = { "m_87963_" }, at = { @At("HEAD") }, remap = false, cancellable = true)
    private void renderButton(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        super.m_87963_(guiGraphics, mouseX, mouseY, partialTicks);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft mc = Minecraft.getInstance();
        if (this.wayStoneAndLightMan$isRender) {
            if (this.waystone.getDimension() == mc.player.m_9236_().dimension()) {
                String distanceStr;
                if (this.wayStoneAndLightMan$distance >= 10000 || mc.font.width(this.m_6035_()) >= 120 && this.wayStoneAndLightMan$distance >= 1000) {
                    distanceStr = String.format("%.1f", (float) this.wayStoneAndLightMan$distance / 1000.0F).replace(",0", "").replace(".0", "") + "km";
                } else {
                    distanceStr = this.wayStoneAndLightMan$distance + "m";
                }
                int xOffset = this.m_5711_() - mc.font.width(distanceStr);
                guiGraphics.drawString(mc.font, distanceStr, this.m_252754_() + xOffset - 4, this.m_252907_() + 6, this.m_142518_() ? 16777215 : 10395294);
            }
            guiGraphics.blit(COIN_GOLD, this.m_252754_() + 2, this.m_252907_() + 2, 16.0F, 16.0F, 16, 16, 16, 16);
            if (this.f_93622_ && mouseX <= this.m_252754_() + 16) {
                List<Component> tooltip = new ArrayList();
                boolean haveXpLevelRequirement = this.xpLevelCost > 0;
                boolean haveMoneyRequirement = !this.wayStoneAndLightMan$teleportCost.getCost().isFree();
                boolean canXpLevelAfford = ((LocalPlayer) Objects.requireNonNull(mc.player)).f_36078_ >= this.xpLevelCost || mc.player.m_150110_().instabuild;
                if (haveXpLevelRequirement) {
                    MutableComponent levelRequirementText = Component.translatable("gui.waystones.waystone_selection.level_requirement", this.xpLevelCost);
                    levelRequirementText.withStyle(canXpLevelAfford ? ChatFormatting.GREEN : ChatFormatting.RED);
                    tooltip.add(levelRequirementText);
                }
                if (haveMoneyRequirement) {
                    MutableComponent moneyRequirementText = Component.translatable("gui.need", this.wayStoneAndLightMan$teleportCost.getCost().getString());
                    moneyRequirementText.withStyle(this.wayStoneAndLightMan$teleportCost.isCanAfford() ? ChatFormatting.GREEN : ChatFormatting.RED);
                    tooltip.add(moneyRequirementText);
                }
                if (!haveXpLevelRequirement && !haveMoneyRequirement) {
                    MutableComponent moneyRequirementText = Component.translatable("gui.free");
                    moneyRequirementText.withStyle(ChatFormatting.GREEN);
                    tooltip.add(moneyRequirementText);
                }
                guiGraphics.renderTooltip(mc.font, tooltip, Optional.empty(), mouseX, mouseY + 9);
            }
        }
        ci.cancel();
    }
}