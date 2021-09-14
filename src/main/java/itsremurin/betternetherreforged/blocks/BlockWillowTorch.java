package itsremurin.betternetherreforged.blocks;

import itsremurin.betternetherreforged.blocks.materials.MaterialBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import itsremurin.betternetherreforged.BlocksHelper;

public class BlockWillowTorch extends BlockBaseNotFull {
	private static final VoxelShape SHAPE_NORTH = Block.makeCuboidShape(5, 0, 8, 11, 16, 16);
	private static final VoxelShape SHAPE_SOUTH = Block.makeCuboidShape(5, 0, 0, 11, 16, 8);
	private static final VoxelShape SHAPE_WEST = Block.makeCuboidShape(8, 0, 5, 16, 16, 11);
	private static final VoxelShape SHAPE_EAST = Block.makeCuboidShape(0, 0, 5, 8, 16, 11);
	private static final VoxelShape SHAPE_UP = Block.makeCuboidShape(5, 0, 5, 11, 9, 11);
	private static final VoxelShape SHAPE_DOWN = Block.makeCuboidShape(5, 3, 5, 11, 16, 11);

	public static final DirectionProperty FACING = BlockStateProperties.FACING;

	public BlockWillowTorch() {
		super(MaterialBuilder.makeWood(MaterialColor.LIGHT_BLUE).setLightLevel((state) -> {return 15;}).doesNotBlockMovement().notSolid());
		this.setDefaultState(getStateContainer().getBaseState().with(FACING, Direction.DOWN));
		this.setRenderLayer(BNRenderLayer.CUTOUT);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		stateManager.add(FACING);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		switch (state.get(FACING)) {
			case NORTH:
				return SHAPE_NORTH;
			case SOUTH:
				return SHAPE_SOUTH;
			case EAST:
				return SHAPE_EAST;
			case WEST:
				return SHAPE_WEST;
			case UP:
				return SHAPE_UP;
			case DOWN:
			default:
				return SHAPE_DOWN;
		}
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		Direction direction = (Direction) state.get(FACING).getOpposite();
		return Block.hasEnoughSolidSide(world, pos.offset(direction), direction.getOpposite());
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return BlocksHelper.rotateHorizontal(state, rotation, FACING);
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return BlocksHelper.mirrorHorizontal(state, mirror, FACING);
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		if (isValidPosition(state, world, pos))
			return state;
		else
			return Blocks.AIR.getDefaultState();
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		BlockState blockState = this.getDefaultState();
		IWorldReader worldView = ctx.getWorld();
		BlockPos blockPos = ctx.getPos();
		Direction[] directions = ctx.getNearestLookingDirections();
		for (int i = 0; i < directions.length; ++i) {
			Direction direction = directions[i];
			Direction direction2 = direction.getOpposite();
			blockState = blockState.with(FACING, direction2);
			if (blockState.isValidPosition(worldView, blockPos)) {
				return blockState;
			}
		}
		return null;
	}
}

