package eliteasian.mods.banneradditions.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import eliteasian.mods.banneradditions.banner.NewBannerTileEntity;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStackTileEntityRenderer.class)
public class ItemStackTileEntityRendererMixin {
    @Inject(method = "func_239207_a_", at = @At("HEAD"), cancellable = true)
    public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType p_239207_2_, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay, CallbackInfo ci) {
        Item item = stack.getItem();
        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();

            if (block instanceof AbstractBannerBlock) {
                NewBannerTileEntity banner = new NewBannerTileEntity();
                banner.loadFromItemStack(stack, ((AbstractBannerBlock) block).getColor());
                TileEntityRendererDispatcher.instance.renderItem(banner, matrixStack, buffer, combinedLight, combinedOverlay);
                ci.cancel();
            }
        }
    }
}
