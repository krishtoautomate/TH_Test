package com.base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import com.aventstack.extentreports.ExtentTest;


public class ScreenShotManager extends BasePageObjects<ScreenShotManager>{
	
	
	public ScreenShotManager(WebDriver driver, Logger log, ExtentTest test) {
		super(driver, log, test);		
	}
	
	public synchronized String generateScreenshot() {
		
		int indexNo = 0;
		
		File ScreenShot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		//Name the screen-shot		
		String imgPath = "img/"+indexNo+"_"+"screenShot" +"_" +Constants.DATE_NOW+".PNG";
		
		do {
			indexNo++;
			imgPath = "img/"+indexNo+"_"+"screenShot" +"_" +Constants.DATE_NOW+".PNG";
		} while(Files.exists(Paths.get(Constants.REPORT_DIR + imgPath)));
		
		File filePath = new File(Constants.REPORT_DIR + imgPath);
		
		try {
			FileUtils.moveFile(ScreenShot, filePath);
		} catch (IOException | WebDriverException e) {
			log.error("screenShot not Found!!!");
		}
		
		return imgPath;
	}
	
}
