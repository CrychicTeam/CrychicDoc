package fuzs.puzzleslib.mixin.accessor;

import java.util.List;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ LootTable.class })
public interface LootTableForgeAccessor {

    @Accessor("pools")
    List<LootPool> puzzleslib$getPools();
}