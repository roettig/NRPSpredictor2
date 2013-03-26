package org.roettig.NRPSpredictor2.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Helper
{
	public static void deployFile(InputStream in, String filename) throws IOException
	{
		FileOutputStream fout = new FileOutputStream(filename);
		byte[] buffer = new byte[2048];
		int size;
		
		while ((size = in.read(buffer, 0, 2048)) != -1)
		{
			fout.write(buffer, 0, size);
		}
		fout.flush();
		fout.close();
		in.close();
	}
	
	public static File deployFile(InputStream in) throws IOException
	{
		File tmp = File.createTempFile("TEMP", "TMP");
		deployFile(in,tmp.getAbsolutePath());
		return tmp;
	}
}
