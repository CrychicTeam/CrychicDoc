declare module "packages/portb/configlib/$ItemProperties" {
import {$TagAccessor, $TagAccessor$Type} from "packages/portb/configlib/$TagAccessor"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Property, $Property$Type} from "packages/portb/configlib/xml/$Property"

export class $ItemProperties {
readonly "modId": string
readonly "itemId": string
readonly "itemCategory": string
readonly "maxStackSize": integer
readonly "isEdible": boolean
readonly "isPlaceable": boolean
readonly "isDamageable": boolean
readonly "isBucket": boolean
readonly "clazz": $Class<(any)>

constructor(arg0: string, arg1: string, arg2: string, arg3: integer, arg4: boolean, arg5: boolean, arg6: boolean, arg7: boolean, arg8: $TagAccessor$Type, arg9: $Class$Type<(any)>)

public "getProperty"(arg0: $Property$Type): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemProperties$Type = ($ItemProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemProperties_ = $ItemProperties$Type;
}}
declare module "packages/portb/configlib/xml/$Operator" {
import {$EnumToStringConverter, $EnumToStringConverter$Type} from "packages/com/thoughtworks/xstream/converters/enums/$EnumToStringConverter"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Operator extends $Enum<($Operator)> {
static readonly "EQUALS": $Operator
static readonly "NOT_EQUALS": $Operator
static readonly "GREATER_THAN": $Operator
static readonly "GREATER_THAN_EQUAL": $Operator
static readonly "LESS_THAN": $Operator
static readonly "LESS_THAN_EQUAL": $Operator
static readonly "INCLUDES": $Operator
static readonly "EXCLUDES": $Operator
static readonly "INSTANCE_OF": $Operator
static readonly "CLASS_NAME_EQUALS": $Operator
static readonly "CLASS_NAME_NOT_EQUALS": $Operator
static readonly "STRING_STARTS_WITH": $Operator
static readonly "STRING_ENDS_WITH": $Operator
static readonly "CONVERTER": $EnumToStringConverter<($Operator)>


public static "values"(): ($Operator)[]
public "compare"(arg0: any, arg1: any): boolean
public static "valueOf"(arg0: string): $Operator
public "symbol"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Operator$Type = (("excludes") | ("includes") | ("not_equals") | ("less_than_equal") | ("string_ends_with") | ("greater_than") | ("equals") | ("less_than") | ("instance_of") | ("class_name_not_equals") | ("string_starts_with") | ("greater_than_equal") | ("class_name_equals")) | ($Operator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Operator_ = $Operator$Type;
}}
declare module "packages/portb/configlib/xml/$EvaluableCondition" {
import {$ItemProperties, $ItemProperties$Type} from "packages/portb/configlib/$ItemProperties"

export interface $EvaluableCondition {

 "isSatisfiedByItem"(arg0: $ItemProperties$Type): boolean

(arg0: $ItemProperties$Type): boolean
}

export namespace $EvaluableCondition {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EvaluableCondition$Type = ($EvaluableCondition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EvaluableCondition_ = $EvaluableCondition$Type;
}}
declare module "packages/portb/configlib/xml/$RuleSet" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Rule, $Rule$Type} from "packages/portb/configlib/xml/$Rule"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ItemProperties, $ItemProperties$Type} from "packages/portb/configlib/$ItemProperties"

export class $RuleSet {

constructor(arg0: $List$Type<($Rule$Type)>)

public "toString"(): string
public "toBytes"(): (byte)[]
public "addRules"(arg0: $List$Type<($Rule$Type)>): void
public "toPrettyXML"(): string
public "getRuleList"(): $List<($Rule)>
public "getMaxStacksize"(): integer
public static "fromBytes"(arg0: (byte)[]): $RuleSet
public "determineStackSizeForItem"(arg0: $ItemProperties$Type): $Optional<(integer)>
get "ruleList"(): $List<($Rule)>
get "maxStacksize"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RuleSet$Type = ($RuleSet);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RuleSet_ = $RuleSet$Type;
}}
declare module "packages/portb/configlib/$TagAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $TagAccessor {

 "doesItemHaveTag"(arg0: string): boolean

(arg0: string): boolean
}

export namespace $TagAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TagAccessor$Type = ($TagAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TagAccessor_ = $TagAccessor$Type;
}}
declare module "packages/portb/configlib/xml/$Property" {
import {$Operator, $Operator$Type} from "packages/portb/configlib/xml/$Operator"
import {$EnumToStringConverter, $EnumToStringConverter$Type} from "packages/com/thoughtworks/xstream/converters/enums/$EnumToStringConverter"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $Property extends $Enum<($Property)> {
static readonly "MOD_ID": $Property
static readonly "ID": $Property
static readonly "CATEGORY": $Property
static readonly "STACK_SIZE": $Property
static readonly "EDIBLE": $Property
static readonly "PLACEABLE": $Property
static readonly "BUCKET": $Property
static readonly "TAGS": $Property
static readonly "CLASS": $Property
static readonly "DAMAGEABLE": $Property
static readonly "CONVERTER": $EnumToStringConverter<($Property)>


public "getName"(): string
public static "values"(): ($Property)[]
public static "valueOf"(arg0: string): $Property
public "getDefaultValue"(): any
public "convertValue"(arg0: string): any
public "convertOperator"(arg0: string): $Operator
public "getDefaultOperator"(): $Operator
get "name"(): string
get "defaultValue"(): any
get "defaultOperator"(): $Operator
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Property$Type = (("bucket") | ("mod_id") | ("damageable") | ("placeable") | ("stack_size") | ("edible") | ("id") | ("category") | ("class") | ("tags")) | ($Property);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Property_ = $Property$Type;
}}
declare module "packages/portb/configlib/template/$TemplateOptions" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $TemplateOptions {

constructor(arg0: integer, arg1: integer, arg2: integer)

public "toString"(): string
public "getEnchBookLimit"(): integer
public "getNormalStackLimit"(): integer
public "getPotionStackLimit"(): integer
get "enchBookLimit"(): integer
get "normalStackLimit"(): integer
get "potionStackLimit"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TemplateOptions$Type = ($TemplateOptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TemplateOptions_ = $TemplateOptions$Type;
}}
declare module "packages/portb/configlib/xml/$Rule" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$EvaluableCondition, $EvaluableCondition$Type} from "packages/portb/configlib/xml/$EvaluableCondition"
import {$ItemProperties, $ItemProperties$Type} from "packages/portb/configlib/$ItemProperties"

export class $Rule {

constructor(arg0: $List$Type<($EvaluableCondition$Type)>, arg1: integer)

public "toString"(): string
public "matchesItem"(arg0: $ItemProperties$Type): boolean
public "getStackSize"(): integer
get "stackSize"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Rule$Type = ($Rule);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Rule_ = $Rule$Type;
}}
