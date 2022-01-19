package com.blakebr0.extendedcrafting.lib;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import net.minecraftforge.fml.common.ICrashCallable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks things used by addons (like PExC) that, if removed or changed, would cause {@link IncompatibleClassChangeError}s.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface AddonReferenced {

	class CrashCallable implements ICrashCallable {

		@Override
		public String getLabel() {
			return ExtendedCrafting.NAME;
		}

		@Override
		public String call() { // Forge doesn't give the crash report for some reason.
			return "You are using a fork of Extended Crafting created for the Nomifactory modpack.\n" +
					"If the error above is a NoSuchFieldError or a NoSuchMethodError relating to\n" +
					ExtendedCrafting.class.getPackage().getName() + ",\n" +
					"then please report to https://github.com/Nomifactory/ExtendedCrafting/issues\n" +
					"with this crash report.\n" +
					"Otherwise, you can ignore this message.";
		}
	}

}
