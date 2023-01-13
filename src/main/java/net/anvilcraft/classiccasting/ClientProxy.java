package net.anvilcraft.classiccasting;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import dev.tilera.auracore.api.research.IResearchTable;
import net.anvilcraft.classiccasting.entities.EntityFrostShard;
import net.anvilcraft.classiccasting.gui.GuiInfusionWorkbench;
import net.anvilcraft.classiccasting.gui.GuiResearchTable;
import net.anvilcraft.classiccasting.render.BlockAlembicRenderer;
import net.anvilcraft.classiccasting.render.BlockCrystalRenderer;
import net.anvilcraft.classiccasting.render.BlockInfusionWorkbenchRenderer;
import net.anvilcraft.classiccasting.render.EntityFrostShardRenderer;
import net.anvilcraft.classiccasting.render.ItemAuraCompassRenderer;
import net.anvilcraft.classiccasting.render.TileAlembicRenderer;
import net.anvilcraft.classiccasting.render.TileCrystalCapacitorRenderer;
import net.anvilcraft.classiccasting.render.TileCrystalCoreRenderer;
import net.anvilcraft.classiccasting.render.TileInfusionWorkbenchRenderer;
import net.anvilcraft.classiccasting.research.ClassicResearchTableExtension;
import net.anvilcraft.classiccasting.tiles.TileAlembic;
import net.anvilcraft.classiccasting.tiles.TileCrystalCapacitor;
import net.anvilcraft.classiccasting.tiles.TileCrystalCore;
import net.anvilcraft.classiccasting.tiles.TileInfusionWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        super.preInit();

        FMLCommonHandler.instance().bus().register(new GuiTicker());

        BlockAlembicRenderer.RI = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new BlockAlembicRenderer());

        BlockCrystalRenderer.RI = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new BlockCrystalRenderer());

        BlockInfusionWorkbenchRenderer.RI = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new BlockInfusionWorkbenchRenderer());
    }

    @Override
    public void init() {
        super.init();

        MinecraftForgeClient.registerItemRenderer(
            CCItems.auraCompass, new ItemAuraCompassRenderer()
        );

        RenderingRegistry.registerEntityRenderingHandler(
            EntityFrostShard.class, new EntityFrostShardRenderer()
        );
    }

    @Override
    public void registerTileEntities() {
        ClientRegistry.registerTileEntity(
            TileAlembic.class, "alembic", new TileAlembicRenderer()
        );
        ClientRegistry.registerTileEntity(
            TileCrystalCapacitor.class,
            "crystalCapacitor",
            new TileCrystalCapacitorRenderer()
        );
        ClientRegistry.registerTileEntity(
            TileCrystalCore.class, "crystalCore", new TileCrystalCoreRenderer()
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

            case RESEARCH_TABLE:
                return new GuiResearchTable(
                    player,
                    (ClassicResearchTableExtension
                    ) ((IResearchTable) world.getTileEntity(x, y, z))
                        .getInternalExtension()
                );

            default:
                return null;
        }
    }
}
