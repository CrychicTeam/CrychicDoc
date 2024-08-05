package noppes.npcs.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import noppes.npcs.CommonProxy;
import noppes.npcs.CustomContainer;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.ModelData;
import noppes.npcs.ModelPartData;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.item.IItemScripted;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.client.controllers.PresetController;
import noppes.npcs.client.gui.GuiBlockBuilder;
import noppes.npcs.client.gui.GuiBlockCopy;
import noppes.npcs.client.gui.GuiBorderBlock;
import noppes.npcs.client.gui.GuiMerchantAdd;
import noppes.npcs.client.gui.GuiNbtBook;
import noppes.npcs.client.gui.GuiNpcDimension;
import noppes.npcs.client.gui.GuiNpcMobSpawner;
import noppes.npcs.client.gui.GuiNpcMobSpawnerMounter;
import noppes.npcs.client.gui.GuiNpcPather;
import noppes.npcs.client.gui.GuiNpcRedstoneBlock;
import noppes.npcs.client.gui.GuiNpcRemoteEditor;
import noppes.npcs.client.gui.GuiNpcWaypoint;
import noppes.npcs.client.gui.custom.GuiCustom;
import noppes.npcs.client.gui.global.GuiNPCManageBanks;
import noppes.npcs.client.gui.global.GuiNPCManageDialogs;
import noppes.npcs.client.gui.global.GuiNPCManageFactions;
import noppes.npcs.client.gui.global.GuiNPCManageLinkedNpc;
import noppes.npcs.client.gui.global.GuiNPCManageQuest;
import noppes.npcs.client.gui.global.GuiNPCManageTransporters;
import noppes.npcs.client.gui.global.GuiNpcManageRecipes;
import noppes.npcs.client.gui.global.GuiNpcQuestReward;
import noppes.npcs.client.gui.mainmenu.GuiNPCGlobalMainMenu;
import noppes.npcs.client.gui.mainmenu.GuiNPCInv;
import noppes.npcs.client.gui.mainmenu.GuiNpcAI;
import noppes.npcs.client.gui.mainmenu.GuiNpcAdvanced;
import noppes.npcs.client.gui.mainmenu.GuiNpcDisplay;
import noppes.npcs.client.gui.mainmenu.GuiNpcStats;
import noppes.npcs.client.gui.player.GuiMailbox;
import noppes.npcs.client.gui.player.GuiMailmanWrite;
import noppes.npcs.client.gui.player.GuiNPCBankChest;
import noppes.npcs.client.gui.player.GuiNPCTrader;
import noppes.npcs.client.gui.player.GuiNpcCarpentryBench;
import noppes.npcs.client.gui.player.GuiNpcFollower;
import noppes.npcs.client.gui.player.GuiNpcFollowerHire;
import noppes.npcs.client.gui.player.GuiTransportSelection;
import noppes.npcs.client.gui.player.companion.GuiNpcCompanionInv;
import noppes.npcs.client.gui.player.companion.GuiNpcCompanionStats;
import noppes.npcs.client.gui.player.companion.GuiNpcCompanionTalents;
import noppes.npcs.client.gui.questtypes.GuiNpcQuestTypeItem;
import noppes.npcs.client.gui.roles.GuiNpcBankSetup;
import noppes.npcs.client.gui.roles.GuiNpcFollowerSetup;
import noppes.npcs.client.gui.roles.GuiNpcItemGiver;
import noppes.npcs.client.gui.roles.GuiNpcTraderSetup;
import noppes.npcs.client.gui.roles.GuiNpcTransporter;
import noppes.npcs.client.gui.script.GuiScript;
import noppes.npcs.client.gui.script.GuiScriptBlock;
import noppes.npcs.client.gui.script.GuiScriptDoor;
import noppes.npcs.client.gui.script.GuiScriptGlobal;
import noppes.npcs.client.gui.script.GuiScriptItem;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.controllers.PixelmonHelper;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.mixin.ArmorLayerMixin;
import noppes.npcs.shared.client.util.TrueTypeFont;
import noppes.npcs.shared.common.util.LogWriter;

public class ClientProxy extends CommonProxy {

    public static PlayerData playerData = new PlayerData();

    public static KeyMapping QuestLog;

    public static KeyMapping Scene1;

    public static KeyMapping SceneReset;

    public static KeyMapping Scene2;

    public static KeyMapping Scene3;

    public static ClientProxy.FontContainer Font;

    public static ModelData data;

    public static PlayerModel playerModel;

