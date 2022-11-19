package net.anvilcraft.classiccasting.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.anvilcraft.classiccasting.ClassicCastingTab;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemAuraCompass extends Item {
    public IIcon iconRing;
    public IIcon iconCore;

    public ItemAuraCompass() {
        super();
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.setCreativeTab(ClassicCastingTab.INSTANCE);
        this.setUnlocalizedName("classiccasting:auraCompass");
    }

    public EnumRarity getRarity(final ItemStack itemstack) {
        return EnumRarity.uncommon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister ir) {
        this.iconRing = ir.registerIcon("classiccasting:auracompassring");
        this.iconCore = ir.registerIcon("classiccasting:auracompasscore");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(final int par1) {
        return this.iconRing;
    }

    @Override
    public int getRenderPasses(final int m) {
        return 2;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamageForRenderPass(final int par1, final int par2) {
        return (par2 == 0) ? this.iconRing : this.iconCore;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }
}
