package net.anvilcraft.classiccasting;

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
        clientSide = "net.anvilcraft.classiccasting.ClientProxy",
        serverSide = "net.anvilcraft.classiccasting.CommonProxy"
    )
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent ev) {
        ClassicCastingTab.INSTANCE = new ClassicCastingTab();

        CCBlocks.init();
        CCItems.init();
        proxy.registerTileEntities();

        proxy.preInit();
    }
}
