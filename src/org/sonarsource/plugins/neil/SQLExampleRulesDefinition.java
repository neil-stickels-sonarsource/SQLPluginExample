package org.sonarsource.plugins.neil;

import static org.sonar.api.server.rule.RuleDescriptionSection.RuleDescriptionSectionKeys.HOW_TO_FIX_SECTION_KEY;
import static org.sonar.api.server.rule.RuleDescriptionSection.RuleDescriptionSectionKeys.INTRODUCTION_SECTION_KEY;
import static org.sonar.api.server.rule.RuleDescriptionSection.RuleDescriptionSectionKeys.ROOT_CAUSE_SECTION_KEY;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleDescriptionSection;
import org.sonar.api.server.rule.RulesDefinition;

/**
 * In your RulesDefinition implementation, you will define all of the rules your plugin adds.  In this case, I am
 * creating a single new rule in the PL/SQL repository of rules.  
 * @author neil.stickels
 *
 */
public class SQLExampleRulesDefinition implements RulesDefinition {

	public static final String REPOSITORY = "PL/SQL";
	public static final RuleKey SQL_EXAMPLE_RULE = RuleKey.of(REPOSITORY, "SQLPluginExample issue identified");

	/**
	 * The define method is where you define the rules you are adding with this plugin.  As mentioned, I am adding a single
	 * rule to the PL/SQL repository.  All of the different addDescriptionSection methods here are just to make the 
	 * tabs in SonarQube when you look at the rule or at issues created by this rule and what to show on those tabs.
	 * The most important part of this is the tags if any you want to add to this rule, the setType to the Type of rule, 
	 * i.e., VULNERABILITY, BUG, or CODE_SMELL, and the setSeverity where you define the default severity of any issues
	 * created by this rule. 
	 */
	@Override
	public void define(Context context) {
		NewRepository repository = context.createRepository(REPOSITORY, "plsql").setName("SQLPluginExample Analyzer");
		
		NewRule rule = repository.createRule(SQL_EXAMPLE_RULE.rule()).setName("SQLPluginExample Rule")
				.setHtmlDescription("The key word SELECT should always be upper case")
				.addDescriptionSection(createDescriptionSection(INTRODUCTION_SECTION_KEY, "The key word SELECT should always be upper case", null))
				.addDescriptionSection(createDescriptionSection(ROOT_CAUSE_SECTION_KEY, "For ease of readability, the key word SELECT should always be uppercase", null))
				.addDescriptionSection(createDescriptionSection(HOW_TO_FIX_SECTION_KEY, "Change the usage of your select to be SELECT", null))
				.setTags("sql-plugin-example")
				.setType(RuleType.CODE_SMELL)
				.setType(RuleType.)
				.setSeverity(Severity.INFO);
		rule.setDebtRemediationFunction(rule.debtRemediationFunctions().linearWithOffset("5min","5min"));
		
		repository.done();
				
	}
	
	private static RuleDescriptionSection createDescriptionSection(String sectionKey, String htmlContent, org.sonar.api.server.rule.Context context)
	{
		return RuleDescriptionSection.builder().sectionKey(sectionKey).htmlContent(htmlContent).context(context).build();
	}

}
