package com.blamejared.controlling.platform;

import com.blamejared.controlling.api.events.IKeyEntryListenersEvent;
import com.blamejared.controlling.api.events.IKeyEntryMouseClickedEvent;
import com.blamejared.controlling.api.events.IKeyEntryMouseReleasedEvent;
import com.blamejared.controlling.api.events.IKeyEntryRenderEvent;
import com.blamejared.controlling.client.NewKeyBindsList;
import com.mojang.datafixers.util.Either;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.util.Unit;

public interface IEventHelper {

    Either<IKeyEntryListenersEvent, List<GuiEventListener>> fireKeyEntryListenersEvent(NewKeyBindsList.KeyEntry var1);

    Either<IKeyEntryMouseClickedEvent, Boolean> fireKeyEntryMouseClickedEvent(NewKeyBindsList.KeyEntry var1, double var2, double var4, int var6);

    Either<IKeyEntryMouseReleasedEvent, Boolean> fireKeyEntryMouseReleasedEvent(NewKeyBindsList.KeyEntry var1, double var2, double var4, int var6);

    Either<IKeyEntryRenderEvent, Unit> fireKeyEntryRenderEvent(NewKeyBindsList.KeyEntry var1, GuiGraphics var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, boolean var10, float var11);
}