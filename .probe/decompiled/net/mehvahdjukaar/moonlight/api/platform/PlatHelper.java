package net.mehvahdjukaar.moonlight.api.platform;

import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfile;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.platform.forge.PlatHelperImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlatHelper {

    @ExpectPlatform
    @Transformed
    public static void addCommonSetup(Runnable commonSetup) {
        PlatHelperImpl.addCommonSetup(commonSetup);
    }

    @ExpectPlatform
    @Transformed
    public static void addCommonSetupAsync(Runnable commonSetup) {
        PlatHelperImpl.addCommonSetupAsync(commonSetup);
    }

    @Contract
    @ExpectPlatform
    @Transformed
    public static boolean isDev() {
        return PlatHelperImpl.isDev();
    }

    @ExpectPlatform
    @Transformed
    public static boolean isData() {
        return PlatHelperImpl.isData();
    }

    @ExpectPlatform
    @Transformed
    public static boolean isModLoadingValid() {
        return PlatHelperImpl.isModLoadingValid();
    }

    @ExpectPlatform
    @Transformed
    public static boolean isInitializing() {
        return PlatHelperImpl.isInitializing();
    }

    @ExpectPlatform
    @Transformed
    public static boolean evaluateRecipeCondition(JsonElement jo) {
        return PlatHelperImpl.evaluateRecipeCondition(jo);
    }

    @Contract
    @ExpectPlatform
    @Transformed
    public static PlatHelper.Platform getPlatform() {
        return PlatHelperImpl.getPlatform();
    }

    @Contract
    @ExpectPlatform
    @Transformed
    public static PlatHelper.Side getPhysicalSide() {
        return PlatHelperImpl.getPhysicalSide();
    }

    @ExpectPlatform
    @Transformed
    public static Path getGamePath() {
        return PlatHelperImpl.getGamePath();
    }

    @ExpectPlatform
    @Transformed
    public static Path getModFilePath(String modId) {
        return PlatHelperImpl.getModFilePath(modId);
    }

    @Nullable
    @ExpectPlatform
    @Transformed
    public static String getModPageUrl(String modId) {
        return PlatHelperImpl.getModPageUrl(modId);
    }

    @ExpectPlatform
    @Transformed
    public static String getModName(String modId) {
        return PlatHelperImpl.getModName(modId);
    }

    @Nullable
    @ExpectPlatform
    @Transformed
    public static <T> Field findField(Class<? super T> clazz, String fieldName) {
        return PlatHelperImpl.findField(clazz, fieldName);
    }

    @Nullable
    @ExpectPlatform
    @Transformed
    public static Method findMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        return PlatHelperImpl.findMethod(clazz, methodName, parameterTypes);
    }

    @Nullable
    @ExpectPlatform
    @Transformed
    public static MinecraftServer getCurrentServer() {
        return PlatHelperImpl.getCurrentServer();
    }

    @ExpectPlatform
    @Transformed
    public static boolean isModLoaded(String modId) {
        return PlatHelperImpl.isModLoaded(modId);
    }

    @Nullable
    @ExpectPlatform
    @Transformed
    public static String getModVersion(String modId) {
        return PlatHelperImpl.getModVersion(modId);
    }

    @ExpectPlatform
    @Transformed
    public static List<String> getInstalledMods() {
        return PlatHelperImpl.getInstalledMods();
    }

    @ExpectPlatform
    @Transformed
    public static void registerResourcePack(PackType packType, Supplier<Pack> packSupplier) {
        PlatHelperImpl.registerResourcePack(packType, packSupplier);
    }

    @Contract
    @ExpectPlatform
    @Transformed
    public static boolean isMobGriefingOn(Level level, Entity entity) {
        return PlatHelperImpl.isMobGriefingOn(level, entity);
    }

    @ExpectPlatform
    @Transformed
    public static boolean isAreaLoaded(LevelReader level, BlockPos pos, int maxRange) {
        return PlatHelperImpl.isAreaLoaded(level, pos, maxRange);
    }

    @ExpectPlatform
    @Transformed
    public static int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        return PlatHelperImpl.getFlammability(state, level, pos, face);
    }

    @ExpectPlatform
    @Nullable
    @Transformed
    public static FoodProperties getFoodProperties(Item food, ItemStack stack, Player player) {
        return PlatHelperImpl.getFoodProperties(food, stack, player);
    }

    @ExpectPlatform
    @Transformed
    public static int getBurnTime(ItemStack stack) {
        return PlatHelperImpl.getBurnTime(stack);
    }

    @ExpectPlatform
    @Transformed
    public static Packet<ClientGamePacketListener> getEntitySpawnPacket(Entity entity) {
        return PlatHelperImpl.getEntitySpawnPacket(entity);
    }

    @ExpectPlatform
    @Transformed
    public static SpawnEggItem newSpawnEgg(Supplier<? extends EntityType<? extends Mob>> entityType, int color, int outerColor, Item.Properties properties) {
        return PlatHelperImpl.newSpawnEgg(entityType, color, outerColor, properties);
    }

    @ExpectPlatform
    @Transformed
    public static FlowerPotBlock newFlowerPot(@Nullable Supplier<FlowerPotBlock> emptyPot, Supplier<? extends Block> supplier, BlockBehaviour.Properties properties) {
        return PlatHelperImpl.newFlowerPot(emptyPot, supplier, properties);
    }

    @ExpectPlatform
    @Transformed
    public static RecordItem newMusicDisc(int power, Supplier<SoundEvent> music, Item.Properties properties, int secondDuration) {
        return PlatHelperImpl.newMusicDisc(power, music, properties, secondDuration);
    }

    @ExpectPlatform
    @Transformed
    public static SimpleParticleType newParticle() {
        return PlatHelperImpl.newParticle();
    }

    @ExpectPlatform
    @Transformed
    public static <T extends BlockEntity> BlockEntityType<T> newBlockEntityType(PlatHelper.BlockEntitySupplier<T> blockEntitySupplier, Block... validBlocks) {
        return PlatHelperImpl.newBlockEntityType(blockEntitySupplier, validBlocks);
    }

    @ExpectPlatform
    @Transformed
    public static <E extends Entity> EntityType<E> newEntityType(String name, EntityType.EntityFactory<E> factory, MobCategory category, float width, float height, int clientTrackingRange, boolean velocityUpdates, int updateInterval) {
        return PlatHelperImpl.newEntityType(name, factory, category, width, height, clientTrackingRange, velocityUpdates, updateInterval);
    }

    @ExpectPlatform
    @Transformed
    public static void addServerReloadListener(PreparableReloadListener listener, ResourceLocation location) {
        PlatHelperImpl.addServerReloadListener(listener, location);
    }

    @ExpectPlatform
    @Transformed
    public static void openCustomMenu(ServerPlayer player, MenuProvider menuProvider, Consumer<FriendlyByteBuf> extraDataProvider) {
        PlatHelperImpl.openCustomMenu(player, menuProvider, extraDataProvider);
    }

    public static void openCustomMenu(ServerPlayer player, MenuProvider menuProvider, BlockPos pos) {
        openCustomMenu(player, menuProvider, buf -> buf.writeBlockPos(pos));
    }

    @ExpectPlatform
    @Transformed
    public static Player getFakeServerPlayer(GameProfile id, ServerLevel level) {
        return PlatHelperImpl.getFakeServerPlayer(id, level);
    }

    @FunctionalInterface
    public interface BlockEntitySupplier<T extends BlockEntity> {

        @NotNull
        T create(BlockPos var1, BlockState var2);
    }

    public static enum Platform {

        FORGE, FABRIC;

        private static boolean quilt = false;

        public boolean isForge() {
            return this == FORGE;
        }

        public boolean isFabric() {
            return this == FABRIC;
        }

        public boolean isQuilt() {
            return this.isFabric() && quilt;
        }

        public void ifForge(Runnable runnable) {
            if (this.isForge()) {
                runnable.run();
            }
        }

        public void ifFabric(Runnable runnable) {
            if (this.isFabric()) {
                runnable.run();
            }
        }

        static {
            try {
                Class.forName("org.quiltmc.loader.api.QuiltLoader");
                quilt = true;
            } catch (ClassNotFoundException var1) {
            }
        }
    }

    public static enum Side {

        CLIENT, SERVER;

        public boolean isClient() {
            return this == CLIENT;
        }

        public boolean isServer() {
            return this == SERVER;
        }

        public void ifClient(Runnable runnable) {
            if (this.isClient()) {
                runnable.run();
            }
        }

        public void ifServer(Runnable runnable) {
            if (this.isServer()) {
                runnable.run();
            }
        }
    }
}