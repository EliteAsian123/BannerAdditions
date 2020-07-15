package eliteasian.mods.banneradditions.banner;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class NewAbstractBannerBlock extends ContainerBlock {
    private final DyeColor color;

    protected NewAbstractBannerBlock(DyeColor color, AbstractBlock.Properties properties) {
        super(properties);
        this.color = color;
    }

    /**
     * Return true if an entity can be spawned inside the block (used to get the player's bed spawn location)
     */
    public boolean canSpawnInBlock() {
        return true;
    }

    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new NewBannerTileEntity(this.color);
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (stack.hasDisplayName()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof NewBannerTileEntity) {
                ((NewBannerTileEntity)tileentity).func_213136_a(stack.getDisplayName());
            }
        }

    }

    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity instanceof NewBannerTileEntity ? ((NewBannerTileEntity)tileentity).getItem(state) : super.getItem(worldIn, pos, state);
    }

    public DyeColor getColor() {
        return this.color;
    }
}
