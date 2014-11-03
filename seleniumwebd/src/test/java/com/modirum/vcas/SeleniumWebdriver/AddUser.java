package com.modirum.vcas.SeleniumWebdriver;

import static org.junit.Assert.*;

import java.net.URL;

import org.junit.*;
import org.junit.rules.TestName;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.support.ui.Select;

import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.SauceOnDemandTestWatcher;

public class AddUser implements SauceOnDemandSessionIdProvider {

	String userName = "jaceqmartin";
	String userKey = "87334f8c-4d83-4909-9b5b-60843d1675e5";
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	String adminUsername = "admin";
	String adminPass = "Password2";
	String vcasUrl = "/mdpayacs-admin/login/login.htm";
	String issuerName = "Royal Bank of Canada";
	String issuerGroup = "Test Issuer Group";
	String testUsername = "testadd";
	String testPass = "Password1";
	String testNewpass = "PasKoLang12";
	String vcasMain = "/mdpayacs-admin/main/main.htm";

	public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication(
			userName, userKey);

	/**
	 * JUnit Rule that marks the Sauce Job as passed/failed when the test
	 * succeeds or fails. Pass/fail status is shown on our Sauce Labs
	 * test page (https://saucelabs.com/tests).
	 */
	public @Rule
	SauceOnDemandTestWatcher resultReportingTestWatcher = new SauceOnDemandTestWatcher(
			this, authentication);

	/**
	 * JUnit Rule that will record the test name of the current test. This is
	 * referenced when creating the {@link DesiredCapabilities}, so the Sauce
	 * Job is created with the test name.
	 */
	public @Rule
	TestName testName = new TestName();

	private WebDriver driver;

	private String sessionId;

