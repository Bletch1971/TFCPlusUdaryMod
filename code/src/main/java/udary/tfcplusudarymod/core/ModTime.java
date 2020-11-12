package udary.tfcplusudarymod.core;

import com.dunk.tfc.Core.TFC_Time;

public class ModTime 
{
	/**
	 * 
	 * @return True if the current hour is during the day; otherwise false.
	 */
	public static Boolean isDayTime()
	{
		return isDayTime(TFC_Time.getHour());
	}
	
	/**
	 * 
	 * @param hour The hour to be check for day time.
	 * @return True if the specified hour is during the day; otherwise false.
	 */
	public static Boolean isDayTime(int hour)
	{
		return hour >= 6 && hour <= 18;
	}
	
	/**
	 * 
	 * @return True if the current hour is during the night; otherwise false.
	 */
	public static Boolean isNightTime()
	{
		return isNightTime(TFC_Time.getHour());
	}
	
	/**
	 * 
	 * @param hour The hour to be check for night time.
	 * @return True if the specified hour is during the night; otherwise false.
	 */
	public static Boolean isNightTime(int hour)
	{
		return hour < 6 && hour > 18;
	}
	
}
