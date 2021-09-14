package someoneelse.betternetherreforged.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import someoneelse.betternetherreforged.registry.BlocksRegistry;

public class BlockCincinnasitPillar extends BlockBase {
	public static final EnumProperty<CincinnasitPillarShape> SHAPE = EnumProperty.create("shape", CincinnasitPillarShape.class);

	public BlockCincinnasitPillar() {
		super(AbstractBlock.Properties.from(BlocksRegistry.CINCINNASITE_BLOCK));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		boolean top = world.getBlockState(pos.up()).getBlock() == this;
		boolean bottom = world.getBlockState(pos.down()).getBlock() == this;
		if (top && bottom)
			return state.with(SHAPE, CincinnasitPillarShape.MIDDLE);
		else if (top)
			return state.with(SHAPE, CincinnasitPillarShape.BOTTOM);
		else if (bottom)
			return state.with(SHAPE, CincinnasitPillarShape.TOP);
		else
			return state.with(SHAPE, CincinnasitPillarShape.SMALL);
	}

	public static enum CincinnasitPillarShape implements IStringSerializable {
		SMALL("small"), TOP("top"), MIDDLE("middle"), BOTTOM("bottom");

		final String name;

		CincinnasitPillarShape(String name) {
			this.name = name;
		}

		@Override
		public String getString() {
			return name;
		}

		@Override
		public String toString() {
			return name;
		}
	}
}