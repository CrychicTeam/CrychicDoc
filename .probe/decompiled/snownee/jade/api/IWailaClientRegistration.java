package snownee.jade.api;

import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.NonExtendable;
import snownee.jade.api.callback.JadeAfterRenderCallback;
import snownee.jade.api.callback.JadeBeforeRenderCallback;
import snownee.jade.api.callback.JadeItemModNameCallback;
import snownee.jade.api.callback.JadeRayTraceCallback;
import snownee.jade.api.callback.JadeRenderBackgroundCallback;
import snownee.jade.api.callback.JadeTooltipCollectedCallback;
import snownee.jade.api.platform.PlatformWailaClientRegistration;
import snownee.jade.api.view.EnergyView;
import snownee.jade.api.view.FluidView;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.ItemView;
import snownee.jade.api.view.ProgressView;

@NonExtendable
public interface IWailaClientRegistration extends PlatformWailaClientRegistration {

    void addConfig(ResourceLocation var1, boolean var2);

    void addConfig(ResourceLocation var1, Enum<?> var2);

    void addConfig(ResourceLocation var1, String var2, Predicate<String> var3);

    void addConfig(ResourceLocation var1, int var2, int var3, int var4, boolean var5);

    void addConfig(ResourceLocation var1, float var2, float var3, float var4, boolean var5);

    void addConfigListener(ResourceLocation var1, Consumer<ResourceLocation> var2);

    void registerBlockIcon(IBlockComponentProvider var1, Class<? extends Block> var2);

    void registerBlockComponent(IBlockComponentProvider var1, Class<? extends Block> var2);

    void registerEntityIcon(IEntityComponentProvider var1, Class<? extends Entity> var2);

    void registerEntityComponent(IEntityComponentProvider var1, Class<? extends Entity> var2);

    void hideTarget(Block var1);

    void hideTarget(EntityType<?> var1);

    void usePickedResult(Block var1);

    void usePickedResult(EntityType<?> var1);

    BlockAccessor.Builder blockAccessor();

    EntityAccessor.Builder entityAccessor();

    boolean shouldHide(Entity var1);

    boolean shouldHide(BlockState var1);

    boolean shouldPick(Entity var1);

    boolean shouldPick(BlockState var1);

    default void addAfterRenderCallback(JadeAfterRenderCallback callback) {
        this.addAfterRenderCallback(0, callback);
    }

    void addAfterRenderCallback(int var1, JadeAfterRenderCallback var2);

    default void addBeforeRenderCallback(JadeBeforeRenderCallback callback) {
        this.addBeforeRenderCallback(0, callback);
    }

    void addBeforeRenderCallback(int var1, JadeBeforeRenderCallback var2);

    default void addRayTraceCallback(JadeRayTraceCallback callback) {
        this.addRayTraceCallback(0, callback);
    }

    void addRayTraceCallback(int var1, JadeRayTraceCallback var2);

    default void addTooltipCollectedCallback(JadeTooltipCollectedCallback callback) {
        this.addTooltipCollectedCallback(0, callback);
    }

    void addTooltipCollectedCallback(int var1, JadeTooltipCollectedCallback var2);

    default void addItemModNameCallback(JadeItemModNameCallback callback) {
        this.addItemModNameCallback(0, callback);
    }

    void addItemModNameCallback(int var1, JadeItemModNameCallback var2);

    default void addRenderBackgroundCallback(JadeRenderBackgroundCallback callback) {
        this.addRenderBackgroundCallback(0, callback);
    }

    void addRenderBackgroundCallback(int var1, JadeRenderBackgroundCallback var2);

    Screen createPluginConfigScreen(@Nullable Screen var1, @Nullable String var2);

    void registerItemStorageClient(IClientExtensionProvider<ItemStack, ItemView> var1);

    void registerFluidStorageClient(IClientExtensionProvider<CompoundTag, FluidView> var1);

    void registerEnergyStorageClient(IClientExtensionProvider<CompoundTag, EnergyView> var1);

    void registerProgressClient(IClientExtensionProvider<CompoundTag, ProgressView> var1);

    boolean isServerConnected();

    boolean isShowDetailsPressed();

    boolean maybeLowVisionUser();

    CompoundTag getServerData();

    void setServerData(CompoundTag var1);

    ItemStack getBlockCamouflage(LevelAccessor var1, BlockPos var2);

    void markAsClientFeature(ResourceLocation var1);

    void markAsServerFeature(ResourceLocation var1);

    boolean isClientFeature(ResourceLocation var1);

    <T extends Accessor<?>> void registerAccessorHandler(Class<T> var1, Accessor.ClientHandler<T> var2);

    Accessor.ClientHandler<Accessor<?>> getAccessorHandler(Class<? extends Accessor<?>> var1);
}