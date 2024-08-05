package dev.xkmc.modulargolems.content.core;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.IGolemModel;
import net.minecraft.client.model.geom.EntityModelSet;

public interface ModelProvider<T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> {

    IGolemModel<T, P, ?> generateModel(EntityModelSet var1);
}