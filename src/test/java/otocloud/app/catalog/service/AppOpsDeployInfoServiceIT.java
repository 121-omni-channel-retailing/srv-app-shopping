package otocloud.app.catalog.service;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import otocloud.test.base.OtoCloudTestBaseIT;

public class AppOpsDeployInfoServiceIT extends OtoCloudTestBaseIT {
    static Logger logger = LoggerFactory.getLogger(AppOpsDeployInfoServiceIT.class.getName());
    
    private static final String APP_VERSION_IDS_WITH_RESULT = "1,2,3";
    private static final String APP_VERSION_IDS_WITH_NO_RESULT = "-1,-2,-3";
    
	@Test
	public void it_should_return_multiple_ops_deploy_info_with_valid_version_ids() {
		given()
			.param("ids", APP_VERSION_IDS_WITH_RESULT)
			.param("token", accessToken)
		.when()
			.get("/api/app-shopping/app-ops-deploy-infos")
		.then().assertThat()
			.statusCode(200)
			.body(not(isEmptyOrNullString()))
			.log().all(true);
	}

	@Test
	public void it_should_return_no_ops_deploy_info_with_invalid_version_ids() {
		given()
			.param("ids", APP_VERSION_IDS_WITH_NO_RESULT)
			.param("token", accessToken)
		.when()
			.get("/api/app-shopping/app-ops-deploy-infos")
		.then().assertThat()
			.statusCode(200)
			.body(equalTo("{}"))
			.log().all(true);

	}
}
