package snownee.jade.addon.create;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;
import snownee.jade.addon.universal.FluidStorageProvider;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.FluidView;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ViewGroup;

public enum HideBoilerHandlerProvider implements IServerExtensionProvider<FluidTankBlockEntity, CompoundTag>, IClientExtensionProvider<CompoundTag, FluidView> {

    INSTANCE;

    @Override
    public ResourceLocation getUid() {
        return CreatePlugin.HIDE_BOILER_TANKS;
    }

    @Override
    public List<ClientViewGroup<FluidView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<CompoundTag>> groups) {
        return FluidStorageProvider.INSTANCE.getClientGroups(accessor, groups);
    }

    @Nullable
    public List<ViewGroup<CompoundTag>> getGroups(ServerPlayer player, ServerLevel level, FluidTankBlockEntity target, boolean showDetails) {
        return target.getControllerBE() != null && target.getControllerBE().boiler.isActive() ? List.of() : FluidStorageProvider.INSTANCE.getGroups(player, level, target, showDetails);
    }
}