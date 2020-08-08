package eliteasian.mods.banneradditions.loom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class NewLoomBlock extends HorizontalBlock {
    public NewLoomBlock(Block.Properties properties) {
        super(properties);
    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            player.openContainer(state.getContainer(worldIn, pos));
            player.addStat(Stats.INTERACT_WITH_LOOM);
            return ActionResultType.CONSUME;
        }
    }

    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
        return new SimpleNamedContainerProvider((p_220254_2_, p_220254_3_, p_220254_4_) -> new NewLoomContainer(p_220254_2_, p_220254_3_, IWorldPosCallable.of(worldIn, pos)), new TranslationTextComponent("container.loom"));
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }
}