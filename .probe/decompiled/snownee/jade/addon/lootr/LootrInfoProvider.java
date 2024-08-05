package snownee.jade.addon.lootr;

import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import noobanidus.mods.lootr.api.blockentity.ILootBlockEntity;
import noobanidus.mods.lootr.data.DataStorage;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;

public enum LootrInfoProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        appendTooltip(tooltip, accessor);
    }

    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        if (accessor.getBlockEntity() instanceof ILootBlockEntity tile) {
            appendServerData(data, tile.getTileId());
        }
    }

    public static void appendTooltip(ITooltip tooltip, Accessor<?> accessor) {
        CompoundTag data = accessor.getServerData();
        int decayValue = data.getInt("LootrDecay");
        IThemeHelper t = IThemeHelper.get();
        if (decayValue > 0) {
            tooltip.add(Component.translatable("jadeaddons.lootr.decay", t.seconds(decayValue)));
        }
        if (data.getBoolean("LootrRefreshed")) {
            tooltip.add(Component.translatable("jadeaddons.lootr.refreshed"));
        } else {
            int refreshValue = data.getInt("LootrRefresh");
            if (refreshValue > 0) {
                tooltip.add(Component.translatable("jadeaddons.lootr.refresh", t.seconds(refreshValue)));
            }
        }
    }

    public static void appendServerData(CompoundTag data, UUID id) {
        if (!DataStorage.isDecayed(id)) {
            int decayValue = DataStorage.getDecayValue(id);
            if (decayValue > 0) {
                data.putInt("LootrDecay", decayValue);
            }
        }
        if (DataStorage.isRefreshed(id)) {
            data.putBoolean("LootrRefreshed", true);
        } else {
            int refreshValue = DataStorage.getRefreshValue(id);
            if (refreshValue > 0) {
                data.putInt("LootrRefresh", refreshValue);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return LootrPlugin.INFO;
    }
}