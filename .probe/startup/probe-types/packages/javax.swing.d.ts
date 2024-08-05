declare module "packages/javax/swing/$Icon" {
import {$Graphics, $Graphics$Type} from "packages/java/awt/$Graphics"
import {$Component, $Component$Type} from "packages/java/awt/$Component"

export interface $Icon {

 "paintIcon"(arg0: $Component$Type, arg1: $Graphics$Type, arg2: integer, arg3: integer): void
 "getIconHeight"(): integer
 "getIconWidth"(): integer
}

export namespace $Icon {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Icon$Type = ($Icon);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Icon_ = $Icon$Type;
}}
declare module "packages/javax/swing/text/$Caret" {
import {$JTextComponent, $JTextComponent$Type} from "packages/javax/swing/text/$JTextComponent"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$Graphics, $Graphics$Type} from "packages/java/awt/$Graphics"
import {$ChangeListener, $ChangeListener$Type} from "packages/javax/swing/event/$ChangeListener"

export interface $Caret {

 "install"(arg0: $JTextComponent$Type): void
 "getDot"(): integer
 "setVisible"(arg0: boolean): void
 "isVisible"(): boolean
 "deinstall"(arg0: $JTextComponent$Type): void
 "setDot"(arg0: integer): void
 "moveDot"(arg0: integer): void
 "paint"(arg0: $Graphics$Type): void
 "removeChangeListener"(arg0: $ChangeListener$Type): void
 "getMark"(): integer
 "addChangeListener"(arg0: $ChangeListener$Type): void
 "getBlinkRate"(): integer
 "setBlinkRate"(arg0: integer): void
 "isSelectionVisible"(): boolean
 "setSelectionVisible"(arg0: boolean): void
 "setMagicCaretPosition"(arg0: $Point$Type): void
 "getMagicCaretPosition"(): $Point
}

export namespace $Caret {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Caret$Type = ($Caret);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Caret_ = $Caret$Type;
}}
declare module "packages/javax/swing/event/$DocumentEvent" {
import {$DocumentEvent$EventType, $DocumentEvent$EventType$Type} from "packages/javax/swing/event/$DocumentEvent$EventType"
import {$Element, $Element$Type} from "packages/javax/swing/text/$Element"
import {$Document, $Document$Type} from "packages/javax/swing/text/$Document"
import {$DocumentEvent$ElementChange, $DocumentEvent$ElementChange$Type} from "packages/javax/swing/event/$DocumentEvent$ElementChange"

export interface $DocumentEvent {

 "getLength"(): integer
 "getType"(): $DocumentEvent$EventType
 "getOffset"(): integer
 "getChange"(arg0: $Element$Type): $DocumentEvent$ElementChange
 "getDocument"(): $Document
}

export namespace $DocumentEvent {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DocumentEvent$Type = ($DocumentEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DocumentEvent_ = $DocumentEvent$Type;
}}
declare module "packages/javax/swing/$DropMode" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $DropMode extends $Enum<($DropMode)> {
static readonly "USE_SELECTION": $DropMode
static readonly "ON": $DropMode
static readonly "INSERT": $DropMode
static readonly "INSERT_ROWS": $DropMode
static readonly "INSERT_COLS": $DropMode
static readonly "ON_OR_INSERT": $DropMode
static readonly "ON_OR_INSERT_ROWS": $DropMode
static readonly "ON_OR_INSERT_COLS": $DropMode


public static "values"(): ($DropMode)[]
public static "valueOf"(arg0: string): $DropMode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DropMode$Type = (("insert_rows") | ("use_selection") | ("insert") | ("on_or_insert") | ("on_or_insert_rows") | ("insert_cols") | ("on_or_insert_cols") | ("on")) | ($DropMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DropMode_ = $DropMode$Type;
}}
declare module "packages/javax/swing/event/$PopupMenuEvent" {
import {$EventObject, $EventObject$Type} from "packages/java/util/$EventObject"

export class $PopupMenuEvent extends $EventObject {

constructor(arg0: any)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PopupMenuEvent$Type = ($PopupMenuEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PopupMenuEvent_ = $PopupMenuEvent$Type;
}}
declare module "packages/javax/swing/event/$MenuKeyListener" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$MenuKeyEvent, $MenuKeyEvent$Type} from "packages/javax/swing/event/$MenuKeyEvent"

export interface $MenuKeyListener extends $EventListener {

 "menuKeyPressed"(arg0: $MenuKeyEvent$Type): void
 "menuKeyReleased"(arg0: $MenuKeyEvent$Type): void
 "menuKeyTyped"(arg0: $MenuKeyEvent$Type): void
}

export namespace $MenuKeyListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuKeyListener$Type = ($MenuKeyListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuKeyListener_ = $MenuKeyListener$Type;
}}
declare module "packages/javax/swing/plaf/$PopupMenuUI" {
import {$JPopupMenu, $JPopupMenu$Type} from "packages/javax/swing/$JPopupMenu"
import {$Popup, $Popup$Type} from "packages/javax/swing/$Popup"
import {$ComponentUI, $ComponentUI$Type} from "packages/javax/swing/plaf/$ComponentUI"
import {$MouseEvent, $MouseEvent$Type} from "packages/java/awt/event/$MouseEvent"

export class $PopupMenuUI extends $ComponentUI {


public "isPopupTrigger"(arg0: $MouseEvent$Type): boolean
public "getPopup"(arg0: $JPopupMenu$Type, arg1: integer, arg2: integer): $Popup
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PopupMenuUI$Type = ($PopupMenuUI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PopupMenuUI_ = $PopupMenuUI$Type;
}}
declare module "packages/javax/swing/text/$AttributeSet" {
import {$Enumeration, $Enumeration$Type} from "packages/java/util/$Enumeration"

export interface $AttributeSet {

