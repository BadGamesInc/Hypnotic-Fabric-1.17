package badgamesinc.hypnotic.module.combat;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.player.inventory.FindItemResult;
import badgamesinc.hypnotic.utils.player.inventory.InventoryUtils;
import net.minecraft.item.Items;

public class AutoTotem extends Mod {

	public BooleanSetting lock = new BooleanSetting("Lock", false);
	public BooleanSetting smart = new BooleanSetting("Smart", true);
	
	int totems;
	private boolean locked;
	
	public AutoTotem() {
		super("AutoTotem", "Automatically places totems in your offhand", Category.COMBAT);
		addSettings(lock, smart);
	}

	@Override
	public void onTick() {
		FindItemResult result = InventoryUtils.find(Items.TOTEM_OF_UNDYING);
        totems = result.getCount();
        this.setDisplayName("AutoTotem " + ColorUtils.gray + totems);
		if (mc.player.getInventory().contains(Items.TOTEM_OF_UNDYING.getDefaultStack())) {
			if (this.lock.isEnabled()) this.locked = true;
			if (mc.player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
				if (!smart.isEnabled()) {
					this.locked = true;
					InventoryUtils.move().from(result.getSlot()).to(InventoryUtils.OFFHAND);
				} else {
					if (mc.player.getHealth() < 15 || mc.player.isFallFlying() || mc.player.fallDistance > 6) {
						this.locked = true;
						InventoryUtils.move().from(result.getSlot()).to(InventoryUtils.OFFHAND);
					} else {
						this.locked = false;
					}
				}
			}
			if (!smart.isEnabled()) {
				this.locked = true;
			} else {
				if (mc.player.getHealth() < 10 || mc.player.isFallFlying() || mc.player.fallDistance > 6) {
					this.locked = true;
				} else {
					this.locked = false;
				}
			}
		}
		super.onTick();
	}
	
	public boolean isLocked() {
		return locked;
	}
}
