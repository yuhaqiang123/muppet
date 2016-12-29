package cn.bronzeware.test1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	
	public static void main(String[] args){
		
		int num = 0;
		Scanner in = new Scanner(System.in);
		num = in.nextInt();
		List<Integer> list = new ArrayList<>(num);
		for(int i = 0;i<num;i++){
			list.add(in.nextInt());
		}
		for(int i =num-1;i>=0;i--){
			list.add(list.get(i));
			
		}
		int result = 0;
		for(int i = 0;i<list.size();i++){
			for(int j = i;j<list.size();j++){
				
				boolean is = true;
				for(int k = i+1;k<=j;k++){
					if(list.get(k)>list.get(i)||list.get(j)<list.get(k)){
						is = false;
						
					}
					if(i+j==list.size()-1){
						is = false;
					}
				}
				
				if(is==true){
					result++;
				}
				
			}
		}
		
		
		
		System.out.println(result);
		
		
	}
}
