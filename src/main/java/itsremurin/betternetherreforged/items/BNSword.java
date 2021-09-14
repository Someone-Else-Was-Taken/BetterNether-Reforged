package itsremurin.betternetherreforged.items;

import net.minecraft.item.IItemTier;
import net.minecraft.item.SwordItem;
import itsremurin.betternetherreforged.registry.ItemsRegistry;

public class BNSword extends SwordItem {
	public BNSword(IItemTier material, int durability, int attackDamage, float attackSpeed) {
		super(material, attackDamage, attackSpeed, ItemsRegistry.defaultSettings().isImmuneToFire());
	}
}
