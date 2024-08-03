package dev.ftb.mods.ftbquests.quest.task;

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

public final class TaskType {

    private final ResourceLocation typeId;

    private final TaskType.Provider provider;

    private final Supplier<Icon> iconSupplier;

    private Component displayName;

    private TaskType.GuiProvider guiProvider;

    public int internalId;

    TaskType(ResourceLocation typeId, TaskType.Provider provider, Supplier<Icon> iconSupplier) {
        this.typeId = typeId;
        this.provider = provider;
        this.iconSupplier = iconSupplier;
        this.displayName = null;
        this.guiProvider = GuiProviders.defaultTaskGuiProvider(provider);
    }

    public ResourceLocation getTypeId() {
        return this.typeId;
    }

    @Nullable
    public static Task createTask(long id, Quest quest, String typeId) {
        if (typeId.isEmpty()) {
            typeId = "ftbquests:item";
        } else if (typeId.indexOf(58) == -1) {
            typeId = "ftbquests:" + typeId;
        }
        TaskType type = (TaskType) TaskTypes.TYPES.get(new ResourceLocation(typeId));
        return type == null ? null : type.provider.create(id, quest);
    }

    public Task createTask(long id, Quest quest) {
        return this.provider.create(id, quest);
    }

    public String getTypeForNBT() {
        return this.typeId.getNamespace().equals("ftbquests") ? this.typeId.getPath() : this.typeId.toString();
    }

    public TaskType setDisplayName(Component name) {
        this.displayName = name;
        return this;
    }

    public Component getDisplayName() {
        if (this.displayName == null) {
            this.displayName = Component.translatable("ftbquests.task." + this.typeId.getNamespace() + "." + this.typeId.getPath());
        }
        return this.displayName;
    }

    public Icon getIconSupplier() {
        return (Icon) this.iconSupplier.get();
    }

    public TaskType setGuiProvider(TaskType.GuiProvider p) {
        this.guiProvider = p;
        return this;
    }

    public TaskType.GuiProvider getGuiProvider() {
        return this.guiProvider;
    }

    @FunctionalInterface
    public interface GuiProvider {

        @OnlyIn(Dist.CLIENT)
        void openCreationGui(Panel var1, Quest var2, Consumer<Task> var3);
    }

    @FunctionalInterface
    public interface Provider {

        Task create(long var1, Quest var3);
    }
}