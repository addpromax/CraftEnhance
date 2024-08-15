package com.dutchjelly.craftenhance.messaging;

import com.dutchjelly.craftenhance.CraftEnhance;

import java.util.logging.Logger;

import static com.dutchjelly.craftenhance.messaging.Debug.Type.Crafting;
import static com.dutchjelly.craftenhance.messaging.Debug.Type.Smelting;

public class Debug {

	public static void init(final CraftEnhance main) {
		enable = main.getConfig().getBoolean("enable-debug");
		enable_crafting = main.getConfig().getBoolean("enable_crafting-debug");
		enable_smelting = main.getConfig().getBoolean("enable_smelting-debug");
		prefix = main.getConfig().getString("debug-prefix");
		logger = main.getLogger();
	}

	private static Logger logger;
	private static boolean enable; //could be a config thing
	private static boolean enable_crafting;
	private static boolean enable_smelting;
	private static String prefix;

	public static void Send(final Object obj) {
		Send(Type.Other, obj);
	}

	public static void Send(final Type type, final Object obj) {
		if (enable_crafting && type == Crafting) {
			System.out.println(prefix + " [crafting] " + (obj != null ? obj.toString() : "null"));
		}
		if (enable_smelting && type == Smelting) {
			System.out.println(prefix + " [furnace] " + (obj != null ? obj.toString() : "null"));
		}
		if (!enable) return;

		System.out.println(prefix + (obj != null ? obj.toString() : "null"));
	}

	public static void Send(final Object sender, final Object obj) {
		if (!enable) return;

		logger.info(prefix + "<" + sender.getClass().getName() + "> " + (obj != null ? obj.toString() : "null"));
	}

	public static void Send(final Object[] arr) {
		if (arr == null) return;
		logger.info(prefix + " ");
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == null) continue;
			logger.info(arr[i].toString());
		}
		logger.info("");
	}

	public enum Type {
		Crafting,
		Smelting,
		Other;
	}
}