 "isDefined"(arg0: any): boolean
 "getAttribute"(arg0: any): any
 "isEqual"(arg0: $AttributeSet$Type): boolean
 "getAttributeCount"(): integer
 "copyAttributes"(): $AttributeSet
 "containsAttribute"(arg0: any, arg1: any): boolean
 "getResolveParent"(): $AttributeSet
 "containsAttributes"(arg0: $AttributeSet$Type): boolean
 "getAttributeNames"(): $Enumeration<(any)>
}

export namespace $AttributeSet {
const NameAttribute: any
const ResolveAttribute: any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AttributeSet$Type = ($AttributeSet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AttributeSet_ = $AttributeSet$Type;
}}
declare module "packages/javax/swing/event/$HyperlinkEvent$EventType" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $HyperlinkEvent$EventType {
static readonly "ENTERED": $HyperlinkEvent$EventType
static readonly "EXITED": $HyperlinkEvent$EventType
static readonly "ACTIVATED": $HyperlinkEvent$EventType


public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HyperlinkEvent$EventType$Type = ($HyperlinkEvent$EventType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HyperlinkEvent$EventType_ = $HyperlinkEvent$EventType$Type;
}}
declare module "packages/javax/swing/$MenuSelectionManager" {
import {$MouseEvent, $MouseEvent$Type} from "packages/java/awt/event/$MouseEvent"
import {$KeyEvent, $KeyEvent$Type} from "packages/java/awt/event/$KeyEvent"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$ChangeListener, $ChangeListener$Type} from "packages/javax/swing/event/$ChangeListener"
import {$MenuElement, $MenuElement$Type} from "packages/javax/swing/$MenuElement"
import {$Component, $Component$Type} from "packages/java/awt/$Component"

export class $MenuSelectionManager {

constructor()

public "getSelectedPath"(): ($MenuElement)[]
public "isComponentPartOfCurrentMenu"(arg0: $Component$Type): boolean
public "componentForPoint"(arg0: $Component$Type, arg1: $Point$Type): $Component
public "processKeyEvent"(arg0: $KeyEvent$Type): void
public "processMouseEvent"(arg0: $MouseEvent$Type): void
public static "defaultManager"(): $MenuSelectionManager
public "setSelectedPath"(arg0: ($MenuElement$Type)[]): void
public "clearSelectedPath"(): void
public "getChangeListeners"(): ($ChangeListener)[]
public "removeChangeListener"(arg0: $ChangeListener$Type): void
public "addChangeListener"(arg0: $ChangeListener$Type): void
get "selectedPath"(): ($MenuElement)[]
set "selectedPath"(value: ($MenuElement$Type)[])
get "changeListeners"(): ($ChangeListener)[]
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuSelectionManager$Type = ($MenuSelectionManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuSelectionManager_ = $MenuSelectionManager$Type;
}}
declare module "packages/javax/swing/$JPopupMenu" {
import {$MenuSelectionManager, $MenuSelectionManager$Type} from "packages/javax/swing/$MenuSelectionManager"
import {$MenuKeyListener, $MenuKeyListener$Type} from "packages/javax/swing/event/$MenuKeyListener"
import {$SingleSelectionModel, $SingleSelectionModel$Type} from "packages/javax/swing/$SingleSelectionModel"
import {$JComponent, $JComponent$Type} from "packages/javax/swing/$JComponent"
import {$PopupMenuListener, $PopupMenuListener$Type} from "packages/javax/swing/event/$PopupMenuListener"
import {$Accessible, $Accessible$Type} from "packages/javax/accessibility/$Accessible"
import {$Action, $Action$Type} from "packages/javax/swing/$Action"
import {$Insets, $Insets$Type} from "packages/java/awt/$Insets"
import {$AccessibleContext, $AccessibleContext$Type} from "packages/javax/accessibility/$AccessibleContext"
import {$PopupMenuUI, $PopupMenuUI$Type} from "packages/javax/swing/plaf/$PopupMenuUI"
import {$Component, $Component$Type} from "packages/java/awt/$Component"
import {$MouseEvent, $MouseEvent$Type} from "packages/java/awt/event/$MouseEvent"
import {$JMenuItem, $JMenuItem$Type} from "packages/javax/swing/$JMenuItem"
import {$KeyEvent, $KeyEvent$Type} from "packages/java/awt/event/$KeyEvent"
import {$Dimension, $Dimension$Type} from "packages/java/awt/$Dimension"
import {$MenuElement, $MenuElement$Type} from "packages/javax/swing/$MenuElement"

export class $JPopupMenu extends $JComponent implements $Accessible, $MenuElement {
static readonly "WHEN_FOCUSED": integer
static readonly "WHEN_ANCESTOR_OF_FOCUSED_COMPONENT": integer
static readonly "WHEN_IN_FOCUSED_WINDOW": integer
static readonly "UNDEFINED_CONDITION": integer
static readonly "TOOL_TIP_TEXT_KEY": string
static readonly "TOP_ALIGNMENT": float
static readonly "CENTER_ALIGNMENT": float
static readonly "BOTTOM_ALIGNMENT": float
static readonly "LEFT_ALIGNMENT": float
static readonly "RIGHT_ALIGNMENT": float

constructor(arg0: string)
constructor()

public "add"(arg0: $JMenuItem$Type): $JMenuItem
public "add"(arg0: string): $JMenuItem
public "add"(arg0: $Action$Type): $JMenuItem
public "remove"(arg0: integer): void
public "insert"(arg0: $Component$Type, arg1: integer): void
public "insert"(arg0: $Action$Type, arg1: integer): void
public "pack"(): void
public "getMargin"(): $Insets
public "setVisible"(arg0: boolean): void
public "addSeparator"(): void
public "isVisible"(): boolean
public "getComponent"(): $Component
public "processKeyEvent"(arg0: $KeyEvent$Type, arg1: ($MenuElement$Type)[], arg2: $MenuSelectionManager$Type): void
public "isPopupTrigger"(arg0: $MouseEvent$Type): boolean
public "processMouseEvent"(arg0: $MouseEvent$Type, arg1: ($MenuElement$Type)[], arg2: $MenuSelectionManager$Type): void
public "setLocation"(arg0: integer, arg1: integer): void
public "setLabel"(arg0: string): void
public "getAccessibleContext"(): $AccessibleContext
public static "getDefaultLightWeightPopupEnabled"(): boolean
public "isLightWeightPopupEnabled"(): boolean
public "setInvoker"(arg0: $Component$Type): void
public "setPopupSize"(arg0: integer, arg1: integer): void
public "setPopupSize"(arg0: $Dimension$Type): void
public static "setDefaultLightWeightPopupEnabled"(arg0: boolean): void
public "setLightWeightPopupEnabled"(arg0: boolean): void
public "addPopupMenuListener"(arg0: $PopupMenuListener$Type): void
public "removePopupMenuListener"(arg0: $PopupMenuListener$Type): void
public "getPopupMenuListeners"(): ($PopupMenuListener)[]
public "addMenuKeyListener"(arg0: $MenuKeyListener$Type): void
public "removeMenuKeyListener"(arg0: $MenuKeyListener$Type): void
public "getMenuKeyListeners"(): ($MenuKeyListener)[]
public "show"(arg0: $Component$Type, arg1: integer, arg2: integer): void
public "setSelected"(arg0: $Component$Type): void
public "getLabel"(): string
public "getInvoker"(): $Component
public "updateUI"(): void
public "setSelectionModel"(arg0: $SingleSelectionModel$Type): void
public "setUI"(arg0: $PopupMenuUI$Type): void
public "getUI"(): $PopupMenuUI
/**
 * 
 * @deprecated
 */
public "getComponentAtIndex"(arg0: integer): $Component
public "getSelectionModel"(): $SingleSelectionModel
public "getComponentIndex"(arg0: $Component$Type): integer
public "isBorderPainted"(): boolean
public "getSubElements"(): ($MenuElement)[]
public "getUIClassID"(): string
public "setBorderPainted"(arg0: boolean): void
public "menuSelectionChanged"(arg0: boolean): void
get "margin"(): $Insets
set "visible"(value: boolean)
get "visible"(): boolean
get "component"(): $Component
set "label"(value: string)
get "accessibleContext"(): $AccessibleContext
get "defaultLightWeightPopupEnabled"(): boolean
get "lightWeightPopupEnabled"(): boolean
set "invoker"(value: $Component$Type)
set "popupSize"(value: $Dimension$Type)
set "defaultLightWeightPopupEnabled"(value: boolean)
set "lightWeightPopupEnabled"(value: boolean)
get "popupMenuListeners"(): ($PopupMenuListener)[]
get "menuKeyListeners"(): ($MenuKeyListener)[]
set "selected"(value: $Component$Type)
get "label"(): string
get "invoker"(): $Component
set "selectionModel"(value: $SingleSelectionModel$Type)
set "uI"(value: $PopupMenuUI$Type)
get "uI"(): $PopupMenuUI
get "selectionModel"(): $SingleSelectionModel
get "borderPainted"(): boolean
get "subElements"(): ($MenuElement)[]
get "uIClassID"(): string
set "borderPainted"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JPopupMenu$Type = ($JPopupMenu);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JPopupMenu_ = $JPopupMenu$Type;
}}
declare module "packages/javax/swing/event/$MenuEvent" {
import {$EventObject, $EventObject$Type} from "packages/java/util/$EventObject"

export class $MenuEvent extends $EventObject {

constructor(arg0: any)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuEvent$Type = ($MenuEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuEvent_ = $MenuEvent$Type;
}}
declare module "packages/javax/swing/$JLayeredPane" {
import {$JComponent, $JComponent$Type} from "packages/javax/swing/$JComponent"
import {$Accessible, $Accessible$Type} from "packages/javax/accessibility/$Accessible"
import {$AccessibleContext, $AccessibleContext$Type} from "packages/javax/accessibility/$AccessibleContext"
import {$Graphics, $Graphics$Type} from "packages/java/awt/$Graphics"
import {$Component, $Component$Type} from "packages/java/awt/$Component"

export class $JLayeredPane extends $JComponent implements $Accessible {
static readonly "DEFAULT_LAYER": integer
static readonly "PALETTE_LAYER": integer
static readonly "MODAL_LAYER": integer
static readonly "POPUP_LAYER": integer
static readonly "DRAG_LAYER": integer
static readonly "FRAME_CONTENT_LAYER": integer
static readonly "LAYER_PROPERTY": string
static readonly "WHEN_FOCUSED": integer
static readonly "WHEN_ANCESTOR_OF_FOCUSED_COMPONENT": integer
static readonly "WHEN_IN_FOCUSED_WINDOW": integer
static readonly "UNDEFINED_CONDITION": integer
static readonly "TOOL_TIP_TEXT_KEY": string
static readonly "TOP_ALIGNMENT": float
static readonly "CENTER_ALIGNMENT": float
static readonly "BOTTOM_ALIGNMENT": float
static readonly "LEFT_ALIGNMENT": float
static readonly "RIGHT_ALIGNMENT": float

constructor()

public "remove"(arg0: integer): void
public "getLayer"(arg0: $Component$Type): integer
public static "getLayer"(arg0: $JComponent$Type): integer
public "removeAll"(): void
public "getPosition"(arg0: $Component$Type): integer
public "moveToFront"(arg0: $Component$Type): void
public "getAccessibleContext"(): $AccessibleContext
public "paint"(arg0: $Graphics$Type): void
public "setPosition"(arg0: $Component$Type, arg1: integer): void
public "moveToBack"(arg0: $Component$Type): void
public "setLayer"(arg0: $Component$Type, arg1: integer, arg2: integer): void
public "setLayer"(arg0: $Component$Type, arg1: integer): void
public "getComponentCountInLayer"(arg0: integer): integer
public static "putLayer"(arg0: $JComponent$Type, arg1: integer): void
public static "getLayeredPaneAbove"(arg0: $Component$Type): $JLayeredPane
public "highestLayer"(): integer
public "lowestLayer"(): integer
public "getComponentsInLayer"(arg0: integer): ($Component)[]
public "getIndexOf"(arg0: $Component$Type): integer
public "isOptimizedDrawingEnabled"(): boolean
get "accessibleContext"(): $AccessibleContext
get "optimizedDrawingEnabled"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JLayeredPane$Type = ($JLayeredPane);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JLayeredPane_ = $JLayeredPane$Type;
}}
declare module "packages/javax/swing/text/$Segment" {
import {$CharacterIterator, $CharacterIterator$Type} from "packages/java/text/$CharacterIterator"
import {$IntStream, $IntStream$Type} from "packages/java/util/stream/$IntStream"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"

export class $Segment implements $Cloneable, $CharacterIterator, charseq {
 "array": (character)[]
 "offset": integer
 "count": integer

constructor()
constructor(arg0: (character)[], arg1: integer, arg2: integer)

public "length"(): integer
public "toString"(): string
public "clone"(): any
public "charAt"(arg0: integer): character
public "next"(): character
public "subSequence"(arg0: integer, arg1: integer): charseq
public "last"(): character
public "first"(): character
public "current"(): character
public "previous"(): character
public "getIndex"(): integer
public "getBeginIndex"(): integer
public "getEndIndex"(): integer
public "setIndex"(arg0: integer): character
public "setPartialReturn"(arg0: boolean): void
public "isPartialReturn"(): boolean
public static "compare"(arg0: charseq, arg1: charseq): integer
public "isEmpty"(): boolean
public "codePoints"(): $IntStream
public "chars"(): $IntStream
get "index"(): integer
get "beginIndex"(): integer
get "endIndex"(): integer
set "index"(value: integer)
set "partialReturn"(value: boolean)
get "partialReturn"(): boolean
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Segment$Type = ($Segment);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Segment_ = $Segment$Type;
}}
declare module "packages/javax/swing/$AbstractButton" {
import {$ItemSelectable, $ItemSelectable$Type} from "packages/java/awt/$ItemSelectable"
import {$ButtonModel, $ButtonModel$Type} from "packages/javax/swing/$ButtonModel"
import {$ButtonUI, $ButtonUI$Type} from "packages/javax/swing/plaf/$ButtonUI"
import {$JComponent, $JComponent$Type} from "packages/javax/swing/$JComponent"
import {$LayoutManager, $LayoutManager$Type} from "packages/java/awt/$LayoutManager"
import {$Insets, $Insets$Type} from "packages/java/awt/$Insets"
import {$Action, $Action$Type} from "packages/javax/swing/$Action"
import {$SwingConstants, $SwingConstants$Type} from "packages/javax/swing/$SwingConstants"
import {$ActionListener, $ActionListener$Type} from "packages/java/awt/event/$ActionListener"
import {$Icon, $Icon$Type} from "packages/javax/swing/$Icon"
import {$ItemListener, $ItemListener$Type} from "packages/java/awt/event/$ItemListener"
import {$ChangeListener, $ChangeListener$Type} from "packages/javax/swing/event/$ChangeListener"
import {$Image, $Image$Type} from "packages/java/awt/$Image"

export class $AbstractButton extends $JComponent implements $ItemSelectable, $SwingConstants {
static readonly "MODEL_CHANGED_PROPERTY": string
static readonly "TEXT_CHANGED_PROPERTY": string
static readonly "MNEMONIC_CHANGED_PROPERTY": string
static readonly "MARGIN_CHANGED_PROPERTY": string
static readonly "VERTICAL_ALIGNMENT_CHANGED_PROPERTY": string
static readonly "HORIZONTAL_ALIGNMENT_CHANGED_PROPERTY": string
static readonly "VERTICAL_TEXT_POSITION_CHANGED_PROPERTY": string
static readonly "HORIZONTAL_TEXT_POSITION_CHANGED_PROPERTY": string
static readonly "BORDER_PAINTED_CHANGED_PROPERTY": string
static readonly "FOCUS_PAINTED_CHANGED_PROPERTY": string
static readonly "ROLLOVER_ENABLED_CHANGED_PROPERTY": string
static readonly "CONTENT_AREA_FILLED_CHANGED_PROPERTY": string
static readonly "ICON_CHANGED_PROPERTY": string
static readonly "PRESSED_ICON_CHANGED_PROPERTY": string
static readonly "SELECTED_ICON_CHANGED_PROPERTY": string
static readonly "ROLLOVER_ICON_CHANGED_PROPERTY": string
static readonly "ROLLOVER_SELECTED_ICON_CHANGED_PROPERTY": string
static readonly "DISABLED_ICON_CHANGED_PROPERTY": string
static readonly "DISABLED_SELECTED_ICON_CHANGED_PROPERTY": string
static readonly "WHEN_FOCUSED": integer
static readonly "WHEN_ANCESTOR_OF_FOCUSED_COMPONENT": integer
static readonly "WHEN_IN_FOCUSED_WINDOW": integer
static readonly "UNDEFINED_CONDITION": integer
static readonly "TOOL_TIP_TEXT_KEY": string
static readonly "TOP_ALIGNMENT": float
static readonly "CENTER_ALIGNMENT": float
static readonly "BOTTOM_ALIGNMENT": float
static readonly "LEFT_ALIGNMENT": float
static readonly "RIGHT_ALIGNMENT": float


public "getText"(): string
public "setText"(arg0: string): void
public "addActionListener"(arg0: $ActionListener$Type): void
public "removeActionListener"(arg0: $ActionListener$Type): void
public "getActionListeners"(): ($ActionListener)[]
public "setActionCommand"(arg0: string): void
public "getMargin"(): $Insets
public "setEnabled"(arg0: boolean): void
public "getMnemonic"(): integer
public "doClick"(arg0: integer): void
public "doClick"(): void
public "setRolloverEnabled"(arg0: boolean): void
public "getSelectedIcon"(): $Icon
public "isRolloverEnabled"(): boolean
public "getHideActionText"(): boolean
public "setHideActionText"(arg0: boolean): void
public "setDisplayedMnemonicIndex"(arg0: integer): void
public "removeItemListener"(arg0: $ItemListener$Type): void
public "addItemListener"(arg0: $ItemListener$Type): void
public "setDisabledIcon"(arg0: $Icon$Type): void
public "setDisabledSelectedIcon"(arg0: $Icon$Type): void
public "getPressedIcon"(): $Icon
public "getRolloverSelectedIcon"(): $Icon
public "getRolloverIcon"(): $Icon
public "setIconTextGap"(arg0: integer): void
public "setContentAreaFilled"(arg0: boolean): void
public "setPressedIcon"(arg0: $Icon$Type): void
public "setSelectedIcon"(arg0: $Icon$Type): void
public "setRolloverIcon"(arg0: $Icon$Type): void
public "setRolloverSelectedIcon"(arg0: $Icon$Type): void
public "setVerticalAlignment"(arg0: integer): void
public "getVerticalTextPosition"(): integer
public "getHorizontalTextPosition"(): integer
public "getIconTextGap"(): integer
public "isFocusPainted"(): boolean
public "isContentAreaFilled"(): boolean
public "getDisplayedMnemonicIndex"(): integer
public "setMultiClickThreshhold"(arg0: long): void
public "getMultiClickThreshhold"(): long
public "getItemListeners"(): ($ItemListener)[]
public "getSelectedObjects"(): (any)[]
public "setAction"(arg0: $Action$Type): void
public "getModel"(): $ButtonModel
public "isSelected"(): boolean
public "setLayout"(arg0: $LayoutManager$Type): void
public "getIcon"(): $Icon
public "setMnemonic"(arg0: character): void
public "setMnemonic"(arg0: integer): void
public "setFocusPainted"(arg0: boolean): void
public "removeNotify"(): void
public "getAction"(): $Action
/**
 * 
 * @deprecated
 */
public "setLabel"(arg0: string): void
public "getActionCommand"(): string
public "setHorizontalTextPosition"(arg0: integer): void
public "setVerticalTextPosition"(arg0: integer): void
public "getChangeListeners"(): ($ChangeListener)[]
public "setModel"(arg0: $ButtonModel$Type): void
public "getDisabledIcon"(): $Icon
public "getDisabledSelectedIcon"(): $Icon
public "setIcon"(arg0: $Icon$Type): void
public "setSelected"(arg0: boolean): void
public "removeChangeListener"(arg0: $ChangeListener$Type): void
/**
 * 
 * @deprecated
 */
public "getLabel"(): string
public "addChangeListener"(arg0: $ChangeListener$Type): void
public "getHorizontalAlignment"(): integer
public "getVerticalAlignment"(): integer
public "imageUpdate"(arg0: $Image$Type, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: integer): boolean
public "setHorizontalAlignment"(arg0: integer): void
public "updateUI"(): void
public "setUI"(arg0: $ButtonUI$Type): void
public "isBorderPainted"(): boolean
public "setBorderPainted"(arg0: boolean): void
public "setMargin"(arg0: $Insets$Type): void
get "text"(): string
set "text"(value: string)
get "actionListeners"(): ($ActionListener)[]
set "actionCommand"(value: string)
get "margin"(): $Insets
set "enabled"(value: boolean)
get "mnemonic"(): integer
set "rolloverEnabled"(value: boolean)
get "selectedIcon"(): $Icon
get "rolloverEnabled"(): boolean
get "hideActionText"(): boolean
set "hideActionText"(value: boolean)
set "displayedMnemonicIndex"(value: integer)
set "disabledIcon"(value: $Icon$Type)
set "disabledSelectedIcon"(value: $Icon$Type)
get "pressedIcon"(): $Icon
get "rolloverSelectedIcon"(): $Icon
get "rolloverIcon"(): $Icon
set "iconTextGap"(value: integer)
set "contentAreaFilled"(value: boolean)
set "pressedIcon"(value: $Icon$Type)
set "selectedIcon"(value: $Icon$Type)
set "rolloverIcon"(value: $Icon$Type)
set "rolloverSelectedIcon"(value: $Icon$Type)
set "verticalAlignment"(value: integer)
get "verticalTextPosition"(): integer
get "horizontalTextPosition"(): integer
get "iconTextGap"(): integer
get "focusPainted"(): boolean
get "contentAreaFilled"(): boolean
get "displayedMnemonicIndex"(): integer
set "multiClickThreshhold"(value: long)
get "multiClickThreshhold"(): long
get "itemListeners"(): ($ItemListener)[]
get "selectedObjects"(): (any)[]
set "action"(value: $Action$Type)
get "model"(): $ButtonModel
get "selected"(): boolean
set "layout"(value: $LayoutManager$Type)
get "icon"(): $Icon
set "mnemonic"(value: character)
set "mnemonic"(value: integer)
set "focusPainted"(value: boolean)
get "action"(): $Action
set "label"(value: string)
get "actionCommand"(): string
set "horizontalTextPosition"(value: integer)
set "verticalTextPosition"(value: integer)
get "changeListeners"(): ($ChangeListener)[]
set "model"(value: $ButtonModel$Type)
get "disabledIcon"(): $Icon
get "disabledSelectedIcon"(): $Icon
set "icon"(value: $Icon$Type)
set "selected"(value: boolean)
get "label"(): string
get "horizontalAlignment"(): integer
get "verticalAlignment"(): integer
set "horizontalAlignment"(value: integer)
set "uI"(value: $ButtonUI$Type)
get "borderPainted"(): boolean
set "borderPainted"(value: boolean)
set "margin"(value: $Insets$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractButton$Type = ($AbstractButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractButton_ = $AbstractButton$Type;
}}
declare module "packages/javax/swing/event/$MenuKeyEvent" {
import {$MenuSelectionManager, $MenuSelectionManager$Type} from "packages/javax/swing/$MenuSelectionManager"
import {$KeyEvent, $KeyEvent$Type} from "packages/java/awt/event/$KeyEvent"
import {$MenuElement, $MenuElement$Type} from "packages/javax/swing/$MenuElement"
import {$Component, $Component$Type} from "packages/java/awt/$Component"

export class $MenuKeyEvent extends $KeyEvent {
static readonly "KEY_FIRST": integer
static readonly "KEY_LAST": integer
static readonly "KEY_TYPED": integer
static readonly "KEY_PRESSED": integer
static readonly "KEY_RELEASED": integer
static readonly "VK_ENTER": integer
static readonly "VK_BACK_SPACE": integer
static readonly "VK_TAB": integer
static readonly "VK_CANCEL": integer
static readonly "VK_CLEAR": integer
static readonly "VK_SHIFT": integer
static readonly "VK_CONTROL": integer
static readonly "VK_ALT": integer
static readonly "VK_PAUSE": integer
static readonly "VK_CAPS_LOCK": integer
static readonly "VK_ESCAPE": integer
static readonly "VK_SPACE": integer
static readonly "VK_PAGE_UP": integer
static readonly "VK_PAGE_DOWN": integer
static readonly "VK_END": integer
static readonly "VK_HOME": integer
static readonly "VK_LEFT": integer
static readonly "VK_UP": integer
static readonly "VK_RIGHT": integer
static readonly "VK_DOWN": integer
static readonly "VK_COMMA": integer
static readonly "VK_MINUS": integer
static readonly "VK_PERIOD": integer
static readonly "VK_SLASH": integer
static readonly "VK_0": integer
static readonly "VK_1": integer
static readonly "VK_2": integer
static readonly "VK_3": integer
static readonly "VK_4": integer
static readonly "VK_5": integer
static readonly "VK_6": integer
static readonly "VK_7": integer
static readonly "VK_8": integer
static readonly "VK_9": integer
static readonly "VK_SEMICOLON": integer
static readonly "VK_EQUALS": integer
static readonly "VK_A": integer
static readonly "VK_B": integer
static readonly "VK_C": integer
static readonly "VK_D": integer
static readonly "VK_E": integer
static readonly "VK_F": integer
static readonly "VK_G": integer
static readonly "VK_H": integer
static readonly "VK_I": integer
static readonly "VK_J": integer
static readonly "VK_K": integer
static readonly "VK_L": integer
static readonly "VK_M": integer
static readonly "VK_N": integer
static readonly "VK_O": integer
static readonly "VK_P": integer
static readonly "VK_Q": integer
static readonly "VK_R": integer
static readonly "VK_S": integer
static readonly "VK_T": integer
static readonly "VK_U": integer
static readonly "VK_V": integer
static readonly "VK_W": integer
static readonly "VK_X": integer
static readonly "VK_Y": integer
static readonly "VK_Z": integer
static readonly "VK_OPEN_BRACKET": integer
static readonly "VK_BACK_SLASH": integer
static readonly "VK_CLOSE_BRACKET": integer
static readonly "VK_NUMPAD0": integer
static readonly "VK_NUMPAD1": integer
static readonly "VK_NUMPAD2": integer
static readonly "VK_NUMPAD3": integer
static readonly "VK_NUMPAD4": integer
static readonly "VK_NUMPAD5": integer
static readonly "VK_NUMPAD6": integer
static readonly "VK_NUMPAD7": integer
static readonly "VK_NUMPAD8": integer
static readonly "VK_NUMPAD9": integer
static readonly "VK_MULTIPLY": integer
static readonly "VK_ADD": integer
static readonly "VK_SEPARATER": integer
static readonly "VK_SEPARATOR": integer
static readonly "VK_SUBTRACT": integer
static readonly "VK_DECIMAL": integer
static readonly "VK_DIVIDE": integer
static readonly "VK_DELETE": integer
static readonly "VK_NUM_LOCK": integer
static readonly "VK_SCROLL_LOCK": integer
static readonly "VK_F1": integer
static readonly "VK_F2": integer
static readonly "VK_F3": integer
static readonly "VK_F4": integer
static readonly "VK_F5": integer
static readonly "VK_F6": integer
static readonly "VK_F7": integer
static readonly "VK_F8": integer
static readonly "VK_F9": integer
static readonly "VK_F10": integer
static readonly "VK_F11": integer
static readonly "VK_F12": integer
static readonly "VK_F13": integer
static readonly "VK_F14": integer
static readonly "VK_F15": integer
static readonly "VK_F16": integer
static readonly "VK_F17": integer
static readonly "VK_F18": integer
static readonly "VK_F19": integer
static readonly "VK_F20": integer
static readonly "VK_F21": integer
static readonly "VK_F22": integer
static readonly "VK_F23": integer
static readonly "VK_F24": integer
static readonly "VK_PRINTSCREEN": integer
static readonly "VK_INSERT": integer
static readonly "VK_HELP": integer
static readonly "VK_META": integer
static readonly "VK_BACK_QUOTE": integer
static readonly "VK_QUOTE": integer
static readonly "VK_KP_UP": integer
static readonly "VK_KP_DOWN": integer
static readonly "VK_KP_LEFT": integer
static readonly "VK_KP_RIGHT": integer
static readonly "VK_DEAD_GRAVE": integer
static readonly "VK_DEAD_ACUTE": integer
static readonly "VK_DEAD_CIRCUMFLEX": integer
static readonly "VK_DEAD_TILDE": integer
static readonly "VK_DEAD_MACRON": integer
static readonly "VK_DEAD_BREVE": integer
static readonly "VK_DEAD_ABOVEDOT": integer
static readonly "VK_DEAD_DIAERESIS": integer
static readonly "VK_DEAD_ABOVERING": integer
static readonly "VK_DEAD_DOUBLEACUTE": integer
static readonly "VK_DEAD_CARON": integer
static readonly "VK_DEAD_CEDILLA": integer
static readonly "VK_DEAD_OGONEK": integer
static readonly "VK_DEAD_IOTA": integer
static readonly "VK_DEAD_VOICED_SOUND": integer
static readonly "VK_DEAD_SEMIVOICED_SOUND": integer
static readonly "VK_AMPERSAND": integer
static readonly "VK_ASTERISK": integer
static readonly "VK_QUOTEDBL": integer
static readonly "VK_LESS": integer
static readonly "VK_GREATER": integer
static readonly "VK_BRACELEFT": integer
static readonly "VK_BRACERIGHT": integer
static readonly "VK_AT": integer
static readonly "VK_COLON": integer
static readonly "VK_CIRCUMFLEX": integer
static readonly "VK_DOLLAR": integer
static readonly "VK_EURO_SIGN": integer
static readonly "VK_EXCLAMATION_MARK": integer
static readonly "VK_INVERTED_EXCLAMATION_MARK": integer
static readonly "VK_LEFT_PARENTHESIS": integer
static readonly "VK_NUMBER_SIGN": integer
static readonly "VK_PLUS": integer
static readonly "VK_RIGHT_PARENTHESIS": integer
static readonly "VK_UNDERSCORE": integer
static readonly "VK_WINDOWS": integer
static readonly "VK_CONTEXT_MENU": integer
static readonly "VK_FINAL": integer
static readonly "VK_CONVERT": integer
static readonly "VK_NONCONVERT": integer
static readonly "VK_ACCEPT": integer
static readonly "VK_MODECHANGE": integer
static readonly "VK_KANA": integer
static readonly "VK_KANJI": integer
static readonly "VK_ALPHANUMERIC": integer
static readonly "VK_KATAKANA": integer
static readonly "VK_HIRAGANA": integer
static readonly "VK_FULL_WIDTH": integer
static readonly "VK_HALF_WIDTH": integer
static readonly "VK_ROMAN_CHARACTERS": integer
static readonly "VK_ALL_CANDIDATES": integer
static readonly "VK_PREVIOUS_CANDIDATE": integer
static readonly "VK_CODE_INPUT": integer
static readonly "VK_JAPANESE_KATAKANA": integer
static readonly "VK_JAPANESE_HIRAGANA": integer
static readonly "VK_JAPANESE_ROMAN": integer
static readonly "VK_KANA_LOCK": integer
static readonly "VK_INPUT_METHOD_ON_OFF": integer
static readonly "VK_CUT": integer
static readonly "VK_COPY": integer
static readonly "VK_PASTE": integer
static readonly "VK_UNDO": integer
static readonly "VK_AGAIN": integer
static readonly "VK_FIND": integer
static readonly "VK_PROPS": integer
static readonly "VK_STOP": integer
static readonly "VK_COMPOSE": integer
static readonly "VK_ALT_GRAPH": integer
static readonly "VK_BEGIN": integer
static readonly "VK_UNDEFINED": integer
static readonly "CHAR_UNDEFINED": character
static readonly "KEY_LOCATION_UNKNOWN": integer
static readonly "KEY_LOCATION_STANDARD": integer
static readonly "KEY_LOCATION_LEFT": integer
static readonly "KEY_LOCATION_RIGHT": integer
static readonly "KEY_LOCATION_NUMPAD": integer
/**
 * 
 * @deprecated
 */
static readonly "SHIFT_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "CTRL_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "META_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "ALT_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "ALT_GRAPH_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "BUTTON1_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "BUTTON2_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "BUTTON3_MASK": integer
static readonly "SHIFT_DOWN_MASK": integer
static readonly "CTRL_DOWN_MASK": integer
static readonly "META_DOWN_MASK": integer
static readonly "ALT_DOWN_MASK": integer
static readonly "BUTTON1_DOWN_MASK": integer
static readonly "BUTTON2_DOWN_MASK": integer
static readonly "BUTTON3_DOWN_MASK": integer
static readonly "ALT_GRAPH_DOWN_MASK": integer
static readonly "COMPONENT_FIRST": integer
static readonly "COMPONENT_LAST": integer
static readonly "COMPONENT_MOVED": integer
static readonly "COMPONENT_RESIZED": integer
static readonly "COMPONENT_SHOWN": integer
static readonly "COMPONENT_HIDDEN": integer
static readonly "COMPONENT_EVENT_MASK": long
static readonly "CONTAINER_EVENT_MASK": long
static readonly "FOCUS_EVENT_MASK": long
static readonly "KEY_EVENT_MASK": long
static readonly "MOUSE_EVENT_MASK": long
static readonly "MOUSE_MOTION_EVENT_MASK": long
static readonly "WINDOW_EVENT_MASK": long
static readonly "ACTION_EVENT_MASK": long
static readonly "ADJUSTMENT_EVENT_MASK": long
static readonly "ITEM_EVENT_MASK": long
static readonly "TEXT_EVENT_MASK": long
static readonly "INPUT_METHOD_EVENT_MASK": long
static readonly "PAINT_EVENT_MASK": long
static readonly "INVOCATION_EVENT_MASK": long
static readonly "HIERARCHY_EVENT_MASK": long
static readonly "HIERARCHY_BOUNDS_EVENT_MASK": long
static readonly "MOUSE_WHEEL_EVENT_MASK": long
static readonly "WINDOW_STATE_EVENT_MASK": long
static readonly "WINDOW_FOCUS_EVENT_MASK": long
static readonly "RESERVED_ID_MAX": integer

constructor(arg0: $Component$Type, arg1: integer, arg2: long, arg3: integer, arg4: integer, arg5: character, arg6: ($MenuElement$Type)[], arg7: $MenuSelectionManager$Type)

public "getPath"(): ($MenuElement)[]
public "getMenuSelectionManager"(): $MenuSelectionManager
get "path"(): ($MenuElement)[]
get "menuSelectionManager"(): $MenuSelectionManager
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuKeyEvent$Type = ($MenuKeyEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuKeyEvent_ = $MenuKeyEvent$Type;
}}
declare module "packages/javax/swing/plaf/$RootPaneUI" {
import {$ComponentUI, $ComponentUI$Type} from "packages/javax/swing/plaf/$ComponentUI"

export class $RootPaneUI extends $ComponentUI {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RootPaneUI$Type = ($RootPaneUI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RootPaneUI_ = $RootPaneUI$Type;
}}
declare module "packages/javax/swing/plaf/$ScrollPaneUI" {
import {$ComponentUI, $ComponentUI$Type} from "packages/javax/swing/plaf/$ComponentUI"

export class $ScrollPaneUI extends $ComponentUI {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScrollPaneUI$Type = ($ScrollPaneUI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScrollPaneUI_ = $ScrollPaneUI$Type;
}}
declare module "packages/javax/swing/$JEditorPane" {
import {$HyperlinkListener, $HyperlinkListener$Type} from "packages/javax/swing/event/$HyperlinkListener"
import {$HyperlinkEvent, $HyperlinkEvent$Type} from "packages/javax/swing/event/$HyperlinkEvent"
import {$JTextComponent, $JTextComponent$Type} from "packages/javax/swing/text/$JTextComponent"
import {$ClassLoader, $ClassLoader$Type} from "packages/java/lang/$ClassLoader"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$EditorKit, $EditorKit$Type} from "packages/javax/swing/text/$EditorKit"
import {$AccessibleContext, $AccessibleContext$Type} from "packages/javax/accessibility/$AccessibleContext"
import {$URL, $URL$Type} from "packages/java/net/$URL"
import {$Dimension, $Dimension$Type} from "packages/java/awt/$Dimension"

export class $JEditorPane extends $JTextComponent {
static readonly "W3C_LENGTH_UNITS": string
static readonly "HONOR_DISPLAY_PROPERTIES": string
static readonly "FOCUS_ACCELERATOR_KEY": string
static readonly "DEFAULT_KEYMAP": string
static readonly "WHEN_FOCUSED": integer
static readonly "WHEN_ANCESTOR_OF_FOCUSED_COMPONENT": integer
static readonly "WHEN_IN_FOCUSED_WINDOW": integer
static readonly "UNDEFINED_CONDITION": integer
static readonly "TOOL_TIP_TEXT_KEY": string
static readonly "TOP_ALIGNMENT": float
static readonly "CENTER_ALIGNMENT": float
static readonly "BOTTOM_ALIGNMENT": float
static readonly "LEFT_ALIGNMENT": float
static readonly "RIGHT_ALIGNMENT": float

constructor(arg0: string, arg1: string)
constructor(arg0: string)
constructor(arg0: $URL$Type)
constructor()

public "read"(arg0: $InputStream$Type, arg1: any): void
public "getText"(): string
public "setText"(arg0: string): void
public "getContentType"(): string
public "setContentType"(arg0: string): void
public "scrollToReference"(arg0: string): void
public "getEditorKitForContentType"(arg0: string): $EditorKit
public "setEditorKit"(arg0: $EditorKit$Type): void
public static "createEditorKitForContentType"(arg0: string): $EditorKit
public "setEditorKitForContentType"(arg0: string, arg1: $EditorKit$Type): void
public static "registerEditorKitForContentType"(arg0: string, arg1: string): void
public static "registerEditorKitForContentType"(arg0: string, arg1: string, arg2: $ClassLoader$Type): void
public "addHyperlinkListener"(arg0: $HyperlinkListener$Type): void
public "removeHyperlinkListener"(arg0: $HyperlinkListener$Type): void
public "getHyperlinkListeners"(): ($HyperlinkListener)[]
public "fireHyperlinkUpdate"(arg0: $HyperlinkEvent$Type): void
public static "getEditorKitClassNameForContentType"(arg0: string): string
public "getPreferredSize"(): $Dimension
public "getAccessibleContext"(): $AccessibleContext
public "getScrollableTracksViewportWidth"(): boolean
public "getEditorKit"(): $EditorKit
public "replaceSelection"(arg0: string): void
public "setPage"(arg0: string): void
public "setPage"(arg0: $URL$Type): void
public "getScrollableTracksViewportHeight"(): boolean
public "getPage"(): $URL
public "getUIClassID"(): string
get "text"(): string
set "text"(value: string)
get "contentType"(): string
set "contentType"(value: string)
set "editorKit"(value: $EditorKit$Type)
get "hyperlinkListeners"(): ($HyperlinkListener)[]
get "preferredSize"(): $Dimension
get "accessibleContext"(): $AccessibleContext
get "scrollableTracksViewportWidth"(): boolean
get "editorKit"(): $EditorKit
set "page"(value: string)
set "page"(value: $URL$Type)
get "scrollableTracksViewportHeight"(): boolean
get "page"(): $URL
get "uIClassID"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JEditorPane$Type = ($JEditorPane);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JEditorPane_ = $JEditorPane$Type;
}}
declare module "packages/javax/swing/text/$JTextComponent$DropLocation" {
import {$TransferHandler$DropLocation, $TransferHandler$DropLocation$Type} from "packages/javax/swing/$TransferHandler$DropLocation"
import {$Position$Bias, $Position$Bias$Type} from "packages/javax/swing/text/$Position$Bias"

export class $JTextComponent$DropLocation extends $TransferHandler$DropLocation {


public "toString"(): string
public "getIndex"(): integer
public "getBias"(): $Position$Bias
get "index"(): integer
get "bias"(): $Position$Bias
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JTextComponent$DropLocation$Type = ($JTextComponent$DropLocation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JTextComponent$DropLocation_ = $JTextComponent$DropLocation$Type;
}}
declare module "packages/javax/swing/plaf/$ComponentUI" {
import {$JComponent, $JComponent$Type} from "packages/javax/swing/$JComponent"
import {$Accessible, $Accessible$Type} from "packages/javax/accessibility/$Accessible"
import {$Component$BaselineResizeBehavior, $Component$BaselineResizeBehavior$Type} from "packages/java/awt/$Component$BaselineResizeBehavior"
import {$Graphics, $Graphics$Type} from "packages/java/awt/$Graphics"
import {$Dimension, $Dimension$Type} from "packages/java/awt/$Dimension"

export class $ComponentUI {

constructor()

public "update"(arg0: $Graphics$Type, arg1: $JComponent$Type): void
public "contains"(arg0: $JComponent$Type, arg1: integer, arg2: integer): boolean
public "getBaseline"(arg0: $JComponent$Type, arg1: integer, arg2: integer): integer
public "getPreferredSize"(arg0: $JComponent$Type): $Dimension
public "getMinimumSize"(arg0: $JComponent$Type): $Dimension
public "getAccessibleChildrenCount"(arg0: $JComponent$Type): integer
public "getAccessibleChild"(arg0: $JComponent$Type, arg1: integer): $Accessible
public "getMaximumSize"(arg0: $JComponent$Type): $Dimension
public "getBaselineResizeBehavior"(arg0: $JComponent$Type): $Component$BaselineResizeBehavior
public "paint"(arg0: $Graphics$Type, arg1: $JComponent$Type): void
public static "createUI"(arg0: $JComponent$Type): $ComponentUI
public "installUI"(arg0: $JComponent$Type): void
public "uninstallUI"(arg0: $JComponent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ComponentUI$Type = ($ComponentUI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ComponentUI_ = $ComponentUI$Type;
}}
declare module "packages/javax/swing/text/$View" {
import {$ViewFactory, $ViewFactory$Type} from "packages/javax/swing/text/$ViewFactory"
import {$Element, $Element$Type} from "packages/javax/swing/text/$Element"
import {$Container, $Container$Type} from "packages/java/awt/$Container"
import {$Document, $Document$Type} from "packages/javax/swing/text/$Document"
import {$Graphics, $Graphics$Type} from "packages/java/awt/$Graphics"
import {$AttributeSet, $AttributeSet$Type} from "packages/javax/swing/text/$AttributeSet"
import {$Position$Bias, $Position$Bias$Type} from "packages/javax/swing/text/$Position$Bias"
import {$Shape, $Shape$Type} from "packages/java/awt/$Shape"
import {$DocumentEvent, $DocumentEvent$Type} from "packages/javax/swing/event/$DocumentEvent"
import {$SwingConstants, $SwingConstants$Type} from "packages/javax/swing/$SwingConstants"

export class $View implements $SwingConstants {
static readonly "BadBreakWeight": integer
static readonly "GoodBreakWeight": integer
static readonly "ExcellentBreakWeight": integer
static readonly "ForcedBreakWeight": integer
static readonly "X_AXIS": integer
static readonly "Y_AXIS": integer

constructor(arg0: $Element$Type)

public "remove"(arg0: integer): void
public "append"(arg0: $View$Type): void
public "insert"(arg0: integer, arg1: $View$Type): void
public "replace"(arg0: integer, arg1: integer, arg2: ($View$Type)[]): void
public "getParent"(): $View
public "setParent"(arg0: $View$Type): void
public "getAttributes"(): $AttributeSet
public "removeAll"(): void
public "setSize"(arg0: float, arg1: float): void
public "getElement"(): $Element
public "getViewFactory"(): $ViewFactory
public "getResizeWeight"(arg0: integer): integer
public "preferenceChanged"(arg0: $View$Type, arg1: boolean, arg2: boolean): void
public "getViewCount"(): integer
public "getViewIndex"(arg0: integer, arg1: $Position$Bias$Type): integer
public "getViewIndex"(arg0: float, arg1: float, arg2: $Shape$Type): integer
public "getChildAllocation"(arg0: integer, arg1: $Shape$Type): $Shape
public "getMinimumSpan"(arg0: integer): float
public "getMaximumSpan"(arg0: integer): float
public "breakView"(arg0: integer, arg1: integer, arg2: float, arg3: float): $View
public "getBreakWeight"(arg0: integer, arg1: float, arg2: float): integer
public "getGraphics"(): $Graphics
public "getAlignment"(arg0: integer): float
public "isVisible"(): boolean
public "getView"(arg0: integer): $View
public "getContainer"(): $Container
public "getEndOffset"(): integer
public "getStartOffset"(): integer
public "getDocument"(): $Document
public "createFragment"(arg0: integer, arg1: integer): $View
public "viewToModel"(arg0: float, arg1: float, arg2: $Shape$Type, arg3: ($Position$Bias$Type)[]): integer
/**
 * 
 * @deprecated
 */
public "viewToModel"(arg0: float, arg1: float, arg2: $Shape$Type): integer
public "modelToView"(arg0: integer, arg1: $Shape$Type, arg2: $Position$Bias$Type): $Shape
public "modelToView"(arg0: integer, arg1: $Position$Bias$Type, arg2: integer, arg3: $Position$Bias$Type, arg4: $Shape$Type): $Shape
/**
 * 
 * @deprecated
 */
public "modelToView"(arg0: integer, arg1: $Shape$Type): $Shape
public "paint"(arg0: $Graphics$Type, arg1: $Shape$Type): void
public "getNextVisualPositionFrom"(arg0: integer, arg1: $Position$Bias$Type, arg2: $Shape$Type, arg3: integer, arg4: ($Position$Bias$Type)[]): integer
public "getPreferredSpan"(arg0: integer): float
public "insertUpdate"(arg0: $DocumentEvent$Type, arg1: $Shape$Type, arg2: $ViewFactory$Type): void
public "removeUpdate"(arg0: $DocumentEvent$Type, arg1: $Shape$Type, arg2: $ViewFactory$Type): void
public "changedUpdate"(arg0: $DocumentEvent$Type, arg1: $Shape$Type, arg2: $ViewFactory$Type): void
public "getToolTipText"(arg0: float, arg1: float, arg2: $Shape$Type): string
get "parent"(): $View
set "parent"(value: $View$Type)
get "attributes"(): $AttributeSet
get "element"(): $Element
get "viewFactory"(): $ViewFactory
get "viewCount"(): integer
get "graphics"(): $Graphics
get "visible"(): boolean
get "container"(): $Container
get "endOffset"(): integer
get "startOffset"(): integer
get "document"(): $Document
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $View$Type = ($View);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $View_ = $View$Type;
}}
declare module "packages/javax/swing/$MenuElement" {
import {$MenuSelectionManager, $MenuSelectionManager$Type} from "packages/javax/swing/$MenuSelectionManager"
import {$MouseEvent, $MouseEvent$Type} from "packages/java/awt/event/$MouseEvent"
import {$KeyEvent, $KeyEvent$Type} from "packages/java/awt/event/$KeyEvent"
import {$Component, $Component$Type} from "packages/java/awt/$Component"

export interface $MenuElement {

