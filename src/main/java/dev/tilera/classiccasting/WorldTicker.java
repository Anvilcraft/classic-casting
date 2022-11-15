package dev.tilera.classiccasting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;
import dev.tilera.classiccasting.items.wands.ItemWandTrade;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockSparkle;

public class WorldTicker {
    long timeThisTick;
    public static Map<Integer, LinkedBlockingQueue<VirtualSwapper>> swapList
        = new HashMap<>();

    @SubscribeEvent
    public void onWorldTick(WorldTickEvent ev) {
        if (ev.phase == TickEvent.Phase.END && ev.side == Side.SERVER)
            this.tickEnd(ev.world);
    }

    private void tickEnd(World world) {
        this.swapTicks(world);
    }

    private void swapTicks(World world) {
        final int dim = ((World) world).provider.dimensionId;
        final LinkedBlockingQueue<VirtualSwapper> queue = WorldTicker.swapList.get(dim);
        if (queue != null) {
            boolean didSomething = false;
            while (!didSomething) {
                final VirtualSwapper vs = queue.poll();
                if (vs != null) {
                    final Block bi = world.getBlock(vs.x, vs.y, vs.z);
                    final int md = world.getBlockMetadata(vs.x, vs.y, vs.z);
                    if ((bi == vs.bTarget && md == vs.mTarget)
                        || vs.player.inventory.getStackInSlot(vs.wand) == null
                        || !(
                            vs.player.inventory.getStackInSlot(vs.wand).getItem()
                                instanceof ItemWandTrade
                        )
                        || vs.player.inventory.getStackInSlot(vs.wand).getItemDamage()
                            > vs.player.inventory.getStackInSlot(vs.wand).getMaxDamage(
                            )) {
                        continue;
                    }
                    final int slot = Utils.isPlayerCarrying(
                        vs.player, new ItemStack(vs.bTarget, 1, vs.mTarget)
                    );
                    if (vs.bSource != bi || vs.mSource != md || slot < 0) {
                        continue;
                    }
                    didSomething = true;
                    final int fortune
                        = ((ItemWandTrade) vs.player.inventory.getStackInSlot(vs.wand)
                               .getItem())
                              .getTreasure(vs.player.inventory.getStackInSlot(vs.wand));
                    vs.player.inventory.decrStackSize(slot, 1);
                    final ArrayList<ItemStack> ret
                        = bi.getDrops((World) world, vs.x, vs.y, vs.z, md, fortune);
                    if (ret.size() > 0) {
                        for (final ItemStack is : ret) {
                            if (!vs.player.inventory.addItemStackToInventory(is)) {
                                world.spawnEntityInWorld((Entity) new EntityItem(
                                    (World) world, vs.x + 0.5, vs.y + 0.5, vs.z + 0.5, is
                                ));
                            }
                        }
                    }
                    ((ItemWandTrade) vs.player.inventory.getStackInSlot(vs.wand).getItem()
                    )
                        .damageWand(
                            vs.player.inventory.getStackInSlot(vs.wand), vs.player, 1
                        );
                    if (vs.player.inventory.getStackInSlot(vs.wand).getItemDamage()
                        >= vs.player.inventory.getStackInSlot(vs.wand).getMaxDamage()) {
                        vs.player.renderBrokenItemStack(
                            vs.player.inventory.getStackInSlot(vs.wand)
                        );
                        vs.player.inventory.setInventorySlotContents(
                            vs.wand, (ItemStack) null
                        );
                        vs.player.inventoryContainer.detectAndSendChanges();
                    }
                    world.setBlock(vs.x, vs.y, vs.z, vs.bTarget, vs.mTarget, 3);
                    PacketHandler.INSTANCE.sendToAllAround(
                        new PacketFXBlockSparkle(vs.x, vs.y, vs.z, -9999),
                        new TargetPoint(world.provider.dimensionId, vs.x, vs.y, vs.z, 32)
                    );
                    world.playAuxSFX(
                        2001,
                        vs.x,
                        vs.y,
                        vs.z,
                        Block.getIdFromBlock(vs.bSource) + (vs.mSource << 12)
                    );
                    if (vs.lifespan <= 0) {
                        continue;
                    }
                    for (int xx = -1; xx <= 1; ++xx) {
                        for (int yy = -1; yy <= 1; ++yy) {
                            for (int zz = -1; zz <= 1; ++zz) {
                                if ((xx != 0 || yy != 0 || zz != 0)
                                    && world.getBlock(vs.x + xx, vs.y + yy, vs.z + zz)
                                        == vs.bSource
                                    && world.getBlockMetadata(
                                           vs.x + xx, vs.y + yy, vs.z + zz
                                       ) == vs.mSource
                                    && Utils.isBlockExposed(
                                        (World) world, vs.x + xx, vs.y + yy, vs.z + zz
                                    )) {
                                    queue.offer(new VirtualSwapper(
                                        vs.x + xx,
                                        vs.y + yy,
                                        vs.z + zz,
                                        vs.bSource,
                                        vs.mSource,
                                        vs.bTarget,
                                        vs.mTarget,
                                        vs.lifespan - 1,
                                        vs.player,
                                        vs.wand
                                    ));
                                }
                            }
                        }
                    }
                } else {
                    didSomething = true;
                }
            }
            WorldTicker.swapList.put(dim, queue);
        }
    }

    public static void addSwapper(
        final World world,
        final int x,
        final int y,
        final int z,
        final Block bs,
        final int ms,
        final Block bt,
        final int mt,
        final int life,
        final EntityPlayer player,
        final int wand
    ) {
        final int dim = world.provider.dimensionId;
        if (bs == Blocks.air || bs.getBlockHardness(world, x, y, z) < 0.0f
            || (bs == bt && ms == mt)) {
            return;
        }
        LinkedBlockingQueue<VirtualSwapper> queue = WorldTicker.swapList.get(dim);
        if (queue == null) {
            WorldTicker.swapList.put(dim, new LinkedBlockingQueue<>());
            queue = WorldTicker.swapList.get(dim);
        }
        queue.offer(new VirtualSwapper(x, y, z, bs, ms, bt, mt, life, player, wand));
        world.playSoundAtEntity((Entity) player, "thaumcraft:wand", 0.25f, 1.0f);
        WorldTicker.swapList.put(dim, queue);
    }

    public static class VirtualSwapper {
        int lifespan;
        int x;
        int y;
        int z;
        Block bSource;
        int mSource;
        Block bTarget;
        int mTarget;
        int wand;
        EntityPlayer player;

        VirtualSwapper(
            int x,
            int y,
            int z,
            Block bs,
            int ms,
            Block bt,
            int mt,
            int life,
            EntityPlayer p,
            int wand
        ) {
            this.lifespan = 0;
            this.x = 0;
            this.y = 0;
            this.z = 0;
            this.bSource = Blocks.air;
            this.mSource = 0;
            this.bTarget = Blocks.air;
            this.mTarget = 0;
            this.wand = 0;
            this.player = null;
            this.x = x;
            this.y = y;
            this.z = z;
            this.bSource = bs;
            this.mSource = ms;
            this.bTarget = bt;
            this.mTarget = mt;
            this.lifespan = life;
            this.player = p;
            this.wand = wand;
        }
    }
}
