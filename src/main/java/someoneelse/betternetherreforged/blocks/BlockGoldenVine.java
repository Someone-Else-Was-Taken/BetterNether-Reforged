package someoneelse.betternetherreforged.blocks;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.state.BooleanProperty;
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
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import someoneelse.betternetherreforged.BlocksHelper;

public class BlockGoldenVine extends BlockBaseNotFull implements IGrowable {
	private static final VoxelShape SHAPE = makeCuboidShape(2, 0, 2, 14, 16, 14);
	public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");

	public BlockGoldenVine() {
		super(AbstractBlock.Properties.create(Material.PLANTS, MaterialColor.RED)
				.sound(SoundType.CROP)
				.doesNotBlockMovement()
				.zeroHardnessAndResistance()
				.notSolid()
				.setLightLevel((state) -> {return 15;}));
		this.setRenderLayer(BNRenderLayer.CUTOUT);
		this.setDropItself(false);
		this.setDefaultState(getStateContainer().getBaseState().with(BOTTOM, true));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		stateManager.add(BOTTOM);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return SHAPE;
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
		if (isValidPosition(state, world, pos))
			return world.getBlockState(pos.down()).getBlock() == this ? state.with(BOTTOM, false) : state.with(BOTTOM, true);
		else
			return Blocks.AIR.getDefaultState();
	}

	@Override
	public boolean canGrow(IBlockReader world, BlockPos pos, BlockState state, boolean isClient) {
		Mutable blockPos = new Mutable().setPos(pos);
		for (int y = pos.getY() - 1; y > 1; y--) {
			blockPos.setY(y);
			if (world.getBlockState(blockPos).getBlock() != this)
				return world.getBlockState(blockPos).getBlock() == Blocks.AIR;
		}
		return false;
	}

	@Override
	public boolean canUseBonemeal(World world, Random random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		Mutable blockPos = new Mutable().setPos(pos);
		for (int y = pos.getY(); y > 1; y--) {
			blockPos.setY(y);
			if (world.getBlockState(blockPos).getBlock() != this)
				break;
		}
		BlocksHelper.setWithUpdate(world, blockPos.up(), getDefaultState().with(BOTTOM, false));
		BlocksHelper.setWithUpdate(world, blockPos, getDefaultState().with(BOTTOM, true));
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.get(LootParameters.TOOL);
		if (tool != null && tool.getItem().isIn(Tags.Items.SHEARS) || EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0) {
			return Lists.newArrayList(new ItemStack(this.asItem()));
		}
		else {
			return Lists.newArrayList();
		}
	}
}


