package itsremurin.betternetherreforged.items;

import net.minecraft.block.BlockState;
import net.minecraft.item.HoeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import itsremurin.betternetherreforged.registry.ItemsRegistry;

public class BNItemHoe extends HoeItem {
	protected float speed;

	public BNItemHoe(IItemTier material, int durability, float speed) {
		super(material, 1, -2.8F, ItemsRegistry.defaultSettings().isImmuneToFire());
		this.speed = speed;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return super.getDestroySpeed(stack, state) * speed;
	}
}
