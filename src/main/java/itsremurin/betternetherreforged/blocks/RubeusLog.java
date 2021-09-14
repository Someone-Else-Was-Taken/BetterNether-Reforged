package itsremurin.betternetherreforged.blocks;

import itsremurin.betternetherreforged.blocks.shapes.TripleShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;

public class RubeusLog extends BNLogStripable {
	public static final EnumProperty<TripleShape> SHAPE = EnumProperty.create("shape", TripleShape.class);

	public RubeusLog(Block striped) {
		super(MaterialColor.MAGENTA, striped);
		this.setDefaultState(this.getDefaultState().with(AXIS, Direction.Axis.Y).with(SHAPE, TripleShape.BOTTOM));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		super.fillStateContainer(stateManager);
		stateManager.add(SHAPE);
	}
}