package someoneelse.betternetherreforged.blocks;

import java.util.List;
import java.util.Random;
import java.util.function.ToIntFunction;

import com.google.common.collect.Lists;

import someoneelse.betternetherreforged.blocks.shapes.TripleShape;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import someoneelse.betternetherreforged.MHelper;
import someoneelse.betternetherreforged.registry.ItemsRegistry;

public class BlockLumabusVine extends BlockBaseNotFull {
	private static final VoxelShape MIDDLE_SHAPE = makeCuboidShape(4, 0, 4, 12, 16, 12);
	private static final VoxelShape BOTTOM_SHAPE = makeCuboidShape(2, 4, 2, 14, 16, 14);
	public static final EnumProperty<TripleShape> SHAPE = EnumProperty.create("shape", TripleShape.class);
	private static final Random RANDOM = new Random();
	private final Block seed;

	public BlockLumabusVine(Block seed) {
		super(AbstractBlock.Properties.create(Material.PLANTS, MaterialColor.CYAN)
				.sound(SoundType.CROP)
				.doesNotBlockMovement()
				.zeroHardnessAndResistance()
				.notSolid()
				.setLightLevel(getLuminance()));
		this.seed = seed;
		this.setRenderLayer(BNRenderLayer.CUTOUT);
		this.setDropItself(false);
		this.setDefaultState(getStateContainer().getBaseState().with(SHAPE, TripleShape.TOP));
	}

	private static ToIntFunction<BlockState> getLuminance() {
		return (blockState) -> {
			return blockState.get(SHAPE) == TripleShape.BOTTOM ? 15 : 0;
		};
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return state.get(SHAPE) == TripleShape.BOTTOM ? BOTTOM_SHAPE : MIDDLE_SHAPE;
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		BlockState upState = world.getBlockState(pos.up());
		return upState.getBlock() == this || upState.isSolidSide(world, pos, Direction.DOWN);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader view, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader view, BlockPos pos) {
		return true;
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		return isValidPosition(state, world, pos) && (world.getBlockState(pos.down()).getBlock() == this || state.get(SHAPE) == TripleShape.BOTTOM) ? state : Blocks.AIR.getDefaultState();
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		if (state.get(SHAPE) == TripleShape.BOTTOM) {
			return Lists.newArrayList(new ItemStack(seed, MHelper.randRange(1, 3, RANDOM)), new ItemStack(ItemsRegistry.GLOWSTONE_PILE, MHelper.randRange(1, 3, RANDOM)));
		}
		return Lists.newArrayList();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem(IBlockReader world, BlockPos pos, BlockState state) {
		return new ItemStack(seed);
	}
}