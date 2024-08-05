package com.mna.commands;

import com.mna.api.commands.AffinityArgument;
import com.mna.api.commands.FactionArgument;
import com.mna.api.commands.SpellPartArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CommandSerializerInit {

    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENTS = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, "mna");

    public static final RegistryObject<ArgumentTypeInfo<?, ?>> SPELL_PART_ARG = ARGUMENTS.register("spell_part_arg", () -> ArgumentTypeInfos.registerByClass(SpellPartArgument.class, SingletonArgumentInfo.contextFree(SpellPartArgument::spell)));

    public static final RegistryObject<ArgumentTypeInfo<?, ?>> AFFINITY_ARG = ARGUMENTS.register("affinity_arg", () -> ArgumentTypeInfos.registerByClass(AffinityArgument.class, SingletonArgumentInfo.contextFree(AffinityArgument::affinity)));

    public static final RegistryObject<ArgumentTypeInfo<?, ?>> FACTION_ARG = ARGUMENTS.register("faction_arg", () -> ArgumentTypeInfos.registerByClass(FactionArgument.class, SingletonArgumentInfo.contextFree(FactionArgument::faction)));
}