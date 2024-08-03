package aurelienribon.tweenengine;

import aurelienribon.tweenengine.paths.CatmullRom;
import aurelienribon.tweenengine.paths.Linear;

public interface TweenPaths {

    Linear linear = new Linear();

    CatmullRom catmullRom = new CatmullRom();
}