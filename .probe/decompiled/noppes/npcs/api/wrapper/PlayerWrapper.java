package noppes.npcs.api.wrapper;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.EventHooks;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IContainer;
import noppes.npcs.api.IPos;
import noppes.npcs.api.ITimers;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.entity.data.IData;
import noppes.npcs.api.entity.data.IPixelmonPlayerData;
import noppes.npcs.api.entity.data.IPlayerMail;
import noppes.npcs.api.gui.ICustomGui;
import noppes.npcs.api.handler.data.IQuest;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.client.EntityUtil;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.containers.ContainerCustomGui;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.PixelmonHelper;
import noppes.npcs.controllers.PlayerQuestController;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogOption;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerDialogData;
import noppes.npcs.controllers.data.PlayerMail;
import noppes.npcs.controllers.data.PlayerQuestData;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.controllers.data.QuestData;
import noppes.npcs.entity.EntityDialogNpc;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketAchievement;
import noppes.npcs.packets.client.PacketChat;
import noppes.npcs.packets.client.PacketGuiClose;
import noppes.npcs.packets.client.PacketPlayMusic;
import noppes.npcs.packets.client.PacketPlaySound;
import noppes.npcs.packets.server.SPacketDimensionTeleport;
import noppes.npcs.shared.client.util.NoppesStringUtils;
import noppes.npcs.util.ValueUtil;

public class PlayerWrapper<T extends ServerPlayer> extends EntityLivingBaseWrapper<T> implements IPlayer {

    private IContainer inventory;

    private Object pixelmonPartyStorage;

    private Object pixelmonPCStorage;

    private final IData storeddata = new IData() {

        @Override
        public void put(String key, Object value) {
            CompoundTag compound = this.getStoredCompound();
            if (value instanceof Number) {
                compound.putDouble(key, ((Number) value).doubleValue());
            } else if (value instanceof String) {
                compound.putString(key, (String) value);
            }
        }

        @Override
        public Object get(String key) {
            CompoundTag compound = this.getStoredCompound();
            if (!compound.contains(key)) {
                return null;
            } else {
                Tag base = compound.get(key);
                return base instanceof NumericTag ? ((NumericTag) base).getAsDouble() : base.getAsString();
            }
        }

        @Override
        public void remove(String key) {
            CompoundTag compound = this.getStoredCompound();
            compound.remove(key);
        }

        @Override
        public boolean has(String key) {
            return this.getStoredCompound().contains(key);
        }

        @Override
        public void clear() {
            PlayerData data = PlayerData.get(PlayerWrapper.this.entity);
            data.scriptStoreddata = new CompoundTag();
        }

        private CompoundTag getStoredCompound() {
            PlayerData data = PlayerData.get(PlayerWrapper.this.entity);
            return data.scriptStoreddata;
        }

        @Override
        public String[] getKeys() {
            CompoundTag compound = this.getStoredCompound();
            return (String[]) compound.getAllKeys().toArray(new String[compound.getAllKeys().size()]);
        }
    };

    private PlayerData data;

    public PlayerWrapper(T player) {
        super(player);
    }

    @Override
    public IData getStoreddata() {
        return this.storeddata;
    }

    @Override
    public String getName() {
        return this.entity.m_7755_().getString();
    }

    @Override
    public String getDisplayName() {
        return this.entity.m_5446_().getString();
    }

    @Override
    public int getHunger() {
        return this.entity.m_36324_().getFoodLevel();
    }

    @Override
    public void setHunger(int level) {
        this.entity.m_36324_().setFoodLevel(level);
    }

    @Override
    public boolean hasFinishedQuest(int id) {
        PlayerQuestData data = this.getData().questData;
        return data.finishedQuests.containsKey(id);
    }

    @Override
    public boolean hasActiveQuest(int id) {
        PlayerQuestData data = this.getData().questData;
        return data.activeQuests.containsKey(id);
    }

    @Override
    public IQuest[] getActiveQuests() {
        PlayerQuestData data = this.getData().questData;
        List<IQuest> quests = new ArrayList();
        for (int id : data.activeQuests.keySet()) {
            IQuest quest = (IQuest) QuestController.instance.quests.get(id);
            if (quest != null) {
                quests.add(quest);
            }
        }
        return (IQuest[]) quests.toArray(new IQuest[quests.size()]);
    }

    @Override
    public IQuest[] getFinishedQuests() {
        PlayerQuestData data = this.getData().questData;
        List<IQuest> quests = new ArrayList();
        for (int id : data.finishedQuests.keySet()) {
            IQuest quest = (IQuest) QuestController.instance.quests.get(id);
            if (quest != null) {
                quests.add(quest);
            }
        }
        return (IQuest[]) quests.toArray(new IQuest[quests.size()]);
    }

