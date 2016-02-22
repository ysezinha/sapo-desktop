# Introduction #

SAPO was developed mainly in Netbeans. But if you would like to work exclusively with Eclipse, this is a quick tutorial for that.


# Details #

I assume you already have the latest "Eclipse IDE for Java Developers" or equivalent installed (http://www.eclipse.org/downloads/).

First, install (from inside Eclipse) the Subversive plug-in (http://www.eclipse.org/subversive/installation-instructions.php).

Import source and libraries from the repository: use File -> Import -> Project from SVN -> URL: http://sapo-desktop.googlecode.com/svn/sapo_desktop

No need to authenticate. Check out as a project configured using the New Project Wizard (then select Java -> Java Project). In the dialog wizard, choose a project name and select “configure separate folders for source  and class files”, then click finish. When asked for the secure storage password, simply cancel. Wait project download.

In Package Explorer, expand the project contents and then expand the lib folder. Select all contents in this folder (selecting the first an then the last one using shift key, for instance). Click inside this selection with the second mouse button and choose from the context menu Build Path -> Add to Build Path.

In order to run the program from within Eclipse, select the file StartSAPO.java (in default package) and in the context menu select Run As -> Java Application.