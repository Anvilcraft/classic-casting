package net.anvilcraft.classiccasting.items.wands;

import java.util.List;

import cpw.mods.fml.common.registry.LanguageRegistry;
import dev.tilera.auracore.api.IWand;
import dev.tilera.auracore.aura.AuraManager;
import net.anvilcraft.classiccasting.ClassicCastingTab;
import net.anvilcraft.classiccasting.WandManager;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.TileArcaneBore;
import thaumcraft.common.tiles.TileArcaneBoreBase;
import thaumcraft.common.tiles.TileArcaneWorkbench;
import thaumcraft.common.tiles.TileCrucible;
import thaumcraft.common.tiles.TileOwned;

public abstract class ItemWandCasting extends Item implements IWand {
    public ItemWandCasting() {
        super();
        super.maxStackSize = 1;
        super.canRepair = false;
        this.setCreativeTab(ClassicCastingTab.INSTANCE);
    }

    public int getRechargeInterval() {
        return 0;
    }

    @Override
    public void addInformation(
        final ItemStack stack,
        final EntityPlayer par2EntityPlayer,
        final List list,
        final boolean par4
    ) {
        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("vis")) {
            final int vl = stack.stackTagCompound.getShort("vis");
            list.add(LanguageRegistry.instance()
                         .getStringLocalization("tc.wandcharge")
                         .replace("%s", vl + ""));
        }
    }

    @Override
    public int getVis(ItemStack stack) {
        return stack.stackTagCompound.hasKey("vis")
            ? stack.stackTagCompound.getShort("vis")
            : 0;
    }

    @Override
    public boolean consumeVis(ItemStack stack, int amount) {
        if (this.getVis(stack) >= amount) {
            stack.stackTagCompound.setShort("vis", (short) (this.getVis(stack) - amount));
            return true;
        }
        return false;
    }

    public boolean recharge(
        final ItemStack is,
        final World w,
        final int count,
        final double x,
        final double y,
        final double z
    ) {
        boolean done = false;
        if (is.hasTagCompound() && is.stackTagCompound.hasKey("vis")) {
            if (!w.isRemote && count % this.getRechargeInterval() == 0) {
                final short charges = is.stackTagCompound.getShort("vis");
                if (charges < this.getMaxVis(is)) {
                    if (AuraManager.decreaseClosestAura(w, x, y, z, 1)) {
                        is.setTagInfo(
                            "vis", (NBTBase) new NBTTagShort((short) (charges + 1))
                        );
                    }
                    done = true;
                }
            }
        } else if (is.getItemDamage() > 0) {
            is.setTagInfo(
                "vis",
                (NBTBase
                ) new NBTTagShort((short) (this.getMaxVis(is) - is.getItemDamage()))
            );
        } else {
            is.setTagInfo("vis", (NBTBase) new NBTTagShort((short) this.getMaxVis(is)));
        }
        if (is.getItemDamage() > 0) {
            is.setItemDamage(0);
        }
        return done;
    }

    @Override
    public void onUpdate(
        final ItemStack is,
        final World w,
        final Entity e,
        final int par4,
        final boolean par5
    ) {
        this.recharge(is, w, e.ticksExisted, e.posX, e.posY, e.posZ);
    }

    //@Override
    //public boolean itemInteractionForEntity(
    //    final ItemStack par1ItemStack, EntityPlayer p, final EntityLivingBase entity
    //) {
    //    if (!(entity instanceof EntityGolemBase) || ((Entity) entity).isDead) {
    //        return false;
    //    }
    //    if (((Entity) entity).worldObj.isRemote) {
    //        // TODO: WTF
    //        //entity.spawnExplosionParticle();
    //
    //        return false;
    //    }
    //    final EnumGolemType md = ((EntityGolemBase) entity).golemType;
    //    final String deco = ((EntityGolemBase) entity).decoration;
    //    final ItemStack dropped = new ItemStack(ConfigItems.itemGolemPlacer, 1, 0);
    //    if (deco.length() > 0) {
    //        dropped.setTagInfo("deco", (NBTBase) new NBTTagString(deco));
    //    }
    //    if (entity instanceof EntityGolemClay) {
    //        dropped.setItemDamage(md + 16);
    //    } else if (entity instanceof EntityGolemWood) {
    //        dropped.setItemDamage(md);
    //    } else if (entity instanceof EntityGolemStone) {
    //        dropped.setItemDamage(md + 32);
    //    } else if (entity instanceof EntityGolemTallow) {
    //        dropped.setItemDamage(md + 48);
    //    } else if (entity instanceof EntityGolemStraw) {
    //        dropped.setItemDamage(md + 64);
    //    } else if (entity instanceof EntityGolemClayAdvanced) {
    //        dropped.setItemDamage(md + 80);
    //    } else if (entity instanceof EntityGolemStoneAdvanced) {
    //        dropped.setItemDamage(md + 96);
    //    } else if (entity instanceof EntityGolemIronGuardian) {
    //        dropped.setItemDamage(md + 112);
    //    } else if (entity instanceof EntityGolemTallowAdvanced) {
    //        dropped.setItemDamage(md + 128);
    //    }
    //    ((EntityGolemBase) entity).entityDropItem(dropped, 0.5f);
    //    ((Entity) entity)
    //        .worldObj.playSoundAtEntity((Entity) entity, "thaumcraft.zap", 0.5f, 1.0f);
    //    entity.setDead();
    //    return true;
    //}

    @Override
    public boolean onItemUseFirst(
        final ItemStack itemstack,
        final EntityPlayer player,
        final World world,
        final int x,
        final int y,
        final int z,
        final int side,
        final float hitX,
        final float hitY,
        final float hitZ
    ) {
        final Block bi = world.getBlock(x, y, z);
        final int md = world.getBlockMetadata(x, y, z);
        boolean result = false;
        final ForgeDirection direction = ForgeDirection.getOrientation(side);
        if (bi == ConfigBlocks.blockTable && md <= 1) {
            world.setBlock(x, y, z, bi, 15, 3);
            world.setTileEntity(x, y, z, (TileEntity) new TileArcaneWorkbench());
            final TileArcaneWorkbench tawb
                = (TileArcaneWorkbench) world.getTileEntity(x, y, z);
            if (tawb != null) {
                tawb.setInventorySlotContents(10, itemstack.copy());
                player.inventory.setInventorySlotContents(
                    player.inventory.currentItem, (ItemStack) null
                );
            }
            world.markBlockForUpdate(x, y, z);
            world.playSoundEffect(x + 0.5, y + 0.1, z + 0.5, "random.click", 0.15f, 0.5f);
            return false;
        }
        if (bi == ConfigBlocks.blockWarded
            || (bi == ConfigBlocks.blockCosmeticOpaque && md == 2)
            || (bi == ConfigBlocks.blockWoodenDevice && md == 2)) {
            final TileEntity tile = world.getTileEntity(x, y, z);
            if (!Config.wardedStone
                || (tile != null && tile instanceof TileOwned
                    && player.getDisplayName().equals(((TileOwned) tile).owner))) {
                if (!world.isRemote) {
                    ((TileOwned) tile).safeToRemove = true;
                    world.spawnEntityInWorld((Entity) new EntityItem(
                        world, x + 0.5, y + 0.5, z + 0.5, new ItemStack(bi, 1, md)
                    ));
                    world.playAuxSFX(
                        2001, x, y, z, Block.getIdFromBlock(bi) + (md << 12)
                    );
                    world.setBlock(x, y, z, Blocks.air, 0, 3);
                } else {
                    player.swingItem();
                }
            }
        }
        if (bi == ConfigBlocks.blockArcaneDoor) {
            TileEntity tile = world.getTileEntity(x, y, z);
            if (!Config.wardedStone
                || (tile != null && tile instanceof TileOwned
                    && player.getDisplayName().equals(((TileOwned) tile).owner))) {
                if (!world.isRemote) {
                    ((TileOwned) tile).safeToRemove = true;
                    if ((md & 0x8) == 0x0) {
                        tile = world.getTileEntity(x, y + 1, z);
                    } else {
                        tile = world.getTileEntity(x, y - 1, z);
                    }
                    if (tile != null && tile instanceof TileOwned) {
                        ((TileOwned) tile).safeToRemove = true;
                    }
                    if (Config.wardedStone
                        || (!Config.wardedStone && (md & 0x8) == 0x0)) {
                        world.spawnEntityInWorld((Entity) new EntityItem(
                            world,
                            x + 0.5,
                            y + 0.5,
                            z + 0.5,
                            new ItemStack(ConfigItems.itemArcaneDoor)
                        ));
                    }
                    world.playAuxSFX(
                        2001, x, y, z, Block.getIdFromBlock(bi) + (md << 12)
                    );
                    world.setBlock(x, y, z, Blocks.air, 0, 3);
                } else {
                    player.swingItem();
                }
            }
        }
        if (bi == ConfigBlocks.blockWoodenDevice && md == 5
            && world.isAirBlock(
                x + direction.offsetX, y + direction.offsetY, z + direction.offsetZ
            )) {
            final TileEntity tile = world.getTileEntity(x, y, z);
            if (tile != null && tile instanceof TileArcaneBore
                && side
                    != ((TileArcaneBore) tile).baseOrientation.getOpposite().ordinal()) {
                ((TileArcaneBore) tile).setOrientation(direction, false);
                ((Entity) player)
                    .worldObj.playSound(
                        x + 0.5,
                        y + 0.5,
                        z + 0.5,
                        "thaumcraft:tool",
                        0.5f,
                        0.9f + ((Entity) player).worldObj.rand.nextFloat() * 0.2f,
                        false
                    );
                player.swingItem();
            }
        }
        if (bi == ConfigBlocks.blockWoodenDevice && md == 4 && side > 1) {
            final TileEntity tile = world.getTileEntity(x, y, z);
            if (tile != null && tile instanceof TileArcaneBoreBase) {
                ((TileArcaneBoreBase) tile).orientation = direction;
                ((Entity) player)
                    .worldObj.playSound(
                        x + 0.5,
                        y + 0.5,
                        z + 0.5,
                        "thaumcraft:tool",
                        0.5f,
                        0.9f + ((Entity) player).worldObj.rand.nextFloat() * 0.2f,
                        false
                    );
                player.swingItem();
            }
        }
        if (bi == Blocks.bookshelf) {
            result = WandManager.createThaumonomicon(itemstack, player, world, x, y, z);
        }
        if (bi == Blocks.cauldron) {
            result = WandManager.createCrucible(itemstack, player, world, x, y, z);
        }
        if ((bi == Blocks.obsidian || bi == Blocks.nether_brick || bi == Blocks.iron_bars)
            && ResearchManager.isResearchComplete(
                player.getDisplayName(), "INFERNALFURNACE"
            )) {
            result = WandManager.createArcaneFurnace(itemstack, player, world, x, y, z);
        }
        // TODO: WTF
        //if ((bi == ConfigBlocks.blockCosmeticSolid || bi == ConfigBlocks.blockCrystal)
        //    && ResearchManager.isResearchComplete(
        //        player.getDisplayName(), "CRYSTALCORE"
        //    )) {
        //    result = WandManager.createNodeMagnet(itemstack, player, world, x, y, z);
        //}

        // TODO: implement infusion workbench
        //if (bi == ConfigBlocks.blockInfusionWorkbench
        //    && ResearchManager.isResearchComplete(player.getDisplayName(), "MAGBLOCK"))
        //    { result
        //        = WandManager.createInfusionWorkbench(itemstack, player, world, x, y,
        //        z);
        //}

        // TODO: need alembics for this
        //if (bi == ConfigBlocks.blockMetalDevice && md >= 1 && md <= 4) {
        //    if (player.isSneaking()) {
        //        final TileEntity tile = world.getTileEntity(x, y, z);
        //        if (tile != null && tile instanceof TileAlembic) {
        //            player.swingItem();
        //            if (world.isRemote) {
        //                world.playSound(
        //                    (double) x,
        //                    (double) y,
        //                    (double) z,
        //                    "thaumcraft.bubble",
        //                    0.2f,
        //                    1.0f + world.rand.nextFloat() * 0.4f,
        //                    false
        //                );
        //                world.addBlockEvent(
        //                    tile.xCoord,
        //                    tile.yCoord,
        //                    tile.zCoord,
        //                    ConfigBlocks.blockMetalDevice,
        //                    0,
        //                    -1
        //                );
        //                return false;
        //            }
        //            ((TileAlembic) tile).spillRemnants();
        //        }
        //    } else {
        //        // TODO: WTF
        //        //result = WandManager.refillCrucible(itemstack, player, world, x, y,
        //        z);
        //    }
        //}

        if (bi == ConfigBlocks.blockMetalDevice && md == 0) {
            if (world.isRemote) {
                return false;
            }
            final TileCrucible tile2 = (TileCrucible) world.getTileEntity(x, y, z);
            if (player.isSneaking()) {
                tile2.spillRemnants();
                return true;
            }
            // TODO: worry about this when tilera implements the other recipe handler
            // thingy
            //if (WandManager.spendCharge(
            //        world,
            //        itemstack,
            //        player,
            //        ThaumcraftCraftingManager.getCrucibleOutputCost(tile2)
            //    )) {
            //    ThaumcraftCraftingManager.performCrucibleCrafting(
            //        world, player, tile2
            //    );
            //    return true;
            //}
        }
        return result;
    }
}
