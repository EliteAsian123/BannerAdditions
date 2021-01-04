package eliteasian.mods.banneradditions.loom;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import javax.annotation.Nullable;

import eliteasian.mods.banneradditions.BannerAdditions;
import eliteasian.mods.banneradditions.BannerAdditionsConfig;
import eliteasian.mods.banneradditions.BannerAdditionsRegistry;
import eliteasian.mods.banneradditions.banner.*;
import eliteasian.mods.banneradditions.bannerpattern.BannerPatternHolder;
import eliteasian.mods.banneradditions.bannerpattern.BannerPatterns;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NewLoomScreen extends ContainerScreen<NewLoomContainer> {
    private static final ResourceLocation guiTexture = new ResourceLocation("minecraft", "textures/gui/container/loom.png");
    private final ModelRenderer bannerRenderer;
    @Nullable
    private List<Pair<BannerPatternHolder, DyeColor>> bannerPatterns;
    private ItemStack bannerStack = ItemStack.EMPTY;
    private ItemStack dyeStack = ItemStack.EMPTY;
    private ItemStack patternStack = ItemStack.EMPTY;
    private boolean hasNeededItems;
    private boolean isMaxPatterns;
    private int patternLength;
    private float scrollAmountFloat;
    private boolean scrollSelected;
    private int scrollAmount = 1;

    public NewLoomScreen(NewLoomContainer p_i51081_1_, PlayerInventory p_i51081_2_, ITextComponent p_i51081_3_) {
        super(p_i51081_1_, p_i51081_2_, p_i51081_3_);
        this.bannerRenderer = NewBannerTileEntityRenderer.func_228836_a_();
        p_i51081_1_.func_217020_a(this::update);
        this.titleY -= 2;

        this.patternLength = BannerPatterns.getWithItem(null).length;
    }

    // Render
    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        this.renderHoveredTooltip(p_230430_1_, p_230430_2_, p_230430_3_);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float p_230450_2_, int mouseX, int mouseY) {
        // Render background
        this.renderBackground(matrixStack);

        this.minecraft.getTextureManager().bindTexture(guiTexture);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
        Slot slot = this.container.getBannerSlot();
        Slot slot1 = this.container.getDyeSlot();
        Slot slot2 = this.container.getPatternSlot();
        Slot slot3 = this.container.getOutputSlot();

        if (!slot.getHasStack()) {
            this.blit(matrixStack, i + slot.xPos, j + slot.yPos, this.xSize, 0, 16, 16);
        }

        if (!slot1.getHasStack()) {
            this.blit(matrixStack,i + slot1.xPos, j + slot1.yPos, this.xSize + 16, 0, 16, 16);
        }

        if (!slot2.getHasStack()) {
            this.blit(matrixStack,i + slot2.xPos, j + slot2.yPos, this.xSize + 32, 0, 16, 16);
        }

        int k = (int)(41.0F * this.scrollAmountFloat);
        this.blit(matrixStack,i + 119, j + 13 + k, 232 + (this.hasNeededItems ? 0 : 12), 0, 12, 15);
        RenderHelper.setupGuiFlatDiffuseLighting();
        if (this.bannerPatterns != null && !this.isMaxPatterns) {
            IRenderTypeBuffer.Impl irendertypebuffer$impl = this.minecraft.getRenderTypeBuffers().getBufferSource();
            matrixStack.push();
            matrixStack.translate((double)(i + 139), (double)(j + 52), 0.0D);
            matrixStack.scale(24.0F, -24.0F, 1.0F);
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            float f = 0.6666667F;
            matrixStack.scale(0.6666667F, -0.6666667F, -0.6666667F);
            this.bannerRenderer.rotateAngleX = 0.0F;
            this.bannerRenderer.rotationPointY = -32.0F;
            NewBannerTileEntityRenderer.func_230180_a_(matrixStack, irendertypebuffer$impl, 15728880, OverlayTexture.NO_OVERLAY, this.bannerRenderer, ModelBakery.LOCATION_BANNER_BASE, true, this.bannerPatterns);
            matrixStack.pop();
            irendertypebuffer$impl.finish();
        } else if (this.isMaxPatterns) {
            this.blit(matrixStack, i + slot3.xPos - 2, j + slot3.yPos - 2, this.xSize, 17, 17, 16);
        }

        if (this.hasNeededItems) {
            BannerPatternHolder[] bannerPatterns = BannerPatterns.getWithItem(this.patternStack.getItem());

            int j2 = i + 60;
            int l2 = j + 13;
            int containerEnd = this.scrollAmount + 16;

            for (int i1 = this.scrollAmount; i1 < containerEnd && i1 < (this.patternStack.getItem() == Items.AIR ? bannerPatterns.length : bannerPatterns.length + 1); ++i1) {
                int j1 = i1 - this.scrollAmount;
                int k1 = j2 + j1 % 4 * 14;
                int l1 = l2 + j1 / 4 * 14;
                this.minecraft.getTextureManager().bindTexture(guiTexture);
                int i2 = this.ySize;
                if (i1 == (this.patternStack.getItem() == Items.AIR ? this.container.getSelectedPattern() : this.container.getSelectedPattern() + 1)) {
                    i2 += 14;
                } else if (mouseX >= k1 && mouseY >= l1 && mouseX < k1 + 14 && mouseY < l1 + 14) {
                    i2 += 28;
                }

                this.blit(matrixStack, k1, l1, 0, i2, 14, 14);
                this.renderPatternButton(bannerPatterns[this.patternStack.getItem() == Items.AIR ? i1 : i1 - 1], k1, l1);
            }
        }

        RenderHelper.setupGui3DDiffuseLighting();
    }

    private void renderPatternButton(BannerPatternHolder p_228190_1_, int p_228190_2_, int p_228190_3_) {
        ItemStack itemstack = new ItemStack(BannerAdditionsRegistry.Items.GRAY_BANNER);

        CompoundNBT compoundnbt = itemstack.getOrCreateChildTag("BlockEntityTag");
        compoundnbt.put("Patterns", BannerPatterns.toListNBT(new Pair<>(BannerPatterns.get(0), DyeColor.GRAY), new Pair<>(p_228190_1_, DyeColor.WHITE)));

        MatrixStack matrixstack = new MatrixStack();
        matrixstack.push();
        matrixstack.translate((float) p_228190_2_ + 0.5F, p_228190_3_ + 16, 0.0D);
        matrixstack.scale(6.0F, -6.0F, 1.0F);
        matrixstack.translate(0.5D, 0.5D, 0.0D);
        matrixstack.translate(0.5D, 0.5D, 0.5D);
        float f = 0.6666667F;
        matrixstack.scale(f, -f, -f);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = this.minecraft.getRenderTypeBuffers().getBufferSource();
        this.bannerRenderer.rotateAngleX = 0.0F;
        this.bannerRenderer.rotationPointY = -32.0F;
        List<Pair<BannerPatternHolder, DyeColor>> list = NewBannerTileEntity.createPatternList(DyeColor.GRAY, NewBannerTileEntity.getPatternsFromItemStack(itemstack));
        NewBannerTileEntityRenderer.func_230180_a_(matrixstack, irendertypebuffer$impl, 15728880, OverlayTexture.NO_OVERLAY, this.bannerRenderer, ModelBakery.LOCATION_BANNER_BASE, true, list);
        matrixstack.pop();
        irendertypebuffer$impl.finish();
    }

    // Mouse clicked
    public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
        this.scrollSelected = false;

        if (this.hasNeededItems) {
            int containerLeft = this.guiLeft + 60;
            int containerTop = this.guiTop + 13;
            int containerEnd = this.scrollAmount + 16;

            for (int l = this.scrollAmount; l < containerEnd; ++l) {
                int i1 = l - this.scrollAmount;

                double d0 = p_231044_1_ - (double)(containerLeft + i1 % 4 * 14);
                double d1 = p_231044_3_ - (double)(containerTop + i1 / 4 * 14);

                if (d0 >= 0.0D && d1 >= 0.0D && d0 < 14.0D && d1 < 14.0D && this.container.enchantItem(this.minecraft.player, this.patternStack.getItem() == Items.AIR ? l : l - 1)) {
                    Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_LOOM_SELECT_PATTERN, 1.0F));

                    this.minecraft.playerController.sendEnchantPacket((this.container).windowId, this.patternStack.getItem() == Items.AIR ? l : l - 1);

                    return true;
                }
            }

            containerLeft = this.guiLeft + 119;
            containerTop = this.guiTop + 9;
            if (p_231044_1_ >= (double) containerLeft && p_231044_1_ < (double) (containerLeft + 12) && p_231044_3_ >= (double) containerTop && p_231044_3_ < (double) (containerTop + 56)) {
                this.scrollSelected = true;
            }
        }

        return super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
    }

    // Mouse dragged
    public boolean mouseDragged(double p_231045_1_, double p_231045_3_, int p_231045_5_, double p_231045_6_, double p_231045_8_) {
        if (this.scrollSelected && this.hasNeededItems && this.patternLength > 16) {
            int containerTop = this.guiTop + 13;
            int j = containerTop + 56;
            this.scrollAmountFloat = ((float) p_231045_3_ - (float) containerTop - 7.5F) / ((float) (j - containerTop) - 15.0F);

            this.scrollAmountFloat = MathHelper.clamp(this.scrollAmountFloat, 0.0F, 1.0F);

            int l = (int) ((double) (this.scrollAmountFloat * ((float) this.patternLength / 4f - 4f)) + 0.5D);

            if (l < 0) {
                l = 0;
            }

            this.scrollAmount = 1 + l * 4;

            return true;
        } else {
            return super.mouseDragged(p_231045_1_, p_231045_3_, p_231045_5_, p_231045_6_, p_231045_8_);
        }
    }

    // Scroll with scroll wheel
    public boolean mouseScrolled(double p_231043_1_, double p_231043_3_, double p_231043_5_) {
        if (this.hasNeededItems && this.patternLength > 16) {
            float i = this.patternLength / 4f - 4f;
            this.scrollAmountFloat = (float) ((double) this.scrollAmountFloat - p_231043_5_ / (double) i);
            this.scrollAmountFloat = MathHelper.clamp(this.scrollAmountFloat, 0.0F, 1.0F);
            this.scrollAmount = 1 + (int) ((this.scrollAmountFloat * i) + 0.5f) * 4;
        }

        return true;
    }

    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
        return mouseX < (double)guiLeftIn || mouseY < (double)guiTopIn || mouseX >= (double)(guiLeftIn + this.xSize) || mouseY >= (double)(guiTopIn + this.ySize);
    }

    private void update() {
        ItemStack itemstack = this.container.getOutputSlot().getStack();
        if (itemstack.isEmpty()) {
            this.bannerPatterns = null;
        } else {
            this.bannerPatterns = NewBannerTileEntity.createPatternList(((NewBannerItem) itemstack.getItem()).getColor(), NewBannerTileEntity.getPatternsFromItemStack(itemstack));
        }

        ItemStack itemstack1 = this.container.getBannerSlot().getStack();
        ItemStack itemstack2 = this.container.getDyeSlot().getStack();
        ItemStack itemstack3 = this.container.getPatternSlot().getStack();
        CompoundNBT compoundnbt = itemstack1.getOrCreateChildTag("BlockEntityTag");

        this.isMaxPatterns = compoundnbt.contains("Patterns", 9) && !itemstack1.isEmpty() && compoundnbt.getList("Patterns", 10).size() >= BannerAdditionsConfig.CONFIG.maxPatternCount.get();

        if (this.isMaxPatterns) {
            this.bannerPatterns = null;
        }

        if (!ItemStack.areItemStacksEqual(itemstack1, this.bannerStack) || !ItemStack.areItemStacksEqual(itemstack2, this.dyeStack)) {
            this.hasNeededItems = !itemstack1.isEmpty() && !itemstack2.isEmpty() && !this.isMaxPatterns;
        }

        if (!ItemStack.areItemStacksEqual(itemstack3, this.patternStack)) {
            this.scrollAmountFloat = 0f;
            this.scrollAmount = 1;

            this.patternLength = BannerPatterns.getWithItem(itemstack3.getItem()).length;
        }

        this.bannerStack = itemstack1.copy();
        this.dyeStack = itemstack2.copy();
        this.patternStack = itemstack3.copy();
    }
}