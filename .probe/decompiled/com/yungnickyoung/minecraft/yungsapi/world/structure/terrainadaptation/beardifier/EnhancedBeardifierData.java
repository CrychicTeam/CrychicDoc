package com.yungnickyoung.minecraft.yungsapi.world.structure.terrainadaptation.beardifier;

import it.unimi.dsi.fastutil.objects.ObjectListIterator;

public interface EnhancedBeardifierData {

    ObjectListIterator<EnhancedBeardifierRigid> getEnhancedRigidIterator();

    void setEnhancedRigidIterator(ObjectListIterator<EnhancedBeardifierRigid> var1);

    ObjectListIterator<EnhancedJigsawJunction> getEnhancedJunctionIterator();

    void setEnhancedJunctionIterator(ObjectListIterator<EnhancedJigsawJunction> var1);
}