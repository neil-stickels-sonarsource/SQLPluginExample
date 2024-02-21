Example of a very simple SQL plugin.  The SQL plugin is just a text scanner that can scan the text 
of a file and create rules to check based on the text in the file.  This simple SQL plugin will
create a rule to look for "select" statements in all lowercase, and create a code smell issue
on each instance saying that they should use SELECT instead.

To build the plugin, you can use Maven, and just run "mvn verify".  This will create a jar file 
in the target directory.  This jar file needs to be added to your
<SONARQUBE_INSTALL_BASE>/extensions/plugins directory, and SonarQube restarted to have 
SonarQube make use of the plugin.  Any rules implemented will need to be added to the
Quality Profile for the language type (in this case PL/SQL) in the plugin in order
to actually create issues, or else SonarQube will just ignore your plugin during
analysis since the rules your plugin is initializing isn't active.
