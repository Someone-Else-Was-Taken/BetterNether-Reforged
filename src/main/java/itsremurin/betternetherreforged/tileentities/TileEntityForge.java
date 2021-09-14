package itsremurin.betternetherreforged.tileentities;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.FurnaceContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import itsremurin.betternetherreforged.registry.TileEntitiesRegistry;

public class TileEntityForge extends AbstractFurnaceTileEntity {
	public TileEntityForge() {
		super(TileEntitiesRegistry.CINCINNASITE_FORGE, IRecipeType.SMELTING);
	}

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("container.forge", new Object[0]);
	}

	@Override
	protected Container createMenu(int syncId, PlayerInventory playerInventory) {
		return new FurnaceContainer(syncId, playerInventory, this, this.furnaceData);
	}

	@Override
	protected int getBurnTime(ItemStack fuel) {
		return super.getBurnTime(fuel) / 2;
	}

	@Override
	protected int getCookTime() {
		return super.getCookTime() / 2;
	}
}
