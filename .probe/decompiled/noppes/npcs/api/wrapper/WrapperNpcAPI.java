package noppes.npcs.api.wrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import nikedemos.markovnames.generators.MarkovGenerator;
import noppes.npcs.CustomEntities;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.IContainer;
import noppes.npcs.api.IDamageSource;
import noppes.npcs.api.INbt;
import noppes.npcs.api.IPos;
import noppes.npcs.api.IWorld;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.entity.ICustomNpc;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.entity.data.IPlayerMail;
import noppes.npcs.api.gui.ICustomGui;
import noppes.npcs.api.handler.ICloneHandler;
import noppes.npcs.api.handler.IDialogHandler;
import noppes.npcs.api.handler.IFactionHandler;
import noppes.npcs.api.handler.IQuestHandler;
import noppes.npcs.api.handler.IRecipeHandler;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.containers.ContainerNpcInterface;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.RecipeController;
import noppes.npcs.controllers.ServerCloneController;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerMail;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.mixin.EntityIMixin;
import noppes.npcs.shared.common.util.LRUHashMap;
import noppes.npcs.util.NBTJsonUtil;

public class WrapperNpcAPI extends NpcAPI {

    private static final Map<DimensionType, WorldWrapper> worldCache = new LRUHashMap<DimensionType, WorldWrapper>(10);

    public static final IEventBus EVENT_BUS = BusBuilder.builder().build();

    private static NpcAPI instance = null;

    public static void clearCache() {
        worldCache.clear();
        BlockWrapper.clearCache();
    }

    @Override
    public IEntity getIEntity(Entity entity) {
        if (entity == null || entity.level().isClientSide) {
            return null;
        } else {
            return (IEntity) (entity instanceof EntityNPCInterface ? ((EntityNPCInterface) entity).wrappedNPC : WrapperEntityData.get(entity));
        }
    }

    @Override
    public ICustomNpc createNPC(Level level) {
        if (level.isClientSide) {
            return null;
        } else {
            EntityCustomNpc npc = new EntityCustomNpc(CustomEntities.entityCustomNpc, level);
            return npc.wrappedNPC;
        }
    }

    public void registerPermissionNode(String permission, int defaultType) {
        throw new CustomNPCsException("registerPermissionNode is nolonger supported");
    }

