package com.dati.net;

import java.io.IOException;

/**
* A simple, static class to display a URL in the system browser.
*
* Under Unix, the system browser is hard-coded to be 'netscape'.
* Netscape must be in your PATH for this to work.  This has been
* tested with the following platforms: AIX, HP-UX and Solaris.
*
* Under Windows, this will bring up the default browser under windows,
* usually either Netscape or Microsoft IE.  The default browser is
* determined by the OS.  This has been tested under Windows 95/98/NT.
*
* Examples:
* 
BrowserControl.displayURL("http://www.javaworld.com")
* 
BrowserControl.displayURL("file://c:\\docs\\index.html")
* 
BrowserContorl.displayURL("file:///user/joe/index.html");
* 
* Note - you must include the url type -- either "http://" or
* "file://".
*/

public class BrowserControl {
    
    // Used to identify the windows platform.
    private static final String WIN_ID = "Windows";
    // Used to identify the macintosh platform.
    private static final String MAC_ID = "Mac";
    // The default system browser under windows.
    private static final String WIN_PATH = "rundll32";
    // The flag to display a url.
    private static final String WIN_FLAG = "url.dll,FileProtocolHandler";
    // The flag to display a url in Mac.
    private static final String MAC_FLAG = "open";
    // The browsers under unix.
    private static final String[] UNIX_PATH = {
    	"firefox",
    	"/opt/firefox/firefox",
    	"firebird",
    	"/opt/firebird/firebird",
    	"swiftfox",
    	"/opt/swiftfox/swiftfox",
    	"mozilla",
    	"/opt/mozilla/mozilla",
    	"netscape",
    	"/opt/netscape/netscape",
    	"konqueror",
    	"galeon",
    	"opera",
    	"/opt/opera/opera"};
    // The flag to display a url.
    private static final String UNIX_FLAG = "-remote openURL";

    /**
     * Display a file in the system browser.  If you want to display a
     * file, you must include the absolute path name.
     *
     * @param url the file's url (the url must start with either "http://" or "file://").
     */
    public static void displayURL(String url)
    {
    	String cmd = null;
    	if (isWindowsPlatform()) {
    		// cmd = 'rundll32 url.dll,FileProtocolHandler http://...'
    		cmd = WIN_PATH + " " + WIN_FLAG + " " + url;
    		try {
				Runtime.getRuntime().exec(cmd);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
    	}
    	else if (isMacintosh()) {
    		cmd = MAC_FLAG + " " + url;
    		try {
				Runtime.getRuntime().exec(cmd);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
    	}
    	else
    	{
    		Process p = null;
    		int exitCode;
    		for (int i=0; i<UNIX_PATH.length; i++) {
    			exitCode = -1;
    			try {
    				cmd = UNIX_PATH[i] + " " + UNIX_FLAG + "(" + url + ")";
    				p = Runtime.getRuntime().exec(cmd);
    			}
    			catch(Exception e) {}
    			try {
    				// wait for exit code -- if it's 0, command worked,
    				// otherwise we need to start the browser up.
    				exitCode = p!=null?(p.waitFor()):-1;
    				if (exitCode != 0) {
    					// 2nd try...
    					cmd = UNIX_PATH[i] + " "  + url;
    					p = Runtime.getRuntime().exec(cmd);
    					exitCode = p!=null?(p.waitFor()):-1;
    					if (exitCode == 0) {
    						System.out.println("cmd="+cmd);
    						return;
    					}
    				}
    				else {
    					System.out.println("cmd="+cmd);
    					return;
    				}
    			}
    			catch(Exception e) {}
    		}
    	}
    	// couldn't exec browser
    	System.err.println("Could not invoke browser, command=" + cmd);
    }
    /**
     * Try to determine whether this application is running under Windows
     * or some other platform by examing the "os.name" property.
     *
     * @return true if this application is running under a Windows OS
     */
    public static boolean isWindowsPlatform()
    {
        String os = System.getProperty("os.name");
        if ( os != null && os.startsWith(WIN_ID))
            return true;
        else
            return false;

    }
    
    public static boolean isMacintosh()
    {
        String os = System.getProperty("os.name");
        if ( os != null && os.startsWith(MAC_ID))
            return true;
        else
            return false;

    }
}
