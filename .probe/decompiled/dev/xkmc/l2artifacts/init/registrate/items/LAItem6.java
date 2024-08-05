package dev.xkmc.l2artifacts.init.registrate.items;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2artifacts.content.core.ArtifactSet;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetEffect;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.registrate.ArtifactTypeRegistry;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.SetRegHelper;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class LAItem6 {

    public static final SetEntry<ArtifactSet> SET_MAGE;

    public static final SetEntry<ArtifactSet> SET_PIRATE;

    public static final RegistryEntry<AttributeSetEffect> EFF_MAGE_2;

    public static final RegistryEntry<AttributeSetEffect> EFF_MAGE_4;

    public static final RegistryEntry<AttributeSetEffect> EFF_PIRATE_2;

    public static final RegistryEntry<AttributeSetEffect> EFF_PIRATE_4;

    public static void register() {
    }

    static {
        SetRegHelper helper = L2Artifacts.REGISTRATE.getSetHelper("mage");
        LinearFuncEntry m2 = helper.regLinear("mage_2_magic", 0.2, 0.1);
        LinearFuncEntry r4 = helper.regLinear("mage_4_reduction", 2.0, 1.0);
        EFF_MAGE_2 = helper.setEffect("mage_2", () -> new AttributeSetEffect(new AttrSetEntry(L2DamageTracker.MAGIC_FACTOR::get, AttributeModifier.Operation.ADDITION, m2, true))).lang("Magical Strength").register();
        EFF_MAGE_4 = helper.setEffect("mage_4", () -> new AttributeSetEffect(new AttrSetEntry(L2DamageTracker.ABSORB::get, AttributeModifier.Operation.ADDITION, r4, false))).lang("Magical Shield").register();
        SET_MAGE = helper.regSet(1, 5, "Mage").setSlots(ArtifactTypeRegistry.SLOT_HEAD, ArtifactTypeRegistry.SLOT_NECKLACE, ArtifactTypeRegistry.SLOT_BODY, ArtifactTypeRegistry.SLOT_BRACELET, ArtifactTypeRegistry.SLOT_BELT).regItems().buildConfig(c -> c.add(2, (SetEffect) LAItem6.EFF_MAGE_2.get()).add(4, (SetEffect) LAItem6.EFF_MAGE_4.get())).register();
        helper = L2Artifacts.REGISTRATE.getSetHelper("pirate");
        m2 = helper.regLinear("pirate_2_explosion", 0.2, 0.1);
        r4 = helper.regLinear("pirate_4_explosion", 0.4, 0.2);
        LinearFuncEntry m4 = helper.regLinear("pirate_4_magic", -0.4, 0.0);
        EFF_PIRATE_2 = helper.setEffect("pirate_2", () -> new AttributeSetEffect(new AttrSetEntry(L2DamageTracker.EXPLOSION_FACTOR::get, AttributeModifier.Operation.ADDITION, m2, true))).lang("Pirate Attack!").register();
        EFF_PIRATE_4 = helper.setEffect("pirate_4", () -> new AttributeSetEffect(new AttrSetEntry(L2DamageTracker.EXPLOSION_FACTOR::get, AttributeModifier.Operation.ADDITION, r4, true), new AttrSetEntry(L2DamageTracker.MAGIC_FACTOR::get, AttributeModifier.Operation.ADDITION, m4, true))).lang("Secret Bomb").register();
        SET_PIRATE = helper.regSet(1, 5, "Pirate").setSlots(ArtifactTypeRegistry.SLOT_HEAD, ArtifactTypeRegistry.SLOT_NECKLACE, ArtifactTypeRegistry.SLOT_BODY, ArtifactTypeRegistry.SLOT_BRACELET, ArtifactTypeRegistry.SLOT_BELT).regItems().buildConfig(c -> c.add(2, (SetEffect) LAItem6.EFF_PIRATE_2.get()).add(4, (SetEffect) LAItem6.EFF_PIRATE_4.get())).register();
    }
}