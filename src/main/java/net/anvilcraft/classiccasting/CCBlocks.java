package net.anvilcraft.classiccasting;

import cpw.mods.fml.common.registry.GameRegistry;
import net.anvilcraft.classiccasting.blocks.BlockAlembic;
import net.anvilcraft.classiccasting.blocks.BlockInfusionWorkbench;
import net.anvilcraft.classiccasting.items.ItemBlockAlembic;
import net.minecraft.block.Block;

public class CCBlocks {
    public static Block alembic;
    public static Block infusionWorkbench;

    public static void init() {
        alembic = new BlockAlembic();
        infusionWorkbench = new BlockInfusionWorkbench();

        GameRegistry.registerBlock(alembic, ItemBlockAlembic.class, "alembic");
        GameRegistry.registerBlock(infusionWorkbench, "infusionWorkbench");
    }
}
