package net.minecraftforge.common.world;

import com.mojang.serialization.Codec;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public interface StructureModifier {

    Codec<StructureModifier> DIRECT_CODEC = ExtraCodecs.lazyInitializedCodec(() -> ((IForgeRegistry) ForgeRegistries.STRUCTURE_MODIFIER_SERIALIZERS.get()).getCodec()).dispatch(StructureModifier::codec, Function.identity());

    Codec<Holder<StructureModifier>> REFERENCE_CODEC = RegistryFileCodec.create(ForgeRegistries.Keys.STRUCTURE_MODIFIERS, DIRECT_CODEC);

    Codec<HolderSet<StructureModifier>> LIST_CODEC = RegistryCodecs.homogeneousList(ForgeRegistries.Keys.STRUCTURE_MODIFIERS, DIRECT_CODEC);

    void modify(Holder<Structure> var1, StructureModifier.Phase var2, ModifiableStructureInfo.StructureInfo.Builder var3);

    Codec<? extends StructureModifier> codec();

    public static enum Phase {

        BEFORE_EVERYTHING, ADD, REMOVE, MODIFY, AFTER_EVERYTHING
    }
}