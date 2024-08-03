package dev.xkmc.l2archery.init.data;

import dev.xkmc.l2damagetracker.contents.damage.DamageTypeRoot;
import dev.xkmc.l2damagetracker.contents.damage.DamageTypeWrapper;
import dev.xkmc.l2damagetracker.contents.damage.DamageWrapperTagProvider;
import dev.xkmc.l2damagetracker.contents.damage.DefaultDamageState;
import dev.xkmc.l2damagetracker.init.data.DamageTypeAndTagsGen;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ArcheryDamageMultiplex extends DamageTypeAndTagsGen {

    public static final DamageTypeRoot ARROW = new DamageTypeRoot("l2archery", DamageTypes.ARROW, List.of(DamageTypeTags.IS_PROJECTILE), type -> new DamageType("arrow", 0.1F));

    protected static final List<DamageTypeWrapper> LIST = new ArrayList();

    public static void register() {
        ARROW.add(DefaultDamageState.BYPASS_MAGIC);
        ARROW.add(DefaultDamageState.BYPASS_ARMOR);
        ARROW.add(ArcheryDamageState.BYPASS_INVUL);
        DamageTypeRoot.configureGeneration(Set.of("l2damagetracker", "l2archery"), "l2archery", LIST);
    }

    public ArcheryDamageMultiplex(PackOutput output, CompletableFuture<HolderLookup.Provider> pvd, ExistingFileHelper files) {
        super(output, pvd, files, "l2archery");
    }

    protected void addDamageTypes(BootstapContext<DamageType> ctx) {
        DamageTypeRoot.generateAll();
        for (DamageTypeWrapper wrapper : LIST) {
            ctx.register(wrapper.type(), wrapper.getObject());
        }
    }

    protected void addDamageTypeTags(DamageWrapperTagProvider pvd, HolderLookup.Provider lookup) {
        DamageTypeRoot.generateAll();
        for (DamageTypeWrapper wrapper : LIST) {
            wrapper.gen(pvd, lookup);
        }
    }
}