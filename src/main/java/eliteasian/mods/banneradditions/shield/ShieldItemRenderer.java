package eliteasian.mods.banneradditions.shield;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.util.Pair;
import eliteasian.mods.banneradditions.BannerAdditionsRegistry;
import eliteasian.mods.banneradditions.bannerpattern.BannerPatternHolder;
import eliteasian.mods.banneradditions.banner.NewBannerTileEntity;
import eliteasian.mods.banneradditions.banner.NewBannerTileEntityRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.model.ShieldModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ShieldItemRenderer extends ItemStackTileEntityRenderer {
    public static final ShieldItemRenderer instance = new ShieldItemRenderer();

    private final ShieldModel modelShield = new ShieldModel();

    @Override
    public void render(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Item item = itemStackIn.getItem();
        if (item == BannerAdditionsRegistry.Items.SHIELD) {
            boolean flag = itemStackIn.getChildTag("BlockEntityTag") != null;
            matrixStackIn.push();
            matrixStackIn.scale(1.0F, -1.0F, -1.0F);
            Material rendermaterial = flag ? ModelBakery.LOCATION_SHIELD_BASE : ModelBakery.LOCATION_SHIELD_NO_PATTERN;
            IVertexBuilder ivertexbuilder = rendermaterial.getSprite().wrapBuffer(ItemRenderer.getBuffer(bufferIn, this.modelShield.getRenderType(rendermaterial.getAtlasLocation()), true, itemStackIn.hasEffect()));
            this.modelShield.func_228294_b_().render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            if (flag) {
                List<Pair<BannerPatternHolder, DyeColor>> list = NewBannerTileEntity.createPatternList(NewShieldItem.getColor(itemStackIn), NewBannerTileEntity.getPatternsFromItemStack(itemStackIn));
                NewBannerTileEntityRenderer.func_241717_a_(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, this.modelShield.func_228293_a_(), rendermaterial, false, list, itemStackIn.hasEffect());
            } else {
                this.modelShield.func_228293_a_().render(matrixStackIn, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            }

            matrixStackIn.pop();
        }
    }
}
