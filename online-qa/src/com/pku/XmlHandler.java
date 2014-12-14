package com.pku;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ember on 12/8/14.
 */
public class XmlHandler {
    public static List<org.apache.lucene.document.Document> parserXml(String fileName) {
        List<org.apache.lucene.document.Document> docList= new ArrayList<org.apache.lucene.document.Document>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fileName);
            NodeList pages = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < pages.getLength(); i++) {
                Node page = pages.item(i);
                if (!page.getNodeName().equals("page"))
                    continue;
                org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
                NodeList pageInfo = page.getChildNodes();
                for (int j = 0; j < pageInfo.getLength(); j++) {
                    Node node = pageInfo.item(j);
                    if (node.getNodeName().equals("title"))
                    {
                        doc.add(new Field("title", node.getTextContent(), TextField.TYPE_STORED));
                    }
                    if (node.getNodeName().equals("revision")) {
                        NodeList revisionInfo = node.getChildNodes();
                        for (int k = 0; k < revisionInfo.getLength(); k++) {
                            Node revisionNode = revisionInfo.item(k);
                            if(revisionNode.getNodeName().equals("text"))
                                doc.add(new Field("text", revisionNode.getTextContent(), TextField.TYPE_STORED));
                        }
                    }
                }
                docList.add(doc);
            }
            return docList;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
        } catch (SAXException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return docList;
    }
    public static List<org.apache.lucene.document.Document> parserSpecial(String fileName) {
        List<org.apache.lucene.document.Document> docList= new ArrayList<org.apache.lucene.document.Document>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fileName);
            NodeList pages = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < pages.getLength(); i++) {
                Node page = pages.item(i);
                if (!page.getNodeName().equals("page"))
                    continue;
                NodeList pageInfo = page.getChildNodes();
                for (int j = 0; j < pageInfo.getLength(); j++) {
                    Node node = pageInfo.item(j);
                    if (node.getNodeName().equals("revision")) {
                        NodeList revisionInfo = node.getChildNodes();
                        for (int k = 0; k < revisionInfo.getLength(); k++) {
                            Node revisionNode = revisionInfo.item(k);
                            org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
                            if(revisionNode.getNodeName().equals("q"))
                                doc.add(new Field("text", revisionNode.getTextContent(), TextField.TYPE_STORED));
                                docList.add(doc);
                        }
                    }
                }
            }
            return docList;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
        } catch (SAXException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return docList;
    }
    public static List<String> parseQuestion(String fileName)
    {
        List<String> questionList = new ArrayList<>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fileName);
            NodeList questions = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < questions.getLength(); i++) {
                Node question = questions.item(i);
                if (!question.getNodeName().equals("question"))
                    continue;
                NodeList paragraph = question.getChildNodes();
                for (int j = 0; j < paragraph.getLength(); j++) {
                    Node node = paragraph.item(j);
                    if (node.getNodeName().equals("q"))
                    {
                        questionList.add(node.getTextContent());
                    }
                }
            }
            return questionList;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
        } catch (SAXException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return questionList;
    }
}
