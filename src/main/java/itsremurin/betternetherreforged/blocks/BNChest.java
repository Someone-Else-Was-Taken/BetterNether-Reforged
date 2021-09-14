package itsremurin.betternetherreforged.blocks;

import java.util.List;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import itsremurin.betternetherreforged.registry.TileEntitiesRegistry;


public class BNChest extends ChestBlock {
	public BNChest(Block source) {
		super(AbstractBlock.Properties.from(source).notSolid(), () -> {
			return TileEntitiesRegistry.CHEST;
		});
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return TileEntitiesRegistry.CHEST.create();
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> drop = super.getDrops(state, builder);
		drop.add(new ItemStack(this.asItem()));
		return drop;
	}
}
