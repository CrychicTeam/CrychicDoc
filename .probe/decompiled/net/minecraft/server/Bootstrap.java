package net.minecraft.server;

import com.mojang.logging.LogUtils;
import java.io.PrintStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.SharedConstants;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.selector.options.EntitySelectorOptions;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FireBlock;
import org.slf4j.Logger;

public class Bootstrap {

    public static final PrintStream STDOUT = System.out;

    private static volatile boolean isBootstrapped;

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final AtomicLong bootstrapDuration = new AtomicLong(-1L);

    public static void bootStrap() {
        if (!isBootstrapped) {
            isBootstrapped = true;
            Instant $$0 = Instant.now();
            if (BuiltInRegistries.REGISTRY.keySet().isEmpty()) {
                throw new IllegalStateException("Unable to load registries");
            } else {
                FireBlock.bootStrap();
                ComposterBlock.bootStrap();
                if (EntityType.getKey(EntityType.PLAYER) == null) {
                    throw new IllegalStateException("Failed loading EntityTypes");
                } else {
                    PotionBrewing.bootStrap();
                    EntitySelectorOptions.bootStrap();
                    DispenseItemBehavior.bootStrap();
                    CauldronInteraction.bootStrap();
                    BuiltInRegistries.bootStrap();
                    CreativeModeTabs.validate();
                    wrapStreams();
                    bootstrapDuration.set(Duration.between($$0, Instant.now()).toMillis());
                }
            }
        }
    }

    private static <T> void checkTranslations(Iterable<T> iterableT0, Function<T, String> functionTString1, Set<String> setString2) {
        Language $$3 = Language.getInstance();
        iterableT0.forEach(p_135883_ -> {
            String $$4 = (String) functionTString1.apply(p_135883_);
            if (!$$3.has($$4)) {
                setString2.add($$4);
            }
        });
    }

    private static void checkGameruleTranslations(final Set<String> setString0) {
        final Language $$1 = Language.getInstance();
        GameRules.visitGameRuleTypes(new GameRules.GameRuleTypeVisitor() {

            @Override
            public <T extends GameRules.Value<T>> void visit(GameRules.Key<T> p_135897_, GameRules.Type<T> p_135898_) {
                if (!$$1.has(p_135897_.getDescriptionId())) {
                    setString0.add(p_135897_.getId());
                }
            }
        });
    }

    public static Set<String> getMissingTranslations() {
        Set<String> $$0 = new TreeSet();
        checkTranslations(BuiltInRegistries.ATTRIBUTE, Attribute::m_22087_, $$0);
        checkTranslations(BuiltInRegistries.ENTITY_TYPE, EntityType::m_20675_, $$0);
        checkTranslations(BuiltInRegistries.MOB_EFFECT, MobEffect::m_19481_, $$0);
        checkTranslations(BuiltInRegistries.ITEM, Item::m_5524_, $$0);
        checkTranslations(BuiltInRegistries.ENCHANTMENT, Enchantment::m_44704_, $$0);
        checkTranslations(BuiltInRegistries.BLOCK, Block::m_7705_, $$0);
        checkTranslations(BuiltInRegistries.CUSTOM_STAT, p_135885_ -> "stat." + p_135885_.toString().replace(':', '.'), $$0);
        checkGameruleTranslations($$0);
        return $$0;
    }

    public static void checkBootstrapCalled(Supplier<String> supplierString0) {
        if (!isBootstrapped) {
            throw createBootstrapException(supplierString0);
        }
    }

    private static RuntimeException createBootstrapException(Supplier<String> supplierString0) {
        try {
            String $$1 = (String) supplierString0.get();
            return new IllegalArgumentException("Not bootstrapped (called from " + $$1 + ")");
        } catch (Exception var3) {
            RuntimeException $$3 = new IllegalArgumentException("Not bootstrapped (failed to resolve location)");
            $$3.addSuppressed(var3);
            return $$3;
        }
    }

    public static void validate() {
        checkBootstrapCalled(() -> "validate");
        if (SharedConstants.IS_RUNNING_IN_IDE) {
            getMissingTranslations().forEach(p_179915_ -> LOGGER.error("Missing translations: {}", p_179915_));
            Commands.validate();
        }
        DefaultAttributes.validate();
    }

    private static void wrapStreams() {
        if (LOGGER.isDebugEnabled()) {
            System.setErr(new DebugLoggedPrintStream("STDERR", System.err));
            System.setOut(new DebugLoggedPrintStream("STDOUT", STDOUT));
        } else {
            System.setErr(new LoggedPrintStream("STDERR", System.err));
            System.setOut(new LoggedPrintStream("STDOUT", STDOUT));
        }
    }

    public static void realStdoutPrintln(String string0) {
        STDOUT.println(string0);
    }
}