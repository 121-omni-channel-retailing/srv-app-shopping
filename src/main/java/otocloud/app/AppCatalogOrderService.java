package otocloud.app;

import java.util.ArrayList;
import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import otocloud.app.catalog.service.AppCatalogComponent;
import otocloud.app.catalog.service.AppOpsDeployInfoComponent;
import otocloud.app.common.MessageSchema;
import otocloud.app.order.service.AppOrderComponent;
import otocloud.framework.core.OtoCloudComponent;
import otocloud.framework.core.OtoCloudServiceForVerticleImpl;

public class AppCatalogOrderService extends OtoCloudServiceForVerticleImpl {
	private static final Logger logger = LoggerFactory.getLogger(AppCatalogOrderService.class);
	
	@Override
	public void start(Future<Void> startFuture) throws Exception{
		super.start(startFuture);
		//Json缺省不支持日期反序列化，需要手工注册。
//		Json.mapper.registerModule(new JavaTimeModule());
//		Json.prettyMapper.registerModule(new JavaTimeModule());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<OtoCloudComponent> createServiceComponents() {
		logger.debug("createServiceComponents-->start");
		List<OtoCloudComponent> retCloudComponents = new ArrayList<OtoCloudComponent>();
		retCloudComponents.add(new AppCatalogComponent());
		retCloudComponents.add(new AppOrderComponent());
		retCloudComponents.add(new AppOpsDeployInfoComponent());
		
		logger.debug("retCloudComponents count: " + retCloudComponents.size());
		return retCloudComponents;
	}  
    
	@Override
	public String getServiceName() {
		return MessageSchema.SRV_NAME_APP_SHOPPING;
	}

}
