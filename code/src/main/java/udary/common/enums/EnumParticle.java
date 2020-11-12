package udary.common.enums;

public enum EnumParticle 
{
	EXPLODE("explode", "Explosions, mob death, mobs spawned by monster spawners, silverfish entering blocks"),
	LARGE_EXPLODE("largeexplode", "Explosions, ghast fireballs, wither skulls, Ender Dragon death"),
	HUGE_EXPLODE("hugeexplosion", "Explosions, Ender Dragon death, shearing mooshrooms"),
	FIREWORKS_SPARK("fireworksSpark", "Firework rocket trail and explosion (trail is not shown when the 'Minimal' particle setting is used)"),
	BUBBLE("bubble", "Entities in water, guardian laser beams, fishing"),
	SPLASH("splash", "Entities in water, wolves, boats"),
	WAKE("wake", "Fishing"),
	SUSPENDED("suspended", "Underwater"),
	DEPTH_SUSPEND("depthsuspend", ""),
	CRIT("crit", "Critical hits, bows"),
	MAGIC_CRIT("magicCrit", "Sword or axe enchanted with Sharpness, Smite, or Bane of Arthropods"),
	SMOKE("smoke", "Torches, primed TNT, droppers, dispensers, end portals, brewing stands, monster spawners, furnaces, ghast fireballs, wither skulls, taming, withers, lava (when raining), placing an eye of ender in an end portal frame [note 1], redstone torches burning out"),
	LARGE_SMOKE("largesmoke", "Fire, Minecart with furnace, blazes, water flowing into lava [note 1], lava flowing into water"),
	SPELL("spell", "Splash potions, bottles o' enchanting"),
	INSTANT_SPELL("instantSpell", "Instant health/damage splash potions"),
	MOB_SPELL("mobSpell", "Status effects, trading, wither armor"),
	MOB_SPELL_AMBIENT("mobSpellAmbient", "Beacon effects"),
	WITCH_MAGIC("witchMagic", "Witches"),
	DRIP_WATER("dripWater", "Water, wet sponges, leaves (when raining)"),
	DROP_LAVA("dripLava", "Lava"),
	ANGRY_VILLAGER("angryVillager", "Attacking a villager in a village"),
	HAPPY_VILLAGER("happyVillager", "Bone meal, trading with a villager, feeding a baby animal"),
	TOWN_AURA("townaura", "Mycelium"),
	NOTE("note", "Note blocks"),
	PORTAL("portal", "Nether portals, endermen, endermites, ender pearls, eyes of ender, ender chests, dragon eggs"),
	ENCHANTMENT_TABLE("enchantmenttable", "Enchantment tables near bookshelves"),
	FLAME("flame", "Torches, furnaces, Magma Cubes, monster spawners"),
	LAVA("lava", "Lava"),
	FOOTSTEP("footstep", ""),
	REDDUST("reddust", "Redstone ore, powered redstone, redstone torches, powered redstone repeaters"),
	SNOWBALL_POOF("snowballpoof", "Thrown snowballs, creating withers, creating iron golems"),
	SLIME("slime", "Slimes"),
	HEART("heart", "Breeding, taming"),
	BARRIER("barrier", "Barriers"),
	CLOUD("cloud", ""),
	SNOW_SHOVEL("snowshovel", "Creating snow golems"),
	DROPLET("droplet", "Rain"),
	TAKE("take", ""),
	MOB_APPEARANCE("mobappearance", "");
	
	protected final String name;	
	protected final String description;

	private EnumParticle(String name, String description)
	{
		this.name = name;
		this.description = description;
	}
	
	public String getName()
	{
		return name;
	}	
	
	public String getDescription()
	{
		return description;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}
