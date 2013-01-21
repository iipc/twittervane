package uk.bl.wap.crowdsourcing.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Logs tweets to a specified json log file
 * @author twoods
 *
 */
public class JsonLogger {

	/** we append to the json log file if it already exists **/
	private static final Boolean APPEND_TO_FILE = true;
	
	/** the application logger **/
	private final Log log;
	
	/** the filename for the log file **/
	private String fileName;
	
	/** the file path for the log file **/
	private String filePath;
	
	/** the file extension for the log file **/
	private String fileExtension;
	
	/** the file suffix for the log file **/
	private String fileSuffix;
	
	/** the absolute file path for the log file **/
	private String absoluteFilePath;
	
	/** the calendar used to derive the log file suffix **/
	private Calendar calendar;
	
	public JsonLogger() {
		log = LogFactory.getLog(getClass());
		log.info("JsonLogger initialised with logfile: " + fileName);
	}
	
	/**
	 * Log a raw Json <code>String</code> to the Json log file
	 * @param rawJson the raw Json <code>String</code> to log
	 * @throws IOException
	 */
	public final void log(String rawJson) throws IOException {
		if (rawJson != null && !rawJson.trim().equals("")) {
			storeJSON(rawJson);
		}
	}
	
 	
    private void storeJSON(String rawJSON) throws IOException {
    	
    	// derive the absolute path for the file
		this.setAbsoluteFilePath();

        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(absoluteFilePath.toString(), APPEND_TO_FILE);
            osw = new OutputStreamWriter(fos, "UTF-8");
            bw = new BufferedWriter(osw);
            bw.write(rawJSON + "\n");
            bw.flush();
        } finally {
            if (bw != null) {
            	bw.close();
            }
            if (osw != null) {
                osw.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public void deleteLogFile() throws IOException {
		this.setAbsoluteFilePath();
		File file = new File(absoluteFilePath);
		if (!file.delete()) {
			throw new IOException("Could not delete file: " + absoluteFilePath);
		}
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @return the fileExtension
	 */
	public String getFileExtension() {
		return fileExtension;
	}

	/**
	 * @return the fileSuffix
	 */
	public String getFileSuffix() {
		return fileSuffix;
	}

	/**
	 * @return the calendar
	 */
	public Calendar getCalendar() {
		return calendar;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @param fileExtension the fileExtension to set (eg: "log")
	 */
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	/**
	 * @param calendar the calendar to set
	 */
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	/**
	 * @return the absoluteFilePath
	 */
	public String getAbsoluteFilePath() {
		return absoluteFilePath;
	}

	/**
	 * @param absoluteFilePath the absoluteFilePath to set
	 */
	private void setAbsoluteFilePath() {
		
		Calendar cal = null; 
		
		if (calendar == null) {
			cal = new GregorianCalendar();
		} else {
			cal = calendar;
		}
		
    	// derive the absolute path for the file
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	
    	// set the file suffix
    	fileSuffix = sdf.format(cal.getTime());

		StringBuilder afp = new StringBuilder(filePath);
		afp.append(fileName).append(fileExtension).append(fileSuffix);

		this.absoluteFilePath = afp.toString();
	}

}
