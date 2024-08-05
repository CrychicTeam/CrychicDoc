package noppes.npcs.roles;

import com.google.common.collect.Multimap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.constants.EnumCompanionJobs;
import noppes.npcs.constants.EnumCompanionStage;
import noppes.npcs.constants.EnumCompanionTalent;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.mixin.ArmorMaterialsMixin;
import noppes.npcs.roles.companion.CompanionFarmer;
import noppes.npcs.roles.companion.CompanionFoodStats;
import noppes.npcs.roles.companion.CompanionGuard;
import noppes.npcs.roles.companion.CompanionJobInterface;
import noppes.npcs.roles.companion.CompanionTrader;

public class RoleCompanion extends RoleInterface {

    private static final CompanionJobInterface NONE = new CompanionJobInterface() {

        @Override
        public CompoundTag getNBT() {
            return null;
        }

        @Override
        public void setNBT(CompoundTag compound) {
        }

        @Override
        public EnumCompanionJobs getType() {
            return EnumCompanionJobs.NONE;
        }
    };

    public NpcMiscInventory inventory;

    public String uuid = "";

    public String ownerName = "";

    public Map<EnumCompanionTalent, Integer> talents = new TreeMap();

    public boolean canAge = true;

    public long ticksActive = 0L;

    public EnumCompanionStage stage = EnumCompanionStage.FULLGROWN;

    public Player owner = null;

    public int companionID;

    public CompanionJobInterface companionJobInterface = NONE;

    public boolean hasInv = true;

    public boolean defendOwner = true;

    public CompanionFoodStats foodstats = new CompanionFoodStats();

    private int eatingTicks = 20;

    private IItemStack eating = null;

    private int eatingDelay = 0;

    public int currentExp = 0;

    public RoleCompanion(EntityNPCInterface npc) {
        super(npc);
        this.inventory = new NpcMiscInventory(12);
    }

    @Override
    public boolean aiShouldExecute() {
        Player prev = this.owner;
        this.owner = this.getOwner();
        if (this.companionJobInterface.isSelfSufficient()) {
            return true;
        } else {
            if (this.owner == null && !this.uuid.isEmpty()) {
                this.npc.m_146870_();
            } else if (prev != this.owner && this.owner != null) {
                this.ownerName = this.owner.getDisplayName().getString();
                PlayerData data = PlayerData.get(this.owner);
                if (data.companionID != this.companionID) {
                    this.npc.m_146870_();
                }
            }
            return this.owner != null;
        }
    }

    @Override
    public void aiUpdateTask() {
        if (this.owner != null && !this.companionJobInterface.isSelfSufficient()) {
            this.foodstats.onUpdate(this.npc);
        }
        if (this.foodstats.getFoodLevel() >= 18) {
            this.npc.stats.healthRegen = 0;
            this.npc.stats.combatRegen = 0;
        }
        if (this.foodstats.needFood() && this.isSitting()) {
            if (this.eatingDelay > 0) {
                this.eatingDelay--;
                return;
            }
            IItemStack prev = this.eating;
            this.eating = this.getFood();
            if (prev != null && this.eating == null) {
                this.npc.setRoleData("");
            }
            if (prev == null && this.eating != null) {
                this.npc.setRoleData("eating");
                this.eatingTicks = 20;
            }
            if (this.isEating()) {
                this.doEating();
            }
        } else if (this.eating != null && !this.isSitting()) {
            this.eating = null;
            this.eatingDelay = 20;
            this.npc.setRoleData("");
        }
        this.ticksActive++;
        if (this.canAge && this.stage != EnumCompanionStage.FULLGROWN) {
            if (this.stage == EnumCompanionStage.BABY && this.ticksActive > (long) EnumCompanionStage.CHILD.matureAge) {
                this.matureTo(EnumCompanionStage.CHILD);
            } else if (this.stage == EnumCompanionStage.CHILD && this.ticksActive > (long) EnumCompanionStage.TEEN.matureAge) {
                this.matureTo(EnumCompanionStage.TEEN);
            } else if (this.stage == EnumCompanionStage.TEEN && this.ticksActive > (long) EnumCompanionStage.ADULT.matureAge) {
                this.matureTo(EnumCompanionStage.ADULT);
            } else if (this.stage == EnumCompanionStage.ADULT && this.ticksActive > (long) EnumCompanionStage.FULLGROWN.matureAge) {
                this.matureTo(EnumCompanionStage.FULLGROWN);
            }
        }
    }

