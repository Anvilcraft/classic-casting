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
import thaumcraft.api.ThaumcraftApi;
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

        new ResearchItem("CCAURA", "CLASSICCASTING", new AspectList(), -2, -4, 1, new ResourceLocation("classiccasting", "textures/misc/aura.png"))
            .setPages(
                new ResearchPage("classiccasting.research_page.CCAURA.1"),
                new ResearchPage("classiccasting.research_page.CCAURA.2")
            )
            .setAutoUnlock()
            .registerResearchItem();

        new ResearchItem("CCFLUX", "CLASSICCASTING", new AspectList(), -4, -4, 1, new ResourceLocation("classiccasting", "textures/misc/flux.png"))
            .setPages(
                new ResearchPage("classiccasting.research_page.CCFLUX.1"),
                new ResearchPage("classiccasting.research_page.CCFLUX.2")
            )
            .setAutoUnlock()
            .registerResearchItem();

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
            -2,
            -2,
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
            "AURACOMPASS",
            "CLASSICCASTING",
            new AspectList()
                .add(Aspect.MAGIC, 8)
                .add(Aspect.MIND, 8)
                .add(Aspect.MECHANISM, 4),
            -2,
            0,
            2,
            new ItemStack(CCItems.auraCompass)
        )
            .setPages(
                new ResearchPage("classiccasting.research_page.AURACOMPASS"),
                new ResearchPage(arcaneRecipes.get("AuraCompass"))
            )
            .setParents("MAGBLOCKS")
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
            -3,
            7,
            3,
            new ItemStack(CCBlocks.crystal)
        )
            .setPages(
                new ResearchPage("classiccasting.research_page.CRYSTALCORE.1"),
                new ResearchPageInfusion(infusionRecipes.get("CrystalCore")),
                new ResearchPage("classiccasting.research_page.CRYSTALCORE.2"),
                new ResearchPage(magnetStructure)
            )
            .setParents("THETHEORYOFEVERYTHING")
            .registerResearchItem();

        new ResearchItem(
            "CRYSTALCAPACITOR",
            "CLASSICCASTING",
            new AspectList()
                .add(Aspect.CRYSTAL, 24)
                .add(Aspect.MAGIC, 32)
                .add(Aspect.EXCHANGE, 24),
            -5,
            7,
            1,
            new ItemStack(CCBlocks.crystal, 1, 1)
        )
            .setPages(
                new ResearchPage("classiccasting.research_page.CRYSTALCORE"),
                new ResearchPageInfusion(infusionRecipes.get("CrystalCapacitor"))
            )
            .setParents("CRYSTALCORE")
            .registerResearchItem();

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
            0,
            0,
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
            .setParents("MAGBLOCKS")
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
            1,
            2,
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
            1,
            3,
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
            -1,
            4,
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
            -1,
            3,
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
                .add(Aspect.ENERGY, 8),
            1,
            4,
            2,
            new ItemStack(CCItems.wandLightning)
        )
            .setPages(
                new ResearchPage("classiccasting.research_page.WANDLIGHTNING"),
                new ResearchPageInfusion(infusionRecipes.get("WandLightning"))
            )
            .setParents("UNIFIEDTHAUMICFIELDTHEORY")
            .registerResearchItem();

        new ResearchItem(
            "BASICFLUX",
            "CLASSICCASTING",
            new AspectList()
                .add(Aspects.FLUX, 20)
                .add(Aspects.CONTROL, 8)
                .add(Aspect.EXCHANGE, 8)
                .add(Aspects.PURE, 8)
                .add(Aspect.MECHANISM, 4),
            -3,
            4,
            2,
            new ResourceLocation("classiccasting", "textures/misc/basic_flux.png")
        )
            .setPages(
                new ResearchPage("classiccasting.research_page.BASICFLUX.1"),
                new ResearchPageInfusion(infusionRecipes.get("FluxFilter")),
                new ResearchPage("classiccasting.research_page.BASICFLUX.2"),
                new ResearchPageInfusion(infusionRecipes.get("Alembic")),
                new ResearchPage("classiccasting.research_page.BASICFLUX.3")
            )
            .setParents("UNIFIEDTHAUMICFIELDTHEORY", "DISTILESSENTIA")
            .registerResearchItem();

        new ResearchItem(
            "THETHEORYOFEVERYTHING",
            "CLASSICCASTING",
            new AspectList()
                .add(Aspect.MIND, 24)
                .add(Aspect.ARMOR, 8)
                .add(Aspects.INSECT, 8)
                .add(Aspect.PLANT, 8)
                .add(Aspect.WEAPON, 8)
                .add(Aspect.BEAST, 8)
                .add(Aspect.FLESH, 8)
                .add(Aspect.LIFE, 8)
                .add(Aspect.POISON, 8)
                .add(Aspect.TREE, 8)
                .add(Aspect.CROP, 8)
                .add(Aspects.FLOWER, 8)
                .add(Aspect.MECHANISM, 8)
                .add(Aspects.SOUND, 8)
                .add(Aspect.CRYSTAL, 8)
                .add(Aspects.FUNGUS, 8)
                .add(Aspect.METAL, 8)
                .add(Aspect.TOOL, 8),
            0,
            7,
            3,
            new ResourceLocation(
                "classiccasting", "textures/misc/the_theory_of_everything.png"
            )
        )
            .setPages(
                new ResearchPage("classiccasting.research_page.THETHEORYOFEVERYTHING"),
                new ResearchPageInfusion(infusionRecipes.get("MageWand"))
            )
            .setParents("UNIFIEDTHAUMICFIELDTHEORY", "BASICFLUX", "GOLEMSTRAW")
            .setSpecial()
            .registerResearchItem();

        new ResearchItem(
            "HELLROD",
            "CLASSICCASTING",
            new AspectList()
                .add(Aspect.FIRE, 24)
                .add(Aspect.MAGIC, 16)
                .add(Aspect.WEAPON, 16)
                .add(Aspect.BEAST, 24)
                .add(Aspects.EVIL, 24),
            3,
            7,
            2,
            new ItemStack(CCItems.wandHellrod)
        )
            .setPages(
                new ResearchPage("classiccasting.research_page.HELLROD"),
                new ResearchPageInfusion(infusionRecipes.get("WandHellrod"))
            )
            .setParents("THETHEORYOFEVERYTHING", "WANDFIRE")
            .registerResearchItem();
        ThaumcraftApi.addWarpToResearch("HELLROD", 2);
        ThaumcraftApi.addWarpToItem(new ItemStack(CCItems.wandHellrod), 1);

        new ResearchItem(
            "PORTABLEHOLE",
            "CLASSICCASTING",
            new AspectList()
                .add(Aspect.VOID, 20)
                .add(Aspect.MOTION, 8)
                .add(Aspect.ELDRITCH, 8)
                .add(Aspect.EXCHANGE, 8),
            -3,
            2,
            1,
            new ItemStack(CCItems.portableHole)
        )
            .setPages(
                new ResearchPage("classiccasting.research_page.PORTABLEHOLE"),
                new ResearchPageInfusion(infusionRecipes.get("PortableHole"))
            )
            .setParents("UNIFIEDTHAUMICFIELDTHEORY", "ENCHFABRIC")
            .registerResearchItem();
    }
}
