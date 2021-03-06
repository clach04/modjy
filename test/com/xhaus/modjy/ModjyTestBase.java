package com.xhaus.modjy;

import java.io.File;

import java.util.Map;
import java.util.Iterator;

import junit.framework.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import com.mockrunner.servlet.BasicServletTestCaseAdapter;
import com.mockrunner.mock.web.WebMockObjectFactory;
import com.mockrunner.mock.web.MockServletConfig;
import com.mockrunner.mock.web.MockServletContext;
import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;

import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import com.xhaus.modjy.ModjyJServlet;

/**
 * 
 */

public class ModjyTestBase extends BasicServletTestCaseAdapter
{

	final	static	String	DEFAULT_APP_DIR  = "test_apps_dir";
	final	static	String	LIB_PYTHON_DIR  = "lib-python";
	final	static	String	LIB_PYTHON_TEST_PATH  = "lib_python_folder";
	final	static	String	DEFAULT_APP_FILE = "simple_app.py";
	final	static	String	DEFAULT_APP_NAME = "simple_app";

	public	WebMockObjectFactory	factory;
	public	MockServletConfig		servletConfig;
	public	MockServletContext		servletContext;

	public WebMockObjectFactory getFactory ()
	{
		if (factory == null)
			factory = getWebMockObjectFactory();
		return factory;
	}

	public MockServletConfig getConfig ()
	{
		if (servletConfig == null)
			servletConfig = getFactory().getMockServletConfig();
		return servletConfig;
	}

	public MockServletContext getContext ()
	{
		if (servletContext == null)
		{
			MockServletContext mctx = getFactory().getMockServletContext();
//			mctx.setContext("", new LoggingMockServletContext("mock_context.log"));
//			servletContext = (MockServletContext)mctx.getContext("");
			return mctx;
		}
		return servletContext;
	}

//	public void dumpContextRealPaths ( )
//	{
//		Map pathMap = ((LoggingMockServletContext)getContext()).actualPaths;
//		Iterator it = pathMap.keySet().iterator();
//		while (it.hasNext())
//		{
//			String pathName = (String) it.next();
//			System.out.println("Path '"+pathName+"'-->'"+pathMap.get(pathName)+"'");
//		}
//	}

	public void setInitParameter ( String name, String value )
	{
		getConfig().setInitParameter(name, value);
	}

	public void setRealPath ( String source, String target )
	{
		getContext().setRealPath(source, target);
	}

	public void setBodyContent(String content)
	{
        MockHttpServletRequest request = (MockHttpServletRequest) getFactory().getWrappedRequest();
        request.setBodyContent(content);
        getFactory().addRequestWrapper(request);
	}

	public void setServletContextPath(String path)
	{
        MockHttpServletRequest request = (MockHttpServletRequest) getFactory().getWrappedRequest();
        request.setContextPath(path);
        getFactory().addRequestWrapper(request);
	}

	public void setServletPath(String path)
	{
        MockHttpServletRequest request = (MockHttpServletRequest) getFactory().getWrappedRequest();
        request.setServletPath(path);
        getFactory().addRequestWrapper(request);
	}

	public void setRequestURI(String uri)
	{
        MockHttpServletRequest request = (MockHttpServletRequest) getFactory().getWrappedRequest();
        request.setRequestURI(uri);
        getFactory().addRequestWrapper(request);
	}

	public void setScheme(String scheme)
	{
        MockHttpServletRequest request = (MockHttpServletRequest) getFactory().getWrappedRequest();
        request.setScheme(scheme);
        getFactory().addRequestWrapper(request);
	}

	public void setPathInfo(String pathInfo)
	{
        MockHttpServletRequest request = (MockHttpServletRequest) getFactory().getWrappedRequest();
        request.setPathInfo(pathInfo);
        getFactory().addRequestWrapper(request);
	}

	public void setQueryString(String qString)
	{
        MockHttpServletRequest request = (MockHttpServletRequest) getFactory().getWrappedRequest();
        request.setQueryString(qString);
        getFactory().addRequestWrapper(request);
	}

	public void setProtocol(String protocol)
	{
        MockHttpServletRequest request = (MockHttpServletRequest) getFactory().getWrappedRequest();
        request.setProtocol(protocol);
        getFactory().addRequestWrapper(request);
	}

	public void setServerName(String name)
	{
        MockHttpServletRequest request = (MockHttpServletRequest) getFactory().getWrappedRequest();
        // Using setLocalName() here: See here for more: http://docs.sun.com/source/819-0077/J2EE.html
        request.setLocalName(name);
        getFactory().addRequestWrapper(request);
	}

	public void setServerPort(int port)
	{
        MockHttpServletRequest request = (MockHttpServletRequest) getFactory().getWrappedRequest();
        request.setLocalPort(port);
        getFactory().addRequestWrapper(request);
	}

	public void setAppDir(String app_dir)
	{
		setInitParameter("app_directory", app_dir);
	}

	public void setAppFile(String app_file)
	{
		setInitParameter("app_filename", app_file);
	}

	public void setAppName(String app_name)
	{
		setInitParameter("app_callable_name", app_name);
	}

	public void setAppImportable(String app_path)
	{
		setInitParameter("app_import_name", app_path);
	}

	public MockHttpServletResponse getResponse()
	{
        MockHttpServletResponse response = (MockHttpServletResponse) getFactory().getWrappedResponse();
        return response;
	}

	public int getStatus()
	{
        MockHttpServletResponse response = (MockHttpServletResponse) getFactory().getWrappedResponse();
        return response.getStatusCode();
	}

    protected void baseSetUp()
    	throws Exception
	{
		super.setUp();
		setRealPath("/WEB-INF/"+LIB_PYTHON_DIR, LIB_PYTHON_TEST_PATH);
		setRealPath("/WEB-INF/lib/modjy.jar", "../modjy.jar");
		setAppDir(DEFAULT_APP_DIR);
		setAppFile(DEFAULT_APP_FILE);
		setAppName(DEFAULT_APP_NAME);
		setInitParameter("exc_handler", "testing");
//		dumpContextRealPaths();
	}

	protected PyObject evalPythonString(String pyString)
	{
		// Efficiency be damned: it's a testing phase
		PythonInterpreter interp = new PythonInterpreter();
		return interp.eval(pyString);
	}

	protected void createServlet()
	{
        createServlet(ModjyJServlet.class);
        // Set zero content: this can be overridden later
        setBodyContent("");
        clearOutput();
	}

    // Leave this here as a simple template for a test
    public void testHelloWorld() throws Exception
	{
		baseSetUp();
		createServlet();
	 	doGet();
		String result = new XMLOutputter().outputString(getOutputAsJDOMDocument());
	}

	public static void main(String args[])
	{
		TestSuite suite = new TestSuite();
		suite.addTestSuite(ModjyTestBase.class);
		suite.addTestSuite(ModjyTestAppInvocation.class);
		suite.addTestSuite(ModjyTestEnviron.class);
		suite.addTestSuite(ModjyTestHeaders.class);
		suite.addTestSuite(ModjyTestContentHeaders.class);
		suite.addTestSuite(ModjyTestReturnIterable.class);
		suite.addTestSuite(ModjyTestWebInf.class);
		suite.addTestSuite(ModjyTestWSGIStreams.class);
		junit.textui.TestRunner.run(suite);
	}

}
