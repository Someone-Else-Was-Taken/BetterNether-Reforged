package itsremurin.betternetherreforged.items;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import itsremurin.betternetherreforged.registry.ItemsRegistry;

public class BNArmor extends ArmorItem {
	public BNArmor(IArmorMaterial material, EquipmentSlotType slot) {
		super(material, slot, ItemsRegistry.defaultSettings());
	}
}
