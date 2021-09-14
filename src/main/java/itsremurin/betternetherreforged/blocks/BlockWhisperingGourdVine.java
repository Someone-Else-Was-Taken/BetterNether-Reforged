package itsremurin.betternetherreforged.blocks;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import itsremurin.betternetherreforged.blocks.shapes.TripleShape;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
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
import itsremurin.betternetherreforged.BlocksHelper;
import itsremurin.betternetherreforged.MHelper;
import itsremurin.betternetherreforged.registry.BlocksRegistry;

public class BlockWhisperingGourdVine extends BlockBaseNotFull implements IGrowable {
	private static final VoxelShape SELECTION = makeCuboidShape(2, 0, 2, 14, 16, 14);
	public static final EnumProperty<TripleShape> SHAPE = EnumProperty.create("shape", TripleShape.class);

	public BlockWhisperingGourdVine() {
		super(AbstractBlock.Properties.create(Material.PLANTS, MaterialColor.RED)
				.sound(SoundType.CROP)
				.doesNotBlockMovement()
				.zeroHardnessAndResistance()
				.notSolid()
				.tickRandomly());
		this.setRenderLayer(BNRenderLayer.CUTOUT);
		this.setDropItself(false);
		this.setDefaultState(getStateContainer().getBaseState().with(SHAPE, TripleShape.BOTTOM));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return SELECTION;
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		BlockState upState = world.getBlockState(pos.up());
		return upState.getBlock() == this || upState.isSolidSide(world, pos, Direction.DOWN);
	}

	@OnlyIn(Dist.CLIENT)
	public float getAmbientOcclusionLightLevel(BlockState state, IBlockReader view, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader view, BlockPos pos) {
		return true;
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		if (!isValidPosition(state, world, pos))
			return Blocks.AIR.getDefaultState();
		else if (world.getBlockState(pos.down()).getBlock() != this)
			return state.with(SHAPE, TripleShape.BOTTOM);
		else if (state.get(SHAPE) == TripleShape.BOTTOM)
			return state.with(SHAPE, TripleShape.TOP);
		else
			return state;
	}

	@Override
	public boolean canGrow(IBlockReader world, BlockPos pos, BlockState state, boolean isRemote) {
		return world.getBlockState(pos.up(3)).getBlock() != this && world.getBlockState(pos.down()).getBlock() != this;
	}

	@Override
	public boolean canUseBonemeal(World world, Random random, BlockPos pos, BlockState state) {
		return world.getBlockState(pos.up(3)).getBlock() != this && world.getBlockState(pos.down()).getMaterial().isReplaceable();
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		BlocksHelper.setWithUpdate(world, pos, state.with(SHAPE, TripleShape.TOP));
		BlocksHelper.setWithUpdate(world, pos.down(), getDefaultState());
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.get(LootParameters.TOOL);
		if (tool != null && tool.getItem().isIn(Tags.Items.SHEARS))
			return Lists.newArrayList(new ItemStack(this.asItem()));
		else if (state.get(SHAPE) == TripleShape.BOTTOM || MHelper.RANDOM.nextBoolean())
			return Lists.newArrayList(new ItemStack(this.asItem()));
		else
			return Lists.newArrayList();
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.tick(state, world, pos, random);
		if (canUseBonemeal(world, random, pos, state)) {
			grow(world, random, pos, state);
		}
		if (state.get(SHAPE) != TripleShape.MIDDLE && state.get(SHAPE) != TripleShape.BOTTOM && random.nextInt(16) == 0) {
			BlocksHelper.setWithUpdate(world, pos, state.with(SHAPE, TripleShape.MIDDLE));
		}
	}

	public ActionResultType onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		ItemStack tool = player.getHeldItem(hand);
		if (tool.getItem().isIn(Tags.Items.SHEARS) && state.get(SHAPE) == TripleShape.MIDDLE) {
			if (!world.isRemote) {
				BlocksHelper.setWithUpdate(world, pos, state.with(SHAPE, TripleShape.BOTTOM));
				world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(BlocksRegistry.WHISPERING_GOURD)));
				if (world.rand.nextBoolean()) {
					world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(BlocksRegistry.WHISPERING_GOURD)));
				}
			}
			return ActionResultType.func_233537_a_(world.isRemote);
		}
		else {
			return super.onBlockActivated(state, world, pos, player, hand, hit);
		}
	}
}

