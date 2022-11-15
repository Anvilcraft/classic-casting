package net.anvilcraft.classiccasting.items.wands;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dev.tilera.auracore.aura.AuraManager;
import net.anvilcraft.classiccasting.WorldTicker;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemWandTrade extends ItemWandBasic {
    public IIcon icon;

    public ItemWandTrade() {
        super();
        this.setMaxDamage(1500);
        this.setUnlocalizedName("classiccasting:wandTrade");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister ir) {
        this.icon = ir.registerIcon("classiccasting:wandtrade");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(final int par1) {
        return this.icon;
    }

    @Override
    public void onUpdate(
        final ItemStack is,
        final World w,
        final Entity e,
        final int par4,
        final boolean par5
    ) {
        if (!this.canCharge(is)) {
            return;
        }
        if (!w.isRemote && e.ticksExisted % 50 == 0 && is.getItemDamage() > 0
            && AuraManager.decreaseClosestAura(w, e.posX, e.posY, e.posZ, 1)) {
            is.damageItem(-4, (EntityLivingBase) e);
            if (is.getItemDamage() < 0) {
                is.setItemDamage(0);
            }
        }
    }

    @Override
    public boolean onItemUseFirst(
        final ItemStack itemstack,
        final EntityPlayer player,
        final World world,
        final int x,
        final int y,
        final int z,
        final int side,
        final float f1,
        final float f2,
        final float f3
    ) {
        if (player.isSneaking()) {
            if (!world.isRemote && world.getTileEntity(x, y, z) == null) {
                this.storePickedBlock(
                    itemstack,
                    world.getBlock(x, y, z),
                    (short) world.getBlockMetadata(x, y, z)
                );
                return true;
            }
            player.swingItem();
        } else {
            final ItemStack pb = this.getPickedBlock(itemstack);
            if (pb != null && world.isRemote) {
                player.swingItem();
            } else if (pb != null && world.getTileEntity(x, y, z) == null) {
                WorldTicker.addSwapper(
                    world,
                    x,
                    y,
                    z,
                    world.getBlock(x, y, z),
                    world.getBlockMetadata(x, y, z),
                    Block.getBlockFromItem(pb.getItem()),
                    pb.getItemDamage(),
                    3 + this.getPotency(itemstack),
                    player,
                    player.inventory.currentItem
                );
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canHarvestBlock(final Block par1Block, ItemStack tool) {
        return true;
    }

    // getStrVsBlock
    @Override
    public float func_150893_a(ItemStack p_150893_1_, Block p_150893_2_) {
        return 2000.0f;
    }

    @Override
    public boolean onBlockStartBreak(
        final ItemStack itemstack,
        final int x,
        final int y,
        final int z,
        final EntityPlayer player
    ) {
        final ItemStack pb = this.getPickedBlock(itemstack);
        if (pb == null || !((Entity) player).worldObj.isRemote) {
            if (pb != null && ((Entity) player).worldObj.getTileEntity(x, y, z) == null) {
                WorldTicker.addSwapper(
                    ((Entity) player).worldObj,
                    x,
                    y,
                    z,
                    ((Entity) player).worldObj.getBlock(x, y, z),
                    ((Entity) player).worldObj.getBlockMetadata(x, y, z),
                    Block.getBlockFromItem(pb.getItem()),
                    pb.getItemDamage(),
                    0,
                    player,
                    player.inventory.currentItem
                );
            }
        }

        // TODO: This is required because the game currently is too retarded to realize
        // the block has, in fact, not been broken when it's instamined. Maybe theres a
        // better workaround?
        player.worldObj.markBlockForUpdate(x, y, z);
        return true;
    }

    public void storePickedBlock(final ItemStack stack, final Block bi, final short md) {
        stack.setTagInfo("blockid", (NBTBase) new NBTTagInt(Block.getIdFromBlock(bi)));
        stack.setTagInfo("blockmd", (NBTBase) new NBTTagInt(md));
    }

    public ItemStack getPickedBlock(final ItemStack stack) {
        return (stack.hasTagCompound() && stack.stackTagCompound.hasKey("blockid")
                && stack.stackTagCompound.hasKey("blockmd"))
            ? new ItemStack(
                Block.getBlockById(stack.stackTagCompound.getInteger("blockid")),
                1,
                (int) stack.stackTagCompound.getShort("blockmd")
            )
            : null;
    }
}
