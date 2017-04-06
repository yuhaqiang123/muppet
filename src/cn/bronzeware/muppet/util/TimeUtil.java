package cn.bronzeware.muppet.util;

public class TimeUtil {

	public static String interval(long start, long end){
		if(start > end){
			throw new IllegalArgumentException("end时间戳应大于start");
		}
		int minuteCount = (int) (end-start)/(1000*60);
		
		int hourCount = (minuteCount)/60;
		minuteCount %= 60;
		
		int dayCount = hourCount/24;
		hourCount %= 24;
		
		StringBuffer time = new StringBuffer();
		if(dayCount>0){
			time.append(String.format("%d 天,　", dayCount));
		}
		if(hourCount>0){
			time.append(String.format("%d 小时，", hourCount));
		}
		if(minuteCount > -1){
			time.append(String.format("%d 分钟", minuteCount));
		}
		return time.toString();
	}
	
}