    @Override
    public void clientUpdate() {
        if (this.npc.getRoleData().equals("eating")) {
            this.eating = this.getFood();
            if (this.isEating()) {
                this.doEating();
            }
        } else if (this.eating != null) {
            this.eating = null;
        }
    }

    private void doEating() {
        if (this.eating != null && !this.eating.isEmpty()) {
            ItemStack eating = this.eating.getMCItemStack();
            if (this.npc.m_9236_().isClientSide) {
                RandomSource rand = this.npc.m_217043_();
                for (int j = 0; j < 2; j++) {
                    Vec3 vec3 = new Vec3(((double) rand.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
                    vec3.xRot(-this.npc.m_146909_() * (float) Math.PI / 180.0F);
                    vec3.yRot(-this.npc.f_20883_ * (float) Math.PI / 180.0F);
                    Vec3 vec31 = new Vec3(((double) rand.nextFloat() - 0.5) * 0.3, (double) (-rand.nextFloat()) * 0.6 - 0.3, (double) (this.npc.m_20205_() / 2.0F) + 0.1);
                    vec31.xRot(-this.npc.m_146909_() * (float) Math.PI / 180.0F);
                    vec31.yRot(-this.npc.f_20883_ * (float) Math.PI / 180.0F);
                    vec31 = vec31.add(this.npc.m_20185_(), this.npc.m_20186_() + (double) this.npc.m_20206_() + 0.1, this.npc.m_20189_());
                    String s = "iconcrack_" + Item.getId(eating.getItem());
                    this.npc.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, eating), vec31.x, vec31.y, vec31.z, vec3.x, vec3.y + 0.05, vec3.z);
                }
            } else {
                this.eatingTicks--;
                if (this.eatingTicks <= 0) {
                    FoodProperties food = eating.getItem().getFoodProperties(eating, this.npc);
                    if (this.inventory.removeItem(eating, 1)) {
                        this.foodstats.onFoodEaten(food, eating);
                        this.npc.m_5496_(SoundEvents.PLAYER_BURP, 0.5F, this.npc.m_217043_().nextFloat() * 0.1F + 0.9F);
                    }
                    this.eatingDelay = 20;
                    this.npc.setRoleData("");
                    this.eating = null;
                } else if (this.eatingTicks > 3 && this.eatingTicks % 2 == 0) {
                    RandomSource rand = this.npc.m_217043_();
                    this.npc.m_5496_(SoundEvents.GENERIC_EAT, 0.5F + 0.5F * (float) rand.nextInt(2), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                }
            }
        }
    }

    public void matureTo(EnumCompanionStage stage) {
        this.stage = stage;
        EntityCustomNpc npc = (EntityCustomNpc) this.npc;
        npc.ais.animationType = stage.animation;
        if (stage == EnumCompanionStage.BABY) {
            npc.modelData.getPartConfig(EnumParts.ARM_LEFT).setScale(0.5F, 0.5F, 0.5F);
            npc.modelData.getPartConfig(EnumParts.LEG_LEFT).setScale(0.5F, 0.5F, 0.5F);
            npc.modelData.getPartConfig(EnumParts.BODY).setScale(0.5F, 0.5F, 0.5F);
            npc.modelData.getPartConfig(EnumParts.HEAD).setScale(0.7F, 0.7F, 0.7F);
            npc.ais.onAttack = 1;
            npc.ais.setWalkingSpeed(3);
            if (!this.talents.containsKey(EnumCompanionTalent.INVENTORY)) {
                this.talents.put(EnumCompanionTalent.INVENTORY, 0);
            }
        }
        if (stage == EnumCompanionStage.CHILD) {
            npc.modelData.getPartConfig(EnumParts.ARM_LEFT).setScale(0.6F, 0.6F, 0.6F);
            npc.modelData.getPartConfig(EnumParts.LEG_LEFT).setScale(0.6F, 0.6F, 0.6F);
            npc.modelData.getPartConfig(EnumParts.BODY).setScale(0.6F, 0.6F, 0.6F);
            npc.modelData.getPartConfig(EnumParts.HEAD).setScale(0.8F, 0.8F, 0.8F);
            npc.ais.onAttack = 0;
            npc.ais.setWalkingSpeed(4);
            if (!this.talents.containsKey(EnumCompanionTalent.SWORD)) {
                this.talents.put(EnumCompanionTalent.SWORD, 0);
            }
        }
        if (stage == EnumCompanionStage.TEEN) {
            npc.modelData.getPartConfig(EnumParts.ARM_LEFT).setScale(0.8F, 0.8F, 0.8F);
            npc.modelData.getPartConfig(EnumParts.LEG_LEFT).setScale(0.8F, 0.8F, 0.8F);
            npc.modelData.getPartConfig(EnumParts.BODY).setScale(0.8F, 0.8F, 0.8F);
            npc.modelData.getPartConfig(EnumParts.HEAD).setScale(0.9F, 0.9F, 0.9F);
            npc.ais.onAttack = 0;
            npc.ais.setWalkingSpeed(5);
            if (!this.talents.containsKey(EnumCompanionTalent.ARMOR)) {
                this.talents.put(EnumCompanionTalent.ARMOR, 0);
            }
        }
        if (stage == EnumCompanionStage.ADULT || stage == EnumCompanionStage.FULLGROWN) {
            npc.modelData.getPartConfig(EnumParts.ARM_LEFT).setScale(1.0F, 1.0F, 1.0F);
            npc.modelData.getPartConfig(EnumParts.LEG_LEFT).setScale(1.0F, 1.0F, 1.0F);
            npc.modelData.getPartConfig(EnumParts.BODY).setScale(1.0F, 1.0F, 1.0F);
            npc.modelData.getPartConfig(EnumParts.HEAD).setScale(1.0F, 1.0F, 1.0F);
            npc.ais.onAttack = 0;
            npc.ais.setWalkingSpeed(5);
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.put("CompanionInventory", this.inventory.getToNBT());
        compound.putString("CompanionOwner", this.uuid);
        compound.putString("CompanionOwnerName", this.ownerName);
        compound.putInt("CompanionID", this.companionID);
        compound.putInt("CompanionStage", this.stage.ordinal());
        compound.putInt("CompanionExp", this.currentExp);
        compound.putBoolean("CompanionCanAge", this.canAge);
        compound.putLong("CompanionAge", this.ticksActive);
        compound.putBoolean("CompanionHasInv", this.hasInv);
        compound.putBoolean("CompanionDefendOwner", this.defendOwner);
        this.foodstats.writeNBT(compound);
        compound.putInt("CompanionJob", this.companionJobInterface.getType().ordinal());
        if (this.companionJobInterface.getType() != EnumCompanionJobs.NONE) {
            compound.put("CompanionJobData", this.companionJobInterface.getNBT());
        }
        ListTag list = new ListTag();
        for (EnumCompanionTalent talent : this.talents.keySet()) {
            CompoundTag c = new CompoundTag();
            c.putInt("Talent", talent.ordinal());
            c.putInt("Exp", (Integer) this.talents.get(talent));
            list.add(c);
        }
        compound.put("CompanionTalents", list);
        return compound;
    }

    @Override
    public void load(CompoundTag compound) {
        this.inventory.setFromNBT(compound.getCompound("CompanionInventory"));
        this.uuid = compound.getString("CompanionOwner");
        this.ownerName = compound.getString("CompanionOwnerName");
        this.companionID = compound.getInt("CompanionID");
        this.stage = EnumCompanionStage.values()[compound.getInt("CompanionStage")];
        this.currentExp = compound.getInt("CompanionExp");
        this.canAge = compound.getBoolean("CompanionCanAge");
        this.ticksActive = compound.getLong("CompanionAge");
        this.hasInv = compound.getBoolean("CompanionHasInv");
        this.defendOwner = compound.getBoolean("CompanionDefendOwner");
        this.foodstats.readNBT(compound);
        ListTag list = compound.getList("CompanionTalents", 10);
        Map<EnumCompanionTalent, Integer> talents = new TreeMap();
        for (int i = 0; i < list.size(); i++) {
            CompoundTag c = list.getCompound(i);
            EnumCompanionTalent talent = EnumCompanionTalent.values()[c.getInt("Talent")];
            talents.put(talent, c.getInt("Exp"));
        }
        this.talents = talents;
        this.setJob(compound.getInt("CompanionJob"));
        this.companionJobInterface.setNBT(compound.getCompound("CompanionJobData"));
        this.setStats();
    }

    private void setJob(int i) {
        EnumCompanionJobs companionJob = EnumCompanionJobs.values()[i];
        if (companionJob == EnumCompanionJobs.SHOP) {
            this.companionJobInterface = new CompanionTrader();
        } else if (companionJob == EnumCompanionJobs.FARMER) {
            this.companionJobInterface = new CompanionFarmer();
        } else if (companionJob == EnumCompanionJobs.GUARD) {
            this.companionJobInterface = new CompanionGuard();
        } else {
            this.companionJobInterface = NONE;
        }
        this.companionJobInterface.npc = this.npc;
    }

    @Override
    public void interact(Player player) {
        this.interact(player, false);
    }

    public void interact(Player player, boolean openGui) {
        if (player != null && this.companionJobInterface.getType() == EnumCompanionJobs.SHOP) {
            ((CompanionTrader) this.companionJobInterface).interact(player);
        }
        if (player == this.owner && this.npc.isAlive() && !this.npc.isAttacking()) {
            if (!player.m_6047_() && !openGui) {
                this.setSitting(!this.isSitting());
            } else {
                this.openGui(player);
            }
        }
    }

    public int getTotalLevel() {
        int level = 0;
        for (EnumCompanionTalent talent : this.talents.keySet()) {
            level += this.getTalentLevel(talent);
        }
        return level;
    }

    public int getMaxExp() {
        return 500 + this.getTotalLevel() * 200;
    }

    public void addExp(int exp) {
        if (this.canAddExp(exp)) {
            this.currentExp += exp;
        }
    }

    public boolean canAddExp(int exp) {
        int newExp = this.currentExp + exp;
        return newExp >= 0 && newExp < this.getMaxExp();
    }

    public void gainExp(int chance) {
        if (this.npc.m_217043_().nextInt(chance) == 0) {
            this.addExp(1);
        }
    }

    private void openGui(Player player) {
        NoppesUtilServer.sendOpenGui(player, EnumGuiType.Companion, this.npc);
    }

    public Player getOwner() {
        if (this.uuid != null && !this.uuid.isEmpty()) {
            try {
                UUID id = UUID.fromString(this.uuid);
                if (id != null) {
                    return NoppesUtilServer.getPlayer(this.npc.m_20194_(), id);
                }
            } catch (IllegalArgumentException var2) {
            }
            return null;
        } else {
            return null;
        }
    }

    public void setOwner(Player player) {
        this.uuid = player.m_20148_().toString();
    }

    public boolean hasTalent(EnumCompanionTalent talent) {
        return this.getTalentLevel(talent) > 0;
    }

    public int getTalentLevel(EnumCompanionTalent talent) {
        if (!this.talents.containsKey(talent)) {
            return 0;
        } else {
            int exp = (Integer) this.talents.get(talent);
            if (exp >= 5000) {
                return 5;
            } else if (exp >= 3000) {
                return 4;
            } else if (exp >= 1700) {
                return 3;
            } else if (exp >= 1000) {
                return 2;
            } else {
                return exp >= 400 ? 1 : 0;
            }
        }
    }

    public Integer getNextLevel(EnumCompanionTalent talent) {
        if (!this.talents.containsKey(talent)) {
            return 0;
        } else {
            int exp = (Integer) this.talents.get(talent);
            if (exp < 400) {
                return 400;
            } else if (exp < 1000) {
                return 700;
            } else if (exp < 1700) {
                return 1700;
            } else {
                return exp < 3000 ? 3000 : 5000;
            }
        }
    }

    public void levelSword() {
        if (this.talents.containsKey(EnumCompanionTalent.SWORD)) {
            ;
        }
    }

    public void levelTalent(EnumCompanionTalent talent, int exp) {
        if (this.talents.containsKey(EnumCompanionTalent.SWORD)) {
            this.talents.put(talent, exp + (Integer) this.talents.get(talent));
        }
    }

    public int getExp(EnumCompanionTalent talent) {
        return this.talents.containsKey(talent) ? (Integer) this.talents.get(talent) : -1;
    }

    public void setExp(EnumCompanionTalent talent, int exp) {
        this.talents.put(talent, exp);
    }

    private boolean isWeapon(ItemStack item) {
        return item != null && item.getItem() != null ? item.getItem() instanceof SwordItem || item.getItem() instanceof BowItem || item.getItem() == Item.byBlock(Blocks.COBBLESTONE) : false;
    }

    public boolean canWearWeapon(IItemStack stack) {
        if (stack != null && stack.getMCItemStack().getItem() != null) {
            Item item = stack.getMCItemStack().getItem();
            if (item instanceof SwordItem) {
                return this.canWearSword(stack);
            } else if (item instanceof BowItem) {
                return this.getTalentLevel(EnumCompanionTalent.RANGED) > 2;
            } else {
                return item == Item.byBlock(Blocks.COBBLESTONE) ? this.getTalentLevel(EnumCompanionTalent.RANGED) > 1 : false;
            }
        } else {
            return false;
        }
    }

    public boolean canWearArmor(ItemStack item) {
        int level = this.getTalentLevel(EnumCompanionTalent.ARMOR);
        if (item == null || !(item.getItem() instanceof ArmorItem) || level <= 0) {
            return false;
        } else if (level >= 5) {
            return true;
        } else {
            ArmorItem armor = (ArmorItem) item.getItem();
            int reduction = 1;
            if (armor.getMaterial() instanceof ArmorMaterials) {
                reduction = ((ArmorMaterialsMixin) armor.getMaterial()).durabilityMultiplier();
            }
            if (reduction <= 5 && level >= 1) {
                return true;
            } else if (reduction <= 7 && level >= 2) {
                return true;
            } else {
                return reduction <= 15 && level >= 3 ? true : reduction <= 33 && level >= 4;
            }
        }
    }

    public boolean canWearSword(IItemStack item) {
        int level = this.getTalentLevel(EnumCompanionTalent.SWORD);
        if (item == null || !(item.getMCItemStack().getItem() instanceof SwordItem) || level <= 0) {
            return false;
        } else {
            return level >= 5 ? true : this.getSwordDamage(item) - (double) level < 4.0;
        }
    }

    private double getSwordDamage(IItemStack item) {
        if (item != null && item.getMCItemStack().getItem() instanceof SwordItem) {
            Multimap<Attribute, AttributeModifier> map = item.getMCItemStack().getAttributeModifiers(EquipmentSlot.MAINHAND);
            for (Entry<Attribute, AttributeModifier> entry : map.entries()) {
                if (entry.getKey() == Attributes.ATTACK_DAMAGE) {
                    AttributeModifier mod = (AttributeModifier) entry.getValue();
                    return mod.getAmount();
                }
            }
            return 0.0;
        } else {
            return 0.0;
        }
    }

    public void setStats() {
        IItemStack weapon = this.npc.inventory.getRightHand();
        this.npc.stats.melee.setStrength((int) (1.0 + this.getSwordDamage(weapon)));
        this.npc.stats.healthRegen = 0;
        this.npc.stats.combatRegen = 0;
        int ranged = this.getTalentLevel(EnumCompanionTalent.RANGED);
        if (ranged > 0 && weapon != null) {
            Item item = weapon.getMCItemStack().getItem();
            if (ranged > 0 && item == Item.byBlock(Blocks.COBBLESTONE)) {
                this.npc.inventory.setProjectile(weapon);
            }
            if (ranged > 0 && item instanceof BowItem) {
                this.npc.inventory.setProjectile(NpcAPI.Instance().getIItemStack(new ItemStack(Items.ARROW)));
            }
        }
        this.inventory.setSize(2 + this.getTalentLevel(EnumCompanionTalent.INVENTORY) * 2);
    }

    public void setSelfsuficient(boolean bo) {
        if (this.owner != null && bo != this.companionJobInterface.isSelfSufficient()) {
            PlayerData data = PlayerData.get(this.owner);
            if (bo || !data.hasCompanion()) {
                data.setCompanion(bo ? null : this.npc);
                if (this.companionJobInterface.getType() == EnumCompanionJobs.GUARD) {
                    ((CompanionGuard) this.companionJobInterface).isStanding = bo;
                } else if (this.companionJobInterface.getType() == EnumCompanionJobs.FARMER) {
                    ((CompanionFarmer) this.companionJobInterface).isStanding = bo;
                }
            }
        }
    }

    public void setSitting(boolean sit) {
        if (sit) {
            this.npc.ais.animationType = 1;
            this.npc.ais.onAttack = 3;
            this.npc.ais.setStartPos(this.npc.m_20183_());
            this.npc.m_21573_().stop();
            this.npc.m_6021_((double) this.npc.getStartXPos(), this.npc.m_20186_(), (double) this.npc.getStartZPos());
        } else {
            this.npc.ais.animationType = this.stage.animation;
            this.npc.ais.onAttack = 0;
        }
        this.npc.updateAI = true;
    }

    public boolean isSitting() {
        return this.npc.ais.animationType == 1;
    }

    public float getDamageAfterArmorAbsorb(DamageSource source, float damage) {
        if (this.hasInv && this.getTalentLevel(EnumCompanionTalent.ARMOR) > 0) {
            if (!source.is(DamageTypeTags.BYPASSES_SHIELD)) {
                this.damageArmor(damage);
                int i = 25 - this.getTotalArmorValue();
                float f1 = damage * (float) i;
                damage = f1 / 25.0F;
            }
            return damage;
        } else {
            return damage;
        }
    }

    private void damageArmor(float damage) {
        damage /= 4.0F;
        if (damage < 1.0F) {
            damage = 1.0F;
        }
        boolean hasArmor = false;
        Iterator<Entry<Integer, IItemStack>> ita = this.npc.inventory.armor.entrySet().iterator();
        while (ita.hasNext()) {
            Entry<Integer, IItemStack> entry = (Entry<Integer, IItemStack>) ita.next();
            IItemStack item = (IItemStack) entry.getValue();
            if (item != null && item.getMCItemStack().getItem() instanceof ArmorItem) {
                hasArmor = true;
                item.getMCItemStack().hurtAndBreak((int) damage, this.npc, entity -> entity.m_21166_(EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, (Integer) entry.getKey())));
                if (item.getStackSize() <= 0) {
                    ita.remove();
                }
            }
        }
        this.gainExp(hasArmor ? 4 : 8);
    }

    public int getTotalArmorValue() {
        int armorValue = 0;
        for (IItemStack armor : this.npc.inventory.armor.values()) {
            if (armor != null && armor.getMCItemStack().getItem() instanceof ArmorItem) {
                armorValue += ((ArmorItem) armor.getMCItemStack().getItem()).getDefense();
            }
        }
        return armorValue;
    }

    @Override
    public boolean isFollowing() {
        return this.companionJobInterface.isSelfSufficient() ? false : this.owner != null && !this.isSitting();
    }

    @Override
    public boolean defendOwner() {
        return this.defendOwner && this.owner != null && this.stage != EnumCompanionStage.BABY && !this.companionJobInterface.isSelfSufficient();
    }

    public boolean hasOwner() {
        return !this.uuid.isEmpty();
    }

    public void addMovementStat(double x, double y, double z) {
        long i = Math.round(Math.sqrt(x * x + y * y + z * z) * 100.0);
        if (this.npc.isAttacking()) {
            this.foodstats.addExhaustion(0.04F * (float) i * 0.01F);
        } else {
            this.foodstats.addExhaustion(0.02F * (float) i * 0.01F);
        }
    }

    private IItemStack getFood() {
        for (ItemStack item : this.inventory.items) {
            if (!item.isEmpty() && item.getItem().getFoodProperties() != null) {
                return NpcAPI.Instance().getIItemStack(item);
            }
        }
        return null;
    }

    public IItemStack getItemInHand() {
        return this.eating != null && !this.eating.isEmpty() ? this.eating : this.npc.inventory.getRightHand();
    }

    public boolean isEating() {
        return this.eating != null && !this.eating.isEmpty();
    }

    public boolean hasInv() {
        return !this.hasInv ? false : this.hasTalent(EnumCompanionTalent.INVENTORY) || this.hasTalent(EnumCompanionTalent.ARMOR) || this.hasTalent(EnumCompanionTalent.SWORD);
    }

    public void attackedEntity(Entity entity) {
        IItemStack weapon = this.npc.inventory.getRightHand();
        this.gainExp(weapon == null ? 8 : 4);
        if (weapon != null) {
            weapon.getMCItemStack().hurtAndBreak(1, this.npc, e -> e.m_21166_(EquipmentSlot.MAINHAND));
            if (weapon.getMCItemStack().getCount() <= 0) {
                this.npc.inventory.setRightHand(null);
            }
        }
    }

    public void addTalentExp(EnumCompanionTalent talent, int exp) {
        if (this.talents.containsKey(talent)) {
            exp += this.talents.get(talent);
        }
        this.talents.put(talent, exp);
    }

    @Override
    public int getType() {
        return 6;
    }
}