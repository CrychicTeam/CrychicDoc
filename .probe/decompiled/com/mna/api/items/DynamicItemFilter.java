package com.mna.api.items;

import com.mna.api.ManaAndArtificeMod;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;

public class DynamicItemFilter {

    private NonNullList<ItemStack> _whiteList = NonNullList.create();

    private NonNullList<ItemStack> _blackList = NonNullList.create();

    private boolean _whitelistDamageMatching;

    private boolean _whitelistTagMatching;

    private boolean _blacklistDamageMatching;

    private boolean _blacklistTagMatching;

    public CompoundTag getTag() {
        CompoundTag tag = new CompoundTag();
        CompoundTag whitelist = new CompoundTag();
        CompoundTag blacklist = new CompoundTag();
        ContainerHelper.saveAllItems(whitelist, this._whiteList);
        ContainerHelper.saveAllItems(blacklist, this._blackList);
        tag.put("whitelist", whitelist);
        tag.put("blacklist", blacklist);
        tag.putBoolean("whitelist_damage", this._whitelistDamageMatching);
        tag.putBoolean("whitelist_tag", this._whitelistTagMatching);
        tag.putBoolean("blacklist_damage", this._blacklistDamageMatching);
        tag.putBoolean("blacklist_tag", this._blacklistTagMatching);
        return tag;
    }

    public void loadFromTag(CompoundTag tag) {
        this.clear();
        if (tag.contains("whitelist")) {
            ContainerHelper.loadAllItems(tag.getCompound("whitelist"), this._whiteList);
        }
        if (tag.contains("blacklist")) {
            ContainerHelper.loadAllItems(tag.getCompound("blacklist"), this._blackList);
        }
        if (tag.contains("whitelist_damage")) {
            this._whitelistDamageMatching = tag.getBoolean("whitelist_damage");
        }
        if (tag.contains("whitelist_tag")) {
            this._whitelistTagMatching = tag.getBoolean("whitelist_tag");
        }
        if (tag.contains("blacklist_damage")) {
            this._blacklistDamageMatching = tag.getBoolean("blacklist_damage");
        }
        if (tag.contains("blacklist_tag")) {
            this._blacklistTagMatching = tag.getBoolean("blacklist_tag");
        }
    }

    public void setWhitelist(NonNullList<ItemStack> items, boolean matchDamage, boolean matchTag) {
        this._whiteList = NonNullList.createWithCapacity(24);
        this._whiteList.addAll(items);
        this._whitelistDamageMatching = matchDamage;
        this._whitelistTagMatching = matchTag;
    }

    public void setBlacklist(NonNullList<ItemStack> items, boolean matchDamage, boolean matchTag) {
        this._blackList = NonNullList.createWithCapacity(24);
        this._blackList.addAll(items);
        this._blacklistDamageMatching = matchDamage;
        this._blacklistTagMatching = matchTag;
    }

    public NonNullList<ItemStack> getWhiteList() {
        return this._whiteList;
    }

    public NonNullList<ItemStack> getBlackList() {
        return this._blackList;
    }

    public boolean isWhitelistEmpty() {
        for (int i = 0; i < this._whiteList.size(); i++) {
            if (!this._whiteList.get(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean isBlacklistEmpty() {
        for (int i = 0; i < this._blackList.size(); i++) {
            if (!this._blackList.get(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        return this.isWhitelistEmpty() && this.isBlacklistEmpty();
    }

    private boolean matchStack(ItemStack check, NonNullList<ItemStack> filterList, boolean matchDamage, boolean matchTags) {
        return filterList.stream().anyMatch(is -> {
            if (is.getItem() != check.getItem()) {
                return false;
            } else {
                return matchDamage && check.getDamageValue() < is.getDamageValue() ? false : !matchTags || ManaAndArtificeMod.getItemHelper().AreTagsEqual(is, check);
            }
        });
    }

    public void copyFrom(DynamicItemFilter other) {
        if (other != null) {
            this.setWhitelist(other.getWhiteList(), other._whitelistDamageMatching, other._whitelistTagMatching);
            this.setBlacklist(other.getBlackList(), other._blacklistDamageMatching, other._blacklistTagMatching);
        }
    }

    public void clear() {
        this._whiteList = NonNullList.createWithCapacity(24);
        this._blackList = NonNullList.createWithCapacity(24);
        for (int i = 0; i < 24; i++) {
            this._whiteList.add(ItemStack.EMPTY);
            this._blackList.add(ItemStack.EMPTY);
        }
    }

    public boolean matches(ItemStack stack) {
        return (this.isWhitelistEmpty() || this.matchStack(stack, this._whiteList, this._whitelistDamageMatching, this._whitelistTagMatching)) && !this.matchStack(stack, this._blackList, this._blacklistDamageMatching, this._blacklistTagMatching);
    }

    public boolean getWhitelistMatchDurability() {
        return this._whitelistDamageMatching;
    }

    public boolean getWhitelistMatchTag() {
        return this._whitelistTagMatching;
    }

    public boolean getBlacklistMatchTag() {
        return this._blacklistTagMatching;
    }

    public boolean getBlacklistMatchDurability() {
        return this._blacklistDamageMatching;
    }

    public int hashStack(ItemStack stack) {
        int itemHash = stack.getItem().hashCode();
        if (this.isWhitelistEmpty()) {
            return itemHash;
        } else {
            if (this._whitelistDamageMatching) {
                itemHash += stack.getDamageValue();
            }
            if (this._whitelistTagMatching && stack.hasTag()) {
                itemHash += stack.getTag().hashCode();
            }
            return itemHash;
        }
    }
}