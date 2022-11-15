package dev.tilera.classiccasting;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(
    modid = "classiccasting",
    name = "Classic Casting",
    version = "@VERSION@",
    dependencies = "required-after:Thaumcraft"
)
public class ClassicCasting {
    @SidedProxy(
        modId = "classiccasting",
        clientSide = "dev.tilera.classiccasting.ClientProxy",
        serverSide = "dev.tilera.classiccasting.CommonProxy"
    )
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        ClassicCastingTab.INSTANCE = new ClassicCastingTab();
        Items.init();
        proxy.preInit();
    }
}
