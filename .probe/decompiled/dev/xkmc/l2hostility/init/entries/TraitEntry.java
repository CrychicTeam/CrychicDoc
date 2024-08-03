package dev.xkmc.l2hostility.init.entries;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraftforge.registries.RegistryObject;

public class TraitEntry<T extends MobTrait> extends RegistryEntry<T> {

    public TraitEntry(LHRegistrate owner, RegistryObject<T> delegate) {
        super(owner, delegate);
    }
}