package com.craisinlord.integrated_api.mixins.structures;

import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ Beardifier.class })
public interface BeardifierAccessor {

    @Accessor
    ObjectListIterator<Beardifier.Rigid> getPieceIterator();

    @Accessor
    ObjectListIterator<JigsawJunction> getJunctionIterator();
}