 "getComponent"(): $Component
 "processKeyEvent"(arg0: $KeyEvent$Type, arg1: ($MenuElement$Type)[], arg2: $MenuSelectionManager$Type): void
 "processMouseEvent"(arg0: $MouseEvent$Type, arg1: ($MenuElement$Type)[], arg2: $MenuSelectionManager$Type): void
 "getSubElements"(): ($MenuElement)[]
 "menuSelectionChanged"(arg0: boolean): void
}

export namespace $MenuElement {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuElement$Type = ($MenuElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuElement_ = $MenuElement$Type;
}}
declare module "packages/javax/swing/$TransferHandler$HasGetTransferHandler" {
import {$TransferHandler, $TransferHandler$Type} from "packages/javax/swing/$TransferHandler"

export interface $TransferHandler$HasGetTransferHandler {

 "getTransferHandler"(): $TransferHandler

(): $TransferHandler
}

export namespace $TransferHandler$HasGetTransferHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TransferHandler$HasGetTransferHandler$Type = ($TransferHandler$HasGetTransferHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TransferHandler$HasGetTransferHandler_ = $TransferHandler$HasGetTransferHandler$Type;
}}
declare module "packages/javax/swing/$JScrollPane" {
import {$ComponentOrientation, $ComponentOrientation$Type} from "packages/java/awt/$ComponentOrientation"
import {$JComponent, $JComponent$Type} from "packages/javax/swing/$JComponent"
import {$LayoutManager, $LayoutManager$Type} from "packages/java/awt/$LayoutManager"
import {$Accessible, $Accessible$Type} from "packages/javax/accessibility/$Accessible"
import {$JScrollBar, $JScrollBar$Type} from "packages/javax/swing/$JScrollBar"
import {$AccessibleContext, $AccessibleContext$Type} from "packages/javax/accessibility/$AccessibleContext"
import {$JViewport, $JViewport$Type} from "packages/javax/swing/$JViewport"
import {$Component, $Component$Type} from "packages/java/awt/$Component"
import {$Border, $Border$Type} from "packages/javax/swing/border/$Border"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$ScrollPaneConstants, $ScrollPaneConstants$Type} from "packages/javax/swing/$ScrollPaneConstants"
import {$ScrollPaneUI, $ScrollPaneUI$Type} from "packages/javax/swing/plaf/$ScrollPaneUI"

export class $JScrollPane extends $JComponent implements $ScrollPaneConstants, $Accessible {
static readonly "WHEN_FOCUSED": integer
static readonly "WHEN_ANCESTOR_OF_FOCUSED_COMPONENT": integer
static readonly "WHEN_IN_FOCUSED_WINDOW": integer
static readonly "UNDEFINED_CONDITION": integer
static readonly "TOOL_TIP_TEXT_KEY": string
static readonly "TOP_ALIGNMENT": float
static readonly "CENTER_ALIGNMENT": float
static readonly "BOTTOM_ALIGNMENT": float
static readonly "LEFT_ALIGNMENT": float
static readonly "RIGHT_ALIGNMENT": float

constructor()
constructor(arg0: integer, arg1: integer)
constructor(arg0: $Component$Type)
constructor(arg0: $Component$Type, arg1: integer, arg2: integer)

public "setLayout"(arg0: $LayoutManager$Type): void
public "getCorner"(arg0: string): $Component
public "isValidateRoot"(): boolean
public "setComponentOrientation"(arg0: $ComponentOrientation$Type): void
public "getAccessibleContext"(): $AccessibleContext
public "setVerticalScrollBarPolicy"(arg0: integer): void
public "setHorizontalScrollBarPolicy"(arg0: integer): void
public "setViewport"(arg0: $JViewport$Type): void
public "createVerticalScrollBar"(): $JScrollBar
public "setVerticalScrollBar"(arg0: $JScrollBar$Type): void
public "createHorizontalScrollBar"(): $JScrollBar
public "setHorizontalScrollBar"(arg0: $JScrollBar$Type): void
public "setViewportView"(arg0: $Component$Type): void
public "getColumnHeader"(): $JViewport
public "getRowHeader"(): $JViewport
public "getVerticalScrollBar"(): $JScrollBar
public "getHorizontalScrollBar"(): $JScrollBar
public "getViewport"(): $JViewport
public "setRowHeader"(arg0: $JViewport$Type): void
public "setColumnHeader"(arg0: $JViewport$Type): void
public "getVerticalScrollBarPolicy"(): integer
public "getHorizontalScrollBarPolicy"(): integer
public "getViewportBorder"(): $Border
public "setViewportBorder"(arg0: $Border$Type): void
public "getViewportBorderBounds"(): $Rectangle
public "setRowHeaderView"(arg0: $Component$Type): void
public "setColumnHeaderView"(arg0: $Component$Type): void
public "setCorner"(arg0: string, arg1: $Component$Type): void
public "isWheelScrollingEnabled"(): boolean
public "setWheelScrollingEnabled"(arg0: boolean): void
public "updateUI"(): void
public "setUI"(arg0: $ScrollPaneUI$Type): void
public "getUI"(): $ScrollPaneUI
public "getUIClassID"(): string
set "layout"(value: $LayoutManager$Type)
get "validateRoot"(): boolean
set "componentOrientation"(value: $ComponentOrientation$Type)
get "accessibleContext"(): $AccessibleContext
set "verticalScrollBarPolicy"(value: integer)
set "horizontalScrollBarPolicy"(value: integer)
set "viewport"(value: $JViewport$Type)
set "verticalScrollBar"(value: $JScrollBar$Type)
set "horizontalScrollBar"(value: $JScrollBar$Type)
set "viewportView"(value: $Component$Type)
get "columnHeader"(): $JViewport
get "rowHeader"(): $JViewport
get "verticalScrollBar"(): $JScrollBar
get "horizontalScrollBar"(): $JScrollBar
get "viewport"(): $JViewport
set "rowHeader"(value: $JViewport$Type)
set "columnHeader"(value: $JViewport$Type)
get "verticalScrollBarPolicy"(): integer
get "horizontalScrollBarPolicy"(): integer
get "viewportBorder"(): $Border
set "viewportBorder"(value: $Border$Type)
get "viewportBorderBounds"(): $Rectangle
set "rowHeaderView"(value: $Component$Type)
set "columnHeaderView"(value: $Component$Type)
get "wheelScrollingEnabled"(): boolean
set "wheelScrollingEnabled"(value: boolean)
set "uI"(value: $ScrollPaneUI$Type)
get "uI"(): $ScrollPaneUI
get "uIClassID"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JScrollPane$Type = ($JScrollPane);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JScrollPane_ = $JScrollPane$Type;
}}
declare module "packages/javax/swing/event/$HyperlinkListener" {
import {$HyperlinkEvent, $HyperlinkEvent$Type} from "packages/javax/swing/event/$HyperlinkEvent"
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"

export interface $HyperlinkListener extends $EventListener {

 "hyperlinkUpdate"(arg0: $HyperlinkEvent$Type): void

(arg0: $HyperlinkEvent$Type): void
}

export namespace $HyperlinkListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HyperlinkListener$Type = ($HyperlinkListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HyperlinkListener_ = $HyperlinkListener$Type;
}}
declare module "packages/javax/swing/text/$Highlighter$Highlight" {
import {$Highlighter$HighlightPainter, $Highlighter$HighlightPainter$Type} from "packages/javax/swing/text/$Highlighter$HighlightPainter"

export interface $Highlighter$Highlight {

 "getEndOffset"(): integer
 "getStartOffset"(): integer
 "getPainter"(): $Highlighter$HighlightPainter
}

export namespace $Highlighter$Highlight {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Highlighter$Highlight$Type = ($Highlighter$Highlight);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Highlighter$Highlight_ = $Highlighter$Highlight$Type;
}}
declare module "packages/javax/swing/$JRootPane" {
import {$JLayeredPane, $JLayeredPane$Type} from "packages/javax/swing/$JLayeredPane"
import {$JMenuBar, $JMenuBar$Type} from "packages/javax/swing/$JMenuBar"
import {$Container, $Container$Type} from "packages/java/awt/$Container"
import {$JComponent, $JComponent$Type} from "packages/javax/swing/$JComponent"
import {$Accessible, $Accessible$Type} from "packages/javax/accessibility/$Accessible"
import {$AccessibleContext, $AccessibleContext$Type} from "packages/javax/accessibility/$AccessibleContext"
import {$JButton, $JButton$Type} from "packages/javax/swing/$JButton"
import {$RootPaneUI, $RootPaneUI$Type} from "packages/javax/swing/plaf/$RootPaneUI"
import {$Component, $Component$Type} from "packages/java/awt/$Component"

export class $JRootPane extends $JComponent implements $Accessible {
static readonly "NONE": integer
static readonly "FRAME": integer
static readonly "PLAIN_DIALOG": integer
static readonly "INFORMATION_DIALOG": integer
static readonly "ERROR_DIALOG": integer
static readonly "COLOR_CHOOSER_DIALOG": integer
static readonly "FILE_CHOOSER_DIALOG": integer
static readonly "QUESTION_DIALOG": integer
static readonly "WARNING_DIALOG": integer
static readonly "WHEN_FOCUSED": integer
static readonly "WHEN_ANCESTOR_OF_FOCUSED_COMPONENT": integer
static readonly "WHEN_IN_FOCUSED_WINDOW": integer
static readonly "UNDEFINED_CONDITION": integer
static readonly "TOOL_TIP_TEXT_KEY": string
static readonly "TOP_ALIGNMENT": float
static readonly "CENTER_ALIGNMENT": float
static readonly "BOTTOM_ALIGNMENT": float
static readonly "LEFT_ALIGNMENT": float
static readonly "RIGHT_ALIGNMENT": float

constructor()

public "setWindowDecorationStyle"(arg0: integer): void
public "setJMenuBar"(arg0: $JMenuBar$Type): void
public "getJMenuBar"(): $JMenuBar
public "removeNotify"(): void
public "addNotify"(): void
public "isValidateRoot"(): boolean
public "getAccessibleContext"(): $AccessibleContext
public "getContentPane"(): $Container
public "setContentPane"(arg0: $Container$Type): void
public "getLayeredPane"(): $JLayeredPane
public "setLayeredPane"(arg0: $JLayeredPane$Type): void
public "getGlassPane"(): $Component
public "setGlassPane"(arg0: $Component$Type): void
/**
 * 
 * @deprecated
 */
public "setMenuBar"(arg0: $JMenuBar$Type): void
/**
 * 
 * @deprecated
 */
public "getMenuBar"(): $JMenuBar
public "getWindowDecorationStyle"(): integer
public "setDefaultButton"(arg0: $JButton$Type): void
public "getDefaultButton"(): $JButton
public "setDoubleBuffered"(arg0: boolean): void
public "updateUI"(): void
public "setUI"(arg0: $RootPaneUI$Type): void
public "getUIClassID"(): string
public "isOptimizedDrawingEnabled"(): boolean
set "windowDecorationStyle"(value: integer)
set "jMenuBar"(value: $JMenuBar$Type)
get "jMenuBar"(): $JMenuBar
get "validateRoot"(): boolean
get "accessibleContext"(): $AccessibleContext
get "contentPane"(): $Container
set "contentPane"(value: $Container$Type)
get "layeredPane"(): $JLayeredPane
set "layeredPane"(value: $JLayeredPane$Type)
get "glassPane"(): $Component
set "glassPane"(value: $Component$Type)
set "menuBar"(value: $JMenuBar$Type)
get "menuBar"(): $JMenuBar
get "windowDecorationStyle"(): integer
set "defaultButton"(value: $JButton$Type)
get "defaultButton"(): $JButton
set "doubleBuffered"(value: boolean)
set "uI"(value: $RootPaneUI$Type)
get "uIClassID"(): string
get "optimizedDrawingEnabled"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JRootPane$Type = ($JRootPane);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JRootPane_ = $JRootPane$Type;
}}
declare module "packages/javax/swing/$JMenuBar" {
import {$MenuSelectionManager, $MenuSelectionManager$Type} from "packages/javax/swing/$MenuSelectionManager"
import {$SingleSelectionModel, $SingleSelectionModel$Type} from "packages/javax/swing/$SingleSelectionModel"
import {$MenuBarUI, $MenuBarUI$Type} from "packages/javax/swing/plaf/$MenuBarUI"
import {$JComponent, $JComponent$Type} from "packages/javax/swing/$JComponent"
import {$Accessible, $Accessible$Type} from "packages/javax/accessibility/$Accessible"
import {$Insets, $Insets$Type} from "packages/java/awt/$Insets"
import {$AccessibleContext, $AccessibleContext$Type} from "packages/javax/accessibility/$AccessibleContext"
import {$Component, $Component$Type} from "packages/java/awt/$Component"
import {$JMenu, $JMenu$Type} from "packages/javax/swing/$JMenu"
import {$MouseEvent, $MouseEvent$Type} from "packages/java/awt/event/$MouseEvent"
import {$KeyEvent, $KeyEvent$Type} from "packages/java/awt/event/$KeyEvent"
import {$MenuElement, $MenuElement$Type} from "packages/javax/swing/$MenuElement"

export class $JMenuBar extends $JComponent implements $Accessible, $MenuElement {
static readonly "WHEN_FOCUSED": integer
static readonly "WHEN_ANCESTOR_OF_FOCUSED_COMPONENT": integer
static readonly "WHEN_IN_FOCUSED_WINDOW": integer
static readonly "UNDEFINED_CONDITION": integer
static readonly "TOOL_TIP_TEXT_KEY": string
static readonly "TOP_ALIGNMENT": float
static readonly "CENTER_ALIGNMENT": float
static readonly "BOTTOM_ALIGNMENT": float
static readonly "LEFT_ALIGNMENT": float
static readonly "RIGHT_ALIGNMENT": float

constructor()

public "add"(arg0: $JMenu$Type): $JMenu
public "getMargin"(): $Insets
public "getComponent"(): $Component
public "isSelected"(): boolean
public "removeNotify"(): void
public "addNotify"(): void
public "processKeyEvent"(arg0: $KeyEvent$Type, arg1: ($MenuElement$Type)[], arg2: $MenuSelectionManager$Type): void
public "processMouseEvent"(arg0: $MouseEvent$Type, arg1: ($MenuElement$Type)[], arg2: $MenuSelectionManager$Type): void
public "getAccessibleContext"(): $AccessibleContext
public "getMenu"(arg0: integer): $JMenu
public "setSelected"(arg0: $Component$Type): void
public "updateUI"(): void
public "setSelectionModel"(arg0: $SingleSelectionModel$Type): void
public "setUI"(arg0: $MenuBarUI$Type): void
public "getUI"(): $MenuBarUI
/**
 * 
 * @deprecated
 */
public "getComponentAtIndex"(arg0: integer): $Component
public "getSelectionModel"(): $SingleSelectionModel
public "getComponentIndex"(arg0: $Component$Type): integer
public "isBorderPainted"(): boolean
public "getSubElements"(): ($MenuElement)[]
public "getUIClassID"(): string
public "getMenuCount"(): integer
public "setHelpMenu"(arg0: $JMenu$Type): void
public "getHelpMenu"(): $JMenu
public "setBorderPainted"(arg0: boolean): void
public "setMargin"(arg0: $Insets$Type): void
public "menuSelectionChanged"(arg0: boolean): void
get "margin"(): $Insets
get "component"(): $Component
get "selected"(): boolean
get "accessibleContext"(): $AccessibleContext
set "selected"(value: $Component$Type)
set "selectionModel"(value: $SingleSelectionModel$Type)
set "uI"(value: $MenuBarUI$Type)
get "uI"(): $MenuBarUI
get "selectionModel"(): $SingleSelectionModel
get "borderPainted"(): boolean
get "subElements"(): ($MenuElement)[]
get "uIClassID"(): string
get "menuCount"(): integer
set "helpMenu"(value: $JMenu$Type)
get "helpMenu"(): $JMenu
set "borderPainted"(value: boolean)
set "margin"(value: $Insets$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JMenuBar$Type = ($JMenuBar);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JMenuBar_ = $JMenuBar$Type;
}}
declare module "packages/javax/swing/$JButton" {
import {$Icon, $Icon$Type} from "packages/javax/swing/$Icon"
import {$Accessible, $Accessible$Type} from "packages/javax/accessibility/$Accessible"
import {$Action, $Action$Type} from "packages/javax/swing/$Action"
import {$AccessibleContext, $AccessibleContext$Type} from "packages/javax/accessibility/$AccessibleContext"
import {$AbstractButton, $AbstractButton$Type} from "packages/javax/swing/$AbstractButton"

export class $JButton extends $AbstractButton implements $Accessible {
static readonly "MODEL_CHANGED_PROPERTY": string
static readonly "TEXT_CHANGED_PROPERTY": string
static readonly "MNEMONIC_CHANGED_PROPERTY": string
static readonly "MARGIN_CHANGED_PROPERTY": string
static readonly "VERTICAL_ALIGNMENT_CHANGED_PROPERTY": string
static readonly "HORIZONTAL_ALIGNMENT_CHANGED_PROPERTY": string
static readonly "VERTICAL_TEXT_POSITION_CHANGED_PROPERTY": string
static readonly "HORIZONTAL_TEXT_POSITION_CHANGED_PROPERTY": string
static readonly "BORDER_PAINTED_CHANGED_PROPERTY": string
static readonly "FOCUS_PAINTED_CHANGED_PROPERTY": string
static readonly "ROLLOVER_ENABLED_CHANGED_PROPERTY": string
static readonly "CONTENT_AREA_FILLED_CHANGED_PROPERTY": string
static readonly "ICON_CHANGED_PROPERTY": string
static readonly "PRESSED_ICON_CHANGED_PROPERTY": string
static readonly "SELECTED_ICON_CHANGED_PROPERTY": string
static readonly "ROLLOVER_ICON_CHANGED_PROPERTY": string
static readonly "ROLLOVER_SELECTED_ICON_CHANGED_PROPERTY": string
static readonly "DISABLED_ICON_CHANGED_PROPERTY": string
static readonly "DISABLED_SELECTED_ICON_CHANGED_PROPERTY": string
static readonly "WHEN_FOCUSED": integer
static readonly "WHEN_ANCESTOR_OF_FOCUSED_COMPONENT": integer
static readonly "WHEN_IN_FOCUSED_WINDOW": integer
static readonly "UNDEFINED_CONDITION": integer
static readonly "TOOL_TIP_TEXT_KEY": string
static readonly "TOP_ALIGNMENT": float
static readonly "CENTER_ALIGNMENT": float
static readonly "BOTTOM_ALIGNMENT": float
static readonly "LEFT_ALIGNMENT": float
static readonly "RIGHT_ALIGNMENT": float

constructor(arg0: string, arg1: $Icon$Type)
constructor(arg0: $Action$Type)
constructor(arg0: string)
constructor(arg0: $Icon$Type)
constructor()

public "removeNotify"(): void
public "getAccessibleContext"(): $AccessibleContext
public "isDefaultButton"(): boolean
public "isDefaultCapable"(): boolean
public "setDefaultCapable"(arg0: boolean): void
public "updateUI"(): void
public "getUIClassID"(): string
get "accessibleContext"(): $AccessibleContext
get "defaultButton"(): boolean
get "defaultCapable"(): boolean
set "defaultCapable"(value: boolean)
get "uIClassID"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JButton$Type = ($JButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JButton_ = $JButton$Type;
}}
declare module "packages/javax/swing/text/$Position" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Position {

