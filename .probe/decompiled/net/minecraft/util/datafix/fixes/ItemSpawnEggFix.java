package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class ItemSpawnEggFix extends DataFix {

    private static final String[] ID_TO_ENTITY = (String[]) DataFixUtils.make(new String[256], p_16054_ -> {
        p_16054_[1] = "Item";
        p_16054_[2] = "XPOrb";
        p_16054_[7] = "ThrownEgg";
        p_16054_[8] = "LeashKnot";
        p_16054_[9] = "Painting";
        p_16054_[10] = "Arrow";
        p_16054_[11] = "Snowball";
        p_16054_[12] = "Fireball";
        p_16054_[13] = "SmallFireball";
        p_16054_[14] = "ThrownEnderpearl";
        p_16054_[15] = "EyeOfEnderSignal";
        p_16054_[16] = "ThrownPotion";
        p_16054_[17] = "ThrownExpBottle";
        p_16054_[18] = "ItemFrame";
        p_16054_[19] = "WitherSkull";
        p_16054_[20] = "PrimedTnt";
        p_16054_[21] = "FallingSand";
        p_16054_[22] = "FireworksRocketEntity";
        p_16054_[23] = "TippedArrow";
        p_16054_[24] = "SpectralArrow";
        p_16054_[25] = "ShulkerBullet";
        p_16054_[26] = "DragonFireball";
        p_16054_[30] = "ArmorStand";
        p_16054_[41] = "Boat";
        p_16054_[42] = "MinecartRideable";
        p_16054_[43] = "MinecartChest";
        p_16054_[44] = "MinecartFurnace";
        p_16054_[45] = "MinecartTNT";
        p_16054_[46] = "MinecartHopper";
        p_16054_[47] = "MinecartSpawner";
        p_16054_[40] = "MinecartCommandBlock";
        p_16054_[48] = "Mob";
        p_16054_[49] = "Monster";
        p_16054_[50] = "Creeper";
        p_16054_[51] = "Skeleton";
        p_16054_[52] = "Spider";
        p_16054_[53] = "Giant";
        p_16054_[54] = "Zombie";
        p_16054_[55] = "Slime";
        p_16054_[56] = "Ghast";
        p_16054_[57] = "PigZombie";
        p_16054_[58] = "Enderman";
        p_16054_[59] = "CaveSpider";
        p_16054_[60] = "Silverfish";
        p_16054_[61] = "Blaze";
        p_16054_[62] = "LavaSlime";
        p_16054_[63] = "EnderDragon";
        p_16054_[64] = "WitherBoss";
        p_16054_[65] = "Bat";
        p_16054_[66] = "Witch";
        p_16054_[67] = "Endermite";
        p_16054_[68] = "Guardian";
        p_16054_[69] = "Shulker";
        p_16054_[90] = "Pig";
        p_16054_[91] = "Sheep";
        p_16054_[92] = "Cow";
        p_16054_[93] = "Chicken";
        p_16054_[94] = "Squid";
        p_16054_[95] = "Wolf";
        p_16054_[96] = "MushroomCow";
        p_16054_[97] = "SnowMan";
        p_16054_[98] = "Ozelot";
        p_16054_[99] = "VillagerGolem";
        p_16054_[100] = "EntityHorse";
        p_16054_[101] = "Rabbit";
        p_16054_[120] = "Villager";
        p_16054_[200] = "EnderCrystal";
    });

    public ItemSpawnEggFix(Schema schema0, boolean boolean1) {
        super(schema0, boolean1);
    }

    public TypeRewriteRule makeRule() {
        Schema $$0 = this.getInputSchema();
        Type<?> $$1 = $$0.getType(References.ITEM_STACK);
        OpticFinder<Pair<String, String>> $$2 = DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), NamespacedSchema.namespacedString()));
        OpticFinder<String> $$3 = DSL.fieldFinder("id", DSL.string());
        OpticFinder<?> $$4 = $$1.findField("tag");
        OpticFinder<?> $$5 = $$4.type().findField("EntityTag");
        OpticFinder<?> $$6 = DSL.typeFinder($$0.getTypeRaw(References.ENTITY));
        Type<?> $$7 = this.getOutputSchema().getTypeRaw(References.ENTITY);
        return this.fixTypeEverywhereTyped("ItemSpawnEggFix", $$1, p_16044_ -> {
            Optional<Pair<String, String>> $$7x = p_16044_.getOptional($$2);
            if ($$7x.isPresent() && Objects.equals(((Pair) $$7x.get()).getSecond(), "minecraft:spawn_egg")) {
                Dynamic<?> $$8 = (Dynamic<?>) p_16044_.get(DSL.remainderFinder());
                short $$9 = $$8.get("Damage").asShort((short) 0);
                Optional<? extends Typed<?>> $$10 = p_16044_.getOptionalTyped($$4);
                Optional<? extends Typed<?>> $$11 = $$10.flatMap(p_145417_ -> p_145417_.getOptionalTyped($$5));
                Optional<? extends Typed<?>> $$12 = $$11.flatMap(p_145414_ -> p_145414_.getOptionalTyped($$6));
                Optional<String> $$13 = $$12.flatMap(p_145406_ -> p_145406_.getOptional($$3));
                Typed<?> $$14 = p_16044_;
                String $$15 = ID_TO_ENTITY[$$9 & 255];
                if ($$15 != null && (!$$13.isPresent() || !Objects.equals($$13.get(), $$15))) {
                    Typed<?> $$16 = p_16044_.getOrCreateTyped($$4);
                    Typed<?> $$17 = $$16.getOrCreateTyped($$5);
                    Typed<?> $$18 = $$17.getOrCreateTyped($$6);
                    Typed<?> $$20 = (Typed<?>) ((Pair) $$18.write().flatMap(p_145411_ -> $$7.readTyped(p_145411_.set("id", $$8.createString($$15)))).result().orElseThrow(() -> new IllegalStateException("Could not parse new entity"))).getFirst();
                    $$14 = p_16044_.set($$4, $$16.set($$5, $$17.set($$6, $$20)));
                }
                if ($$9 != 0) {
                    $$8 = $$8.set("Damage", $$8.createShort((short) 0));
                    $$14 = $$14.set(DSL.remainderFinder(), $$8);
                }
                return $$14;
            } else {
                return p_16044_;
            }
        });
    }
}