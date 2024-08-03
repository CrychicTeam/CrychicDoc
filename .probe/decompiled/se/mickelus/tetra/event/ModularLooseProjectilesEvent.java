package se.mickelus.tetra.event;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Event;

public class ModularLooseProjectilesEvent extends Event {

    private final ItemStack firingStack;

    private ItemStack ammoStack;

    private final ItemStack initialAmmoStack;

    private final Player shooter;

    private final Level level;

    private final int initialDrawProgress;

    private int drawProgress;

    private final double initialStrength;

    private double strength;

    private final boolean initialHasSuspend;

    private boolean hasSuspend;

    private final float initialProjectileVelocity;

    private float projectileVelocity;

    private final double initialMultishotSpread;

    private double multishotSpread;

    private final float initialAccuracy;

    private float accuracy;

    private final boolean initialInfiniteAmmo;

    private boolean infiniteAmmo;

    private final int initialCount;

    private int count;

    private final double initialBasePitch;

    private double basePitch;

    private final double initialBaseYaw;

    private double baseYaw;

    private List<Function<AbstractArrow, AbstractArrow>> projectileRemappers = new ArrayList();

    public ModularLooseProjectilesEvent(ItemStack firingStack, ItemStack ammoStack, Player shooter, Level level, int drawProgress, double strength, boolean hasSuspend, float projectileVelocity, double multishotSpread, float accuracy, boolean infiniteAmmo, int count, double basePitch, double baseYaw) {
        this.firingStack = firingStack;
        this.initialAmmoStack = ammoStack;
        this.ammoStack = ammoStack;
        this.shooter = shooter;
        this.level = level;
        this.drawProgress = drawProgress;
        this.initialDrawProgress = drawProgress;
        this.strength = strength;
        this.initialStrength = strength;
        this.hasSuspend = hasSuspend;
        this.initialHasSuspend = hasSuspend;
        this.projectileVelocity = projectileVelocity;
        this.initialProjectileVelocity = projectileVelocity;
        this.multishotSpread = multishotSpread;
        this.initialMultishotSpread = multishotSpread;
        this.accuracy = accuracy;
        this.initialAccuracy = accuracy;
        this.infiniteAmmo = infiniteAmmo;
        this.initialInfiniteAmmo = infiniteAmmo;
        this.count = count;
        this.initialCount = count;
        this.basePitch = basePitch;
        this.initialBasePitch = basePitch;
        this.baseYaw = baseYaw;
        this.initialBaseYaw = baseYaw;
    }

    public ItemStack getFiringStack() {
        return this.firingStack;
    }

    public ItemStack getInitialAmmoStack() {
        return this.initialAmmoStack;
    }

    public ItemStack getAmmoStack() {
        return this.ammoStack;
    }

    public void setAmmoStack(ItemStack ammoStack) {
        this.ammoStack = ammoStack;
    }

    public Player getShooter() {
        return this.shooter;
    }

    public Level getLevel() {
        return this.level;
    }

    public int getInitialDrawProgress() {
        return this.initialDrawProgress;
    }

    public double getInitialStrength() {
        return this.initialStrength;
    }

    public boolean isInitialHasSuspend() {
        return this.initialHasSuspend;
    }

    public float getInitialProjectileVelocity() {
        return this.initialProjectileVelocity;
    }

    public double getInitialMultishotSpread() {
        return this.initialMultishotSpread;
    }

    public float getInitialAccuracy() {
        return this.initialAccuracy;
    }

    public boolean isInitialInfiniteAmmo() {
        return this.initialInfiniteAmmo;
    }

    public int getInitialCount() {
        return this.initialCount;
    }

    public double getInitialBasePitch() {
        return this.initialBasePitch;
    }

    public double getInitialBaseYaw() {
        return this.initialBaseYaw;
    }

    public int getDrawProgress() {
        return this.drawProgress;
    }

    public void setDrawProgress(int drawProgress) {
        this.drawProgress = drawProgress;
    }

    public double getStrength() {
        return this.strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public boolean isHasSuspend() {
        return this.hasSuspend;
    }

    public void setHasSuspend(boolean hasSuspend) {
        this.hasSuspend = hasSuspend;
    }

    public float getProjectileVelocity() {
        return this.projectileVelocity;
    }

    public void setProjectileVelocity(float projectileVelocity) {
        this.projectileVelocity = projectileVelocity;
    }

    public double getMultishotSpread() {
        return this.multishotSpread;
    }

    public void setMultishotSpread(double multishotSpread) {
        this.multishotSpread = multishotSpread;
    }

    public float getAccuracy() {
        return this.accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public boolean isInfiniteAmmo() {
        return this.infiniteAmmo;
    }

    public void setInfiniteAmmo(boolean infiniteAmmo) {
        this.infiniteAmmo = infiniteAmmo;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getBasePitch() {
        return this.basePitch;
    }

    public void setBasePitch(double basePitch) {
        this.basePitch = basePitch;
    }

    public double getBaseYaw() {
        return this.baseYaw;
    }

    public void setBaseYaw(double baseYaw) {
        this.baseYaw = baseYaw;
    }

    public void addProjectileRemapper(Function<AbstractArrow, AbstractArrow> remapper) {
        this.projectileRemappers.add(remapper);
    }

    public ImmutableList<Function<AbstractArrow, AbstractArrow>> getProjectileRemappers() {
        return ImmutableList.copyOf(this.projectileRemappers);
    }
}