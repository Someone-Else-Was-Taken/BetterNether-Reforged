package someoneelse.betternetherreforged.blocks;

import net.minecraft.state.properties.BlockStateProperties;
import someoneelse.betternetherreforged.blocks.materials.MaterialBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import someoneelse.betternetherreforged.BlocksHelper;

public class BlockWhisperingGourdLantern extends Block {
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

	public BlockWhisperingGourdLantern() {
		super(MaterialBuilder.makeWood(MaterialColor.BLUE).setLightLevel((state) -> {return 15;}));
	}

	protected void appendProperties(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);

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
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlacementHorizontalFacing().getOpposite());
	}
}