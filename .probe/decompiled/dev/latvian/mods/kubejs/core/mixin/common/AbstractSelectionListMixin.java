package dev.latvian.mods.kubejs.core.mixin.common;

import net.minecraft.client.gui.components.AbstractSelectionList;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ AbstractSelectionList.class })
public abstract class AbstractSelectionListMixin<E extends AbstractSelectionList.Entry<E>> {
}