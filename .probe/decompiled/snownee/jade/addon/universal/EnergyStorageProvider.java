package snownee.jade.addon.universal;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.Identifiers;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.api.ui.IProgressStyle;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.EnergyView;
import snownee.jade.api.view.IClientExtensionProvider;
import snownee.jade.api.view.IServerExtensionProvider;
import snownee.jade.api.view.ViewGroup;
import snownee.jade.impl.WailaClientRegistration;
import snownee.jade.impl.WailaCommonRegistration;
import snownee.jade.util.CommonProxy;

public enum EnergyStorageProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor>, IServerExtensionProvider<Object, CompoundTag>, IClientExtensionProvider<CompoundTag, EnergyView> {

    INSTANCE;

    public static void append(ITooltip tooltip, Accessor<?> accessor, IPluginConfig config) {
        if (accessor.showDetails() || !config.get(Identifiers.UNIVERSAL_ENERGY_STORAGE_DETAILED)) {
            if (accessor.getServerData().contains("JadeEnergyStorage")) {
                Optional<IClientExtensionProvider<CompoundTag, EnergyView>> provider = Optional.ofNullable(ResourceLocation.tryParse(accessor.getServerData().getString("JadeEnergyStorageUid"))).map(WailaClientRegistration.INSTANCE.energyStorageProviders::get);
                if (provider.isPresent()) {
                    List<ClientViewGroup<EnergyView>> groups = ((IClientExtensionProvider) provider.get()).getClientGroups(accessor, ViewGroup.readList(accessor.getServerData(), "JadeEnergyStorage", Function.identity()));
                    if (groups.isEmpty()) {
                        return;
                    }
                    IElementHelper helper = IElementHelper.get();
                    boolean renderGroup = groups.size() > 1 || ((ClientViewGroup) groups.get(0)).shouldRenderGroup();
                    ClientViewGroup.tooltip(tooltip, groups, renderGroup, (theTooltip, group) -> {
                        if (renderGroup) {
                            group.renderHeader(theTooltip);
                        }
                        for (EnergyView view : group.views) {
                            Component text;
                            if (view.overrideText != null) {
                                text = view.overrideText;
                            } else {
                                text = Component.translatable("jade.fe", ChatFormatting.WHITE + view.current, view.max).withStyle(ChatFormatting.GRAY);
                            }
                            IProgressStyle progressStyle = helper.progressStyle().color(-5636096, -10092544);
                            theTooltip.add(helper.progress(view.ratio, text, progressStyle, BoxStyle.DEFAULT, true));
                        }
                    });
                }
            }
        }
    }

    public static void putData(Accessor<?> accessor) {
        CompoundTag tag = accessor.getServerData();
        Object target = accessor.getTarget();
        ServerPlayer player = (ServerPlayer) accessor.getPlayer();
        boolean showDetails = accessor.showDetails();
        for (IServerExtensionProvider<Object, CompoundTag> provider : WailaCommonRegistration.INSTANCE.energyStorageProviders.get(target)) {
            List<ViewGroup<CompoundTag>> groups = provider.getGroups(player, player.serverLevel(), target, showDetails);
            if (groups != null) {
                if (ViewGroup.saveList(tag, "JadeEnergyStorage", groups, Function.identity())) {
                    tag.putString("JadeEnergyStorageUid", provider.getUid().toString());
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
        return Identifiers.UNIVERSAL_ENERGY_STORAGE;
    }

    @Override
    public int getDefaultPriority() {
        return 1000;
    }

    @Override
    public List<ClientViewGroup<EnergyView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<CompoundTag>> groups) {
        return groups.stream().map($ -> {
            String unit = $.getExtraData().getString("Unit");
            return new ClientViewGroup($.views.stream().map(tag -> EnergyView.read(tag, unit)).filter(Objects::nonNull).toList());
        }).toList();
    }

    @Override
    public List<ViewGroup<CompoundTag>> getGroups(ServerPlayer player, ServerLevel world, Object target, boolean showDetails) {
        return CommonProxy.wrapEnergyStorage(target, player);
    }
}