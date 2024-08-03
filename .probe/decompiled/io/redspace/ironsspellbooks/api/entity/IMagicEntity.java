package io.redspace.ironsspellbooks.api.entity;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;

public interface IMagicEntity {

    MagicData getMagicData();

    void setSyncedSpellData(SyncedSpellData var1);

    boolean isCasting();

    void initiateCastSpell(AbstractSpell var1, int var2);

    void cancelCast();

    void castComplete();

    void notifyDangerousProjectile(Projectile var1);

    boolean setTeleportLocationBehindTarget(int var1);

    void setBurningDashDirectionData();

    ItemStack getItemBySlot(EquipmentSlot var1);

    boolean isDrinkingPotion();

    boolean getHasUsedSingleAttack();

    void setHasUsedSingleAttack(boolean var1);

    void startDrinkingPotion();
}