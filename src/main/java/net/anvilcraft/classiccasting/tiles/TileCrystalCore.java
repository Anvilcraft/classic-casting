package net.anvilcraft.classiccasting.tiles;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.client.FMLClientHandler;
import dev.tilera.auracore.api.AuraNode;
import dev.tilera.auracore.aura.AuraManager;
import dev.tilera.auracore.client.AuraManagerClient;
import dev.tilera.auracore.client.AuraManagerClient.NodeStats;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.utils.Utils;

public class TileCrystalCore extends TileEntity {
    public boolean active;
    public int nodeKey;
    public float speed;
    public float rotation;
    private int count;

    public TileCrystalCore() {
        this.active = false;
        this.nodeKey = -1;
        this.speed = 0.0f;
        this.rotation = 0.0f;
        this.count = -1;
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    public void activate() {
        this.active = true;
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    private boolean isValidStructure() {
        return this.worldObj.getBlock(this.xCoord - 1, this.yCoord - 1, this.zCoord - 1)
            == ConfigBlocks.blockCosmeticSolid
            && this.worldObj.getBlock(this.xCoord + 1, this.yCoord - 1, this.zCoord + 1)
            == ConfigBlocks.blockCosmeticSolid
            && this.worldObj.getBlock(this.xCoord - 1, this.yCoord - 1, this.zCoord + 1)
            == ConfigBlocks.blockCosmeticSolid
            && this.worldObj.getBlock(this.xCoord + 1, this.yCoord - 1, this.zCoord - 1)
            == ConfigBlocks.blockCosmeticSolid
            && this.worldObj.getBlock(this.xCoord - 1, this.yCoord - 2, this.zCoord - 1)
            == ConfigBlocks.blockCosmeticSolid
            && this.worldObj.getBlock(this.xCoord + 1, this.yCoord - 2, this.zCoord + 1)
            == ConfigBlocks.blockCosmeticSolid
            && this.worldObj.getBlock(this.xCoord - 1, this.yCoord - 2, this.zCoord + 1)
            == ConfigBlocks.blockCosmeticSolid
            && this.worldObj.getBlock(this.xCoord + 1, this.yCoord - 2, this.zCoord - 1)
            == ConfigBlocks.blockCosmeticSolid;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        ++this.count;
        this.rotation += this.speed;
        if (this.active) {
            if (this.speed < 1.0f) {
                this.speed += 0.001f + this.speed / 100.0f;
            } else {
                this.speed = 1.0f;
            }
        } else if (this.speed > 0.0f) {
            this.speed -= 0.01f;
        } else {
            this.speed = 0.0f;
        }
        if (this.worldObj.isRemote) {
            if (this.active && this.speed > 0.9 && this.nodeKey > -1
                && this.count % 2 == 0) {
                switch (this.count % 8 / 2) {
                    case 0: {
                        Thaumcraft.proxy.blockRunes(
                            this.worldObj,
                            this.xCoord - 1,
                            this.yCoord - 1,
                            this.zCoord - 1,
                            0.3f + this.worldObj.rand.nextFloat() * 0.7f,
                            0.0f,
                            0.3f + this.worldObj.rand.nextFloat() * 0.7f,
                            20,
                            0.0f
                        );
                        break;
                    }
                    case 1: {
                        Thaumcraft.proxy.blockRunes(
                            this.worldObj,
                            this.xCoord + 1,
                            this.yCoord - 1,
                            this.zCoord + 1,
                            0.3f + this.worldObj.rand.nextFloat() * 0.7f,
                            0.0f,
                            0.3f + this.worldObj.rand.nextFloat() * 0.7f,
                            20,
                            0.0f
                        );
                        break;
                    }
                    case 2: {
                        Thaumcraft.proxy.blockRunes(
                            this.worldObj,
                            this.xCoord - 1,
                            this.yCoord - 1,
                            this.zCoord + 1,
                            0.3f + this.worldObj.rand.nextFloat() * 0.7f,
                            0.0f,
                            0.3f + this.worldObj.rand.nextFloat() * 0.7f,
                            20,
                            0.0f
                        );
                        break;
                    }
                    case 3: {
                        Thaumcraft.proxy.blockRunes(
                            this.worldObj,
                            this.xCoord + 1,
                            this.yCoord - 1,
                            this.zCoord - 1,
                            0.3f + this.worldObj.rand.nextFloat() * 0.7f,
                            0.0f,
                            0.3f + this.worldObj.rand.nextFloat() * 0.7f,
                            20,
                            0.0f
                        );
                        break;
                    }
                }
            }

            this.renderBeam();

            return;
        }
        if (this.active && this.speed > 0.9f && this.count % 20 == 0) {
            if (this.count % 100 == 0 && this.nodeKey > -1
                && AuraManager.copyNode(AuraManager.getNode(this.nodeKey)) == null) {
                this.nodeKey = -1;
            }
            if (!this.isValidStructure()) {
                this.active = false;
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }
            if (this.nodeKey < 0) {
                this.nodeKey = AuraManager.getClosestAuraWithinRange(
                    this.worldObj,
                    this.xCoord + 0.5,
                    this.yCoord + 0.5,
                    this.zCoord + 0.5,
                    24.0
                );
                if (this.nodeKey < 0) {
                    this.active = false;
                }
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }
            if (!this.active) {
                return;
            }
            AuraNode node = null;
            if (node == null && this.nodeKey > -1) {
                node = AuraManager.copyNode(AuraManager.getNode(this.nodeKey));
                if (this.count % 80 == 0) {
                    AuraManager.decreaseClosestAura(
                        this.worldObj,
                        this.xCoord + 0.5,
                        this.yCoord + 0.5,
                        this.zCoord + 0.5,
                        1
                    );
                }
            }
            if (node != null) {
                double dist
                    = Math.sqrt(this.getDistanceFrom2(node.xPos, node.yPos, node.zPos));
                float x = 0.0f;
                float y = 0.0f;
                float z = 0.0f;
                if (dist < 0.2) {
                    this.active = false;
                    this.worldObj.markBlockForUpdate(
                        this.xCoord, this.yCoord, this.zCoord
                    );
                    x = (float) (this.xCoord + 0.5f - node.xPos);
                    y = (float) (this.yCoord + 0.5f - node.yPos);
                    z = (float) (this.zCoord + 0.5f - node.zPos);
                } else {
                    dist *= 20.0;
                    x = (float) ((this.xCoord + 0.5f - node.xPos) / dist);
                    y = (float) ((this.yCoord + 0.5f - node.yPos) / dist);
                    z = (float) ((this.zCoord + 0.5f - node.zPos) / dist);
                }
                x = Utils.clamp_float(x, -0.25f, 0.25f);
                y = Utils.clamp_float(y, -0.25f, 0.25f);
                z = Utils.clamp_float(z, -0.25f, 0.25f);
                AuraManager.queueNodeChanges(this.nodeKey, 0, 0, false, null, x, y, z);
            }
        }
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.active = nbttagcompound.getBoolean("active");
        this.speed = nbttagcompound.getFloat("speed");
        this.nodeKey = nbttagcompound.getInteger("node");
    }

    @Override
    public void writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("active", this.active);
        nbttagcompound.setFloat("speed", this.speed);
        nbttagcompound.setInteger("node", this.nodeKey);
    }

    public double
    getDistanceFrom2(final double par1, final double par3, final double par5) {
        final double var7 = this.xCoord + 0.5 - par1;
        final double var8 = this.yCoord + 0.5 - par3;
        final double var9 = this.zCoord + 0.5 - par5;
        return var7 * var7 + var8 * var8 + var9 * var9;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setBoolean("active", this.active);
        nbt.setFloat("speed", this.speed);
        nbt.setInteger("node", this.nodeKey);

        return new S35PacketUpdateTileEntity(
            this.xCoord, this.yCoord, this.zCoord, this.getBlockMetadata(), nbt
        );
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();

        this.active = nbt.getBoolean("active");
        this.speed = nbt.getFloat("speed");
        this.nodeKey = nbt.getInteger("node");
    }

    // TODO: WTF
    //public static void handlePacket(final ByteArrayDataInput dat) {
    //    final int x = dat.readInt();
    //    final int y = dat.readInt();
    //    final int z = dat.readInt();
    //    final boolean active = dat.readBoolean();
    //    final float speed = dat.readFloat();
    //    final int node = dat.readInt();
    //    final World world = Thaumcraft.proxy.getClientWorld();
    //    TileEntity te = world.getTileEntity(x, y, z);
    //    if (te instanceof TileCrystalCore) {
    //        ((TileCrystalCore) te).active = active;
    //        ((TileCrystalCore) te).speed = speed;
    //        ((TileCrystalCore) te).nodeKey = node;
    //    } else if (te == null) {
    //        final TileHole ts = new TileHole();
    //        world.setTileEntity(x, y, z, (TileEntity) ts);
    //        te = world.getTileEntity(x, y, z);
    //        if (te instanceof TileCrystalCore) {
    //            ((TileCrystalCore) te).active = active;
    //            ((TileCrystalCore) te).speed = speed;
    //            ((TileCrystalCore) te).nodeKey = node;
    //        }
    //    }
    //}

    // FIXME: move into TESR
    @SideOnly(Side.CLIENT)
    void renderBeam() {
        if (dev.tilera.auracore.helper.Utils.hasGoggles(
                FMLClientHandler.instance().getClientPlayerEntity()
            )
            && this.active && this.nodeKey > -1 && this.speed > 0.9
            && AuraManagerClient.auraClientMovementList.get(this.nodeKey) != null
            && this.count % 20 == 0) {
            final AuraManagerClient.NodeRenderInfo nri
                = AuraManagerClient.auraClientMovementList.get(this.nodeKey);
            float size = 3.0f;
            final NodeStats l = AuraManagerClient.auraClientList.get(this.nodeKey);
            if (l != null) {
                size = l.level / 100.0f;
            }
            Thaumcraft.proxy.beam(
                this.worldObj,
                this.xCoord + 0.5,
                this.yCoord + 0.5 + this.speed,
                this.zCoord + 0.5,
                nri.x,
                nri.y,
                nri.z,
                0,
                16777215,
                true,
                size,
                20
            );
            Thaumcraft.proxy.beam(
                this.worldObj,
                this.xCoord + 0.5,
                this.yCoord + 0.5 + this.speed,
                this.zCoord + 0.5,
                nri.x,
                nri.y,
                nri.z,
                1,
                16777215,
                true,
                size / 2.0f,
                20
            );
        }
    }
}
