package net.minecraft.client.gui.screens;

import com.ibm.icu.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

public class CreateBuffetWorldScreen extends Screen {

    private static final Component BIOME_SELECT_INFO = Component.translatable("createWorld.customize.buffet.biome");

    private final Screen parent;

    private final Consumer<Holder<Biome>> applySettings;

    final Registry<Biome> biomes;

    private CreateBuffetWorldScreen.BiomeList list;

    Holder<Biome> biome;

    private Button doneButton;

    public CreateBuffetWorldScreen(Screen screen0, WorldCreationContext worldCreationContext1, Consumer<Holder<Biome>> consumerHolderBiome2) {
        super(Component.translatable("createWorld.customize.buffet.title"));
        this.parent = screen0;
        this.applySettings = consumerHolderBiome2;
        this.biomes = worldCreationContext1.worldgenLoadContext().m_175515_(Registries.BIOME);
        Holder<Biome> $$3 = (Holder<Biome>) this.biomes.getHolder(Biomes.PLAINS).or(() -> this.biomes.holders().findAny()).orElseThrow();
        this.biome = (Holder<Biome>) worldCreationContext1.selectedDimensions().overworld().getBiomeSource().possibleBiomes().stream().findFirst().orElse($$3);
    }

    @Override
    public void onClose() {
        this.f_96541_.setScreen(this.parent);
    }

    @Override
    protected void init() {
        this.list = new CreateBuffetWorldScreen.BiomeList();
        this.m_7787_(this.list);
        this.doneButton = (Button) this.m_142416_(Button.builder(CommonComponents.GUI_DONE, p_280788_ -> {
            this.applySettings.accept(this.biome);
            this.f_96541_.setScreen(this.parent);
        }).bounds(this.f_96543_ / 2 - 155, this.f_96544_ - 28, 150, 20).build());
        this.m_142416_(Button.builder(CommonComponents.GUI_CANCEL, p_280789_ -> this.f_96541_.setScreen(this.parent)).bounds(this.f_96543_ / 2 + 5, this.f_96544_ - 28, 150, 20).build());
        this.list.setSelected((CreateBuffetWorldScreen.BiomeList.Entry) this.list.m_6702_().stream().filter(p_232738_ -> Objects.equals(p_232738_.biome, this.biome)).findFirst().orElse(null));
    }

    void updateButtonValidity() {
        this.doneButton.f_93623_ = this.list.m_93511_() != null;
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280039_(guiGraphics0);
        this.list.m_88315_(guiGraphics0, int1, int2, float3);
        guiGraphics0.drawCenteredString(this.f_96547_, this.f_96539_, this.f_96543_ / 2, 8, 16777215);
        guiGraphics0.drawCenteredString(this.f_96547_, BIOME_SELECT_INFO, this.f_96543_ / 2, 28, 10526880);
        super.render(guiGraphics0, int1, int2, float3);
    }

    class BiomeList extends ObjectSelectionList<CreateBuffetWorldScreen.BiomeList.Entry> {

        BiomeList() {
            super(CreateBuffetWorldScreen.this.f_96541_, CreateBuffetWorldScreen.this.f_96543_, CreateBuffetWorldScreen.this.f_96544_, 40, CreateBuffetWorldScreen.this.f_96544_ - 37, 16);
            Collator $$0 = Collator.getInstance(Locale.getDefault());
            CreateBuffetWorldScreen.this.biomes.holders().map(p_205389_ -> new CreateBuffetWorldScreen.BiomeList.Entry(p_205389_)).sorted(Comparator.comparing(p_203142_ -> p_203142_.name.getString(), $$0)).forEach(p_203138_ -> this.m_7085_(p_203138_));
        }

        public void setSelected(@Nullable CreateBuffetWorldScreen.BiomeList.Entry createBuffetWorldScreenBiomeListEntry0) {
            super.m_6987_(createBuffetWorldScreenBiomeListEntry0);
            if (createBuffetWorldScreenBiomeListEntry0 != null) {
                CreateBuffetWorldScreen.this.biome = createBuffetWorldScreenBiomeListEntry0.biome;
            }
            CreateBuffetWorldScreen.this.updateButtonValidity();
        }

        class Entry extends ObjectSelectionList.Entry<CreateBuffetWorldScreen.BiomeList.Entry> {

            final Holder.Reference<Biome> biome;

            final Component name;

            public Entry(Holder.Reference<Biome> holderReferenceBiome0) {
                this.biome = holderReferenceBiome0;
                ResourceLocation $$1 = holderReferenceBiome0.key().location();
                String $$2 = $$1.toLanguageKey("biome");
                if (Language.getInstance().has($$2)) {
                    this.name = Component.translatable($$2);
                } else {
                    this.name = Component.literal($$1.toString());
                }
            }

            @Override
            public Component getNarration() {
                return Component.translatable("narrator.select", this.name);
            }

            @Override
            public void render(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, boolean boolean8, float float9) {
                guiGraphics0.drawString(CreateBuffetWorldScreen.this.f_96547_, this.name, int3 + 5, int2 + 2, 16777215);
            }

            @Override
            public boolean mouseClicked(double double0, double double1, int int2) {
                if (int2 == 0) {
                    BiomeList.this.setSelected(this);
                    return true;
                } else {
                    return false;
                }
            }
        }
    }
}