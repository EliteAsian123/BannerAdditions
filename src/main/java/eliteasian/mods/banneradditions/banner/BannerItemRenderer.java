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

@OnlyIn(Dist.CLIENT)
public class BannerItemRenderer extends ItemStackTileEntityRenderer {
    public static final BannerItemRenderer instance = new BannerItemRenderer();

    private final NewBannerTileEntity banner = new NewBannerTileEntity();

    @Override
    public void render(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Item item = itemStackIn.getItem();
        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();

            if (block instanceof NewAbstractBannerBlock) {
                this.banner.loadFromItemStack(itemStackIn, ((NewAbstractBannerBlock) block).getColor());
                TileEntityRendererDispatcher.instance.renderItem(this.banner, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
            }
        }
    }
}
