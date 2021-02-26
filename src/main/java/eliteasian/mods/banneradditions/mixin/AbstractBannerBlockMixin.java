package eliteasian.mods.banneradditions.mixin;

import eliteasian.mods.banneradditions.banner.NewBannerTileEntity;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(AbstractBannerBlock.class)
public class AbstractBannerBlockMixin extends ContainerBlock {
    @Shadow
    private DyeColor color;

    protected AbstractBannerBlockMixin(Block.Properties properties) {
        super(properties);
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new NewBannerTileEntity(this.color);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (stack.hasDisplayName()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof NewBannerTileEntity) {
                ((NewBannerTileEntity)tileentity).func_213136_a(stack.getDisplayName());
            }
        }

    }

    @Override
    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity instanceof NewBannerTileEntity ? ((NewBannerTileEntity)tileentity).getItem(state) : super.getItem(worldIn, pos, state);
    }
}
