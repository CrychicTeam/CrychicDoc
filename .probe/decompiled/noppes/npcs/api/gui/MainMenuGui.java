package noppes.npcs.api.gui;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.wrapper.gui.CustomGuiButtonWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.containers.ContainerCustomGui;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiParts;

public abstract class MainMenuGui {

    protected CustomGuiWrapper gui;

    public MainMenuGui(int active, EntityCustomNpc npc, IPlayer player) {
        this.gui = new CustomGuiWrapper(player);
        this.gui.setBackgroundTexture("customnpcs:textures/gui/components.png");
        this.gui.setSize(420, 220);
        this.gui.getBackgroundRect().setPos(0, 20);
        this.gui.getBackgroundRect().setSize(420, 200);
        this.gui.getBackgroundRect().setTextureOffset(0, 0);
        this.gui.getBackgroundRect().setRepeatingTexture(64, 64, 4);
        this.gui.npc = npc;
        int buttonId = 0;
        IButton button = new CustomGuiButtonWrapper().setSize(22, 20);
        ITexturedRect rect = button.getTextureRect().setTexture("customnpcs:textures/gui/components.png").setRepeatingTexture(64, 20, 3).setTextureOffset(0, 64);
        button.setTextureHoverOffset(22).setPos(buttonId * 22 + 4, 0).setID(buttonId++);
        button.setDisplayItem(NpcAPI.Instance().getIItemStack(new ItemStack(Items.ENDER_EYE)));
        button.setHoverText("menu.display");
        button.setOnPress((gui, bx) -> player.showCustomGui((new DisplayMenu(npc, player)).gui));
        this.gui.addComponent(button);
        button = new CustomGuiButtonWrapper().setSize(22, 20);
        button.setTextureRect(rect);
        button.setTextureHoverOffset(22).setPos(buttonId * 22 + 4, 0).setID(buttonId++);
        button.setDisplayItem(NpcAPI.Instance().getIItemStack(new ItemStack(Items.PLAYER_HEAD)));
        button.setHoverText("menu.model");
        button.setOnPress((gui, bx) -> {
            CustomGuiWrapper wrapper = (new ModelMenu(npc, player)).gui;
            ((ContainerCustomGui) player.getMCEntity().f_36096_).setGui(wrapper, player.getMCEntity());
            Packets.send(player.getMCEntity(), new PacketGuiParts(npc.m_19879_(), wrapper.toNBT()));
        });
        this.gui.addComponent(button);
        IButton var19 = new CustomGuiButtonWrapper().setSize(22, 20);
        var19.setTextureRect(rect);
        var19.setTextureHoverOffset(22).setPos(buttonId * 22 + 4, 0).setID(buttonId++);
        var19.setDisplayItem(NpcAPI.Instance().getIItemStack(new ItemStack(Items.CHEST)));
        var19.setHoverText("menu.inventory");
        var19.setOnPress((gui, bx) -> player.showCustomGui((new InventoryMenu(npc, player)).gui));
        this.gui.addComponent(var19);
        IButton var20 = new CustomGuiButtonWrapper().setSize(22, 20);
        var20.setTextureRect(rect);
        var20.setTextureHoverOffset(22).setPos(buttonId * 22 + 4, 0).setID(buttonId++);
        var20.setDisplayItem(NpcAPI.Instance().getIItemStack(new ItemStack(Items.REDSTONE)));
        var20.setHoverText("menu.logic");
        var20.setOnPress((gui, bx) -> player.showCustomGui((new LogicMenu(npc, player)).gui));
        this.gui.addComponent(var20);
        IButton var21 = new CustomGuiButtonWrapper().setSize(22, 20);
        var21.setTextureRect(rect);
        var21.setTextureHoverOffset(22).setPos(buttonId * 22 + 4, 0).setID(buttonId++);
        var21.setDisplayItem(NpcAPI.Instance().getIItemStack(new ItemStack(Items.DIAMOND_CHESTPLATE)));
        var21.setHoverText("menu.health");
        var21.setOnPress((gui, bx) -> player.showCustomGui((new HealthMenu(npc, player)).gui));
        this.gui.addComponent(var21);
        IButton var22 = new CustomGuiButtonWrapper().setSize(22, 20);
        var22.setTextureRect(rect);
        var22.setTextureHoverOffset(22).setPos(buttonId * 22 + 4, 0).setID(buttonId++);
        var22.setDisplayItem(NpcAPI.Instance().getIItemStack(new ItemStack(Items.TOTEM_OF_UNDYING)));
        var22.setHoverText("menu.death");
        var22.setOnPress((gui, bx) -> player.showCustomGui((new DeathMenu(npc, player)).gui));
        this.gui.addComponent(var22);
        IButton var23 = new CustomGuiButtonWrapper().setSize(22, 20);
        var23.setTextureRect(rect);
        var23.setTextureHoverOffset(22).setPos(buttonId * 22 + 4, 0).setID(buttonId++);
        var23.setDisplayItem(NpcAPI.Instance().getIItemStack(new ItemStack(Items.IRON_BOOTS)));
        var23.setHoverText("menu.movement");
        var23.setOnPress((gui, bx) -> player.showCustomGui((new MovementMenu(npc, player)).gui));
        this.gui.addComponent(var23);
        IButton var24 = new CustomGuiButtonWrapper().setSize(22, 20);
        var24.setTextureRect(rect);
        var24.setTextureHoverOffset(22).setPos(buttonId * 22 + 4, 0).setID(buttonId++);
        var24.setDisplayItem(NpcAPI.Instance().getIItemStack(new ItemStack(Items.DIAMOND_SWORD)));
        var24.setHoverText("stats.meleeproperties");
        var24.setOnPress((gui, bx) -> player.showCustomGui((new MeleeMenu(npc, player)).gui));
        this.gui.addComponent(var24);
        IButton var25 = new CustomGuiButtonWrapper().setSize(22, 20);
        var25.setTextureRect(rect);
        var25.setTextureHoverOffset(22).setPos(buttonId * 22 + 4, 0).setID(buttonId++);
        var25.setDisplayItem(NpcAPI.Instance().getIItemStack(new ItemStack(Items.ARROW)));
        var25.setOnPress((gui, bx) -> player.showCustomGui((new DisplayMenu(npc, player)).gui));
        this.gui.addComponent(var25);
        IButton var26 = new CustomGuiButtonWrapper().setSize(22, 20);
        var26.setTextureRect(rect);
        var26.setTextureHoverOffset(22).setPos(buttonId * 22 + 4, 0).setID(buttonId++);
        var26.setDisplayItem(NpcAPI.Instance().getIItemStack(new ItemStack(Items.REDSTONE)));
        var26.setOnPress((gui, bx) -> player.showCustomGui((new DisplayMenu(npc, player)).gui));
        this.gui.addComponent(var26);
        IButton b = (IButton) this.gui.getComponent(active);
        b.setEnabled(false);
        b.setPos(b.getPosX(), 3);
    }

    public static void open(Player player, EntityCustomNpc npc) {
        IPlayer p = (IPlayer) NpcAPI.Instance().getIEntity(player);
        DisplayMenu menu = new DisplayMenu(npc, p);
        p.showCustomGui(menu.gui);
    }
}