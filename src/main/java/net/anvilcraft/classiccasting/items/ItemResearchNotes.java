package net.anvilcraft.classiccasting.items;

import java.util.List;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.anvilcraft.classiccasting.research.CCResearchManager;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketResearchComplete;
import thaumcraft.common.lib.research.ResearchManager;

public class ItemResearchNotes extends Item {
    @SideOnly(value = Side.CLIENT)
    public IIcon iconNote;
    @SideOnly(value = Side.CLIENT)
    public IIcon iconNoteOver;
    @SideOnly(value = Side.CLIENT)
    public IIcon iconDiscovery;
    @SideOnly(value = Side.CLIENT)
    public IIcon iconDiscoveryOver;

    public ItemResearchNotes() {
        super();
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
    }

    @SideOnly(value = Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister ir) {
        this.iconNote = ir.registerIcon("thaumcraft:researchnotes");
        this.iconNoteOver = ir.registerIcon("thaumcraft:researchnotesoverlay");
        this.iconDiscovery = ir.registerIcon("thaumcraft:discovery");
        this.iconDiscoveryOver = ir.registerIcon("thaumcraft:discoveryoverlay");
    }

    @SideOnly(value = Side.CLIENT)
    @Override
    public IIcon getIcon(ItemStack stack, int par1) {
        return par1 / 64 == 0 ? this.iconNote : this.iconDiscovery;
    }

    @SideOnly(value = Side.CLIENT)
    @Override
    public IIcon getIconFromDamageForRenderPass(int par1, int renderPass) {
        return renderPass == 0
            ? (par1 / 64 == 0 ? this.iconNote : this.iconDiscovery)
            : (par1 / 64 == 0 ? this.iconNoteOver : this.iconDiscoveryOver);
    }

    @Override
    public ItemStack
    onItemRightClick(ItemStack stack, World par2World, EntityPlayer player) {
        if (!par2World.isRemote
            && CCResearchManager.getData(stack).getTotalProgress() == 1.0f
            && !ResearchManager.isResearchComplete(
                player.getDisplayName(), CCResearchManager.getData((ItemStack) stack).key
            )) {
            if (ResearchManager.doesPlayerHaveRequisites(
                    player.getDisplayName(),
                    CCResearchManager.getData((ItemStack) stack).key
                )) {
                PacketHandler.INSTANCE.sendTo(
                    new PacketResearchComplete(
                        CCResearchManager.getData((ItemStack) stack).key
                    ),
                    (EntityPlayerMP) player
                );
                Thaumcraft.proxy.getResearchManager().completeResearch(
                    player, CCResearchManager.getData(stack).key
                );
                String[] siblings
                    = ResearchCategories
                          .getResearch(
                              (String) CCResearchManager.getData((ItemStack) stack).key
                          )
                          .siblings;
                if (siblings != null) {
                    for (String s : siblings) {
                        ResearchItem sibling = ResearchCategories.getResearch(s);
                        if (s == null)
                            continue;
                        if (ResearchManager.isResearchComplete(
                                player.getDisplayName(), sibling.key
                            )
                            || !ResearchManager.doesPlayerHaveRequisites(
                                player.getDisplayName(), sibling.key
                            ))
                            continue;
                        PacketHandler.INSTANCE.sendTo(
                            new PacketResearchComplete(sibling.key),
                            (EntityPlayerMP) player
                        );
                        Thaumcraft.proxy.getResearchManager().completeResearch(
                            player, sibling.key
                        );
                    }
                }
                --stack.stackSize;
                par2World.playSoundAtEntity(player, "thaumcraft.learn", 0.75f, 1.0f);
            } else {
                player.addChatMessage(new ChatComponentText(
                    LanguageRegistry.instance().getStringLocalization("tc.discoveryerror")
                ));
            }
        }
        return stack;
    }

    @Override
    @SideOnly(value = Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int par2) {
        int md = stack.getItemDamage();
        if (md >= 64) {
            md -= 64;
        }
        //TODO: WTF
        //return stack.getItemDamage() == 0 || par2 == 0 ? 0xFFFFFF :
        //EnumTag.get((int)md).color;
        return 0xFFFFFF;
    }

    @Override
    @SideOnly(value = Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemstack) {
        return itemstack.getItemDamage() < 64
            ? LanguageRegistry.instance().getStringLocalization("item.researchnotes.name")
            : LanguageRegistry.instance().getStringLocalization("item.discovery.name");
    }

    @Override
    public void addInformation(
        ItemStack stack, EntityPlayer par2EntityPlayer, List list, boolean par4
    ) {
        if (CCResearchManager.getData(stack).getTotalProgress() >= 0.2f) {
            ResearchItem item = ResearchCategories.getResearch(
                CCResearchManager.getData((ItemStack) stack).key
            );
            if (item != null)
                list.add(item.getName());
        } else {
            list.add(
                LanguageRegistry.instance().getStringLocalization("tc.discoveryunknown")
            );
        }
        if (stack.getItemDamage() < 64
            && CCResearchManager.getData(stack).getTotalProgress() > 0.333332f) {
            list.add(
                (int) (CCResearchManager.getData(stack).getTotalProgress() * 100.0f)
                + LanguageRegistry.instance().getStringLocalization("tc.discoveryprogress"
                )
            );
        }
    }

    public EnumRarity getRarity(ItemStack itemstack) {
        return itemstack.getItemDamage() < 64 ? EnumRarity.rare : EnumRarity.epic;
    }
}
