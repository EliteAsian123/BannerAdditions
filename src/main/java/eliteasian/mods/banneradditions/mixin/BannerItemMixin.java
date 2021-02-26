package eliteasian.mods.banneradditions.mixin;

import eliteasian.mods.banneradditions.bannerpattern.BannerPatternHolder;
import eliteasian.mods.banneradditions.bannerpattern.BannerPatterns;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.Validate;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(BannerItem.class)
public class BannerItemMixin extends WallOrFloorItem {
    public BannerItemMixin(Block p_i48529_1_, Block p_i48529_2_, Item.Properties builder) {
        super(p_i48529_1_, p_i48529_2_, builder);
        Validate.isInstanceOf(AbstractBannerBlock.class, p_i48529_1_);
        Validate.isInstanceOf(AbstractBannerBlock.class, p_i48529_2_);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT compoundnbt = stack.getChildTag("BlockEntityTag");
        if (compoundnbt != null && compoundnbt.contains("Patterns")) {
            ListNBT listnbt = compoundnbt.getList("Patterns", 10);

            for(int i = 0; i < listnbt.size(); ++i) {
                CompoundNBT compoundnbt1 = listnbt.getCompound(i);
                DyeColor dyecolor = DyeColor.byId(compoundnbt1.getInt("Color"));
                BannerPatternHolder bannerpattern = BannerPatterns.get(compoundnbt1.getString("Pattern"));
                if (bannerpattern != null) {
                    tooltip.add((new TranslationTextComponent(bannerpattern.getTranslationKey() + '.' + dyecolor.getTranslationKey())).mergeStyle(TextFormatting.GRAY));
                }
            }
        }
    }
}
