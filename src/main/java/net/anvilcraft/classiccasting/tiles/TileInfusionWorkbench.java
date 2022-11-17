package net.anvilcraft.classiccasting.tiles;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectSource;
import thaumcraft.common.Thaumcraft;

public class TileInfusionWorkbench extends TileMagicWorkbench {
    public InfuserAspectList foundAspects;
    public AspectList dispTags;
    public AspectList partTags;

    public TileInfusionWorkbench() {
        this.foundAspects = new InfuserAspectList();
        this.dispTags = new AspectList();
        this.partTags = new AspectList();
    }

    public boolean canUpdate() {
        return true;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!this.worldObj.isRemote) {
            if (super.count % 20 == 0 && this.crafting()) {
                this.findSources(true);
            }
        }
        else if (this.foundAspects != null && this.foundAspects.size() > 0 && this.partTags != null && this.partTags.size() > 0) {
            for (final Aspect tag : this.foundAspects.getAspects()) {
                if (this.worldObj.rand.nextInt(10) == 0
                    && this.partTags.getAmount(tag) > 0
                    && this.foundAspects.getAmount(tag) >= this.partTags.getAmount(tag)) {
                    final TileEntity ts = (TileEntity) this.foundAspects.getSource(tag);
                    Thaumcraft.proxy.sourceStreamFX(
                        this.worldObj,
                        ts.xCoord + 0.1f + this.worldObj.rand.nextFloat() * 0.8f,
                        ts.yCoord + 0.5f + this.worldObj.rand.nextFloat() * 0.5f,
                        ts.zCoord + 0.1f + this.worldObj.rand.nextFloat() * 0.8f,
                        this.xCoord + 1 + this.worldObj.rand.nextFloat()
                            - this.worldObj.rand.nextFloat(),
                        (float) (this.yCoord + 1),
                        this.zCoord + 1 + this.worldObj.rand.nextFloat()
                            - this.worldObj.rand.nextFloat(),
                        tag.getColor()
                    );
                }
            }
        }
    }

    public boolean doSourcesMatch(final AspectList requiredTags) {
        this.findSources(false);
        this.partTags = new AspectList();
        boolean wrong = true;
        if (requiredTags != null && requiredTags.size() > 0) {
            for (final Aspect tag : requiredTags.getAspects()) {
                if (this.foundAspects.getAmount(tag) < requiredTags.getAmount(tag)) {
                    wrong = false;
                } else {
                    this.partTags.merge(tag, this.foundAspects.getAmount(tag));
                }
            }
        }
        this.dispTags = requiredTags;
        return wrong;
    }

    private boolean crafting() {
        if (this.getStackInSlot(10) != null) {
            for (int a = 0; a < 9; ++a) {
                if (this.getStackInSlot(a) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public void findSources(final boolean transmit) {
        final int t = this.foundAspects.getAspects().length;
        int a = 0;
        for (final Aspect tag : this.foundAspects.getAspects()) {
            a += this.foundAspects.getAmount(tag);
        }
        this.foundAspects = new InfuserAspectList();
        for (int x = -12; x <= 12; ++x) {
            for (int z = -12; z <= 12; ++z) {
                for (int y = -5; y <= 5; ++y) {
                    if (y + this.yCoord > 0
                        && y + this.yCoord < this.worldObj.getActualHeight()) {
                        final TileEntity te = this.worldObj.getTileEntity(
                            x + this.xCoord, y + this.yCoord, z + this.zCoord
                        );
                        if (te != null && te instanceof IAspectSource) {
                            final IAspectSource ts = (IAspectSource) te;
                            for (final Aspect tag2 : ts.getAspects().getAspects()) {
                                if (ts.containerContains(tag2)
                                    > this.foundAspects.getAmount(tag2)) {
                                    this.foundAspects.merge(
                                        tag2, ts.containerContains(tag2)
                                    );
                                    this.foundAspects.linkSource(tag2, ts);
                                }
                            }
                        }
                    }
                }
            }
        }
        int b = 0;
        for (final Aspect tag3 : this.foundAspects.getAspects()) {
            b += this.foundAspects.getAmount(tag3);
        }
        if (t != this.foundAspects.getAspects().length || a != b) {
            if (super.eventHandler != null) {
                super.eventHandler.onCraftMatrixChanged((IInventory) this);
            }
            if (!this.worldObj.isRemote && transmit) {
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }
        }
    }

    @Override
    public String getInventoryName() {
        return "container.infusionworkbench";
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        if (this.getStackInSlot(10) != null) {
            nbt.setTag("wand", this.getStackInSlot(10).writeToNBT(new NBTTagCompound()));
        }

        NBTTagCompound aspects = new NBTTagCompound();
        this.foundAspects.writeToNBT(aspects);
        nbt.setTag("aspects", aspects);

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();

        if (nbt.hasKey("wand")) {
            this.setInventorySlotContents(
                10, ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("wand"))
            );
        }

        this.foundAspects.readFromNBT(nbt.getCompoundTag("aspects"));
    }

    //public static void handlePacket(final ByteArrayDataInput dat) {
    //    final World world = Thaumcraft.proxy.getClientWorld();
    //    final int x = dat.readInt();
    //    final int y = dat.readInt();
    //    final int z = dat.readInt();
    //    final short id = dat.readShort();
    //    final short dmg = dat.readShort();
    //    final InfuserAspectList ot = new InfuserAspectList();
    //    try {
    //        while (true) {
    //            final int tid = dat.readByte();
    //            final int tam = dat.readShort();
    //            final int sx = dat.readInt();
    //            final int sy = dat.readInt();
    //            final int sz = dat.readInt();
    //            final TileEntity te = world.getTileEntity(sx, sy, sz);
    //            if (te != null && te instanceof IAspectSource) {
    //                ot.merge(Aspect.get(tid), tam);
    //                ot.linkSource(Aspect.get(tid), (IAspectSource) te);
    //            }
    //        }
    //    } catch (final Exception e) {
    //        final TileEntity te2 = world.getTileEntity(x, y, z);
    //        if (te2 instanceof TileInfusionWorkbench && id != 0) {
    //            ((TileInfusionWorkbench) te2)
    //                .setInventorySlotContentsSoftly(10, new ItemStack(id, 1, (int)
    //                dmg));
    //            ((TileInfusionWorkbench) te2).foundAspects = ot;
    //            if (((TileInfusionWorkbench) te2).eventHandler != null) {
    //                ((TileInfusionWorkbench) te2)
    //                    .eventHandler.onCraftMatrixChanged((IInventory) te2);
    //            }
    //        }
    //    }
    //}

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    public static class InfuserAspectList extends AspectList {
        public Map<Aspect, IAspectSource> linkedSource;

        public InfuserAspectList() {
            this.linkedSource = new HashMap<>();
        }

        public void linkSource(final Aspect key, final IAspectSource te) {
            this.linkedSource.put(key, te);
        }

        public IAspectSource getSource(final Aspect key) {
            return this.linkedSource.get(key);
        }
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
        return new int[] {};
    }

    @Override
    public boolean
    canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
        return false;
    }

    @Override
    public boolean
    canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
        return false;
    }
}
