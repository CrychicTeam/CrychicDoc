package se.mickelus.tetra.blocks.workbench.gui;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringSmall;
import se.mickelus.mutil.gui.GuiTextureOffset;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.mutil.gui.impl.GuiHorizontalLayoutGroup;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.module.ItemModuleMajor;
import se.mickelus.tetra.module.data.GlyphData;
import se.mickelus.tetra.module.data.ImprovementData;

@ParametersAreNonnullByDefault
public class GuiModuleMajor extends GuiModule {

    private GuiStringSmall slotString;

    private GuiHorizontalLayoutGroup improvementGroup;

    public GuiModuleMajor(int x, int y, GuiAttachment attachmentPoint, ItemStack itemStack, ItemStack previewStack, String slotKey, String slotName, ItemModuleMajor module, ItemModuleMajor previewModule, Consumer<String> slotClickHandler, BiConsumer<String, String> hoverHandler) {
        super(x, y, attachmentPoint, itemStack, previewStack, slotKey, slotName, module, previewModule, slotClickHandler, hoverHandler);
        this.height = 17;
        this.improvementGroup = new GuiHorizontalLayoutGroup(GuiAttachment.topRight.equals(attachmentPoint) ? -17 : 19, "".equals(slotName) ? 12 : 13, 3, 1);
        this.improvementGroup.setAttachment(attachmentPoint);
        this.addChild(this.improvementGroup);
        if (module != null && previewModule != null) {
            this.setupImprovements(previewModule, previewStack, module, itemStack);
        }
    }

    public static String[] getImprovementUnion(ImprovementData[] improvements, ImprovementData[] previewImprovements) {
        return (String[]) Stream.concat(Arrays.stream(improvements), Arrays.stream(previewImprovements)).map(improvement -> improvement.key).distinct().toArray(String[]::new);
    }

    public static List<Enchantment> getEnchantmentUnion(Set<Enchantment> enchantments, Set<Enchantment> previewEnchantments) {
        return (List<Enchantment>) Stream.concat(enchantments.stream(), previewEnchantments.stream()).distinct().collect(Collectors.toList());
    }

    @Override
    protected void setupChildren(String moduleName, GlyphData glyphData, String slotName, boolean tweakable) {
        this.backdrop = new GuiModuleBackdrop(1, 0, 16777215);
        this.backdrop.setAttachment(this.attachmentPoint);
        this.addChild(this.backdrop);
        if (tweakable) {
            this.tweakingIndicator = new GuiTextureOffset(1, 0, 15, 15, 176, 32, GuiTextures.workbench);
            this.tweakingIndicator.setAttachment(this.attachmentPoint);
            this.addChild(this.tweakingIndicator);
        }
        this.moduleString = new GuiString(19, "".equals(slotName) ? 4 : 5, "");
        if (moduleName != null) {
            this.moduleString.setString(moduleName);
        } else {
            this.moduleString.setString(I18n.get("item.tetra.modular.empty_slot"));
        }
        if (GuiAttachment.topRight.equals(this.attachmentPoint)) {
            this.moduleString.setX(-16);
        }
        this.moduleString.setAttachment(this.attachmentPoint);
        this.addChild(this.moduleString);
        this.slotString = new GuiStringSmall(19, 0, slotName);
        if (GuiAttachment.topRight.equals(this.attachmentPoint)) {
            this.slotString.setX(-16);
        }
        this.slotString.setAttachment(this.attachmentPoint);
        this.addChild(this.slotString);
        this.width = this.moduleString.getWidth() + 19;
        if (glyphData != null) {
            this.glyph = new GuiModuleGlyph(0, 0, 16, 16, glyphData.tint, glyphData.textureX, glyphData.textureY, glyphData.textureLocation);
            if (GuiAttachment.topRight.equals(this.attachmentPoint)) {
                this.glyph.setX(1);
            }
            this.glyph.setAttachment(this.attachmentPoint);
            this.addChild(this.glyph);
        }
    }

