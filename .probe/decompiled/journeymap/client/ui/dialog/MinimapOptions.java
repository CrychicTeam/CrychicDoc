package journeymap.client.ui.dialog;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import journeymap.client.Constants;
import journeymap.client.properties.ClientCategory;
import journeymap.client.properties.MiniMapProperties;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.component.Button;
import journeymap.client.ui.component.DraggableListPane;
import journeymap.client.ui.component.JmUI;
import journeymap.client.ui.component.ResetButton;
import journeymap.client.ui.component.ScrollListPane;
import journeymap.client.ui.minimap.Effect;
import journeymap.client.ui.minimap.MiniMap;
import journeymap.client.ui.minimap.Selectable;
import journeymap.client.ui.option.CategorySlot;
import journeymap.client.ui.option.OptionSlotFactory;
import journeymap.client.ui.option.SlotMetadata;
import journeymap.common.properties.PropertiesBase;
import journeymap.common.properties.catagory.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class MinimapOptions extends JmUI {

    protected Button buttonClose;

    protected Map<Category, List<SlotMetadata>> toolbars;

    protected DraggableListPane<CategorySlot> minimapPositionPane;

    private final MiniMapProperties miniMapProperties;

    private final MiniMap minimap;

    private final Effect effect;

    private Selectable selected;

    private Selectable notSelected;

    private Map<MobEffect, MobEffectInstance> activeEffects;

    private Map<Category, PropertiesBase> slotMap = Maps.newHashMap();

    private static final Map<MobEffect, MobEffectInstance> FAKE_EFFECT_MAP;

    public MinimapOptions(Screen returnDisplay, MiniMapProperties miniMapProperties) {
        super(Constants.getString("jm.common.minimap_options.title"), returnDisplay);
        this.miniMapProperties = miniMapProperties;
        UIManager.INSTANCE.switchMiniMapPreset(miniMapProperties.getId());
        this.minimap = UIManager.INSTANCE.getMiniMap();
        this.effect = Effect.getInstance();
        this.slotMap.put(ClientCategory.MinimapPosition, miniMapProperties);
        this.activeEffects = Maps.newHashMap(Minecraft.getInstance().player.m_21221_());
        Minecraft.getInstance().player.m_21221_().clear();
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.setRenderBottomBar(true);
        super.init(minecraft, width, height);
        this.minimapPositionPane = new DraggableListPane<>(this, this.f_96541_, 0, 0, 20, 35);
        List<CategorySlot> slotList = OptionSlotFactory.getOptionSlots(this.getToolbars(this.minimapPositionPane), this.slotMap, false, true);
        this.minimapPositionPane.setAlignTop(false);
        this.minimapPositionPane.m_93488_(false);
        this.minimapPositionPane.m_93496_(false);
        this.minimapPositionPane.setSlots(slotList);
        this.minimapPositionPane.updateSlots();
        this.buttonClose = (Button) this.m_142416_(new Button(Constants.getString("jm.common.close"), button -> this.closeAndReturn()));
        this.buttonClose.fitWidth(minecraft.font);
        this.buttonClose.setX(minecraft.getWindow().getGuiScaledWidth() / 2 - this.buttonClose.m_5711_() / 2);
        this.buttonClose.setY(minecraft.getWindow().getGuiScaledHeight() - 25);
    }

    protected Map<Category, List<SlotMetadata>> getToolbars(ScrollListPane<CategorySlot> pane) {
        if (this.toolbars == null) {
            this.toolbars = new HashMap();
            for (Category category : ClientCategory.values) {
                String name = Constants.getString("jm.config.reset");
                String tooltip = Constants.getString("jm.config.reset.tooltip");
                SlotMetadata toolbarSlotMetadata = new SlotMetadata(new ResetButton(category, button -> this.resetOptions(category, pane)), name, tooltip);
                this.toolbars.put(category, Arrays.asList(toolbarSlotMetadata));
            }
        }
        return this.toolbars;
    }

    protected void resetOptions(Category category, ScrollListPane<CategorySlot> pane) {
        Set<PropertiesBase> updatedProperties = new HashSet();
        for (CategorySlot categorySlot : pane.getRootSlots()) {
            if (category.equals(categorySlot.getCategory())) {
                for (SlotMetadata slotMetadata : categorySlot.getAllChildMetadata()) {
                    slotMetadata.resetToDefaultValue();
                    if (slotMetadata.hasConfigField()) {
                        PropertiesBase properties = slotMetadata.getProperties();
                        if (properties instanceof MiniMapProperties) {
                            this.miniMapProperties.effectTranslateX.setToDefault();
                            this.miniMapProperties.effectTranslateY.setToDefault();
                            this.miniMapProperties.positionX.set(Float.valueOf(0.82F));
                            this.miniMapProperties.positionY.set(Float.valueOf(0.05F));
                        }
                        this.miniMapProperties.effectTranslateX.set(Integer.valueOf(0));
                        if (properties != null) {
                            updatedProperties.add(properties);
                        }
                    }
                }
                break;
            }
        }
        for (PropertiesBase propertiesx : updatedProperties) {
            propertiesx.save();
        }
        this.minimapPositionPane.updateSlots();
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, float partialTicks) {
        this.minimap.drawMap(graphics, true);
        this.renderEffectBox(graphics);
        List<FormattedCharSequence> lastTooltip = this.minimapPositionPane.lastTooltip;
        long lastTooltipTime = this.minimapPositionPane.lastTooltipTime;
        this.minimapPositionPane.lastTooltip = null;
        this.minimapPositionPane.render(graphics, x, y, partialTicks);
        this.minimap.updateDisplayVars(true);
        super.render(graphics, x, y, partialTicks);
        this.renderTooltip(graphics, x, lastTooltipTime, lastTooltip);
        this.f_96541_.player.m_21221_().putAll(FAKE_EFFECT_MAP);
    }

    private void renderTooltip(GuiGraphics graphics, int x, long lastTooltipTime, List<FormattedCharSequence> lastTooltip) {
        if (this.minimapPositionPane.lastTooltip != null && !this.minimapPositionPane.lastTooltip.equals(lastTooltip)) {
            this.minimapPositionPane.lastTooltipTime = lastTooltipTime;
            if (System.currentTimeMillis() - this.minimapPositionPane.lastTooltipTime > this.minimapPositionPane.hoverDelay) {
                Button button = this.minimapPositionPane.lastTooltipMetadata.getButton();
                graphics.renderTooltip(this.f_96547_, this.minimapPositionPane.lastTooltip, x, button.getBottomY() + 15);
            }
        }
    }

    private void renderEffectBox(GuiGraphics graphics) {
        if (this.selected != null && this.notSelected != null) {
            this.selected.renderBorder(graphics, -16711936);
            this.notSelected.renderBorder(graphics, -65536);
        } else {
            this.minimap.renderBorder(graphics, -65536);
            this.effect.renderBorder(graphics, -65536);
        }
        DrawUtil.zLevel = 0.0;
    }

    @Override
    protected void closeAndReturn() {
        this.removeTempEffects();
        super.closeAndReturn();
    }

    @Override
    public void close() {
        this.removeTempEffects();
        super.close();
    }

    @Override
    public void onClose() {
        this.removeTempEffects();
        super.m_7379_();
    }

    private void removeTempEffects() {
        this.f_96541_.player.m_21221_().clear();
        this.f_96541_.player.m_21221_().putAll(this.activeEffects);
    }

    @Override
    protected void layoutButtons(GuiGraphics graphics) {
        if (this.getRenderables().isEmpty()) {
            this.m_7856_();
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (this.minimapPositionPane.mouseClicked(pMouseX, pMouseY, pButton)) {
            this.minimap.updateDisplayVars(true);
        } else {
            if (this.minimap.mouseClicked(pMouseX, pMouseY, pButton)) {
                this.selected = this.minimap;
                this.notSelected = this.effect;
                return true;
            }
            if (this.effect.mouseClicked(pMouseX, pMouseY, pButton)) {
                this.selected = this.effect;
                this.notSelected = this.minimap;
                return true;
            }
        }
        return super.m_6375_(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scroll) {
        this.minimapPositionPane.m_6050_(x, y, scroll);
        return super.m_6050_(x, y, scroll);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        return !this.minimapPositionPane.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY) && this.selected != null && this.selected.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY) ? true : super.m_7979_(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        this.minimapPositionPane.mouseReleased(mouseX, mouseY, mouseButton);
        this.minimap.mouseReleased(mouseX, mouseY, mouseButton);
        this.effect.mouseReleased(mouseX, mouseY, mouseButton);
        return super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public void tick() {
        if (this.selected != null) {
            this.selected.tick();
        }
    }

    static {
        List<MobEffectInstance> fakeEffect = Lists.newArrayList(new MobEffectInstance[] { new MobEffectInstance(MobEffects.CONFUSION), new MobEffectInstance(MobEffects.BLINDNESS), new MobEffectInstance(MobEffects.POISON), new MobEffectInstance(MobEffects.REGENERATION), new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE), new MobEffectInstance(MobEffects.WATER_BREATHING), new MobEffectInstance(MobEffects.INVISIBILITY) });
        FAKE_EFFECT_MAP = (Map<MobEffect, MobEffectInstance>) fakeEffect.stream().collect(Collectors.toMap(MobEffectInstance::m_19544_, e -> e));
    }
}