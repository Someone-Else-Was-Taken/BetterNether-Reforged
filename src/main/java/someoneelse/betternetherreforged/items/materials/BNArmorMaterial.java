package someoneelse.betternetherreforged.items.materials;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.SoundEvent;

public class BNArmorMaterial implements IArmorMaterial {
	private static final int[] DURABILITY = new int[] { 3, 6, 8, 3 };
	private final String name;
	private final int multiplier;
	private final int enchantLevel;
	private final SoundEvent sound;
	private final IItemProvider repair;
	private final float toughness;
	private final int[] protection;

	public BNArmorMaterial(String name, int durabilityMultiplier, int enchantLevel, SoundEvent equipSound, IItemProvider repairItem, float toughness, int[] protection) {
		this.name = name;
		this.multiplier = durabilityMultiplier;
		this.enchantLevel = enchantLevel;
		this.sound = equipSound;
		this.repair = repairItem;
		this.toughness = toughness;
		this.protection = protection;
	}

	@Override
	public int getDurability(EquipmentSlotType slot) {
		return DURABILITY[slot.getIndex()] * multiplier;
	}

	@Override
	public int getDamageReductionAmount(EquipmentSlotType slot) {
		return protection[slot.getIndex()];
	}

	@Override
	public int getEnchantability() {
		return enchantLevel;
	}

	@Override
	public SoundEvent getSoundEvent() {
		return sound;
	}

	@Override
	public Ingredient getRepairMaterial() {
		return Ingredient.fromItems(repair);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public float getToughness() {
		return toughness;
	}

	@Override
	public float getKnockbackResistance() {
		return 0;
	}
}
