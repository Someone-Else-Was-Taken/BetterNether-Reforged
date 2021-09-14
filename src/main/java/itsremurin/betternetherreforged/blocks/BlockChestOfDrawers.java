package itsremurin.betternetherreforged.blocks;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import itsremurin.betternetherreforged.BlocksHelper;
import itsremurin.betternetherreforged.registry.BlocksRegistry;
import itsremurin.betternetherreforged.tileentities.TileEntityChestOfDrawers;


public class BlockChestOfDrawers extends ContainerBlock {
	private static final EnumMap<Direction, VoxelShape> BOUNDING_SHAPES = Maps.newEnumMap(ImmutableMap.of(
			Direction.NORTH, Block.makeCuboidShape(0, 0, 8, 16, 16, 16),
			Direction.SOUTH, Block.makeCuboidShape(0, 0, 0, 16, 16, 8),
			Direction.WEST, Block.makeCuboidShape(8, 0, 0, 16, 16, 16),
			Direction.EAST, Block.makeCuboidShape(0, 0, 0, 8, 16, 16)));
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final BooleanProperty OPEN = BooleanProperty.create("open");

	public BlockChestOfDrawers() {
		super(AbstractBlock.Properties.from(BlocksRegistry.CINCINNASITE_BLOCK).notSolid());
		this.setDefaultState(getStateContainer().getBaseState().with(FACING, Direction.NORTH).with(OPEN, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		stateManager.add(FACING, OPEN);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return BOUNDING_SHAPES.get(state.get(FACING));
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader view) {
		return new TileEntityChestOfDrawers();
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (itemStack.hasDisplayName()) {
			TileEntity blockEntity = world.getTileEntity(pos);
			if (blockEntity instanceof TileEntityChestOfDrawers) {
				((TileEntityChestOfDrawers) blockEntity).setCustomName(itemStack.getDisplayName());
			}
		}
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (world.isRemote) {
			return ActionResultType.SUCCESS;
		}
		else {
			TileEntity blockEntity = world.getTileEntity(pos);
			if (blockEntity instanceof TileEntityChestOfDrawers) {
				player.openContainer((TileEntityChestOfDrawers) blockEntity);
			}
			return ActionResultType.SUCCESS;
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> drop = new ArrayList<ItemStack>();
		TileEntityChestOfDrawers entity = (TileEntityChestOfDrawers) builder.get(LootParameters.BLOCK_ENTITY);
		drop.add(new ItemStack(this.asItem()));
		entity.addItemsToList(drop);
		return drop;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return BlocksHelper.rotateHorizontal(state, rotation, FACING);
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return BlocksHelper.mirrorHorizontal(state, mirror, FACING);
	}
}