 "getOffset"(): integer

(): integer
}

export namespace $Position {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Position$Type = ($Position);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Position_ = $Position$Type;
}}
declare module "packages/javax/swing/event/$CaretListener" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$CaretEvent, $CaretEvent$Type} from "packages/javax/swing/event/$CaretEvent"

export interface $CaretListener extends $EventListener {

 "caretUpdate"(arg0: $CaretEvent$Type): void

(arg0: $CaretEvent$Type): void
}

export namespace $CaretListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CaretListener$Type = ($CaretListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CaretListener_ = $CaretListener$Type;
}}
declare module "packages/javax/swing/$KeyStroke" {
import {$KeyEvent, $KeyEvent$Type} from "packages/java/awt/event/$KeyEvent"
import {$AWTKeyStroke, $AWTKeyStroke$Type} from "packages/java/awt/$AWTKeyStroke"

export class $KeyStroke extends $AWTKeyStroke {


public static "getKeyStrokeForEvent"(arg0: $KeyEvent$Type): $KeyStroke
public static "getKeyStroke"(arg0: integer, arg1: integer): $KeyStroke
public static "getKeyStroke"(arg0: integer, arg1: integer, arg2: boolean): $KeyStroke
public static "getKeyStroke"(arg0: string): $KeyStroke
public static "getKeyStroke"(arg0: character): $KeyStroke
public static "getKeyStroke"(arg0: character, arg1: integer): $KeyStroke
/**
 * 
 * @deprecated
 */
public static "getKeyStroke"(arg0: character, arg1: boolean): $KeyStroke
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyStroke$Type = ($KeyStroke);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyStroke_ = $KeyStroke$Type;
}}
declare module "packages/javax/swing/text/$Element" {
import {$Document, $Document$Type} from "packages/javax/swing/text/$Document"
import {$AttributeSet, $AttributeSet$Type} from "packages/javax/swing/text/$AttributeSet"

export interface $Element {

 "getName"(): string
 "getAttributes"(): $AttributeSet
 "isLeaf"(): boolean
 "getElement"(arg0: integer): $Element
 "getEndOffset"(): integer
 "getStartOffset"(): integer
 "getDocument"(): $Document
 "getElementIndex"(arg0: integer): integer
 "getElementCount"(): integer
 "getParentElement"(): $Element
}

export namespace $Element {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Element$Type = ($Element);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Element_ = $Element$Type;
}}
declare module "packages/javax/swing/$JViewport" {
import {$JComponent, $JComponent$Type} from "packages/javax/swing/$JComponent"
import {$Accessible, $Accessible$Type} from "packages/javax/accessibility/$Accessible"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$Insets, $Insets$Type} from "packages/java/awt/$Insets"
import {$AccessibleContext, $AccessibleContext$Type} from "packages/javax/accessibility/$AccessibleContext"
import {$Graphics, $Graphics$Type} from "packages/java/awt/$Graphics"
import {$Component, $Component$Type} from "packages/java/awt/$Component"
import {$Border, $Border$Type} from "packages/javax/swing/border/$Border"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$Dimension, $Dimension$Type} from "packages/java/awt/$Dimension"
import {$ChangeListener, $ChangeListener$Type} from "packages/javax/swing/event/$ChangeListener"
import {$ViewportUI, $ViewportUI$Type} from "packages/javax/swing/plaf/$ViewportUI"

export class $JViewport extends $JComponent implements $Accessible {
static readonly "BLIT_SCROLL_MODE": integer
static readonly "BACKINGSTORE_SCROLL_MODE": integer
static readonly "SIMPLE_SCROLL_MODE": integer
static readonly "WHEN_FOCUSED": integer
static readonly "WHEN_ANCESTOR_OF_FOCUSED_COMPONENT": integer
static readonly "WHEN_IN_FOCUSED_WINDOW": integer
static readonly "UNDEFINED_CONDITION": integer
static readonly "TOOL_TIP_TEXT_KEY": string
static readonly "TOP_ALIGNMENT": float
static readonly "CENTER_ALIGNMENT": float
static readonly "BOTTOM_ALIGNMENT": float
static readonly "LEFT_ALIGNMENT": float
static readonly "RIGHT_ALIGNMENT": float

constructor()

public "remove"(arg0: $Component$Type): void
public "getView"(): $Component
public "repaint"(arg0: long, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public "getInsets"(arg0: $Insets$Type): $Insets
public "getInsets"(): $Insets
public "getAccessibleContext"(): $AccessibleContext
public "setViewPosition"(arg0: $Point$Type): void
public "getViewPosition"(): $Point
public "getExtentSize"(): $Dimension
public "setScrollMode"(arg0: integer): void
public "getScrollMode"(): integer
/**
 * 
 * @deprecated
 */
public "isBackingStoreEnabled"(): boolean
/**
 * 
 * @deprecated
 */
public "setBackingStoreEnabled"(arg0: boolean): void
public "getViewSize"(): $Dimension
public "setViewSize"(arg0: $Dimension$Type): void
public "getViewRect"(): $Rectangle
public "toViewCoordinates"(arg0: $Dimension$Type): $Dimension
public "toViewCoordinates"(arg0: $Point$Type): $Point
public "setExtentSize"(arg0: $Dimension$Type): void
public "getChangeListeners"(): ($ChangeListener)[]
public "setBorder"(arg0: $Border$Type): void
public "setView"(arg0: $Component$Type): void
public "paint"(arg0: $Graphics$Type): void
public "removeChangeListener"(arg0: $ChangeListener$Type): void
public "addChangeListener"(arg0: $ChangeListener$Type): void
public "reshape"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "scrollRectToVisible"(arg0: $Rectangle$Type): void
public "updateUI"(): void
public "setUI"(arg0: $ViewportUI$Type): void
public "getUIClassID"(): string
public "isOptimizedDrawingEnabled"(): boolean
get "view"(): $Component
get "insets"(): $Insets
get "accessibleContext"(): $AccessibleContext
set "viewPosition"(value: $Point$Type)
get "viewPosition"(): $Point
get "extentSize"(): $Dimension
set "scrollMode"(value: integer)
get "scrollMode"(): integer
get "backingStoreEnabled"(): boolean
set "backingStoreEnabled"(value: boolean)
get "viewSize"(): $Dimension
set "viewSize"(value: $Dimension$Type)
get "viewRect"(): $Rectangle
set "extentSize"(value: $Dimension$Type)
get "changeListeners"(): ($ChangeListener)[]
set "border"(value: $Border$Type)
set "view"(value: $Component$Type)
set "uI"(value: $ViewportUI$Type)
get "uIClassID"(): string
get "optimizedDrawingEnabled"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JViewport$Type = ($JViewport);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JViewport_ = $JViewport$Type;
}}
declare module "packages/javax/swing/$Action" {
import {$PropertyChangeListener, $PropertyChangeListener$Type} from "packages/java/beans/$PropertyChangeListener"
import {$ActionEvent, $ActionEvent$Type} from "packages/java/awt/event/$ActionEvent"
import {$ActionListener, $ActionListener$Type} from "packages/java/awt/event/$ActionListener"

export interface $Action extends $ActionListener {

 "getValue"(arg0: string): any
 "accept"(arg0: any): boolean
 "putValue"(arg0: string, arg1: any): void
 "isEnabled"(): boolean
 "setEnabled"(arg0: boolean): void
 "addPropertyChangeListener"(arg0: $PropertyChangeListener$Type): void
 "removePropertyChangeListener"(arg0: $PropertyChangeListener$Type): void
 "actionPerformed"(arg0: $ActionEvent$Type): void
}

export namespace $Action {
const DEFAULT: string
const NAME: string
const SHORT_DESCRIPTION: string
const LONG_DESCRIPTION: string
const SMALL_ICON: string
const ACTION_COMMAND_KEY: string
const ACCELERATOR_KEY: string
const MNEMONIC_KEY: string
const SELECTED_KEY: string
const DISPLAYED_MNEMONIC_INDEX_KEY: string
const LARGE_ICON_KEY: string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Action$Type = ($Action);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Action_ = $Action$Type;
}}
declare module "packages/javax/swing/$Scrollable" {
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$Dimension, $Dimension$Type} from "packages/java/awt/$Dimension"

export interface $Scrollable {

 "getScrollableTracksViewportWidth"(): boolean
 "getPreferredScrollableViewportSize"(): $Dimension
 "getScrollableUnitIncrement"(arg0: $Rectangle$Type, arg1: integer, arg2: integer): integer
 "getScrollableBlockIncrement"(arg0: $Rectangle$Type, arg1: integer, arg2: integer): integer
 "getScrollableTracksViewportHeight"(): boolean
}

export namespace $Scrollable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Scrollable$Type = ($Scrollable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Scrollable_ = $Scrollable$Type;
}}
declare module "packages/javax/swing/$JList" {
import {$ListSelectionListener, $ListSelectionListener$Type} from "packages/javax/swing/event/$ListSelectionListener"
import {$JList$DropLocation, $JList$DropLocation$Type} from "packages/javax/swing/$JList$DropLocation"
import {$JComponent, $JComponent$Type} from "packages/javax/swing/$JComponent"
import {$Accessible, $Accessible$Type} from "packages/javax/accessibility/$Accessible"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$AccessibleContext, $AccessibleContext$Type} from "packages/javax/accessibility/$AccessibleContext"
import {$ListUI, $ListUI$Type} from "packages/javax/swing/plaf/$ListUI"
import {$ListModel, $ListModel$Type} from "packages/javax/swing/$ListModel"
import {$Position$Bias, $Position$Bias$Type} from "packages/javax/swing/text/$Position$Bias"
import {$Vector, $Vector$Type} from "packages/java/util/$Vector"
import {$Color, $Color$Type} from "packages/java/awt/$Color"
import {$MouseEvent, $MouseEvent$Type} from "packages/java/awt/event/$MouseEvent"
import {$Scrollable, $Scrollable$Type} from "packages/javax/swing/$Scrollable"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Dimension, $Dimension$Type} from "packages/java/awt/$Dimension"
import {$ListCellRenderer, $ListCellRenderer$Type} from "packages/javax/swing/$ListCellRenderer"
import {$ListSelectionModel, $ListSelectionModel$Type} from "packages/javax/swing/$ListSelectionModel"
import {$DropMode, $DropMode$Type} from "packages/javax/swing/$DropMode"

export class $JList<E> extends $JComponent implements $Scrollable, $Accessible {
static readonly "VERTICAL": integer
static readonly "VERTICAL_WRAP": integer
static readonly "HORIZONTAL_WRAP": integer
static readonly "WHEN_FOCUSED": integer
static readonly "WHEN_ANCESTOR_OF_FOCUSED_COMPONENT": integer
static readonly "WHEN_IN_FOCUSED_WINDOW": integer
static readonly "UNDEFINED_CONDITION": integer
static readonly "TOOL_TIP_TEXT_KEY": string
static readonly "TOP_ALIGNMENT": float
static readonly "CENTER_ALIGNMENT": float
static readonly "BOTTOM_ALIGNMENT": float
static readonly "LEFT_ALIGNMENT": float
static readonly "RIGHT_ALIGNMENT": float

constructor()
constructor(arg0: $Vector$Type<(any)>)
constructor(arg0: (E)[])
constructor(arg0: $ListModel$Type<(E)>)

public "setSelectedValue"(arg0: any, arg1: boolean): void
public "getSelectedIndex"(): integer
public "getModel"(): $ListModel<(E)>
public "getSelectedValue"(): E
public "getCellRenderer"(): $ListCellRenderer<(any)>
public "getPrototypeCellValue"(): E
public "locationToIndex"(arg0: $Point$Type): integer
public "getCellBounds"(arg0: integer, arg1: integer): $Rectangle
public "getLayoutOrientation"(): integer
public "setSelectedIndices"(arg0: (integer)[]): void
public "getSelectedIndices"(): (integer)[]
public "getAnchorSelectionIndex"(): integer
public "getLeadSelectionIndex"(): integer
public "setSelectionInterval"(arg0: integer, arg1: integer): void
public "isSelectedIndex"(arg0: integer): boolean
public "indexToLocation"(arg0: integer): $Point
public "addListSelectionListener"(arg0: $ListSelectionListener$Type): void
public "removeListSelectionListener"(arg0: $ListSelectionListener$Type): void
public "setSelectionMode"(arg0: integer): void
public "getSelectionMode"(): integer
public "getMinSelectionIndex"(): integer
public "getMaxSelectionIndex"(): integer
public "isSelectionEmpty"(): boolean
public "addSelectionInterval"(arg0: integer, arg1: integer): void
public "removeSelectionInterval"(arg0: integer, arg1: integer): void
public "ensureIndexIsVisible"(arg0: integer): void
public "getVisibleRowCount"(): integer
public "getFixedCellWidth"(): integer
public "getFixedCellHeight"(): integer
public "getFirstVisibleIndex"(): integer
public "setPrototypeCellValue"(arg0: E): void
public "setFixedCellWidth"(arg0: integer): void
public "setFixedCellHeight"(arg0: integer): void
public "setCellRenderer"(arg0: $ListCellRenderer$Type<(any)>): void
public "getSelectionForeground"(): $Color
public "setSelectionForeground"(arg0: $Color$Type): void
public "getSelectionBackground"(): $Color
public "setSelectionBackground"(arg0: $Color$Type): void
public "setVisibleRowCount"(arg0: integer): void
public "setLayoutOrientation"(arg0: integer): void
public "getLastVisibleIndex"(): integer
public "getListSelectionListeners"(): ($ListSelectionListener)[]
/**
 * 
 * @deprecated
 */
public "getSelectedValues"(): (any)[]
public "getSelectedValuesList"(): $List<(E)>
public "setSelectedIndex"(arg0: integer): void
public "getAccessibleContext"(): $AccessibleContext
public "getValueIsAdjusting"(): boolean
public "getNextMatch"(arg0: string, arg1: integer, arg2: $Position$Bias$Type): integer
public "clearSelection"(): void
public "setValueIsAdjusting"(arg0: boolean): void
public "setModel"(arg0: $ListModel$Type<(E)>): void
public "getScrollableTracksViewportWidth"(): boolean
public "getPreferredScrollableViewportSize"(): $Dimension
public "getScrollableUnitIncrement"(arg0: $Rectangle$Type, arg1: integer, arg2: integer): integer
public "setDragEnabled"(arg0: boolean): void
public "getDragEnabled"(): boolean
public "setDropMode"(arg0: $DropMode$Type): void
public "getDropMode"(): $DropMode
public "getDropLocation"(): $JList$DropLocation
public "getScrollableBlockIncrement"(arg0: $Rectangle$Type, arg1: integer, arg2: integer): integer
public "getScrollableTracksViewportHeight"(): boolean
public "setListData"(arg0: (E)[]): void
public "setListData"(arg0: $Vector$Type<(any)>): void
public "getToolTipText"(arg0: $MouseEvent$Type): string
public "updateUI"(): void
public "setSelectionModel"(arg0: $ListSelectionModel$Type): void
public "setUI"(arg0: $ListUI$Type): void
public "getUI"(): $ListUI
public "getSelectionModel"(): $ListSelectionModel
public "getUIClassID"(): string
get "selectedIndex"(): integer
get "model"(): $ListModel<(E)>
get "selectedValue"(): E
get "cellRenderer"(): $ListCellRenderer<(any)>
get "prototypeCellValue"(): E
get "layoutOrientation"(): integer
set "selectedIndices"(value: (integer)[])
get "selectedIndices"(): (integer)[]
get "anchorSelectionIndex"(): integer
get "leadSelectionIndex"(): integer
set "selectionMode"(value: integer)
get "selectionMode"(): integer
get "minSelectionIndex"(): integer
get "maxSelectionIndex"(): integer
get "selectionEmpty"(): boolean
get "visibleRowCount"(): integer
get "fixedCellWidth"(): integer
get "fixedCellHeight"(): integer
get "firstVisibleIndex"(): integer
set "prototypeCellValue"(value: E)
set "fixedCellWidth"(value: integer)
set "fixedCellHeight"(value: integer)
set "cellRenderer"(value: $ListCellRenderer$Type<(any)>)
get "selectionForeground"(): $Color
set "selectionForeground"(value: $Color$Type)
get "selectionBackground"(): $Color
set "selectionBackground"(value: $Color$Type)
set "visibleRowCount"(value: integer)
set "layoutOrientation"(value: integer)
get "lastVisibleIndex"(): integer
get "listSelectionListeners"(): ($ListSelectionListener)[]
get "selectedValues"(): (any)[]
get "selectedValuesList"(): $List<(E)>
set "selectedIndex"(value: integer)
get "accessibleContext"(): $AccessibleContext
get "valueIsAdjusting"(): boolean
set "valueIsAdjusting"(value: boolean)
set "model"(value: $ListModel$Type<(E)>)
get "scrollableTracksViewportWidth"(): boolean
get "preferredScrollableViewportSize"(): $Dimension
set "dragEnabled"(value: boolean)
get "dragEnabled"(): boolean
set "dropMode"(value: $DropMode$Type)
get "dropMode"(): $DropMode
get "dropLocation"(): $JList$DropLocation
get "scrollableTracksViewportHeight"(): boolean
set "listData"(value: (E)[])
set "listData"(value: $Vector$Type<(any)>)
set "selectionModel"(value: $ListSelectionModel$Type)
set "uI"(value: $ListUI$Type)
get "uI"(): $ListUI
get "selectionModel"(): $ListSelectionModel
get "uIClassID"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JList$Type<E> = ($JList<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JList_<E> = $JList$Type<(E)>;
}}
declare module "packages/javax/swing/$SwingConstants" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $SwingConstants {

}

export namespace $SwingConstants {
const CENTER: integer
const TOP: integer
const LEFT: integer
const BOTTOM: integer
const RIGHT: integer
const NORTH: integer
const NORTH_EAST: integer
const EAST: integer
const SOUTH_EAST: integer
const SOUTH: integer
const SOUTH_WEST: integer
const WEST: integer
const NORTH_WEST: integer
const HORIZONTAL: integer
const VERTICAL: integer
const LEADING: integer
const TRAILING: integer
const NEXT: integer
const PREVIOUS: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SwingConstants$Type = ($SwingConstants);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SwingConstants_ = $SwingConstants$Type;
}}
declare module "packages/javax/swing/event/$AncestorEvent" {
import {$AWTEvent, $AWTEvent$Type} from "packages/java/awt/$AWTEvent"
import {$Container, $Container$Type} from "packages/java/awt/$Container"
import {$JComponent, $JComponent$Type} from "packages/javax/swing/$JComponent"

export class $AncestorEvent extends $AWTEvent {
static readonly "ANCESTOR_ADDED": integer
static readonly "ANCESTOR_REMOVED": integer
static readonly "ANCESTOR_MOVED": integer
static readonly "COMPONENT_EVENT_MASK": long
static readonly "CONTAINER_EVENT_MASK": long
static readonly "FOCUS_EVENT_MASK": long
static readonly "KEY_EVENT_MASK": long
static readonly "MOUSE_EVENT_MASK": long
static readonly "MOUSE_MOTION_EVENT_MASK": long
static readonly "WINDOW_EVENT_MASK": long
static readonly "ACTION_EVENT_MASK": long
static readonly "ADJUSTMENT_EVENT_MASK": long
static readonly "ITEM_EVENT_MASK": long
static readonly "TEXT_EVENT_MASK": long
static readonly "INPUT_METHOD_EVENT_MASK": long
static readonly "PAINT_EVENT_MASK": long
static readonly "INVOCATION_EVENT_MASK": long
static readonly "HIERARCHY_EVENT_MASK": long
static readonly "HIERARCHY_BOUNDS_EVENT_MASK": long
static readonly "MOUSE_WHEEL_EVENT_MASK": long
static readonly "WINDOW_STATE_EVENT_MASK": long
static readonly "WINDOW_FOCUS_EVENT_MASK": long
static readonly "RESERVED_ID_MAX": integer

constructor(arg0: $JComponent$Type, arg1: integer, arg2: $Container$Type, arg3: $Container$Type)

public "getComponent"(): $JComponent
public "getAncestorParent"(): $Container
public "getAncestor"(): $Container
get "component"(): $JComponent
get "ancestorParent"(): $Container
get "ancestor"(): $Container
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AncestorEvent$Type = ($AncestorEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AncestorEvent_ = $AncestorEvent$Type;
}}
declare module "packages/javax/swing/text/$NavigationFilter" {
import {$JTextComponent, $JTextComponent$Type} from "packages/javax/swing/text/$JTextComponent"
import {$Position$Bias, $Position$Bias$Type} from "packages/javax/swing/text/$Position$Bias"
import {$NavigationFilter$FilterBypass, $NavigationFilter$FilterBypass$Type} from "packages/javax/swing/text/$NavigationFilter$FilterBypass"

export class $NavigationFilter {

constructor()

public "setDot"(arg0: $NavigationFilter$FilterBypass$Type, arg1: integer, arg2: $Position$Bias$Type): void
public "moveDot"(arg0: $NavigationFilter$FilterBypass$Type, arg1: integer, arg2: $Position$Bias$Type): void
public "getNextVisualPositionFrom"(arg0: $JTextComponent$Type, arg1: integer, arg2: $Position$Bias$Type, arg3: integer, arg4: ($Position$Bias$Type)[]): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NavigationFilter$Type = ($NavigationFilter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NavigationFilter_ = $NavigationFilter$Type;
}}
declare module "packages/javax/swing/event/$AncestorListener" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$AncestorEvent, $AncestorEvent$Type} from "packages/javax/swing/event/$AncestorEvent"

export interface $AncestorListener extends $EventListener {

