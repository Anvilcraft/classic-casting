package net.anvilcraft.classiccasting.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dev.tilera.auracore.api.IWand;
import dev.tilera.auracore.api.crafting.IInfusionRecipe;
import dev.tilera.auracore.crafting.AuracoreCraftingManager;
import net.anvilcraft.classiccasting.WandManager;
import net.anvilcraft.classiccasting.container.ContainerInfusionWorkbench;
import net.anvilcraft.classiccasting.tiles.TileInfusionWorkbench;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.codechicken.lib.math.MathHelper;
import thaumcraft.common.tiles.TileMagicWorkbench;

@SideOnly(Side.CLIENT)
public class GuiInfusionWorkbench extends GuiContainer {
    private TileInfusionWorkbench tileEntity;
    private InventoryPlayer ip;

    public GuiInfusionWorkbench(
        final InventoryPlayer par1InventoryPlayer, final TileInfusionWorkbench e
    ) {
        super((Container) new ContainerInfusionWorkbench(par1InventoryPlayer, e));
        this.tileEntity = e;
        this.ip = par1InventoryPlayer;
        this.ySize += 21;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int par1, final int par2) {}

    @Override
    public void drawScreen(final int par1, final int par2, final float par3) {
        super.drawScreen(par1, par2, par3);
        // TODO: crafting
        //if (ThaumcraftCraftingManager.findMatchingInfusionRecipe(
        //        (IInventory) this.tileEntity, this.ip.player
        //    )
        //    != null) {
        //    final ObjectTags reqs
        //        = ThaumcraftCraftingManager.findMatchingInfusionRecipeTags(
        //            (IInventory) this.tileEntity, this.ip.player
        //        );
        //    int count = 0;
        //    if (reqs != null && reqs.size() > 0) {
        //        for (final EnumTag tag : reqs.getAspects()) {
        //            mposx = par1 - (var5 + 24 + 16 * count + (5 - reqs.size()) * 8);
        //            mposy = par2 - (var6 + 72);
        //            if (mposx >= 0 && mposy >= 0 && mposx < 16 && mposy < 16) {
        //                UtilsFX.drawCustomTooltip(
        //                    (GuiScreen) this,
        //                    GuiInfusionWorkbench.field_74196_a,
        //                    this.field_73886_k,
        //                    Arrays.asList(tag.name, tag.meaning),
        //                    par1,
        //                    par2,
        //                    11
        //                );
        //            }
        //            ++count;
        //        }
        //    }
        //}
    }

