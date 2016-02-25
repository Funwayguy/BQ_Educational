package bq_educational.core.proxies;

import bq_educational.core.BQ_Educational;
import bq_educational.network.PacketEdu;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy
{
	@Override
	public boolean isClient()
	{
		return true;
	}
	
	@Override
	public void registerHandlers()
	{
		super.registerHandlers();
		
    	BQ_Educational.instance.network.registerMessage(PacketEdu.HandlerClient.class, PacketEdu.class, 0, Side.CLIENT);
	}
	
	@Override
	public void registerThemes()
	{
	}
}