 "ancestorMoved"(arg0: $AncestorEvent$Type): void
 "ancestorAdded"(arg0: $AncestorEvent$Type): void
 "ancestorRemoved"(arg0: $AncestorEvent$Type): void
}

export namespace $AncestorListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AncestorListener$Type = ($AncestorListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AncestorListener_ = $AncestorListener$Type;
}}
declare module "packages/javax/swing/$JMenuItem" {
import {$ButtonModel, $ButtonModel$Type} from "packages/javax/swing/$ButtonModel"
import {$MenuSelectionManager, $MenuSelectionManager$Type} from "packages/javax/swing/$MenuSelectionManager"
import {$MenuKeyListener, $MenuKeyListener$Type} from "packages/javax/swing/event/$MenuKeyListener"
import {$Accessible, $Accessible$Type} from "packages/javax/accessibility/$Accessible"
import {$Action, $Action$Type} from "packages/javax/swing/$Action"
import {$AccessibleContext, $AccessibleContext$Type} from "packages/javax/accessibility/$AccessibleContext"
import {$MenuKeyEvent, $MenuKeyEvent$Type} from "packages/javax/swing/event/$MenuKeyEvent"
import {$MenuDragMouseListener, $MenuDragMouseListener$Type} from "packages/javax/swing/event/$MenuDragMouseListener"
import {$KeyStroke, $KeyStroke$Type} from "packages/javax/swing/$KeyStroke"
import {$Component, $Component$Type} from "packages/java/awt/$Component"
import {$Icon, $Icon$Type} from "packages/javax/swing/$Icon"
import {$MouseEvent, $MouseEvent$Type} from "packages/java/awt/event/$MouseEvent"
import {$MenuDragMouseEvent, $MenuDragMouseEvent$Type} from "packages/javax/swing/event/$MenuDragMouseEvent"
import {$MenuItemUI, $MenuItemUI$Type} from "packages/javax/swing/plaf/$MenuItemUI"
import {$KeyEvent, $KeyEvent$Type} from "packages/java/awt/event/$KeyEvent"
import {$AbstractButton, $AbstractButton$Type} from "packages/javax/swing/$AbstractButton"
import {$MenuElement, $MenuElement$Type} from "packages/javax/swing/$MenuElement"

export class $JMenuItem extends $AbstractButton implements $Accessible, $MenuElement {
static readonly "MODEL_CHANGED_PROPERTY": string
static readonly "TEXT_CHANGED_PROPERTY": string
static readonly "MNEMONIC_CHANGED_PROPERTY": string
static readonly "MARGIN_CHANGED_PROPERTY": string
static readonly "VERTICAL_ALIGNMENT_CHANGED_PROPERTY": string
static readonly "HORIZONTAL_ALIGNMENT_CHANGED_PROPERTY": string
static readonly "VERTICAL_TEXT_POSITION_CHANGED_PROPERTY": string
static readonly "HORIZONTAL_TEXT_POSITION_CHANGED_PROPERTY": string
static readonly "BORDER_PAINTED_CHANGED_PROPERTY": string
static readonly "FOCUS_PAINTED_CHANGED_PROPERTY": string
static readonly "ROLLOVER_ENABLED_CHANGED_PROPERTY": string
static readonly "CONTENT_AREA_FILLED_CHANGED_PROPERTY": string
static readonly "ICON_CHANGED_PROPERTY": string
static readonly "PRESSED_ICON_CHANGED_PROPERTY": string
static readonly "SELECTED_ICON_CHANGED_PROPERTY": string
static readonly "ROLLOVER_ICON_CHANGED_PROPERTY": string
static readonly "ROLLOVER_SELECTED_ICON_CHANGED_PROPERTY": string
static readonly "DISABLED_ICON_CHANGED_PROPERTY": string
static readonly "DISABLED_SELECTED_ICON_CHANGED_PROPERTY": string
static readonly "WHEN_FOCUSED": integer
static readonly "WHEN_ANCESTOR_OF_FOCUSED_COMPONENT": integer
static readonly "WHEN_IN_FOCUSED_WINDOW": integer
static readonly "UNDEFINED_CONDITION": integer
static readonly "TOOL_TIP_TEXT_KEY": string
static readonly "TOP_ALIGNMENT": float
static readonly "CENTER_ALIGNMENT": float
static readonly "BOTTOM_ALIGNMENT": float
static readonly "LEFT_ALIGNMENT": float
static readonly "RIGHT_ALIGNMENT": float

constructor(arg0: string, arg1: integer)
constructor(arg0: string, arg1: $Icon$Type)
constructor(arg0: $Action$Type)
constructor()
constructor(arg0: $Icon$Type)
constructor(arg0: string)

public "setEnabled"(arg0: boolean): void
public "isArmed"(): boolean
public "setArmed"(arg0: boolean): void
public "setAccelerator"(arg0: $KeyStroke$Type): void
public "processMenuDragMouseEvent"(arg0: $MenuDragMouseEvent$Type): void
public "getAccelerator"(): $KeyStroke
public "addMenuDragMouseListener"(arg0: $MenuDragMouseListener$Type): void
public "removeMenuDragMouseListener"(arg0: $MenuDragMouseListener$Type): void
public "getMenuDragMouseListeners"(): ($MenuDragMouseListener)[]
public "getComponent"(): $Component
public "processKeyEvent"(arg0: $KeyEvent$Type, arg1: ($MenuElement$Type)[], arg2: $MenuSelectionManager$Type): void
public "processMouseEvent"(arg0: $MouseEvent$Type, arg1: ($MenuElement$Type)[], arg2: $MenuSelectionManager$Type): void
public "getAccessibleContext"(): $AccessibleContext
public "processMenuKeyEvent"(arg0: $MenuKeyEvent$Type): void
public "addMenuKeyListener"(arg0: $MenuKeyListener$Type): void
public "removeMenuKeyListener"(arg0: $MenuKeyListener$Type): void
public "getMenuKeyListeners"(): ($MenuKeyListener)[]
public "setModel"(arg0: $ButtonModel$Type): void
public "updateUI"(): void
public "setUI"(arg0: $MenuItemUI$Type): void
public "getSubElements"(): ($MenuElement)[]
public "getUIClassID"(): string
public "menuSelectionChanged"(arg0: boolean): void
set "enabled"(value: boolean)
get "armed"(): boolean
set "armed"(value: boolean)
set "accelerator"(value: $KeyStroke$Type)
get "accelerator"(): $KeyStroke
get "menuDragMouseListeners"(): ($MenuDragMouseListener)[]
get "component"(): $Component
get "accessibleContext"(): $AccessibleContext
get "menuKeyListeners"(): ($MenuKeyListener)[]
set "model"(value: $ButtonModel$Type)
set "uI"(value: $MenuItemUI$Type)
get "subElements"(): ($MenuElement)[]
get "uIClassID"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JMenuItem$Type = ($JMenuItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JMenuItem_ = $JMenuItem$Type;
}}
declare module "packages/javax/swing/$BoundedRangeModel" {
import {$ChangeListener, $ChangeListener$Type} from "packages/javax/swing/event/$ChangeListener"

export interface $BoundedRangeModel {

 "getValue"(): integer
 "setValue"(arg0: integer): void
 "getMaximum"(): integer
 "getMinimum"(): integer
 "getExtent"(): integer
 "getValueIsAdjusting"(): boolean
 "setExtent"(arg0: integer): void
 "setValueIsAdjusting"(arg0: boolean): void
 "setRangeProperties"(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: boolean): void
 "removeChangeListener"(arg0: $ChangeListener$Type): void
 "setMaximum"(arg0: integer): void
 "setMinimum"(arg0: integer): void
 "addChangeListener"(arg0: $ChangeListener$Type): void
}

export namespace $BoundedRangeModel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BoundedRangeModel$Type = ($BoundedRangeModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BoundedRangeModel_ = $BoundedRangeModel$Type;
}}
declare module "packages/javax/swing/text/$JTextComponent" {
import {$Point2D, $Point2D$Type} from "packages/java/awt/geom/$Point2D"
import {$MessageFormat, $MessageFormat$Type} from "packages/java/text/$MessageFormat"
import {$CaretListener, $CaretListener$Type} from "packages/javax/swing/event/$CaretListener"
import {$PrintRequestAttributeSet, $PrintRequestAttributeSet$Type} from "packages/javax/print/attribute/$PrintRequestAttributeSet"
import {$JTextComponent$DropLocation, $JTextComponent$DropLocation$Type} from "packages/javax/swing/text/$JTextComponent$DropLocation"
import {$PrintService, $PrintService$Type} from "packages/javax/print/$PrintService"
import {$JComponent, $JComponent$Type} from "packages/javax/swing/$JComponent"
import {$Accessible, $Accessible$Type} from "packages/javax/accessibility/$Accessible"
import {$Action, $Action$Type} from "packages/javax/swing/$Action"
import {$InputMethodListener, $InputMethodListener$Type} from "packages/java/awt/event/$InputMethodListener"
import {$JTextComponent$KeyBinding, $JTextComponent$KeyBinding$Type} from "packages/javax/swing/text/$JTextComponent$KeyBinding"
import {$Color, $Color$Type} from "packages/java/awt/$Color"
import {$Scrollable, $Scrollable$Type} from "packages/javax/swing/$Scrollable"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$Rectangle2D, $Rectangle2D$Type} from "packages/java/awt/geom/$Rectangle2D"
import {$Caret, $Caret$Type} from "packages/javax/swing/text/$Caret"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"
import {$DropMode, $DropMode$Type} from "packages/javax/swing/$DropMode"
import {$ComponentOrientation, $ComponentOrientation$Type} from "packages/java/awt/$ComponentOrientation"
import {$Keymap, $Keymap$Type} from "packages/javax/swing/text/$Keymap"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$Insets, $Insets$Type} from "packages/java/awt/$Insets"
import {$Document, $Document$Type} from "packages/javax/swing/text/$Document"
import {$AccessibleContext, $AccessibleContext$Type} from "packages/javax/accessibility/$AccessibleContext"
import {$TextUI, $TextUI$Type} from "packages/javax/swing/plaf/$TextUI"
import {$Highlighter, $Highlighter$Type} from "packages/javax/swing/text/$Highlighter"
import {$Writer, $Writer$Type} from "packages/java/io/$Writer"
import {$NavigationFilter, $NavigationFilter$Type} from "packages/javax/swing/text/$NavigationFilter"
import {$MouseEvent, $MouseEvent$Type} from "packages/java/awt/event/$MouseEvent"
import {$InputMethodRequests, $InputMethodRequests$Type} from "packages/java/awt/im/$InputMethodRequests"
import {$Dimension, $Dimension$Type} from "packages/java/awt/$Dimension"
import {$Printable, $Printable$Type} from "packages/java/awt/print/$Printable"

export class $JTextComponent extends $JComponent implements $Scrollable, $Accessible {
static readonly "FOCUS_ACCELERATOR_KEY": string
static readonly "DEFAULT_KEYMAP": string
static readonly "WHEN_FOCUSED": integer
static readonly "WHEN_ANCESTOR_OF_FOCUSED_COMPONENT": integer
static readonly "WHEN_IN_FOCUSED_WINDOW": integer
static readonly "UNDEFINED_CONDITION": integer
static readonly "TOOL_TIP_TEXT_KEY": string
static readonly "TOP_ALIGNMENT": float
static readonly "CENTER_ALIGNMENT": float
static readonly "BOTTOM_ALIGNMENT": float
static readonly "LEFT_ALIGNMENT": float
static readonly "RIGHT_ALIGNMENT": float

constructor()

public "write"(arg0: $Writer$Type): void
public "read"(arg0: $Reader$Type, arg1: any): void
public "getActions"(): ($Action)[]
public "print"(): boolean
public "print"(arg0: $MessageFormat$Type, arg1: $MessageFormat$Type): boolean
public "print"(arg0: $MessageFormat$Type, arg1: $MessageFormat$Type, arg2: boolean, arg3: $PrintService$Type, arg4: $PrintRequestAttributeSet$Type, arg5: boolean): boolean
public "copy"(): void
public "getText"(arg0: integer, arg1: integer): string
public "getText"(): string
public "setText"(arg0: string): void
public "getMargin"(): $Insets
public "getSelectionStart"(): integer
public "getSelectionEnd"(): integer
public "getHighlighter"(): $Highlighter
public "isEditable"(): boolean
public "setEditable"(arg0: boolean): void
public "removeNotify"(): void
public "getDocument"(): $Document
public "addInputMethodListener"(arg0: $InputMethodListener$Type): void
public "setComponentOrientation"(arg0: $ComponentOrientation$Type): void
public "getAccessibleContext"(): $AccessibleContext
public "getInputMethodRequests"(): $InputMethodRequests
public "setDocument"(arg0: $Document$Type): void
public "getScrollableTracksViewportWidth"(): boolean
public "getPreferredScrollableViewportSize"(): $Dimension
public "getScrollableUnitIncrement"(arg0: $Rectangle$Type, arg1: integer, arg2: integer): integer
public "setDragEnabled"(arg0: boolean): void
/**
 * 
 * @deprecated
 */
public "viewToModel"(arg0: $Point$Type): integer
public static "addKeymap"(arg0: string, arg1: $Keymap$Type): $Keymap
/**
 * 
 * @deprecated
 */
public "modelToView"(arg0: integer): $Rectangle
public "modelToView2D"(arg0: integer): $Rectangle2D
public "viewToModel2D"(arg0: $Point2D$Type): integer
public "setCaretPosition"(arg0: integer): void
public "moveCaretPosition"(arg0: integer): void
public "getPrintable"(arg0: $MessageFormat$Type, arg1: $MessageFormat$Type): $Printable
public "replaceSelection"(arg0: string): void
public "getKeymap"(): $Keymap
public static "getKeymap"(arg0: string): $Keymap
public "setSelectedTextColor"(arg0: $Color$Type): void
public "getSelectedText"(): string
public "selectAll"(): void
public "paste"(): void
public "select"(arg0: integer, arg1: integer): void
public "getCaret"(): $Caret
public "setCaret"(arg0: $Caret$Type): void
public "addCaretListener"(arg0: $CaretListener$Type): void
public "removeCaretListener"(arg0: $CaretListener$Type): void
public "getCaretListeners"(): ($CaretListener)[]
public "setNavigationFilter"(arg0: $NavigationFilter$Type): void
public "getNavigationFilter"(): $NavigationFilter
public "setHighlighter"(arg0: $Highlighter$Type): void
public "setKeymap"(arg0: $Keymap$Type): void
public "getDragEnabled"(): boolean
public "setDropMode"(arg0: $DropMode$Type): void
public "getDropMode"(): $DropMode
public "getDropLocation"(): $JTextComponent$DropLocation
public static "removeKeymap"(arg0: string): $Keymap
public static "loadKeymap"(arg0: $Keymap$Type, arg1: ($JTextComponent$KeyBinding$Type)[], arg2: ($Action$Type)[]): void
public "getCaretColor"(): $Color
public "setCaretColor"(arg0: $Color$Type): void
public "getSelectionColor"(): $Color
public "setSelectionColor"(arg0: $Color$Type): void
public "getSelectedTextColor"(): $Color
public "getDisabledTextColor"(): $Color
public "setDisabledTextColor"(arg0: $Color$Type): void
public "setFocusAccelerator"(arg0: character): void
public "getFocusAccelerator"(): character
public "getCaretPosition"(): integer
public "setSelectionStart"(arg0: integer): void
public "setSelectionEnd"(arg0: integer): void
public "getScrollableBlockIncrement"(arg0: $Rectangle$Type, arg1: integer, arg2: integer): integer
public "getScrollableTracksViewportHeight"(): boolean
public "cut"(): void
public "getToolTipText"(arg0: $MouseEvent$Type): string
public "updateUI"(): void
public "setUI"(arg0: $TextUI$Type): void
public "setMargin"(arg0: $Insets$Type): void
get "actions"(): ($Action)[]
get "text"(): string
set "text"(value: string)
get "margin"(): $Insets
get "selectionStart"(): integer
get "selectionEnd"(): integer
get "highlighter"(): $Highlighter
get "editable"(): boolean
set "editable"(value: boolean)
get "document"(): $Document
set "componentOrientation"(value: $ComponentOrientation$Type)
get "accessibleContext"(): $AccessibleContext
get "inputMethodRequests"(): $InputMethodRequests
set "document"(value: $Document$Type)
get "scrollableTracksViewportWidth"(): boolean
get "preferredScrollableViewportSize"(): $Dimension
set "dragEnabled"(value: boolean)
set "caretPosition"(value: integer)
get "keymap"(): $Keymap
set "selectedTextColor"(value: $Color$Type)
get "selectedText"(): string
get "caret"(): $Caret
set "caret"(value: $Caret$Type)
get "caretListeners"(): ($CaretListener)[]
set "navigationFilter"(value: $NavigationFilter$Type)
get "navigationFilter"(): $NavigationFilter
set "highlighter"(value: $Highlighter$Type)
set "keymap"(value: $Keymap$Type)
get "dragEnabled"(): boolean
set "dropMode"(value: $DropMode$Type)
get "dropMode"(): $DropMode
get "dropLocation"(): $JTextComponent$DropLocation
get "caretColor"(): $Color
set "caretColor"(value: $Color$Type)
get "selectionColor"(): $Color
set "selectionColor"(value: $Color$Type)
get "selectedTextColor"(): $Color
get "disabledTextColor"(): $Color
set "disabledTextColor"(value: $Color$Type)
set "focusAccelerator"(value: character)
get "focusAccelerator"(): character
get "caretPosition"(): integer
set "selectionStart"(value: integer)
set "selectionEnd"(value: integer)
get "scrollableTracksViewportHeight"(): boolean
set "uI"(value: $TextUI$Type)
set "margin"(value: $Insets$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JTextComponent$Type = ($JTextComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JTextComponent_ = $JTextComponent$Type;
}}
declare module "packages/javax/swing/$JComponent" {
import {$JPopupMenu, $JPopupMenu$Type} from "packages/javax/swing/$JPopupMenu"
import {$Component$BaselineResizeBehavior, $Component$BaselineResizeBehavior$Type} from "packages/java/awt/$Component$BaselineResizeBehavior"
import {$AncestorListener, $AncestorListener$Type} from "packages/javax/swing/event/$AncestorListener"
import {$VetoableChangeListener, $VetoableChangeListener$Type} from "packages/java/beans/$VetoableChangeListener"
import {$Graphics, $Graphics$Type} from "packages/java/awt/$Graphics"
import {$Component, $Component$Type} from "packages/java/awt/$Component"
import {$Border, $Border$Type} from "packages/javax/swing/border/$Border"
import {$Color, $Color$Type} from "packages/java/awt/$Color"
import {$ComponentUI, $ComponentUI$Type} from "packages/javax/swing/plaf/$ComponentUI"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$TransferHandler, $TransferHandler$Type} from "packages/javax/swing/$TransferHandler"
import {$JRootPane, $JRootPane$Type} from "packages/javax/swing/$JRootPane"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$ActionMap, $ActionMap$Type} from "packages/javax/swing/$ActionMap"
import {$Container, $Container$Type} from "packages/java/awt/$Container"
import {$InputMap, $InputMap$Type} from "packages/javax/swing/$InputMap"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$FontMetrics, $FontMetrics$Type} from "packages/java/awt/$FontMetrics"
import {$Insets, $Insets$Type} from "packages/java/awt/$Insets"
import {$JToolTip, $JToolTip$Type} from "packages/javax/swing/$JToolTip"
import {$Font, $Font$Type} from "packages/java/awt/$Font"
import {$KeyStroke, $KeyStroke$Type} from "packages/javax/swing/$KeyStroke"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"
import {$TransferHandler$HasGetTransferHandler, $TransferHandler$HasGetTransferHandler$Type} from "packages/javax/swing/$TransferHandler$HasGetTransferHandler"
import {$InputVerifier, $InputVerifier$Type} from "packages/javax/swing/$InputVerifier"
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$ActionListener, $ActionListener$Type} from "packages/java/awt/event/$ActionListener"
import {$MouseEvent, $MouseEvent$Type} from "packages/java/awt/event/$MouseEvent"
import {$Dimension, $Dimension$Type} from "packages/java/awt/$Dimension"

export class $JComponent extends $Container implements $Serializable, $TransferHandler$HasGetTransferHandler {
static readonly "WHEN_FOCUSED": integer
static readonly "WHEN_ANCESTOR_OF_FOCUSED_COMPONENT": integer
static readonly "WHEN_IN_FOCUSED_WINDOW": integer
static readonly "UNDEFINED_CONDITION": integer
static readonly "TOOL_TIP_TEXT_KEY": string
static readonly "TOP_ALIGNMENT": float
static readonly "CENTER_ALIGNMENT": float
static readonly "BOTTOM_ALIGNMENT": float
static readonly "LEFT_ALIGNMENT": float
static readonly "RIGHT_ALIGNMENT": float

constructor()

public "update"(arg0: $Graphics$Type): void
public "contains"(arg0: integer, arg1: integer): boolean
public "getBounds"(arg0: $Rectangle$Type): $Rectangle
public "getLocation"(arg0: $Point$Type): $Point
public "print"(arg0: $Graphics$Type): void
public "getSize"(arg0: $Dimension$Type): $Dimension
public "setOpaque"(arg0: boolean): void
public "isOpaque"(): boolean
/**
 * 
 * @deprecated
 */
public "enable"(): void
public "getY"(): integer
public "setEnabled"(arg0: boolean): void
public "getGraphics"(): $Graphics
public "setVisible"(arg0: boolean): void
public "getListeners"<T extends $EventListener>(arg0: $Class$Type<(T)>): (T)[]
public "getX"(): integer
public "getBaseline"(arg0: integer, arg1: integer): integer
/**
 * 
 * @deprecated
 */
public "hide"(): void
public "getWidth"(): integer
public "getHeight"(): integer
public "firePropertyChange"(arg0: string, arg1: character, arg2: character): void
public "firePropertyChange"(arg0: string, arg1: integer, arg2: integer): void
public "firePropertyChange"(arg0: string, arg1: boolean, arg2: boolean): void
public "removeNotify"(): void
public "addNotify"(): void
public "repaint"(arg0: long, arg1: integer, arg2: integer, arg3: integer, arg4: integer): void
public "repaint"(arg0: $Rectangle$Type): void
public "getPreferredSize"(): $Dimension
public "getMinimumSize"(): $Dimension
public "isValidateRoot"(): boolean
public "getInsets"(arg0: $Insets$Type): $Insets
public "getInsets"(): $Insets
public "requestFocusInWindow"(): boolean
public "isDoubleBuffered"(): boolean
public "setPreferredSize"(arg0: $Dimension$Type): void
public "setMinimumSize"(arg0: $Dimension$Type): void
public "setMaximumSize"(arg0: $Dimension$Type): void
public "getMaximumSize"(): $Dimension
public "getAlignmentX"(): float
public "getAlignmentY"(): float
public "getBaselineResizeBehavior"(): $Component$BaselineResizeBehavior
public "revalidate"(): void
public "printAll"(arg0: $Graphics$Type): void
public "setFocusTraversalKeys"(arg0: integer, arg1: $Set$Type<(any)>): void
public "setBackground"(arg0: $Color$Type): void
public "setForeground"(arg0: $Color$Type): void
public "setFont"(arg0: $Font$Type): void
public "getBorder"(): $Border
public "setBorder"(arg0: $Border$Type): void
public "paint"(arg0: $Graphics$Type): void
public "requestFocus"(): void
public "requestFocus"(arg0: boolean): boolean
public "createToolTip"(): $JToolTip
/**
 * 
 * @deprecated
 */
public "disable"(): void
public "getFontMetrics"(arg0: $Font$Type): $FontMetrics
/**
 * 
 * @deprecated
 */
public "reshape"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
/**
 * 
 * @deprecated
 */
public "getNextFocusableComponent"(): $Component
/**
 * 
 * @deprecated
 */
public "setNextFocusableComponent"(arg0: $Component$Type): void
public "getClientProperty"(arg0: any): any
public "getInputMap"(): $InputMap
public "getInputMap"(arg0: integer): $InputMap
public "getActionMap"(): $ActionMap
public "registerKeyboardAction"(arg0: $ActionListener$Type, arg1: string, arg2: $KeyStroke$Type, arg3: integer): void
public "registerKeyboardAction"(arg0: $ActionListener$Type, arg1: $KeyStroke$Type, arg2: integer): void
public "setInputMap"(arg0: integer, arg1: $InputMap$Type): void
public "setActionMap"(arg0: $ActionMap$Type): void
public static "setDefaultLocale"(arg0: $Locale$Type): void
public "getToolTipText"(): string
public "getToolTipText"(arg0: $MouseEvent$Type): string
public "scrollRectToVisible"(arg0: $Rectangle$Type): void
public "setAutoscrolls"(arg0: boolean): void
public "computeVisibleRect"(arg0: $Rectangle$Type): void
public "addVetoableChangeListener"(arg0: $VetoableChangeListener$Type): void
public "removeVetoableChangeListener"(arg0: $VetoableChangeListener$Type): void
public "getVetoableChangeListeners"(): ($VetoableChangeListener)[]
public "addAncestorListener"(arg0: $AncestorListener$Type): void
public "removeAncestorListener"(arg0: $AncestorListener$Type): void
public "getAncestorListeners"(): ($AncestorListener)[]
public "paintImmediately"(arg0: $Rectangle$Type): void
public "paintImmediately"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "setInheritsPopupMenu"(arg0: boolean): void
public "setComponentPopupMenu"(arg0: $JPopupMenu$Type): void
public "isPaintingTile"(): boolean
public "isPaintingForPrint"(): boolean
public "setRequestFocusEnabled"(arg0: boolean): void
public "isRequestFocusEnabled"(): boolean
public "grabFocus"(): void
public "setVerifyInputWhenFocusTarget"(arg0: boolean): void
public "getVerifyInputWhenFocusTarget"(): boolean
public "setAlignmentY"(arg0: float): void
public "setAlignmentX"(arg0: float): void
public "setInputVerifier"(arg0: $InputVerifier$Type): void
public "getInputVerifier"(): $InputVerifier
public "setDebugGraphicsOptions"(arg0: integer): void
public "getDebugGraphicsOptions"(): integer
public "unregisterKeyboardAction"(arg0: $KeyStroke$Type): void
public "getRegisteredKeyStrokes"(): ($KeyStroke)[]
public "getConditionForKeyStroke"(arg0: $KeyStroke$Type): integer
public "getActionForKeyStroke"(arg0: $KeyStroke$Type): $ActionListener
public "resetKeyboardActions"(): void
/**
 * 
 * @deprecated
 */
public "requestDefaultFocus"(): boolean
public "setToolTipText"(arg0: string): void
public "getToolTipLocation"(arg0: $MouseEvent$Type): $Point
public "getPopupLocation"(arg0: $MouseEvent$Type): $Point
public "getAutoscrolls"(): boolean
public "setTransferHandler"(arg0: $TransferHandler$Type): void
public "getTransferHandler"(): $TransferHandler
public "getVisibleRect"(): $Rectangle
public "getTopLevelAncestor"(): $Container
public "setDoubleBuffered"(arg0: boolean): void
public "updateUI"(): void
public "getUI"(): $ComponentUI
public "getUIClassID"(): string
public "getRootPane"(): $JRootPane
public "getInheritsPopupMenu"(): boolean
public "getComponentPopupMenu"(): $JPopupMenu
/**
 * 
 * @deprecated
 */
public "isManagingFocus"(): boolean
public static "getDefaultLocale"(): $Locale
public "putClientProperty"(arg0: any, arg1: any): void
public "isOptimizedDrawingEnabled"(): boolean
public static "isLightweightComponent"(arg0: $Component$Type): boolean
set "opaque"(value: boolean)
get "opaque"(): boolean
get "y"(): integer
set "enabled"(value: boolean)
get "graphics"(): $Graphics
set "visible"(value: boolean)
get "x"(): integer
get "width"(): integer
get "height"(): integer
get "preferredSize"(): $Dimension
get "minimumSize"(): $Dimension
get "validateRoot"(): boolean
get "insets"(): $Insets
get "doubleBuffered"(): boolean
set "preferredSize"(value: $Dimension$Type)
set "minimumSize"(value: $Dimension$Type)
set "maximumSize"(value: $Dimension$Type)
get "maximumSize"(): $Dimension
get "alignmentX"(): float
get "alignmentY"(): float
get "baselineResizeBehavior"(): $Component$BaselineResizeBehavior
set "background"(value: $Color$Type)
set "foreground"(value: $Color$Type)
set "font"(value: $Font$Type)
get "border"(): $Border
set "border"(value: $Border$Type)
get "nextFocusableComponent"(): $Component
set "nextFocusableComponent"(value: $Component$Type)
get "inputMap"(): $InputMap
get "actionMap"(): $ActionMap
set "actionMap"(value: $ActionMap$Type)
set "defaultLocale"(value: $Locale$Type)
get "toolTipText"(): string
set "autoscrolls"(value: boolean)
get "vetoableChangeListeners"(): ($VetoableChangeListener)[]
get "ancestorListeners"(): ($AncestorListener)[]
set "inheritsPopupMenu"(value: boolean)
set "componentPopupMenu"(value: $JPopupMenu$Type)
get "paintingTile"(): boolean
get "paintingForPrint"(): boolean
set "requestFocusEnabled"(value: boolean)
get "requestFocusEnabled"(): boolean
set "verifyInputWhenFocusTarget"(value: boolean)
get "verifyInputWhenFocusTarget"(): boolean
set "alignmentY"(value: float)
set "alignmentX"(value: float)
set "inputVerifier"(value: $InputVerifier$Type)
get "inputVerifier"(): $InputVerifier
set "debugGraphicsOptions"(value: integer)
get "debugGraphicsOptions"(): integer
get "registeredKeyStrokes"(): ($KeyStroke)[]
set "toolTipText"(value: string)
get "autoscrolls"(): boolean
set "transferHandler"(value: $TransferHandler$Type)
get "transferHandler"(): $TransferHandler
get "visibleRect"(): $Rectangle
get "topLevelAncestor"(): $Container
set "doubleBuffered"(value: boolean)
get "uI"(): $ComponentUI
get "uIClassID"(): string
get "rootPane"(): $JRootPane
get "inheritsPopupMenu"(): boolean
get "componentPopupMenu"(): $JPopupMenu
get "managingFocus"(): boolean
get "defaultLocale"(): $Locale
get "optimizedDrawingEnabled"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JComponent$Type = ($JComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JComponent_ = $JComponent$Type;
}}
declare module "packages/javax/swing/plaf/$ListUI" {
import {$JList, $JList$Type} from "packages/javax/swing/$JList"
import {$ComponentUI, $ComponentUI$Type} from "packages/javax/swing/plaf/$ComponentUI"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$Point, $Point$Type} from "packages/java/awt/$Point"

export class $ListUI extends $ComponentUI {


public "locationToIndex"(arg0: $JList$Type<(any)>, arg1: $Point$Type): integer
public "getCellBounds"(arg0: $JList$Type<(any)>, arg1: integer, arg2: integer): $Rectangle
public "indexToLocation"(arg0: $JList$Type<(any)>, arg1: integer): $Point
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListUI$Type = ($ListUI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListUI_ = $ListUI$Type;
}}
declare module "packages/javax/swing/text/$Highlighter$HighlightPainter" {
import {$JTextComponent, $JTextComponent$Type} from "packages/javax/swing/text/$JTextComponent"
import {$Graphics, $Graphics$Type} from "packages/java/awt/$Graphics"
import {$Shape, $Shape$Type} from "packages/java/awt/$Shape"

export interface $Highlighter$HighlightPainter {

