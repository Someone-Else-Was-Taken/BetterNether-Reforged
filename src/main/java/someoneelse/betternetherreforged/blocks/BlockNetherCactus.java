package someoneelse.betternetherreforged.blocks;

import java.util.Random;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import someoneelse.betternetherreforged.BlocksHelper;

public class BlockNetherCactus extends BlockBaseNotFull {
	private static final VoxelShape TOP_SHAPE = makeCuboidShape(4, 0, 4, 12, 8, 12);
	private static final VoxelShape SIDE_SHAPE = makeCuboidShape(5, 0, 5, 11, 16, 11);
	public static final BooleanProperty TOP = BooleanProperty.create("top");

	public BlockNetherCactus() {
		super(AbstractBlock.Properties.create(Material.CACTUS, MaterialColor.ORANGE_TERRACOTTA)
				.sound(SoundType.CLOTH)
				.notSolid()
				.tickRandomly());
		this.setRenderLayer(BNRenderLayer.CUTOUT);
		this.setDefaultState(getStateContainer().getBaseState().with(TOP, true));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		stateManager.add(TOP);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return state.get(TOP).booleanValue() ? TOP_SHAPE : SIDE_SHAPE;
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		if (isValidPosition(state, world, pos)) {
			Block up = world.getBlockState(pos.up()).getBlock();
			if (up == this)
				return state.with(TOP, false);
			else
				return this.getDefaultState();
		}
		else
			return Blocks.AIR.getDefaultState();
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		Block down = world.getBlockState(pos.down()).getBlock();
		return down == Blocks.GRAVEL || down == this;
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!isValidPosition(state, world, pos)) {
			world.destroyBlock(pos, true);
			return;
		}
		if (state.get(TOP).booleanValue() && random.nextInt(16) == 0) {
			BlockPos up = pos.up();
			boolean grow = world.getBlockState(up).getBlock() == Blocks.AIR;
			grow = grow && (BlocksHelper.getLengthDown(world, pos, this) < 3);
			if (grow) {
				BlocksHelper.setWithUpdate(world, up, getDefaultState());
				BlocksHelper.setWithUpdate(world, pos, getDefaultState().with(TOP, false));
			}
		}
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		entity.attackEntityFrom(DamageSource.CACTUS, 1.0F);
	}
}
