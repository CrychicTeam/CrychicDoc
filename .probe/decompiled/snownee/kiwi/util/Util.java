package snownee.kiwi.util;

import com.google.gson.JsonElement;
import java.io.Reader;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;
import snownee.kiwi.block.def.BlockDefinition;
import snownee.kiwi.loader.Platform;
import snownee.kiwi.shadowed.org.yaml.snakeyaml.DumperOptions;
import snownee.kiwi.shadowed.org.yaml.snakeyaml.LoaderOptions;
import snownee.kiwi.shadowed.org.yaml.snakeyaml.Yaml;
import snownee.kiwi.shadowed.org.yaml.snakeyaml.constructor.Constructor;
import snownee.kiwi.shadowed.org.yaml.snakeyaml.error.YAMLException;
import snownee.kiwi.shadowed.org.yaml.snakeyaml.nodes.MappingNode;
import snownee.kiwi.shadowed.org.yaml.snakeyaml.nodes.Node;
import snownee.kiwi.shadowed.org.yaml.snakeyaml.nodes.NodeId;
import snownee.kiwi.shadowed.org.yaml.snakeyaml.nodes.SequenceNode;
import snownee.kiwi.shadowed.org.yaml.snakeyaml.representer.Representer;

public final class Util {

    public static final MessageFormat MESSAGE_FORMAT = new MessageFormat("{0,number,#.#}");

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,###");

    private static final Yaml YAML;

    private static RecipeManager recipeManager;

    public static final List<Direction> DIRECTIONS = Direction.stream().toList();

    public static final List<Direction> HORIZONTAL_DIRECTIONS = Direction.Plane.HORIZONTAL.stream().toList();

    private Util() {
    }

    public static String color(int color) {
        return String.format("§x%06x", color & 16777215);
    }

    public static String formatComma(long number) {
        return DECIMAL_FORMAT.format(number);
    }

    public static String formatCompact(long number) {
        int unit = 1000;
        if (number < (long) unit) {
            return Long.toString(number);
        } else {
            int exp = (int) (Math.log((double) number) / Math.log((double) unit));
            if (exp - 1 >= 0 && exp - 1 < 6) {
                char pre = "kMGTPE".charAt(exp - 1);
                return MESSAGE_FORMAT.format(new Double[] { (double) number / Math.pow((double) unit, (double) exp) }) + pre;
            } else {
                return Long.toString(number);
            }
        }
    }

    public static String trimRL(ResourceLocation rl) {
        return trimRL(rl, "minecraft");
    }

    public static String trimRL(String rl) {
        return trimRL(rl, "minecraft");
    }

    public static String trimRL(ResourceLocation rl, String defaultNamespace) {
        return rl.getNamespace().equals(defaultNamespace) ? rl.getPath() : rl.toString();
    }

    public static String trimRL(String rl, String defaultNamespace) {
        return rl.startsWith(defaultNamespace + ":") ? rl.substring(defaultNamespace.length() + 1) : rl;
    }

    @Nullable
    public static ResourceLocation RL(@Nullable String string) {
        try {
            return ResourceLocation.tryParse(string);
        } catch (Exception var2) {
            return null;
        }
    }

    @Nullable
    public static ResourceLocation RL(@Nullable String string, String defaultNamespace) {
        if (string != null && !string.contains(":")) {
            string = defaultNamespace + ":" + string;
        }
        return RL(string);
    }

    @Nullable
    public static Component getBlockDefName(ItemStack stack, String key) {
        NBTHelper data = NBTHelper.of(stack);
        CompoundTag tag = data.getTag("BlockEntityTag.Overrides." + key);
        if (tag != null) {
            BlockDefinition def = BlockDefinition.fromNBT(tag);
            if (def != null) {
                return def.getDescription();
            }
        }
        return null;
    }

    @Nullable
    public static RecipeManager getRecipeManager() {
        if (recipeManager == null && Platform.isPhysicalClient()) {
            ClientPacketListener connection = Minecraft.getInstance().getConnection();
            if (connection != null) {
                return connection.getRecipeManager();
            }
        }
        return recipeManager;
    }