	@Before
	public void setUp() throws Exception {

		baseUrl = "https://visa-admin.test.modirum.com";

		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		capabilities.setCapability("version", "27");
		// Note: XP is tested as Windows 2003 Server on the Sauce Cloud
		capabilities.setCapability("platform", Platform.MAC);
		capabilities.setCapability("name", testName.getMethodName());
		this.driver = new RemoteWebDriver(new URL("http://"
				+ authentication.getUsername() + ":"
				+ authentication.getAccessKey()
				+ "@ondemand.saucelabs.com:80/wd/hub"), capabilities);
		this.sessionId = ((RemoteWebDriver) driver).getSessionId().toString();

		driver.get(baseUrl + vcasUrl);
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(adminUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(adminPass);
		driver.findElement(By.id("button")).click();

	}

	public String getSessionId() {
		return sessionId;
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

	@Test
	public void testAddInactiveUser() throws Exception {
		driver.get(baseUrl + vcasMain);
		driver.findElement(By.linkText("User Admin")).click();
		driver.findElement(By.name("button")).click();
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys(testUsername);
		driver.findElement(By.id("fullname")).clear();
		driver.findElement(By.id("fullname")).sendKeys(testUsername);
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(testPass);
		driver.findElement(By.id("pwdCheck")).clear();
		driver.findElement(By.id("pwdCheck")).sendKeys(testPass);
		driver.findElement(By.xpath("//input[@value='Save as new user']"))
				.click();
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(testUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(testPass);
		driver.findElement(By.id("button")).click();
		assertEquals("Invalid username or password.",
				driver.findElement(By.cssSelector("td.error")).getText());
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(adminUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(adminPass);
		driver.findElement(By.id("button")).click();
		driver.findElement(By.linkText("User Admin")).click();
		driver.findElement(By.linkText(testUsername)).click();
		driver.findElement(By.xpath("(//input[@name='button'])[2]")).click();
		assertTrue(closeAlertAndGetItsText().matches(
				"^Confirm delete 'testadd'[\\s\\S]$"));
	}

	@Test
	public void testAddUserNoIssuerNoGroup() throws Exception {
		driver.get(baseUrl + vcasMain);
		driver.findElement(By.linkText("User Admin")).click();
		driver.findElement(By.name("button")).click();
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys(testUsername);
		driver.findElement(By.id("fullname")).clear();
		driver.findElement(By.id("fullname")).sendKeys(testUsername);
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(testPass);
		driver.findElement(By.id("pwdCheck")).clear();
		driver.findElement(By.id("pwdCheck")).sendKeys(testPass);
		new Select(driver.findElement(By.id("status")))
				.selectByVisibleText("Active");
		driver.findElement(By.xpath("//input[@value='Save as new user']"))
				.click();
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(testUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(testPass);
		driver.findElement(By.id("button")).click();
		driver.findElement(By.id("oldPwd")).clear();
		driver.findElement(By.id("oldPwd")).sendKeys(testPass);
		driver.findElement(By.id("newPwd")).clear();
		driver.findElement(By.id("newPwd")).sendKeys(testNewpass);
		driver.findElement(By.id("verify")).clear();
		driver.findElement(By.id("verify")).sendKeys(testNewpass);
		driver.findElement(By.id("saveButton")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(testUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(testNewpass);
		driver.findElement(By.id("button")).click();
		assertEquals("Home", driver.findElement(By.linkText("Home")).getText());
		assertEquals("Logout", driver.findElement(By.linkText("Logout"))
				.getText());
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(adminUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(adminPass);
		driver.findElement(By.id("button")).click();
		driver.findElement(By.linkText("User Admin")).click();
		driver.findElement(By.linkText("testadd")).click();
		driver.findElement(By.xpath("(//input[@name='button'])[2]")).click();
		assertTrue(closeAlertAndGetItsText().matches(
				"^Confirm delete 'testadd'[\\s\\S]$"));
	}

	@Test
	public void testAddUserWithIssuerAndGroup() throws Exception {
		driver.get(baseUrl + vcasMain);
		driver.findElement(By.linkText("User Admin")).click();
		driver.findElement(By.name("button")).click();
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys(testUsername);
		driver.findElement(By.id("fullname")).clear();
		driver.findElement(By.id("fullname")).sendKeys(testUsername);
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(testPass);
		driver.findElement(By.id("pwdCheck")).clear();
		driver.findElement(By.id("pwdCheck")).sendKeys(testPass);
		new Select(driver.findElement(By.id("issuerOrGroup")))
				.selectByVisibleText(issuerName);
		driver.findElement(By.xpath("//input[@value='Save as new user']"))
				.click();
		driver.findElement(By.xpath("(//input[@name='button'])[3]")).click();
		
		// Warning: assertTextPresent may require manual changes
		assertTrue(driver.findElement(By.cssSelector("BODY")).getText()
				.matches(issuerName));
		driver.findElement(By.name("button")).click();
		new Select(driver.findElement(By.id("issuerOrGroup")))
				.selectByVisibleText(issuerGroup + " (GROUP)");
		driver.findElement(By.xpath("//input[@value='Update']")).click();
		
		// Warning: assertTextPresent may require manual changes
		assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches(issuerGroup + " help"));

		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(adminUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(adminPass);
		driver.findElement(By.id("button")).click();
		driver.findElement(By.linkText("User Admin")).click();
		driver.findElement(By.linkText(testUsername)).click();
		driver.findElement(By.xpath("(//input[@name='button'])[2]")).click();
		assertTrue(closeAlertAndGetItsText().matches(
				"^Confirm delete '\\$\\{testUsername\\}'[\\s\\S]$"));
	}

	@Test
	public void testEditAuthAdminPrivilege() throws Exception {
		driver.get(baseUrl + vcasMain);
		driver.findElement(By.linkText("User Admin")).click();
		driver.findElement(By.name("button")).click();
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys(testUsername);
		driver.findElement(By.id("fullname")).clear();
		driver.findElement(By.id("fullname")).sendKeys(testUsername);
		new Select(driver.findElement(By.id("status")))
				.selectByVisibleText("Active");
		driver.findElement(By.id("rights.viewIssuers")).click();
		driver.findElement(By.id("rights.editIssuers")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(testPass);
		driver.findElement(By.id("pwdCheck")).clear();
		driver.findElement(By.id("pwdCheck")).sendKeys(testPass);
		driver.findElement(By.xpath("//input[@value='Save as new user']"))
				.click();
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(testUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(testPass);
		driver.findElement(By.id("button")).click();
		driver.findElement(By.id("oldPwd")).clear();
		driver.findElement(By.id("oldPwd")).sendKeys(testPass);
		driver.findElement(By.id("newPwd")).clear();
		driver.findElement(By.id("newPwd")).sendKeys(testNewpass);
		driver.findElement(By.id("verify")).clear();
		driver.findElement(By.id("verify")).sendKeys(testNewpass);
		driver.findElement(By.id("saveButton")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(testUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(testNewpass);
		driver.findElement(By.id("button")).click();
		try {
			assertTrue(isElementPresent(By.linkText("Authentication Admin")));
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		driver.findElement(By.linkText("Authentication Admin")).click();
		try {
			assertTrue(isElementPresent(By
					.cssSelector("input[type=\"submit\"]")));
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertTrue(isElementPresent(By.xpath("//input[@value='Remove']")));
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		assertEquals("Issuer Maintenance",
				driver.findElement(By.cssSelector("h2")).getText());
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(adminUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(adminPass);
		driver.findElement(By.id("button")).click();
		driver.findElement(By.linkText("User Admin")).click();
		driver.findElement(By.linkText(testUsername)).click();
		driver.findElement(By.xpath("(//input[@name='button'])[2]")).click();
		assertTrue(closeAlertAndGetItsText().matches(
				"^Confirm delete 'testadd'[\\s\\S]$"));
	}

	@Test
	public void testEditCardHoldersPrivilege() throws Exception {
		driver.get(baseUrl + vcasMain);
		driver.findElement(By.linkText("User Admin")).click();
		driver.findElement(By.name("button")).click();
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys(testUsername);
		driver.findElement(By.id("fullname")).clear();
		driver.findElement(By.id("fullname")).sendKeys(testUsername);
		new Select(driver.findElement(By.id("status")))
				.selectByVisibleText("Active");
		driver.findElement(By.id("rights.viewCardHolders")).click();
		driver.findElement(By.id("rights.editCardHolders")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(testPass);
		driver.findElement(By.id("pwdCheck")).clear();
		driver.findElement(By.id("pwdCheck")).sendKeys(testPass);
		driver.findElement(By.xpath("//input[@value='Save as new user']"))
				.click();
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(testUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(testPass);
		driver.findElement(By.id("button")).click();
		driver.findElement(By.id("oldPwd")).clear();
		driver.findElement(By.id("oldPwd")).sendKeys(testPass);
		driver.findElement(By.id("newPwd")).clear();
		driver.findElement(By.id("newPwd")).sendKeys(testNewpass);
		driver.findElement(By.id("verify")).clear();
		driver.findElement(By.id("verify")).sendKeys(testNewpass);
		driver.findElement(By.id("saveButton")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(testUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(testNewpass);
		driver.findElement(By.id("button")).click();
		assertEquals("Cards Search", driver.findElement(By.cssSelector("h2"))
				.getText());
		try {
			assertTrue(isElementPresent(By
					.cssSelector("#cardForm > input[type=\"submit\"]")));
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertEquals(
					"Add new card",
					driver.findElement(
							By.cssSelector("#cardForm > input[type=\"submit\"]"))
							.getAttribute("value"));
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		driver.findElement(By.cssSelector("#cardForm > input[type=\"submit\"]"))
				.click();
		assertEquals("Card Holder Details",
				driver.findElement(By.xpath("//h2[2]")).getText());
		try {
			assertTrue(isElementPresent(By
					.xpath("//form[@id='cForm']/table/tbody/tr[10]/td")));
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertTrue(isElementPresent(By.id("id")));
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		assertEquals("PAN",
				driver.findElement(By.cssSelector("td.tdHeaderVertical"))
						.getText());
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(adminUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(adminPass);
		driver.findElement(By.id("button")).click();
		driver.findElement(By.linkText("User Admin")).click();
		driver.findElement(By.linkText(testUsername)).click();
		driver.findElement(By.xpath("(//input[@name='button'])[2]")).click();
		assertTrue(closeAlertAndGetItsText().matches(
				"^Confirm delete 'testadd'[\\s\\S]$"));
	}

	@Test
	public void testEditUsersPrivilege() throws Exception {
		driver.get(baseUrl + vcasMain);
		driver.findElement(By.linkText("User Admin")).click();
		driver.findElement(By.name("button")).click();
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys(testUsername);
		driver.findElement(By.id("fullname")).clear();
		driver.findElement(By.id("fullname")).sendKeys(testUsername);
		new Select(driver.findElement(By.id("status")))
				.selectByVisibleText("Active");
		driver.findElement(By.id("rights.viewUsers")).click();
		driver.findElement(By.id("rights.editUsers")).click();
		driver.findElement(By.id("rights.viewTransactions")).click();
		driver.findElement(By.id("rights.viewCardHolders")).click();
		driver.findElement(By.id("rights.editCardHolders")).click();
		driver.findElement(By.id("rights.viewIssuers")).click();
		driver.findElement(By.id("rights.editIssuers")).click();
		driver.findElement(By.id("rights.manageKeys")).click();
		driver.findElement(By.id("rights.adminSetup")).click();
		driver.findElement(By.id("rights.editRules")).click();
		driver.findElement(By.id("rights.temporaryPanActions")).click();
		driver.findElement(By.id("rights.viewDebug")).click();
		driver.findElement(By.id("rights.fileUpload")).click();
		driver.findElement(By.id("rights.reports")).click();
		driver.findElement(By.id("rights.decryptPan")).click();
		driver.findElement(By.xpath("//input[@value='Save as new user']"))
				.click();
	}

	@Test
	public void testManageKeysPrivilege() throws Exception {
		driver.get(baseUrl + vcasMain);
		driver.findElement(By.linkText("User Admin")).click();
		driver.findElement(By.name("button")).click();
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys(testUsername);
		driver.findElement(By.id("fullname")).clear();
		driver.findElement(By.id("fullname")).sendKeys(testUsername);
		new Select(driver.findElement(By.id("status")))
				.selectByVisibleText("Active");
		driver.findElement(By.id("rights.manageKeys")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(testPass);
		driver.findElement(By.id("pwdCheck")).clear();
		driver.findElement(By.id("pwdCheck")).sendKeys(testPass);
		driver.findElement(By.xpath("//input[@value='Save as new user']"))
				.click();
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(testUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(testPass);
		driver.findElement(By.id("button")).click();
		driver.findElement(By.id("oldPwd")).clear();
		driver.findElement(By.id("oldPwd")).sendKeys(testPass);
		driver.findElement(By.id("newPwd")).clear();
		driver.findElement(By.id("newPwd")).sendKeys(testNewpass);
		driver.findElement(By.id("verify")).clear();
		driver.findElement(By.id("verify")).sendKeys(testNewpass);
		driver.findElement(By.id("saveButton")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(testUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(testNewpass);
		driver.findElement(By.id("button")).click();
		try {
			assertTrue(isElementPresent(By.linkText("Keys")));
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		assertEquals("Keys Administration",
				driver.findElement(By.cssSelector("h2")).getText());
		try {
			assertTrue(isElementPresent(By.linkText("HSM Keysets")));
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertTrue(isElementPresent(By.linkText("HSM Engines")));
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertTrue(isElementPresent(By.linkText("HSM Keys")));
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(adminUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(adminPass);
		driver.findElement(By.id("button")).click();
		driver.findElement(By.linkText("User Admin")).click();
		driver.findElement(By.linkText(testUsername)).click();
		driver.findElement(By.xpath("(//input[@name='button'])[2]")).click();
		assertTrue(closeAlertAndGetItsText().matches(
				"^Confirm delete 'testadd'[\\s\\S]$"));
	}

	@Test
	public void testMixedNoValueForPasswordFields() throws Exception {
		driver.get(baseUrl + vcasMain);
		driver.findElement(By.linkText("User Admin")).click();
		driver.findElement(By.name("button")).click();
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys(testUsername);
		driver.findElement(By.id("fullname")).clear();
		driver.findElement(By.id("fullname")).sendKeys(testUsername);
		driver.findElement(By.xpath("//input[@value='Save as new user']"))
				.click();
		assertEquals("Password is required.",
				driver.findElement(By.cssSelector("span.error")).getText());
		// Warning: assertTextPresent may require manual changes
		try {
			assertTrue(driver
					.findElement(By.cssSelector("BODY"))
					.getText()
					.matches(
							"^[\\s\\S]*Passwords must be a minimum of 8 characters and contain at least one uppercase letter, lowercase letter and a number.[\\s\\S]*$"));
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}

		driver.findElement(By.id("pwdCheck")).clear();
		driver.findElement(By.id("pwdCheck")).sendKeys("Password2");
		driver.findElement(By.xpath("//input[@value='Save as new user']"))
				.click();
		assertEquals("Passwords do not match.",
				driver.findElement(By.cssSelector("span.error")).getText());
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("Password2");
		driver.findElement(By.xpath("//input[@value='Save as new user']"))
				.click();
		assertEquals("Password is required.",
				driver.findElement(By.cssSelector("span.error")).getText());
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("Password1");
		driver.findElement(By.id("pwdCheck")).clear();
		driver.findElement(By.id("pwdCheck")).sendKeys("Password2");
		driver.findElement(By.xpath("//input[@value='Save as new user']"))
				.click();
		assertEquals("Passwords do not match.",
				driver.findElement(By.cssSelector("span.error")).getText());
	}

	@Test
	public void testNoValueForLoginOrName() throws Exception {
		driver.get(baseUrl + vcasMain);
		driver.findElement(By.linkText("User Admin")).click();
		driver.findElement(By.name("button")).click();
		driver.findElement(By.id("fullname")).clear();
		driver.findElement(By.id("fullname")).sendKeys(testUsername);
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(testPass);
		driver.findElement(By.id("pwdCheck")).clear();
		driver.findElement(By.id("pwdCheck")).sendKeys(testPass);
		driver.findElement(By.xpath("//input[@value='Save as new user']"))
				.click();
		assertEquals("size must be between 1 and 128",
				driver.findElement(By.cssSelector("span.error")).getText());
		assertEquals(
				"User name must be 1-128 letter string and consist of a-z, A-Z, 0-9, '-', '_', '@', '.' characters.",
				driver.findElement(
						By.xpath("//form[@id='uForm']/table/tbody/tr/td[2]/span[2]"))
						.getText());
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		driver.findElement(By.name("button")).click();
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys(testUsername);
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(testPass);
		driver.findElement(By.id("pwdCheck")).clear();
		driver.findElement(By.id("pwdCheck")).sendKeys(testPass);
		driver.findElement(By.xpath("//input[@value='Save as new user']"))
				.click();
		assertEquals("size must be between 1 and 128",
				driver.findElement(By.cssSelector("span.error")).getText());
	}

	@Test
	public void testViewAuthAdminPrivilege() throws Exception {
		driver.get(baseUrl + vcasMain);
		driver.findElement(By.linkText("User Admin")).click();
		driver.findElement(By.name("button")).click();
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys(testUsername);
		driver.findElement(By.id("fullname")).clear();
		driver.findElement(By.id("fullname")).sendKeys(testUsername);
		new Select(driver.findElement(By.id("status")))
				.selectByVisibleText("Active");
		driver.findElement(By.id("rights.viewIssuers")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(testPass);
		driver.findElement(By.id("pwdCheck")).clear();
		driver.findElement(By.id("pwdCheck")).sendKeys(testPass);
		driver.findElement(By.xpath("//input[@value='Save as new user']"))
				.click();
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(testUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(testPass);
		driver.findElement(By.id("button")).click();
		driver.findElement(By.id("oldPwd")).clear();
		driver.findElement(By.id("oldPwd")).sendKeys(testPass);
		driver.findElement(By.id("newPwd")).clear();
		driver.findElement(By.id("newPwd")).sendKeys(testNewpass);
		driver.findElement(By.id("verify")).clear();
		driver.findElement(By.id("verify")).sendKeys(testNewpass);
		driver.findElement(By.id("saveButton")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(testUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(testNewpass);
		driver.findElement(By.id("button")).click();
		try {
			assertTrue(isElementPresent(By.linkText("Authentication Admin")));
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		driver.findElement(By.linkText("Authentication Admin")).click();
		assertEquals("Issuer Maintenance",
				driver.findElement(By.cssSelector("h2")).getText());
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(adminUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(adminPass);
		driver.findElement(By.id("button")).click();
		driver.findElement(By.linkText("User Admin")).click();
		driver.findElement(By.linkText(testUsername)).click();
		driver.findElement(By.xpath("(//input[@name='button'])[2]")).click();
		assertTrue(closeAlertAndGetItsText().matches(
				"^Confirm delete 'testadd'[\\s\\S]$"));
	}

	@Test
	public void testViewCardHoldersPrivilege() throws Exception {
		driver.get(baseUrl + vcasMain);
		driver.findElement(By.linkText("User Admin")).click();
		driver.findElement(By.name("button")).click();
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys(testUsername);
		driver.findElement(By.id("fullname")).clear();
		driver.findElement(By.id("fullname")).sendKeys(testUsername);
		new Select(driver.findElement(By.id("status")))
				.selectByVisibleText("Active");
		driver.findElement(By.id("rights.viewCardHolders")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(testPass);
		driver.findElement(By.id("pwdCheck")).clear();
		driver.findElement(By.id("pwdCheck")).sendKeys(testPass);
		driver.findElement(By.xpath("//input[@value='Save as new user']"))
				.click();
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(testUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(testPass);
		driver.findElement(By.id("button")).click();
		driver.findElement(By.id("oldPwd")).clear();
		driver.findElement(By.id("oldPwd")).sendKeys(testPass);
		driver.findElement(By.id("newPwd")).clear();
		driver.findElement(By.id("newPwd")).sendKeys(testNewpass);
		driver.findElement(By.id("verify")).clear();
		driver.findElement(By.id("verify")).sendKeys(testNewpass);
		driver.findElement(By.id("saveButton")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(testUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(testNewpass);
		driver.findElement(By.id("button")).click();
		new Select(driver.findElement(By.id("issuerId")))
				.selectByVisibleText("Royal Bank of Canada");
		driver.findElement(By.xpath("(//input[@id='smallbutton'])[3]")).click();
		driver.findElement(By.id("cardButton")).click();
		assertEquals("asd", driver.findElement(By.linkText("asd")).getText());
		try {
			assertTrue(isElementPresent(By.linkText("410700")));
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertFalse(isElementPresent(By
					.cssSelector("#cardForm > input[type=\"submit\"]")));
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(adminUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(adminPass);
		driver.findElement(By.id("button")).click();
		driver.findElement(By.linkText("User Admin")).click();
		driver.findElement(By.linkText(testUsername)).click();
		driver.findElement(By.xpath("(//input[@name='button'])[2]")).click();
		assertTrue(closeAlertAndGetItsText().matches(
				"^Confirm delete 'testadd'[\\s\\S]$"));
	}

	@Test
	public void testViewTransactionsPrivilege() throws Exception {
		driver.get(baseUrl + vcasMain);
		driver.findElement(By.linkText("User Admin")).click();
		driver.findElement(By.name("button")).click();
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys(testUsername);
		driver.findElement(By.id("fullname")).clear();
		driver.findElement(By.id("fullname")).sendKeys(testUsername);
		new Select(driver.findElement(By.id("status")))
				.selectByVisibleText("Active");
		driver.findElement(By.id("rights.viewTransactions")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(testPass);
		driver.findElement(By.id("pwdCheck")).clear();
		driver.findElement(By.id("pwdCheck")).sendKeys(testPass);
		driver.findElement(By.xpath("//input[@value='Save as new user']"))
				.click();
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(testUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys("Password1");
		driver.findElement(By.id("button")).click();
		driver.findElement(By.id("oldPwd")).clear();
		driver.findElement(By.id("oldPwd")).sendKeys(testPass);
		driver.findElement(By.id("newPwd")).clear();
		driver.findElement(By.id("newPwd")).sendKeys(testNewpass);
		driver.findElement(By.id("verify")).clear();
		driver.findElement(By.id("verify")).sendKeys(testNewpass);
		driver.findElement(By.id("saveButton")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(testUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(testNewpass);
		driver.findElement(By.id("button")).click();
		try {
			assertTrue(isElementPresent(By.id("txButton")));
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertTrue(isElementPresent(By
					.xpath("//div[@class='searchOptions']/input[3]")));
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		try {
			assertTrue(isElementPresent(By
					.xpath("//div[@class='searchOptions']/input[4]")));
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(adminUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(adminPass);
		driver.findElement(By.id("button")).click();
		driver.findElement(By.linkText("User Admin")).click();
		driver.findElement(By.linkText(testUsername)).click();
		driver.findElement(By.xpath("(//input[@name='button'])[2]")).click();
		assertTrue(closeAlertAndGetItsText().matches(
				"^Confirm delete 'testadd'[\\s\\S]$"));
	}

	@Test
	public void testViewUsersPrivilege() throws Exception {
		driver.get(baseUrl + vcasMain);
		driver.findElement(By.linkText("User Admin")).click();
		driver.findElement(By.name("button")).click();
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys(testUsername);
		driver.findElement(By.id("fullname")).clear();
		driver.findElement(By.id("fullname")).sendKeys(testUsername);
		new Select(driver.findElement(By.id("status")))
				.selectByVisibleText("Active");
		driver.findElement(By.id("rights.viewUsers")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(testPass);
		driver.findElement(By.id("pwdCheck")).clear();
		driver.findElement(By.id("pwdCheck")).sendKeys(testPass);
		driver.findElement(By.xpath("//input[@value='Save as new user']"))
				.click();
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(testUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(testPass);
		driver.findElement(By.id("button")).click();
		driver.findElement(By.id("oldPwd")).clear();
		driver.findElement(By.id("oldPwd")).sendKeys(testPass);
		driver.findElement(By.id("newPwd")).clear();
		driver.findElement(By.id("newPwd")).sendKeys(testNewpass);
		driver.findElement(By.id("verify")).clear();
		driver.findElement(By.id("verify")).sendKeys(testNewpass);
		driver.findElement(By.id("saveButton")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(testUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(testNewpass);
		driver.findElement(By.id("button")).click();
		driver.findElement(By.linkText("User Admin")).click();
		assertEquals("Home", driver.findElement(By.linkText("Home")).getText());
		assertEquals("Logout", driver.findElement(By.linkText("Logout"))
				.getText());
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.id("j_username")).clear();
		driver.findElement(By.id("j_username")).sendKeys(adminUsername);
		driver.findElement(By.id("j_password")).clear();
		driver.findElement(By.id("j_password")).sendKeys(adminPass);
		driver.findElement(By.id("button")).click();
		driver.findElement(By.linkText("User Admin")).click();
		driver.findElement(By.linkText(testUsername)).click();
		driver.findElement(By.xpath("(//input[@name='button'])[2]")).click();
		assertTrue(closeAlertAndGetItsText().matches(
				"^Confirm delete 'testadd'[\\s\\S]$"));
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if (isElementPresent(By.name("button")))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}

	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}