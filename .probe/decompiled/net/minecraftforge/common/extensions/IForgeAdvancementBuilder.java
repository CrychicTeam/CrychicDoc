package net.minecraftforge.common.extensions;

import com.google.common.collect.Maps;
import java.util.function.Consumer;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.common.data.ExistingFileHelper;

public interface IForgeAdvancementBuilder {

    private Advancement.Builder self() {
        return (Advancement.Builder) this;
    }

    default Advancement save(Consumer<Advancement> saver, ResourceLocation id, ExistingFileHelper fileHelper) {
        boolean canBuild = this.self().canBuild(advancementId -> fileHelper.exists(advancementId, PackType.SERVER_DATA, ".json", "advancements") ? new Advancement(advancementId, null, null, AdvancementRewards.EMPTY, Maps.newHashMap(), new String[0][0], false) : null);
        if (!canBuild) {
            throw new IllegalStateException("Tried to build Advancement without valid Parent!");
        } else {
            Advancement advancement = this.self().build(id);
            saver.accept(advancement);
            return advancement;
        }
    }
}