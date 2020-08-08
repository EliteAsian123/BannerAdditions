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
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.RenderMaterial;
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
    public void func_239207_a_(ItemStack p_239207_1_, ItemCameraTransforms.TransformType p_239207_2_, MatrixStack p_239207_3_, IRenderTypeBuffer p_239207_4_, int p_239207_5_, int p_239207_6_) {
        Item item = p_239207_1_.getItem();
        if (item == BannerAdditionsRegistry.Items.SHIELD) {
            boolean flag = p_239207_1_.getChildTag("BlockEntityTag") != null;
            p_239207_3_.push();
            p_239207_3_.scale(1.0F, -1.0F, -1.0F);
            RenderMaterial rendermaterial = flag ? ModelBakery.LOCATION_SHIELD_BASE : ModelBakery.LOCATION_SHIELD_NO_PATTERN;
            IVertexBuilder ivertexbuilder = rendermaterial.getSprite().wrapBuffer(ItemRenderer.func_239391_c_(p_239207_4_, this.modelShield.getRenderType(rendermaterial.getAtlasLocation()), true, p_239207_1_.hasEffect()));
            this.modelShield.func_228294_b_().render(p_239207_3_, ivertexbuilder, p_239207_5_, p_239207_6_, 1.0F, 1.0F, 1.0F, 1.0F);
            if (flag) {
                List<Pair<BannerPatternHolder, DyeColor>> list = NewBannerTileEntity.createPatternList(NewShieldItem.getColor(p_239207_1_), NewBannerTileEntity.getPatternsFromItemStack(p_239207_1_));
                NewBannerTileEntityRenderer.func_241717_a_(p_239207_3_, p_239207_4_, p_239207_5_, p_239207_6_, this.modelShield.func_228293_a_(), rendermaterial, false, list, p_239207_1_.hasEffect());
            } else {
                this.modelShield.func_228293_a_().render(p_239207_3_, ivertexbuilder, p_239207_5_, p_239207_6_, 1.0F, 1.0F, 1.0F, 1.0F);
            }

            p_239207_3_.pop();
        }
    }
}
