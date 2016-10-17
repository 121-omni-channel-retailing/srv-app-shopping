package otocloud.app.catalog.dao;

import java.util.List;
import java.util.Map;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import otocloud.app.catalog.model.AppActivity;
import otocloud.app.catalog.model.AppFeature;
import otocloud.app.catalog.model.AppModule;
import otocloud.app.catalog.model.AppModuleInfoExtra;
import otocloud.app.catalog.model.AppPrice;

public interface AppCatalogDAO {
	public void getAppModules(List<Integer> appModulesIds, Handler<AsyncResult<List<AppModule>>> done);
	public void getAppFeatures(List<Integer> appModulesIds, Handler<AsyncResult<Map<Integer, List<AppFeature>>>> done);
	public void getAppPrices(List<Integer> appModulesIds, Handler<AsyncResult<Map<String, List<AppPrice>>>> done);
	public void getAppModuleInfoExtras(List<Integer> appModulesIds, Handler<AsyncResult<Map<Integer, List<AppModuleInfoExtra>>>> done);
	public void getAppActivities(List<Integer> appModulesIds, Handler<AsyncResult<Map<Integer, List<AppActivity>>>> done);
}
