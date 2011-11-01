package org.guardian.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

public class Utils
{
	/**
	 * Downloads a file from a url and gives progress messages.
	 */
	public static void download(Logger log, URL url, File file) throws IOException {
		if (!file.getParentFile().exists())
			file.getParentFile().mkdir();
		if (file.exists())
			file.delete();
		file.createNewFile();
		final int size = url.openConnection().getContentLength();
		log.info("Downloading " + file.getName() + " (" + size / 1024 + "kb) ...");
		final InputStream in = url.openStream();
		final OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
		final byte[] buffer = new byte[1024];
		int len, downloaded = 0, msgs = 0;
		final long start = System.currentTimeMillis();
		while ((len = in.read(buffer)) >= 0) {
			out.write(buffer, 0, len);
			downloaded += len;
			if ((int)((System.currentTimeMillis() - start) / 500) > msgs) {
				log.info((int)(downloaded / (double)size * 100d) + "%");
				msgs++;
			}
		}
		in.close();
		out.close();
		log.info("Download finished");
	}

	/**
	 * Checks if inputted string is an integer
	 */
	public static boolean isInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (final NumberFormatException ex) {}
		return false;
	}

	/**
	 * Checks if inputted string is a byte
	 */
	public static boolean isByte(String str) {
		try {
			Byte.parseByte(str);
			return true;
		} catch (final NumberFormatException ex) {}
		return false;
	}

	/**
	 * Java version of PHP's join(array, delimiter). Takes any kind of collection (List, HashMap etc)
	 */
	public static String join(Collection<?> s, String delimiter) {
		final StringBuffer buffer = new StringBuffer();
		final Iterator<?> iter = s.iterator();
		while (iter.hasNext()) {
			buffer.append(iter.next());
			if (iter.hasNext())
				buffer.append(delimiter);
		}
		return buffer.toString();
	}

	/**
	 * Joins a string array
	 */
	public static String join(String[] s, String delimiter) {
		if (s == null || s.length == 0)
			return "";
		final int len = s.length;
		final StringBuffer buffer = new StringBuffer(s[0]);
		for (int i = 1; i < len; i++)
			buffer.append(delimiter).append(s[i]);
		return buffer.toString();
	}

	/**
	 * Concatenate any number of arrays of the same type
	 */
	public static <T> T[] concat(T[] first, T[]... rest) {
		// Read rest
		int totalLength = first.length;
		for (final T[] array : rest)
			totalLength += array.length;
		// Concat with arraycopy
		final T[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (final T[] array : rest) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}

	/**
	 * Returns a string of spaces
	 */
	public static String spaces(int count) {
		final StringBuilder filled = new StringBuilder(count);
		for (int i = 0; i < count; i++)
			filled.append(' ');
		return filled.toString();
	}

	/**
	 * Returns the content of a web page as string.
	 */
	public static String readURL(URL url) throws IOException {
		final StringBuilder content = new StringBuilder();
		final BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null)
			content.append(inputLine);
		in.close();
		return content.toString();
	}

	/**
	 * Accepts following time formats: 1 day, 2 hours, 3 minutes, 1d2h3m, HH:mm:ss, dd.MM.yyyy and dd.MM.yyyy HH:mm:ss
	 * 
	 * @return Number of minutes, or -1 on error
	 */
	public static int parseTimeSpec(String arg) {
		final String[] spec = arg.split(" ");
		if (spec == null || spec.length < 1 || spec.length > 2)
			return -1;
		if (spec.length == 1 && isInt(spec[0]))
			return Integer.valueOf(spec[0]);
		if (!spec[0].contains(":") && !spec[0].contains("."))
			if (spec.length == 2) {
				if (!isInt(spec[0]))
					return -1;
				int min = Integer.parseInt(spec[0]);
				if (spec[1].startsWith("h"))
					min *= 60;
				else if (spec[1].startsWith("d"))
					min *= 1440;
				return min;
			} else if (spec.length == 1) {
				int days = 0, hours = 0, minutes = 0;
				int lastIndex = 0, currIndex = 1;
				while (currIndex <= spec[0].length()) {
					while (currIndex <= spec[0].length() && isInt(spec[0].substring(lastIndex, currIndex)))
						currIndex++;
					if (currIndex - 1 != lastIndex) {
						final String param = spec[0].substring(currIndex - 1, currIndex).toLowerCase();
						if (param.equals("d"))
							days = Integer.parseInt(spec[0].substring(lastIndex, currIndex - 1));
						else if (param.equals("h"))
							hours = Integer.parseInt(spec[0].substring(lastIndex, currIndex - 1));
						else if (param.equals("m"))
							minutes = Integer.parseInt(spec[0].substring(lastIndex, currIndex - 1));
					}
					lastIndex = currIndex;
					currIndex++;
				}
				if (days == 0 && hours == 0 && minutes == 0)
					return -1;
				return minutes + hours * 60 + days * 1440;
			} else
				return -1;
		// TODO This part could need some optimization, despite it work
		final String timestamp;
		if (spec.length == 1) {
			if (spec[0].contains(":"))
				timestamp = new SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis()) + " " + spec[0];
			else
				timestamp = spec[0] + " 00:00:00";
		} else
			timestamp = spec[0] + " " + spec[1];
		try {
			return (int)((System.currentTimeMillis() - new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(timestamp).getTime()) / 60000);
		} catch (final ParseException ex) {
			return -1;
		}
	}
}
