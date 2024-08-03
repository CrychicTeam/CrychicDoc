package noppes.npcs.client.gui.global;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface2;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.containers.ContainerManageRecipes;
import noppes.npcs.controllers.data.RecipeCarpentry;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketRecipeGet;
import noppes.npcs.packets.server.SPacketRecipeRemove;
import noppes.npcs.packets.server.SPacketRecipeSave;
import noppes.npcs.packets.server.SPacketRecipesGet;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiButtonYesNo;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.ICustomScrollListener;
import noppes.npcs.shared.client.gui.listeners.IGuiData;
import noppes.npcs.shared.client.gui.listeners.IScrollData;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

public class GuiNpcManageRecipes extends GuiContainerNPCInterface2<ContainerManageRecipes> implements IScrollData, IGuiData, ICustomScrollListener, ITextfieldListener {

    private GuiCustomScrollNop scroll;

    private Map<String, Integer> data = new HashMap();

    private ContainerManageRecipes container;

    private String selected = null;

    private ResourceLocation slot;

    public GuiNpcManageRecipes(ContainerManageRecipes container, Inventory inv, Component titleIn) {
        super(NoppesUtil.getLastNpc(), container, inv, titleIn);
        this.container = container;
        this.drawDefaultBackground = false;
        Packets.sendServer(new SPacketRecipesGet(container.width));
        this.setBackground("inventorymenu.png");
        this.slot = this.getResource("slot.png");
        this.f_97727_ = 200;
    }

    @Override
    public void init() {
        super.init();
        if (this.scroll == null) {
            this.scroll = new GuiCustomScrollNop(this, 0);
        }
        this.scroll.setSize(130, 180);
        this.scroll.guiLeft = this.guiLeft + 172;
        this.scroll.guiTop = this.guiTop + 8;
        this.addScroll(this.scroll);
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 306, this.guiTop + 10, 84, 20, "menu.global"));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 306, this.guiTop + 32, 84, 20, "block.customnpcs.npccarpentybench"));
        this.getButton(0).setEnabled(this.container.width == 4);
        this.getButton(1).setEnabled(this.container.width == 3);
        this.addButton(new GuiButtonNop(this, 3, this.guiLeft + 306, this.guiTop + 60, 84, 20, "gui.add"));
        this.addButton(new GuiButtonNop(this, 4, this.guiLeft + 306, this.guiTop + 82, 84, 20, "gui.remove"));
        this.addLabel(new GuiLabel(0, "gui.ignoreDamage", this.guiLeft + 86, this.guiTop + 32));
        this.addButton(new GuiButtonYesNo(this, 5, this.guiLeft + 114, this.guiTop + 40, 50, 20, this.container.recipe.ignoreDamage));
        this.addLabel(new GuiLabel(1, "gui.ignoreNBT", this.guiLeft + 86, this.guiTop + 82));
        this.addButton(new GuiButtonYesNo(this, 6, this.guiLeft + 114, this.guiTop + 90, 50, 20, this.container.recipe.ignoreNBT));
        this.addTextField(new GuiTextFieldNop(0, this, this.guiLeft + 8, this.guiTop + 8, 160, 20, this.container.recipe.name));
        this.getTextField(0).enabled = false;
        this.getButton(5).setEnabled(false);
        this.getButton(6).setEnabled(false);
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 0) {
            this.save();
            NoppesUtil.requestOpenGUI(EnumGuiType.ManageRecipes, new BlockPos(3, 0, 0));
        }
        if (guibutton.id == 1) {
            this.save();
            NoppesUtil.requestOpenGUI(EnumGuiType.ManageRecipes, new BlockPos(4, 0, 0));
        }
        if (guibutton.id == 3) {
            this.save();
            this.scroll.clear();
            String name = I18n.get("gui.new");
            while (this.data.containsKey(name)) {
                name = name + "_";
            }
            RecipeCarpentry recipe = new RecipeCarpentry(new ResourceLocation("customnpcs", name), name);
            recipe.isGlobal = this.container.width == 3;
            Packets.sendServer(new SPacketRecipeSave(recipe.writeNBT()));
        }
        if (guibutton.id == 4 && this.data.containsKey(this.scroll.getSelected())) {
            Packets.sendServer(new SPacketRecipeRemove((Integer) this.data.get(this.scroll.getSelected())));
            this.scroll.clear();
        }
        if (guibutton.id == 5) {
            this.container.recipe.ignoreDamage = guibutton.getValue() == 1;
        }
        if (guibutton.id == 6) {
            this.container.recipe.ignoreNBT = guibutton.getValue() == 1;
        }
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        RecipeCarpentry recipe = RecipeCarpentry.load(compound);
        this.getTextField(0).m_94144_(recipe.name);
        this.container.setRecipe(recipe, this.player.m_9236_().registryAccess());
        this.getTextField(0).enabled = true;
        this.getButton(5).setEnabled(true);
        this.getButton(5).setDisplay(recipe.ignoreDamage ? 1 : 0);
        this.getButton(6).setEnabled(true);
        this.getButton(6).setDisplay(recipe.ignoreNBT ? 1 : 0);
        this.setSelected(recipe.name);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float f, int x, int y) {
        super.renderBg(graphics, f, x, y);
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.slot);
        for (int i = 0; i < this.container.width; i++) {
            for (int j = 0; j < this.container.width; j++) {
                graphics.blit(this.slot, this.guiLeft + i * 18 + 7, this.guiTop + j * 18 + 34, 0, 0, 18, 18);
            }
        }
        graphics.blit(this.slot, this.guiLeft + 86, this.guiTop + 60, 0, 0, 18, 18);
    }

    @Override
    public void setData(Vector<String> list, Map<String, Integer> data) {
        String name = this.scroll.getSelected();
        this.data = data;
        this.scroll.setList(list);
        this.getTextField(0).enabled = name != null;
        this.getButton(5).setEnabled(name != null);
        if (name != null) {
            this.scroll.setSelected(name);
        }
    }

    @Override
    public void setSelected(String selected) {
        this.selected = selected;
        this.scroll.setSelected(selected);
    }

    @Override
    public void scrollClicked(double i, double j, int k, GuiCustomScrollNop guiCustomScroll) {
        this.save();
        this.selected = this.scroll.getSelected();
        Packets.sendServer(new SPacketRecipeGet((Integer) this.data.get(this.selected)));
    }

    @Override
    public void save() {
        GuiTextFieldNop.unfocus();
        if (this.selected != null && this.data.containsKey(this.selected)) {
            this.container.saveRecipe();
            Packets.sendServer(new SPacketRecipeSave(this.container.recipe.writeNBT()));
        }
    }

    @Override
    public void unFocused(GuiTextFieldNop guiNpcTextField) {
        String name = guiNpcTextField.m_94155_();
        if (!name.isEmpty() && !this.data.containsKey(name)) {
            String old = this.container.recipe.name;
            this.data.remove(this.container.recipe.name);
            this.container.recipe.name = name;
            this.selected = name;
            this.scroll.replace(old, this.container.recipe.name);
        }
    }

    @Override
    public void scrollDoubleClicked(String selection, GuiCustomScrollNop scroll) {
    }
}