package net.anvilcraft.classiccasting;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import dev.tilera.auracore.api.CrystalColors;
import net.anvilcraft.classiccasting.items.wands.ItemHellrod;
import net.anvilcraft.classiccasting.items.wands.ItemWandCasting;
import net.anvilcraft.classiccasting.items.wands.ItemWandTrade;
import net.anvilcraft.classiccasting.tiles.TileCrystalCapacitor;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;

public class GuiTicker {
    private int lastCount;
    private ItemStack lastItem;

    @SubscribeEvent
    public void onRenderTick(RenderTickEvent ev) {
        if (ev.phase == TickEvent.Phase.END)
            tickEnd(ev.renderTickTime);
    }

    private void tickEnd(float partialTicks) {
        final Minecraft mc = Minecraft.getMinecraft();
        if (Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer) {
            final EntityPlayer player
                = (EntityPlayer) Minecraft.getMinecraft().renderViewEntity;
            final long time = System.currentTimeMillis();
            if (player != null && mc.inGameHasFocus && Minecraft.isGuiEnabled()) {
                this.renderCrystalCapacitorChargeHud(player);
                if (player.inventory.getCurrentItem() != null) {
                    if (player.inventory.getCurrentItem().getItem()
                            instanceof ItemWandTrade) {
                        final ItemWandTrade wt
                            = (ItemWandTrade) player.inventory.getCurrentItem().getItem();
                        if (wt.getPickedBlock(player.inventory.getCurrentItem())
                            != null) {
                            this.renderWandTradeHud(
                                partialTicks,
                                player,
                                time,
                                wt.getPickedBlock(player.inventory.getCurrentItem())
                            );
                        }
                    } else if (player.inventory.getCurrentItem().getItem() instanceof ItemHellrod) {
                        final ItemStack stack = player.inventory.getCurrentItem();
                        if (stack.hasTagCompound()
                            && stack.stackTagCompound.hasKey("charges")) {
                            final short charges
                                = stack.stackTagCompound.getShort("charges");
                            this.renderWandHellrodHud(
                                partialTicks, player, time, charges
                            );
                        }
                    } else if (player.inventory.getCurrentItem().getItem() instanceof ItemWandCasting) {
                        final ItemStack stack = player.inventory.getCurrentItem();
                        if (stack.hasTagCompound()
                            && stack.stackTagCompound.hasKey("vis")) {
                            final short charges = stack.stackTagCompound.getShort("vis");
                            final int maxCharges
                                = ((ItemWandCasting) player.inventory.getCurrentItem()
                                       .getItem())
                                      .getMaxVis(player.inventory.getCurrentItem());
                            this.renderCastingWandHud(
                                partialTicks, player, time, charges, maxCharges
                            );
                        }
                    }
                }
            }
        }
    }

