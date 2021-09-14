package someoneelse.betternetherreforged.blocks;

import java.util.Random;

import someoneelse.betternetherreforged.blocks.materials.MaterialBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import someoneelse.betternetherreforged.registry.BlocksRegistry;

public class BlockRubeusLeaves extends BlockBase {
	private static final int MAX_DIST = 10;
	public static final IntegerProperty DISTANCE_CUSTOM = IntegerProperty.create("dist_custom", 1, MAX_DIST);
	public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;

	public BlockRubeusLeaves() {
		super(MaterialBuilder.makeLeaves(MaterialColor.LIGHT_BLUE));
		this.setDefaultState(this.stateContainer.getBaseState().with(DISTANCE_CUSTOM, 1).with(PERSISTENT, false));
		this.setRenderLayer(BNRenderLayer.CUTOUT);
		this.setDropItself(false);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(PERSISTENT, DISTANCE_CUSTOM);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		return updateDistanceFromLogs((BlockState) this.getDefaultState().with(PERSISTENT, true), ctx.getWorld(), ctx.getPos());
	}

	@Override
	public boolean ticksRandomly(BlockState state) {
		return state.get(DISTANCE_CUSTOM) == MAX_DIST && !(Boolean) state.get(PERSISTENT);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!state.get(PERSISTENT) && state.get(DISTANCE_CUSTOM) == MAX_DIST) {
			spawnDrops(state, world, pos);
			world.removeBlock(pos, false);
		}
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		world.setBlockState(pos, updateDistanceFromLogs(state, world, pos), 3);
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction direction, BlockState newState, IWorld world, BlockPos pos, BlockPos posFrom) {
		int dist = getDistanceFromLog(newState) + 1;
		if (dist != 1 || state.get(DISTANCE_CUSTOM) != dist) {
			world.getPendingBlockTicks().scheduleTick(pos, this, 1);
		}

		return state;
	}

	private static BlockState updateDistanceFromLogs(BlockState state, IWorld world, BlockPos pos) {
		int dist = MAX_DIST;
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		Direction[] dirs = Direction.values();
		int count = dirs.length;

		for (int n = 0; n < count; ++n) {
			Direction dir = dirs[n];
			mutable.setAndMove(pos, dir);
			dist = Math.min(dist, getDistanceFromLog(world.getBlockState(mutable)) + 1);
			if (dist == 1) {
				break;
			}
		}

		return (BlockState) state.with(DISTANCE_CUSTOM, dist);
	}

	private static int getDistanceFromLog(BlockState state) {
		if (state.getBlock() == BlocksRegistry.RUBEUS_LOG || state.getBlock() == BlocksRegistry.RUBEUS_BARK) {
			return 0;
		}
		else {
			return state.getBlock() instanceof BlockRubeusLeaves ? state.get(DISTANCE_CUSTOM) : MAX_DIST;
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (world.isRainingAt(pos.up())) {
			if (random.nextInt(15) == 1) {
				BlockPos blockPos = pos.down();
				BlockState blockState = world.getBlockState(blockPos);
				if (!blockState.isSolid() || !blockState.isSolidSide(world, blockPos, Direction.UP)) {
					double d = (double) ((float) pos.getX() + random.nextFloat());
					double e = (double) pos.getY() - 0.05D;
					double f = (double) ((float) pos.getZ() + random.nextFloat());
					world.addParticle(ParticleTypes.DRIPPING_WATER, d, e, f, 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}
}
