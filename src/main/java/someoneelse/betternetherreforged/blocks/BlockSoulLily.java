package someoneelse.betternetherreforged.blocks;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import someoneelse.betternetherreforged.blocks.materials.MaterialBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
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
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.MHelper;
import someoneelse.betternetherreforged.registry.BlocksRegistry;
import someoneelse.betternetherreforged.structures.plants.StructureSoulLily;

public class BlockSoulLily extends BlockBaseNotFull {
	public static final EnumProperty<SoulLilyShape> SHAPE = EnumProperty.create("shape", SoulLilyShape.class);

	private static final VoxelShape SHAPE_SMALL = Block.makeCuboidShape(6, 0, 6, 10, 16, 10);
	private static final VoxelShape SHAPE_MEDIUM_BOTTOM = Block.makeCuboidShape(5, 0, 5, 11, 16, 11);
	private static final VoxelShape SHAPE_MEDIUM_TOP = Block.makeCuboidShape(0, 0, 0, 16, 3, 16);
	private static final VoxelShape SHAPE_BIG_BOTTOM = Block.makeCuboidShape(3, 0, 3, 13, 16, 13);
	private static final VoxelShape SHAPE_BIG_MIDDLE = Block.makeCuboidShape(6, 0, 6, 10, 16, 10);
	private static final VoxelShape SHAPE_BIG_TOP_CENTER = Block.makeCuboidShape(0, 0, 0, 16, 4, 16);
	private static final VoxelShape SHAPE_BIG_TOP_SIDE_N = Block.makeCuboidShape(0, 4, 0, 16, 6, 8);
	private static final VoxelShape SHAPE_BIG_TOP_SIDE_S = Block.makeCuboidShape(0, 4, 8, 16, 6, 16);
	private static final VoxelShape SHAPE_BIG_TOP_SIDE_E = Block.makeCuboidShape(8, 4, 0, 16, 6, 16);
	private static final VoxelShape SHAPE_BIG_TOP_SIDE_W = Block.makeCuboidShape(0, 4, 0, 8, 6, 16);

	private static final StructureSoulLily STRUCTURE = new StructureSoulLily();

	private static final SoulLilyShape[] ROT = new SoulLilyShape[] {
			SoulLilyShape.BIG_TOP_SIDE_N,
			SoulLilyShape.BIG_TOP_SIDE_E,
			SoulLilyShape.BIG_TOP_SIDE_S,
			SoulLilyShape.BIG_TOP_SIDE_W
	};

	public BlockSoulLily() {
		super(MaterialBuilder.makeWood(MaterialColor.ADOBE).notSolid().tickRandomly());
		this.setDefaultState(getStateContainer().getBaseState().with(SHAPE, SoulLilyShape.SMALL));
		this.setRenderLayer(BNRenderLayer.CUTOUT);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}

