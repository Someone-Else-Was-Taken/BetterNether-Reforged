package someoneelse.betternetherreforged.items;

import net.minecraft.block.BlockState;
import net.minecraft.item.AxeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import someoneelse.betternetherreforged.registry.ItemsRegistry;

public class BNItemAxe extends AxeItem {
	protected float speed;

	public BNItemAxe(IItemTier material, int durability, float speed) {
		super(material, 1, -2.8F, ItemsRegistry.defaultSettings().isImmuneToFire());
		this.speed = speed;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		return super.getDestroySpeed(stack, state) * speed;
	}
}