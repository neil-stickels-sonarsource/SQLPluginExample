package org.sonarsource.plugins.neil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.scanner.ScannerSide;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

/**
 * The Checker is what is actually checking to see if a file violates the rules you are creating.  
 * @author neil.stickels
 *
 */
@ScannerSide
public class SQLExampleChecker {
	
	private static final Logger LOGGER = Loggers.get(SQLExampleChecker.class);
	
	/**
	 * Each PLSQL file found by the Sensor will get passed in to this file.  This is where you implement your custom
	 * rules and determine if that file violated your rule.
	 * @param file the SQL file we are going to run the rules on
	 * @return a list of places in the file where violations of this rule exist
	 */
	public List<SQLExampleResult> call(InputFile file)
	{
		InputStream in = null;
		BufferedReader br = null;
		try
		{
			List<SQLExampleResult> results = new ArrayList<>();
			in = file.inputStream();
			br = new BufferedReader(new InputStreamReader(in));
			String line;
			int counter = 0;

			/*
			 * This while loop is really the heart of the logic for this checker.  This is where I am iterating 
			 * line by line through the file to look for any violations of the rule that I made, and if one is found
			 * create something (in this case a SQLExampleResult) to keep the information on this rule violation
			 * so that an issue can be made by the Sensor
			 */
			while((line = br.readLine()) != null)
			{
				counter++;
				if(line.contains("select"))
				{
					int startIndex = line.indexOf("select");
					int endIndex = startIndex+6;
					SQLExampleResult result = new SQLExampleResult(line,counter,startIndex,endIndex);
					results.add(result);
				}
			}
			return results;
		} catch (IOException ex)
		{
			ex.printStackTrace();
		} finally
		{
			try {
				if(br != null)
					br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(in != null)
					in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new ArrayList<>() ;
	}
	
	class SQLExampleResult
	{
		String line;
		int lineNum;
		int indexStart;
		int indexEnd;
		
		public SQLExampleResult()
		{
			
		}
		
		public SQLExampleResult(String line, int lineNum, int indexStart, int indexEnd)
		{
			this.line = line;
			this.lineNum = lineNum;
			this.indexStart = indexStart;
			this.indexEnd = indexEnd;
		}

		public String getLine() {
			return line;
		}

		public void setLine(String line) {
			this.line = line;
		}

		public int getLineNum() {
			return lineNum;
		}

		public void setLineNum(int lineNum) {
			this.lineNum = lineNum;
		}

		public int getIndexStart() {
			return indexStart;
		}

		public void setIndexStart(int indexStart) {
			this.indexStart = indexStart;
		}

		public int getIndexEnd() {
			return indexEnd;
		}

		public void setIndexEnd(int indexEnd) {
			this.indexEnd = indexEnd;
		}
		
		
	}

}
