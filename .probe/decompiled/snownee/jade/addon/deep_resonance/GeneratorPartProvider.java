package snownee.jade.addon.deep_resonance;

import mcjty.deepresonance.modules.generator.block.GeneratorPartTileEntity;
import mcjty.deepresonance.modules.generator.data.GeneratorBlob;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;

public enum GeneratorPartProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getServerData().contains("DeepResonance")) {
            IThemeHelper t = IThemeHelper.get();
            CompoundTag tag = accessor.getServerData().getCompound("DeepResonance");
            int id = tag.getInt("Id");
            int collectors = tag.getInt("Collectors");
            int generators = tag.getInt("Generators");
            tooltip.add(Component.translatable("jadeaddons.deepresonance.id", t.info(id)));
            tooltip.add(Component.translatable("jadeaddons.deepresonance.collectors", t.info(collectors)));
            tooltip.add(Component.translatable("jadeaddons.deepresonance.generators", t.info(generators)));
        }
    }

    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        if (accessor.getBlockEntity() instanceof GeneratorPartTileEntity part) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("Id", part.getMultiblockId());
            GeneratorBlob network = part.getBlob();
            tag.putInt("Collectors", network.getCollectorBlocks());
            tag.putInt("Generators", network.getGeneratorBlocks());
            data.put("DeepResonance", tag);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return DeepResonancePlugin.GENERATOR_PART;
    }
}