package net.anvilcraft.classiccasting.items;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dev.tilera.auracore.aura.AuraManager;
import net.anvilcraft.classiccasting.ClassicCastingTab;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileHole;

public class ItemPortableHole extends Item {
    public IIcon icon;

    public ItemPortableHole() {
        super();
        this.setMaxStackSize(1);
        this.setMaxDamage(321);
        this.isDamageable();
        this.setNoRepair();
        this.setCreativeTab(ClassicCastingTab.INSTANCE);
        this.setUnlocalizedName("classiccasting:portableHole");
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister ir) {
        this.icon = ir.registerIcon("classiccasting:portablehole");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(final int par1) {
        return this.icon;
    }

    public void onUpdate(
        final ItemStack is,
        final World w,
        final Entity e,
        final int par4,
        final boolean par5
    ) {
        if (!w.isRemote && is.getItemDamage() >= 10
            && AuraManager.decreaseClosestAura(w, e.posX, e.posY, e.posZ, 1)) {
            is.setItemDamage(is.getItemDamage() - 10);
        }
    }

    public static boolean spendCharge(
        final World world,
        final ItemStack itemstack,
        final EntityPlayer player,
        final int amount
    ) {
        final int charge = itemstack.getMaxDamage() - itemstack.getItemDamage();
        if (charge >= amount * 10) {
            if (!world.isRemote) {
                itemstack.damageItem(amount * 10, player);
            }
            return true;
        }
        if (!world.isRemote) {
            player.addChatMessage(new ChatComponentText(
                LanguageRegistry.instance().getStringLocalization("tc.portableholeerror")
            ));
        }
        return false;
    }

    public static boolean createHole(
        final World world,
        final int ii,
        final int jj,
        final int kk,
        final int side,
        final byte count
    ) {
        final Block bi = world.getBlock(ii, jj, kk);
        if (world.getTileEntity(ii, jj, kk) == null
            && !ThaumcraftApi.portableHoleBlackList.contains(bi) && bi != Blocks.bedrock
            && bi != ConfigBlocks.blockHole && bi != null
            && !bi.canPlaceBlockAt(world, ii, jj, kk)
            && bi.getBlockHardness(world, ii, jj, kk) != -1.0f) {
            final TileHole ts = new TileHole(
                bi,
                world.getBlockMetadata(ii, jj, kk),
                (short) 120,
                count,
                (byte) side,
                null
            );
            world.setBlock(ii, jj, kk, Blocks.air, 0, 0);
            if (world.setBlock(ii, jj, kk, ConfigBlocks.blockHole, 0, 0)) {
                world.setTileEntity(ii, jj, kk, (TileEntity) ts);
            }
            world.markBlockForUpdate(ii, jj, kk);
            Thaumcraft.proxy.blockSparkle(world, ii, jj, kk, 5, 1);
            return true;
        }
        return false;
    }

    public boolean onItemUseFirst(
        final ItemStack itemstack,
        final EntityPlayer entityplayer,
        final World world,
        final int i,
        final int j,
        final int k,
        final int side,
        final float f1,
        final float f2,
        final float f3
    ) {
        int ii = i;
        int jj = j;
        int kk = k;
        int distance;
        Block bi;
        for (distance = 0, distance = 0; distance < 33; ++distance) {
            bi = world.getBlock(ii, jj, kk);
            if (ThaumcraftApi.portableHoleBlackList.contains(bi) || bi == Blocks.bedrock
                || bi == ConfigBlocks.blockHole || bi == Blocks.air) {
                break;
            }
            if (bi.getBlockHardness(world, ii, jj, kk) == -1.0f) {
                break;
            }
            switch (side) {
                case 0: {
                    ++jj;
                    break;
                }
                case 1: {
                    --jj;
                    break;
                }
                case 2: {
                    ++kk;
                    break;
                }
                case 3: {
                    --kk;
                    break;
                }
                case 4: {
                    ++ii;
                    break;
                }
                case 5: {
                    --ii;
                    break;
                }
            }
        }
        if (spendCharge(world, itemstack, entityplayer, distance)) {
            createHole(world, i, j, k, side, (byte) (distance + 1));
        }
        if (!world.isRemote) {
            world.playSoundEffect(
                i + 0.5, j + 0.5, k + 0.5, "mob.endermen.portal", 1.0f, 1.0f
            );
        }
        return false;
    }

    public EnumRarity getRarity(final ItemStack itemstack) {
        return EnumRarity.rare;
    }
}
