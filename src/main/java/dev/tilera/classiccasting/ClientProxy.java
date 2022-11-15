package dev.tilera.classiccasting;

import cpw.mods.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        super.preInit();

        FMLCommonHandler.instance().bus().register(new GuiTicker());
    }
}
