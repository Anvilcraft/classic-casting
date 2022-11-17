package net.anvilcraft.classiccasting;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.classiccasting.tiles.TileAlembic;

public class CommonProxy {
    public void preInit() {
        FMLCommonHandler.instance().bus().register(new WorldTicker());
    }

    public void alembicSpill(TileAlembic a) {
        // TODO
    }

    public void registerTileEntities() {
        GameRegistry.registerTileEntity(TileAlembic.class, "alembic");
    }
}
