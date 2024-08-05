package net.minecraftforge.common.data;

import com.google.gson.JsonObject;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

public abstract class LanguageProvider implements DataProvider {

    private final Map<String, String> data = new TreeMap();

    private final PackOutput output;

    private final String modid;

    private final String locale;

    public LanguageProvider(PackOutput output, String modid, String locale) {
        this.output = output;
        this.modid = modid;
        this.locale = locale;
    }

    protected abstract void addTranslations();

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        this.addTranslations();
        return !this.data.isEmpty() ? this.save(cache, this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(this.modid).resolve("lang").resolve(this.locale + ".json")) : CompletableFuture.allOf();
    }

    @Override
    public String getName() {
        return "Languages: " + this.locale;
    }

    private CompletableFuture<?> save(CachedOutput cache, Path target) {
        JsonObject json = new JsonObject();
        this.data.forEach(json::addProperty);
        return DataProvider.saveStable(cache, json, target);
    }

    public void addBlock(Supplier<? extends Block> key, String name) {
        this.add((Block) key.get(), name);
    }

    public void add(Block key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void addItem(Supplier<? extends Item> key, String name) {
        this.add((Item) key.get(), name);
    }

    public void add(Item key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void addItemStack(Supplier<ItemStack> key, String name) {
        this.add((ItemStack) key.get(), name);
    }

    public void add(ItemStack key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void addEnchantment(Supplier<? extends Enchantment> key, String name) {
        this.add((Enchantment) key.get(), name);
    }

    public void add(Enchantment key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void addEffect(Supplier<? extends MobEffect> key, String name) {
        this.add((MobEffect) key.get(), name);
    }

    public void add(MobEffect key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void addEntityType(Supplier<? extends EntityType<?>> key, String name) {
        this.add((EntityType<?>) key.get(), name);
    }

    public void add(EntityType<?> key, String name) {
        this.add(key.getDescriptionId(), name);
    }

    public void add(String key, String value) {
        if (this.data.put(key, value) != null) {
            throw new IllegalStateException("Duplicate translation key " + key);
        }
    }
}