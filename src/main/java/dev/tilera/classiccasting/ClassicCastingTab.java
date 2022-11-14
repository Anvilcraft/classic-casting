package dev.tilera.classiccasting;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ClassicCastingTab extends CreativeTabs {
    public static ClassicCastingTab INSTANCE;

    public ClassicCastingTab() {
        super("classiccasting");
    }

    @Override
    public Item getTabIconItem() {
        return Items.wandExcavation;
    }
}
