package com.core.plugin.uploadcutpic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PathFormatter {
	
	private static final String TIME = "time";
	private static final String FULL_YEAR = "yyyy";
	private static final String YEAR = "yy";
	private static final String MONTH = "mm";
	private static final String DAY = "dd";
	private static final String HOUR = "hh";
	private static final String MINUTE = "ii";
	private static final String SECOND = "ss";
	private static final String RAND = "rand";
	private static final String PARAM = "param";
	
	private static Date currentDate = null;
	
	public static String parse ( String input, Map<String, Object> param) {
		
		Pattern pattern = Pattern.compile( "\\{([^\\}]+)\\}", Pattern.CASE_INSENSITIVE  );
		Matcher matcher = pattern.matcher(input);
		
		PathFormatter.currentDate = new Date();
		
		StringBuffer sb = new StringBuffer();
		
		while ( matcher.find() ) {
			
			matcher.appendReplacement(sb, PathFormatter.getString( matcher.group( 1 ), param ) );
			
		}
		
		matcher.appendTail(sb);
		
		return sb.toString();
	}
	
	/**
	 * 格式化路径, 把windows路径替换成标准路径
	 * @param input 待格式化的路径
	 * @return 格式化后的路径
	 */
	public static String format ( String input ) {
		
		return input.replace( "\\", "/" );
		
	}

	public static String parse ( String input, String filename, Map<String, Object> param ) {
		Pattern pattern = Pattern.compile( "\\{([^\\}]+)\\}", Pattern.CASE_INSENSITIVE  );
		Matcher matcher = pattern.matcher(input);
		String matchStr = null;
		
		PathFormatter.currentDate = new Date();
		
		StringBuffer sb = new StringBuffer();
		
		while ( matcher.find() ) {
			
			matchStr = matcher.group( 1 );
			if ( matchStr.indexOf( "filename" ) != -1 ) {
				filename = filename.replace( "$", "\\$" ).replaceAll( "[\\/:*?\"<>|]", "" );
				matcher.appendReplacement(sb, filename );
			} else {
				matcher.appendReplacement(sb, PathFormatter.getString( matchStr, param ) );
			}
			
		}
		
		matcher.appendTail(sb);
		
		return sb.toString();
	}
		
	private static String getString ( String pattern, Map<String, Object> param ) {
		
		// time 处理
		if ( pattern.indexOf( PathFormatter.TIME ) != -1 ) {
			pattern = pattern.toLowerCase();
			return PathFormatter.getTimestamp();
		} else if ( pattern.indexOf( PathFormatter.FULL_YEAR ) != -1 ) {
			pattern = pattern.toLowerCase();
			return PathFormatter.getFullYear();
		} else if ( pattern.indexOf( PathFormatter.YEAR ) != -1 ) {
			pattern = pattern.toLowerCase();
			return PathFormatter.getYear();
		} else if ( pattern.indexOf( PathFormatter.MONTH ) != -1 ) {
			pattern = pattern.toLowerCase();
			return PathFormatter.getMonth();
		} else if ( pattern.indexOf( PathFormatter.DAY ) != -1 ) {
			pattern = pattern.toLowerCase();
			return PathFormatter.getDay();
		} else if ( pattern.indexOf( PathFormatter.HOUR ) != -1 ) {
			pattern = pattern.toLowerCase();
			return PathFormatter.getHour();
		} else if ( pattern.indexOf( PathFormatter.MINUTE ) != -1 ) {
			pattern = pattern.toLowerCase();
			return PathFormatter.getMinute();
		} else if ( pattern.indexOf( PathFormatter.SECOND ) != -1 ) {
			pattern = pattern.toLowerCase();
			return PathFormatter.getSecond();
		} else if ( pattern.indexOf( PathFormatter.RAND ) != -1 ) {
			pattern = pattern.toLowerCase();
			return PathFormatter.getRandom( pattern );
		} else if ( pattern.indexOf( PathFormatter.PARAM ) != -1 ) {
			return PathFormatter.getParam( pattern, param);
		}
		
		return pattern;
		
	}

	private static String getTimestamp () {
		return System.currentTimeMillis() + "";
	}
	
	private static String getFullYear () {
		return new SimpleDateFormat( "yyyy" ).format( PathFormatter.currentDate );
	}
	
	private static String getYear () {
		return new SimpleDateFormat( "yy" ).format( PathFormatter.currentDate );
	}
	
	private static String getMonth () {
		return new SimpleDateFormat( "MM" ).format( PathFormatter.currentDate );
	}
	
	private static String getDay () {
		return new SimpleDateFormat( "dd" ).format( PathFormatter.currentDate );
	}
	
	private static String getHour () {
		return new SimpleDateFormat( "HH" ).format( PathFormatter.currentDate );
	}
	
	private static String getMinute () {
		return new SimpleDateFormat( "mm" ).format( PathFormatter.currentDate );
	}
	
	private static String getSecond () {
		return new SimpleDateFormat( "ss" ).format( PathFormatter.currentDate );
	}
	
	private static String getRandom ( String pattern ) {
		
		int length = 0;
		pattern = pattern.split( ":" )[ 1 ].trim();
		
		length = Integer.parseInt( pattern );
		
		return ( Math.random() + "" ).replace( ".", "" ).substring( 0, length );
		
	}
	
	private static String getParam ( String pattern,Map<String, Object> param ) {
		
		pattern = pattern.split( ":" )[ 1 ].trim();
		
		if(param.containsKey(pattern)){
			return param.get(pattern).toString();
		}else{
			return "none";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

