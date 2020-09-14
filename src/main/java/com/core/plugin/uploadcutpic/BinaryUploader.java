package com.core.plugin.uploadcutpic;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


public class BinaryUploader {

	public static final State save(HttpServletRequest request,
			Properties conf, Map<String, Object> param) {
		FileItemStream fileStream = null;
		boolean isAjaxUpload = request.getHeader( "X_Requested_With" ) != null;

		if (!ServletFileUpload.isMultipartContent(request)) {
			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
		}

		ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());

        if ( isAjaxUpload ) {
            upload.setHeaderEncoding( "UTF-8" );
        }

		try {
			FileItemIterator iterator = upload.getItemIterator(request);

			while (iterator.hasNext()) {
				fileStream = iterator.next();
				if (!fileStream.isFormField()){
					break;
				}
				fileStream = null;
			}

			if (fileStream == null) {
				return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
			}

			String savePath = (String) conf.get("uploadPath");
			String originFileName = fileStream.getName();
			String suffix = FileType.getSuffixByFilename(originFileName);
			//为了保证相同名字的图片只有一张采用统一的图片格式jpg格式
//			String suffix = FileType.getSuffix("JPG");

			originFileName = originFileName.substring(0,
					originFileName.length() - suffix.length());
			String readyPath = savePath+FileType.getSuffix("JPG");
			savePath = savePath + suffix;
			
			long maxSize = Long.parseLong((String)conf.get("maxSize"));

			if (!validType(suffix, ((String)conf.get("allowFiles")).split("\\|"))) {
				return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
			}

			savePath = PathFormatter.parse(savePath, originFileName, param);

			String physicalPath = (String) conf.get("rootPath") + savePath;

			InputStream is = fileStream.openStream();
			//保存
			State storageState = StorageManager.saveFileByInputStream(is,
					physicalPath, maxSize);
			storageState.putInfo("title", originFileName + suffix);
			is.close();

			if (storageState.isSuccess()) {
				
				storageState.putInfo("url", PathFormatter.parse(readyPath, originFileName, param));
				storageState.putInfo("type", suffix);
			}

			return storageState;
		} catch (FileUploadException e) {
			return new BaseState(false, AppInfo.PARSE_REQUEST_ERROR);
		} catch (IOException e) {
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static boolean validType(String type, String[] allowTypes) {
		type = type.replace(".", "");
		List<String> list = Arrays.asList(allowTypes);
		return list.contains(type);
	}
}
