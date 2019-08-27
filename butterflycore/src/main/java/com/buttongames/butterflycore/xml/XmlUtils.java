package com.buttongames.butterflycore.xml;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Class with helper methods for manipulating XML files.
 * @author skogaby (skogabyskogaby@gmail.com)
 */
public class XmlUtils {

    private static final XPath XPATH = XPathFactory.newInstance().newXPath();

    /**
     * First bytes of a plaintext XML response, so we know if an array needs to be converted
     * to/from binary XML.
     */
    private static final byte[] XML_PREFIX = "<?xml".getBytes();

    /**
     * Says whether or not the input is binary XML.
     * @param input
     * @return
     */
    public static boolean isBinaryXML(final byte[] input) {
        boolean isBinary = false;

        for (int i = 0; i < XML_PREFIX.length; i++) {
            if (input[i] != XML_PREFIX[i]) {
                isBinary = true;
                break;
            }
        }

        return isBinary;
    }

    /**
     * Scrubs empty nodes from a document so we don't accidentally read them.
     * @param node The root node of the document to clean.
     */
    public static void clean(final Node node) {
        final NodeList childrem = node.getChildNodes();

        for (int n = childrem.getLength() - 1; n >= 0; n--) {
            final Node child = childrem.item(n);
            final short nodeType = child.getNodeType();

            if (nodeType == Node.ELEMENT_NODE) {
                clean(child);
            } else if (nodeType == Node.TEXT_NODE) {
                final String trimmedNodeVal = child.getNodeValue().trim();

                if (trimmedNodeVal.length() == 0) {
                    node.removeChild(child);
                } else {
                    child.setNodeValue(trimmedNodeVal);
                }
            } else if (nodeType == Node.COMMENT_NODE) {
                node.removeChild(child);
            }
        }
    }

