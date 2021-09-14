package someoneelse.betternetherreforged.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class BlockStalactite extends BlockBaseNotFull {
	public static final IntegerProperty SIZE = IntegerProperty.create("size", 0, 7);
	private static final Mutable POS = new Mutable();
	private static final VoxelShape[] SHAPES;

	public BlockStalactite(Block source) {
		super(AbstractBlock.Properties.from(source).notSolid());
		this.setDefaultState(getStateContainer().getBaseState().with(SIZE, 0));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		stateManager.add(SIZE);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return SHAPES[state.get(SIZE)];
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (world.getBlockState(pos.down()).getBlock() instanceof BlockStalactite) {
			POS.setX(pos.getX());
			POS.setZ(pos.getZ());
			for (int i = 1; i < 8; i++) {
				POS.setY(pos.getY() - i);
				if (world.getBlockState(POS).getBlock() instanceof BlockStalactite) {
					BlockState state2 = world.getBlockState(POS);
					int size = state2.get(SIZE);
					if (size < i) {
						world.setBlockState(POS, state2.with(SIZE, i));
					}
					else
						break;
				}
				else
					break;
			}
		}
		if (world.getBlockState(pos.up()).getBlock() instanceof BlockStalactite) {
			POS.setX(pos.getX());
			POS.setZ(pos.getZ());
			for (int i = 1; i < 8; i++) {
				POS.setY(pos.getY() + i);
				if (world.getBlockState(POS).getBlock() instanceof BlockStalactite) {
					BlockState state2 = world.getBlockState(POS);
					int size = state2.get(SIZE);
					if (size < i) {
						world.setBlockState(POS, state2.with(SIZE, i));
					}
					else
						break;
				}
				else
					break;
			}
		}
	}

	@Override
	public void onPlayerDestroy(IWorld world, BlockPos pos, BlockState state) {
		BlockPos pos2 = pos.up();
		BlockState state2 = world.getBlockState(pos2);
		if (state2.getBlock() instanceof BlockStalactite && state2.get(SIZE) < state.get(SIZE)) {
			state2.getBlock().onPlayerDestroy(world, pos2, state2);
			world.destroyBlock(pos2, true);
		}

		pos2 = pos.down();
		state2 = world.getBlockState(pos2);
		if (state2.getBlock() instanceof BlockStalactite && state2.get(SIZE) < state.get(SIZE)) {
			state2.getBlock().onPlayerDestroy(world, pos2, state2);
			world.destroyBlock(pos2, true);
		}
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		return canPlace(world, pos, Direction.UP) || canPlace(world, pos, Direction.DOWN);
	}

	private boolean canPlace(IWorldReader world, BlockPos pos, Direction dir) {
		return world.getBlockState(pos.offset(dir)).getBlock() instanceof BlockStalactite || hasEnoughSolidSide(world, pos.offset(dir), dir.getOpposite());
	}

	static {
		SHAPES = new VoxelShape[8];
		for (int i = 0; i < 8; i++)
			SHAPES[i] = makeCuboidShape(7 - i, 0, 7 - i, 9 + i, 16, 9 + i);
	}
}
