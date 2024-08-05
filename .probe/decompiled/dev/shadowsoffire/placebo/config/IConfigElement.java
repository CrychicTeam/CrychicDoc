package dev.shadowsoffire.placebo.config;

import java.util.List;
import java.util.regex.Pattern;

public interface IConfigElement {

    boolean isProperty();

    String getName();

    String getQualifiedName();

    String getLanguageKey();

    String getComment();

    List<IConfigElement> getChildElements();

    ConfigGuiType getType();

    boolean isList();

    boolean isListLengthFixed();

    int getMaxListLength();

    boolean isDefault();

    Object getDefault();

    Object[] getDefaults();

    void setToDefault();

    boolean requiresWorldRestart();

    boolean showInGui();

    boolean requiresMcRestart();

    Object get();

    Object[] getList();

    void set(Object var1);

    void set(Object[] var1);

    String[] getValidValues();

    String[] getValidValuesDisplay();

    Object getMinValue();

    Object getMaxValue();

    Pattern getValidationPattern();

    default boolean hasSlidingControl() {
        return false;
    }
}