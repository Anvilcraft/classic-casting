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
            new AspectList()
                .add(Aspect.MAGIC, 32)
                .add(Aspects.CONTROL, 32)
                .add(Aspect.EXCHANGE, 24)
                .add(Aspects.FLUX, 16)
                .add(Aspect.MOTION, 32)
                .add(Aspect.VOID, 16),
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

        new ResearchItem(
            "CRYSTALCAPACITOR",
            "CLASSICCASTING",
            new AspectList()
                .add(Aspect.CRYSTAL, 24)
                .add(Aspect.MAGIC, 32)
                .add(Aspect.EXCHANGE, 24),
            -2,
            2,
            1,
            new ItemStack(CCBlocks.crystal, 1, 1)
        )
            .setPages(
                new ResearchPage("classiccasting.research_page.CRYSTALCORE"),
                new ResearchPageInfusion(infusionRecipes.get("CrystalCapacitor"))
            )
            .setParents("CRYSTALCORE")
            .registerResearchItem();

        int utftCol = 3;
        int utftRow = -2;

        new ResearchItem(
            "UNIFIEDTHAUMICFIELDTHEORY",
            "CLASSICCASTING",
            new AspectList()
                .add(Aspect.MIND, 8)
                .add(Aspects.FLUX, 8)
                .add(Aspect.MAGIC, 8)
                .add(Aspect.FIRE, 8)
                .add(Aspect.AIR, 8)
                .add(Aspect.EARTH, 8)
                .add(Aspect.WATER, 8)
                .add(Aspects.TIME, 4),
            utftCol,
            utftRow,
            3,
            new ResourceLocation(
                "classiccasting", "textures/misc/unified_thaumic_field_theory.png"
            )
        )
            .setPages(
                new ResearchPage("classiccasting.research_page.UNIFIEDTHAUMICFIELDTHEORY"
                ),
                new ResearchPageInfusion(infusionRecipes.get("AdeptWand"))
            )
            .setSpecial()
            .registerResearchItem();

        new ResearchItem(
            "WANDFIRE",
            "CLASSICCASTING",
            new AspectList()
                .add(Aspects.DESTRUCTION, 8)
                .add(Aspect.MAGIC, 8)
                .add(Aspect.WEAPON, 8)
                .add(Aspect.FIRE, 8),
            utftCol + 1,
            utftRow + 2,
            2,
            new ItemStack(CCItems.wandFire)
        )
            .setPages(
                new ResearchPage("classiccasting.research_page.WANDFIRE"),
                new ResearchPageInfusion(infusionRecipes.get("WandFire"))
            )
            .setParents("UNIFIEDTHAUMICFIELDTHEORY")
            .registerResearchItem();

        new ResearchItem(
            "WANDFROST",
            "CLASSICCASTING",
            new AspectList().add(Aspect.COLD, 16).add(Aspect.WEAPON, 4),
            utftCol + 1,
            utftRow + 3,
            2,
            new ItemStack(CCItems.wandFrost)
        )
            .setPages(
                new ResearchPage("classiccasting.research_page.WANDFROST"),
                new ResearchPageInfusion(infusionRecipes.get("WandFrost"))
            )
            .setParents("UNIFIEDTHAUMICFIELDTHEORY")
            .registerResearchItem();

        new ResearchItem(
            "WANDTRADE",
            "CLASSICCASTING",
            new AspectList()
                .add(Aspect.EXCHANGE, 16)
                .add(Aspect.MAGIC, 8)
                .add(Aspect.TOOL, 8),
            utftCol - 1,
            utftRow + 4,
            2,
            new ItemStack(CCItems.wandTrade)
        )
            .setPages(
                new ResearchPage("classiccasting.research_page.WANDTRADE"),
                new ResearchPageInfusion(infusionRecipes.get("WandTrade"))
            )
            .setParents("UNIFIEDTHAUMICFIELDTHEORY")
            .registerResearchItem();

        new ResearchItem(
            "WANDEXCAVATE",
            "CLASSICCASTING",
            new AspectList().add(Aspect.METAL, 16).add(Aspect.TOOL, 4),
            utftCol - 1,
            utftRow + 3,
            2,
            new ItemStack(CCItems.wandExcavation)
        )
            .setPages(
                new ResearchPage("classiccasting.research_page.WANDEXCAVATE"),
                new ResearchPageInfusion(infusionRecipes.get("WandExcavate"))
            )
            .setParents("UNIFIEDTHAUMICFIELDTHEORY")
            .registerResearchItem();

        new ResearchItem(
            "WANDLIGHTNING",
            "CLASSICCASTING",
            new AspectList()
                .add(Aspects.DESTRUCTION, 8)
                .add(Aspect.MAGIC, 8)
                .add(Aspect.WEAPON, 8)
                // TODO: this used to be Aspect.POWER. is ENERGY correct?
                .add(Aspect.ENERGY, 8),
            utftCol + 1,
            utftRow + 4,
            2,
            new ItemStack(CCItems.wandLightning)
        )
            .setPages(
                new ResearchPage("classiccasting.research_page.WANDLIGHTNING"),
                new ResearchPageInfusion(infusionRecipes.get("wandLightning"))
            )
            .setParents("UNIFIEDTHAUMICFIELDTHEORY")
            .registerResearchItem();
    }
}