 "paint"(arg0: $Graphics$Type, arg1: integer, arg2: integer, arg3: $Shape$Type, arg4: $JTextComponent$Type): void

(arg0: $Graphics$Type, arg1: integer, arg2: integer, arg3: $Shape$Type, arg4: $JTextComponent$Type): void
}

export namespace $Highlighter$HighlightPainter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Highlighter$HighlightPainter$Type = ($Highlighter$HighlightPainter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Highlighter$HighlightPainter_ = $Highlighter$HighlightPainter$Type;
}}
declare module "packages/javax/swing/event/$DocumentEvent$ElementChange" {
import {$Element, $Element$Type} from "packages/javax/swing/text/$Element"

export interface $DocumentEvent$ElementChange {

 "getIndex"(): integer
 "getElement"(): $Element
 "getChildrenRemoved"(): ($Element)[]
 "getChildrenAdded"(): ($Element)[]
}

export namespace $DocumentEvent$ElementChange {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DocumentEvent$ElementChange$Type = ($DocumentEvent$ElementChange);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DocumentEvent$ElementChange_ = $DocumentEvent$ElementChange$Type;
}}
declare module "packages/javax/swing/event/$CaretEvent" {
import {$EventObject, $EventObject$Type} from "packages/java/util/$EventObject"

export class $CaretEvent extends $EventObject {

constructor(arg0: any)

public "getDot"(): integer
public "getMark"(): integer
get "dot"(): integer
get "mark"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CaretEvent$Type = ($CaretEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CaretEvent_ = $CaretEvent$Type;
}}
declare module "packages/javax/swing/event/$UndoableEditListener" {
import {$UndoableEditEvent, $UndoableEditEvent$Type} from "packages/javax/swing/event/$UndoableEditEvent"
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"

export interface $UndoableEditListener extends $EventListener {

 "undoableEditHappened"(arg0: $UndoableEditEvent$Type): void

(arg0: $UndoableEditEvent$Type): void
}

export namespace $UndoableEditListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UndoableEditListener$Type = ($UndoableEditListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UndoableEditListener_ = $UndoableEditListener$Type;
}}
declare module "packages/javax/swing/plaf/$ToolTipUI" {
import {$ComponentUI, $ComponentUI$Type} from "packages/javax/swing/plaf/$ComponentUI"

export class $ToolTipUI extends $ComponentUI {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ToolTipUI$Type = ($ToolTipUI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ToolTipUI_ = $ToolTipUI$Type;
}}
declare module "packages/javax/swing/$JMenu" {
import {$ComponentOrientation, $ComponentOrientation$Type} from "packages/java/awt/$ComponentOrientation"
import {$JPopupMenu, $JPopupMenu$Type} from "packages/javax/swing/$JPopupMenu"
import {$ButtonModel, $ButtonModel$Type} from "packages/javax/swing/$ButtonModel"
import {$MenuSelectionManager, $MenuSelectionManager$Type} from "packages/javax/swing/$MenuSelectionManager"
import {$Accessible, $Accessible$Type} from "packages/javax/accessibility/$Accessible"
import {$Action, $Action$Type} from "packages/javax/swing/$Action"
import {$AccessibleContext, $AccessibleContext$Type} from "packages/javax/accessibility/$AccessibleContext"
import {$KeyStroke, $KeyStroke$Type} from "packages/javax/swing/$KeyStroke"
import {$Component, $Component$Type} from "packages/java/awt/$Component"
import {$MenuListener, $MenuListener$Type} from "packages/javax/swing/event/$MenuListener"
import {$MouseEvent, $MouseEvent$Type} from "packages/java/awt/event/$MouseEvent"
import {$JMenuItem, $JMenuItem$Type} from "packages/javax/swing/$JMenuItem"
import {$KeyEvent, $KeyEvent$Type} from "packages/java/awt/event/$KeyEvent"
import {$MenuElement, $MenuElement$Type} from "packages/javax/swing/$MenuElement"

export class $JMenu extends $JMenuItem implements $Accessible, $MenuElement {
static readonly "MODEL_CHANGED_PROPERTY": string
static readonly "TEXT_CHANGED_PROPERTY": string
static readonly "MNEMONIC_CHANGED_PROPERTY": string
static readonly "MARGIN_CHANGED_PROPERTY": string
static readonly "VERTICAL_ALIGNMENT_CHANGED_PROPERTY": string
static readonly "HORIZONTAL_ALIGNMENT_CHANGED_PROPERTY": string
static readonly "VERTICAL_TEXT_POSITION_CHANGED_PROPERTY": string
static readonly "HORIZONTAL_TEXT_POSITION_CHANGED_PROPERTY": string
static readonly "BORDER_PAINTED_CHANGED_PROPERTY": string
static readonly "FOCUS_PAINTED_CHANGED_PROPERTY": string
static readonly "ROLLOVER_ENABLED_CHANGED_PROPERTY": string
static readonly "CONTENT_AREA_FILLED_CHANGED_PROPERTY": string
static readonly "ICON_CHANGED_PROPERTY": string
static readonly "PRESSED_ICON_CHANGED_PROPERTY": string
static readonly "SELECTED_ICON_CHANGED_PROPERTY": string
static readonly "ROLLOVER_ICON_CHANGED_PROPERTY": string
static readonly "ROLLOVER_SELECTED_ICON_CHANGED_PROPERTY": string
static readonly "DISABLED_ICON_CHANGED_PROPERTY": string
static readonly "DISABLED_SELECTED_ICON_CHANGED_PROPERTY": string
static readonly "WHEN_FOCUSED": integer
static readonly "WHEN_ANCESTOR_OF_FOCUSED_COMPONENT": integer
static readonly "WHEN_IN_FOCUSED_WINDOW": integer
static readonly "UNDEFINED_CONDITION": integer
static readonly "TOOL_TIP_TEXT_KEY": string
static readonly "TOP_ALIGNMENT": float
static readonly "CENTER_ALIGNMENT": float
static readonly "BOTTOM_ALIGNMENT": float
static readonly "LEFT_ALIGNMENT": float
static readonly "RIGHT_ALIGNMENT": float

constructor(arg0: string, arg1: boolean)
constructor(arg0: $Action$Type)
constructor(arg0: string)
constructor()

public "add"(arg0: string): $JMenuItem
public "add"(arg0: $Action$Type): $JMenuItem
public "add"(arg0: $Component$Type, arg1: integer): $Component
public "add"(arg0: $Component$Type): $Component
public "add"(arg0: $JMenuItem$Type): $JMenuItem
public "remove"(arg0: $JMenuItem$Type): void
public "remove"(arg0: $Component$Type): void
public "remove"(arg0: integer): void
public "insert"(arg0: $Action$Type, arg1: integer): $JMenuItem
public "insert"(arg0: string, arg1: integer): void
public "insert"(arg0: $JMenuItem$Type, arg1: integer): $JMenuItem
public "removeAll"(): void
public "getItemCount"(): integer
public "getItem"(arg0: integer): $JMenuItem
public "setAccelerator"(arg0: $KeyStroke$Type): void
public "doClick"(arg0: integer): void
public "addSeparator"(): void
public "getComponent"(): $Component
public "getDelay"(): integer
public "isSelected"(): boolean
public "setDelay"(arg0: integer): void
public "setComponentOrientation"(arg0: $ComponentOrientation$Type): void
public "getAccessibleContext"(): $AccessibleContext
public "applyComponentOrientation"(arg0: $ComponentOrientation$Type): void
public "setPopupMenuVisible"(arg0: boolean): void
public "setModel"(arg0: $ButtonModel$Type): void
public "setSelected"(arg0: boolean): void
public "isPopupMenuVisible"(): boolean
public "getMenuComponent"(arg0: integer): $Component
public "getMenuComponentCount"(): integer
public "isMenuComponent"(arg0: $Component$Type): boolean
public "setMenuLocation"(arg0: integer, arg1: integer): void
public "isTopLevelMenu"(): boolean
public "addMenuListener"(arg0: $MenuListener$Type): void
public "removeMenuListener"(arg0: $MenuListener$Type): void
public "getMenuListeners"(): ($MenuListener)[]
public "getPopupMenu"(): $JPopupMenu
public "isTearOff"(): boolean
public "insertSeparator"(arg0: integer): void
public "getMenuComponents"(): ($Component)[]
public "updateUI"(): void
public "getSubElements"(): ($MenuElement)[]
public "getUIClassID"(): string
public "menuSelectionChanged"(arg0: boolean): void
public "processKeyEvent"(arg0: $KeyEvent$Type, arg1: ($MenuElement$Type)[], arg2: $MenuSelectionManager$Type): void
public "processMouseEvent"(arg0: $MouseEvent$Type, arg1: ($MenuElement$Type)[], arg2: $MenuSelectionManager$Type): void
get "itemCount"(): integer
set "accelerator"(value: $KeyStroke$Type)
get "component"(): $Component
get "delay"(): integer
get "selected"(): boolean
set "delay"(value: integer)
set "componentOrientation"(value: $ComponentOrientation$Type)
get "accessibleContext"(): $AccessibleContext
set "popupMenuVisible"(value: boolean)
set "model"(value: $ButtonModel$Type)
set "selected"(value: boolean)
get "popupMenuVisible"(): boolean
get "menuComponentCount"(): integer
get "topLevelMenu"(): boolean
get "menuListeners"(): ($MenuListener)[]
get "popupMenu"(): $JPopupMenu
get "tearOff"(): boolean
get "menuComponents"(): ($Component)[]
get "subElements"(): ($MenuElement)[]
get "uIClassID"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JMenu$Type = ($JMenu);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JMenu_ = $JMenu$Type;
}}
declare module "packages/javax/swing/$TransferHandler" {
import {$DataFlavor, $DataFlavor$Type} from "packages/java/awt/datatransfer/$DataFlavor"
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Icon, $Icon$Type} from "packages/javax/swing/$Icon"
import {$JComponent, $JComponent$Type} from "packages/javax/swing/$JComponent"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$Action, $Action$Type} from "packages/javax/swing/$Action"
import {$InputEvent, $InputEvent$Type} from "packages/java/awt/event/$InputEvent"
import {$TransferHandler$TransferSupport, $TransferHandler$TransferSupport$Type} from "packages/javax/swing/$TransferHandler$TransferSupport"
import {$Image, $Image$Type} from "packages/java/awt/$Image"
import {$Transferable, $Transferable$Type} from "packages/java/awt/datatransfer/$Transferable"
import {$Clipboard, $Clipboard$Type} from "packages/java/awt/datatransfer/$Clipboard"

export class $TransferHandler implements $Serializable {
static readonly "NONE": integer
static readonly "COPY": integer
static readonly "MOVE": integer
static readonly "COPY_OR_MOVE": integer
static readonly "LINK": integer

constructor(arg0: string)

public "getSourceActions"(arg0: $JComponent$Type): integer
public "importData"(arg0: $JComponent$Type, arg1: $Transferable$Type): boolean
public "importData"(arg0: $TransferHandler$TransferSupport$Type): boolean
public "canImport"(arg0: $TransferHandler$TransferSupport$Type): boolean
public "canImport"(arg0: $JComponent$Type, arg1: ($DataFlavor$Type)[]): boolean
public static "getCutAction"(): $Action
public static "getCopyAction"(): $Action
public static "getPasteAction"(): $Action
public "setDragImage"(arg0: $Image$Type): void
public "getDragImage"(): $Image
public "setDragImageOffset"(arg0: $Point$Type): void
public "getDragImageOffset"(): $Point
public "exportAsDrag"(arg0: $JComponent$Type, arg1: $InputEvent$Type, arg2: integer): void
public "exportToClipboard"(arg0: $JComponent$Type, arg1: $Clipboard$Type, arg2: integer): void
public "getVisualRepresentation"(arg0: $Transferable$Type): $Icon
get "cutAction"(): $Action
get "copyAction"(): $Action
get "pasteAction"(): $Action
set "dragImage"(value: $Image$Type)
get "dragImage"(): $Image
set "dragImageOffset"(value: $Point$Type)
get "dragImageOffset"(): $Point
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TransferHandler$Type = ($TransferHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TransferHandler_ = $TransferHandler$Type;
}}
declare module "packages/javax/swing/event/$ListDataEvent" {
import {$EventObject, $EventObject$Type} from "packages/java/util/$EventObject"

export class $ListDataEvent extends $EventObject {
static readonly "CONTENTS_CHANGED": integer
static readonly "INTERVAL_ADDED": integer
static readonly "INTERVAL_REMOVED": integer

constructor(arg0: any, arg1: integer, arg2: integer, arg3: integer)

public "toString"(): string
public "getType"(): integer
public "getIndex0"(): integer
public "getIndex1"(): integer
get "type"(): integer
get "index0"(): integer
get "index1"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListDataEvent$Type = ($ListDataEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListDataEvent_ = $ListDataEvent$Type;
}}
declare module "packages/javax/swing/plaf/$ViewportUI" {
import {$ComponentUI, $ComponentUI$Type} from "packages/javax/swing/plaf/$ComponentUI"

export class $ViewportUI extends $ComponentUI {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ViewportUI$Type = ($ViewportUI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ViewportUI_ = $ViewportUI$Type;
}}
declare module "packages/javax/swing/$ListModel" {
import {$ListDataListener, $ListDataListener$Type} from "packages/javax/swing/event/$ListDataListener"

export interface $ListModel<E> {

 "getSize"(): integer
 "getElementAt"(arg0: integer): E
 "addListDataListener"(arg0: $ListDataListener$Type): void
 "removeListDataListener"(arg0: $ListDataListener$Type): void
}

export namespace $ListModel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListModel$Type<E> = ($ListModel<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListModel_<E> = $ListModel$Type<(E)>;
}}
declare module "packages/javax/swing/text/$Keymap" {
import {$Action, $Action$Type} from "packages/javax/swing/$Action"
import {$KeyStroke, $KeyStroke$Type} from "packages/javax/swing/$KeyStroke"

export interface $Keymap {

