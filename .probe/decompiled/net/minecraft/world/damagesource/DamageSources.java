package net.minecraft.world.damagesource;

import javax.annotation.Nullable;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;

public class DamageSources {

    private final Registry<DamageType> damageTypes;

    private final DamageSource inFire;

    private final DamageSource lightningBolt;

    private final DamageSource onFire;

    private final DamageSource lava;

    private final DamageSource hotFloor;

    private final DamageSource inWall;

    private final DamageSource cramming;

    private final DamageSource drown;

    private final DamageSource starve;

    private final DamageSource cactus;

    private final DamageSource fall;

    private final DamageSource flyIntoWall;

    private final DamageSource fellOutOfWorld;

    private final DamageSource generic;

    private final DamageSource magic;

    private final DamageSource wither;

    private final DamageSource dragonBreath;

    private final DamageSource dryOut;

    private final DamageSource sweetBerryBush;

    private final DamageSource freeze;

    private final DamageSource stalagmite;

    private final DamageSource outsideBorder;

    private final DamageSource genericKill;

    public DamageSources(RegistryAccess registryAccess0) {
        this.damageTypes = registryAccess0.registryOrThrow(Registries.DAMAGE_TYPE);
        this.inFire = this.source(DamageTypes.IN_FIRE);
        this.lightningBolt = this.source(DamageTypes.LIGHTNING_BOLT);
        this.onFire = this.source(DamageTypes.ON_FIRE);
        this.lava = this.source(DamageTypes.LAVA);
        this.hotFloor = this.source(DamageTypes.HOT_FLOOR);
        this.inWall = this.source(DamageTypes.IN_WALL);
        this.cramming = this.source(DamageTypes.CRAMMING);
        this.drown = this.source(DamageTypes.DROWN);
        this.starve = this.source(DamageTypes.STARVE);
        this.cactus = this.source(DamageTypes.CACTUS);
        this.fall = this.source(DamageTypes.FALL);
        this.flyIntoWall = this.source(DamageTypes.FLY_INTO_WALL);
        this.fellOutOfWorld = this.source(DamageTypes.FELL_OUT_OF_WORLD);
        this.generic = this.source(DamageTypes.GENERIC);
        this.magic = this.source(DamageTypes.MAGIC);
        this.wither = this.source(DamageTypes.WITHER);
        this.dragonBreath = this.source(DamageTypes.DRAGON_BREATH);
        this.dryOut = this.source(DamageTypes.DRY_OUT);
        this.sweetBerryBush = this.source(DamageTypes.SWEET_BERRY_BUSH);
        this.freeze = this.source(DamageTypes.FREEZE);
        this.stalagmite = this.source(DamageTypes.STALAGMITE);
        this.outsideBorder = this.source(DamageTypes.OUTSIDE_BORDER);
        this.genericKill = this.source(DamageTypes.GENERIC_KILL);
    }

    private DamageSource source(ResourceKey<DamageType> resourceKeyDamageType0) {
        return new DamageSource(this.damageTypes.getHolderOrThrow(resourceKeyDamageType0));
    }

    private DamageSource source(ResourceKey<DamageType> resourceKeyDamageType0, @Nullable Entity entity1) {
        return new DamageSource(this.damageTypes.getHolderOrThrow(resourceKeyDamageType0), entity1);
    }

    private DamageSource source(ResourceKey<DamageType> resourceKeyDamageType0, @Nullable Entity entity1, @Nullable Entity entity2) {
        return new DamageSource(this.damageTypes.getHolderOrThrow(resourceKeyDamageType0), entity1, entity2);
    }

    public DamageSource inFire() {
        return this.inFire;
    }

    public DamageSource lightningBolt() {
        return this.lightningBolt;
    }

    public DamageSource onFire() {
        return this.onFire;
    }

    public DamageSource lava() {
        return this.lava;
    }

    public DamageSource hotFloor() {
        return this.hotFloor;
    }

    public DamageSource inWall() {
        return this.inWall;
    }

    public DamageSource cramming() {
        return this.cramming;
    }

    public DamageSource drown() {
        return this.drown;
    }

    public DamageSource starve() {
        return this.starve;
    }

    public DamageSource cactus() {
        return this.cactus;
    }

    public DamageSource fall() {
        return this.fall;
    }

    public DamageSource flyIntoWall() {
        return this.flyIntoWall;
    }

