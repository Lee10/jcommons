package org.lee.coderepo.mail;

import org.apache.commons.lang3.StringUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.util.Properties;

/**
 * Created by lee on 2017/2/14.
 * mail 操作类
 */
public class MailUtils {

	/**
	 * 发送邮件
	 *
	 * @param subject     邮件主题
	 * @param text    邮件内容
	 * @param toUser 收件人
	 */
	public final static void send(String host, String username, String password, String subject, String text,
	                              String toUser, String ccUser) throws MailException {

		Session session = getSession(host);
		MimeMessage message = getMessage(session);
		Transport transport = null;

		try {

			// 发件人
			InternetAddress from = new InternetAddress(username);
			message.setFrom(from);

			// 收件人
			String[] arrReceiveUser = toUser.split(",");
			Address[] tos = new InternetAddress[arrReceiveUser.length];
			for (int i = 0; i < arrReceiveUser.length; i++) {
				tos[i] = new InternetAddress(arrReceiveUser[i]);
			}
			message.setRecipients(Message.RecipientType.TO, tos);

			//抄送
			if (StringUtils.isNotEmpty(ccUser)) {
				String[] arrCC = ccUser.split(",");
				Address[] ccs = new InternetAddress[arrCC.length];
				for (int i = 0; i < arrCC.length; i++) {
					ccs[i] = new InternetAddress(arrCC[i]);
				}
				message.setRecipients(Message.RecipientType.CC, ccs);
			}

			// 邮件主题
			message.setSubject(subject);

			String content = text.toString();
			// 邮件内容,也可以使纯文本"text/plain"
			message.setContent(content, "text/html;charset=UTF-8");

			// 保存邮件
			message.saveChanges();

			transport = session.getTransport("smtp");
			// smtp验证，就是你用来发邮件的邮箱用户名密码
			transport.connect(host, username, password);
			// 发送
			transport.sendMessage(message, message.getAllRecipients());
		} catch (Exception e) {
			e.printStackTrace();
			throw new MailException(e.getMessage());
		} finally {
			if (transport != null) {
				try {
					transport.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 发送邮件
	 *
	 * @param subject     邮件主题
	 * @param text    邮件内容
	 * @param toUser 收件人地址
	 * @param attachment  附件
	 */
	public final static void send(String host, String username, String password, String subject, String text,
	                 String toUser, String ccUser, File ...attachment) throws MailException {

		Session session = getSession(host);
		MimeMessage message = getMessage(session);
		Transport transport = null;

		try {
			// 发件人
			InternetAddress from = new InternetAddress(username);
			message.setFrom(from);

			// 收件人
			// 收件人
			if (StringUtils.isEmpty(toUser)) return;
			String[] arrReceiveUser = toUser.split(",");
			Address[] tos = new InternetAddress[arrReceiveUser.length];
			for (int i = 0; i < arrReceiveUser.length; i++) {
				tos[i] = new InternetAddress(arrReceiveUser[i]);
			}
			message.setRecipients(Message.RecipientType.TO, tos);

			//抄送
			if (StringUtils.isNotEmpty(ccUser)) {
				String[] arrCC = ccUser.split(",");
				Address[] ccs = new InternetAddress[arrCC.length];
				for (int i = 0; i < arrCC.length; i++) {
					ccs[i] = new InternetAddress(arrCC[i]);
				}
				message.setRecipients(Message.RecipientType.CC, ccs);
			}

			// 邮件主题
			message.setSubject(subject);

			// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
			Multipart multipart = new MimeMultipart();

			// 添加邮件正文
			BodyPart contentPart = new MimeBodyPart();
			contentPart.setContent(text, "text/html;charset=UTF-8");
			multipart.addBodyPart(contentPart);

			// 添加附件的内容
			if (attachment != null) {
				for (File file : attachment) {
					BodyPart attachmentBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(file);
					attachmentBodyPart.setDataHandler(new DataHandler(source));
					//MimeUtility.encodeWord可以避免文件名乱码
					attachmentBodyPart.setFileName(MimeUtility.encodeWord(file.getName()));
					multipart.addBodyPart(attachmentBodyPart);
				}
			}

			// 将multipart对象放到message中
			message.setContent(multipart);
			// 保存邮件
			message.saveChanges();

			transport = session.getTransport("smtp");
			// smtp验证，就是你用来发邮件的邮箱用户名密码
			transport.connect(host, username, password);
			// 发送
			transport.sendMessage(message, message.getAllRecipients());
		} catch (Exception e) {
			e.printStackTrace();
			throw new MailException(e.getMessage());
		} finally {
			if (transport != null) {
				try {
					transport.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private final static Session getSession(String host) {

		if (StringUtils.isNotEmpty(host)) {
			Properties props = new Properties();
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.auth", "true");

			return Session.getInstance(props);
		} else return null;
	}

	private final static MimeMessage getMessage(Session session) {
		return session != null ? new MimeMessage(session) : null;
	}

}
