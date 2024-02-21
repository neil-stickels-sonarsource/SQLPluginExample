package org.sonarsource.plugins.neil;

import org.sonar.api.Plugin;

/**
 * Plugin is the primary interfact that all Sonar plugins must use to register the plugin with SonarQube at startup
 * @author neil.stickels
 *
 */
public class SQLExamplePlugin implements Plugin {

	/**
	 * The define method is what the Plugin interface uses to register the plugin with SonarQube.  In this case, I am
	 * adding in 3 classes:
	 * - a Checker class, which is what actually does the analysis on a specific file
	 * - a Sensor class, which is used by the Scanner to decide which files to check
	 * - a RulesDefinition class, which is where the Rules this plugin is adding are put
	 */
    @Override
    public void define(Context context)
    {
      context.addExtensions(SQLExampleChecker.class, SQLExampleSensor.class, SQLExampleRulesDefinition.class);
    }
    
}
