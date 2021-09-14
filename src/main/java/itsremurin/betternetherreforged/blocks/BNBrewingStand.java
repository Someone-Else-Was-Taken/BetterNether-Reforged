package itsremurin.betternetherreforged.blocks;

import itsremurin.betternetherreforged.client.IRenderTypeable;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BrewingStandBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
/*
public class BNBrewingStand extends BrewingStandBlock implements IRenderTypeable {
	public BNBrewingStand() {
		super(AbstractBlock.Properties.from(Blocks.NETHER_BRICKS)
				.hardnessAndResistance(0.5F, 0.5F)
				.setLightLevel((state) -> {return 1;})
				.notSolid());
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader view) {
		return new BNBrewingStandTileEntity();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (world.isRemote) {
			return ActionResultType.SUCCESS;
		}
		else {
			TileEntity blockEntity = world.getTileEntity(pos);
			if (blockEntity instanceof BNBrewingStandTileEntity) {
				player.openContainer((BNBrewingStandTileEntity) blockEntity);
				player.addStat(Stats.INTERACT_WITH_BREWINGSTAND);
			}

			return ActionResultType.SUCCESS;
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (itemStack.hasDisplayName()) {
			TileEntity blockEntity = world.getTileEntity(pos);
			if (blockEntity instanceof BNBrewingStandTileEntity) {
				((BNBrewingStandTileEntity) blockEntity).setCustomName(itemStack.getDisplayName());
			}
		}
	}

	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean notify) {
		if (!state.isIn(newState.getBlock())) {
			TileEntity blockEntity = world.getTileEntity(pos);
			if (blockEntity instanceof BNBrewingStandTileEntity) {
				InventoryHelper.dropInventoryItems(world, (BlockPos) pos, (IInventory) ((BNBrewingStandTileEntity) blockEntity));
			}

			super.onReplaced(state, world, pos, newState, notify);
		}
	}

	@Override
	public BNRenderLayer getRenderLayer() {
		return BNRenderLayer.CUTOUT;
	}
}
*/