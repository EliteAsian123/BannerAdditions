package eliteasian.mods.banneradditions.banner;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.*;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.concurrent.Callable;

@OnlyIn(Dist.CLIENT)
public class BannerItemRenderer extends ItemStackTileEntityRenderer {
    public static final BannerItemRenderer instance = new BannerItemRenderer();

    private final NewBannerTileEntity banner = new NewBannerTileEntity();

    @Override
    public void func_239207_a_(ItemStack p_239207_1_, ItemCameraTransforms.TransformType p_239207_2_, MatrixStack p_239207_3_, IRenderTypeBuffer p_239207_4_, int p_239207_5_, int p_239207_6_) {
        Item item = p_239207_1_.getItem();
        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();

            if (block instanceof NewAbstractBannerBlock) {
                this.banner.loadFromItemStack(p_239207_1_, ((NewAbstractBannerBlock) block).getColor());
                TileEntityRendererDispatcher.instance.renderItem(this.banner, p_239207_3_, p_239207_4_, p_239207_5_, p_239207_6_);
            }
        }
    }

    public static Callable<ItemStackTileEntityRenderer> ister() {
        return instance::getSelf;
    }

    private ItemStackTileEntityRenderer getSelf() {
        return instance;
    }
}
