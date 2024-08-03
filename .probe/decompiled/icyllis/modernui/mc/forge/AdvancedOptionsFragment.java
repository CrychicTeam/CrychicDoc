package icyllis.modernui.mc.forge;

import icyllis.arc3d.engine.ResourceCache;
import icyllis.arc3d.opengl.GLCaps;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Clipboard;
import icyllis.modernui.core.Context;
import icyllis.modernui.core.Core;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.graphics.font.GlyphManager;
import icyllis.modernui.graphics.text.LayoutCache;
import icyllis.modernui.mc.ModernUIClient;
import icyllis.modernui.mc.ModernUIMod;
import icyllis.modernui.mc.UIManager;
import icyllis.modernui.mc.text.TextLayoutEngine;
import icyllis.modernui.text.InputFilter;
import icyllis.modernui.text.TextUtils;
import icyllis.modernui.text.method.DigitsInputFilter;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.LayoutInflater;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.Button;
import icyllis.modernui.widget.EditText;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.ScrollView;
import icyllis.modernui.widget.SwitchButton;
import icyllis.modernui.widget.TextView;
import icyllis.modernui.widget.Toast;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.commons.io.output.StringBuilderWriter;

public class AdvancedOptionsFragment extends Fragment {

    private static final Field OPTION_VALUE = ObfuscationReflectionHelper.findField(OptionInstance.class, "f_231481_");

    ViewGroup mContent;

    TextView mUIManagerDump;

    TextView mGPUResourceDump;

    TextView mPSOStatsDump;

    TextView mGPUStatsDump;

    public static Button createDebugButton(Context context, String text) {
        Button button = new Button(context);
        button.setText(text);
        button.setTextSize(14.0F);
        button.setGravity(8388611);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.setMargins(button.dp(6.0F), 0, button.dp(6.0F), 0);
        button.setLayoutParams(params);
        return button;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
        Context context = this.requireContext();
        ScrollView sv = new ScrollView(context);
        sv.addView(this.createPage(context), -1, -2);
        sv.setEdgeEffectColor(-3300456);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(sv.dp(720.0F), -1, 17);
        int dp6 = sv.dp(6.0F);
        params.setMargins(dp6, dp6, dp6, dp6);
        sv.setLayoutParams(params);
        return sv;
    }

