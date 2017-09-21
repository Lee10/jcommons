package org.lee.coderepo.xml;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by lee on 2017/5/23.
 */
public class XmlUtils {

	public static final String DEFAULT_CHARSET = "UTF-8";

	public static String toXml(Object object) throws JAXBException, IOException {
		JAXBContext context = JAXBContext.newInstance(object.getClass());
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, DEFAULT_CHARSET);  //编码格式
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); //是否格式化xml
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false); //是否生成xml头信息

		// 将XML文档导出成字符串
		OutputFormat format = OutputFormat.createCompactFormat();
		format.setEncoding(DEFAULT_CHARSET);
		StringWriter out = new StringWriter();
		XMLWriter writer = null;
		try {
			writer = new XMLWriter(out, format);
			marshaller.marshal(object, writer);
		} finally {
			if (writer != null) writer.close();
		}
		return out.toString();
	}


	public static <T> T parseXml(String xml, Class<T> clazz) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(clazz);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (T) unmarshaller.unmarshal(new StringReader(xml));
	}
}