    /**
     * Reads the given byte[] into an Element that represents the root node of the XML body.
     * @param body
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static Element byteArrayToXmlFile(final byte[] body) {
        try {
            final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = builderFactory.newDocumentBuilder();
            final Document reqDocument = builder.parse(new ByteArrayInputStream(body));
            XmlUtils.clean(reqDocument);

            return reqDocument.getDocumentElement();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads the given string into an Element that represents the root node of the XML body.
     * @param body
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static Element stringToXmlFile(String body) {
        try {
            if(!body.contains("xml version")){
                body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+body;
            }
            final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = builderFactory.newDocumentBuilder();
            final Document reqDocument = builder.parse(new ByteArrayInputStream(body.getBytes()));
            XmlUtils.clean(reqDocument);

            return reqDocument.getDocumentElement();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads the String value at the given XPath expression from the given document.
     * @param doc
     * @param path
     * @return
     */
    public static String strAtPath(final Element doc, final String path) {
        try {
            return (String) XPATH.compile("/" + path).evaluate(doc, XPathConstants.STRING);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Reads the boolean value at the given XPath expression from the given document.
     * @param doc
     * @param path
     * @return
     */
    public static Boolean boolAtPath(final Element doc, final String path) {
        try {
            final String val = (String) XPATH.compile("/" + path).evaluate(doc, XPathConstants.STRING);

            return val.equals("1");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads the double value at the given XPath expression from the given document.
     * @param doc
     * @param path
     * @return
     */
    public static Double doubleAtPath(final Element doc, final String path) {
        try {
            return (Double) XPATH.compile("/" + path).evaluate(doc, XPathConstants.NUMBER);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads the long value at the given XPath expression from the given document.
     * @param doc
     * @param path
     * @return
     */
    public static Long longAtPath(final Element doc, final String path) {
        try {
            return Long.parseLong((String) XPATH.compile("/" + path).evaluate(doc, XPathConstants.STRING));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads the integer value at the given XPath expression from the given document.
     * @param doc
     * @param path
     * @return
     */
    public static Integer intAtPath(final Element doc, final String path) {
        try {
            return ((Double) XPATH.compile("/" + path).evaluate(doc, XPathConstants.NUMBER)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads the node at the given XPath expression from the given document.
     * @param doc
     * @param path
     * @return
     */
    public static Node nodeAtPath(final Element doc, final String path) {
        try {
            return (Node) XPATH.compile("/" + path).evaluate(doc, XPathConstants.NODE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads the nodes at the given XPath expression from the given document.
     * @param doc
     * @param path
     * @return
     */
    public static NodeList nodesAtPath(final Element doc, final String path) {
        try {
            return (NodeList) XPATH.compile("/" + path).evaluate(doc, XPathConstants.NODESET);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the string value of a child element of the given document whose name is the given name
     * @param doc The document to search its children
     * @param name The name of the element to return
     * @return The value at the given element
     */
    public static String strAtChild(final Element doc, final String name) {
        try{
            return doc.getElementsByTagName(name).item(0).getTextContent();
        }catch (Exception e){
            return null;
        }

    }

    /**
     * Returns the int value of a child element of the given document whose name is the given name
     * @param doc The document to search its children
     * @param name The name of the element to return
     * @return The value at the given element
     */
    public static Integer intAtChild(final Element doc, final String name) {
        try{
            return Integer.parseInt(doc.getElementsByTagName(name).item(0).getTextContent());
        }catch (Exception e){
            return null;
        }
    }

    public static Integer intAtChild(final Element doc, final String name, final int defaultValue) {
        try{
            return Integer.parseInt(doc.getElementsByTagName(name).item(0).getTextContent());
        }catch (Exception e){
            return defaultValue;
        }
    }

    /**
     * Returns the long value of a child element of the given document whose name is the given name
     * @param doc The document to search its children
     * @param name The name of the element to return
     * @return The value at the given element
     */
    public static Long longAtChild(final Element doc, final String name) {
        try{
            return Long.parseLong(doc.getElementsByTagName(name).item(0).getTextContent());
        }catch (Exception e){
            return null;
        }

    }

    /**
     * Returns the double value of a child element of the given document whose name is the given name
     * @param doc The document to search its children
     * @param name The name of the element to return
     * @return The value at the given element
     */
    public static Double doubleAtChild(final Element doc, final String name) {
        try{
            return Double.parseDouble(doc.getElementsByTagName(name).item(0).getTextContent());
        }catch (Exception e){
            return null;
        }

    }

    /**
     * Returns the bool value of a child element of the given document whose name is the given name
     * @param doc The document to search its children
     * @param name The name of the element to return
     * @return The value at the given element
     */
    public static Boolean boolAtChild(final Element doc, final String name) {
        try{
            return doc.getElementsByTagName(name).item(0).getTextContent().equals("1");
        }catch (Exception e){
            return null;
        }

    }


    public static String getStringFromElement(final Element doc){
        Node node = doc.getParentNode();
        return getStringFromNode(node);
    }

    public static String getStringFromNode(final Node node){
        try {
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            StringWriter buffer = new StringWriter();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(new DOMSource(node),
                    new StreamResult(buffer));
            String str = buffer.toString();
            return str;
        }catch (Exception e){
            return null;
        }
    }

    public static void importNodeToPath(final Document document,final Node node ,String path ){
        final Element elem = document.getDocumentElement();
        Node tmp = document.importNode(node,true);
        Element tempelem = (Element) XmlUtils.nodeAtPath(elem,path);
        tempelem.appendChild(tmp);
    }

    public static void importStringToPath(final Document document,final String stringxml,String path ){
        Node node = XmlUtils.stringToXmlFile(stringxml );
        final Element elem = document.getDocumentElement();
        Node tmp = document.importNode(node,true);
        Element tempelem = (Element) XmlUtils.nodeAtPath(elem,path);
        tempelem.appendChild(tmp);
    }

    public static void createNodeToPath(final Document document,final String nodename,String path ){
        Node node = document.createElement(nodename);
        final Element elem = document.getDocumentElement();
        Node tmp = document.importNode(node,true);
        Element tempelem = (Element) XmlUtils.nodeAtPath(elem,path);
        tempelem.appendChild(tmp);
    }
}
