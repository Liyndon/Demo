package com.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class TextFileWriteAndFilter extends FileFilter {
	private ArrayList<String> extensions = new ArrayList<String>();
	private ArrayList<String> descriptions = new ArrayList<String>();

	public TextFileWriteAndFilter() {
		super();
	}

	public TextFileWriteAndFilter(String extension, String description) {
		super();
		this.extensions.add(extension);
		this.descriptions.add(description);
	}

	@Override
	public boolean accept(File pathname) {
		if (pathname != null) {
			if (pathname.isDirectory()) {
				return true;
			}
			String extension = getExtension(pathname);
			for (int i = 0; i < extensions.size(); i++) {
				if (extensions.get(i).toLowerCase()
						.endsWith(extension.toLowerCase())) {
					return true;
				}
			}
		}
		return false;
	}

	private String getExtension(File pathname) {
		if (pathname != null) {
			String filename = pathname.getName();
			int i = filename.lastIndexOf('.');
			if (i > 0 && i < filename.length() - 1) {
				return filename.substring(i).toLowerCase();
			}
		}
		return null;
	}

	@Override
	public String getDescription() {
		return descriptions.get(descriptions.size() - 1);
	}

	/**
	 * @Title: write
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @author lxy
	 * @throws
	 */
	public static boolean write(List<Object> list) {
		JFileChooser dialog = new JFileChooser();
		dialog.setDialogTitle("另存为");
		dialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
		dialog.setDialogType(JFileChooser.SAVE_DIALOG);
		dialog.setFileFilter(new TextFileWriteAndFilter("*.txt", "文本文档(*.txt)"));
		int result = dialog.showSaveDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = dialog.getSelectedFile();
			FileOutputStream out = null;
			PrintWriter writer = null;
			try {
				out = new FileOutputStream(file + ".txt", true);
				writer = new PrintWriter(out);
				String temp = "";
				StringBuffer sBuffer = new StringBuffer();
				writer.write(temp.toString());
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				try {
					writer.close();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return true;
		}
		return false;
	}

}
