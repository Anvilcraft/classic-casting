package net.anvilcraft.classiccasting;

import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class ItemAspects {
    
    public static void init() {
        ThaumcraftApi.registerObjectTag(new ItemStack(CCItems.wandCastingApprentice, 1, 32767), new AspectList().add(Aspect.MAGIC, 4).add(Aspect.TOOL, 2).add(Aspect.CRAFT, 2));        
    }

}
