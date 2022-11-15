package dev.tilera.classiccasting;

import cpw.mods.fml.common.FMLCommonHandler;

public class CommonProxy {
    public void preInit() {
        FMLCommonHandler.instance().bus().register(new WorldTicker());
    }
}
