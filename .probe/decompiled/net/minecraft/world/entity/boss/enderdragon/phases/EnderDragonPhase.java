package net.minecraft.world.entity.boss.enderdragon.phases;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;

public class EnderDragonPhase<T extends DragonPhaseInstance> {

    private static EnderDragonPhase<?>[] phases = new EnderDragonPhase[0];

    public static final EnderDragonPhase<DragonHoldingPatternPhase> HOLDING_PATTERN = create(DragonHoldingPatternPhase.class, "HoldingPattern");

    public static final EnderDragonPhase<DragonStrafePlayerPhase> STRAFE_PLAYER = create(DragonStrafePlayerPhase.class, "StrafePlayer");

    public static final EnderDragonPhase<DragonLandingApproachPhase> LANDING_APPROACH = create(DragonLandingApproachPhase.class, "LandingApproach");

    public static final EnderDragonPhase<DragonLandingPhase> LANDING = create(DragonLandingPhase.class, "Landing");

    public static final EnderDragonPhase<DragonTakeoffPhase> TAKEOFF = create(DragonTakeoffPhase.class, "Takeoff");

    public static final EnderDragonPhase<DragonSittingFlamingPhase> SITTING_FLAMING = create(DragonSittingFlamingPhase.class, "SittingFlaming");

    public static final EnderDragonPhase<DragonSittingScanningPhase> SITTING_SCANNING = create(DragonSittingScanningPhase.class, "SittingScanning");

    public static final EnderDragonPhase<DragonSittingAttackingPhase> SITTING_ATTACKING = create(DragonSittingAttackingPhase.class, "SittingAttacking");

    public static final EnderDragonPhase<DragonChargePlayerPhase> CHARGING_PLAYER = create(DragonChargePlayerPhase.class, "ChargingPlayer");

    public static final EnderDragonPhase<DragonDeathPhase> DYING = create(DragonDeathPhase.class, "Dying");

    public static final EnderDragonPhase<DragonHoverPhase> HOVERING = create(DragonHoverPhase.class, "Hover");

    private final Class<? extends DragonPhaseInstance> instanceClass;

    private final int id;

    private final String name;

    private EnderDragonPhase(int int0, Class<? extends DragonPhaseInstance> classExtendsDragonPhaseInstance1, String string2) {
        this.id = int0;
        this.instanceClass = classExtendsDragonPhaseInstance1;
        this.name = string2;
    }

    public DragonPhaseInstance createInstance(EnderDragon enderDragon0) {
        try {
            Constructor<? extends DragonPhaseInstance> $$1 = this.getConstructor();
            return (DragonPhaseInstance) $$1.newInstance(enderDragon0);
        } catch (Exception var3) {
            throw new Error(var3);
        }
    }

    protected Constructor<? extends DragonPhaseInstance> getConstructor() throws NoSuchMethodException {
        return this.instanceClass.getConstructor(EnderDragon.class);
    }

    public int getId() {
        return this.id;
    }

    public String toString() {
        return this.name + " (#" + this.id + ")";
    }

    public static EnderDragonPhase<?> getById(int int0) {
        return int0 >= 0 && int0 < phases.length ? phases[int0] : HOLDING_PATTERN;
    }

    public static int getCount() {
        return phases.length;
    }

    private static <T extends DragonPhaseInstance> EnderDragonPhase<T> create(Class<T> classT0, String string1) {
        EnderDragonPhase<T> $$2 = new EnderDragonPhase<>(phases.length, classT0, string1);
        phases = (EnderDragonPhase<?>[]) Arrays.copyOf(phases, phases.length + 1);
        phases[$$2.getId()] = $$2;
        return $$2;
    }
}