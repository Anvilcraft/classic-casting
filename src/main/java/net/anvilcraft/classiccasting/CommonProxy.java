package net.anvilcraft.classiccasting;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.classiccasting.container.ContainerInfusionWorkbench;
import net.anvilcraft.classiccasting.tiles.TileAlembic;
import net.anvilcraft.classiccasting.tiles.TileCrystalCapacitor;
import net.anvilcraft.classiccasting.tiles.TileCrystalCore;
import net.anvilcraft.classiccasting.tiles.TileInfusionWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class CommonProxy implements IGuiHandler {
    public void preInit() {
        FMLCommonHandler.instance().bus().register(new WorldTicker());
    }

    public void init() {}

    public void alembicSpill(TileAlembic a) {
        // TODO
    }

    public void registerTileEntities() {
        GameRegistry.registerTileEntity(TileAlembic.class, "alembic");
        GameRegistry.registerTileEntity(TileCrystalCapacitor.class, "crystalCapacitor");
        GameRegistry.registerTileEntity(TileCrystalCore.class, "crystalCore");
        GameRegistry.registerTileEntity(TileInfusionWorkbench.class, "infusionWorkbench");
    }

    @Override
    public Object
    getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (GuiType.get(id)) {
            case INFUSION_WORKBENCH:
                return new ContainerInfusionWorkbench(
                    player.inventory, (TileInfusionWorkbench) world.getTileEntity(x, y, z)
                );

            default:
                return null;
        }
    }

    @Override
    public Object
    getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}
