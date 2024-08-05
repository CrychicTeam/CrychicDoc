package snownee.jade.addon.deep_resonance;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import mcjty.deepresonance.modules.core.block.ResonatingCrystalTileEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum CrystalProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    INSTANCE;

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getServerData().contains("DeepResonance")) {
            CompoundTag tag = accessor.getServerData().getCompound("DeepResonance");
            double strength = tag.getDouble("Strength");
            double efficiency = tag.getDouble("Efficiency");
            double purity = tag.getDouble("Purity");
            int rfpertick = tag.getInt("RfPerTick");
            double power = tag.getDouble("Power");
            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            decimalFormat.setRoundingMode(RoundingMode.DOWN);
            tooltip.add(Component.translatable("jadeaddons.deepresonance.sep", decimalFormat.format(strength), decimalFormat.format(efficiency), decimalFormat.format(purity)));
            tooltip.add(Component.translatable("jadeaddons.deepresonance.crystalPower", decimalFormat.format(power), rfpertick));
        }
    }

    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        if (accessor.getBlockEntity() instanceof ResonatingCrystalTileEntity crystal) {
            CompoundTag tag = new CompoundTag();
            tag.putDouble("Strength", crystal.getStrength());
            tag.putDouble("Efficiency", crystal.getEfficiency());
            tag.putDouble("Purity", crystal.getPurity());
            tag.putInt("RfPerTick", crystal.getRfPerTick());
            tag.putDouble("Power", crystal.getPower());
            data.put("DeepResonance", tag);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return DeepResonancePlugin.CRYSTAL;
    }
}