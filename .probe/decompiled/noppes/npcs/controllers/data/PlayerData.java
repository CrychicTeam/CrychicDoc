package noppes.npcs.controllers.data;

import java.io.File;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import noppes.npcs.CustomEntities;
import noppes.npcs.CustomNpcs;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.data.DataTimers;
import noppes.npcs.roles.RoleCompanion;
import noppes.npcs.shared.common.util.LogWriter;
import noppes.npcs.util.CustomNPCsScheduler;
import noppes.npcs.util.NBTJsonUtil;

public class PlayerData implements ICapabilityProvider {

    public static Capability<PlayerData> PLAYERDATA_CAPABILITY = CapabilityManager.get(new CapabilityToken<PlayerData>() {
    });

    public BlockPos scriptBlockPos = BlockPos.ZERO;

    private LazyOptional<PlayerData> instance = LazyOptional.of(() -> this);

    public PlayerDialogData dialogData = new PlayerDialogData();

    public PlayerBankData bankData = new PlayerBankData();

    public PlayerQuestData questData = new PlayerQuestData();

    public PlayerTransportData transportData = new PlayerTransportData();

    public PlayerFactionData factionData = new PlayerFactionData();

    public PlayerItemGiverData itemgiverData = new PlayerItemGiverData();

    public PlayerMailData mailData = new PlayerMailData();

    public PlayerScriptData scriptData;

    public CompoundTag scriptStoreddata = new CompoundTag();

    public DataTimers timers = new DataTimers(this);

    public EntityNPCInterface editingNpc;

    public CompoundTag cloned;

    public Player player;

    public String playername = "";

    public String uuid = "";

    private EntityNPCInterface activeCompanion = null;

    public int companionID = 0;

    public int playerLevel = 0;

    public boolean updateClient = false;

    public int dialogId = -1;

    public ItemStack prevHeldItem = ItemStack.EMPTY;

    public Entity mounted;

    public UUID iAmStealingYourDatas = UUID.randomUUID();

    private static final ResourceLocation key = new ResourceLocation("customnpcs", "playerdata");

    private static PlayerData backup = new PlayerData();

    public void setNBT(CompoundTag data) {
        this.dialogData.loadNBTData(data);
        this.bankData.loadNBTData(data);
        this.questData.loadNBTData(data);
        this.transportData.loadNBTData(data);
        this.factionData.loadNBTData(data);
        this.itemgiverData.loadNBTData(data);
        this.mailData.loadNBTData(data);
        this.timers.load(data);
        if (this.player != null) {
            this.playername = this.player.getName().getString();
            this.uuid = this.player.m_20148_().toString();
        } else {
            this.playername = data.getString("PlayerName");
            this.uuid = data.getString("UUID");
        }
        this.companionID = data.getInt("PlayerCompanionId");
        if (data.contains("PlayerCompanion") && !this.hasCompanion() && this.player != null) {
            EntityCustomNpc npc = new EntityCustomNpc(CustomEntities.entityCustomNpc, this.player.m_9236_());
            npc.readAdditionalSaveData(data.getCompound("PlayerCompanion"));
            npc.m_6034_(this.player.m_20185_(), this.player.m_20186_(), this.player.m_20189_());
            if (npc.role.getType() == 6) {
                ((RoleCompanion) npc.role).setSitting(false);
                this.player.m_9236_().m_7967_(npc);
                this.setCompanion(npc);
            }
        }
        this.scriptStoreddata = data.getCompound("ScriptStoreddata");
    }

    public CompoundTag getSyncNBT() {
        CompoundTag compound = new CompoundTag();
        this.dialogData.saveNBTData(compound);
        this.questData.saveNBTData(compound);
        this.factionData.saveNBTData(compound);
        return compound;
    }

