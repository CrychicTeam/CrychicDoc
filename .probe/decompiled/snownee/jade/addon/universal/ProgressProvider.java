package snownee.jade.addon.universal;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.Identifiers;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ProgressView;
import snownee.jade.api.view.ViewGroup;
import snownee.jade.impl.WailaClientRegistration;
import snownee.jade.impl.WailaCommonRegistration;
import snownee.jade.impl.ui.ScaledTextElement;

public enum ProgressProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    INSTANCE;

    public static void append(ITooltip tooltip, Accessor<?> accessor, IPluginConfig config) {
        if (accessor.getServerData().contains("JadeProgress")) {
            Optional<IClientExtensionProvider<CompoundTag, ProgressView>> provider = Optional.ofNullable(ResourceLocation.tryParse(accessor.getServerData().getString("JadeProgressUid"))).map(WailaClientRegistration.INSTANCE.progressProviders::get);
            if (provider.isPresent()) {
                List<ClientViewGroup<ProgressView>> groups = ((IClientExtensionProvider) provider.get()).getClientGroups(accessor, ViewGroup.readList(accessor.getServerData(), "JadeProgress", Function.identity()));
                if (groups.isEmpty()) {
                    return;
                }
                IElementHelper helper = IElementHelper.get();
                boolean renderGroup = groups.size() > 1 || ((ClientViewGroup) groups.get(0)).shouldRenderGroup();
                BoxStyle box = new BoxStyle();
                box.bgColor = -2013265920;
                ClientViewGroup.tooltip(tooltip, groups, renderGroup, (theTooltip, group) -> {
                    if (renderGroup) {
                        group.renderHeader(theTooltip);
                    }
                    for (ProgressView view : group.views) {
                        if (view.text != null) {
                            theTooltip.add(new ScaledTextElement(view.text, 0.75F));
                        }
                        theTooltip.add(helper.progress(view.progress, null, view.style, box, false).size(new Vec2(10.0F, 4.0F)));
                    }
                });
            }
        }
    }

    public static void putData(Accessor<?> accessor) {
        CompoundTag tag = accessor.getServerData();
        Object target = accessor.getTarget();
        ServerPlayer player = (ServerPlayer) accessor.getPlayer();
        boolean showDetails = accessor.showDetails();
        for (IServerExtensionProvider<Object, CompoundTag> provider : WailaCommonRegistration.INSTANCE.progressProviders.get(target)) {
            List<ViewGroup<CompoundTag>> groups = provider.getGroups(player, player.serverLevel(), target, showDetails);
            if (groups != null) {
                if (ViewGroup.saveList(tag, "JadeProgress", groups, Function.identity())) {
                    tag.putString("JadeProgressUid", provider.getUid().toString());
                }
                return;
            }
        }
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        append(tooltip, accessor, config);
    }

    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        putData(accessor);
    }

    @Override
    public ResourceLocation getUid() {
        return Identifiers.UNIVERSAL_PROGRESS;
    }

    @Override
    public int getDefaultPriority() {
        return 1000;
    }

    @Override
    public boolean isRequired() {
        return true;
    }
}