package otocloud.app;

import otocloud.test.base.OtoCloudTestITMain;

public class AppCatalogOrderServiceTestITMain extends OtoCloudTestITMain {

    public static void main(String[] args) {
    	new AppCatalogOrderServiceTestITMain().run();
    }

	@Override
	public String getServiceConfigFileName() {
		return "otocloud-app-shopping.json";
	}

	@Override
	public String getServiceClassName() {
		return AppCatalogOrderService.class.getName();
	}
	
}