    public DamageSource fellOutOfWorld() {
        return this.fellOutOfWorld;
    }

    public DamageSource generic() {
        return this.generic;
    }

    public DamageSource magic() {
        return this.magic;
    }

    public DamageSource wither() {
        return this.wither;
    }

    public DamageSource dragonBreath() {
        return this.dragonBreath;
    }

    public DamageSource dryOut() {
        return this.dryOut;
    }

    public DamageSource sweetBerryBush() {
        return this.sweetBerryBush;
    }

    public DamageSource freeze() {
        return this.freeze;
    }

    public DamageSource stalagmite() {
        return this.stalagmite;
    }

    public DamageSource fallingBlock(Entity entity0) {
        return this.source(DamageTypes.FALLING_BLOCK, entity0);
    }

    public DamageSource anvil(Entity entity0) {
        return this.source(DamageTypes.FALLING_ANVIL, entity0);
    }

    public DamageSource fallingStalactite(Entity entity0) {
        return this.source(DamageTypes.FALLING_STALACTITE, entity0);
    }

    public DamageSource sting(LivingEntity livingEntity0) {
        return this.source(DamageTypes.STING, livingEntity0);
    }

    public DamageSource mobAttack(LivingEntity livingEntity0) {
        return this.source(DamageTypes.MOB_ATTACK, livingEntity0);
    }

    public DamageSource noAggroMobAttack(LivingEntity livingEntity0) {
        return this.source(DamageTypes.MOB_ATTACK_NO_AGGRO, livingEntity0);
    }

    public DamageSource playerAttack(Player player0) {
        return this.source(DamageTypes.PLAYER_ATTACK, player0);
    }

    public DamageSource arrow(AbstractArrow abstractArrow0, @Nullable Entity entity1) {
        return this.source(DamageTypes.ARROW, abstractArrow0, entity1);
    }

    public DamageSource trident(Entity entity0, @Nullable Entity entity1) {
        return this.source(DamageTypes.TRIDENT, entity0, entity1);
    }

    public DamageSource mobProjectile(Entity entity0, @Nullable LivingEntity livingEntity1) {
        return this.source(DamageTypes.MOB_PROJECTILE, entity0, livingEntity1);
    }

    public DamageSource fireworks(FireworkRocketEntity fireworkRocketEntity0, @Nullable Entity entity1) {
        return this.source(DamageTypes.FIREWORKS, fireworkRocketEntity0, entity1);
    }

    public DamageSource fireball(Fireball fireball0, @Nullable Entity entity1) {
        return entity1 == null ? this.source(DamageTypes.UNATTRIBUTED_FIREBALL, fireball0) : this.source(DamageTypes.FIREBALL, fireball0, entity1);
    }

    public DamageSource witherSkull(WitherSkull witherSkull0, Entity entity1) {
        return this.source(DamageTypes.WITHER_SKULL, witherSkull0, entity1);
    }

    public DamageSource thrown(Entity entity0, @Nullable Entity entity1) {
        return this.source(DamageTypes.THROWN, entity0, entity1);
    }

    public DamageSource indirectMagic(Entity entity0, @Nullable Entity entity1) {
        return this.source(DamageTypes.INDIRECT_MAGIC, entity0, entity1);
    }

    public DamageSource thorns(Entity entity0) {
        return this.source(DamageTypes.THORNS, entity0);
    }

    public DamageSource explosion(@Nullable Explosion explosion0) {
        return explosion0 != null ? this.explosion(explosion0.getDirectSourceEntity(), explosion0.getIndirectSourceEntity()) : this.explosion(null, null);
    }

    public DamageSource explosion(@Nullable Entity entity0, @Nullable Entity entity1) {
        return this.source(entity1 != null && entity0 != null ? DamageTypes.PLAYER_EXPLOSION : DamageTypes.EXPLOSION, entity0, entity1);
    }

    public DamageSource sonicBoom(Entity entity0) {
        return this.source(DamageTypes.SONIC_BOOM, entity0);
    }

    public DamageSource badRespawnPointExplosion(Vec3 vec0) {
        return new DamageSource(this.damageTypes.getHolderOrThrow(DamageTypes.BAD_RESPAWN_POINT), vec0);
    }

    public DamageSource outOfBorder() {
        return this.outsideBorder;
    }

    public DamageSource genericKill() {
        return this.genericKill;
    }
}