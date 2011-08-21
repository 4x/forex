import java.util.*;

public class dataFile {
	String dataString="";
	public dataFile(String input){
		dataString=input;
	}
	public String getValue(String block, String key, int index){
		
		String blockString=this.getDataStringWithTitle("\""+block+"\"", dataString);
		ArrayList<String> splited=this.splitGroup(blockString);
		String item=this.getDataStringWithTitle("\""+key+"\"", splited.get(index));
		return item;
	}
	public int getSize(String block){
		String blockString=this.getDataStringWithTitle("\""+block+"\"", dataString);
		ArrayList<String> splited=this.splitGroup(blockString);
		return splited.size();
	}
	private ArrayList<String> splitGroup(String data){
		ArrayList<String> r=new ArrayList<String>();
		int i=0;
		while(i<data.length()){
			if(data.charAt(i)=='{'){
				i++;
				int startIndex=i;
				i++;
//				while(data.charAt(i)!='}'){
//					i++;
//				}
				
				int level=1;
				while(level>0){
					char currentChar=data.charAt(i);
					if(currentChar=='{'){
						level++;
					}else if(currentChar=='}'){
						level--;
					}
					if(level>0){
					i++;
					}
				}
				
				
				int endIndex=i;
				r.add(data.substring(startIndex, endIndex));
			}
			i++;
		}
		return r;
	}
	private String getDataStringWithTitle(String title, String data){
		int titleIndex=data.indexOf(title);
		int startIndex=titleIndex+title.length()+1;
		
		if(data.charAt(startIndex)=='['){
			int level=1;
			int i=startIndex+1;
			while(level>0){
				char currentChar=data.charAt(i);
				if(currentChar=='['){
					level++;
				}else if(currentChar==']'){
					level--;
				}
				if(level>0){
				i++;
				}
			}
			int endIndex=i;
			return data.substring(startIndex+1, endIndex);
		}else{
			int i=startIndex+1;
			while(data.charAt(i)!=','){
				
				i++;
			}
			int endIndex=i;
			if(data.charAt(startIndex)=='"'){
				return data.substring(startIndex+1, endIndex-1);
			}else{
				return data.substring(startIndex, endIndex);
			}
		}
	}
}
