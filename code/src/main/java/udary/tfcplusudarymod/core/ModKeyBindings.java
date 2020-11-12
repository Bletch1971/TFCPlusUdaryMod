package udary.tfcplusudarymod.core;

import java.util.ArrayList;

import net.minecraft.client.settings.KeyBinding;

public class ModKeyBindings
{
	public static ArrayList<KeyBinding> keyBindingsList;
	public static ArrayList<Boolean> isRepeatingList;

	public static void addKeyBinding(String name, Integer value, String category, Boolean isRepeating)
	{
		if (keyBindingsList == null)
			keyBindingsList = new ArrayList<KeyBinding>();
		keyBindingsList.add(new KeyBinding(name, value, category));

		if (isRepeatingList == null)
			isRepeatingList = new ArrayList<Boolean>();
		isRepeatingList.add(isRepeating);
	}

	public static void addKeyBinding(KeyBinding keyBinding, Boolean isRepeating)
	{
		if (keyBindingsList == null)
			keyBindingsList = new ArrayList<KeyBinding>();
		keyBindingsList.add(keyBinding);

		if (isRepeatingList == null)
			isRepeatingList = new ArrayList<Boolean>();
		isRepeatingList.add(isRepeating);
	}

	public static KeyBinding[] gatherKeyBindings()
	{
		if (keyBindingsList == null)
			return new KeyBinding[0];
		return keyBindingsList.toArray(new KeyBinding[keyBindingsList.size()]);
	}

	public static Boolean[] gatherIsRepeating()
	{
		if (isRepeatingList == null)
			return new Boolean[0];
		
		Boolean[] isRepeating = new Boolean[isRepeatingList.size()];
		for (int index = 0; index < isRepeating.length; index++)
			isRepeating[index] = isRepeatingList.get(index).booleanValue();
		return isRepeating;
	}
}
