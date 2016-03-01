package bq_educational.core;

import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;
import betterquesting.quests.tasks.TaskRegistry;
import bq_educational.core.proxies.CommonProxy;
import bq_educational.handlers.ConfigHandler;
import bq_educational.tasks.EduTaskWorksheet;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = BQ_Educational.MODID, version = BQ_Educational.VERSION, name = BQ_Educational.NAME, guiFactory = "bq_educational.handlers.ConfigGuiFactory")
public class BQ_Educational
{
    public static final String MODID = "bq_educational";
    public static final String VERSION = "BQ_EDU_VER";
    public static final String NAME = "Educational Expansion";
    public static final String PROXY = "bq_educational.core.proxies";
    public static final String CHANNEL = "BQ_EDU_CHAN";
	
	@Instance(MODID)
	public static BQ_Educational instance;
	
	@SidedProxy(clientSide = PROXY + ".ClientProxy", serverSide = PROXY + ".CommonProxy")
	public static CommonProxy proxy;
	public SimpleNetworkWrapper network;
	public static Logger logger;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	logger = event.getModLog();
    	network = NetworkRegistry.INSTANCE.newSimpleChannel(CHANNEL);
    	
    	ConfigHandler.config = new Configuration(event.getSuggestedConfigurationFile(), true);
    	ConfigHandler.initConfigs();
    	
    	proxy.registerHandlers();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.registerThemes();
    	
    	TaskRegistry.RegisterTask(EduTaskWorksheet.class, "worksheet");
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    }
}
