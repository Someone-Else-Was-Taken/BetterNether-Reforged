package someoneelse.betternetherreforged.blocks;

import java.util.EnumMap;
import java.util.Random;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.registry.BlocksRegistry;
import someoneelse.betternetherreforged.structures.plants.StructureLucis;

public class BlockLucisSpore extends BlockBaseNotFull implements IGrowable {
	private static final EnumMap<Direction, VoxelShape> BOUNDING_SHAPES = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, makeCuboidShape(4, 4, 8, 12, 12, 16),
			Direction.SOUTH, makeCuboidShape(4, 4, 0, 12, 12, 8),
			Direction.WEST, makeCuboidShape(8, 4, 4, 16, 12, 12),
			Direction.EAST, makeCuboidShape(0, 4, 4, 8, 12, 12)));
	private static final StructureLucis STRUCTURE = new StructureLucis();
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

	public BlockLucisSpore() {
		super(AbstractBlock.Properties.create(Material.PLANTS, MaterialColor.LIME)
				.sound(SoundType.CROP)
				.zeroHardnessAndResistance()
				.notSolid()
				.doesNotBlockMovement()
				.tickRandomly()
				.setLightLevel((state) -> {return 7;}));
		this.setDefaultState(getStateContainer().getBaseState().with(FACING, Direction.NORTH));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		stateManager.add(FACING);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return BOUNDING_SHAPES.get(state.get(FACING));
	}

	@Override
	public boolean canGrow(IBlockReader world, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World world, Random random, BlockPos pos, BlockState state) {
		return random.nextInt(16) == 0;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		STRUCTURE.generate(world, pos, random);
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		if (isValidPosition(state, world, pos))
			return state;
		else
			return Blocks.AIR.getDefaultState();
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.tick(state, world, pos, random);
		if (canUseBonemeal(world, random, pos, state)) {
			grow(world, random, pos, state);
		}
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
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		Direction direction = (Direction) state.get(FACING);
		BlockPos blockPos = pos.offset(direction.getOpposite());
		BlockState blockState = world.getBlockState(blockPos);
		return BlocksHelper.isNetherrack(blockState) || BlocksRegistry.ANCHOR_TREE.isTreeLog(blockState.getBlock());
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		BlockState blockState = this.getDefaultState();
		IWorldReader worldView = ctx.getWorld();
		BlockPos blockPos = ctx.getPos();
		Direction[] directions = ctx.getNearestLookingDirections();
		for (int i = 0; i < directions.length; ++i) {
			Direction direction = directions[i];
			if (direction.getAxis().isHorizontal()) {
				Direction direction2 = direction.getOpposite();
				blockState = blockState.with(FACING, direction2);
				if (blockState.isValidPosition(worldView, blockPos)) {
					return blockState;
				}
			}
		}
		return null;
	}
}