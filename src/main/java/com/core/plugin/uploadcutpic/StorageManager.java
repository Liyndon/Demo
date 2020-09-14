package com.core.plugin.uploadcutpic;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

public class StorageManager {
	public static final int BUFFER_SIZE = 8192;

	public StorageManager() {
	}

	public static State saveBinaryFile(byte[] data, String path) {
		File file = new File(path);

		State state = valid(file);

		if (!state.isSuccess()) {
			return state;
		}

		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bos.write(data);
			bos.flush();
			bos.close();
		} catch (IOException ioe) {
			return new BaseState(false, AppInfo.IO_ERROR);
		}

		state = new BaseState(true, file.getAbsolutePath());
		state.putInfo("size", data.length);
		state.putInfo("title", file.getName());
		return state;
	}

	public static State saveFileByInputStream(InputStream is, String path,
			long maxSize) {
		State state = null;
		File tmpFile = getTmpFile();

		byte[] dataBuf = new byte[2048];
		BufferedInputStream bis = new BufferedInputStream(is,
				StorageManager.BUFFER_SIZE);

		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(tmpFile), StorageManager.BUFFER_SIZE);

			int count = 0;
			while ((count = bis.read(dataBuf)) != -1) {
				bos.write(dataBuf, 0, count);
			}
			bos.flush();
			bos.close();

			if (tmpFile.length() > maxSize) {
				tmpFile.delete();
				return new BaseState(false, AppInfo.MAX_SIZE);
			}

			state = saveTmpFile(tmpFile, path);

			if (state.isSuccess()) {
				tmpFile.delete();
			}
			return state;

		} catch (IOException e) {
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	public static State saveFileByInputStream(InputStream is, String path) {
		State state = null;

		File tmpFile = getTmpFile();

		byte[] dataBuf = new byte[2048];
		BufferedInputStream bis = new BufferedInputStream(is,
				StorageManager.BUFFER_SIZE);

		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(tmpFile), StorageManager.BUFFER_SIZE);

			int count = 0;
			while ((count = bis.read(dataBuf)) != -1) {
				bos.write(dataBuf, 0, count);
			}
			bos.flush();
			bos.close();

			state = saveTmpFile(tmpFile, path);

			if (!state.isSuccess()) {
				tmpFile.delete();
			}

			return state;
		} catch (IOException e) {
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static File getTmpFile() {
		File tmpDir = FileUtils.getTempDirectory();
		String tmpFileName = (Math.random() * 10000 + "").replace(".", "");
		return new File(tmpDir, tmpFileName);
	}

	private static State saveTmpFile(File tmpFile, String path) {
		State state = null;
		File targetFile = new File(path);
		try { 
			if(targetFile.exists()){ 
				if(targetFile.delete()){
					targetFile.createNewFile(); 
				}; 
			}else{
				File parentFile = targetFile.getParentFile();
				if(!parentFile.exists()){
					parentFile.mkdirs();
					targetFile.createNewFile();
				}else{
					targetFile.createNewFile();
				}
			} 
			
		} catch (IOException e) {
			e.printStackTrace(); 
		}
		if (!targetFile.canWrite()) { return new BaseState(false,AppInfo.PERMISSION_DENIED); }
		 
		state = fileChannelCopy(tmpFile, targetFile);
		if (state.isSuccess()) {
			try {
				state = imageFormatterCoverter(targetFile,
						FileType.getSuffix("JPG"));
				if (state.isSuccess()) {
					state.putInfo("title", targetFile.getName());
					state.putInfo("size", targetFile.length());
				}
				;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return state;
	}

	private static State valid(File file) {
		File parentPath = file.getParentFile();

		if ((!parentPath.exists()) && (!parentPath.mkdirs())) {
			return new BaseState(false, AppInfo.FAILED_CREATE_FILE);
		}

		if (!parentPath.canWrite()) {
			return new BaseState(false, AppInfo.PERMISSION_DENIED);
		}

		return new BaseState(true);
	}

	@SuppressWarnings("finally")
	private static State fileChannelCopy(File s, File t) {
		State state = null;
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				state = new BaseState(true);
				fi.close();
				in.close();
				fo.close();
				out.close();
				return state;
			} catch (IOException e) {
				e.printStackTrace();
				return new BaseState(false, AppInfo.IO_ERROR);
			}
		}
	}

	public static State imageFormatterCoverter(File f, String format)
			throws FileNotFoundException, IOException {
		State state = null;
		BufferedImage bufferedImage = ImageIO.read(f);
		String fPath = f.getAbsolutePath();
		String formatFilePath = fPath.substring(0, fPath.lastIndexOf("."))
				+ format;
		if (fPath.toLowerCase().equals(formatFilePath.toLowerCase())) {// 相同则不需转化格式
			state = new BaseState(true);
			state.putInfo("width", bufferedImage.getWidth());
			state.putInfo("height", bufferedImage.getHeight());
			return state;
		}
		File formatFile = new File(formatFilePath);
		if (!formatFile.exists()) {
			formatFile.createNewFile();
		}
		BufferedImage newBufferedImage = new BufferedImage(
				bufferedImage.getWidth(), bufferedImage.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0,
				Color.WHITE, null);
		if (ImageIO.write(newBufferedImage, format.substring(1, format.length()), formatFile)) {
			newBufferedImage.flush();
			System.out.println(f.delete());
			state = new BaseState(true);
			state.putInfo("width", newBufferedImage.getWidth());
			state.putInfo("height", newBufferedImage.getHeight());
		} else {
			state = new BaseState(false, AppInfo.IO_ERROR);
		}
		return state;
	}
}
