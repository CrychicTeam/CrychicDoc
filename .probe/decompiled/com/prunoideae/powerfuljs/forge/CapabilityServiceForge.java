package com.prunoideae.powerfuljs.forge;

import com.mojang.datafixers.util.Pair;
import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CapabilityServiceForge {

    public static final CapabilityServiceForge INSTANCE = new CapabilityServiceForge();

    protected final List<Pair<Predicate<ItemStack>, CapabilityBuilderForge<ItemStack, ?>>> itemTester = new ArrayList();

    protected final List<Pair<Predicate<BlockEntity>, CapabilityBuilderForge<BlockEntity, ?>>> beTester = new ArrayList();

    protected final List<Pair<Predicate<Entity>, CapabilityBuilderForge<Entity, ?>>> entityTester = new ArrayList();

    public Stream<CapabilityBuilderForge<ItemStack, ?>> getCapabilitiesFor(ItemStack stack) {
        return this.itemTester.stream().filter(pair -> ((Predicate) pair.getFirst()).test(stack)).map(Pair::getSecond);
    }

    public Stream<CapabilityBuilderForge<BlockEntity, ?>> getCapabilitiesFor(BlockEntity be) {
        return this.beTester.stream().filter(pair -> ((Predicate) pair.getFirst()).test(be)).map(Pair::getSecond);
    }

    public Stream<CapabilityBuilderForge<Entity, ?>> getCapabilitiesFor(Entity entity) {
        return this.entityTester.stream().filter(pair -> ((Predicate) pair.getFirst()).test(entity)).map(Pair::getSecond);
    }

    public void addItem(Predicate<ItemStack> predicate, CapabilityBuilderForge<ItemStack, ?> builder) {
        this.itemTester.add(new Pair(predicate, builder));
    }

    public void addBE(Predicate<BlockEntity> predicate, CapabilityBuilderForge<BlockEntity, ?> builder) {
        this.beTester.add(new Pair(predicate, builder));
    }

    public void addEntity(Predicate<Entity> predicate, CapabilityBuilderForge<Entity, ?> builder) {
        this.entityTester.add(new Pair(predicate, builder));
    }
}