    @Override
    public void startQuest(int id) {
        Quest quest = (Quest) QuestController.instance.quests.get(id);
        if (quest != null) {
            QuestData questdata = new QuestData(quest);
            PlayerData data = this.getData();
            data.questData.activeQuests.put(id, questdata);
            Packets.send(this.entity, new PacketAchievement(Component.translatable("quest.newquest"), Component.translatable(quest.title), 2));
            Component text = Component.translatable("quest.newquest").append(":").append(Component.translatable(quest.title));
            Packets.send(this.entity, new PacketChat(text));
            data.updateClient = true;
        }
    }

    @Override
    public void sendNotification(String title, String msg, int type) {
        if (type >= 0 && type <= 3) {
            Packets.send(this.entity, new PacketAchievement(Component.translatable(title), Component.translatable(msg), type));
        } else {
            throw new CustomNPCsException("Wrong type value given " + type);
        }
    }

    @Override
    public void finishQuest(int id) {
        Quest quest = (Quest) QuestController.instance.quests.get(id);
        if (quest != null) {
            PlayerData data = this.getData();
            data.questData.finishedQuests.put(id, System.currentTimeMillis());
            data.updateClient = true;
        }
    }

    @Override
    public void stopQuest(int id) {
        Quest quest = (Quest) QuestController.instance.quests.get(id);
        if (quest != null) {
            PlayerData data = this.getData();
            data.questData.activeQuests.remove(id);
            data.updateClient = true;
        }
    }

    @Override
    public void removeQuest(int id) {
        Quest quest = (Quest) QuestController.instance.quests.get(id);
        if (quest != null) {
            PlayerData data = this.getData();
            data.questData.activeQuests.remove(id);
            data.questData.finishedQuests.remove(id);
            data.updateClient = true;
        }
    }

    @Override
    public boolean hasReadDialog(int id) {
        PlayerDialogData data = this.getData().dialogData;
        return data.dialogsRead.contains(id);
    }

    @Override
    public void showDialog(int id, String name) {
        Dialog dialog = (Dialog) DialogController.instance.dialogs.get(id);
        if (dialog == null) {
            throw new CustomNPCsException("Unknown Dialog id: " + id);
        } else if (dialog.availability.isAvailable(this.entity)) {
            EntityDialogNpc npc = new EntityDialogNpc(this.getWorld().getMCLevel());
            npc.display.setName(name);
            EntityUtil.Copy(this.entity, npc);
            DialogOption option = new DialogOption();
            option.dialogId = id;
            option.title = dialog.title;
            npc.dialogs.put(0, option);
            NoppesUtilServer.openDialog(this.entity, npc, dialog);
        }
    }

    @Override
    public void addFactionPoints(int faction, int points) {
        PlayerData data = this.getData();
        data.factionData.increasePoints(this.entity, faction, points);
        data.updateClient = true;
    }

    @Override
    public int getFactionPoints(int faction) {
        Player var10001 = this.entity;
        return this.getData().factionData.getFactionPoints(var10001, faction);
    }

    @Override
    public float getRotation() {
        return this.entity.m_146908_();
    }

    @Override
    public void setRotation(float rotation) {
        this.entity.m_146922_(rotation);
    }

    @Override
    public void message(String message) {
        this.entity.sendSystemMessage(Component.translatable(NoppesStringUtils.formatText(message, this.entity)));
    }

    @Override
    public int getGamemode() {
        return this.entity.gameMode.getGameModeForPlayer().getId();
    }

    @Override
    public void setGamemode(int type) {
        this.entity.setGameMode(GameType.byId(type));
    }

    @Override
    public int inventoryItemCount(IItemStack item) {
        int count = 0;
        for (int i = 0; i < this.entity.m_150109_().getContainerSize(); i++) {
            ItemStack is = this.entity.m_150109_().getItem(i);
            if (is != null && this.isItemEqual(item.getMCItemStack(), is)) {
                count += is.getCount();
            }
        }
        return count;
    }

    private boolean isItemEqual(ItemStack stack, ItemStack other) {
        return other.isEmpty() ? false : stack.getItem() == other.getItem();
    }

