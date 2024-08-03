package noppes.npcs.api;

import java.io.File;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
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

public abstract class NpcAPI {

    private static NpcAPI instance = null;

    public abstract ICustomNpc createNPC(Level var1);

    public abstract ICustomNpc spawnNPC(Level var1, int var2, int var3, int var4);

    public abstract IEntity getIEntity(Entity var1);

    public abstract IBlock getIBlock(Level var1, BlockPos var2);

    public abstract IContainer getIContainer(Container var1);

    public abstract IContainer getIContainer(AbstractContainerMenu var1);

    public abstract IItemStack getIItemStack(ItemStack var1);

    public abstract IWorld getIWorld(ServerLevel var1);

    public abstract IWorld getIWorld(String var1);

    public abstract IWorld getIWorld(DimensionType var1);

    public abstract IWorld[] getIWorlds();

    public abstract INbt getINbt(CompoundTag var1);

    public abstract IPos getIPos(double var1, double var3, double var5);

    public abstract IFactionHandler getFactions();

    public abstract IRecipeHandler getRecipes();

    public abstract IQuestHandler getQuests();

    public abstract IDialogHandler getDialogs();

    public abstract ICloneHandler getClones();

    public abstract IDamageSource getIDamageSource(DamageSource var1);

    public abstract INbt stringToNbt(String var1);

    public abstract IPlayerMail createMail(String var1, String var2);

    public abstract ICustomGui createCustomGui(int var1, int var2, int var3, boolean var4, IPlayer var5);

    public abstract INbt getRawPlayerData(String var1);

    public abstract IEventBus events();

    public abstract File getGlobalDir();

    public abstract File getLevelDir();

    public static boolean IsAvailable() {
        return ModList.get().isLoaded("customnpcs");
    }

    public static NpcAPI Instance() {
        if (instance != null) {
            return instance;
        } else if (!IsAvailable()) {
            return null;
        } else {
            try {
                Class c = Class.forName("noppes.npcs.api.wrapper.WrapperNpcAPI");
                instance = (NpcAPI) c.getMethod("Instance").invoke(null);
            } catch (Exception var1) {
                var1.printStackTrace();
            }
            return instance;
        }
    }

    public abstract boolean hasPermissionNode(String var1);

    public abstract String executeCommand(IWorld var1, String var2);

    public abstract String getRandomName(int var1, int var2);
}