    public CompoundTag getNBT() {
        if (this.player != null) {
            this.playername = this.player.getName().getString();
            this.uuid = this.player.m_20148_().toString();
        }
        CompoundTag compound = new CompoundTag();
        this.dialogData.saveNBTData(compound);
        this.bankData.saveNBTData(compound);
        this.questData.saveNBTData(compound);
        this.transportData.saveNBTData(compound);
        this.factionData.saveNBTData(compound);
        this.itemgiverData.saveNBTData(compound);
        this.mailData.saveNBTData(compound);
        this.timers.save(compound);
        compound.putString("PlayerName", this.playername);
        compound.putString("UUID", this.uuid);
        compound.putInt("PlayerCompanionId", this.companionID);
        compound.put("ScriptStoreddata", this.scriptStoreddata);
        if (this.hasCompanion()) {
            CompoundTag nbt = new CompoundTag();
            if (this.activeCompanion.m_20086_(nbt)) {
                compound.put("PlayerCompanion", nbt);
            }
        }
        return compound;
    }

    public boolean hasCompanion() {
        return this.activeCompanion != null && !this.activeCompanion.m_213877_();
    }

    public void setCompanion(EntityNPCInterface npc) {
        if (npc == null || npc.role.getType() == 6) {
            this.companionID++;
            this.activeCompanion = npc;
            if (npc != null) {
                ((RoleCompanion) npc.role).companionID = this.companionID;
            }
            this.save(false);
        }
    }

    public void updateCompanion(Level level) {
        if (this.hasCompanion() && level != this.activeCompanion.m_9236_()) {
            RoleCompanion role = (RoleCompanion) this.activeCompanion.role;
            role.owner = this.player;
            if (role.isFollowing()) {
                CompoundTag nbt = new CompoundTag();
                this.activeCompanion.m_20086_(nbt);
                this.activeCompanion.m_146870_();
                EntityCustomNpc npc = new EntityCustomNpc(CustomEntities.entityCustomNpc, level);
                npc.readAdditionalSaveData(nbt);
                npc.m_6034_(this.player.m_20185_(), this.player.m_20186_(), this.player.m_20189_());
                this.setCompanion(npc);
                ((RoleCompanion) npc.role).setSitting(false);
                level.m_7967_(npc);
            }
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
        return capability == PLAYERDATA_CAPABILITY ? this.instance.cast() : LazyOptional.empty();
    }

    public static void register(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(key, new PlayerData());
        }
    }

    public synchronized void save(boolean update) {
        CompoundTag compound = this.getNBT();
        String filename = this.uuid + ".json";
        CustomNPCsScheduler.runTack(() -> {
            try {
                File saveDir = CustomNpcs.getLevelSaveDirectory("playerdata");
                File file = new File(saveDir, filename + "_new");
                File file1 = new File(saveDir, filename);
                NBTJsonUtil.SaveFile(file, compound);
                if (file1.exists()) {
                    file1.delete();
                }
                file.renameTo(file1);
            } catch (Exception var5) {
                LogWriter.except(var5);
            }
        });
        if (update) {
            this.updateClient = true;
        }
    }

    public static CompoundTag loadPlayerData(String player) {
        File saveDir = CustomNpcs.getLevelSaveDirectory("playerdata");
        String filename = player;
        if (player.isEmpty()) {
            filename = "noplayername";
        }
        filename = filename + ".json";
        File file = null;
        try {
            file = new File(saveDir, filename);
            if (file.exists()) {
                return NBTJsonUtil.LoadFile(file);
            }
        } catch (Exception var5) {
            LogWriter.error("Error loading: " + file.getAbsolutePath(), var5);
        }
        return new CompoundTag();
    }

    public static PlayerData get(Player player) {
        if (player.m_9236_().isClientSide) {
            return CustomNpcs.proxy.getPlayerData(player);
        } else {
            PlayerData data = player.getCapability(PLAYERDATA_CAPABILITY, null).orElse(backup);
            if (data.player == null) {
                data.player = player;
                data.playerLevel = player.experienceLevel;
                data.scriptData = new PlayerScriptData(player);
                CompoundTag compound = loadPlayerData(player.m_20148_().toString());
                data.setNBT(compound);
            }
            return data;
        }
    }
}