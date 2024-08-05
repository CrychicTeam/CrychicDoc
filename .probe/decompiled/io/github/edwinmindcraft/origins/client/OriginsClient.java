package io.github.edwinmindcraft.origins.client;

import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OriginsClient {

    public static final AtomicBoolean AWAITING_DISPLAY = new AtomicBoolean();

    public static final AtomicBoolean WAITING_FOR_POWERS = new AtomicBoolean();

    public static final AtomicBoolean OPEN_NEXT_LAYER = new AtomicBoolean();

    public static Set<ResourceKey<ConfiguredPower<?, ?>>> WAITING_POWERS = new HashSet();

    public static boolean SELECTION_WAS_ORB = false;

    public static boolean SHOW_DIRT_BACKGROUND = false;
}