declare module "packages/com/madgag/gif/fmsware/$GifDecoder" {
import {$BufferedInputStream, $BufferedInputStream$Type} from "packages/java/io/$BufferedInputStream"
import {$BufferedImage, $BufferedImage$Type} from "packages/java/awt/image/$BufferedImage"
import {$InputStream, $InputStream$Type} from "packages/java/io/$InputStream"
import {$Dimension, $Dimension$Type} from "packages/java/awt/$Dimension"

export class $GifDecoder {
static readonly "STATUS_OK": integer
static readonly "STATUS_FORMAT_ERROR": integer
static readonly "STATUS_OPEN_ERROR": integer

constructor()

public "read"(arg0: string): integer
public "read"(arg0: $BufferedInputStream$Type): integer
public "read"(arg0: $InputStream$Type): integer
public "getFrameSize"(): $Dimension
public "getDelay"(arg0: integer): integer
public "getFrame"(arg0: integer): $BufferedImage
public "getLoopCount"(): integer
public "getFrameCount"(): integer
public "getImage"(): $BufferedImage
get "frameSize"(): $Dimension
get "loopCount"(): integer
get "frameCount"(): integer
get "image"(): $BufferedImage
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GifDecoder$Type = ($GifDecoder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GifDecoder_ = $GifDecoder$Type;
}}
