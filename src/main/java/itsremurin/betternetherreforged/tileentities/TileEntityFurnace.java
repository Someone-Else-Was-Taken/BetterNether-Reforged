package itsremurin.betternetherreforged.tileentities;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.FurnaceContainer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import itsremurin.betternetherreforged.registry.TileEntitiesRegistry;

public class TileEntityFurnace extends AbstractFurnaceTileEntity {
	public TileEntityFurnace() {
		super(TileEntitiesRegistry.NETHERRACK_FURNACE, IRecipeType.SMELTING);
	}

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("container.furnace", new Object[0]);
	}

	@Override
	protected Container createMenu(int syncId, PlayerInventory playerInventory) {
		return new FurnaceContainer(syncId, playerInventory, this, this.furnaceData);
	}
}
