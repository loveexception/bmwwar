package com.maodajun.bmw.quartz;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.maodajun.bmw.bean.Login;

public class BoccDriverTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.setProperty("webdriver.chrome.driver","/Users/maodajun/Downloads/chromedriver");
		//System.setProperty("webdriver.firefox.bin", "/Applications/Firefox.app/Contents/MacOS/firefox"); 
		System.setProperty("webdriver.gecko.driver","/Users/maodajun/Downloads/geckodriver");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

//	@Test
//	public void testGetDriver() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetDriver() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testCreatePersonsDriversByLogin() {
		Login login = new Login();
		Map<Login, BoccDriver> map = BoccDriver.createPersonsDriversByLogin(Arrays.asList(login));
		assertEquals(1, map.size());
	}

	@Test
	public void testCreatePersonsDriversBySubtopic() throws MalformedURLException {
		  ChromeDriverService service = new ChromeDriverService
				  .Builder()
				  .usingDriverExecutable(new File("/Users/maodajun/Downloads/chromedriver"))
				  .usingAnyFreePort()
				  
				  .build();
	        try {
	            service.start();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	       
		ChromeOptions options = new ChromeOptions();
		DesiredCapabilities d = DesiredCapabilities.chrome();
		
		WebDriver driver = new RemoteWebDriver(service.getUrl(), d);

		//WebDriver driver =new ChromeDriver();
		driver.get("http://www.baidu.com");
	    driver.quit();
	    service.stop();
		
	}

	@Test
	public void testLogin() {
		ChromeDriverService driverService = ChromeDriverService.createDefaultService();

		WebDriver driver = new ChromeDriver(driverService, new ChromeOptions());	
		driver.get("http://www.baidu.com");
	    //driver.quit();	
	    driverService.stop();
	
	
	}

	@Test
	public void testReaded() {
//		PhantomJSDriverService driverService = PhantomJSDriverService.CreateDefaultService();
//		driverService.HideCommandPromptWindow = true;
//
//		var driver = new PhantomJSDriver(driverService);	
		
	}

	@Test
	public void testLiked() throws MalformedURLException {

		WebDriver driver = new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub"),DesiredCapabilities.htmlUnitWithJs());
		driver.get("http://www.baidu.com");
	    driver.quit();	
	}

	@Test
	public void testColled() {
		//fail("Not yet implemented");
	}

	@Test
	public void testSubtopiced() {
		//fail("Not yet implemented");
	}

}
