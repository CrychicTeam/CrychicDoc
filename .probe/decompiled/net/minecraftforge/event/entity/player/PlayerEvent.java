package net.minecraftforge.event.entity.player;

import java.io.File;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerEvent extends LivingEvent {

    private final Player player;

    public PlayerEvent(Player player) {
        super(player);
        this.player = player;
    }

    public Player getEntity() {
        return this.player;
    }

    @Cancelable
    public static class BreakSpeed extends PlayerEvent {

        private static final BlockPos LEGACY_UNKNOWN = new BlockPos(0, -1, 0);

        private final BlockState state;

        private final float originalSpeed;

        private float newSpeed = 0.0F;

        private final Optional<BlockPos> pos;

        public BreakSpeed(Player player, BlockState state, float original, @Nullable BlockPos pos) {
            super(player);
            this.state = state;
            this.originalSpeed = original;
            this.setNewSpeed(original);
            this.pos = Optional.ofNullable(pos);
        }

        public BlockState getState() {
            return this.state;
        }

        public float getOriginalSpeed() {
            return this.originalSpeed;
        }

        public float getNewSpeed() {
            return this.newSpeed;
        }

        public void setNewSpeed(float newSpeed) {
            this.newSpeed = newSpeed;
        }

        public Optional<BlockPos> getPosition() {
            return this.pos;
        }
    }

    public static class Clone extends PlayerEvent {

        private final Player original;

        private final boolean wasDeath;

        public Clone(Player _new, Player oldPlayer, boolean wasDeath) {
            super(_new);
            this.original = oldPlayer;
            this.wasDeath = wasDeath;
        }

        public Player getOriginal() {
            return this.original;
        }

        public boolean isWasDeath() {
            return this.wasDeath;
        }
    }

    public static class HarvestCheck extends PlayerEvent {

        private final BlockState state;

        private boolean success;

        public HarvestCheck(Player player, BlockState state, boolean success) {
            super(player);
            this.state = state;
            this.success = success;
        }

        public BlockState getTargetBlock() {
            return this.state;
        }

        public boolean canHarvest() {
            return this.success;
        }

        public void setCanHarvest(boolean success) {
            this.success = success;
        }
    }

    public static class ItemCraftedEvent extends PlayerEvent {

        @NotNull
        private final ItemStack crafting;

        private final Container craftMatrix;

        public ItemCraftedEvent(Player player, @NotNull ItemStack crafting, Container craftMatrix) {
            super(player);
            this.crafting = crafting;
            this.craftMatrix = craftMatrix;
        }

        @NotNull
        public ItemStack getCrafting() {
            return this.crafting;
        }

        public Container getInventory() {
            return this.craftMatrix;
        }
    }

    public static class ItemPickupEvent extends PlayerEvent {

        private final ItemEntity originalEntity;

        private final ItemStack stack;

        public ItemPickupEvent(Player player, ItemEntity entPickedUp, ItemStack stack) {
            super(player);
            this.originalEntity = entPickedUp;
            this.stack = stack;
        }

        public ItemStack getStack() {
            return this.stack;
        }

        public ItemEntity getOriginalEntity() {
            return this.originalEntity;
        }
    }

    public static class ItemSmeltedEvent extends PlayerEvent {

        @NotNull
        private final ItemStack smelting;

        public ItemSmeltedEvent(Player player, @NotNull ItemStack crafting) {
            super(player);
            this.smelting = crafting;
        }

        @NotNull
        public ItemStack getSmelting() {
            return this.smelting;
        }
    }

    public static class LoadFromFile extends PlayerEvent {

        private final File playerDirectory;

        private final String playerUUID;

        public LoadFromFile(Player player, File originDirectory, String playerUUID) {
            super(player);
            this.playerDirectory = originDirectory;
            this.playerUUID = playerUUID;
        }

        public File getPlayerFile(String suffix) {
            if ("dat".equals(suffix)) {
                throw new IllegalArgumentException("The suffix 'dat' is reserved");
            } else {
                return new File(this.getPlayerDirectory(), this.getPlayerUUID() + "." + suffix);
            }
        }

        public File getPlayerDirectory() {
            return this.playerDirectory;
        }

        public String getPlayerUUID() {
            return this.playerUUID;
        }
    }

    public static class NameFormat extends PlayerEvent {

        private final Component username;

        private Component displayname;

        public NameFormat(Player player, Component username) {
            super(player);
            this.username = username;
            this.setDisplayname(username);
        }

        public Component getUsername() {
            return this.username;
        }

        public Component getDisplayname() {
            return this.displayname;
        }

        public void setDisplayname(Component displayname) {
            this.displayname = displayname;
        }
    }

    @Cancelable
    public static class PlayerChangeGameModeEvent extends PlayerEvent {

        private final GameType currentGameMode;

        private GameType newGameMode;

        public PlayerChangeGameModeEvent(Player player, GameType currentGameMode, GameType newGameMode) {
            super(player);
            this.currentGameMode = currentGameMode;
            this.newGameMode = newGameMode;
        }

        public GameType getCurrentGameMode() {
            return this.currentGameMode;
        }

        public GameType getNewGameMode() {
            return this.newGameMode;
        }

        public void setNewGameMode(GameType newGameMode) {
            this.newGameMode = newGameMode;
        }
    }

    public static class PlayerChangedDimensionEvent extends PlayerEvent {

        private final ResourceKey<Level> fromDim;

        private final ResourceKey<Level> toDim;

        public PlayerChangedDimensionEvent(Player player, ResourceKey<Level> fromDim, ResourceKey<Level> toDim) {
            super(player);
            this.fromDim = fromDim;
            this.toDim = toDim;
        }

        public ResourceKey<Level> getFrom() {
            return this.fromDim;
        }

        public ResourceKey<Level> getTo() {
            return this.toDim;
        }
    }

    public static class PlayerLoggedInEvent extends PlayerEvent {

        public PlayerLoggedInEvent(Player player) {
            super(player);
        }
    }

    public static class PlayerLoggedOutEvent extends PlayerEvent {

        public PlayerLoggedOutEvent(Player player) {
            super(player);
        }
    }

    public static class PlayerRespawnEvent extends PlayerEvent {

        private final boolean endConquered;

        public PlayerRespawnEvent(Player player, boolean endConquered) {
            super(player);
            this.endConquered = endConquered;
        }

        public boolean isEndConquered() {
            return this.endConquered;
        }
    }

    public static class SaveToFile extends PlayerEvent {

        private final File playerDirectory;

        private final String playerUUID;

        public SaveToFile(Player player, File originDirectory, String playerUUID) {
            super(player);
            this.playerDirectory = originDirectory;
            this.playerUUID = playerUUID;
        }

        public File getPlayerFile(String suffix) {
            if ("dat".equals(suffix)) {
                throw new IllegalArgumentException("The suffix 'dat' is reserved");
            } else {
                return new File(this.getPlayerDirectory(), this.getPlayerUUID() + "." + suffix);
            }
        }

        public File getPlayerDirectory() {
            return this.playerDirectory;
        }

        public String getPlayerUUID() {
            return this.playerUUID;
        }
    }

    public static class StartTracking extends PlayerEvent {

        private final Entity target;

        public StartTracking(Player player, Entity target) {
            super(player);
            this.target = target;
        }

        public Entity getTarget() {
            return this.target;
        }
    }

    public static class StopTracking extends PlayerEvent {

        private final Entity target;

        public StopTracking(Player player, Entity target) {
            super(player);
            this.target = target;
        }

        public Entity getTarget() {
            return this.target;
        }
    }

    public static class TabListNameFormat extends PlayerEvent {

        @Nullable
        private Component displayName;

        public TabListNameFormat(Player player) {
            super(player);
        }

        @Nullable
        public Component getDisplayName() {
            return this.displayName;
        }

        public void setDisplayName(@Nullable Component displayName) {
            this.displayName = displayName;
        }
    }
}