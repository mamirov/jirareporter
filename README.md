Plug-in is designed for sending build results from TeamCity in JIRA ticket and it is also possible to move the issue through your workflow. Report are sent as comments to the issue.

[Download plugin (6.1 Mb)](https://bitbucket.org/mamirov/jirareporter/downloads/jirareporter.zip)
[Source code](https://github.com/mamirov/jirareporter)
[Release notes](https://github.com/mamirov/jirareporter/wiki/Release-Notes)

## The advantages

* Sending TeamCity build results from any VCS branch;
* Moving the issues through workflow, depending on the results of TeamCity builds;
* Sending report of TeamCity build on several issues at the same time.

## Settings

1. Install plug-in in [ < TeamCity Data Directory >/plugins](http://confluence.jetbrains.com/display/TCD7/TeamCity+Data+Directory) and restart TeamCity server;
2. Add Build Step JIRA Reporter to your configuration. 
** Important!** In order to create an account Build Step must be the last step in hierarchy;

3. Plug-in parameters:

![pluginParams](http://gyazo.com/77caaea9ec007db89525b35f0ca7cea5.png)


**Get issue from:**

Field, determining the method of getting id issue:

_JIRA Reporter_ - connected id of the issue is indicated in the plug-in settings;

_VCS Comment_ - issue id is taken from vcs commit, for that it’s needed to set up integration of TeamCity, Administration -> Issue Tracker -> Create new connection.

Example:

```
git commit -m "fix for EXAMPLE-123"
```


**Enable issue progressing:**

Movement of the issue through the workflow turns on depending on test results;

**Enter your JIRA workflow for issue progressing:**

Appears when turning on Enable issue progressing; 

Field to insert the requirements for issue movement through workflow.

Format for inserting requirements: 

```
SUCCESS:In Progress-Resolve Issue,Closed-Deploy,In Testing-Close Issue;
FAILURE:In Progress-Reopen Issue,In Testing-Reopen Issue,Closed-Reopen Issue;
```

**Enable SSL connection:**

Needs to be turned on if JIRA server uses SSL connection.

## Example of JIRA result output:

![commentJira](http://gyazo.com/9b09457cf79370ee743bc7aba1e02015.png)

## Template for JIRA comment

Activate checkbox for editing template, and write yourself template of JIRA comment.

Also you have defined parameters:
```
*status.build*, *build.type.name*, *tests.results*, *teamcity.server.url*, *build.id*, *build.type*.
```

Example:
```
*build.type.name* : *status.build* 
*test.results* 
Template example.
```
