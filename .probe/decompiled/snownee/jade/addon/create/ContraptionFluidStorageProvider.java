package snownee.jade.addon.create;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import snownee.jade.addon.lootr.LootrPlugin;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.FluidView;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ViewGroup;
import snownee.jade.util.JadeForgeUtils;

public enum ContraptionFluidStorageProvider implements IServerExtensionProvider<AbstractContraptionEntity, CompoundTag>, IClientExtensionProvider<CompoundTag, FluidView> {

    INSTANCE;

    @Override
    public ResourceLocation getUid() {
        return LootrPlugin.INVENTORY;
    }

    @Override
    public List<ClientViewGroup<FluidView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<CompoundTag>> groups) {
        return ClientViewGroup.map(groups, FluidView::readDefault, null);
    }

    public List<ViewGroup<CompoundTag>> getGroups(ServerPlayer player, ServerLevel level, AbstractContraptionEntity entity, boolean showDetails) {
        Contraption contraption = entity.getContraption();
        return JadeForgeUtils.fromFluidHandler(contraption.getSharedFluidTanks());
    }
}