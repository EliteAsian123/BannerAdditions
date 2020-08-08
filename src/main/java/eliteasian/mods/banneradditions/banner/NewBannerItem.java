package eliteasian.mods.banneradditions.banner;

import java.util.List;
import javax.annotation.Nullable;

import eliteasian.mods.banneradditions.bannerpattern.BannerPatternHolder;
import eliteasian.mods.banneradditions.bannerpattern.BannerPatterns;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WallOrFloorItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.commons.lang3.Validate;

public class NewBannerItem extends WallOrFloorItem {
    public NewBannerItem(Block p_i48529_1_, Block p_i48529_2_, Item.Properties builder) {
        super(p_i48529_1_, p_i48529_2_, builder.setISTER(() -> BannerItemRenderer::new));
        Validate.isInstanceOf(NewAbstractBannerBlock.class, p_i48529_1_);
        Validate.isInstanceOf(NewAbstractBannerBlock.class, p_i48529_2_);
    }

    @OnlyIn(Dist.CLIENT)
    public static void appendHoverTextFromTileEntityTag(ItemStack stack, List<ITextComponent> p_185054_1_) {
        CompoundNBT compoundnbt = stack.getChildTag("BlockEntityTag");
        if (compoundnbt != null && compoundnbt.contains("Patterns")) {
            ListNBT listnbt = compoundnbt.getList("Patterns", 10);

            for(int i = 0; i < listnbt.size(); ++i) {
                CompoundNBT compoundnbt1 = listnbt.getCompound(i);
                DyeColor dyecolor = DyeColor.byId(compoundnbt1.getInt("Color"));
                BannerPatternHolder bannerpattern = BannerPatterns.get(compoundnbt1.getString("Pattern"));
                if (bannerpattern != null) {
                    p_185054_1_.add((new TranslationTextComponent(bannerpattern.getTranslationKey() + '.' + dyecolor.getTranslationKey())).func_240699_a_(TextFormatting.GRAY));
                }
            }

        }
    }

    public DyeColor getColor() {
        return ((NewAbstractBannerBlock)this.getBlock()).getColor();
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        appendHoverTextFromTileEntityTag(stack, tooltip);
    }
}