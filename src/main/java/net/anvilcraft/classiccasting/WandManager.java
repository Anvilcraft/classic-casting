package net.anvilcraft.classiccasting;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.registry.LanguageRegistry;
import dev.tilera.auracore.api.IWand;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.EntitySpecialItem;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockSparkle;

public class WandManager {
    public static int getTotalVisDiscount(final EntityPlayer player) {
        int total = 0;
        for (int a = 0; a < 4; ++a) {
            if (player.inventory.armorItemInSlot(a) != null
                && player.inventory.armorItemInSlot(a).getItem()
                        instanceof IVisDiscountGear) {
                total
                    += ((IVisDiscountGear) player.inventory.armorItemInSlot(a).getItem())
                           .getVisDiscount(
                               player.inventory.armorItemInSlot(a), player, null
                           );
            }
        }
        return total;
    }

    public static boolean spendCharge(
        final World world,
        final ItemStack itemstack,
        final EntityPlayer player,
        int amount
    ) {
        final int discount = 100 - Math.min(50, getTotalVisDiscount(player));
        amount = Math.round(amount * (discount / 100.0f));
        final int charge = ((IWand) itemstack.getItem()).getVis(itemstack);
        if (world.isRemote) {
            if (charge >= amount) {
                player.swingItem();
                return true;
            }
            return false;
        } else {
            if (player.capabilities.isCreativeMode
                || ((IWand) itemstack.getItem()).consumeVis(itemstack, amount)) {
                world.playSoundAtEntity((Entity) player, "thaumcraft:wand", 0.5f, 1.0f);
                return true;
            }
            player.addChatMessage(new ChatComponentText(
                LanguageRegistry.instance().getStringLocalization("tc.wandnocharge")
            ));
        }
        return false;
    }

    public static boolean hasCharge(ItemStack is, EntityPlayer pl, int c) {
        final int discount = 100 - Math.min(50, getTotalVisDiscount(pl));
        c = Math.round(c * (discount / 100.0f));
        return ((IWand) is.getItem()).getVis(is) >= c;
    }

    public static boolean
    spendCharge(final ItemStack itemstack, final EntityPlayer player, int amount) {
        final int discount = 100 - Math.min(50, getTotalVisDiscount(player));
        amount = Math.round(amount * (discount / 100.0f));
        return ((IWand) itemstack.getItem()).consumeVis(itemstack, amount);
    }

