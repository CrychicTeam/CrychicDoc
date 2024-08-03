package noppes.npcs.client.gui.model;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.resources.language.I18n;
import noppes.npcs.ModelPartConfig;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiSliderNop;
import noppes.npcs.shared.client.gui.listeners.ICustomScrollListener;
import noppes.npcs.shared.client.gui.listeners.ISliderListener;

public class GuiCreationScale extends GuiCreationScreenInterface implements ISliderListener, ICustomScrollListener {

    private GuiCustomScrollNop scroll;

    private List<EnumParts> data = new ArrayList();

    private static EnumParts selected = EnumParts.HEAD;

    public GuiCreationScale(EntityNPCInterface npc) {
        super(npc);
        this.active = 3;
        this.xOffset = 140;
    }

    @Override
    public void init() {
        super.init();
        if (this.scroll == null) {
            this.scroll = new GuiCustomScrollNop(this, 0);
        }
        ArrayList list = new ArrayList();
        EnumParts[] parts = new EnumParts[] { EnumParts.HEAD, EnumParts.BODY, EnumParts.ARM_LEFT, EnumParts.ARM_RIGHT, EnumParts.LEG_LEFT, EnumParts.LEG_RIGHT };
        this.data.clear();
        for (EnumParts part : parts) {
            if (part == EnumParts.ARM_RIGHT) {
                ModelPartConfig config = this.playerdata.getPartConfig(EnumParts.ARM_LEFT);
                if (!config.notShared) {
                    continue;
                }
            }
            if (part == EnumParts.LEG_RIGHT) {
                ModelPartConfig config = this.playerdata.getPartConfig(EnumParts.LEG_LEFT);
                if (!config.notShared) {
                    continue;
                }
            }
            this.data.add(part);
            list.add(I18n.get("part." + part.name));
        }
        this.scroll.setUnsortedList(list);
        this.scroll.setSelected(I18n.get("part." + selected.name));
        this.scroll.guiLeft = this.guiLeft;
        this.scroll.guiTop = this.guiTop + 46;
        this.scroll.setSize(100, this.imageHeight - 74);
        this.addScroll(this.scroll);
        ModelPartConfig config = this.playerdata.getPartConfig(selected);
        int y = this.guiTop + 65;
        this.addLabel(new GuiLabel(10, "scale.width", this.guiLeft + 102, y + 5, 16777215));
        this.addSlider(new GuiSliderNop(this, 10, this.guiLeft + 150, y, 100, 20, config.scaleX - 0.5F));
        y += 22;
        this.addLabel(new GuiLabel(11, "scale.height", this.guiLeft + 102, y + 5, 16777215));
        this.addSlider(new GuiSliderNop(this, 11, this.guiLeft + 150, y, 100, 20, config.scaleY - 0.5F));
        y += 22;
        this.addLabel(new GuiLabel(12, "scale.depth", this.guiLeft + 102, y + 5, 16777215));
        this.addSlider(new GuiSliderNop(this, 12, this.guiLeft + 150, y, 100, 20, config.scaleZ - 0.5F));
        if (selected == EnumParts.ARM_LEFT || selected == EnumParts.LEG_LEFT) {
            y += 22;
            this.addLabel(new GuiLabel(13, "scale.shared", this.guiLeft + 102, y + 5, 16777215));
            this.addButton(new GuiButtonNop(this, 13, this.guiLeft + 150, y, 50, 20, new String[] { "gui.no", "gui.yes" }, config.notShared ? 0 : 1));
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop btn) {
        if (btn.id == 13) {
            boolean bo = btn.getValue() == 0;
            this.playerdata.getPartConfig(selected).notShared = bo;
            this.init();
        }
    }

    @Override
    public void mouseDragged(GuiSliderNop slider) {
        super.mouseDragged(slider);
        if (slider.id >= 10 && slider.id <= 12) {
            int percent = (int) (50.0F + slider.sliderValue * 100.0F);
            slider.setString(percent + "%");
            ModelPartConfig config = this.playerdata.getPartConfig(selected);
            if (slider.id == 10) {
                config.scaleX = slider.sliderValue + 0.5F;
            }
            if (slider.id == 11) {
                config.scaleY = slider.sliderValue + 0.5F;
            }
            if (slider.id == 12) {
                config.scaleZ = slider.sliderValue + 0.5F;
            }
            this.updateTransate();
        }
    }

    private void updateTransate() {
        for (EnumParts part : EnumParts.values()) {
            ModelPartConfig config = this.playerdata.getPartConfig(part);
            if (config != null) {
                if (part == EnumParts.HEAD) {
                    config.setTranslate(0.0F, this.playerdata.getBodyY(), 0.0F);
                } else if (part == EnumParts.ARM_LEFT) {
                    ModelPartConfig body = this.playerdata.getPartConfig(EnumParts.BODY);
                    float x = (1.0F - body.scaleX) * 0.25F + (1.0F - config.scaleX) * 0.075F;
                    float y = this.playerdata.getBodyY() + (1.0F - config.scaleY) * -0.1F;
                    config.setTranslate(-x, y, 0.0F);
                    if (!config.notShared) {
                        ModelPartConfig arm = this.playerdata.getPartConfig(EnumParts.ARM_RIGHT);
                        arm.copyValues(config);
                    }
                } else if (part == EnumParts.ARM_RIGHT) {
                    ModelPartConfig body = this.playerdata.getPartConfig(EnumParts.BODY);
                    float x = (1.0F - body.scaleX) * 0.25F + (1.0F - config.scaleX) * 0.075F;
                    float y = this.playerdata.getBodyY() + (1.0F - config.scaleY) * -0.1F;
                    config.setTranslate(x, y, 0.0F);
                } else if (part == EnumParts.LEG_LEFT) {
                    config.setTranslate(config.scaleX * 0.125F - 0.113F, this.playerdata.getLegsY(), 0.0F);
                    if (!config.notShared) {
                        ModelPartConfig leg = this.playerdata.getPartConfig(EnumParts.LEG_RIGHT);
                        leg.copyValues(config);
                    }
                } else if (part == EnumParts.LEG_RIGHT) {
                    config.setTranslate((1.0F - config.scaleX) * 0.125F, this.playerdata.getLegsY(), 0.0F);
                } else if (part == EnumParts.BODY) {
                    config.setTranslate(0.0F, this.playerdata.getBodyY(), 0.0F);
                }
            }
        }
    }

    @Override
    public void scrollClicked(double i, double j, int k, GuiCustomScrollNop scroll) {
        if (scroll.hasSelected()) {
            selected = (EnumParts) this.data.get(scroll.getSelectedIndex());
            this.init();
        }
    }

    @Override
    public void scrollDoubleClicked(String selection, GuiCustomScrollNop scroll) {
    }
}