    public static ArmorLayerMixin armorLayer;

    @Override
    public void load() {
        Font = new ClientProxy.FontContainer(CustomNpcs.FontType, CustomNpcs.FontSize);
        this.createFolders();
        CustomNpcResourceListener listener = new CustomNpcResourceListener();
        ((ReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener(listener);
        listener.onResourceManagerReload(Minecraft.getInstance().getResourceManager());
        MenuScreens.register(CustomContainer.container_carpentrybench, GuiNpcCarpentryBench::new);
        MenuScreens.register(CustomContainer.container_customgui, (container, inv, comp) -> {
            GuiCustom gui = new GuiCustom(container, inv, comp);
            gui.setGuiData(container.data);
            return gui;
        });
        MenuScreens.register(CustomContainer.container_mail, GuiMailmanWrite::new);
        MenuScreens.register(CustomContainer.container_managebanks, GuiNPCManageBanks::new);
        MenuScreens.register(CustomContainer.container_managerecipes, GuiNpcManageRecipes::new);
        MenuScreens.register(CustomContainer.container_merchantadd, GuiMerchantAdd::new);
        MenuScreens.register(CustomContainer.container_banklarge, GuiNPCBankChest::new);
        MenuScreens.register(CustomContainer.container_banksmall, GuiNPCBankChest::new);
        MenuScreens.register(CustomContainer.container_bankunlock, GuiNPCBankChest::new);
        MenuScreens.register(CustomContainer.container_bankupgrade, GuiNPCBankChest::new);
        MenuScreens.register(CustomContainer.container_companion, GuiNpcCompanionInv::new);
        MenuScreens.register(CustomContainer.container_follower, GuiNpcFollower::new);
        MenuScreens.register(CustomContainer.container_followerhire, GuiNpcFollowerHire::new);
        MenuScreens.register(CustomContainer.container_followersetup, GuiNpcFollowerSetup::new);
        MenuScreens.register(CustomContainer.container_inv, GuiNPCInv::new);
        MenuScreens.register(CustomContainer.container_itemgiver, GuiNpcItemGiver::new);
        MenuScreens.register(CustomContainer.container_questreward, GuiNpcQuestReward::new);
        MenuScreens.register(CustomContainer.container_questtypeitem, GuiNpcQuestTypeItem::new);
        MenuScreens.register(CustomContainer.container_trader, GuiNPCTrader::new);
        MenuScreens.register(CustomContainer.container_tradersetup, GuiNpcTraderSetup::new);
        new MusicController();
        MinecraftForge.EVENT_BUS.register(new ClientTickHandler());
        Minecraft mc = Minecraft.getInstance();
        new PresetController(CustomNpcs.Dir);
        if (CustomNpcs.EnableUpdateChecker) {
            VersionChecker checker = new VersionChecker();
            checker.start();
        }
        PixelmonHelper.loadClient();
    }

    @Override
    public PlayerData getPlayerData(Player player) {
        if (player.m_20148_() == Minecraft.getInstance().player.m_20148_()) {
            if (playerData.player != player) {
                playerData.player = player;
            }
            return playerData;
        } else {
            return null;
        }
    }

    @Override
    public void postload() {
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        Minecraft.getInstance().getItemColors().register((stack, tintIndex) -> 9127187, CustomItems.mount, CustomItems.cloner, CustomItems.moving, CustomItems.scripter, CustomItems.wand, CustomItems.teleporter);
        Minecraft.getInstance().getItemColors().register((stack, tintIndex) -> {
            if (stack.getItem() == CustomItems.scripted_item) {
                IItemStack item = NpcAPI.Instance().getIItemStack(stack);
                if (!item.isEmpty()) {
                    return ((IItemScripted) item).getColor();
                }
            }
            return -1;
        }, CustomItems.scripted_item);
    }

    private void createFolders() {
        File file = new File(CustomNpcs.Dir, "assets/customnpcs");
        if (!file.exists()) {
            file.mkdirs();
        }
        File check = new File(file, "sounds");
        if (!check.exists()) {
            check.mkdir();
        }
        File json = new File(file, "sounds.json");
        if (!json.exists()) {
            try {
                json.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(json));
                writer.write("{\n\n}");
                writer.close();
            } catch (IOException var7) {
            }
        }
        File meta = new File(CustomNpcs.Dir, "pack.mcmeta");
        if (!meta.exists()) {
            try {
                meta.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(meta));
                writer.write("{\n    \"pack\": {\n        \"description\": \"customnpcs map resource pack\",\n        \"pack_format\": 6\n    }\n}");
                writer.close();
            } catch (IOException var6) {
                var6.printStackTrace();
            }
        }
        check = new File(file, "textures");
        if (!check.exists()) {
            check.mkdir();
        }
    }