 "getName"(): string
 "getAction"(arg0: $KeyStroke$Type): $Action
 "setDefaultAction"(arg0: $Action$Type): void
 "addActionForKeyStroke"(arg0: $KeyStroke$Type, arg1: $Action$Type): void
 "getDefaultAction"(): $Action
 "getBoundKeyStrokes"(): ($KeyStroke)[]
 "getBoundActions"(): ($Action)[]
 "getKeyStrokesForAction"(arg0: $Action$Type): ($KeyStroke)[]
 "isLocallyDefined"(arg0: $KeyStroke$Type): boolean
 "removeKeyStrokeBinding"(arg0: $KeyStroke$Type): void
 "removeBindings"(): void
 "getResolveParent"(): $Keymap
 "setResolveParent"(arg0: $Keymap$Type): void
}

export namespace $Keymap {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Keymap$Type = ($Keymap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Keymap_ = $Keymap$Type;
}}
declare module "packages/javax/swing/$ListSelectionModel" {
import {$ListSelectionListener, $ListSelectionListener$Type} from "packages/javax/swing/event/$ListSelectionListener"

export interface $ListSelectionModel {

 "getSelectedIndices"(): (integer)[]
 "getAnchorSelectionIndex"(): integer
 "getLeadSelectionIndex"(): integer
 "setAnchorSelectionIndex"(arg0: integer): void
 "setLeadSelectionIndex"(arg0: integer): void
 "setSelectionInterval"(arg0: integer, arg1: integer): void
 "isSelectedIndex"(arg0: integer): boolean
 "addListSelectionListener"(arg0: $ListSelectionListener$Type): void
 "removeListSelectionListener"(arg0: $ListSelectionListener$Type): void
 "setSelectionMode"(arg0: integer): void
 "getSelectionMode"(): integer
 "getMinSelectionIndex"(): integer
 "getMaxSelectionIndex"(): integer
 "isSelectionEmpty"(): boolean
 "addSelectionInterval"(arg0: integer, arg1: integer): void
 "removeSelectionInterval"(arg0: integer, arg1: integer): void
 "insertIndexInterval"(arg0: integer, arg1: integer, arg2: boolean): void
 "removeIndexInterval"(arg0: integer, arg1: integer): void
 "getSelectedItemsCount"(): integer
 "getValueIsAdjusting"(): boolean
 "clearSelection"(): void
 "setValueIsAdjusting"(arg0: boolean): void
}

export namespace $ListSelectionModel {
const SINGLE_SELECTION: integer
const SINGLE_INTERVAL_SELECTION: integer
const MULTIPLE_INTERVAL_SELECTION: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListSelectionModel$Type = ($ListSelectionModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListSelectionModel_ = $ListSelectionModel$Type;
}}
declare module "packages/javax/swing/event/$ListDataListener" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$ListDataEvent, $ListDataEvent$Type} from "packages/javax/swing/event/$ListDataEvent"

export interface $ListDataListener extends $EventListener {

 "contentsChanged"(arg0: $ListDataEvent$Type): void
 "intervalAdded"(arg0: $ListDataEvent$Type): void
 "intervalRemoved"(arg0: $ListDataEvent$Type): void
}

export namespace $ListDataListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListDataListener$Type = ($ListDataListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListDataListener_ = $ListDataListener$Type;
}}
declare module "packages/javax/swing/$JScrollBar" {
import {$AdjustmentListener, $AdjustmentListener$Type} from "packages/java/awt/event/$AdjustmentListener"
import {$JComponent, $JComponent$Type} from "packages/javax/swing/$JComponent"
import {$Accessible, $Accessible$Type} from "packages/javax/accessibility/$Accessible"
import {$AccessibleContext, $AccessibleContext$Type} from "packages/javax/accessibility/$AccessibleContext"
import {$Dimension, $Dimension$Type} from "packages/java/awt/$Dimension"
import {$ScrollBarUI, $ScrollBarUI$Type} from "packages/javax/swing/plaf/$ScrollBarUI"
import {$Adjustable, $Adjustable$Type} from "packages/java/awt/$Adjustable"
import {$BoundedRangeModel, $BoundedRangeModel$Type} from "packages/javax/swing/$BoundedRangeModel"

export class $JScrollBar extends $JComponent implements $Adjustable, $Accessible {
static readonly "WHEN_FOCUSED": integer
static readonly "WHEN_ANCESTOR_OF_FOCUSED_COMPONENT": integer
static readonly "WHEN_IN_FOCUSED_WINDOW": integer
static readonly "UNDEFINED_CONDITION": integer
static readonly "TOOL_TIP_TEXT_KEY": string
static readonly "TOP_ALIGNMENT": float
static readonly "CENTER_ALIGNMENT": float
static readonly "BOTTOM_ALIGNMENT": float
static readonly "LEFT_ALIGNMENT": float
static readonly "RIGHT_ALIGNMENT": float

constructor(arg0: integer)
constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer)
constructor()

public "getValue"(): integer
public "setValue"(arg0: integer): void
public "getMaximum"(): integer
public "getMinimum"(): integer
public "setEnabled"(arg0: boolean): void
public "setOrientation"(arg0: integer): void
public "getModel"(): $BoundedRangeModel
public "getMinimumSize"(): $Dimension
public "setValues"(arg0: integer, arg1: integer, arg2: integer, arg3: integer): void
public "getAccessibleContext"(): $AccessibleContext
public "getMaximumSize"(): $Dimension
public "getValueIsAdjusting"(): boolean
public "setValueIsAdjusting"(arg0: boolean): void
public "setModel"(arg0: $BoundedRangeModel$Type): void
public "getUnitIncrement"(arg0: integer): integer
public "getUnitIncrement"(): integer
public "setUnitIncrement"(arg0: integer): void
public "getBlockIncrement"(arg0: integer): integer
public "getBlockIncrement"(): integer
public "setBlockIncrement"(arg0: integer): void
public "getVisibleAmount"(): integer
public "setVisibleAmount"(arg0: integer): void
public "addAdjustmentListener"(arg0: $AdjustmentListener$Type): void
public "removeAdjustmentListener"(arg0: $AdjustmentListener$Type): void
public "getAdjustmentListeners"(): ($AdjustmentListener)[]
public "setMaximum"(arg0: integer): void
public "setMinimum"(arg0: integer): void
public "getOrientation"(): integer
public "updateUI"(): void
public "setUI"(arg0: $ScrollBarUI$Type): void
public "getUIClassID"(): string
get "value"(): integer
set "value"(value: integer)
get "maximum"(): integer
get "minimum"(): integer
set "enabled"(value: boolean)
set "orientation"(value: integer)
get "model"(): $BoundedRangeModel
get "minimumSize"(): $Dimension
get "accessibleContext"(): $AccessibleContext
get "maximumSize"(): $Dimension
get "valueIsAdjusting"(): boolean
set "valueIsAdjusting"(value: boolean)
set "model"(value: $BoundedRangeModel$Type)
get "unitIncrement"(): integer
set "unitIncrement"(value: integer)
get "blockIncrement"(): integer
set "blockIncrement"(value: integer)
get "visibleAmount"(): integer
set "visibleAmount"(value: integer)
get "adjustmentListeners"(): ($AdjustmentListener)[]
set "maximum"(value: integer)
set "minimum"(value: integer)
get "orientation"(): integer
set "uI"(value: $ScrollBarUI$Type)
get "uIClassID"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JScrollBar$Type = ($JScrollBar);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JScrollBar_ = $JScrollBar$Type;
}}
declare module "packages/javax/swing/$SingleSelectionModel" {
import {$ChangeListener, $ChangeListener$Type} from "packages/javax/swing/event/$ChangeListener"

export interface $SingleSelectionModel {

 "getSelectedIndex"(): integer
 "isSelected"(): boolean
 "setSelectedIndex"(arg0: integer): void
 "clearSelection"(): void
 "removeChangeListener"(arg0: $ChangeListener$Type): void
 "addChangeListener"(arg0: $ChangeListener$Type): void
}

export namespace $SingleSelectionModel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SingleSelectionModel$Type = ($SingleSelectionModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SingleSelectionModel_ = $SingleSelectionModel$Type;
}}
declare module "packages/javax/swing/event/$DocumentEvent$EventType" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $DocumentEvent$EventType {
static readonly "INSERT": $DocumentEvent$EventType
static readonly "REMOVE": $DocumentEvent$EventType
static readonly "CHANGE": $DocumentEvent$EventType


public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DocumentEvent$EventType$Type = ($DocumentEvent$EventType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DocumentEvent$EventType_ = $DocumentEvent$EventType$Type;
}}
declare module "packages/javax/swing/text/$Document" {
import {$DocumentListener, $DocumentListener$Type} from "packages/javax/swing/event/$DocumentListener"
import {$UndoableEditListener, $UndoableEditListener$Type} from "packages/javax/swing/event/$UndoableEditListener"
import {$Element, $Element$Type} from "packages/javax/swing/text/$Element"
import {$Segment, $Segment$Type} from "packages/javax/swing/text/$Segment"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$AttributeSet, $AttributeSet$Type} from "packages/javax/swing/text/$AttributeSet"
import {$Position, $Position$Type} from "packages/javax/swing/text/$Position"

export interface $Document {

 "remove"(arg0: integer, arg1: integer): void
 "getProperty"(arg0: any): any
 "getLength"(): integer
 "getText"(arg0: integer, arg1: integer, arg2: $Segment$Type): void
 "getText"(arg0: integer, arg1: integer): string
 "putProperty"(arg0: any, arg1: any): void
 "render"(arg0: $Runnable$Type): void
 "getDefaultRootElement"(): $Element
 "insertString"(arg0: integer, arg1: string, arg2: $AttributeSet$Type): void
 "removeDocumentListener"(arg0: $DocumentListener$Type): void
 "addDocumentListener"(arg0: $DocumentListener$Type): void
 "createPosition"(arg0: integer): $Position
 "addUndoableEditListener"(arg0: $UndoableEditListener$Type): void
 "removeUndoableEditListener"(arg0: $UndoableEditListener$Type): void
 "getStartPosition"(): $Position
 "getEndPosition"(): $Position
 "getRootElements"(): ($Element)[]
}

export namespace $Document {
const StreamDescriptionProperty: string
const TitleProperty: string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Document$Type = ($Document);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Document_ = $Document$Type;
}}
declare module "packages/javax/swing/$InputVerifier" {
import {$JComponent, $JComponent$Type} from "packages/javax/swing/$JComponent"

export class $InputVerifier {


public "verify"(arg0: $JComponent$Type): boolean
public "shouldYieldFocus"(arg0: $JComponent$Type, arg1: $JComponent$Type): boolean
/**
 * 
 * @deprecated
 */
public "shouldYieldFocus"(arg0: $JComponent$Type): boolean
public "verifyTarget"(arg0: $JComponent$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InputVerifier$Type = ($InputVerifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InputVerifier_ = $InputVerifier$Type;
}}
declare module "packages/javax/swing/event/$ListSelectionEvent" {
import {$EventObject, $EventObject$Type} from "packages/java/util/$EventObject"

export class $ListSelectionEvent extends $EventObject {

constructor(arg0: any, arg1: integer, arg2: integer, arg3: boolean)

public "toString"(): string
public "getValueIsAdjusting"(): boolean
public "getLastIndex"(): integer
public "getFirstIndex"(): integer
get "valueIsAdjusting"(): boolean
get "lastIndex"(): integer
get "firstIndex"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListSelectionEvent$Type = ($ListSelectionEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListSelectionEvent_ = $ListSelectionEvent$Type;
}}
declare module "packages/javax/swing/$JTextArea" {
import {$JTextComponent, $JTextComponent$Type} from "packages/javax/swing/text/$JTextComponent"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$Document, $Document$Type} from "packages/javax/swing/text/$Document"
import {$AccessibleContext, $AccessibleContext$Type} from "packages/javax/accessibility/$AccessibleContext"
import {$Dimension, $Dimension$Type} from "packages/java/awt/$Dimension"
import {$Font, $Font$Type} from "packages/java/awt/$Font"

export class $JTextArea extends $JTextComponent {
static readonly "FOCUS_ACCELERATOR_KEY": string
static readonly "DEFAULT_KEYMAP": string
static readonly "WHEN_FOCUSED": integer
static readonly "WHEN_ANCESTOR_OF_FOCUSED_COMPONENT": integer
static readonly "WHEN_IN_FOCUSED_WINDOW": integer
static readonly "UNDEFINED_CONDITION": integer
static readonly "TOOL_TIP_TEXT_KEY": string
static readonly "TOP_ALIGNMENT": float
static readonly "CENTER_ALIGNMENT": float
static readonly "BOTTOM_ALIGNMENT": float
static readonly "LEFT_ALIGNMENT": float
static readonly "RIGHT_ALIGNMENT": float

constructor(arg0: $Document$Type, arg1: string, arg2: integer, arg3: integer)
constructor(arg0: $Document$Type)
constructor(arg0: string, arg1: integer, arg2: integer)
constructor()
constructor(arg0: string)
constructor(arg0: integer, arg1: integer)

public "append"(arg0: string): void
public "insert"(arg0: string, arg1: integer): void
public "setRows"(arg0: integer): void
public "getRows"(): integer
public "getColumns"(): integer
public "getPreferredSize"(): $Dimension
public "getAccessibleContext"(): $AccessibleContext
public "getScrollableTracksViewportWidth"(): boolean
public "getPreferredScrollableViewportSize"(): $Dimension
public "setLineWrap"(arg0: boolean): void
public "getLineWrap"(): boolean
public "setWrapStyleWord"(arg0: boolean): void
public "getWrapStyleWord"(): boolean
public "getLineOfOffset"(arg0: integer): integer
public "getLineStartOffset"(arg0: integer): integer
public "getLineEndOffset"(arg0: integer): integer
public "replaceRange"(arg0: string, arg1: integer, arg2: integer): void
public "setColumns"(arg0: integer): void
public "getScrollableUnitIncrement"(arg0: $Rectangle$Type, arg1: integer, arg2: integer): integer
public "setFont"(arg0: $Font$Type): void
public "getLineCount"(): integer
public "getTabSize"(): integer
public "setTabSize"(arg0: integer): void
public "getUIClassID"(): string
set "rows"(value: integer)
get "rows"(): integer
get "columns"(): integer
get "preferredSize"(): $Dimension
get "accessibleContext"(): $AccessibleContext
get "scrollableTracksViewportWidth"(): boolean
get "preferredScrollableViewportSize"(): $Dimension
set "lineWrap"(value: boolean)
get "lineWrap"(): boolean
set "wrapStyleWord"(value: boolean)
get "wrapStyleWord"(): boolean
set "columns"(value: integer)
set "font"(value: $Font$Type)
get "lineCount"(): integer
get "tabSize"(): integer
set "tabSize"(value: integer)
get "uIClassID"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JTextArea$Type = ($JTextArea);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JTextArea_ = $JTextArea$Type;
}}
declare module "packages/javax/swing/event/$PopupMenuListener" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$PopupMenuEvent, $PopupMenuEvent$Type} from "packages/javax/swing/event/$PopupMenuEvent"

export interface $PopupMenuListener extends $EventListener {

 "popupMenuWillBecomeVisible"(arg0: $PopupMenuEvent$Type): void
 "popupMenuWillBecomeInvisible"(arg0: $PopupMenuEvent$Type): void
 "popupMenuCanceled"(arg0: $PopupMenuEvent$Type): void
}

export namespace $PopupMenuListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PopupMenuListener$Type = ($PopupMenuListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PopupMenuListener_ = $PopupMenuListener$Type;
}}
declare module "packages/javax/swing/$ButtonModel" {
import {$ItemSelectable, $ItemSelectable$Type} from "packages/java/awt/$ItemSelectable"
import {$ActionListener, $ActionListener$Type} from "packages/java/awt/event/$ActionListener"
import {$ButtonGroup, $ButtonGroup$Type} from "packages/javax/swing/$ButtonGroup"
import {$ItemListener, $ItemListener$Type} from "packages/java/awt/event/$ItemListener"
import {$ChangeListener, $ChangeListener$Type} from "packages/javax/swing/event/$ChangeListener"

export interface $ButtonModel extends $ItemSelectable {

 "isEnabled"(): boolean
 "addActionListener"(arg0: $ActionListener$Type): void
 "removeActionListener"(arg0: $ActionListener$Type): void
 "setActionCommand"(arg0: string): void
 "setGroup"(arg0: $ButtonGroup$Type): void
 "setEnabled"(arg0: boolean): void
 "isArmed"(): boolean
 "setArmed"(arg0: boolean): void
 "getMnemonic"(): integer
 "setRollover"(arg0: boolean): void
 "removeItemListener"(arg0: $ItemListener$Type): void
 "addItemListener"(arg0: $ItemListener$Type): void
 "isRollover"(): boolean
 "isSelected"(): boolean
 "setMnemonic"(arg0: integer): void
 "getActionCommand"(): string
 "isPressed"(): boolean
 "setPressed"(arg0: boolean): void
 "setSelected"(arg0: boolean): void
 "removeChangeListener"(arg0: $ChangeListener$Type): void
 "getGroup"(): $ButtonGroup
 "addChangeListener"(arg0: $ChangeListener$Type): void
 "getSelectedObjects"(): (any)[]
}

export namespace $ButtonModel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ButtonModel$Type = ($ButtonModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ButtonModel_ = $ButtonModel$Type;
}}
declare module "packages/javax/swing/$ButtonGroup" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$ButtonModel, $ButtonModel$Type} from "packages/javax/swing/$ButtonModel"
import {$Enumeration, $Enumeration$Type} from "packages/java/util/$Enumeration"
import {$AbstractButton, $AbstractButton$Type} from "packages/javax/swing/$AbstractButton"

export class $ButtonGroup implements $Serializable {

constructor()

public "add"(arg0: $AbstractButton$Type): void
public "remove"(arg0: $AbstractButton$Type): void
public "getElements"(): $Enumeration<($AbstractButton)>
public "getSelection"(): $ButtonModel
public "isSelected"(arg0: $ButtonModel$Type): boolean
public "clearSelection"(): void
public "setSelected"(arg0: $ButtonModel$Type, arg1: boolean): void
public "getButtonCount"(): integer
get "elements"(): $Enumeration<($AbstractButton)>
get "selection"(): $ButtonModel
get "buttonCount"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ButtonGroup$Type = ($ButtonGroup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ButtonGroup_ = $ButtonGroup$Type;
}}
declare module "packages/javax/swing/text/$NavigationFilter$FilterBypass" {
import {$Caret, $Caret$Type} from "packages/javax/swing/text/$Caret"
import {$Position$Bias, $Position$Bias$Type} from "packages/javax/swing/text/$Position$Bias"

export class $NavigationFilter$FilterBypass {


public "setDot"(arg0: integer, arg1: $Position$Bias$Type): void
public "moveDot"(arg0: integer, arg1: $Position$Bias$Type): void
public "getCaret"(): $Caret
get "caret"(): $Caret
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NavigationFilter$FilterBypass$Type = ($NavigationFilter$FilterBypass);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NavigationFilter$FilterBypass_ = $NavigationFilter$FilterBypass$Type;
}}
declare module "packages/javax/swing/text/$JTextComponent$KeyBinding" {
import {$KeyStroke, $KeyStroke$Type} from "packages/javax/swing/$KeyStroke"

export class $JTextComponent$KeyBinding {
 "key": $KeyStroke
 "actionName": string

constructor(arg0: $KeyStroke$Type, arg1: string)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JTextComponent$KeyBinding$Type = ($JTextComponent$KeyBinding);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JTextComponent$KeyBinding_ = $JTextComponent$KeyBinding$Type;
}}
declare module "packages/javax/swing/$JToolTip" {
import {$JComponent, $JComponent$Type} from "packages/javax/swing/$JComponent"
import {$Accessible, $Accessible$Type} from "packages/javax/accessibility/$Accessible"
import {$AccessibleContext, $AccessibleContext$Type} from "packages/javax/accessibility/$AccessibleContext"
import {$ToolTipUI, $ToolTipUI$Type} from "packages/javax/swing/plaf/$ToolTipUI"

export class $JToolTip extends $JComponent implements $Accessible {
static readonly "WHEN_FOCUSED": integer
static readonly "WHEN_ANCESTOR_OF_FOCUSED_COMPONENT": integer
static readonly "WHEN_IN_FOCUSED_WINDOW": integer
static readonly "UNDEFINED_CONDITION": integer
static readonly "TOOL_TIP_TEXT_KEY": string
static readonly "TOP_ALIGNMENT": float
static readonly "CENTER_ALIGNMENT": float
static readonly "BOTTOM_ALIGNMENT": float
static readonly "LEFT_ALIGNMENT": float
static readonly "RIGHT_ALIGNMENT": float

constructor()

public "getComponent"(): $JComponent
public "getAccessibleContext"(): $AccessibleContext
public "setTipText"(arg0: string): void
public "getTipText"(): string
public "setComponent"(arg0: $JComponent$Type): void
public "updateUI"(): void
public "getUI"(): $ToolTipUI
public "getUIClassID"(): string
get "component"(): $JComponent
get "accessibleContext"(): $AccessibleContext
set "tipText"(value: string)
get "tipText"(): string
set "component"(value: $JComponent$Type)
get "uI"(): $ToolTipUI
get "uIClassID"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JToolTip$Type = ($JToolTip);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JToolTip_ = $JToolTip$Type;
}}
declare module "packages/javax/swing/plaf/$MenuItemUI" {
import {$ButtonUI, $ButtonUI$Type} from "packages/javax/swing/plaf/$ButtonUI"

export class $MenuItemUI extends $ButtonUI {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuItemUI$Type = ($MenuItemUI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuItemUI_ = $MenuItemUI$Type;
}}
declare module "packages/javax/swing/event/$MenuListener" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$MenuEvent, $MenuEvent$Type} from "packages/javax/swing/event/$MenuEvent"

export interface $MenuListener extends $EventListener {

 "menuSelected"(arg0: $MenuEvent$Type): void
 "menuDeselected"(arg0: $MenuEvent$Type): void
 "menuCanceled"(arg0: $MenuEvent$Type): void
}

export namespace $MenuListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuListener$Type = ($MenuListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuListener_ = $MenuListener$Type;
}}
declare module "packages/javax/swing/plaf/$ScrollBarUI" {
import {$ComponentUI, $ComponentUI$Type} from "packages/javax/swing/plaf/$ComponentUI"

export class $ScrollBarUI extends $ComponentUI {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScrollBarUI$Type = ($ScrollBarUI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScrollBarUI_ = $ScrollBarUI$Type;
}}
declare module "packages/javax/swing/event/$ChangeListener" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$ChangeEvent, $ChangeEvent$Type} from "packages/javax/swing/event/$ChangeEvent"

export interface $ChangeListener extends $EventListener {

 "stateChanged"(arg0: $ChangeEvent$Type): void

(arg0: $ChangeEvent$Type): void
}

export namespace $ChangeListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChangeListener$Type = ($ChangeListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChangeListener_ = $ChangeListener$Type;
}}
declare module "packages/javax/swing/$ListCellRenderer" {
import {$JList, $JList$Type} from "packages/javax/swing/$JList"
import {$Component, $Component$Type} from "packages/java/awt/$Component"

export interface $ListCellRenderer<E> {

