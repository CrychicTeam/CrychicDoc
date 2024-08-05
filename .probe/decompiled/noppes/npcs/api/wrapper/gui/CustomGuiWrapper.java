package noppes.npcs.api.wrapper.gui;

import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.gui.ICustomGui;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.gui.ITexturedRect;
import noppes.npcs.containers.ContainerCustomGui;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiComponentUpdate;
import noppes.npcs.packets.client.PacketGuiData;

public class CustomGuiWrapper extends GuiComponentsWrapper implements ICustomGui {

    private int id;

    private int width;

    private int height;

    private boolean pauseGame;

    private final CustomGuiTexturedRectWrapper background = new CustomGuiTexturedRectWrapper();

    private final GuiComponentsScrollableWrapper scrollingPanel;

    private ScriptContainer scriptHandler;

    private CustomGuiWrapper parent;

    private CustomGuiWrapper subgui;

    public EntityCustomNpc npc;

    public CustomGuiWrapper(IPlayer player) {
        super(player);
        this.scrollingPanel = new GuiComponentsScrollableWrapper(this, player);
    }

    public CustomGuiWrapper(IPlayer player, int id, int width, int height, boolean pauseGame) {
        this(player);
        this.id = id;
        this.setSize(width, height);
        this.pauseGame = pauseGame;
        this.scriptHandler = ScriptContainer.Current;
        this.background.setID(-1);
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    public ScriptContainer getScriptHandler() {
        return this.scriptHandler;
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        if (this.background.getWidth() <= 0 || this.background.getHeight() <= 0) {
            this.background.setSize(width, height);
        }
    }

    @Override
    public void setDoesPauseGame(boolean pauseGame) {
        this.pauseGame = pauseGame;
    }

    public boolean getDoesPauseGame() {
        return this.pauseGame;
    }

    @Override
    public void setBackgroundTexture(String resourceLocation) {
        this.background.texture = resourceLocation;
    }

    public String getBackgroundTexture() {
        return this.background.texture;
    }

    public ITexturedRect getBackgroundRect() {
        return this.background;
    }

    public GuiComponentsScrollableWrapper getScrollingPanel() {
        return this.scrollingPanel;
    }

    @Override
    public void openSubGui(ICustomGui gui) {
        this.subgui = (CustomGuiWrapper) gui;
        this.subgui.parent = this;
        this.subgui.npc = this.npc;
        this.getRootGui().update();
    }

    public CustomGuiWrapper getSubGui() {
        return this.subgui;
    }

    @Override
    public boolean hasSubGui() {
        return this.subgui != null;
    }

    public CustomGuiWrapper closeSubGui() {
        if (this.subgui == null) {
            throw new CustomNPCsException("Current gui has no subgui");
        } else {
            CustomGuiWrapper gui = this.subgui;
            this.subgui = null;
            this.player.showCustomGui(this.getRootGui());
            return gui;
        }
    }

    @Override
    public void close() {
        if (this.parent == null) {
            this.player.closeGui();
        } else {
            this.parent.subgui = null;
            this.getRootGui().update();
        }
    }

    public CustomGuiWrapper getParentGui() {
        return this.parent;
    }

    public CustomGuiWrapper getRootGui() {
        return this.parent == null ? this : this.parent.getRootGui();
    }

    public CustomGuiWrapper getActiveGui() {
        return this.subgui == null ? this : this.subgui.getActiveGui();
    }

    @Override
    public IPlayer getPlayer() {
        return this.player;
    }

    @Override
    public void update() {
        if (this.player.getMCEntity().f_36096_ instanceof ContainerCustomGui) {
            Packets.send(this.player.getMCEntity(), new PacketGuiData(this.getRootGui().toNBT()));
        }
        ((ContainerCustomGui) this.player.getMCEntity().f_36096_).setGui(this.getActiveGui(), this.player.getMCEntity());
    }

    @Override
    public void update(ICustomGuiComponent component) {
        if (this.player.getMCEntity().f_36096_ instanceof ContainerCustomGui) {
            Packets.send(this.player.getMCEntity(), new PacketGuiComponentUpdate(component.getUniqueID(), ((CustomGuiComponentWrapper) component).toNBT(new CompoundTag())));
        }
    }

    public ICustomGui fromNBT(CompoundTag tag) {
        this.id = tag.getInt("id");
        this.width = tag.getIntArray("size")[0];
        this.height = tag.getIntArray("size")[1];
        this.pauseGame = tag.getBoolean("pause");
        this.background.fromNBT(tag.getCompound("backgroundRect"));
        this.setComponentNbt(tag.getCompound("components"));
        this.scrollingPanel.setComponentNbt(tag.getCompound("scrolling_components"));
        if (tag.contains("subgui")) {
            if (this.subgui == null) {
                this.subgui = new CustomGuiWrapper(this.player);
                this.subgui.fromNBT(tag.getCompound("subgui"));
            }
        } else {
            this.subgui = null;
        }
        return this;
    }

    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("id", this.id);
        tag.putIntArray("size", new int[] { this.width, this.height });
        tag.putBoolean("pause", this.pauseGame);
        tag.put("backgroundRect", this.background.toNBT(new CompoundTag()));
        tag.put("components", this.getComponentNbt());
        tag.put("scrolling_components", this.scrollingPanel.getComponentNbt());
        if (this.parent == null) {
            tag.putInt("slotSize", this.getActiveGui().getSlots().size());
        }
        if (this.subgui != null) {
            tag.put("subgui", this.subgui.toNBT());
        }
        return tag;
    }

    @Override
    public ICustomGuiComponent getComponentUuid(UUID id) {
        if (this.subgui != null) {
            ICustomGuiComponent comp = this.subgui.getComponentUuid(id);
            if (comp != null) {
                return comp;
            }
        }
        ICustomGuiComponent comp = super.getComponentUuid(id);
        return comp != null ? comp : this.scrollingPanel.getComponentUuid(id);
    }
}