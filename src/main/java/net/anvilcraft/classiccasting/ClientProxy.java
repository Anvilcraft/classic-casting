package net.anvilcraft.classiccasting;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.anvilcraft.classiccasting.gui.GuiInfusionWorkbench;
import net.anvilcraft.classiccasting.render.BlockAlembicRenderer;
import net.anvilcraft.classiccasting.render.BlockInfusionWorkbenchRenderer;
import net.anvilcraft.classiccasting.render.TileAlembicRenderer;
import net.anvilcraft.classiccasting.render.TileInfusionWorkbenchRenderer;
import net.anvilcraft.classiccasting.tiles.TileAlembic;
import net.anvilcraft.classiccasting.tiles.TileInfusionWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        super.preInit();

        FMLCommonHandler.instance().bus().register(new GuiTicker());

        BlockAlembicRenderer.RI = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new BlockAlembicRenderer());

        BlockInfusionWorkbenchRenderer.RI = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new BlockInfusionWorkbenchRenderer());
    }

    @Override
    public void registerTileEntities() {
        ClientRegistry.registerTileEntity(
            TileAlembic.class, "alembic", new TileAlembicRenderer()
        );
        ClientRegistry.registerTileEntity(
            TileInfusionWorkbench.class,
            "infusionWorkbench",
            new TileInfusionWorkbenchRenderer()
        );
    }

    @Override
    public Object
    getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        switch (GuiType.get(id)) {
            case INFUSION_WORKBENCH:
                return new GuiInfusionWorkbench(
                    player.inventory, (TileInfusionWorkbench) world.getTileEntity(x, y, z)
                );

            default:
                return null;
        }
    }
}
