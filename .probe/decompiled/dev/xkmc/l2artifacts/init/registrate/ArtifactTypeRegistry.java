package dev.xkmc.l2artifacts.init.registrate;

import com.mojang.serialization.Codec;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.core.ArtifactSlot;
import dev.xkmc.l2artifacts.content.core.LinearFuncHandle;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.data.ArtifactSlotCuriosType;
import dev.xkmc.l2artifacts.init.data.loot.AddLootTableModifier;
import dev.xkmc.l2artifacts.init.data.loot.ArtifactLootModifier;
import dev.xkmc.l2library.base.L2Registrate;
import net.minecraftforge.registries.ForgeRegistries;

public class ArtifactTypeRegistry {

    public static final L2Registrate.RegistryInstance<ArtifactSlot> SLOT = L2Artifacts.REGISTRATE.newRegistry("slot", ArtifactSlot.class);

    public static final L2Registrate.RegistryInstance<ArtifactSet> SET = L2Artifacts.REGISTRATE.newRegistry("set", ArtifactSet.class);

    public static final L2Registrate.RegistryInstance<SetEffect> SET_EFFECT = L2Artifacts.REGISTRATE.newRegistry("set_effect", SetEffect.class);

    public static final L2Registrate.RegistryInstance<LinearFuncHandle> LINEAR = L2Artifacts.REGISTRATE.newRegistry("linear", LinearFuncHandle.class);

    public static final RegistryEntry<ArtifactSlot> SLOT_HEAD = regSlot("head", () -> new ArtifactSlot(ArtifactSlotCuriosType.HEAD));

    public static final RegistryEntry<ArtifactSlot> SLOT_NECKLACE = regSlot("necklace", () -> new ArtifactSlot(ArtifactSlotCuriosType.NECKLACE));

    public static final RegistryEntry<ArtifactSlot> SLOT_BRACELET = regSlot("bracelet", () -> new ArtifactSlot(ArtifactSlotCuriosType.BRACELET));

    public static final RegistryEntry<ArtifactSlot> SLOT_BODY = regSlot("body", () -> new ArtifactSlot(ArtifactSlotCuriosType.BODY));

    public static final RegistryEntry<ArtifactSlot> SLOT_BELT = regSlot("belt", () -> new ArtifactSlot(ArtifactSlotCuriosType.BELT));

    public static final RegistryEntry<Codec<ArtifactLootModifier>> SER = L2Artifacts.REGISTRATE.simple("main", ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, () -> ArtifactLootModifier.CODEC);

    public static final RegistryEntry<Codec<AddLootTableModifier>> ADD_TABLE = L2Artifacts.REGISTRATE.simple("add_table", ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, () -> AddLootTableModifier.CODEC);

    public static void register() {
    }

    private static RegistryEntry<ArtifactSlot> regSlot(String id, NonNullSupplier<ArtifactSlot> slot) {
        return L2Artifacts.REGISTRATE.generic(SLOT, id, slot).defaultLang().register();
    }
}