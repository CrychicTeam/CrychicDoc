package se.mickelus.tetra.gui;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.StringUtils;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.Tooltips;
import se.mickelus.tetra.aspect.ItemAspect;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.ItemModule;
import se.mickelus.tetra.module.data.AspectData;

@ParametersAreNonnullByDefault
public class AspectIconGui extends GuiElement {

    protected List<Component> tooltip;

    protected int detailOffset;

    protected List<Component> detailTooltip;

    protected List<ItemAspect> aspects;

    public AspectIconGui(int x, int y) {
        super(x, y, 10, 10);
        GuiTexture indicator = new GuiTexture(0, 0, this.width, this.height, 208, 32, GuiTextures.workbench);
        this.addChild(indicator);
    }

    public void update(ItemStack itemStack, ItemModule module) {
        AspectData aspects = module.getAspects(itemStack);
        this.tooltip = new ArrayList();
        this.detailOffset = 0;
        if (aspects != null && !aspects.getValues().isEmpty()) {
            this.tooltip.add(Component.translatable("tetra.modular.aspects.header").withStyle(ChatFormatting.GRAY));
            aspects.getLevelMap().forEach((aspect, level) -> {
                Component levelString = Component.translatable("enchantment.level." + level);
                this.tooltip.add(Component.literal("  ").append(this.getAspectLabel(aspect.getKey())).append(Component.literal(" ")).append(levelString));
            });
            this.tooltip.add(Component.literal(" "));
            this.tooltip.add(Tooltips.expand);
            this.aspects = new ArrayList(aspects.getValues());
            this.updateDetailTooltip();
        } else {
            this.tooltip.add(Component.translatable("tetra.modular.aspects.empty").withStyle(ChatFormatting.GRAY));
            this.detailTooltip = null;
        }
    }

    public void update(ItemStack itemStack, String slot) {
        CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getModuleFromSlot(itemStack, slot)).ifPresent(module -> this.update(itemStack, module));
    }

    public void updateDetailTooltip() {
        ItemAspect aspect = (ItemAspect) this.aspects.get(this.detailOffset);
        this.detailTooltip = new ArrayList();
        this.detailTooltip.add(Component.translatable("tetra.modular.aspects.detail_header", this.detailOffset + 1, this.aspects.size()).withStyle(ChatFormatting.GRAY));
        this.detailTooltip.add(Component.literal(" "));
        this.detailTooltip.add(this.getAspectLabel(aspect.getKey()).withStyle(ChatFormatting.YELLOW));
        this.detailTooltip.add(this.getAspectDescription(aspect.getKey()));
        this.detailTooltip.add(Component.literal(" "));
        this.detailTooltip.add(Tooltips.expanded);
        if (this.aspects.size() > 1) {
            this.detailTooltip.add(Component.translatable("tetra.modular.aspects.scroll_tooltip"));
        }
    }

    private MutableComponent getAspectLabel(String key) {
        String localizationKey = "tetra.aspect." + key;
        return I18n.exists(localizationKey) ? Component.translatable(localizationKey) : Component.literal(StringUtils.capitalize(key.replace("_", " ")));
    }

    private MutableComponent getAspectDescription(String key) {
        String localizationKey = "tetra.aspect." + key + ".description";
        return I18n.exists(localizationKey) ? Component.translatable(localizationKey) : Component.translatable("tetra.modular.aspects.missing_description").withStyle(ChatFormatting.GRAY);
    }

    @Override
    public List<Component> getTooltipLines() {
        if (this.hasFocus()) {
            return Screen.hasShiftDown() && !this.aspects.isEmpty() ? this.detailTooltip : this.tooltip;
        } else {
            return super.getTooltipLines();
        }
    }

    @Override
    public boolean onMouseScroll(double mouseX, double mouseY, double distance) {
        if (this.hasFocus()) {
            if (this.aspects.size() > 1) {
                this.detailOffset = Mth.clamp(this.detailOffset + (int) Math.signum(-distance), 0, this.aspects.size() - 1);
                this.updateDetailTooltip();
            }
            return true;
        } else {
            return false;
        }
    }
}