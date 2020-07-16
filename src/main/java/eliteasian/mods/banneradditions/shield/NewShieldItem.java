package eliteasian.mods.banneradditions.shield;

import java.util.List;
import javax.annotation.Nullable;

import eliteasian.mods.banneradditions.banner.NewBannerItem;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class NewShieldItem extends ShieldItem {
    public NewShieldItem(Item.Properties builder) {
        super(builder.setISTER(ShieldItemRenderer::ister));
        DispenserBlock.registerDispenseBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        NewBannerItem.appendHoverTextFromTileEntityTag(stack, tooltip);
    }
}