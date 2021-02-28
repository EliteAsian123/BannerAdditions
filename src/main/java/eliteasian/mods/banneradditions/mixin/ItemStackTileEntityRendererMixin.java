package eliteasian.mods.banneradditions.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.util.Pair;
import eliteasian.mods.banneradditions.banner.NewBannerTileEntity;
import eliteasian.mods.banneradditions.banner.NewBannerTileEntityRenderer;
import eliteasian.mods.banneradditions.bannerpattern.BannerPatternHolder;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.model.ShieldModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

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
        } else if (item == Items.SHIELD) {
            ShieldModel modelShield = new ShieldModel();

            boolean flag = stack.getChildTag("BlockEntityTag") != null;
            matrixStack.push();
            matrixStack.scale(1.0F, -1.0F, -1.0F);
            RenderMaterial rendermaterial = flag ? ModelBakery.LOCATION_SHIELD_BASE : ModelBakery.LOCATION_SHIELD_NO_PATTERN;
            IVertexBuilder ivertexbuilder = rendermaterial.getSprite().wrapBuffer(ItemRenderer.getEntityGlintVertexBuilder(buffer, modelShield.getRenderType(rendermaterial.getAtlasLocation()), true, stack.hasEffect()));
            modelShield.func_228294_b_().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
            if (flag) {
                List<Pair<BannerPatternHolder, DyeColor>> list = NewBannerTileEntity.createPatternList(ShieldItem.getColor(stack), NewBannerTileEntity.getPatternsFromItemStack(stack));
                NewBannerTileEntityRenderer.func_241717_a_(matrixStack, buffer, combinedLight, combinedOverlay, modelShield.func_228293_a_(), rendermaterial, false, list, stack.hasEffect());
            } else {
                modelShield.func_228293_a_().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
            }

            matrixStack.pop();
            ci.cancel();
        }
    }
}
