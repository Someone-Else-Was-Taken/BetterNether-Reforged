package someoneelse.betternetherreforged.blocks;

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