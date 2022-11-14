package dev.tilera.classiccasting.items.wands;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemWandCastingApprentice extends ItemWandCasting
{
    public IIcon icon;
    
    public ItemWandCastingApprentice() {
        super();
        super.maxStackSize = 1;
        super.canRepair = false;
        this.setUnlocalizedName("classiccasting:wandCastingApprentice");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister ir) {
        this.icon = ir.registerIcon("classiccasting:wandapprentice");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(final int par1) {
        return this.icon;
    }
    
    @Override
    public int getMaxVis(ItemStack i) {
        return 50;
    }
    
    @Override
    public EnumRarity getRarity(final ItemStack itemstack) {
        return EnumRarity.uncommon;
    }
    
    @Override
    public int getRechargeInterval() {
        return 10;
    }
}
