package icyllis.modernui.mc.forge;

import icyllis.modernui.ModernUI;
import icyllis.modernui.animation.LayoutTransition;
import icyllis.modernui.animation.ObjectAnimator;
import icyllis.modernui.animation.TimeInterpolator;
import icyllis.modernui.animation.ValueAnimator;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Context;
import icyllis.modernui.core.Core;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.graphics.Color;
import icyllis.modernui.graphics.MathUtil;
import icyllis.modernui.graphics.text.FontFamily;
import icyllis.modernui.mc.ModernUIClient;
import icyllis.modernui.mc.MuiModApi;
import icyllis.modernui.mc.UIManager;
import icyllis.modernui.mc.ui.FourColorPicker;
import icyllis.modernui.mc.ui.ThemeControl;
import icyllis.modernui.text.Editable;
import icyllis.modernui.text.InputFilter;
import icyllis.modernui.text.SpannableString;
import icyllis.modernui.text.Typeface;
import icyllis.modernui.text.method.DigitsInputFilter;
import icyllis.modernui.text.style.ForegroundColorSpan;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.LayoutInflater;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.AdapterView;
import icyllis.modernui.widget.ArrayAdapter;
import icyllis.modernui.widget.Button;
import icyllis.modernui.widget.EditText;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.LinearPagerIndicator;
import icyllis.modernui.widget.PagerAdapter;
import icyllis.modernui.widget.ScrollView;
import icyllis.modernui.widget.SeekBar;
import icyllis.modernui.widget.Spinner;
import icyllis.modernui.widget.SpinnerAdapter;
import icyllis.modernui.widget.SwitchButton;
import icyllis.modernui.widget.TextView;
import icyllis.modernui.widget.Toast;
import icyllis.modernui.widget.ViewPager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.common.ForgeConfigSpec;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

public class PreferencesFragment extends Fragment {

    LinearLayout mTooltipCategory;

    LinearLayout mTextLayoutCategory;

