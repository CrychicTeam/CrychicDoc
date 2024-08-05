package dev.xkmc.l2artifacts.content.core;

import dev.xkmc.l2artifacts.content.config.SlotStatConfig;
import dev.xkmc.l2artifacts.content.config.StatTypeConfig;
import dev.xkmc.l2artifacts.content.search.token.IArtifactFeature;
import dev.xkmc.l2artifacts.content.upgrades.Upgrade;
import dev.xkmc.l2artifacts.init.data.ArtifactSlotCuriosType;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2library.base.NamedEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

public class ArtifactSlot extends NamedEntry<ArtifactSlot> implements IArtifactFeature.Sprite {

    private final ArtifactSlotCuriosType curios;

    public ArtifactSlot(ArtifactSlotCuriosType curios) {
        super(ArtifactTypeRegistry.SLOT);
        this.curios = curios;
    }

    public void generate(ArtifactStats stat, Upgrade upgrade, RandomSource random) {
        List<ResourceLocation> main_list = new ArrayList((Collection) SlotStatConfig.getInstance().available_main_stats.get(this));
        List<ResourceLocation> sub_list = new ArrayList((Collection) SlotStatConfig.getInstance().available_sub_stats.get(this));
        ResourceLocation main = (ResourceLocation) main_list.get(random.nextInt(main_list.size()));
        sub_list.remove(main);
        stat.add(main, StatTypeConfig.get(main).getInitialValue(random, upgrade.removeMain()));
        int roll = stat.rank - 1;
        for (int i = 0; i < roll && !sub_list.isEmpty(); i++) {
            ResourceLocation sub;
            if (!upgrade.stats.isEmpty()) {
                sub = (ResourceLocation) upgrade.stats.remove(0);
            } else {
                int index = random.nextInt(sub_list.size());
                sub = (ResourceLocation) sub_list.get(index);
            }
            sub_list.remove(sub);
            stat.add(sub, StatTypeConfig.get(sub).getSubValue(random, upgrade.removeSub()));
        }
    }

    @Override
    public ResourceLocation getIcon() {
        return new ResourceLocation("l2artifacts", "textures/slot/empty_artifact_" + this.getRegistryName().getPath() + "_slot.png");
    }

    public String getCurioIdentifier() {
        return this.curios.getIdentifier();
    }
}