    public static void setRecipeManager(RecipeManager recipeManager) {
        Util.recipeManager = recipeManager;
    }

    public static <C extends Container, T extends Recipe<C>> List<T> getRecipes(RecipeType<T> recipeTypeIn) {
        RecipeManager manager = getRecipeManager();
        return manager == null ? Collections.EMPTY_LIST : getRecipeManager().getAllRecipesFor(recipeTypeIn);
    }

    public static int friendlyCompare(String a, String b) {
        int aLength = a.length();
        int bLength = b.length();
        int minSize = Math.min(aLength, bLength);
        boolean asNumeric = false;
        int lastNumericCompare = 0;
        for (int i = 0; i < minSize; i++) {
            char aChar = a.charAt(i);
            char bChar = b.charAt(i);
            boolean aNumber = aChar >= '0' && aChar <= '9';
            boolean bNumber = bChar >= '0' && bChar <= '9';
            if (asNumeric) {
                if (aNumber && bNumber) {
                    if (lastNumericCompare == 0) {
                        lastNumericCompare = aChar - bChar;
                    }
                } else {
                    if (aNumber) {
                        return 1;
                    }
                    if (bNumber) {
                        return -1;
                    }
                    if (lastNumericCompare != 0) {
                        return lastNumericCompare;
                    }
                    if (aChar != bChar) {
                        return aChar - bChar;
                    }
                    asNumeric = false;
                }
            } else if (aNumber && bNumber) {
                asNumeric = true;
                if (lastNumericCompare == 0) {
                    lastNumericCompare = aChar - bChar;
                }
            } else if (aChar != bChar) {
                return aChar - bChar;
            }
        }
        if (!asNumeric) {
            return aLength - bLength;
        } else if (aLength > bLength && a.charAt(bLength) >= '0' && a.charAt(bLength) <= '9') {
            return 1;
        } else if (bLength > aLength && b.charAt(aLength) >= '0' && b.charAt(aLength) <= '9') {
            return -1;
        } else {
            return lastNumericCompare == 0 ? aLength - bLength : lastNumericCompare;
        }
    }

    public static String friendlyText(String s) {
        StringBuilder sb = new StringBuilder();
        MutableBoolean lastIsUpper = new MutableBoolean(true);
        s.codePoints().forEach(ch -> {
            if (Character.isUpperCase(ch) && lastIsUpper.isFalse()) {
                sb.append(' ');
            } else if (Character.isLowerCase(ch)) {
                if (sb.isEmpty()) {
                    ch = Character.toUpperCase(ch);
                } else if (lastIsUpper.isTrue() && sb.length() > 1 && Character.isUpperCase(sb.codePointAt(sb.length() - 2))) {
                    sb.insert(sb.length() - 1, ' ');
                }
            }
            lastIsUpper.setValue(Character.isUpperCase(ch));
            sb.appendCodePoint(ch);
        });
        return sb.toString();
    }

    public static boolean canPlayerBreak(Player player, BlockState state, BlockPos pos) {
        return !player.mayBuild() || !player.m_9236_().mayInteract(player, pos) ? false : player.isCreative() || !(state.m_60625_(player, player.m_9236_(), pos) <= 0.0F);
    }

    public static int applyAlpha(int color, float alpha) {
        int prevAlphaChannel = color >> 24 & 0xFF;
        if (prevAlphaChannel > 0) {
            alpha *= (float) prevAlphaChannel / 256.0F;
        }
        int alphaChannel = (int) (255.0F * Mth.clamp(alpha, 0.0F, 1.0F));
        return alphaChannel < 5 ? 0 : color & 16777215 | alphaChannel << 24;
    }

    public static float getPickRange(Player player) {
        float attrib = 5.0F;
        return player.isCreative() ? attrib : attrib - 0.5F;
    }

