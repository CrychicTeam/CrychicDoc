package icyllis.modernui.mc;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.markdown.Markdown;
import icyllis.modernui.markdown.MarkdownPlugin;
import icyllis.modernui.markdown.MarkdownTheme;
import icyllis.modernui.markdown.core.CorePlugin;
import icyllis.modernui.text.Editable;
import icyllis.modernui.text.TextWatcher;
import icyllis.modernui.text.Typeface;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.LayoutInflater;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.EditText;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.TextView;

public class MarkdownFragment extends Fragment {

    private Markdown mMarkdown;

    private EditText mInput;

    private TextView mPreview;

    private final Runnable mRenderMarkdown = () -> this.mMarkdown.setMarkdown(this.mPreview, this.mInput.getText().toString());

    @Override
    public void onCreate(DataSet savedInstanceState) {
        super.onCreate(savedInstanceState);
        Markdown.Builder builder = Markdown.builder(this.requireContext()).usePlugin(CorePlugin.create());
        final Typeface monoFont = Typeface.getSystemFont("JetBrains Mono Medium");
        if (monoFont != Typeface.SANS_SERIF) {
            builder.usePlugin(new MarkdownPlugin() {

                @Override
                public void configureTheme(@NonNull MarkdownTheme.Builder builder) {
                    builder.setCodeTypeface(monoFont);
                }
            });
        }
        this.mMarkdown = builder.setBufferType(TextView.BufferType.EDITABLE).build();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
        LinearLayout layout = new LinearLayout(this.requireContext());
        layout.setOrientation(0);
        EditText input = this.mInput = new EditText(this.requireContext());
        int dp6 = input.dp(6.0F);
        input.setPadding(dp6, dp6, dp6, dp6);
        input.setTextDirection(6);
        input.setTextAlignment(2);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -1, 1.0F);
        layout.addView(input, params);
        TextView preview = this.mPreview = new TextView(this.requireContext());
        dp6 = preview.dp(6.0F);
        preview.setPadding(dp6, dp6, dp6, dp6);
        preview.setTextDirection(6);
        preview.setTextAlignment(2);
        preview.setTextIsSelectable(true);
        params = new LinearLayout.LayoutParams(-1, -1, 1.0F);
        layout.addView(preview, params);
        this.mInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                MarkdownFragment.this.mPreview.removeCallbacks(MarkdownFragment.this.mRenderMarkdown);
                MarkdownFragment.this.mPreview.postDelayed(MarkdownFragment.this.mRenderMarkdown, 600L);
            }
        });
        this.mInput.setText("Modern UI Markdown\n---\nMy **First** Line\n> My *Second* Line\n* One\n  * ```java\n    public static void main(String[] args) {\n        System.out.println(\"Hello, Modern UI!\");\n    }\n    ```\n  * Three\n    * Four\n\n1. One\n2. Two\n3. Three\n# Heading 1\n## Heading 2 \ud83d\udc4b\n### Heading 3 \ud83e\udd14\n\nAAA AAA\n******\nBBB BBB\n");
        return layout;
    }
}