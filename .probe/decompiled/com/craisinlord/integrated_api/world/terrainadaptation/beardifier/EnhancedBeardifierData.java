package com.craisinlord.integrated_api.world.terrainadaptation.beardifier;

import it.unimi.dsi.fastutil.objects.ObjectListIterator;

public interface EnhancedBeardifierData {

    ObjectListIterator<EnhancedBeardifierRigid> getEnhancedRigidIterator();

    void setEnhancedRigidIterator(ObjectListIterator<EnhancedBeardifierRigid> var1);

    ObjectListIterator<EnhancedJigsawJunction> getEnhancedJunctionIterator();

    void setEnhancedJunctionIterator(ObjectListIterator<EnhancedJigsawJunction> var1);
}