    public static Screen getGui(EnumGuiType gui, EntityNPCInterface npc, FriendlyByteBuf buf) {
        try {
            if (gui == EnumGuiType.MainMenuDisplay) {
                if (npc != null) {
                    return new GuiNpcDisplay(npc);
                }
                Minecraft.getInstance().player.sendSystemMessage(Component.literal("Unable to find npc"));
            } else {
                if (gui == EnumGuiType.MainMenuStats) {
                    return new GuiNpcStats(npc);
                }
                if (gui == EnumGuiType.MainMenuAdvanced) {
                    return new GuiNpcAdvanced(npc);
                }
                if (gui == EnumGuiType.MovingPath) {
                    return new GuiNpcPather(npc);
                }
                if (gui == EnumGuiType.ManageFactions) {
                    return new GuiNPCManageFactions(npc);
                }
                if (gui == EnumGuiType.ManageLinked) {
                    return new GuiNPCManageLinkedNpc(npc);
                }
                if (gui == EnumGuiType.BuilderBlock) {
                    return new GuiBlockBuilder(buf.readBlockPos());
                }
                if (gui == EnumGuiType.ManageTransport) {
                    return new GuiNPCManageTransporters(npc);
                }
                if (gui == EnumGuiType.ManageDialogs) {
                    return new GuiNPCManageDialogs(npc);
                }
                if (gui == EnumGuiType.ManageQuests) {
                    return new GuiNPCManageQuest(npc);
                }
                if (gui == EnumGuiType.Companion) {
                    return new GuiNpcCompanionStats(npc);
                }
                if (gui == EnumGuiType.CompanionTalent) {
                    return new GuiNpcCompanionTalents(npc);
                }
                if (gui == EnumGuiType.MainMenuGlobal) {
                    return new GuiNPCGlobalMainMenu(npc);
                }
                if (gui == EnumGuiType.MainMenuAI) {
                    return new GuiNpcAI(npc);
                }
                if (gui == EnumGuiType.PlayerTransporter) {
                    return new GuiTransportSelection(npc);
                }
                if (gui == EnumGuiType.Script) {
                    return new GuiScript(npc);
                }
                if (gui == EnumGuiType.ScriptBlock) {
                    return new GuiScriptBlock(buf.readBlockPos());
                }
                if (gui == EnumGuiType.ScriptItem) {
                    return new GuiScriptItem(Minecraft.getInstance().player);
                }
                if (gui == EnumGuiType.ScriptDoor) {
                    return new GuiScriptDoor(buf.readBlockPos());
                }
                if (gui == EnumGuiType.ScriptPlayers) {
                    return new GuiScriptGlobal();
                }
                if (gui == EnumGuiType.SetupTransporter) {
                    return new GuiNpcTransporter(npc);
                }
                if (gui == EnumGuiType.SetupBank) {
                    return new GuiNpcBankSetup(npc);
                }
                if (gui == EnumGuiType.NpcRemote && Minecraft.getInstance().screen == null) {
                    return new GuiNpcRemoteEditor();
                }
                if (gui == EnumGuiType.PlayerMailbox) {
                    return new GuiMailbox();
                }
                if (gui == EnumGuiType.NpcDimensions) {
                    return new GuiNpcDimension();
                }
                if (gui == EnumGuiType.Border) {
                    return new GuiBorderBlock(buf.readBlockPos());
                }
                if (gui == EnumGuiType.RedstoneBlock) {
                    return new GuiNpcRedstoneBlock(buf.readBlockPos());
                }
                if (gui == EnumGuiType.MobSpawner) {
                    return new GuiNpcMobSpawner(buf.readBlockPos());
                }
                if (gui == EnumGuiType.CopyBlock) {
                    return new GuiBlockCopy(buf.readBlockPos());
                }
                if (gui == EnumGuiType.MobSpawnerMounter) {
                    return new GuiNpcMobSpawnerMounter();
                }
                if (gui == EnumGuiType.Waypoint) {
                    return new GuiNpcWaypoint(buf.readBlockPos());
                }
                if (gui == EnumGuiType.NbtBook) {
                    return new GuiNbtBook(buf.readBlockPos());
                }
            }
            return null;
        } finally {
            if (buf != null) {
                buf.release();
            }
        }
    }

