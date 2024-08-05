package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.HitResult;

public final class InteractionInputEvents {

    @Deprecated(forRemoval = true)
    public static final EventInvoker<InteractionInputEvents.Attack> ATTACK = EventInvoker.lookup(InteractionInputEvents.Attack.class);

    public static final EventInvoker<InteractionInputEvents.AttackV2> ATTACK_V2 = EventInvoker.lookup(InteractionInputEvents.AttackV2.class);

    public static final EventInvoker<InteractionInputEvents.Use> USE = EventInvoker.lookup(InteractionInputEvents.Use.class);

    public static final EventInvoker<InteractionInputEvents.Pick> PICK = EventInvoker.lookup(InteractionInputEvents.Pick.class);

    private InteractionInputEvents() {
    }

    @Deprecated(forRemoval = true)
    @FunctionalInterface
    public interface Attack {

        EventResult onAttackInteraction(Minecraft var1, LocalPlayer var2);
    }

    @FunctionalInterface
    public interface AttackV2 {

        EventResult onAttackInteraction(Minecraft var1, LocalPlayer var2, HitResult var3);
    }

    @FunctionalInterface
    public interface Pick {

        EventResult onPickInteraction(Minecraft var1, LocalPlayer var2, HitResult var3);
    }

    @FunctionalInterface
    public interface Use {

        EventResult onUseInteraction(Minecraft var1, LocalPlayer var2, InteractionHand var3, HitResult var4);
    }
}