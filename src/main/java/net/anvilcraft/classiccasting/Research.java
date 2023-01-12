package net.anvilcraft.classiccasting;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.tilera.auracore.api.Aspects;
import dev.tilera.auracore.api.crafting.IInfusionRecipe;
import dev.tilera.auracore.api.research.ResearchPageInfusion;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.common.config.ConfigBlocks;

public class Research {
    public static List<Object> infusionStructure;
    public static List<Object> magnetStructure;
    public static Map<String, IArcaneRecipe> arcaneRecipes = new HashMap<>();
    public static Map<String, IInfusionRecipe> infusionRecipes = new HashMap<>();
    public static ItemStack empty = new ItemStack(ConfigBlocks.blockHole, 1, 15);

    public static void init() {
        infusionStructure = Arrays.asList(
            new AspectList().add(Aspects.VIS, 20),
            2,
            1,
            2,
            Arrays.asList(
                new ItemStack(CCBlocks.infusionWorkbench),
                new ItemStack(CCBlocks.infusionWorkbench),
                new ItemStack(CCBlocks.infusionWorkbench),
                new ItemStack(CCBlocks.infusionWorkbench)
            )
        );
        magnetStructure = Arrays.asList(
            new AspectList().add(Aspects.VIS, 300),
            3,
            3,
            3,
            Arrays.asList(
                empty,
                empty,
                empty,
                empty,
                new ItemStack(CCBlocks.crystal),
                empty,
                empty,
                empty,
                empty,
                new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 0),
                empty,
                new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 0),
                empty,
                empty,
                empty,
                new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 0),
                empty,
                new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 0),
                new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 0),
                empty,
                new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 0),
                empty,
                empty,
                empty,
                new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 0),
                empty,
                new ItemStack(ConfigBlocks.blockCosmeticSolid, 1, 0)
            )
        );

        ResearchCategories.registerCategory(
            "CLASSICCASTING",
            new ResourceLocation("classiccasting", "textures/items/wandapprentice.png"),
            new ResourceLocation("thaumcraft", "textures/gui/gui_researchback.png")
        );

        // TODO: move research to right place
        new ResearchItem(
            "MAGBLOCKS",
            "CLASSICCASTING",
            new AspectList()
                .add(Aspects.ROCK, 1)
                .add(Aspect.METAL, 1)
                .add(Aspect.MAGIC, 1)
                .add(Aspect.TREE, 1)
                .add(Aspect.CRAFT, 1),
            0,
            0,
            2,
            new ItemStack(CCBlocks.infusionWorkbench)
        )
            .setPages(
                new ResearchPage("classiccasting.research_page.MAGBLOCKS.1"),
                new ResearchPage(arcaneRecipes.get("ArcaneStone")),
                new ResearchPage("classiccasting.research_page.MAGBLOCKS.2"),
                new ResearchPage(infusionStructure),
                new ResearchPage("classiccasting.research_page.MAGBLOCKS.3")
            )
            .setParents("THAUMIUM")
            .registerResearchItem();

        new ResearchItem(
            "CRYSTALCORE",
            "CLASSICCASTING",
            new AspectList(),
            0,
            2,
            3,
            new ItemStack(CCBlocks.crystal)
        )
            .setPages(
                new ResearchPage("classiccasting.research_page.CRYSTALCORE.1"),
                new ResearchPageInfusion(infusionRecipes.get("CrystalCore")),
                new ResearchPage("classiccasting.research_page.CRYSTALCORE.2"),
                new ResearchPage(magnetStructure)
            )
            .registerResearchItem();
    }
}