    public static void displayClientMessage(@Nullable Player player, boolean client, String key, Object... args) {
        if (player != null) {
            if (client == player.m_9236_().isClientSide) {
                player.m_213846_(Component.translatable(key, args));
            }
        }
    }

    public static void jsonList(JsonElement json, Consumer<JsonElement> collector) {
        if (json.isJsonArray()) {
            for (JsonElement e : json.getAsJsonArray()) {
                collector.accept(e);
            }
        } else {
            collector.accept(json);
        }
    }

    @Nullable
    public static String[] readNBTStrings(CompoundTag tag, String key, @Nullable String[] strings) {
        if (!tag.contains(key, 9)) {
            return null;
        } else {
            ListTag list = tag.getList(key, 8);
            if (list.isEmpty()) {
                return null;
            } else {
                if (strings == null || strings.length != list.size()) {
                    strings = new String[list.size()];
                }
                for (int i = 0; i < strings.length; i++) {
                    String s = list.getString(i);
                    strings[i] = s;
                }
                return strings;
            }
        }
    }

    public static void writeNBTStrings(CompoundTag tag, String key, @Nullable String[] strings) {
        if (strings != null && strings.length != 0) {
            ListTag list = new ListTag();
            for (String s : strings) {
                list.add(StringTag.valueOf(s));
            }
            tag.put(key, list);
        }
    }

    public static InteractionResult onAttackEntity(Player player, Level world, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (entity instanceof ItemFrame frame && !frame.getItem().isEmpty() && !frame.m_20068_() && !frame.m_20147_()) {
            ItemStack stack = player.m_21120_(hand);
            if (stack.is(Items.END_PORTAL_FRAME)) {
                frame.m_6842_(!frame.m_20145_());
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    public static <T> T loadYaml(String yaml, Class<? super T> type) {
        return YAML.loadAs(yaml, type);
    }

    public static <T> T loadYaml(Reader reader, Class<? super T> type) {
        return YAML.loadAs(reader, type);
    }

    public static void dumpYaml(Object object, Writer writer) {
        YAML.dump(object, writer);
    }

    static {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        YAML = new Yaml(new Util.ResafeConstructor(new LoaderOptions()), new Representer(dumperOptions), dumperOptions);
    }

    public static class ResafeConstructor extends Constructor {

        public ResafeConstructor(LoaderOptions loaderOptions) {
            super(loaderOptions);
            this.yamlClassConstructors.put(NodeId.scalar, undefinedConstructor);
            this.yamlClassConstructors.put(NodeId.mapping, new Util.ResafeConstructor.ConstructSafeMapping());
            this.yamlClassConstructors.put(NodeId.sequence, new Util.ResafeConstructor.ConstructSafeSequence());
        }

        private class ConstructSafeMapping extends Constructor.ConstructMapping {

            @Override
            public Object construct(Node node) {
                MappingNode mnode = (MappingNode) node;
                return node.isTwoStepsConstruction() ? ResafeConstructor.this.newMap(mnode) : ResafeConstructor.this.constructMapping(mnode);
            }

            @Override
            public void construct2ndStep(Node node, Object object) {
                ResafeConstructor.this.constructMapping2ndStep((MappingNode) node, (Map<Object, Object>) object);
            }
        }

        private class ConstructSafeSequence extends Constructor.ConstructSequence {

            @Override
            public Object construct(Node node) {
                SequenceNode snode = (SequenceNode) node;
                if (Set.class.isAssignableFrom(node.getType())) {
                    if (node.isTwoStepsConstruction()) {
                        throw new YAMLException("Set cannot be recursive.");
                    } else {
                        return ResafeConstructor.this.constructSet(snode);
                    }
                } else if (Collection.class.isAssignableFrom(node.getType())) {
                    return node.isTwoStepsConstruction() ? ResafeConstructor.this.newList(snode) : ResafeConstructor.this.constructSequence(snode);
                } else {
                    return node.isTwoStepsConstruction() ? ResafeConstructor.this.createArray(node.getType(), snode.getValue().size()) : ResafeConstructor.this.constructArray(snode);
                }
            }
        }
    }
}