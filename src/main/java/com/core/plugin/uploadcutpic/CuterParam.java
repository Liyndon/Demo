package com.core.plugin.uploadcutpic;

/**
 * 
 * @ClassName: CuterParam 
 * @Description: 剪切参数 
 * @author zq_ja.Zhang
 * @date 2015-11-23 下午7:00:30 
 *
 */
public class CuterParam {
	private String purl; //获得路径
	private float scale; //尺寸比例       原图:剪切图
	private int x;       //起点的X坐标
	private int y;       //起点的Y坐标
	private int w;       //剪切图宽度
	private int h;       //剪切图高度
	public String getPurl() {
		return purl;
	}
	public void setPurl(String purl) {
		this.purl = purl;
	}
	public void setScale(float scale) {
		this.scale = scale;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setW(int w) {
		this.w = w;
	}
	public void setH(int h) {
		this.h = h;
	}
	@Override
	public String toString() {
		return "UploadParam [purl=" + purl + ", scale=" + scale + ", x=" + x
				+ ", y=" + y + ", w=" + w + ", h=" + h + "]";
	}
	/**
	 * @Title: getCutX 
	 * @Description: 获得实际剪切x坐标
	 * @param @return    设定文件 
	 * @return int    返回类型 
	 * @throws
	 */
	public int getCutX(){
		return (int) Math.floor(this.scale*this.x);
	}
	/**
	 * @Title: getCutY 
	 * @Description: 获得实际剪切y坐标
	 * @param @return    设定文件 
	 * @return int    返回类型 
	 * @throws
	 */
	public int getCutY(){
		return (int) Math.floor(this.scale*this.y);
	}
	/**
	 * @Title: getCutW 
	 * @Description: 获得实际剪切剪切宽度
	 * @param @return    设定文件 
	 * @return int    返回类型 
	 * @throws
	 */
	public int getCutW(){
		return (int) Math.floor(this.scale*this.w);
	}
	/**
	 * @Title: getCutH 
	 * @Description: 获得实际剪切高度 
	 * @param @return    设定文件 
	 * @return int    返回类型 
	 * @throws
	 */
	public int getCutH(){
		return (int) Math.floor(this.scale*this.h);
	}
}
