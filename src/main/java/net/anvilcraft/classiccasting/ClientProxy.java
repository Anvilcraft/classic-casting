package net.anvilcraft.classiccasting;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.anvilcraft.classiccasting.render.TileAlembicRenderer;
import net.anvilcraft.classiccasting.tiles.TileAlembic;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        super.preInit();

        FMLCommonHandler.instance().bus().register(new GuiTicker());
    }

    @Override
    public void registerTileEntities() {
        ClientRegistry.registerTileEntity(
            TileAlembic.class, "alembic", new TileAlembicRenderer()
        );
    }
}
