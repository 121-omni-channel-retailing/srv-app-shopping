package otocloud.app.catalog.service;

import java.util.ArrayList;
import java.util.List;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import otocloud.app.common.MessageSchema;
import otocloud.framework.core.OtoCloudComponentImpl;
import otocloud.framework.core.OtoCloudEventHandlerRegistry;

public class AppCatalogComponent extends OtoCloudComponentImpl {
	private static final Logger logger = LoggerFactory.getLogger(AppCatalogComponent.class);

	@Override
	public String getName() {
		return MessageSchema.COMP_NAME_APP_CATALOG;
	}

	@Override
	public List<OtoCloudEventHandlerRegistry> registerEventHandlers() {
		logger.debug("registerEventHandlers-->start");
		List<OtoCloudEventHandlerRegistry> ret = new ArrayList<OtoCloudEventHandlerRegistry>();

		AppCatalogListHandler appCatalogListHandler = new AppCatalogListHandler(this);
		ret.add(appCatalogListHandler);
		
		AppCatalogGetHandler appCatalogGetHandler = new AppCatalogGetHandler(this);
		ret.add(appCatalogGetHandler);
		
		logger.debug("Registered Handler count: " + ret.size());
		return ret;
	}

}
