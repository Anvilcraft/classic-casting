package net.anvilcraft.classiccasting;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(
    modid = "classiccasting",
    name = "Classic Casting",
    version = "@VERSION@",
    dependencies = "required-after:Thaumcraft"
)
public class ClassicCasting {
    @Mod.Instance
    public static ClassicCasting INSTANCE;

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

    @Mod.EventHandler
    public void init(FMLInitializationEvent ev) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
        proxy.init();
    }
}