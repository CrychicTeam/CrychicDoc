package icyllis.modernui.text;

public interface TextWatcher extends NoCopySpan {

    void beforeTextChanged(CharSequence var1, int var2, int var3, int var4);

    void onTextChanged(CharSequence var1, int var2, int var3, int var4);

    void afterTextChanged(Editable var1);
}