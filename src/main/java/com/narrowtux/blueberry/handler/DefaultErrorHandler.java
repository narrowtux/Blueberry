package com.narrowtux.blueberry.handler;

import java.io.IOException;

import com.narrowtux.blueberry.http.HttpExchange;

public class DefaultErrorHandler extends ErrorHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		exchange.sendResponseHeaders(getCurrentCode(), 0);
		exchange.echo("<html><head><title>"+getCurrentCode().getCode()+" "+getCurrentCode().getTitle()+"</title></head>");
		exchange.echo("<body><h1>"+getCurrentCode().getCode()+" "+getCurrentCode().getTitle()+"</h1><p>That's an error.</p>");
		if (getThrowed() != null) {
			if (exchange.getWebServer().isDebug()) {
				exchange.echo("<h2>Stack trace:</h2><pre>");
				StackTraceElement[] elements = getThrowed().getStackTrace();
				exchange.echo(getThrowed().getClass().getName()+ ": "+getThrowed().getMessage()+"\n");
				for (StackTraceElement element:elements) {
					exchange.echo("    " + element.getClassName()+"."+element.getMethodName() + "() in " + element.getFileName() +":"+element.getLineNumber()+"\n");
				}
				exchange.echo("</pre>");
			} else {
				exchange.echo("<h2>Error message:</h2><pre>"+getThrowed().getMessage()+"</pre>");
			}
		}
		exchange.echo("<hr/><p>");
		exchange.echo(exchange.getWebServer().getApplicationName() + " using <a href='http://narrowtux.com/projects/blueberry'>Blueberry</a> version {$version} on "+System.getProperty("os.name") + " version "+System.getProperty("os.version"));
		exchange.echo("</p>");
		exchange.echo("</body></html>");
		exchange.close();
	}
}