    @Override
    public void openGui(Player player, EnumGuiType gui) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == player) {
            Screen screen = getGui(gui, null, null);
            if (screen != null) {
                minecraft.setScreen(screen);
            }
        }
    }

    @Override
    public void openGui(EntityNPCInterface npc, EnumGuiType gui) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.setScreen(getGui(gui, npc, null));
    }

    @Override
    public void openGui(Player player, Object guiscreen) {
        Minecraft minecraft = Minecraft.getInstance();
        if (player.m_9236_().isClientSide && guiscreen instanceof Screen) {
            if (guiscreen != null) {
                minecraft.setScreen((Screen) guiscreen);
            }
        }
    }

    @Override
    public void spawnParticle(LivingEntity player, String string, Object... ob) {
        if (string.equals("Block")) {
            BlockPos pos = (BlockPos) ob[0];
            BlockState state = (BlockState) ob[1];
            Minecraft.getInstance().particleEngine.destroy(pos, state);
        } else if (string.equals("ModelData")) {
            ModelData data = (ModelData) ob[0];
            ModelPartData particles = (ModelPartData) ob[1];
            EntityCustomNpc npc = (EntityCustomNpc) player;
            Minecraft minecraft = Minecraft.getInstance();
            double height = npc.m_6049_() + (double) data.getBodyY();
            RandomSource var10 = npc.m_217043_();
        }
    }

    @Override
    public boolean hasClient() {
        return true;
    }

    @Override
    public Player getPlayer() {
        return Minecraft.getInstance().player;
    }

    public static void bind(ResourceLocation location) {
        try {
            if (location == null) {
                return;
            }
            TextureManager manager = Minecraft.getInstance().getTextureManager();
            AbstractTexture ob = manager.getTexture(location);
            if (ob == null) {
                ob = new SimpleTexture(location);
                manager.register(location, ob);
            }
            RenderSystem.bindTexture(ob.getId());
        } catch (NullPointerException var3) {
        }
    }

    @Override
    public void spawnParticle(ParticleOptions particle, double x, double y, double z, double motionX, double motionY, double motionZ, float scale) {
        Minecraft mc = Minecraft.getInstance();
        double xx = mc.getCameraEntity().getX() - x;
        double yy = mc.getCameraEntity().getY() - y;
        double zz = mc.getCameraEntity().getZ() - z;
        if (!(xx * xx + yy * yy + zz * zz > 256.0)) {
            Particle fx = mc.particleEngine.createParticle(particle, x, y, z, motionX, motionY, motionZ);
            if (fx != null) {
                if (particle == ParticleTypes.FLAME) {
                    fx.scale(1.0E-5F);
                } else if (particle == ParticleTypes.SMOKE) {
                    fx.scale(1.0E-5F);
                }
            }
        }
    }

    public static class FontContainer {

        private TrueTypeFont textFont = null;

        public boolean useCustomFont = true;

        private FontContainer() {
        }

        public FontContainer(String fontType, int fontSize) {
            try {
                this.textFont = new TrueTypeFont(new Font(fontType, 0, fontSize), 1.0F);
                this.useCustomFont = !fontType.equalsIgnoreCase("minecraft");
                if (!this.useCustomFont || fontType.isEmpty() || fontType.equalsIgnoreCase("default")) {
                    this.textFont = new TrueTypeFont(new ResourceLocation("customnpcs", "opensans.ttf"), fontSize, 1.0F);
                }
            } catch (Throwable var4) {
                LogWriter.except(var4);
                this.useCustomFont = false;
            }
        }

        public int height(String text) {
            return this.useCustomFont ? this.textFont.height(text) : 9;
        }

        public int width(String text) {
            return this.useCustomFont ? this.textFont.width(text) : Minecraft.getInstance().font.width(text);
        }

        public ClientProxy.FontContainer copy() {
            ClientProxy.FontContainer font = new ClientProxy.FontContainer();
            font.textFont = this.textFont;
            font.useCustomFont = this.useCustomFont;
            return font;
        }

        public void draw(PoseStack matrixStack, String text, int x, int y, int color) {
            this.textFont.draw(matrixStack, text, (float) x, (float) y, color);
        }

        public String getName() {
            return !this.useCustomFont ? "Minecraft" : this.textFont.getFontName();
        }

        public void clear() {
            if (this.textFont != null) {
                this.textFont.dispose();
            }
        }
    }
}