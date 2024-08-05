package com.github.alexmodguy.alexscaves.server.entity;

import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import java.util.Optional;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ACEntityDataRegistry {

    public static final DeferredRegister<EntityDataSerializer<?>> DEF_REG = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, "alexscaves");

    public static final RegistryObject<EntityDataSerializer<Optional<Vec3>>> OPTIONAL_VEC_3 = DEF_REG.register("optional_vec_3", () -> EntityDataSerializer.optional(ACMath::writeVec3, ACMath::readVec3));
}