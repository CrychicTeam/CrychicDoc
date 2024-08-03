package dev.xkmc.l2backpack.init.loot;

import java.util.Random;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class BackpackGLMProvider extends GlobalLootModifierProvider {

    public BackpackGLMProvider(PackOutput gen) {
        super(gen, "l2backpack");
    }

    @Override
    protected void start() {
        Random r = new Random(12345L);
        for (LootGen.LootDefinition def : LootGen.LootDefinition.values()) {
            this.add(def.id, new BackpackLootModifier(def.chance, def, r.nextLong(), LootTableIdCondition.builder(def.target).build()));
        }
    }
}