    LinearLayout mTextRenderingCategory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
        ViewPager pager = new ViewPager(this.getContext());
        pager.setAdapter(new PreferencesFragment.ThePagerAdapter());
        pager.setFocusableInTouchMode(true);
        pager.setKeyboardNavigationCluster(true);
        pager.setEdgeEffectColor(-3300456);
        LinearPagerIndicator indicator = new LinearPagerIndicator(this.getContext());
        indicator.setPager(pager);
        indicator.setLineWidth((float) pager.dp(4.0F));
        indicator.setLineColor(-3300456);
        ViewPager.LayoutParams lp = new ViewPager.LayoutParams();
        lp.height = pager.dp(30.0F);
        lp.isDecor = true;
        lp.gravity = 81;
        pager.addView(indicator, lp);
        FrameLayout.LayoutParams lpx = new FrameLayout.LayoutParams(pager.dp(720.0F), -1);
        lpx.gravity = 17;
        pager.setLayoutParams(lpx);
        return pager;
    }

    public static LinearLayout createSecondPage(Context context) {
        LinearLayout content = new LinearLayout(context);
        content.setOrientation(1);
        LayoutTransition transition = new LayoutTransition();
        transition.enableTransitionType(4);
        content.setLayoutTransition(transition);
        LinearLayout category = createCategoryList(context, "modernui.center.category.font");
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(1);
        int dp6 = layout.dp(6.0F);
        LinearLayout firstLine = new LinearLayout(context);
        firstLine.setOrientation(0);
        TextView title = new TextView(context);
        title.setText(I18n.get("modernui.center.font.firstFont"));
        title.setTextAlignment(5);
        title.setTextSize(14.0F);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2, 1.0F);
        firstLine.addView(title, params);
        TextView value = new TextView(context);
        Runnable onFontChanged = () -> {
            FontFamily first = ModernUIClient.getInstance().getFirstFontFamily();
            if (first != null) {
                value.setText(first.getFamilyName(value.getTextLocale()));
            } else {
                value.setText("NONE");
            }
        };
        onFontChanged.run();
        value.setTextAlignment(6);
        value.setTextSize(14.0F);
        LinearLayout.LayoutParams paramsx = new LinearLayout.LayoutParams(-2, -2);
        firstLine.addView(value, paramsx);
        firstLine.setOnClickListener(new PreferencesFragment.PreferredFontCollapsed(layout, onFontChanged));
        ThemeControl.addBackground(firstLine);
        layout.addView(firstLine);
        params = new LinearLayout.LayoutParams(-1, -2);
        params.gravity = 17;
        params.setMargins(dp6, layout.dp(3.0F), dp6, layout.dp(3.0F));
        category.addView(layout, params);
        layout = createStringListOption(context, "modernui.center.font.fallbackFonts", Config.CLIENT.mFallbackFontFamilyList, () -> {
            Config.CLIENT.saveAndReloadAsync();
            reloadDefaultTypeface(context, () -> {
            });
        });
        category.addView(layout);
        category.addView(createBooleanOption(context, "modernui.center.font.colorEmoji", Config.CLIENT.mUseColorEmoji, () -> {
            Config.CLIENT.saveAndReloadAsync();
            reloadDefaultTypeface(context, () -> {
            });
        }));
        category.addView(createBooleanOption(context, "modernui.center.font.antiAliasing", Config.CLIENT.mAntiAliasing, Config.CLIENT::saveAndReloadAsync));
        category.addView(createBooleanOption(context, "modernui.center.font.autoHinting", Config.CLIENT.mAutoHinting, Config.CLIENT::saveAndReloadAsync));
        category.addView(createStringListOption(context, "modernui.center.font.fontRegistrationList", Config.CLIENT.mFontRegistrationList, () -> {
            Config.CLIENT.saveAsync();
            Toast.makeText(context, I18n.get("gui.modernui.restart_to_work"), 0).show();
        }));
        content.addView(category);
        category = createCategoryList(context, "modernui.center.category.system");
        category.addView(createBooleanOption(context, "modernui.center.system.forceRtlLayout", Config.CLIENT.mForceRtl, Config.CLIENT::saveAndReloadAsync));
        category.addView(createFloatOption(context, "modernui.center.system.globalFontScale", 0.5F, 2.0F, 4, Config.CLIENT.mFontScale, 10.0F, Config.CLIENT::saveAndReloadAsync));
        layout = createInputBox(context, "modernui.center.system.globalAnimationScale");
        EditText input = layout.requireViewById(16908297);
        input.setText(Float.toString(ValueAnimator.sDurationScale));
        input.setFilters(new InputFilter[] { DigitsInputFilter.getInstance(null, false, true), new InputFilter.LengthFilter(4) });
        input.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                EditText v = (EditText) view;
                double scale = Math.max(Math.min(Double.parseDouble(v.getText().toString()), 10.0), 0.1);
                v.setText(Double.toString(scale));
                if (scale != (double) ValueAnimator.sDurationScale) {
                    ValueAnimator.sDurationScale = (float) scale;
                }
            }
        });
        category.addView(layout);
        category.addView(createBooleanOption(context, "modernui.center.system.developerMode", Config.COMMON.developerMode, Config.COMMON::saveAndReloadAsync));
        content.addView(category);
        content.setDividerDrawable(ThemeControl.makeDivider(content));
        content.setDividerPadding(content.dp(8.0F));
        content.setShowDividers(2);
        return content;
    }

    public LinearLayout createFirstPage(Context context) {
        LinearLayout content = new LinearLayout(context);
        content.setOrientation(1);
        LayoutTransition transition = new LayoutTransition();
        transition.enableTransitionType(4);
        content.setLayoutTransition(transition);
        Runnable saveFn = Config.CLIENT::saveAndReloadAsync;
        LinearLayout list = createCategoryList(context, "modernui.center.category.screen");
        list.addView(createGuiScaleOption(context));
        list.addView(createColorOpacityOption(context, "modernui.center.screen.backgroundOpacity", Config.CLIENT.mBackgroundColor, saveFn));
        list.addView(createIntegerOption(context, "modernui.center.screen.backgroundDuration", 0, 800, 3, 50, Config.CLIENT.mBackgroundDuration, saveFn));
        list.addView(createBooleanOption(context, "modernui.center.screen.blurEffect", Config.CLIENT.mBlurEffect, saveFn));
        list.addView(createBooleanOption(context, "modernui.center.screen.blurWithBackground", Config.CLIENT.mBlurWithBackground, saveFn));
        list.addView(createIntegerOption(context, "modernui.center.screen.blurRadius", 2, 18, 2, 1, Config.CLIENT.mBlurRadius, saveFn));
        list.addView(createSpinnerOption(context, "modernui.center.screen.windowMode", Config.Client.WindowMode.values(), Config.CLIENT.mWindowMode, saveFn));
        list.addView(createIntegerOption(context, "modernui.center.screen.framerateInactive", 0, 255, 3, 5, Config.CLIENT.mFramerateInactive, saveFn));
        list.addView(createIntegerOption(context, "modernui.center.screen.framerateMinimized", 0, 255, 3, 5, Config.CLIENT.mFramerateMinimized, saveFn));
        list.addView(createFloatOption(context, "modernui.center.screen.masterVolumeInactive", 0.0F, 1.0F, 4, Config.CLIENT.mMasterVolumeInactive, 100.0F, saveFn));
        list.addView(createFloatOption(context, "modernui.center.screen.masterVolumeMinimized", 0.0F, 1.0F, 4, Config.CLIENT.mMasterVolumeMinimized, 100.0F, saveFn));
        list.addView(createBooleanOption(context, "modernui.center.screen.inventoryPause", Config.CLIENT.mInventoryPause, saveFn));
        content.addView(list);
        boolean rawTextEngineEnabled = !Boolean.parseBoolean(ModernUIClient.getBootstrapProperty("modernui_mc_disableTextEngine"));
        LinearLayout listx = createCategoryList(context, "modernui.center.category.extension");
        listx.addView(createBooleanOption(context, "modernui.center.extension.ding", Config.CLIENT.mDing, saveFn));
        listx.addView(createBooleanOption(context, "key.modernui.zoom", Config.CLIENT.mZoom, saveFn));
        listx.addView(createBooleanOption(context, "modernui.center.text.emojiShortcodes", Config.CLIENT.mEmojiShortcodes, Config.CLIENT::saveAndReloadAsync));
        LinearLayout option = createSwitchLayout(context, "modernui.center.extension.smoothScrolling");
        SwitchButton button = option.requireViewById(16908313);
        button.setChecked(!Boolean.parseBoolean(ModernUIClient.getBootstrapProperty("modernui_mc_disableSmoothScrolling")));
        button.setOnCheckedChangeListener((__, checked) -> {
            ModernUIClient.setBootstrapProperty("modernui_mc_disableSmoothScrolling", Boolean.toString(!checked));
            Toast.makeText(__.getContext(), I18n.get("gui.modernui.restart_to_work"), 0).show();
        });
        listx.addView(option);
        option = createSwitchLayout(context, "modernui.center.text.enhancedTextField");
        button = option.requireViewById(16908313);
        button.setChecked(!Boolean.parseBoolean(ModernUIClient.getBootstrapProperty("modernui_mc_disableEnhancedTextField")));
        button.setOnCheckedChangeListener((__, checked) -> {
            ModernUIClient.setBootstrapProperty("modernui_mc_disableEnhancedTextField", Boolean.toString(!checked));
            Toast.makeText(__.getContext(), I18n.get("gui.modernui.restart_to_work"), 0).show();
        });
        listx.addView(option);
        option = createSwitchLayout(context, "modernui.center.extension.tooltip");
        button = option.requireViewById(16908313);
        button.setChecked(Config.CLIENT.mTooltip.get());
        button.setOnCheckedChangeListener((view, checked) -> {
            Config.CLIENT.mTooltip.set(Boolean.valueOf(checked));
            saveFn.run();
            if (checked) {
                if (this.mTooltipCategory == null) {
                    this.mTooltipCategory = createTooltipCategory(view.getContext());
                    content.addView(this.mTooltipCategory, 2);
                } else {
                    this.mTooltipCategory.setVisibility(0);
                }
            } else if (this.mTooltipCategory != null) {
                this.mTooltipCategory.setVisibility(8);
            }
        });
        listx.addView(option);
        option = createSwitchLayout(context, "modernui.center.text.textEngine");
        button = option.requireViewById(16908313);
        button.setChecked(rawTextEngineEnabled);
        button.setOnCheckedChangeListener((view, checked) -> {
            ModernUIClient.setBootstrapProperty("modernui_mc_disableTextEngine", Boolean.toString(!checked));
            Toast.makeText(view.getContext(), I18n.get("gui.modernui.restart_to_work"), 0).show();
            if (checked) {
                if (this.mTextLayoutCategory == null) {
                    this.mTextLayoutCategory = createTextLayoutCategory(view.getContext());
                    this.mTextRenderingCategory = createTextRenderingCategory(view.getContext());
                    content.addView(this.mTextLayoutCategory);
                    content.addView(this.mTextRenderingCategory);
                } else {
                    this.mTextLayoutCategory.setVisibility(0);
                    this.mTextRenderingCategory.setVisibility(0);
                }
            } else if (this.mTextLayoutCategory != null) {
                this.mTextLayoutCategory.setVisibility(8);
                this.mTextRenderingCategory.setVisibility(8);
            }
        });
        listx.addView(option);
        content.addView(listx);
        if (Config.CLIENT.mTooltip.get()) {
            this.mTooltipCategory = createTooltipCategory(context);
            content.addView(this.mTooltipCategory);
        }
        if (rawTextEngineEnabled) {
            this.mTextLayoutCategory = createTextLayoutCategory(context);
            content.addView(this.mTextLayoutCategory);
            this.mTextRenderingCategory = createTextRenderingCategory(context);
            content.addView(this.mTextRenderingCategory);
        }
        content.setDividerDrawable(ThemeControl.makeDivider(content));
        content.setDividerPadding(content.dp(8.0F));
        content.setShowDividers(2);
        return content;
    }

    public static LinearLayout createTooltipCategory(Context context) {
        LinearLayout category = createCategoryList(context, "modernui.center.category.tooltip");
        Runnable saveFn = Config.CLIENT::saveAndReloadAsync;
        category.addView(createBooleanOption(context, "modernui.center.tooltip.centerTitle", Config.CLIENT.mCenterTooltipTitle, saveFn));
        category.addView(createBooleanOption(context, "modernui.center.tooltip.titleBreak", Config.CLIENT.mTooltipTitleBreak, saveFn));
        category.addView(createBooleanOption(context, "modernui.center.tooltip.exactPositioning", Config.CLIENT.mExactTooltipPositioning, saveFn));
        category.addView(createColorOpacityOption(context, "modernui.center.tooltip.backgroundOpacity", Config.CLIENT.mTooltipFill, saveFn));
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(1);
        int dp6 = layout.dp(6.0F);
        Button title = new Button(context);
        title.setText(I18n.get("modernui.center.tooltip.borderStyle"));
        title.setTextAlignment(5);
        title.setTextSize(14.0F);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        layout.addView(title, params);
        title.setOnClickListener(new PreferencesFragment.TooltipBorderCollapsed(layout, saveFn));
        ThemeControl.addBackground(title);
        params = new LinearLayout.LayoutParams(-1, -2);
        params.gravity = 17;
        params.setMargins(dp6, layout.dp(3.0F), dp6, 0);
        category.addView(layout, params);
        return category;
    }

    public static LinearLayout createTextLayoutCategory(Context context) {
        LinearLayout category = createCategoryList(context, "modernui.center.category.textLayout");
        Runnable saveFn = Config.TEXT::saveAndReloadAsync;
        category.addView(createSpinnerOption(context, "modernui.center.text.defaultFontBehavior", Config.Text.DefaultFontBehavior.values(), Config.TEXT.mDefaultFontBehavior, saveFn));
        category.addView(createStringListOption(context, "modernui.center.text.defaultFontRuleSet", Config.TEXT.mDefaultFontRuleSet, saveFn));
        category.addView(createSpinnerOption(context, "modernui.center.text.bidiHeuristicAlgo", Config.Text.TextDirection.values(), Config.TEXT.mTextDirection, saveFn));
        category.addView(createSpinnerOption(context, "modernui.center.text.lineBreakStyle", Config.Text.LineBreakStyle.values(), Config.TEXT.mLineBreakStyle, saveFn));
        category.addView(createSpinnerOption(context, "modernui.center.text.lineBreakWordStyle", Config.Text.LineBreakWordStyle.values(), Config.TEXT.mLineBreakWordStyle, saveFn));
        category.addView(createBooleanOption(context, "modernui.center.text.allowAsyncLayout", Config.TEXT.mAllowAsyncLayout, saveFn));
        category.addView(createBooleanOption(context, "modernui.center.text.useComponentCache", Config.TEXT.mUseComponentCache, saveFn));
        category.addView(createBooleanOption(context, "modernui.center.text.fixedResolution", Config.TEXT.mFixedResolution, saveFn));
        category.addView(createFloatOption(context, "modernui.center.text.baseFontSize", 6.5F, 9.5F, 5, Config.TEXT.mBaseFontSize, 10.0F, saveFn));
        category.addView(createIntegerOption(context, "modernui.center.text.cacheLifespan", 2, 15, 2, 1, Config.TEXT.mCacheLifespan, saveFn));
        return category;
    }

    public static LinearLayout createTextRenderingCategory(Context context) {
        LinearLayout category = createCategoryList(context, "modernui.center.category.textRendering");
        Runnable saveFn = Config.TEXT::saveAndReloadAsync;
        category.addView(createBooleanOption(context, "modernui.center.text.textShadersInWorld", Config.TEXT.mUseTextShadersInWorld, saveFn));
        category.addView(createBooleanOption(context, "modernui.center.text.allowSDFTextIn2D", Config.TEXT.mAllowSDFTextIn2D, saveFn));
        category.addView(createBooleanOption(context, "modernui.center.text.smartSDFShaders", Config.TEXT.mSmartSDFShaders, saveFn));
        category.addView(createBooleanOption(context, "modernui.center.text.computeDeviceFontSize", Config.TEXT.mComputeDeviceFontSize, saveFn));
        category.addView(createBooleanOption(context, "modernui.center.text.allowShadow", Config.TEXT.mAllowShadow, saveFn));
        category.addView(createFloatOption(context, "modernui.center.text.shadowOffset", 0.2F, 2.0F, 5, Config.TEXT.mShadowOffset, 10.0F, saveFn));
        category.addView(createFloatOption(context, "modernui.center.text.baselineShift", 4.0F, 10.0F, 5, Config.TEXT.mBaselineShift, 10.0F, saveFn));
        category.addView(createFloatOption(context, "modernui.center.text.outlineOffset", 0.2F, 2.0F, 5, Config.TEXT.mOutlineOffset, 10.0F, saveFn));
        return category;
    }

    @NonNull
    public static LinearLayout createCategoryList(Context context, String name) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(1);
        int dp6 = layout.dp(6.0F);
        int dp12 = layout.dp(12.0F);
        int dp18 = layout.dp(18.0F);
        TextView title = new TextView(context);
        title.setId(16908310);
        title.setText(I18n.get(name));
        title.setTextSize(16.0F);
        title.setTextColor(-3300456);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
        params.gravity = 8388611;
        params.setMargins(dp6, dp6, dp6, dp6);
        layout.addView(title, params);
        LinearLayout.LayoutParams paramsx = new LinearLayout.LayoutParams(-1, -2);
        paramsx.gravity = 17;
        paramsx.setMargins(dp12, dp12, dp12, dp18);
        layout.setLayoutParams(paramsx);
        return layout;
    }

    public static LinearLayout createSwitchLayout(Context context, String name) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(0);
        layout.setHorizontalGravity(8388611);
        int dp3 = layout.dp(3.0F);
        int dp6 = layout.dp(6.0F);
        TextView title = new TextView(context);
        title.setText(I18n.get(name));
        title.setTextAlignment(5);
        title.setTextSize(14.0F);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2, 1.0F);
        params.gravity = 16;
        layout.addView(title, params);
        SwitchButton button = new SwitchButton(context);
        button.setId(16908313);
        button.setCheckedColor(-3300456);
        params = new LinearLayout.LayoutParams(layout.dp(36.0F), layout.dp(16.0F));
        params.gravity = 16;
        params.setMargins(0, dp3, 0, dp3);
        layout.addView(button, params);
        LinearLayout.LayoutParams paramsx = new LinearLayout.LayoutParams(-1, -2);
        paramsx.gravity = 17;
        paramsx.setMargins(dp6, 0, dp6, 0);
        layout.setLayoutParams(paramsx);
        String tooltip = name + "_desc";
        if (I18n.exists(tooltip)) {
            layout.setTooltipText(I18n.get(tooltip));
        }
        return layout;
    }

    public static LinearLayout createBooleanOption(Context context, String name, ForgeConfigSpec.BooleanValue config, Runnable saveFn) {
        LinearLayout layout = createSwitchLayout(context, name);
        SwitchButton button = layout.requireViewById(16908313);
        button.setChecked(config.get());
        button.setOnCheckedChangeListener((__, checked) -> {
            config.set(Boolean.valueOf(checked));
            saveFn.run();
        });
        return layout;
    }

    public static <E extends Enum<E>> LinearLayout createSpinnerOption(Context context, String name, E[] values, ForgeConfigSpec.EnumValue<E> config, Runnable saveFn) {
        LinearLayout option = new LinearLayout(context);
        option.setOrientation(0);
        option.setHorizontalGravity(8388611);
        int dp6 = option.dp(6.0F);
        TextView title = new TextView(context);
        title.setText(I18n.get(name));
        title.setTextAlignment(5);
        title.setTextSize(14.0F);
        String tooltip = name + "_desc";
        if (I18n.exists(tooltip)) {
            title.setTooltipText(I18n.get(tooltip));
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2, 1.0F);
        params.gravity = 16;
        option.addView(title, params);
        Spinner spinner = new Spinner(context);
        spinner.setGravity(8388613);
        spinner.setAdapter((SpinnerAdapter) (new ArrayAdapter<>(context, values)));
        spinner.setSelection(config.get().ordinal());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                E newValue = values[position];
                if (config.get() != newValue) {
                    config.set(newValue);
                    saveFn.run();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        LinearLayout.LayoutParams paramsx = new LinearLayout.LayoutParams(-2, -2);
        paramsx.gravity = 16;
        option.addView(spinner, paramsx);
        LinearLayout.LayoutParams paramsxx = new LinearLayout.LayoutParams(-1, -2);
        paramsxx.gravity = 17;
        paramsxx.setMargins(dp6, 0, dp6, 0);
        option.setLayoutParams(paramsxx);
        return option;
    }

    private static LinearLayout createGuiScaleOption(Context context) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(0);
        layout.setHorizontalGravity(8388611);
        int dp3 = layout.dp(3.0F);
        int dp6 = layout.dp(6.0F);
        TextView title = new TextView(context);
        title.setText(I18n.get("options.guiScale"));
        title.setTextAlignment(5);
        title.setTextSize(14.0F);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2, 1.0F);
        params.gravity = 8388627;
        layout.addView(title, params);
        SeekBar slider = new SeekBar(context);
        slider.setClickable(true);
        params = new LinearLayout.LayoutParams(slider.dp(200.0F), -2);
        params.gravity = 16;
        layout.addView(slider, params);
        final TextView tv = new TextView(context);
        tv.setTextAlignment(6);
        tv.setTextSize(14.0F);
        tv.setPadding(dp3, 0, dp3, 0);
        LinearLayout.LayoutParams paramsx = new LinearLayout.LayoutParams(-2, -2);
        paramsx.gravity = 16;
        layout.addView(tv, paramsx);
        int curValue = Minecraft.getInstance().options.guiScale().get();
        tv.setText(guiScaleToString(curValue));
        tv.setMinWidth(slider.dp(50.0F));
        slider.setMax(8);
        slider.setProgress(curValue);
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int newValue = seekBar.getProgress();
                tv.setText(PreferencesFragment.guiScaleToString(newValue));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int newValue = seekBar.getProgress();
                Core.executeOnMainThread(() -> {
                    Minecraft minecraft = Minecraft.getInstance();
                    minecraft.options.guiScale().set(newValue);
                    if ((int) minecraft.getWindow().getGuiScale() != minecraft.getWindow().calculateScale(newValue, false)) {
                        minecraft.resizeDisplay();
                    }
                    minecraft.options.save();
                });
                tv.setText(PreferencesFragment.guiScaleToString(newValue));
            }
        });
        LinearLayout.LayoutParams paramsxx = new LinearLayout.LayoutParams(-1, -2);
        paramsxx.gravity = 17;
        paramsxx.setMargins(dp6, 0, dp6, 0);
        layout.setLayoutParams(paramsxx);
        return layout;
    }

    private static CharSequence guiScaleToString(int value) {
        int r = MuiModApi.calcGuiScales();
        if (value == 0) {
            int auto = r >> 4 & 15;
            return "A (" + auto + ")";
        } else {
            String valueString = Integer.toString(value);
            int min = r >> 8 & 15;
            int max = r & 15;
            if (value >= min && value <= max) {
                return valueString;
            } else {
                String hint;
                if (value < min) {
                    hint = " (" + min + ")";
                } else {
                    hint = " (" + max + ")";
                }
                SpannableString spannableString = new SpannableString(valueString + hint);
                spannableString.setSpan(new ForegroundColorSpan(-43691), 0, spannableString.length(), 33);
                return spannableString;
            }
        }
    }

    @NonNull
    public static LinearLayout createInputBox(Context context, String name) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(0);
        layout.setHorizontalGravity(8388611);
        int dp3 = layout.dp(3.0F);
        int dp6 = layout.dp(6.0F);
        TextView title = new TextView(context);
        title.setText(I18n.get(name));
        title.setTextAlignment(5);
        title.setTextSize(14.0F);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2, 1.0F);
        params.gravity = 8388627;
        layout.addView(title, params);
        EditText input = new EditText(context);
        input.setId(16908297);
        input.setTextAlignment(6);
        input.setTextSize(14.0F);
        input.setPadding(dp3, 0, dp3, 0);
        ThemeControl.addBackground(input);
        params = new LinearLayout.LayoutParams(-2, -2);
        params.gravity = 16;
        layout.addView(input, params);
        LinearLayout.LayoutParams paramsx = new LinearLayout.LayoutParams(-1, -2);
        paramsx.gravity = 17;
        paramsx.setMargins(dp6, 0, dp6, 0);
        layout.setLayoutParams(paramsx);
        String tooltip = name + "_desc";
        if (I18n.exists(tooltip)) {
            layout.setTooltipText(I18n.get(tooltip));
        }
        return layout;
    }

    public static LinearLayout createInputBoxWithSlider(Context context, String name) {
        LinearLayout layout = createInputBox(context, name);
        SeekBar slider = new SeekBar(context);
        slider.setId(16908314);
        slider.setClickable(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(slider.dp(200.0F), -2);
        params.gravity = 16;
        layout.addView(slider, 1, params);
        return layout;
    }

    public static LinearLayout createIntegerOption(Context context, String name, int minValue, int maxValue, int maxLength, int stepSize, ForgeConfigSpec.IntValue config, Runnable saveFn) {
        return createIntegerOption(context, name, minValue, maxValue, maxLength, stepSize, config, config::set, saveFn);
    }

    public static LinearLayout createIntegerOption(Context context, String name, int minValue, int maxValue, int maxLength, int stepSize, Supplier<Integer> getter, Consumer<Integer> setter, Runnable saveFn) {
        LinearLayout layout = createInputBoxWithSlider(context, name);
        SeekBar slider = layout.requireViewById(16908314);
        final EditText input = layout.requireViewById(16908297);
        input.setFilters(new InputFilter[] { DigitsInputFilter.getInstance((Locale) null), new InputFilter.LengthFilter(maxLength) });
        int curValue = (Integer) getter.get();
        input.setText(Integer.toString(curValue));
        input.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                EditText v = (EditText) view;
                int newValue = MathUtil.clamp(Integer.parseInt(v.getText().toString()), minValue, maxValue);
                replaceText(v, Integer.toString(newValue));
                if (newValue != (Integer) getter.get()) {
                    setter.accept(newValue);
                    int curProgress = (newValue - minValue) / stepSize;
                    slider.setProgress(curProgress, true);
                    saveFn.run();
                }
            }
        });
        input.setMinWidth(slider.dp(50.0F));
        int steps = (maxValue - minValue) / stepSize;
        slider.setMax(steps);
        slider.setProgress((curValue - minValue) / stepSize);
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int newValue = seekBar.getProgress() * stepSize + minValue;
                PreferencesFragment.replaceText(input, Integer.toString(newValue));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int newValue = seekBar.getProgress() * stepSize + minValue;
                if (newValue != (Integer) getter.get()) {
                    setter.accept(newValue);
                    PreferencesFragment.replaceText(input, Integer.toString(newValue));
                    saveFn.run();
                }
            }
        });
        return layout;
    }

    public static LinearLayout createColorOpacityOption(Context context, String name, ForgeConfigSpec.ConfigValue<List<? extends String>> config, Runnable saveFn) {
        Supplier<Double> getter = () -> {
            List<? extends String> colors = config.get();
            if (colors != null && !colors.isEmpty()) {
                try {
                    int color = Color.parseColor((String) colors.get(0));
                    return (double) (color >>> 24) / 255.0;
                } catch (IllegalArgumentException var3) {
                    var3.printStackTrace();
                }
            }
            return 1.0;
        };
        Consumer<Double> setter = d -> {
            int alpha = (int) (d * 255.0 + 0.5);
            ArrayList<String> newList = new ArrayList((Collection) config.get());
            if (newList.isEmpty()) {
                newList.add("#FF000000");
            }
            ListIterator<String> it = newList.listIterator();
            while (it.hasNext()) {
                int color = Color.parseColor((String) it.next());
                color = color & 16777215 | alpha << 24;
                it.set(String.format(Locale.ROOT, "#%08X", color));
            }
            config.set(newList);
        };
        return createFloatOption(context, name, 0.0F, 1.0F, 4, getter, setter, 100.0F, saveFn);
    }

    public static LinearLayout createFloatOption(Context context, String name, float minValue, float maxValue, int maxLength, ForgeConfigSpec.DoubleValue config, float denominator, Runnable saveFn) {
        return createFloatOption(context, name, minValue, maxValue, maxLength, config, config::set, denominator, saveFn);
    }

    public static LinearLayout createFloatOption(Context context, String name, float minValue, float maxValue, int maxLength, Supplier<Double> getter, Consumer<Double> setter, float denominator, Runnable saveFn) {
        LinearLayout layout = createInputBoxWithSlider(context, name);
        SeekBar slider = layout.requireViewById(16908314);
        final EditText input = layout.requireViewById(16908297);
        input.setFilters(new InputFilter[] { DigitsInputFilter.getInstance(null, minValue < 0.0F, true), new InputFilter.LengthFilter(maxLength) });
        float curValue = ((Double) getter.get()).floatValue();
        input.setText(Float.toString(curValue));
        input.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                EditText v = (EditText) view;
                float newValue = MathUtil.clamp(Float.parseFloat(v.getText().toString()), minValue, maxValue);
                replaceText(v, Float.toString(newValue));
                if ((double) newValue != (Double) getter.get()) {
                    setter.accept((double) newValue);
                    int curProgressx = Math.round((newValue - minValue) * denominator);
                    slider.setProgress(curProgressx, true);
                    saveFn.run();
                }
            }
        });
        input.setMinWidth(slider.dp(50.0F));
        int steps = Math.round((maxValue - minValue) * denominator);
        slider.setMax(steps);
        int curProgress = Math.round((curValue - minValue) * denominator);
        slider.setProgress(curProgress);
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double newValue = (double) ((float) seekBar.getProgress() / denominator + minValue);
                PreferencesFragment.replaceText(input, Float.toString((float) newValue));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                double newValue = (double) ((float) seekBar.getProgress() / denominator + minValue);
                if (newValue != (Double) getter.get()) {
                    setter.accept((double) ((float) newValue));
                    PreferencesFragment.replaceText(input, Float.toString((float) newValue));
                    saveFn.run();
                }
            }
        });
        return layout;
    }

    public static LinearLayout createStringListOption(Context context, String name, ForgeConfigSpec.ConfigValue<List<? extends String>> config, Runnable saveFn) {
        LinearLayout option = new LinearLayout(context);
        option.setOrientation(0);
        option.setHorizontalGravity(8388611);
        int dp3 = option.dp(3.0F);
        TextView title = new TextView(context);
        title.setText(I18n.get(name));
        title.setTextAlignment(5);
        title.setTextSize(14.0F);
        title.setMinWidth(option.dp(60.0F));
        String tooltip = name + "_desc";
        if (I18n.exists(tooltip)) {
            title.setTooltipText(I18n.get(tooltip));
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, -2, 2.0F);
        params.gravity = 8388627;
        option.addView(title, params);
        EditText input = new EditText(context);
        input.setId(16908297);
        input.setTextAlignment(6);
        input.setTextSize(14.0F);
        input.setPadding(dp3, 0, dp3, 0);
        input.setText(String.join("\n", (Iterable) config.get()));
        input.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                EditText v = (EditText) view;
                ArrayList<String> result = new ArrayList();
                for (String s : v.getText().toString().split("\n")) {
                    if (!s.isBlank()) {
                        String strip = s.strip();
                        if (!strip.isEmpty() && !result.contains(strip)) {
                            result.add(strip);
                        }
                    }
                }
                replaceText(v, String.join("\n", result));
                if (!Objects.equals(config.get(), result)) {
                    config.set(result);
                    saveFn.run();
                }
            }
        });
        ThemeControl.addBackground(input);
        LinearLayout.LayoutParams paramsx = new LinearLayout.LayoutParams(0, -2, 5.0F);
        paramsx.gravity = 16;
        option.addView(input, paramsx);
        LinearLayout.LayoutParams paramsxx = new LinearLayout.LayoutParams(-1, -2);
        paramsxx.gravity = 17;
        paramsxx.setMargins(option.dp(6.0F), dp3, option.dp(6.0F), dp3);
        option.setLayoutParams(paramsxx);
        return option;
    }

    private static void replaceText(@NonNull EditText editText, @NonNull CharSequence newText) {
        Editable text = editText.getText();
        text.replace(0, text.length(), newText);
    }

    private static void reloadDefaultTypeface(@NonNull Context context, @NonNull Runnable onFontChanged) {
        CompletableFuture<Typeface> future = Minecraft.getInstance().m_18691_(() -> {
            Typeface oldTypeface = ModernUI.getSelectedTypeface();
            ModernUIClient client = ModernUIClient.getInstance();
            client.reloadTypeface();
            client.reloadFontStrike();
            return oldTypeface;
        });
        future.whenCompleteAsync((oldTypeface, throwable) -> {
            if (throwable == null) {
                onFontChanged.run();
                refreshViewTypeface(UIManager.getInstance().getDecorView(), oldTypeface);
                Toast.makeText(context, I18n.get("gui.modernui.font_reloaded"), 0).show();
            }
        }, Core.getUiThreadExecutor());
    }

    private static void refreshViewTypeface(@NonNull ViewGroup vg, Typeface oldTypeface) {
        int cc = vg.getChildCount();
        for (int i = 0; i < cc; i++) {
            View v = vg.getChildAt(i);
            if (v instanceof ViewGroup) {
                refreshViewTypeface((ViewGroup) v, oldTypeface);
            } else if (v instanceof TextView) {
                TextView tv = (TextView) v;
                if (tv.getTypeface() == oldTypeface) {
                    tv.setTypeface(ModernUI.getSelectedTypeface());
                }
            }
        }
    }

    public static class PreferredFontCollapsed implements View.OnClickListener {

        final ViewGroup mParent;

        final Runnable mOnFontChanged;

        LinearLayout mContent;

        EditText mInput;

        Spinner mSpinner;

        public PreferredFontCollapsed(ViewGroup parent, Runnable onFontChanged) {
            this.mParent = parent;
            this.mOnFontChanged = onFontChanged;
        }

        @Override
        public void onClick(View v) {
            if (this.mContent != null) {
                this.mContent.setVisibility(this.mContent.getVisibility() == 8 ? 0 : 8);
            } else {
                this.mContent = new LinearLayout(this.mParent.getContext());
                this.mContent.setOrientation(1);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
                params.setMargins(0, this.mContent.dp(6.0F), 0, 0);
                LinearLayout layout = PreferencesFragment.createInputBox(this.mParent.getContext(), "gui.modernui.configValue");
                EditText input = this.mInput = layout.requireViewById(16908297);
                input.setText((CharSequence) Config.CLIENT.mFirstFontFamily.get());
                input.setOnFocusChangeListener((view, hasFocus) -> {
                    if (!hasFocus) {
                        EditText v1 = (EditText) view;
                        String newValue = v1.getText().toString().strip();
                        this.applyNewValue(v1.getContext(), newValue);
                    }
                });
                this.mContent.addView(layout);
                Spinner spinner = this.mSpinner = new Spinner(this.mParent.getContext());
                CompletableFuture.supplyAsync(() -> {
                    List<PreferencesFragment.PreferredFontCollapsed.FontFamilyItem> values = (List<PreferencesFragment.PreferredFontCollapsed.FontFamilyItem>) FontFamily.getSystemFontMap().values().stream().map(family -> new PreferencesFragment.PreferredFontCollapsed.FontFamilyItem(family.getFamilyName(), family.getFamilyName(ModernUI.getSelectedLocale()))).sorted().collect(Collectors.toList());
                    String chooseFont = I18n.get("modernui.center.font.chooseFont");
                    values.add(0, new PreferencesFragment.PreferredFontCollapsed.FontFamilyItem(chooseFont, chooseFont));
                    return values;
                }).thenAcceptAsync(values -> {
                    this.mSpinner.setAdapter((SpinnerAdapter) (new PreferencesFragment.PreferredFontCollapsed.FontFamilyAdapter(this.mParent.getContext(), values)));
                    this.mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position != 0) {
                                String newValue = ((PreferencesFragment.PreferredFontCollapsed.FontFamilyItem) values.get(position)).rootName;
                                boolean changed = PreferredFontCollapsed.this.applyNewValue(view.getContext(), newValue);
                                if (changed) {
                                    PreferredFontCollapsed.this.mInput.setText(newValue);
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    FontFamily first = ModernUIClient.getInstance().getFirstFontFamily();
                    if (first != null) {
                        for (int i = 1; i < values.size(); i++) {
                            PreferencesFragment.PreferredFontCollapsed.FontFamilyItem candidate = (PreferencesFragment.PreferredFontCollapsed.FontFamilyItem) values.get(i);
                            if (candidate.rootName.equalsIgnoreCase(first.getFamilyName())) {
                                this.mSpinner.setSelection(i);
                                break;
                            }
                        }
                    }
                }, Core.getUiThreadExecutor());
                this.mContent.addView(spinner, new LinearLayout.LayoutParams(params));
                Button openFile = new Button(this.mParent.getContext());
                openFile.setText(I18n.get("modernui.center.font.openFontFile"));
                openFile.setTextSize(14.0F);
                openFile.setOnClickListener(v1 -> CompletableFuture.runAsync(() -> {
                    MemoryStack stack = MemoryStack.stackPush();
                    String path;
                    try {
                        PointerBuffer filters = stack.mallocPointer(4);
                        stack.nUTF8("*.ttf", true);
                        filters.put(stack.getPointerAddress());
                        stack.nUTF8("*.otf", true);
                        filters.put(stack.getPointerAddress());
                        stack.nUTF8("*.ttc", true);
                        filters.put(stack.getPointerAddress());
                        stack.nUTF8("*.otc", true);
                        filters.put(stack.getPointerAddress());
                        filters.rewind();
                        path = TinyFileDialogs.tinyfd_openFileDialog(null, null, filters, "TrueType/OpenType Fonts (*.ttf;*.otf;*.ttc;*.otc)", false);
                    } catch (Throwable var7) {
                        if (stack != null) {
                            try {
                                stack.close();
                            } catch (Throwable var6x) {
                                var7.addSuppressed(var6x);
                            }
                        }
                        throw var7;
                    }
                    if (stack != null) {
                        stack.close();
                    }
                    if (path != null) {
                        v1.post(() -> {
                            boolean changed = this.applyNewValue(v1.getContext(), path);
                            if (changed) {
                                this.mInput.setText(path);
                            }
                            this.mSpinner.setSelection(0);
                        });
                    }
                }));
                this.mContent.addView(openFile, new LinearLayout.LayoutParams(params));
                this.mParent.addView(this.mContent, params);
            }
        }

        private boolean applyNewValue(Context context, @NonNull String newValue) {
            if (!newValue.equals(Config.CLIENT.mFirstFontFamily.get())) {
                Config.CLIENT.mFirstFontFamily.set(newValue);
                Config.CLIENT.saveAndReloadAsync();
                PreferencesFragment.reloadDefaultTypeface(context, this.mOnFontChanged);
                return true;
            } else {
                return false;
            }
        }

        private static class FontFamilyAdapter extends ArrayAdapter<PreferencesFragment.PreferredFontCollapsed.FontFamilyItem> {

            private final Context mContext;

            public FontFamilyAdapter(Context context, @NonNull List<PreferencesFragment.PreferredFontCollapsed.FontFamilyItem> objects) {
                super(context, objects);
                this.mContext = context;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView tv;
                if (convertView == null) {
                    tv = new TextView(this.mContext);
                } else {
                    tv = (TextView) convertView;
                }
                PreferencesFragment.PreferredFontCollapsed.FontFamilyItem item = this.getItem(position);
                tv.setText(item.localeName);
                tv.setTextSize(14.0F);
                tv.setTextAlignment(4);
                int dp4 = tv.dp(4.0F);
                tv.setPadding(dp4, dp4, dp4, dp4);
                return tv;
            }

            @NonNull
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                return this.getView(position, convertView, parent);
            }
        }

        public static record FontFamilyItem(String rootName, String localeName) implements Comparable<PreferencesFragment.PreferredFontCollapsed.FontFamilyItem> {

            public String toString() {
                return this.localeName;
            }

            public int compareTo(@NonNull PreferencesFragment.PreferredFontCollapsed.FontFamilyItem o) {
                return this.localeName.compareTo(o.localeName);
            }
        }
    }

    private class ThePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Context context = container.getContext();
            ScrollView sv = new ScrollView(context);
            if (position == 1) {
                sv.addView(PreferencesFragment.createSecondPage(context), -1, -2);
            } else {
                sv.addView(PreferencesFragment.this.createFirstPage(context), -1, -2);
                final ObjectAnimator animator = ObjectAnimator.ofFloat(sv, View.ROTATION_Y, container.isLayoutRtl() ? -45.0F : 45.0F, 0.0F);
                animator.setInterpolator(TimeInterpolator.DECELERATE);
                sv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        animator.start();
                        v.removeOnLayoutChangeListener(this);
                    }
                });
            }
            sv.setEdgeEffectColor(-3300456);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, -1, 1.0F);
            int dp6 = sv.dp(6.0F);
            params.setMargins(dp6, dp6, dp6, dp6);
            container.addView(sv, params);
            return sv;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }

    public static class TooltipBorderCollapsed implements View.OnClickListener {

        public static final String[][] PRESET_COLORS = new String[][] { { "#F0AADCF0", "#F0FFC3F7", "#F0BFF2B2", "#F0D27F3D" }, { "#F0AADCF0", "#F0DAD0F4", "#F0FFC3F7", "#F0DAD0F4" }, { "#F028007F", "#F028007F", "#F014003F", "#F014003F" }, { "#F0E0E0E0", "#F0B0B0B0", "#F0FFFFFF", "#F0B0B0B0" } };

        final ViewGroup mParent;

        final Runnable mSaveFn;

        LinearLayout mContent;

        FourColorPicker mColorPicker;

        ViewGroup mBorderWidth;

        ViewGroup mCornerRadius;

        ViewGroup mShadowRadius;

        ViewGroup mShadowAlpha;

        public TooltipBorderCollapsed(ViewGroup parent, Runnable saveFn) {
            this.mParent = parent;
            this.mSaveFn = saveFn;
        }

        @Override
        public void onClick(View v) {
            if (this.mContent != null) {
                this.mContent.setVisibility(this.mContent.getVisibility() == 8 ? 0 : 8);
            } else {
                this.mContent = new LinearLayout(this.mParent.getContext());
                this.mContent.setOrientation(1);
                LinearLayout option = PreferencesFragment.createSwitchLayout(this.mParent.getContext(), "modernui.center.tooltip.roundedShapes");
                SwitchButton button = option.requireViewById(16908313);
                boolean rounded;
                button.setChecked(rounded = Config.CLIENT.mRoundedTooltip.get());
                button.setOnCheckedChangeListener((__, checked) -> {
                    Config.CLIENT.mRoundedTooltip.set(Boolean.valueOf(checked));
                    int visibility = checked ? 0 : 8;
                    this.mBorderWidth.setVisibility(visibility);
                    this.mCornerRadius.setVisibility(visibility);
                    this.mShadowRadius.setVisibility(visibility);
                    this.mShadowAlpha.setVisibility(visibility);
                    this.mSaveFn.run();
                });
                this.mContent.addView(option);
                option = PreferencesFragment.createFloatOption(this.mParent.getContext(), "modernui.center.tooltip.borderWidth", 0.5F, 2.5F, 4, Config.CLIENT.mTooltipWidth, thickness -> {
                    Config.CLIENT.mTooltipWidth.set(thickness);
                    if (this.mColorPicker != null) {
                        this.mColorPicker.setThicknessFactor(thickness.floatValue() / 3.0F);
                    }
                }, 100.0F, this.mSaveFn);
                if (!rounded) {
                    option.setVisibility(8);
                }
                this.mBorderWidth = option;
                this.mContent.addView(option);
                option = PreferencesFragment.createFloatOption(this.mParent.getContext(), "modernui.center.tooltip.cornerRadius", 0.0F, 8.0F, 3, Config.CLIENT.mTooltipRadius, 10.0F, this.mSaveFn);
                if (!rounded) {
                    option.setVisibility(8);
                }
                this.mCornerRadius = option;
                this.mContent.addView(option);
                option = PreferencesFragment.createFloatOption(this.mParent.getContext(), "modernui.center.tooltip.shadowRadius", 0.0F, 32.0F, 4, Config.CLIENT.mTooltipShadowRadius, 10.0F, this.mSaveFn);
                if (!rounded) {
                    option.setVisibility(8);
                }
                this.mShadowRadius = option;
                this.mContent.addView(option);
                option = PreferencesFragment.createFloatOption(this.mParent.getContext(), "modernui.center.tooltip.shadowOpacity", 0.0F, 1.0F, 4, Config.CLIENT.mTooltipShadowAlpha, 100.0F, this.mSaveFn);
                if (!rounded) {
                    option.setVisibility(8);
                }
                this.mShadowAlpha = option;
                this.mContent.addView(option);
                this.mContent.addView(PreferencesFragment.createBooleanOption(this.mParent.getContext(), "modernui.center.tooltip.adaptiveColors", Config.CLIENT.mAdaptiveTooltipColors, this.mSaveFn));
                this.mContent.addView(PreferencesFragment.createIntegerOption(this.mParent.getContext(), "modernui.center.tooltip.borderCycle", 0, 5000, 4, 100, Config.CLIENT.mTooltipCycle, this.mSaveFn));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
                params.setMargins(0, this.mContent.dp(6.0F), 0, 0);
                LinearLayout buttonGroup = new LinearLayout(this.mParent.getContext());
                buttonGroup.setOrientation(0);
                for (int i = 0; i < 4; i++) {
                    Button buttonx = new Button(this.mParent.getContext());
                    buttonx.setText(I18n.get("gui.modernui.preset_s", i + 1));
                    int idx = i;
                    buttonx.setOnClickListener(__ -> this.mColorPicker.setColors(PRESET_COLORS[idx]));
                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-2, -2, 1.0F);
                    buttonGroup.addView(buttonx, p);
                }
                this.mContent.addView(buttonGroup, new LinearLayout.LayoutParams(params));
                this.mContent.addView(this.mColorPicker = new FourColorPicker(this.mParent.getContext(), Config.CLIENT.mTooltipStroke, Config.CLIENT.mTooltipStroke::set, this.mSaveFn), new LinearLayout.LayoutParams(params));
                this.mParent.addView(this.mContent, params);
            }
        }
    }
}