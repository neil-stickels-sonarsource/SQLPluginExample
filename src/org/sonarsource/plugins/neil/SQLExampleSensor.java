package org.sonarsource.plugins.neil;

import java.util.List;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonarsource.plugins.neil.SQLExampleChecker.SQLExampleResult;

/**
 * The Sensor implementation is what is used to determine which files are to be analyzed by this plugin.  In this case, 
 * I am just telling it to use any files the scanner finds that are files it considers plsql files.  Any of the files
 * found are passed to my Checker to determine if there are any issues in that file.  Then I will use this class to
 * actually create the issues in SonarQube for anything found by my Checker
 * @author neil.stickels
 *
 */
public class SQLExampleSensor implements Sensor {
	
	private static final Logger LOGGER = Loggers.get(SQLExampleSensor.class);
	private final SQLExampleChecker checker;
	
	public SQLExampleSensor(SQLExampleChecker checker)
	{
		this.checker = checker;
	}

	@Override
	public void describe(SensorDescriptor descriptor) {
		descriptor.name("Simple plugin to show SQL examples");
		descriptor.createIssuesForRuleRepositories(SQLExampleRulesDefinition.REPOSITORY);
	}

	/**
	 * The execute method is the method used by the plugin to determine what it actually does.  In this case, I am
	 * telling it to look for any files that Sonar has determined are plsql files (by default any .sql file) and then
	 * call my Checker with each file.  The Checker creates a list of results from that file, and then I create issues
	 * in SonarQube for each issue found, specifying which file, the line number, and the place in the line where the
	 * issue was found.  This is all used by the SonarQube UI to highlight where the issue is in the file when it shows it.
	 */
	@Override
	public void execute(SensorContext context) {
		FileSystem fs = context.fileSystem();
		Iterable<InputFile> sqlFiles = fs.inputFiles(fs.predicates().hasLanguage("plsql"));
		for(InputFile sqlFile : sqlFiles)
		{
			LOGGER.warn("*****SQLExamplePlugin found a sql file: "+sqlFile+" *******");
			List<SQLExampleResult> results = checker.call(sqlFile);
			for(SQLExampleResult result : results)
			{
				NewIssue newIssue = context.newIssue().forRule(SQLExampleRulesDefinition.SQL_EXAMPLE_RULE);
				TextRange newRange = sqlFile.newRange(result.lineNum, result.indexStart, result.lineNum, result.indexEnd);
				NewIssueLocation primaryLocation = newIssue.newLocation()
						.on(sqlFile)
						.at(newRange)
						.message("SELECT statements should use uppercase");
				newIssue.at(primaryLocation);
				newIssue.save();
			}
		}


	}
	


}
