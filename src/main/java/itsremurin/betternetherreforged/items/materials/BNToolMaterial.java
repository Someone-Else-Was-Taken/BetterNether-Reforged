package itsremurin.betternetherreforged.items.materials;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;

public class BNToolMaterial implements IItemTier {
	private final int durability;
	private final float speed;
	private final int level;
	private final int enchantibility;
	private final float damage;
	private final IItemProvider repair;

	public BNToolMaterial(int durability, float speed, int level, int enchantibility, float damage, IItemProvider repair) {
		this.durability = durability;
		this.speed = speed;
		this.level = level;
		this.enchantibility = enchantibility;
		this.damage = damage;
		this.repair = repair;
	}

	@Override
	public int getMaxUses() {
		return durability;
	}

	@Override
	public float getEfficiency() {
		return speed;
	}

	@Override
	public float getAttackDamage() {
		return damage;
	}

	@Override
	public int getHarvestLevel() {
		return level;
	}

	@Override
	public int getEnchantability() {
		return enchantibility;
	}

	@Override
	public Ingredient getRepairMaterial() {
		return Ingredient.fromItems(repair);
	}
}
