package net.anvilcraft.classiccasting.tiles;

import java.util.ArrayList;
import java.util.Random;

import dev.tilera.auracore.api.AuraNode;
import dev.tilera.auracore.aura.AuraManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileCrystalCapacitor extends TileEntity {
    public short storedVis;
    public short maxVis;
    short interval;
    private long count;

    public TileCrystalCapacitor() {
        this.maxVis = 100;
        this.interval = 5000;
        this.count = System.currentTimeMillis() + new Random().nextInt(this.interval);
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (this.worldObj.isRemote) {
            return;
        }
        if (this.count <= System.currentTimeMillis()) {
            try {
                this.count = System.currentTimeMillis() + this.interval;
                final ArrayList<Integer> nodes = AuraManager.getAurasWithin(
                    this.worldObj,
                    this.xCoord + 0.5f,
                    this.yCoord + 0.5f,
                    this.zCoord + 0.5f
                );
                if (nodes.size() == 0) {
                    return;
                }
                for (final Integer key : nodes) {
                    final AuraNode nd = AuraManager.getNode(key);
                    if (nd == null) {
                        continue;
                    }
                    if (nd.level > nd.baseLevel && this.storedVis < this.maxVis) {
                        AuraManager.queueNodeChanges(
                            nd.key, -1, 0, false, null, 0.0f, 0.0f, 0.0f
                        );
                        ++this.storedVis;
                        this.worldObj.markBlockForUpdate(
                            this.xCoord, this.yCoord, this.zCoord
                        );
                        break;
                    }
                    if (nd.level<nd.baseLevel&& this.storedVis> 0) {
                        AuraManager.queueNodeChanges(
                            nd.key, 1, 0, false, null, 0.0f, 0.0f, 0.0f
                        );
                        --this.storedVis;
                        this.worldObj.markBlockForUpdate(
                            this.xCoord, this.yCoord, this.zCoord
                        );
                        break;
                    }
                }
            } catch (final Exception ex) {}
        }
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.storedVis = nbttagcompound.getShort("storedVis");
    }

    @Override
    public void writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("storedVis", this.storedVis);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setShort("storedVis", this.storedVis);

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();

        this.storedVis = nbt.getShort("storedVis");
    }

    // TODO: WTF
    //public static void handlePacket(final ByteArrayDataInput dat) {
    //    final int x = dat.readInt();
    //    final int y = dat.readInt();
    //    final int z = dat.readInt();
    //    final short storedVis = dat.readShort();
    //    final World world = Thaumcraft.proxy.getClientWorld();
    //    TileEntity te = world.getTileEntity(x, y, z);
    //    if (te instanceof TileCrystalCapacitor) {
    //        ((TileCrystalCapacitor) te).storedVis = storedVis;
    //    } else if (te == null) {
    //        final TileHole ts = new TileHole();
    //        world.func_72837_a(x, y, z, (TileEntity) ts);
    //        te = world.func_72796_p(x, y, z);
    //        if (te instanceof TileCrystalCapacitor) {
    //            ((TileCrystalCapacitor) te).storedVis = storedVis;
    //        }
    //    }
    //}
}