    private void renderWandTradeHud(
        final float partialTicks,
        final EntityPlayer player,
        final long time,
        final ItemStack stack
    ) {
        final Minecraft mc = Minecraft.getMinecraft();
        final int loc = player.inventory.currentItem * 20;
        final int shift = player.capabilities.isCreativeMode ? 0 : 20;
        int amount = this.lastCount;
        if (player.inventory.inventoryChanged || !stack.isItemEqual(this.lastItem)) {
            amount = 0;
            for (final ItemStack is : player.inventory.mainInventory) {
                if (is != null && is.isItemEqual(stack)) {
                    amount += is.stackSize;
                }
            }
            this.lastItem = stack;
            player.inventory.inventoryChanged = false;
        }
        this.lastCount = amount;
        GL11.glPushMatrix();
        final ScaledResolution sr
            = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        GL11.glClear(256);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glOrtho(
            0.0,
            sr.getScaledWidth_double(),
            sr.getScaledHeight_double(),
            0.0,
            1000.0,
            3000.0
        );
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0f, 0.0f, -2000.0f);
        final int k = sr.getScaledWidth();
        final int l = sr.getScaledHeight();
        GL11.glPushMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 1);
        mc.renderEngine.bindTexture(
            new ResourceLocation("auracore", "textures/misc/particles.png")
        );
        final int px = 32 * (mc.thePlayer.ticksExisted % 16);
        final int py = 32 * (mc.thePlayer.ticksExisted % 32 / 16);
        UtilsFX.drawTexturedQuad(
            k / 2 - 96 + loc, l - 58 - shift, px, 96 + py, 32, 32, -90.0
        );
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glDisable(2896);
        GL11.glEnable(32826);
        GL11.glEnable(2903);
        GL11.glEnable(2896);
        final RenderItem itemRenderer = new RenderItem();
        try {
            itemRenderer.renderItemIntoGUI(
                mc.fontRenderer,
                mc.renderEngine,
                stack,
                k / 2 - 96 + 8 + loc,
                l - 50 - shift
            );
        } catch (final Exception ex) {}
        GL11.glDisable(2896);
        GL11.glPushMatrix();
        final String am = "" + amount;
        final int sw = mc.fontRenderer.getStringWidth(am);
        GL11.glTranslatef(
            (float) (k / 2 - 96 + 24 + loc),
            (float) (l - 30 - shift - mc.fontRenderer.FONT_HEIGHT),
            500.0f
        );
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        for (int a = -1; a <= 1; ++a) {
            for (int b = -1; b <= 1; ++b) {
                if ((a == 0 || b == 0) && (a != 0 || b != 0)) {
                    mc.fontRenderer.drawString(am, a - sw, b, 0);
                }
            }
        }
        mc.fontRenderer.drawString(am, -sw, 0, 16777215);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    private void renderWandHellrodHud(
        final float partialTicks,
        final EntityPlayer player,
        final long time,
        final short charges
    ) {
        final Minecraft mc = Minecraft.getMinecraft();
        final int shift = player.capabilities.isCreativeMode
            ? 6
            : (player.isInsideOfMaterial(Material.water) ? 29 : 20);
        GL11.glPushMatrix();
        final ScaledResolution sr
            = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        GL11.glClear(256);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glOrtho(
            0.0,
            sr.getScaledWidth_double(),
            sr.getScaledHeight_double(),
            0.0,
            1000.0,
            3000.0
        );
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        final int k = sr.getScaledWidth();
        final int l = sr.getScaledHeight();
        GL11.glTranslatef((float) (k / 2 + 10), l - 29.0f - shift, -2000.0f);
        GL11.glPushMatrix();
        GL11.glScalef(0.525f, 0.525f, 0.525f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        mc.renderEngine.bindTexture(
            new ResourceLocation("auracore", "textures/misc/particles.png")
        );
        for (int a = 0; a < 9; ++a) {
            GL11.glPushMatrix();
            final float bob
                = MathHelper.sin((player.ticksExisted + a * 10) / 5.0f) * 1.0f + 1.0f;
            GL11.glTranslatef(0.0f, bob, 0.0f);
            UtilsFX.drawTexturedQuad(
                a * 17, 0, 160 + ((a >= charges) ? 16 : 0), 0, 16, 16, -90.0
            );
            GL11.glPopMatrix();
        }
        GL11.glDisable(3042);
        GL11.glScalef(1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    private void renderCrystalCapacitorChargeHud(EntityPlayer pl) {
        Minecraft mc = Minecraft.getMinecraft();
        if (!dev.tilera.auracore.helper.Utils.hasGoggles(pl))
            return;

        MovingObjectPosition mop = Utils.getTargetBlock(pl.worldObj, pl, false, 3.0f);
        if (mop == null || mop.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK)
            return;

        TileEntity te = pl.worldObj.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
        if (!(te instanceof TileCrystalCapacitor))
            return;

        int storedVis = ((TileCrystalCapacitor) te).storedVis;
        int maxVis = ((TileCrystalCapacitor) te).maxVis;

        String top_s = storedVis + " Vis";
        String bot_s = Math.round(maxVis == 0 ? 0 : ((float)storedVis / (float)maxVis) * 100.0) + "%";

        int textwidth = Math.max(
            mc.fontRenderer.getStringWidth(top_s), mc.fontRenderer.getStringWidth(bot_s)
        );

        final ScaledResolution res
            = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        final int width = res.getScaledWidth();
        final int height = res.getScaledHeight();

        GL11.glPushMatrix();
        GL11.glTranslatef((width / 2), (height / 2) + 16, 0.0f);
        mc.renderEngine.bindTexture(
            new ResourceLocation("auracore", "textures/misc/particles.png")
        );
        final int px = 32 * (mc.thePlayer.ticksExisted % 16);
        final int py = 32 * (mc.thePlayer.ticksExisted % 32 / 16);
        GL11.glScalef(2.0f, 2.0f, 1.0f);
        UtilsFX.drawTexturedQuad(
            -16, -16 + (mc.fontRenderer.FONT_HEIGHT / 2), px, 96 + py, 32, 32, -90.0
        );
        GL11.glScalef(0.5f, 0.5f, 1.0f);
        mc.fontRenderer.drawString(top_s, -(textwidth / 2), 0, CrystalColors.colors[7]);
        mc.fontRenderer.drawString(
            bot_s, -(textwidth / 2), mc.fontRenderer.FONT_HEIGHT, CrystalColors.colors[7]
        );
        GL11.glPopMatrix();
    }

    private void renderCastingWandHud(
        final Float partialTicks,
        final EntityPlayer player,
        final long time,
        final short charges,
        final int maxCharges
    ) {
        final Minecraft mc = Minecraft.getMinecraft();
        final int shift = player.capabilities.isCreativeMode
            ? 6
            : (player.isInsideOfMaterial(Material.water) ? 29 : 20);
        final int size = (int) (charges / (float) maxCharges * 80.0f);
        GL11.glPushMatrix();
        final ScaledResolution sr
            = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        GL11.glClear(256);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glOrtho(
            0.0,
            sr.getScaledWidth_double(),
            sr.getScaledHeight_double(),
            0.0,
            1000.0,
            3000.0
        );
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        final int k = sr.getScaledWidth();
        final int l = sr.getScaledHeight();
        GL11.glTranslatef((float) (k / 2 + 10), l - 28.5f - shift, -2000.0f);
        GL11.glPushMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        mc.renderEngine.bindTexture(
            new ResourceLocation("auracore", "textures/misc/particles.png")
        );
        UtilsFX.drawTexturedQuad(0, 0, 0, 248, size, 8, -90.0);
        UtilsFX.drawTexturedQuad(size, 0, size, 240, 80 - size, 8, -90.0);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef(40.0f, 1.5f, 0.0f);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        String msg = charges + " vis";
        mc.ingameGUI.drawString(
            mc.fontRenderer, msg, -mc.fontRenderer.getStringWidth(msg) / 2, 1, 16777215
        );
        final int discount = WandManager.getTotalVisDiscount(player);
        if (discount > 0) {
            msg = "-" + discount + "%";
            mc.ingameGUI.drawString(
                mc.fontRenderer,
                msg,
                75 - mc.fontRenderer.getStringWidth(msg),
                1,
                16777215
            );
        }
        GL11.glScalef(1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }
}
