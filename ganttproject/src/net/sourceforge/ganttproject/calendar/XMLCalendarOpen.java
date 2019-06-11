->RefactoringNumber->90953<-PullUpMethod(net.sourceforge.ganttproject.calendar.XMLCalendarOpen.MyException;java.lang.Exception;[];[load])
<-endRefactoring marker->
/**
 * 
 */
package net.sourceforge.ganttproject.calendar;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sourceforge.ganttproject.parser.FileFormatException;
import net.sourceforge.ganttproject.parser.ParsingListener;
import net.sourceforge.ganttproject.parser.TagHandler;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author nbohn
 */
public class XMLCalendarOpen {
	public static class MyException extends Exception {
		MyException(Throwable cause) {
			super(cause);
		}
	}
    //private File myCalendarFiles[];

    private List<URL> myCalendarResources = new ArrayList<URL>();
    private String myCalendarLabels[];

    /** The main frame */
    private ArrayList<TagHandler> myTagHandlers = new ArrayList<TagHandler>();

    private ArrayList<ParsingListener> myListeners = new ArrayList<ParsingListener>();

    boolean load(InputStream inputStream) throws MyException {
        // Use an instance of ourselves as the SAX event handler
        DefaultHandler handler = new GanttXMLParser();

        // Use the default (non-validating) parser
        SAXParserFactory factory = SAXParserFactory.newInstance();
        
		try {
			SAXParser saxParser = factory.newSAXParser();
	        saxParser.parse(inputStream, handler);
		} catch (ParserConfigurationException e) {
			throw new MyException(e);
		} catch (SAXException e) {
			throw new MyException(e);
		} catch (IOException e) {
			throw new MyException(e);
		}
        return true;
    }

    void addTagHandler(TagHandler handler) {
        myTagHandlers.add(handler);
    }

    void addParsingListener(ParsingListener listener) {
        myListeners.add(listener);
    }

    private TagHandler getDefaultTagHandler() {
        return new DefaultTagHandler();
    }

    private class DefaultTagHandler implements TagHandler {
        private String name;

        public void startElement(String namespaceURI, String sName,
                String qName, Attributes attrs) {
            String eName = qName; // element name
            if (attrs != null) {
                for (int i = 0; i < attrs.getLength(); i++) {
                    String aName = attrs.getLocalName(i); // Attr name
                    if ("".equals(aName)) {
                        aName = attrs.getQName(i);

                        // The project part
                    }
                    if (eName.equals("calendar")) {
                        if (aName.equals("name")) {
                            name = attrs.getValue(i);
                        } else if (aName.equals("type")) {
                        }
                    }
                }
            }
        }

        public void endElement(String namespaceURI, String sName, String qName) {
        }

        public String getName() {
            return name;
        }

    }

    private class GanttXMLParser extends DefaultHandler {

        // ===========================================================
        // SAX DocumentHandler methods
        // ===========================================================

        public void endDocument() throws SAXException {
            for (int i = 0; i < myListeners.size(); i++) {
                ParsingListener l = myListeners.get(i);
                l.parsingFinished();
            }
        }

        public void startElement(String namespaceURI, String sName, // simple
                // name
                String qName, // qualified name
                Attributes attrs) throws SAXException {
            for (Iterator<TagHandler> handlers = myTagHandlers.iterator(); handlers
                    .hasNext();) {
                TagHandler next = handlers.next();
                try {
                    next.startElement(namespaceURI, sName, qName, attrs);
                } catch (FileFormatException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void endElement(String namespaceURI, String sName, String qName)
                throws SAXException {
            for (Iterator<TagHandler> handlers = myTagHandlers.iterator(); handlers
                    .hasNext();) {
                TagHandler next = handlers.next();
                next.endElement(namespaceURI, sName, qName);
            }
        }
    }

    public void setCalendars() throws MyException  {
        myCalendarResources.clear();
        DefaultTagHandler th = (DefaultTagHandler) getDefaultTagHandler();
        addTagHandler(th);
        IConfigurationElement[] calendarExtensions = Platform.getExtensionRegistry().getConfigurationElementsFor(GPCalendar.EXTENSION_POINT_ID);
        myCalendarLabels = new String[calendarExtensions.length];
        for (int i = 0; i < calendarExtensions.length; i++) {
            Bundle nextBundle = Platform.getBundle(calendarExtensions[i].getDeclaringExtension().getNamespaceIdentifier());
            URL calendarUrl = nextBundle.getResource(calendarExtensions[i].getAttribute("resource-url"));
            if (calendarUrl != null) {
                try {
					load(calendarUrl.openStream());
				} catch (IOException e) {
					throw new MyException(e);
				}
                myCalendarLabels[i] = th.getName();
                myCalendarResources.add(calendarUrl);
            }
        }
    }

    public URL[] getCalendarResources() {
        return myCalendarResources.toArray(new URL[0]);
    }

    public String[] getLabels() {
        return myCalendarLabels;
    }
}