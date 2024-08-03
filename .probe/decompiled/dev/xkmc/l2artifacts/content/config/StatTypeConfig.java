package dev.xkmc.l2artifacts.content.config;

import com.google.common.collect.ImmutableMultimap.Builder;
import dev.xkmc.l2artifacts.content.core.StatEntry;
import dev.xkmc.l2artifacts.content.search.token.IArtifactFeature;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2damagetracker.contents.curios.AttrTooltip;
import dev.xkmc.l2library.serial.config.BaseConfig;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.Collection;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

@SerialClass
public class StatTypeConfig extends BaseConfig implements IArtifactFeature.Sprite {

    @SerialField
    public double base;

    @SerialField
    public double base_low;

    @SerialField
    public double base_high;

    @SerialField
    public double main_low;

    @SerialField
    public double main_high;

    @SerialField
    public double sub_low;

    @SerialField
    public double sub_high;

    @SerialField
    public Attribute attr;

    @SerialField
    public AttributeModifier.Operation op;

    @SerialField
    public boolean usePercent;

    @SerialField
    public ResourceLocation icon;

    public static StatTypeConfig get(ResourceLocation id) {
        return NetworkManager.STAT_TYPES.getEntry(id);
    }

    public static Collection<StatTypeConfig> getValues() {
        return NetworkManager.STAT_TYPES.getAll();
    }

    public void getModifier(Builder<Attribute, AttributeModifier> builder, StatEntry entry, UUID uuid) {
        builder.put(this.attr, new AttributeModifier(uuid, entry.getName(), entry.getValue(), this.op));
    }

    public double getInitialValue(RandomSource random, boolean max) {
        return max ? this.base_high : Mth.nextDouble(random, this.base_low, this.base_high);
    }

    public double getMainValue(RandomSource random, boolean max) {
        return max ? this.main_high : Mth.nextDouble(random, this.main_low, this.main_high);
    }

    public double getSubValue(RandomSource random, boolean max) {
        return max ? this.base_high : Mth.nextDouble(random, this.sub_low, this.sub_high);
    }

    public MutableComponent getValueText(double val) {
        MutableComponent ans = Component.literal("+");
        ans = ans.append(ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(this.usePercent ? val * 100.0 : val));
        if (this.usePercent) {
            ans = ans.append("%");
        }
        return ans;
    }

    public Component getTooltip(double val) {
        boolean neg = val < 0.0 ^ AttrTooltip.isNegative(this.attr);
        return Component.translatable("attribute.modifier.plus." + (this.usePercent ? 1 : 0), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(this.usePercent ? val * 100.0 : val), Component.translatable(this.attr.getDescriptionId())).withStyle(neg ? ChatFormatting.RED : ChatFormatting.BLUE);
    }

    @Override
    public ResourceLocation getIcon() {
        if (this.icon != null) {
            return this.icon;
        } else {
            ResourceLocation rl = this.getID();
            return new ResourceLocation(rl.getNamespace(), "textures/stat_type/" + rl.getPath() + ".png");
        }
    }

    @Override
    public MutableComponent getDesc() {
        return Component.translatable("stat_type." + this.getID().getNamespace() + "." + this.getID().getPath());
    }

    public double getBaseValue() {
        return this.base;
    }
}