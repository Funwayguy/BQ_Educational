package bq_educational.handlers;

import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import bq_educational.core.BQE_Settings;
import bq_educational.core.BQ_Educational;

public class ConfigHandler
{
	public static Configuration config;
	
	public static void initConfigs()
	{
		if(config == null)
		{
			BQ_Educational.logger.log(Level.ERROR, "Config attempted to be loaded before it was initialised!");
			return;
		}
		
		config.load();
		
		BQE_Settings.hideUpdates = config.getBoolean("Hide Updates", Configuration.CATEGORY_GENERAL, false, "Hide update notifications");
		
		config.save();
		
		BQ_Educational.logger.log(Level.INFO, "Loaded configs...");
	}
}
