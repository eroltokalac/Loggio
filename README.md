# Loggio
Log Management Component

Java Bases Log Framework with Elastic Search Distributed Architecture &amp; MongoDB Map Reduce Based Recommendation Logging

# Architecture
![loggio](https://cloud.githubusercontent.com/assets/9517583/7901973/dadf6f32-07ab-11e5-8323-e0e53b21131d.jpg)

# Architecture
- Elastic Search
- MongoDB & Map Reduce

# Deployment Instructions
To quickly use the Loggio, you must follow this procedure in your IDE - Development environment. 
In this documentation, Eclipse IDE is used as an example.
Two possibilities: Dependence maven or classic project
Add your dependence POM (Maven)
<dependency>
   <groupId>org.tokalac.logger</groupId>
   <artifactId>loggio</artifactId>
   <version>${last-version}</version>
</dependency>
Substitute ${last-version} the last release proposed by auto completion maven or so present at: https://github.com/eroltokalac/loggio/releases
Download the latest release (other than Project java maven)
Releases: https://github.com/eroltokalac/loggio/releases
Collect only two files loggio-Xxjar and loggio.properties

# Prerequisites in your project
On the name of your Java project, right click> Build Path> Add External Archives.
Choose library loggio-Xxjar on your computer
Copy the configuration file loggio.properties to the root of your project at the same level as the directories bin and src by having individually beforehand (All instructions are in the file).


# Usage

Example of use
By default loggio IDE written in the console and in a log file.
To instantiate a logger:
// Initiate a context logging and support configurations  
public static final LogManager logmanager =  new LogManager(); 
// To instantiate a logger identified by the canonical name of the calling class
Logger log = logmanager.getLogger();
// To instantiate a logger identifed by canonical name MyClass.class  
Logger MyClassLogger  = logmanager.getLogger(MyClass.class); 
// To instantiate a logger declared in the configuration file where 'acme is the identifier of the logger. If the logger is customized in the configuration file, it will be loaded with its configuration
Logger CustomLogger  = logmanager.getLogger("acme" ); 
No matter how a logger was created; loaded with its configuration file or simply instantiate loggio.properties, you can override the setting by programming. Eg for logger "logger" instanced above can be:
// To add the console to target logger  
logger.addHandler (new ConsoleHandler()); 
// File to add the target but also to log out, the path of the log file and size to be determined by rotating the default configuration  
logger.addHandler(new FileHandler()); 
// To add the file to the target logger, specifying the path to the file log and size in KB of each file (0 means infinite)  
log.addHandler (new FileHandler ( " /var/log/loggio/05302015.log " , 5 )); 
// Add a format to log  
logger.setFormater(new DefaultFormater()); 
// Specify the information level log(TRACE> ERROR> DEBUG> WARN> INFO)
logger.setLevel(Level.INFO); 
If specific customization has been defined for a logger (Through the configuration file or programmatically) are the values in the "Framework Default configuration" section of the configuration file that will be used for this logger.
Note that you can create your own formatters’ messages and your own targets without needing the source code loggio: For Target, simply inherit the AbstractHandler class and for the trainer it is just enough to inherit your new trainer of AbstractFormatter class. Once completed inheritance, implement different methods imposed and set your loggers with your new classes.
// INFO log level  
logger.info ("Connection to mongoDB successful!");
Your message will be logged according to your configurations
The current release of the loggio allows to write all the logs to a text file locally, to console, to elastic search distributed server and for the user activity logs, to mongoDB with map reduce.
Text file properties
The configuration is stored in a text file loggio.properties with key / value pairs.
Example parameter
//Enable log file  
FileHandler = true

________________________________________
#Default Configuration

//###############################################################
//Framework Default configuration                               #
//Do not delete this section, but you can modify it!            #
//it is loggio's reference when user omit some parameters       #
//###############################################################
//Global levels activation
AppName=loggioTest
LevelINFO=true
LevelDEBUG=true
LevelWARN=true
LevelERROR=true
LevelTrace=true
LevelRECOMMENDATION=true
//Default output Handlers
//File Handler:
FileHandler=false
LogFilePath=/var/logs/loggio.log
//O means Infinite, other forced the limit for rollback (in Ko)
LogFileSize=0
//Console Handler
ConsoleHandler=true
//Elastic Search Handler
ESHandler=true
ESSAddress=localhost
ESSPort=9300
//Recommendation Engine Handler
REHandler=true
REDBAddress=localhost
REPort=27017
REDBPath=logs
//Default Formater Configuration
ShowLEVEL=true
ShowDATE=true
ShowFQCN=true
ShowProcess=true
ShowAppName=true
ShowUserId=true
ShowSessionId=true
ShowCategoryId=true
ShowPrice=true
ShowCartId=true
ShowQuantity=true
ShowOnSale=true
ShowProductId=true
Separator=|
//##############################################################
//END of framework Default Configuration                       #
//##############################################################

//##############################################################
//Logger specific configuration                                #
//You can add an unlimited logger specific configuration here  #
//Note that, if you omit a parameter for a logger, the default #
//value for that parameter will be use.                        #
//Loggers configured here overwrite java configuration         #
//##############################################################
//The first line in this section list all available loggers
//loggers=logger1, logger2, ....

//You can configured you declared loggers on the first line by this model
//logger1.Name=com.foo
//logger1.Level=TRACE
//logger1.Handlers=com.handlers.handler1,com.handlers.handler2, ...
//logger1.LogFilePath=my/log/file/path
//logger1.LogFileSize=2048           #0 means infinite
//loggio.ESSAddress=localhost:9300
//loggio.REDBAddress:localhost:27017
//loggio.REDBPath:log
//logger1.Formatter=com.formatter.Myformatter

//Example
loggers=loggio, acme
//Example of a fully configured logger
loggio.Name=com.loggio
loggio.AppName=loggioTest
loggio.Level=DEBUG
loggio.Handlers=org.tokalac.logging.modules.output.handler.ConsoleHandler, org.tokalac.logging.modules.output.handler.ESHandler, org.tokalac.logging.modules.output.handler.REHandler
loggio.ESSAddress=localhost
loggio.ESSPort:9300
loggio.REDBAddress:localhost
loggio.REDBPath:logs
loggio.REPort:27017
loggio.Formatter= org.tokalac.logging.modules.output.formatter.DefaultFormatter

//Example of only declared logger, this logger will use default config or java Config
acme.Name=com.acme.test
acme.AppName=acmeTest

 ________________________________________

#Configure your Java code
Directly in your Java project, you can set several parameters.

LogManager logMgtA =  new  LogManager ();  
Logger LogA = logTMgtA.getLogger ();

With the object LogA it is possible to change a number of parameters: levels, display, export.
Restrict the log level
To choose the level of monitoring messages (log)
LogA.setLevel ( Level.INFO ); // all levels choice (ALL OFF ...)
Selecting the output method (export)
It is possible to define which handler to use.
// output to a file with rotation every 4 KB
LogA.addHandler ( new  FileHandler ( " /Users/Tokalac/logsTestA/log.txt " , 4 ));  
Details message display
The message type consists of: date / time Class (FQCN), and level.
You can choose which post (s) information (s) you want to keep for tracking. For that you need to create a class dedicated to your format.
Important: If you redefine a method is___Enable (display / info masking) you must redefine them all: they must return true or false.
Import  java.awt.List; 

// Class name to choose who inherits AbstractFormater  
import org.tokalac.logging.modules.output.formater.AbstractFormater; 

public  class newFormatterA extends  AbstractFormater {      

	@Override      
	public  List < String >  getOthers () {          
		return  super.getOthers ();     
	}      

	@Override      
	public  String  getSeparator () {          
		return  ":" ;     
	}      

	@Override      
	public  boolean  isDateEnabled () {          
		return  true ;     
	}      

	@Override      
	public  boolean  isFQCNEnabled () {          
		return  true ;     
	}      

	@Override      
	public  boolean  isLevelEnabled () {          
		return  true ;     
	}      

	@Override      
	public  boolean  isMessageEnabled () {          
		return  true ;     
	} 
}
Then set in your code when using your customized format!
LogA.setFormatter ( new  newFormatterA());
 ________________________________________
#Details on rotating log files
The Loggio manages the rotation of files by size. Each use of the library, the size of the log file is verified and all previous messages are moved to a new file with suffix _1.log (Java_1.log example). Then the number of the next generated file is incremented.

Example log files:
// JavaProject/logs/  
30  May  18 : 28  loggio.log  
30  May  18 : 17  loggio_1.log  
30  May  18 : 18  loggio_2.log  
30  May  18 : 20  loggio_3.log


