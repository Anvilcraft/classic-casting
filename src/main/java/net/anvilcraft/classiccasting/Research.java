package net.anvilcraft.classiccasting;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.tilera.auracore.api.Aspects;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.common.config.ConfigBlocks;

public class Research {

    public static List<Object> infusionStructure;
    public static List<Object> magnetStructure;
    public static Map<String, IArcaneRecipe> arcaneRecipes = new HashMap<>();
    public static ItemStack empty = new ItemStack(ConfigBlocks.blockHole, 1, 15);
 
    public static void init() {

        infusionStructure = Arrays.asList(new AspectList().add(Aspects.VIS, 20), 2, 1, 2, Arrays.asList(new ItemStack(CCBlocks.infusionWorkbench), new ItemStack(CCBlocks.infusionWorkbench), new ItemStack(CCBlocks.infusionWorkbench), new ItemStack(CCBlocks.infusionWorkbench)));
        magnetStructure = Arrays.asList(new AspectList().add(Aspects.VIS, 300), 3, 3, 3, Arrays.asList(empty, empty, empty, empty, new ItemStack(CCBlocks.crystal), empty, empty, empty, empty, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 0), empty, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 0), empty, empty, empty, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 0), empty, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 0), new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 0), empty, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 0), empty, empty, empty, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 0), empty, new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 0)));

        new ResearchItem("MAGBLOCKS", "ARTIFICE", new AspectList().add(Aspects.ROCK, 1).add(Aspect.METAL, 1).add(Aspect.MAGIC, 1).add(Aspect.TREE, 1).add(Aspect.CRAFT, 1), 9, -2, 2, new ItemStack(CCBlocks.infusionWorkbench)).setPages(
            new ResearchPage("classiccasting.research_page.MAGBLOCKS.1"),
            new ResearchPage(arcaneRecipes.get("ArcaneStone")),
            new ResearchPage("classiccasting.research_page.MAGBLOCKS.2"), 
            new ResearchPage(infusionStructure), 
            new ResearchPage("classiccasting.research_page.MAGBLOCKS.3")
        ).setParents("THAUMIUM").registerResearchItem();

    }
    
}