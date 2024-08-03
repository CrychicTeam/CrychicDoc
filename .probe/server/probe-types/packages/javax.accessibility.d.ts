declare module "packages/javax/accessibility/$AccessibleStateSet" {
import {$AccessibleState, $AccessibleState$Type} from "packages/javax/accessibility/$AccessibleState"

export class $AccessibleStateSet {

constructor()
constructor(arg0: ($AccessibleState$Type)[])

public "add"(arg0: $AccessibleState$Type): boolean
public "remove"(arg0: $AccessibleState$Type): boolean
public "toString"(): string
public "clear"(): void
public "toArray"(): ($AccessibleState)[]
public "contains"(arg0: $AccessibleState$Type): boolean
public "addAll"(arg0: ($AccessibleState$Type)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessibleStateSet$Type = ($AccessibleStateSet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessibleStateSet_ = $AccessibleStateSet$Type;
}}
declare module "packages/javax/accessibility/$AccessibleSelection" {
import {$Accessible, $Accessible$Type} from "packages/javax/accessibility/$Accessible"

export interface $AccessibleSelection {

 "getAccessibleSelection"(arg0: integer): $Accessible
 "isAccessibleChildSelected"(arg0: integer): boolean
 "getAccessibleSelectionCount"(): integer
 "addAccessibleSelection"(arg0: integer): void
 "removeAccessibleSelection"(arg0: integer): void
 "clearAccessibleSelection"(): void
 "selectAllAccessibleSelection"(): void
}

export namespace $AccessibleSelection {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessibleSelection$Type = ($AccessibleSelection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessibleSelection_ = $AccessibleSelection$Type;
}}
declare module "packages/javax/accessibility/$AccessibleEditableText" {
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$AccessibleText, $AccessibleText$Type} from "packages/javax/accessibility/$AccessibleText"
import {$AttributeSet, $AttributeSet$Type} from "packages/javax/swing/text/$AttributeSet"

export interface $AccessibleEditableText extends $AccessibleText {

 "delete"(arg0: integer, arg1: integer): void
 "replaceText"(arg0: integer, arg1: integer, arg2: string): void
 "setAttributes"(arg0: integer, arg1: integer, arg2: $AttributeSet$Type): void
 "setTextContents"(arg0: string): void
 "insertTextAtIndex"(arg0: integer, arg1: string): void
 "getTextRange"(arg0: integer, arg1: integer): string
 "selectText"(arg0: integer, arg1: integer): void
 "paste"(arg0: integer): void
 "cut"(arg0: integer, arg1: integer): void
 "getSelectionStart"(): integer
 "getSelectionEnd"(): integer
 "getCharCount"(): integer
 "getIndexAtPoint"(arg0: $Point$Type): integer
 "getCharacterBounds"(arg0: integer): $Rectangle
 "getAtIndex"(arg0: integer, arg1: integer): string
 "getAfterIndex"(arg0: integer, arg1: integer): string
 "getBeforeIndex"(arg0: integer, arg1: integer): string
 "getCharacterAttribute"(arg0: integer): $AttributeSet
 "getSelectedText"(): string
 "getCaretPosition"(): integer
}

export namespace $AccessibleEditableText {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessibleEditableText$Type = ($AccessibleEditableText);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessibleEditableText_ = $AccessibleEditableText$Type;
}}
declare module "packages/javax/accessibility/$AccessibleRole" {
import {$AccessibleBundle, $AccessibleBundle$Type} from "packages/javax/accessibility/$AccessibleBundle"

export class $AccessibleRole extends $AccessibleBundle {
static readonly "ALERT": $AccessibleRole
static readonly "COLUMN_HEADER": $AccessibleRole
static readonly "CANVAS": $AccessibleRole
static readonly "COMBO_BOX": $AccessibleRole
static readonly "DESKTOP_ICON": $AccessibleRole
static readonly "HTML_CONTAINER": $AccessibleRole
static readonly "INTERNAL_FRAME": $AccessibleRole
static readonly "DESKTOP_PANE": $AccessibleRole
static readonly "OPTION_PANE": $AccessibleRole
static readonly "WINDOW": $AccessibleRole
static readonly "FRAME": $AccessibleRole
static readonly "DIALOG": $AccessibleRole
static readonly "COLOR_CHOOSER": $AccessibleRole
static readonly "DIRECTORY_PANE": $AccessibleRole
static readonly "FILE_CHOOSER": $AccessibleRole
static readonly "FILLER": $AccessibleRole
static readonly "HYPERLINK": $AccessibleRole
static readonly "ICON": $AccessibleRole
static readonly "LABEL": $AccessibleRole
static readonly "ROOT_PANE": $AccessibleRole
static readonly "GLASS_PANE": $AccessibleRole
static readonly "LAYERED_PANE": $AccessibleRole
static readonly "LIST": $AccessibleRole
static readonly "LIST_ITEM": $AccessibleRole
static readonly "MENU_BAR": $AccessibleRole
static readonly "POPUP_MENU": $AccessibleRole
static readonly "MENU": $AccessibleRole
static readonly "MENU_ITEM": $AccessibleRole
static readonly "SEPARATOR": $AccessibleRole
static readonly "PAGE_TAB_LIST": $AccessibleRole
static readonly "PAGE_TAB": $AccessibleRole
static readonly "PANEL": $AccessibleRole
static readonly "PROGRESS_BAR": $AccessibleRole
static readonly "PASSWORD_TEXT": $AccessibleRole
static readonly "PUSH_BUTTON": $AccessibleRole
static readonly "TOGGLE_BUTTON": $AccessibleRole
static readonly "CHECK_BOX": $AccessibleRole
static readonly "RADIO_BUTTON": $AccessibleRole
static readonly "ROW_HEADER": $AccessibleRole
static readonly "SCROLL_PANE": $AccessibleRole
static readonly "SCROLL_BAR": $AccessibleRole
static readonly "VIEWPORT": $AccessibleRole
static readonly "SLIDER": $AccessibleRole
static readonly "SPLIT_PANE": $AccessibleRole
static readonly "TABLE": $AccessibleRole
static readonly "TEXT": $AccessibleRole
static readonly "TREE": $AccessibleRole
static readonly "TOOL_BAR": $AccessibleRole
static readonly "TOOL_TIP": $AccessibleRole
static readonly "AWT_COMPONENT": $AccessibleRole
static readonly "SWING_COMPONENT": $AccessibleRole
static readonly "UNKNOWN": $AccessibleRole
static readonly "STATUS_BAR": $AccessibleRole
static readonly "DATE_EDITOR": $AccessibleRole
static readonly "SPIN_BOX": $AccessibleRole
static readonly "FONT_CHOOSER": $AccessibleRole
static readonly "GROUP_BOX": $AccessibleRole
static readonly "HEADER": $AccessibleRole
static readonly "FOOTER": $AccessibleRole
static readonly "PARAGRAPH": $AccessibleRole
static readonly "RULER": $AccessibleRole
static readonly "EDITBAR": $AccessibleRole
static readonly "PROGRESS_MONITOR": $AccessibleRole


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessibleRole$Type = ($AccessibleRole);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessibleRole_ = $AccessibleRole$Type;
}}
declare module "packages/javax/accessibility/$AccessibleState" {
import {$AccessibleBundle, $AccessibleBundle$Type} from "packages/javax/accessibility/$AccessibleBundle"

export class $AccessibleState extends $AccessibleBundle {
static readonly "ACTIVE": $AccessibleState
static readonly "PRESSED": $AccessibleState
static readonly "ARMED": $AccessibleState
static readonly "BUSY": $AccessibleState
static readonly "CHECKED": $AccessibleState
static readonly "EDITABLE": $AccessibleState
static readonly "EXPANDABLE": $AccessibleState
static readonly "COLLAPSED": $AccessibleState
static readonly "EXPANDED": $AccessibleState
static readonly "ENABLED": $AccessibleState
static readonly "FOCUSABLE": $AccessibleState
static readonly "FOCUSED": $AccessibleState
static readonly "ICONIFIED": $AccessibleState
static readonly "MODAL": $AccessibleState
static readonly "OPAQUE": $AccessibleState
static readonly "RESIZABLE": $AccessibleState
static readonly "MULTISELECTABLE": $AccessibleState
static readonly "SELECTABLE": $AccessibleState
static readonly "SELECTED": $AccessibleState
static readonly "SHOWING": $AccessibleState
static readonly "VISIBLE": $AccessibleState
static readonly "VERTICAL": $AccessibleState
static readonly "HORIZONTAL": $AccessibleState
static readonly "SINGLE_LINE": $AccessibleState
static readonly "MULTI_LINE": $AccessibleState
static readonly "TRANSIENT": $AccessibleState
static readonly "MANAGES_DESCENDANTS": $AccessibleState
static readonly "INDETERMINATE": $AccessibleState
static readonly "TRUNCATED": $AccessibleState


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessibleState$Type = ($AccessibleState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessibleState_ = $AccessibleState$Type;
}}
declare module "packages/javax/accessibility/$AccessibleBundle" {
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"

export class $AccessibleBundle {

constructor()

public "toString"(): string
public "toDisplayString"(): string
public "toDisplayString"(arg0: $Locale$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessibleBundle$Type = ($AccessibleBundle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessibleBundle_ = $AccessibleBundle$Type;
}}
declare module "packages/javax/accessibility/$AccessibleContext" {
import {$AccessibleRelationSet, $AccessibleRelationSet$Type} from "packages/javax/accessibility/$AccessibleRelationSet"
import {$AccessibleTable, $AccessibleTable$Type} from "packages/javax/accessibility/$AccessibleTable"
import {$AccessibleAction, $AccessibleAction$Type} from "packages/javax/accessibility/$AccessibleAction"
import {$AccessibleComponent, $AccessibleComponent$Type} from "packages/javax/accessibility/$AccessibleComponent"
import {$AccessibleRole, $AccessibleRole$Type} from "packages/javax/accessibility/$AccessibleRole"
import {$PropertyChangeListener, $PropertyChangeListener$Type} from "packages/java/beans/$PropertyChangeListener"
import {$AccessibleEditableText, $AccessibleEditableText$Type} from "packages/javax/accessibility/$AccessibleEditableText"
import {$Accessible, $Accessible$Type} from "packages/javax/accessibility/$Accessible"
import {$AccessibleSelection, $AccessibleSelection$Type} from "packages/javax/accessibility/$AccessibleSelection"
import {$AccessibleValue, $AccessibleValue$Type} from "packages/javax/accessibility/$AccessibleValue"
import {$Locale, $Locale$Type} from "packages/java/util/$Locale"
import {$AccessibleIcon, $AccessibleIcon$Type} from "packages/javax/accessibility/$AccessibleIcon"
import {$AccessibleText, $AccessibleText$Type} from "packages/javax/accessibility/$AccessibleText"
import {$AccessibleStateSet, $AccessibleStateSet$Type} from "packages/javax/accessibility/$AccessibleStateSet"

export class $AccessibleContext {
static readonly "ACCESSIBLE_NAME_PROPERTY": string
static readonly "ACCESSIBLE_DESCRIPTION_PROPERTY": string
static readonly "ACCESSIBLE_STATE_PROPERTY": string
static readonly "ACCESSIBLE_VALUE_PROPERTY": string
static readonly "ACCESSIBLE_SELECTION_PROPERTY": string
static readonly "ACCESSIBLE_CARET_PROPERTY": string
static readonly "ACCESSIBLE_VISIBLE_DATA_PROPERTY": string
static readonly "ACCESSIBLE_CHILD_PROPERTY": string
static readonly "ACCESSIBLE_ACTIVE_DESCENDANT_PROPERTY": string
static readonly "ACCESSIBLE_TABLE_CAPTION_CHANGED": string
static readonly "ACCESSIBLE_TABLE_SUMMARY_CHANGED": string
static readonly "ACCESSIBLE_TABLE_MODEL_CHANGED": string
static readonly "ACCESSIBLE_TABLE_ROW_HEADER_CHANGED": string
static readonly "ACCESSIBLE_TABLE_ROW_DESCRIPTION_CHANGED": string
static readonly "ACCESSIBLE_TABLE_COLUMN_HEADER_CHANGED": string
static readonly "ACCESSIBLE_TABLE_COLUMN_DESCRIPTION_CHANGED": string
static readonly "ACCESSIBLE_ACTION_PROPERTY": string
static readonly "ACCESSIBLE_HYPERTEXT_OFFSET": string
static readonly "ACCESSIBLE_TEXT_PROPERTY": string
static readonly "ACCESSIBLE_INVALIDATE_CHILDREN": string
static readonly "ACCESSIBLE_TEXT_ATTRIBUTES_CHANGED": string
static readonly "ACCESSIBLE_COMPONENT_BOUNDS_CHANGED": string


public "getLocale"(): $Locale
public "firePropertyChange"(arg0: string, arg1: any, arg2: any): void
public "getAccessibleParent"(): $Accessible
public "getAccessibleChildrenCount"(): integer
public "getAccessibleChild"(arg0: integer): $Accessible
public "getAccessibleSelection"(): $AccessibleSelection
public "getAccessibleIndexInParent"(): integer
public "getAccessibleStateSet"(): $AccessibleStateSet
public "getAccessibleName"(): string
public "setAccessibleName"(arg0: string): void
public "getAccessibleDescription"(): string
public "setAccessibleDescription"(arg0: string): void
public "getAccessibleRole"(): $AccessibleRole
public "setAccessibleParent"(arg0: $Accessible$Type): void
public "getAccessibleAction"(): $AccessibleAction
public "getAccessibleText"(): $AccessibleText
public "getAccessibleEditableText"(): $AccessibleEditableText
public "getAccessibleValue"(): $AccessibleValue
public "getAccessibleIcon"(): ($AccessibleIcon)[]
public "getAccessibleRelationSet"(): $AccessibleRelationSet
public "getAccessibleTable"(): $AccessibleTable
public "addPropertyChangeListener"(arg0: $PropertyChangeListener$Type): void
public "removePropertyChangeListener"(arg0: $PropertyChangeListener$Type): void
public "getAccessibleComponent"(): $AccessibleComponent
get "locale"(): $Locale
get "accessibleParent"(): $Accessible
get "accessibleChildrenCount"(): integer
get "accessibleSelection"(): $AccessibleSelection
get "accessibleIndexInParent"(): integer
get "accessibleStateSet"(): $AccessibleStateSet
get "accessibleName"(): string
set "accessibleName"(value: string)
get "accessibleDescription"(): string
set "accessibleDescription"(value: string)
get "accessibleRole"(): $AccessibleRole
set "accessibleParent"(value: $Accessible$Type)
get "accessibleAction"(): $AccessibleAction
get "accessibleText"(): $AccessibleText
get "accessibleEditableText"(): $AccessibleEditableText
get "accessibleValue"(): $AccessibleValue
get "accessibleIcon"(): ($AccessibleIcon)[]
get "accessibleRelationSet"(): $AccessibleRelationSet
get "accessibleTable"(): $AccessibleTable
get "accessibleComponent"(): $AccessibleComponent
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessibleContext$Type = ($AccessibleContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessibleContext_ = $AccessibleContext$Type;
}}
declare module "packages/javax/accessibility/$AccessibleRelation" {
import {$AccessibleBundle, $AccessibleBundle$Type} from "packages/javax/accessibility/$AccessibleBundle"

export class $AccessibleRelation extends $AccessibleBundle {
static readonly "LABEL_FOR": string
static readonly "LABELED_BY": string
static readonly "MEMBER_OF": string
static readonly "CONTROLLER_FOR": string
static readonly "CONTROLLED_BY": string
static readonly "FLOWS_TO": string
static readonly "FLOWS_FROM": string
static readonly "SUBWINDOW_OF": string
static readonly "PARENT_WINDOW_OF": string
static readonly "EMBEDS": string
static readonly "EMBEDDED_BY": string
static readonly "CHILD_NODE_OF": string
static readonly "LABEL_FOR_PROPERTY": string
static readonly "LABELED_BY_PROPERTY": string
static readonly "MEMBER_OF_PROPERTY": string
static readonly "CONTROLLER_FOR_PROPERTY": string
static readonly "CONTROLLED_BY_PROPERTY": string
static readonly "FLOWS_TO_PROPERTY": string
static readonly "FLOWS_FROM_PROPERTY": string
static readonly "SUBWINDOW_OF_PROPERTY": string
static readonly "PARENT_WINDOW_OF_PROPERTY": string
static readonly "EMBEDS_PROPERTY": string
static readonly "EMBEDDED_BY_PROPERTY": string
static readonly "CHILD_NODE_OF_PROPERTY": string

constructor(arg0: string, arg1: (any)[])
constructor(arg0: string, arg1: any)
constructor(arg0: string)

public "getKey"(): string
public "getTarget"(): (any)[]
public "setTarget"(arg0: any): void
public "setTarget"(arg0: (any)[]): void
get "key"(): string
get "target"(): (any)[]
set "target"(value: any)
set "target"(value: (any)[])
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessibleRelation$Type = ($AccessibleRelation);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessibleRelation_ = $AccessibleRelation$Type;
}}
declare module "packages/javax/accessibility/$AccessibleComponent" {
import {$FocusListener, $FocusListener$Type} from "packages/java/awt/event/$FocusListener"
import {$Color, $Color$Type} from "packages/java/awt/$Color"
import {$Cursor, $Cursor$Type} from "packages/java/awt/$Cursor"
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$Accessible, $Accessible$Type} from "packages/javax/accessibility/$Accessible"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$FontMetrics, $FontMetrics$Type} from "packages/java/awt/$FontMetrics"
import {$Dimension, $Dimension$Type} from "packages/java/awt/$Dimension"
import {$Font, $Font$Type} from "packages/java/awt/$Font"

export interface $AccessibleComponent {

 "contains"(arg0: $Point$Type): boolean
 "getBounds"(): $Rectangle
 "getLocation"(): $Point
 "getSize"(): $Dimension
 "setSize"(arg0: $Dimension$Type): void
 "isEnabled"(): boolean
 "getLocationOnScreen"(): $Point
 "setEnabled"(arg0: boolean): void
 "setVisible"(arg0: boolean): void
 "getCursor"(): $Cursor
 "isVisible"(): boolean
 "isShowing"(): boolean
 "setLocation"(arg0: $Point$Type): void
 "isFocusTraversable"(): boolean
 "addFocusListener"(arg0: $FocusListener$Type): void
 "removeFocusListener"(arg0: $FocusListener$Type): void
 "getBackground"(): $Color
 "setBackground"(arg0: $Color$Type): void
 "getForeground"(): $Color
 "setForeground"(arg0: $Color$Type): void
 "getFont"(): $Font
 "setFont"(arg0: $Font$Type): void
 "setBounds"(arg0: $Rectangle$Type): void
 "requestFocus"(): void
 "getFontMetrics"(arg0: $Font$Type): $FontMetrics
 "setCursor"(arg0: $Cursor$Type): void
 "getAccessibleAt"(arg0: $Point$Type): $Accessible
}

export namespace $AccessibleComponent {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessibleComponent$Type = ($AccessibleComponent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessibleComponent_ = $AccessibleComponent$Type;
}}
declare module "packages/javax/accessibility/$AccessibleIcon" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $AccessibleIcon {

 "getAccessibleIconDescription"(): string
 "setAccessibleIconDescription"(arg0: string): void
 "getAccessibleIconWidth"(): integer
 "getAccessibleIconHeight"(): integer
}

export namespace $AccessibleIcon {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessibleIcon$Type = ($AccessibleIcon);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessibleIcon_ = $AccessibleIcon$Type;
}}
declare module "packages/javax/accessibility/$AccessibleText" {
import {$Rectangle, $Rectangle$Type} from "packages/java/awt/$Rectangle"
import {$Point, $Point$Type} from "packages/java/awt/$Point"
import {$AttributeSet, $AttributeSet$Type} from "packages/javax/swing/text/$AttributeSet"

export interface $AccessibleText {

 "getSelectionStart"(): integer
 "getSelectionEnd"(): integer
 "getCharCount"(): integer
 "getIndexAtPoint"(arg0: $Point$Type): integer
 "getCharacterBounds"(arg0: integer): $Rectangle
 "getAtIndex"(arg0: integer, arg1: integer): string
 "getAfterIndex"(arg0: integer, arg1: integer): string
 "getBeforeIndex"(arg0: integer, arg1: integer): string
 "getCharacterAttribute"(arg0: integer): $AttributeSet
 "getSelectedText"(): string
 "getCaretPosition"(): integer
}

export namespace $AccessibleText {
const CHARACTER: integer
const WORD: integer
const SENTENCE: integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessibleText$Type = ($AccessibleText);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessibleText_ = $AccessibleText$Type;
}}
declare module "packages/javax/accessibility/$AccessibleValue" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $AccessibleValue {

 "getCurrentAccessibleValue"(): number
 "setCurrentAccessibleValue"(arg0: number): boolean
 "getMinimumAccessibleValue"(): number
 "getMaximumAccessibleValue"(): number
}

export namespace $AccessibleValue {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessibleValue$Type = ($AccessibleValue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessibleValue_ = $AccessibleValue$Type;
}}
declare module "packages/javax/accessibility/$AccessibleRelationSet" {
import {$AccessibleRelation, $AccessibleRelation$Type} from "packages/javax/accessibility/$AccessibleRelation"

export class $AccessibleRelationSet {

constructor()
constructor(arg0: ($AccessibleRelation$Type)[])

public "add"(arg0: $AccessibleRelation$Type): boolean
public "remove"(arg0: $AccessibleRelation$Type): boolean
public "get"(arg0: string): $AccessibleRelation
public "toString"(): string
public "clear"(): void
public "size"(): integer
public "toArray"(): ($AccessibleRelation)[]
public "contains"(arg0: string): boolean
public "addAll"(arg0: ($AccessibleRelation$Type)[]): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessibleRelationSet$Type = ($AccessibleRelationSet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessibleRelationSet_ = $AccessibleRelationSet$Type;
}}
declare module "packages/javax/accessibility/$AccessibleTable" {
import {$Accessible, $Accessible$Type} from "packages/javax/accessibility/$Accessible"

export interface $AccessibleTable {

 "getAccessibleCaption"(): $Accessible
 "setAccessibleCaption"(arg0: $Accessible$Type): void
 "getAccessibleSummary"(): $Accessible
 "setAccessibleSummary"(arg0: $Accessible$Type): void
 "getAccessibleRowCount"(): integer
 "getAccessibleColumnCount"(): integer
 "getAccessibleRowExtentAt"(arg0: integer, arg1: integer): integer
 "getAccessibleColumnExtentAt"(arg0: integer, arg1: integer): integer
 "getAccessibleRowHeader"(): $AccessibleTable
 "setAccessibleRowHeader"(arg0: $AccessibleTable$Type): void
 "getAccessibleColumnHeader"(): $AccessibleTable
 "setAccessibleColumnHeader"(arg0: $AccessibleTable$Type): void
 "getAccessibleRowDescription"(arg0: integer): $Accessible
 "setAccessibleRowDescription"(arg0: integer, arg1: $Accessible$Type): void
 "getAccessibleColumnDescription"(arg0: integer): $Accessible
 "setAccessibleColumnDescription"(arg0: integer, arg1: $Accessible$Type): void
 "isAccessibleSelected"(arg0: integer, arg1: integer): boolean
 "isAccessibleRowSelected"(arg0: integer): boolean
 "isAccessibleColumnSelected"(arg0: integer): boolean
 "getSelectedAccessibleRows"(): (integer)[]
 "getSelectedAccessibleColumns"(): (integer)[]
 "getAccessibleAt"(arg0: integer, arg1: integer): $Accessible
}

export namespace $AccessibleTable {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessibleTable$Type = ($AccessibleTable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessibleTable_ = $AccessibleTable$Type;
}}
declare module "packages/javax/accessibility/$AccessibleAction" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $AccessibleAction {

 "getAccessibleActionCount"(): integer
 "getAccessibleActionDescription"(arg0: integer): string
 "doAccessibleAction"(arg0: integer): boolean
}

export namespace $AccessibleAction {
const TOGGLE_EXPAND: string
const INCREMENT: string
const DECREMENT: string
const CLICK: string
const TOGGLE_POPUP: string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessibleAction$Type = ($AccessibleAction);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessibleAction_ = $AccessibleAction$Type;
}}
declare module "packages/javax/accessibility/$Accessible" {
import {$AccessibleContext, $AccessibleContext$Type} from "packages/javax/accessibility/$AccessibleContext"

export interface $Accessible {

 "getAccessibleContext"(): $AccessibleContext

(): $AccessibleContext
}

export namespace $Accessible {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Accessible$Type = ($Accessible);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Accessible_ = $Accessible$Type;
}}