    @Override
    public int inventoryItemCount(String id) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
        if (item == null) {
            throw new CustomNPCsException("Unknown item id: " + id);
        } else {
            return this.inventoryItemCount(NpcAPI.Instance().getIItemStack(new ItemStack(item, 1)));
        }
    }

    @Override
    public IContainer getInventory() {
        if (this.inventory == null) {
            this.inventory = new ContainerWrapper(this.entity.m_150109_());
        }
        return this.inventory;
    }

    @Override
    public IItemStack getInventoryHeldItem() {
        return NpcAPI.Instance().getIItemStack(this.entity.f_36096_.getCarried());
    }

    @Override
    public boolean removeItem(IItemStack item, int amount) {
        int count = this.inventoryItemCount(item);
        if (amount > count) {
            return false;
        } else {
            if (count == amount) {
                this.removeAllItems(item);
            } else {
                for (int i = 0; i < this.entity.m_150109_().getContainerSize(); i++) {
                    ItemStack is = this.entity.m_150109_().getItem(i);
                    if (is != null && this.isItemEqual(item.getMCItemStack(), is)) {
                        if (amount < is.getCount()) {
                            is.split(amount);
                            break;
                        }
                        this.entity.m_150109_().setItem(i, ItemStack.EMPTY);
                        amount -= is.getCount();
                    }
                }
            }
            this.updatePlayerInventory();
            return true;
        }
    }

    @Override
    public boolean removeItem(String id, int amount) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
        if (item == null) {
            throw new CustomNPCsException("Unknown item id: " + id);
        } else {
            return this.removeItem(NpcAPI.Instance().getIItemStack(new ItemStack(item, 1)), amount);
        }
    }

    @Override
    public boolean giveItem(IItemStack item) {
        ItemStack mcItem = item.getMCItemStack();
        if (mcItem.isEmpty()) {
            return false;
        } else {
            boolean bo = this.entity.m_150109_().add(mcItem.copy());
            if (bo) {
                NoppesUtilServer.playSound(this.entity, SoundEvents.ITEM_PICKUP, 0.2F, ((this.entity.m_217043_().nextFloat() - this.entity.m_217043_().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                this.updatePlayerInventory();
            }
            return bo;
        }
    }

    @Override
    public boolean giveItem(String id, int amount) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(id));
        if (item == null) {
            return false;
        } else {
            ItemStack mcStack = new ItemStack(item);
            IItemStack itemStack = NpcAPI.Instance().getIItemStack(mcStack);
            itemStack.setStackSize(amount);
            return this.giveItem(itemStack);
        }
    }

    @Override
    public void updatePlayerInventory() {
        this.entity.f_36095_.m_38946_();
        this.entity.connection.send(new ClientboundContainerSetSlotPacket(-2, 0, this.entity.m_150109_().selected, this.entity.m_150109_().getItem(this.entity.m_150109_().selected)));
        PlayerQuestData playerdata = this.getData().questData;
        playerdata.checkQuestCompletion(this.entity, 0);
    }

    @Override
    public IBlock getSpawnPoint() {
        BlockPos pos = (BlockPos) this.entity.m_21257_().orElse(null);
        return pos == null ? this.getWorld().getSpawnPoint() : NpcAPI.Instance().getIBlock(this.entity.m_9236_(), pos);
    }

    @Override
    public void setSpawnPoint(IBlock block) {
        this.setSpawnpoint(block.getX(), block.getY(), block.getZ());
    }

    @Override
    public void setSpawnpoint(int x, int y, int z) {
        x = ValueUtil.CorrectInt(x, -30000000, 30000000);
        z = ValueUtil.CorrectInt(z, -30000000, 30000000);
        y = ValueUtil.CorrectInt(y, 0, 256);
        this.entity.setRespawnPosition(this.getWorld().getMCLevel().m_46472_(), new BlockPos(x, y, z), 0.0F, true, false);
    }

    @Override
    public void resetSpawnpoint() {
        this.entity.setRespawnPosition(this.getWorld().getMCLevel().m_46472_(), null, 0.0F, true, false);
    }

    @Override
    public void removeAllItems(IItemStack item) {
        for (int i = 0; i < this.entity.m_150109_().getContainerSize(); i++) {
            ItemStack is = this.entity.m_150109_().getItem(i);
            if (is != null && ItemStack.isSameItem(is, item.getMCItemStack())) {
                this.entity.m_150109_().setItem(i, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public boolean hasAdvancement(String achievement) {
        Advancement advancement = this.entity.m_20194_().getAdvancements().getAdvancement(new ResourceLocation(achievement));
        if (advancement == null) {
            throw new CustomNPCsException("Advancement doesnt exist");
        } else {
            AdvancementProgress progress = this.entity.m_20194_().getPlayerList().getPlayerAdvancements(this.entity).getOrStartProgress(advancement);
            return progress.isDone();
        }
    }

    @Override
    public int getExpLevel() {
        return this.entity.f_36078_;
    }

    @Override
    public void setExpLevel(int level) {
        this.entity.giveExperienceLevels(level - this.entity.f_36078_);
    }

    @Override
    public void setPosition(double x, double y, double z) {
        SPacketDimensionTeleport.teleportPlayer(this.entity, x, y, z, this.entity.m_9236_().dimension());
    }

    @Override
    public void setPos(IPos pos) {
        SPacketDimensionTeleport.teleportPlayer(this.entity, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), this.entity.m_9236_().dimension());
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public boolean typeOf(int type) {
        return type == 1 ? true : super.typeOf(type);
    }

    @Override
    public boolean hasPermission(String permission) {
        for (PermissionNode<?> node : PermissionAPI.getRegisteredNodes()) {
            if (node.getNodeName().equals(permission)) {
                try {
                    return CustomNpcsPermissions.hasPermission(this.entity, (PermissionNode<Boolean>) node);
                } catch (Throwable var5) {
                }
            }
        }
        return false;
    }

    public IPixelmonPlayerData getPixelmonData() {
        if (!PixelmonHelper.Enabled) {
            throw new CustomNPCsException("Pixelmon isnt installed");
        } else {
            return new IPixelmonPlayerData() {

                @Override
                public Object getParty() {
                    if (PlayerWrapper.this.pixelmonPartyStorage == null) {
                        PlayerWrapper.this.pixelmonPartyStorage = PixelmonHelper.getParty(PlayerWrapper.this.entity);
                    }
                    return PlayerWrapper.this.pixelmonPartyStorage;
                }

                @Override
                public Object getPC() {
                    if (PlayerWrapper.this.pixelmonPCStorage == null) {
                        PlayerWrapper.this.pixelmonPCStorage = PixelmonHelper.getPc(PlayerWrapper.this.entity);
                    }
                    return PlayerWrapper.this.pixelmonPCStorage;
                }
            };
        }
    }

    private PlayerData getData() {
        if (this.data == null) {
            this.data = PlayerData.get(this.entity);
        }
        return this.data;
    }

    @Override
    public ITimers getTimers() {
        return this.getData().timers;
    }

    @Override
    public void removeDialog(int id) {
        PlayerData data = this.getData();
        data.dialogData.dialogsRead.remove(id);
        data.updateClient = true;
    }

    @Override
    public void addDialog(int id) {
        PlayerData data = this.getData();
        data.dialogData.dialogsRead.add(id);
        data.updateClient = true;
    }

    @Override
    public void closeGui() {
        this.entity.closeContainer();
        Packets.send(this.entity, new PacketGuiClose(new CompoundTag()));
    }

    @Override
    public int factionStatus(int factionId) {
        Faction faction = FactionController.instance.getFaction(factionId);
        if (faction == null) {
            throw new CustomNPCsException("Unknown faction: " + factionId);
        } else {
            return faction.playerStatus(this);
        }
    }

    @Override
    public void kick(String message) {
        this.entity.connection.disconnect(Component.translatable(message));
    }

    @Override
    public boolean canQuestBeAccepted(int questId) {
        return PlayerQuestController.canQuestBeAccepted(this.entity, questId);
    }

    @Override
    public void showCustomGui(ICustomGui gui) {
        NoppesUtilServer.openContainerGui(this.getMCEntity(), EnumGuiType.CustomGui, buf -> buf.writeNbt(((CustomGuiWrapper) gui).toNBT()));
        ((ContainerCustomGui) this.getMCEntity().f_36096_).setGui((CustomGuiWrapper) gui, this.entity);
    }

    @Override
    public ICustomGui getCustomGui() {
        return this.entity.f_36096_ instanceof ContainerCustomGui ? ((ContainerCustomGui) this.entity.f_36096_).customGui : null;
    }

    @Override
    public void clearData() {
        PlayerData data = this.getData();
        data.setNBT(new CompoundTag());
        data.save(true);
    }

    @Override
    public IContainer getOpenContainer() {
        return NpcAPI.Instance().getIContainer(this.entity.f_36096_);
    }

    @Override
    public void playSound(String sound, float volume, float pitch) {
        BlockPos pos = this.entity.m_20183_();
        Packets.send(this.entity, new PacketPlaySound(sound, pos, volume, pitch));
    }

    @Override
    public void playMusic(String sound, boolean background, boolean loops) {
        Packets.send(this.entity, new PacketPlayMusic(sound, !background, loops));
    }

    @Override
    public void sendMail(IPlayerMail mail) {
        PlayerData data = this.getData();
        data.mailData.playermail.add(((PlayerMail) mail).copy());
        data.save(false);
    }

    @Override
    public void trigger(int id, Object... arguments) {
        EventHooks.onScriptTriggerEvent(PlayerData.get(this.entity).scriptData, id, this.getWorld(), this.getPos(), null, arguments);
    }
}