 "getListCellRendererComponent"(arg0: $JList$Type<(any)>, arg1: E, arg2: integer, arg3: boolean, arg4: boolean): $Component

(arg0: $JList$Type<(any)>, arg1: E, arg2: integer, arg3: boolean, arg4: boolean): $Component
}

export namespace $ListCellRenderer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListCellRenderer$Type<E> = ($ListCellRenderer<(E)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListCellRenderer_<E> = $ListCellRenderer$Type<(E)>;
}}
declare module "packages/javax/swing/event/$DocumentListener" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$DocumentEvent, $DocumentEvent$Type} from "packages/javax/swing/event/$DocumentEvent"

export interface $DocumentListener extends $EventListener {

 "insertUpdate"(arg0: $DocumentEvent$Type): void
 "removeUpdate"(arg0: $DocumentEvent$Type): void
 "changedUpdate"(arg0: $DocumentEvent$Type): void
}

export namespace $DocumentListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DocumentListener$Type = ($DocumentListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DocumentListener_ = $DocumentListener$Type;
}}
declare module "packages/javax/swing/text/$ViewFactory" {
import {$Element, $Element$Type} from "packages/javax/swing/text/$Element"
import {$View, $View$Type} from "packages/javax/swing/text/$View"

export interface $ViewFactory {

 "create"(arg0: $Element$Type): $View

(arg0: $Element$Type): $View
}

export namespace $ViewFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ViewFactory$Type = ($ViewFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ViewFactory_ = $ViewFactory$Type;
}}
declare module "packages/javax/swing/$TransferHandler$DropLocation" {
import {$Point, $Point$Type} from "packages/java/awt/$Point"

export class $TransferHandler$DropLocation {


public "toString"(): string
public "getDropPoint"(): $Point
get "dropPoint"(): $Point
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TransferHandler$DropLocation$Type = ($TransferHandler$DropLocation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TransferHandler$DropLocation_ = $TransferHandler$DropLocation$Type;
}}
declare module "packages/javax/swing/undo/$UndoableEdit" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $UndoableEdit {

 "canUndo"(): boolean
 "canRedo"(): boolean
 "redo"(): void
 "undo"(): void
 "addEdit"(arg0: $UndoableEdit$Type): boolean
 "die"(): void
 "replaceEdit"(arg0: $UndoableEdit$Type): boolean
 "isSignificant"(): boolean
 "getPresentationName"(): string
 "getUndoPresentationName"(): string
 "getRedoPresentationName"(): string
}

export namespace $UndoableEdit {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UndoableEdit$Type = ($UndoableEdit);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UndoableEdit_ = $UndoableEdit$Type;
}}
declare module "packages/javax/swing/plaf/$TextUI" {
import {$Point2D, $Point2D$Type} from "packages/java/awt/geom/$Point2D"
import {$ComponentUI, $ComponentUI$Type} from "packages/javax/swing/plaf/$ComponentUI"
import {$JTextComponent, $JTextComponent$Type} from "packages/javax/swing/text/$JTextComponent"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$EditorKit, $EditorKit$Type} from "packages/javax/swing/text/$EditorKit"
import {$View, $View$Type} from "packages/javax/swing/text/$View"
import {$Rectangle2D, $Rectangle2D$Type} from "packages/java/awt/geom/$Rectangle2D"
import {$Position$Bias, $Position$Bias$Type} from "packages/javax/swing/text/$Position$Bias"

export class $TextUI extends $ComponentUI {


public "getEditorKit"(arg0: $JTextComponent$Type): $EditorKit
/**
 * 
 * @deprecated
 */
public "viewToModel"(arg0: $JTextComponent$Type, arg1: $Point$Type): integer
/**
 * 
 * @deprecated
 */
public "viewToModel"(arg0: $JTextComponent$Type, arg1: $Point$Type, arg2: ($Position$Bias$Type)[]): integer
/**
 * 
 * @deprecated
 */
public "modelToView"(arg0: $JTextComponent$Type, arg1: integer, arg2: $Position$Bias$Type): $Rectangle
/**
 * 
 * @deprecated
 */
public "modelToView"(arg0: $JTextComponent$Type, arg1: integer): $Rectangle
public "modelToView2D"(arg0: $JTextComponent$Type, arg1: integer, arg2: $Position$Bias$Type): $Rectangle2D
public "viewToModel2D"(arg0: $JTextComponent$Type, arg1: $Point2D$Type, arg2: ($Position$Bias$Type)[]): integer
public "getRootView"(arg0: $JTextComponent$Type): $View
public "getNextVisualPositionFrom"(arg0: $JTextComponent$Type, arg1: integer, arg2: $Position$Bias$Type, arg3: integer, arg4: ($Position$Bias$Type)[]): integer
public "damageRange"(arg0: $JTextComponent$Type, arg1: integer, arg2: integer, arg3: $Position$Bias$Type, arg4: $Position$Bias$Type): void
public "damageRange"(arg0: $JTextComponent$Type, arg1: integer, arg2: integer): void
public "getToolTipText2D"(arg0: $JTextComponent$Type, arg1: $Point2D$Type): string
/**
 * 
 * @deprecated
 */
public "getToolTipText"(arg0: $JTextComponent$Type, arg1: $Point$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TextUI$Type = ($TextUI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TextUI_ = $TextUI$Type;
}}
declare module "packages/javax/swing/text/$Position$Bias" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Position$Bias {
static readonly "Forward": $Position$Bias
static readonly "Backward": $Position$Bias


public "toString"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Position$Bias$Type = ($Position$Bias);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Position$Bias_ = $Position$Bias$Type;
}}
declare module "packages/javax/swing/$ScrollPaneConstants" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ScrollPaneConstants {

}

export namespace $ScrollPaneConstants {
const VIEWPORT: string
const VERTICAL_SCROLLBAR: string
const HORIZONTAL_SCROLLBAR: string
const ROW_HEADER: string
const COLUMN_HEADER: string
const LOWER_LEFT_CORNER: string
const LOWER_RIGHT_CORNER: string
const UPPER_LEFT_CORNER: string
const UPPER_RIGHT_CORNER: string
const LOWER_LEADING_CORNER: string
const LOWER_TRAILING_CORNER: string
const UPPER_LEADING_CORNER: string
const UPPER_TRAILING_CORNER: string
const VERTICAL_SCROLLBAR_POLICY: string
const HORIZONTAL_SCROLLBAR_POLICY: string
const VERTICAL_SCROLLBAR_AS_NEEDED: integer
const VERTICAL_SCROLLBAR_NEVER: integer
const VERTICAL_SCROLLBAR_ALWAYS: integer
const HORIZONTAL_SCROLLBAR_AS_NEEDED: integer
const HORIZONTAL_SCROLLBAR_NEVER: integer
const HORIZONTAL_SCROLLBAR_ALWAYS: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScrollPaneConstants$Type = ($ScrollPaneConstants);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScrollPaneConstants_ = $ScrollPaneConstants$Type;
}}
declare module "packages/javax/swing/event/$ChangeEvent" {
import {$EventObject, $EventObject$Type} from "packages/java/util/$EventObject"

export class $ChangeEvent extends $EventObject {

constructor(arg0: any)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChangeEvent$Type = ($ChangeEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChangeEvent_ = $ChangeEvent$Type;
}}
declare module "packages/javax/swing/event/$UndoableEditEvent" {
import {$UndoableEdit, $UndoableEdit$Type} from "packages/javax/swing/undo/$UndoableEdit"
import {$EventObject, $EventObject$Type} from "packages/java/util/$EventObject"

export class $UndoableEditEvent extends $EventObject {

constructor(arg0: any, arg1: $UndoableEdit$Type)

public "getEdit"(): $UndoableEdit
get "edit"(): $UndoableEdit
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UndoableEditEvent$Type = ($UndoableEditEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UndoableEditEvent_ = $UndoableEditEvent$Type;
}}
declare module "packages/javax/swing/$TransferHandler$TransferSupport" {
import {$DataFlavor, $DataFlavor$Type} from "packages/java/awt/datatransfer/$DataFlavor"
import {$TransferHandler$DropLocation, $TransferHandler$DropLocation$Type} from "packages/javax/swing/$TransferHandler$DropLocation"
import {$Component, $Component$Type} from "packages/java/awt/$Component"
import {$Transferable, $Transferable$Type} from "packages/java/awt/datatransfer/$Transferable"

export class $TransferHandler$TransferSupport {

constructor(arg0: $Component$Type, arg1: $Transferable$Type)

public "isDataFlavorSupported"(arg0: $DataFlavor$Type): boolean
public "getComponent"(): $Component
public "getDropAction"(): integer
public "isDrop"(): boolean
public "getSourceDropActions"(): integer
public "getUserDropAction"(): integer
public "setShowDropLocation"(arg0: boolean): void
public "setDropAction"(arg0: integer): void
public "getTransferable"(): $Transferable
public "getDataFlavors"(): ($DataFlavor)[]
public "getDropLocation"(): $TransferHandler$DropLocation
get "component"(): $Component
get "dropAction"(): integer
get "drop"(): boolean
get "sourceDropActions"(): integer
get "userDropAction"(): integer
set "showDropLocation"(value: boolean)
set "dropAction"(value: integer)
get "transferable"(): $Transferable
get "dataFlavors"(): ($DataFlavor)[]
get "dropLocation"(): $TransferHandler$DropLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TransferHandler$TransferSupport$Type = ($TransferHandler$TransferSupport);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TransferHandler$TransferSupport_ = $TransferHandler$TransferSupport$Type;
}}
declare module "packages/javax/swing/$ActionMap" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$Action, $Action$Type} from "packages/javax/swing/$Action"

export class $ActionMap implements $Serializable {

constructor()

public "remove"(arg0: any): void
public "get"(arg0: any): $Action
public "put"(arg0: any, arg1: $Action$Type): void
public "clear"(): void
public "size"(): integer
public "getParent"(): $ActionMap
public "keys"(): (any)[]
public "setParent"(arg0: $ActionMap$Type): void
public "allKeys"(): (any)[]
get "parent"(): $ActionMap
set "parent"(value: $ActionMap$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ActionMap$Type = ($ActionMap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ActionMap_ = $ActionMap$Type;
}}
declare module "packages/javax/swing/event/$MenuDragMouseListener" {
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"
import {$MenuDragMouseEvent, $MenuDragMouseEvent$Type} from "packages/javax/swing/event/$MenuDragMouseEvent"

export interface $MenuDragMouseListener extends $EventListener {

 "menuDragMouseEntered"(arg0: $MenuDragMouseEvent$Type): void
 "menuDragMouseExited"(arg0: $MenuDragMouseEvent$Type): void
 "menuDragMouseDragged"(arg0: $MenuDragMouseEvent$Type): void
 "menuDragMouseReleased"(arg0: $MenuDragMouseEvent$Type): void
}

export namespace $MenuDragMouseListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuDragMouseListener$Type = ($MenuDragMouseListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuDragMouseListener_ = $MenuDragMouseListener$Type;
}}
declare module "packages/javax/swing/text/$EditorKit" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$OutputStream, $OutputStream$Type} from "packages/java/io/$OutputStream"
import {$ViewFactory, $ViewFactory$Type} from "packages/javax/swing/text/$ViewFactory"
import {$JEditorPane, $JEditorPane$Type} from "packages/javax/swing/$JEditorPane"
import {$Cloneable, $Cloneable$Type} from "packages/java/lang/$Cloneable"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Document, $Document$Type} from "packages/javax/swing/text/$Document"
import {$Action, $Action$Type} from "packages/javax/swing/$Action"
import {$Caret, $Caret$Type} from "packages/javax/swing/text/$Caret"
import {$Writer, $Writer$Type} from "packages/java/io/$Writer"
import {$Reader, $Reader$Type} from "packages/java/io/$Reader"

export class $EditorKit implements $Cloneable, $Serializable {

constructor()

public "clone"(): any
public "write"(arg0: $Writer$Type, arg1: $Document$Type, arg2: integer, arg3: integer): void
public "write"(arg0: $OutputStream$Type, arg1: $Document$Type, arg2: integer, arg3: integer): void
public "read"(arg0: $InputStream$Type, arg1: $Document$Type, arg2: integer): void
public "read"(arg0: $Reader$Type, arg1: $Document$Type, arg2: integer): void
public "getActions"(): ($Action)[]
public "getContentType"(): string
public "install"(arg0: $JEditorPane$Type): void
public "getViewFactory"(): $ViewFactory
public "createCaret"(): $Caret
public "deinstall"(arg0: $JEditorPane$Type): void
public "createDefaultDocument"(): $Document
get "actions"(): ($Action)[]
get "contentType"(): string
get "viewFactory"(): $ViewFactory
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EditorKit$Type = ($EditorKit);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EditorKit_ = $EditorKit$Type;
}}
declare module "packages/javax/swing/text/$Highlighter" {
import {$Highlighter$HighlightPainter, $Highlighter$HighlightPainter$Type} from "packages/javax/swing/text/$Highlighter$HighlightPainter"
import {$Highlighter$Highlight, $Highlighter$Highlight$Type} from "packages/javax/swing/text/$Highlighter$Highlight"
import {$JTextComponent, $JTextComponent$Type} from "packages/javax/swing/text/$JTextComponent"
import {$Graphics, $Graphics$Type} from "packages/java/awt/$Graphics"

export interface $Highlighter {

 "install"(arg0: $JTextComponent$Type): void
 "deinstall"(arg0: $JTextComponent$Type): void
 "paint"(arg0: $Graphics$Type): void
 "addHighlight"(arg0: integer, arg1: integer, arg2: $Highlighter$HighlightPainter$Type): any
 "removeHighlight"(arg0: any): void
 "removeAllHighlights"(): void
 "changeHighlight"(arg0: any, arg1: integer, arg2: integer): void
 "getHighlights"(): ($Highlighter$Highlight)[]
}

export namespace $Highlighter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Highlighter$Type = ($Highlighter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Highlighter_ = $Highlighter$Type;
}}
declare module "packages/javax/swing/event/$ListSelectionListener" {
import {$ListSelectionEvent, $ListSelectionEvent$Type} from "packages/javax/swing/event/$ListSelectionEvent"
import {$EventListener, $EventListener$Type} from "packages/java/util/$EventListener"

export interface $ListSelectionListener extends $EventListener {

 "valueChanged"(arg0: $ListSelectionEvent$Type): void

(arg0: $ListSelectionEvent$Type): void
}

export namespace $ListSelectionListener {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ListSelectionListener$Type = ($ListSelectionListener);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ListSelectionListener_ = $ListSelectionListener$Type;
}}
declare module "packages/javax/swing/event/$HyperlinkEvent" {
import {$Element, $Element$Type} from "packages/javax/swing/text/$Element"
import {$HyperlinkEvent$EventType, $HyperlinkEvent$EventType$Type} from "packages/javax/swing/event/$HyperlinkEvent$EventType"
import {$InputEvent, $InputEvent$Type} from "packages/java/awt/event/$InputEvent"
import {$URL, $URL$Type} from "packages/java/net/$URL"
import {$EventObject, $EventObject$Type} from "packages/java/util/$EventObject"

export class $HyperlinkEvent extends $EventObject {

constructor(arg0: any, arg1: $HyperlinkEvent$EventType$Type, arg2: $URL$Type, arg3: string, arg4: $Element$Type, arg5: $InputEvent$Type)
constructor(arg0: any, arg1: $HyperlinkEvent$EventType$Type, arg2: $URL$Type, arg3: string, arg4: $Element$Type)
constructor(arg0: any, arg1: $HyperlinkEvent$EventType$Type, arg2: $URL$Type, arg3: string)
constructor(arg0: any, arg1: $HyperlinkEvent$EventType$Type, arg2: $URL$Type)

public "getURL"(): $URL
public "getDescription"(): string
public "getEventType"(): $HyperlinkEvent$EventType
public "getSourceElement"(): $Element
public "getInputEvent"(): $InputEvent
get "uRL"(): $URL
get "description"(): string
get "eventType"(): $HyperlinkEvent$EventType
get "sourceElement"(): $Element
get "inputEvent"(): $InputEvent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HyperlinkEvent$Type = ($HyperlinkEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HyperlinkEvent_ = $HyperlinkEvent$Type;
}}
declare module "packages/javax/swing/$InputMap" {
import {$Serializable, $Serializable$Type} from "packages/java/io/$Serializable"
import {$KeyStroke, $KeyStroke$Type} from "packages/javax/swing/$KeyStroke"

export class $InputMap implements $Serializable {

constructor()

public "remove"(arg0: $KeyStroke$Type): void
public "get"(arg0: $KeyStroke$Type): any
public "put"(arg0: $KeyStroke$Type, arg1: any): void
public "clear"(): void
public "size"(): integer
public "getParent"(): $InputMap
public "keys"(): ($KeyStroke)[]
public "setParent"(arg0: $InputMap$Type): void
public "allKeys"(): ($KeyStroke)[]
get "parent"(): $InputMap
set "parent"(value: $InputMap$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InputMap$Type = ($InputMap);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InputMap_ = $InputMap$Type;
}}
declare module "packages/javax/swing/border/$Border" {
import {$Insets, $Insets$Type} from "packages/java/awt/$Insets"
import {$Graphics, $Graphics$Type} from "packages/java/awt/$Graphics"
import {$Component, $Component$Type} from "packages/java/awt/$Component"

export interface $Border {

 "isBorderOpaque"(): boolean
 "getBorderInsets"(arg0: $Component$Type): $Insets
 "paintBorder"(arg0: $Component$Type, arg1: $Graphics$Type, arg2: integer, arg3: integer, arg4: integer, arg5: integer): void
}

export namespace $Border {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Border$Type = ($Border);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Border_ = $Border$Type;
}}
declare module "packages/javax/swing/event/$MenuDragMouseEvent" {
import {$MenuSelectionManager, $MenuSelectionManager$Type} from "packages/javax/swing/$MenuSelectionManager"
import {$MouseEvent, $MouseEvent$Type} from "packages/java/awt/event/$MouseEvent"
import {$MenuElement, $MenuElement$Type} from "packages/javax/swing/$MenuElement"
import {$Component, $Component$Type} from "packages/java/awt/$Component"

export class $MenuDragMouseEvent extends $MouseEvent {
static readonly "MOUSE_FIRST": integer
static readonly "MOUSE_LAST": integer
static readonly "MOUSE_CLICKED": integer
static readonly "MOUSE_PRESSED": integer
static readonly "MOUSE_RELEASED": integer
static readonly "MOUSE_MOVED": integer
static readonly "MOUSE_ENTERED": integer
static readonly "MOUSE_EXITED": integer
static readonly "MOUSE_DRAGGED": integer
static readonly "MOUSE_WHEEL": integer
static readonly "NOBUTTON": integer
static readonly "BUTTON1": integer
static readonly "BUTTON2": integer
static readonly "BUTTON3": integer
/**
 * 
 * @deprecated
 */
static readonly "SHIFT_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "CTRL_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "META_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "ALT_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "ALT_GRAPH_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "BUTTON1_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "BUTTON2_MASK": integer
/**
 * 
 * @deprecated
 */
static readonly "BUTTON3_MASK": integer
static readonly "SHIFT_DOWN_MASK": integer
static readonly "CTRL_DOWN_MASK": integer
static readonly "META_DOWN_MASK": integer
static readonly "ALT_DOWN_MASK": integer
static readonly "BUTTON1_DOWN_MASK": integer
static readonly "BUTTON2_DOWN_MASK": integer
static readonly "BUTTON3_DOWN_MASK": integer
static readonly "ALT_GRAPH_DOWN_MASK": integer
static readonly "COMPONENT_FIRST": integer
static readonly "COMPONENT_LAST": integer
static readonly "COMPONENT_MOVED": integer
static readonly "COMPONENT_RESIZED": integer
static readonly "COMPONENT_SHOWN": integer
static readonly "COMPONENT_HIDDEN": integer
static readonly "COMPONENT_EVENT_MASK": long
static readonly "CONTAINER_EVENT_MASK": long
static readonly "FOCUS_EVENT_MASK": long
static readonly "KEY_EVENT_MASK": long
static readonly "MOUSE_EVENT_MASK": long
static readonly "MOUSE_MOTION_EVENT_MASK": long
static readonly "WINDOW_EVENT_MASK": long
static readonly "ACTION_EVENT_MASK": long
static readonly "ADJUSTMENT_EVENT_MASK": long
static readonly "ITEM_EVENT_MASK": long
static readonly "TEXT_EVENT_MASK": long
static readonly "INPUT_METHOD_EVENT_MASK": long
static readonly "PAINT_EVENT_MASK": long
static readonly "INVOCATION_EVENT_MASK": long
static readonly "HIERARCHY_EVENT_MASK": long
static readonly "HIERARCHY_BOUNDS_EVENT_MASK": long
static readonly "MOUSE_WHEEL_EVENT_MASK": long
static readonly "WINDOW_STATE_EVENT_MASK": long
static readonly "WINDOW_FOCUS_EVENT_MASK": long
static readonly "RESERVED_ID_MAX": integer

constructor(arg0: $Component$Type, arg1: integer, arg2: long, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: boolean, arg8: ($MenuElement$Type)[], arg9: $MenuSelectionManager$Type)
constructor(arg0: $Component$Type, arg1: integer, arg2: long, arg3: integer, arg4: integer, arg5: integer, arg6: integer, arg7: integer, arg8: integer, arg9: boolean, arg10: ($MenuElement$Type)[], arg11: $MenuSelectionManager$Type)

public "getPath"(): ($MenuElement)[]
public "getMenuSelectionManager"(): $MenuSelectionManager
get "path"(): ($MenuElement)[]
get "menuSelectionManager"(): $MenuSelectionManager
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuDragMouseEvent$Type = ($MenuDragMouseEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuDragMouseEvent_ = $MenuDragMouseEvent$Type;
}}
declare module "packages/javax/swing/plaf/$ButtonUI" {
import {$ComponentUI, $ComponentUI$Type} from "packages/javax/swing/plaf/$ComponentUI"

export class $ButtonUI extends $ComponentUI {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ButtonUI$Type = ($ButtonUI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ButtonUI_ = $ButtonUI$Type;
}}
declare module "packages/javax/swing/$Popup" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Popup {


public "hide"(): void
public "show"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Popup$Type = ($Popup);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Popup_ = $Popup$Type;
}}
declare module "packages/javax/swing/$JList$DropLocation" {
import {$TransferHandler$DropLocation, $TransferHandler$DropLocation$Type} from "packages/javax/swing/$TransferHandler$DropLocation"

export class $JList$DropLocation extends $TransferHandler$DropLocation {


public "toString"(): string
public "getIndex"(): integer
public "isInsert"(): boolean
get "index"(): integer
get "insert"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JList$DropLocation$Type = ($JList$DropLocation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JList$DropLocation_ = $JList$DropLocation$Type;
}}
declare module "packages/javax/swing/plaf/$MenuBarUI" {
import {$ComponentUI, $ComponentUI$Type} from "packages/javax/swing/plaf/$ComponentUI"

export class $MenuBarUI extends $ComponentUI {


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MenuBarUI$Type = ($MenuBarUI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MenuBarUI_ = $MenuBarUI$Type;
}}
