package se.mickelus.tetra.blocks.workbench.gui;

import java.util.Arrays;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiTextSmall;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.AnimationChain;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.gui.AspectIconGui;
import se.mickelus.tetra.gui.GuiMagicUsage;
import se.mickelus.tetra.gui.GuiSettleProgress;
import se.mickelus.tetra.gui.GuiSynergyIndicator;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.module.ItemModule;
import se.mickelus.tetra.module.ItemModuleMajor;
import se.mickelus.tetra.module.RepairRegistry;
import se.mickelus.tetra.module.data.GlyphData;

@ParametersAreNonnullByDefault
public class GuiModuleDetails extends GuiElement {

    private final GuiElement glyph;

    private final GuiElement wrapper;

    private final GuiString title;

    private final GuiTextSmall description;

    private final GuiString emptyLabel;

    private final GuiMagicUsage magicBar;

    private final GuiSettleProgress settleBar;

    private final GuiSynergyIndicator synergyIndicator;

    private final AspectIconGui aspectIcon;

    private final RepairInfoGui repairInfo;

    private final AnimationChain flash;

    public GuiModuleDetails(int x, int y) {
        super(x, y, 224, 67);
        this.addChild(new GuiTexture(-4, -4, 239, 69, 0, 118, GuiTextures.workbench));
        this.wrapper = new GuiElement(x, y, this.width, this.height);
        this.addChild(this.wrapper);
        this.glyph = new GuiElement(3, 3, 16, 16);
        this.wrapper.addChild(this.glyph);
        this.title = new GuiString(20, 7, 106, "");
        this.wrapper.addChild(this.title);
        this.description = new GuiTextSmall(5, 19, 121, "");
        this.wrapper.addChild(this.description);
        this.emptyLabel = new GuiString(-44, -3, ChatFormatting.DARK_GRAY + I18n.get("tetra.workbench.module_detail.empty"));
        this.emptyLabel.setAttachment(GuiAttachment.middleCenter);
        this.wrapper.addChild(this.emptyLabel);
        this.synergyIndicator = new GuiSynergyIndicator(137, 10);
        this.wrapper.addChild(this.synergyIndicator);
        this.aspectIcon = new AspectIconGui(156, 11);
        this.wrapper.addChild(this.aspectIcon);
        this.repairInfo = new RepairInfoGui(173, 7);
        this.wrapper.addChild(this.repairInfo);
        this.magicBar = new GuiMagicUsage(138, 33, 80);
        this.wrapper.addChild(this.magicBar);
        this.settleBar = new GuiSettleProgress(138, 49, 80);
        this.wrapper.addChild(this.settleBar);
        GuiTexture flashOverlay = new GuiTexture(-4, -4, 239, 69, 0, 118, GuiTextures.workbench);
        flashOverlay.setColor(0);
        this.addChild(flashOverlay);
        this.flash = new AnimationChain(new KeyframeAnimation(40, flashOverlay).applyTo(new Applier.Opacity(0.3F)), new KeyframeAnimation(80, flashOverlay).applyTo(new Applier.Opacity(0.0F)));
    }

    public void update(@Nullable ItemModule module, ItemStack itemStack) {
        this.glyph.clearChildren();
        if (module != null) {
            this.title.setString(module.getName(itemStack));
            this.description.setString(ChatFormatting.GRAY + module.getDescription(itemStack).replace(ChatFormatting.RESET.toString(), ChatFormatting.GRAY.toString()));
            GlyphData glyphData = module.getVariantData(itemStack).glyph;
            if (module instanceof ItemModuleMajor majorModule) {
                this.glyph.addChild(new GuiTexture(0, 0, 15, 15, 52, 0, GuiTextures.workbench));
                this.glyph.addChild(new GuiModuleGlyph(-1, 0, 16, 16, glyphData).setShift(false));
                this.settleBar.update(itemStack, majorModule);
            } else {
                this.glyph.addChild(new GuiTexture(3, 2, 11, 11, 68, 0, GuiTextures.workbench));
                this.glyph.addChild(new GuiModuleGlyph(5, 4, 8, 8, glyphData).setShift(false));
            }
            this.magicBar.update(itemStack, ItemStack.EMPTY, module.getSlot());
            this.synergyIndicator.update(itemStack, module);
            this.aspectIcon.update(itemStack, module);
            ItemStack[] repairItemStacks = (ItemStack[]) RepairRegistry.instance.getDefinitions(module.getVariantData(itemStack).key).stream().map(definition -> definition.material.getApplicableItemStacks()).flatMap(Arrays::stream).toArray(ItemStack[]::new);
            this.repairInfo.update(repairItemStacks);
        }
        this.synergyIndicator.setVisible(module != null);
        this.aspectIcon.setVisible(module != null);
        this.title.setVisible(module != null);
        this.description.setVisible(module != null);
        this.settleBar.setVisible(module instanceof ItemModuleMajor);
        this.magicBar.setVisible(module instanceof ItemModuleMajor);
        this.emptyLabel.setVisible(module == null);
        this.repairInfo.setVisible(module != null);
        this.flash();
    }

    public void flash() {
        this.flash.stop();
        this.flash.start();
    }
}