	public VoxelShape getOutlineShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		switch (state.get(SHAPE)) {
			case BIG_BOTTOM:
				return SHAPE_BIG_BOTTOM;
			case BIG_MIDDLE:
				return SHAPE_BIG_MIDDLE;
			case BIG_TOP_CENTER:
				return SHAPE_BIG_TOP_CENTER;
			case MEDIUM_BOTTOM:
				return SHAPE_MEDIUM_BOTTOM;
			case MEDIUM_TOP:
				return SHAPE_MEDIUM_TOP;
			case BIG_TOP_SIDE_N:
				return SHAPE_BIG_TOP_SIDE_N;
			case BIG_TOP_SIDE_S:
				return SHAPE_BIG_TOP_SIDE_S;
			case BIG_TOP_SIDE_E:
				return SHAPE_BIG_TOP_SIDE_E;
			case BIG_TOP_SIDE_W:
				return SHAPE_BIG_TOP_SIDE_W;
			case SMALL:
			default:
				return SHAPE_SMALL;
		}
	}

	public enum SoulLilyShape implements IStringSerializable {
		SMALL("small"), MEDIUM_BOTTOM("medium_bottom"), MEDIUM_TOP("medium_top"), BIG_BOTTOM("big_bottom"), BIG_MIDDLE("big_middle"), BIG_TOP_CENTER("big_top_center"), BIG_TOP_SIDE_N("big_top_side_n"), BIG_TOP_SIDE_S(
				"big_top_side_s"), BIG_TOP_SIDE_E("big_top_side_e"), BIG_TOP_SIDE_W("big_top_side_w");

		final String name;

		SoulLilyShape(String name) {
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

	public boolean canGrow(World world, BlockPos pos, Random random) {
		BlockState state = world.getBlockState(pos.down());
		if (state.getBlock() == this || state.getBlock() == Blocks.SOUL_SAND || BlocksHelper.isFertile(world.getBlockState(pos.down()))) {
			return BlocksHelper.isFertile(world.getBlockState(pos.down())) ? (random.nextInt(8) == 0) : (random.nextInt(16) == 0);
		}
		return false;
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.tick(state, world, pos, random);
		if (canGrow(world, pos, random)) {
			SoulLilyShape shape = state.get(SHAPE);
			if (shape == SoulLilyShape.SMALL && world.isAirBlock(pos.up())) {
				STRUCTURE.growMedium(world, pos);
			}
			else if (shape == SoulLilyShape.MEDIUM_BOTTOM && world.isAirBlock(pos.up(2)) && isAirSides(world, pos.up(2))) {
				STRUCTURE.growBig(world, pos);
			}
		}
	}

	private boolean isAirSides(World world, BlockPos pos) {
		return world.isAirBlock(pos.north()) && world.isAirBlock(pos.south()) && world.isAirBlock(pos.east()) && world.isAirBlock(pos.west());
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		SoulLilyShape shape = state.get(SHAPE);
		int index = getRotationIndex(shape);
		if (index < 0)
			return state;
		int offset = rotOffset(rotation);
		return state.with(SHAPE, ROT[(index + offset) & 3]);
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		SoulLilyShape shape = state.get(SHAPE);
		int index = getRotationIndex(shape);
		if (index < 0)
			return state;
		if (mirror == Mirror.FRONT_BACK) {
			if (shape == SoulLilyShape.BIG_TOP_SIDE_E)
				shape = SoulLilyShape.BIG_TOP_SIDE_W;
			else if (shape == SoulLilyShape.BIG_TOP_SIDE_W)
				shape = SoulLilyShape.BIG_TOP_SIDE_E;
		}
		else if (mirror == Mirror.LEFT_RIGHT) {
			if (shape == SoulLilyShape.BIG_TOP_SIDE_N)
				shape = SoulLilyShape.BIG_TOP_SIDE_S;
			else if (shape == SoulLilyShape.BIG_TOP_SIDE_S)
				shape = SoulLilyShape.BIG_TOP_SIDE_N;
		}
		return state.with(SHAPE, shape);
	}

	private int getRotationIndex(SoulLilyShape shape) {
		for (int i = 0; i < 4; i++) {
			if (shape == ROT[i])
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
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem(IBlockReader world, BlockPos pos, BlockState state) {
		return new ItemStack(BlocksRegistry.SOUL_LILY_SAPLING);
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		SoulLilyShape shape = state.get(SHAPE);
		if (shape == SoulLilyShape.BIG_TOP_SIDE_N)
			return world.getBlockState(pos.north()).getBlock() == this;
		if (shape == SoulLilyShape.BIG_TOP_SIDE_S)
			return world.getBlockState(pos.south()).getBlock() == this;
		if (shape == SoulLilyShape.BIG_TOP_SIDE_E)
			return world.getBlockState(pos.east()).getBlock() == this;
		if (shape == SoulLilyShape.BIG_TOP_SIDE_W)
			return world.getBlockState(pos.west()).getBlock() == this;
		BlockState down = world.getBlockState(pos.down());
		return down.getBlock() == this || BlocksHelper.isSoulSand(down);
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		return isValidPosition(state, world, pos) ? state : Blocks.AIR.getDefaultState();
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		switch (state.get(SHAPE)) {
			case BIG_BOTTOM:
			case BIG_MIDDLE:
				return Lists.newArrayList(new ItemStack(BlocksRegistry.MUSHROOM_STEM));
			case BIG_TOP_CENTER:
				return Lists.newArrayList(new ItemStack(BlocksRegistry.MUSHROOM_STEM), new ItemStack(BlocksRegistry.SOUL_LILY_SAPLING));
			case MEDIUM_BOTTOM:
				return Lists.newArrayList(new ItemStack(BlocksRegistry.MUSHROOM_STEM));
			case BIG_TOP_SIDE_N:
			case BIG_TOP_SIDE_S:
			case BIG_TOP_SIDE_E:
			case BIG_TOP_SIDE_W:
				return Lists.newArrayList(new ItemStack(BlocksRegistry.MUSHROOM_STEM), new ItemStack(BlocksRegistry.SOUL_LILY_SAPLING, MHelper.randRange(0, 1, MHelper.RANDOM)));
			case SMALL:
			case MEDIUM_TOP:
			default:
				return Lists.newArrayList(new ItemStack(BlocksRegistry.MUSHROOM_STEM), new ItemStack(BlocksRegistry.SOUL_LILY_SAPLING));
		}
	}
}