    @Override
    public void showAnimation(int offset) {
        if (this.isVisible()) {
            int direction = this.attachmentPoint == GuiAttachment.topLeft ? -2 : 2;
            new KeyframeAnimation(100, this.backdrop).withDelay(offset * 80).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateX((float) direction, 0.0F, true)).start();
            if (this.glyph != null) {
                new KeyframeAnimation(100, this.glyph).withDelay(offset * 80 + 100).applyTo(new Applier.Opacity(0.0F, 1.0F)).start();
            }
            if (this.tweakingIndicator != null) {
                new KeyframeAnimation(100, this.tweakingIndicator).withDelay(offset * 80 + 100).applyTo(new Applier.Opacity(0.0F, 1.0F)).start();
            }
            new KeyframeAnimation(100, this.moduleString).withDelay(offset * 80 + 200).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateX((float) (direction * 2), 0.0F, true)).start();
            new KeyframeAnimation(100, this.slotString).withDelay(offset * 80 + 100).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateX((float) (direction * 2), 0.0F, true)).start();
            for (int i = 0; i < this.improvementGroup.getNumChildren(); i++) {
                GuiElement element = this.improvementGroup.getChild(i);
                element.setOpacity(0.0F);
                new KeyframeAnimation(100, element).withDelay(offset * 200 + 280 + i * 80).applyTo(new Applier.Opacity(1.0F)).start();
            }
        }
    }

    private void setupImprovements(ItemModuleMajor previewModule, ItemStack previewStack, ItemModuleMajor module, ItemStack itemStack) {
        this.improvementGroup.clearChildren();
        String[] improvements = getImprovementUnion(module.getImprovements(itemStack), previewModule.getImprovements(previewStack));
        for (String improvementKey : improvements) {
            int currentValue = module.getImprovementLevel(itemStack, improvementKey);
            int previewValue = previewModule.getImprovementLevel(previewStack, improvementKey);
            int color;
            if (currentValue == -1) {
                color = 11206570;
            } else if (previewValue == -1) {
                color = 16755370;
            } else if (currentValue != previewValue) {
                color = 11184895;
            } else {
                color = module.getImprovement(itemStack, improvementKey).glyph.tint;
            }
            GuiModuleImprovement improvement = new GuiModuleImprovement(0, 0, improvementKey, previewValue, color, () -> this.hoverHandler.accept(this.slotKey, improvementKey), () -> {
                if (this.hasFocus()) {
                    this.hoverHandler.accept(this.slotKey, null);
                }
            });
            this.improvementGroup.addChild(improvement);
        }
        Map<Enchantment, Integer> currentEnchantments = module.getEnchantments(itemStack);
        Map<Enchantment, Integer> previewEnchantments = module.getEnchantments(previewStack);
        getEnchantmentUnion(currentEnchantments.keySet(), previewEnchantments.keySet()).forEach(enchantment -> {
            int currentLevel = (Integer) currentEnchantments.getOrDefault(enchantment, 0);
            int previewLevel = (Integer) previewEnchantments.getOrDefault(enchantment, 0);
            int colorx;
            if (currentLevel == 0) {
                colorx = 11206570;
            } else if (previewLevel == 0) {
                colorx = 16755370;
            } else if (currentLevel != previewLevel) {
                colorx = 11184895;
            } else {
                colorx = 16777215;
            }
            String enchantmentKey = "enchantment:" + ForgeRegistries.ENCHANTMENTS.getKey(enchantment).toString();
            this.improvementGroup.addChild(new GuiModuleEnchantment(0, 0, enchantment, previewLevel, colorx, () -> this.hoverHandler.accept(this.slotKey, enchantmentKey), () -> {
                if (this.hasFocus()) {
                    this.hoverHandler.accept(this.slotKey, null);
                }
            }));
        });
        this.improvementGroup.forceLayout();
    }

    @Override
    protected void setColor(int color) {
        super.setColor(color);
        this.slotString.setColor(color);
        this.improvementGroup.setOpacity(color == 8355711 ? 0.5F : 1.0F);
    }
}