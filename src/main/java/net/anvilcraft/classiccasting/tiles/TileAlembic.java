package net.anvilcraft.classiccasting.tiles;

import dev.tilera.auracore.aura.AuraManager;
import net.anvilcraft.classiccasting.ClassicCasting;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectSource;

public class TileAlembic extends TileEntity implements IAspectSource {
    public Aspect tag;
    public int amount;
    public int maxAmount;

    public TileAlembic() {
        this.tag = null;
        this.amount = 0;
        this.maxAmount = 16;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        if (nbttagcompound.hasKey("tag"))
            this.tag = Aspect.getAspect(nbttagcompound.getString("tag"));
        this.amount = nbttagcompound.getShort("amount");
    }

    @Override
    public void writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        if (this.tag != null)
            nbttagcompound.setString("tag", this.tag.getTag());
        nbttagcompound.setShort("amount", (short) this.amount);
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public int addToContainer(final Aspect tt, int am) {
        if ((this.amount < this.maxAmount && tt == this.tag) || this.amount == 0) {
            this.tag = tt;
            final int added = Math.min(am, this.maxAmount - this.amount);
            this.amount += added;
            am -= added;
        }
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        return am;
    }

    @Override
    public boolean takeFromContainer(final Aspect tt, final int am) {
        if (this.amount >= am && tt == this.tag) {
            this.amount -= am;
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            return true;
        }
        return false;
    }

    @Override
    public boolean doesContainerContain(final AspectList ot) {
        for (final Aspect tt : ot.getAspects()) {
            if (this.amount > 0 && tt == this.tag) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(final Aspect tt, final int am) {
        return this.amount >= am && tt == this.tag;
    }

    @Override
    public int containerContains(final Aspect tt) {
        return (tt == this.tag) ? this.amount : 0;
    }

    @Override
    public AspectList getAspects() {
        return new AspectList().add(this.tag, this.amount);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        if (this.tag != null)
            nbt.setString("tag", this.tag.getTag());

        nbt.setInteger("amount", this.amount);

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();

        if (nbt.hasKey("tag"))
            this.tag = Aspect.getAspect(nbt.getString("tag"));

        this.amount = nbt.getInteger("amount");
    }

    @Override
    public boolean takeFromContainer(AspectList arg0) {
        return false;
    }

    public void spillRemnants() {
        AuraManager.addFluxToClosest(
            this.worldObj,
            this.xCoord + 0.5f,
            this.yCoord + 0.5f,
            this.zCoord + 0.5f,
            new AspectList().add(this.tag, this.amount)
        );
        this.takeFromContainer(this.tag, this.amount);
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public boolean receiveClientEvent(final int i, final int j) {
        if (i == 0 && this.amount > 0) {
            if (this.worldObj.isRemote) {
                ClassicCasting.proxy.alembicSpill(this);
            }
            return true;
        }
        return super.receiveClientEvent(i, j);
    }

    @Override
    public boolean doesContainerAccept(Aspect arg0) {
        return false;
    }

    @Override
    public void setAspects(AspectList arg0) {}
}
