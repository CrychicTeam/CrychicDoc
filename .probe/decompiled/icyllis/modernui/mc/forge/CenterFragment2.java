package icyllis.modernui.mc.forge;

import icyllis.modernui.TestFragment;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Context;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.fragment.FragmentContainerView;
import icyllis.modernui.fragment.FragmentManager;
import icyllis.modernui.fragment.FragmentTransaction;
import icyllis.modernui.mc.DashboardFragment;
import icyllis.modernui.mc.MarkdownFragment;
import icyllis.modernui.mc.ModernUIMod;
import icyllis.modernui.mc.MusicFragment;
import icyllis.modernui.mc.ui.ThemeControl;
import icyllis.modernui.util.ColorStateList;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.util.StateSet;
import icyllis.modernui.view.LayoutInflater;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.RadioButton;
import icyllis.modernui.widget.RadioGroup;
import icyllis.modernui.widget.TextView;
import net.minecraft.client.resources.language.I18n;

public class CenterFragment2 extends Fragment {

    private static final int id_tab_container = 8194;

    private static final ColorStateList NAV_BUTTON_COLOR = new ColorStateList(new int[][] { { 16842912 }, StateSet.get(64), StateSet.WILD_CARD }, new int[] { -1, -2039584, -4934476 });

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.getParentFragmentManager().beginTransaction().setPrimaryNavigationFragment(this).commit();
    }

    @Override
    public void onCreate(@Nullable DataSet savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentTransaction ft = this.getChildFragmentManager().beginTransaction();
        DataSet args = this.getArguments();
        if (args != null && args.getBoolean("navigateToPreferences")) {
            ft.replace(8194, PreferencesFragment.class, null, "preferences");
        } else {
            ft.replace(8194, DashboardFragment.class, null, "dashboard");
        }
        ft.setTransition(4097).setReorderingAllowed(true).commit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
        LinearLayout base = new LinearLayout(this.getContext());
        base.setOrientation(1);
        base.setShowDividers(2);
        base.setDividerDrawable(ThemeControl.makeDivider(base));
        base.setDividerPadding(base.dp(8.0F));
        TextView title = new TextView(this.getContext());
        title.setId(16908310);
        title.setText(I18n.get("modernui.center.title"));
        title.setTextSize(22.0F);
        title.setTextStyle(1);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
        params.gravity = 17;
        params.setMarginsRelative(0, base.dp(12.0F), 0, base.dp(12.0F));
        base.addView(title, params);
        RadioGroup buttonGroup = new RadioGroup(this.getContext());
        buttonGroup.setOrientation(0);
        buttonGroup.setHorizontalGravity(1);
        buttonGroup.addView(this.createNavButton(1001, "modernui.center.tab.dashboard"));
        buttonGroup.addView(this.createNavButton(1002, "modernui.center.tab.preferences"));
        if (ModernUIMod.isDeveloperMode()) {
            buttonGroup.addView(this.createNavButton(1003, "modernui.center.tab.developerOptions"));
        }
        buttonGroup.addView(this.createNavButton(1004, "soundCategory.music"));
        if (ModernUIMod.isDeveloperMode()) {
            buttonGroup.addView(this.createNavButton(1005, "Dev"));
        }
        buttonGroup.addView(this.createNavButton(1006, "Markdown"));
        DataSet args = this.getArguments();
        buttonGroup.check(args != null && args.getBoolean("navigateToPreferences") ? 1002 : 1001);
        buttonGroup.setOnCheckedChangeListener((group, checkedId) -> {
            FragmentManager fm = this.getChildFragmentManager();
            FragmentTransaction ft = null;
            switch(checkedId) {
                case 1001:
                    ft = fm.beginTransaction().replace(8194, DashboardFragment.class, null, "dashboard");
                    break;
                case 1002:
                    ft = fm.beginTransaction().replace(8194, PreferencesFragment.class, null, "preferences");
                    break;
                case 1003:
                    ft = fm.beginTransaction().replace(8194, AdvancedOptionsFragment.class, null, "developerOptions");
                    break;
                case 1004:
                    ft = fm.beginTransaction().replace(8194, MusicFragment.class, null, "music");
                    break;
                case 1005:
                    ft = fm.beginTransaction().replace(8194, TestFragment.class, null, "dev");
                    break;
                case 1006:
                    ft = fm.beginTransaction().replace(8194, MarkdownFragment.class, null, "markdown");
            }
            if (ft != null) {
                ft.setTransition(4097).setReorderingAllowed(true).commit();
            }
        });
        LinearLayout.LayoutParams paramsx = new LinearLayout.LayoutParams(-2, -2);
        paramsx.gravity = 17;
        base.addView(buttonGroup, paramsx);
        FragmentContainerView tabContainer = new FragmentContainerView(this.getContext());
        tabContainer.setId(8194);
        params = new LinearLayout.LayoutParams(-1, -1);
        base.addView(tabContainer, params);
        FrameLayout.LayoutParams paramsxx = new FrameLayout.LayoutParams(-1, -1);
        base.setLayoutParams(paramsxx);
        return base;
    }

    private RadioButton createNavButton(int id, String text) {
        RadioButton button = new RadioButton(this.getContext());
        button.setId(id);
        button.setText(I18n.get(text));
        button.setTextSize(16.0F);
        button.setTextColor(NAV_BUTTON_COLOR);
        int dp6 = button.dp(6.0F);
        button.setPadding(dp6, 0, dp6, 0);
        ThemeControl.addBackground(button);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
        params.setMarginsRelative(dp6 * 3, dp6, dp6 * 3, dp6);
        button.setLayoutParams(params);
        return button;
    }
}