    @Override
    protected void
    drawGuiContainerBackgroundLayer(final float par1, final int par2, final int par3) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(3042);
        this.mc.renderEngine.bindTexture(new ResourceLocation(
            "classiccasting", "textures/gui/gui_infusionworkbench.png"
        ));
        final int var5 = (this.width - this.xSize) / 2;
        final int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
        GL11.glDisable(3042);
        ItemStack result = null;
        TileMagicWorkbench bridge
            = AuracoreCraftingManager.createBridgeInventory(this.tileEntity, 0, 9);
        IArcaneRecipe arcaneRecipe
            = AuracoreCraftingManager.findMatchingArcaneRecipe(bridge, this.ip.player);
        IInfusionRecipe rec = null;
        if (arcaneRecipe != null) {
            result = arcaneRecipe.getCraftingResult(bridge);
        } else {
            rec = AuracoreCraftingManager.findMatchingInfusionRecipe(
                bridge, this.ip.player
            );
            if (rec != null) {
                result = rec.getRecipeOutput();
                final AspectList reqs = rec.getAspects();
                int count = 0;
                if (reqs != null && reqs.size() > 0) {
                    for (final Aspect tag : reqs.getAspects()) {
                        float op = 1.0f;
                        if (this.tileEntity.foundAspects.getAmount(tag)
                            < reqs.getAmount(tag)) {
                            op = (float
                                 ) (MathHelper.sin(
                                        this.ip.player.ticksExisted - count * 10
                                    )
                                    / 8.0f)
                                    * 0.125f
                                + 0.25f;
                        }
                        UtilsFX.drawTag(
                            var5 + 24 + 16 * count + (5 - reqs.size()) * 8,
                            var6 + 72,
                            tag,
                            reqs.getAmount(tag),
                            0,
                            op
                        );
                        ++count;
                    }
                }
            }
        }
        if (this.tileEntity.getStackInSlot(10) != null) {
            final int charge = ((IWand) this.tileEntity.getStackInSlot(10).getItem())
                                   .getVis(this.tileEntity.getStackInSlot(10));
            if (charge > 0) {
                GL11.glPushMatrix();
                GL11.glTranslatef((float) (var5 + 140), (float) (var6 + 85), 505.0f);
                GL11.glScalef(0.5f, 0.5f, 0.0f);
                final String text = charge + " vis";
                final int ll = this.fontRendererObj.getStringWidth(text) / 2;
                this.fontRendererObj.drawStringWithShadow(text, -ll, -16, 16777215);
                GL11.glScalef(1.0f, 1.0f, 1.0f);
                GL11.glPopMatrix();
            }
            if (result != null) {
                final int discount
                    = 100 - Math.min(50, WandManager.getTotalVisDiscount(this.ip.player));
                int cost1 = arcaneRecipe != null
                    ? AuracoreCraftingManager.getArcaneRecipeVisCost(arcaneRecipe, bridge)
                    : 0;
                cost1 = Math.round(cost1 * (discount / 100.0f));
                int cost2 = rec != null ? rec.getCost() : 0;
                cost2 = Math.round(cost2 * (discount / 100.0f));
                if (charge < cost1 || charge < cost2) {
                    GL11.glPushMatrix();
                    RenderHelper.enableGUIStandardItemLighting();
                    GL11.glDisable(2896);
                    GL11.glEnable(32826);
                    GL11.glEnable(2903);
                    GL11.glEnable(2896);
                    RenderItem.getInstance().renderItemIntoGUI(
                        this.mc.fontRenderer,
                        this.mc.renderEngine,
                        result,
                        var5 + 132,
                        var6 + 28
                    );
                    RenderItem.getInstance().renderItemOverlayIntoGUI(
                        this.mc.fontRenderer,
                        this.mc.renderEngine,
                        result,
                        var5 + 132,
                        var6 + 28
                    );
                    GL11.glDisable(2896);
                    GL11.glDepthMask(true);
                    GL11.glEnable(2929);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glTranslatef((float) (var5 + 140), (float) (var6 + 85), 0.0f);
                    GL11.glScalef(0.5f, 0.5f, 0.0f);
                    String text2 = "Insufficient charge";
                    if (cost1 > ((IWand) this.tileEntity.getStackInSlot(10).getItem())
                                    .getMaxVis(this.tileEntity.getStackInSlot(10))
                        || cost2 > ((IWand) this.tileEntity.getStackInSlot(10).getItem())
                                       .getMaxVis(this.tileEntity.getStackInSlot(10))) {
                        text2 = "This wand is too weak";
                    }
                    final int ll2 = this.fontRendererObj.getStringWidth(text2) / 2;
                    this.fontRendererObj.drawString(text2, -ll2, 0, 15625838);
                    GL11.glScalef(1.0f, 1.0f, 1.0f);
                    GL11.glPopMatrix();
                }
                if (cost1 > 0 || cost2 > 0) {
                    GL11.glPushMatrix();
                    GL11.glTranslatef((float) (var5 + 140), (float) (var6 + 81), 0.0f);
                    GL11.glScalef(0.5f, 0.5f, 0.0f);
                    final String text2 = Math.max(cost1, cost2) + " vis";
                    final int ll2 = this.fontRendererObj.getStringWidth(text2) / 2;
                    this.fontRendererObj.drawStringWithShadow(text2, -ll2, -64, 15658734);
                    GL11.glScalef(1.0f, 1.0f, 1.0f);
                    GL11.glPopMatrix();
                }
            }
        }
    }
}
