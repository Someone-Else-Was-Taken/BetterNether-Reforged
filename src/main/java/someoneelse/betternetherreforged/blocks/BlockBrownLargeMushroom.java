package someoneelse.betternetherreforged.blocks;

import someoneelse.betternetherreforged.blocks.materials.MaterialBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import someoneelse.betternetherreforged.registry.BlocksRegistry;

public class BlockBrownLargeMushroom extends BlockBaseNotFull {
	private static final VoxelShape TOP_CENTER_SHAPE = makeCuboidShape(0, 0.1, 0, 16, 16, 16);
	private static final VoxelShape TOP_EDGE_SHAPE = makeCuboidShape(0, 8, 0, 16, 16, 16);
	private static final VoxelShape MIDDLE_SHAPE = makeCuboidShape(4, 0, 4, 12, 16, 12);
	public static final EnumProperty<BrownMushroomShape> SHAPE = EnumProperty.create("shape", BrownMushroomShape.class);

	private static final BrownMushroomShape[] ROT_SIDE = new BrownMushroomShape[] {
			BrownMushroomShape.SIDE_N,
			BrownMushroomShape.SIDE_E,
			BrownMushroomShape.SIDE_S,
			BrownMushroomShape.SIDE_W
	};

	private static final BrownMushroomShape[] ROT_CORNER = new BrownMushroomShape[] {
			BrownMushroomShape.CORNER_N,
			BrownMushroomShape.CORNER_E,
			BrownMushroomShape.CORNER_S,
			BrownMushroomShape.CORNER_W
	};

	public BlockBrownLargeMushroom() {
		super(MaterialBuilder.makeWood(MaterialColor.BROWN).notSolid());
		this.setDropItself(false);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem(IBlockReader world, BlockPos pos, BlockState state) {
		BrownMushroomShape shape = state.get(SHAPE);
		return shape == BrownMushroomShape.BOTTOM || shape == BrownMushroomShape.MIDDLE ? new ItemStack(BlocksRegistry.MUSHROOM_STEM) : new ItemStack(Items.BROWN_MUSHROOM);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		BrownMushroomShape shape = state.get(SHAPE);
		if (shape == BrownMushroomShape.TOP)
			return TOP_CENTER_SHAPE;
		else if (shape == BrownMushroomShape.MIDDLE || shape == BrownMushroomShape.BOTTOM)
			return MIDDLE_SHAPE;
		else
			return TOP_EDGE_SHAPE;
	}

	public static enum BrownMushroomShape implements IStringSerializable {
		TOP("top"), SIDE_N("side_n"), SIDE_S("side_s"), SIDE_E("side_e"), SIDE_W("side_w"), CORNER_N("corner_n"), CORNER_S("corner_s"), CORNER_E("corner_e"), CORNER_W("corner_w"), MIDDLE("middle"), BOTTOM("bottom");

		final String name;

		BrownMushroomShape(String name) {
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

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		BrownMushroomShape shape = state.get(SHAPE);

		int index = getRotationIndex(shape, ROT_SIDE);
		if (index < 0) {
			index = getRotationIndex(shape, ROT_CORNER);

			if (index < 0) {
				return state;
			}

			int offset = rotOffset(rotation);
			return state.with(SHAPE, ROT_CORNER[(index + offset) & 3]);
		}

		int offset = rotOffset(rotation);
		return state.with(SHAPE, ROT_SIDE[(index + offset) & 3]);
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		BrownMushroomShape shape = state.get(SHAPE);
		int index = getRotationIndex(shape, ROT_SIDE);
		if (index < 0) {
			index = getRotationIndex(shape, ROT_CORNER);
			if (index < 0)
				return state;
			if (mirror == Mirror.FRONT_BACK) {
				if (shape == BrownMushroomShape.CORNER_E)
					shape = BrownMushroomShape.CORNER_W;
				else if (shape == BrownMushroomShape.CORNER_W)
					shape = BrownMushroomShape.CORNER_E;
			}
			else if (mirror == Mirror.LEFT_RIGHT) {
				if (shape == BrownMushroomShape.CORNER_N)
					shape = BrownMushroomShape.CORNER_S;
				else if (shape == BrownMushroomShape.CORNER_S)
					shape = BrownMushroomShape.CORNER_N;
			}
			return state.with(SHAPE, shape);
		}
		if (mirror == Mirror.FRONT_BACK) {
			if (shape == BrownMushroomShape.SIDE_E)
				shape = BrownMushroomShape.SIDE_W;
			else if (shape == BrownMushroomShape.SIDE_W)
				shape = BrownMushroomShape.SIDE_E;
		}
		else if (mirror == Mirror.LEFT_RIGHT) {
			if (shape == BrownMushroomShape.SIDE_N)
				shape = BrownMushroomShape.SIDE_S;
			else if (shape == BrownMushroomShape.SIDE_S)
				shape = BrownMushroomShape.SIDE_N;
		}
		return state.with(SHAPE, shape);
	}

	private int getRotationIndex(BrownMushroomShape shape, BrownMushroomShape[] rotations) {
		for (int i = 0; i < 4; i++) {
			if (shape == rotations[i])
				return i;
		}
		return -1;
	}

	private int rotOffset(Rotation rotation) {
		if (rotation == Rotation.NONE)
			return 0;
		else if (rotation == Rotation.CLOCKWISE_90)
			return 1;
		else if (rotation == Rotation.CLOCKWISE_180)
			return 2;
		else
			return 3;
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		switch (state.get(SHAPE)) {
			case BOTTOM:
				return state;
			case MIDDLE:
			case TOP:
			default:
				return getStateIfSame(state, world, pos.down());
			case SIDE_E:
			case CORNER_E:
				return getStateIfSame(state, world, pos.west());
			case SIDE_N:
			case CORNER_N:
				return getStateIfSame(state, world, pos.south());
			case SIDE_S:
			case CORNER_S:
				return getStateIfSame(state, world, pos.north());
			case SIDE_W:
			case CORNER_W:
				return getStateIfSame(state, world, pos.east());
		}
	}

	private BlockState getStateIfSame(BlockState state, IWorld world, BlockPos pos) {
		return world.getBlockState(pos).getBlock() == this ? state : Blocks.AIR.getDefaultState();
	}
}
