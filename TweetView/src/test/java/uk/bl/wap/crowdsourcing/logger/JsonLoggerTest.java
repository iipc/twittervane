package uk.bl.wap.crowdsourcing.logger;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/testApplicationContext.xml"})
public class JsonLoggerTest {

	/**
	 * The Json logger to test
	 */
	@Autowired
	private JsonLogger logger;
	
	private String lineSeparator =	(String) java.security.AccessController.doPrivileged(
            new sun.security.action.GetPropertyAction("line.separator"));

	
	/**
	 * After setting the calendar, tests that a file is persisted with the correct suffix
	 */
	@Test
	public void setCalendarTest() throws IOException {
		
		Calendar calendar = new GregorianCalendar();
		int year = 2013;
		int month = 01 - 1;
		int dayOfMonth = 2;
		int hour = 03;
		int minute = 04;
		int second = 05;
		
		// set the calendar to the test date
		calendar.set(year, month, dayOfMonth, hour, minute, second);
		logger.setCalendar(calendar);
		
		// delete the log file
		try {
			logger.deleteLogFile();
		} catch (IOException fne) {
			// ignore the log file if missing
		}
		
		String rawJson = "{'elememt1':test1, 'element2':test2}";
		logger.log(rawJson);
		
		String fileSuffix = logger.getFileSuffix();
		// check that the file suffix is correct
		Assert.assertEquals("2013-01-02", fileSuffix);
		
		// check the file has been generated
		String absoluteFilePath = logger.getAbsoluteFilePath();
		FileInputStream fis = new FileInputStream(absoluteFilePath);
		int readByte;
		StringBuilder content = new StringBuilder();
		while ((readByte = fis.read()) != -1) {
			content.append((char)readByte);
		}
		
		fis.close();
		
		// check that the content is correct
		Assert.assertEquals(rawJson + "\n", content.toString());
	}
	
	/**
	 * Tests that a file is rotated correctly after the calendar date is changed
	 */
	@Test 
	public void logRotationTest() throws IOException {
		
		Calendar calendar = new GregorianCalendar();
		logger.setCalendar(calendar);

		// delete the log file
		try {
			logger.deleteLogFile();
		} catch (IOException fne) {
			// ignore the log file if missing
		}
		
		String rawJson = "{'elememt3':test3, 'element4':test4}";
		logger.log(rawJson);

		String fileSuffix = logger.getFileSuffix();
		
		// derive the file suffix for the current date
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String expectedFileSuffix = sdf.format(calendar.getTime());
		
		// check that the file suffix is correct
		Assert.assertEquals(expectedFileSuffix, fileSuffix);
		
		// check the file has been generated
		String absoluteFilePath = logger.getAbsoluteFilePath();
		FileInputStream fis = new FileInputStream(absoluteFilePath);
		int readByte;
		StringBuilder content = new StringBuilder();
		while ((readByte = fis.read()) != -1) {
			content.append((char)readByte);
		}
		
		fis.close();
		
		// check that the content is correct
		Assert.assertEquals(rawJson + "\n", content.toString());

	}
	
	@Test
	public void multiLineTest() throws IOException {
		Calendar calendar = new GregorianCalendar();
		logger.setCalendar(calendar);

		// delete the log file
		try {
			logger.deleteLogFile();
		} catch (IOException fne) {
			// ignore the log file if missing
		}
		
		String rawJsonLine1 = "{'elememt5':test5, 'element6':test6}";
		String rawJsonLine2 = "{'elememt7':test7, 'element8':test8}";
		logger.log(rawJsonLine1);
		logger.log(rawJsonLine2);
		
		// use a list to hold the  lines for comparison
		List<String> testContent = new ArrayList<String>();
		testContent.add(rawJsonLine1);
		testContent.add(rawJsonLine2);

		String fileSuffix = logger.getFileSuffix();
		
		// derive the file suffix for the current date
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String expectedFileSuffix = sdf.format(calendar.getTime());
		
		// check that the file suffix is correct
		Assert.assertEquals(expectedFileSuffix, fileSuffix);
		
		// check the file has been generated
		String absoluteFilePath = logger.getAbsoluteFilePath();
		FileInputStream fis = new FileInputStream(absoluteFilePath);
		DataInputStream in = new DataInputStream(fis);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		String line;
		List<String> content = new ArrayList<String>();
		while ((line = reader.readLine()) != null) {
			content.add(line);
		}
		
		reader.close();
		in.close();
		fis.close();
		
		for (int i = 0; i < testContent.size(); i++) {
			String testLine = testContent.get(i);
			// check that the content is correct
			Assert.assertEquals(testLine, content.get(i));
		}

	}
}
