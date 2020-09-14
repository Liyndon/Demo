package com.core.plugin.uploadcutpic;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * @ClassName: CuterHelper 
 * @Description: 图片剪切工具类
 * @author zq_ja.Zhang
 * @date 2015-11-23 下午7:31:07 
 */
public class CuterHelper {  
	/**
	 * @Title: cutCenterImage 
	 * @Description:根据尺寸图片居中裁剪 
	 * @param @param src
	 * @param @param dest
	 * @param @param w
	 * @param @param h
	 * @param @throws IOException    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
     public static boolean cutCenterImage(String src,String dest,int w,int h) throws IOException{   
         Iterator<?> iterator = ImageIO.getImageReadersByFormatName(FileType.getSuffix("JPG"));   
         ImageReader reader = (ImageReader)iterator.next();   
         InputStream in=new FileInputStream(src);  
         ImageInputStream iis = ImageIO.createImageInputStream(in);   
         reader.setInput(iis, true);   
         ImageReadParam param = reader.getDefaultReadParam();   
         int imageIndex = 0;   
         Rectangle rect = new Rectangle((reader.getWidth(imageIndex)-w)/2, (reader.getHeight(imageIndex)-h)/2, w, h);    
         param.setSourceRegion(rect);   
         BufferedImage bi = reader.read(0,param);     
         return ImageIO.write(bi, FileType.getSuffix("JPG"), new File(dest));             
    
     }  
    /**
     * @Title: cutHalfImage 
     * @Description: 图片裁剪二分之一 
     * @param @param src
     * @param @param dest
     * @param @throws IOException    设定文件 
     * @return void    返回类型 
     * @throws
     */
     public static void cutHalfImage(String src,String dest) throws IOException{   
         Iterator<?> iterator = ImageIO.getImageReadersByFormatName(FileType.getSuffix("JPG"));   
         ImageReader reader = (ImageReader)iterator.next();   
         InputStream in=new FileInputStream(src);  
         ImageInputStream iis = ImageIO.createImageInputStream(in);   
         reader.setInput(iis, true);   
         ImageReadParam param = reader.getDefaultReadParam();   
         int imageIndex = 0;   
         int width = reader.getWidth(imageIndex)/2;   
         int height = reader.getHeight(imageIndex)/2;   
         Rectangle rect = new Rectangle(width/2, height/2, width, height);   
         param.setSourceRegion(rect);   
         BufferedImage bi = reader.read(0,param);     
         ImageIO.write(bi, FileType.getSuffix("JPG"), new File(dest));     
     }  
    /**
     * @Title: cutImage 
     * @Description: 图片裁剪通用接口 
     * @param @param src
     * @param @param dest
     * @param @param x
     * @param @param y
     * @param @param w
     * @param @param h
     * @param @throws IOException    设定文件 
     * @return void    返回类型 
     * @throws
     */
  
    public static boolean cutImage(String src,String dest,int x,int y,int w,int h) throws IOException{
           FileInputStream is = null;  
           ImageInputStream iis = null;  
           // 如果源图片不存在  
           File srcFile = new File(src);
           if (!srcFile.exists()) {  
               return false;  
           }  
           // 读取图片文件  
           is = new FileInputStream(src);  
           // 获取文件格式  
           String ext = src.substring(src.lastIndexOf(".") + 1);  
           // ImageReader声称能够解码指定格式  
           Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(ext);  
           ImageReader reader = it.next();  
           // 获取图片流  
           iis = ImageIO.createImageInputStream(is);  
           // 输入源中的图像将只按顺序读取  
           reader.setInput(iis, true);  
           // 描述如何对流进行解码  
           ImageReadParam param = reader.getDefaultReadParam();  
           // 图片裁剪区域  
           Rectangle rect = new Rectangle(x, y, w, h);  
           // 提供一个 BufferedImage，将其用作解码像素数据的目标  
           param.setSourceRegion(rect);  
           // 使用所提供的 ImageReadParam 读取通过索引 imageIndex 指定的对象  
           BufferedImage bi = reader.read(0, param);  
           // 保存新图片  
           File tempOutFile = new File(dest);  
           if (!tempOutFile.exists()) {  
               tempOutFile.mkdirs();  
           }  
           ImageIO.write(bi, ext, new File(dest));  
           is.close();
           iis.close();
           return true;
  
    }   
    /**
     * @Title: zoomImage 
     * @Description: 图片缩放 
     * @param @param src
     * @param @param dest
     * @param @param w
     * @param @param h
     * @param @throws Exception    设定文件 
     * @return void    返回类型 
     * @throws
     */
    public static boolean zoomImage(String src,String dest,int w,int h) throws Exception {  
        double wr=0,hr=0;  
        File srcFile = new File(src);  
        File destFile = new File(dest);  
        BufferedImage bufImg = ImageIO.read(srcFile);  
        Image Itemp = bufImg.getScaledInstance(w, h, Image.SCALE_SMOOTH);  
        wr=w*1.0/bufImg.getWidth();  
        hr=h*1.0 / bufImg.getHeight();  
        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);  
        Itemp = ato.filter(bufImg, null);  
        try {  
            return ImageIO.write((BufferedImage) Itemp,dest.substring(dest.lastIndexOf(".")+1), destFile);  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }
		return false;  
    }
}  
