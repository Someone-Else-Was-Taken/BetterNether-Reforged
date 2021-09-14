package someoneelse.betternetherreforged.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class BlockSoulSandstone extends BlockBase {
	public static final BooleanProperty UP = BooleanProperty.create("up");

	public BlockSoulSandstone() {
		super(AbstractBlock.Properties.from(Blocks.SANDSTONE));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		stateManager.add(UP);
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		return state.with(UP, world.getBlockState(pos.up()).getBlock() != this);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		return this.getDefaultState().with(UP, ctx.getWorld().getBlockState(ctx.getPos().up()).getBlock() != this);
	}
}