    @Override
    public boolean hasPermissionNode(String permission) {
        for (PermissionNode<?> node : PermissionAPI.getRegisteredNodes()) {
            if (node.getNodeName().equals(permission)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ICustomNpc spawnNPC(Level level, int x, int y, int z) {
        if (level.isClientSide) {
            return null;
        } else {
            EntityCustomNpc npc = new EntityCustomNpc(CustomEntities.entityCustomNpc, level);
            npc.m_19890_((double) x + 0.5, (double) y, (double) z + 0.5, 0.0F, 0.0F);
            npc.ais.setStartPos((double) x, (double) y, (double) z);
            npc.m_21153_(npc.m_21233_());
            level.m_7967_(npc);
            return npc.wrappedNPC;
        }
    }

    public static NpcAPI Instance() {
        if (instance == null) {
            instance = new WrapperNpcAPI();
        }
        return instance;
    }

    @Override
    public IEventBus events() {
        return EVENT_BUS;
    }

    @Override
    public IBlock getIBlock(Level level, BlockPos pos) {
        return BlockWrapper.createNew(level, pos, level.getBlockState(pos));
    }

    @Override
    public IItemStack getIItemStack(ItemStack itemstack) {
        return (IItemStack) (itemstack != null && !itemstack.isEmpty() ? itemstack.getCapability(ItemStackWrapper.ITEMSCRIPTEDDATA_CAPABILITY, null).orElse(ItemStackWrapper.AIR) : ItemStackWrapper.AIR);
    }

    @Override
    public IWorld getIWorld(ServerLevel level) {
        WorldWrapper w = (WorldWrapper) worldCache.get(level.m_6042_());
        if (w != null) {
            w.level = level;
            return w;
        } else {
            worldCache.put(level.m_6042_(), w = WorldWrapper.createNew(level));
            return w;
        }
    }

    @Override
    public IWorld getIWorld(DimensionType dimension) {
        for (ServerLevel level : CustomNpcs.Server.getAllLevels()) {
            if (level.m_6042_() == dimension) {
                return this.getIWorld(level);
            }
        }
        throw new CustomNPCsException("Unknown dimension: " + dimension);
    }

    @Override
    public IWorld getIWorld(String dimension) {
        ResourceLocation loc = new ResourceLocation(dimension);
        for (ServerLevel level : CustomNpcs.Server.getAllLevels()) {
            if (level.m_46472_().location().equals(loc)) {
                return this.getIWorld(level);
            }
        }
        throw new CustomNPCsException("Unknown dimension: " + loc);
    }

    @Override
    public IContainer getIContainer(AbstractContainerMenu inventory) {
        return new ContainerWrapper(inventory);
    }

    @Override
    public IContainer getIContainer(Container container) {
        return (IContainer) (container instanceof ContainerNpcInterface ? ContainerNpcInterface.getOrCreateIContainer((ContainerNpcInterface) container) : new ContainerWrapper(container));
    }

    @Override
    public IFactionHandler getFactions() {
        this.checkLevel();
        return FactionController.instance;
    }

    private void checkLevel() {
        if (CustomNpcs.Server == null || CustomNpcs.Server.isStopped()) {
            throw new CustomNPCsException("No world is loaded right now");
        }
    }

    @Override
    public IRecipeHandler getRecipes() {
        this.checkLevel();
        return RecipeController.instance;
    }

    @Override
    public IQuestHandler getQuests() {
        this.checkLevel();
        return QuestController.instance;
    }

    @Override
    public IWorld[] getIWorlds() {
        this.checkLevel();
        List<IWorld> list = new ArrayList();
        for (ServerLevel level : CustomNpcs.Server.getAllLevels()) {
            list.add(this.getIWorld(level));
        }
        return (IWorld[]) list.toArray(new IWorld[list.size()]);
    }

    @Override
    public IPos getIPos(double x, double y, double z) {
        return new BlockPosWrapper(new BlockPos((int) x, (int) y, (int) z));
    }

    @Override
    public File getGlobalDir() {
        return CustomNpcs.Dir;
    }

    @Override
    public File getLevelDir() {
        return CustomNpcs.getLevelSaveDirectory();
    }

    @Override
    public INbt getINbt(CompoundTag compound) {
        return compound == null ? new NBTWrapper(new CompoundTag()) : new NBTWrapper(compound);
    }

    @Override
    public INbt stringToNbt(String str) {
        if (str != null && !str.isEmpty()) {
            try {
                return this.getINbt(NBTJsonUtil.Convert(str));
            } catch (NBTJsonUtil.JsonException var3) {
                throw new CustomNPCsException(var3, "Failed converting " + str);
            }
        } else {
            throw new CustomNPCsException("Cant cast empty string to nbt");
        }
    }

    @Override
    public IDamageSource getIDamageSource(DamageSource damagesource) {
        return new DamageSourceWrapper(damagesource);
    }

    @Override
    public IDialogHandler getDialogs() {
        return DialogController.instance;
    }

    @Override
    public ICloneHandler getClones() {
        return ServerCloneController.Instance;
    }

    @Override
    public String executeCommand(IWorld level, String command) {
        FakePlayer player = EntityNPCInterface.CommandPlayer;
        ((EntityIMixin) player).setLevel(level.getMCLevel());
        player.m_6034_(0.0, 0.0, 0.0);
        return NoppesUtilServer.runCommand(level.getMCLevel(), BlockPos.ZERO, "API", command, null, player);
    }

    @Override
    public INbt getRawPlayerData(String uuid) {
        return this.getINbt(PlayerData.loadPlayerData(uuid));
    }

    @Override
    public IPlayerMail createMail(String sender, String subject) {
        PlayerMail mail = new PlayerMail();
        mail.sender = sender;
        mail.subject = subject;
        return mail;
    }

    @Override
    public ICustomGui createCustomGui(int id, int width, int height, boolean pauseGame, IPlayer player) {
        return new CustomGuiWrapper(player, id, width, height, pauseGame);
    }

    @Override
    public String getRandomName(int dictionary, int gender) {
        return MarkovGenerator.fetch(dictionary, gender);
    }
}