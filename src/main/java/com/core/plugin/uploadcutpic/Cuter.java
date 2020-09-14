package com.core.plugin.uploadcutpic;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;


public class Cuter {
	private Properties conf = null;
	private CuterParam cuterParam = null;
	private Map<String, Object> param  = null;

	public Cuter(Properties conf, CuterParam cuterParam, Map<String, Object> param) {
		this.conf = conf;
		this.cuterParam = cuterParam;
		this.param = param;
	}

	public final State doExec() {
		String url = cuterParam.getPurl();
		String uploadImagePath = (String)conf.get("rootPath") + url;
		State state = null;
		try {
			String suffix = FileType.getSuffix("JPG");
			String savePath = PathFormatter.parse((String) conf.get("cuterPath"),
					(String) conf.get("filename"), param);
			
			savePath = savePath + suffix;
			String physicalPath = (String) conf.get("rootPath") + savePath;
			state = valid(new File(physicalPath));
			if (!state.isSuccess()) {
				return state;
			}
			//剪切图片
			if(!CuterHelper.cutImage(uploadImagePath, physicalPath, cuterParam.getCutX(),
					cuterParam.getCutY(), cuterParam.getCutW(), cuterParam.getCutH())){
				state = new BaseState( false, AppInfo.CUTER_ERROR );
			}else{
				state = new BaseState(true, "头像剪切成功");
				state.putInfo("url", savePath);
				File uploadFile = new File(uploadImagePath);
				if(uploadFile.exists()){
					System.out.println(uploadFile.delete());//删除临时文件
				}
			};
		} catch (IOException e) {
			e.printStackTrace();
			return new BaseState( false, AppInfo.INVALID_ACTION );
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
}
