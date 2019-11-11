//package test;

import java.io.Console;
import java.rmi.UnexpectedException;

import javax.ws.rs.NotSupportedException;

import org.openqa.selenium.WebDriver;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.selenium.Eyes;

public class CompareEyes extends Eyes {
	
	private boolean isComparisonEnabled = false;
	
	private boolean isInBaselineMode = true;
	BatchInfo batchInfo = null;
	
	private String appName;
	private String testName;
	private RectangleSize viewportSize;
	
	// Public Methods
	@Override
	public WebDriver open(WebDriver driver, String appName, String testName, RectangleSize viewportSize) {
		
		if (this.isComparisonEnabled){
			close();
			
				this.appName = appName;
				this.testName = testName;
				this.viewportSize = viewportSize;
				
				this.setSaveFailedTests(true);
				this.isInBaselineMode = true;

            System.out.println(viewportSize.toString());

            try {
                WebDriver newdriver = super.open(driver, appName, testName, viewportSize);
                return newdriver;
            } catch (Exception e) {           }
			return null;
		}
		else{
			return super.open(driver, appName, testName, viewportSize);
		}
	}
	
	@Override
	public WebDriver open(WebDriver driver, String appName, String testName) {
		
		if (isComparisonEnabled){
			throw new NotSupportedException("This overload is not supported in comparison mode.");
		}
		
		return super.open(driver, appName, testName);
	}
	
	public void switchToComparisonMode(WebDriver driver) throws UnexpectedException {
		
		if (!isComparisonEnabled){
			throw new UnexpectedException("Comparison Mode is not enabled!");
		}
		
		close();
		
		this.setSaveFailedTests(false);
		this.isInBaselineMode = false;
		
		
		super.open(driver, appName, testName, viewportSize);
	}
	
	@Override
	public TestResults close(boolean throwException) {
		
		if (this.isComparisonEnabled){
			TestResults testResults = null;
			
			if (this.getIsOpen()){
				if (this.isInBaselineMode){
					testResults = super.close(false);
					testResults.delete();
				}
				else{
					testResults = super.close(throwException);
				}
			}
			
			return testResults;
		}
		else
		{
			return super.close(throwException);
		}
	}

	public void setEnableComparison(boolean enableComparison) {
		this.isComparisonEnabled = enableComparison;
	}
	
	public boolean getEnableComparison(){
		return this.isComparisonEnabled;
	}

}
