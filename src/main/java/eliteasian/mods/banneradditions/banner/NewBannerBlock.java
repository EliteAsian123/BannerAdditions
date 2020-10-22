package eliteasian.mods.banneradditions.banner;

import com.google.common.collect.Maps;
import java.util.Map;

import eliteasian.mods.banneradditions.BannerAdditions;
import eliteasian.mods.banneradditions.BannerAdditionsRegistry;
import net.minecraft.block.*;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;

public class NewBannerBlock extends NewAbstractBannerBlock implements IWaterLoggable {
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_0_15;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final Map<DyeColor, Block> BANNERS_BY_COLOR = Maps.newHashMap();
    private static final VoxelShape SHAPE = Block.makeCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);

    public NewBannerBlock(DyeColor color, AbstractBlock.Properties properties) {
        super(color, properties);

        if (BannerAdditions.morewaterloggingLoaded) {
            this.setDefaultState(this.stateContainer.getBaseState().with(ROTATION, Integer.valueOf(0)).with(WATERLOGGED, false));
        } else {
            this.setDefaultState(this.stateContainer.getBaseState().with(ROTATION, Integer.valueOf(0)));
        }

        BANNERS_BY_COLOR.put(color, this);
    }

    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.down()).getMaterial().isSolid();
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = this.getDefaultState().with(ROTATION, Integer.valueOf(MathHelper.floor((double)((180.0F + context.getPlacementYaw()) * 16.0F / 360.0F) + 0.5D) & 15));

        if (BannerAdditions.morewaterloggingLoaded) {
            FluidState fluidstate = context.getWorld().getFluidState(context.getPos());
            state = state.with(WATERLOGGED, fluidstate.getFluid() == Fluids.WATER);
        }

        return state;
    }

    /**
     * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
     * returns its solidified counterpart.
     * Note that this method should ideally consider only the specific face passed in.
     */
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (BannerAdditions.morewaterloggingLoaded) {
            if (stateIn.get(WATERLOGGED)) {
                worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
            }
        }

        return facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via IBlockState#withRotation(Rotation) whenever possible. Implementing/overriding is
     * fine.
     */
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(ROTATION, Integer.valueOf(rot.rotate(state.get(ROTATION), 16)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     * @deprecated call via IBlockState#withMirror(Mirror) whenever possible. Implementing/overriding is fine.
     */
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.with(ROTATION, Integer.valueOf(mirrorIn.mirrorRotation(state.get(ROTATION), 16)));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ROTATION);

        if (BannerAdditions.morewaterloggingLoaded)
            builder.add(WATERLOGGED);
    }

    @OnlyIn(Dist.CLIENT)
    public static Block forColor(DyeColor p_196287_0_) {
        return BANNERS_BY_COLOR.getOrDefault(p_196287_0_, BannerAdditionsRegistry.Blocks.WHITE_BANNER);
    }

    public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
        if (BannerAdditions.morewaterloggingLoaded) {
            return !state.get(WATERLOGGED) && fluidIn == Fluids.WATER;
        }

        return false;
    }

    public boolean receiveFluid(IWorld worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        if (BannerAdditions.morewaterloggingLoaded) {
            if (!state.get(WATERLOGGED) && fluidStateIn.getFluid() == Fluids.WATER) {
                if (!worldIn.isRemote()) {
                    worldIn.setBlockState(pos, state.with(WATERLOGGED, Boolean.valueOf(true)), 3);
                    worldIn.getPendingFluidTicks().scheduleTick(pos, fluidStateIn.getFluid(), fluidStateIn.getFluid().getTickRate(worldIn));
                }

                return true;
            }
        }

        return false;
    }

    public Fluid pickupFluid(IWorld worldIn, BlockPos pos, BlockState state) {
        if (BannerAdditions.morewaterloggingLoaded) {
            if (state.get(WATERLOGGED)) {
                worldIn.setBlockState(pos, state.with(WATERLOGGED, Boolean.valueOf(false)), 3);
                return Fluids.WATER;
            }
        }

        return Fluids.EMPTY;
    }

    public FluidState getFluidState(BlockState state) {
        if (BannerAdditions.morewaterloggingLoaded) {
            return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
        }

        return Fluids.EMPTY.getDefaultState();
    }
}