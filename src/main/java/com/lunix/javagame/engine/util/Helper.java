package com.lunix.javagame.engine.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunix.javagame.engine.Component;
import com.lunix.javagame.engine.struct.ComponentMenuItem;

public class Helper {
	private static final Logger logger = LogManager.getLogger(Helper.class);

	public static List<ComponentMenuItem> getAllComponentClasses() {
		String packageName = "com.lunix.javagame.engine.components";
		try (InputStream stream = ClassLoader.getSystemClassLoader()
				.getResourceAsStream(packageName.replaceAll("[.]", "/"))) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

			List<String> classes = reader.lines().filter(line -> line.endsWith(".class")).collect(Collectors.toList());
			List<ComponentMenuItem> menuItems = new ArrayList<>();
			for (String clazz : classes) {
				Class<?> claz = stringToClass(clazz, packageName);

				if (!isComponent(claz.getSuperclass()))
					continue;

				if (Modifier.isAbstract(claz.getModifiers()))
					continue;

				if (Modifier.isFinal(claz.getModifiers()))
					continue;

				Class<?> baseClass = getBaseClass(claz.getSuperclass());
				if (baseClass.equals(Component.class)) {
					ComponentMenuItem item = new ComponentMenuItem();
					item.name(claz.getSimpleName()).type(claz.asSubclass(Component.class));
					menuItems.add(item);
				} else {
					boolean added = false;
					for (ComponentMenuItem item : menuItems) {
						if(item.type().equals(baseClass)) {
							item.addChild(claz.asSubclass(Component.class));
							added = true;
							break;
						}
					}
					
					if(!added) {
						ComponentMenuItem item = new ComponentMenuItem();
						item.name(baseClass.getSimpleName())
							.type(baseClass.asSubclass(Component.class))
							.addChild(claz.asSubclass(Component.class));
						menuItems.add(item);
					}
				}
			}

			return menuItems;
		} catch (Exception e) {
			logger.error("Error while trying to get all Component classes", e);
			return Collections.emptyList();
		}
	}

	private static Class<?> getBaseClass(Class<?> clazz) {
		if (Modifier.isAbstract(clazz.getModifiers()))
			return clazz;

		if (clazz.getSuperclass() == null)
			return clazz;

		return getBaseClass(clazz.getSuperclass());
	}

	private static boolean isComponent(Class<?> clazz) {
		if(clazz == null)
			return false;
		
		if(clazz.equals(Component.class))
			return true;
		
		return isComponent(clazz.getSuperclass());
	}

	private static Class<?> stringToClass(String clazz, String pakage) throws ClassNotFoundException {
		return Class.forName(pakage + "." + clazz.substring(0, clazz.lastIndexOf('.')));
	}
}
