package dev.xkmc.l2weaponry.init.data;

import dev.xkmc.l2damagetracker.contents.damage.DamageTypeRoot;
import dev.xkmc.l2damagetracker.contents.damage.DamageTypeWrapper;
import dev.xkmc.l2damagetracker.contents.damage.DamageWrapperTagProvider;
import dev.xkmc.l2damagetracker.contents.damage.DefaultDamageState;
import dev.xkmc.l2damagetracker.init.data.DamageTypeAndTagsGen;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
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

public class LWDamageTypeGen extends DamageTypeAndTagsGen {

    public static final DamageTypeRoot TRIDENT = new DamageTypeRoot("l2weaponry", DamageTypes.TRIDENT, List.of(DamageTypeTags.IS_PROJECTILE), type -> new DamageType("trident", 0.1F));

    protected static final List<DamageTypeWrapper> LIST = new ArrayList();

    public LWDamageTypeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> pvd, ExistingFileHelper helper) {
        super(output, pvd, helper, "l2weaponry");
    }

    public static void register() {
        TRIDENT.add(DefaultDamageState.BYPASS_MAGIC);
        TRIDENT.add(DefaultDamageState.BYPASS_ARMOR);
        TRIDENT.add(LWNegateStates.NO_PROJECTILE);
        TRIDENT.add(LWDamageStates.NO_ANGER);
        L2DamageTypes.PLAYER_ATTACK.add(LWDamageStates.NO_ANGER);
        L2DamageTypes.MOB_ATTACK.add(LWDamageStates.NO_ANGER);
        DamageTypeRoot.configureGeneration(Set.of("l2damagetracker", "l2weaponry"), "l2weaponry", LIST);
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