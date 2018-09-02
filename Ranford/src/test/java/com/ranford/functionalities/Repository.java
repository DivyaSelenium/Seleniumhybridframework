package com.ranford.functionalities;

import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import Excel.Excel_Class;
import PageLibrary.AdminPage;
import PageLibrary.BranchesPage;
import PageLibrary.GenericPage;
import PageLibrary.LoginPage;
import TestBase.Base;
import Utility.Screenshotss;

public class Repository extends Base{
	
	WebDriver driver;
	
	public ExtentReports extentreport;
	public ExtentTest extenttest;
	
	
	public void launch_Application()
	{
		
		Report_Extent();
		extenttest=extentreport.startTest("Start");   //reporting all data when launch the browser start.
		
		//System.setProperty("webdriver.chrome.driver", "C:\\Users\\divnagar\\Desktop\\geckodriver-v0.21.0-win64\\geckodriver.exe");
		//driver=new FirefoxDriver();
	
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\divnagar\\Downloads\\chromedriver_win32\\chromedriver.exe");
		driver=new ChromeDriver();
		
		log.info("Chrome browser launched");  //we can add/call ANYWHERE
		
		extenttest.log(LogStatus.PASS, "Launch Success");  //result of report pass or fail
		
		driver.get(read_testdata("sitUrl"));
		
		log.info("URL entered "+read_testdata("sitUrl"));
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		
		log.info("Maximize the browser");
		
		extenttest.log(LogStatus.PASS, "maximize Success"); //result of report pass or fail
		
		
		String strtitle=driver.getTitle();
		if(strtitle.equals("HDFC BANK"))
		{
			System.out.println("Title is corret : "+strtitle);
			
			log.info("Title is correct "+strtitle);    // Log4j report purpose we using 'log' is reference variable
			
			extenttest.log(LogStatus.PASS, "Tittle is correct"+strtitle); 
		}else {
			
			//FAILRE CONDITION ADD/CALL the screenshot
			Screenshotss.CaptureScreenShot("verifyTitle");
			//System.out.println(path);
			//extenttest.log(LogStatus.PASS, details);  /////////////////////////////////////////////////////////////////
			System.out.println("Title is INcorrect : "+strtitle);
			extenttest.log(LogStatus.FAIL, "Tittle is incorrect"+strtitle); 
		}   
			
		
	//	Assert.assertEquals(3, 10);   //(actual=coming from application, expected)
		
	//	Assert.assertEquals(driver.getTitle(), "HDFC BANK");   //Compare two values in TESTNG assert.
		
	}   
	
	public void login_Application()
	{
		LoginPage.username_textfield(driver).sendKeys(read_testdata("username"));
		LoginPage.password_textfield(driver).sendKeys(read_testdata("password"));
		LoginPage.login_button(driver).click();
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);    //implicit wait
		
		boolean blnLogout=AdminPage.logout_button(driver).isDisplayed();
		if(blnLogout)
		{
			Assert.assertTrue(true, "Login Suucesful ");
			extenttest.log(LogStatus.PASS, "Login Suucesfull"); 
		}
		else
		{
			Assert.assertTrue(false, "Login UnSuucesful");
			extenttest.log(LogStatus.PASS, "Login UnSuucesful"); 
		}
		
		
	}
	
	public void clickbranches()
	{
		AdminPage.branches_button(driver).click();
	}
		
	/*public void createNewBranch()  //WE ARE REFERING FROM APPLICATION
	{
		

		BranchesPage.branchName_txt(driver).sendKeys(read_testdata("branchname"));
		BranchesPage.branchAddress1_txt(driver).sendKeys(read_testdata("address"));
		BranchesPage.zipcode_txt(driver).sendKeys(read_testdata("zipcode"));
		//GenericPage.dropDownSelection(driver, By.id(read_OR("branch_country"))).selectByValue(read_testdata("country"));
		//GenericPage.dropDownSelection(driver, By.id(read_OR("branch_state"))).selectByValue(read_testdata("state"));
		//GenericPage.dropDownSelection(driver, By.id(read_OR("branch_city"))).selectByValue(read_testdata("city"));
		
		//made it more generic
		GenericPage.dropDownSelection(driver, getlocator("branch_country")).selectByValue(read_testdata("country"));
		GenericPage.dropDownSelection(driver, getlocator("branch_state")).selectByValue(read_testdata("state"));
		GenericPage.dropDownSelection(driver, getlocator("branch_city")).selectByValue(read_testdata("city"));
		BranchesPage.cancel_btn(driver).click();
	}*/
	
	public void createBranch(String bname, String address, String zip, String country, String state, String city)
	{
		
		BranchesPage.newBranch_btn(driver).click();
		BranchesPage.branchAddress1_txt(driver).sendKeys(bname);
		BranchesPage.branchAddress1_txt(driver).sendKeys(address);
		BranchesPage.zipcode_txt(driver).sendKeys(zip);
		GenericPage.dropDownSelection(driver, getlocator("branch_country")).selectByValue(country);
		GenericPage.dropDownSelection(driver, getlocator("branch_state")).selectByValue(state);
		GenericPage.dropDownSelection(driver, getlocator("branch_cityid")).selectByValue(city);
		BranchesPage.cancel_btn(driver).click();
		
		
	}
	
	
	public Object[][] excelContent(String fileName, String sheetName) throws IOException  //read file name and sheet name
	{
		Excel_Class.excelconnection(fileName, sheetName);   //connecting to the excel code
		int rc = Excel_Class.rcount();  //count row count
		int cc = Excel_Class.ccount();  //count coloumn count
		
		String[][] data=new String[rc-1][cc];
		
		for(int r=1;r<rc;r++)   //row
		{
			for(int c=0;c<cc;c++)  //column
			{
				data[r-1][c] = Excel_Class.readdata(c, r);    
			}
		}
		
		
		return data;
		
		
	}

	
	public void Report_Extent()
	{
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
		String timestamp= df.format(date);
		extentreport = new ExtentReports("C:\\Users\\divnagar\\Desktop\\Ranford (1)\\Ranford\\Reports"+"ExtentReportResults"+timestamp+".html",false);
	}

	
	public void logout_Application()
	{
		AdminPage.branches_button(driver).click();
		driver.close();
	}
	
	
	public void close()   //when  reach this condition the report should going to stop
	{
		driver.close();
		extentreport.endTest(extenttest);
		extentreport.flush();
	}
	

}
