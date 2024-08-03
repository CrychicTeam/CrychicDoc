package dev.xkmc.modulargolems.content.menu.attribute;

import dev.xkmc.l2tabs.init.data.L2TabsLangData;
import dev.xkmc.l2tabs.tabs.contents.AttributeEntry;
import dev.xkmc.l2tabs.tabs.contents.BaseTextScreen;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.menu.registry.EquipmentGroup;
import dev.xkmc.modulargolems.content.menu.registry.GolemTabRegistry;
import dev.xkmc.modulargolems.content.menu.tabs.GolemTabManager;
import dev.xkmc.modulargolems.content.menu.tabs.ITabScreen;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.Lazy;

public class AttributeScreen extends BaseTextScreen implements ITabScreen {

    public static final Lazy<List<AttributeEntry>> ATTRIBUTES = Lazy.of(() -> List.of(new AttributeEntry(Attributes.MAX_HEALTH, false, 0, 0.0), new AttributeEntry(Attributes.ATTACK_DAMAGE, false, 1, 0.0), new AttributeEntry(Attributes.ARMOR, false, 2, 0.0), new AttributeEntry(Attributes.ARMOR_TOUGHNESS, false, 3, 0.0), new AttributeEntry(Attributes.KNOCKBACK_RESISTANCE, false, 4, 0.0), new AttributeEntry(Attributes.MOVEMENT_SPEED, false, 5, 0.0), new AttributeEntry(ForgeMod.ENTITY_REACH.get(), false, 6, 0.0), new AttributeEntry((Attribute) GolemTypes.GOLEM_REGEN.get(), false, 7, 0.0), new AttributeEntry((Attribute) GolemTypes.GOLEM_SWEEP.get(), false, 8, 0.0), new AttributeEntry((Attribute) GolemTypes.GOLEM_SIZE.get(), false, 9, 0.0), new AttributeEntry((Attribute) GolemTypes.GOLEM_JUMP.get(), false, 10, 0.0)));

    private final AbstractGolemEntity<?, ?> golem;

    public AttributeScreen(AbstractGolemEntity<?, ?> golem, Component title) {
        super(title, new ResourceLocation("l2tabs:textures/gui/empty.png"));
        this.golem = golem;
    }

    public void init() {
        super.m_7856_();
        new GolemTabManager<>(this, new EquipmentGroup(this.golem)).init(x$0 -> {
            AbstractWidget var10000 = (AbstractWidget) this.m_142416_(x$0);
        }, GolemTabRegistry.ATTRIBUTE);
    }

    public void render(GuiGraphics g, int mx, int my, float ptick) {
        super.m_88315_(g, mx, my, ptick);
        int x = this.leftPos + 8;
        int y = this.topPos + 6;
        Attribute focus = null;
        for (AttributeEntry entry : (List) ATTRIBUTES.get()) {
            if (this.golem.m_21051_(entry.attr()) != null) {
                double val = this.golem.m_21133_(entry.attr());
                Component comp = Component.translatable("attribute.modifier.equals." + (entry.usePercent() ? 1 : 0), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(entry.usePercent() ? val * 100.0 : val), Component.translatable(entry.attr().getDescriptionId()));
                g.drawString(this.f_96547_, comp, x, y, 0, false);
                if (mx > x && mx < this.leftPos + this.imageWidth && my > y && my < y + 10) {
                    focus = entry.attr();
                }
                y += 10;
            }
        }
        if (focus != null) {
            g.renderComponentTooltip(this.f_96547_, this.getAttributeDetail(focus), mx, my);
        }
    }

    public List<Component> getAttributeDetail(Attribute attr) {
        AttributeInstance ins = this.golem.m_21051_(attr);
        if (ins == null) {
            return List.of();
        } else {
            Set<AttributeModifier> adds = ins.getModifiers(AttributeModifier.Operation.ADDITION);
            Set<AttributeModifier> m0s = ins.getModifiers(AttributeModifier.Operation.MULTIPLY_BASE);
            Set<AttributeModifier> m1s = ins.getModifiers(AttributeModifier.Operation.MULTIPLY_TOTAL);
            double base = ins.getBaseValue();
            double addv = 0.0;
            double m0v = 0.0;
            double m1v = 1.0;
            for (AttributeModifier e : adds) {
                addv += e.getAmount();
            }
            for (AttributeModifier e : m0s) {
                m0v += e.getAmount();
            }
            for (AttributeModifier e : m1s) {
                m1v *= 1.0 + e.getAmount();
            }
            double total = (base + addv) * (1.0 + m0v) * m1v;
            List<Component> ans = new ArrayList();
            ans.add(Component.translatable(attr.getDescriptionId()).withStyle(ChatFormatting.GOLD));
            boolean shift = Screen.hasShiftDown();
            ans.add(L2TabsLangData.BASE.get(new Object[] { number("%s", base) }).withStyle(ChatFormatting.BLUE));
            ans.add(L2TabsLangData.ADD.get(new Object[] { numberSigned("%s", addv) }).withStyle(ChatFormatting.BLUE));
            if (shift) {
                for (AttributeModifier e : adds) {
                    ans.add(numberSigned("%s", e.getAmount()).append(name(e)));
                }
            }
            ans.add(L2TabsLangData.MULT_BASE.get(new Object[] { numberSigned("%s%%", m0v * 100.0) }).withStyle(ChatFormatting.BLUE));
            if (shift) {
                for (AttributeModifier e : m0s) {
                    ans.add(numberSigned("%s%%", e.getAmount() * 100.0).append(name(e)));
                }
            }
            ans.add(L2TabsLangData.MULT_TOTAL.get(new Object[] { number("x%s", m1v) }).withStyle(ChatFormatting.BLUE));
            if (shift) {
                for (AttributeModifier e : m1s) {
                    ans.add(number("x%s", 1.0 + e.getAmount()).append(name(e)));
                }
            }
            ans.add(L2TabsLangData.FORMAT.get(new Object[] { number("%s", base), numberSigned("%s", addv), numberSigned("%s", m0v), number("%s", m1v), number("%s", total) }));
            if (!shift) {
                ans.add(L2TabsLangData.DETAIL.get(new Object[0]).withStyle(ChatFormatting.GRAY));
            }
            return ans;
        }
    }

    private static MutableComponent number(String base, double value) {
        return Component.literal(String.format(base, ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(value))).withStyle(ChatFormatting.GREEN);
    }

    private static MutableComponent numberSigned(String base, double value) {
        return value >= 0.0 ? Component.literal(String.format("+" + base, ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(value))).withStyle(ChatFormatting.GREEN) : Component.literal(String.format("-" + base, ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(-value))).withStyle(ChatFormatting.RED);
    }

    private static MutableComponent name(AttributeModifier e) {
        return Component.literal("  (" + e.getName() + ")").withStyle(ChatFormatting.DARK_GRAY);
    }

    @Override
    public int getGuiLeft() {
        return this.leftPos;
    }

    @Override
    public int getGuiTop() {
        return this.topPos;
    }

    @Override
    public int screenWidth() {
        return this.f_96543_;
    }

    @Override
    public int screenHeight() {
        return this.f_96544_;
    }

    @Override
    public int getXSize() {
        return this.imageWidth;
    }

    @Override
    public int getYSize() {
        return this.imageHeight;
    }
}