declare module "packages/joptsimple/$ValueConverter" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $ValueConverter<V> {

 "convert"(arg0: string): V
 "valueType"(): $Class<(any)>
 "valuePattern"(): string
}

export namespace $ValueConverter {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ValueConverter$Type<V> = ($ValueConverter<(V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ValueConverter_<V> = $ValueConverter$Type<(V)>;
}}
