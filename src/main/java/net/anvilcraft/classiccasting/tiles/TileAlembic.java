package net.anvilcraft.classiccasting.tiles;

import dev.tilera.auracore.api.IAlembic;
import dev.tilera.auracore.api.IEssenceContainer;
import dev.tilera.auracore.aura.AuraManager;
import net.anvilcraft.classiccasting.ClassicCasting;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaTransport;

public class TileAlembic
    extends TileEntity implements IAlembic, IEssenceContainer, IEssentiaTransport {
    public Aspect aspect;
    public int amount;
    public int maxAmount;

    public TileAlembic() {
        this.aspect = null;
        this.amount = 0;
        this.maxAmount = 16;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        if (nbttagcompound.hasKey("tag"))
            this.aspect = Aspect.getAspect(nbttagcompound.getString("tag"));
        this.amount = nbttagcompound.getShort("amount");
    }

    @Override
    public void writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        if (this.aspect != null)
            nbttagcompound.setString("tag", this.aspect.getTag());
        nbttagcompound.setShort("amount", (short) this.amount);
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public int addToContainer(final Aspect tt, int am) {
        if ((this.amount < this.maxAmount && tt == this.aspect) || this.amount == 0) {
            this.aspect = tt;
            final int added = Math.min(am, this.maxAmount - this.amount);
            this.amount += added;
            am -= added;
        }
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        return am;
    }

    @Override
    public boolean takeFromContainer(final Aspect tt, final int am) {
        if (this.amount >= am && tt == this.aspect) {
            this.amount -= am;

            if (this.amount == 0)
                this.aspect = null;

            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            return true;
        }
        return false;
    }

    @Override
    public boolean doesContainerContain(final AspectList ot) {
        for (final Aspect tt : ot.getAspects()) {
            if (this.amount > 0 && tt == this.aspect) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(final Aspect tt, final int am) {
        return this.amount >= am && tt == this.aspect;
    }

    @Override
    public int containerContains(final Aspect tt) {
        return (tt == this.aspect) ? this.amount : 0;
    }

    @Override
    public AspectList getAspects() {
        AspectList l = new AspectList();
        if (this.aspect != null)
            l.add(this.aspect, this.amount);
        return l;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        if (this.aspect != null)
            nbt.setString("tag", this.aspect.getTag());

        nbt.setInteger("amount", this.amount);

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();

        if (nbt.hasKey("tag"))
            this.aspect = Aspect.getAspect(nbt.getString("tag"));
        else
            this.aspect = null;

        this.amount = nbt.getInteger("amount");

        this.worldObj.markBlockRangeForRenderUpdate(
            this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord
        );
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
            new AspectList().add(this.aspect, this.amount)
        );
        this.takeFromContainer(this.aspect, this.amount);
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

    @Override
    public Aspect getAspect() {
        return this.aspect;
    }

    @Override
    public int getAmount() {
        return this.amount;
    }

    @Override
    public int addEssentia(Aspect arg0, int arg1, ForgeDirection arg2) {
        return 0;
    }

    @Override
    public boolean canInputFrom(ForgeDirection arg0) {
        return false;
    }

    @Override
    public boolean canOutputTo(ForgeDirection arg0) {
        return this.isConnectable(arg0);
    }

    @Override
    public int getEssentiaAmount(ForgeDirection arg0) {
        return this.amount;
    }

    @Override
    public Aspect getEssentiaType(ForgeDirection arg0) {
        return this.aspect;
    }

    @Override
    public int getMinimumSuction() {
        return 0;
    }

    @Override
    public int getSuctionAmount(ForgeDirection arg0) {
        return 0;
    }

    @Override
    public Aspect getSuctionType(ForgeDirection arg0) {
        return null;
    }

    @Override
    public boolean isConnectable(ForgeDirection arg0) {
        return arg0 != ForgeDirection.UP;
    }

    @Override
    public boolean renderExtendedTube() {
        return true;
    }

    @Override
    public void setSuction(Aspect arg0, int arg1) {}

    @Override
    public int takeEssentia(Aspect arg0, int arg1, ForgeDirection arg2) {
        return this.canOutputTo(arg2) && this.takeFromContainer(arg0, arg1) ? arg1 : 0;
    }
}
