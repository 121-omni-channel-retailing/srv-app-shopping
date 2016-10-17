package otocloud.app.catalog.service;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import otocloud.test.base.OtoCloudTestBaseIT;

public class AppCatalogServiceIT extends OtoCloudTestBaseIT {
    static Logger logger = LoggerFactory.getLogger(AppCatalogServiceIT.class.getName());
    
    private static final int CATALOG_ID_GET_VALID = 1;
    private static final int CATALOG_ID_GET_INVALID = -1;
    
	@Test
	public void it_should_return_all_catalogs() {
		get("/api/app-shopping/app-catalogs?token={accessToken}",accessToken)
		.then().assertThat()
			.statusCode(200)
			.body("size()", greaterThan(0))
			.log().all(true);
	}
	
	@Test
	public void it_should_return_one_catalog_with_valid_catalog_id() {
		get("/api/app-shopping/app-catalogs/{id}?token={accessToken}",CATALOG_ID_GET_VALID,accessToken)
		.then().assertThat()
			.statusCode(200)
			.body(not(isEmptyOrNullString()))
			.log().all(true);
	}
	
	@Test
	public void it_should_return_no_catalog_with_invalid_catalog_id() {
		get("/api/app-shopping/app-catalogs/{id}?token={accessToken}",CATALOG_ID_GET_INVALID,accessToken)
		.then().assertThat()
			.statusCode(200)
			.body(isEmptyOrNullString())
			.log().all(true);
	}

}
