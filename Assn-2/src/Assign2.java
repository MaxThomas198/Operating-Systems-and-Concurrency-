import java.util.Scanner;
import java.lang.Runtime;

public class Assign2{
	
	public static void main(String[] args){
		if(args.length == 0){
			System.out.println("No args");
		}
		else{
			for(int i = 0; i < args.length; i++){
				if(args[i].equals("-cpu")){
					cpu();
					System.out.println();
				}
				else if(args[i].equals("-mem")){	
					mem();
					System.out.println();
				}
				else if(args[i].equals("-dirs")){
					dirs();
					System.out.println();
				}
				else if(args[i].equals("-os")){
					os();
					System.out.println();
				}
				else if(args[i].equals("-java")){
					java();
					System.out.println();
				}
				else{
					System.out.print("Command Not Found");
					System.out.println();
				}	
			}
		}
	}
	
	public static void cpu(){
		 System.out.println("Processors : "+Runtime.getRuntime().availableProcessors());
	}
	
	public static void mem(){
		 System.out.println("Free Memory : " + Runtime.getRuntime().freeMemory());
		 System.out.println("Total Memory : " + Runtime.getRuntime().totalMemory());
		 System.out.println("Max Memory : " + Runtime.getRuntime().maxMemory());
	}
	
	public static void dirs(){
		System.out.println("Working Directory : " + System.getProperty("user.dir"));
		System.out.println("User Home Directory : " + System.getProperty("user.home"));
	}
	
	public static void os(){
		System.out.println("OS Name : " + System.getProperty("os.name"));
		System.out.println("OS Version : " + System.getProperty("os.version"));
	}
	
	public static void java(){
		System.out.println("Java Vendor : " + System.getProperty("java.vendor"));
		System.out.println("Java Runtime : " + System.getProperty("java.runtime.name")); 
		System.out.println("Java Version : " + System.getProperty("java.version"));
		System.out.println("Java VM Version : " + System.getProperty("java.vm.version"));
		System.out.println("Java VM Name : " + System.getProperty("java.vm.name"));
	}
}