    public static boolean
    createThaumonomicon(ItemStack is, EntityPlayer p, World w, int x, int y, int z) {
        if (!spendCharge(is, p, 25) || w.isRemote)
            return false;

        w.setBlockToAir(x, y, z);

        EntitySpecialItem entityItem = new EntitySpecialItem(
            w,
            (double) (x + 0.5D),
            (double) (y + 0.3D),
            (double) (z + 0.5D),
            new ItemStack(ConfigItems.itemThaumonomicon)
        );

        entityItem.motionY = 0.0D;
        entityItem.motionX = 0.0D;
        entityItem.motionZ = 0.0D;

        w.spawnEntityInWorld(entityItem);
        PacketHandler.INSTANCE.sendToAllAround(
            new PacketFXBlockSparkle(x, y, z, -9999),
            new TargetPoint(w.provider.dimensionId, x, y, z, 32)
        );
        w.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, "thaumcraft:wand", 1.0F, 1.0F);
        return true;
    }

    public static boolean
    createCrucible(ItemStack is, EntityPlayer p, World w, int x, int y, int z) {
        if (!spendCharge(is, p, 25) || w.isRemote)
            return false;

        w.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, "thaumcraft:wand", 1.0F, 1.0F);
        w.setBlock(x, y, z, ConfigBlocks.blockMetalDevice, 0, 3);
        w.notifyBlocksOfNeighborChange(x, y, z, w.getBlock(x, y, z));
        w.markBlockForUpdate(x, y, z);
        w.addBlockEvent(x, y, z, ConfigBlocks.blockMetalDevice, 1, 1);
        return true;
    }

    public static boolean createArcaneFurnace(
        final ItemStack itemstack,
        final EntityPlayer player,
        final World world,
        final int x,
        final int y,
        final int z
    ) {
        for (int xx = x - 2; xx <= x; ++xx) {
            for (int yy = y - 2; yy <= y; ++yy) {
                int zz = z - 2;
                while (zz <= z) {
                    if (fitArcaneFurnace(world, xx, yy, zz)
                        && spendCharge(world, itemstack, player, 100)) {
                        if (!world.isRemote) {
                            replaceArcaneFurnace(world, xx, yy, zz);
                            return true;
                        }
                        return false;
                    } else {
                        ++zz;
                    }
                }
            }
        }
        return false;
    }

    public static boolean
    fitArcaneFurnace(final World world, final int x, final int y, final int z) {
        final Block bo = Blocks.obsidian;
        final Block bn = Blocks.nether_brick;
        final Block bf = Blocks.iron_bars;
        final Block bl = Blocks.lava;
        final Block[][][] blueprint
            = { { { bn, bo, bn }, { bo, Blocks.air, bo }, { bn, bo, bn } },
                { { bn, bo, bn }, { bo, bl, bo }, { bn, bo, bn } },
                { { bn, bo, bn }, { bo, bo, bo }, { bn, bo, bn } } };
        boolean fencefound = false;
        for (int yy = 0; yy < 3; ++yy) {
            for (int xx = 0; xx < 3; ++xx) {
                for (int zz = 0; zz < 3; ++zz) {
                    final Block block = world.getBlock(x + xx, y - yy + 2, z + zz);
                    if (block != blueprint[yy][xx][zz]) {
                        if (yy != 1 || fencefound || block != bf || xx == zz
                            || (xx != 1 && zz != 1)) {
                            return false;
                        }
                        fencefound = true;
                    }
                }
            }
        }
        return fencefound;
    }

    public static boolean
    replaceArcaneFurnace(final World world, final int x, final int y, final int z) {
        final boolean fencefound = false;
        for (int yy = 0; yy < 3; ++yy) {
            int step = 1;
            for (int zz = 0; zz < 3; ++zz) {
                for (int xx = 0; xx < 3; ++xx) {
                    int md = step;
                    if (world.getBlock(x + xx, y + yy, z + zz) == Blocks.lava
                        || world.getBlock(x + xx, y + yy, z + zz)
                            == Blocks.flowing_lava) {
                        md = 0;
                    }
                    if (world.getBlock(x + xx, y + yy, z + zz) == Blocks.iron_bars) {
                        md = 10;
                    }
                    if (!world.isAirBlock(x + xx, y + yy, z + zz)) {
                        world.setBlock(
                            x + xx, y + yy, z + zz, ConfigBlocks.blockArcaneFurnace, md, 0
                        );
                        world.addBlockEvent(
                            x + xx, y + yy, z + zz, ConfigBlocks.blockArcaneFurnace, 1, 4
                        );
                    }
                    ++step;
                }
            }
        }
        for (int yy = 0; yy < 3; ++yy) {
            for (int zz2 = 0; zz2 < 3; ++zz2) {
                for (int xx2 = 0; xx2 < 3; ++xx2) {
                    world.markBlockForUpdate(x + xx2, y + yy, z + zz2);
                }
            }
        }
        return fencefound;
    }

    public static boolean createInfusionWorkbench(
        final ItemStack itemstack,
        final EntityPlayer player,
        final World world,
        final int x,
        final int y,
        final int z
    ) {
        for (int xx = x - 1; xx <= x; ++xx) {
            int zz = z - 1;
            while (zz <= z) {
                if (fitInfusionWorkbench(world, xx, y, zz)
                    && spendCharge(world, itemstack, player, 25)) {
                    if (!world.isRemote) {
                        world.setBlock(xx, y, zz, CCBlocks.infusionWorkbench, 1, 0);
                        world.setBlock(xx + 1, y, zz, CCBlocks.infusionWorkbench, 2, 0);
                        world.setBlock(xx, y, zz + 1, CCBlocks.infusionWorkbench, 3, 0);
                        world.setBlock(
                            xx + 1, y, zz + 1, CCBlocks.infusionWorkbench, 4, 0
                        );
                        world.addBlockEvent(xx, y, zz, CCBlocks.infusionWorkbench, 1, 0);
                        world.addBlockEvent(
                            xx + 1, y, zz, CCBlocks.infusionWorkbench, 1, 0
                        );
                        world.addBlockEvent(
                            xx, y, zz + 1, CCBlocks.infusionWorkbench, 1, 0
                        );
                        world.addBlockEvent(
                            xx + 1, y, zz + 1, CCBlocks.infusionWorkbench, 1, 0
                        );
                        world.markBlockForUpdate(xx, y, zz);
                        world.markBlockForUpdate(xx + 1, y, zz);
                        world.markBlockForUpdate(xx, y, zz + 1);
                        world.markBlockForUpdate(xx + 1, y, zz + 1);
                        return true;
                    }
                    return false;
                } else {
                    ++zz;
                }
            }
        }
        return false;
    }

    public static boolean
    fitInfusionWorkbench(final World world, final int x, final int y, final int z) {
        return world.getBlock(x, y, z) == CCBlocks.infusionWorkbench
            && world.getBlockMetadata(x, y, z) == 0
            && world.getBlock(x + 1, y, z) == CCBlocks.infusionWorkbench
            && world.getBlockMetadata(x + 1, y, z) == 0
            && world.getBlock(x, y, z + 1) == CCBlocks.infusionWorkbench
            && world.getBlockMetadata(x, y, z + 1) == 0
            && world.getBlock(x + 1, y, z + 1) == CCBlocks.infusionWorkbench
            && world.getBlockMetadata(x + 1, y, z + 1) == 0;
    }
}
