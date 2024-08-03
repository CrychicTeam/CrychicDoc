package dev.ftb.mods.ftbquests.quest.reward;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftbquests.client.GuiProviders;
import dev.ftb.mods.ftbquests.quest.Quest;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public final class RewardType {

    private final ResourceLocation typeId;

    private final RewardType.Provider provider;

    private final Supplier<Icon> iconSupplier;

    private Component displayName;

    private RewardType.GuiProvider guiProvider;

    private boolean excludeFromListRewards = false;

    public int intId;

    public RewardType(ResourceLocation typeId, RewardType.Provider provider, Supplier<Icon> iconSupplier) {
        this.typeId = typeId;
        this.provider = provider;
        this.iconSupplier = iconSupplier;
        this.displayName = null;
        this.guiProvider = GuiProviders.defaultRewardGuiProvider(provider);
    }

    @Nullable
    public static Reward createReward(long id, Quest quest, String typeId) {
        if (typeId.isEmpty()) {
            typeId = "ftbquests:item";
        } else if (typeId.indexOf(58) == -1) {
            typeId = "ftbquests:" + typeId;
        }
        RewardType type = (RewardType) RewardTypes.TYPES.get(new ResourceLocation(typeId));
        return type == null ? null : type.provider.create(id, quest);
    }

    public ResourceLocation getTypeId() {
        return this.typeId;
    }

    public Reward createReward(long id, Quest quest) {
        return this.provider.create(id, quest);
    }

    public String getTypeForNBT() {
        return this.typeId.getNamespace().equals("ftbquests") ? this.typeId.getPath() : this.typeId.toString();
    }

    public RewardType setDisplayName(Component name) {
        this.displayName = name;
        return this;
    }

    public Component getDisplayName() {
        if (this.displayName == null) {
            this.displayName = Component.translatable("ftbquests.reward." + this.typeId.getNamespace() + "." + this.typeId.getPath());
        }
        return this.displayName;
    }

    public Icon getIconSupplier() {
        return (Icon) this.iconSupplier.get();
    }

    public RewardType setGuiProvider(RewardType.GuiProvider p) {
        this.guiProvider = p;
        return this;
    }

    public RewardType.GuiProvider getGuiProvider() {
        return this.guiProvider;
    }

    public RewardType setExcludeFromListRewards(boolean v) {
        this.excludeFromListRewards = v;
        return this;
    }

    public boolean getExcludeFromListRewards() {
        return this.excludeFromListRewards;
    }

    @FunctionalInterface
    public interface GuiProvider {

        @OnlyIn(Dist.CLIENT)
        void openCreationGui(Panel var1, Quest var2, Consumer<Reward> var3);
    }

    @FunctionalInterface
    public interface Provider {

        Reward create(long var1, Quest var3);
    }
}