    LinearLayout createPage(Context context) {
        LinearLayout content = new LinearLayout(context);
        content.setOrientation(1);
        this.mContent = content;
        int dp6 = content.dp(6.0F);
        LinearLayout category = PreferencesFragment.createCategoryList(context, "Developer");
        if (ModernUIMod.isDeveloperMode()) {
            LinearLayout option = PreferencesFragment.createInputBox(context, "Gamma");
            EditText input = option.requireViewById(16908297);
            input.setText(Minecraft.getInstance().options.gamma().get().toString());
            input.setFilters(new InputFilter[] { DigitsInputFilter.getInstance(input.getTextLocale(), false, true), new InputFilter.LengthFilter(6) });
            input.setOnFocusChangeListener((view, hasFocus) -> {
                if (!hasFocus) {
                    EditText v = (EditText) view;
                    double gamma = Double.parseDouble(v.getText().toString());
                    v.setText(Double.toString(gamma));
                    try {
                        OPTION_VALUE.set(Minecraft.getInstance().options.gamma(), gamma);
                    } catch (Exception var6x) {
                        Minecraft.getInstance().options.gamma().set(gamma);
                    }
                }
            });
            category.addView(option);
            category.addView(PreferencesFragment.createBooleanOption(context, "Remove telemetry session", Config.CLIENT.mRemoveTelemetry, Config.CLIENT::saveAndReloadAsync));
        }
        LinearLayout layout = PreferencesFragment.createSwitchLayout(context, "Show Layout Bounds");
        SwitchButton button = layout.requireViewById(16908313);
        button.setChecked(UIManager.getInstance().isShowingLayoutBounds());
        button.setOnCheckedChangeListener((__, checked) -> UIManager.getInstance().setShowingLayoutBounds(checked));
        category.addView(layout);
        Button buttonx = createDebugButton(context, "Take UI screenshot (Y)");
        buttonx.setOnClickListener(__ -> Core.executeOnRenderThread(() -> UIManager.getInstance().takeScreenshot()));
        category.addView(buttonx);
        Button buttonxx = createDebugButton(context, "Dump UI manager (P)");
        buttonxx.setOnClickListener(__ -> Core.executeOnMainThread(() -> UIManager.getInstance().dump()));
        category.addView(buttonxx);
        Button buttonxxx = createDebugButton(context, "Dump font atlases (G)");
        buttonxxx.setOnClickListener(__ -> Core.executeOnMainThread(() -> GlyphManager.getInstance().debug()));
        category.addView(buttonxxx);
        if (ModernUIMod.isTextEngineEnabled()) {
            Button buttonxxxx = createDebugButton(context, "Dump bitmap fonts (V)");
            buttonxxxx.setOnClickListener(__ -> Core.executeOnMainThread(() -> TextLayoutEngine.getInstance().dumpBitmapFonts()));
            category.addView(buttonxxxx);
            Button buttonxxxxx = createDebugButton(context, "Reload text layout (MC)");
            buttonxxxxx.setOnClickListener(__ -> Core.executeOnMainThread(() -> TextLayoutEngine.getInstance().reload()));
            category.addView(buttonxxxxx);
        }
        Button buttonxxxx = createDebugButton(context, "Reload glyph manager");
        buttonxxxx.setOnClickListener(__ -> Core.executeOnMainThread(() -> GlyphManager.getInstance().reload()));
        category.addView(buttonxxxx);
        Button buttonxxxxx = createDebugButton(context, "Reset layout cache");
        buttonxxxxx.setOnClickListener(__ -> Core.executeOnMainThread(LayoutCache::clear));
        category.addView(buttonxxxxx);
        Button buttonxxxxxx = createDebugButton(context, "Reload full text engine");
        buttonxxxxxx.setOnClickListener(__ -> ModernUIClient.getInstance().reloadFontStrike());
        category.addView(buttonxxxxxx);
        Button buttonxxxxxxx = createDebugButton(context, "GC (F)");
        buttonxxxxxxx.setOnClickListener(__ -> System.gc());
        category.addView(buttonxxxxxxx);
        Button buttonxxxxxxxx = createDebugButton(context, "Copy this page to clipboard");
        buttonxxxxxxxx.setOnClickListener(__ -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < content.getChildCount(); i++) {
                if (content.getChildAt(i) instanceof TextView tvx) {
                    sb.append(tvx.getText());
                    sb.append('\n');
                }
            }
            if (!sb.isEmpty()) {
                sb.deleteCharAt(sb.length() - 1);
            }
            Core.executeOnMainThread(() -> Clipboard.setText(sb));
            Toast.makeText(context, "Copied to clipboard", 0).show();
        });
        category.addView(buttonxxxxxxxx);
        content.addView(category);
        TextView tv = new TextView(context);
        tv.setTextSize(12.0F);
        tv.setPadding(dp6, dp6, dp6, dp6);
        this.mUIManagerDump = tv;
        content.addView(tv);
        TextView tvx = new TextView(context);
        tvx.setTextSize(12.0F);
        tvx.setPadding(dp6, dp6, dp6, dp6);
        tvx.setText("Rendering pipeline: Arc 3D OpenGL");
        content.addView(tvx);
        TextView tvxx = new TextView(context);
        tvxx.setTextSize(12.0F);
        tvxx.setPadding(dp6, dp6, dp6, dp6);
        tvxx.setText("Rectangle packing algorithm: Skyline (silhouette)");
        content.addView(tvxx);
        TextView tvxxx = new TextView(context);
        tvxxx.setTextSize(12.0F);
        tvxxx.setPadding(dp6, dp6, dp6, dp6);
        this.mGPUResourceDump = tvxxx;
        content.addView(tvxxx);
        TextView tvxxxx = new TextView(context);
        tvxxxx.setTextSize(12.0F);
        tvxxxx.setPadding(dp6, dp6, dp6, dp6);
        this.mPSOStatsDump = tvxxxx;
        content.addView(tvxxxx);
        TextView tvxxxxx = new TextView(context);
        tvxxxxx.setTextSize(12.0F);
        tvxxxxx.setPadding(dp6, dp6, dp6, dp6);
        this.mGPUStatsDump = tvxxxxx;
        content.addView(tvxxxxx);
        TextView tvxxxxxx = new TextView(context);
        tvxxxxxx.setTextSize(12.0F);
        tvxxxxxx.setPadding(dp6, dp6, dp6, dp6);
        Core.executeOnRenderThread(() -> {
            GLCaps caps = (GLCaps) Core.requireDirectContext().getCaps();
            String s = caps.toString(false);
            tv.post(() -> tv.setText(s));
        });
        content.addView(tvxxxxxx);
        this.refreshPage();
        return content;
    }

    void refreshPage() {
        if (this.mUIManagerDump != null) {
            Core.executeOnRenderThread(() -> {
                StringBuilder builder = new StringBuilder();
                PrintWriter w = new PrintWriter(new StringBuilderWriter(builder));
                try {
                    UIManager.getInstance().dump(w, false);
                } catch (Throwable var6) {
                    try {
                        w.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                    throw var6;
                }
                w.close();
                String var7 = builder.toString();
                this.mUIManagerDump.post(() -> this.mUIManagerDump.setText(var7));
            });
        }
        if (this.mGPUResourceDump != null) {
            Core.executeOnRenderThread(() -> {
                ResourceCache rc = Core.requireDirectContext().getResourceCache();
                String s = "GPU Resource Cache:\n" + String.format("Resource bytes: %s (%s bytes)", TextUtils.binaryCompact(rc.getResourceBytes()), rc.getResourceBytes()) + "\n" + String.format("Budgeted resource bytes: %s (%s bytes)", TextUtils.binaryCompact(rc.getBudgetedResourceBytes()), rc.getBudgetedResourceBytes()) + "\n" + String.format("Resource count: %s", rc.getResourceCount()) + "\n" + String.format("Budgeted resource count: %s", rc.getBudgetedResourceCount()) + "\n" + String.format("Free resource bytes: %s (%s bytes)", TextUtils.binaryCompact(rc.getFreeResourceBytes()), rc.getFreeResourceBytes()) + "\n" + String.format("Max resource bytes: %s (%s bytes)", TextUtils.binaryCompact(rc.getMaxResourceBytes()), rc.getMaxResourceBytes());
                this.mGPUResourceDump.post(() -> this.mGPUResourceDump.setText(s));
            });
        }
        if (this.mPSOStatsDump != null) {
            this.mPSOStatsDump.setText(Core.requireUiRecordingContext().getPipelineStateCache().getStats().toString());
        }
        if (this.mGPUStatsDump != null) {
            Core.executeOnRenderThread(() -> {
                String s = Core.requireDirectContext().getDevice().getStats().toString();
                this.mGPUStatsDump.post(() -> this.mGPUStatsDump.setText(s));
            });
        }
        if (this.mContent != null) {
            this.mContent.postDelayed(this::refreshPage, 10000L);
        }
    }
}