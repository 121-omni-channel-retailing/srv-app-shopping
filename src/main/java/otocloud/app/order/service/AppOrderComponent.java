package otocloud.app.order.service;

import java.util.ArrayList;
import java.util.List;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import otocloud.app.common.MessageSchema;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerRegistry;

public class AppOrderComponent extends OtoCloudComponentImpl {
	private static final Logger logger = LoggerFactory.getLogger(AppOrderComponent.class);

	@Override
	public String getName() {
		return MessageSchema.COMP_NAME_APP_ORDER;
	}

	@Override
	public List<OtoCloudEventHandlerRegistry> registerEventHandlers() {
		logger.debug("registerEventHandlers-->start");
		List<OtoCloudEventHandlerRegistry> ret = new ArrayList<OtoCloudEventHandlerRegistry>();
		ret.add(new AppOrderListHandler(this));
		ret.add(new AppOrderGetHandler(this));
		ret.add(new AppOrderPostHandler(this));
		ret.add(new AppOrderPutHandler(this));
		ret.add(new AppOrderDeleteHandler(this));
		
		logger.debug("Registered Handler count: " + ret.size());
		return ret;
	}

}
