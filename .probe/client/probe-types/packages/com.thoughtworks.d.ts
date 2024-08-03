declare module "packages/com/thoughtworks/xstream/converters/enums/$EnumToStringConverter" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$AbstractSingleValueConverter, $AbstractSingleValueConverter$Type} from "packages/com/thoughtworks/xstream/converters/basic/$AbstractSingleValueConverter"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $EnumToStringConverter<T extends $Enum<(T)>> extends $AbstractSingleValueConverter {

constructor(arg0: $Class$Type<(T)>, arg1: $Map$Type<(string), (T)>)
constructor(arg0: $Class$Type<(T)>)

public "toString"(arg0: any): string
public "canConvert"(arg0: $Class$Type<(any)>): boolean
public "fromString"(arg0: string): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnumToStringConverter$Type<T> = ($EnumToStringConverter<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnumToStringConverter_<T> = $EnumToStringConverter$Type<(T)>;
}}
declare module "packages/com/thoughtworks/xstream/converters/$SingleValueConverter" {
import {$ConverterMatcher, $ConverterMatcher$Type} from "packages/com/thoughtworks/xstream/converters/$ConverterMatcher"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $SingleValueConverter extends $ConverterMatcher {

 "toString"(arg0: any): string
 "fromString"(arg0: string): any
 "canConvert"(arg0: $Class$Type<(any)>): boolean
}

export namespace $SingleValueConverter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SingleValueConverter$Type = ($SingleValueConverter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SingleValueConverter_ = $SingleValueConverter$Type;
}}
declare module "packages/com/thoughtworks/xstream/converters/$ConverterMatcher" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $ConverterMatcher {

 "canConvert"(arg0: $Class$Type<(any)>): boolean

(arg0: $Class$Type<(any)>): boolean
}

export namespace $ConverterMatcher {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConverterMatcher$Type = ($ConverterMatcher);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConverterMatcher_ = $ConverterMatcher$Type;
}}
declare module "packages/com/thoughtworks/xstream/converters/basic/$AbstractSingleValueConverter" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$SingleValueConverter, $SingleValueConverter$Type} from "packages/com/thoughtworks/xstream/converters/$SingleValueConverter"

export class $AbstractSingleValueConverter implements $SingleValueConverter {

constructor()

public "toString"(arg0: any): string
public "canConvert"(arg0: $Class$Type<(any)>): boolean
public "fromString"(arg0: string): any
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractSingleValueConverter$Type = ($AbstractSingleValueConverter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractSingleValueConverter_ = $AbstractSingleValueConverter$Type;
}}
